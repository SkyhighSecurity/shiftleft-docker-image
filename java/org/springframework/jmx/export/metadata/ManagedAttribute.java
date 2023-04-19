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
/*    */ 
/*    */ 
/*    */ public class ManagedAttribute
/*    */   extends AbstractJmxAttribute
/*    */ {
/* 30 */   public static final ManagedAttribute EMPTY = new ManagedAttribute();
/*    */ 
/*    */   
/*    */   private Object defaultValue;
/*    */   
/*    */   private String persistPolicy;
/*    */   
/* 37 */   private int persistPeriod = -1;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefaultValue(Object defaultValue) {
/* 44 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getDefaultValue() {
/* 51 */     return this.defaultValue;
/*    */   }
/*    */   
/*    */   public void setPersistPolicy(String persistPolicy) {
/* 55 */     this.persistPolicy = persistPolicy;
/*    */   }
/*    */   
/*    */   public String getPersistPolicy() {
/* 59 */     return this.persistPolicy;
/*    */   }
/*    */   
/*    */   public void setPersistPeriod(int persistPeriod) {
/* 63 */     this.persistPeriod = persistPeriod;
/*    */   }
/*    */   
/*    */   public int getPersistPeriod() {
/* 67 */     return this.persistPeriod;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\metadata\ManagedAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */