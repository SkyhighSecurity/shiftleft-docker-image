/*     */ package com.fasterxml.jackson.databind.exc;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import java.io.Closeable;
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
/*     */ public class InvalidDefinitionException
/*     */   extends JsonMappingException
/*     */ {
/*     */   protected final JavaType _type;
/*     */   protected transient BeanDescription _beanDesc;
/*     */   protected transient BeanPropertyDefinition _property;
/*     */   
/*     */   protected InvalidDefinitionException(JsonParser p, String msg, JavaType type) {
/*  30 */     super((Closeable)p, msg);
/*  31 */     this._type = type;
/*  32 */     this._beanDesc = null;
/*  33 */     this._property = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InvalidDefinitionException(JsonGenerator g, String msg, JavaType type) {
/*  38 */     super((Closeable)g, msg);
/*  39 */     this._type = type;
/*  40 */     this._beanDesc = null;
/*  41 */     this._property = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InvalidDefinitionException(JsonParser p, String msg, BeanDescription bean, BeanPropertyDefinition prop) {
/*  46 */     super((Closeable)p, msg);
/*  47 */     this._type = (bean == null) ? null : bean.getType();
/*  48 */     this._beanDesc = bean;
/*  49 */     this._property = prop;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InvalidDefinitionException(JsonGenerator g, String msg, BeanDescription bean, BeanPropertyDefinition prop) {
/*  54 */     super((Closeable)g, msg);
/*  55 */     this._type = (bean == null) ? null : bean.getType();
/*  56 */     this._beanDesc = bean;
/*  57 */     this._property = prop;
/*     */   }
/*     */ 
/*     */   
/*     */   public static InvalidDefinitionException from(JsonParser p, String msg, BeanDescription bean, BeanPropertyDefinition prop) {
/*  62 */     return new InvalidDefinitionException(p, msg, bean, prop);
/*     */   }
/*     */ 
/*     */   
/*     */   public static InvalidDefinitionException from(JsonParser p, String msg, JavaType type) {
/*  67 */     return new InvalidDefinitionException(p, msg, type);
/*     */   }
/*     */ 
/*     */   
/*     */   public static InvalidDefinitionException from(JsonGenerator g, String msg, BeanDescription bean, BeanPropertyDefinition prop) {
/*  72 */     return new InvalidDefinitionException(g, msg, bean, prop);
/*     */   }
/*     */ 
/*     */   
/*     */   public static InvalidDefinitionException from(JsonGenerator g, String msg, JavaType type) {
/*  77 */     return new InvalidDefinitionException(g, msg, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType getType() {
/*  85 */     return this._type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDescription getBeanDescription() {
/*  93 */     return this._beanDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanPropertyDefinition getProperty() {
/* 102 */     return this._property;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\exc\InvalidDefinitionException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */