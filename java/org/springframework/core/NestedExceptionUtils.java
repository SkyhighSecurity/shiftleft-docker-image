/*    */ package org.springframework.core;
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
/*    */ public abstract class NestedExceptionUtils
/*    */ {
/*    */   public static String buildMessage(String message, Throwable cause) {
/* 42 */     if (cause == null) {
/* 43 */       return message;
/*    */     }
/* 45 */     StringBuilder sb = new StringBuilder(64);
/* 46 */     if (message != null) {
/* 47 */       sb.append(message).append("; ");
/*    */     }
/* 49 */     sb.append("nested exception is ").append(cause);
/* 50 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Throwable getRootCause(Throwable original) {
/* 60 */     if (original == null) {
/* 61 */       return null;
/*    */     }
/* 63 */     Throwable rootCause = null;
/* 64 */     Throwable cause = original.getCause();
/* 65 */     while (cause != null && cause != rootCause) {
/* 66 */       rootCause = cause;
/* 67 */       cause = cause.getCause();
/*    */     } 
/* 69 */     return rootCause;
/*    */   }
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
/*    */   public static Throwable getMostSpecificCause(Throwable original) {
/* 82 */     Throwable rootCause = getRootCause(original);
/* 83 */     return (rootCause != null) ? rootCause : original;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\NestedExceptionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */