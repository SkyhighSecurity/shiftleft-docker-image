/*    */ package org.apache.commons.collections.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections.Predicate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class InstanceofPredicate
/*    */   implements Predicate, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -6682656911025165584L;
/*    */   private final Class iType;
/*    */   
/*    */   public static Predicate getInstance(Class type) {
/* 48 */     if (type == null) {
/* 49 */       throw new IllegalArgumentException("The type to check instanceof must not be null");
/*    */     }
/* 51 */     return new InstanceofPredicate(type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InstanceofPredicate(Class type) {
/* 62 */     this.iType = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean evaluate(Object object) {
/* 72 */     return this.iType.isInstance(object);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class getType() {
/* 82 */     return this.iType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\InstanceofPredicate.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */