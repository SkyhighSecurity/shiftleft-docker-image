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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeanIsNotAFactoryException
/*    */   extends BeanNotOfRequiredTypeException
/*    */ {
/*    */   public BeanIsNotAFactoryException(String name, Class<?> actualType) {
/* 38 */     super(name, FactoryBean.class, actualType);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\BeanIsNotAFactoryException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */