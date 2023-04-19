/*    */ package org.apache.commons.collections.iterators;
/*    */ 
/*    */ import org.apache.commons.collections.OrderedMapIterator;
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
/*    */ public class EmptyOrderedMapIterator
/*    */   extends AbstractEmptyIterator
/*    */   implements OrderedMapIterator, ResettableIterator
/*    */ {
/* 36 */   public static final OrderedMapIterator INSTANCE = new EmptyOrderedMapIterator();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\iterators\EmptyOrderedMapIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */