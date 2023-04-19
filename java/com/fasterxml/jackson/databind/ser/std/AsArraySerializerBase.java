/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AsArraySerializerBase<T>
/*     */   extends ContainerSerializer<T>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final JavaType _elementType;
/*     */   protected final BeanProperty _property;
/*     */   protected final boolean _staticTyping;
/*     */   protected final Boolean _unwrapSingle;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   protected final JsonSerializer<Object> _elementSerializer;
/*     */   protected PropertySerializerMap _dynamicSerializers;
/*     */   
/*     */   protected AsArraySerializerBase(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> elementSerializer) {
/*  79 */     super(cls, false);
/*  80 */     this._elementType = et;
/*     */     
/*  82 */     this._staticTyping = (staticTyping || (et != null && et.isFinal()));
/*  83 */     this._valueTypeSerializer = vts;
/*  84 */     this._property = null;
/*  85 */     this._elementSerializer = elementSerializer;
/*  86 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*  87 */     this._unwrapSingle = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected AsArraySerializerBase(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> elementSerializer) {
/*  99 */     super(cls, false);
/* 100 */     this._elementType = et;
/*     */     
/* 102 */     this._staticTyping = (staticTyping || (et != null && et.isFinal()));
/* 103 */     this._valueTypeSerializer = vts;
/* 104 */     this._property = property;
/* 105 */     this._elementSerializer = elementSerializer;
/* 106 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/* 107 */     this._unwrapSingle = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AsArraySerializerBase(AsArraySerializerBase<?> src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle) {
/* 115 */     super(src);
/* 116 */     this._elementType = src._elementType;
/* 117 */     this._staticTyping = src._staticTyping;
/* 118 */     this._valueTypeSerializer = vts;
/* 119 */     this._property = property;
/* 120 */     this._elementSerializer = (JsonSerializer)elementSerializer;
/*     */     
/* 122 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/* 123 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected AsArraySerializerBase(AsArraySerializerBase<?> src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer) {
/* 133 */     this(src, property, vts, elementSerializer, src._unwrapSingle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final AsArraySerializerBase<T> withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer) {
/* 142 */     return withResolved(property, vts, elementSerializer, this._unwrapSingle);
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
/*     */   public abstract AsArraySerializerBase<T> withResolved(BeanProperty paramBeanProperty, TypeSerializer paramTypeSerializer, JsonSerializer<?> paramJsonSerializer, Boolean paramBoolean);
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
/* 169 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 170 */     if (typeSer != null) {
/* 171 */       typeSer = typeSer.forProperty(property);
/*     */     }
/* 173 */     JsonSerializer<?> ser = null;
/* 174 */     Boolean unwrapSingle = null;
/*     */ 
/*     */     
/* 177 */     if (property != null) {
/* 178 */       AnnotationIntrospector intr = serializers.getAnnotationIntrospector();
/* 179 */       AnnotatedMember m = property.getMember();
/* 180 */       if (m != null) {
/* 181 */         Object serDef = intr.findContentSerializer((Annotated)m);
/* 182 */         if (serDef != null) {
/* 183 */           ser = serializers.serializerInstance((Annotated)m, serDef);
/*     */         }
/*     */       } 
/*     */     } 
/* 187 */     JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
/* 188 */     if (format != null) {
/* 189 */       unwrapSingle = format.getFeature(JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
/*     */     }
/* 191 */     if (ser == null) {
/* 192 */       ser = this._elementSerializer;
/*     */     }
/*     */     
/* 195 */     ser = findContextualConvertingSerializer(serializers, property, ser);
/* 196 */     if (ser == null)
/*     */     {
/*     */       
/* 199 */       if (this._elementType != null && 
/* 200 */         this._staticTyping && !this._elementType.isJavaLangObject()) {
/* 201 */         ser = serializers.findValueSerializer(this._elementType, property);
/*     */       }
/*     */     }
/*     */     
/* 205 */     if (ser != this._elementSerializer || property != this._property || this._valueTypeSerializer != typeSer || this._unwrapSingle != unwrapSingle)
/*     */     {
/*     */ 
/*     */       
/* 209 */       return (JsonSerializer<?>)withResolved(property, typeSer, ser, unwrapSingle);
/*     */     }
/* 211 */     return (JsonSerializer<?>)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType getContentType() {
/* 222 */     return this._elementType;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer() {
/* 227 */     return this._elementSerializer;
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
/*     */   public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 242 */     if (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) && 
/* 243 */       hasSingleElement(value)) {
/* 244 */       serializeContents(value, gen, provider);
/*     */       return;
/*     */     } 
/* 247 */     gen.writeStartArray();
/*     */     
/* 249 */     gen.setCurrentValue(value);
/* 250 */     serializeContents(value, gen, provider);
/* 251 */     gen.writeEndArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(T value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 259 */     g.setCurrentValue(value);
/* 260 */     WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 261 */         .typeId(value, JsonToken.START_ARRAY));
/* 262 */     serializeContents(value, g, provider);
/* 263 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void serializeContents(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider) throws IOException;
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
/* 274 */     ObjectNode o = createSchemaNode("array", true);
/* 275 */     if (this._elementSerializer != null) {
/* 276 */       JsonNode schemaNode = null;
/* 277 */       if (this._elementSerializer instanceof SchemaAware) {
/* 278 */         schemaNode = ((SchemaAware)this._elementSerializer).getSchema(provider, null);
/*     */       }
/* 280 */       if (schemaNode == null) {
/* 281 */         schemaNode = JsonSchema.getDefaultSchemaNode();
/*     */       }
/* 283 */       o.set("items", schemaNode);
/*     */     } 
/* 285 */     return (JsonNode)o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 292 */     JsonSerializer<?> valueSer = this._elementSerializer;
/* 293 */     if (valueSer == null)
/*     */     {
/*     */       
/* 296 */       if (this._elementType != null) {
/* 297 */         valueSer = visitor.getProvider().findValueSerializer(this._elementType, this._property);
/*     */       }
/*     */     }
/* 300 */     visitArrayFormat(visitor, typeHint, valueSer, this._elementType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
/* 306 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 308 */     if (map != result.map) {
/* 309 */       this._dynamicSerializers = result.map;
/*     */     }
/* 311 */     return result.serializer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider) throws JsonMappingException {
/* 317 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 318 */     if (map != result.map) {
/* 319 */       this._dynamicSerializers = result.map;
/*     */     }
/* 321 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\AsArraySerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */