/*    */ package org.springframework.aop.aspectj.annotation;
/*    */ 
/*    */ import org.springframework.aop.framework.AopConfigException;
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
/*    */ public class NotAnAtAspectException
/*    */   extends AopConfigException
/*    */ {
/*    */   private Class<?> nonAspectClass;
/*    */   
/*    */   public NotAnAtAspectException(Class<?> nonAspectClass) {
/* 40 */     super(nonAspectClass.getName() + " is not an @AspectJ aspect");
/* 41 */     this.nonAspectClass = nonAspectClass;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getNonAspectClass() {
/* 48 */     return this.nonAspectClass;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\annotation\NotAnAtAspectException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */