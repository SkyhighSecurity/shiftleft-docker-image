/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.validation.support.BindingAwareModelMap;
/*     */ import org.springframework.web.bind.support.SessionStatus;
/*     */ import org.springframework.web.bind.support.SimpleSessionStatus;
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
/*     */ public class ModelAndViewContainer
/*     */ {
/*     */   private boolean ignoreDefaultModelOnRedirect = false;
/*     */   private Object view;
/*  55 */   private final ModelMap defaultModel = (ModelMap)new BindingAwareModelMap();
/*     */   
/*     */   private ModelMap redirectModel;
/*     */   
/*     */   private boolean redirectModelScenario = false;
/*     */   
/*     */   private HttpStatus status;
/*     */   
/*  63 */   private final Set<String> noBinding = new HashSet<String>(4);
/*     */   
/*  65 */   private final Set<String> bindingDisabled = new HashSet<String>(4);
/*     */   
/*  67 */   private final SessionStatus sessionStatus = (SessionStatus)new SimpleSessionStatus();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean requestHandled = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect) {
/*  85 */     this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewName(String viewName) {
/*  93 */     this.view = viewName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getViewName() {
/* 101 */     return (this.view instanceof String) ? (String)this.view : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setView(Object view) {
/* 109 */     this.view = view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getView() {
/* 117 */     return this.view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isViewReference() {
/* 125 */     return this.view instanceof String;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMap getModel() {
/* 135 */     if (useDefaultModel()) {
/* 136 */       return this.defaultModel;
/*     */     }
/*     */     
/* 139 */     if (this.redirectModel == null) {
/* 140 */       this.redirectModel = new ModelMap();
/*     */     }
/* 142 */     return this.redirectModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useDefaultModel() {
/* 150 */     return (!this.redirectModelScenario || (this.redirectModel == null && !this.ignoreDefaultModelOnRedirect));
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
/*     */   public ModelMap getDefaultModel() {
/* 164 */     return this.defaultModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRedirectModel(ModelMap redirectModel) {
/* 174 */     this.redirectModel = redirectModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRedirectModelScenario(boolean redirectModelScenario) {
/* 182 */     this.redirectModelScenario = redirectModelScenario;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(HttpStatus status) {
/* 191 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpStatus getStatus() {
/* 199 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBindingDisabled(String attributeName) {
/* 209 */     this.bindingDisabled.add(attributeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBindingDisabled(String name) {
/* 217 */     return (this.bindingDisabled.contains(name) || this.noBinding.contains(name));
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
/*     */   public void setBinding(String attributeName, boolean enabled) {
/* 229 */     if (!enabled) {
/* 230 */       this.noBinding.add(attributeName);
/*     */     } else {
/*     */       
/* 233 */       this.noBinding.remove(attributeName);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionStatus getSessionStatus() {
/* 242 */     return this.sessionStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequestHandled(boolean requestHandled) {
/* 253 */     this.requestHandled = requestHandled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequestHandled() {
/* 260 */     return this.requestHandled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer addAttribute(String name, Object value) {
/* 268 */     getModel().addAttribute(name, value);
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer addAttribute(Object value) {
/* 277 */     getModel().addAttribute(value);
/* 278 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer addAllAttributes(Map<String, ?> attributes) {
/* 286 */     getModel().addAllAttributes(attributes);
/* 287 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer mergeAttributes(Map<String, ?> attributes) {
/* 296 */     getModel().mergeAttributes(attributes);
/* 297 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAndViewContainer removeAttributes(Map<String, ?> attributes) {
/* 304 */     if (attributes != null) {
/* 305 */       for (String key : attributes.keySet()) {
/* 306 */         getModel().remove(key);
/*     */       }
/*     */     }
/* 309 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAttribute(String name) {
/* 317 */     return getModel().containsAttribute(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 326 */     StringBuilder sb = new StringBuilder("ModelAndViewContainer: ");
/* 327 */     if (!isRequestHandled()) {
/* 328 */       if (isViewReference()) {
/* 329 */         sb.append("reference to view with name '").append(this.view).append("'");
/*     */       } else {
/*     */         
/* 332 */         sb.append("View is [").append(this.view).append(']');
/*     */       } 
/* 334 */       if (useDefaultModel()) {
/* 335 */         sb.append("; default model ");
/*     */       } else {
/*     */         
/* 338 */         sb.append("; redirect model ");
/*     */       } 
/* 340 */       sb.append(getModel());
/*     */     } else {
/*     */       
/* 343 */       sb.append("Request handled directly");
/*     */     } 
/* 345 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\support\ModelAndViewContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */