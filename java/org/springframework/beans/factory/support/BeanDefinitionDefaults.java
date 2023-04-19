/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class BeanDefinitionDefaults
/*    */ {
/*    */   private boolean lazyInit;
/* 31 */   private int dependencyCheck = 0;
/*    */   
/* 33 */   private int autowireMode = 0;
/*    */   
/*    */   private String initMethodName;
/*    */   
/*    */   private String destroyMethodName;
/*    */ 
/*    */   
/*    */   public void setLazyInit(boolean lazyInit) {
/* 41 */     this.lazyInit = lazyInit;
/*    */   }
/*    */   
/*    */   public boolean isLazyInit() {
/* 45 */     return this.lazyInit;
/*    */   }
/*    */   
/*    */   public void setDependencyCheck(int dependencyCheck) {
/* 49 */     this.dependencyCheck = dependencyCheck;
/*    */   }
/*    */   
/*    */   public int getDependencyCheck() {
/* 53 */     return this.dependencyCheck;
/*    */   }
/*    */   
/*    */   public void setAutowireMode(int autowireMode) {
/* 57 */     this.autowireMode = autowireMode;
/*    */   }
/*    */   
/*    */   public int getAutowireMode() {
/* 61 */     return this.autowireMode;
/*    */   }
/*    */   
/*    */   public void setInitMethodName(String initMethodName) {
/* 65 */     this.initMethodName = StringUtils.hasText(initMethodName) ? initMethodName : null;
/*    */   }
/*    */   
/*    */   public String getInitMethodName() {
/* 69 */     return this.initMethodName;
/*    */   }
/*    */   
/*    */   public void setDestroyMethodName(String destroyMethodName) {
/* 73 */     this.destroyMethodName = StringUtils.hasText(destroyMethodName) ? destroyMethodName : null;
/*    */   }
/*    */   
/*    */   public String getDestroyMethodName() {
/* 77 */     return this.destroyMethodName;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\BeanDefinitionDefaults.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */