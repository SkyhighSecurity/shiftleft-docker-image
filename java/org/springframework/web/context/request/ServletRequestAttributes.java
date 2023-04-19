/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.NumberUtils;
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
/*     */ public class ServletRequestAttributes
/*     */   extends AbstractRequestAttributes
/*     */ {
/*  49 */   public static final String DESTRUCTION_CALLBACK_NAME_PREFIX = ServletRequestAttributes.class
/*  50 */     .getName() + ".DESTRUCTION_CALLBACK.";
/*     */   
/*  52 */   protected static final Set<Class<?>> immutableValueTypes = new HashSet<Class<?>>(16);
/*     */   
/*     */   static {
/*  55 */     immutableValueTypes.addAll(NumberUtils.STANDARD_NUMBER_TYPES);
/*  56 */     immutableValueTypes.add(Boolean.class);
/*  57 */     immutableValueTypes.add(Character.class);
/*  58 */     immutableValueTypes.add(String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   private final HttpServletRequest request;
/*     */   
/*     */   private HttpServletResponse response;
/*     */   
/*     */   private volatile HttpSession session;
/*     */   
/*  68 */   private final Map<String, Object> sessionAttributesToUpdate = new ConcurrentHashMap<String, Object>(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletRequestAttributes(HttpServletRequest request) {
/*  76 */     Assert.notNull(request, "Request must not be null");
/*  77 */     this.request = request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletRequestAttributes(HttpServletRequest request, HttpServletResponse response) {
/*  86 */     this(request);
/*  87 */     this.response = response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpServletRequest getRequest() {
/*  95 */     return this.request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpServletResponse getResponse() {
/* 102 */     return this.response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final HttpSession getSession(boolean allowCreate) {
/* 110 */     if (isRequestActive()) {
/* 111 */       HttpSession httpSession = this.request.getSession(allowCreate);
/* 112 */       this.session = httpSession;
/* 113 */       return httpSession;
/*     */     } 
/*     */ 
/*     */     
/* 117 */     HttpSession session = this.session;
/* 118 */     if (session == null) {
/* 119 */       if (allowCreate) {
/* 120 */         throw new IllegalStateException("No session found and request already completed - cannot create new session!");
/*     */       }
/*     */ 
/*     */       
/* 124 */       session = this.request.getSession(false);
/* 125 */       this.session = session;
/*     */     } 
/*     */     
/* 128 */     return session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAttribute(String name, int scope) {
/* 135 */     if (scope == 0) {
/* 136 */       if (!isRequestActive()) {
/* 137 */         throw new IllegalStateException("Cannot ask for request attribute - request is not active anymore!");
/*     */       }
/*     */       
/* 140 */       return this.request.getAttribute(name);
/*     */     } 
/*     */     
/* 143 */     HttpSession session = getSession(false);
/* 144 */     if (session != null) {
/*     */       try {
/* 146 */         Object value = session.getAttribute(name);
/* 147 */         if (value != null) {
/* 148 */           this.sessionAttributesToUpdate.put(name, value);
/*     */         }
/* 150 */         return value;
/*     */       }
/* 152 */       catch (IllegalStateException illegalStateException) {}
/*     */     }
/*     */ 
/*     */     
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object value, int scope) {
/* 162 */     if (scope == 0) {
/* 163 */       if (!isRequestActive()) {
/* 164 */         throw new IllegalStateException("Cannot set request attribute - request is not active anymore!");
/*     */       }
/*     */       
/* 167 */       this.request.setAttribute(name, value);
/*     */     } else {
/*     */       
/* 170 */       HttpSession session = getSession(true);
/* 171 */       this.sessionAttributesToUpdate.remove(name);
/* 172 */       session.setAttribute(name, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttribute(String name, int scope) {
/* 178 */     if (scope == 0) {
/* 179 */       if (isRequestActive()) {
/* 180 */         this.request.removeAttribute(name);
/* 181 */         removeRequestDestructionCallback(name);
/*     */       } 
/*     */     } else {
/*     */       
/* 185 */       HttpSession session = getSession(false);
/* 186 */       if (session != null) {
/* 187 */         this.sessionAttributesToUpdate.remove(name);
/*     */         try {
/* 189 */           session.removeAttribute(name);
/*     */           
/* 191 */           session.removeAttribute(DESTRUCTION_CALLBACK_NAME_PREFIX + name);
/*     */         }
/* 193 */         catch (IllegalStateException illegalStateException) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAttributeNames(int scope) {
/* 202 */     if (scope == 0) {
/* 203 */       if (!isRequestActive()) {
/* 204 */         throw new IllegalStateException("Cannot ask for request attributes - request is not active anymore!");
/*     */       }
/*     */       
/* 207 */       return StringUtils.toStringArray(this.request.getAttributeNames());
/*     */     } 
/*     */     
/* 210 */     HttpSession session = getSession(false);
/* 211 */     if (session != null) {
/*     */       try {
/* 213 */         return StringUtils.toStringArray(session.getAttributeNames());
/*     */       }
/* 215 */       catch (IllegalStateException illegalStateException) {}
/*     */     }
/*     */ 
/*     */     
/* 219 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDestructionCallback(String name, Runnable callback, int scope) {
/* 225 */     if (scope == 0) {
/* 226 */       registerRequestDestructionCallback(name, callback);
/*     */     } else {
/*     */       
/* 229 */       registerSessionDestructionCallback(name, callback);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object resolveReference(String key) {
/* 235 */     if ("request".equals(key)) {
/* 236 */       return this.request;
/*     */     }
/* 238 */     if ("session".equals(key)) {
/* 239 */       return getSession(true);
/*     */     }
/*     */     
/* 242 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/* 248 */     return getSession(true).getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSessionMutex() {
/* 253 */     return WebUtils.getSessionMutex(getSession(true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateAccessedSessionAttributes() {
/* 263 */     if (!this.sessionAttributesToUpdate.isEmpty()) {
/*     */       
/* 265 */       HttpSession session = getSession(false);
/* 266 */       if (session != null) {
/*     */         try {
/* 268 */           for (Map.Entry<String, Object> entry : this.sessionAttributesToUpdate.entrySet()) {
/* 269 */             String name = entry.getKey();
/* 270 */             Object newValue = entry.getValue();
/* 271 */             Object oldValue = session.getAttribute(name);
/* 272 */             if (oldValue == newValue && !isImmutableSessionAttribute(name, newValue)) {
/* 273 */               session.setAttribute(name, newValue);
/*     */             }
/*     */           }
/*     */         
/* 277 */         } catch (IllegalStateException illegalStateException) {}
/*     */       }
/*     */ 
/*     */       
/* 281 */       this.sessionAttributesToUpdate.clear();
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
/*     */   protected boolean isImmutableSessionAttribute(String name, Object value) {
/* 298 */     return (value == null || immutableValueTypes.contains(value.getClass()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerSessionDestructionCallback(String name, Runnable callback) {
/* 309 */     HttpSession session = getSession(true);
/* 310 */     session.setAttribute(DESTRUCTION_CALLBACK_NAME_PREFIX + name, new DestructionCallbackBindingListener(callback));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 317 */     return this.request.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\ServletRequestAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */