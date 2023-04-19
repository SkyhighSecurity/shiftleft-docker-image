/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.http.HttpMethod;
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
/*    */ public class InterceptingClientHttpRequestFactory
/*    */   extends AbstractClientHttpRequestFactoryWrapper
/*    */ {
/*    */   private final List<ClientHttpRequestInterceptor> interceptors;
/*    */   
/*    */   public InterceptingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors) {
/* 46 */     super(requestFactory);
/* 47 */     this.interceptors = (interceptors != null) ? interceptors : Collections.<ClientHttpRequestInterceptor>emptyList();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory) {
/* 53 */     return new InterceptingClientHttpRequest(requestFactory, this.interceptors, uri, httpMethod);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\InterceptingClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */