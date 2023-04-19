/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.time.LocalDate;
/*    */ import java.time.LocalDateTime;
/*    */ import java.time.LocalTime;
/*    */ import java.time.OffsetDateTime;
/*    */ import java.time.OffsetTime;
/*    */ import java.time.ZonedDateTime;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.temporal.TemporalAccessor;
/*    */ import java.util.Locale;
/*    */ import org.springframework.format.Parser;
/*    */ import org.springframework.lang.UsesJava8;
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
/*    */ @UsesJava8
/*    */ public final class TemporalAccessorParser
/*    */   implements Parser<TemporalAccessor>
/*    */ {
/*    */   private final Class<? extends TemporalAccessor> temporalAccessorType;
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public TemporalAccessorParser(Class<? extends TemporalAccessor> temporalAccessorType, DateTimeFormatter formatter) {
/* 62 */     this.temporalAccessorType = temporalAccessorType;
/* 63 */     this.formatter = formatter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TemporalAccessor parse(String text, Locale locale) throws ParseException {
/* 69 */     DateTimeFormatter formatterToUse = DateTimeContextHolder.getFormatter(this.formatter, locale);
/* 70 */     if (LocalDate.class == this.temporalAccessorType) {
/* 71 */       return LocalDate.parse(text, formatterToUse);
/*    */     }
/* 73 */     if (LocalTime.class == this.temporalAccessorType) {
/* 74 */       return LocalTime.parse(text, formatterToUse);
/*    */     }
/* 76 */     if (LocalDateTime.class == this.temporalAccessorType) {
/* 77 */       return LocalDateTime.parse(text, formatterToUse);
/*    */     }
/* 79 */     if (ZonedDateTime.class == this.temporalAccessorType) {
/* 80 */       return ZonedDateTime.parse(text, formatterToUse);
/*    */     }
/* 82 */     if (OffsetDateTime.class == this.temporalAccessorType) {
/* 83 */       return OffsetDateTime.parse(text, formatterToUse);
/*    */     }
/* 85 */     if (OffsetTime.class == this.temporalAccessorType) {
/* 86 */       return OffsetTime.parse(text, formatterToUse);
/*    */     }
/*    */     
/* 89 */     throw new IllegalStateException("Unsupported TemporalAccessor type: " + this.temporalAccessorType);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\standard\TemporalAccessorParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */