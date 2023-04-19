/*     */ package org.springframework.web.cors;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.server.ServerHttpRequest;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public class DefaultCorsProcessor
/*     */   implements CorsProcessor
/*     */ {
/*  55 */   private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
/*     */   
/*  57 */   private static final Log logger = LogFactory.getLog(DefaultCorsProcessor.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean processRequest(CorsConfiguration config, HttpServletRequest request, HttpServletResponse response) throws IOException {
/*  65 */     if (!CorsUtils.isCorsRequest(request)) {
/*  66 */       return true;
/*     */     }
/*     */     
/*  69 */     ServletServerHttpResponse serverResponse = new ServletServerHttpResponse(response);
/*  70 */     if (responseHasCors((ServerHttpResponse)serverResponse)) {
/*  71 */       logger.debug("Skip CORS processing: response already contains \"Access-Control-Allow-Origin\" header");
/*  72 */       return true;
/*     */     } 
/*     */     
/*  75 */     ServletServerHttpRequest serverRequest = new ServletServerHttpRequest(request);
/*  76 */     if (WebUtils.isSameOrigin((HttpRequest)serverRequest)) {
/*  77 */       logger.debug("Skip CORS processing: request is from same origin");
/*  78 */       return true;
/*     */     } 
/*     */     
/*  81 */     boolean preFlightRequest = CorsUtils.isPreFlightRequest(request);
/*  82 */     if (config == null) {
/*  83 */       if (preFlightRequest) {
/*  84 */         rejectRequest((ServerHttpResponse)serverResponse);
/*  85 */         return false;
/*     */       } 
/*     */       
/*  88 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  92 */     return handleInternal((ServerHttpRequest)serverRequest, (ServerHttpResponse)serverResponse, config, preFlightRequest);
/*     */   }
/*     */   
/*     */   private boolean responseHasCors(ServerHttpResponse response) {
/*     */     try {
/*  97 */       return (response.getHeaders().getAccessControlAllowOrigin() != null);
/*     */     }
/*  99 */     catch (NullPointerException npe) {
/*     */       
/* 101 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rejectRequest(ServerHttpResponse response) throws IOException {
/* 111 */     response.setStatusCode(HttpStatus.FORBIDDEN);
/* 112 */     response.getBody().write("Invalid CORS request".getBytes(UTF8_CHARSET));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleInternal(ServerHttpRequest request, ServerHttpResponse response, CorsConfiguration config, boolean preFlightRequest) throws IOException {
/* 121 */     String requestOrigin = request.getHeaders().getOrigin();
/* 122 */     String allowOrigin = checkOrigin(config, requestOrigin);
/*     */     
/* 124 */     HttpMethod requestMethod = getMethodToUse(request, preFlightRequest);
/* 125 */     List<HttpMethod> allowMethods = checkMethods(config, requestMethod);
/*     */     
/* 127 */     List<String> requestHeaders = getHeadersToUse(request, preFlightRequest);
/* 128 */     List<String> allowHeaders = checkHeaders(config, requestHeaders);
/*     */     
/* 130 */     if (allowOrigin == null || allowMethods == null || (preFlightRequest && allowHeaders == null)) {
/* 131 */       rejectRequest(response);
/* 132 */       return false;
/*     */     } 
/*     */     
/* 135 */     HttpHeaders responseHeaders = response.getHeaders();
/* 136 */     responseHeaders.setAccessControlAllowOrigin(allowOrigin);
/* 137 */     responseHeaders.add("Vary", "Origin");
/*     */     
/* 139 */     if (preFlightRequest) {
/* 140 */       responseHeaders.setAccessControlAllowMethods(allowMethods);
/*     */     }
/*     */     
/* 143 */     if (preFlightRequest && !allowHeaders.isEmpty()) {
/* 144 */       responseHeaders.setAccessControlAllowHeaders(allowHeaders);
/*     */     }
/*     */     
/* 147 */     if (!CollectionUtils.isEmpty(config.getExposedHeaders())) {
/* 148 */       responseHeaders.setAccessControlExposeHeaders(config.getExposedHeaders());
/*     */     }
/*     */     
/* 151 */     if (Boolean.TRUE.equals(config.getAllowCredentials())) {
/* 152 */       responseHeaders.setAccessControlAllowCredentials(true);
/*     */     }
/*     */     
/* 155 */     if (preFlightRequest && config.getMaxAge() != null) {
/* 156 */       responseHeaders.setAccessControlMaxAge(config.getMaxAge().longValue());
/*     */     }
/*     */     
/* 159 */     response.flush();
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String checkOrigin(CorsConfiguration config, String requestOrigin) {
/* 169 */     return config.checkOrigin(requestOrigin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<HttpMethod> checkMethods(CorsConfiguration config, HttpMethod requestMethod) {
/* 178 */     return config.checkHttpMethod(requestMethod);
/*     */   }
/*     */   
/*     */   private HttpMethod getMethodToUse(ServerHttpRequest request, boolean isPreFlight) {
/* 182 */     return isPreFlight ? request.getHeaders().getAccessControlRequestMethod() : request.getMethod();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<String> checkHeaders(CorsConfiguration config, List<String> requestHeaders) {
/* 191 */     return config.checkHeaders(requestHeaders);
/*     */   }
/*     */   
/*     */   private List<String> getHeadersToUse(ServerHttpRequest request, boolean isPreFlight) {
/* 195 */     HttpHeaders headers = request.getHeaders();
/* 196 */     return isPreFlight ? headers.getAccessControlRequestHeaders() : new ArrayList<String>(headers.keySet());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\cors\DefaultCorsProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */