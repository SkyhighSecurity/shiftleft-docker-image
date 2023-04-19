/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ import java.net.SocketAddress;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public class ProxyFactoryBean
/*    */   implements FactoryBean<Proxy>, InitializingBean
/*    */ {
/* 37 */   private Proxy.Type type = Proxy.Type.HTTP;
/*    */   
/*    */   private String hostname;
/*    */   
/* 41 */   private int port = -1;
/*    */ 
/*    */ 
/*    */   
/*    */   private Proxy proxy;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setType(Proxy.Type type) {
/* 51 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setHostname(String hostname) {
/* 58 */     this.hostname = hostname;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPort(int port) {
/* 65 */     this.port = port;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() throws IllegalArgumentException {
/* 71 */     Assert.notNull(this.type, "'type' must not be null");
/* 72 */     Assert.hasLength(this.hostname, "'hostname' must not be empty");
/* 73 */     if (this.port < 0 || this.port > 65535) {
/* 74 */       throw new IllegalArgumentException("'port' value out of range: " + this.port);
/*    */     }
/*    */     
/* 77 */     SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
/* 78 */     this.proxy = new Proxy(this.type, socketAddress);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Proxy getObject() {
/* 84 */     return this.proxy;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 89 */     return Proxy.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 94 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\support\ProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */