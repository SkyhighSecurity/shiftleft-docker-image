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
/*     */ public abstract class NestedCheckedException
/*     */   extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 7100714597678207546L;
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
/*     */   public NestedCheckedException(String msg) {
/*  54 */     super(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NestedCheckedException(String msg, Throwable cause) {
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
/*     */   public Throwable getRootCause() {
/*  83 */     return NestedExceptionUtils.getRootCause(this);
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
/*  95 */     Throwable rootCause = getRootCause();
/*  96 */     return (rootCause != null) ? rootCause : this;
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
/* 107 */     if (exType == null) {
/* 108 */       return false;
/*     */     }
/* 110 */     if (exType.isInstance(this)) {
/* 111 */       return true;
/*     */     }
/* 113 */     Throwable cause = getCause();
/* 114 */     if (cause == this) {
/* 115 */       return false;
/*     */     }
/* 117 */     if (cause instanceof NestedCheckedException) {
/* 118 */       return ((NestedCheckedException)cause).contains(exType);
/*     */     }
/*     */     
/* 121 */     while (cause != null) {
/* 122 */       if (exType.isInstance(cause)) {
/* 123 */         return true;
/*     */       }
/* 125 */       if (cause.getCause() == cause) {
/*     */         break;
/*     */       }
/* 128 */       cause = cause.getCause();
/*     */     } 
/* 130 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\NestedCheckedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */