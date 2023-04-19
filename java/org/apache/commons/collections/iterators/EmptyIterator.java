/*    */ package org.apache.commons.collections.iterators;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmptyIterator
/*    */   extends AbstractEmptyIterator
/*    */   implements ResettableIterator
/*    */ {
/* 41 */   public static final ResettableIterator RESETTABLE_INSTANCE = new EmptyIterator();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static final Iterator INSTANCE = (Iterator)RESETTABLE_INSTANCE;
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\iterators\EmptyIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */