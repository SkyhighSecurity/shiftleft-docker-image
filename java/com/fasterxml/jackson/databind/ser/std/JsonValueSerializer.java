/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
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
/*     */ public class JsonValueSerializer
/*     */   extends StdSerializer<Object>
/*     */   implements ContextualSerializer, JsonFormatVisitable, SchemaAware
/*     */ {
/*     */   protected final AnnotatedMember _accessor;
/*     */   protected final JsonSerializer<Object> _valueSerializer;
/*     */   protected final BeanProperty _property;
/*     */   protected final boolean _forceTypeInformation;
/*     */   
/*     */   public JsonValueSerializer(AnnotatedMember accessor, JsonSerializer<?> ser) {
/*  78 */     super(accessor.getType());
/*  79 */     this._accessor = accessor;
/*  80 */     this._valueSerializer = (JsonSerializer)ser;
/*  81 */     this._property = null;
/*  82 */     this._forceTypeInformation = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonValueSerializer(JsonValueSerializer src, BeanProperty property, JsonSerializer<?> ser, boolean forceTypeInfo) {
/*  89 */     super(_notNullClass(src.handledType()));
/*  90 */     this._accessor = src._accessor;
/*  91 */     this._valueSerializer = (JsonSerializer)ser;
/*  92 */     this._property = property;
/*  93 */     this._forceTypeInformation = forceTypeInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Class<Object> _notNullClass(Class<?> cls) {
/*  98 */     return (cls == null) ? Object.class : (Class)cls;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonValueSerializer withResolved(BeanProperty property, JsonSerializer<?> ser, boolean forceTypeInfo) {
/* 104 */     if (this._property == property && this._valueSerializer == ser && forceTypeInfo == this._forceTypeInformation)
/*     */     {
/* 106 */       return this;
/*     */     }
/* 108 */     return new JsonValueSerializer(this, property, ser, forceTypeInfo);
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
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/* 126 */     JsonSerializer<?> ser = this._valueSerializer;
/* 127 */     if (ser == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       JavaType t = this._accessor.getType();
/* 133 */       if (provider.isEnabled(MapperFeature.USE_STATIC_TYPING) || t.isFinal()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 140 */         ser = provider.findPrimaryPropertySerializer(t, property);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 145 */         boolean forceTypeInformation = isNaturalTypeWithStdHandling(t.getRawClass(), ser);
/* 146 */         return withResolved(property, ser, forceTypeInformation);
/*     */       } 
/*     */     } else {
/*     */       
/* 150 */       ser = provider.handlePrimaryContextualization(ser, property);
/* 151 */       return withResolved(property, ser, this._forceTypeInformation);
/*     */     } 
/* 153 */     return this;
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
/*     */   public void serialize(Object bean, JsonGenerator gen, SerializerProvider prov) throws IOException {
/*     */     try {
/* 166 */       Object value = this._accessor.getValue(bean);
/* 167 */       if (value == null) {
/* 168 */         prov.defaultSerializeNull(gen);
/*     */         return;
/*     */       } 
/* 171 */       JsonSerializer<Object> ser = this._valueSerializer;
/* 172 */       if (ser == null) {
/* 173 */         Class<?> c = value.getClass();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 179 */         ser = prov.findTypedValueSerializer(c, true, this._property);
/*     */       } 
/* 181 */       ser.serialize(value, gen, prov);
/* 182 */     } catch (Exception e) {
/* 183 */       wrapAndThrow(prov, e, bean, this._accessor.getName() + "()");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer0) throws IOException {
/* 192 */     Object value = null;
/*     */     try {
/* 194 */       value = this._accessor.getValue(bean);
/*     */       
/* 196 */       if (value == null) {
/* 197 */         provider.defaultSerializeNull(gen);
/*     */         return;
/*     */       } 
/* 200 */       JsonSerializer<Object> ser = this._valueSerializer;
/* 201 */       if (ser == null) {
/* 202 */         ser = provider.findValueSerializer(value.getClass(), this._property);
/*     */ 
/*     */       
/*     */       }
/* 206 */       else if (this._forceTypeInformation) {
/*     */         
/* 208 */         WritableTypeId typeIdDef = typeSer0.writeTypePrefix(gen, typeSer0
/* 209 */             .typeId(bean, JsonToken.VALUE_STRING));
/* 210 */         ser.serialize(value, gen, provider);
/* 211 */         typeSer0.writeTypeSuffix(gen, typeIdDef);
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 219 */       TypeSerializerRerouter rr = new TypeSerializerRerouter(typeSer0, bean);
/* 220 */       ser.serializeWithType(value, gen, provider, rr);
/* 221 */     } catch (Exception e) {
/* 222 */       wrapAndThrow(provider, e, bean, this._accessor.getName() + "()");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
/* 231 */     if (this._valueSerializer instanceof SchemaAware) {
/* 232 */       return ((SchemaAware)this._valueSerializer).getSchema(provider, null);
/*     */     }
/* 234 */     return JsonSchema.getDefaultSchemaNode();
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
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 250 */     JavaType type = this._accessor.getType();
/* 251 */     Class<?> declaring = this._accessor.getDeclaringClass();
/* 252 */     if (declaring != null && declaring.isEnum() && 
/* 253 */       _acceptJsonFormatVisitorForEnum(visitor, typeHint, declaring)) {
/*     */       return;
/*     */     }
/*     */     
/* 257 */     JsonSerializer<Object> ser = this._valueSerializer;
/* 258 */     if (ser == null) {
/* 259 */       ser = visitor.getProvider().findTypedValueSerializer(type, false, this._property);
/* 260 */       if (ser == null) {
/* 261 */         visitor.expectAnyFormat(typeHint);
/*     */         return;
/*     */       } 
/*     */     } 
/* 265 */     ser.acceptJsonFormatVisitor(visitor, type);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _acceptJsonFormatVisitorForEnum(JsonFormatVisitorWrapper visitor, JavaType typeHint, Class<?> enumType) throws JsonMappingException {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: aload_2
/*     */     //   2: invokeinterface expectStringFormat : (Lcom/fasterxml/jackson/databind/JavaType;)Lcom/fasterxml/jackson/databind/jsonFormatVisitors/JsonStringFormatVisitor;
/*     */     //   7: astore #4
/*     */     //   9: aload #4
/*     */     //   11: ifnull -> 160
/*     */     //   14: new java/util/LinkedHashSet
/*     */     //   17: dup
/*     */     //   18: invokespecial <init> : ()V
/*     */     //   21: astore #5
/*     */     //   23: aload_3
/*     */     //   24: invokevirtual getEnumConstants : ()[Ljava/lang/Object;
/*     */     //   27: astore #6
/*     */     //   29: aload #6
/*     */     //   31: arraylength
/*     */     //   32: istore #7
/*     */     //   34: iconst_0
/*     */     //   35: istore #8
/*     */     //   37: iload #8
/*     */     //   39: iload #7
/*     */     //   41: if_icmpge -> 151
/*     */     //   44: aload #6
/*     */     //   46: iload #8
/*     */     //   48: aaload
/*     */     //   49: astore #9
/*     */     //   51: aload #5
/*     */     //   53: aload_0
/*     */     //   54: getfield _accessor : Lcom/fasterxml/jackson/databind/introspect/AnnotatedMember;
/*     */     //   57: aload #9
/*     */     //   59: invokevirtual getValue : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   62: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   65: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   70: pop
/*     */     //   71: goto -> 145
/*     */     //   74: astore #10
/*     */     //   76: aload #10
/*     */     //   78: astore #11
/*     */     //   80: aload #11
/*     */     //   82: instanceof java/lang/reflect/InvocationTargetException
/*     */     //   85: ifeq -> 106
/*     */     //   88: aload #11
/*     */     //   90: invokevirtual getCause : ()Ljava/lang/Throwable;
/*     */     //   93: ifnull -> 106
/*     */     //   96: aload #11
/*     */     //   98: invokevirtual getCause : ()Ljava/lang/Throwable;
/*     */     //   101: astore #11
/*     */     //   103: goto -> 80
/*     */     //   106: aload #11
/*     */     //   108: invokestatic throwIfError : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*     */     //   111: pop
/*     */     //   112: aload #11
/*     */     //   114: aload #9
/*     */     //   116: new java/lang/StringBuilder
/*     */     //   119: dup
/*     */     //   120: invokespecial <init> : ()V
/*     */     //   123: aload_0
/*     */     //   124: getfield _accessor : Lcom/fasterxml/jackson/databind/introspect/AnnotatedMember;
/*     */     //   127: invokevirtual getName : ()Ljava/lang/String;
/*     */     //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   133: ldc '()'
/*     */     //   135: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   138: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   141: invokestatic wrapWithPath : (Ljava/lang/Throwable;Ljava/lang/Object;Ljava/lang/String;)Lcom/fasterxml/jackson/databind/JsonMappingException;
/*     */     //   144: athrow
/*     */     //   145: iinc #8, 1
/*     */     //   148: goto -> 37
/*     */     //   151: aload #4
/*     */     //   153: aload #5
/*     */     //   155: invokeinterface enumTypes : (Ljava/util/Set;)V
/*     */     //   160: iconst_1
/*     */     //   161: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #282	-> 0
/*     */     //   #283	-> 9
/*     */     //   #284	-> 14
/*     */     //   #285	-> 23
/*     */     //   #290	-> 51
/*     */     //   #298	-> 71
/*     */     //   #291	-> 74
/*     */     //   #292	-> 76
/*     */     //   #293	-> 80
/*     */     //   #294	-> 96
/*     */     //   #296	-> 106
/*     */     //   #297	-> 112
/*     */     //   #285	-> 145
/*     */     //   #300	-> 151
/*     */     //   #302	-> 160
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   80	65	11	t	Ljava/lang/Throwable;
/*     */     //   76	69	10	e	Ljava/lang/Exception;
/*     */     //   51	94	9	en	Ljava/lang/Object;
/*     */     //   23	137	5	enums	Ljava/util/Set;
/*     */     //   0	162	0	this	Lcom/fasterxml/jackson/databind/ser/std/JsonValueSerializer;
/*     */     //   0	162	1	visitor	Lcom/fasterxml/jackson/databind/jsonFormatVisitors/JsonFormatVisitorWrapper;
/*     */     //   0	162	2	typeHint	Lcom/fasterxml/jackson/databind/JavaType;
/*     */     //   0	162	3	enumType	Ljava/lang/Class;
/*     */     //   9	153	4	stringVisitor	Lcom/fasterxml/jackson/databind/jsonFormatVisitors/JsonStringFormatVisitor;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   23	137	5	enums	Ljava/util/Set<Ljava/lang/String;>;
/*     */     //   0	162	3	enumType	Ljava/lang/Class<*>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   51	71	74	java/lang/Exception
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isNaturalTypeWithStdHandling(Class<?> rawType, JsonSerializer<?> ser) {
/* 308 */     if (rawType.isPrimitive()) {
/* 309 */       if (rawType != int.class && rawType != boolean.class && rawType != double.class) {
/* 310 */         return false;
/*     */       }
/*     */     }
/* 313 */     else if (rawType != String.class && rawType != Integer.class && rawType != Boolean.class && rawType != Double.class) {
/*     */       
/* 315 */       return false;
/*     */     } 
/*     */     
/* 318 */     return isDefaultSerializer(ser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 329 */     return "(@JsonValue serializer for method " + this._accessor.getDeclaringClass() + "#" + this._accessor.getName() + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class TypeSerializerRerouter
/*     */     extends TypeSerializer
/*     */   {
/*     */     protected final TypeSerializer _typeSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final Object _forObject;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeSerializerRerouter(TypeSerializer ts, Object ob) {
/* 350 */       this._typeSerializer = ts;
/* 351 */       this._forObject = ob;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeSerializer forProperty(BeanProperty prop) {
/* 356 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonTypeInfo.As getTypeInclusion() {
/* 361 */       return this._typeSerializer.getTypeInclusion();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPropertyName() {
/* 366 */       return this._typeSerializer.getPropertyName();
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeIdResolver getTypeIdResolver() {
/* 371 */       return this._typeSerializer.getTypeIdResolver();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WritableTypeId writeTypePrefix(JsonGenerator g, WritableTypeId typeId) throws IOException {
/* 380 */       typeId.forValue = this._forObject;
/* 381 */       return this._typeSerializer.writeTypePrefix(g, typeId);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WritableTypeId writeTypeSuffix(JsonGenerator g, WritableTypeId typeId) throws IOException {
/* 388 */       return this._typeSerializer.writeTypeSuffix(g, typeId);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeTypePrefixForScalar(Object value, JsonGenerator gen) throws IOException {
/* 396 */       this._typeSerializer.writeTypePrefixForScalar(this._forObject, gen);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeTypePrefixForObject(Object value, JsonGenerator gen) throws IOException {
/* 402 */       this._typeSerializer.writeTypePrefixForObject(this._forObject, gen);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeTypePrefixForArray(Object value, JsonGenerator gen) throws IOException {
/* 408 */       this._typeSerializer.writeTypePrefixForArray(this._forObject, gen);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeTypeSuffixForScalar(Object value, JsonGenerator gen) throws IOException {
/* 414 */       this._typeSerializer.writeTypeSuffixForScalar(this._forObject, gen);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeTypeSuffixForObject(Object value, JsonGenerator gen) throws IOException {
/* 420 */       this._typeSerializer.writeTypeSuffixForObject(this._forObject, gen);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeTypeSuffixForArray(Object value, JsonGenerator gen) throws IOException {
/* 426 */       this._typeSerializer.writeTypeSuffixForArray(this._forObject, gen);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeTypePrefixForScalar(Object value, JsonGenerator gen, Class<?> type) throws IOException {
/* 432 */       this._typeSerializer.writeTypePrefixForScalar(this._forObject, gen, type);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeTypePrefixForObject(Object value, JsonGenerator gen, Class<?> type) throws IOException {
/* 438 */       this._typeSerializer.writeTypePrefixForObject(this._forObject, gen, type);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeTypePrefixForArray(Object value, JsonGenerator gen, Class<?> type) throws IOException {
/* 444 */       this._typeSerializer.writeTypePrefixForArray(this._forObject, gen, type);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeCustomTypePrefixForScalar(Object value, JsonGenerator gen, String typeId) throws IOException {
/* 457 */       this._typeSerializer.writeCustomTypePrefixForScalar(this._forObject, gen, typeId);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeCustomTypePrefixForObject(Object value, JsonGenerator gen, String typeId) throws IOException {
/* 463 */       this._typeSerializer.writeCustomTypePrefixForObject(this._forObject, gen, typeId);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeCustomTypePrefixForArray(Object value, JsonGenerator gen, String typeId) throws IOException {
/* 469 */       this._typeSerializer.writeCustomTypePrefixForArray(this._forObject, gen, typeId);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator gen, String typeId) throws IOException {
/* 475 */       this._typeSerializer.writeCustomTypeSuffixForScalar(this._forObject, gen, typeId);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeCustomTypeSuffixForObject(Object value, JsonGenerator gen, String typeId) throws IOException {
/* 481 */       this._typeSerializer.writeCustomTypeSuffixForObject(this._forObject, gen, typeId);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void writeCustomTypeSuffixForArray(Object value, JsonGenerator gen, String typeId) throws IOException {
/* 487 */       this._typeSerializer.writeCustomTypeSuffixForArray(this._forObject, gen, typeId);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\JsonValueSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */