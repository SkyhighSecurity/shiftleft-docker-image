/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
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
/*     */ class FactoryBasedEnumDeserializer
/*     */   extends StdDeserializer<Object>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _inputType;
/*     */   protected final boolean _hasArgs;
/*     */   protected final AnnotatedMethod _factory;
/*     */   protected final JsonDeserializer<?> _deser;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final SettableBeanProperty[] _creatorProps;
/*     */   private transient PropertyBasedCreator _propCreator;
/*     */   
/*     */   public FactoryBasedEnumDeserializer(Class<?> cls, AnnotatedMethod f, JavaType paramType, ValueInstantiator valueInstantiator, SettableBeanProperty[] creatorProps) {
/*  49 */     super(cls);
/*  50 */     this._factory = f;
/*  51 */     this._hasArgs = true;
/*     */     
/*  53 */     this._inputType = paramType.hasRawClass(String.class) ? null : paramType;
/*  54 */     this._deser = null;
/*  55 */     this._valueInstantiator = valueInstantiator;
/*  56 */     this._creatorProps = creatorProps;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FactoryBasedEnumDeserializer(Class<?> cls, AnnotatedMethod f) {
/*  64 */     super(cls);
/*  65 */     this._factory = f;
/*  66 */     this._hasArgs = false;
/*  67 */     this._inputType = null;
/*  68 */     this._deser = null;
/*  69 */     this._valueInstantiator = null;
/*  70 */     this._creatorProps = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FactoryBasedEnumDeserializer(FactoryBasedEnumDeserializer base, JsonDeserializer<?> deser) {
/*  75 */     super(base._valueClass);
/*  76 */     this._inputType = base._inputType;
/*  77 */     this._factory = base._factory;
/*  78 */     this._hasArgs = base._hasArgs;
/*  79 */     this._valueInstantiator = base._valueInstantiator;
/*  80 */     this._creatorProps = base._creatorProps;
/*     */     
/*  82 */     this._deser = deser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
/*  90 */     if (this._deser == null && this._inputType != null && this._creatorProps == null) {
/*  91 */       return new FactoryBasedEnumDeserializer(this, ctxt
/*  92 */           .findContextualValueDeserializer(this._inputType, property));
/*     */     }
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean supportsUpdate(DeserializationConfig config) {
/*  99 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCachable() {
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 109 */     Object value = null;
/* 110 */     if (this._deser != null) {
/* 111 */       value = this._deser.deserialize(p, ctxt);
/* 112 */     } else if (this._hasArgs) {
/* 113 */       JsonToken curr = p.getCurrentToken();
/*     */ 
/*     */       
/* 116 */       if (curr == JsonToken.VALUE_STRING || curr == JsonToken.FIELD_NAME)
/* 117 */       { value = p.getText(); }
/* 118 */       else { if (this._creatorProps != null && p.isExpectedStartObjectToken()) {
/* 119 */           if (this._propCreator == null) {
/* 120 */             this._propCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, this._creatorProps, ctxt
/* 121 */                 .isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
/*     */           }
/* 123 */           p.nextToken();
/* 124 */           return deserializeEnumUsingPropertyBased(p, ctxt, this._propCreator);
/*     */         } 
/* 126 */         value = p.getValueAsString(); }
/*     */     
/*     */     } else {
/* 129 */       p.skipChildren();
/*     */       try {
/* 131 */         return this._factory.call();
/* 132 */       } catch (Exception e) {
/* 133 */         Throwable t = ClassUtil.throwRootCauseIfIOE(e);
/* 134 */         return ctxt.handleInstantiationProblem(this._valueClass, null, t);
/*     */       } 
/*     */     } 
/*     */     try {
/* 138 */       return this._factory.callOnWith(this._valueClass, new Object[] { value });
/* 139 */     } catch (Exception e) {
/* 140 */       Throwable t = ClassUtil.throwRootCauseIfIOE(e);
/*     */       
/* 142 */       if (ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL) && t instanceof IllegalArgumentException)
/*     */       {
/* 144 */         return null;
/*     */       }
/* 146 */       return ctxt.handleInstantiationProblem(this._valueClass, value, t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 152 */     if (this._deser == null) {
/* 153 */       return deserialize(p, ctxt);
/*     */     }
/* 155 */     return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object deserializeEnumUsingPropertyBased(JsonParser p, DeserializationContext ctxt, PropertyBasedCreator creator) throws IOException {
/* 162 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, null);
/*     */     
/* 164 */     JsonToken t = p.getCurrentToken();
/* 165 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 166 */       String propName = p.getCurrentName();
/* 167 */       p.nextToken();
/*     */       
/* 169 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 170 */       if (creatorProp != null) {
/* 171 */         buffer.assignParameter(creatorProp, _deserializeWithErrorWrapping(p, ctxt, creatorProp));
/*     */       
/*     */       }
/* 174 */       else if (buffer.readIdProperty(propName)) {
/*     */       
/*     */       } 
/*     */     } 
/* 178 */     return creator.build(ctxt, buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object _deserializeWithErrorWrapping(JsonParser p, DeserializationContext ctxt, SettableBeanProperty prop) throws IOException {
/*     */     try {
/* 187 */       return prop.deserialize(p, ctxt);
/* 188 */     } catch (Exception e) {
/* 189 */       return wrapAndThrow(e, handledType(), prop.getName(), ctxt);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt) throws IOException {
/* 196 */     throw JsonMappingException.wrapWithPath(throwOrReturnThrowable(t, ctxt), bean, fieldName);
/*     */   }
/*     */ 
/*     */   
/*     */   private Throwable throwOrReturnThrowable(Throwable t, DeserializationContext ctxt) throws IOException {
/* 201 */     t = ClassUtil.getRootCause(t);
/*     */     
/* 203 */     ClassUtil.throwIfError(t);
/* 204 */     boolean wrap = (ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/*     */     
/* 206 */     if (t instanceof IOException) {
/* 207 */       if (!wrap || !(t instanceof com.fasterxml.jackson.core.JsonProcessingException)) {
/* 208 */         throw (IOException)t;
/*     */       }
/* 210 */     } else if (!wrap) {
/* 211 */       ClassUtil.throwIfRTE(t);
/*     */     } 
/* 213 */     return t;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\FactoryBasedEnumDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */