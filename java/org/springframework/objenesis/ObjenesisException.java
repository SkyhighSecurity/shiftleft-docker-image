/*    */ package org.springframework.objenesis;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObjenesisException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -2677230016262426968L;
/*    */   
/*    */   public ObjenesisException(String msg) {
/* 32 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjenesisException(Throwable cause) {
/* 39 */     super(cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjenesisException(String msg, Throwable cause) {
/* 47 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\ObjenesisException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */