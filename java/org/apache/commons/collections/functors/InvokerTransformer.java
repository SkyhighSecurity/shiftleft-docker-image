/*     */ package org.apache.commons.collections.functors;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.commons.collections.FunctorException;
/*     */ import org.apache.commons.collections.Transformer;
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
/*     */ public class InvokerTransformer
/*     */   implements Transformer, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8653385846894047688L;
/*     */   private final String iMethodName;
/*     */   private final Class[] iParamTypes;
/*     */   private final Object[] iArgs;
/*     */   
/*     */   public static Transformer getInstance(String methodName) {
/*  67 */     if (methodName == null) {
/*  68 */       throw new IllegalArgumentException("The method to invoke must not be null");
/*     */     }
/*  70 */     return new InvokerTransformer(methodName);
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
/*     */   public static Transformer getInstance(String methodName, Class[] paramTypes, Object[] args) {
/*  82 */     if (methodName == null) {
/*  83 */       throw new IllegalArgumentException("The method to invoke must not be null");
/*     */     }
/*  85 */     if ((paramTypes == null && args != null) || (paramTypes != null && args == null) || (paramTypes != null && args != null && paramTypes.length != args.length))
/*     */     {
/*     */       
/*  88 */       throw new IllegalArgumentException("The parameter types must match the arguments");
/*     */     }
/*  90 */     if (paramTypes == null || paramTypes.length == 0) {
/*  91 */       return new InvokerTransformer(methodName);
/*     */     }
/*  93 */     paramTypes = (Class[])paramTypes.clone();
/*  94 */     args = (Object[])args.clone();
/*  95 */     return new InvokerTransformer(methodName, paramTypes, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InvokerTransformer(String methodName) {
/* 106 */     this.iMethodName = methodName;
/* 107 */     this.iParamTypes = null;
/* 108 */     this.iArgs = null;
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
/*     */   public InvokerTransformer(String methodName, Class[] paramTypes, Object[] args) {
/* 121 */     this.iMethodName = methodName;
/* 122 */     this.iParamTypes = paramTypes;
/* 123 */     this.iArgs = args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object transform(Object input) {
/* 133 */     if (input == null) {
/* 134 */       return null;
/*     */     }
/*     */     try {
/* 137 */       Class cls = input.getClass();
/* 138 */       Method method = cls.getMethod(this.iMethodName, this.iParamTypes);
/* 139 */       return method.invoke(input, this.iArgs);
/*     */     }
/* 141 */     catch (NoSuchMethodException ex) {
/* 142 */       throw new FunctorException("InvokerTransformer: The method '" + this.iMethodName + "' on '" + input.getClass() + "' does not exist");
/* 143 */     } catch (IllegalAccessException ex) {
/* 144 */       throw new FunctorException("InvokerTransformer: The method '" + this.iMethodName + "' on '" + input.getClass() + "' cannot be accessed");
/* 145 */     } catch (InvocationTargetException ex) {
/* 146 */       throw new FunctorException("InvokerTransformer: The method '" + this.iMethodName + "' on '" + input.getClass() + "' threw an exception", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream os) throws IOException {
/* 155 */     FunctorUtils.checkUnsafeSerialization(InvokerTransformer.class);
/* 156 */     os.defaultWriteObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
/* 164 */     FunctorUtils.checkUnsafeSerialization(InvokerTransformer.class);
/* 165 */     is.defaultReadObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\InvokerTransformer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */