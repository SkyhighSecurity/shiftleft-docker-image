/*    */ package org.apache.commons.collections.set;
/*    */ 
/*    */ import java.util.SortedSet;
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
/*    */ public class TypedSortedSet
/*    */ {
/*    */   public static SortedSet decorate(SortedSet set, Class type) {
/* 51 */     return new PredicatedSortedSet(set, InstanceofPredicate.getInstance(type));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\set\TypedSortedSet.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */