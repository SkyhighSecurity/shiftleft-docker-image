/*    */ package org.apache.commons.collections.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections.Closure;
/*    */ import org.apache.commons.collections.FunctorException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ExceptionClosure
/*    */   implements Closure, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 7179106032121985545L;
/* 39 */   public static final Closure INSTANCE = new ExceptionClosure();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Closure getInstance() {
/* 48 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(Object input) {
/* 65 */     throw new FunctorException("ExceptionClosure invoked");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\ExceptionClosure.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */