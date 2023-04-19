/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ExceptionDepthComparator
/*    */   implements Comparator<Class<? extends Throwable>>
/*    */ {
/*    */   private final Class<? extends Throwable> targetException;
/*    */   
/*    */   public ExceptionDepthComparator(Throwable exception) {
/* 44 */     Assert.notNull(exception, "Target exception must not be null");
/* 45 */     this.targetException = (Class)exception.getClass();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExceptionDepthComparator(Class<? extends Throwable> exceptionType) {
/* 53 */     Assert.notNull(exceptionType, "Target exception type must not be null");
/* 54 */     this.targetException = exceptionType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(Class<? extends Throwable> o1, Class<? extends Throwable> o2) {
/* 60 */     int depth1 = getDepth(o1, this.targetException, 0);
/* 61 */     int depth2 = getDepth(o2, this.targetException, 0);
/* 62 */     return depth1 - depth2;
/*    */   }
/*    */   
/*    */   private int getDepth(Class<?> declaredException, Class<?> exceptionToMatch, int depth) {
/* 66 */     if (exceptionToMatch.equals(declaredException))
/*    */     {
/* 68 */       return depth;
/*    */     }
/*    */     
/* 71 */     if (exceptionToMatch == Throwable.class) {
/* 72 */       return Integer.MAX_VALUE;
/*    */     }
/* 74 */     return getDepth(declaredException, exceptionToMatch.getSuperclass(), depth + 1);
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
/*    */   public static Class<? extends Throwable> findClosestMatch(Collection<Class<? extends Throwable>> exceptionTypes, Throwable targetException) {
/* 87 */     Assert.notEmpty(exceptionTypes, "Exception types must not be empty");
/* 88 */     if (exceptionTypes.size() == 1) {
/* 89 */       return exceptionTypes.iterator().next();
/*    */     }
/* 91 */     List<Class<? extends Throwable>> handledExceptions = new ArrayList<Class<? extends Throwable>>(exceptionTypes);
/*    */     
/* 93 */     Collections.sort(handledExceptions, new ExceptionDepthComparator(targetException));
/* 94 */     return handledExceptions.get(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\ExceptionDepthComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */