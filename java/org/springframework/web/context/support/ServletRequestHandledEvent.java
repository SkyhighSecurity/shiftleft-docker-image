/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletRequestHandledEvent
/*     */   extends RequestHandledEvent
/*     */ {
/*     */   private final String requestUrl;
/*     */   private final String clientAddress;
/*     */   private final String method;
/*     */   private final String servletName;
/*     */   private final int statusCode;
/*     */   
/*     */   public ServletRequestHandledEvent(Object source, String requestUrl, String clientAddress, String method, String servletName, String sessionId, String userName, long processingTimeMillis) {
/*  63 */     super(source, sessionId, userName, processingTimeMillis);
/*  64 */     this.requestUrl = requestUrl;
/*  65 */     this.clientAddress = clientAddress;
/*  66 */     this.method = method;
/*  67 */     this.servletName = servletName;
/*  68 */     this.statusCode = -1;
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
/*     */   public ServletRequestHandledEvent(Object source, String requestUrl, String clientAddress, String method, String servletName, String sessionId, String userName, long processingTimeMillis, Throwable failureCause) {
/*  88 */     super(source, sessionId, userName, processingTimeMillis, failureCause);
/*  89 */     this.requestUrl = requestUrl;
/*  90 */     this.clientAddress = clientAddress;
/*  91 */     this.method = method;
/*  92 */     this.servletName = servletName;
/*  93 */     this.statusCode = -1;
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
/*     */   public ServletRequestHandledEvent(Object source, String requestUrl, String clientAddress, String method, String servletName, String sessionId, String userName, long processingTimeMillis, Throwable failureCause, int statusCode) {
/* 114 */     super(source, sessionId, userName, processingTimeMillis, failureCause);
/* 115 */     this.requestUrl = requestUrl;
/* 116 */     this.clientAddress = clientAddress;
/* 117 */     this.method = method;
/* 118 */     this.servletName = servletName;
/* 119 */     this.statusCode = statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRequestUrl() {
/* 127 */     return this.requestUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientAddress() {
/* 134 */     return this.clientAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethod() {
/* 141 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServletName() {
/* 148 */     return this.servletName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusCode() {
/* 157 */     return this.statusCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getShortDescription() {
/* 162 */     StringBuilder sb = new StringBuilder();
/* 163 */     sb.append("url=[").append(getRequestUrl()).append("]; ");
/* 164 */     sb.append("client=[").append(getClientAddress()).append("]; ");
/* 165 */     sb.append(super.getShortDescription());
/* 166 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 171 */     StringBuilder sb = new StringBuilder();
/* 172 */     sb.append("url=[").append(getRequestUrl()).append("]; ");
/* 173 */     sb.append("client=[").append(getClientAddress()).append("]; ");
/* 174 */     sb.append("method=[").append(getMethod()).append("]; ");
/* 175 */     sb.append("servlet=[").append(getServletName()).append("]; ");
/* 176 */     sb.append(super.getDescription());
/* 177 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 182 */     return "ServletRequestHandledEvent: " + getDescription();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletRequestHandledEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */