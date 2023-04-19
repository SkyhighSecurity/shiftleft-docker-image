/*     */ package org.springframework.remoting.jaxws;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.handler.HandlerResolver;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class LocalJaxWsServiceFactory
/*     */ {
/*     */   private URL wsdlDocumentUrl;
/*     */   private String namespaceUri;
/*     */   private String serviceName;
/*     */   private WebServiceFeature[] serviceFeatures;
/*     */   private Executor executor;
/*     */   private HandlerResolver handlerResolver;
/*     */   
/*     */   public void setWsdlDocumentUrl(URL wsdlDocumentUrl) {
/*  65 */     this.wsdlDocumentUrl = wsdlDocumentUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWsdlDocumentResource(Resource wsdlDocumentResource) throws IOException {
/*  73 */     Assert.notNull(wsdlDocumentResource, "WSDL Resource must not be null.");
/*  74 */     this.wsdlDocumentUrl = wsdlDocumentResource.getURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getWsdlDocumentUrl() {
/*  81 */     return this.wsdlDocumentUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespaceUri(String namespaceUri) {
/*  89 */     this.namespaceUri = (namespaceUri != null) ? namespaceUri.trim() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceUri() {
/*  96 */     return this.namespaceUri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceName(String serviceName) {
/* 104 */     this.serviceName = serviceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServiceName() {
/* 111 */     return this.serviceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceFeatures(WebServiceFeature... serviceFeatures) {
/* 122 */     this.serviceFeatures = serviceFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutor(Executor executor) {
/* 131 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerResolver(HandlerResolver handlerResolver) {
/* 140 */     this.handlerResolver = handlerResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UsesJava7
/*     */   public Service createJaxWsService() {
/*     */     Service service;
/* 151 */     Assert.notNull(this.serviceName, "No service name specified");
/*     */ 
/*     */     
/* 154 */     if (this.serviceFeatures != null) {
/*     */ 
/*     */       
/* 157 */       service = (this.wsdlDocumentUrl != null) ? Service.create(this.wsdlDocumentUrl, getQName(this.serviceName), this.serviceFeatures) : Service.create(getQName(this.serviceName), this.serviceFeatures);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 162 */       service = (this.wsdlDocumentUrl != null) ? Service.create(this.wsdlDocumentUrl, getQName(this.serviceName)) : Service.create(getQName(this.serviceName));
/*     */     } 
/*     */     
/* 165 */     if (this.executor != null) {
/* 166 */       service.setExecutor(this.executor);
/*     */     }
/* 168 */     if (this.handlerResolver != null) {
/* 169 */       service.setHandlerResolver(this.handlerResolver);
/*     */     }
/*     */     
/* 172 */     return service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName getQName(String name) {
/* 181 */     return (getNamespaceUri() != null) ? new QName(getNamespaceUri(), name) : new QName(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\jaxws\LocalJaxWsServiceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */