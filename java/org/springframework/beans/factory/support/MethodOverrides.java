/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ public class MethodOverrides
/*     */ {
/*  39 */   private final Set<MethodOverride> overrides = Collections.synchronizedSet(new LinkedHashSet<MethodOverride>(0));
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean modified = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodOverrides() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodOverrides(MethodOverrides other) {
/*  54 */     addOverrides(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOverrides(MethodOverrides other) {
/*  62 */     if (other != null) {
/*  63 */       this.modified = true;
/*  64 */       this.overrides.addAll(other.overrides);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOverride(MethodOverride override) {
/*  72 */     this.modified = true;
/*  73 */     this.overrides.add(override);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<MethodOverride> getOverrides() {
/*  82 */     this.modified = true;
/*  83 */     return this.overrides;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  90 */     return (!this.modified || this.overrides.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodOverride getOverride(Method method) {
/*  99 */     if (!this.modified) {
/* 100 */       return null;
/*     */     }
/* 102 */     synchronized (this.overrides) {
/* 103 */       MethodOverride match = null;
/* 104 */       for (MethodOverride candidate : this.overrides) {
/* 105 */         if (candidate.matches(method)) {
/* 106 */           match = candidate;
/*     */         }
/*     */       } 
/* 109 */       return match;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 116 */     if (this == other) {
/* 117 */       return true;
/*     */     }
/* 119 */     if (!(other instanceof MethodOverrides)) {
/* 120 */       return false;
/*     */     }
/* 122 */     MethodOverrides that = (MethodOverrides)other;
/* 123 */     return this.overrides.equals(that.overrides);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 129 */     return this.overrides.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\MethodOverrides.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */