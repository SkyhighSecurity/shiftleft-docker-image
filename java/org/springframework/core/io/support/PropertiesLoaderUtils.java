/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.DefaultPropertiesPersister;
/*     */ import org.springframework.util.PropertiesPersister;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertiesLoaderUtils
/*     */ {
/*     */   private static final String XML_FILE_EXTENSION = ".xml";
/*     */   
/*     */   public static Properties loadProperties(EncodedResource resource) throws IOException {
/*  57 */     Properties props = new Properties();
/*  58 */     fillProperties(props, resource);
/*  59 */     return props;
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
/*     */   public static void fillProperties(Properties props, EncodedResource resource) throws IOException {
/*  72 */     fillProperties(props, resource, (PropertiesPersister)new DefaultPropertiesPersister());
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
/*     */   static void fillProperties(Properties props, EncodedResource resource, PropertiesPersister persister) throws IOException {
/*  85 */     InputStream stream = null;
/*  86 */     Reader reader = null;
/*     */     try {
/*  88 */       String filename = resource.getResource().getFilename();
/*  89 */       if (filename != null && filename.endsWith(".xml")) {
/*  90 */         stream = resource.getInputStream();
/*  91 */         persister.loadFromXml(props, stream);
/*     */       }
/*  93 */       else if (resource.requiresReader()) {
/*  94 */         reader = resource.getReader();
/*  95 */         persister.load(props, reader);
/*     */       } else {
/*     */         
/*  98 */         stream = resource.getInputStream();
/*  99 */         persister.load(props, stream);
/*     */       } 
/*     */     } finally {
/*     */       
/* 103 */       if (stream != null) {
/* 104 */         stream.close();
/*     */       }
/* 106 */       if (reader != null) {
/* 107 */         reader.close();
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
/*     */   public static Properties loadProperties(Resource resource) throws IOException {
/* 120 */     Properties props = new Properties();
/* 121 */     fillProperties(props, resource);
/* 122 */     return props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fillProperties(Properties props, Resource resource) throws IOException {
/* 132 */     InputStream is = resource.getInputStream();
/*     */     try {
/* 134 */       String filename = resource.getFilename();
/* 135 */       if (filename != null && filename.endsWith(".xml")) {
/* 136 */         props.loadFromXML(is);
/*     */       } else {
/*     */         
/* 139 */         props.load(is);
/*     */       } 
/*     */     } finally {
/*     */       
/* 143 */       is.close();
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
/*     */   public static Properties loadAllProperties(String resourceName) throws IOException {
/* 157 */     return loadAllProperties(resourceName, null);
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
/*     */   public static Properties loadAllProperties(String resourceName, ClassLoader classLoader) throws IOException {
/* 172 */     Assert.notNull(resourceName, "Resource name must not be null");
/* 173 */     ClassLoader classLoaderToUse = classLoader;
/* 174 */     if (classLoaderToUse == null) {
/* 175 */       classLoaderToUse = ClassUtils.getDefaultClassLoader();
/*     */     }
/*     */     
/* 178 */     Enumeration<URL> urls = (classLoaderToUse != null) ? classLoaderToUse.getResources(resourceName) : ClassLoader.getSystemResources(resourceName);
/* 179 */     Properties props = new Properties();
/* 180 */     while (urls.hasMoreElements()) {
/* 181 */       URL url = urls.nextElement();
/* 182 */       URLConnection con = url.openConnection();
/* 183 */       ResourceUtils.useCachesIfNecessary(con);
/* 184 */       InputStream is = con.getInputStream();
/*     */       try {
/* 186 */         if (resourceName.endsWith(".xml")) {
/* 187 */           props.loadFromXML(is);
/*     */         } else {
/*     */           
/* 190 */           props.load(is);
/*     */         } 
/*     */       } finally {
/*     */         
/* 194 */         is.close();
/*     */       } 
/*     */     } 
/* 197 */     return props;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\PropertiesLoaderUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */