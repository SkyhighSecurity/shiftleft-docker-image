/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import javax.faces.context.ExternalContext;
/*     */ import javax.faces.context.FacesContext;
/*     */ import javax.portlet.PortletSession;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FacesRequestAttributes
/*     */   implements RequestAttributes
/*     */ {
/*  56 */   private static final boolean portletApiPresent = ClassUtils.isPresent("javax.portlet.PortletSession", FacesRequestAttributes.class.getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private static final Log logger = LogFactory.getLog(FacesRequestAttributes.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final FacesContext facesContext;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FacesRequestAttributes(FacesContext facesContext) {
/*  72 */     Assert.notNull(facesContext, "FacesContext must not be null");
/*  73 */     this.facesContext = facesContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final FacesContext getFacesContext() {
/*  81 */     return this.facesContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ExternalContext getExternalContext() {
/*  89 */     return getFacesContext().getExternalContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, Object> getAttributeMap(int scope) {
/* 100 */     if (scope == 0) {
/* 101 */       return getExternalContext().getRequestMap();
/*     */     }
/*     */     
/* 104 */     return getExternalContext().getSessionMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAttribute(String name, int scope) {
/* 111 */     if (scope == 2 && portletApiPresent) {
/* 112 */       return PortletSessionAccessor.getAttribute(name, getExternalContext());
/*     */     }
/*     */     
/* 115 */     return getAttributeMap(scope).get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object value, int scope) {
/* 121 */     if (scope == 2 && portletApiPresent) {
/* 122 */       PortletSessionAccessor.setAttribute(name, value, getExternalContext());
/*     */     } else {
/*     */       
/* 125 */       getAttributeMap(scope).put(name, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttribute(String name, int scope) {
/* 131 */     if (scope == 2 && portletApiPresent) {
/* 132 */       PortletSessionAccessor.removeAttribute(name, getExternalContext());
/*     */     } else {
/*     */       
/* 135 */       getAttributeMap(scope).remove(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAttributeNames(int scope) {
/* 141 */     if (scope == 2 && portletApiPresent) {
/* 142 */       return PortletSessionAccessor.getAttributeNames(getExternalContext());
/*     */     }
/*     */     
/* 145 */     return StringUtils.toStringArray(getAttributeMap(scope).keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDestructionCallback(String name, Runnable callback, int scope) {
/* 151 */     if (logger.isWarnEnabled()) {
/* 152 */       logger.warn("Could not register destruction callback [" + callback + "] for attribute '" + name + "' because FacesRequestAttributes does not support such callbacks");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object resolveReference(String key) {
/* 159 */     if ("request".equals(key)) {
/* 160 */       return getExternalContext().getRequest();
/*     */     }
/* 162 */     if ("session".equals(key)) {
/* 163 */       return getExternalContext().getSession(true);
/*     */     }
/* 165 */     if ("application".equals(key)) {
/* 166 */       return getExternalContext().getContext();
/*     */     }
/* 168 */     if ("requestScope".equals(key)) {
/* 169 */       return getExternalContext().getRequestMap();
/*     */     }
/* 171 */     if ("sessionScope".equals(key)) {
/* 172 */       return getExternalContext().getSessionMap();
/*     */     }
/* 174 */     if ("applicationScope".equals(key)) {
/* 175 */       return getExternalContext().getApplicationMap();
/*     */     }
/* 177 */     if ("facesContext".equals(key)) {
/* 178 */       return getFacesContext();
/*     */     }
/* 180 */     if ("cookie".equals(key)) {
/* 181 */       return getExternalContext().getRequestCookieMap();
/*     */     }
/* 183 */     if ("header".equals(key)) {
/* 184 */       return getExternalContext().getRequestHeaderMap();
/*     */     }
/* 186 */     if ("headerValues".equals(key)) {
/* 187 */       return getExternalContext().getRequestHeaderValuesMap();
/*     */     }
/* 189 */     if ("param".equals(key)) {
/* 190 */       return getExternalContext().getRequestParameterMap();
/*     */     }
/* 192 */     if ("paramValues".equals(key)) {
/* 193 */       return getExternalContext().getRequestParameterValuesMap();
/*     */     }
/* 195 */     if ("initParam".equals(key)) {
/* 196 */       return getExternalContext().getInitParameterMap();
/*     */     }
/* 198 */     if ("view".equals(key)) {
/* 199 */       return getFacesContext().getViewRoot();
/*     */     }
/* 201 */     if ("viewScope".equals(key)) {
/* 202 */       return getFacesContext().getViewRoot().getViewMap();
/*     */     }
/* 204 */     if ("flash".equals(key)) {
/* 205 */       return getExternalContext().getFlash();
/*     */     }
/* 207 */     if ("resource".equals(key)) {
/* 208 */       return getFacesContext().getApplication().getResourceHandler();
/*     */     }
/*     */     
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/* 217 */     Object session = getExternalContext().getSession(true);
/*     */     
/*     */     try {
/* 220 */       Method getIdMethod = session.getClass().getMethod("getId", new Class[0]);
/* 221 */       return ReflectionUtils.invokeMethod(getIdMethod, session).toString();
/*     */     }
/* 223 */     catch (NoSuchMethodException ex) {
/* 224 */       throw new IllegalStateException("Session object [" + session + "] does not have a getId() method");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getSessionMutex() {
/* 231 */     ExternalContext externalContext = getExternalContext();
/* 232 */     Object session = externalContext.getSession(true);
/* 233 */     Object mutex = externalContext.getSessionMap().get(WebUtils.SESSION_MUTEX_ATTRIBUTE);
/* 234 */     if (mutex == null) {
/* 235 */       mutex = (session != null) ? session : externalContext;
/*     */     }
/* 237 */     return mutex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PortletSessionAccessor
/*     */   {
/*     */     public static Object getAttribute(String name, ExternalContext externalContext) {
/* 247 */       Object session = externalContext.getSession(false);
/* 248 */       if (session instanceof PortletSession) {
/* 249 */         return ((PortletSession)session).getAttribute(name, 1);
/*     */       }
/* 251 */       if (session != null) {
/* 252 */         return externalContext.getSessionMap().get(name);
/*     */       }
/*     */       
/* 255 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public static void setAttribute(String name, Object value, ExternalContext externalContext) {
/* 260 */       Object session = externalContext.getSession(true);
/* 261 */       if (session instanceof PortletSession) {
/* 262 */         ((PortletSession)session).setAttribute(name, value, 1);
/*     */       } else {
/*     */         
/* 265 */         externalContext.getSessionMap().put(name, value);
/*     */       } 
/*     */     }
/*     */     
/*     */     public static void removeAttribute(String name, ExternalContext externalContext) {
/* 270 */       Object session = externalContext.getSession(false);
/* 271 */       if (session instanceof PortletSession) {
/* 272 */         ((PortletSession)session).removeAttribute(name, 1);
/*     */       }
/* 274 */       else if (session != null) {
/* 275 */         externalContext.getSessionMap().remove(name);
/*     */       } 
/*     */     }
/*     */     
/*     */     public static String[] getAttributeNames(ExternalContext externalContext) {
/* 280 */       Object session = externalContext.getSession(false);
/* 281 */       if (session instanceof PortletSession) {
/* 282 */         return StringUtils.toStringArray(((PortletSession)session)
/* 283 */             .getAttributeNames(1));
/*     */       }
/* 285 */       if (session != null) {
/* 286 */         return StringUtils.toStringArray(externalContext.getSessionMap().keySet());
/*     */       }
/*     */       
/* 289 */       return new String[0];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\FacesRequestAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */