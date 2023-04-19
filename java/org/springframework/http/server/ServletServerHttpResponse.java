/*     */ package org.springframework.http.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletServerHttpResponse
/*     */   implements ServerHttpResponse
/*     */ {
/*  44 */   private static final boolean servlet3Present = ClassUtils.hasMethod(HttpServletResponse.class, "getHeader", new Class[] { String.class });
/*     */ 
/*     */   
/*     */   private final HttpServletResponse servletResponse;
/*     */ 
/*     */   
/*     */   private final HttpHeaders headers;
/*     */ 
/*     */   
/*     */   private boolean headersWritten = false;
/*     */ 
/*     */   
/*     */   private boolean bodyUsed = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletServerHttpResponse(HttpServletResponse servletResponse) {
/*  61 */     Assert.notNull(servletResponse, "HttpServletResponse must not be null");
/*  62 */     this.servletResponse = servletResponse;
/*  63 */     this.headers = servlet3Present ? new ServletResponseHttpHeaders() : new HttpHeaders();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServletResponse getServletResponse() {
/*  71 */     return this.servletResponse;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatusCode(HttpStatus status) {
/*  76 */     Assert.notNull(status, "HttpStatus must not be null");
/*  77 */     this.servletResponse.setStatus(status.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/*  82 */     return this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream getBody() throws IOException {
/*  87 */     this.bodyUsed = true;
/*  88 */     writeHeaders();
/*  89 */     return (OutputStream)this.servletResponse.getOutputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  94 */     writeHeaders();
/*  95 */     if (this.bodyUsed) {
/*  96 */       this.servletResponse.flushBuffer();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 102 */     writeHeaders();
/*     */   }
/*     */   
/*     */   private void writeHeaders() {
/* 106 */     if (!this.headersWritten) {
/* 107 */       for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)this.headers.entrySet()) {
/* 108 */         String headerName = entry.getKey();
/* 109 */         for (String headerValue : entry.getValue()) {
/* 110 */           this.servletResponse.addHeader(headerName, headerValue);
/*     */         }
/*     */       } 
/*     */       
/* 114 */       if (this.servletResponse.getContentType() == null && this.headers.getContentType() != null) {
/* 115 */         this.servletResponse.setContentType(this.headers.getContentType().toString());
/*     */       }
/* 117 */       if (this.servletResponse.getCharacterEncoding() == null && this.headers.getContentType() != null && this.headers
/* 118 */         .getContentType().getCharset() != null) {
/* 119 */         this.servletResponse.setCharacterEncoding(this.headers.getContentType().getCharset().name());
/*     */       }
/* 121 */       this.headersWritten = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ServletResponseHttpHeaders
/*     */     extends HttpHeaders
/*     */   {
/*     */     private static final long serialVersionUID = 3410708522401046302L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ServletResponseHttpHeaders() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 143 */       return (super.containsKey(key) || get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFirst(String headerName) {
/* 148 */       String value = ServletServerHttpResponse.this.servletResponse.getHeader(headerName);
/* 149 */       if (value != null) {
/* 150 */         return value;
/*     */       }
/*     */       
/* 153 */       return super.getFirst(headerName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<String> get(Object key) {
/* 159 */       Assert.isInstanceOf(String.class, key, "Key must be a String-based header name");
/*     */       
/* 161 */       Collection<String> values1 = ServletServerHttpResponse.this.servletResponse.getHeaders((String)key);
/* 162 */       boolean isEmpty1 = CollectionUtils.isEmpty(values1);
/*     */       
/* 164 */       List<String> values2 = super.get(key);
/* 165 */       boolean isEmpty2 = CollectionUtils.isEmpty(values2);
/*     */       
/* 167 */       if (isEmpty1 && isEmpty2) {
/* 168 */         return null;
/*     */       }
/*     */       
/* 171 */       List<String> values = new ArrayList<String>();
/* 172 */       if (!isEmpty1) {
/* 173 */         values.addAll(values1);
/*     */       }
/* 175 */       if (!isEmpty2) {
/* 176 */         values.addAll(values2);
/*     */       }
/* 178 */       return values;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\server\ServletServerHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */