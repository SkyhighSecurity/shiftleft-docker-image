/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.converter.ConverterRegistry;
/*    */ import org.springframework.core.convert.support.ConversionServiceFactory;
/*    */ import org.springframework.core.convert.support.DefaultConversionService;
/*    */ import org.springframework.core.convert.support.GenericConversionService;
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
/*    */ public class ConversionServiceFactoryBean
/*    */   implements FactoryBean<ConversionService>, InitializingBean
/*    */ {
/*    */   private Set<?> converters;
/*    */   private GenericConversionService conversionService;
/*    */   
/*    */   public void setConverters(Set<?> converters) {
/* 64 */     this.converters = converters;
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() {
/* 69 */     this.conversionService = createConversionService();
/* 70 */     ConversionServiceFactory.registerConverters(this.converters, (ConverterRegistry)this.conversionService);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected GenericConversionService createConversionService() {
/* 80 */     return (GenericConversionService)new DefaultConversionService();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConversionService getObject() {
/* 88 */     return (ConversionService)this.conversionService;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends ConversionService> getObjectType() {
/* 93 */     return (Class)GenericConversionService.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 98 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\ConversionServiceFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */