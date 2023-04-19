/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstanceFilter<T>
/*     */ {
/*     */   private final Collection<? extends T> includes;
/*     */   private final Collection<? extends T> excludes;
/*     */   private final boolean matchIfEmpty;
/*     */   
/*     */   public InstanceFilter(Collection<? extends T> includes, Collection<? extends T> excludes, boolean matchIfEmpty) {
/*  57 */     this.includes = (includes != null) ? includes : Collections.<T>emptyList();
/*  58 */     this.excludes = (excludes != null) ? excludes : Collections.<T>emptyList();
/*  59 */     this.matchIfEmpty = matchIfEmpty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(T instance) {
/*  67 */     Assert.notNull(instance, "Instance to match must not be null");
/*     */     
/*  69 */     boolean includesSet = !this.includes.isEmpty();
/*  70 */     boolean excludesSet = !this.excludes.isEmpty();
/*  71 */     if (!includesSet && !excludesSet) {
/*  72 */       return this.matchIfEmpty;
/*     */     }
/*     */     
/*  75 */     boolean matchIncludes = match(instance, this.includes);
/*  76 */     boolean matchExcludes = match(instance, this.excludes);
/*  77 */     if (!includesSet) {
/*  78 */       return !matchExcludes;
/*     */     }
/*  80 */     if (!excludesSet) {
/*  81 */       return matchIncludes;
/*     */     }
/*  83 */     return (matchIncludes && !matchExcludes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean match(T instance, T candidate) {
/*  94 */     return instance.equals(candidate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean match(T instance, Collection<? extends T> candidates) {
/* 105 */     for (T candidate : candidates) {
/* 106 */       if (match(instance, candidate)) {
/* 107 */         return true;
/*     */       }
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     StringBuilder sb = new StringBuilder(getClass().getSimpleName());
/* 116 */     sb.append(": includes=").append(this.includes);
/* 117 */     sb.append(", excludes=").append(this.excludes);
/* 118 */     sb.append(", matchIfEmpty=").append(this.matchIfEmpty);
/* 119 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\InstanceFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */