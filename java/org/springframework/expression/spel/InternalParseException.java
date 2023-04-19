/*    */ package org.springframework.expression.spel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InternalParseException
/*    */   extends RuntimeException
/*    */ {
/*    */   public InternalParseException(SpelParseException cause) {
/* 30 */     super((Throwable)cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public SpelParseException getCause() {
/* 35 */     return (SpelParseException)super.getCause();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\InternalParseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */