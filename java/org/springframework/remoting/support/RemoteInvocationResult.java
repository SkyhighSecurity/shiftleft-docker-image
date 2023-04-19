/*     */ package org.springframework.remoting.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RemoteInvocationResult
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2138555143707773549L;
/*     */   private Object value;
/*     */   private Throwable exception;
/*     */   
/*     */   public RemoteInvocationResult(Object value) {
/*  53 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteInvocationResult(Throwable exception) {
/*  62 */     this.exception = exception;
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
/*     */   public RemoteInvocationResult() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) {
/*  83 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue() {
/*  92 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setException(Throwable exception) {
/* 103 */     this.exception = exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getException() {
/* 112 */     return this.exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasException() {
/* 123 */     return (this.exception != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasInvocationTargetException() {
/* 132 */     return this.exception instanceof InvocationTargetException;
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
/*     */   public Object recreate() throws Throwable {
/* 144 */     if (this.exception != null) {
/* 145 */       Throwable exToThrow = this.exception;
/* 146 */       if (this.exception instanceof InvocationTargetException) {
/* 147 */         exToThrow = ((InvocationTargetException)this.exception).getTargetException();
/*     */       }
/* 149 */       RemoteInvocationUtils.fillInClientStackTraceIfPossible(exToThrow);
/* 150 */       throw exToThrow;
/*     */     } 
/*     */     
/* 153 */     return this.value;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\RemoteInvocationResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */