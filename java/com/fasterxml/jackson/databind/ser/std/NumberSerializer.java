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
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class NumberSerializer
/*     */   extends StdScalarSerializer<Number>
/*     */   implements ContextualSerializer
/*     */ {
/*  32 */   public static final NumberSerializer instance = new NumberSerializer(Number.class);
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int MAX_BIG_DECIMAL_SCALE = 9999;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean _isInt;
/*     */ 
/*     */ 
/*     */   
/*     */   public NumberSerializer(Class<? extends Number> rawType) {
/*  45 */     super(rawType, false);
/*     */     
/*  47 */     this._isInt = (rawType == BigInteger.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
/*  54 */     JsonFormat.Value format = findFormatOverrides(prov, property, handledType());
/*  55 */     if (format != null) {
/*  56 */       switch (format.getShape()) {
/*     */         
/*     */         case STRING:
/*  59 */           if (handledType() == BigDecimal.class) {
/*  60 */             return bigDecimalAsStringSerializer();
/*     */           }
/*  62 */           return ToStringSerializer.instance;
/*     */       } 
/*     */     
/*     */     }
/*  66 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serialize(Number value, JsonGenerator g, SerializerProvider provider) throws IOException {
/*  73 */     if (value instanceof BigDecimal) {
/*  74 */       g.writeNumber((BigDecimal)value);
/*  75 */     } else if (value instanceof BigInteger) {
/*  76 */       g.writeNumber((BigInteger)value);
/*     */ 
/*     */     
/*     */     }
/*  80 */     else if (value instanceof Long) {
/*  81 */       g.writeNumber(value.longValue());
/*  82 */     } else if (value instanceof Double) {
/*  83 */       g.writeNumber(value.doubleValue());
/*  84 */     } else if (value instanceof Float) {
/*  85 */       g.writeNumber(value.floatValue());
/*  86 */     } else if (value instanceof Integer || value instanceof Byte || value instanceof Short) {
/*  87 */       g.writeNumber(value.intValue());
/*     */     } else {
/*     */       
/*  90 */       g.writeNumber(value.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/*  96 */     return (JsonNode)createSchemaNode(this._isInt ? "integer" : "number", true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 102 */     if (this._isInt) {
/* 103 */       visitIntFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER);
/*     */     }
/* 105 */     else if (handledType() == BigDecimal.class) {
/* 106 */       visitFloatFormat(visitor, typeHint, JsonParser.NumberType.BIG_DECIMAL);
/*     */     } else {
/*     */       
/* 109 */       visitor.expectNumberFormat(typeHint);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonSerializer<?> bigDecimalAsStringSerializer() {
/* 118 */     return BigDecimalAsStringSerializer.BD_INSTANCE;
/*     */   }
/*     */   
/*     */   static final class BigDecimalAsStringSerializer
/*     */     extends ToStringSerializerBase
/*     */   {
/* 124 */     static final BigDecimalAsStringSerializer BD_INSTANCE = new BigDecimalAsStringSerializer();
/*     */     
/*     */     public BigDecimalAsStringSerializer() {
/* 127 */       super(BigDecimal.class);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, Object value) {
/* 132 */       return valueToString(value).isEmpty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*     */       String text;
/* 140 */       if (gen.isEnabled(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)) {
/* 141 */         BigDecimal bd = (BigDecimal)value;
/*     */         
/* 143 */         if (!_verifyBigDecimalRange(gen, bd)) {
/*     */ 
/*     */           
/* 146 */           String errorMsg = String.format("Attempt to write plain `java.math.BigDecimal` (see JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN) with illegal scale (%d): needs to be between [-%d, %d]", new Object[] {
/*     */                 
/* 148 */                 Integer.valueOf(bd.scale()), Integer.valueOf(9999), Integer.valueOf(9999) });
/* 149 */           provider.reportMappingProblem(errorMsg, new Object[0]);
/*     */         } 
/* 151 */         text = bd.toPlainString();
/*     */       } else {
/* 153 */         text = value.toString();
/*     */       } 
/* 155 */       gen.writeString(text);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String valueToString(Object value) {
/* 161 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean _verifyBigDecimalRange(JsonGenerator gen, BigDecimal value) throws IOException {
/* 166 */       int scale = value.scale();
/* 167 */       return (scale >= -9999 && scale <= 9999);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\NumberSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */