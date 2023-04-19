/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpEntity<T>
/*     */ {
/*  61 */   public static final HttpEntity<?> EMPTY = new HttpEntity();
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpHeaders headers;
/*     */ 
/*     */   
/*     */   private final T body;
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpEntity() {
/*  73 */     this(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntity(T body) {
/*  81 */     this(body, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntity(MultiValueMap<String, String> headers) {
/*  89 */     this(null, headers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntity(T body, MultiValueMap<String, String> headers) {
/*  98 */     this.body = body;
/*  99 */     HttpHeaders tempHeaders = new HttpHeaders();
/* 100 */     if (headers != null) {
/* 101 */       tempHeaders.putAll((Map)headers);
/*     */     }
/* 103 */     this.headers = HttpHeaders.readOnlyHttpHeaders(tempHeaders);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/* 111 */     return this.headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getBody() {
/* 118 */     return this.body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBody() {
/* 125 */     return (this.body != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 131 */     if (this == other) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (other == null || other.getClass() != getClass()) {
/* 135 */       return false;
/*     */     }
/* 137 */     HttpEntity<?> otherEntity = (HttpEntity)other;
/* 138 */     return (ObjectUtils.nullSafeEquals(this.headers, otherEntity.headers) && 
/* 139 */       ObjectUtils.nullSafeEquals(this.body, otherEntity.body));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 144 */     return ObjectUtils.nullSafeHashCode(this.headers) * 29 + ObjectUtils.nullSafeHashCode(this.body);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     StringBuilder builder = new StringBuilder("<");
/* 150 */     if (this.body != null) {
/* 151 */       builder.append(this.body);
/* 152 */       if (this.headers != null) {
/* 153 */         builder.append(',');
/*     */       }
/*     */     } 
/* 156 */     if (this.headers != null) {
/* 157 */       builder.append(this.headers);
/*     */     }
/* 159 */     builder.append('>');
/* 160 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\HttpEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */