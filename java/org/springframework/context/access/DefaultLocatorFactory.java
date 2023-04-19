/*    */ package org.springframework.context.access;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
/*    */ import org.springframework.beans.factory.access.BeanFactoryLocator;
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
/*    */ public class DefaultLocatorFactory
/*    */ {
/*    */   public static BeanFactoryLocator getInstance() throws FatalBeanException {
/* 36 */     return ContextSingletonBeanFactoryLocator.getInstance();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BeanFactoryLocator getInstance(String selector) throws FatalBeanException {
/* 47 */     return ContextSingletonBeanFactoryLocator.getInstance(selector);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\access\DefaultLocatorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */