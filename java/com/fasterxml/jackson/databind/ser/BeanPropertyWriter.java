/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class BeanPropertyWriter
/*     */   extends PropertyWriter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  48 */   public static final Object MARKER_FOR_EMPTY = JsonInclude.Include.NON_EMPTY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SerializedString _name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PropertyName _wrapperName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _declaredType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _cfgSerializationType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaType _nonTrivialBaseType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final transient Annotations _contextAnnotations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotatedMember _member;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected transient Method _accessorMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected transient Field _field;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonSerializer<Object> _serializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonSerializer<Object> _nullSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeSerializer _typeSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected transient PropertySerializerMap _dynamicSerializers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean _suppressNulls;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object _suppressableValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Class<?>[] _includeInViews;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected transient HashMap<Object, Object> _internalSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember member, Annotations contextAnnotations, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType, boolean suppressNulls, Object suppressableValue, Class<?>[] includeInViews) {
/* 216 */     super(propDef);
/* 217 */     this._member = member;
/* 218 */     this._contextAnnotations = contextAnnotations;
/*     */     
/* 220 */     this._name = new SerializedString(propDef.getName());
/* 221 */     this._wrapperName = propDef.getWrapperName();
/*     */     
/* 223 */     this._declaredType = declaredType;
/* 224 */     this._serializer = (JsonSerializer)ser;
/* 225 */     this
/* 226 */       ._dynamicSerializers = (ser == null) ? PropertySerializerMap.emptyForProperties() : null;
/* 227 */     this._typeSerializer = typeSer;
/* 228 */     this._cfgSerializationType = serType;
/*     */     
/* 230 */     if (member instanceof com.fasterxml.jackson.databind.introspect.AnnotatedField) {
/* 231 */       this._accessorMethod = null;
/* 232 */       this._field = (Field)member.getMember();
/* 233 */     } else if (member instanceof com.fasterxml.jackson.databind.introspect.AnnotatedMethod) {
/* 234 */       this._accessorMethod = (Method)member.getMember();
/* 235 */       this._field = null;
/*     */     }
/*     */     else {
/*     */       
/* 239 */       this._accessorMethod = null;
/* 240 */       this._field = null;
/*     */     } 
/* 242 */     this._suppressNulls = suppressNulls;
/* 243 */     this._suppressableValue = suppressableValue;
/*     */ 
/*     */     
/* 246 */     this._nullSerializer = null;
/* 247 */     this._includeInViews = includeInViews;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember member, Annotations contextAnnotations, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType, boolean suppressNulls, Object suppressableValue) {
/* 257 */     this(propDef, member, contextAnnotations, declaredType, ser, typeSer, serType, suppressNulls, suppressableValue, null);
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
/*     */   protected BeanPropertyWriter() {
/* 270 */     super(PropertyMetadata.STD_REQUIRED_OR_OPTIONAL);
/* 271 */     this._member = null;
/* 272 */     this._contextAnnotations = null;
/*     */     
/* 274 */     this._name = null;
/* 275 */     this._wrapperName = null;
/* 276 */     this._includeInViews = null;
/*     */     
/* 278 */     this._declaredType = null;
/* 279 */     this._serializer = null;
/* 280 */     this._dynamicSerializers = null;
/* 281 */     this._typeSerializer = null;
/* 282 */     this._cfgSerializationType = null;
/*     */     
/* 284 */     this._accessorMethod = null;
/* 285 */     this._field = null;
/* 286 */     this._suppressNulls = false;
/* 287 */     this._suppressableValue = null;
/*     */     
/* 289 */     this._nullSerializer = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base) {
/* 296 */     this(base, base._name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base, PropertyName name) {
/* 303 */     super(base);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 310 */     this._name = new SerializedString(name.getSimpleName());
/* 311 */     this._wrapperName = base._wrapperName;
/*     */     
/* 313 */     this._contextAnnotations = base._contextAnnotations;
/* 314 */     this._declaredType = base._declaredType;
/*     */     
/* 316 */     this._member = base._member;
/* 317 */     this._accessorMethod = base._accessorMethod;
/* 318 */     this._field = base._field;
/*     */     
/* 320 */     this._serializer = base._serializer;
/* 321 */     this._nullSerializer = base._nullSerializer;
/*     */     
/* 323 */     if (base._internalSettings != null) {
/* 324 */       this._internalSettings = new HashMap<>(base._internalSettings);
/*     */     }
/*     */     
/* 327 */     this._cfgSerializationType = base._cfgSerializationType;
/* 328 */     this._dynamicSerializers = base._dynamicSerializers;
/* 329 */     this._suppressNulls = base._suppressNulls;
/* 330 */     this._suppressableValue = base._suppressableValue;
/* 331 */     this._includeInViews = base._includeInViews;
/* 332 */     this._typeSerializer = base._typeSerializer;
/* 333 */     this._nonTrivialBaseType = base._nonTrivialBaseType;
/*     */   }
/*     */   
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base, SerializedString name) {
/* 337 */     super(base);
/* 338 */     this._name = name;
/* 339 */     this._wrapperName = base._wrapperName;
/*     */     
/* 341 */     this._member = base._member;
/* 342 */     this._contextAnnotations = base._contextAnnotations;
/* 343 */     this._declaredType = base._declaredType;
/* 344 */     this._accessorMethod = base._accessorMethod;
/* 345 */     this._field = base._field;
/* 346 */     this._serializer = base._serializer;
/* 347 */     this._nullSerializer = base._nullSerializer;
/* 348 */     if (base._internalSettings != null) {
/* 349 */       this._internalSettings = new HashMap<>(base._internalSettings);
/*     */     }
/*     */     
/* 352 */     this._cfgSerializationType = base._cfgSerializationType;
/* 353 */     this._dynamicSerializers = base._dynamicSerializers;
/* 354 */     this._suppressNulls = base._suppressNulls;
/* 355 */     this._suppressableValue = base._suppressableValue;
/* 356 */     this._includeInViews = base._includeInViews;
/* 357 */     this._typeSerializer = base._typeSerializer;
/* 358 */     this._nonTrivialBaseType = base._nonTrivialBaseType;
/*     */   }
/*     */   
/*     */   public BeanPropertyWriter rename(NameTransformer transformer) {
/* 362 */     String newName = transformer.transform(this._name.getValue());
/* 363 */     if (newName.equals(this._name.toString())) {
/* 364 */       return this;
/*     */     }
/* 366 */     return _new(PropertyName.construct(newName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanPropertyWriter _new(PropertyName newName) {
/* 375 */     return new BeanPropertyWriter(this, newName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void assignTypeSerializer(TypeSerializer typeSer) {
/* 385 */     this._typeSerializer = typeSer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void assignSerializer(JsonSerializer<Object> ser) {
/* 393 */     if (this._serializer != null && this._serializer != ser)
/* 394 */       throw new IllegalStateException(String.format("Cannot override _serializer: had a %s, trying to set to %s", new Object[] {
/*     */               
/* 396 */               ClassUtil.classNameOf(this._serializer), ClassUtil.classNameOf(ser)
/*     */             })); 
/* 398 */     this._serializer = ser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void assignNullSerializer(JsonSerializer<Object> nullSer) {
/* 406 */     if (this._nullSerializer != null && this._nullSerializer != nullSer)
/* 407 */       throw new IllegalStateException(String.format("Cannot override _nullSerializer: had a %s, trying to set to %s", new Object[] {
/*     */               
/* 409 */               ClassUtil.classNameOf(this._nullSerializer), ClassUtil.classNameOf(nullSer)
/*     */             })); 
/* 411 */     this._nullSerializer = nullSer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper) {
/* 419 */     return (BeanPropertyWriter)new UnwrappingBeanPropertyWriter(this, unwrapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNonTrivialBaseType(JavaType t) {
/* 428 */     this._nonTrivialBaseType = t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fixAccess(SerializationConfig config) {
/* 439 */     this._member.fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
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
/*     */   Object readResolve() {
/* 454 */     if (this._member instanceof com.fasterxml.jackson.databind.introspect.AnnotatedField) {
/* 455 */       this._accessorMethod = null;
/* 456 */       this._field = (Field)this._member.getMember();
/* 457 */     } else if (this._member instanceof com.fasterxml.jackson.databind.introspect.AnnotatedMethod) {
/* 458 */       this._accessorMethod = (Method)this._member.getMember();
/* 459 */       this._field = null;
/*     */     } 
/* 461 */     if (this._serializer == null) {
/* 462 */       this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*     */     }
/* 464 */     return this;
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
/*     */   public String getName() {
/* 476 */     return this._name.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyName getFullName() {
/* 482 */     return new PropertyName(this._name.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType getType() {
/* 487 */     return this._declaredType;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyName getWrapperName() {
/* 492 */     return this._wrapperName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) {
/* 498 */     return (this._member == null) ? null : (A)this._member.getAnnotation(acls);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.lang.annotation.Annotation> A getContextAnnotation(Class<A> acls) {
/* 504 */     return (this._contextAnnotations == null) ? null : (A)this._contextAnnotations
/* 505 */       .get(acls);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedMember getMember() {
/* 510 */     return this._member;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _depositSchemaProperty(ObjectNode propertiesNode, JsonNode schemaNode) {
/* 516 */     propertiesNode.set(getName(), schemaNode);
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
/*     */   public Object getInternalSetting(Object key) {
/* 532 */     return (this._internalSettings == null) ? null : this._internalSettings.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setInternalSetting(Object key, Object value) {
/* 541 */     if (this._internalSettings == null) {
/* 542 */       this._internalSettings = new HashMap<>();
/*     */     }
/* 544 */     return this._internalSettings.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object removeInternalSetting(Object key) {
/* 553 */     Object removed = null;
/* 554 */     if (this._internalSettings != null) {
/* 555 */       removed = this._internalSettings.remove(key);
/*     */       
/* 557 */       if (this._internalSettings.size() == 0) {
/* 558 */         this._internalSettings = null;
/*     */       }
/*     */     } 
/* 561 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializableString getSerializedName() {
/* 571 */     return (SerializableString)this._name;
/*     */   }
/*     */   
/*     */   public boolean hasSerializer() {
/* 575 */     return (this._serializer != null);
/*     */   }
/*     */   
/*     */   public boolean hasNullSerializer() {
/* 579 */     return (this._nullSerializer != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeSerializer getTypeSerializer() {
/* 586 */     return this._typeSerializer;
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
/*     */   public boolean isUnwrapping() {
/* 600 */     return false;
/*     */   }
/*     */   
/*     */   public boolean willSuppressNulls() {
/* 604 */     return this._suppressNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wouldConflictWithName(PropertyName name) {
/* 614 */     if (this._wrapperName != null) {
/* 615 */       return this._wrapperName.equals(name);
/*     */     }
/*     */     
/* 618 */     return (name.hasSimpleName(this._name.getValue()) && !name.hasNamespace());
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<Object> getSerializer() {
/* 623 */     return this._serializer;
/*     */   }
/*     */   
/*     */   public JavaType getSerializationType() {
/* 627 */     return this._cfgSerializationType;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> getRawSerializationType() {
/* 632 */     return (this._cfgSerializationType == null) ? null : this._cfgSerializationType
/* 633 */       .getRawClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> getPropertyType() {
/* 641 */     if (this._accessorMethod != null) {
/* 642 */       return this._accessorMethod.getReturnType();
/*     */     }
/* 644 */     if (this._field != null) {
/* 645 */       return this._field.getType();
/*     */     }
/* 647 */     return null;
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
/*     */   public Type getGenericPropertyType() {
/* 659 */     if (this._accessorMethod != null) {
/* 660 */       return this._accessorMethod.getGenericReturnType();
/*     */     }
/* 662 */     if (this._field != null) {
/* 663 */       return this._field.getGenericType();
/*     */     }
/* 665 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?>[] getViews() {
/* 669 */     return this._includeInViews;
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
/*     */   public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
/* 688 */     Object value = (this._accessorMethod == null) ? this._field.get(bean) : this._accessorMethod.invoke(bean, (Object[])null);
/*     */ 
/*     */     
/* 691 */     if (value == null) {
/* 692 */       if (this._nullSerializer != null) {
/* 693 */         gen.writeFieldName((SerializableString)this._name);
/* 694 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 699 */     JsonSerializer<Object> ser = this._serializer;
/* 700 */     if (ser == null) {
/* 701 */       Class<?> cls = value.getClass();
/* 702 */       PropertySerializerMap m = this._dynamicSerializers;
/* 703 */       ser = m.serializerFor(cls);
/* 704 */       if (ser == null) {
/* 705 */         ser = _findAndAddDynamic(m, cls, prov);
/*     */       }
/*     */     } 
/*     */     
/* 709 */     if (this._suppressableValue != null) {
/* 710 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 711 */         if (ser.isEmpty(prov, value)) {
/*     */           return;
/*     */         }
/* 714 */       } else if (this._suppressableValue.equals(value)) {
/*     */         return;
/*     */       } 
/*     */     }
/*     */     
/* 719 */     if (value == bean)
/*     */     {
/* 721 */       if (_handleSelfReference(bean, gen, prov, ser)) {
/*     */         return;
/*     */       }
/*     */     }
/* 725 */     gen.writeFieldName((SerializableString)this._name);
/* 726 */     if (this._typeSerializer == null) {
/* 727 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 729 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
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
/*     */   
/*     */   public void serializeAsOmittedField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
/* 743 */     if (!gen.canOmitFields()) {
/* 744 */       gen.writeOmittedField(this._name.getValue());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
/* 760 */     Object value = (this._accessorMethod == null) ? this._field.get(bean) : this._accessorMethod.invoke(bean, (Object[])null);
/* 761 */     if (value == null) {
/* 762 */       if (this._nullSerializer != null) {
/* 763 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       } else {
/* 765 */         gen.writeNull();
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 770 */     JsonSerializer<Object> ser = this._serializer;
/* 771 */     if (ser == null) {
/* 772 */       Class<?> cls = value.getClass();
/* 773 */       PropertySerializerMap map = this._dynamicSerializers;
/* 774 */       ser = map.serializerFor(cls);
/* 775 */       if (ser == null) {
/* 776 */         ser = _findAndAddDynamic(map, cls, prov);
/*     */       }
/*     */     } 
/*     */     
/* 780 */     if (this._suppressableValue != null) {
/* 781 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 782 */         if (ser.isEmpty(prov, value)) {
/*     */           
/* 784 */           serializeAsPlaceholder(bean, gen, prov);
/*     */           return;
/*     */         } 
/* 787 */       } else if (this._suppressableValue.equals(value)) {
/*     */ 
/*     */         
/* 790 */         serializeAsPlaceholder(bean, gen, prov);
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/* 795 */     if (value == bean && 
/* 796 */       _handleSelfReference(bean, gen, prov, ser)) {
/*     */       return;
/*     */     }
/*     */     
/* 800 */     if (this._typeSerializer == null) {
/* 801 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 803 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
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
/*     */ 
/*     */   
/*     */   public void serializeAsPlaceholder(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
/* 818 */     if (this._nullSerializer != null) {
/* 819 */       this._nullSerializer.serialize(null, gen, prov);
/*     */     } else {
/* 821 */       gen.writeNull();
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
/*     */   
/*     */   public void depositSchemaProperty(JsonObjectFormatVisitor v, SerializerProvider provider) throws JsonMappingException {
/* 835 */     if (v != null) {
/* 836 */       if (isRequired()) {
/* 837 */         v.property((BeanProperty)this);
/*     */       } else {
/* 839 */         v.optionalProperty((BeanProperty)this);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void depositSchemaProperty(ObjectNode propertiesNode, SerializerProvider provider) throws JsonMappingException {
/*     */     JsonNode schemaNode;
/* 861 */     JavaType propType = getSerializationType();
/*     */ 
/*     */     
/* 864 */     Type hint = (propType == null) ? (Type)getType() : propType.getRawClass();
/*     */ 
/*     */     
/* 867 */     JsonSerializer<Object> ser = getSerializer();
/* 868 */     if (ser == null) {
/* 869 */       ser = provider.findValueSerializer(getType(), (BeanProperty)this);
/*     */     }
/* 871 */     boolean isOptional = !isRequired();
/* 872 */     if (ser instanceof SchemaAware) {
/* 873 */       schemaNode = ((SchemaAware)ser).getSchema(provider, hint, isOptional);
/*     */     }
/*     */     else {
/*     */       
/* 877 */       schemaNode = JsonSchema.getDefaultSchemaNode();
/*     */     } 
/* 879 */     _depositSchemaProperty(propertiesNode, schemaNode);
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
/*     */   protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/* 892 */     if (this._nonTrivialBaseType != null) {
/* 893 */       JavaType t = provider.constructSpecializedType(this._nonTrivialBaseType, type);
/*     */       
/* 895 */       result = map.findAndAddPrimarySerializer(t, provider, (BeanProperty)this);
/*     */     } else {
/* 897 */       result = map.findAndAddPrimarySerializer(type, provider, (BeanProperty)this);
/*     */     } 
/*     */     
/* 900 */     if (map != result.map) {
/* 901 */       this._dynamicSerializers = result.map;
/*     */     }
/* 903 */     return result.serializer;
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
/*     */   public final Object get(Object bean) throws Exception {
/* 915 */     return (this._accessorMethod == null) ? this._field.get(bean) : this._accessorMethod
/* 916 */       .invoke(bean, (Object[])null);
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
/*     */   protected boolean _handleSelfReference(Object bean, JsonGenerator gen, SerializerProvider prov, JsonSerializer<?> ser) throws JsonMappingException {
/* 936 */     if (prov.isEnabled(SerializationFeature.FAIL_ON_SELF_REFERENCES) && 
/* 937 */       !ser.usesObjectId())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 943 */       if (ser instanceof com.fasterxml.jackson.databind.ser.std.BeanSerializerBase) {
/* 944 */         prov.reportBadDefinition(getType(), "Direct self-reference leading to cycle");
/*     */       }
/*     */     }
/* 947 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 952 */     StringBuilder sb = new StringBuilder(40);
/* 953 */     sb.append("property '").append(getName()).append("' (");
/* 954 */     if (this._accessorMethod != null) {
/* 955 */       sb.append("via method ")
/* 956 */         .append(this._accessorMethod.getDeclaringClass().getName())
/* 957 */         .append("#").append(this._accessorMethod.getName());
/* 958 */     } else if (this._field != null) {
/* 959 */       sb.append("field \"").append(this._field.getDeclaringClass().getName())
/* 960 */         .append("#").append(this._field.getName());
/*     */     } else {
/* 962 */       sb.append("virtual");
/*     */     } 
/* 964 */     if (this._serializer == null) {
/* 965 */       sb.append(", no static serializer");
/*     */     } else {
/* 967 */       sb.append(", static serializer of type " + this._serializer
/* 968 */           .getClass().getName());
/*     */     } 
/* 970 */     sb.append(')');
/* 971 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\BeanPropertyWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */