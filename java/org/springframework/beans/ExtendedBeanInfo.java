/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.beans.BeanDescriptor;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.EventSetDescriptor;
/*     */ import java.beans.IndexedPropertyDescriptor;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.MethodDescriptor;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ExtendedBeanInfo
/*     */   implements BeanInfo
/*     */ {
/*  78 */   private static final Log logger = LogFactory.getLog(ExtendedBeanInfo.class);
/*     */   
/*     */   private final BeanInfo delegate;
/*     */   
/*  82 */   private final Set<PropertyDescriptor> propertyDescriptors = new TreeSet<PropertyDescriptor>(new PropertyDescriptorComparator());
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
/*     */   public ExtendedBeanInfo(BeanInfo delegate) throws IntrospectionException {
/*  99 */     this.delegate = delegate;
/* 100 */     for (PropertyDescriptor pd : delegate.getPropertyDescriptors()) {
/*     */       try {
/* 102 */         this.propertyDescriptors.add((pd instanceof IndexedPropertyDescriptor) ? new SimpleIndexedPropertyDescriptor((IndexedPropertyDescriptor)pd) : new SimplePropertyDescriptor(pd));
/*     */ 
/*     */       
/*     */       }
/* 106 */       catch (IntrospectionException ex) {
/*     */         
/* 108 */         if (logger.isDebugEnabled()) {
/* 109 */           logger.debug("Ignoring invalid bean property '" + pd.getName() + "': " + ex.getMessage());
/*     */         }
/*     */       } 
/*     */     } 
/* 113 */     MethodDescriptor[] methodDescriptors = delegate.getMethodDescriptors();
/* 114 */     if (methodDescriptors != null) {
/* 115 */       for (Method method : findCandidateWriteMethods(methodDescriptors)) {
/*     */         try {
/* 117 */           handleCandidateWriteMethod(method);
/*     */         }
/* 119 */         catch (IntrospectionException ex) {
/*     */           
/* 121 */           if (logger.isDebugEnabled()) {
/* 122 */             logger.debug("Ignoring candidate write method [" + method + "]: " + ex.getMessage());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Method> findCandidateWriteMethods(MethodDescriptor[] methodDescriptors) {
/* 131 */     List<Method> matches = new ArrayList<Method>();
/* 132 */     for (MethodDescriptor methodDescriptor : methodDescriptors) {
/* 133 */       Method method = methodDescriptor.getMethod();
/* 134 */       if (isCandidateWriteMethod(method)) {
/* 135 */         matches.add(method);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 141 */     Collections.sort(matches, new Comparator<Method>()
/*     */         {
/*     */           public int compare(Method m1, Method m2) {
/* 144 */             return m2.toString().compareTo(m1.toString());
/*     */           }
/*     */         });
/* 147 */     return matches;
/*     */   }
/*     */   
/*     */   public static boolean isCandidateWriteMethod(Method method) {
/* 151 */     String methodName = method.getName();
/* 152 */     Class<?>[] parameterTypes = method.getParameterTypes();
/* 153 */     int nParams = parameterTypes.length;
/* 154 */     return (methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) && (
/* 155 */       !void.class.isAssignableFrom(method.getReturnType()) || Modifier.isStatic(method.getModifiers())) && (nParams == 1 || (nParams == 2 && int.class == parameterTypes[0])));
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleCandidateWriteMethod(Method method) throws IntrospectionException {
/* 160 */     int nParams = (method.getParameterTypes()).length;
/* 161 */     String propertyName = propertyNameFor(method);
/* 162 */     Class<?> propertyType = method.getParameterTypes()[nParams - 1];
/* 163 */     PropertyDescriptor existingPd = findExistingPropertyDescriptor(propertyName, propertyType);
/* 164 */     if (nParams == 1) {
/* 165 */       if (existingPd == null) {
/* 166 */         this.propertyDescriptors.add(new SimplePropertyDescriptor(propertyName, null, method));
/*     */       } else {
/*     */         
/* 169 */         existingPd.setWriteMethod(method);
/*     */       }
/*     */     
/* 172 */     } else if (nParams == 2) {
/* 173 */       if (existingPd == null) {
/* 174 */         this.propertyDescriptors.add(new SimpleIndexedPropertyDescriptor(propertyName, null, null, null, method));
/*     */       
/*     */       }
/* 177 */       else if (existingPd instanceof IndexedPropertyDescriptor) {
/* 178 */         ((IndexedPropertyDescriptor)existingPd).setIndexedWriteMethod(method);
/*     */       } else {
/*     */         
/* 181 */         this.propertyDescriptors.remove(existingPd);
/* 182 */         this.propertyDescriptors.add(new SimpleIndexedPropertyDescriptor(propertyName, existingPd
/* 183 */               .getReadMethod(), existingPd.getWriteMethod(), null, method));
/*     */       } 
/*     */     } else {
/*     */       
/* 187 */       throw new IllegalArgumentException("Write method must have exactly 1 or 2 parameters: " + method);
/*     */     } 
/*     */   }
/*     */   
/*     */   private PropertyDescriptor findExistingPropertyDescriptor(String propertyName, Class<?> propertyType) {
/* 192 */     for (PropertyDescriptor pd : this.propertyDescriptors) {
/*     */       
/* 194 */       String candidateName = pd.getName();
/* 195 */       if (pd instanceof IndexedPropertyDescriptor) {
/* 196 */         IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor)pd;
/* 197 */         Class<?> clazz = ipd.getIndexedPropertyType();
/* 198 */         if (candidateName.equals(propertyName) && (clazz
/* 199 */           .equals(propertyType) || clazz.equals(propertyType.getComponentType()))) {
/* 200 */           return pd;
/*     */         }
/*     */         continue;
/*     */       } 
/* 204 */       Class<?> candidateType = pd.getPropertyType();
/* 205 */       if (candidateName.equals(propertyName) && (candidateType
/* 206 */         .equals(propertyType) || propertyType.equals(candidateType.getComponentType()))) {
/* 207 */         return pd;
/*     */       }
/*     */     } 
/*     */     
/* 211 */     return null;
/*     */   }
/*     */   
/*     */   private String propertyNameFor(Method method) {
/* 215 */     return Introspector.decapitalize(method.getName().substring(3, method.getName().length()));
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
/*     */   public PropertyDescriptor[] getPropertyDescriptors() {
/* 227 */     return this.propertyDescriptors.<PropertyDescriptor>toArray(new PropertyDescriptor[this.propertyDescriptors.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanInfo[] getAdditionalBeanInfo() {
/* 232 */     return this.delegate.getAdditionalBeanInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDescriptor getBeanDescriptor() {
/* 237 */     return this.delegate.getBeanDescriptor();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultEventIndex() {
/* 242 */     return this.delegate.getDefaultEventIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultPropertyIndex() {
/* 247 */     return this.delegate.getDefaultPropertyIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public EventSetDescriptor[] getEventSetDescriptors() {
/* 252 */     return this.delegate.getEventSetDescriptors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Image getIcon(int iconKind) {
/* 257 */     return this.delegate.getIcon(iconKind);
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodDescriptor[] getMethodDescriptors() {
/* 262 */     return this.delegate.getMethodDescriptors();
/*     */   }
/*     */ 
/*     */   
/*     */   static class SimplePropertyDescriptor
/*     */     extends PropertyDescriptor
/*     */   {
/*     */     private Method readMethod;
/*     */     
/*     */     private Method writeMethod;
/*     */     
/*     */     private Class<?> propertyType;
/*     */     private Class<?> propertyEditorClass;
/*     */     
/*     */     public SimplePropertyDescriptor(PropertyDescriptor original) throws IntrospectionException {
/* 277 */       this(original.getName(), original.getReadMethod(), original.getWriteMethod());
/* 278 */       PropertyDescriptorUtils.copyNonMethodProperties(original, this);
/*     */     }
/*     */     
/*     */     public SimplePropertyDescriptor(String propertyName, Method readMethod, Method writeMethod) throws IntrospectionException {
/* 282 */       super(propertyName, null, null);
/* 283 */       this.readMethod = readMethod;
/* 284 */       this.writeMethod = writeMethod;
/* 285 */       this.propertyType = PropertyDescriptorUtils.findPropertyType(readMethod, writeMethod);
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getReadMethod() {
/* 290 */       return this.readMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setReadMethod(Method readMethod) {
/* 295 */       this.readMethod = readMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getWriteMethod() {
/* 300 */       return this.writeMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setWriteMethod(Method writeMethod) {
/* 305 */       this.writeMethod = writeMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getPropertyType() {
/* 310 */       if (this.propertyType == null) {
/*     */         try {
/* 312 */           this.propertyType = PropertyDescriptorUtils.findPropertyType(this.readMethod, this.writeMethod);
/*     */         }
/* 314 */         catch (IntrospectionException introspectionException) {}
/*     */       }
/*     */ 
/*     */       
/* 318 */       return this.propertyType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getPropertyEditorClass() {
/* 323 */       return this.propertyEditorClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPropertyEditorClass(Class<?> propertyEditorClass) {
/* 328 */       this.propertyEditorClass = propertyEditorClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 333 */       return (this == other || (other instanceof PropertyDescriptor && 
/* 334 */         PropertyDescriptorUtils.equals(this, (PropertyDescriptor)other)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 339 */       return ObjectUtils.nullSafeHashCode(getReadMethod()) * 29 + ObjectUtils.nullSafeHashCode(getWriteMethod());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 344 */       return String.format("%s[name=%s, propertyType=%s, readMethod=%s, writeMethod=%s]", new Object[] {
/* 345 */             getClass().getSimpleName(), getName(), getPropertyType(), this.readMethod, this.writeMethod
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class SimpleIndexedPropertyDescriptor
/*     */     extends IndexedPropertyDescriptor
/*     */   {
/*     */     private Method readMethod;
/*     */     
/*     */     private Method writeMethod;
/*     */     
/*     */     private Class<?> propertyType;
/*     */     
/*     */     private Method indexedReadMethod;
/*     */     
/*     */     private Method indexedWriteMethod;
/*     */     private Class<?> indexedPropertyType;
/*     */     private Class<?> propertyEditorClass;
/*     */     
/*     */     public SimpleIndexedPropertyDescriptor(IndexedPropertyDescriptor original) throws IntrospectionException {
/* 367 */       this(original.getName(), original.getReadMethod(), original.getWriteMethod(), original
/* 368 */           .getIndexedReadMethod(), original.getIndexedWriteMethod());
/* 369 */       PropertyDescriptorUtils.copyNonMethodProperties(original, this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public SimpleIndexedPropertyDescriptor(String propertyName, Method readMethod, Method writeMethod, Method indexedReadMethod, Method indexedWriteMethod) throws IntrospectionException {
/* 375 */       super(propertyName, (Method)null, (Method)null, (Method)null, (Method)null);
/* 376 */       this.readMethod = readMethod;
/* 377 */       this.writeMethod = writeMethod;
/* 378 */       this.propertyType = PropertyDescriptorUtils.findPropertyType(readMethod, writeMethod);
/* 379 */       this.indexedReadMethod = indexedReadMethod;
/* 380 */       this.indexedWriteMethod = indexedWriteMethod;
/* 381 */       this.indexedPropertyType = PropertyDescriptorUtils.findIndexedPropertyType(propertyName, this.propertyType, indexedReadMethod, indexedWriteMethod);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Method getReadMethod() {
/* 387 */       return this.readMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setReadMethod(Method readMethod) {
/* 392 */       this.readMethod = readMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getWriteMethod() {
/* 397 */       return this.writeMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setWriteMethod(Method writeMethod) {
/* 402 */       this.writeMethod = writeMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getPropertyType() {
/* 407 */       if (this.propertyType == null) {
/*     */         try {
/* 409 */           this.propertyType = PropertyDescriptorUtils.findPropertyType(this.readMethod, this.writeMethod);
/*     */         }
/* 411 */         catch (IntrospectionException introspectionException) {}
/*     */       }
/*     */ 
/*     */       
/* 415 */       return this.propertyType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getIndexedReadMethod() {
/* 420 */       return this.indexedReadMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setIndexedReadMethod(Method indexedReadMethod) throws IntrospectionException {
/* 425 */       this.indexedReadMethod = indexedReadMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getIndexedWriteMethod() {
/* 430 */       return this.indexedWriteMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setIndexedWriteMethod(Method indexedWriteMethod) throws IntrospectionException {
/* 435 */       this.indexedWriteMethod = indexedWriteMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getIndexedPropertyType() {
/* 440 */       if (this.indexedPropertyType == null) {
/*     */         try {
/* 442 */           this.indexedPropertyType = PropertyDescriptorUtils.findIndexedPropertyType(
/* 443 */               getName(), getPropertyType(), this.indexedReadMethod, this.indexedWriteMethod);
/*     */         }
/* 445 */         catch (IntrospectionException introspectionException) {}
/*     */       }
/*     */ 
/*     */       
/* 449 */       return this.indexedPropertyType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getPropertyEditorClass() {
/* 454 */       return this.propertyEditorClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPropertyEditorClass(Class<?> propertyEditorClass) {
/* 459 */       this.propertyEditorClass = propertyEditorClass;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 467 */       if (this == other) {
/* 468 */         return true;
/*     */       }
/* 470 */       if (!(other instanceof IndexedPropertyDescriptor)) {
/* 471 */         return false;
/*     */       }
/* 473 */       IndexedPropertyDescriptor otherPd = (IndexedPropertyDescriptor)other;
/* 474 */       return (ObjectUtils.nullSafeEquals(getIndexedReadMethod(), otherPd.getIndexedReadMethod()) && 
/* 475 */         ObjectUtils.nullSafeEquals(getIndexedWriteMethod(), otherPd.getIndexedWriteMethod()) && 
/* 476 */         ObjectUtils.nullSafeEquals(getIndexedPropertyType(), otherPd.getIndexedPropertyType()) && 
/* 477 */         PropertyDescriptorUtils.equals(this, otherPd));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 482 */       int hashCode = ObjectUtils.nullSafeHashCode(getReadMethod());
/* 483 */       hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getWriteMethod());
/* 484 */       hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getIndexedReadMethod());
/* 485 */       hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getIndexedWriteMethod());
/* 486 */       return hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 491 */       return String.format("%s[name=%s, propertyType=%s, indexedPropertyType=%s, readMethod=%s, writeMethod=%s, indexedReadMethod=%s, indexedWriteMethod=%s]", new Object[] {
/*     */             
/* 493 */             getClass().getSimpleName(), getName(), getPropertyType(), getIndexedPropertyType(), this.readMethod, this.writeMethod, this.indexedReadMethod, this.indexedWriteMethod
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class PropertyDescriptorComparator
/*     */     implements Comparator<PropertyDescriptor>
/*     */   {
/*     */     public int compare(PropertyDescriptor desc1, PropertyDescriptor desc2) {
/* 508 */       String left = desc1.getName();
/* 509 */       String right = desc2.getName();
/* 510 */       for (int i = 0; i < left.length(); i++) {
/* 511 */         if (right.length() == i) {
/* 512 */           return 1;
/*     */         }
/* 514 */         int result = left.getBytes()[i] - right.getBytes()[i];
/* 515 */         if (result != 0) {
/* 516 */           return result;
/*     */         }
/*     */       } 
/* 519 */       return left.length() - right.length();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\ExtendedBeanInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */