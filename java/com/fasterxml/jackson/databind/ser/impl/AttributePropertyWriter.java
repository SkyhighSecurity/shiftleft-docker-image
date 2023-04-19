/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*    */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*    */ import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
/*    */ import com.fasterxml.jackson.databind.util.Annotations;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributePropertyWriter
/*    */   extends VirtualBeanPropertyWriter
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final String _attrName;
/*    */   
/*    */   protected AttributePropertyWriter(String attrName, BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType) {
/* 35 */     this(attrName, propDef, contextAnnotations, declaredType, propDef.findInclusion());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AttributePropertyWriter(String attrName, BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType, JsonInclude.Value inclusion) {
/* 42 */     super(propDef, contextAnnotations, declaredType, null, null, null, inclusion, null);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     this._attrName = attrName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AttributePropertyWriter construct(String attrName, BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType) {
/* 55 */     return new AttributePropertyWriter(attrName, propDef, contextAnnotations, declaredType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AttributePropertyWriter(AttributePropertyWriter base) {
/* 60 */     super(base);
/* 61 */     this._attrName = base._attrName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VirtualBeanPropertyWriter withConfig(MapperConfig<?> config, AnnotatedClass declaringClass, BeanPropertyDefinition propDef, JavaType type) {
/* 71 */     throw new IllegalStateException("Should not be called on this type");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object value(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
/* 82 */     return prov.getAttribute(this._attrName);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\AttributePropertyWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */