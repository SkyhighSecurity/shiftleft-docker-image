/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.core.Ordered;
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
/*    */ public class DefaultEventListenerFactory
/*    */   implements EventListenerFactory, Ordered
/*    */ {
/* 35 */   private int order = Integer.MAX_VALUE;
/*    */ 
/*    */   
/*    */   public void setOrder(int order) {
/* 39 */     this.order = order;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 44 */     return this.order;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportsMethod(Method method) {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
/* 54 */     return new ApplicationListenerMethodAdapter(beanName, type, method);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\event\DefaultEventListenerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */