/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.net.URL;
/*     */ import org.apache.log4j.LogManager;
/*     */ import org.apache.log4j.PropertyConfigurator;
/*     */ import org.apache.log4j.xml.DOMConfigurator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class Log4jConfigurer
/*     */ {
/*     */   public static final String CLASSPATH_URL_PREFIX = "classpath:";
/*     */   public static final String XML_FILE_EXTENSION = ".xml";
/*     */   
/*     */   public static void initLogging(String location) throws FileNotFoundException {
/*  68 */     String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
/*  69 */     URL url = ResourceUtils.getURL(resolvedLocation);
/*  70 */     if ("file".equals(url.getProtocol()) && !ResourceUtils.getFile(url).exists()) {
/*  71 */       throw new FileNotFoundException("Log4j config file [" + resolvedLocation + "] not found");
/*     */     }
/*     */     
/*  74 */     if (resolvedLocation.toLowerCase().endsWith(".xml")) {
/*  75 */       DOMConfigurator.configure(url);
/*     */     } else {
/*     */       
/*  78 */       PropertyConfigurator.configure(url);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initLogging(String location, long refreshInterval) throws FileNotFoundException {
/* 102 */     String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
/* 103 */     File file = ResourceUtils.getFile(resolvedLocation);
/* 104 */     if (!file.exists()) {
/* 105 */       throw new FileNotFoundException("Log4j config file [" + resolvedLocation + "] not found");
/*     */     }
/*     */     
/* 108 */     if (resolvedLocation.toLowerCase().endsWith(".xml")) {
/* 109 */       DOMConfigurator.configureAndWatch(file.getAbsolutePath(), refreshInterval);
/*     */     } else {
/*     */       
/* 112 */       PropertyConfigurator.configureAndWatch(file.getAbsolutePath(), refreshInterval);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void shutdownLogging() {
/* 123 */     LogManager.shutdown();
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
/*     */   public static void setWorkingDirSystemProperty(String key) {
/* 135 */     System.setProperty(key, (new File("")).getAbsolutePath());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\Log4jConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */