/*    */ package org.springframework.aop.aspectj.annotation;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.beans.factory.BeanFactory;
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
/*    */ public class PrototypeAspectInstanceFactory
/*    */   extends BeanFactoryAspectInstanceFactory
/*    */   implements Serializable
/*    */ {
/*    */   public PrototypeAspectInstanceFactory(BeanFactory beanFactory, String name) {
/* 48 */     super(beanFactory, name);
/* 49 */     if (!beanFactory.isPrototype(name))
/* 50 */       throw new IllegalArgumentException("Cannot use PrototypeAspectInstanceFactory with bean named '" + name + "': not a prototype"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\PrototypeAspectInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */