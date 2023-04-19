/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MethodProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedMethod _annotated;
/*     */   protected final transient Method _setter;
/*     */   protected final boolean _skipNulls;
/*     */   
/*     */   public MethodProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedMethod method) {
/*  42 */     super(propDef, type, typeDeser, contextAnnotations);
/*  43 */     this._annotated = method;
/*  44 */     this._setter = method.getAnnotated();
/*  45 */     this._skipNulls = NullsConstantProvider.isSkipper(this._nullProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MethodProperty(MethodProperty src, JsonDeserializer<?> deser, NullValueProvider nva) {
/*  50 */     super(src, deser, nva);
/*  51 */     this._annotated = src._annotated;
/*  52 */     this._setter = src._setter;
/*  53 */     this._skipNulls = NullsConstantProvider.isSkipper(nva);
/*     */   }
/*     */   
/*     */   protected MethodProperty(MethodProperty src, PropertyName newName) {
/*  57 */     super(src, newName);
/*  58 */     this._annotated = src._annotated;
/*  59 */     this._setter = src._setter;
/*  60 */     this._skipNulls = src._skipNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MethodProperty(MethodProperty src, Method m) {
/*  67 */     super(src);
/*  68 */     this._annotated = src._annotated;
/*  69 */     this._setter = m;
/*  70 */     this._skipNulls = src._skipNulls;
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withName(PropertyName newName) {
/*  75 */     return new MethodProperty(this, newName);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
/*  80 */     if (this._valueDeserializer == deser) {
/*  81 */       return this;
/*     */     }
/*     */     
/*  84 */     NullValueProvider nvp = (this._valueDeserializer == this._nullProvider) ? (NullValueProvider)deser : this._nullProvider;
/*  85 */     return new MethodProperty(this, deser, nvp);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withNullProvider(NullValueProvider nva) {
/*  90 */     return new MethodProperty(this, this._valueDeserializer, nva);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fixAccess(DeserializationConfig config) {
/*  95 */     this._annotated.fixAccess(config
/*  96 */         .isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
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
/* 107 */     return (this._annotated == null) ? null : (A)this._annotated.getAnnotation(acls);
/*     */   }
/*     */   public AnnotatedMember getMember() {
/* 110 */     return (AnnotatedMember)this._annotated;
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
/*     */   public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/*     */     Object value;
/* 123 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/* 124 */       if (this._skipNulls) {
/*     */         return;
/*     */       }
/* 127 */       value = this._nullProvider.getNullValue(ctxt);
/* 128 */     } else if (this._valueTypeDeserializer == null) {
/* 129 */       value = this._valueDeserializer.deserialize(p, ctxt);
/*     */       
/* 131 */       if (value == null) {
/* 132 */         if (this._skipNulls) {
/*     */           return;
/*     */         }
/* 135 */         value = this._nullProvider.getNullValue(ctxt);
/*     */       } 
/*     */     } else {
/* 138 */       value = this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     } 
/*     */     try {
/* 141 */       this._setter.invoke(instance, new Object[] { value });
/* 142 */     } catch (Exception e) {
/* 143 */       _throwAsIOE(p, e, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/*     */     Object value;
/* 152 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/* 153 */       if (this._skipNulls) {
/* 154 */         return instance;
/*     */       }
/* 156 */       value = this._nullProvider.getNullValue(ctxt);
/* 157 */     } else if (this._valueTypeDeserializer == null) {
/* 158 */       value = this._valueDeserializer.deserialize(p, ctxt);
/*     */       
/* 160 */       if (value == null) {
/* 161 */         if (this._skipNulls) {
/* 162 */           return instance;
/*     */         }
/* 164 */         value = this._nullProvider.getNullValue(ctxt);
/*     */       } 
/*     */     } else {
/* 167 */       value = this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     } 
/*     */     try {
/* 170 */       Object result = this._setter.invoke(instance, new Object[] { value });
/* 171 */       return (result == null) ? instance : result;
/* 172 */     } catch (Exception e) {
/* 173 */       _throwAsIOE(p, e, value);
/* 174 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(Object instance, Object value) throws IOException {
/*     */     try {
/* 182 */       this._setter.invoke(instance, new Object[] { value });
/* 183 */     } catch (Exception e) {
/*     */       
/* 185 */       _throwAsIOE(e, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException {
/*     */     try {
/* 193 */       Object result = this._setter.invoke(instance, new Object[] { value });
/* 194 */       return (result == null) ? instance : result;
/* 195 */     } catch (Exception e) {
/*     */       
/* 197 */       _throwAsIOE(e, value);
/* 198 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object readResolve() {
/* 209 */     return new MethodProperty(this, this._annotated.getAnnotated());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\MethodProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */