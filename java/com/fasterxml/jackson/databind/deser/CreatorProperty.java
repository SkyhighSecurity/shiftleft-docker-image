/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
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
/*     */ public class CreatorProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedParameter _annotated;
/*     */   protected final Object _injectableValueId;
/*     */   protected SettableBeanProperty _fallbackSetter;
/*     */   protected final int _creatorIndex;
/*     */   protected boolean _ignorable;
/*     */   
/*     */   public CreatorProperty(PropertyName name, JavaType type, PropertyName wrapperName, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param, int index, Object injectableValueId, PropertyMetadata metadata) {
/*  95 */     super(name, type, wrapperName, typeDeser, contextAnnotations, metadata);
/*  96 */     this._annotated = param;
/*  97 */     this._creatorIndex = index;
/*  98 */     this._injectableValueId = injectableValueId;
/*  99 */     this._fallbackSetter = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CreatorProperty(CreatorProperty src, PropertyName newName) {
/* 106 */     super(src, newName);
/* 107 */     this._annotated = src._annotated;
/* 108 */     this._injectableValueId = src._injectableValueId;
/* 109 */     this._fallbackSetter = src._fallbackSetter;
/* 110 */     this._creatorIndex = src._creatorIndex;
/* 111 */     this._ignorable = src._ignorable;
/*     */   }
/*     */ 
/*     */   
/*     */   protected CreatorProperty(CreatorProperty src, JsonDeserializer<?> deser, NullValueProvider nva) {
/* 116 */     super(src, deser, nva);
/* 117 */     this._annotated = src._annotated;
/* 118 */     this._injectableValueId = src._injectableValueId;
/* 119 */     this._fallbackSetter = src._fallbackSetter;
/* 120 */     this._creatorIndex = src._creatorIndex;
/* 121 */     this._ignorable = src._ignorable;
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withName(PropertyName newName) {
/* 126 */     return new CreatorProperty(this, newName);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
/* 131 */     if (this._valueDeserializer == deser) {
/* 132 */       return this;
/*     */     }
/*     */     
/* 135 */     NullValueProvider nvp = (this._valueDeserializer == this._nullProvider) ? (NullValueProvider)deser : this._nullProvider;
/* 136 */     return new CreatorProperty(this, deser, nvp);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withNullProvider(NullValueProvider nva) {
/* 141 */     return new CreatorProperty(this, this._valueDeserializer, nva);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fixAccess(DeserializationConfig config) {
/* 146 */     if (this._fallbackSetter != null) {
/* 147 */       this._fallbackSetter.fixAccess(config);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFallbackSetter(SettableBeanProperty fallbackSetter) {
/* 158 */     this._fallbackSetter = fallbackSetter;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markAsIgnorable() {
/* 163 */     this._ignorable = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnorable() {
/* 168 */     return this._ignorable;
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
/*     */   public Object findInjectableValue(DeserializationContext context, Object beanInstance) throws JsonMappingException {
/* 184 */     if (this._injectableValueId == null)
/* 185 */       context.reportBadDefinition(ClassUtil.classOf(beanInstance), 
/* 186 */           String.format("Property '%s' (type %s) has no injectable value id configured", new Object[] {
/* 187 */               getName(), getClass().getName()
/*     */             })); 
/* 189 */     return context.findInjectableValue(this._injectableValueId, (BeanProperty)this, beanInstance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inject(DeserializationContext context, Object beanInstance) throws IOException {
/* 198 */     set(beanInstance, findInjectableValue(context, beanInstance));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) {
/* 209 */     if (this._annotated == null) {
/* 210 */       return null;
/*     */     }
/* 212 */     return (A)this._annotated.getAnnotation(acls);
/*     */   }
/*     */   public AnnotatedMember getMember() {
/* 215 */     return (AnnotatedMember)this._annotated;
/*     */   }
/*     */   public int getCreatorIndex() {
/* 218 */     return this._creatorIndex;
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
/*     */   public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/* 231 */     _verifySetter();
/* 232 */     this._fallbackSetter.set(instance, deserialize(p, ctxt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/* 239 */     _verifySetter();
/* 240 */     return this._fallbackSetter.setAndReturn(instance, deserialize(p, ctxt));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Object instance, Object value) throws IOException {
/* 246 */     _verifySetter();
/* 247 */     this._fallbackSetter.set(instance, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException {
/* 253 */     _verifySetter();
/* 254 */     return this._fallbackSetter.setAndReturn(instance, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyMetadata getMetadata() {
/* 264 */     PropertyMetadata md = super.getMetadata();
/* 265 */     if (this._fallbackSetter != null) {
/* 266 */       return md.withMergeInfo(this._fallbackSetter.getMetadata().getMergeInfo());
/*     */     }
/* 268 */     return md;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getInjectableValueId() {
/* 273 */     return this._injectableValueId;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 277 */     return "[creator property, name '" + getName() + "'; inject id '" + this._injectableValueId + "']";
/*     */   }
/*     */   
/*     */   private final void _verifySetter() throws IOException {
/* 281 */     if (this._fallbackSetter == null) {
/* 282 */       _reportMissingSetter((JsonParser)null, (DeserializationContext)null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void _reportMissingSetter(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 289 */     String msg = "No fallback setter/field defined for creator property '" + getName() + "'";
/*     */ 
/*     */     
/* 292 */     if (ctxt != null) {
/* 293 */       ctxt.reportBadDefinition(getType(), msg);
/*     */     } else {
/* 295 */       throw InvalidDefinitionException.from(p, msg, getType());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\CreatorProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */