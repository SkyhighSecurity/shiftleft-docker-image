/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HandlerMethodReturnValueHandlerComposite
/*     */   implements AsyncHandlerMethodReturnValueHandler
/*     */ {
/*  38 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  40 */   private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HandlerMethodReturnValueHandler> getHandlers() {
/*  48 */     return Collections.unmodifiableList(this.returnValueHandlers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/*  57 */     return (getReturnValueHandler(returnType) != null);
/*     */   }
/*     */   
/*     */   private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnType) {
/*  61 */     for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
/*  62 */       if (handler.supportsReturnType(returnType)) {
/*  63 */         return handler;
/*     */       }
/*     */     } 
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/*  77 */     HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
/*  78 */     if (handler == null) {
/*  79 */       throw new IllegalArgumentException("Unknown return value type: " + returnType.getParameterType().getName());
/*     */     }
/*  81 */     handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
/*     */   }
/*     */   
/*     */   private HandlerMethodReturnValueHandler selectHandler(Object value, MethodParameter returnType) {
/*  85 */     boolean isAsyncValue = isAsyncReturnValue(value, returnType);
/*  86 */     for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
/*  87 */       if (isAsyncValue && !(handler instanceof AsyncHandlerMethodReturnValueHandler)) {
/*     */         continue;
/*     */       }
/*  90 */       if (handler.supportsReturnType(returnType)) {
/*  91 */         return handler;
/*     */       }
/*     */     } 
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAsyncReturnValue(Object value, MethodParameter returnType) {
/*  99 */     for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
/* 100 */       if (handler instanceof AsyncHandlerMethodReturnValueHandler && (
/* 101 */         (AsyncHandlerMethodReturnValueHandler)handler).isAsyncReturnValue(value, returnType)) {
/* 102 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodReturnValueHandlerComposite addHandler(HandlerMethodReturnValueHandler handler) {
/* 113 */     this.returnValueHandlers.add(handler);
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodReturnValueHandlerComposite addHandlers(List<? extends HandlerMethodReturnValueHandler> handlers) {
/* 121 */     if (handlers != null) {
/* 122 */       for (HandlerMethodReturnValueHandler handler : handlers) {
/* 123 */         this.returnValueHandlers.add(handler);
/*     */       }
/*     */     }
/* 126 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\support\HandlerMethodReturnValueHandlerComposite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */