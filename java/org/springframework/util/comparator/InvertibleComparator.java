/*     */ package org.springframework.util.comparator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ public class InvertibleComparator<T>
/*     */   implements Comparator<T>, Serializable
/*     */ {
/*     */   private final Comparator<T> comparator;
/*     */   private boolean ascending = true;
/*     */   
/*     */   public InvertibleComparator(Comparator<T> comparator) {
/*  47 */     Assert.notNull(comparator, "Comparator must not be null");
/*  48 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InvertibleComparator(Comparator<T> comparator, boolean ascending) {
/*  58 */     Assert.notNull(comparator, "Comparator must not be null");
/*  59 */     this.comparator = comparator;
/*  60 */     setAscending(ascending);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAscending(boolean ascending) {
/*  68 */     this.ascending = ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAscending() {
/*  75 */     return this.ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invertOrder() {
/*  83 */     this.ascending = !this.ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(T o1, T o2) {
/*  89 */     int result = this.comparator.compare(o1, o2);
/*  90 */     if (result != 0) {
/*     */       
/*  92 */       if (!this.ascending) {
/*  93 */         if (Integer.MIN_VALUE == result) {
/*  94 */           result = Integer.MAX_VALUE;
/*     */         } else {
/*     */           
/*  97 */           result *= -1;
/*     */         } 
/*     */       }
/* 100 */       return result;
/*     */     } 
/* 102 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 108 */     if (this == obj) {
/* 109 */       return true;
/*     */     }
/* 111 */     if (!(obj instanceof InvertibleComparator)) {
/* 112 */       return false;
/*     */     }
/* 114 */     InvertibleComparator<T> other = (InvertibleComparator<T>)obj;
/* 115 */     return (this.comparator.equals(other.comparator) && this.ascending == other.ascending);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 120 */     return this.comparator.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     return "InvertibleComparator: [" + this.comparator + "]; ascending=" + this.ascending;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\comparator\InvertibleComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */