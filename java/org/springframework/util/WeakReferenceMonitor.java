/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class WeakReferenceMonitor
/*     */ {
/*  52 */   private static final Log logger = LogFactory.getLog(WeakReferenceMonitor.class);
/*     */ 
/*     */   
/*  55 */   private static final ReferenceQueue<Object> handleQueue = new ReferenceQueue();
/*     */ 
/*     */   
/*  58 */   private static final Map<Reference<?>, ReleaseListener> trackedEntries = new HashMap<Reference<?>, ReleaseListener>();
/*     */ 
/*     */   
/*  61 */   private static Thread monitoringThread = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void monitor(Object handle, ReleaseListener listener) {
/*  71 */     if (logger.isDebugEnabled()) {
/*  72 */       logger.debug("Monitoring handle [" + handle + "] with release listener [" + listener + "]");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  77 */     WeakReference<Object> weakRef = new WeakReference(handle, handleQueue);
/*     */ 
/*     */     
/*  80 */     addEntry(weakRef, listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addEntry(Reference<?> ref, ReleaseListener entry) {
/*  90 */     synchronized (WeakReferenceMonitor.class) {
/*     */       
/*  92 */       trackedEntries.put(ref, entry);
/*     */ 
/*     */       
/*  95 */       if (monitoringThread == null) {
/*  96 */         monitoringThread = new Thread(new MonitoringProcess(), WeakReferenceMonitor.class.getName());
/*  97 */         monitoringThread.setDaemon(true);
/*  98 */         monitoringThread.start();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ReleaseListener removeEntry(Reference<?> reference) {
/* 109 */     synchronized (WeakReferenceMonitor.class) {
/* 110 */       return trackedEntries.remove(reference);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean keepMonitoringThreadAlive() {
/* 119 */     synchronized (WeakReferenceMonitor.class) {
/* 120 */       if (!trackedEntries.isEmpty()) {
/* 121 */         return true;
/*     */       }
/*     */       
/* 124 */       logger.debug("No entries left to track - stopping reference monitor thread");
/* 125 */       monitoringThread = null;
/* 126 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface ReleaseListener {
/*     */     void released();
/*     */   }
/*     */   
/*     */   private static class MonitoringProcess
/*     */     implements Runnable {
/*     */     private MonitoringProcess() {}
/*     */     
/*     */     public void run() {
/* 139 */       WeakReferenceMonitor.logger.debug("Starting reference monitor thread");
/*     */       
/* 141 */       while (WeakReferenceMonitor.keepMonitoringThreadAlive()) {
/*     */         try {
/* 143 */           Reference<?> reference = WeakReferenceMonitor.handleQueue.remove();
/*     */           
/* 145 */           WeakReferenceMonitor.ReleaseListener entry = WeakReferenceMonitor.removeEntry(reference);
/* 146 */           if (entry != null) {
/*     */             
/*     */             try {
/* 149 */               entry.released();
/*     */             }
/* 151 */             catch (Throwable ex) {
/* 152 */               WeakReferenceMonitor.logger.warn("Reference release listener threw exception", ex);
/*     */             } 
/*     */           }
/*     */           continue;
/* 156 */         } catch (InterruptedException ex) {
/* 157 */           synchronized (WeakReferenceMonitor.class) {
/* 158 */             WeakReferenceMonitor.monitoringThread = null;
/*     */           } 
/* 160 */           WeakReferenceMonitor.logger.debug("Reference monitor thread interrupted", ex);
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\WeakReferenceMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */