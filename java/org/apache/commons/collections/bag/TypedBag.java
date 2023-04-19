/*    */ package org.apache.commons.collections.bag;
/*    */ 
/*    */ import org.apache.commons.collections.Bag;
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
/*    */ public class TypedBag
/*    */ {
/*    */   public static Bag decorate(Bag bag, Class type) {
/* 51 */     return new PredicatedBag(bag, InstanceofPredicate.getInstance(type));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\bag\TypedBag.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */