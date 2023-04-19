/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.joda.time.LocalDate;
/*     */ import org.joda.time.LocalDateTime;
/*     */ import org.joda.time.LocalTime;
/*     */ import org.joda.time.ReadableInstant;
/*     */ import org.joda.time.ReadablePartial;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.springframework.context.support.EmbeddedValueResolutionSupport;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Parser;
/*     */ import org.springframework.format.Printer;
/*     */ import org.springframework.format.annotation.DateTimeFormat;
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
/*     */ public class JodaDateTimeFormatAnnotationFormatterFactory
/*     */   extends EmbeddedValueResolutionSupport
/*     */   implements AnnotationFormatterFactory<DateTimeFormat>
/*     */ {
/*     */   private static final Set<Class<?>> FIELD_TYPES;
/*     */   
/*     */   static {
/*  60 */     Set<Class<?>> fieldTypes = new HashSet<Class<?>>(8);
/*  61 */     fieldTypes.add(ReadableInstant.class);
/*  62 */     fieldTypes.add(LocalDate.class);
/*  63 */     fieldTypes.add(LocalTime.class);
/*  64 */     fieldTypes.add(LocalDateTime.class);
/*  65 */     fieldTypes.add(Date.class);
/*  66 */     fieldTypes.add(Calendar.class);
/*  67 */     fieldTypes.add(Long.class);
/*  68 */     FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Set<Class<?>> getFieldTypes() {
/*  74 */     return FIELD_TYPES;
/*     */   }
/*     */ 
/*     */   
/*     */   public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
/*  79 */     DateTimeFormatter formatter = getFormatter(annotation, fieldType);
/*  80 */     if (ReadablePartial.class.isAssignableFrom(fieldType)) {
/*  81 */       return new ReadablePartialPrinter(formatter);
/*     */     }
/*  83 */     if (ReadableInstant.class.isAssignableFrom(fieldType) || Calendar.class.isAssignableFrom(fieldType))
/*     */     {
/*  85 */       return new ReadableInstantPrinter(formatter);
/*     */     }
/*     */ 
/*     */     
/*  89 */     return new MillisecondInstantPrinter(formatter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
/*  95 */     if (LocalDate.class == fieldType) {
/*  96 */       return new LocalDateParser(getFormatter(annotation, fieldType));
/*     */     }
/*  98 */     if (LocalTime.class == fieldType) {
/*  99 */       return new LocalTimeParser(getFormatter(annotation, fieldType));
/*     */     }
/* 101 */     if (LocalDateTime.class == fieldType) {
/* 102 */       return new LocalDateTimeParser(getFormatter(annotation, fieldType));
/*     */     }
/*     */     
/* 105 */     return new DateTimeParser(getFormatter(annotation, fieldType));
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
/*     */   protected DateTimeFormatter getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
/* 117 */     DateTimeFormatterFactory factory = new DateTimeFormatterFactory();
/* 118 */     factory.setStyle(resolveEmbeddedValue(annotation.style()));
/* 119 */     factory.setIso(annotation.iso());
/* 120 */     factory.setPattern(resolveEmbeddedValue(annotation.pattern()));
/* 121 */     return factory.createDateTimeFormatter();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\joda\JodaDateTimeFormatAnnotationFormatterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */