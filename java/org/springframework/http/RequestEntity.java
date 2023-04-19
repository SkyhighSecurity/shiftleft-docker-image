/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URI;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestEntity<T>
/*     */   extends HttpEntity<T>
/*     */ {
/*     */   private final HttpMethod method;
/*     */   private final URI url;
/*     */   private final Type type;
/*     */   
/*     */   public RequestEntity(HttpMethod method, URI url) {
/*  78 */     this((T)null, (MultiValueMap<String, String>)null, method, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestEntity(T body, HttpMethod method, URI url) {
/*  88 */     this(body, (MultiValueMap<String, String>)null, method, url, (Type)null);
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
/*     */   public RequestEntity(T body, HttpMethod method, URI url, Type type) {
/* 100 */     this(body, (MultiValueMap<String, String>)null, method, url, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestEntity(MultiValueMap<String, String> headers, HttpMethod method, URI url) {
/* 110 */     this((T)null, headers, method, url, (Type)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestEntity(T body, MultiValueMap<String, String> headers, HttpMethod method, URI url) {
/* 121 */     this(body, headers, method, url, (Type)null);
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
/*     */   public RequestEntity(T body, MultiValueMap<String, String> headers, HttpMethod method, URI url, Type type) {
/* 134 */     super(body, headers);
/* 135 */     this.method = method;
/* 136 */     this.url = url;
/* 137 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/* 146 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getUrl() {
/* 154 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/* 163 */     if (this.type == null) {
/* 164 */       T body = getBody();
/* 165 */       if (body != null) {
/* 166 */         return body.getClass();
/*     */       }
/*     */     } 
/* 169 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 175 */     if (this == other) {
/* 176 */       return true;
/*     */     }
/* 178 */     if (!super.equals(other)) {
/* 179 */       return false;
/*     */     }
/* 181 */     RequestEntity<?> otherEntity = (RequestEntity)other;
/* 182 */     return (ObjectUtils.nullSafeEquals(getMethod(), otherEntity.getMethod()) && 
/* 183 */       ObjectUtils.nullSafeEquals(getUrl(), otherEntity.getUrl()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 188 */     int hashCode = super.hashCode();
/* 189 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.method);
/* 190 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.url);
/* 191 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 196 */     StringBuilder builder = new StringBuilder("<");
/* 197 */     builder.append(getMethod());
/* 198 */     builder.append(' ');
/* 199 */     builder.append(getUrl());
/* 200 */     builder.append(',');
/* 201 */     T body = getBody();
/* 202 */     HttpHeaders headers = getHeaders();
/* 203 */     if (body != null) {
/* 204 */       builder.append(body);
/* 205 */       if (headers != null) {
/* 206 */         builder.append(',');
/*     */       }
/*     */     } 
/* 209 */     if (headers != null) {
/* 210 */       builder.append(headers);
/*     */     }
/* 212 */     builder.append('>');
/* 213 */     return builder.toString();
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
/*     */   public static BodyBuilder method(HttpMethod method, URI url) {
/* 226 */     return new DefaultBodyBuilder(method, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> get(URI url) {
/* 235 */     return method(HttpMethod.GET, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> head(URI url) {
/* 244 */     return method(HttpMethod.HEAD, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder post(URI url) {
/* 253 */     return method(HttpMethod.POST, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder put(URI url) {
/* 262 */     return method(HttpMethod.PUT, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BodyBuilder patch(URI url) {
/* 271 */     return method(HttpMethod.PATCH, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> delete(URI url) {
/* 280 */     return method(HttpMethod.DELETE, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HeadersBuilder<?> options(URI url) {
/* 289 */     return method(HttpMethod.OPTIONS, url);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DefaultBodyBuilder
/*     */     implements BodyBuilder
/*     */   {
/*     */     private final HttpMethod method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final URI url;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 394 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/*     */     public DefaultBodyBuilder(HttpMethod method, URI url) {
/* 397 */       this.method = method;
/* 398 */       this.url = url;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder header(String headerName, String... headerValues) {
/* 403 */       for (String headerValue : headerValues) {
/* 404 */         this.headers.add(headerName, headerValue);
/*     */       }
/* 406 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder accept(MediaType... acceptableMediaTypes) {
/* 411 */       this.headers.setAccept(Arrays.asList(acceptableMediaTypes));
/* 412 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder acceptCharset(Charset... acceptableCharsets) {
/* 417 */       this.headers.setAcceptCharset(Arrays.asList(acceptableCharsets));
/* 418 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder contentLength(long contentLength) {
/* 423 */       this.headers.setContentLength(contentLength);
/* 424 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder contentType(MediaType contentType) {
/* 429 */       this.headers.setContentType(contentType);
/* 430 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder ifModifiedSince(long ifModifiedSince) {
/* 435 */       this.headers.setIfModifiedSince(ifModifiedSince);
/* 436 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity.BodyBuilder ifNoneMatch(String... ifNoneMatches) {
/* 441 */       this.headers.setIfNoneMatch(Arrays.asList(ifNoneMatches));
/* 442 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RequestEntity<Void> build() {
/* 447 */       return new RequestEntity<Void>(this.headers, this.method, this.url);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> RequestEntity<T> body(T body) {
/* 452 */       return new RequestEntity<T>(body, this.headers, this.method, this.url);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> RequestEntity<T> body(T body, Type type) {
/* 457 */       return new RequestEntity<T>(body, this.headers, this.method, this.url, type);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface BodyBuilder extends HeadersBuilder<BodyBuilder> {
/*     */     BodyBuilder contentLength(long param1Long);
/*     */     
/*     */     BodyBuilder contentType(MediaType param1MediaType);
/*     */     
/*     */     <T> RequestEntity<T> body(T param1T);
/*     */     
/*     */     <T> RequestEntity<T> body(T param1T, Type param1Type);
/*     */   }
/*     */   
/*     */   public static interface HeadersBuilder<B extends HeadersBuilder<B>> {
/*     */     B header(String param1String, String... param1VarArgs);
/*     */     
/*     */     B accept(MediaType... param1VarArgs);
/*     */     
/*     */     B acceptCharset(Charset... param1VarArgs);
/*     */     
/*     */     B ifModifiedSince(long param1Long);
/*     */     
/*     */     B ifNoneMatch(String... param1VarArgs);
/*     */     
/*     */     RequestEntity<Void> build();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\RequestEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */