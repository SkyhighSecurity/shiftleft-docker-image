/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.LocalTime;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.OffsetTime;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.context.support.EmbeddedValueResolutionSupport;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Parser;
/*     */ import org.springframework.format.Printer;
/*     */ import org.springframework.format.annotation.DateTimeFormat;
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
/*     */ @UsesJava8
/*     */ public class Jsr310DateTimeFormatAnnotationFormatterFactory
/*     */   extends EmbeddedValueResolutionSupport
/*     */   implements AnnotationFormatterFactory<DateTimeFormat>
/*     */ {
/*     */   private static final Set<Class<?>> FIELD_TYPES;
/*     */   
/*     */   static {
/*  54 */     Set<Class<?>> fieldTypes = new HashSet<Class<?>>(8);
/*  55 */     fieldTypes.add(LocalDate.class);
/*  56 */     fieldTypes.add(LocalTime.class);
/*  57 */     fieldTypes.add(LocalDateTime.class);
/*  58 */     fieldTypes.add(ZonedDateTime.class);
/*  59 */     fieldTypes.add(OffsetDateTime.class);
/*  60 */     fieldTypes.add(OffsetTime.class);
/*  61 */     FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Set<Class<?>> getFieldTypes() {
/*  67 */     return FIELD_TYPES;
/*     */   }
/*     */ 
/*     */   
/*     */   public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
/*  72 */     DateTimeFormatter formatter = getFormatter(annotation, fieldType);
/*     */ 
/*     */     
/*  75 */     if (formatter == DateTimeFormatter.ISO_DATE) {
/*  76 */       if (isLocal(fieldType)) {
/*  77 */         formatter = DateTimeFormatter.ISO_LOCAL_DATE;
/*     */       }
/*     */     }
/*  80 */     else if (formatter == DateTimeFormatter.ISO_TIME) {
/*  81 */       if (isLocal(fieldType)) {
/*  82 */         formatter = DateTimeFormatter.ISO_LOCAL_TIME;
/*     */       }
/*     */     }
/*  85 */     else if (formatter == DateTimeFormatter.ISO_DATE_TIME && 
/*  86 */       isLocal(fieldType)) {
/*  87 */       formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
/*     */     } 
/*     */ 
/*     */     
/*  91 */     return new TemporalAccessorPrinter(formatter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
/*  97 */     DateTimeFormatter formatter = getFormatter(annotation, fieldType);
/*  98 */     return new TemporalAccessorParser((Class)fieldType, formatter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DateTimeFormatter getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
/* 108 */     DateTimeFormatterFactory factory = new DateTimeFormatterFactory();
/* 109 */     factory.setStylePattern(resolveEmbeddedValue(annotation.style()));
/* 110 */     factory.setIso(annotation.iso());
/* 111 */     factory.setPattern(resolveEmbeddedValue(annotation.pattern()));
/* 112 */     return factory.createDateTimeFormatter();
/*     */   }
/*     */   
/*     */   private boolean isLocal(Class<?> fieldType) {
/* 116 */     return fieldType.getSimpleName().startsWith("Local");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\standard\Jsr310DateTimeFormatAnnotationFormatterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */