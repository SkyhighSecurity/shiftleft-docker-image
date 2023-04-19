/*    */ package org.springframework.web.util;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import org.springframework.core.NestedExceptionUtils;
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
/*    */ 
/*    */ 
/*    */ public class NestedServletException
/*    */   extends ServletException
/*    */ {
/*    */   private static final long serialVersionUID = -5292377985529381145L;
/*    */   
/*    */   static {
/* 51 */     NestedExceptionUtils.class.getName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NestedServletException(String msg) {
/* 60 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NestedServletException(String msg, Throwable cause) {
/* 70 */     super(msg, cause);
/*    */ 
/*    */     
/* 73 */     if (getCause() == null && cause != null) {
/* 74 */       initCause(cause);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 85 */     return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\NestedServletException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */