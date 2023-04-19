/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.util.Locale;
/*    */ import org.springframework.core.NamedThreadLocal;
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
/*    */ @UsesJava8
/*    */ public final class DateTimeContextHolder
/*    */ {
/* 35 */   private static final ThreadLocal<DateTimeContext> dateTimeContextHolder = (ThreadLocal<DateTimeContext>)new NamedThreadLocal("DateTimeContext");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void resetDateTimeContext() {
/* 43 */     dateTimeContextHolder.remove();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setDateTimeContext(DateTimeContext dateTimeContext) {
/* 52 */     if (dateTimeContext == null) {
/* 53 */       resetDateTimeContext();
/*    */     } else {
/*    */       
/* 56 */       dateTimeContextHolder.set(dateTimeContext);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DateTimeContext getDateTimeContext() {
/* 65 */     return dateTimeContextHolder.get();
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
/* 77 */     DateTimeFormatter formatterToUse = (locale != null) ? formatter.withLocale(locale) : formatter;
/* 78 */     DateTimeContext context = getDateTimeContext();
/* 79 */     return (context != null) ? context.getFormatter(formatterToUse) : formatterToUse;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\standard\DateTimeContextHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */