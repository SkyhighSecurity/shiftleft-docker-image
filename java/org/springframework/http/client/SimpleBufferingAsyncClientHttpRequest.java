/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.concurrent.Callable;
/*    */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.util.FileCopyUtils;
/*    */ import org.springframework.util.concurrent.ListenableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class SimpleBufferingAsyncClientHttpRequest
/*    */   extends AbstractBufferingAsyncClientHttpRequest
/*    */ {
/*    */   private final HttpURLConnection connection;
/*    */   private final boolean outputStreaming;
/*    */   private final AsyncListenableTaskExecutor taskExecutor;
/*    */   
/*    */   SimpleBufferingAsyncClientHttpRequest(HttpURLConnection connection, boolean outputStreaming, AsyncListenableTaskExecutor taskExecutor) {
/* 52 */     this.connection = connection;
/* 53 */     this.outputStreaming = outputStreaming;
/* 54 */     this.taskExecutor = taskExecutor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMethod getMethod() {
/* 60 */     return HttpMethod.resolve(this.connection.getRequestMethod());
/*    */   }
/*    */ 
/*    */   
/*    */   public URI getURI() {
/*    */     try {
/* 66 */       return this.connection.getURL().toURI();
/*    */     }
/* 68 */     catch (URISyntaxException ex) {
/* 69 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ListenableFuture<ClientHttpResponse> executeInternal(final HttpHeaders headers, final byte[] bufferedOutput) throws IOException {
/* 77 */     return this.taskExecutor.submitListenable(new Callable<ClientHttpResponse>()
/*    */         {
/*    */           public ClientHttpResponse call() throws Exception {
/* 80 */             SimpleBufferingClientHttpRequest.addHeaders(SimpleBufferingAsyncClientHttpRequest.this.connection, headers);
/*    */             
/* 82 */             if (SimpleBufferingAsyncClientHttpRequest.this.getMethod() == HttpMethod.DELETE && bufferedOutput.length == 0) {
/* 83 */               SimpleBufferingAsyncClientHttpRequest.this.connection.setDoOutput(false);
/*    */             }
/* 85 */             if (SimpleBufferingAsyncClientHttpRequest.this.connection.getDoOutput() && SimpleBufferingAsyncClientHttpRequest.this.outputStreaming) {
/* 86 */               SimpleBufferingAsyncClientHttpRequest.this.connection.setFixedLengthStreamingMode(bufferedOutput.length);
/*    */             }
/* 88 */             SimpleBufferingAsyncClientHttpRequest.this.connection.connect();
/* 89 */             if (SimpleBufferingAsyncClientHttpRequest.this.connection.getDoOutput()) {
/* 90 */               FileCopyUtils.copy(bufferedOutput, SimpleBufferingAsyncClientHttpRequest.this.connection.getOutputStream());
/*    */             }
/*    */             else {
/*    */               
/* 94 */               SimpleBufferingAsyncClientHttpRequest.this.connection.getResponseCode();
/*    */             } 
/* 96 */             return new SimpleClientHttpResponse(SimpleBufferingAsyncClientHttpRequest.this.connection);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\SimpleBufferingAsyncClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */