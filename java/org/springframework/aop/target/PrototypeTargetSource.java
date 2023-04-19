/*    */ package org.springframework.aop.target;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
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
/*    */ public class PrototypeTargetSource
/*    */   extends AbstractPrototypeBasedTargetSource
/*    */ {
/*    */   public Object getTarget() throws BeansException {
/* 43 */     return newPrototypeInstance();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void releaseTarget(Object target) {
/* 52 */     destroyPrototypeInstance(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "PrototypeTargetSource for target bean with name '" + getTargetBeanName() + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\target\PrototypeTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */