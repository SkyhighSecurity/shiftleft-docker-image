/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.JMX;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerFactory;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ 
/*     */ 
/*     */ public abstract class JmxUtils
/*     */ {
/*     */   public static final String IDENTITY_OBJECT_NAME_KEY = "identity";
/*     */   private static final String MBEAN_SUFFIX = "MBean";
/*  64 */   private static final Log logger = LogFactory.getLog(JmxUtils.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MBeanServer locateMBeanServer() throws MBeanServerNotFoundException {
/*  76 */     return locateMBeanServer(null);
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
/*     */   public static MBeanServer locateMBeanServer(String agentId) throws MBeanServerNotFoundException {
/*  91 */     MBeanServer server = null;
/*     */ 
/*     */     
/*  94 */     if (!"".equals(agentId)) {
/*  95 */       List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(agentId);
/*  96 */       if (!CollectionUtils.isEmpty(servers)) {
/*     */         
/*  98 */         if (servers.size() > 1 && logger.isWarnEnabled()) {
/*  99 */           logger.warn("Found more than one MBeanServer instance" + ((agentId != null) ? (" with agent id [" + agentId + "]") : "") + ". Returning first from list.");
/*     */         }
/*     */ 
/*     */         
/* 103 */         server = servers.get(0);
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     if (server == null && !StringUtils.hasLength(agentId)) {
/*     */       
/*     */       try {
/* 110 */         server = ManagementFactory.getPlatformMBeanServer();
/*     */       }
/* 112 */       catch (SecurityException ex) {
/* 113 */         throw new MBeanServerNotFoundException("No specific MBeanServer found, and not allowed to obtain the Java platform MBeanServer", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 118 */     if (server == null) {
/* 119 */       throw new MBeanServerNotFoundException("Unable to locate an MBeanServer instance" + ((agentId != null) ? (" with agent id [" + agentId + "]") : ""));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (logger.isDebugEnabled()) {
/* 125 */       logger.debug("Found MBeanServer: " + server);
/*     */     }
/* 127 */     return server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?>[] parameterInfoToTypes(MBeanParameterInfo[] paramInfo) throws ClassNotFoundException {
/* 138 */     return parameterInfoToTypes(paramInfo, ClassUtils.getDefaultClassLoader());
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
/*     */   public static Class<?>[] parameterInfoToTypes(MBeanParameterInfo[] paramInfo, ClassLoader classLoader) throws ClassNotFoundException {
/* 152 */     Class<?>[] types = null;
/* 153 */     if (paramInfo != null && paramInfo.length > 0) {
/* 154 */       types = new Class[paramInfo.length];
/* 155 */       for (int x = 0; x < paramInfo.length; x++) {
/* 156 */         types[x] = ClassUtils.forName(paramInfo[x].getType(), classLoader);
/*     */       }
/*     */     } 
/* 159 */     return types;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getMethodSignature(Method method) {
/* 170 */     Class<?>[] types = method.getParameterTypes();
/* 171 */     String[] signature = new String[types.length];
/* 172 */     for (int x = 0; x < types.length; x++) {
/* 173 */       signature[x] = types[x].getName();
/*     */     }
/* 175 */     return signature;
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
/*     */   public static String getAttributeName(PropertyDescriptor property, boolean useStrictCasing) {
/* 189 */     if (useStrictCasing) {
/* 190 */       return StringUtils.capitalize(property.getName());
/*     */     }
/*     */     
/* 193 */     return property.getName();
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
/*     */ 
/*     */   
/*     */   public static ObjectName appendIdentityToObjectName(ObjectName objectName, Object managedResource) throws MalformedObjectNameException {
/* 214 */     Hashtable<String, String> keyProperties = objectName.getKeyPropertyList();
/* 215 */     keyProperties.put("identity", ObjectUtils.getIdentityHexString(managedResource));
/* 216 */     return ObjectNameManager.getInstance(objectName.getDomain(), keyProperties);
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
/*     */   public static Class<?> getClassToExpose(Object managedBean) {
/* 230 */     return ClassUtils.getUserClass(managedBean);
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
/*     */   public static Class<?> getClassToExpose(Class<?> clazz) {
/* 244 */     return ClassUtils.getUserClass(clazz);
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
/*     */   public static boolean isMBean(Class<?> clazz) {
/* 257 */     return (clazz != null && (DynamicMBean.class
/* 258 */       .isAssignableFrom(clazz) || 
/* 259 */       getMBeanInterface(clazz) != null || getMXBeanInterface(clazz) != null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMBeanInterface(Class<?> clazz) {
/* 270 */     if (clazz == null || clazz.getSuperclass() == null) {
/* 271 */       return null;
/*     */     }
/* 273 */     String mbeanInterfaceName = clazz.getName() + "MBean";
/* 274 */     Class<?>[] implementedInterfaces = clazz.getInterfaces();
/* 275 */     for (Class<?> iface : implementedInterfaces) {
/* 276 */       if (iface.getName().equals(mbeanInterfaceName)) {
/* 277 */         return iface;
/*     */       }
/*     */     } 
/* 280 */     return getMBeanInterface(clazz.getSuperclass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMXBeanInterface(Class<?> clazz) {
/* 291 */     if (clazz == null || clazz.getSuperclass() == null) {
/* 292 */       return null;
/*     */     }
/* 294 */     Class<?>[] implementedInterfaces = clazz.getInterfaces();
/* 295 */     for (Class<?> iface : implementedInterfaces) {
/* 296 */       if (JMX.isMXBeanInterface(iface)) {
/* 297 */         return iface;
/*     */       }
/*     */     } 
/* 300 */     return getMXBeanInterface(clazz.getSuperclass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static boolean isMXBeanSupportAvailable() {
/* 311 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\support\JmxUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */