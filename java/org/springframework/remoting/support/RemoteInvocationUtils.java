/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public abstract class RemoteInvocationUtils
/*    */ {
/*    */   public static void fillInClientStackTraceIfPossible(Throwable ex) {
/* 44 */     if (ex != null) {
/* 45 */       StackTraceElement[] clientStack = (new Throwable()).getStackTrace();
/* 46 */       Set<Throwable> visitedExceptions = new HashSet<Throwable>();
/* 47 */       Throwable exToUpdate = ex;
/* 48 */       while (exToUpdate != null && !visitedExceptions.contains(exToUpdate)) {
/* 49 */         StackTraceElement[] serverStack = exToUpdate.getStackTrace();
/* 50 */         StackTraceElement[] combinedStack = new StackTraceElement[serverStack.length + clientStack.length];
/* 51 */         System.arraycopy(serverStack, 0, combinedStack, 0, serverStack.length);
/* 52 */         System.arraycopy(clientStack, 0, combinedStack, serverStack.length, clientStack.length);
/* 53 */         exToUpdate.setStackTrace(combinedStack);
/* 54 */         visitedExceptions.add(exToUpdate);
/* 55 */         exToUpdate = exToUpdate.getCause();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\RemoteInvocationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */