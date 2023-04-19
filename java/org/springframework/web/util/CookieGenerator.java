/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ 
/*     */ public class CookieGenerator
/*     */ {
/*     */   public static final String DEFAULT_COOKIE_PATH = "/";
/*  50 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private String cookieName;
/*     */   
/*     */   private String cookieDomain;
/*     */   
/*  56 */   private String cookiePath = "/";
/*     */ 
/*     */   
/*     */   private Integer cookieMaxAge;
/*     */ 
/*     */   
/*     */   private boolean cookieSecure = false;
/*     */ 
/*     */   
/*     */   private boolean cookieHttpOnly = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieName(String cookieName) {
/*  70 */     this.cookieName = cookieName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCookieName() {
/*  77 */     return this.cookieName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieDomain(String cookieDomain) {
/*  86 */     this.cookieDomain = cookieDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCookieDomain() {
/*  93 */     return this.cookieDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookiePath(String cookiePath) {
/* 102 */     this.cookiePath = cookiePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCookiePath() {
/* 109 */     return this.cookiePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieMaxAge(Integer cookieMaxAge) {
/* 120 */     this.cookieMaxAge = cookieMaxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getCookieMaxAge() {
/* 127 */     return this.cookieMaxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieSecure(boolean cookieSecure) {
/* 138 */     this.cookieSecure = cookieSecure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCookieSecure() {
/* 146 */     return this.cookieSecure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieHttpOnly(boolean cookieHttpOnly) {
/* 155 */     this.cookieHttpOnly = cookieHttpOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCookieHttpOnly() {
/* 162 */     return this.cookieHttpOnly;
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
/*     */   public void addCookie(HttpServletResponse response, String cookieValue) {
/* 178 */     Assert.notNull(response, "HttpServletResponse must not be null");
/* 179 */     Cookie cookie = createCookie(cookieValue);
/* 180 */     Integer maxAge = getCookieMaxAge();
/* 181 */     if (maxAge != null) {
/* 182 */       cookie.setMaxAge(maxAge.intValue());
/*     */     }
/* 184 */     if (isCookieSecure()) {
/* 185 */       cookie.setSecure(true);
/*     */     }
/* 187 */     if (isCookieHttpOnly()) {
/* 188 */       cookie.setHttpOnly(true);
/*     */     }
/* 190 */     response.addCookie(cookie);
/* 191 */     if (this.logger.isDebugEnabled()) {
/* 192 */       this.logger.debug("Added cookie with name [" + getCookieName() + "] and value [" + cookieValue + "]");
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
/*     */   public void removeCookie(HttpServletResponse response) {
/* 206 */     Assert.notNull(response, "HttpServletResponse must not be null");
/* 207 */     Cookie cookie = createCookie("");
/* 208 */     cookie.setMaxAge(0);
/* 209 */     if (isCookieSecure()) {
/* 210 */       cookie.setSecure(true);
/*     */     }
/* 212 */     if (isCookieHttpOnly()) {
/* 213 */       cookie.setHttpOnly(true);
/*     */     }
/* 215 */     response.addCookie(cookie);
/* 216 */     if (this.logger.isDebugEnabled()) {
/* 217 */       this.logger.debug("Removed cookie with name [" + getCookieName() + "]");
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
/*     */   protected Cookie createCookie(String cookieValue) {
/* 231 */     Cookie cookie = new Cookie(getCookieName(), cookieValue);
/* 232 */     if (getCookieDomain() != null) {
/* 233 */       cookie.setDomain(getCookieDomain());
/*     */     }
/* 235 */     cookie.setPath(getCookiePath());
/* 236 */     return cookie;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\CookieGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */