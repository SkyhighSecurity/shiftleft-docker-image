/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.DigestUtils;
/*     */ import org.springframework.web.util.ContentCachingResponseWrapper;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShallowEtagHeaderFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   private static final String HEADER_ETAG = "ETag";
/*     */   private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
/*     */   private static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*     */   private static final String DIRECTIVE_NO_STORE = "no-store";
/*  62 */   private static final String STREAMING_ATTRIBUTE = ShallowEtagHeaderFilter.class.getName() + ".STREAMING";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final boolean servlet3Present = ClassUtils.hasMethod(HttpServletResponse.class, "getHeader", new Class[] { String.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean writeWeakETag = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteWeakETag(boolean writeWeakETag) {
/*  80 */     this.writeWeakETag = writeWeakETag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWriteWeakETag() {
/*  88 */     return this.writeWeakETag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterAsyncDispatch() {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*     */     HttpStreamingAwareContentCachingResponseWrapper httpStreamingAwareContentCachingResponseWrapper;
/* 105 */     HttpServletResponse responseToUse = response;
/* 106 */     if (!isAsyncDispatch(request) && !(response instanceof ContentCachingResponseWrapper)) {
/* 107 */       httpStreamingAwareContentCachingResponseWrapper = new HttpStreamingAwareContentCachingResponseWrapper(response, request);
/*     */     }
/*     */     
/* 110 */     filterChain.doFilter((ServletRequest)request, (ServletResponse)httpStreamingAwareContentCachingResponseWrapper);
/*     */     
/* 112 */     if (!isAsyncStarted(request) && !isContentCachingDisabled(request)) {
/* 113 */       updateResponse(request, (HttpServletResponse)httpStreamingAwareContentCachingResponseWrapper);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
/* 119 */     ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper)WebUtils.getNativeResponse((ServletResponse)response, ContentCachingResponseWrapper.class);
/* 120 */     Assert.notNull(responseWrapper, "ContentCachingResponseWrapper not found");
/* 121 */     HttpServletResponse rawResponse = (HttpServletResponse)responseWrapper.getResponse();
/* 122 */     int statusCode = responseWrapper.getStatusCode();
/*     */     
/* 124 */     if (rawResponse.isCommitted()) {
/* 125 */       responseWrapper.copyBodyToResponse();
/*     */     }
/* 127 */     else if (isEligibleForEtag(request, (HttpServletResponse)responseWrapper, statusCode, responseWrapper.getContentInputStream())) {
/* 128 */       String responseETag = generateETagHeaderValue(responseWrapper.getContentInputStream(), this.writeWeakETag);
/* 129 */       rawResponse.setHeader("ETag", responseETag);
/* 130 */       String requestETag = request.getHeader("If-None-Match");
/* 131 */       if (requestETag != null && ("*".equals(requestETag) || responseETag.equals(requestETag) || responseETag
/* 132 */         .replaceFirst("^W/", "").equals(requestETag.replaceFirst("^W/", "")))) {
/* 133 */         if (this.logger.isTraceEnabled()) {
/* 134 */           this.logger.trace("ETag [" + responseETag + "] equal to If-None-Match, sending 304");
/*     */         }
/* 136 */         rawResponse.setStatus(304);
/*     */       } else {
/*     */         
/* 139 */         if (this.logger.isTraceEnabled()) {
/* 140 */           this.logger.trace("ETag [" + responseETag + "] not equal to If-None-Match [" + requestETag + "], sending normal response");
/*     */         }
/*     */         
/* 143 */         responseWrapper.copyBodyToResponse();
/*     */       } 
/*     */     } else {
/*     */       
/* 147 */       if (this.logger.isTraceEnabled()) {
/* 148 */         this.logger.trace("Response with status code [" + statusCode + "] not eligible for ETag");
/*     */       }
/* 150 */       responseWrapper.copyBodyToResponse();
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
/*     */   protected boolean isEligibleForEtag(HttpServletRequest request, HttpServletResponse response, int responseStatusCode, InputStream inputStream) {
/* 171 */     String method = request.getMethod();
/* 172 */     if (responseStatusCode >= 200 && responseStatusCode < 300 && HttpMethod.GET.matches(method)) {
/* 173 */       String cacheControl = null;
/* 174 */       if (servlet3Present) {
/* 175 */         cacheControl = response.getHeader("Cache-Control");
/*     */       }
/* 177 */       if (cacheControl == null || !cacheControl.contains("no-store")) {
/* 178 */         return true;
/*     */       }
/*     */     } 
/* 181 */     return false;
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
/*     */   protected String generateETagHeaderValue(InputStream inputStream, boolean isWeak) throws IOException {
/* 194 */     StringBuilder builder = new StringBuilder(37);
/* 195 */     if (isWeak) {
/* 196 */       builder.append("W/");
/*     */     }
/* 198 */     builder.append("\"0");
/* 199 */     DigestUtils.appendMd5DigestAsHex(inputStream, builder);
/* 200 */     builder.append('"');
/* 201 */     return builder.toString();
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
/*     */   public static void disableContentCaching(ServletRequest request) {
/* 213 */     Assert.notNull(request, "ServletRequest must not be null");
/* 214 */     request.setAttribute(STREAMING_ATTRIBUTE, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   private static boolean isContentCachingDisabled(HttpServletRequest request) {
/* 218 */     return (request.getAttribute(STREAMING_ATTRIBUTE) != null);
/*     */   }
/*     */   
/*     */   private static class HttpStreamingAwareContentCachingResponseWrapper
/*     */     extends ContentCachingResponseWrapper
/*     */   {
/*     */     private final HttpServletRequest request;
/*     */     
/*     */     public HttpStreamingAwareContentCachingResponseWrapper(HttpServletResponse response, HttpServletRequest request) {
/* 227 */       super(response);
/* 228 */       this.request = request;
/*     */     }
/*     */ 
/*     */     
/*     */     public ServletOutputStream getOutputStream() throws IOException {
/* 233 */       return useRawResponse() ? getResponse().getOutputStream() : super.getOutputStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public PrintWriter getWriter() throws IOException {
/* 238 */       return useRawResponse() ? getResponse().getWriter() : super.getWriter();
/*     */     }
/*     */     
/*     */     private boolean useRawResponse() {
/* 242 */       return ShallowEtagHeaderFilter.isContentCachingDisabled(this.request);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\ShallowEtagHeaderFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */