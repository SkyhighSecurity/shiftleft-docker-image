/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardServletAsyncWebRequest
/*     */   extends ServletWebRequest
/*     */   implements AsyncWebRequest, AsyncListener
/*     */ {
/*     */   private Long timeout;
/*     */   private AsyncContext asyncContext;
/*  49 */   private AtomicBoolean asyncCompleted = new AtomicBoolean(false);
/*     */   
/*  51 */   private final List<Runnable> timeoutHandlers = new ArrayList<Runnable>();
/*     */   
/*     */   private ErrorHandler errorHandler;
/*     */   
/*  55 */   private final List<Runnable> completionHandlers = new ArrayList<Runnable>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardServletAsyncWebRequest(HttpServletRequest request, HttpServletResponse response) {
/*  64 */     super(request, response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeout(Long timeout) {
/*  74 */     Assert.state(!isAsyncStarted(), "Cannot change the timeout with concurrent handling in progress");
/*  75 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTimeoutHandler(Runnable timeoutHandler) {
/*  80 */     this.timeoutHandlers.add(timeoutHandler);
/*     */   }
/*     */   
/*     */   void setErrorHandler(ErrorHandler errorHandler) {
/*  84 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCompletionHandler(Runnable runnable) {
/*  89 */     this.completionHandlers.add(runnable);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAsyncStarted() {
/*  94 */     return (this.asyncContext != null && getRequest().isAsyncStarted());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAsyncComplete() {
/* 104 */     return this.asyncCompleted.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void startAsync() {
/* 109 */     Assert.state(getRequest().isAsyncSupported(), "Async support must be enabled on a servlet and for all filters involved in async request processing. This is done in Java code using the Servlet API or by adding \"<async-supported>true</async-supported>\" to servlet and filter declarations in web.xml.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     Assert.state(!isAsyncComplete(), "Async processing has already completed");
/*     */     
/* 116 */     if (isAsyncStarted()) {
/*     */       return;
/*     */     }
/* 119 */     this.asyncContext = getRequest().startAsync((ServletRequest)getRequest(), (ServletResponse)getResponse());
/* 120 */     this.asyncContext.addListener(this);
/* 121 */     if (this.timeout != null) {
/* 122 */       this.asyncContext.setTimeout(this.timeout.longValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispatch() {
/* 128 */     Assert.notNull(this.asyncContext, "Cannot dispatch without an AsyncContext");
/* 129 */     this.asyncContext.dispatch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onStartAsync(AsyncEvent event) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(AsyncEvent event) throws IOException {
/* 143 */     if (this.errorHandler != null) {
/* 144 */       this.errorHandler.handle(event.getThrowable());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTimeout(AsyncEvent event) throws IOException {
/* 150 */     for (Runnable handler : this.timeoutHandlers) {
/* 151 */       handler.run();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete(AsyncEvent event) throws IOException {
/* 157 */     for (Runnable handler : this.completionHandlers) {
/* 158 */       handler.run();
/*     */     }
/* 160 */     this.asyncContext = null;
/* 161 */     this.asyncCompleted.set(true);
/*     */   }
/*     */   
/*     */   static interface ErrorHandler {
/*     */     void handle(Throwable param1Throwable);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\StandardServletAsyncWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */