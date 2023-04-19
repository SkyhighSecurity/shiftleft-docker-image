/*     */ package org.springframework.remoting.caucho;
/*     */ 
/*     */ import com.caucho.hessian.io.AbstractHessianInput;
/*     */ import com.caucho.hessian.io.AbstractHessianOutput;
/*     */ import com.caucho.hessian.io.Hessian2Input;
/*     */ import com.caucho.hessian.io.Hessian2Output;
/*     */ import com.caucho.hessian.io.HessianDebugInputStream;
/*     */ import com.caucho.hessian.io.HessianDebugOutputStream;
/*     */ import com.caucho.hessian.io.HessianInput;
/*     */ import com.caucho.hessian.io.HessianOutput;
/*     */ import com.caucho.hessian.io.HessianRemoteResolver;
/*     */ import com.caucho.hessian.io.SerializerFactory;
/*     */ import com.caucho.hessian.server.HessianSkeleton;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.remoting.support.RemoteExporter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CommonsLogWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HessianExporter
/*     */   extends RemoteExporter
/*     */   implements InitializingBean
/*     */ {
/*     */   public static final String CONTENT_TYPE_HESSIAN = "application/x-hessian";
/*  62 */   private SerializerFactory serializerFactory = new SerializerFactory();
/*     */ 
/*     */ 
/*     */   
/*     */   private HessianRemoteResolver remoteResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   private Log debugLogger;
/*     */ 
/*     */   
/*     */   private HessianSkeleton skeleton;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializerFactory(SerializerFactory serializerFactory) {
/*  78 */     this.serializerFactory = (serializerFactory != null) ? serializerFactory : new SerializerFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSendCollectionType(boolean sendCollectionType) {
/*  86 */     this.serializerFactory.setSendCollectionType(sendCollectionType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowNonSerializable(boolean allowNonSerializable) {
/*  94 */     this.serializerFactory.setAllowNonSerializable(allowNonSerializable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteResolver(HessianRemoteResolver remoteResolver) {
/* 102 */     this.remoteResolver = remoteResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 111 */     this.debugLogger = debug ? this.logger : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 117 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 124 */     checkService();
/* 125 */     checkServiceInterface();
/* 126 */     this.skeleton = new HessianSkeleton(getProxyForService(), getServiceInterface());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invoke(InputStream inputStream, OutputStream outputStream) throws Throwable {
/* 137 */     Assert.notNull(this.skeleton, "Hessian exporter has not been initialized");
/* 138 */     doInvoke(this.skeleton, inputStream, outputStream);
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
/*     */   protected void doInvoke(HessianSkeleton skeleton, InputStream inputStream, OutputStream outputStream) throws Throwable {
/* 151 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader(); try {
/*     */       HessianDebugInputStream hessianDebugInputStream; BufferedInputStream bufferedInputStream; HessianDebugOutputStream hessianDebugOutputStream; HessianInput hessianInput; HessianOutput hessianOutput;
/* 153 */       InputStream isToUse = inputStream;
/* 154 */       OutputStream osToUse = outputStream;
/*     */       
/* 156 */       if (this.debugLogger != null && this.debugLogger.isDebugEnabled()) {
/* 157 */         PrintWriter debugWriter = new PrintWriter((Writer)new CommonsLogWriter(this.debugLogger));
/*     */         
/* 159 */         HessianDebugInputStream dis = new HessianDebugInputStream(inputStream, debugWriter);
/*     */         
/* 161 */         HessianDebugOutputStream dos = new HessianDebugOutputStream(outputStream, debugWriter);
/* 162 */         dis.startTop2();
/* 163 */         dos.startTop2();
/* 164 */         hessianDebugInputStream = dis;
/* 165 */         hessianDebugOutputStream = dos;
/*     */       } 
/*     */       
/* 168 */       if (!hessianDebugInputStream.markSupported()) {
/* 169 */         bufferedInputStream = new BufferedInputStream((InputStream)hessianDebugInputStream);
/* 170 */         bufferedInputStream.mark(1);
/*     */       } 
/*     */       
/* 173 */       int code = bufferedInputStream.read();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       if (code == 72) {
/*     */         
/* 182 */         int major = bufferedInputStream.read();
/* 183 */         int minor = bufferedInputStream.read();
/* 184 */         if (major != 2) {
/* 185 */           throw new IOException("Version " + major + '.' + minor + " is not understood");
/*     */         }
/* 187 */         Hessian2Input hessian2Input = new Hessian2Input(bufferedInputStream);
/* 188 */         Hessian2Output hessian2Output = new Hessian2Output((OutputStream)hessianDebugOutputStream);
/* 189 */         hessian2Input.readCall();
/*     */       }
/* 191 */       else if (code == 67) {
/*     */         
/* 193 */         bufferedInputStream.reset();
/* 194 */         Hessian2Input hessian2Input = new Hessian2Input(bufferedInputStream);
/* 195 */         Hessian2Output hessian2Output = new Hessian2Output((OutputStream)hessianDebugOutputStream);
/* 196 */         hessian2Input.readCall();
/*     */       }
/* 198 */       else if (code == 99) {
/*     */         
/* 200 */         int major = bufferedInputStream.read();
/* 201 */         int minor = bufferedInputStream.read();
/* 202 */         hessianInput = new HessianInput(bufferedInputStream);
/* 203 */         if (major >= 2) {
/* 204 */           Hessian2Output hessian2Output = new Hessian2Output((OutputStream)hessianDebugOutputStream);
/*     */         } else {
/*     */           
/* 207 */           hessianOutput = new HessianOutput((OutputStream)hessianDebugOutputStream);
/*     */         } 
/*     */       } else {
/*     */         
/* 211 */         throw new IOException("Expected 'H'/'C' (Hessian 2.0) or 'c' (Hessian 1.0) in hessian input at " + code);
/*     */       } 
/*     */       
/* 214 */       if (this.serializerFactory != null) {
/* 215 */         hessianInput.setSerializerFactory(this.serializerFactory);
/* 216 */         hessianOutput.setSerializerFactory(this.serializerFactory);
/*     */       } 
/* 218 */       if (this.remoteResolver != null) {
/* 219 */         hessianInput.setRemoteResolver(this.remoteResolver);
/*     */       }
/*     */       
/*     */       try {
/* 223 */         skeleton.invoke((AbstractHessianInput)hessianInput, (AbstractHessianOutput)hessianOutput);
/*     */       } finally {
/*     */         
/*     */         try {
/* 227 */           hessianInput.close();
/* 228 */           bufferedInputStream.close();
/*     */         }
/* 230 */         catch (IOException iOException) {}
/*     */ 
/*     */         
/*     */         try {
/* 234 */           hessianOutput.close();
/* 235 */           hessianDebugOutputStream.close();
/*     */         }
/* 237 */         catch (IOException iOException) {}
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 243 */       resetThreadContextClassLoader(originalClassLoader);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\caucho\HessianExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */