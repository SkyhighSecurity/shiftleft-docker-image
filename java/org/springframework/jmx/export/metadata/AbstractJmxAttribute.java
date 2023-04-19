/*    */ package org.springframework.jmx.export.metadata;
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
/*    */ public class AbstractJmxAttribute
/*    */ {
/* 27 */   private String description = "";
/*    */   
/* 29 */   private int currencyTimeLimit = -1;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDescription(String description) {
/* 36 */     this.description = description;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 43 */     return this.description;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCurrencyTimeLimit(int currencyTimeLimit) {
/* 50 */     this.currencyTimeLimit = currencyTimeLimit;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCurrencyTimeLimit() {
/* 57 */     return this.currencyTimeLimit;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\metadata\AbstractJmxAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */