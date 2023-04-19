/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.List;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.MethodExecutor;
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
/*    */ public class DataBindingMethodResolver
/*    */   extends ReflectiveMethodResolver
/*    */ {
/*    */   public MethodExecutor resolve(EvaluationContext context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) throws AccessException {
/* 51 */     if (targetObject instanceof Class) {
/* 52 */       throw new IllegalArgumentException("DataBindingMethodResolver does not support Class targets");
/*    */     }
/* 54 */     return super.resolve(context, targetObject, name, argumentTypes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isCandidateForInvocation(Method method, Class<?> targetClass) {
/* 59 */     if (Modifier.isStatic(method.getModifiers())) {
/* 60 */       return false;
/*    */     }
/* 62 */     Class<?> clazz = method.getDeclaringClass();
/* 63 */     return (clazz != Object.class && clazz != Class.class && !ClassLoader.class.isAssignableFrom(targetClass));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataBindingMethodResolver forInstanceMethodInvocation() {
/* 71 */     return new DataBindingMethodResolver();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\DataBindingMethodResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */