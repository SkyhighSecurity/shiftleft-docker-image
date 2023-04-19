/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.http.client.AsyncClientHttpRequest;
/*    */ import org.springframework.http.client.AsyncClientHttpRequestFactory;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ public class AsyncHttpAccessor
/*    */ {
/* 45 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */   
/*    */   private AsyncClientHttpRequestFactory asyncRequestFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsyncRequestFactory(AsyncClientHttpRequestFactory asyncRequestFactory) {
/* 55 */     Assert.notNull(asyncRequestFactory, "AsyncClientHttpRequestFactory must not be null");
/* 56 */     this.asyncRequestFactory = asyncRequestFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncClientHttpRequestFactory getAsyncRequestFactory() {
/* 64 */     return this.asyncRequestFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AsyncClientHttpRequest createAsyncRequest(URI url, HttpMethod method) throws IOException {
/* 76 */     AsyncClientHttpRequest request = getAsyncRequestFactory().createAsyncRequest(url, method);
/* 77 */     if (this.logger.isDebugEnabled()) {
/* 78 */       this.logger.debug("Created asynchronous " + method.name() + " request for \"" + url + "\"");
/*    */     }
/* 80 */     return request;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\support\AsyncHttpAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */