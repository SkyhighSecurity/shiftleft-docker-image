/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.util.EnumValues;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class EnumSerializer
/*     */   extends StdScalarSerializer<Enum<?>>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final EnumValues _values;
/*     */   protected final Boolean _serializeAsIndex;
/*     */   
/*     */   public EnumSerializer(EnumValues v, Boolean serializeAsIndex) {
/*  58 */     super(v.getEnumClass(), false);
/*  59 */     this._values = v;
/*  60 */     this._serializeAsIndex = serializeAsIndex;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EnumSerializer construct(Class<?> enumClass, SerializationConfig config, BeanDescription beanDesc, JsonFormat.Value format) {
/*  77 */     EnumValues v = EnumValues.constructFromName((MapperConfig)config, enumClass);
/*  78 */     Boolean serializeAsIndex = _isShapeWrittenUsingIndex(enumClass, format, true, (Boolean)null);
/*  79 */     return new EnumSerializer(v, serializeAsIndex);
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
/*  91 */     JsonFormat.Value format = findFormatOverrides(serializers, property, 
/*  92 */         handledType());
/*  93 */     if (format != null) {
/*  94 */       Class<?> type = handledType();
/*  95 */       Boolean serializeAsIndex = _isShapeWrittenUsingIndex(type, format, false, this._serializeAsIndex);
/*     */       
/*  97 */       if (serializeAsIndex != this._serializeAsIndex) {
/*  98 */         return new EnumSerializer(this._values, serializeAsIndex);
/*     */       }
/*     */     } 
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumValues getEnumValues() {
/* 110 */     return this._values;
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
/*     */   public final void serialize(Enum<?> en, JsonGenerator gen, SerializerProvider serializers) throws IOException {
/* 122 */     if (_serializeAsIndex(serializers)) {
/* 123 */       gen.writeNumber(en.ordinal());
/*     */       
/*     */       return;
/*     */     } 
/* 127 */     if (serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
/* 128 */       gen.writeString(en.toString());
/*     */       return;
/*     */     } 
/* 131 */     gen.writeString(this._values.serializedValueFor(en));
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
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 143 */     if (_serializeAsIndex(provider)) {
/* 144 */       return (JsonNode)createSchemaNode("integer", true);
/*     */     }
/* 146 */     ObjectNode objectNode = createSchemaNode("string", true);
/* 147 */     if (typeHint != null) {
/* 148 */       JavaType type = provider.constructType(typeHint);
/* 149 */       if (type.isEnumType()) {
/* 150 */         ArrayNode enumNode = objectNode.putArray("enum");
/* 151 */         for (SerializableString value : this._values.values()) {
/* 152 */           enumNode.add(value.getValue());
/*     */         }
/*     */       } 
/*     */     } 
/* 156 */     return (JsonNode)objectNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 163 */     SerializerProvider serializers = visitor.getProvider();
/* 164 */     if (_serializeAsIndex(serializers)) {
/* 165 */       visitIntFormat(visitor, typeHint, JsonParser.NumberType.INT);
/*     */       return;
/*     */     } 
/* 168 */     JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(typeHint);
/* 169 */     if (stringVisitor != null) {
/* 170 */       Set<String> enums = new LinkedHashSet<>();
/*     */ 
/*     */       
/* 173 */       if (serializers != null && serializers
/* 174 */         .isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
/* 175 */         for (Enum<?> e : (Iterable<Enum<?>>)this._values.enums()) {
/* 176 */           enums.add(e.toString());
/*     */         }
/*     */       } else {
/*     */         
/* 180 */         for (SerializableString value : this._values.values()) {
/* 181 */           enums.add(value.getValue());
/*     */         }
/*     */       } 
/* 184 */       stringVisitor.enumTypes(enums);
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
/*     */   protected final boolean _serializeAsIndex(SerializerProvider serializers) {
/* 196 */     if (this._serializeAsIndex != null) {
/* 197 */       return this._serializeAsIndex.booleanValue();
/*     */     }
/* 199 */     return serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Boolean _isShapeWrittenUsingIndex(Class<?> enumClass, JsonFormat.Value format, boolean fromClass, Boolean defaultValue) {
/* 210 */     JsonFormat.Shape shape = (format == null) ? null : format.getShape();
/* 211 */     if (shape == null) {
/* 212 */       return defaultValue;
/*     */     }
/*     */     
/* 215 */     if (shape == JsonFormat.Shape.ANY || shape == JsonFormat.Shape.SCALAR) {
/* 216 */       return defaultValue;
/*     */     }
/*     */     
/* 219 */     if (shape == JsonFormat.Shape.STRING || shape == JsonFormat.Shape.NATURAL) {
/* 220 */       return Boolean.FALSE;
/*     */     }
/*     */     
/* 223 */     if (shape.isNumeric() || shape == JsonFormat.Shape.ARRAY) {
/* 224 */       return Boolean.TRUE;
/*     */     }
/*     */     
/* 227 */     throw new IllegalArgumentException(String.format("Unsupported serialization shape (%s) for Enum %s, not supported as %s annotation", new Object[] { shape, enumClass
/*     */             
/* 229 */             .getName(), fromClass ? "class" : "property" }));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\EnumSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */