/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import javax.naming.NamingException;
/*    */ import org.springframework.core.NestedRuntimeException;
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
/*    */ public class JndiLookupFailureException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public JndiLookupFailureException(String msg, NamingException cause) {
/* 42 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jndi\JndiLookupFailureException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */