/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.joda.time.DateMidnight;
/*     */ import org.joda.time.DateTime;
/*     */ import org.joda.time.Instant;
/*     */ import org.joda.time.LocalDate;
/*     */ import org.joda.time.LocalDateTime;
/*     */ import org.joda.time.LocalTime;
/*     */ import org.joda.time.MutableDateTime;
/*     */ import org.joda.time.ReadableInstant;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.datetime.DateFormatterRegistrar;
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
/*     */ final class JodaTimeConverters
/*     */ {
/*     */   public static void registerConverters(ConverterRegistry registry) {
/*  55 */     DateFormatterRegistrar.addDateConverters(registry);
/*     */     
/*  57 */     registry.addConverter(new DateTimeToLocalDateConverter());
/*  58 */     registry.addConverter(new DateTimeToLocalTimeConverter());
/*  59 */     registry.addConverter(new DateTimeToLocalDateTimeConverter());
/*  60 */     registry.addConverter(new DateTimeToDateMidnightConverter());
/*  61 */     registry.addConverter(new DateTimeToMutableDateTimeConverter());
/*  62 */     registry.addConverter(new DateTimeToInstantConverter());
/*  63 */     registry.addConverter(new DateTimeToDateConverter());
/*  64 */     registry.addConverter(new DateTimeToCalendarConverter());
/*  65 */     registry.addConverter(new DateTimeToLongConverter());
/*  66 */     registry.addConverter(new DateToReadableInstantConverter());
/*  67 */     registry.addConverter(new CalendarToReadableInstantConverter());
/*  68 */     registry.addConverter(new LongToReadableInstantConverter());
/*  69 */     registry.addConverter(new LocalDateTimeToLocalDateConverter());
/*  70 */     registry.addConverter(new LocalDateTimeToLocalTimeConverter());
/*     */   }
/*     */   
/*     */   private static class DateTimeToLocalDateConverter
/*     */     implements Converter<DateTime, LocalDate> {
/*     */     private DateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(DateTime source) {
/*  78 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToLocalTimeConverter
/*     */     implements Converter<DateTime, LocalTime> {
/*     */     private DateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(DateTime source) {
/*  87 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToLocalDateTimeConverter
/*     */     implements Converter<DateTime, LocalDateTime> {
/*     */     private DateTimeToLocalDateTimeConverter() {}
/*     */     
/*     */     public LocalDateTime convert(DateTime source) {
/*  96 */       return source.toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   private static class DateTimeToDateMidnightConverter
/*     */     implements Converter<DateTime, DateMidnight> {
/*     */     private DateTimeToDateMidnightConverter() {}
/*     */     
/*     */     public DateMidnight convert(DateTime source) {
/* 106 */       return source.toDateMidnight();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToMutableDateTimeConverter
/*     */     implements Converter<DateTime, MutableDateTime> {
/*     */     private DateTimeToMutableDateTimeConverter() {}
/*     */     
/*     */     public MutableDateTime convert(DateTime source) {
/* 115 */       return source.toMutableDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToInstantConverter
/*     */     implements Converter<DateTime, Instant> {
/*     */     private DateTimeToInstantConverter() {}
/*     */     
/*     */     public Instant convert(DateTime source) {
/* 124 */       return source.toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToDateConverter
/*     */     implements Converter<DateTime, Date> {
/*     */     private DateTimeToDateConverter() {}
/*     */     
/*     */     public Date convert(DateTime source) {
/* 133 */       return source.toDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToCalendarConverter
/*     */     implements Converter<DateTime, Calendar> {
/*     */     private DateTimeToCalendarConverter() {}
/*     */     
/*     */     public Calendar convert(DateTime source) {
/* 142 */       return source.toGregorianCalendar();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToLongConverter
/*     */     implements Converter<DateTime, Long> {
/*     */     private DateTimeToLongConverter() {}
/*     */     
/*     */     public Long convert(DateTime source) {
/* 151 */       return Long.valueOf(source.getMillis());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DateToReadableInstantConverter
/*     */     implements Converter<Date, ReadableInstant>
/*     */   {
/*     */     private DateToReadableInstantConverter() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public ReadableInstant convert(Date source) {
/* 165 */       return (ReadableInstant)new DateTime(source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CalendarToReadableInstantConverter
/*     */     implements Converter<Calendar, ReadableInstant>
/*     */   {
/*     */     private CalendarToReadableInstantConverter() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public ReadableInstant convert(Calendar source) {
/* 179 */       return (ReadableInstant)new DateTime(source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LongToReadableInstantConverter
/*     */     implements Converter<Long, ReadableInstant>
/*     */   {
/*     */     private LongToReadableInstantConverter() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public ReadableInstant convert(Long source) {
/* 193 */       return (ReadableInstant)new DateTime(source.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LocalDateTimeToLocalDateConverter
/*     */     implements Converter<LocalDateTime, LocalDate> {
/*     */     private LocalDateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(LocalDateTime source) {
/* 202 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LocalDateTimeToLocalTimeConverter
/*     */     implements Converter<LocalDateTime, LocalTime> {
/*     */     private LocalDateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(LocalDateTime source) {
/* 211 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\joda\JodaTimeConverters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */