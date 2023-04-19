/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.SettableListenableFuture;
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
/*     */ class Netty4ClientHttpRequest
/*     */   extends AbstractAsyncClientHttpRequest
/*     */   implements ClientHttpRequest
/*     */ {
/*     */   private final Bootstrap bootstrap;
/*     */   private final URI uri;
/*     */   private final HttpMethod method;
/*     */   private final ByteBufOutputStream body;
/*     */   
/*     */   public Netty4ClientHttpRequest(Bootstrap bootstrap, URI uri, HttpMethod method) {
/*  66 */     this.bootstrap = bootstrap;
/*  67 */     this.uri = uri;
/*  68 */     this.method = method;
/*  69 */     this.body = new ByteBufOutputStream(Unpooled.buffer(1024));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  75 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  80 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientHttpResponse execute() throws IOException {
/*     */     try {
/*  86 */       return (ClientHttpResponse)executeAsync().get();
/*     */     }
/*  88 */     catch (InterruptedException ex) {
/*  89 */       Thread.currentThread().interrupt();
/*  90 */       throw new IOException("Interrupted during request execution", ex);
/*     */     }
/*  92 */     catch (ExecutionException ex) {
/*  93 */       if (ex.getCause() instanceof IOException) {
/*  94 */         throw (IOException)ex.getCause();
/*     */       }
/*     */       
/*  97 */       throw new IOException(ex.getMessage(), ex.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
/* 104 */     return (OutputStream)this.body;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(final HttpHeaders headers) throws IOException {
/* 109 */     final SettableListenableFuture<ClientHttpResponse> responseFuture = new SettableListenableFuture();
/*     */ 
/*     */     
/* 112 */     ChannelFutureListener connectionListener = new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 115 */           if (future.isSuccess()) {
/* 116 */             Channel channel = future.channel();
/* 117 */             channel.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)new Netty4ClientHttpRequest.RequestExecuteHandler(this.val$responseFuture) });
/* 118 */             FullHttpRequest nettyRequest = Netty4ClientHttpRequest.this.createFullHttpRequest(headers);
/* 119 */             channel.writeAndFlush(nettyRequest);
/*     */           } else {
/*     */             
/* 122 */             responseFuture.setException(future.cause());
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 127 */     this.bootstrap.connect(this.uri.getHost(), getPort(this.uri)).addListener((GenericFutureListener)connectionListener);
/* 128 */     return (ListenableFuture<ClientHttpResponse>)responseFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   private FullHttpRequest createFullHttpRequest(HttpHeaders headers) {
/* 133 */     HttpMethod nettyMethod = HttpMethod.valueOf(this.method.name());
/*     */     
/* 135 */     String authority = this.uri.getRawAuthority();
/* 136 */     String path = this.uri.toString().substring(this.uri.toString().indexOf(authority) + authority.length());
/*     */     
/* 138 */     DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, nettyMethod, path, this.body.buffer());
/*     */     
/* 140 */     defaultFullHttpRequest.headers().set("Host", this.uri.getHost() + ":" + getPort(this.uri));
/* 141 */     defaultFullHttpRequest.headers().set("Connection", "close");
/* 142 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)headers.entrySet()) {
/* 143 */       defaultFullHttpRequest.headers().add(entry.getKey(), entry.getValue());
/*     */     }
/* 145 */     if (!defaultFullHttpRequest.headers().contains("Content-Length") && this.body.buffer().readableBytes() > 0) {
/* 146 */       defaultFullHttpRequest.headers().set("Content-Length", Integer.valueOf(this.body.buffer().readableBytes()));
/*     */     }
/*     */     
/* 149 */     return (FullHttpRequest)defaultFullHttpRequest;
/*     */   }
/*     */   
/*     */   private static int getPort(URI uri) {
/* 153 */     int port = uri.getPort();
/* 154 */     if (port == -1) {
/* 155 */       if ("http".equalsIgnoreCase(uri.getScheme())) {
/* 156 */         port = 80;
/*     */       }
/* 158 */       else if ("https".equalsIgnoreCase(uri.getScheme())) {
/* 159 */         port = 443;
/*     */       } 
/*     */     }
/* 162 */     return port;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RequestExecuteHandler
/*     */     extends SimpleChannelInboundHandler<FullHttpResponse>
/*     */   {
/*     */     private final SettableListenableFuture<ClientHttpResponse> responseFuture;
/*     */ 
/*     */     
/*     */     public RequestExecuteHandler(SettableListenableFuture<ClientHttpResponse> responseFuture) {
/* 174 */       this.responseFuture = responseFuture;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void channelRead0(ChannelHandlerContext context, FullHttpResponse response) throws Exception {
/* 179 */       this.responseFuture.set(new Netty4ClientHttpResponse(context, response));
/*     */     }
/*     */ 
/*     */     
/*     */     public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
/* 184 */       this.responseFuture.setException(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\client\Netty4ClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */