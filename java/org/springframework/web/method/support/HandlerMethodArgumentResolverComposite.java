/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HandlerMethodArgumentResolverComposite
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*  42 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  44 */   private final List<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<HandlerMethodArgumentResolver>();
/*     */ 
/*     */   
/*  47 */   private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap<MethodParameter, HandlerMethodArgumentResolver>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver resolver) {
/*  55 */     this.argumentResolvers.add(resolver);
/*  56 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodArgumentResolverComposite addResolvers(HandlerMethodArgumentResolver... resolvers) {
/*  64 */     if (resolvers != null) {
/*  65 */       for (HandlerMethodArgumentResolver resolver : resolvers) {
/*  66 */         this.argumentResolvers.add(resolver);
/*     */       }
/*     */     }
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodArgumentResolverComposite addResolvers(List<? extends HandlerMethodArgumentResolver> resolvers) {
/*  76 */     if (resolvers != null) {
/*  77 */       for (HandlerMethodArgumentResolver resolver : resolvers) {
/*  78 */         this.argumentResolvers.add(resolver);
/*     */       }
/*     */     }
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HandlerMethodArgumentResolver> getResolvers() {
/*  88 */     return Collections.unmodifiableList(this.argumentResolvers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  96 */     this.argumentResolvers.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/* 106 */     return (getArgumentResolver(parameter) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
/* 117 */     HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
/* 118 */     if (resolver == null) {
/* 119 */       throw new IllegalArgumentException("Unknown parameter type [" + parameter.getParameterType().getName() + "]");
/*     */     }
/* 121 */     return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
/* 128 */     HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
/* 129 */     if (result == null) {
/* 130 */       for (HandlerMethodArgumentResolver methodArgumentResolver : this.argumentResolvers) {
/* 131 */         if (this.logger.isTraceEnabled()) {
/* 132 */           this.logger.trace("Testing if argument resolver [" + methodArgumentResolver + "] supports [" + parameter
/* 133 */               .getGenericParameterType() + "]");
/*     */         }
/* 135 */         if (methodArgumentResolver.supportsParameter(parameter)) {
/* 136 */           result = methodArgumentResolver;
/* 137 */           this.argumentResolverCache.put(parameter, result);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 142 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\support\HandlerMethodArgumentResolverComposite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */