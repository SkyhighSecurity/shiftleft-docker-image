/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.PropertyAccessor;
/*    */ import org.springframework.expression.TypedValue;
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
/*    */ public class EnvironmentAccessor
/*    */   implements PropertyAccessor
/*    */ {
/*    */   public Class<?>[] getSpecificTargetClasses() {
/* 36 */     return new Class[] { Environment.class };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
/* 54 */     return new TypedValue(((Environment)target).getProperty(name));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
/* 62 */     return false;
/*    */   }
/*    */   
/*    */   public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {}
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\expression\EnvironmentAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */