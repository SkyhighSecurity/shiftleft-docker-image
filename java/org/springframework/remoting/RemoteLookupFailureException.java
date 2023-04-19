/*    */ package org.springframework.remoting;
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
/*    */ public class RemoteLookupFailureException
/*    */   extends RemoteAccessException
/*    */ {
/*    */   public RemoteLookupFailureException(String msg) {
/* 34 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RemoteLookupFailureException(String msg, Throwable cause) {
/* 43 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\RemoteLookupFailureException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */