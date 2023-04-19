/*    */ package org.springframework.core.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public abstract class OrderUtils
/*    */ {
/* 36 */   private static Class<? extends Annotation> priorityAnnotationType = null;
/*    */ 
/*    */   
/*    */   static {
/*    */     try {
/* 41 */       priorityAnnotationType = ClassUtils.forName("javax.annotation.Priority", OrderUtils.class.getClassLoader());
/*    */     }
/* 43 */     catch (Throwable throwable) {}
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
/*    */   
/*    */   public static Integer getOrder(Class<?> type) {
/* 57 */     return getOrder(type, null);
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
/*    */   public static Integer getOrder(Class<?> type, Integer defaultOrder) {
/* 69 */     Order order = AnnotationUtils.<Order>findAnnotation(type, Order.class);
/* 70 */     if (order != null) {
/* 71 */       return Integer.valueOf(order.value());
/*    */     }
/* 73 */     Integer priorityOrder = getPriority(type);
/* 74 */     if (priorityOrder != null) {
/* 75 */       return priorityOrder;
/*    */     }
/* 77 */     return defaultOrder;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Integer getPriority(Class<?> type) {
/* 87 */     if (priorityAnnotationType != null) {
/* 88 */       Annotation priority = AnnotationUtils.findAnnotation(type, (Class)priorityAnnotationType);
/* 89 */       if (priority != null) {
/* 90 */         return (Integer)AnnotationUtils.getValue(priority);
/*    */       }
/*    */     } 
/* 93 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\annotation\OrderUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */