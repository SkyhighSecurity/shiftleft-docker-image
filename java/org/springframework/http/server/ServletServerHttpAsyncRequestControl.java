/*     */ package org.springframework.http.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletServerHttpAsyncRequestControl
/*     */   implements ServerHttpAsyncRequestControl, AsyncListener
/*     */ {
/*     */   private static final long NO_TIMEOUT_VALUE = -9223372036854775808L;
/*     */   private final ServletServerHttpRequest request;
/*     */   private final ServletServerHttpResponse response;
/*     */   private AsyncContext asyncContext;
/*  46 */   private AtomicBoolean asyncCompleted = new AtomicBoolean(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletServerHttpAsyncRequestControl(ServletServerHttpRequest request, ServletServerHttpResponse response) {
/*  55 */     Assert.notNull(request, "request is required");
/*  56 */     Assert.notNull(response, "response is required");
/*     */     
/*  58 */     Assert.isTrue(request.getServletRequest().isAsyncSupported(), "Async support must be enabled on a servlet and for all filters involved in async request processing. This is done in Java code using the Servlet API or by adding \"<async-supported>true</async-supported>\" to servlet and filter declarations in web.xml. Also you must use a Servlet 3.0+ container");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     this.request = request;
/*  65 */     this.response = response;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/*  71 */     return (this.asyncContext != null && this.request.getServletRequest().isAsyncStarted());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompleted() {
/*  76 */     return this.asyncCompleted.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  81 */     start(Long.MIN_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(long timeout) {
/*  86 */     Assert.state(!isCompleted(), "Async processing has already completed");
/*  87 */     if (isStarted()) {
/*     */       return;
/*     */     }
/*     */     
/*  91 */     HttpServletRequest servletRequest = this.request.getServletRequest();
/*  92 */     HttpServletResponse servletResponse = this.response.getServletResponse();
/*     */     
/*  94 */     this.asyncContext = servletRequest.startAsync((ServletRequest)servletRequest, (ServletResponse)servletResponse);
/*  95 */     this.asyncContext.addListener(this);
/*     */     
/*  97 */     if (timeout != Long.MIN_VALUE) {
/*  98 */       this.asyncContext.setTimeout(timeout);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void complete() {
/* 104 */     if (isStarted() && !isCompleted()) {
/* 105 */       this.asyncContext.complete();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete(AsyncEvent event) throws IOException {
/* 116 */     this.asyncContext = null;
/* 117 */     this.asyncCompleted.set(true);
/*     */   }
/*     */   
/*     */   public void onStartAsync(AsyncEvent event) throws IOException {}
/*     */   
/*     */   public void onError(AsyncEvent event) throws IOException {}
/*     */   
/*     */   public void onTimeout(AsyncEvent event) throws IOException {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\server\ServletServerHttpAsyncRequestControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */