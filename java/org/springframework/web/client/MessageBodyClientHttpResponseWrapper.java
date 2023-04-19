/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MessageBodyClientHttpResponseWrapper
/*     */   implements ClientHttpResponse
/*     */ {
/*     */   private final ClientHttpResponse response;
/*     */   private PushbackInputStream pushbackInputStream;
/*     */   
/*     */   public MessageBodyClientHttpResponseWrapper(ClientHttpResponse response) throws IOException {
/*  44 */     this.response = response;
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
/*     */   public boolean hasMessageBody() throws IOException {
/*     */     try {
/*  60 */       HttpStatus status = getStatusCode();
/*  61 */       if ((status != null && status.is1xxInformational()) || status == HttpStatus.NO_CONTENT || status == HttpStatus.NOT_MODIFIED)
/*     */       {
/*  63 */         return false;
/*     */       }
/*     */     }
/*  66 */     catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */     
/*  69 */     if (getHeaders().getContentLength() == 0L) {
/*  70 */       return false;
/*     */     }
/*  72 */     return true;
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
/*     */   public boolean hasEmptyMessageBody() throws IOException {
/*  86 */     InputStream body = this.response.getBody();
/*  87 */     if (body == null) {
/*  88 */       return true;
/*     */     }
/*  90 */     if (body.markSupported()) {
/*  91 */       body.mark(1);
/*  92 */       if (body.read() == -1) {
/*  93 */         return true;
/*     */       }
/*     */       
/*  96 */       body.reset();
/*  97 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 101 */     this.pushbackInputStream = new PushbackInputStream(body);
/* 102 */     int b = this.pushbackInputStream.read();
/* 103 */     if (b == -1) {
/* 104 */       return true;
/*     */     }
/*     */     
/* 107 */     this.pushbackInputStream.unread(b);
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/* 116 */     return this.response.getHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getBody() throws IOException {
/* 121 */     return (this.pushbackInputStream != null) ? this.pushbackInputStream : this.response.getBody();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpStatus getStatusCode() throws IOException {
/* 126 */     return this.response.getStatusCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRawStatusCode() throws IOException {
/* 131 */     return this.response.getRawStatusCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStatusText() throws IOException {
/* 136 */     return this.response.getStatusText();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 141 */     this.response.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\MessageBodyClientHttpResponseWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */