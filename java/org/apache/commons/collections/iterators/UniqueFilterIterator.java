/*    */ package org.apache.commons.collections.iterators;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import org.apache.commons.collections.functors.UniquePredicate;
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
/*    */ public class UniqueFilterIterator
/*    */   extends FilterIterator
/*    */ {
/*    */   public UniqueFilterIterator(Iterator iterator) {
/* 43 */     super(iterator, UniquePredicate.getInstance());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\iterators\UniqueFilterIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */