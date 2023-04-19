/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.util.PropertyPlaceholderHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ServletContextPropertyUtils
/*     */ {
/*  38 */   private static final PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
/*     */ 
/*     */ 
/*     */   
/*  42 */   private static final PropertyPlaceholderHelper nonStrictHelper = new PropertyPlaceholderHelper("${", "}", ":", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String resolvePlaceholders(String text, ServletContext servletContext) {
/*  59 */     return resolvePlaceholders(text, servletContext, false);
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
/*     */   public static String resolvePlaceholders(String text, ServletContext servletContext, boolean ignoreUnresolvablePlaceholders) {
/*  76 */     PropertyPlaceholderHelper helper = ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper;
/*  77 */     return helper.replacePlaceholders(text, new ServletContextPlaceholderResolver(text, servletContext));
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ServletContextPlaceholderResolver
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/*     */     private final String text;
/*     */     private final ServletContext servletContext;
/*     */     
/*     */     public ServletContextPlaceholderResolver(String text, ServletContext servletContext) {
/*  88 */       this.text = text;
/*  89 */       this.servletContext = servletContext;
/*     */     }
/*     */ 
/*     */     
/*     */     public String resolvePlaceholder(String placeholderName) {
/*     */       try {
/*  95 */         String propVal = this.servletContext.getInitParameter(placeholderName);
/*  96 */         if (propVal == null) {
/*     */           
/*  98 */           propVal = System.getProperty(placeholderName);
/*  99 */           if (propVal == null)
/*     */           {
/* 101 */             propVal = System.getenv(placeholderName);
/*     */           }
/*     */         } 
/* 104 */         return propVal;
/*     */       }
/* 106 */       catch (Throwable ex) {
/* 107 */         System.err.println("Could not resolve placeholder '" + placeholderName + "' in [" + this.text + "] as ServletContext init-parameter or system property: " + ex);
/*     */         
/* 109 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\ServletContextPropertyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */