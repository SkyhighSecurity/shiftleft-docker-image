/*    */ package org.springframework.web.context.request.async;
/*    */ 
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.web.context.request.WebRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class WebAsyncUtils
/*    */ {
/* 36 */   public static final String WEB_ASYNC_MANAGER_ATTRIBUTE = WebAsyncManager.class.getName() + ".WEB_ASYNC_MANAGER";
/*    */ 
/*    */   
/* 39 */   private static final boolean startAsyncAvailable = ClassUtils.hasMethod(ServletRequest.class, "startAsync", new Class[0]);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static WebAsyncManager getAsyncManager(ServletRequest servletRequest) {
/* 47 */     WebAsyncManager asyncManager = null;
/* 48 */     Object asyncManagerAttr = servletRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE);
/* 49 */     if (asyncManagerAttr instanceof WebAsyncManager) {
/* 50 */       asyncManager = (WebAsyncManager)asyncManagerAttr;
/*    */     }
/* 52 */     if (asyncManager == null) {
/* 53 */       asyncManager = new WebAsyncManager();
/* 54 */       servletRequest.setAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, asyncManager);
/*    */     } 
/* 56 */     return asyncManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static WebAsyncManager getAsyncManager(WebRequest webRequest) {
/* 64 */     int scope = 0;
/* 65 */     WebAsyncManager asyncManager = null;
/* 66 */     Object asyncManagerAttr = webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, scope);
/* 67 */     if (asyncManagerAttr instanceof WebAsyncManager) {
/* 68 */       asyncManager = (WebAsyncManager)asyncManagerAttr;
/*    */     }
/* 70 */     if (asyncManager == null) {
/* 71 */       asyncManager = new WebAsyncManager();
/* 72 */       webRequest.setAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, asyncManager, scope);
/*    */     } 
/* 74 */     return asyncManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AsyncWebRequest createAsyncWebRequest(HttpServletRequest request, HttpServletResponse response) {
/* 87 */     return startAsyncAvailable ? AsyncWebRequestFactory.createStandardAsyncWebRequest(request, response) : new NoSupportAsyncWebRequest(request, response);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static class AsyncWebRequestFactory
/*    */   {
/*    */     public static AsyncWebRequest createStandardAsyncWebRequest(HttpServletRequest request, HttpServletResponse response) {
/* 98 */       return new StandardServletAsyncWebRequest(request, response);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\async\WebAsyncUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */