/*    */ package org.apache.commons.collections.iterators;
/*    */ 
/*    */ import org.apache.commons.collections.OrderedIterator;
/*    */ import org.apache.commons.collections.ResettableIterator;
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
/*    */ public class EmptyOrderedIterator
/*    */   extends AbstractEmptyIterator
/*    */   implements OrderedIterator, ResettableIterator
/*    */ {
/* 36 */   public static final OrderedIterator INSTANCE = new EmptyOrderedIterator();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\iterators\EmptyOrderedIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */