/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import org.joda.time.Duration;
/*     */ import org.joda.time.LocalDate;
/*     */ import org.joda.time.LocalDateTime;
/*     */ import org.joda.time.LocalTime;
/*     */ import org.joda.time.MonthDay;
/*     */ import org.joda.time.Period;
/*     */ import org.joda.time.ReadableInstant;
/*     */ import org.joda.time.YearMonth;
/*     */ import org.joda.time.format.DateTimeFormat;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.FormatterRegistrar;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.format.Parser;
/*     */ import org.springframework.format.Printer;
/*     */ import org.springframework.format.annotation.DateTimeFormat;
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
/*     */ 
/*     */ 
/*     */ public class JodaTimeFormatterRegistrar
/*     */   implements FormatterRegistrar
/*     */ {
/*     */   private enum Type
/*     */   {
/*  62 */     DATE, TIME, DATE_TIME;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private static final boolean jodaTime2Available = ClassUtils.isPresent("org.joda.time.YearMonth", JodaTimeFormatterRegistrar.class
/*  71 */       .getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private final Map<Type, DateTimeFormatter> formatters = new EnumMap<Type, DateTimeFormatter>(Type.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<Type, DateTimeFormatterFactory> factories;
/*     */ 
/*     */ 
/*     */   
/*     */   public JodaTimeFormatterRegistrar() {
/*  85 */     this.factories = new EnumMap<Type, DateTimeFormatterFactory>(Type.class);
/*  86 */     for (Type type : Type.values()) {
/*  87 */       this.factories.put(type, new DateTimeFormatterFactory());
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
/*     */   public void setUseIsoFormat(boolean useIsoFormat) {
/*  99 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE)).setIso(useIsoFormat ? DateTimeFormat.ISO.DATE : null);
/* 100 */     ((DateTimeFormatterFactory)this.factories.get(Type.TIME)).setIso(useIsoFormat ? DateTimeFormat.ISO.TIME : null);
/* 101 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE_TIME)).setIso(useIsoFormat ? DateTimeFormat.ISO.DATE_TIME : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateStyle(String dateStyle) {
/* 109 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE)).setStyle(dateStyle + "-");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeStyle(String timeStyle) {
/* 117 */     ((DateTimeFormatterFactory)this.factories.get(Type.TIME)).setStyle("-" + timeStyle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateTimeStyle(String dateTimeStyle) {
/* 126 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE_TIME)).setStyle(dateTimeStyle);
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
/*     */   public void setDateFormatter(DateTimeFormatter formatter) {
/* 140 */     this.formatters.put(Type.DATE, formatter);
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
/*     */   public void setTimeFormatter(DateTimeFormatter formatter) {
/* 154 */     this.formatters.put(Type.TIME, formatter);
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
/*     */   public void setDateTimeFormatter(DateTimeFormatter formatter) {
/* 169 */     this.formatters.put(Type.DATE_TIME, formatter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerFormatters(FormatterRegistry registry) {
/* 175 */     JodaTimeConverters.registerConverters((ConverterRegistry)registry);
/*     */     
/* 177 */     DateTimeFormatter dateFormatter = getFormatter(Type.DATE);
/* 178 */     DateTimeFormatter timeFormatter = getFormatter(Type.TIME);
/* 179 */     DateTimeFormatter dateTimeFormatter = getFormatter(Type.DATE_TIME);
/*     */     
/* 181 */     addFormatterForFields(registry, new ReadablePartialPrinter(dateFormatter), new LocalDateParser(dateFormatter), new Class[] { LocalDate.class });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     addFormatterForFields(registry, new ReadablePartialPrinter(timeFormatter), new LocalTimeParser(timeFormatter), new Class[] { LocalTime.class });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     addFormatterForFields(registry, new ReadablePartialPrinter(dateTimeFormatter), new LocalDateTimeParser(dateTimeFormatter), new Class[] { LocalDateTime.class });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     addFormatterForFields(registry, new ReadableInstantPrinter(dateTimeFormatter), new DateTimeParser(dateTimeFormatter), new Class[] { ReadableInstant.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     if (this.formatters.containsKey(Type.DATE_TIME)) {
/* 204 */       addFormatterForFields(registry, new ReadableInstantPrinter(dateTimeFormatter), new DateTimeParser(dateTimeFormatter), new Class[] { Date.class, Calendar.class });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     registry.addFormatterForFieldType(Period.class, new PeriodFormatter());
/* 211 */     registry.addFormatterForFieldType(Duration.class, new DurationFormatter());
/* 212 */     if (jodaTime2Available) {
/* 213 */       JodaTime2Delegate.registerAdditionalFormatters(registry);
/*     */     }
/*     */     
/* 216 */     registry.addFormatterForFieldAnnotation(new JodaDateTimeFormatAnnotationFormatterFactory());
/*     */   }
/*     */   
/*     */   private DateTimeFormatter getFormatter(Type type) {
/* 220 */     DateTimeFormatter formatter = this.formatters.get(type);
/* 221 */     if (formatter != null) {
/* 222 */       return formatter;
/*     */     }
/* 224 */     DateTimeFormatter fallbackFormatter = getFallbackFormatter(type);
/* 225 */     return ((DateTimeFormatterFactory)this.factories.get(type)).createDateTimeFormatter(fallbackFormatter);
/*     */   }
/*     */   
/*     */   private DateTimeFormatter getFallbackFormatter(Type type) {
/* 229 */     switch (type) { case DATE:
/* 230 */         return DateTimeFormat.shortDate();
/* 231 */       case TIME: return DateTimeFormat.shortTime(); }
/* 232 */      return DateTimeFormat.shortDateTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addFormatterForFields(FormatterRegistry registry, Printer<?> printer, Parser<?> parser, Class<?>... fieldTypes) {
/* 239 */     for (Class<?> fieldType : fieldTypes) {
/* 240 */       registry.addFormatterForFieldType(fieldType, printer, parser);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JodaTime2Delegate
/*     */   {
/*     */     public static void registerAdditionalFormatters(FormatterRegistry registry) {
/* 251 */       registry.addFormatterForFieldType(YearMonth.class, new YearMonthFormatter());
/* 252 */       registry.addFormatterForFieldType(MonthDay.class, new MonthDayFormatter());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\joda\JodaTimeFormatterRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */