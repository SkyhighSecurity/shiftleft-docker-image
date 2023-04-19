/*    */ package org.springframework.core.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ class DefaultAnnotationAttributeExtractor
/*    */   extends AbstractAliasAwareAnnotationAttributeExtractor<Annotation>
/*    */ {
/*    */   DefaultAnnotationAttributeExtractor(Annotation annotation, Object annotatedElement) {
/* 45 */     super(annotation.annotationType(), annotatedElement, annotation);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object getRawAttributeValue(Method attributeMethod) {
/* 51 */     ReflectionUtils.makeAccessible(attributeMethod);
/* 52 */     return ReflectionUtils.invokeMethod(attributeMethod, getSource());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object getRawAttributeValue(String attributeName) {
/* 57 */     Method attributeMethod = ReflectionUtils.findMethod(getAnnotationType(), attributeName);
/* 58 */     return getRawAttributeValue(attributeMethod);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\annotation\DefaultAnnotationAttributeExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */