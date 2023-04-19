/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.time.Instant;
/*    */ import java.time.format.DateTimeFormatter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @UsesJava8
/*    */ public class InstantFormatter
/*    */   implements Formatter<Instant>
/*    */ {
/*    */   public Instant parse(String text, Locale locale) throws ParseException {
/* 45 */     if (text.length() > 0 && Character.isDigit(text.charAt(0)))
/*    */     {
/* 47 */       return Instant.parse(text);
/*    */     }
/*    */ 
/*    */     
/* 51 */     return Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(text));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String print(Instant object, Locale locale) {
/* 57 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\standard\InstantFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */