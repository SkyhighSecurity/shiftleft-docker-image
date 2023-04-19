/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.context.expression.MethodBasedEvaluationContext;
/*    */ import org.springframework.core.ParameterNameDiscoverer;
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
/*    */ class CacheEvaluationContext
/*    */   extends MethodBasedEvaluationContext
/*    */ {
/* 46 */   private final Set<String> unavailableVariables = new HashSet<String>(1);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   CacheEvaluationContext(Object rootObject, Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer) {
/* 52 */     super(rootObject, method, arguments, parameterNameDiscoverer);
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
/*    */   public void addUnavailableVariable(String name) {
/* 64 */     this.unavailableVariables.add(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object lookupVariable(String name) {
/* 73 */     if (this.unavailableVariables.contains(name)) {
/* 74 */       throw new VariableNotAvailableException(name);
/*    */     }
/* 76 */     return super.lookupVariable(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\CacheEvaluationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */