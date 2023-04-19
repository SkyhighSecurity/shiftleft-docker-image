/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SpringProperties
/*     */ {
/*     */   private static final String PROPERTIES_RESOURCE_LOCATION = "spring.properties";
/*  51 */   private static final Log logger = LogFactory.getLog(SpringProperties.class);
/*     */   
/*  53 */   private static final Properties localProperties = new Properties();
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  58 */       ClassLoader cl = SpringProperties.class.getClassLoader();
/*     */       
/*  60 */       URL url = (cl != null) ? cl.getResource("spring.properties") : ClassLoader.getSystemResource("spring.properties");
/*  61 */       if (url != null) {
/*  62 */         logger.info("Found 'spring.properties' file in local classpath");
/*  63 */         InputStream is = url.openStream();
/*     */         try {
/*  65 */           localProperties.load(is);
/*     */         } finally {
/*     */           
/*  68 */           is.close();
/*     */         }
/*     */       
/*     */       } 
/*  72 */     } catch (IOException ex) {
/*  73 */       if (logger.isInfoEnabled()) {
/*  74 */         logger.info("Could not load 'spring.properties' file from local classpath: " + ex);
/*     */       }
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
/*     */   public static void setProperty(String key, String value) {
/*  87 */     if (value != null) {
/*  88 */       localProperties.setProperty(key, value);
/*     */     } else {
/*     */       
/*  91 */       localProperties.remove(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getProperty(String key) {
/* 102 */     String value = localProperties.getProperty(key);
/* 103 */     if (value == null) {
/*     */       try {
/* 105 */         value = System.getProperty(key);
/*     */       }
/* 107 */       catch (Throwable ex) {
/* 108 */         if (logger.isDebugEnabled()) {
/* 109 */           logger.debug("Could not retrieve system property '" + key + "': " + ex);
/*     */         }
/*     */       } 
/*     */     }
/* 113 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setFlag(String key) {
/* 122 */     localProperties.put(key, Boolean.TRUE.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getFlag(String key) {
/* 132 */     return Boolean.parseBoolean(getProperty(key));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\SpringProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */