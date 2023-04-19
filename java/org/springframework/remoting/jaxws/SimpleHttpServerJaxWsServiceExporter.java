/*     */ package org.springframework.remoting.jaxws;
/*     */ 
/*     */ import com.sun.net.httpserver.Authenticator;
/*     */ import com.sun.net.httpserver.Filter;
/*     */ import com.sun.net.httpserver.HttpContext;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.List;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.ws.Endpoint;
/*     */ import javax.xml.ws.WebServiceProvider;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.UsesSunHttpServer;
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
/*     */ @UsesSunHttpServer
/*     */ public class SimpleHttpServerJaxWsServiceExporter
/*     */   extends AbstractJaxWsServiceExporter
/*     */ {
/*  54 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private HttpServer server;
/*     */   
/*  58 */   private int port = 8080;
/*     */   
/*     */   private String hostname;
/*     */   
/*  62 */   private int backlog = -1;
/*     */   
/*  64 */   private int shutdownDelay = 0;
/*     */   
/*  66 */   private String basePath = "/";
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Filter> filters;
/*     */ 
/*     */ 
/*     */   
/*     */   private Authenticator authenticator;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean localServer = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServer(HttpServer server) {
/*  84 */     this.server = server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/*  93 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHostname(String hostname) {
/* 103 */     this.hostname = hostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBacklog(int backlog) {
/* 113 */     this.backlog = backlog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShutdownDelay(int shutdownDelay) {
/* 123 */     this.shutdownDelay = shutdownDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBasePath(String basePath) {
/* 135 */     this.basePath = basePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilters(List<Filter> filters) {
/* 143 */     this.filters = filters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAuthenticator(Authenticator authenticator) {
/* 151 */     this.authenticator = authenticator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 157 */     if (this.server == null) {
/* 158 */       InetSocketAddress address = (this.hostname != null) ? new InetSocketAddress(this.hostname, this.port) : new InetSocketAddress(this.port);
/*     */       
/* 160 */       HttpServer server = HttpServer.create(address, this.backlog);
/* 161 */       if (this.logger.isInfoEnabled()) {
/* 162 */         this.logger.info("Starting HttpServer at address " + address);
/*     */       }
/* 164 */       server.start();
/* 165 */       this.server = server;
/* 166 */       this.localServer = true;
/*     */     } 
/* 168 */     super.afterPropertiesSet();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void publishEndpoint(Endpoint endpoint, WebService annotation) {
/* 173 */     endpoint.publish(buildHttpContext(endpoint, annotation.serviceName()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void publishEndpoint(Endpoint endpoint, WebServiceProvider annotation) {
/* 178 */     endpoint.publish(buildHttpContext(endpoint, annotation.serviceName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpContext buildHttpContext(Endpoint endpoint, String serviceName) {
/* 188 */     String fullPath = calculateEndpointPath(endpoint, serviceName);
/* 189 */     HttpContext httpContext = this.server.createContext(fullPath);
/* 190 */     if (this.filters != null) {
/* 191 */       httpContext.getFilters().addAll(this.filters);
/*     */     }
/* 193 */     if (this.authenticator != null) {
/* 194 */       httpContext.setAuthenticator(this.authenticator);
/*     */     }
/* 196 */     return httpContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String calculateEndpointPath(Endpoint endpoint, String serviceName) {
/* 206 */     return this.basePath + serviceName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 212 */     super.destroy();
/* 213 */     if (this.localServer) {
/* 214 */       this.logger.info("Stopping HttpServer");
/* 215 */       this.server.stop(this.shutdownDelay);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\jaxws\SimpleHttpServerJaxWsServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */