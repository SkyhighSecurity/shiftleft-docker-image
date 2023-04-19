/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.util.TimeZone;
/*     */ import org.joda.time.Chronology;
/*     */ import org.joda.time.DateTimeZone;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
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
/*     */ public class JodaTimeContext
/*     */ {
/*     */   private Chronology chronology;
/*     */   private DateTimeZone timeZone;
/*     */   
/*     */   public void setChronology(Chronology chronology) {
/*  50 */     this.chronology = chronology;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Chronology getChronology() {
/*  57 */     return this.chronology;
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
/*     */   public void setTimeZone(DateTimeZone timeZone) {
/*  69 */     this.timeZone = timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DateTimeZone getTimeZone() {
/*  76 */     return this.timeZone;
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
/*     */   public DateTimeFormatter getFormatter(DateTimeFormatter formatter) {
/*  88 */     if (this.chronology != null) {
/*  89 */       formatter = formatter.withChronology(this.chronology);
/*     */     }
/*  91 */     if (this.timeZone != null) {
/*  92 */       formatter = formatter.withZone(this.timeZone);
/*     */     } else {
/*     */       
/*  95 */       LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
/*  96 */       if (localeContext instanceof TimeZoneAwareLocaleContext) {
/*  97 */         TimeZone timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/*  98 */         if (timeZone != null) {
/*  99 */           formatter = formatter.withZone(DateTimeZone.forTimeZone(timeZone));
/*     */         }
/*     */       } 
/*     */     } 
/* 103 */     return formatter;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\joda\JodaTimeContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */