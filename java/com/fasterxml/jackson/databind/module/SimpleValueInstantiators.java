/*    */ package com.fasterxml.jackson.databind.module;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanDescription;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*    */ import com.fasterxml.jackson.databind.deser.ValueInstantiators;
/*    */ import com.fasterxml.jackson.databind.type.ClassKey;
/*    */ import java.io.Serializable;
/*    */ import java.util.HashMap;
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
/*    */ public class SimpleValueInstantiators
/*    */   extends ValueInstantiators.Base
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -8929386427526115130L;
/* 31 */   protected HashMap<ClassKey, ValueInstantiator> _classMappings = new HashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleValueInstantiators addValueInstantiator(Class<?> forType, ValueInstantiator inst) {
/* 37 */     this._classMappings.put(new ClassKey(forType), inst);
/* 38 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ValueInstantiator findValueInstantiator(DeserializationConfig config, BeanDescription beanDesc, ValueInstantiator defaultInstantiator) {
/* 45 */     ValueInstantiator inst = this._classMappings.get(new ClassKey(beanDesc.getBeanClass()));
/* 46 */     return (inst == null) ? defaultInstantiator : inst;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\module\SimpleValueInstantiators.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */