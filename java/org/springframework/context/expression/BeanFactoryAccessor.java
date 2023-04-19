/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanFactory;
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
/*    */ 
/*    */ public class BeanFactoryAccessor
/*    */   implements PropertyAccessor
/*    */ {
/*    */   public Class<?>[] getSpecificTargetClasses() {
/* 37 */     return new Class[] { BeanFactory.class };
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
/* 42 */     return ((BeanFactory)target).containsBean(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
/* 47 */     return new TypedValue(((BeanFactory)target).getBean(name));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
/* 57 */     throw new AccessException("Beans in a BeanFactory are read-only");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\expression\BeanFactoryAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */