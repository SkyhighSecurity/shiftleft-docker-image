/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.time.ZonedDateTime;
/*    */ import java.util.Calendar;
/*    */ import java.util.GregorianCalendar;
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ final class ZonedDateTimeToCalendarConverter
/*    */   implements Converter<ZonedDateTime, Calendar>
/*    */ {
/*    */   public Calendar convert(ZonedDateTime source) {
/* 44 */     return GregorianCalendar.from(source);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ZonedDateTimeToCalendarConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */