/*     */ package org.springframework.web.bind.support;
/*     */ 
/*     */ import org.springframework.beans.PropertyEditorRegistrar;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.validation.BindingErrorProcessor;
/*     */ import org.springframework.validation.MessageCodesResolver;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurableWebBindingInitializer
/*     */   implements WebBindingInitializer
/*     */ {
/*     */   private boolean autoGrowNestedPaths = true;
/*     */   private boolean directFieldAccess = false;
/*     */   private MessageCodesResolver messageCodesResolver;
/*     */   private BindingErrorProcessor bindingErrorProcessor;
/*     */   private Validator validator;
/*     */   private ConversionService conversionService;
/*     */   private PropertyEditorRegistrar[] propertyEditorRegistrars;
/*     */   
/*     */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
/*  69 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowNestedPaths() {
/*  76 */     return this.autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setDirectFieldAccess(boolean directFieldAccess) {
/*  87 */     this.directFieldAccess = directFieldAccess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectFieldAccess() {
/*  94 */     return this.directFieldAccess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMessageCodesResolver(MessageCodesResolver messageCodesResolver) {
/* 105 */     this.messageCodesResolver = messageCodesResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MessageCodesResolver getMessageCodesResolver() {
/* 112 */     return this.messageCodesResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setBindingErrorProcessor(BindingErrorProcessor bindingErrorProcessor) {
/* 123 */     this.bindingErrorProcessor = bindingErrorProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BindingErrorProcessor getBindingErrorProcessor() {
/* 130 */     return this.bindingErrorProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setValidator(Validator validator) {
/* 137 */     this.validator = validator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Validator getValidator() {
/* 144 */     return this.validator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setConversionService(ConversionService conversionService) {
/* 152 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConversionService getConversionService() {
/* 159 */     return this.conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPropertyEditorRegistrar(PropertyEditorRegistrar propertyEditorRegistrar) {
/* 166 */     this.propertyEditorRegistrars = new PropertyEditorRegistrar[] { propertyEditorRegistrar };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars) {
/* 173 */     this.propertyEditorRegistrars = propertyEditorRegistrars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PropertyEditorRegistrar[] getPropertyEditorRegistrars() {
/* 180 */     return this.propertyEditorRegistrars;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initBinder(WebDataBinder binder, WebRequest request) {
/* 186 */     binder.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/* 187 */     if (this.directFieldAccess) {
/* 188 */       binder.initDirectFieldAccess();
/*     */     }
/* 190 */     if (this.messageCodesResolver != null) {
/* 191 */       binder.setMessageCodesResolver(this.messageCodesResolver);
/*     */     }
/* 193 */     if (this.bindingErrorProcessor != null) {
/* 194 */       binder.setBindingErrorProcessor(this.bindingErrorProcessor);
/*     */     }
/* 196 */     if (this.validator != null && binder.getTarget() != null && this.validator
/* 197 */       .supports(binder.getTarget().getClass())) {
/* 198 */       binder.setValidator(this.validator);
/*     */     }
/* 200 */     if (this.conversionService != null) {
/* 201 */       binder.setConversionService(this.conversionService);
/*     */     }
/* 203 */     if (this.propertyEditorRegistrars != null)
/* 204 */       for (PropertyEditorRegistrar propertyEditorRegistrar : this.propertyEditorRegistrars)
/* 205 */         propertyEditorRegistrar.registerCustomEditors((PropertyEditorRegistry)binder);  
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\support\ConfigurableWebBindingInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */