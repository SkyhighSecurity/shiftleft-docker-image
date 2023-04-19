/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.ConstructorExecutor;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.TypedValue;
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
/*    */ public class ReflectiveConstructorExecutor
/*    */   implements ConstructorExecutor
/*    */ {
/*    */   private final Constructor<?> ctor;
/*    */   private final Integer varargsPosition;
/*    */   
/*    */   public ReflectiveConstructorExecutor(Constructor<?> ctor) {
/* 43 */     this.ctor = ctor;
/* 44 */     if (ctor.isVarArgs()) {
/* 45 */       Class<?>[] paramTypes = ctor.getParameterTypes();
/* 46 */       this.varargsPosition = Integer.valueOf(paramTypes.length - 1);
/*    */     } else {
/*    */       
/* 49 */       this.varargsPosition = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public TypedValue execute(EvaluationContext context, Object... arguments) throws AccessException {
/*    */     try {
/* 56 */       if (arguments != null) {
/* 57 */         ReflectionHelper.convertArguments(context.getTypeConverter(), arguments, this.ctor, this.varargsPosition);
/*    */       }
/* 59 */       if (this.ctor.isVarArgs()) {
/* 60 */         arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.ctor.getParameterTypes(), arguments);
/*    */       }
/* 62 */       ReflectionUtils.makeAccessible(this.ctor);
/* 63 */       return new TypedValue(this.ctor.newInstance(arguments));
/*    */     }
/* 65 */     catch (Exception ex) {
/* 66 */       throw new AccessException("Problem invoking constructor: " + this.ctor, ex);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Constructor<?> getConstructor() {
/* 71 */     return this.ctor;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\ReflectiveConstructorExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */