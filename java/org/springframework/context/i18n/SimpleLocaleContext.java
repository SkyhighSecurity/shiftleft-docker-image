/*    */ package org.springframework.context.i18n;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ public class SimpleLocaleContext
/*    */   implements LocaleContext
/*    */ {
/*    */   private final Locale locale;
/*    */   
/*    */   public SimpleLocaleContext(Locale locale) {
/* 42 */     this.locale = locale;
/*    */   }
/*    */ 
/*    */   
/*    */   public Locale getLocale() {
/* 47 */     return this.locale;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return (this.locale != null) ? this.locale.toString() : "-";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\i18n\SimpleLocaleContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */