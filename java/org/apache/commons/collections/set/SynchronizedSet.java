/*    */ package org.apache.commons.collections.set;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.apache.commons.collections.collection.SynchronizedCollection;
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
/*    */ public class SynchronizedSet
/*    */   extends SynchronizedCollection
/*    */   implements Set
/*    */ {
/*    */   private static final long serialVersionUID = -8304417378626543635L;
/*    */   
/*    */   public static Set decorate(Set set) {
/* 48 */     return new SynchronizedSet(set);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected SynchronizedSet(Set set) {
/* 59 */     super(set);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected SynchronizedSet(Set set, Object lock) {
/* 70 */     super(set, lock);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Set getSet() {
/* 79 */     return (Set)this.collection;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\set\SynchronizedSet.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */