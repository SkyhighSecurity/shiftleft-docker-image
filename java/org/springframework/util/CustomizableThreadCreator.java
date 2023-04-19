/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomizableThreadCreator
/*     */   implements Serializable
/*     */ {
/*     */   private String threadNamePrefix;
/*  38 */   private int threadPriority = 5;
/*     */   
/*     */   private boolean daemon = false;
/*     */   
/*     */   private ThreadGroup threadGroup;
/*     */   
/*  44 */   private final AtomicInteger threadCount = new AtomicInteger(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomizableThreadCreator() {
/*  51 */     this.threadNamePrefix = getDefaultThreadNamePrefix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomizableThreadCreator(String threadNamePrefix) {
/*  59 */     this.threadNamePrefix = (threadNamePrefix != null) ? threadNamePrefix : getDefaultThreadNamePrefix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadNamePrefix(String threadNamePrefix) {
/*  68 */     this.threadNamePrefix = (threadNamePrefix != null) ? threadNamePrefix : getDefaultThreadNamePrefix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getThreadNamePrefix() {
/*  76 */     return this.threadNamePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadPriority(int threadPriority) {
/*  85 */     this.threadPriority = threadPriority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getThreadPriority() {
/*  92 */     return this.threadPriority;
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
/*     */   public void setDaemon(boolean daemon) {
/* 106 */     this.daemon = daemon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDaemon() {
/* 113 */     return this.daemon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadGroupName(String name) {
/* 121 */     this.threadGroup = new ThreadGroup(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadGroup(ThreadGroup threadGroup) {
/* 129 */     this.threadGroup = threadGroup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadGroup getThreadGroup() {
/* 137 */     return this.threadGroup;
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
/*     */   public Thread createThread(Runnable runnable) {
/* 149 */     Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
/* 150 */     thread.setPriority(getThreadPriority());
/* 151 */     thread.setDaemon(isDaemon());
/* 152 */     return thread;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String nextThreadName() {
/* 162 */     return getThreadNamePrefix() + this.threadCount.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDefaultThreadNamePrefix() {
/* 170 */     return ClassUtils.getShortName(getClass()) + "-";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\CustomizableThreadCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */