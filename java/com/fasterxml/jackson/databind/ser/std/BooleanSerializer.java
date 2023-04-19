/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class BooleanSerializer
/*     */   extends StdScalarSerializer<Object>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final boolean _forPrimitive;
/*     */   
/*     */   public BooleanSerializer(boolean forPrimitive) {
/*  42 */     super(forPrimitive ? boolean.class : Boolean.class, false);
/*  43 */     this._forPrimitive = forPrimitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
/*  50 */     JsonFormat.Value format = findFormatOverrides(serializers, property, Boolean.class);
/*     */     
/*  52 */     if (format != null) {
/*  53 */       JsonFormat.Shape shape = format.getShape();
/*  54 */       if (shape.isNumeric()) {
/*  55 */         return new AsNumber(this._forPrimitive);
/*     */       }
/*     */     } 
/*  58 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
/*  63 */     g.writeBoolean(Boolean.TRUE.equals(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void serializeWithType(Object value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/*  70 */     g.writeBoolean(Boolean.TRUE.equals(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/*  75 */     return (JsonNode)createSchemaNode("boolean", !this._forPrimitive);
/*     */   }
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/*  80 */     visitor.expectBooleanFormat(typeHint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class AsNumber
/*     */     extends StdScalarSerializer<Object>
/*     */     implements ContextualSerializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final boolean _forPrimitive;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AsNumber(boolean forPrimitive) {
/* 102 */       super(forPrimitive ? boolean.class : Boolean.class, false);
/* 103 */       this._forPrimitive = forPrimitive;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 108 */       g.writeNumber(Boolean.FALSE.equals(value) ? 0 : 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void serializeWithType(Object value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 119 */       g.writeBoolean(Boolean.TRUE.equals(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 126 */       visitIntFormat(visitor, typeHint, JsonParser.NumberType.INT);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
/* 133 */       JsonFormat.Value format = findFormatOverrides(serializers, property, Boolean.class);
/*     */       
/* 135 */       if (format != null) {
/* 136 */         JsonFormat.Shape shape = format.getShape();
/* 137 */         if (!shape.isNumeric()) {
/* 138 */           return new BooleanSerializer(this._forPrimitive);
/*     */         }
/*     */       } 
/* 141 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\BooleanSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */