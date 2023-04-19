/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.faces.context.ExternalContext;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FacesWebRequest
/*     */   extends FacesRequestAttributes
/*     */   implements NativeWebRequest
/*     */ {
/*     */   public FacesWebRequest(FacesContext facesContext) {
/*  44 */     super(facesContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getNativeRequest() {
/*  50 */     return getExternalContext().getRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getNativeResponse() {
/*  55 */     return getExternalContext().getResponse();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeRequest(Class<T> requiredType) {
/*  61 */     if (requiredType != null) {
/*  62 */       Object request = getExternalContext().getRequest();
/*  63 */       if (requiredType.isInstance(request)) {
/*  64 */         return (T)request;
/*     */       }
/*     */     } 
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeResponse(Class<T> requiredType) {
/*  73 */     if (requiredType != null) {
/*  74 */       Object response = getExternalContext().getResponse();
/*  75 */       if (requiredType.isInstance(response)) {
/*  76 */         return (T)response;
/*     */       }
/*     */     } 
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHeader(String headerName) {
/*  85 */     return (String)getExternalContext().getRequestHeaderMap().get(headerName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getHeaderValues(String headerName) {
/*  90 */     return (String[])getExternalContext().getRequestHeaderValuesMap().get(headerName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getHeaderNames() {
/*  95 */     return getExternalContext().getRequestHeaderMap().keySet().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParameter(String paramName) {
/* 100 */     return (String)getExternalContext().getRequestParameterMap().get(paramName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getParameterNames() {
/* 105 */     return getExternalContext().getRequestParameterNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getParameterValues(String paramName) {
/* 110 */     return (String[])getExternalContext().getRequestParameterValuesMap().get(paramName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 115 */     return getExternalContext().getRequestParameterValuesMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 120 */     return getFacesContext().getExternalContext().getRequestLocale();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContextPath() {
/* 125 */     return getFacesContext().getExternalContext().getRequestContextPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRemoteUser() {
/* 130 */     return getFacesContext().getExternalContext().getRemoteUser();
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getUserPrincipal() {
/* 135 */     return getFacesContext().getExternalContext().getUserPrincipal();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUserInRole(String role) {
/* 140 */     return getFacesContext().getExternalContext().isUserInRole(role);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(long lastModifiedTimestamp) {
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(String eTag) {
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(String etag, long lastModifiedTimestamp) {
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription(boolean includeClientInfo) {
/* 170 */     ExternalContext externalContext = getExternalContext();
/* 171 */     StringBuilder sb = new StringBuilder();
/* 172 */     sb.append("context=").append(externalContext.getRequestContextPath());
/* 173 */     if (includeClientInfo) {
/* 174 */       Object session = externalContext.getSession(false);
/* 175 */       if (session != null) {
/* 176 */         sb.append(";session=").append(getSessionId());
/*     */       }
/* 178 */       String user = externalContext.getRemoteUser();
/* 179 */       if (StringUtils.hasLength(user)) {
/* 180 */         sb.append(";user=").append(user);
/*     */       }
/*     */     } 
/* 183 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 189 */     return "FacesWebRequest: " + getDescription(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\FacesWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */