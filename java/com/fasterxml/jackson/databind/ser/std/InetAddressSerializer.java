/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonFormat;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
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
/*    */ public class InetAddressSerializer
/*    */   extends StdScalarSerializer<InetAddress>
/*    */   implements ContextualSerializer
/*    */ {
/*    */   protected final boolean _asNumeric;
/*    */   
/*    */   public InetAddressSerializer() {
/* 36 */     this(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InetAddressSerializer(boolean asNumeric) {
/* 43 */     super(InetAddress.class);
/* 44 */     this._asNumeric = asNumeric;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
/* 51 */     JsonFormat.Value format = findFormatOverrides(serializers, property, 
/* 52 */         handledType());
/* 53 */     boolean asNumeric = false;
/* 54 */     if (format != null) {
/* 55 */       JsonFormat.Shape shape = format.getShape();
/* 56 */       if (shape.isNumeric() || shape == JsonFormat.Shape.ARRAY) {
/* 57 */         asNumeric = true;
/*    */       }
/*    */     } 
/* 60 */     if (asNumeric != this._asNumeric) {
/* 61 */       return new InetAddressSerializer(asNumeric);
/*    */     }
/* 63 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(InetAddress value, JsonGenerator g, SerializerProvider provider) throws IOException {
/*    */     String str;
/* 71 */     if (this._asNumeric) {
/* 72 */       str = value.getHostAddress();
/*    */     } else {
/*    */       
/* 75 */       str = value.toString().trim();
/* 76 */       int ix = str.indexOf('/');
/* 77 */       if (ix >= 0) {
/* 78 */         if (ix == 0) {
/* 79 */           str = str.substring(1);
/*    */         } else {
/* 81 */           str = str.substring(0, ix);
/*    */         } 
/*    */       }
/*    */     } 
/* 85 */     g.writeString(str);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeWithType(InetAddress value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 93 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 94 */         .typeId(value, InetAddress.class, JsonToken.VALUE_STRING));
/* 95 */     serialize(value, g, provider);
/* 96 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\InetAddressSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */