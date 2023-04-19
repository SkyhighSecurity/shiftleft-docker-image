/*     */ package org.springframework.format.support;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.core.convert.support.ConversionServiceFactory;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.FormatterRegistrar;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormattingConversionServiceFactoryBean
/*     */   implements FactoryBean<FormattingConversionService>, EmbeddedValueResolverAware, InitializingBean
/*     */ {
/*     */   private Set<?> converters;
/*     */   private Set<?> formatters;
/*     */   private Set<FormatterRegistrar> formatterRegistrars;
/*     */   private boolean registerDefaultFormatters = true;
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   private FormattingConversionService conversionService;
/*     */   
/*     */   public void setConverters(Set<?> converters) {
/*  87 */     this.converters = converters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormatters(Set<?> formatters) {
/*  95 */     this.formatters = formatters;
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
/*     */   public void setFormatterRegistrars(Set<FormatterRegistrar> formatterRegistrars) {
/* 113 */     this.formatterRegistrars = formatterRegistrars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegisterDefaultFormatters(boolean registerDefaultFormatters) {
/* 124 */     this.registerDefaultFormatters = registerDefaultFormatters;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver embeddedValueResolver) {
/* 129 */     this.embeddedValueResolver = embeddedValueResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 135 */     this.conversionService = new DefaultFormattingConversionService(this.embeddedValueResolver, this.registerDefaultFormatters);
/* 136 */     ConversionServiceFactory.registerConverters(this.converters, (ConverterRegistry)this.conversionService);
/* 137 */     registerFormatters();
/*     */   }
/*     */   
/*     */   private void registerFormatters() {
/* 141 */     if (this.formatters != null) {
/* 142 */       for (Object formatter : this.formatters) {
/* 143 */         if (formatter instanceof Formatter) {
/* 144 */           this.conversionService.addFormatter((Formatter)formatter); continue;
/*     */         } 
/* 146 */         if (formatter instanceof AnnotationFormatterFactory) {
/* 147 */           this.conversionService.addFormatterForFieldAnnotation((AnnotationFormatterFactory<? extends Annotation>)formatter);
/*     */           continue;
/*     */         } 
/* 150 */         throw new IllegalArgumentException("Custom formatters must be implementations of Formatter or AnnotationFormatterFactory");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 155 */     if (this.formatterRegistrars != null) {
/* 156 */       for (FormatterRegistrar registrar : this.formatterRegistrars) {
/* 157 */         registrar.registerFormatters(this.conversionService);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattingConversionService getObject() {
/* 165 */     return this.conversionService;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends FormattingConversionService> getObjectType() {
/* 170 */     return FormattingConversionService.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 175 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\support\FormattingConversionServiceFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */