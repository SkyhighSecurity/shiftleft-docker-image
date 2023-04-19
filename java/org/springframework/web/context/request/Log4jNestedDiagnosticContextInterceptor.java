/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.NDC;
/*     */ import org.springframework.ui.ModelMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class Log4jNestedDiagnosticContextInterceptor
/*     */   implements AsyncWebRequestInterceptor
/*     */ {
/*  40 */   protected final Logger log4jLogger = Logger.getLogger(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean includeClientInfo = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeClientInfo(boolean includeClientInfo) {
/*  50 */     this.includeClientInfo = includeClientInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIncludeClientInfo() {
/*  58 */     return this.includeClientInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void preHandle(WebRequest request) throws Exception {
/*  67 */     NDC.push(getNestedDiagnosticContextMessage(request));
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
/*     */   protected String getNestedDiagnosticContextMessage(WebRequest request) {
/*  79 */     return request.getDescription(isIncludeClientInfo());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postHandle(WebRequest request, ModelMap model) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterCompletion(WebRequest request, Exception ex) throws Exception {
/*  91 */     NDC.pop();
/*  92 */     if (NDC.getDepth() == 0) {
/*  93 */       NDC.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterConcurrentHandlingStarted(WebRequest request) {
/* 103 */     NDC.pop();
/* 104 */     if (NDC.getDepth() == 0)
/* 105 */       NDC.remove(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\Log4jNestedDiagnosticContextInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */