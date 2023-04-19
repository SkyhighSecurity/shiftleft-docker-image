/*    */ package com.fasterxml.jackson.databind.deser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*    */ import com.fasterxml.jackson.databind.util.Annotations;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValueInjector
/*    */   extends BeanProperty.Std
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final Object _valueId;
/*    */   
/*    */   public ValueInjector(PropertyName propName, JavaType type, AnnotatedMember mutator, Object valueId) {
/* 27 */     super(propName, type, null, mutator, PropertyMetadata.STD_OPTIONAL);
/* 28 */     this._valueId = valueId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public ValueInjector(PropertyName propName, JavaType type, Annotations contextAnnotations, AnnotatedMember mutator, Object valueId) {
/* 39 */     this(propName, type, mutator, valueId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object findValue(DeserializationContext context, Object beanInstance) throws JsonMappingException {
/* 45 */     return context.findInjectableValue(this._valueId, (BeanProperty)this, beanInstance);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void inject(DeserializationContext context, Object beanInstance) throws IOException {
/* 51 */     this._member.setValue(beanInstance, findValue(context, beanInstance));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\ValueInjector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */