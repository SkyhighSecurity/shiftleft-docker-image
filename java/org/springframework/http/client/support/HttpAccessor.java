/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.http.client.ClientHttpRequest;
/*    */ import org.springframework.http.client.ClientHttpRequestFactory;
/*    */ import org.springframework.http.client.SimpleClientHttpRequestFactory;
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
/*    */ 
/*    */ public abstract class HttpAccessor
/*    */ {
/* 47 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/* 49 */   private ClientHttpRequestFactory requestFactory = (ClientHttpRequestFactory)new SimpleClientHttpRequestFactory();
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
/*    */   public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
/* 63 */     Assert.notNull(requestFactory, "ClientHttpRequestFactory must not be null");
/* 64 */     this.requestFactory = requestFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientHttpRequestFactory getRequestFactory() {
/* 71 */     return this.requestFactory;
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
/*    */ 
/*    */   
/*    */   protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
/* 85 */     ClientHttpRequest request = getRequestFactory().createRequest(url, method);
/* 86 */     if (this.logger.isDebugEnabled()) {
/* 87 */       this.logger.debug("Created " + method.name() + " request for \"" + url + "\"");
/*    */     }
/* 89 */     return request;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\support\HttpAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */