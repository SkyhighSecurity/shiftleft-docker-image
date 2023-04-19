/*     */ package org.apache.commons.collections.bag;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import org.apache.commons.collections.Bag;
/*     */ import org.apache.commons.collections.Predicate;
/*     */ import org.apache.commons.collections.SortedBag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredicatedSortedBag
/*     */   extends PredicatedBag
/*     */   implements SortedBag
/*     */ {
/*     */   private static final long serialVersionUID = 3448581314086406616L;
/*     */   
/*     */   public static SortedBag decorate(SortedBag bag, Predicate predicate) {
/*  62 */     return new PredicatedSortedBag(bag, predicate);
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
/*     */   
/*     */   protected PredicatedSortedBag(SortedBag bag, Predicate predicate) {
/*  78 */     super((Bag)bag, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedBag getSortedBag() {
/*  87 */     return (SortedBag)getCollection();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object first() {
/*  92 */     return getSortedBag().first();
/*     */   }
/*     */   
/*     */   public Object last() {
/*  96 */     return getSortedBag().last();
/*     */   }
/*     */   
/*     */   public Comparator comparator() {
/* 100 */     return getSortedBag().comparator();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\bag\PredicatedSortedBag.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */