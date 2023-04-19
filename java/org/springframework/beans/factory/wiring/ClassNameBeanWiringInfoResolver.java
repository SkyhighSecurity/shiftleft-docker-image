/*    */ package org.springframework.beans.factory.wiring;
/*    */ 
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class ClassNameBeanWiringInfoResolver
/*    */   implements BeanWiringInfoResolver
/*    */ {
/*    */   public BeanWiringInfo resolveWiringInfo(Object beanInstance) {
/* 36 */     Assert.notNull(beanInstance, "Bean instance must not be null");
/* 37 */     return new BeanWiringInfo(ClassUtils.getUserClass(beanInstance).getName(), true);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\wiring\ClassNameBeanWiringInfoResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */