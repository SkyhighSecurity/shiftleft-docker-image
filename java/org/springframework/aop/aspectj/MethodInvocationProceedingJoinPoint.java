/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.aspectj.lang.JoinPoint;
/*     */ import org.aspectj.lang.ProceedingJoinPoint;
/*     */ import org.aspectj.lang.Signature;
/*     */ import org.aspectj.lang.reflect.MethodSignature;
/*     */ import org.aspectj.lang.reflect.SourceLocation;
/*     */ import org.aspectj.runtime.internal.AroundClosure;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ public class MethodInvocationProceedingJoinPoint
/*     */   implements ProceedingJoinPoint, JoinPoint.StaticPart
/*     */ {
/*  53 */   private static final ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */   
/*     */   private final ProxyMethodInvocation methodInvocation;
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] args;
/*     */ 
/*     */   
/*     */   private Signature signature;
/*     */ 
/*     */   
/*     */   private SourceLocation sourceLocation;
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInvocationProceedingJoinPoint(ProxyMethodInvocation methodInvocation) {
/*  72 */     Assert.notNull(methodInvocation, "MethodInvocation must not be null");
/*  73 */     this.methodInvocation = methodInvocation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set$AroundClosure(AroundClosure aroundClosure) {
/*  79 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object proceed() throws Throwable {
/*  84 */     return this.methodInvocation.invocableClone().proceed();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object proceed(Object[] arguments) throws Throwable {
/*  89 */     Assert.notNull(arguments, "Argument array passed to proceed cannot be null");
/*  90 */     if (arguments.length != (this.methodInvocation.getArguments()).length) {
/*  91 */       throw new IllegalArgumentException("Expecting " + (this.methodInvocation
/*  92 */           .getArguments()).length + " arguments to proceed, but was passed " + arguments.length + " arguments");
/*     */     }
/*     */     
/*  95 */     this.methodInvocation.setArguments(arguments);
/*  96 */     return this.methodInvocation.invocableClone(arguments).proceed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getThis() {
/* 104 */     return this.methodInvocation.getProxy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTarget() {
/* 112 */     return this.methodInvocation.getThis();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getArgs() {
/* 117 */     if (this.args == null) {
/* 118 */       this.args = (Object[])this.methodInvocation.getArguments().clone();
/*     */     }
/* 120 */     return this.args;
/*     */   }
/*     */ 
/*     */   
/*     */   public Signature getSignature() {
/* 125 */     if (this.signature == null) {
/* 126 */       this.signature = (Signature)new MethodSignatureImpl();
/*     */     }
/* 128 */     return this.signature;
/*     */   }
/*     */ 
/*     */   
/*     */   public SourceLocation getSourceLocation() {
/* 133 */     if (this.sourceLocation == null) {
/* 134 */       this.sourceLocation = new SourceLocationImpl();
/*     */     }
/* 136 */     return this.sourceLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKind() {
/* 141 */     return "method-execution";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getId() {
/* 147 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public JoinPoint.StaticPart getStaticPart() {
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toShortString() {
/* 157 */     return "execution(" + getSignature().toShortString() + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toLongString() {
/* 162 */     return "execution(" + getSignature().toLongString() + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     return "execution(" + getSignature().toString() + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   private class MethodSignatureImpl
/*     */     implements MethodSignature
/*     */   {
/*     */     private volatile String[] parameterNames;
/*     */ 
/*     */     
/*     */     private MethodSignatureImpl() {}
/*     */     
/*     */     public String getName() {
/* 180 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getModifiers() {
/* 185 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getModifiers();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getDeclaringType() {
/* 190 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getDeclaringClass();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDeclaringTypeName() {
/* 195 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getDeclaringClass().getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getReturnType() {
/* 200 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getReturnType();
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getMethod() {
/* 205 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?>[] getParameterTypes() {
/* 210 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getParameterTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getParameterNames() {
/* 215 */       if (this.parameterNames == null) {
/* 216 */         this.parameterNames = MethodInvocationProceedingJoinPoint.parameterNameDiscoverer.getParameterNames(getMethod());
/*     */       }
/* 218 */       return this.parameterNames;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?>[] getExceptionTypes() {
/* 223 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getExceptionTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toShortString() {
/* 228 */       return toString(false, false, false, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toLongString() {
/* 233 */       return toString(true, true, true, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 238 */       return toString(false, true, false, true);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private String toString(boolean includeModifier, boolean includeReturnTypeAndArgs, boolean useLongReturnAndArgumentTypeName, boolean useLongTypeName) {
/* 244 */       StringBuilder sb = new StringBuilder();
/* 245 */       if (includeModifier) {
/* 246 */         sb.append(Modifier.toString(getModifiers()));
/* 247 */         sb.append(" ");
/*     */       } 
/* 249 */       if (includeReturnTypeAndArgs) {
/* 250 */         appendType(sb, getReturnType(), useLongReturnAndArgumentTypeName);
/* 251 */         sb.append(" ");
/*     */       } 
/* 253 */       appendType(sb, getDeclaringType(), useLongTypeName);
/* 254 */       sb.append(".");
/* 255 */       sb.append(getMethod().getName());
/* 256 */       sb.append("(");
/* 257 */       Class<?>[] parametersTypes = getParameterTypes();
/* 258 */       appendTypes(sb, parametersTypes, includeReturnTypeAndArgs, useLongReturnAndArgumentTypeName);
/* 259 */       sb.append(")");
/* 260 */       return sb.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void appendTypes(StringBuilder sb, Class<?>[] types, boolean includeArgs, boolean useLongReturnAndArgumentTypeName) {
/* 266 */       if (includeArgs) {
/* 267 */         for (int size = types.length, i = 0; i < size; i++) {
/* 268 */           appendType(sb, types[i], useLongReturnAndArgumentTypeName);
/* 269 */           if (i < size - 1) {
/* 270 */             sb.append(",");
/*     */           }
/*     */         }
/*     */       
/*     */       }
/* 275 */       else if (types.length != 0) {
/* 276 */         sb.append("..");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void appendType(StringBuilder sb, Class<?> type, boolean useLongTypeName) {
/* 282 */       if (type.isArray()) {
/* 283 */         appendType(sb, type.getComponentType(), useLongTypeName);
/* 284 */         sb.append("[]");
/*     */       } else {
/*     */         
/* 287 */         sb.append(useLongTypeName ? type.getName() : type.getSimpleName());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class SourceLocationImpl
/*     */     implements SourceLocation
/*     */   {
/*     */     private SourceLocationImpl() {}
/*     */ 
/*     */     
/*     */     public Class<?> getWithinType() {
/* 300 */       if (MethodInvocationProceedingJoinPoint.this.methodInvocation.getThis() == null) {
/* 301 */         throw new UnsupportedOperationException("No source location joinpoint available: target is null");
/*     */       }
/* 303 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getThis().getClass();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFileName() {
/* 308 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLine() {
/* 313 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public int getColumn() {
/* 319 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\MethodInvocationProceedingJoinPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */