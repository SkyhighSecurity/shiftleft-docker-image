/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import java.io.IOException;
/*    */ import java.sql.Date;
/*    */ import java.text.DateFormat;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class SqlDateSerializer
/*    */   extends DateTimeSerializerBase<Date>
/*    */ {
/*    */   public SqlDateSerializer() {
/* 23 */     this((Boolean)null, (DateFormat)null);
/*    */   }
/*    */   
/*    */   protected SqlDateSerializer(Boolean useTimestamp, DateFormat customFormat) {
/* 27 */     super(Date.class, useTimestamp, customFormat);
/*    */   }
/*    */ 
/*    */   
/*    */   public SqlDateSerializer withFormat(Boolean timestamp, DateFormat customFormat) {
/* 32 */     return new SqlDateSerializer(timestamp, customFormat);
/*    */   }
/*    */ 
/*    */   
/*    */   protected long _timestamp(Date value) {
/* 37 */     return (value == null) ? 0L : value.getTime();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 44 */     if (_asTimestamp(provider)) {
/* 45 */       g.writeNumber(_timestamp(value));
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     if (this._customFormat == null) {
/*    */ 
/*    */ 
/*    */       
/* 53 */       g.writeString(value.toString());
/*    */       return;
/*    */     } 
/* 56 */     _serializeAsString(value, g, provider);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\SqlDateSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */