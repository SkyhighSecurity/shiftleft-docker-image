/*     */ package org.springframework.scheduling.config;
/*     */ 
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.task.TaskExecutor;
/*     */ import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaskExecutorFactoryBean
/*     */   implements FactoryBean<TaskExecutor>, BeanNameAware, InitializingBean, DisposableBean
/*     */ {
/*     */   private String poolSize;
/*     */   private Integer queueCapacity;
/*     */   private RejectedExecutionHandler rejectedExecutionHandler;
/*     */   private Integer keepAliveSeconds;
/*     */   private String beanName;
/*     */   private ThreadPoolTaskExecutor target;
/*     */   
/*     */   public void setPoolSize(String poolSize) {
/*  54 */     this.poolSize = poolSize;
/*     */   }
/*     */   
/*     */   public void setQueueCapacity(int queueCapacity) {
/*  58 */     this.queueCapacity = Integer.valueOf(queueCapacity);
/*     */   }
/*     */   
/*     */   public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
/*  62 */     this.rejectedExecutionHandler = rejectedExecutionHandler;
/*     */   }
/*     */   
/*     */   public void setKeepAliveSeconds(int keepAliveSeconds) {
/*  66 */     this.keepAliveSeconds = Integer.valueOf(keepAliveSeconds);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/*  71 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  77 */     this.target = new ThreadPoolTaskExecutor();
/*  78 */     determinePoolSizeRange();
/*  79 */     if (this.queueCapacity != null) {
/*  80 */       this.target.setQueueCapacity(this.queueCapacity.intValue());
/*     */     }
/*  82 */     if (this.keepAliveSeconds != null) {
/*  83 */       this.target.setKeepAliveSeconds(this.keepAliveSeconds.intValue());
/*     */     }
/*  85 */     if (this.rejectedExecutionHandler != null) {
/*  86 */       this.target.setRejectedExecutionHandler(this.rejectedExecutionHandler);
/*     */     }
/*  88 */     if (this.beanName != null) {
/*  89 */       this.target.setThreadNamePrefix(this.beanName + "-");
/*     */     }
/*  91 */     this.target.afterPropertiesSet();
/*     */   }
/*     */   
/*     */   private void determinePoolSizeRange() {
/*  95 */     if (StringUtils.hasText(this.poolSize)) {
/*     */       
/*     */       try {
/*     */         
/*  99 */         int corePoolSize, maxPoolSize, separatorIndex = this.poolSize.indexOf('-');
/* 100 */         if (separatorIndex != -1) {
/* 101 */           corePoolSize = Integer.valueOf(this.poolSize.substring(0, separatorIndex)).intValue();
/* 102 */           maxPoolSize = Integer.valueOf(this.poolSize.substring(separatorIndex + 1, this.poolSize.length())).intValue();
/* 103 */           if (corePoolSize > maxPoolSize) {
/* 104 */             throw new IllegalArgumentException("Lower bound of pool-size range must not exceed the upper bound");
/*     */           }
/*     */           
/* 107 */           if (this.queueCapacity == null)
/*     */           {
/* 109 */             if (corePoolSize == 0)
/*     */             {
/*     */               
/* 112 */               this.target.setAllowCoreThreadTimeOut(true);
/* 113 */               corePoolSize = maxPoolSize;
/*     */             }
/*     */             else
/*     */             {
/* 117 */               throw new IllegalArgumentException("A non-zero lower bound for the size range requires a queue-capacity value");
/*     */             }
/*     */           
/*     */           }
/*     */         } else {
/*     */           
/* 123 */           Integer value = Integer.valueOf(this.poolSize);
/* 124 */           corePoolSize = value.intValue();
/* 125 */           maxPoolSize = value.intValue();
/*     */         } 
/* 127 */         this.target.setCorePoolSize(corePoolSize);
/* 128 */         this.target.setMaxPoolSize(maxPoolSize);
/*     */       }
/* 130 */       catch (NumberFormatException ex) {
/* 131 */         throw new IllegalArgumentException("Invalid pool-size value [" + this.poolSize + "]: only single maximum integer (e.g. \"5\") and minimum-maximum range (e.g. \"3-5\") are supported", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaskExecutor getObject() {
/* 140 */     return (TaskExecutor)this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends TaskExecutor> getObjectType() {
/* 145 */     return (this.target != null) ? (Class)this.target.getClass() : (Class)ThreadPoolTaskExecutor.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 156 */     this.target.destroy();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\config\TaskExecutorFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */