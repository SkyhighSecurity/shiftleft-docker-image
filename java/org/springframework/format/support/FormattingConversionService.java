/*     */ package org.springframework.format.support;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.core.convert.support.GenericConversionService;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.format.Parser;
/*     */ import org.springframework.format.Printer;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormattingConversionService
/*     */   extends GenericConversionService
/*     */   implements FormatterRegistry, EmbeddedValueResolverAware
/*     */ {
/*     */   private StringValueResolver embeddedValueResolver;
/*  55 */   private final Map<AnnotationConverterKey, GenericConverter> cachedPrinters = new ConcurrentHashMap<AnnotationConverterKey, GenericConverter>(64);
/*     */ 
/*     */   
/*  58 */   private final Map<AnnotationConverterKey, GenericConverter> cachedParsers = new ConcurrentHashMap<AnnotationConverterKey, GenericConverter>(64);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver resolver) {
/*  64 */     this.embeddedValueResolver = resolver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFormatter(Formatter<?> formatter) {
/*  70 */     addFormatterForFieldType(getFieldType(formatter), formatter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter) {
/*  75 */     addConverter(new PrinterConverter(fieldType, (Printer<?>)formatter, (ConversionService)this));
/*  76 */     addConverter(new ParserConverter(fieldType, (Parser<?>)formatter, (ConversionService)this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser) {
/*  81 */     addConverter(new PrinterConverter(fieldType, printer, (ConversionService)this));
/*  82 */     addConverter(new ParserConverter(fieldType, parser, (ConversionService)this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFormatterForFieldAnnotation(AnnotationFormatterFactory<? extends Annotation> annotationFormatterFactory) {
/*  87 */     Class<? extends Annotation> annotationType = getAnnotationType(annotationFormatterFactory);
/*  88 */     if (this.embeddedValueResolver != null && annotationFormatterFactory instanceof EmbeddedValueResolverAware) {
/*  89 */       ((EmbeddedValueResolverAware)annotationFormatterFactory).setEmbeddedValueResolver(this.embeddedValueResolver);
/*     */     }
/*  91 */     Set<Class<?>> fieldTypes = annotationFormatterFactory.getFieldTypes();
/*  92 */     for (Class<?> fieldType : fieldTypes) {
/*  93 */       addConverter((GenericConverter)new AnnotationPrinterConverter(annotationType, annotationFormatterFactory, fieldType));
/*  94 */       addConverter((GenericConverter)new AnnotationParserConverter(annotationType, annotationFormatterFactory, fieldType));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static Class<?> getFieldType(Formatter<?> formatter) {
/* 100 */     Class<?> fieldType = GenericTypeResolver.resolveTypeArgument(formatter.getClass(), Formatter.class);
/* 101 */     if (fieldType == null && formatter instanceof DecoratingProxy) {
/* 102 */       fieldType = GenericTypeResolver.resolveTypeArgument(((DecoratingProxy)formatter)
/* 103 */           .getDecoratedClass(), Formatter.class);
/*     */     }
/* 105 */     if (fieldType == null) {
/* 106 */       throw new IllegalArgumentException("Unable to extract the parameterized field type from Formatter [" + formatter
/* 107 */           .getClass().getName() + "]; does the class parameterize the <T> generic type?");
/*     */     }
/* 109 */     return fieldType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Class<? extends Annotation> getAnnotationType(AnnotationFormatterFactory<? extends Annotation> factory) {
/* 115 */     Class<? extends Annotation> annotationType = GenericTypeResolver.resolveTypeArgument(factory.getClass(), AnnotationFormatterFactory.class);
/* 116 */     if (annotationType == null) {
/* 117 */       throw new IllegalArgumentException("Unable to extract parameterized Annotation type argument from AnnotationFormatterFactory [" + factory
/* 118 */           .getClass().getName() + "]; does the factory parameterize the <A extends Annotation> generic type?");
/*     */     }
/*     */     
/* 121 */     return annotationType;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PrinterConverter
/*     */     implements GenericConverter
/*     */   {
/*     */     private final Class<?> fieldType;
/*     */     
/*     */     private final TypeDescriptor printerObjectType;
/*     */     
/*     */     private final Printer printer;
/*     */     
/*     */     private final ConversionService conversionService;
/*     */     
/*     */     public PrinterConverter(Class<?> fieldType, Printer<?> printer, ConversionService conversionService) {
/* 137 */       this.fieldType = fieldType;
/* 138 */       this.printerObjectType = TypeDescriptor.valueOf(resolvePrinterObjectType(printer));
/* 139 */       this.printer = printer;
/* 140 */       this.conversionService = conversionService;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 145 */       return Collections.singleton(new GenericConverter.ConvertiblePair(this.fieldType, String.class));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 151 */       if (source == null) {
/* 152 */         return "";
/*     */       }
/* 154 */       if (!sourceType.isAssignableTo(this.printerObjectType)) {
/* 155 */         source = this.conversionService.convert(source, sourceType, this.printerObjectType);
/*     */       }
/* 157 */       return this.printer.print(source, LocaleContextHolder.getLocale());
/*     */     }
/*     */     
/*     */     private Class<?> resolvePrinterObjectType(Printer<?> printer) {
/* 161 */       return GenericTypeResolver.resolveTypeArgument(printer.getClass(), Printer.class);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 166 */       return this.fieldType.getName() + " -> " + String.class.getName() + " : " + this.printer;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ParserConverter
/*     */     implements GenericConverter
/*     */   {
/*     */     private final Class<?> fieldType;
/*     */     
/*     */     private final Parser<?> parser;
/*     */     private final ConversionService conversionService;
/*     */     
/*     */     public ParserConverter(Class<?> fieldType, Parser<?> parser, ConversionService conversionService) {
/* 180 */       this.fieldType = fieldType;
/* 181 */       this.parser = parser;
/* 182 */       this.conversionService = conversionService;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 187 */       return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, this.fieldType));
/*     */     }
/*     */     
/*     */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*     */       Object result;
/* 192 */       String text = (String)source;
/* 193 */       if (!StringUtils.hasText(text)) {
/* 194 */         return null;
/*     */       }
/*     */       
/*     */       try {
/* 198 */         result = this.parser.parse(text, LocaleContextHolder.getLocale());
/*     */       }
/* 200 */       catch (IllegalArgumentException ex) {
/* 201 */         throw ex;
/*     */       }
/* 203 */       catch (Throwable ex) {
/* 204 */         throw new IllegalArgumentException("Parse attempt failed for value [" + text + "]", ex);
/*     */       } 
/* 206 */       if (result == null) {
/* 207 */         throw new IllegalStateException("Parsers are not allowed to return null: " + this.parser);
/*     */       }
/* 209 */       TypeDescriptor resultType = TypeDescriptor.valueOf(result.getClass());
/* 210 */       if (!resultType.isAssignableTo(targetType)) {
/* 211 */         result = this.conversionService.convert(result, resultType, targetType);
/*     */       }
/* 213 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 218 */       return String.class.getName() + " -> " + this.fieldType.getName() + ": " + this.parser;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AnnotationPrinterConverter
/*     */     implements ConditionalGenericConverter
/*     */   {
/*     */     private final Class<? extends Annotation> annotationType;
/*     */     
/*     */     private final AnnotationFormatterFactory annotationFormatterFactory;
/*     */     
/*     */     private final Class<?> fieldType;
/*     */ 
/*     */     
/*     */     public AnnotationPrinterConverter(Class<? extends Annotation> annotationType, AnnotationFormatterFactory<?> annotationFormatterFactory, Class<?> fieldType) {
/* 235 */       this.annotationType = annotationType;
/* 236 */       this.annotationFormatterFactory = annotationFormatterFactory;
/* 237 */       this.fieldType = fieldType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 242 */       return Collections.singleton(new GenericConverter.ConvertiblePair(this.fieldType, String.class));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 247 */       return sourceType.hasAnnotation(this.annotationType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 253 */       Annotation ann = sourceType.getAnnotation(this.annotationType);
/* 254 */       if (ann == null) {
/* 255 */         throw new IllegalStateException("Expected [" + this.annotationType
/* 256 */             .getName() + "] to be present on " + sourceType);
/*     */       }
/* 258 */       FormattingConversionService.AnnotationConverterKey converterKey = new FormattingConversionService.AnnotationConverterKey(ann, sourceType.getObjectType());
/* 259 */       GenericConverter converter = (GenericConverter)FormattingConversionService.this.cachedPrinters.get(converterKey);
/* 260 */       if (converter == null) {
/* 261 */         Printer<?> printer = this.annotationFormatterFactory.getPrinter(converterKey
/* 262 */             .getAnnotation(), converterKey.getFieldType());
/* 263 */         converter = new FormattingConversionService.PrinterConverter(this.fieldType, printer, (ConversionService)FormattingConversionService.this);
/* 264 */         FormattingConversionService.this.cachedPrinters.put(converterKey, converter);
/*     */       } 
/* 266 */       return converter.convert(source, sourceType, targetType);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 271 */       return "@" + this.annotationType.getName() + " " + this.fieldType.getName() + " -> " + String.class
/* 272 */         .getName() + ": " + this.annotationFormatterFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AnnotationParserConverter
/*     */     implements ConditionalGenericConverter
/*     */   {
/*     */     private final Class<? extends Annotation> annotationType;
/*     */     
/*     */     private final AnnotationFormatterFactory annotationFormatterFactory;
/*     */     
/*     */     private final Class<?> fieldType;
/*     */ 
/*     */     
/*     */     public AnnotationParserConverter(Class<? extends Annotation> annotationType, AnnotationFormatterFactory<?> annotationFormatterFactory, Class<?> fieldType) {
/* 289 */       this.annotationType = annotationType;
/* 290 */       this.annotationFormatterFactory = annotationFormatterFactory;
/* 291 */       this.fieldType = fieldType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 296 */       return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, this.fieldType));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 301 */       return targetType.hasAnnotation(this.annotationType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 307 */       Annotation ann = targetType.getAnnotation(this.annotationType);
/* 308 */       if (ann == null) {
/* 309 */         throw new IllegalStateException("Expected [" + this.annotationType
/* 310 */             .getName() + "] to be present on " + targetType);
/*     */       }
/* 312 */       FormattingConversionService.AnnotationConverterKey converterKey = new FormattingConversionService.AnnotationConverterKey(ann, targetType.getObjectType());
/* 313 */       GenericConverter converter = (GenericConverter)FormattingConversionService.this.cachedParsers.get(converterKey);
/* 314 */       if (converter == null) {
/* 315 */         Parser<?> parser = this.annotationFormatterFactory.getParser(converterKey
/* 316 */             .getAnnotation(), converterKey.getFieldType());
/* 317 */         converter = new FormattingConversionService.ParserConverter(this.fieldType, parser, (ConversionService)FormattingConversionService.this);
/* 318 */         FormattingConversionService.this.cachedParsers.put(converterKey, converter);
/*     */       } 
/* 320 */       return converter.convert(source, sourceType, targetType);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 325 */       return String.class.getName() + " -> @" + this.annotationType.getName() + " " + this.fieldType
/* 326 */         .getName() + ": " + this.annotationFormatterFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AnnotationConverterKey
/*     */   {
/*     */     private final Annotation annotation;
/*     */     
/*     */     private final Class<?> fieldType;
/*     */     
/*     */     public AnnotationConverterKey(Annotation annotation, Class<?> fieldType) {
/* 338 */       this.annotation = annotation;
/* 339 */       this.fieldType = fieldType;
/*     */     }
/*     */     
/*     */     public Annotation getAnnotation() {
/* 343 */       return this.annotation;
/*     */     }
/*     */     
/*     */     public Class<?> getFieldType() {
/* 347 */       return this.fieldType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 352 */       if (this == other) {
/* 353 */         return true;
/*     */       }
/* 355 */       AnnotationConverterKey otherKey = (AnnotationConverterKey)other;
/* 356 */       return (this.fieldType == otherKey.fieldType && this.annotation.equals(otherKey.annotation));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 361 */       return this.fieldType.hashCode() * 29 + this.annotation.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\support\FormattingConversionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */