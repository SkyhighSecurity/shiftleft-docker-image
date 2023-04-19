/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.Charset;
/*    */ import org.springframework.http.HttpRequest;
/*    */ import org.springframework.http.client.ClientHttpRequestExecution;
/*    */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*    */ import org.springframework.http.client.ClientHttpResponse;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.Base64Utils;
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
/*    */ public class BasicAuthorizationInterceptor
/*    */   implements ClientHttpRequestInterceptor
/*    */ {
/* 37 */   private static final Charset UTF_8 = Charset.forName("UTF-8");
/*    */ 
/*    */ 
/*    */   
/*    */   private final String username;
/*    */ 
/*    */ 
/*    */   
/*    */   private final String password;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BasicAuthorizationInterceptor(String username, String password) {
/* 51 */     Assert.hasLength(username, "Username must not be empty");
/* 52 */     this.username = username;
/* 53 */     this.password = (password != null) ? password : "";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
/* 61 */     String token = Base64Utils.encodeToString((this.username + ":" + this.password).getBytes(UTF_8));
/* 62 */     request.getHeaders().add("Authorization", "Basic " + token);
/* 63 */     return execution.execute(request, body);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\support\BasicAuthorizationInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */