/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.joda.time.format.DateTimeFormatter;
/*    */ import org.springframework.core.NamedThreadLocal;
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
/*    */ public final class JodaTimeContextHolder
/*    */ {
/* 36 */   private static final ThreadLocal<JodaTimeContext> jodaTimeContextHolder = (ThreadLocal<JodaTimeContext>)new NamedThreadLocal("JodaTimeContext");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void resetJodaTimeContext() {
/* 44 */     jodaTimeContextHolder.remove();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setJodaTimeContext(JodaTimeContext jodaTimeContext) {
/* 53 */     if (jodaTimeContext == null) {
/* 54 */       resetJodaTimeContext();
/*    */     } else {
/*    */       
/* 57 */       jodaTimeContextHolder.set(jodaTimeContext);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static JodaTimeContext getJodaTimeContext() {
/* 66 */     return jodaTimeContextHolder.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DateTimeFormatter getFormatter(DateTimeFormatter formatter, Locale locale) {
/* 78 */     DateTimeFormatter formatterToUse = (locale != null) ? formatter.withLocale(locale) : formatter;
/* 79 */     JodaTimeContext context = getJodaTimeContext();
/* 80 */     return (context != null) ? context.getFormatter(formatterToUse) : formatterToUse;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\joda\JodaTimeContextHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */