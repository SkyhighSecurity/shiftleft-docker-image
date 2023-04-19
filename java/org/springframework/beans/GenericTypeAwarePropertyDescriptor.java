/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ final class GenericTypeAwarePropertyDescriptor
/*     */   extends PropertyDescriptor
/*     */ {
/*     */   private final Class<?> beanClass;
/*     */   private final Method readMethod;
/*     */   private final Method writeMethod;
/*     */   private volatile Set<Method> ambiguousWriteMethods;
/*     */   private MethodParameter writeMethodParameter;
/*     */   private Class<?> propertyType;
/*     */   private final Class<?> propertyEditorClass;
/*     */   
/*     */   public GenericTypeAwarePropertyDescriptor(Class<?> beanClass, String propertyName, Method readMethod, Method writeMethod, Class<?> propertyEditorClass) throws IntrospectionException {
/*  63 */     super(propertyName, (Method)null, (Method)null);
/*     */     
/*  65 */     if (beanClass == null) {
/*  66 */       throw new IntrospectionException("Bean class must not be null");
/*     */     }
/*  68 */     this.beanClass = beanClass;
/*     */     
/*  70 */     Method readMethodToUse = BridgeMethodResolver.findBridgedMethod(readMethod);
/*  71 */     Method writeMethodToUse = BridgeMethodResolver.findBridgedMethod(writeMethod);
/*  72 */     if (writeMethodToUse == null && readMethodToUse != null) {
/*     */ 
/*     */ 
/*     */       
/*  76 */       Method candidate = ClassUtils.getMethodIfAvailable(this.beanClass, "set" + 
/*  77 */           StringUtils.capitalize(getName()), (Class[])null);
/*  78 */       if (candidate != null && (candidate.getParameterTypes()).length == 1) {
/*  79 */         writeMethodToUse = candidate;
/*     */       }
/*     */     } 
/*  82 */     this.readMethod = readMethodToUse;
/*  83 */     this.writeMethod = writeMethodToUse;
/*     */     
/*  85 */     if (this.writeMethod != null) {
/*  86 */       if (this.readMethod == null) {
/*     */ 
/*     */ 
/*     */         
/*  90 */         Set<Method> ambiguousCandidates = new HashSet<Method>();
/*  91 */         for (Method method : beanClass.getMethods()) {
/*  92 */           if (method.getName().equals(writeMethodToUse.getName()) && 
/*  93 */             !method.equals(writeMethodToUse) && !method.isBridge() && (method
/*  94 */             .getParameterTypes()).length == (writeMethodToUse.getParameterTypes()).length) {
/*  95 */             ambiguousCandidates.add(method);
/*     */           }
/*     */         } 
/*  98 */         if (!ambiguousCandidates.isEmpty()) {
/*  99 */           this.ambiguousWriteMethods = ambiguousCandidates;
/*     */         }
/*     */       } 
/* 102 */       this.writeMethodParameter = new MethodParameter(this.writeMethod, 0);
/* 103 */       GenericTypeResolver.resolveParameterType(this.writeMethodParameter, this.beanClass);
/*     */     } 
/*     */     
/* 106 */     if (this.readMethod != null) {
/* 107 */       this.propertyType = GenericTypeResolver.resolveReturnType(this.readMethod, this.beanClass);
/*     */     }
/* 109 */     else if (this.writeMethodParameter != null) {
/* 110 */       this.propertyType = this.writeMethodParameter.getParameterType();
/*     */     } 
/*     */     
/* 113 */     this.propertyEditorClass = propertyEditorClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getBeanClass() {
/* 118 */     return this.beanClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public Method getReadMethod() {
/* 123 */     return this.readMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   public Method getWriteMethod() {
/* 128 */     return this.writeMethod;
/*     */   }
/*     */   
/*     */   public Method getWriteMethodForActualAccess() {
/* 132 */     Set<Method> ambiguousCandidates = this.ambiguousWriteMethods;
/* 133 */     if (ambiguousCandidates != null) {
/* 134 */       this.ambiguousWriteMethods = null;
/* 135 */       LogFactory.getLog(GenericTypeAwarePropertyDescriptor.class).warn("Invalid JavaBean property '" + 
/* 136 */           getName() + "' being accessed! Ambiguous write methods found next to actually used [" + this.writeMethod + "]: " + ambiguousCandidates);
/*     */     } 
/*     */     
/* 139 */     return this.writeMethod;
/*     */   }
/*     */   
/*     */   public MethodParameter getWriteMethodParameter() {
/* 143 */     return this.writeMethodParameter;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getPropertyType() {
/* 148 */     return this.propertyType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getPropertyEditorClass() {
/* 153 */     return this.propertyEditorClass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 159 */     if (this == other) {
/* 160 */       return true;
/*     */     }
/* 162 */     if (!(other instanceof GenericTypeAwarePropertyDescriptor)) {
/* 163 */       return false;
/*     */     }
/* 165 */     GenericTypeAwarePropertyDescriptor otherPd = (GenericTypeAwarePropertyDescriptor)other;
/* 166 */     return (getBeanClass().equals(otherPd.getBeanClass()) && PropertyDescriptorUtils.equals(this, otherPd));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 171 */     int hashCode = getBeanClass().hashCode();
/* 172 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getReadMethod());
/* 173 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getWriteMethod());
/* 174 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\GenericTypeAwarePropertyDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */