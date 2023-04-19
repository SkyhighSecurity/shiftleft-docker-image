/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.time.Duration;
/*    */ import java.util.Locale;
/*    */ import org.springframework.format.Formatter;
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
/*    */ @UsesJava8
/*    */ class DurationFormatter
/*    */   implements Formatter<Duration>
/*    */ {
/*    */   public Duration parse(String text, Locale locale) throws ParseException {
/* 39 */     return Duration.parse(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public String print(Duration object, Locale locale) {
/* 44 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\standard\DurationFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */