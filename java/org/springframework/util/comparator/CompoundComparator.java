/*     */ package org.springframework.util.comparator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompoundComparator<T>
/*     */   implements Comparator<T>, Serializable
/*     */ {
/*     */   private final List<InvertibleComparator> comparators;
/*     */   
/*     */   public CompoundComparator() {
/*  52 */     this.comparators = new ArrayList<InvertibleComparator>();
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
/*     */   public CompoundComparator(Comparator... comparators) {
/*  64 */     Assert.notNull(comparators, "Comparators must not be null");
/*  65 */     this.comparators = new ArrayList<InvertibleComparator>(comparators.length);
/*  66 */     for (Comparator<? extends T> comparator : comparators) {
/*  67 */       addComparator(comparator);
/*     */     }
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
/*     */   public void addComparator(Comparator<? extends T> comparator) {
/*  81 */     if (comparator instanceof InvertibleComparator) {
/*  82 */       this.comparators.add((InvertibleComparator)comparator);
/*     */     } else {
/*     */       
/*  85 */       this.comparators.add(new InvertibleComparator<T>(comparator));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComparator(Comparator<? extends T> comparator, boolean ascending) {
/*  96 */     this.comparators.add(new InvertibleComparator<T>(comparator, ascending));
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
/*     */   public void setComparator(int index, Comparator<? extends T> comparator) {
/* 109 */     if (comparator instanceof InvertibleComparator) {
/* 110 */       this.comparators.set(index, (InvertibleComparator)comparator);
/*     */     } else {
/*     */       
/* 113 */       this.comparators.set(index, new InvertibleComparator<T>(comparator));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComparator(int index, Comparator<T> comparator, boolean ascending) {
/* 124 */     this.comparators.set(index, new InvertibleComparator<T>(comparator, ascending));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invertOrder() {
/* 132 */     for (InvertibleComparator comparator : this.comparators) {
/* 133 */       comparator.invertOrder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invertOrder(int index) {
/* 142 */     ((InvertibleComparator)this.comparators.get(index)).invertOrder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAscendingOrder(int index) {
/* 150 */     ((InvertibleComparator)this.comparators.get(index)).setAscending(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescendingOrder(int index) {
/* 158 */     ((InvertibleComparator)this.comparators.get(index)).setAscending(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getComparatorCount() {
/* 165 */     return this.comparators.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(T o1, T o2) {
/* 171 */     Assert.state((this.comparators.size() > 0), "No sort definitions have been added to this CompoundComparator to compare");
/*     */     
/* 173 */     for (InvertibleComparator<T> comparator : this.comparators) {
/* 174 */       int result = comparator.compare(o1, o2);
/* 175 */       if (result != 0) {
/* 176 */         return result;
/*     */       }
/*     */     } 
/* 179 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 185 */     if (this == obj) {
/* 186 */       return true;
/*     */     }
/* 188 */     if (!(obj instanceof CompoundComparator)) {
/* 189 */       return false;
/*     */     }
/* 191 */     CompoundComparator<T> other = (CompoundComparator<T>)obj;
/* 192 */     return this.comparators.equals(other.comparators);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 197 */     return this.comparators.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 202 */     return "CompoundComparator: " + this.comparators;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\comparator\CompoundComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */