/*     */ package com.google.common.base;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FinalizableReferenceQueue
/*     */ {
/*  91 */   private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
/*     */   
/*     */   private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
/*     */   
/*     */   private static final Method startFinalizer;
/*     */   final ReferenceQueue<Object> queue;
/*     */   final boolean threadStarted;
/*     */   
/*     */   static {
/* 100 */     Class<?> finalizer = loadFinalizer(new FinalizerLoader[] { new SystemLoader(), new DecoupledLoader(), new DirectLoader() });
/*     */     
/* 102 */     startFinalizer = getStartFinalizer(finalizer);
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
/*     */   public FinalizableReferenceQueue() {
/*     */     ReferenceQueue<Object> referenceQueue;
/* 122 */     boolean threadStarted = false;
/*     */     try {
/* 124 */       referenceQueue = (ReferenceQueue<Object>)startFinalizer.invoke(null, new Object[] { FinalizableReference.class, this });
/*     */       
/* 126 */       threadStarted = true;
/* 127 */     } catch (IllegalAccessException e) {
/*     */       
/* 129 */       throw new AssertionError(e);
/* 130 */     } catch (Throwable t) {
/* 131 */       logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
/*     */ 
/*     */       
/* 134 */       referenceQueue = new ReferenceQueue();
/*     */     } 
/*     */     
/* 137 */     this.queue = referenceQueue;
/* 138 */     this.threadStarted = threadStarted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void cleanUp() {
/* 148 */     if (this.threadStarted) {
/*     */       return;
/*     */     }
/*     */     
/*     */     Reference<?> reference;
/* 153 */     while ((reference = this.queue.poll()) != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 158 */       reference.clear();
/*     */       try {
/* 160 */         ((FinalizableReference)reference).finalizeReferent();
/* 161 */       } catch (Throwable t) {
/* 162 */         logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> loadFinalizer(FinalizerLoader... loaders) {
/* 174 */     for (FinalizerLoader loader : loaders) {
/* 175 */       Class<?> finalizer = loader.loadFinalizer();
/* 176 */       if (finalizer != null) {
/* 177 */         return finalizer;
/*     */       }
/*     */     } 
/*     */     
/* 181 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface FinalizerLoader
/*     */   {
/*     */     Class<?> loadFinalizer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class SystemLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     public Class<?> loadFinalizer() {
/*     */       ClassLoader systemLoader;
/*     */       try {
/* 206 */         systemLoader = ClassLoader.getSystemClassLoader();
/* 207 */       } catch (SecurityException e) {
/* 208 */         FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
/* 209 */         return null;
/*     */       } 
/* 211 */       if (systemLoader != null) {
/*     */         try {
/* 213 */           return systemLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 214 */         } catch (ClassNotFoundException e) {
/*     */           
/* 216 */           return null;
/*     */         } 
/*     */       }
/* 219 */       return null;
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
/*     */   static class DecoupledLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Google Collections to your system class path.";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 253 */         ClassLoader finalizerLoader = newLoader(getBaseUrl());
/* 254 */         return finalizerLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 255 */       } catch (Exception e) {
/* 256 */         FinalizableReferenceQueue.logger.log(Level.WARNING, "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Google Collections to your system class path.", e);
/* 257 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URL getBaseUrl() throws IOException {
/* 266 */       String finalizerPath = "com.google.common.base.internal.Finalizer".replace('.', '/') + ".class";
/* 267 */       URL finalizerUrl = getClass().getClassLoader().getResource(finalizerPath);
/* 268 */       if (finalizerUrl == null) {
/* 269 */         throw new FileNotFoundException(finalizerPath);
/*     */       }
/*     */ 
/*     */       
/* 273 */       String urlString = finalizerUrl.toString();
/* 274 */       if (!urlString.endsWith(finalizerPath)) {
/* 275 */         throw new IOException("Unsupported path style: " + urlString);
/*     */       }
/* 277 */       urlString = urlString.substring(0, urlString.length() - finalizerPath.length());
/*     */       
/* 279 */       return new URL(finalizerUrl, urlString);
/*     */     }
/*     */ 
/*     */     
/*     */     URLClassLoader newLoader(URL base) {
/* 284 */       return new URLClassLoader(new URL[] { base });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class DirectLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 296 */         return Class.forName("com.google.common.base.internal.Finalizer");
/* 297 */       } catch (ClassNotFoundException e) {
/* 298 */         throw new AssertionError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Method getStartFinalizer(Class<?> finalizer) {
/*     */     try {
/* 308 */       return finalizer.getMethod("startFinalizer", new Class[] { Class.class, Object.class });
/* 309 */     } catch (NoSuchMethodException e) {
/* 310 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\base\FinalizableReferenceQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */