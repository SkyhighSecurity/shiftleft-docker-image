/*    */ package org.apache.commons.collections.bag;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.apache.commons.collections.Bag;
/*    */ import org.apache.commons.collections.SortedBag;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractSortedBagDecorator
/*    */   extends AbstractBagDecorator
/*    */   implements SortedBag
/*    */ {
/*    */   protected AbstractSortedBagDecorator() {}
/*    */   
/*    */   protected AbstractSortedBagDecorator(SortedBag bag) {
/* 51 */     super((Bag)bag);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected SortedBag getSortedBag() {
/* 60 */     return (SortedBag)getCollection();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object first() {
/* 65 */     return getSortedBag().first();
/*    */   }
/*    */   
/*    */   public Object last() {
/* 69 */     return getSortedBag().last();
/*    */   }
/*    */   
/*    */   public Comparator comparator() {
/* 73 */     return getSortedBag().comparator();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\bag\AbstractSortedBagDecorator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */