/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import java.io.IOException;
/*    */ import java.text.DateFormat;
/*    */ import java.util.Calendar;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class CalendarSerializer
/*    */   extends DateTimeSerializerBase<Calendar>
/*    */ {
/* 21 */   public static final CalendarSerializer instance = new CalendarSerializer();
/*    */   public CalendarSerializer() {
/* 23 */     this((Boolean)null, (DateFormat)null);
/*    */   }
/*    */   public CalendarSerializer(Boolean useTimestamp, DateFormat customFormat) {
/* 26 */     super(Calendar.class, useTimestamp, customFormat);
/*    */   }
/*    */ 
/*    */   
/*    */   public CalendarSerializer withFormat(Boolean timestamp, DateFormat customFormat) {
/* 31 */     return new CalendarSerializer(timestamp, customFormat);
/*    */   }
/*    */ 
/*    */   
/*    */   protected long _timestamp(Calendar value) {
/* 36 */     return (value == null) ? 0L : value.getTimeInMillis();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(Calendar value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 42 */     if (_asTimestamp(provider)) {
/* 43 */       g.writeNumber(_timestamp(value));
/*    */       return;
/*    */     } 
/* 46 */     _serializeAsString(value.getTime(), g, provider);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\CalendarSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */