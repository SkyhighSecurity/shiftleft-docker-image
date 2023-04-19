/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ public class DefaultConversionService
/*     */   extends GenericConversionService
/*     */ {
/*  45 */   private static final boolean javaUtilOptionalClassAvailable = ClassUtils.isPresent("java.util.Optional", DefaultConversionService.class.getClassLoader());
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static final boolean jsr310Available = ClassUtils.isPresent("java.time.ZoneId", DefaultConversionService.class.getClassLoader());
/*     */ 
/*     */   
/*  52 */   private static final boolean streamAvailable = ClassUtils.isPresent("java.util.stream.Stream", DefaultConversionService.class
/*  53 */       .getClassLoader());
/*     */ 
/*     */ 
/*     */   
/*     */   private static volatile DefaultConversionService sharedInstance;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultConversionService() {
/*  63 */     addDefaultConverters(this);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConversionService getSharedInstance() {
/*  79 */     if (sharedInstance == null) {
/*  80 */       synchronized (DefaultConversionService.class) {
/*  81 */         if (sharedInstance == null) {
/*  82 */           sharedInstance = new DefaultConversionService();
/*     */         }
/*     */       } 
/*     */     }
/*  86 */     return sharedInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addDefaultConverters(ConverterRegistry converterRegistry) {
/*  96 */     addScalarConverters(converterRegistry);
/*  97 */     addCollectionConverters(converterRegistry);
/*     */     
/*  99 */     converterRegistry.addConverter((GenericConverter)new ByteBufferConverter((ConversionService)converterRegistry));
/* 100 */     if (jsr310Available) {
/* 101 */       Jsr310ConverterRegistrar.registerJsr310Converters(converterRegistry);
/*     */     }
/*     */     
/* 104 */     converterRegistry.addConverter((GenericConverter)new ObjectToObjectConverter());
/* 105 */     converterRegistry.addConverter((GenericConverter)new IdToEntityConverter((ConversionService)converterRegistry));
/* 106 */     converterRegistry.addConverter((GenericConverter)new FallbackObjectToStringConverter());
/* 107 */     if (javaUtilOptionalClassAvailable) {
/* 108 */       converterRegistry.addConverter((GenericConverter)new ObjectToOptionalConverter((ConversionService)converterRegistry));
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
/*     */   public static void addCollectionConverters(ConverterRegistry converterRegistry) {
/* 120 */     ConversionService conversionService = (ConversionService)converterRegistry;
/*     */     
/* 122 */     converterRegistry.addConverter((GenericConverter)new ArrayToCollectionConverter(conversionService));
/* 123 */     converterRegistry.addConverter((GenericConverter)new CollectionToArrayConverter(conversionService));
/*     */     
/* 125 */     converterRegistry.addConverter((GenericConverter)new ArrayToArrayConverter(conversionService));
/* 126 */     converterRegistry.addConverter((GenericConverter)new CollectionToCollectionConverter(conversionService));
/* 127 */     converterRegistry.addConverter((GenericConverter)new MapToMapConverter(conversionService));
/*     */     
/* 129 */     converterRegistry.addConverter((GenericConverter)new ArrayToStringConverter(conversionService));
/* 130 */     converterRegistry.addConverter((GenericConverter)new StringToArrayConverter(conversionService));
/*     */     
/* 132 */     converterRegistry.addConverter((GenericConverter)new ArrayToObjectConverter(conversionService));
/* 133 */     converterRegistry.addConverter((GenericConverter)new ObjectToArrayConverter(conversionService));
/*     */     
/* 135 */     converterRegistry.addConverter((GenericConverter)new CollectionToStringConverter(conversionService));
/* 136 */     converterRegistry.addConverter((GenericConverter)new StringToCollectionConverter(conversionService));
/*     */     
/* 138 */     converterRegistry.addConverter((GenericConverter)new CollectionToObjectConverter(conversionService));
/* 139 */     converterRegistry.addConverter((GenericConverter)new ObjectToCollectionConverter(conversionService));
/*     */     
/* 141 */     if (streamAvailable) {
/* 142 */       converterRegistry.addConverter((GenericConverter)new StreamConverter(conversionService));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addScalarConverters(ConverterRegistry converterRegistry) {
/* 147 */     converterRegistry.addConverterFactory(new NumberToNumberConverterFactory());
/*     */     
/* 149 */     converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
/* 150 */     converterRegistry.addConverter(Number.class, String.class, new ObjectToStringConverter());
/*     */     
/* 152 */     converterRegistry.addConverter(new StringToCharacterConverter());
/* 153 */     converterRegistry.addConverter(Character.class, String.class, new ObjectToStringConverter());
/*     */     
/* 155 */     converterRegistry.addConverter(new NumberToCharacterConverter());
/* 156 */     converterRegistry.addConverterFactory(new CharacterToNumberFactory());
/*     */     
/* 158 */     converterRegistry.addConverter(new StringToBooleanConverter());
/* 159 */     converterRegistry.addConverter(Boolean.class, String.class, new ObjectToStringConverter());
/*     */     
/* 161 */     converterRegistry.addConverterFactory(new StringToEnumConverterFactory());
/* 162 */     converterRegistry.addConverter(new EnumToStringConverter((ConversionService)converterRegistry));
/*     */     
/* 164 */     converterRegistry.addConverterFactory(new IntegerToEnumConverterFactory());
/* 165 */     converterRegistry.addConverter(new EnumToIntegerConverter((ConversionService)converterRegistry));
/*     */     
/* 167 */     converterRegistry.addConverter(new StringToLocaleConverter());
/* 168 */     converterRegistry.addConverter(Locale.class, String.class, new ObjectToStringConverter());
/*     */     
/* 170 */     converterRegistry.addConverter(new StringToCharsetConverter());
/* 171 */     converterRegistry.addConverter(Charset.class, String.class, new ObjectToStringConverter());
/*     */     
/* 173 */     converterRegistry.addConverter(new StringToCurrencyConverter());
/* 174 */     converterRegistry.addConverter(Currency.class, String.class, new ObjectToStringConverter());
/*     */     
/* 176 */     converterRegistry.addConverter(new StringToPropertiesConverter());
/* 177 */     converterRegistry.addConverter(new PropertiesToStringConverter());
/*     */     
/* 179 */     converterRegistry.addConverter(new StringToUUIDConverter());
/* 180 */     converterRegistry.addConverter(UUID.class, String.class, new ObjectToStringConverter());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Jsr310ConverterRegistrar
/*     */   {
/*     */     public static void registerJsr310Converters(ConverterRegistry converterRegistry) {
/* 190 */       converterRegistry.addConverter(new StringToTimeZoneConverter());
/* 191 */       converterRegistry.addConverter(new ZoneIdToTimeZoneConverter());
/* 192 */       converterRegistry.addConverter(new ZonedDateTimeToCalendarConverter());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\DefaultConversionService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */