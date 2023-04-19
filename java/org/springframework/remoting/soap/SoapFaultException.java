/*    */ package org.springframework.remoting.soap;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import org.springframework.remoting.RemoteInvocationFailureException;
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
/*    */ public abstract class SoapFaultException
/*    */   extends RemoteInvocationFailureException
/*    */ {
/*    */   protected SoapFaultException(String msg, Throwable cause) {
/* 41 */     super(msg, cause);
/*    */   }
/*    */   
/*    */   public abstract String getFaultCode();
/*    */   
/*    */   public abstract QName getFaultCodeAsQName();
/*    */   
/*    */   public abstract String getFaultString();
/*    */   
/*    */   public abstract String getFaultActor();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\soap\SoapFaultException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */