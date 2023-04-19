/*    */ package org.springframework.web.context.request;
/*    */ 
/*    */ import org.springframework.beans.factory.ObjectFactory;
/*    */ import org.springframework.beans.factory.config.Scope;
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
/*    */ public abstract class AbstractRequestAttributesScope
/*    */   implements Scope
/*    */ {
/*    */   public Object get(String name, ObjectFactory<?> objectFactory) {
/* 41 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 42 */     Object scopedObject = attributes.getAttribute(name, getScope());
/* 43 */     if (scopedObject == null) {
/* 44 */       scopedObject = objectFactory.getObject();
/* 45 */       attributes.setAttribute(name, scopedObject, getScope());
/*    */ 
/*    */       
/* 48 */       Object retrievedObject = attributes.getAttribute(name, getScope());
/* 49 */       if (retrievedObject != null)
/*    */       {
/*    */         
/* 52 */         scopedObject = retrievedObject;
/*    */       }
/*    */     } 
/* 55 */     return scopedObject;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object remove(String name) {
/* 60 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 61 */     Object scopedObject = attributes.getAttribute(name, getScope());
/* 62 */     if (scopedObject != null) {
/* 63 */       attributes.removeAttribute(name, getScope());
/* 64 */       return scopedObject;
/*    */     } 
/*    */     
/* 67 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerDestructionCallback(String name, Runnable callback) {
/* 73 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 74 */     attributes.registerDestructionCallback(name, callback, getScope());
/*    */   }
/*    */ 
/*    */   
/*    */   public Object resolveContextualObject(String key) {
/* 79 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 80 */     return attributes.resolveReference(key);
/*    */   }
/*    */   
/*    */   protected abstract int getScope();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\AbstractRequestAttributesScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */