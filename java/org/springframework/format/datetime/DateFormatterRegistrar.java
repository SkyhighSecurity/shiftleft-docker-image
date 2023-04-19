/*     */ package org.springframework.format.datetime;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.FormatterRegistrar;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateFormatterRegistrar
/*     */   implements FormatterRegistrar
/*     */ {
/*     */   private DateFormatter dateFormatter;
/*     */   
/*     */   public void setFormatter(DateFormatter dateFormatter) {
/*  54 */     Assert.notNull(dateFormatter, "DateFormatter must not be null");
/*  55 */     this.dateFormatter = dateFormatter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerFormatters(FormatterRegistry registry) {
/*  61 */     addDateConverters((ConverterRegistry)registry);
/*  62 */     registry.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
/*     */ 
/*     */ 
/*     */     
/*  66 */     if (this.dateFormatter != null) {
/*  67 */       registry.addFormatter(this.dateFormatter);
/*  68 */       registry.addFormatterForFieldType(Calendar.class, this.dateFormatter);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addDateConverters(ConverterRegistry converterRegistry) {
/*  77 */     converterRegistry.addConverter(new DateToLongConverter());
/*  78 */     converterRegistry.addConverter(new DateToCalendarConverter());
/*  79 */     converterRegistry.addConverter(new CalendarToDateConverter());
/*  80 */     converterRegistry.addConverter(new CalendarToLongConverter());
/*  81 */     converterRegistry.addConverter(new LongToDateConverter());
/*  82 */     converterRegistry.addConverter(new LongToCalendarConverter());
/*     */   }
/*     */   
/*     */   private static class DateToLongConverter
/*     */     implements Converter<Date, Long> {
/*     */     private DateToLongConverter() {}
/*     */     
/*     */     public Long convert(Date source) {
/*  90 */       return Long.valueOf(source.getTime());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateToCalendarConverter
/*     */     implements Converter<Date, Calendar> {
/*     */     private DateToCalendarConverter() {}
/*     */     
/*     */     public Calendar convert(Date source) {
/*  99 */       Calendar calendar = Calendar.getInstance();
/* 100 */       calendar.setTime(source);
/* 101 */       return calendar;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToDateConverter
/*     */     implements Converter<Calendar, Date> {
/*     */     private CalendarToDateConverter() {}
/*     */     
/*     */     public Date convert(Calendar source) {
/* 110 */       return source.getTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToLongConverter
/*     */     implements Converter<Calendar, Long> {
/*     */     private CalendarToLongConverter() {}
/*     */     
/*     */     public Long convert(Calendar source) {
/* 119 */       return Long.valueOf(source.getTimeInMillis());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongToDateConverter
/*     */     implements Converter<Long, Date> {
/*     */     private LongToDateConverter() {}
/*     */     
/*     */     public Date convert(Long source) {
/* 128 */       return new Date(source.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongToCalendarConverter
/*     */     implements Converter<Long, Calendar> {
/*     */     private LongToCalendarConverter() {}
/*     */     
/*     */     public Calendar convert(Long source) {
/* 137 */       Calendar calendar = Calendar.getInstance();
/* 138 */       calendar.setTimeInMillis(source.longValue());
/* 139 */       return calendar;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\DateFormatterRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */