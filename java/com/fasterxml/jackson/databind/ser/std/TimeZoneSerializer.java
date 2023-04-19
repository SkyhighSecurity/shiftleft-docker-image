/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.TimeZone;
/*    */ 
/*    */ public class TimeZoneSerializer
/*    */   extends StdScalarSerializer<TimeZone> {
/*    */   public TimeZoneSerializer() {
/* 14 */     super(TimeZone.class);
/*    */   }
/*    */   
/*    */   public void serialize(TimeZone value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 18 */     g.writeString(value.getID());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeWithType(TimeZone value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 26 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 27 */         .typeId(value, TimeZone.class, JsonToken.VALUE_STRING));
/* 28 */     serialize(value, g, provider);
/* 29 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\TimeZoneSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */