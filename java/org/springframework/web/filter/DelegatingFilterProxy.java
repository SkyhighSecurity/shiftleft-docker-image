/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.support.WebApplicationContextUtils;
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
/*     */ public class DelegatingFilterProxy
/*     */   extends GenericFilterBean
/*     */ {
/*     */   private String contextAttribute;
/*     */   private WebApplicationContext webApplicationContext;
/*     */   private String targetBeanName;
/*     */   private boolean targetFilterLifecycle = false;
/*     */   private volatile Filter delegate;
/*  93 */   private final Object delegateMonitor = new Object();
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
/*     */   public DelegatingFilterProxy() {}
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
/*     */   public DelegatingFilterProxy(Filter delegate) {
/* 118 */     Assert.notNull(delegate, "Delegate Filter must not be null");
/* 119 */     this.delegate = delegate;
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
/*     */   public DelegatingFilterProxy(String targetBeanName) {
/* 136 */     this(targetBeanName, (WebApplicationContext)null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public DelegatingFilterProxy(String targetBeanName, WebApplicationContext wac) {
/* 160 */     Assert.hasText(targetBeanName, "Target Filter bean name must not be null or empty");
/* 161 */     setTargetBeanName(targetBeanName);
/* 162 */     this.webApplicationContext = wac;
/* 163 */     if (wac != null) {
/* 164 */       setEnvironment(wac.getEnvironment());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContextAttribute(String contextAttribute) {
/* 173 */     this.contextAttribute = contextAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContextAttribute() {
/* 181 */     return this.contextAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetBeanName(String targetBeanName) {
/* 191 */     this.targetBeanName = targetBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getTargetBeanName() {
/* 198 */     return this.targetBeanName;
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
/*     */   public void setTargetFilterLifecycle(boolean targetFilterLifecycle) {
/* 210 */     this.targetFilterLifecycle = targetFilterLifecycle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isTargetFilterLifecycle() {
/* 218 */     return this.targetFilterLifecycle;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initFilterBean() throws ServletException {
/* 224 */     synchronized (this.delegateMonitor) {
/* 225 */       if (this.delegate == null) {
/*     */         
/* 227 */         if (this.targetBeanName == null) {
/* 228 */           this.targetBeanName = getFilterName();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 233 */         WebApplicationContext wac = findWebApplicationContext();
/* 234 */         if (wac != null) {
/* 235 */           this.delegate = initDelegate(wac);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 246 */     Filter delegateToUse = this.delegate;
/* 247 */     if (delegateToUse == null) {
/* 248 */       synchronized (this.delegateMonitor) {
/* 249 */         delegateToUse = this.delegate;
/* 250 */         if (delegateToUse == null) {
/* 251 */           WebApplicationContext wac = findWebApplicationContext();
/* 252 */           if (wac == null) {
/* 253 */             throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener or DispatcherServlet registered?");
/*     */           }
/*     */           
/* 256 */           delegateToUse = initDelegate(wac);
/*     */         } 
/* 258 */         this.delegate = delegateToUse;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 263 */     invokeDelegate(delegateToUse, request, response, filterChain);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 268 */     Filter delegateToUse = this.delegate;
/* 269 */     if (delegateToUse != null) {
/* 270 */       destroyDelegate(delegateToUse);
/*     */     }
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
/*     */   protected WebApplicationContext findWebApplicationContext() {
/* 292 */     if (this.webApplicationContext != null) {
/*     */       
/* 294 */       if (this.webApplicationContext instanceof ConfigurableApplicationContext) {
/* 295 */         ConfigurableApplicationContext cac = (ConfigurableApplicationContext)this.webApplicationContext;
/* 296 */         if (!cac.isActive())
/*     */         {
/* 298 */           cac.refresh();
/*     */         }
/*     */       } 
/* 301 */       return this.webApplicationContext;
/*     */     } 
/* 303 */     String attrName = getContextAttribute();
/* 304 */     if (attrName != null) {
/* 305 */       return WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
/*     */     }
/*     */     
/* 308 */     return WebApplicationContextUtils.findWebApplicationContext(getServletContext());
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
/*     */   protected Filter initDelegate(WebApplicationContext wac) throws ServletException {
/* 327 */     Filter delegate = (Filter)wac.getBean(getTargetBeanName(), Filter.class);
/* 328 */     if (isTargetFilterLifecycle()) {
/* 329 */       delegate.init(getFilterConfig());
/*     */     }
/* 331 */     return delegate;
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
/*     */   protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 347 */     delegate.doFilter(request, response, filterChain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroyDelegate(Filter delegate) {
/* 358 */     if (isTargetFilterLifecycle())
/* 359 */       delegate.destroy(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\DelegatingFilterProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */