/*    */ package org.apache.commons.collections.set;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.apache.commons.collections.collection.AbstractCollectionDecorator;
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
/*    */ public abstract class AbstractSetDecorator
/*    */   extends AbstractCollectionDecorator
/*    */   implements Set
/*    */ {
/*    */   protected AbstractSetDecorator() {}
/*    */   
/*    */   protected AbstractSetDecorator(Set set) {
/* 50 */     super(set);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Set getSet() {
/* 59 */     return (Set)getCollection();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\set\AbstractSetDecorator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */