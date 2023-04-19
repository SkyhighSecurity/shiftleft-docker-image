/*    */ package org.springframework.aop.framework.autoproxy;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanNameAware;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultAdvisorAutoProxyCreator
/*    */   extends AbstractAdvisorAutoProxyCreator
/*    */   implements BeanNameAware
/*    */ {
/*    */   public static final String SEPARATOR = ".";
/*    */   private boolean usePrefix = false;
/*    */   private String advisorBeanNamePrefix;
/*    */   
/*    */   public void setUsePrefix(boolean usePrefix) {
/* 55 */     this.usePrefix = usePrefix;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isUsePrefix() {
/* 62 */     return this.usePrefix;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAdvisorBeanNamePrefix(String advisorBeanNamePrefix) {
/* 72 */     this.advisorBeanNamePrefix = advisorBeanNamePrefix;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAdvisorBeanNamePrefix() {
/* 80 */     return this.advisorBeanNamePrefix;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBeanName(String name) {
/* 86 */     if (this.advisorBeanNamePrefix == null) {
/* 87 */       this.advisorBeanNamePrefix = name + ".";
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isEligibleAdvisorBean(String beanName) {
/* 99 */     return (!isUsePrefix() || beanName.startsWith(getAdvisorBeanNamePrefix()));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\DefaultAdvisorAutoProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */