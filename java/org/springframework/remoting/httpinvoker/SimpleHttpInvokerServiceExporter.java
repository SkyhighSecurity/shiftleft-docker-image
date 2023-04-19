/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import com.sun.net.httpserver.HttpHandler;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.springframework.lang.UsesSunHttpServer;
/*     */ import org.springframework.remoting.rmi.RemoteInvocationSerializingExporter;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
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
/*     */ public class SimpleHttpInvokerServiceExporter
/*     */   extends RemoteInvocationSerializingExporter
/*     */   implements HttpHandler
/*     */ {
/*     */   public void handle(HttpExchange exchange) throws IOException {
/*     */     try {
/*  74 */       RemoteInvocation invocation = readRemoteInvocation(exchange);
/*  75 */       RemoteInvocationResult result = invokeAndCreateResult(invocation, getProxy());
/*  76 */       writeRemoteInvocationResult(exchange, result);
/*  77 */       exchange.close();
/*     */     }
/*  79 */     catch (ClassNotFoundException ex) {
/*  80 */       exchange.sendResponseHeaders(500, -1L);
/*  81 */       this.logger.error("Class not found during deserialization", ex);
/*     */     } 
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
/*     */ 
/*     */   
/*     */   protected RemoteInvocation readRemoteInvocation(HttpExchange exchange) throws IOException, ClassNotFoundException {
/*  97 */     return readRemoteInvocation(exchange, exchange.getRequestBody());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteInvocation readRemoteInvocation(HttpExchange exchange, InputStream is) throws IOException, ClassNotFoundException {
/* 116 */     ObjectInputStream ois = createObjectInputStream(decorateInputStream(exchange, is));
/* 117 */     return doReadRemoteInvocation(ois);
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
/*     */   
/*     */   protected InputStream decorateInputStream(HttpExchange exchange, InputStream is) throws IOException {
/* 131 */     return is;
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
/*     */   protected void writeRemoteInvocationResult(HttpExchange exchange, RemoteInvocationResult result) throws IOException {
/* 143 */     exchange.getResponseHeaders().set("Content-Type", getContentType());
/* 144 */     exchange.sendResponseHeaders(200, 0L);
/* 145 */     writeRemoteInvocationResult(exchange, result, exchange.getResponseBody());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRemoteInvocationResult(HttpExchange exchange, RemoteInvocationResult result, OutputStream os) throws IOException {
/* 165 */     ObjectOutputStream oos = createObjectOutputStream(decorateOutputStream(exchange, os));
/* 166 */     doWriteRemoteInvocationResult(result, oos);
/* 167 */     oos.flush();
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
/*     */   
/*     */   protected OutputStream decorateOutputStream(HttpExchange exchange, OutputStream os) throws IOException {
/* 181 */     return os;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\httpinvoker\SimpleHttpInvokerServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */