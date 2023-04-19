/*    */ package org.apache.commons.collections.iterators;
/*    */ 
/*    */ import java.util.ListIterator;
/*    */ import org.apache.commons.collections.ResettableListIterator;
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
/*    */ public class EmptyListIterator
/*    */   extends AbstractEmptyIterator
/*    */   implements ResettableListIterator
/*    */ {
/* 41 */   public static final ResettableListIterator RESETTABLE_INSTANCE = new EmptyListIterator();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static final ListIterator INSTANCE = (ListIterator)RESETTABLE_INSTANCE;
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\iterators\EmptyListIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */