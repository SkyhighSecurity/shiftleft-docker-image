/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class PropertyBatchUpdateException
/*     */   extends BeansException
/*     */ {
/*     */   private PropertyAccessException[] propertyAccessExceptions;
/*     */   
/*     */   public PropertyBatchUpdateException(PropertyAccessException[] propertyAccessExceptions) {
/*  50 */     super((String)null);
/*  51 */     Assert.notEmpty((Object[])propertyAccessExceptions, "At least 1 PropertyAccessException required");
/*  52 */     this.propertyAccessExceptions = propertyAccessExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getExceptionCount() {
/*  60 */     return this.propertyAccessExceptions.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PropertyAccessException[] getPropertyAccessExceptions() {
/*  68 */     return this.propertyAccessExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyAccessException getPropertyAccessException(String propertyName) {
/*  75 */     for (PropertyAccessException pae : this.propertyAccessExceptions) {
/*  76 */       if (ObjectUtils.nullSafeEquals(propertyName, pae.getPropertyName())) {
/*  77 */         return pae;
/*     */       }
/*     */     } 
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  86 */     StringBuilder sb = new StringBuilder("Failed properties: ");
/*  87 */     for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
/*  88 */       sb.append(this.propertyAccessExceptions[i].getMessage());
/*  89 */       if (i < this.propertyAccessExceptions.length - 1) {
/*  90 */         sb.append("; ");
/*     */       }
/*     */     } 
/*  93 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     StringBuilder sb = new StringBuilder();
/*  99 */     sb.append(getClass().getName()).append("; nested PropertyAccessExceptions (");
/* 100 */     sb.append(getExceptionCount()).append(") are:");
/* 101 */     for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
/* 102 */       sb.append('\n').append("PropertyAccessException ").append(i + 1).append(": ");
/* 103 */       sb.append(this.propertyAccessExceptions[i]);
/*     */     } 
/* 105 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintStream ps) {
/* 110 */     synchronized (ps) {
/* 111 */       ps.println(getClass().getName() + "; nested PropertyAccessException details (" + 
/* 112 */           getExceptionCount() + ") are:");
/* 113 */       for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
/* 114 */         ps.println("PropertyAccessException " + (i + 1) + ":");
/* 115 */         this.propertyAccessExceptions[i].printStackTrace(ps);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintWriter pw) {
/* 122 */     synchronized (pw) {
/* 123 */       pw.println(getClass().getName() + "; nested PropertyAccessException details (" + 
/* 124 */           getExceptionCount() + ") are:");
/* 125 */       for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
/* 126 */         pw.println("PropertyAccessException " + (i + 1) + ":");
/* 127 */         this.propertyAccessExceptions[i].printStackTrace(pw);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Class<?> exType) {
/* 134 */     if (exType == null) {
/* 135 */       return false;
/*     */     }
/* 137 */     if (exType.isInstance(this)) {
/* 138 */       return true;
/*     */     }
/* 140 */     for (PropertyAccessException pae : this.propertyAccessExceptions) {
/* 141 */       if (pae.contains(exType)) {
/* 142 */         return true;
/*     */       }
/*     */     } 
/* 145 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyBatchUpdateException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */