/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.rmi.RemoteException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.remoting.rmi.CodebaseAwareObjectInputStream;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHttpInvokerRequestExecutor
/*     */   implements HttpInvokerRequestExecutor, BeanClassLoaderAware
/*     */ {
/*     */   public static final String CONTENT_TYPE_SERIALIZED_OBJECT = "application/x-java-serialized-object";
/*     */   private static final int SERIALIZED_INVOCATION_BYTE_ARRAY_INITIAL_SIZE = 1024;
/*     */   protected static final String HTTP_METHOD_POST = "POST";
/*     */   protected static final String HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Language";
/*     */   protected static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
/*     */   protected static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";
/*     */   protected static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
/*     */   protected static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
/*     */   protected static final String ENCODING_GZIP = "gzip";
/*  72 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  74 */   private String contentType = "application/x-java-serialized-object";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean acceptGzipEncoding = true;
/*     */ 
/*     */   
/*     */   private ClassLoader beanClassLoader;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentType(String contentType) {
/*  86 */     Assert.notNull(contentType, "'contentType' must not be null");
/*  87 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  94 */     return this.contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAcceptGzipEncoding(boolean acceptGzipEncoding) {
/* 104 */     this.acceptGzipEncoding = acceptGzipEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAcceptGzipEncoding() {
/* 112 */     return this.acceptGzipEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 117 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader getBeanClassLoader() {
/* 124 */     return this.beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration config, RemoteInvocation invocation) throws Exception {
/* 132 */     ByteArrayOutputStream baos = getByteArrayOutputStream(invocation);
/* 133 */     if (this.logger.isDebugEnabled()) {
/* 134 */       this.logger.debug("Sending HTTP invoker request for service at [" + config.getServiceUrl() + "], with size " + baos
/* 135 */           .size());
/*     */     }
/* 137 */     return doExecuteRequest(config, baos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteArrayOutputStream getByteArrayOutputStream(RemoteInvocation invocation) throws IOException {
/* 147 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/* 148 */     writeRemoteInvocation(invocation, baos);
/* 149 */     return baos;
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
/*     */   protected void writeRemoteInvocation(RemoteInvocation invocation, OutputStream os) throws IOException {
/* 166 */     ObjectOutputStream oos = new ObjectOutputStream(decorateOutputStream(os));
/*     */     try {
/* 168 */       doWriteRemoteInvocation(invocation, oos);
/*     */     } finally {
/*     */       
/* 171 */       oos.close();
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
/*     */   protected OutputStream decorateOutputStream(OutputStream os) throws IOException {
/* 184 */     return os;
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
/*     */   protected void doWriteRemoteInvocation(RemoteInvocation invocation, ObjectOutputStream oos) throws IOException {
/* 199 */     oos.writeObject(invocation);
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
/*     */   protected abstract RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration paramHttpInvokerClientConfiguration, ByteArrayOutputStream paramByteArrayOutputStream) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteInvocationResult readRemoteInvocationResult(InputStream is, String codebaseUrl) throws IOException, ClassNotFoundException {
/* 240 */     ObjectInputStream ois = createObjectInputStream(decorateInputStream(is), codebaseUrl);
/*     */     try {
/* 242 */       return doReadRemoteInvocationResult(ois);
/*     */     } finally {
/*     */       
/* 245 */       ois.close();
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
/*     */   protected InputStream decorateInputStream(InputStream is) throws IOException {
/* 258 */     return is;
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
/*     */   protected ObjectInputStream createObjectInputStream(InputStream is, String codebaseUrl) throws IOException {
/* 272 */     return (ObjectInputStream)new CodebaseAwareObjectInputStream(is, getBeanClassLoader(), codebaseUrl);
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
/*     */   protected RemoteInvocationResult doReadRemoteInvocationResult(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 291 */     Object obj = ois.readObject();
/* 292 */     if (!(obj instanceof RemoteInvocationResult)) {
/* 293 */       throw new RemoteException("Deserialized object needs to be assignable to type [" + RemoteInvocationResult.class
/* 294 */           .getName() + "]: " + ClassUtils.getDescriptiveType(obj));
/*     */     }
/* 296 */     return (RemoteInvocationResult)obj;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\httpinvoker\AbstractHttpInvokerRequestExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */