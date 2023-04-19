/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.deser.impl.FailingDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.NullsConstantProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ConcreteBeanPropertyBase;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.ViewMatcher;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SettableBeanProperty
/*     */   extends ConcreteBeanPropertyBase
/*     */   implements Serializable
/*     */ {
/*  36 */   protected static final JsonDeserializer<Object> MISSING_VALUE_DESERIALIZER = (JsonDeserializer<Object>)new FailingDeserializer("No _valueDeserializer assigned");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PropertyName _propName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _type;
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
/*     */   protected final transient Annotations _contextAnnotations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final NullValueProvider _nullProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _managedReferenceName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ObjectIdInfo _objectIdInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ViewMatcher _viewMatcher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int _propertyIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations) {
/* 136 */     this(propDef.getFullName(), type, propDef.getWrapperName(), typeDeser, contextAnnotations, propDef
/* 137 */         .getMetadata());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty(PropertyName propName, JavaType type, PropertyName wrapper, TypeDeserializer typeDeser, Annotations contextAnnotations, PropertyMetadata metadata) {
/* 144 */     super(metadata);
/*     */ 
/*     */     
/*     */     this._propertyIndex = -1;
/*     */ 
/*     */     
/* 150 */     if (propName == null) {
/* 151 */       this._propName = PropertyName.NO_NAME;
/*     */     } else {
/* 153 */       this._propName = propName.internSimpleName();
/*     */     } 
/* 155 */     this._type = type;
/* 156 */     this._wrapperName = wrapper;
/* 157 */     this._contextAnnotations = contextAnnotations;
/* 158 */     this._viewMatcher = null;
/*     */ 
/*     */     
/* 161 */     if (typeDeser != null) {
/* 162 */       typeDeser = typeDeser.forProperty((BeanProperty)this);
/*     */     }
/* 164 */     this._valueTypeDeserializer = typeDeser;
/* 165 */     this._valueDeserializer = MISSING_VALUE_DESERIALIZER;
/* 166 */     this._nullProvider = (NullValueProvider)MISSING_VALUE_DESERIALIZER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty(PropertyName propName, JavaType type, PropertyMetadata metadata, JsonDeserializer<Object> valueDeser) {
/* 177 */     super(metadata);
/*     */     this._propertyIndex = -1;
/* 179 */     if (propName == null) {
/* 180 */       this._propName = PropertyName.NO_NAME;
/*     */     } else {
/* 182 */       this._propName = propName.internSimpleName();
/*     */     } 
/* 184 */     this._type = type;
/* 185 */     this._wrapperName = null;
/* 186 */     this._contextAnnotations = null;
/* 187 */     this._viewMatcher = null;
/* 188 */     this._valueTypeDeserializer = null;
/* 189 */     this._valueDeserializer = valueDeser;
/*     */     
/* 191 */     this._nullProvider = (NullValueProvider)valueDeser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty(SettableBeanProperty src) {
/* 199 */     super(src); this._propertyIndex = -1;
/* 200 */     this._propName = src._propName;
/* 201 */     this._type = src._type;
/* 202 */     this._wrapperName = src._wrapperName;
/* 203 */     this._contextAnnotations = src._contextAnnotations;
/* 204 */     this._valueDeserializer = src._valueDeserializer;
/* 205 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 206 */     this._managedReferenceName = src._managedReferenceName;
/* 207 */     this._propertyIndex = src._propertyIndex;
/* 208 */     this._viewMatcher = src._viewMatcher;
/* 209 */     this._nullProvider = src._nullProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty(SettableBeanProperty src, JsonDeserializer<?> deser, NullValueProvider nuller) {
/* 219 */     super(src); JsonDeserializer<Object> jsonDeserializer; this._propertyIndex = -1;
/* 220 */     this._propName = src._propName;
/* 221 */     this._type = src._type;
/* 222 */     this._wrapperName = src._wrapperName;
/* 223 */     this._contextAnnotations = src._contextAnnotations;
/* 224 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 225 */     this._managedReferenceName = src._managedReferenceName;
/* 226 */     this._propertyIndex = src._propertyIndex;
/*     */     
/* 228 */     if (deser == null) {
/* 229 */       this._valueDeserializer = MISSING_VALUE_DESERIALIZER;
/*     */     } else {
/* 231 */       this._valueDeserializer = (JsonDeserializer)deser;
/*     */     } 
/* 233 */     this._viewMatcher = src._viewMatcher;
/*     */     
/* 235 */     if (nuller == MISSING_VALUE_DESERIALIZER) {
/* 236 */       jsonDeserializer = this._valueDeserializer;
/*     */     }
/* 238 */     this._nullProvider = (NullValueProvider)jsonDeserializer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty(SettableBeanProperty src, PropertyName newName) {
/* 246 */     super(src); this._propertyIndex = -1;
/* 247 */     this._propName = newName;
/* 248 */     this._type = src._type;
/* 249 */     this._wrapperName = src._wrapperName;
/* 250 */     this._contextAnnotations = src._contextAnnotations;
/* 251 */     this._valueDeserializer = src._valueDeserializer;
/* 252 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 253 */     this._managedReferenceName = src._managedReferenceName;
/* 254 */     this._propertyIndex = src._propertyIndex;
/* 255 */     this._viewMatcher = src._viewMatcher;
/* 256 */     this._nullProvider = src._nullProvider;
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
/*     */   public abstract SettableBeanProperty withValueDeserializer(JsonDeserializer<?> paramJsonDeserializer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract SettableBeanProperty withName(PropertyName paramPropertyName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withSimpleName(String simpleName) {
/* 288 */     PropertyName n = (this._propName == null) ? new PropertyName(simpleName) : this._propName.withSimpleName(simpleName);
/* 289 */     return (n == this._propName) ? this : withName(n);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract SettableBeanProperty withNullProvider(NullValueProvider paramNullValueProvider);
/*     */ 
/*     */   
/*     */   public void setManagedReferenceName(String n) {
/* 298 */     this._managedReferenceName = n;
/*     */   }
/*     */   
/*     */   public void setObjectIdInfo(ObjectIdInfo objectIdInfo) {
/* 302 */     this._objectIdInfo = objectIdInfo;
/*     */   }
/*     */   
/*     */   public void setViews(Class<?>[] views) {
/* 306 */     if (views == null) {
/* 307 */       this._viewMatcher = null;
/*     */     } else {
/* 309 */       this._viewMatcher = ViewMatcher.construct(views);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void assignIndex(int index) {
/* 317 */     if (this._propertyIndex != -1) {
/* 318 */       throw new IllegalStateException("Property '" + getName() + "' already had index (" + this._propertyIndex + "), trying to assign " + index);
/*     */     }
/* 320 */     this._propertyIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fixAccess(DeserializationConfig config) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void markAsIgnorable() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnorable() {
/* 342 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 352 */     return this._propName.getSimpleName();
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyName getFullName() {
/* 357 */     return this._propName;
/*     */   }
/*     */   
/*     */   public JavaType getType() {
/* 361 */     return this._type;
/*     */   }
/*     */   
/*     */   public PropertyName getWrapperName() {
/* 365 */     return this._wrapperName;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract AnnotatedMember getMember();
/*     */ 
/*     */   
/*     */   public abstract <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> paramClass);
/*     */ 
/*     */   
/*     */   public <A extends java.lang.annotation.Annotation> A getContextAnnotation(Class<A> acls) {
/* 376 */     return (A)this._contextAnnotations.get(acls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor, SerializerProvider provider) throws JsonMappingException {
/* 384 */     if (isRequired()) {
/* 385 */       objectVisitor.property((BeanProperty)this);
/*     */     } else {
/* 387 */       objectVisitor.optionalProperty((BeanProperty)this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getDeclaringClass() {
/* 398 */     return getMember().getDeclaringClass();
/*     */   }
/*     */   public String getManagedReferenceName() {
/* 401 */     return this._managedReferenceName;
/*     */   } public ObjectIdInfo getObjectIdInfo() {
/* 403 */     return this._objectIdInfo;
/*     */   }
/*     */   public boolean hasValueDeserializer() {
/* 406 */     return (this._valueDeserializer != null && this._valueDeserializer != MISSING_VALUE_DESERIALIZER);
/*     */   }
/*     */   public boolean hasValueTypeDeserializer() {
/* 409 */     return (this._valueTypeDeserializer != null);
/*     */   }
/*     */   public JsonDeserializer<Object> getValueDeserializer() {
/* 412 */     JsonDeserializer<Object> deser = this._valueDeserializer;
/* 413 */     if (deser == MISSING_VALUE_DESERIALIZER) {
/* 414 */       return null;
/*     */     }
/* 416 */     return deser;
/*     */   }
/*     */   public TypeDeserializer getValueTypeDeserializer() {
/* 419 */     return this._valueTypeDeserializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public NullValueProvider getNullValueProvider() {
/* 424 */     return this._nullProvider;
/*     */   }
/*     */   public boolean visibleInView(Class<?> activeView) {
/* 427 */     return (this._viewMatcher == null || this._viewMatcher.isVisibleForView(activeView));
/*     */   }
/*     */   public boolean hasViews() {
/* 430 */     return (this._viewMatcher != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPropertyIndex() {
/* 439 */     return this._propertyIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCreatorIndex() {
/* 449 */     throw new IllegalStateException(String.format("Internal error: no creator index for property '%s' (of type %s)", new Object[] {
/*     */             
/* 451 */             getName(), getClass().getName()
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getInjectableValueId() {
/* 458 */     return null;
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
/*     */   public abstract void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject) throws IOException;
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
/*     */   public abstract Object deserializeSetAndReturn(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject) throws IOException;
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
/*     */   public abstract void set(Object paramObject1, Object paramObject2) throws IOException;
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
/*     */   public abstract Object setAndReturn(Object paramObject1, Object paramObject2) throws IOException;
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
/*     */   public final Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 523 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/* 524 */       return this._nullProvider.getNullValue(ctxt);
/*     */     }
/* 526 */     if (this._valueTypeDeserializer != null) {
/* 527 */       return this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     }
/*     */     
/* 530 */     Object value = this._valueDeserializer.deserialize(p, ctxt);
/* 531 */     if (value == null) {
/* 532 */       value = this._nullProvider.getNullValue(ctxt);
/*     */     }
/* 534 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object deserializeWith(JsonParser p, DeserializationContext ctxt, Object toUpdate) throws IOException {
/* 545 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/*     */       
/* 547 */       if (NullsConstantProvider.isSkipper(this._nullProvider)) {
/* 548 */         return toUpdate;
/*     */       }
/* 550 */       return this._nullProvider.getNullValue(ctxt);
/*     */     } 
/*     */     
/* 553 */     if (this._valueTypeDeserializer != null) {
/* 554 */       ctxt.reportBadDefinition(getType(), 
/* 555 */           String.format("Cannot merge polymorphic property '%s'", new Object[] {
/* 556 */               getName()
/*     */             }));
/*     */     }
/*     */     
/* 560 */     Object value = this._valueDeserializer.deserialize(p, ctxt, toUpdate);
/* 561 */     if (value == null) {
/* 562 */       if (NullsConstantProvider.isSkipper(this._nullProvider)) {
/* 563 */         return toUpdate;
/*     */       }
/* 565 */       value = this._nullProvider.getNullValue(ctxt);
/*     */     } 
/* 567 */     return value;
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
/*     */   protected void _throwAsIOE(JsonParser p, Exception e, Object value) throws IOException {
/* 582 */     if (e instanceof IllegalArgumentException) {
/* 583 */       String actType = ClassUtil.classNameOf(value);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 589 */       StringBuilder msg = (new StringBuilder("Problem deserializing property '")).append(getName()).append("' (expected type: ").append(getType()).append("; actual type: ").append(actType).append(")");
/* 590 */       String origMsg = ClassUtil.exceptionMessage(e);
/* 591 */       if (origMsg != null) {
/* 592 */         msg.append(", problem: ")
/* 593 */           .append(origMsg);
/*     */       } else {
/* 595 */         msg.append(" (no error message provided)");
/*     */       } 
/* 597 */       throw JsonMappingException.from(p, msg.toString(), e);
/*     */     } 
/* 599 */     _throwAsIOE(p, e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IOException _throwAsIOE(JsonParser p, Exception e) throws IOException {
/* 607 */     ClassUtil.throwIfIOE(e);
/* 608 */     ClassUtil.throwIfRTE(e);
/*     */     
/* 610 */     Throwable th = ClassUtil.getRootCause(e);
/* 611 */     throw JsonMappingException.from(p, ClassUtil.exceptionMessage(th), th);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected IOException _throwAsIOE(Exception e) throws IOException {
/* 616 */     return _throwAsIOE((JsonParser)null, e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _throwAsIOE(Exception e, Object value) throws IOException {
/* 622 */     _throwAsIOE((JsonParser)null, e, value);
/*     */   }
/*     */   public String toString() {
/* 625 */     return "[property '" + getName() + "']";
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
/*     */   public static abstract class Delegating
/*     */     extends SettableBeanProperty
/*     */   {
/*     */     protected final SettableBeanProperty delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Delegating(SettableBeanProperty d) {
/* 649 */       super(d);
/* 650 */       this.delegate = d;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract SettableBeanProperty withDelegate(SettableBeanProperty param1SettableBeanProperty);
/*     */ 
/*     */ 
/*     */     
/*     */     protected SettableBeanProperty _with(SettableBeanProperty newDelegate) {
/* 660 */       if (newDelegate == this.delegate) {
/* 661 */         return this;
/*     */       }
/* 663 */       return withDelegate(newDelegate);
/*     */     }
/*     */ 
/*     */     
/*     */     public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
/* 668 */       return _with(this.delegate.withValueDeserializer(deser));
/*     */     }
/*     */ 
/*     */     
/*     */     public SettableBeanProperty withName(PropertyName newName) {
/* 673 */       return _with(this.delegate.withName(newName));
/*     */     }
/*     */ 
/*     */     
/*     */     public SettableBeanProperty withNullProvider(NullValueProvider nva) {
/* 678 */       return _with(this.delegate.withNullProvider(nva));
/*     */     }
/*     */ 
/*     */     
/*     */     public void assignIndex(int index) {
/* 683 */       this.delegate.assignIndex(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public void fixAccess(DeserializationConfig config) {
/* 688 */       this.delegate.fixAccess(config);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Class<?> getDeclaringClass() {
/* 698 */       return this.delegate.getDeclaringClass();
/*     */     }
/*     */     public String getManagedReferenceName() {
/* 701 */       return this.delegate.getManagedReferenceName();
/*     */     }
/*     */     public ObjectIdInfo getObjectIdInfo() {
/* 704 */       return this.delegate.getObjectIdInfo();
/*     */     }
/*     */     public boolean hasValueDeserializer() {
/* 707 */       return this.delegate.hasValueDeserializer();
/*     */     }
/*     */     public boolean hasValueTypeDeserializer() {
/* 710 */       return this.delegate.hasValueTypeDeserializer();
/*     */     }
/*     */     public JsonDeserializer<Object> getValueDeserializer() {
/* 713 */       return this.delegate.getValueDeserializer();
/*     */     }
/*     */     public TypeDeserializer getValueTypeDeserializer() {
/* 716 */       return this.delegate.getValueTypeDeserializer();
/*     */     }
/*     */     public boolean visibleInView(Class<?> activeView) {
/* 719 */       return this.delegate.visibleInView(activeView);
/*     */     }
/*     */     public boolean hasViews() {
/* 722 */       return this.delegate.hasViews();
/*     */     }
/*     */     public int getPropertyIndex() {
/* 725 */       return this.delegate.getPropertyIndex();
/*     */     }
/*     */     public int getCreatorIndex() {
/* 728 */       return this.delegate.getCreatorIndex();
/*     */     }
/*     */     public Object getInjectableValueId() {
/* 731 */       return this.delegate.getInjectableValueId();
/*     */     }
/*     */     
/*     */     public AnnotatedMember getMember() {
/* 735 */       return this.delegate.getMember();
/*     */     }
/*     */ 
/*     */     
/*     */     public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) {
/* 740 */       return this.delegate.getAnnotation(acls);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SettableBeanProperty getDelegate() {
/* 750 */       return this.delegate;
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
/*     */     public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/* 762 */       this.delegate.deserializeAndSet(p, ctxt, instance);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/* 769 */       return this.delegate.deserializeSetAndReturn(p, ctxt, instance);
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(Object instance, Object value) throws IOException {
/* 774 */       this.delegate.set(instance, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object setAndReturn(Object instance, Object value) throws IOException {
/* 779 */       return this.delegate.setAndReturn(instance, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\SettableBeanProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */