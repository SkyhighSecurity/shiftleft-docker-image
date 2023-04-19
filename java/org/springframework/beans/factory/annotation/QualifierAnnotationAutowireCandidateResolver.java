/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*     */ import org.springframework.beans.factory.support.GenericTypeAwareAutowireCandidateResolver;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class QualifierAnnotationAutowireCandidateResolver
/*     */   extends GenericTypeAwareAutowireCandidateResolver
/*     */ {
/*  61 */   private final Set<Class<? extends Annotation>> qualifierTypes = new LinkedHashSet<Class<? extends Annotation>>(2);
/*     */   
/*  63 */   private Class<? extends Annotation> valueAnnotationType = (Class)Value.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QualifierAnnotationAutowireCandidateResolver() {
/*  73 */     this.qualifierTypes.add(Qualifier.class);
/*     */     try {
/*  75 */       this.qualifierTypes.add(ClassUtils.forName("javax.inject.Qualifier", QualifierAnnotationAutowireCandidateResolver.class
/*  76 */             .getClassLoader()));
/*     */     }
/*  78 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QualifierAnnotationAutowireCandidateResolver(Class<? extends Annotation> qualifierType) {
/*  89 */     Assert.notNull(qualifierType, "'qualifierType' must not be null");
/*  90 */     this.qualifierTypes.add(qualifierType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QualifierAnnotationAutowireCandidateResolver(Set<Class<? extends Annotation>> qualifierTypes) {
/*  99 */     Assert.notNull(qualifierTypes, "'qualifierTypes' must not be null");
/* 100 */     this.qualifierTypes.addAll(qualifierTypes);
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
/*     */   public void addQualifierType(Class<? extends Annotation> qualifierType) {
/* 115 */     this.qualifierTypes.add(qualifierType);
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
/*     */   public void setValueAnnotationType(Class<? extends Annotation> valueAnnotationType) {
/* 128 */     this.valueAnnotationType = valueAnnotationType;
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
/*     */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
/* 146 */     boolean match = super.isAutowireCandidate(bdHolder, descriptor);
/* 147 */     if (match && descriptor != null) {
/* 148 */       match = checkQualifiers(bdHolder, descriptor.getAnnotations());
/* 149 */       if (match) {
/* 150 */         MethodParameter methodParam = descriptor.getMethodParameter();
/* 151 */         if (methodParam != null) {
/* 152 */           Method method = methodParam.getMethod();
/* 153 */           if (method == null || void.class == method.getReturnType()) {
/* 154 */             match = checkQualifiers(bdHolder, methodParam.getMethodAnnotations());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 159 */     return match;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkQualifiers(BeanDefinitionHolder bdHolder, Annotation[] annotationsToSearch) {
/* 166 */     if (ObjectUtils.isEmpty((Object[])annotationsToSearch)) {
/* 167 */       return true;
/*     */     }
/* 169 */     SimpleTypeConverter typeConverter = new SimpleTypeConverter();
/* 170 */     for (Annotation annotation : annotationsToSearch) {
/* 171 */       Class<? extends Annotation> type = annotation.annotationType();
/* 172 */       boolean checkMeta = true;
/* 173 */       boolean fallbackToMeta = false;
/* 174 */       if (isQualifier(type)) {
/* 175 */         if (!checkQualifier(bdHolder, annotation, (TypeConverter)typeConverter)) {
/* 176 */           fallbackToMeta = true;
/*     */         } else {
/*     */           
/* 179 */           checkMeta = false;
/*     */         } 
/*     */       }
/* 182 */       if (checkMeta) {
/* 183 */         boolean foundMeta = false;
/* 184 */         for (Annotation metaAnn : type.getAnnotations()) {
/* 185 */           Class<? extends Annotation> metaType = metaAnn.annotationType();
/* 186 */           if (isQualifier(metaType)) {
/* 187 */             foundMeta = true;
/*     */ 
/*     */             
/* 190 */             if ((fallbackToMeta && StringUtils.isEmpty(AnnotationUtils.getValue(metaAnn))) || 
/* 191 */               !checkQualifier(bdHolder, metaAnn, (TypeConverter)typeConverter)) {
/* 192 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/* 196 */         if (fallbackToMeta && !foundMeta) {
/* 197 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 201 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isQualifier(Class<? extends Annotation> annotationType) {
/* 208 */     for (Class<? extends Annotation> qualifierType : this.qualifierTypes) {
/* 209 */       if (annotationType.equals(qualifierType) || annotationType.isAnnotationPresent(qualifierType)) {
/* 210 */         return true;
/*     */       }
/*     */     } 
/* 213 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkQualifier(BeanDefinitionHolder bdHolder, Annotation annotation, TypeConverter typeConverter) {
/* 222 */     Class<? extends Annotation> type = annotation.annotationType();
/* 223 */     RootBeanDefinition bd = (RootBeanDefinition)bdHolder.getBeanDefinition();
/*     */     
/* 225 */     AutowireCandidateQualifier qualifier = bd.getQualifier(type.getName());
/* 226 */     if (qualifier == null) {
/* 227 */       qualifier = bd.getQualifier(ClassUtils.getShortName(type));
/*     */     }
/* 229 */     if (qualifier == null) {
/*     */       
/* 231 */       Annotation targetAnnotation = getQualifiedElementAnnotation(bd, type);
/*     */       
/* 233 */       if (targetAnnotation == null) {
/* 234 */         targetAnnotation = getFactoryMethodAnnotation(bd, type);
/*     */       }
/* 236 */       if (targetAnnotation == null) {
/* 237 */         RootBeanDefinition dbd = getResolvedDecoratedDefinition(bd);
/* 238 */         if (dbd != null) {
/* 239 */           targetAnnotation = getFactoryMethodAnnotation(dbd, type);
/*     */         }
/*     */       } 
/* 242 */       if (targetAnnotation == null) {
/*     */         
/* 244 */         if (getBeanFactory() != null) {
/*     */           try {
/* 246 */             Class<?> beanType = getBeanFactory().getType(bdHolder.getBeanName());
/* 247 */             if (beanType != null) {
/* 248 */               targetAnnotation = AnnotationUtils.getAnnotation(ClassUtils.getUserClass(beanType), type);
/*     */             }
/*     */           }
/* 251 */           catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */         }
/*     */ 
/*     */         
/* 255 */         if (targetAnnotation == null && bd.hasBeanClass()) {
/* 256 */           targetAnnotation = AnnotationUtils.getAnnotation(ClassUtils.getUserClass(bd.getBeanClass()), type);
/*     */         }
/*     */       } 
/* 259 */       if (targetAnnotation != null && targetAnnotation.equals(annotation)) {
/* 260 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 264 */     Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);
/* 265 */     if (attributes.isEmpty() && qualifier == null)
/*     */     {
/* 267 */       return false;
/*     */     }
/* 269 */     for (Map.Entry<String, Object> entry : attributes.entrySet()) {
/* 270 */       String attributeName = entry.getKey();
/* 271 */       Object expectedValue = entry.getValue();
/* 272 */       Object actualValue = null;
/*     */       
/* 274 */       if (qualifier != null) {
/* 275 */         actualValue = qualifier.getAttribute(attributeName);
/*     */       }
/* 277 */       if (actualValue == null)
/*     */       {
/* 279 */         actualValue = bd.getAttribute(attributeName);
/*     */       }
/* 281 */       if (actualValue == null && attributeName.equals("value") && expectedValue instanceof String && bdHolder
/* 282 */         .matchesName((String)expectedValue)) {
/*     */         continue;
/*     */       }
/*     */       
/* 286 */       if (actualValue == null && qualifier != null)
/*     */       {
/* 288 */         actualValue = AnnotationUtils.getDefaultValue(annotation, attributeName);
/*     */       }
/* 290 */       if (actualValue != null) {
/* 291 */         actualValue = typeConverter.convertIfNecessary(actualValue, expectedValue.getClass());
/*     */       }
/* 293 */       if (!expectedValue.equals(actualValue)) {
/* 294 */         return false;
/*     */       }
/*     */     } 
/* 297 */     return true;
/*     */   }
/*     */   
/*     */   protected Annotation getQualifiedElementAnnotation(RootBeanDefinition bd, Class<? extends Annotation> type) {
/* 301 */     AnnotatedElement qualifiedElement = bd.getQualifiedElement();
/* 302 */     return (qualifiedElement != null) ? AnnotationUtils.getAnnotation(qualifiedElement, type) : null;
/*     */   }
/*     */   
/*     */   protected Annotation getFactoryMethodAnnotation(RootBeanDefinition bd, Class<? extends Annotation> type) {
/* 306 */     Method resolvedFactoryMethod = bd.getResolvedFactoryMethod();
/* 307 */     return (resolvedFactoryMethod != null) ? AnnotationUtils.getAnnotation(resolvedFactoryMethod, type) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequired(DependencyDescriptor descriptor) {
/* 318 */     if (!super.isRequired(descriptor)) {
/* 319 */       return false;
/*     */     }
/* 321 */     Autowired autowired = (Autowired)descriptor.getAnnotation(Autowired.class);
/* 322 */     return (autowired == null || autowired.required());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getSuggestedValue(DependencyDescriptor descriptor) {
/* 331 */     Object value = findValue(descriptor.getAnnotations());
/* 332 */     if (value == null) {
/* 333 */       MethodParameter methodParam = descriptor.getMethodParameter();
/* 334 */       if (methodParam != null) {
/* 335 */         value = findValue(methodParam.getMethodAnnotations());
/*     */       }
/*     */     } 
/* 338 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object findValue(Annotation[] annotationsToSearch) {
/* 345 */     if (annotationsToSearch.length > 0) {
/* 346 */       AnnotationAttributes attr = AnnotatedElementUtils.getMergedAnnotationAttributes(
/* 347 */           AnnotatedElementUtils.forAnnotations(annotationsToSearch), this.valueAnnotationType);
/* 348 */       if (attr != null) {
/* 349 */         return extractValue(attr);
/*     */       }
/*     */     } 
/* 352 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object extractValue(AnnotationAttributes attr) {
/* 360 */     Object value = attr.get("value");
/* 361 */     if (value == null) {
/* 362 */       throw new IllegalStateException("Value annotation must have a value attribute");
/*     */     }
/* 364 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\annotation\QualifierAnnotationAutowireCandidateResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */