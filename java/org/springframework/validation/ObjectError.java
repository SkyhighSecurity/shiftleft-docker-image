/*    */ package org.springframework.validation;
/*    */ 
/*    */ import org.springframework.context.support.DefaultMessageSourceResolvable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ObjectError
/*    */   extends DefaultMessageSourceResolvable
/*    */ {
/*    */   private final String objectName;
/*    */   
/*    */   public ObjectError(String objectName, String defaultMessage) {
/* 46 */     this(objectName, null, null, defaultMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectError(String objectName, String[] codes, Object[] arguments, String defaultMessage) {
/* 57 */     super(codes, arguments, defaultMessage);
/* 58 */     Assert.notNull(objectName, "Object name must not be null");
/* 59 */     this.objectName = objectName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getObjectName() {
/* 67 */     return this.objectName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 73 */     if (this == other) {
/* 74 */       return true;
/*    */     }
/* 76 */     if (other == null || other.getClass() != getClass() || !super.equals(other)) {
/* 77 */       return false;
/*    */     }
/* 79 */     ObjectError otherError = (ObjectError)other;
/* 80 */     return getObjectName().equals(otherError.getObjectName());
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 85 */     return super.hashCode() * 29 + getObjectName().hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     return "Error in object '" + this.objectName + "': " + resolvableToString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\ObjectError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */