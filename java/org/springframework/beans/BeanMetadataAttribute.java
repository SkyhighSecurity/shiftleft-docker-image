/*    */ package org.springframework.beans;
/*    */ 
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ public class BeanMetadataAttribute
/*    */   implements BeanMetadataElement
/*    */ {
/*    */   private final String name;
/*    */   private final Object value;
/*    */   private Object source;
/*    */   
/*    */   public BeanMetadataAttribute(String name, Object value) {
/* 44 */     Assert.notNull(name, "Name must not be null");
/* 45 */     this.name = name;
/* 46 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 54 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 61 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSource(Object source) {
/* 69 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 74 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 80 */     if (this == other) {
/* 81 */       return true;
/*    */     }
/* 83 */     if (!(other instanceof BeanMetadataAttribute)) {
/* 84 */       return false;
/*    */     }
/* 86 */     BeanMetadataAttribute otherMa = (BeanMetadataAttribute)other;
/* 87 */     return (this.name.equals(otherMa.name) && 
/* 88 */       ObjectUtils.nullSafeEquals(this.value, otherMa.value) && 
/* 89 */       ObjectUtils.nullSafeEquals(this.source, otherMa.source));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 94 */     return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 99 */     return "metadata attribute '" + this.name + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\BeanMetadataAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */