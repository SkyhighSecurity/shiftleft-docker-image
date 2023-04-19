/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RestClientResponseException
/*     */   extends RestClientException
/*     */ {
/*     */   private static final long serialVersionUID = -8803556342728481792L;
/*     */   private static final String DEFAULT_CHARSET = "ISO-8859-1";
/*     */   private final int rawStatusCode;
/*     */   private final String statusText;
/*     */   private final byte[] responseBody;
/*     */   private final HttpHeaders responseHeaders;
/*     */   private final String responseCharset;
/*     */   
/*     */   public RestClientResponseException(String message, int statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
/*  59 */     super(message);
/*  60 */     this.rawStatusCode = statusCode;
/*  61 */     this.statusText = statusText;
/*  62 */     this.responseHeaders = responseHeaders;
/*  63 */     this.responseBody = (responseBody != null) ? responseBody : new byte[0];
/*  64 */     this.responseCharset = (responseCharset != null) ? responseCharset.name() : "ISO-8859-1";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRawStatusCode() {
/*  72 */     return this.rawStatusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatusText() {
/*  79 */     return this.statusText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders getResponseHeaders() {
/*  86 */     return this.responseHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getResponseBodyAsByteArray() {
/*  93 */     return this.responseBody;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResponseBodyAsString() {
/*     */     try {
/* 101 */       return new String(this.responseBody, this.responseCharset);
/*     */     }
/* 103 */     catch (UnsupportedEncodingException ex) {
/*     */       
/* 105 */       throw new IllegalStateException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\RestClientResponseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */