/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Role;
/*    */ import org.springframework.core.annotation.AnnotationUtils;
/*    */ import org.springframework.util.Assert;
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
/*    */ @Role(2)
/*    */ public class ProxyAsyncConfiguration
/*    */   extends AbstractAsyncConfiguration
/*    */ {
/*    */   @Bean(name = {"org.springframework.context.annotation.internalAsyncAnnotationProcessor"})
/*    */   @Role(2)
/*    */   public AsyncAnnotationBeanPostProcessor asyncAdvisor() {
/* 46 */     Assert.notNull(this.enableAsync, "@EnableAsync annotation metadata was not injected");
/* 47 */     AsyncAnnotationBeanPostProcessor bpp = new AsyncAnnotationBeanPostProcessor();
/* 48 */     Class<? extends Annotation> customAsyncAnnotation = this.enableAsync.getClass("annotation");
/* 49 */     if (customAsyncAnnotation != AnnotationUtils.getDefaultValue(EnableAsync.class, "annotation")) {
/* 50 */       bpp.setAsyncAnnotationType(customAsyncAnnotation);
/*    */     }
/* 52 */     if (this.executor != null) {
/* 53 */       bpp.setExecutor(this.executor);
/*    */     }
/* 55 */     if (this.exceptionHandler != null) {
/* 56 */       bpp.setExceptionHandler(this.exceptionHandler);
/*    */     }
/* 58 */     bpp.setProxyTargetClass(this.enableAsync.getBoolean("proxyTargetClass"));
/* 59 */     bpp.setOrder(((Integer)this.enableAsync.getNumber("order")).intValue());
/* 60 */     return bpp;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\annotation\ProxyAsyncConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */