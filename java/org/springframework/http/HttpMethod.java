/*    */ package org.springframework.http;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public enum HttpMethod
/*    */ {
/* 33 */   GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
/*    */   
/*    */   static {
/* 36 */     mappings = new HashMap<String, HttpMethod>(16);
/*    */ 
/*    */     
/* 39 */     for (HttpMethod httpMethod : values()) {
/* 40 */       mappings.put(httpMethod.name(), httpMethod);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final Map<String, HttpMethod> mappings;
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpMethod resolve(String method) {
/* 52 */     return (method != null) ? mappings.get(method) : null;
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
/*    */   public boolean matches(String method) {
/* 64 */     return (this == resolve(method));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\HttpMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */