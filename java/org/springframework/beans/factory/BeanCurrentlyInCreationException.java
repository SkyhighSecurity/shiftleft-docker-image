/*    */ package org.springframework.beans.factory;
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
/*    */ public class BeanCurrentlyInCreationException
/*    */   extends BeanCreationException
/*    */ {
/*    */   public BeanCurrentlyInCreationException(String beanName) {
/* 35 */     super(beanName, "Requested bean is currently in creation: Is there an unresolvable circular reference?");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BeanCurrentlyInCreationException(String beanName, String msg) {
/* 45 */     super(beanName, msg);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\BeanCurrentlyInCreationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */