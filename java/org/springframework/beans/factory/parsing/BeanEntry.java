/*    */ package org.springframework.beans.factory.parsing;
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
/*    */ public class BeanEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private String beanDefinitionName;
/*    */   
/*    */   public BeanEntry(String beanDefinitionName) {
/* 35 */     this.beanDefinitionName = beanDefinitionName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return "Bean '" + this.beanDefinitionName + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\BeanEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */