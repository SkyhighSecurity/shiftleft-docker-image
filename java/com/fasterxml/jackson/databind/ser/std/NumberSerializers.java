/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
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
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ public class NumberSerializers
/*     */ {
/*     */   public static void addAll(Map<String, JsonSerializer<?>> allDeserializers) {
/*  27 */     allDeserializers.put(Integer.class.getName(), new IntegerSerializer(Integer.class));
/*  28 */     allDeserializers.put(int.class.getName(), new IntegerSerializer(int.class));
/*  29 */     allDeserializers.put(Long.class.getName(), new LongSerializer(Long.class));
/*  30 */     allDeserializers.put(long.class.getName(), new LongSerializer(long.class));
/*     */     
/*  32 */     allDeserializers.put(Byte.class.getName(), IntLikeSerializer.instance);
/*  33 */     allDeserializers.put(byte.class.getName(), IntLikeSerializer.instance);
/*  34 */     allDeserializers.put(Short.class.getName(), ShortSerializer.instance);
/*  35 */     allDeserializers.put(short.class.getName(), ShortSerializer.instance);
/*     */ 
/*     */     
/*  38 */     allDeserializers.put(Double.class.getName(), new DoubleSerializer(Double.class));
/*  39 */     allDeserializers.put(double.class.getName(), new DoubleSerializer(double.class));
/*  40 */     allDeserializers.put(Float.class.getName(), FloatSerializer.instance);
/*  41 */     allDeserializers.put(float.class.getName(), FloatSerializer.instance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Base<T>
/*     */     extends StdScalarSerializer<T>
/*     */     implements ContextualSerializer
/*     */   {
/*     */     protected final JsonParser.NumberType _numberType;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final String _schemaType;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final boolean _isInt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Base(Class<?> cls, JsonParser.NumberType numberType, String schemaType) {
/*  69 */       super(cls, false);
/*  70 */       this._numberType = numberType;
/*  71 */       this._schemaType = schemaType;
/*  72 */       this._isInt = (numberType == JsonParser.NumberType.INT || numberType == JsonParser.NumberType.LONG || numberType == JsonParser.NumberType.BIG_INTEGER);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/*  79 */       return (JsonNode)createSchemaNode(this._schemaType, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/*  86 */       if (this._isInt) {
/*  87 */         visitIntFormat(visitor, typeHint, this._numberType);
/*     */       } else {
/*  89 */         visitFloatFormat(visitor, typeHint, this._numberType);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
/*  97 */       JsonFormat.Value format = findFormatOverrides(prov, property, handledType());
/*  98 */       if (format != null) {
/*  99 */         switch (format.getShape()) {
/*     */           case STRING:
/* 101 */             if (handledType() == BigDecimal.class) {
/* 102 */               return NumberSerializer.bigDecimalAsStringSerializer();
/*     */             }
/* 104 */             return ToStringSerializer.instance;
/*     */         } 
/*     */       
/*     */       }
/* 108 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class ShortSerializer
/*     */     extends Base<Object>
/*     */   {
/* 120 */     static final ShortSerializer instance = new ShortSerializer();
/*     */     
/*     */     public ShortSerializer() {
/* 123 */       super(Short.class, JsonParser.NumberType.INT, "number");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 129 */       gen.writeNumber(((Short)value).shortValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class IntegerSerializer
/*     */     extends Base<Object>
/*     */   {
/*     */     public IntegerSerializer(Class<?> type) {
/* 146 */       super(type, JsonParser.NumberType.INT, "integer");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 152 */       gen.writeNumber(((Integer)value).intValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 161 */       serialize(value, gen, provider);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class IntLikeSerializer
/*     */     extends Base<Object>
/*     */   {
/* 172 */     static final IntLikeSerializer instance = new IntLikeSerializer();
/*     */     
/*     */     public IntLikeSerializer() {
/* 175 */       super(Number.class, JsonParser.NumberType.INT, "integer");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 181 */       gen.writeNumber(((Number)value).intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class LongSerializer extends Base<Object> {
/*     */     public LongSerializer(Class<?> cls) {
/* 188 */       super(cls, JsonParser.NumberType.LONG, "number");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 194 */       gen.writeNumber(((Long)value).longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class FloatSerializer extends Base<Object> {
/* 200 */     static final FloatSerializer instance = new FloatSerializer();
/*     */     
/*     */     public FloatSerializer() {
/* 203 */       super(Float.class, JsonParser.NumberType.FLOAT, "number");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 209 */       gen.writeNumber(((Float)value).floatValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class DoubleSerializer
/*     */     extends Base<Object>
/*     */   {
/*     */     public DoubleSerializer(Class<?> cls) {
/* 223 */       super(cls, JsonParser.NumberType.DOUBLE, "number");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 229 */       gen.writeNumber(((Double)value).doubleValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeWithType(Object value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 239 */       Double d = (Double)value;
/* 240 */       if (notFinite(d.doubleValue())) {
/* 241 */         WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/*     */             
/* 243 */             .typeId(value, JsonToken.VALUE_NUMBER_FLOAT));
/* 244 */         g.writeNumber(d.doubleValue());
/* 245 */         typeSer.writeTypeSuffix(g, typeIdDef);
/*     */       } else {
/* 247 */         g.writeNumber(d.doubleValue());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public static boolean notFinite(double value) {
/* 253 */       return (Double.isNaN(value) || Double.isInfinite(value));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\NumberSerializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */