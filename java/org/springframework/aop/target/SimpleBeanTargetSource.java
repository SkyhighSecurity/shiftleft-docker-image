/*    */ package org.springframework.aop.target;
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
/*    */ public class SimpleBeanTargetSource
/*    */   extends AbstractBeanFactoryBasedTargetSource
/*    */ {
/*    */   public Object getTarget() throws Exception {
/* 35 */     return getBeanFactory().getBean(getTargetBeanName());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\target\SimpleBeanTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */