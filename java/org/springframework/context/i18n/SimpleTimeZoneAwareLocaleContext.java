/*    */ package org.springframework.context.i18n;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
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
/*    */ public class SimpleTimeZoneAwareLocaleContext
/*    */   extends SimpleLocaleContext
/*    */   implements TimeZoneAwareLocaleContext
/*    */ {
/*    */   private final TimeZone timeZone;
/*    */   
/*    */   public SimpleTimeZoneAwareLocaleContext(Locale locale, TimeZone timeZone) {
/* 48 */     super(locale);
/* 49 */     this.timeZone = timeZone;
/*    */   }
/*    */ 
/*    */   
/*    */   public TimeZone getTimeZone() {
/* 54 */     return this.timeZone;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return super.toString() + " " + ((this.timeZone != null) ? this.timeZone.toString() : "-");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\i18n\SimpleTimeZoneAwareLocaleContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */