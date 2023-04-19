/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.config.Scope;
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
/*     */ public class ServletContextScope
/*     */   implements Scope, DisposableBean
/*     */ {
/*     */   private final ServletContext servletContext;
/*  52 */   private final Map<String, Runnable> destructionCallbacks = new LinkedHashMap<String, Runnable>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletContextScope(ServletContext servletContext) {
/*  60 */     Assert.notNull(servletContext, "ServletContext must not be null");
/*  61 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(String name, ObjectFactory<?> objectFactory) {
/*  67 */     Object scopedObject = this.servletContext.getAttribute(name);
/*  68 */     if (scopedObject == null) {
/*  69 */       scopedObject = objectFactory.getObject();
/*  70 */       this.servletContext.setAttribute(name, scopedObject);
/*     */     } 
/*  72 */     return scopedObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object remove(String name) {
/*  77 */     Object scopedObject = this.servletContext.getAttribute(name);
/*  78 */     if (scopedObject != null) {
/*  79 */       this.servletContext.removeAttribute(name);
/*  80 */       this.destructionCallbacks.remove(name);
/*  81 */       return scopedObject;
/*     */     } 
/*     */     
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDestructionCallback(String name, Runnable callback) {
/*  90 */     this.destructionCallbacks.put(name, callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object resolveContextualObject(String key) {
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConversationId() {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 111 */     for (Runnable runnable : this.destructionCallbacks.values()) {
/* 112 */       runnable.run();
/*     */     }
/* 114 */     this.destructionCallbacks.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\ServletContextScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */