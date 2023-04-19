/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.core.annotation.AnnotatedElementUtils;
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
/*    */ class BeanAnnotationHelper
/*    */ {
/*    */   public static boolean isBeanAnnotated(Method method) {
/* 33 */     return AnnotatedElementUtils.hasAnnotation(method, Bean.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String determineBeanNameFor(Method beanMethod) {
/* 38 */     String beanName = beanMethod.getName();
/*    */     
/* 40 */     Bean bean = (Bean)AnnotatedElementUtils.findMergedAnnotation(beanMethod, Bean.class);
/* 41 */     if (bean != null) {
/* 42 */       String[] names = bean.name();
/* 43 */       if (names.length > 0) {
/* 44 */         beanName = names[0];
/*    */       }
/*    */     } 
/* 47 */     return beanName;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\BeanAnnotationHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */