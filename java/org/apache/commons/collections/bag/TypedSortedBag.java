/*    */ package org.apache.commons.collections.bag;
/*    */ 
/*    */ import org.apache.commons.collections.SortedBag;
/*    */ import org.apache.commons.collections.functors.InstanceofPredicate;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TypedSortedBag
/*    */ {
/*    */   public static SortedBag decorate(SortedBag bag, Class type) {
/* 51 */     return new PredicatedSortedBag(bag, InstanceofPredicate.getInstance(type));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\bag\TypedSortedBag.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */