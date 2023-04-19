/*     */ package org.springframework.format.support;
/*     */ 
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.core.convert.support.DefaultConversionService;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.format.datetime.DateFormatterRegistrar;
/*     */ import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
/*     */ import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
/*     */ import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
/*     */ import org.springframework.format.number.money.CurrencyUnitFormatter;
/*     */ import org.springframework.format.number.money.Jsr354NumberFormatAnnotationFormatterFactory;
/*     */ import org.springframework.format.number.money.MonetaryAmountFormatter;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultFormattingConversionService
/*     */   extends FormattingConversionService
/*     */ {
/*  49 */   private static final boolean jsr354Present = ClassUtils.isPresent("javax.money.MonetaryAmount", DefaultFormattingConversionService.class
/*  50 */       .getClassLoader());
/*     */   
/*  52 */   private static final boolean jsr310Present = ClassUtils.isPresent("java.time.LocalDate", DefaultFormattingConversionService.class
/*  53 */       .getClassLoader());
/*     */   
/*  55 */   private static final boolean jodaTimePresent = ClassUtils.isPresent("org.joda.time.LocalDate", DefaultFormattingConversionService.class
/*  56 */       .getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormattingConversionService() {
/*  65 */     this(null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormattingConversionService(boolean registerDefaultFormatters) {
/*  76 */     this(null, registerDefaultFormatters);
/*     */   }
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
/*     */   public DefaultFormattingConversionService(StringValueResolver embeddedValueResolver, boolean registerDefaultFormatters) {
/*  89 */     setEmbeddedValueResolver(embeddedValueResolver);
/*  90 */     DefaultConversionService.addDefaultConverters((ConverterRegistry)this);
/*  91 */     if (registerDefaultFormatters) {
/*  92 */       addDefaultFormatters(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addDefaultFormatters(FormatterRegistry formatterRegistry) {
/* 105 */     formatterRegistry.addFormatterForFieldAnnotation((AnnotationFormatterFactory)new NumberFormatAnnotationFormatterFactory());
/*     */ 
/*     */     
/* 108 */     if (jsr354Present) {
/* 109 */       formatterRegistry.addFormatter((Formatter)new CurrencyUnitFormatter());
/* 110 */       formatterRegistry.addFormatter((Formatter)new MonetaryAmountFormatter());
/* 111 */       formatterRegistry.addFormatterForFieldAnnotation((AnnotationFormatterFactory)new Jsr354NumberFormatAnnotationFormatterFactory());
/*     */     } 
/*     */ 
/*     */     
/* 115 */     if (jsr310Present)
/*     */     {
/* 117 */       (new DateTimeFormatterRegistrar()).registerFormatters(formatterRegistry);
/*     */     }
/* 119 */     if (jodaTimePresent) {
/*     */       
/* 121 */       (new JodaTimeFormatterRegistrar()).registerFormatters(formatterRegistry);
/*     */     }
/*     */     else {
/*     */       
/* 125 */       (new DateFormatterRegistrar()).registerFormatters(formatterRegistry);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\support\DefaultFormattingConversionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */