/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodNameBasedMBeanInfoAssembler
/*     */   extends AbstractConfigurableMBeanInfoAssembler
/*     */ {
/*     */   private Set<String> managedMethods;
/*     */   private Map<String, Set<String>> methodMappings;
/*     */   
/*     */   public void setManagedMethods(String... methodNames) {
/*  76 */     this.managedMethods = new HashSet<String>(Arrays.asList(methodNames));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodMappings(Properties mappings) {
/*  87 */     this.methodMappings = new HashMap<String, Set<String>>();
/*  88 */     for (Enumeration<?> en = mappings.keys(); en.hasMoreElements(); ) {
/*  89 */       String beanKey = (String)en.nextElement();
/*  90 */       String[] methodNames = StringUtils.commaDelimitedListToStringArray(mappings.getProperty(beanKey));
/*  91 */       this.methodMappings.put(beanKey, new HashSet<String>(Arrays.asList(methodNames)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean includeReadAttribute(Method method, String beanKey) {
/*  98 */     return isMatch(method, beanKey);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean includeWriteAttribute(Method method, String beanKey) {
/* 103 */     return isMatch(method, beanKey);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean includeOperation(Method method, String beanKey) {
/* 108 */     return isMatch(method, beanKey);
/*     */   }
/*     */   
/*     */   protected boolean isMatch(Method method, String beanKey) {
/* 112 */     if (this.methodMappings != null) {
/* 113 */       Set<String> methodNames = this.methodMappings.get(beanKey);
/* 114 */       if (methodNames != null) {
/* 115 */         return methodNames.contains(method.getName());
/*     */       }
/*     */     } 
/* 118 */     return (this.managedMethods != null && this.managedMethods.contains(method.getName()));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\assembler\MethodNameBasedMBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */