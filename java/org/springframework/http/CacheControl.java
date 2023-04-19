/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CacheControl
/*     */ {
/*  52 */   private long maxAge = -1L;
/*     */   
/*     */   private boolean noCache = false;
/*     */   
/*     */   private boolean noStore = false;
/*     */   
/*     */   private boolean mustRevalidate = false;
/*     */   
/*     */   private boolean noTransform = false;
/*     */   
/*     */   private boolean cachePublic = false;
/*     */   
/*     */   private boolean cachePrivate = false;
/*     */   
/*     */   private boolean proxyRevalidate = false;
/*     */   
/*  68 */   private long staleWhileRevalidate = -1L;
/*     */   
/*  70 */   private long staleIfError = -1L;
/*     */   
/*  72 */   private long sMaxAge = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CacheControl empty() {
/*  90 */     return new CacheControl();
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
/*     */   public static CacheControl maxAge(long maxAge, TimeUnit unit) {
/* 108 */     CacheControl cc = new CacheControl();
/* 109 */     cc.maxAge = unit.toSeconds(maxAge);
/* 110 */     return cc;
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
/*     */   public static CacheControl noCache() {
/* 126 */     CacheControl cc = new CacheControl();
/* 127 */     cc.noCache = true;
/* 128 */     return cc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CacheControl noStore() {
/* 139 */     CacheControl cc = new CacheControl();
/* 140 */     cc.noStore = true;
/* 141 */     return cc;
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
/*     */   public CacheControl mustRevalidate() {
/* 154 */     this.mustRevalidate = true;
/* 155 */     return this;
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
/*     */   public CacheControl noTransform() {
/* 167 */     this.noTransform = true;
/* 168 */     return this;
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
/*     */   public CacheControl cachePublic() {
/* 180 */     this.cachePublic = true;
/* 181 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheControl cachePrivate() {
/* 192 */     this.cachePrivate = true;
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheControl proxyRevalidate() {
/* 204 */     this.proxyRevalidate = true;
/* 205 */     return this;
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
/*     */   public CacheControl sMaxAge(long sMaxAge, TimeUnit unit) {
/* 218 */     this.sMaxAge = unit.toSeconds(sMaxAge);
/* 219 */     return this;
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
/*     */   public CacheControl staleWhileRevalidate(long staleWhileRevalidate, TimeUnit unit) {
/* 235 */     this.staleWhileRevalidate = unit.toSeconds(staleWhileRevalidate);
/* 236 */     return this;
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
/*     */   public CacheControl staleIfError(long staleIfError, TimeUnit unit) {
/* 249 */     this.staleIfError = unit.toSeconds(staleIfError);
/* 250 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHeaderValue() {
/* 259 */     StringBuilder ccValue = new StringBuilder();
/* 260 */     if (this.maxAge != -1L) {
/* 261 */       appendDirective(ccValue, "max-age=" + Long.toString(this.maxAge));
/*     */     }
/* 263 */     if (this.noCache) {
/* 264 */       appendDirective(ccValue, "no-cache");
/*     */     }
/* 266 */     if (this.noStore) {
/* 267 */       appendDirective(ccValue, "no-store");
/*     */     }
/* 269 */     if (this.mustRevalidate) {
/* 270 */       appendDirective(ccValue, "must-revalidate");
/*     */     }
/* 272 */     if (this.noTransform) {
/* 273 */       appendDirective(ccValue, "no-transform");
/*     */     }
/* 275 */     if (this.cachePublic) {
/* 276 */       appendDirective(ccValue, "public");
/*     */     }
/* 278 */     if (this.cachePrivate) {
/* 279 */       appendDirective(ccValue, "private");
/*     */     }
/* 281 */     if (this.proxyRevalidate) {
/* 282 */       appendDirective(ccValue, "proxy-revalidate");
/*     */     }
/* 284 */     if (this.sMaxAge != -1L) {
/* 285 */       appendDirective(ccValue, "s-maxage=" + Long.toString(this.sMaxAge));
/*     */     }
/* 287 */     if (this.staleIfError != -1L) {
/* 288 */       appendDirective(ccValue, "stale-if-error=" + Long.toString(this.staleIfError));
/*     */     }
/* 290 */     if (this.staleWhileRevalidate != -1L) {
/* 291 */       appendDirective(ccValue, "stale-while-revalidate=" + Long.toString(this.staleWhileRevalidate));
/*     */     }
/*     */     
/* 294 */     String ccHeaderValue = ccValue.toString();
/* 295 */     return StringUtils.hasText(ccHeaderValue) ? ccHeaderValue : null;
/*     */   }
/*     */   
/*     */   private void appendDirective(StringBuilder builder, String value) {
/* 299 */     if (builder.length() > 0) {
/* 300 */       builder.append(", ");
/*     */     }
/* 302 */     builder.append(value);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\CacheControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */