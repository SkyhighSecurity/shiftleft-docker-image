/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class SerializableTypeWrapper
/*     */ {
/*  58 */   private static final Class<?>[] SUPPORTED_SERIALIZABLE_TYPES = new Class[] { GenericArrayType.class, ParameterizedType.class, TypeVariable.class, WildcardType.class };
/*     */ 
/*     */   
/*  61 */   static final ConcurrentReferenceHashMap<Type, Type> cache = new ConcurrentReferenceHashMap(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type forField(Field field) {
/*  68 */     return forTypeProvider(new FieldTypeProvider(field));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type forMethodParameter(MethodParameter methodParameter) {
/*  76 */     return forTypeProvider(new MethodParameterTypeProvider(methodParameter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type forGenericSuperclass(final Class<?> type) {
/*  84 */     return forTypeProvider(new SimpleTypeProvider()
/*     */         {
/*     */           public Type getType() {
/*  87 */             return type.getGenericSuperclass();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type[] forGenericInterfaces(final Class<?> type) {
/*  97 */     Type[] result = new Type[(type.getGenericInterfaces()).length];
/*  98 */     for (int i = 0; i < result.length; i++) {
/*  99 */       final int index = i;
/* 100 */       result[i] = forTypeProvider(new SimpleTypeProvider()
/*     */           {
/*     */             public Type getType() {
/* 103 */               return type.getGenericInterfaces()[index];
/*     */             }
/*     */           });
/*     */     } 
/* 107 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type[] forTypeParameters(final Class<?> type) {
/* 115 */     Type[] result = new Type[(type.getTypeParameters()).length];
/* 116 */     for (int i = 0; i < result.length; i++) {
/* 117 */       final int index = i;
/* 118 */       result[i] = forTypeProvider(new SimpleTypeProvider()
/*     */           {
/*     */             public Type getType() {
/* 121 */               return type.getTypeParameters()[index];
/*     */             }
/*     */           });
/*     */     } 
/* 125 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Type> T unwrap(T type) {
/*     */     Type type1;
/* 135 */     T t = type;
/* 136 */     while (t instanceof SerializableTypeProxy) {
/* 137 */       type1 = ((SerializableTypeProxy)type).getTypeProvider().getType();
/*     */     }
/* 139 */     return (T)type1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Type forTypeProvider(TypeProvider provider) {
/* 146 */     Type providedType = provider.getType();
/* 147 */     if (providedType == null || providedType instanceof Serializable)
/*     */     {
/* 149 */       return providedType;
/*     */     }
/*     */ 
/*     */     
/* 153 */     Type cached = (Type)cache.get(providedType);
/* 154 */     if (cached != null) {
/* 155 */       return cached;
/*     */     }
/* 157 */     for (Class<?> type : SUPPORTED_SERIALIZABLE_TYPES) {
/* 158 */       if (type.isInstance(providedType)) {
/* 159 */         ClassLoader classLoader = provider.getClass().getClassLoader();
/* 160 */         Class<?>[] interfaces = new Class[] { type, SerializableTypeProxy.class, Serializable.class };
/* 161 */         InvocationHandler handler = new TypeProxyInvocationHandler(provider);
/* 162 */         cached = (Type)Proxy.newProxyInstance(classLoader, interfaces, handler);
/* 163 */         cache.put(providedType, cached);
/* 164 */         return cached;
/*     */       } 
/*     */     } 
/* 167 */     throw new IllegalArgumentException("Unsupported Type class: " + providedType.getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface SerializableTypeProxy
/*     */   {
/*     */     SerializableTypeWrapper.TypeProvider getTypeProvider();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface TypeProvider
/*     */     extends Serializable
/*     */   {
/*     */     Type getType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object getSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class SimpleTypeProvider
/*     */     implements TypeProvider
/*     */   {
/*     */     private SimpleTypeProvider() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 208 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TypeProxyInvocationHandler
/*     */     implements InvocationHandler, Serializable
/*     */   {
/*     */     private final SerializableTypeWrapper.TypeProvider provider;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeProxyInvocationHandler(SerializableTypeWrapper.TypeProvider provider) {
/* 224 */       this.provider = provider;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 229 */       if (method.getName().equals("equals")) {
/* 230 */         Object other = args[0];
/*     */         
/* 232 */         if (other instanceof Type) {
/* 233 */           other = SerializableTypeWrapper.unwrap((Type)other);
/*     */         }
/* 235 */         return Boolean.valueOf(this.provider.getType().equals(other));
/*     */       } 
/* 237 */       if (method.getName().equals("hashCode")) {
/* 238 */         return Integer.valueOf(this.provider.getType().hashCode());
/*     */       }
/* 240 */       if (method.getName().equals("getTypeProvider")) {
/* 241 */         return this.provider;
/*     */       }
/*     */       
/* 244 */       if (Type.class == method.getReturnType() && args == null) {
/* 245 */         return SerializableTypeWrapper.forTypeProvider(new SerializableTypeWrapper.MethodInvokeTypeProvider(this.provider, method, -1));
/*     */       }
/* 247 */       if (Type[].class == method.getReturnType() && args == null) {
/* 248 */         Type[] result = new Type[((Type[])method.invoke(this.provider.getType(), args)).length];
/* 249 */         for (int i = 0; i < result.length; i++) {
/* 250 */           result[i] = SerializableTypeWrapper.forTypeProvider(new SerializableTypeWrapper.MethodInvokeTypeProvider(this.provider, method, i));
/*     */         }
/* 252 */         return result;
/*     */       } 
/*     */       
/*     */       try {
/* 256 */         return method.invoke(this.provider.getType(), args);
/*     */       }
/* 258 */       catch (InvocationTargetException ex) {
/* 259 */         throw ex.getTargetException();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class FieldTypeProvider
/*     */     implements TypeProvider
/*     */   {
/*     */     private final String fieldName;
/*     */ 
/*     */     
/*     */     private final Class<?> declaringClass;
/*     */     
/*     */     private transient Field field;
/*     */ 
/*     */     
/*     */     public FieldTypeProvider(Field field) {
/* 278 */       this.fieldName = field.getName();
/* 279 */       this.declaringClass = field.getDeclaringClass();
/* 280 */       this.field = field;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getType() {
/* 285 */       return this.field.getGenericType();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 290 */       return this.field;
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 294 */       inputStream.defaultReadObject();
/*     */       try {
/* 296 */         this.field = this.declaringClass.getDeclaredField(this.fieldName);
/*     */       }
/* 298 */       catch (Throwable ex) {
/* 299 */         throw new IllegalStateException("Could not find original class structure", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class MethodParameterTypeProvider
/*     */     implements TypeProvider
/*     */   {
/*     */     private final String methodName;
/*     */ 
/*     */     
/*     */     private final Class<?>[] parameterTypes;
/*     */     
/*     */     private final Class<?> declaringClass;
/*     */     
/*     */     private final int parameterIndex;
/*     */     
/*     */     private transient MethodParameter methodParameter;
/*     */ 
/*     */     
/*     */     public MethodParameterTypeProvider(MethodParameter methodParameter) {
/* 322 */       if (methodParameter.getMethod() != null) {
/* 323 */         this.methodName = methodParameter.getMethod().getName();
/* 324 */         this.parameterTypes = methodParameter.getMethod().getParameterTypes();
/*     */       } else {
/*     */         
/* 327 */         this.methodName = null;
/* 328 */         this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
/*     */       } 
/* 330 */       this.declaringClass = methodParameter.getDeclaringClass();
/* 331 */       this.parameterIndex = methodParameter.getParameterIndex();
/* 332 */       this.methodParameter = methodParameter;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Type getType() {
/* 338 */       return this.methodParameter.getGenericParameterType();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 343 */       return this.methodParameter;
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 347 */       inputStream.defaultReadObject();
/*     */       try {
/* 349 */         if (this.methodName != null) {
/* 350 */           this
/* 351 */             .methodParameter = new MethodParameter(this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes), this.parameterIndex);
/*     */         } else {
/*     */           
/* 354 */           this
/* 355 */             .methodParameter = new MethodParameter(this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
/*     */         }
/*     */       
/* 358 */       } catch (Throwable ex) {
/* 359 */         throw new IllegalStateException("Could not find original class structure", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class MethodInvokeTypeProvider
/*     */     implements TypeProvider
/*     */   {
/*     */     private final SerializableTypeWrapper.TypeProvider provider;
/*     */ 
/*     */     
/*     */     private final String methodName;
/*     */     
/*     */     private final Class<?> declaringClass;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     private transient Method method;
/*     */     
/*     */     private volatile transient Object result;
/*     */ 
/*     */     
/*     */     public MethodInvokeTypeProvider(SerializableTypeWrapper.TypeProvider provider, Method method, int index) {
/* 384 */       this.provider = provider;
/* 385 */       this.methodName = method.getName();
/* 386 */       this.declaringClass = method.getDeclaringClass();
/* 387 */       this.index = index;
/* 388 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getType() {
/* 393 */       Object result = this.result;
/* 394 */       if (result == null) {
/*     */         
/* 396 */         result = ReflectionUtils.invokeMethod(this.method, this.provider.getType());
/*     */         
/* 398 */         this.result = result;
/*     */       } 
/* 400 */       return (result instanceof Type[]) ? ((Type[])result)[this.index] : (Type)result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 405 */       return null;
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 409 */       inputStream.defaultReadObject();
/* 410 */       this.method = ReflectionUtils.findMethod(this.declaringClass, this.methodName);
/* 411 */       if (this.method.getReturnType() != Type.class && this.method.getReturnType() != Type[].class)
/* 412 */         throw new IllegalStateException("Invalid return type on deserialized method - needs to be Type or Type[]: " + this.method); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\SerializableTypeWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */