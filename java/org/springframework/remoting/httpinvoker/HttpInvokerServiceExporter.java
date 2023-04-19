/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.remoting.rmi.RemoteInvocationSerializingExporter;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
/*     */ import org.springframework.web.HttpRequestHandler;
/*     */ import org.springframework.web.util.NestedServletException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpInvokerServiceExporter
/*     */   extends RemoteInvocationSerializingExporter
/*     */   implements HttpRequestHandler
/*     */ {
/*     */   public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*     */     try {
/*  79 */       RemoteInvocation invocation = readRemoteInvocation(request);
/*  80 */       RemoteInvocationResult result = invokeAndCreateResult(invocation, getProxy());
/*  81 */       writeRemoteInvocationResult(request, response, result);
/*     */     }
/*  83 */     catch (ClassNotFoundException ex) {
/*  84 */       throw new NestedServletException("Class not found during deserialization", ex);
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
/*     */   protected RemoteInvocation readRemoteInvocation(HttpServletRequest request) throws IOException, ClassNotFoundException {
/* 100 */     return readRemoteInvocation(request, (InputStream)request.getInputStream());
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
/*     */   protected RemoteInvocation readRemoteInvocation(HttpServletRequest request, InputStream is) throws IOException, ClassNotFoundException {
/* 119 */     ObjectInputStream ois = createObjectInputStream(decorateInputStream(request, is));
/*     */     try {
/* 121 */       return doReadRemoteInvocation(ois);
/*     */     } finally {
/*     */       
/* 124 */       ois.close();
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
/*     */   protected InputStream decorateInputStream(HttpServletRequest request, InputStream is) throws IOException {
/* 139 */     return is;
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
/*     */   protected void writeRemoteInvocationResult(HttpServletRequest request, HttpServletResponse response, RemoteInvocationResult result) throws IOException {
/* 153 */     response.setContentType(getContentType());
/* 154 */     writeRemoteInvocationResult(request, response, result, (OutputStream)response.getOutputStream());
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRemoteInvocationResult(HttpServletRequest request, HttpServletResponse response, RemoteInvocationResult result, OutputStream os) throws IOException {
/* 177 */     ObjectOutputStream oos = createObjectOutputStream(new FlushGuardedOutputStream(decorateOutputStream(request, response, os)));
/*     */     try {
/* 179 */       doWriteRemoteInvocationResult(result, oos);
/*     */     } finally {
/*     */       
/* 182 */       oos.close();
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
/*     */ 
/*     */   
/*     */   protected OutputStream decorateOutputStream(HttpServletRequest request, HttpServletResponse response, OutputStream os) throws IOException {
/* 200 */     return os;
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
/*     */   private static class FlushGuardedOutputStream
/*     */     extends FilterOutputStream
/*     */   {
/*     */     public FlushGuardedOutputStream(OutputStream out) {
/* 216 */       super(out);
/*     */     }
/*     */     
/*     */     public void flush() throws IOException {}
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\httpinvoker\HttpInvokerServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */