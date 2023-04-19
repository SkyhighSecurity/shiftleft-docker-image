/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.nio.NioEventLoopGroup;
/*     */ import io.netty.channel.socket.SocketChannel;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.channel.socket.nio.NioSocketChannel;
/*     */ import io.netty.handler.codec.http.HttpClientCodec;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.ssl.SslContext;
/*     */ import io.netty.handler.ssl.SslContextBuilder;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.http.HttpMethod;
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
/*     */ public class Netty4ClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, AsyncClientHttpRequestFactory, InitializingBean, DisposableBean
/*     */ {
/*     */   public static final int DEFAULT_MAX_RESPONSE_SIZE = 10485760;
/*     */   private final EventLoopGroup eventLoopGroup;
/*     */   private final boolean defaultEventLoopGroup;
/*  74 */   private int maxResponseSize = 10485760;
/*     */   
/*     */   private SslContext sslContext;
/*     */   
/*  78 */   private int connectTimeout = -1;
/*     */   
/*  80 */   private int readTimeout = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Bootstrap bootstrap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Netty4ClientHttpRequestFactory() {
/*  90 */     int ioWorkerCount = Runtime.getRuntime().availableProcessors() * 2;
/*  91 */     this.eventLoopGroup = (EventLoopGroup)new NioEventLoopGroup(ioWorkerCount);
/*  92 */     this.defaultEventLoopGroup = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Netty4ClientHttpRequestFactory(EventLoopGroup eventLoopGroup) {
/* 103 */     Assert.notNull(eventLoopGroup, "EventLoopGroup must not be null");
/* 104 */     this.eventLoopGroup = eventLoopGroup;
/* 105 */     this.defaultEventLoopGroup = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxResponseSize(int maxResponseSize) {
/* 116 */     this.maxResponseSize = maxResponseSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSslContext(SslContext sslContext) {
/* 125 */     this.sslContext = sslContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int connectTimeout) {
/* 134 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/* 143 */     this.readTimeout = readTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 149 */     if (this.sslContext == null) {
/* 150 */       this.sslContext = getDefaultClientSslContext();
/*     */     }
/*     */   }
/*     */   
/*     */   private SslContext getDefaultClientSslContext() {
/*     */     try {
/* 156 */       return SslContextBuilder.forClient().build();
/*     */     }
/* 158 */     catch (SSLException ex) {
/* 159 */       throw new IllegalStateException("Could not create default client SslContext", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
/* 166 */     return createRequestInternal(uri, httpMethod);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
/* 171 */     return createRequestInternal(uri, httpMethod);
/*     */   }
/*     */   
/*     */   private Netty4ClientHttpRequest createRequestInternal(URI uri, HttpMethod httpMethod) {
/* 175 */     return new Netty4ClientHttpRequest(getBootstrap(uri), uri, httpMethod);
/*     */   }
/*     */   
/*     */   private Bootstrap getBootstrap(URI uri) {
/* 179 */     boolean isSecure = (uri.getPort() == 443 || "https".equalsIgnoreCase(uri.getScheme()));
/* 180 */     if (isSecure) {
/* 181 */       return buildBootstrap(uri, true);
/*     */     }
/*     */     
/* 184 */     if (this.bootstrap == null) {
/* 185 */       this.bootstrap = buildBootstrap(uri, false);
/*     */     }
/* 187 */     return this.bootstrap;
/*     */   }
/*     */ 
/*     */   
/*     */   private Bootstrap buildBootstrap(final URI uri, final boolean isSecure) {
/* 192 */     Bootstrap bootstrap = new Bootstrap();
/* 193 */     ((Bootstrap)((Bootstrap)bootstrap.group(this.eventLoopGroup)).channel(NioSocketChannel.class))
/* 194 */       .handler((ChannelHandler)new ChannelInitializer<SocketChannel>()
/*     */         {
/*     */           protected void initChannel(SocketChannel channel) throws Exception {
/* 197 */             Netty4ClientHttpRequestFactory.this.configureChannel(channel.config());
/* 198 */             ChannelPipeline pipeline = channel.pipeline();
/* 199 */             if (isSecure) {
/* 200 */               Assert.notNull(Netty4ClientHttpRequestFactory.this.sslContext, "sslContext should not be null");
/* 201 */               pipeline.addLast(new ChannelHandler[] { (ChannelHandler)Netty4ClientHttpRequestFactory.access$000(this.this$0).newHandler(channel.alloc(), this.val$uri.getHost(), this.val$uri.getPort()) });
/*     */             } 
/* 203 */             pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new HttpClientCodec() });
/* 204 */             pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new HttpObjectAggregator(Netty4ClientHttpRequestFactory.access$100(this.this$0)) });
/* 205 */             if (Netty4ClientHttpRequestFactory.this.readTimeout > 0) {
/* 206 */               pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new ReadTimeoutHandler(Netty4ClientHttpRequestFactory.access$200(this.this$0), TimeUnit.MILLISECONDS) });
/*     */             }
/*     */           }
/*     */         });
/*     */     
/* 211 */     return bootstrap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configureChannel(SocketChannelConfig config) {
/* 220 */     if (this.connectTimeout >= 0) {
/* 221 */       config.setConnectTimeoutMillis(this.connectTimeout);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws InterruptedException {
/* 228 */     if (this.defaultEventLoopGroup)
/*     */     {
/* 230 */       this.eventLoopGroup.shutdownGracefully().sync();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\Netty4ClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */