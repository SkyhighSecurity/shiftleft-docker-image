/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ public class ExceptionTypeFilter
/*    */   extends InstanceFilter<Class<? extends Throwable>>
/*    */ {
/*    */   public ExceptionTypeFilter(Collection<? extends Class<? extends Throwable>> includes, Collection<? extends Class<? extends Throwable>> excludes, boolean matchIfEmpty) {
/* 33 */     super(includes, excludes, matchIfEmpty);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean match(Class<? extends Throwable> instance, Class<? extends Throwable> candidate) {
/* 38 */     return candidate.isAssignableFrom(instance);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\ExceptionTypeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */