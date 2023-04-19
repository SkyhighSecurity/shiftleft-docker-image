/*    */ package org.apache.commons.collections.iterators;
/*    */ 
/*    */ import org.apache.commons.collections.MapIterator;
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
/*    */ public class EmptyMapIterator
/*    */   extends AbstractEmptyIterator
/*    */   implements MapIterator, ResettableIterator
/*    */ {
/* 36 */   public static final MapIterator INSTANCE = new EmptyMapIterator();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\iterators\EmptyMapIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */