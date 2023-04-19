/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import java.io.IOException;
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
/*     */ @JacksonStdImpl
/*     */ public class ObjectArraySerializer
/*     */   extends ArraySerializerBase<Object[]>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final boolean _staticTyping;
/*     */   protected final JavaType _elementType;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   protected JsonSerializer<Object> _elementSerializer;
/*     */   protected PropertySerializerMap _dynamicSerializers;
/*     */   
/*     */   public ObjectArraySerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> elementSerializer) {
/*  64 */     super(Object[].class);
/*  65 */     this._elementType = elemType;
/*  66 */     this._staticTyping = staticTyping;
/*  67 */     this._valueTypeSerializer = vts;
/*  68 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*  69 */     this._elementSerializer = elementSerializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectArraySerializer(ObjectArraySerializer src, TypeSerializer vts) {
/*  74 */     super(src);
/*  75 */     this._elementType = src._elementType;
/*  76 */     this._valueTypeSerializer = vts;
/*  77 */     this._staticTyping = src._staticTyping;
/*     */ 
/*     */     
/*  80 */     this._dynamicSerializers = src._dynamicSerializers;
/*  81 */     this._elementSerializer = src._elementSerializer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArraySerializer(ObjectArraySerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle) {
/*  89 */     super(src, property, unwrapSingle);
/*  90 */     this._elementType = src._elementType;
/*  91 */     this._valueTypeSerializer = vts;
/*  92 */     this._staticTyping = src._staticTyping;
/*     */     
/*  94 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*  95 */     this._elementSerializer = (JsonSerializer)elementSerializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
/* 100 */     return (JsonSerializer<?>)new ObjectArraySerializer(this, prop, this._valueTypeSerializer, this._elementSerializer, unwrapSingle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
/* 106 */     return new ObjectArraySerializer(this._elementType, this._staticTyping, vts, this._elementSerializer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectArraySerializer withResolved(BeanProperty prop, TypeSerializer vts, JsonSerializer<?> ser, Boolean unwrapSingle) {
/* 111 */     if (this._property == prop && ser == this._elementSerializer && this._valueTypeSerializer == vts && this._unwrapSingle == unwrapSingle)
/*     */     {
/* 113 */       return this;
/*     */     }
/* 115 */     return new ObjectArraySerializer(this, prop, vts, ser, unwrapSingle);
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
/* 129 */     TypeSerializer vts = this._valueTypeSerializer;
/* 130 */     if (vts != null) {
/* 131 */       vts = vts.forProperty(property);
/*     */     }
/* 133 */     JsonSerializer<?> ser = null;
/* 134 */     Boolean unwrapSingle = null;
/*     */ 
/*     */     
/* 137 */     if (property != null) {
/* 138 */       AnnotatedMember m = property.getMember();
/* 139 */       AnnotationIntrospector intr = serializers.getAnnotationIntrospector();
/* 140 */       if (m != null) {
/* 141 */         Object serDef = intr.findContentSerializer((Annotated)m);
/* 142 */         if (serDef != null) {
/* 143 */           ser = serializers.serializerInstance((Annotated)m, serDef);
/*     */         }
/*     */       } 
/*     */     } 
/* 147 */     JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
/* 148 */     if (format != null) {
/* 149 */       unwrapSingle = format.getFeature(JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
/*     */     }
/* 151 */     if (ser == null) {
/* 152 */       ser = this._elementSerializer;
/*     */     }
/*     */     
/* 155 */     ser = findContextualConvertingSerializer(serializers, property, ser);
/* 156 */     if (ser == null)
/*     */     {
/*     */       
/* 159 */       if (this._elementType != null && 
/* 160 */         this._staticTyping && !this._elementType.isJavaLangObject()) {
/* 161 */         ser = serializers.findValueSerializer(this._elementType, property);
/*     */       }
/*     */     }
/*     */     
/* 165 */     return (JsonSerializer<?>)withResolved(property, vts, ser, unwrapSingle);
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
/* 176 */     return this._elementType;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer() {
/* 181 */     return this._elementSerializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, Object[] value) {
/* 186 */     return (value.length == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSingleElement(Object[] value) {
/* 191 */     return (value.length == 1);
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
/*     */   public final void serialize(Object[] value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 203 */     int len = value.length;
/* 204 */     if (len == 1 && ((
/* 205 */       this._unwrapSingle == null && provider
/* 206 */       .isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) || this._unwrapSingle == Boolean.TRUE)) {
/*     */       
/* 208 */       serializeContents(value, gen, provider);
/*     */       
/*     */       return;
/*     */     } 
/* 212 */     gen.writeStartArray(len);
/* 213 */     serializeContents(value, gen, provider);
/* 214 */     gen.writeEndArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeContents(Object[] value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 220 */     int len = value.length;
/* 221 */     if (len == 0) {
/*     */       return;
/*     */     }
/* 224 */     if (this._elementSerializer != null) {
/* 225 */       serializeContentsUsing(value, gen, provider, this._elementSerializer);
/*     */       return;
/*     */     } 
/* 228 */     if (this._valueTypeSerializer != null) {
/* 229 */       serializeTypedContents(value, gen, provider);
/*     */       return;
/*     */     } 
/* 232 */     int i = 0;
/* 233 */     Object elem = null;
/*     */     try {
/* 235 */       PropertySerializerMap serializers = this._dynamicSerializers;
/* 236 */       for (; i < len; i++) {
/* 237 */         elem = value[i];
/* 238 */         if (elem == null)
/* 239 */         { provider.defaultSerializeNull(gen); }
/*     */         else
/*     */         
/* 242 */         { Class<?> cc = elem.getClass();
/* 243 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 244 */           if (serializer == null) {
/* 245 */             if (this._elementType.hasGenericTypes()) {
/* 246 */               serializer = _findAndAddDynamic(serializers, provider
/* 247 */                   .constructSpecializedType(this._elementType, cc), provider);
/*     */             } else {
/* 249 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             } 
/*     */           }
/* 252 */           serializer.serialize(elem, gen, provider); } 
/*     */       } 
/* 254 */     } catch (Exception e) {
/* 255 */       wrapAndThrow(provider, e, elem, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeContentsUsing(Object[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException {
/* 262 */     int len = value.length;
/* 263 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*     */     
/* 265 */     int i = 0;
/* 266 */     Object elem = null;
/*     */     try {
/* 268 */       for (; i < len; i++) {
/* 269 */         elem = value[i];
/* 270 */         if (elem == null) {
/* 271 */           provider.defaultSerializeNull(jgen);
/*     */         
/*     */         }
/* 274 */         else if (typeSer == null) {
/* 275 */           ser.serialize(elem, jgen, provider);
/*     */         } else {
/* 277 */           ser.serializeWithType(elem, jgen, provider, typeSer);
/*     */         } 
/*     */       } 
/* 280 */     } catch (Exception e) {
/* 281 */       wrapAndThrow(provider, e, elem, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void serializeTypedContents(Object[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
/* 287 */     int len = value.length;
/* 288 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 289 */     int i = 0;
/* 290 */     Object elem = null;
/*     */     try {
/* 292 */       PropertySerializerMap serializers = this._dynamicSerializers;
/* 293 */       for (; i < len; i++) {
/* 294 */         elem = value[i];
/* 295 */         if (elem == null)
/* 296 */         { provider.defaultSerializeNull(jgen); }
/*     */         else
/*     */         
/* 299 */         { Class<?> cc = elem.getClass();
/* 300 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 301 */           if (serializer == null) {
/* 302 */             serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */           }
/* 304 */           serializer.serializeWithType(elem, jgen, provider, typeSer); } 
/*     */       } 
/* 306 */     } catch (Exception e) {
/* 307 */       wrapAndThrow(provider, e, elem, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 315 */     JsonArrayFormatVisitor arrayVisitor = visitor.expectArrayFormat(typeHint);
/* 316 */     if (arrayVisitor != null) {
/* 317 */       JavaType contentType = this._elementType;
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
/* 328 */       JsonSerializer<?> valueSer = this._elementSerializer;
/* 329 */       if (valueSer == null) {
/* 330 */         valueSer = visitor.getProvider().findValueSerializer(contentType, this._property);
/*     */       }
/* 332 */       arrayVisitor.itemsFormat((JsonFormatVisitable)valueSer, contentType);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
/* 339 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 341 */     if (map != result.map) {
/* 342 */       this._dynamicSerializers = result.map;
/*     */     }
/* 344 */     return result.serializer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider) throws JsonMappingException {
/* 350 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 352 */     if (map != result.map) {
/* 353 */       this._dynamicSerializers = result.map;
/*     */     }
/* 355 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\ObjectArraySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */