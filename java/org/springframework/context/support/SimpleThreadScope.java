/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.beans.factory.ObjectFactory;
/*    */ import org.springframework.beans.factory.config.Scope;
/*    */ import org.springframework.core.NamedThreadLocal;
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
/*    */ public class SimpleThreadScope
/*    */   implements Scope
/*    */ {
/* 56 */   private static final Log logger = LogFactory.getLog(SimpleThreadScope.class);
/*    */   
/* 58 */   private final ThreadLocal<Map<String, Object>> threadScope = (ThreadLocal<Map<String, Object>>)new NamedThreadLocal<Map<String, Object>>("SimpleThreadScope")
/*    */     {
/*    */       protected Map<String, Object> initialValue()
/*    */       {
/* 62 */         return new HashMap<String, Object>();
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */   
/*    */   public Object get(String name, ObjectFactory<?> objectFactory) {
/* 69 */     Map<String, Object> scope = this.threadScope.get();
/* 70 */     Object scopedObject = scope.get(name);
/* 71 */     if (scopedObject == null) {
/* 72 */       scopedObject = objectFactory.getObject();
/* 73 */       scope.put(name, scopedObject);
/*    */     } 
/* 75 */     return scopedObject;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object remove(String name) {
/* 80 */     Map<String, Object> scope = this.threadScope.get();
/* 81 */     return scope.remove(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerDestructionCallback(String name, Runnable callback) {
/* 86 */     logger.warn("SimpleThreadScope does not support destruction callbacks. Consider using RequestScope in a web environment.");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object resolveContextualObject(String key) {
/* 92 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConversationId() {
/* 97 */     return Thread.currentThread().getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\SimpleThreadScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */