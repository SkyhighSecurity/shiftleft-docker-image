/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*     */ import org.springframework.web.filter.OncePerRequestFilter;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*     */ import org.springframework.web.multipart.MultipartResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultipartFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   public static final String DEFAULT_MULTIPART_RESOLVER_BEAN_NAME = "filterMultipartResolver";
/*  70 */   private final MultipartResolver defaultMultipartResolver = new StandardServletMultipartResolver();
/*     */   
/*  72 */   private String multipartResolverBeanName = "filterMultipartResolver";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMultipartResolverBeanName(String multipartResolverBeanName) {
/*  80 */     this.multipartResolverBeanName = multipartResolverBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getMultipartResolverBeanName() {
/*  88 */     return this.multipartResolverBeanName;
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
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*     */     MultipartHttpServletRequest multipartHttpServletRequest;
/* 104 */     MultipartResolver multipartResolver = lookupMultipartResolver(request);
/*     */     
/* 106 */     HttpServletRequest processedRequest = request;
/* 107 */     if (multipartResolver.isMultipart(processedRequest)) {
/* 108 */       if (this.logger.isDebugEnabled()) {
/* 109 */         this.logger.debug("Resolving multipart request [" + processedRequest.getRequestURI() + "] with MultipartFilter");
/*     */       }
/*     */       
/* 112 */       multipartHttpServletRequest = multipartResolver.resolveMultipart(processedRequest);
/*     */ 
/*     */     
/*     */     }
/* 116 */     else if (this.logger.isDebugEnabled()) {
/* 117 */       this.logger.debug("Request [" + multipartHttpServletRequest.getRequestURI() + "] is not a multipart request");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 122 */       filterChain.doFilter((ServletRequest)multipartHttpServletRequest, (ServletResponse)response);
/*     */     } finally {
/*     */       
/* 125 */       if (multipartHttpServletRequest instanceof MultipartHttpServletRequest) {
/* 126 */         multipartResolver.cleanupMultipart(multipartHttpServletRequest);
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
/*     */   
/*     */   protected MultipartResolver lookupMultipartResolver(HttpServletRequest request) {
/* 140 */     return lookupMultipartResolver();
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
/*     */   protected MultipartResolver lookupMultipartResolver() {
/* 152 */     WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
/* 153 */     String beanName = getMultipartResolverBeanName();
/* 154 */     if (wac != null && wac.containsBean(beanName)) {
/* 155 */       if (this.logger.isDebugEnabled()) {
/* 156 */         this.logger.debug("Using MultipartResolver '" + beanName + "' for MultipartFilter");
/*     */       }
/* 158 */       return (MultipartResolver)wac.getBean(beanName, MultipartResolver.class);
/*     */     } 
/*     */     
/* 161 */     return this.defaultMultipartResolver;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\MultipartFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */