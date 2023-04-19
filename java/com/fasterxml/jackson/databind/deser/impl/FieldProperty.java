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
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FieldProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedField _annotated;
/*     */   protected final transient Field _field;
/*     */   protected final boolean _skipNulls;
/*     */   
/*     */   public FieldProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedField field) {
/*  46 */     super(propDef, type, typeDeser, contextAnnotations);
/*  47 */     this._annotated = field;
/*  48 */     this._field = field.getAnnotated();
/*  49 */     this._skipNulls = NullsConstantProvider.isSkipper(this._nullProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FieldProperty(FieldProperty src, JsonDeserializer<?> deser, NullValueProvider nva) {
/*  54 */     super(src, deser, nva);
/*  55 */     this._annotated = src._annotated;
/*  56 */     this._field = src._field;
/*  57 */     this._skipNulls = NullsConstantProvider.isSkipper(nva);
/*     */   }
/*     */   
/*     */   protected FieldProperty(FieldProperty src, PropertyName newName) {
/*  61 */     super(src, newName);
/*  62 */     this._annotated = src._annotated;
/*  63 */     this._field = src._field;
/*  64 */     this._skipNulls = src._skipNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FieldProperty(FieldProperty src) {
/*  72 */     super(src);
/*  73 */     this._annotated = src._annotated;
/*  74 */     Field f = this._annotated.getAnnotated();
/*  75 */     if (f == null) {
/*  76 */       throw new IllegalArgumentException("Missing field (broken JDK (de)serialization?)");
/*     */     }
/*  78 */     this._field = f;
/*  79 */     this._skipNulls = src._skipNulls;
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withName(PropertyName newName) {
/*  84 */     return new FieldProperty(this, newName);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
/*  89 */     if (this._valueDeserializer == deser) {
/*  90 */       return this;
/*     */     }
/*     */     
/*  93 */     NullValueProvider nvp = (this._valueDeserializer == this._nullProvider) ? (NullValueProvider)deser : this._nullProvider;
/*  94 */     return new FieldProperty(this, deser, nvp);
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty withNullProvider(NullValueProvider nva) {
/*  99 */     return new FieldProperty(this, this._valueDeserializer, nva);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fixAccess(DeserializationConfig config) {
/* 104 */     ClassUtil.checkAndFixAccess(this._field, config
/* 105 */         .isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
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
/* 116 */     return (this._annotated == null) ? null : (A)this._annotated.getAnnotation(acls);
/*     */   }
/*     */   public AnnotatedMember getMember() {
/* 119 */     return (AnnotatedMember)this._annotated;
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
/* 132 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/* 133 */       if (this._skipNulls) {
/*     */         return;
/*     */       }
/* 136 */       value = this._nullProvider.getNullValue(ctxt);
/* 137 */     } else if (this._valueTypeDeserializer == null) {
/* 138 */       value = this._valueDeserializer.deserialize(p, ctxt);
/*     */       
/* 140 */       if (value == null) {
/* 141 */         if (this._skipNulls) {
/*     */           return;
/*     */         }
/* 144 */         value = this._nullProvider.getNullValue(ctxt);
/*     */       } 
/*     */     } else {
/* 147 */       value = this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     } 
/*     */     try {
/* 150 */       this._field.set(instance, value);
/* 151 */     } catch (Exception e) {
/* 152 */       _throwAsIOE(p, e, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
/*     */     Object value;
/* 161 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/* 162 */       if (this._skipNulls) {
/* 163 */         return instance;
/*     */       }
/* 165 */       value = this._nullProvider.getNullValue(ctxt);
/* 166 */     } else if (this._valueTypeDeserializer == null) {
/* 167 */       value = this._valueDeserializer.deserialize(p, ctxt);
/*     */       
/* 169 */       if (value == null) {
/* 170 */         if (this._skipNulls) {
/* 171 */           return instance;
/*     */         }
/* 173 */         value = this._nullProvider.getNullValue(ctxt);
/*     */       } 
/*     */     } else {
/* 176 */       value = this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     } 
/*     */     try {
/* 179 */       this._field.set(instance, value);
/* 180 */     } catch (Exception e) {
/* 181 */       _throwAsIOE(p, e, value);
/*     */     } 
/* 183 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Object instance, Object value) throws IOException {
/*     */     try {
/* 190 */       this._field.set(instance, value);
/* 191 */     } catch (Exception e) {
/*     */       
/* 193 */       _throwAsIOE(e, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException {
/*     */     try {
/* 201 */       this._field.set(instance, value);
/* 202 */     } catch (Exception e) {
/*     */       
/* 204 */       _throwAsIOE(e, value);
/*     */     } 
/* 206 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object readResolve() {
/* 216 */     return new FieldProperty(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\FieldProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */