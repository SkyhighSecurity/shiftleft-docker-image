/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import javax.naming.NamingException;
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
/*    */ public class TypeMismatchNamingException
/*    */   extends NamingException
/*    */ {
/*    */   private Class<?> requiredType;
/*    */   private Class<?> actualType;
/*    */   
/*    */   public TypeMismatchNamingException(String jndiName, Class<?> requiredType, Class<?> actualType) {
/* 45 */     super("Object of type [" + actualType + "] available at JNDI location [" + jndiName + "] is not assignable to [" + requiredType
/* 46 */         .getName() + "]");
/* 47 */     this.requiredType = requiredType;
/* 48 */     this.actualType = actualType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public TypeMismatchNamingException(String explanation) {
/* 58 */     super(explanation);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Class<?> getRequiredType() {
/* 66 */     return this.requiredType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Class<?> getActualType() {
/* 73 */     return this.actualType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jndi\TypeMismatchNamingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */