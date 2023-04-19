/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.ContentCachingRequestWrapper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRequestLoggingFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   public static final String DEFAULT_BEFORE_MESSAGE_PREFIX = "Before request [";
/*     */   public static final String DEFAULT_BEFORE_MESSAGE_SUFFIX = "]";
/*     */   public static final String DEFAULT_AFTER_MESSAGE_PREFIX = "After request [";
/*     */   public static final String DEFAULT_AFTER_MESSAGE_SUFFIX = "]";
/*     */   private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 50;
/*     */   private boolean includeQueryString = false;
/*     */   private boolean includeClientInfo = false;
/*     */   private boolean includeHeaders = false;
/*     */   private boolean includePayload = false;
/*  80 */   private int maxPayloadLength = 50;
/*     */   
/*  82 */   private String beforeMessagePrefix = "Before request [";
/*     */   
/*  84 */   private String beforeMessageSuffix = "]";
/*     */   
/*  86 */   private String afterMessagePrefix = "After request [";
/*     */   
/*  88 */   private String afterMessageSuffix = "]";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeQueryString(boolean includeQueryString) {
/*  97 */     this.includeQueryString = includeQueryString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIncludeQueryString() {
/* 104 */     return this.includeQueryString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeClientInfo(boolean includeClientInfo) {
/* 114 */     this.includeClientInfo = includeClientInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIncludeClientInfo() {
/* 122 */     return this.includeClientInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeHeaders(boolean includeHeaders) {
/* 132 */     this.includeHeaders = includeHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeHeaders() {
/* 140 */     return this.includeHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludePayload(boolean includePayload) {
/* 150 */     this.includePayload = includePayload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIncludePayload() {
/* 158 */     return this.includePayload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPayloadLength(int maxPayloadLength) {
/* 167 */     Assert.isTrue((maxPayloadLength >= 0), "'maxPayloadLength' should be larger than or equal to 0");
/* 168 */     this.maxPayloadLength = maxPayloadLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMaxPayloadLength() {
/* 176 */     return this.maxPayloadLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeforeMessagePrefix(String beforeMessagePrefix) {
/* 184 */     this.beforeMessagePrefix = beforeMessagePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeforeMessageSuffix(String beforeMessageSuffix) {
/* 192 */     this.beforeMessageSuffix = beforeMessageSuffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAfterMessagePrefix(String afterMessagePrefix) {
/* 200 */     this.afterMessagePrefix = afterMessagePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAfterMessageSuffix(String afterMessageSuffix) {
/* 208 */     this.afterMessageSuffix = afterMessageSuffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterAsyncDispatch() {
/* 219 */     return false;
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
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*     */     ContentCachingRequestWrapper contentCachingRequestWrapper;
/* 232 */     boolean isFirstRequest = !isAsyncDispatch(request);
/* 233 */     HttpServletRequest requestToUse = request;
/*     */     
/* 235 */     if (isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
/* 236 */       contentCachingRequestWrapper = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
/*     */     }
/*     */     
/* 239 */     boolean shouldLog = shouldLog((HttpServletRequest)contentCachingRequestWrapper);
/* 240 */     if (shouldLog && isFirstRequest) {
/* 241 */       beforeRequest((HttpServletRequest)contentCachingRequestWrapper, getBeforeMessage((HttpServletRequest)contentCachingRequestWrapper));
/*     */     }
/*     */     try {
/* 244 */       filterChain.doFilter((ServletRequest)contentCachingRequestWrapper, (ServletResponse)response);
/*     */     } finally {
/*     */       
/* 247 */       if (shouldLog && !isAsyncStarted((HttpServletRequest)contentCachingRequestWrapper)) {
/* 248 */         afterRequest((HttpServletRequest)contentCachingRequestWrapper, getAfterMessage((HttpServletRequest)contentCachingRequestWrapper));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getBeforeMessage(HttpServletRequest request) {
/* 258 */     return createMessage(request, this.beforeMessagePrefix, this.beforeMessageSuffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getAfterMessage(HttpServletRequest request) {
/* 266 */     return createMessage(request, this.afterMessagePrefix, this.afterMessageSuffix);
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
/*     */   protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
/* 278 */     StringBuilder msg = new StringBuilder();
/* 279 */     msg.append(prefix);
/* 280 */     msg.append("uri=").append(request.getRequestURI());
/*     */     
/* 282 */     if (isIncludeQueryString()) {
/* 283 */       String queryString = request.getQueryString();
/* 284 */       if (queryString != null) {
/* 285 */         msg.append('?').append(queryString);
/*     */       }
/*     */     } 
/*     */     
/* 289 */     if (isIncludeClientInfo()) {
/* 290 */       String client = request.getRemoteAddr();
/* 291 */       if (StringUtils.hasLength(client)) {
/* 292 */         msg.append(";client=").append(client);
/*     */       }
/* 294 */       HttpSession session = request.getSession(false);
/* 295 */       if (session != null) {
/* 296 */         msg.append(";session=").append(session.getId());
/*     */       }
/* 298 */       String user = request.getRemoteUser();
/* 299 */       if (user != null) {
/* 300 */         msg.append(";user=").append(user);
/*     */       }
/*     */     } 
/*     */     
/* 304 */     if (isIncludeHeaders()) {
/* 305 */       msg.append(";headers=").append((new ServletServerHttpRequest(request)).getHeaders());
/*     */     }
/*     */     
/* 308 */     if (isIncludePayload()) {
/*     */       
/* 310 */       ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper)WebUtils.getNativeRequest((ServletRequest)request, ContentCachingRequestWrapper.class);
/* 311 */       if (wrapper != null) {
/* 312 */         byte[] buf = wrapper.getContentAsByteArray();
/* 313 */         if (buf.length > 0) {
/* 314 */           String payload; int length = Math.min(buf.length, getMaxPayloadLength());
/*     */           
/*     */           try {
/* 317 */             payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
/*     */           }
/* 319 */           catch (UnsupportedEncodingException ex) {
/* 320 */             payload = "[unknown]";
/*     */           } 
/* 322 */           msg.append(";payload=").append(payload);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 327 */     msg.append(suffix);
/* 328 */     return msg.toString();
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
/*     */   protected boolean shouldLog(HttpServletRequest request) {
/* 344 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract void beforeRequest(HttpServletRequest paramHttpServletRequest, String paramString);
/*     */   
/*     */   protected abstract void afterRequest(HttpServletRequest paramHttpServletRequest, String paramString);
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\AbstractRequestLoggingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */