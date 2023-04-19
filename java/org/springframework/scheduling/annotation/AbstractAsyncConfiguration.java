/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.Executor;
/*    */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.ImportAware;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ public abstract class AbstractAsyncConfiguration
/*    */   implements ImportAware
/*    */ {
/*    */   protected AnnotationAttributes enableAsync;
/*    */   protected Executor executor;
/*    */   protected AsyncUncaughtExceptionHandler exceptionHandler;
/*    */   
/*    */   public void setImportMetadata(AnnotationMetadata importMetadata) {
/* 51 */     this.enableAsync = AnnotationAttributes.fromMap(importMetadata
/* 52 */         .getAnnotationAttributes(EnableAsync.class.getName(), false));
/* 53 */     if (this.enableAsync == null) {
/* 54 */       throw new IllegalArgumentException("@EnableAsync is not present on importing class " + importMetadata
/* 55 */           .getClassName());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired(required = false)
/*    */   void setConfigurers(Collection<AsyncConfigurer> configurers) {
/* 64 */     if (CollectionUtils.isEmpty(configurers)) {
/*    */       return;
/*    */     }
/* 67 */     if (configurers.size() > 1) {
/* 68 */       throw new IllegalStateException("Only one AsyncConfigurer may exist");
/*    */     }
/* 70 */     AsyncConfigurer configurer = configurers.iterator().next();
/* 71 */     this.executor = configurer.getAsyncExecutor();
/* 72 */     this.exceptionHandler = configurer.getAsyncUncaughtExceptionHandler();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\annotation\AbstractAsyncConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */