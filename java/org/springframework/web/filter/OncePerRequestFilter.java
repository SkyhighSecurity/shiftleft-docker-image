/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OncePerRequestFilter
/*     */   extends GenericFilterBean
/*     */ {
/*     */   public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";
/*     */   
/*     */   public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*  89 */     if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
/*  90 */       throw new ServletException("OncePerRequestFilter just supports HTTP requests");
/*     */     }
/*  92 */     HttpServletRequest httpRequest = (HttpServletRequest)request;
/*  93 */     HttpServletResponse httpResponse = (HttpServletResponse)response;
/*     */     
/*  95 */     String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
/*  96 */     boolean hasAlreadyFilteredAttribute = (request.getAttribute(alreadyFilteredAttributeName) != null);
/*     */     
/*  98 */     if (hasAlreadyFilteredAttribute || skipDispatch(httpRequest) || shouldNotFilter(httpRequest)) {
/*     */ 
/*     */       
/* 101 */       filterChain.doFilter(request, response);
/*     */     }
/*     */     else {
/*     */       
/* 105 */       request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
/*     */       try {
/* 107 */         doFilterInternal(httpRequest, httpResponse, filterChain);
/*     */       }
/*     */       finally {
/*     */         
/* 111 */         request.removeAttribute(alreadyFilteredAttributeName);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean skipDispatch(HttpServletRequest request) {
/* 118 */     if (isAsyncDispatch(request) && shouldNotFilterAsyncDispatch()) {
/* 119 */       return true;
/*     */     }
/* 121 */     if (request.getAttribute("javax.servlet.error.request_uri") != null && shouldNotFilterErrorDispatch()) {
/* 122 */       return true;
/*     */     }
/* 124 */     return false;
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
/*     */   protected boolean isAsyncDispatch(HttpServletRequest request) {
/* 137 */     return WebAsyncUtils.getAsyncManager((ServletRequest)request).hasConcurrentResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAsyncStarted(HttpServletRequest request) {
/* 148 */     return WebAsyncUtils.getAsyncManager((ServletRequest)request).isConcurrentHandlingStarted();
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
/*     */   protected String getAlreadyFilteredAttributeName() {
/* 161 */     String name = getFilterName();
/* 162 */     if (name == null) {
/* 163 */       name = getClass().getName();
/*     */     }
/* 165 */     return name + ".FILTERED";
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
/*     */   protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
/* 177 */     return false;
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
/*     */   protected boolean shouldNotFilterAsyncDispatch() {
/* 198 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterErrorDispatch() {
/* 209 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract void doFilterInternal(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, FilterChain paramFilterChain) throws ServletException, IOException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\OncePerRequestFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */