/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.faces.context.ExternalContext;
/*     */ import javax.faces.context.FacesContext;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.Scope;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.RequestScope;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.context.request.SessionScope;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WebApplicationContextUtils
/*     */ {
/*  67 */   private static final boolean jsfPresent = ClassUtils.isPresent("javax.faces.context.FacesContext", RequestContextHolder.class.getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WebApplicationContext getRequiredWebApplicationContext(ServletContext sc) throws IllegalStateException {
/*  81 */     WebApplicationContext wac = getWebApplicationContext(sc);
/*  82 */     if (wac == null) {
/*  83 */       throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/*     */     }
/*  85 */     return wac;
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
/*     */   public static WebApplicationContext getWebApplicationContext(ServletContext sc) {
/*  98 */     return getWebApplicationContext(sc, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WebApplicationContext getWebApplicationContext(ServletContext sc, String attrName) {
/* 108 */     Assert.notNull(sc, "ServletContext must not be null");
/* 109 */     Object attr = sc.getAttribute(attrName);
/* 110 */     if (attr == null) {
/* 111 */       return null;
/*     */     }
/* 113 */     if (attr instanceof RuntimeException) {
/* 114 */       throw (RuntimeException)attr;
/*     */     }
/* 116 */     if (attr instanceof Error) {
/* 117 */       throw (Error)attr;
/*     */     }
/* 119 */     if (attr instanceof Exception) {
/* 120 */       throw new IllegalStateException((Exception)attr);
/*     */     }
/* 122 */     if (!(attr instanceof WebApplicationContext)) {
/* 123 */       throw new IllegalStateException("Context attribute is not of type WebApplicationContext: " + attr);
/*     */     }
/* 125 */     return (WebApplicationContext)attr;
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
/*     */   public static WebApplicationContext findWebApplicationContext(ServletContext sc) {
/* 144 */     WebApplicationContext wac = getWebApplicationContext(sc);
/* 145 */     if (wac == null) {
/* 146 */       Enumeration<String> attrNames = sc.getAttributeNames();
/* 147 */       while (attrNames.hasMoreElements()) {
/* 148 */         String attrName = attrNames.nextElement();
/* 149 */         Object attrValue = sc.getAttribute(attrName);
/* 150 */         if (attrValue instanceof WebApplicationContext) {
/* 151 */           if (wac != null) {
/* 152 */             throw new IllegalStateException("No unique WebApplicationContext found: more than one DispatcherServlet registered with publishContext=true?");
/*     */           }
/*     */           
/* 155 */           wac = (WebApplicationContext)attrValue;
/*     */         } 
/*     */       } 
/*     */     } 
/* 159 */     return wac;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerWebApplicationScopes(ConfigurableListableBeanFactory beanFactory) {
/* 169 */     registerWebApplicationScopes(beanFactory, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerWebApplicationScopes(ConfigurableListableBeanFactory beanFactory, ServletContext sc) {
/* 179 */     beanFactory.registerScope("request", (Scope)new RequestScope());
/* 180 */     beanFactory.registerScope("session", (Scope)new SessionScope(false));
/* 181 */     beanFactory.registerScope("globalSession", (Scope)new SessionScope(true));
/* 182 */     if (sc != null) {
/* 183 */       ServletContextScope appScope = new ServletContextScope(sc);
/* 184 */       beanFactory.registerScope("application", appScope);
/*     */       
/* 186 */       sc.setAttribute(ServletContextScope.class.getName(), appScope);
/*     */     } 
/*     */     
/* 189 */     beanFactory.registerResolvableDependency(ServletRequest.class, new RequestObjectFactory());
/* 190 */     beanFactory.registerResolvableDependency(ServletResponse.class, new ResponseObjectFactory());
/* 191 */     beanFactory.registerResolvableDependency(HttpSession.class, new SessionObjectFactory());
/* 192 */     beanFactory.registerResolvableDependency(WebRequest.class, new WebRequestObjectFactory());
/* 193 */     if (jsfPresent) {
/* 194 */       FacesDependencyRegistrar.registerFacesDependencies(beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerEnvironmentBeans(ConfigurableListableBeanFactory bf, ServletContext sc) {
/* 205 */     registerEnvironmentBeans(bf, sc, null);
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
/*     */   public static void registerEnvironmentBeans(ConfigurableListableBeanFactory bf, ServletContext servletContext, ServletConfig servletConfig) {
/* 218 */     if (servletContext != null && !bf.containsBean("servletContext")) {
/* 219 */       bf.registerSingleton("servletContext", servletContext);
/*     */     }
/*     */     
/* 222 */     if (servletConfig != null && !bf.containsBean("servletConfig")) {
/* 223 */       bf.registerSingleton("servletConfig", servletConfig);
/*     */     }
/*     */     
/* 226 */     if (!bf.containsBean("contextParameters")) {
/* 227 */       Map<String, String> parameterMap = new HashMap<String, String>();
/* 228 */       if (servletContext != null) {
/* 229 */         Enumeration<?> paramNameEnum = servletContext.getInitParameterNames();
/* 230 */         while (paramNameEnum.hasMoreElements()) {
/* 231 */           String paramName = (String)paramNameEnum.nextElement();
/* 232 */           parameterMap.put(paramName, servletContext.getInitParameter(paramName));
/*     */         } 
/*     */       } 
/* 235 */       if (servletConfig != null) {
/* 236 */         Enumeration<?> paramNameEnum = servletConfig.getInitParameterNames();
/* 237 */         while (paramNameEnum.hasMoreElements()) {
/* 238 */           String paramName = (String)paramNameEnum.nextElement();
/* 239 */           parameterMap.put(paramName, servletConfig.getInitParameter(paramName));
/*     */         } 
/*     */       } 
/* 242 */       bf.registerSingleton("contextParameters", 
/* 243 */           Collections.unmodifiableMap(parameterMap));
/*     */     } 
/*     */     
/* 246 */     if (!bf.containsBean("contextAttributes")) {
/* 247 */       Map<String, Object> attributeMap = new HashMap<String, Object>();
/* 248 */       if (servletContext != null) {
/* 249 */         Enumeration<?> attrNameEnum = servletContext.getAttributeNames();
/* 250 */         while (attrNameEnum.hasMoreElements()) {
/* 251 */           String attrName = (String)attrNameEnum.nextElement();
/* 252 */           attributeMap.put(attrName, servletContext.getAttribute(attrName));
/*     */         } 
/*     */       } 
/* 255 */       bf.registerSingleton("contextAttributes", 
/* 256 */           Collections.unmodifiableMap(attributeMap));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initServletPropertySources(MutablePropertySources propertySources, ServletContext servletContext) {
/* 267 */     initServletPropertySources(propertySources, servletContext, null);
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
/*     */   public static void initServletPropertySources(MutablePropertySources propertySources, ServletContext servletContext, ServletConfig servletConfig) {
/* 291 */     Assert.notNull(propertySources, "'propertySources' must not be null");
/* 292 */     if (servletContext != null && propertySources.contains("servletContextInitParams") && propertySources
/* 293 */       .get("servletContextInitParams") instanceof PropertySource.StubPropertySource) {
/* 294 */       propertySources.replace("servletContextInitParams", (PropertySource)new ServletContextPropertySource("servletContextInitParams", servletContext));
/*     */     }
/*     */     
/* 297 */     if (servletConfig != null && propertySources.contains("servletConfigInitParams") && propertySources
/* 298 */       .get("servletConfigInitParams") instanceof PropertySource.StubPropertySource) {
/* 299 */       propertySources.replace("servletConfigInitParams", (PropertySource)new ServletConfigPropertySource("servletConfigInitParams", servletConfig));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ServletRequestAttributes currentRequestAttributes() {
/* 309 */     RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
/* 310 */     if (!(requestAttr instanceof ServletRequestAttributes)) {
/* 311 */       throw new IllegalStateException("Current request is not a servlet request");
/*     */     }
/* 313 */     return (ServletRequestAttributes)requestAttr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RequestObjectFactory
/*     */     implements ObjectFactory<ServletRequest>, Serializable
/*     */   {
/*     */     private RequestObjectFactory() {}
/*     */ 
/*     */     
/*     */     public ServletRequest getObject() {
/* 325 */       return (ServletRequest)WebApplicationContextUtils.currentRequestAttributes().getRequest();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 330 */       return "Current HttpServletRequest";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ResponseObjectFactory
/*     */     implements ObjectFactory<ServletResponse>, Serializable
/*     */   {
/*     */     private ResponseObjectFactory() {}
/*     */ 
/*     */     
/*     */     public ServletResponse getObject() {
/* 343 */       HttpServletResponse httpServletResponse = WebApplicationContextUtils.currentRequestAttributes().getResponse();
/* 344 */       if (httpServletResponse == null) {
/* 345 */         throw new IllegalStateException("Current servlet response not available - consider using RequestContextFilter instead of RequestContextListener");
/*     */       }
/*     */       
/* 348 */       return (ServletResponse)httpServletResponse;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 353 */       return "Current HttpServletResponse";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SessionObjectFactory
/*     */     implements ObjectFactory<HttpSession>, Serializable
/*     */   {
/*     */     private SessionObjectFactory() {}
/*     */ 
/*     */     
/*     */     public HttpSession getObject() {
/* 366 */       return WebApplicationContextUtils.currentRequestAttributes().getRequest().getSession();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 371 */       return "Current HttpSession";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WebRequestObjectFactory
/*     */     implements ObjectFactory<WebRequest>, Serializable
/*     */   {
/*     */     private WebRequestObjectFactory() {}
/*     */ 
/*     */     
/*     */     public WebRequest getObject() {
/* 384 */       ServletRequestAttributes requestAttr = WebApplicationContextUtils.currentRequestAttributes();
/* 385 */       return (WebRequest)new ServletWebRequest(requestAttr.getRequest(), requestAttr.getResponse());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 390 */       return "Current ServletWebRequest";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FacesDependencyRegistrar
/*     */   {
/*     */     public static void registerFacesDependencies(ConfigurableListableBeanFactory beanFactory) {
/* 401 */       beanFactory.registerResolvableDependency(FacesContext.class, new ObjectFactory<FacesContext>()
/*     */           {
/*     */             public FacesContext getObject() {
/* 404 */               return FacesContext.getCurrentInstance();
/*     */             }
/*     */             
/*     */             public String toString() {
/* 408 */               return "Current JSF FacesContext";
/*     */             }
/*     */           });
/* 411 */       beanFactory.registerResolvableDependency(ExternalContext.class, new ObjectFactory<ExternalContext>()
/*     */           {
/*     */             public ExternalContext getObject() {
/* 414 */               return FacesContext.getCurrentInstance().getExternalContext();
/*     */             }
/*     */             
/*     */             public String toString() {
/* 418 */               return "Current JSF ExternalContext";
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\WebApplicationContextUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */