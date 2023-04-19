/*     */ package org.springframework.web.method;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.annotation.OrderUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ControllerAdviceBean
/*     */   implements Ordered
/*     */ {
/*     */   private final Object bean;
/*     */   private final BeanFactory beanFactory;
/*     */   private final int order;
/*     */   private final Set<String> basePackages;
/*     */   private final List<Class<?>> assignableTypes;
/*     */   private final List<Class<? extends Annotation>> annotations;
/*     */   
/*     */   public ControllerAdviceBean(Object bean) {
/*  72 */     this(bean, (BeanFactory)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ControllerAdviceBean(String beanName, BeanFactory beanFactory) {
/*  81 */     this(beanName, beanFactory);
/*     */   }
/*     */   private ControllerAdviceBean(Object bean, BeanFactory beanFactory) {
/*     */     Class<?> beanType;
/*  85 */     this.bean = bean;
/*  86 */     this.beanFactory = beanFactory;
/*     */ 
/*     */     
/*  89 */     if (bean instanceof String) {
/*  90 */       String beanName = (String)bean;
/*  91 */       Assert.hasText(beanName, "Bean name must not be null");
/*  92 */       Assert.notNull(beanFactory, "BeanFactory must not be null");
/*  93 */       if (!beanFactory.containsBean(beanName)) {
/*  94 */         throw new IllegalArgumentException("BeanFactory [" + beanFactory + "] does not contain specified controller advice bean '" + beanName + "'");
/*     */       }
/*     */       
/*  97 */       beanType = this.beanFactory.getType(beanName);
/*  98 */       this.order = initOrderFromBeanType(beanType);
/*     */     } else {
/*     */       
/* 101 */       Assert.notNull(bean, "Bean must not be null");
/* 102 */       beanType = bean.getClass();
/* 103 */       this.order = initOrderFromBean(bean);
/*     */     } 
/*     */ 
/*     */     
/* 107 */     ControllerAdvice annotation = (ControllerAdvice)AnnotatedElementUtils.findMergedAnnotation(beanType, ControllerAdvice.class);
/*     */     
/* 109 */     if (annotation != null) {
/* 110 */       this.basePackages = initBasePackages(annotation);
/* 111 */       this.assignableTypes = Arrays.asList(annotation.assignableTypes());
/* 112 */       this.annotations = Arrays.asList((Class<? extends Annotation>[])annotation.annotations());
/*     */     } else {
/*     */       
/* 115 */       this.basePackages = Collections.emptySet();
/* 116 */       this.assignableTypes = Collections.emptyList();
/* 117 */       this.annotations = Collections.emptyList();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 128 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getBeanType() {
/* 138 */     Class<?> clazz = (this.bean instanceof String) ? this.beanFactory.getType((String)this.bean) : this.bean.getClass();
/* 139 */     return ClassUtils.getUserClass(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object resolveBean() {
/* 146 */     return (this.bean instanceof String) ? this.beanFactory.getBean((String)this.bean) : this.bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isApplicableToBeanType(Class<?> beanType) {
/* 157 */     if (!hasSelectors()) {
/* 158 */       return true;
/*     */     }
/* 160 */     if (beanType != null) {
/* 161 */       for (String basePackage : this.basePackages) {
/* 162 */         if (beanType.getName().startsWith(basePackage)) {
/* 163 */           return true;
/*     */         }
/*     */       } 
/* 166 */       for (Class<?> clazz : this.assignableTypes) {
/* 167 */         if (ClassUtils.isAssignable(clazz, beanType)) {
/* 168 */           return true;
/*     */         }
/*     */       } 
/* 171 */       for (Class<? extends Annotation> annotationClass : this.annotations) {
/* 172 */         if (AnnotationUtils.findAnnotation(beanType, annotationClass) != null) {
/* 173 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 177 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasSelectors() {
/* 181 */     return (!this.basePackages.isEmpty() || !this.assignableTypes.isEmpty() || !this.annotations.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 187 */     if (this == other) {
/* 188 */       return true;
/*     */     }
/* 190 */     if (!(other instanceof ControllerAdviceBean)) {
/* 191 */       return false;
/*     */     }
/* 193 */     ControllerAdviceBean otherAdvice = (ControllerAdviceBean)other;
/* 194 */     return (this.bean.equals(otherAdvice.bean) && this.beanFactory == otherAdvice.beanFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 199 */     return this.bean.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 204 */     return this.bean.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext applicationContext) {
/* 214 */     List<ControllerAdviceBean> beans = new ArrayList<ControllerAdviceBean>();
/* 215 */     for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)applicationContext, Object.class)) {
/* 216 */       if (applicationContext.findAnnotationOnBean(name, ControllerAdvice.class) != null) {
/* 217 */         beans.add(new ControllerAdviceBean(name, (BeanFactory)applicationContext));
/*     */       }
/*     */     } 
/* 220 */     return beans;
/*     */   }
/*     */   
/*     */   private static int initOrderFromBean(Object bean) {
/* 224 */     return (bean instanceof Ordered) ? ((Ordered)bean).getOrder() : initOrderFromBeanType(bean.getClass());
/*     */   }
/*     */   
/*     */   private static int initOrderFromBeanType(Class<?> beanType) {
/* 228 */     return OrderUtils.getOrder(beanType, Integer.valueOf(2147483647)).intValue();
/*     */   }
/*     */   
/*     */   private static Set<String> initBasePackages(ControllerAdvice annotation) {
/* 232 */     Set<String> basePackages = new LinkedHashSet<String>();
/* 233 */     for (String basePackage : annotation.basePackages()) {
/* 234 */       if (StringUtils.hasText(basePackage)) {
/* 235 */         basePackages.add(adaptBasePackage(basePackage));
/*     */       }
/*     */     } 
/* 238 */     for (Class<?> markerClass : annotation.basePackageClasses()) {
/* 239 */       basePackages.add(adaptBasePackage(ClassUtils.getPackageName(markerClass)));
/*     */     }
/* 241 */     return basePackages;
/*     */   }
/*     */   
/*     */   private static String adaptBasePackage(String basePackage) {
/* 245 */     return basePackage.endsWith(".") ? basePackage : (basePackage + ".");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\ControllerAdviceBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */