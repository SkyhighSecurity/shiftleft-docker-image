/*     */ package org.springframework.util.comparator;
/*     */ 
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
/*     */ public class NullSafeComparator<T>
/*     */   implements Comparator<T>
/*     */ {
/*  39 */   public static final NullSafeComparator NULLS_LOW = new NullSafeComparator(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final NullSafeComparator NULLS_HIGH = new NullSafeComparator(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Comparator<T> nonNullComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean nullsLow;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NullSafeComparator(boolean nullsLow) {
/*  69 */     this.nonNullComparator = (Comparator)new ComparableComparator<Comparable>();
/*  70 */     this.nullsLow = nullsLow;
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
/*     */   public NullSafeComparator(Comparator<T> comparator, boolean nullsLow) {
/*  83 */     Assert.notNull(comparator, "The non-null comparator is required");
/*  84 */     this.nonNullComparator = comparator;
/*  85 */     this.nullsLow = nullsLow;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(T o1, T o2) {
/*  91 */     if (o1 == o2) {
/*  92 */       return 0;
/*     */     }
/*  94 */     if (o1 == null) {
/*  95 */       return this.nullsLow ? -1 : 1;
/*     */     }
/*  97 */     if (o2 == null) {
/*  98 */       return this.nullsLow ? 1 : -1;
/*     */     }
/* 100 */     return this.nonNullComparator.compare(o1, o2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 106 */     if (this == obj) {
/* 107 */       return true;
/*     */     }
/* 109 */     if (!(obj instanceof NullSafeComparator)) {
/* 110 */       return false;
/*     */     }
/* 112 */     NullSafeComparator<T> other = (NullSafeComparator<T>)obj;
/* 113 */     return (this.nonNullComparator.equals(other.nonNullComparator) && this.nullsLow == other.nullsLow);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 118 */     return (this.nullsLow ? -1 : 1) * this.nonNullComparator.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     return "NullSafeComparator: non-null comparator [" + this.nonNullComparator + "]; " + (this.nullsLow ? "nulls low" : "nulls high");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\comparator\NullSafeComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */