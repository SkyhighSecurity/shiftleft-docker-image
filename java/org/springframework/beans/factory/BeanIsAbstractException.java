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
/*    */ public class BeanIsAbstractException
/*    */   extends BeanCreationException
/*    */ {
/*    */   public BeanIsAbstractException(String beanName) {
/* 35 */     super(beanName, "Bean definition is abstract");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\BeanIsAbstractException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */