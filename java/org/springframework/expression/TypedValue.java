/*    */ package org.springframework.expression;
/*    */ 
/*    */ import org.springframework.core.convert.TypeDescriptor;
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
/*    */ public class TypedValue
/*    */ {
/* 33 */   public static final TypedValue NULL = new TypedValue(null);
/*    */ 
/*    */ 
/*    */   
/*    */   private final Object value;
/*    */ 
/*    */ 
/*    */   
/*    */   private TypeDescriptor typeDescriptor;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue(Object value) {
/* 47 */     this.value = value;
/* 48 */     this.typeDescriptor = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue(Object value, TypeDescriptor typeDescriptor) {
/* 58 */     this.value = value;
/* 59 */     this.typeDescriptor = typeDescriptor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 64 */     return this.value;
/*    */   }
/*    */   
/*    */   public TypeDescriptor getTypeDescriptor() {
/* 68 */     if (this.typeDescriptor == null && this.value != null) {
/* 69 */       this.typeDescriptor = TypeDescriptor.forObject(this.value);
/*    */     }
/* 71 */     return this.typeDescriptor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 77 */     if (this == other) {
/* 78 */       return true;
/*    */     }
/* 80 */     if (!(other instanceof TypedValue)) {
/* 81 */       return false;
/*    */     }
/* 83 */     TypedValue otherTv = (TypedValue)other;
/*    */     
/* 85 */     return (ObjectUtils.nullSafeEquals(this.value, otherTv.value) && ((this.typeDescriptor == null && otherTv.typeDescriptor == null) || 
/*    */       
/* 87 */       ObjectUtils.nullSafeEquals(getTypeDescriptor(), otherTv.getTypeDescriptor())));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 92 */     return ObjectUtils.nullSafeHashCode(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 97 */     return "TypedValue: '" + this.value + "' of [" + getTypeDescriptor() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\TypedValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */