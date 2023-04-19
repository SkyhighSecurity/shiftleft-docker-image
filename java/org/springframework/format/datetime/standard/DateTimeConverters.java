/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.LocalTime;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.datetime.DateFormatterRegistrar;
/*     */ import org.springframework.lang.UsesJava8;
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
/*     */ @UsesJava8
/*     */ final class DateTimeConverters
/*     */ {
/*     */   public static void registerConverters(ConverterRegistry registry) {
/*  54 */     DateFormatterRegistrar.addDateConverters(registry);
/*     */     
/*  56 */     registry.addConverter(new LocalDateTimeToLocalDateConverter());
/*  57 */     registry.addConverter(new LocalDateTimeToLocalTimeConverter());
/*  58 */     registry.addConverter(new ZonedDateTimeToLocalDateConverter());
/*  59 */     registry.addConverter(new ZonedDateTimeToLocalTimeConverter());
/*  60 */     registry.addConverter(new ZonedDateTimeToLocalDateTimeConverter());
/*  61 */     registry.addConverter(new ZonedDateTimeToOffsetDateTimeConverter());
/*  62 */     registry.addConverter(new ZonedDateTimeToInstantConverter());
/*  63 */     registry.addConverter(new OffsetDateTimeToLocalDateConverter());
/*  64 */     registry.addConverter(new OffsetDateTimeToLocalTimeConverter());
/*  65 */     registry.addConverter(new OffsetDateTimeToLocalDateTimeConverter());
/*  66 */     registry.addConverter(new OffsetDateTimeToZonedDateTimeConverter());
/*  67 */     registry.addConverter(new OffsetDateTimeToInstantConverter());
/*  68 */     registry.addConverter(new CalendarToZonedDateTimeConverter());
/*  69 */     registry.addConverter(new CalendarToOffsetDateTimeConverter());
/*  70 */     registry.addConverter(new CalendarToLocalDateConverter());
/*  71 */     registry.addConverter(new CalendarToLocalTimeConverter());
/*  72 */     registry.addConverter(new CalendarToLocalDateTimeConverter());
/*  73 */     registry.addConverter(new CalendarToInstantConverter());
/*  74 */     registry.addConverter(new LongToInstantConverter());
/*  75 */     registry.addConverter(new InstantToLongConverter());
/*     */   }
/*     */   
/*     */   private static ZonedDateTime calendarToZonedDateTime(Calendar source) {
/*  79 */     if (source instanceof GregorianCalendar) {
/*  80 */       return ((GregorianCalendar)source).toZonedDateTime();
/*     */     }
/*     */     
/*  83 */     return ZonedDateTime.ofInstant(Instant.ofEpochMilli(source.getTimeInMillis()), source
/*  84 */         .getTimeZone().toZoneId());
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class LocalDateTimeToLocalDateConverter
/*     */     implements Converter<LocalDateTime, LocalDate>
/*     */   {
/*     */     private LocalDateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(LocalDateTime source) {
/*  94 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class LocalDateTimeToLocalTimeConverter
/*     */     implements Converter<LocalDateTime, LocalTime> {
/*     */     private LocalDateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(LocalDateTime source) {
/* 104 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToLocalDateConverter
/*     */     implements Converter<ZonedDateTime, LocalDate> {
/*     */     private ZonedDateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(ZonedDateTime source) {
/* 114 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToLocalTimeConverter
/*     */     implements Converter<ZonedDateTime, LocalTime> {
/*     */     private ZonedDateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(ZonedDateTime source) {
/* 124 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToLocalDateTimeConverter
/*     */     implements Converter<ZonedDateTime, LocalDateTime> {
/*     */     private ZonedDateTimeToLocalDateTimeConverter() {}
/*     */     
/*     */     public LocalDateTime convert(ZonedDateTime source) {
/* 134 */       return source.toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToOffsetDateTimeConverter implements Converter<ZonedDateTime, OffsetDateTime> {
/*     */     private ZonedDateTimeToOffsetDateTimeConverter() {}
/*     */     
/*     */     public OffsetDateTime convert(ZonedDateTime source) {
/* 143 */       return source.toOffsetDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToInstantConverter
/*     */     implements Converter<ZonedDateTime, Instant>
/*     */   {
/*     */     private ZonedDateTimeToInstantConverter() {}
/*     */     
/*     */     public Instant convert(ZonedDateTime source) {
/* 154 */       return source.toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToLocalDateConverter
/*     */     implements Converter<OffsetDateTime, LocalDate> {
/*     */     private OffsetDateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(OffsetDateTime source) {
/* 164 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToLocalTimeConverter
/*     */     implements Converter<OffsetDateTime, LocalTime> {
/*     */     private OffsetDateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(OffsetDateTime source) {
/* 174 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToLocalDateTimeConverter
/*     */     implements Converter<OffsetDateTime, LocalDateTime> {
/*     */     private OffsetDateTimeToLocalDateTimeConverter() {}
/*     */     
/*     */     public LocalDateTime convert(OffsetDateTime source) {
/* 184 */       return source.toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToZonedDateTimeConverter
/*     */     implements Converter<OffsetDateTime, ZonedDateTime> {
/*     */     private OffsetDateTimeToZonedDateTimeConverter() {}
/*     */     
/*     */     public ZonedDateTime convert(OffsetDateTime source) {
/* 194 */       return source.toZonedDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToInstantConverter
/*     */     implements Converter<OffsetDateTime, Instant> {
/*     */     private OffsetDateTimeToInstantConverter() {}
/*     */     
/*     */     public Instant convert(OffsetDateTime source) {
/* 204 */       return source.toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToZonedDateTimeConverter
/*     */     implements Converter<Calendar, ZonedDateTime> {
/*     */     private CalendarToZonedDateTimeConverter() {}
/*     */     
/*     */     public ZonedDateTime convert(Calendar source) {
/* 214 */       return DateTimeConverters.calendarToZonedDateTime(source);
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToOffsetDateTimeConverter
/*     */     implements Converter<Calendar, OffsetDateTime> {
/*     */     private CalendarToOffsetDateTimeConverter() {}
/*     */     
/*     */     public OffsetDateTime convert(Calendar source) {
/* 224 */       return DateTimeConverters.calendarToZonedDateTime(source).toOffsetDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToLocalDateConverter
/*     */     implements Converter<Calendar, LocalDate> {
/*     */     private CalendarToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(Calendar source) {
/* 234 */       return DateTimeConverters.calendarToZonedDateTime(source).toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToLocalTimeConverter
/*     */     implements Converter<Calendar, LocalTime> {
/*     */     private CalendarToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(Calendar source) {
/* 244 */       return DateTimeConverters.calendarToZonedDateTime(source).toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToLocalDateTimeConverter
/*     */     implements Converter<Calendar, LocalDateTime> {
/*     */     private CalendarToLocalDateTimeConverter() {}
/*     */     
/*     */     public LocalDateTime convert(Calendar source) {
/* 254 */       return DateTimeConverters.calendarToZonedDateTime(source).toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToInstantConverter
/*     */     implements Converter<Calendar, Instant>
/*     */   {
/*     */     private CalendarToInstantConverter() {}
/*     */     
/*     */     public Instant convert(Calendar source) {
/* 265 */       return DateTimeConverters.calendarToZonedDateTime(source).toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class LongToInstantConverter
/*     */     implements Converter<Long, Instant> {
/*     */     private LongToInstantConverter() {}
/*     */     
/*     */     public Instant convert(Long source) {
/* 275 */       return Instant.ofEpochMilli(source.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class InstantToLongConverter
/*     */     implements Converter<Instant, Long> {
/*     */     private InstantToLongConverter() {}
/*     */     
/*     */     public Long convert(Instant source) {
/* 285 */       return Long.valueOf(source.toEpochMilli());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\standard\DateTimeConverters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */