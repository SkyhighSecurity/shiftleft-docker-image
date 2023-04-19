/*     */ package org.springframework.remoting.support;
/*     */ 
/*     */ import com.sun.net.httpserver.Authenticator;
/*     */ import com.sun.net.httpserver.Filter;
/*     */ import com.sun.net.httpserver.HttpContext;
/*     */ import com.sun.net.httpserver.HttpHandler;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ public class SimpleHttpServerFactoryBean
/*     */   implements FactoryBean<HttpServer>, InitializingBean, DisposableBean
/*     */ {
/*  58 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  60 */   private int port = 8080;
/*     */   
/*     */   private String hostname;
/*     */   
/*  64 */   private int backlog = -1;
/*     */   
/*  66 */   private int shutdownDelay = 0;
/*     */ 
/*     */   
/*     */   private Executor executor;
/*     */ 
/*     */   
/*     */   private Map<String, HttpHandler> contexts;
/*     */ 
/*     */   
/*     */   private List<Filter> filters;
/*     */   
/*     */   private Authenticator authenticator;
/*     */   
/*     */   private HttpServer server;
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/*  83 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHostname(String hostname) {
/*  91 */     this.hostname = hostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBacklog(int backlog) {
/*  99 */     this.backlog = backlog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShutdownDelay(int shutdownDelay) {
/* 107 */     this.shutdownDelay = shutdownDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutor(Executor executor) {
/* 115 */     this.executor = executor;
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
/*     */   
/*     */   public void setContexts(Map<String, HttpHandler> contexts) {
/* 128 */     this.contexts = contexts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilters(List<Filter> filters) {
/* 136 */     this.filters = filters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAuthenticator(Authenticator authenticator) {
/* 144 */     this.authenticator = authenticator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws IOException {
/* 150 */     InetSocketAddress address = (this.hostname != null) ? new InetSocketAddress(this.hostname, this.port) : new InetSocketAddress(this.port);
/*     */     
/* 152 */     this.server = HttpServer.create(address, this.backlog);
/* 153 */     if (this.executor != null) {
/* 154 */       this.server.setExecutor(this.executor);
/*     */     }
/* 156 */     if (this.contexts != null) {
/* 157 */       for (String key : this.contexts.keySet()) {
/* 158 */         HttpContext httpContext = this.server.createContext(key, this.contexts.get(key));
/* 159 */         if (this.filters != null) {
/* 160 */           httpContext.getFilters().addAll(this.filters);
/*     */         }
/* 162 */         if (this.authenticator != null) {
/* 163 */           httpContext.setAuthenticator(this.authenticator);
/*     */         }
/*     */       } 
/*     */     }
/* 167 */     if (this.logger.isInfoEnabled()) {
/* 168 */       this.logger.info("Starting HttpServer at address " + address);
/*     */     }
/* 170 */     this.server.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpServer getObject() {
/* 175 */     return this.server;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends HttpServer> getObjectType() {
/* 180 */     return (this.server != null) ? (Class)this.server.getClass() : HttpServer.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 185 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 190 */     this.logger.info("Stopping HttpServer");
/* 191 */     this.server.stop(this.shutdownDelay);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\support\SimpleHttpServerFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */