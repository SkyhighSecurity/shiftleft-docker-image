/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import org.springframework.util.Assert;
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
/*     */ @Deprecated
/*     */ public abstract class ControlFlowFactory
/*     */ {
/*     */   public static ControlFlow createControlFlow() {
/*  43 */     return new Jdk14ControlFlow();
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
/*     */ 
/*     */ 
/*     */   
/*     */   static class Jdk14ControlFlow
/*     */     implements ControlFlow
/*     */   {
/*  60 */     private StackTraceElement[] stack = (new Throwable()).getStackTrace();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean under(Class<?> clazz) {
/*  68 */       Assert.notNull(clazz, "Class must not be null");
/*  69 */       String className = clazz.getName();
/*  70 */       for (StackTraceElement element : this.stack) {
/*  71 */         if (element.getClassName().equals(className)) {
/*  72 */           return true;
/*     */         }
/*     */       } 
/*  75 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean under(Class<?> clazz, String methodName) {
/*  84 */       Assert.notNull(clazz, "Class must not be null");
/*  85 */       Assert.notNull(methodName, "Method name must not be null");
/*  86 */       String className = clazz.getName();
/*  87 */       for (StackTraceElement element : this.stack) {
/*  88 */         if (element.getClassName().equals(className) && element
/*  89 */           .getMethodName().equals(methodName)) {
/*  90 */           return true;
/*     */         }
/*     */       } 
/*  93 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean underToken(String token) {
/* 102 */       if (token == null) {
/* 103 */         return false;
/*     */       }
/* 105 */       StringWriter sw = new StringWriter();
/* 106 */       (new Throwable()).printStackTrace(new PrintWriter(sw));
/* 107 */       String stackTrace = sw.toString();
/* 108 */       return stackTrace.contains(token);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 113 */       StringBuilder sb = new StringBuilder("Jdk14ControlFlow: ");
/* 114 */       for (int i = 0; i < this.stack.length; i++) {
/* 115 */         if (i > 0) {
/* 116 */           sb.append("\n\t@");
/*     */         }
/* 118 */         sb.append(this.stack[i]);
/*     */       } 
/* 120 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\ControlFlowFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */