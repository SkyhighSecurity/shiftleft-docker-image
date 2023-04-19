/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import org.springframework.core.ConfigurableObjectInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CodebaseAwareObjectInputStream
/*     */   extends ConfigurableObjectInputStream
/*     */ {
/*     */   private final String codebaseUrl;
/*     */   
/*     */   public CodebaseAwareObjectInputStream(InputStream in, String codebaseUrl) throws IOException {
/*  66 */     this(in, (ClassLoader)null, codebaseUrl);
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
/*     */   public CodebaseAwareObjectInputStream(InputStream in, ClassLoader classLoader, String codebaseUrl) throws IOException {
/*  81 */     super(in, classLoader);
/*  82 */     this.codebaseUrl = codebaseUrl;
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
/*     */   public CodebaseAwareObjectInputStream(InputStream in, ClassLoader classLoader, boolean acceptProxyClasses) throws IOException {
/*  97 */     super(in, classLoader, acceptProxyClasses);
/*  98 */     this.codebaseUrl = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> resolveFallbackIfPossible(String className, ClassNotFoundException ex) throws IOException, ClassNotFoundException {
/* 108 */     if (this.codebaseUrl == null) {
/* 109 */       throw ex;
/*     */     }
/* 111 */     return RMIClassLoader.loadClass(this.codebaseUrl, className);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassLoader getFallbackClassLoader() throws IOException {
/* 116 */     return RMIClassLoader.getClassLoader(this.codebaseUrl);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\rmi\CodebaseAwareObjectInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */