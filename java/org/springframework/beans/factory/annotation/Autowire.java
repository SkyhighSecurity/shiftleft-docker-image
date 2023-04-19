/*    */ package org.springframework.beans.factory.annotation;
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
/*    */ public enum Autowire
/*    */ {
/* 40 */   NO(0),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   BY_NAME(1),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   BY_TYPE(2);
/*    */ 
/*    */   
/*    */   private final int value;
/*    */ 
/*    */   
/*    */   Autowire(int value) {
/* 57 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int value() {
/* 61 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAutowire() {
/* 70 */     return (this == BY_NAME || this == BY_TYPE);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\Autowire.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */