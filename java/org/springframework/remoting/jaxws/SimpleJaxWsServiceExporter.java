/*    */ package org.springframework.remoting.jaxws;
/*    */ 
/*    */ import javax.jws.WebService;
/*    */ import javax.xml.ws.Endpoint;
/*    */ import javax.xml.ws.WebServiceProvider;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleJaxWsServiceExporter
/*    */   extends AbstractJaxWsServiceExporter
/*    */ {
/*    */   public static final String DEFAULT_BASE_ADDRESS = "http://localhost:8080/";
/* 49 */   private String baseAddress = "http://localhost:8080/";
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
/*    */   public void setBaseAddress(String baseAddress) {
/* 62 */     this.baseAddress = baseAddress;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void publishEndpoint(Endpoint endpoint, WebService annotation) {
/* 68 */     endpoint.publish(calculateEndpointAddress(endpoint, annotation.serviceName()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void publishEndpoint(Endpoint endpoint, WebServiceProvider annotation) {
/* 73 */     endpoint.publish(calculateEndpointAddress(endpoint, annotation.serviceName()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String calculateEndpointAddress(Endpoint endpoint, String serviceName) {
/* 83 */     String fullAddress = this.baseAddress + serviceName;
/* 84 */     if (endpoint.getClass().getName().startsWith("weblogic."))
/*    */     {
/* 86 */       fullAddress = fullAddress + "/";
/*    */     }
/* 88 */     return fullAddress;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\jaxws\SimpleJaxWsServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */