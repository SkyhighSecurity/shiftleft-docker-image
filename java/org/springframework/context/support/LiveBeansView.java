/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class LiveBeansView
/*     */   implements LiveBeansViewMBean, ApplicationContextAware
/*     */ {
/*     */   public static final String MBEAN_DOMAIN_PROPERTY_NAME = "spring.liveBeansView.mbeanDomain";
/*     */   public static final String MBEAN_APPLICATION_KEY = "application";
/*  58 */   private static final Set<ConfigurableApplicationContext> applicationContexts = new LinkedHashSet<ConfigurableApplicationContext>();
/*     */   
/*     */   private static String applicationName;
/*     */   
/*     */   private ConfigurableApplicationContext applicationContext;
/*     */   
/*     */   static void registerApplicationContext(ConfigurableApplicationContext applicationContext) {
/*  65 */     String mbeanDomain = applicationContext.getEnvironment().getProperty("spring.liveBeansView.mbeanDomain");
/*  66 */     if (mbeanDomain != null) {
/*  67 */       synchronized (applicationContexts) {
/*  68 */         if (applicationContexts.isEmpty()) {
/*     */           try {
/*  70 */             MBeanServer server = ManagementFactory.getPlatformMBeanServer();
/*  71 */             applicationName = applicationContext.getApplicationName();
/*  72 */             server.registerMBean(new LiveBeansView(), new ObjectName(mbeanDomain, "application", applicationName));
/*     */           
/*     */           }
/*  75 */           catch (Throwable ex) {
/*  76 */             throw new ApplicationContextException("Failed to register LiveBeansView MBean", ex);
/*     */           } 
/*     */         }
/*  79 */         applicationContexts.add(applicationContext);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static void unregisterApplicationContext(ConfigurableApplicationContext applicationContext) {
/*  85 */     synchronized (applicationContexts) {
/*  86 */       if (applicationContexts.remove(applicationContext) && applicationContexts.isEmpty()) {
/*     */         try {
/*  88 */           MBeanServer server = ManagementFactory.getPlatformMBeanServer();
/*  89 */           String mbeanDomain = applicationContext.getEnvironment().getProperty("spring.liveBeansView.mbeanDomain");
/*  90 */           server.unregisterMBean(new ObjectName(mbeanDomain, "application", applicationName));
/*     */         }
/*  92 */         catch (Throwable ex) {
/*  93 */           throw new ApplicationContextException("Failed to unregister LiveBeansView MBean", ex);
/*     */         } finally {
/*     */           
/*  96 */           applicationName = null;
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 108 */     Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext, "ApplicationContext does not implement ConfigurableApplicationContext");
/*     */     
/* 110 */     this.applicationContext = (ConfigurableApplicationContext)applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSnapshotAsJson() {
/*     */     Set<ConfigurableApplicationContext> contexts;
/* 122 */     if (this.applicationContext != null) {
/* 123 */       contexts = Collections.singleton(this.applicationContext);
/*     */     } else {
/*     */       
/* 126 */       contexts = findApplicationContexts();
/*     */     } 
/* 128 */     return generateJson(contexts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<ConfigurableApplicationContext> findApplicationContexts() {
/* 137 */     synchronized (applicationContexts) {
/* 138 */       return new LinkedHashSet<ConfigurableApplicationContext>(applicationContexts);
/*     */     } 
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
/*     */   protected String generateJson(Set<ConfigurableApplicationContext> contexts) {
/* 154 */     StringBuilder result = new StringBuilder("[\n");
/* 155 */     for (Iterator<ConfigurableApplicationContext> it = contexts.iterator(); it.hasNext(); ) {
/* 156 */       ConfigurableApplicationContext context = it.next();
/* 157 */       result.append("{\n\"context\": \"").append(context.getId()).append("\",\n");
/* 158 */       if (context.getParent() != null) {
/* 159 */         result.append("\"parent\": \"").append(context.getParent().getId()).append("\",\n");
/*     */       } else {
/*     */         
/* 162 */         result.append("\"parent\": null,\n");
/*     */       } 
/* 164 */       result.append("\"beans\": [\n");
/* 165 */       ConfigurableListableBeanFactory bf = context.getBeanFactory();
/* 166 */       String[] beanNames = bf.getBeanDefinitionNames();
/* 167 */       boolean elementAppended = false;
/* 168 */       for (String beanName : beanNames) {
/* 169 */         BeanDefinition bd = bf.getBeanDefinition(beanName);
/* 170 */         if (isBeanEligible(beanName, bd, (ConfigurableBeanFactory)bf)) {
/* 171 */           if (elementAppended) {
/* 172 */             result.append(",\n");
/*     */           }
/* 174 */           result.append("{\n\"bean\": \"").append(beanName).append("\",\n");
/* 175 */           result.append("\"aliases\": ");
/* 176 */           appendArray(result, bf.getAliases(beanName));
/* 177 */           result.append(",\n");
/* 178 */           String scope = bd.getScope();
/* 179 */           if (!StringUtils.hasText(scope)) {
/* 180 */             scope = "singleton";
/*     */           }
/* 182 */           result.append("\"scope\": \"").append(scope).append("\",\n");
/* 183 */           Class<?> beanType = bf.getType(beanName);
/* 184 */           if (beanType != null) {
/* 185 */             result.append("\"type\": \"").append(beanType.getName()).append("\",\n");
/*     */           } else {
/*     */             
/* 188 */             result.append("\"type\": null,\n");
/*     */           } 
/* 190 */           result.append("\"resource\": \"").append(getEscapedResourceDescription(bd)).append("\",\n");
/* 191 */           result.append("\"dependencies\": ");
/* 192 */           appendArray(result, bf.getDependenciesForBean(beanName));
/* 193 */           result.append("\n}");
/* 194 */           elementAppended = true;
/*     */         } 
/*     */       } 
/* 197 */       result.append("]\n");
/* 198 */       result.append("}");
/* 199 */       if (it.hasNext()) {
/* 200 */         result.append(",\n");
/*     */       }
/*     */     } 
/* 203 */     result.append("]");
/* 204 */     return result.toString();
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
/*     */   protected boolean isBeanEligible(String beanName, BeanDefinition bd, ConfigurableBeanFactory bf) {
/* 216 */     return (bd.getRole() != 2 && (
/* 217 */       !bd.isLazyInit() || bf.containsSingleton(beanName)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getEscapedResourceDescription(BeanDefinition bd) {
/* 227 */     String resourceDescription = bd.getResourceDescription();
/* 228 */     if (resourceDescription == null) {
/* 229 */       return null;
/*     */     }
/* 231 */     StringBuilder result = new StringBuilder(resourceDescription.length() + 16);
/* 232 */     for (int i = 0; i < resourceDescription.length(); i++) {
/* 233 */       char character = resourceDescription.charAt(i);
/* 234 */       if (character == '\\') {
/* 235 */         result.append('/');
/*     */       }
/* 237 */       else if (character == '"') {
/* 238 */         result.append("\\").append('"');
/*     */       } else {
/*     */         
/* 241 */         result.append(character);
/*     */       } 
/*     */     } 
/* 244 */     return result.toString();
/*     */   }
/*     */   
/*     */   private void appendArray(StringBuilder result, String[] arr) {
/* 248 */     result.append('[');
/* 249 */     if (arr.length > 0) {
/* 250 */       result.append('"');
/*     */     }
/* 252 */     result.append(StringUtils.arrayToDelimitedString((Object[])arr, "\", \""));
/* 253 */     if (arr.length > 0) {
/* 254 */       result.append('"');
/*     */     }
/* 256 */     result.append(']');
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\LiveBeansView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */