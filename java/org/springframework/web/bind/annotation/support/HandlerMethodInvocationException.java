/*    */ package org.springframework.web.bind.annotation.support;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ @Deprecated
/*    */ public class HandlerMethodInvocationException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public HandlerMethodInvocationException(Method handlerMethod, Throwable cause) {
/* 41 */     super("Failed to invoke handler method [" + handlerMethod + "]", cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\annotation\support\HandlerMethodInvocationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */