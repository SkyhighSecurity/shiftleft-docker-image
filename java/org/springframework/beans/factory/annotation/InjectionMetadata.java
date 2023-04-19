/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
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
/*     */ public class InjectionMetadata
/*     */ {
/*  49 */   private static final Log logger = LogFactory.getLog(InjectionMetadata.class);
/*     */   
/*     */   private final Class<?> targetClass;
/*     */   
/*     */   private final Collection<InjectedElement> injectedElements;
/*     */   
/*     */   private volatile Set<InjectedElement> checkedElements;
/*     */ 
/*     */   
/*     */   public InjectionMetadata(Class<?> targetClass, Collection<InjectedElement> elements) {
/*  59 */     this.targetClass = targetClass;
/*  60 */     this.injectedElements = elements;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkConfigMembers(RootBeanDefinition beanDefinition) {
/*  65 */     Set<InjectedElement> checkedElements = new LinkedHashSet<InjectedElement>(this.injectedElements.size());
/*  66 */     for (InjectedElement element : this.injectedElements) {
/*  67 */       Member member = element.getMember();
/*  68 */       if (!beanDefinition.isExternallyManagedConfigMember(member)) {
/*  69 */         beanDefinition.registerExternallyManagedConfigMember(member);
/*  70 */         checkedElements.add(element);
/*  71 */         if (logger.isDebugEnabled()) {
/*  72 */           logger.debug("Registered injected element on class [" + this.targetClass.getName() + "]: " + element);
/*     */         }
/*     */       } 
/*     */     } 
/*  76 */     this.checkedElements = checkedElements;
/*     */   }
/*     */   
/*     */   public void inject(Object target, String beanName, PropertyValues pvs) throws Throwable {
/*  80 */     Collection<InjectedElement> elementsToIterate = (this.checkedElements != null) ? this.checkedElements : this.injectedElements;
/*     */     
/*  82 */     if (!elementsToIterate.isEmpty()) {
/*  83 */       for (InjectedElement element : elementsToIterate) {
/*  84 */         if (logger.isDebugEnabled()) {
/*  85 */           logger.debug("Processing injected element of bean '" + beanName + "': " + element);
/*     */         }
/*  87 */         element.inject(target, beanName, pvs);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear(PropertyValues pvs) {
/*  96 */     Collection<InjectedElement> elementsToIterate = (this.checkedElements != null) ? this.checkedElements : this.injectedElements;
/*     */     
/*  98 */     if (!elementsToIterate.isEmpty()) {
/*  99 */       for (InjectedElement element : elementsToIterate) {
/* 100 */         element.clearPropertySkipping(pvs);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean needsRefresh(InjectionMetadata metadata, Class<?> clazz) {
/* 107 */     return (metadata == null || metadata.targetClass != clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class InjectedElement
/*     */   {
/*     */     protected final Member member;
/*     */ 
/*     */     
/*     */     protected final boolean isField;
/*     */     
/*     */     protected final PropertyDescriptor pd;
/*     */     
/*     */     protected volatile Boolean skip;
/*     */ 
/*     */     
/*     */     protected InjectedElement(Member member, PropertyDescriptor pd) {
/* 125 */       this.member = member;
/* 126 */       this.isField = member instanceof Field;
/* 127 */       this.pd = pd;
/*     */     }
/*     */     
/*     */     public final Member getMember() {
/* 131 */       return this.member;
/*     */     }
/*     */     
/*     */     protected final Class<?> getResourceType() {
/* 135 */       if (this.isField) {
/* 136 */         return ((Field)this.member).getType();
/*     */       }
/* 138 */       if (this.pd != null) {
/* 139 */         return this.pd.getPropertyType();
/*     */       }
/*     */       
/* 142 */       return ((Method)this.member).getParameterTypes()[0];
/*     */     }
/*     */ 
/*     */     
/*     */     protected final void checkResourceType(Class<?> resourceType) {
/* 147 */       if (this.isField) {
/* 148 */         Class<?> fieldType = ((Field)this.member).getType();
/* 149 */         if (!resourceType.isAssignableFrom(fieldType) && !fieldType.isAssignableFrom(resourceType)) {
/* 150 */           throw new IllegalStateException("Specified field type [" + fieldType + "] is incompatible with resource type [" + resourceType
/* 151 */               .getName() + "]");
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 156 */         Class<?> paramType = (this.pd != null) ? this.pd.getPropertyType() : ((Method)this.member).getParameterTypes()[0];
/* 157 */         if (!resourceType.isAssignableFrom(paramType) && !paramType.isAssignableFrom(resourceType)) {
/* 158 */           throw new IllegalStateException("Specified parameter type [" + paramType + "] is incompatible with resource type [" + resourceType
/* 159 */               .getName() + "]");
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void inject(Object target, String requestingBeanName, PropertyValues pvs) throws Throwable {
/* 168 */       if (this.isField) {
/* 169 */         Field field = (Field)this.member;
/* 170 */         ReflectionUtils.makeAccessible(field);
/* 171 */         field.set(target, getResourceToInject(target, requestingBeanName));
/*     */       } else {
/*     */         
/* 174 */         if (checkPropertySkipping(pvs)) {
/*     */           return;
/*     */         }
/*     */         try {
/* 178 */           Method method = (Method)this.member;
/* 179 */           ReflectionUtils.makeAccessible(method);
/* 180 */           method.invoke(target, new Object[] { getResourceToInject(target, requestingBeanName) });
/*     */         }
/* 182 */         catch (InvocationTargetException ex) {
/* 183 */           throw ex.getTargetException();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean checkPropertySkipping(PropertyValues pvs) {
/* 194 */       if (this.skip != null) {
/* 195 */         return this.skip.booleanValue();
/*     */       }
/* 197 */       if (pvs == null) {
/* 198 */         this.skip = Boolean.valueOf(false);
/* 199 */         return false;
/*     */       } 
/* 201 */       synchronized (pvs) {
/* 202 */         if (this.skip != null) {
/* 203 */           return this.skip.booleanValue();
/*     */         }
/* 205 */         if (this.pd != null) {
/* 206 */           if (pvs.contains(this.pd.getName())) {
/*     */             
/* 208 */             this.skip = Boolean.valueOf(true);
/* 209 */             return true;
/*     */           } 
/* 211 */           if (pvs instanceof MutablePropertyValues) {
/* 212 */             ((MutablePropertyValues)pvs).registerProcessedProperty(this.pd.getName());
/*     */           }
/*     */         } 
/* 215 */         this.skip = Boolean.valueOf(false);
/* 216 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void clearPropertySkipping(PropertyValues pvs) {
/* 225 */       if (pvs == null) {
/*     */         return;
/*     */       }
/* 228 */       synchronized (pvs) {
/* 229 */         if (Boolean.FALSE.equals(this.skip) && this.pd != null && pvs instanceof MutablePropertyValues) {
/* 230 */           ((MutablePropertyValues)pvs).clearProcessedProperty(this.pd.getName());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object getResourceToInject(Object target, String requestingBeanName) {
/* 239 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 244 */       if (this == other) {
/* 245 */         return true;
/*     */       }
/* 247 */       if (!(other instanceof InjectedElement)) {
/* 248 */         return false;
/*     */       }
/* 250 */       InjectedElement otherElement = (InjectedElement)other;
/* 251 */       return this.member.equals(otherElement.member);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 256 */       return this.member.getClass().hashCode() * 29 + this.member.getName().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 261 */       return getClass().getSimpleName() + " for " + this.member;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\InjectionMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */