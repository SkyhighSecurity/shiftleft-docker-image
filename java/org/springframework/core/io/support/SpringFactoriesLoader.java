/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ public abstract class SpringFactoriesLoader
/*     */ {
/*  60 */   private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> List<T> loadFactories(Class<T> factoryClass, ClassLoader classLoader) {
/*  82 */     Assert.notNull(factoryClass, "'factoryClass' must not be null");
/*  83 */     ClassLoader classLoaderToUse = classLoader;
/*  84 */     if (classLoaderToUse == null) {
/*  85 */       classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
/*     */     }
/*  87 */     List<String> factoryNames = loadFactoryNames(factoryClass, classLoaderToUse);
/*  88 */     if (logger.isTraceEnabled()) {
/*  89 */       logger.trace("Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
/*     */     }
/*  91 */     List<T> result = new ArrayList<T>(factoryNames.size());
/*  92 */     for (String factoryName : factoryNames) {
/*  93 */       result.add(instantiateFactory(factoryName, factoryClass, classLoaderToUse));
/*     */     }
/*  95 */     AnnotationAwareOrderComparator.sort(result);
/*  96 */     return result;
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
/*     */   public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {
/* 110 */     String factoryClassName = factoryClass.getName();
/*     */     
/*     */     try {
/* 113 */       Enumeration<URL> urls = (classLoader != null) ? classLoader.getResources("META-INF/spring.factories") : ClassLoader.getSystemResources("META-INF/spring.factories");
/* 114 */       List<String> result = new ArrayList<String>();
/* 115 */       while (urls.hasMoreElements()) {
/* 116 */         URL url = urls.nextElement();
/* 117 */         Properties properties = PropertiesLoaderUtils.loadProperties((Resource)new UrlResource(url));
/* 118 */         String factoryClassNames = properties.getProperty(factoryClassName);
/* 119 */         result.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(factoryClassNames)));
/*     */       } 
/* 121 */       return result;
/*     */     }
/* 123 */     catch (IOException ex) {
/* 124 */       throw new IllegalArgumentException("Unable to load [" + factoryClass.getName() + "] factories from location [" + "META-INF/spring.factories" + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T instantiateFactory(String instanceClassName, Class<T> factoryClass, ClassLoader classLoader) {
/*     */     try {
/* 132 */       Class<?> instanceClass = ClassUtils.forName(instanceClassName, classLoader);
/* 133 */       if (!factoryClass.isAssignableFrom(instanceClass)) {
/* 134 */         throw new IllegalArgumentException("Class [" + instanceClassName + "] is not assignable to [" + factoryClass
/* 135 */             .getName() + "]");
/*     */       }
/* 137 */       Constructor<?> constructor = instanceClass.getDeclaredConstructor(new Class[0]);
/* 138 */       ReflectionUtils.makeAccessible(constructor);
/* 139 */       return (T)constructor.newInstance(new Object[0]);
/*     */     }
/* 141 */     catch (Throwable ex) {
/* 142 */       throw new IllegalArgumentException("Unable to instantiate factory class: " + factoryClass.getName(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\SpringFactoriesLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */