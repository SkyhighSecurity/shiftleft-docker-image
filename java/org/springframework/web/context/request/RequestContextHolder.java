/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.core.NamedInheritableThreadLocal;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RequestContextHolder
/*     */ {
/*  49 */   private static final boolean jsfPresent = ClassUtils.isPresent("javax.faces.context.FacesContext", RequestContextHolder.class.getClassLoader());
/*     */   
/*  51 */   private static final ThreadLocal<RequestAttributes> requestAttributesHolder = (ThreadLocal<RequestAttributes>)new NamedThreadLocal("Request attributes");
/*     */ 
/*     */   
/*  54 */   private static final ThreadLocal<RequestAttributes> inheritableRequestAttributesHolder = (ThreadLocal<RequestAttributes>)new NamedInheritableThreadLocal("Request context");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetRequestAttributes() {
/*  62 */     requestAttributesHolder.remove();
/*  63 */     inheritableRequestAttributesHolder.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setRequestAttributes(RequestAttributes attributes) {
/*  73 */     setRequestAttributes(attributes, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setRequestAttributes(RequestAttributes attributes, boolean inheritable) {
/*  84 */     if (attributes == null) {
/*  85 */       resetRequestAttributes();
/*     */     
/*     */     }
/*  88 */     else if (inheritable) {
/*  89 */       inheritableRequestAttributesHolder.set(attributes);
/*  90 */       requestAttributesHolder.remove();
/*     */     } else {
/*     */       
/*  93 */       requestAttributesHolder.set(attributes);
/*  94 */       inheritableRequestAttributesHolder.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestAttributes getRequestAttributes() {
/* 105 */     RequestAttributes attributes = requestAttributesHolder.get();
/* 106 */     if (attributes == null) {
/* 107 */       attributes = inheritableRequestAttributesHolder.get();
/*     */     }
/* 109 */     return attributes;
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
/*     */   public static RequestAttributes currentRequestAttributes() throws IllegalStateException {
/* 125 */     RequestAttributes attributes = getRequestAttributes();
/* 126 */     if (attributes == null) {
/* 127 */       if (jsfPresent) {
/* 128 */         attributes = FacesRequestAttributesFactory.getFacesRequestAttributes();
/*     */       }
/* 130 */       if (attributes == null) {
/* 131 */         throw new IllegalStateException("No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread? If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet/DispatcherPortlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     return attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FacesRequestAttributesFactory
/*     */   {
/*     */     public static RequestAttributes getFacesRequestAttributes() {
/* 149 */       FacesContext facesContext = FacesContext.getCurrentInstance();
/* 150 */       return (facesContext != null) ? new FacesRequestAttributes(facesContext) : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\RequestContextHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */