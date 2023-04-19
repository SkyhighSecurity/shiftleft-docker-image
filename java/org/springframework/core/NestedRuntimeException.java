/*     */ package org.springframework.core;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NestedRuntimeException
/*     */   extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 5439915454935047936L;
/*     */   
/*     */   static {
/*  45 */     NestedExceptionUtils.class.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NestedRuntimeException(String msg) {
/*  54 */     super(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NestedRuntimeException(String msg, Throwable cause) {
/*  64 */     super(msg, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  74 */     return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getRootCause() {
/*  84 */     return NestedExceptionUtils.getRootCause(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getMostSpecificCause() {
/*  96 */     Throwable rootCause = getRootCause();
/*  97 */     return (rootCause != null) ? rootCause : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Class<?> exType) {
/* 108 */     if (exType == null) {
/* 109 */       return false;
/*     */     }
/* 111 */     if (exType.isInstance(this)) {
/* 112 */       return true;
/*     */     }
/* 114 */     Throwable cause = getCause();
/* 115 */     if (cause == this) {
/* 116 */       return false;
/*     */     }
/* 118 */     if (cause instanceof NestedRuntimeException) {
/* 119 */       return ((NestedRuntimeException)cause).contains(exType);
/*     */     }
/*     */     
/* 122 */     while (cause != null) {
/* 123 */       if (exType.isInstance(cause)) {
/* 124 */         return true;
/*     */       }
/* 126 */       if (cause.getCause() == cause) {
/*     */         break;
/*     */       }
/* 129 */       cause = cause.getCause();
/*     */     } 
/* 131 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\NestedRuntimeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */