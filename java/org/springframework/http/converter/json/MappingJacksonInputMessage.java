/*    */ package org.springframework.http.converter.json;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpInputMessage;
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
/*    */ public class MappingJacksonInputMessage
/*    */   implements HttpInputMessage
/*    */ {
/*    */   private final InputStream body;
/*    */   private final HttpHeaders headers;
/*    */   private Class<?> deserializationView;
/*    */   
/*    */   public MappingJacksonInputMessage(InputStream body, HttpHeaders headers) {
/* 42 */     this.body = body;
/* 43 */     this.headers = headers;
/*    */   }
/*    */   
/*    */   public MappingJacksonInputMessage(InputStream body, HttpHeaders headers, Class<?> deserializationView) {
/* 47 */     this(body, headers);
/* 48 */     this.deserializationView = deserializationView;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 54 */     return this.body;
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 59 */     return this.headers;
/*    */   }
/*    */   
/*    */   public void setDeserializationView(Class<?> deserializationView) {
/* 63 */     this.deserializationView = deserializationView;
/*    */   }
/*    */   
/*    */   public Class<?> getDeserializationView() {
/* 67 */     return this.deserializationView;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\json\MappingJacksonInputMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */