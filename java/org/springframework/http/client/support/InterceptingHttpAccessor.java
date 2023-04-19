/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.http.client.ClientHttpRequestFactory;
/*    */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*    */ import org.springframework.http.client.InterceptingClientHttpRequestFactory;
/*    */ import org.springframework.util.CollectionUtils;
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
/*    */ public abstract class InterceptingHttpAccessor
/*    */   extends HttpAccessor
/*    */ {
/* 37 */   private List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
/* 43 */     this.interceptors = interceptors;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ClientHttpRequestInterceptor> getInterceptors() {
/* 50 */     return this.interceptors;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClientHttpRequestFactory getRequestFactory() {
/* 55 */     ClientHttpRequestFactory delegate = super.getRequestFactory();
/* 56 */     if (!CollectionUtils.isEmpty(getInterceptors())) {
/* 57 */       return (ClientHttpRequestFactory)new InterceptingClientHttpRequestFactory(delegate, getInterceptors());
/*    */     }
/*    */     
/* 60 */     return delegate;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\support\InterceptingHttpAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */