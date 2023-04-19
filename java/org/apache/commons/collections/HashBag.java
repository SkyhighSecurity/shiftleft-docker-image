/*    */ package org.apache.commons.collections;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HashBag
/*    */   extends DefaultMapBag
/*    */   implements Bag
/*    */ {
/*    */   public HashBag() {
/* 37 */     super(new HashMap());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HashBag(Collection coll) {
/* 47 */     this();
/* 48 */     addAll(coll);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\HashBag.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */