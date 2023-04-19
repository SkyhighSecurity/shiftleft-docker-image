/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class InterfaceBasedMBeanInfoAssembler
/*     */   extends AbstractConfigurableMBeanInfoAssembler
/*     */   implements BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   private Class<?>[] managedInterfaces;
/*     */   private Properties interfaceMappings;
/*  68 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Class<?>[]> resolvedInterfaceMappings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagedInterfaces(Class<?>... managedInterfaces) {
/*  83 */     if (managedInterfaces != null) {
/*  84 */       for (Class<?> ifc : managedInterfaces) {
/*  85 */         if (!ifc.isInterface()) {
/*  86 */           throw new IllegalArgumentException("Management interface [" + ifc
/*  87 */               .getName() + "] is not an interface");
/*     */         }
/*     */       } 
/*     */     }
/*  91 */     this.managedInterfaces = managedInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterfaceMappings(Properties mappings) {
/* 102 */     this.interfaceMappings = mappings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 107 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 113 */     if (this.interfaceMappings != null) {
/* 114 */       this.resolvedInterfaceMappings = resolveInterfaceMappings(this.interfaceMappings);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Class<?>[]> resolveInterfaceMappings(Properties mappings) {
/* 124 */     Map<String, Class<?>[]> resolvedMappings = (Map)new HashMap<String, Class<?>>(mappings.size());
/* 125 */     for (Enumeration<?> en = mappings.propertyNames(); en.hasMoreElements(); ) {
/* 126 */       String beanKey = (String)en.nextElement();
/* 127 */       String[] classNames = StringUtils.commaDelimitedListToStringArray(mappings.getProperty(beanKey));
/* 128 */       Class<?>[] classes = resolveClassNames(classNames, beanKey);
/* 129 */       resolvedMappings.put(beanKey, classes);
/*     */     } 
/* 131 */     return resolvedMappings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<?>[] resolveClassNames(String[] classNames, String beanKey) {
/* 141 */     Class<?>[] classes = new Class[classNames.length];
/* 142 */     for (int x = 0; x < classes.length; x++) {
/* 143 */       Class<?> cls = ClassUtils.resolveClassName(classNames[x].trim(), this.beanClassLoader);
/* 144 */       if (!cls.isInterface()) {
/* 145 */         throw new IllegalArgumentException("Class [" + classNames[x] + "] mapped to bean key [" + beanKey + "] is no interface");
/*     */       }
/*     */       
/* 148 */       classes[x] = cls;
/*     */     } 
/* 150 */     return classes;
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
/*     */   protected boolean includeReadAttribute(Method method, String beanKey) {
/* 165 */     return isPublicInInterface(method, beanKey);
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
/*     */   protected boolean includeWriteAttribute(Method method, String beanKey) {
/* 179 */     return isPublicInInterface(method, beanKey);
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
/*     */   protected boolean includeOperation(Method method, String beanKey) {
/* 193 */     return isPublicInInterface(method, beanKey);
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
/*     */   private boolean isPublicInInterface(Method method, String beanKey) {
/* 205 */     return ((method.getModifiers() & 0x1) > 0 && isDeclaredInInterface(method, beanKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDeclaredInInterface(Method method, String beanKey) {
/* 213 */     Class<?>[] ifaces = null;
/*     */     
/* 215 */     if (this.resolvedInterfaceMappings != null) {
/* 216 */       ifaces = this.resolvedInterfaceMappings.get(beanKey);
/*     */     }
/*     */     
/* 219 */     if (ifaces == null) {
/* 220 */       ifaces = this.managedInterfaces;
/* 221 */       if (ifaces == null) {
/* 222 */         ifaces = ClassUtils.getAllInterfacesForClass(method.getDeclaringClass());
/*     */       }
/*     */     } 
/*     */     
/* 226 */     for (Class<?> ifc : ifaces) {
/* 227 */       for (Method ifcMethod : ifc.getMethods()) {
/* 228 */         if (ifcMethod.getName().equals(method.getName()) && 
/* 229 */           Arrays.equals((Object[])ifcMethod.getParameterTypes(), (Object[])method.getParameterTypes())) {
/* 230 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 235 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\assembler\InterfaceBasedMBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */