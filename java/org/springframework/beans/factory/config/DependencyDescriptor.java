/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.core.GenericCollectionTypeResolver;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.ResolvableType;
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
/*     */ public class DependencyDescriptor
/*     */   extends InjectionPoint
/*     */   implements Serializable
/*     */ {
/*     */   private final Class<?> declaringClass;
/*     */   private String methodName;
/*     */   private Class<?>[] parameterTypes;
/*     */   private int parameterIndex;
/*     */   private String fieldName;
/*     */   private final boolean required;
/*     */   private final boolean eager;
/*  61 */   private int nestingLevel = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<?> containingClass;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile ResolvableType resolvableType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(MethodParameter methodParameter, boolean required) {
/*  75 */     this(methodParameter, required, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(MethodParameter methodParameter, boolean required, boolean eager) {
/*  86 */     super(methodParameter);
/*  87 */     this.declaringClass = methodParameter.getDeclaringClass();
/*  88 */     if (this.methodParameter.getMethod() != null) {
/*  89 */       this.methodName = methodParameter.getMethod().getName();
/*  90 */       this.parameterTypes = methodParameter.getMethod().getParameterTypes();
/*     */     } else {
/*     */       
/*  93 */       this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
/*     */     } 
/*  95 */     this.parameterIndex = methodParameter.getParameterIndex();
/*  96 */     this.containingClass = methodParameter.getContainingClass();
/*  97 */     this.required = required;
/*  98 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(Field field, boolean required) {
/* 108 */     this(field, required, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(Field field, boolean required, boolean eager) {
/* 119 */     super(field);
/* 120 */     this.declaringClass = field.getDeclaringClass();
/* 121 */     this.fieldName = field.getName();
/* 122 */     this.required = required;
/* 123 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor(DependencyDescriptor original) {
/* 131 */     super(original);
/* 132 */     this.declaringClass = original.declaringClass;
/* 133 */     this.methodName = original.methodName;
/* 134 */     this.parameterTypes = original.parameterTypes;
/* 135 */     this.parameterIndex = original.parameterIndex;
/* 136 */     this.fieldName = original.fieldName;
/* 137 */     this.containingClass = original.containingClass;
/* 138 */     this.required = original.required;
/* 139 */     this.eager = original.eager;
/* 140 */     this.nestingLevel = original.nestingLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequired() {
/* 148 */     return this.required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEager() {
/* 156 */     return this.eager;
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
/*     */   public Object resolveNotUnique(Class<?> type, Map<String, Object> matchingBeans) throws BeansException {
/* 173 */     throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
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
/*     */   public Object resolveShortcut(BeanFactory beanFactory) throws BeansException {
/* 189 */     return null;
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
/*     */   public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) throws BeansException {
/* 208 */     return beanFactory.getBean(beanName, requiredType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseNestingLevel() {
/* 217 */     this.nestingLevel++;
/* 218 */     this.resolvableType = null;
/* 219 */     if (this.methodParameter != null) {
/* 220 */       this.methodParameter.increaseNestingLevel();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContainingClass(Class<?> containingClass) {
/* 231 */     this.containingClass = containingClass;
/* 232 */     this.resolvableType = null;
/* 233 */     if (this.methodParameter != null) {
/* 234 */       GenericTypeResolver.resolveParameterType(this.methodParameter, containingClass);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvableType getResolvableType() {
/* 243 */     ResolvableType resolvableType = this.resolvableType;
/* 244 */     if (resolvableType == null) {
/*     */ 
/*     */       
/* 247 */       resolvableType = (this.field != null) ? ResolvableType.forField(this.field, this.nestingLevel, this.containingClass) : ResolvableType.forMethodParameter(this.methodParameter);
/* 248 */       this.resolvableType = resolvableType;
/*     */     } 
/* 250 */     return resolvableType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean fallbackMatchAllowed() {
/* 261 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependencyDescriptor forFallbackMatch() {
/* 270 */     return new DependencyDescriptor(this)
/*     */       {
/*     */         public boolean fallbackMatchAllowed() {
/* 273 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
/* 285 */     if (this.methodParameter != null) {
/* 286 */       this.methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDependencyName() {
/* 295 */     return (this.field != null) ? this.field.getName() : this.methodParameter.getParameterName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getDependencyType() {
/* 303 */     if (this.field != null) {
/* 304 */       if (this.nestingLevel > 1) {
/* 305 */         Type type = this.field.getGenericType();
/* 306 */         for (int i = 2; i <= this.nestingLevel; i++) {
/* 307 */           if (type instanceof ParameterizedType) {
/* 308 */             Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 309 */             type = args[args.length - 1];
/*     */           } 
/*     */         } 
/* 312 */         if (type instanceof Class) {
/* 313 */           return (Class)type;
/*     */         }
/* 315 */         if (type instanceof ParameterizedType) {
/* 316 */           Type arg = ((ParameterizedType)type).getRawType();
/* 317 */           if (arg instanceof Class) {
/* 318 */             return (Class)arg;
/*     */           }
/*     */         } 
/* 321 */         return Object.class;
/*     */       } 
/*     */       
/* 324 */       return this.field.getType();
/*     */     } 
/*     */ 
/*     */     
/* 328 */     return this.methodParameter.getNestedParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> getCollectionType() {
/* 339 */     return (this.field != null) ? 
/* 340 */       GenericCollectionTypeResolver.getCollectionFieldType(this.field, this.nestingLevel) : 
/* 341 */       GenericCollectionTypeResolver.getCollectionParameterType(this.methodParameter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> getMapKeyType() {
/* 351 */     return (this.field != null) ? 
/* 352 */       GenericCollectionTypeResolver.getMapKeyFieldType(this.field, this.nestingLevel) : 
/* 353 */       GenericCollectionTypeResolver.getMapKeyParameterType(this.methodParameter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> getMapValueType() {
/* 363 */     return (this.field != null) ? 
/* 364 */       GenericCollectionTypeResolver.getMapValueFieldType(this.field, this.nestingLevel) : 
/* 365 */       GenericCollectionTypeResolver.getMapValueParameterType(this.methodParameter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 371 */     if (this == other) {
/* 372 */       return true;
/*     */     }
/* 374 */     if (!super.equals(other)) {
/* 375 */       return false;
/*     */     }
/* 377 */     DependencyDescriptor otherDesc = (DependencyDescriptor)other;
/* 378 */     return (this.required == otherDesc.required && this.eager == otherDesc.eager && this.nestingLevel == otherDesc.nestingLevel && this.containingClass == otherDesc.containingClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 389 */     ois.defaultReadObject();
/*     */ 
/*     */     
/*     */     try {
/* 393 */       if (this.fieldName != null) {
/* 394 */         this.field = this.declaringClass.getDeclaredField(this.fieldName);
/*     */       } else {
/*     */         
/* 397 */         if (this.methodName != null) {
/* 398 */           this
/* 399 */             .methodParameter = new MethodParameter(this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes), this.parameterIndex);
/*     */         } else {
/*     */           
/* 402 */           this
/* 403 */             .methodParameter = new MethodParameter(this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
/*     */         } 
/* 405 */         for (int i = 1; i < this.nestingLevel; i++) {
/* 406 */           this.methodParameter.increaseNestingLevel();
/*     */         }
/*     */       }
/*     */     
/* 410 */     } catch (Throwable ex) {
/* 411 */       throw new IllegalStateException("Could not find original class structure", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\DependencyDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */