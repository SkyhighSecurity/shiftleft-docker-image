/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.util.Log4jConfigurer;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Log4jWebConfigurer
/*     */ {
/*     */   public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";
/*     */   public static final String REFRESH_INTERVAL_PARAM = "log4jRefreshInterval";
/*     */   public static final String EXPOSE_WEB_APP_ROOT_PARAM = "log4jExposeWebAppRoot";
/*     */   
/*     */   public static void initLogging(ServletContext servletContext) {
/* 118 */     if (exposeWebAppRoot(servletContext)) {
/* 119 */       WebUtils.setWebAppRootSystemProperty(servletContext);
/*     */     }
/*     */ 
/*     */     
/* 123 */     String location = servletContext.getInitParameter("log4jConfigLocation");
/* 124 */     if (location != null) {
/*     */       
/*     */       try {
/*     */         
/* 128 */         location = ServletContextPropertyUtils.resolvePlaceholders(location, servletContext);
/*     */ 
/*     */         
/* 131 */         if (!ResourceUtils.isUrl(location))
/*     */         {
/* 133 */           location = WebUtils.getRealPath(servletContext, location);
/*     */         }
/*     */ 
/*     */         
/* 137 */         servletContext.log("Initializing log4j from [" + location + "]");
/*     */ 
/*     */         
/* 140 */         String intervalString = servletContext.getInitParameter("log4jRefreshInterval");
/* 141 */         if (StringUtils.hasText(intervalString)) {
/*     */ 
/*     */           
/*     */           try {
/* 145 */             long refreshInterval = Long.parseLong(intervalString);
/* 146 */             Log4jConfigurer.initLogging(location, refreshInterval);
/*     */           }
/* 148 */           catch (NumberFormatException ex) {
/* 149 */             throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 154 */           Log4jConfigurer.initLogging(location);
/*     */         }
/*     */       
/* 157 */       } catch (FileNotFoundException ex) {
/* 158 */         throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex.getMessage());
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
/*     */   public static void shutdownLogging(ServletContext servletContext) {
/* 170 */     servletContext.log("Shutting down log4j");
/*     */     try {
/* 172 */       Log4jConfigurer.shutdownLogging();
/*     */     }
/*     */     finally {
/*     */       
/* 176 */       if (exposeWebAppRoot(servletContext)) {
/* 177 */         WebUtils.removeWebAppRootSystemProperty(servletContext);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean exposeWebAppRoot(ServletContext servletContext) {
/* 188 */     String exposeWebAppRootParam = servletContext.getInitParameter("log4jExposeWebAppRoot");
/* 189 */     return (exposeWebAppRootParam == null || Boolean.valueOf(exposeWebAppRootParam).booleanValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\Log4jWebConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */