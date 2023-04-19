/*     */ package org.springframework.scripting.bsh;
/*     */ 
/*     */ import bsh.EvalError;
/*     */ import bsh.Interpreter;
/*     */ import bsh.Primitive;
/*     */ import bsh.XThis;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import org.springframework.core.NestedRuntimeException;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BshScriptUtils
/*     */ {
/*     */   public static Object createBshObject(String scriptSource) throws EvalError {
/*  51 */     return createBshObject(scriptSource, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object createBshObject(String scriptSource, Class<?>... scriptInterfaces) throws EvalError {
/*  70 */     return createBshObject(scriptSource, scriptInterfaces, ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object createBshObject(String scriptSource, Class<?>[] scriptInterfaces, ClassLoader classLoader) throws EvalError {
/*  90 */     Object result = evaluateBshScript(scriptSource, scriptInterfaces, classLoader);
/*  91 */     if (result instanceof Class) {
/*  92 */       Class<?> clazz = (Class)result;
/*     */       try {
/*  94 */         return clazz.newInstance();
/*     */       }
/*  96 */       catch (Throwable ex) {
/*  97 */         throw new IllegalStateException("Could not instantiate script class: " + clazz.getName(), ex);
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Class<?> determineBshObjectType(String scriptSource, ClassLoader classLoader) throws EvalError {
/* 117 */     Assert.hasText(scriptSource, "Script source must not be empty");
/* 118 */     Interpreter interpreter = new Interpreter();
/* 119 */     interpreter.setClassLoader(classLoader);
/* 120 */     Object result = interpreter.eval(scriptSource);
/* 121 */     if (result instanceof Class) {
/* 122 */       return (Class)result;
/*     */     }
/* 124 */     if (result != null) {
/* 125 */       return result.getClass();
/*     */     }
/*     */     
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object evaluateBshScript(String scriptSource, Class<?>[] scriptInterfaces, ClassLoader classLoader) throws EvalError {
/* 150 */     Assert.hasText(scriptSource, "Script source must not be empty");
/* 151 */     Interpreter interpreter = new Interpreter();
/* 152 */     interpreter.setClassLoader(classLoader);
/* 153 */     Object result = interpreter.eval(scriptSource);
/* 154 */     if (result != null) {
/* 155 */       return result;
/*     */     }
/*     */ 
/*     */     
/* 159 */     Assert.notEmpty((Object[])scriptInterfaces, "Given script requires a script proxy: At least one script interface is required.");
/*     */     
/* 161 */     XThis xt = (XThis)interpreter.eval("return this");
/* 162 */     return Proxy.newProxyInstance(classLoader, scriptInterfaces, new BshObjectInvocationHandler(xt));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BshObjectInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final XThis xt;
/*     */ 
/*     */ 
/*     */     
/*     */     public BshObjectInvocationHandler(XThis xt) {
/* 175 */       this.xt = xt;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 180 */       if (ReflectionUtils.isEqualsMethod(method)) {
/* 181 */         return Boolean.valueOf(isProxyForSameBshObject(args[0]));
/*     */       }
/* 183 */       if (ReflectionUtils.isHashCodeMethod(method)) {
/* 184 */         return Integer.valueOf(this.xt.hashCode());
/*     */       }
/* 186 */       if (ReflectionUtils.isToStringMethod(method)) {
/* 187 */         return "BeanShell object [" + this.xt + "]";
/*     */       }
/*     */       try {
/* 190 */         Object result = this.xt.invokeMethod(method.getName(), args);
/* 191 */         if (result == Primitive.NULL || result == Primitive.VOID) {
/* 192 */           return null;
/*     */         }
/* 194 */         if (result instanceof Primitive) {
/* 195 */           return ((Primitive)result).getValue();
/*     */         }
/* 197 */         return result;
/*     */       }
/* 199 */       catch (EvalError ex) {
/* 200 */         throw new BshScriptUtils.BshExecutionException(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean isProxyForSameBshObject(Object other) {
/* 205 */       if (!Proxy.isProxyClass(other.getClass())) {
/* 206 */         return false;
/*     */       }
/* 208 */       InvocationHandler ih = Proxy.getInvocationHandler(other);
/* 209 */       return (ih instanceof BshObjectInvocationHandler && this.xt
/* 210 */         .equals(((BshObjectInvocationHandler)ih).xt));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BshExecutionException
/*     */     extends NestedRuntimeException
/*     */   {
/*     */     private BshExecutionException(EvalError ex) {
/* 222 */       super("BeanShell script execution failed", (Throwable)ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\bsh\BshScriptUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */