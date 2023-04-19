/*    */ package org.springframework.format.datetime;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.Calendar;
/*    */ import java.util.Collections;
/*    */ import java.util.Date;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.context.support.EmbeddedValueResolutionSupport;
/*    */ import org.springframework.format.AnnotationFormatterFactory;
/*    */ import org.springframework.format.Formatter;
/*    */ import org.springframework.format.Parser;
/*    */ import org.springframework.format.Printer;
/*    */ import org.springframework.format.annotation.DateTimeFormat;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateTimeFormatAnnotationFormatterFactory
/*    */   extends EmbeddedValueResolutionSupport
/*    */   implements AnnotationFormatterFactory<DateTimeFormat>
/*    */ {
/*    */   private static final Set<Class<?>> FIELD_TYPES;
/*    */   
/*    */   static {
/* 45 */     Set<Class<?>> fieldTypes = new HashSet<Class<?>>(4);
/* 46 */     fieldTypes.add(Date.class);
/* 47 */     fieldTypes.add(Calendar.class);
/* 48 */     fieldTypes.add(Long.class);
/* 49 */     FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<Class<?>> getFieldTypes() {
/* 55 */     return FIELD_TYPES;
/*    */   }
/*    */ 
/*    */   
/*    */   public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
/* 60 */     return (Printer<?>)getFormatter(annotation, fieldType);
/*    */   }
/*    */ 
/*    */   
/*    */   public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
/* 65 */     return (Parser<?>)getFormatter(annotation, fieldType);
/*    */   }
/*    */   
/*    */   protected Formatter<Date> getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
/* 69 */     DateFormatter formatter = new DateFormatter();
/* 70 */     formatter.setStylePattern(resolveEmbeddedValue(annotation.style()));
/* 71 */     formatter.setIso(annotation.iso());
/* 72 */     formatter.setPattern(resolveEmbeddedValue(annotation.pattern()));
/* 73 */     return formatter;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\DateTimeFormatAnnotationFormatterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */