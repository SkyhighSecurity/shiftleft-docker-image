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
/*    */ 
/*    */ public final class IdentityPredicate
/*    */   implements Predicate, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -89901658494523293L;
/*    */   private final Object iValue;
/*    */   
/*    */   public static Predicate getInstance(Object object) {
/* 49 */     if (object == null) {
/* 50 */       return NullPredicate.INSTANCE;
/*    */     }
/* 52 */     return new IdentityPredicate(object);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IdentityPredicate(Object object) {
/* 63 */     this.iValue = object;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean evaluate(Object object) {
/* 74 */     return (this.iValue == object);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 84 */     return this.iValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\IdentityPredicate.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */