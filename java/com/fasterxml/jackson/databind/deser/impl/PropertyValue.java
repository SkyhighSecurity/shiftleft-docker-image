/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
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
/*     */ public abstract class PropertyValue
/*     */ {
/*     */   public final PropertyValue next;
/*     */   public final Object value;
/*     */   
/*     */   protected PropertyValue(PropertyValue next, Object value) {
/*  25 */     this.next = next;
/*  26 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void assign(Object paramObject) throws IOException, JsonProcessingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Regular
/*     */     extends PropertyValue
/*     */   {
/*     */     final SettableBeanProperty _property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Regular(PropertyValue next, Object value, SettableBeanProperty prop) {
/*  54 */       super(next, value);
/*  55 */       this._property = prop;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void assign(Object bean) throws IOException, JsonProcessingException {
/*  62 */       this._property.set(bean, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Any
/*     */     extends PropertyValue
/*     */   {
/*     */     final SettableAnyProperty _property;
/*     */ 
/*     */ 
/*     */     
/*     */     final String _propertyName;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Any(PropertyValue next, Object value, SettableAnyProperty prop, String propName) {
/*  82 */       super(next, value);
/*  83 */       this._property = prop;
/*  84 */       this._propertyName = propName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void assign(Object bean) throws IOException, JsonProcessingException {
/*  91 */       this._property.set(bean, this._propertyName, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Map
/*     */     extends PropertyValue
/*     */   {
/*     */     final Object _key;
/*     */ 
/*     */ 
/*     */     
/*     */     public Map(PropertyValue next, Object value, Object key) {
/* 106 */       super(next, value);
/* 107 */       this._key = key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void assign(Object bean) throws IOException, JsonProcessingException {
/* 115 */       ((java.util.Map<Object, Object>)bean).put(this._key, this.value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\PropertyValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */