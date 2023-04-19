/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.ServletException;
/*     */ import org.springframework.beans.ConversionNotSupportedException;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*     */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.Scope;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.RequestScope;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractNamedValueMethodArgumentResolver
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*     */   private final ConfigurableBeanFactory configurableBeanFactory;
/*     */   private final BeanExpressionContext expressionContext;
/*  69 */   private final Map<MethodParameter, NamedValueInfo> namedValueInfoCache = new ConcurrentHashMap<MethodParameter, NamedValueInfo>(256);
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractNamedValueMethodArgumentResolver() {
/*  74 */     this.configurableBeanFactory = null;
/*  75 */     this.expressionContext = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractNamedValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
/*  84 */     this.configurableBeanFactory = beanFactory;
/*  85 */     this.expressionContext = (beanFactory != null) ? new BeanExpressionContext(beanFactory, (Scope)new RequestScope()) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
/*  94 */     NamedValueInfo namedValueInfo = getNamedValueInfo(parameter);
/*  95 */     MethodParameter nestedParameter = parameter.nestedIfOptional();
/*     */     
/*  97 */     Object resolvedName = resolveStringValue(namedValueInfo.name);
/*  98 */     if (resolvedName == null) {
/*  99 */       throw new IllegalArgumentException("Specified name must not resolve to null: [" + namedValueInfo
/* 100 */           .name + "]");
/*     */     }
/*     */     
/* 103 */     Object arg = resolveName(resolvedName.toString(), nestedParameter, webRequest);
/* 104 */     if (arg == null) {
/* 105 */       if (namedValueInfo.defaultValue != null) {
/* 106 */         arg = resolveStringValue(namedValueInfo.defaultValue);
/*     */       }
/* 108 */       else if (namedValueInfo.required && !nestedParameter.isOptional()) {
/* 109 */         handleMissingValue(namedValueInfo.name, nestedParameter, webRequest);
/*     */       } 
/* 111 */       arg = handleNullValue(namedValueInfo.name, arg, nestedParameter.getNestedParameterType());
/*     */     }
/* 113 */     else if ("".equals(arg) && namedValueInfo.defaultValue != null) {
/* 114 */       arg = resolveStringValue(namedValueInfo.defaultValue);
/*     */     } 
/*     */     
/* 117 */     if (binderFactory != null) {
/* 118 */       WebDataBinder binder = binderFactory.createBinder(webRequest, null, namedValueInfo.name);
/*     */       try {
/* 120 */         arg = binder.convertIfNecessary(arg, parameter.getParameterType(), parameter);
/*     */       }
/* 122 */       catch (ConversionNotSupportedException ex) {
/* 123 */         throw new MethodArgumentConversionNotSupportedException(arg, ex.getRequiredType(), namedValueInfo
/* 124 */             .name, parameter, ex.getCause());
/*     */       }
/* 126 */       catch (TypeMismatchException ex) {
/* 127 */         throw new MethodArgumentTypeMismatchException(arg, ex.getRequiredType(), namedValueInfo
/* 128 */             .name, parameter, ex.getCause());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 133 */     handleResolvedValue(arg, namedValueInfo.name, parameter, mavContainer, webRequest);
/*     */     
/* 135 */     return arg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
/* 142 */     NamedValueInfo namedValueInfo = this.namedValueInfoCache.get(parameter);
/* 143 */     if (namedValueInfo == null) {
/* 144 */       namedValueInfo = createNamedValueInfo(parameter);
/* 145 */       namedValueInfo = updateNamedValueInfo(parameter, namedValueInfo);
/* 146 */       this.namedValueInfoCache.put(parameter, namedValueInfo);
/*     */     } 
/* 148 */     return namedValueInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract NamedValueInfo createNamedValueInfo(MethodParameter paramMethodParameter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NamedValueInfo updateNamedValueInfo(MethodParameter parameter, NamedValueInfo info) {
/* 163 */     String name = info.name;
/* 164 */     if (info.name.isEmpty()) {
/* 165 */       name = parameter.getParameterName();
/* 166 */       if (name == null) {
/* 167 */         throw new IllegalArgumentException("Name for argument type [" + parameter
/* 168 */             .getNestedParameterType().getName() + "] not available, and parameter name information not found in class file either.");
/*     */       }
/*     */     } 
/*     */     
/* 172 */     String defaultValue = "\n\t\t\n\t\t\n\n\t\t\t\t\n".equals(info.defaultValue) ? null : info.defaultValue;
/* 173 */     return new NamedValueInfo(name, info.required, defaultValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolveStringValue(String value) {
/* 181 */     if (this.configurableBeanFactory == null) {
/* 182 */       return value;
/*     */     }
/* 184 */     String placeholdersResolved = this.configurableBeanFactory.resolveEmbeddedValue(value);
/* 185 */     BeanExpressionResolver exprResolver = this.configurableBeanFactory.getBeanExpressionResolver();
/* 186 */     if (exprResolver == null) {
/* 187 */       return value;
/*     */     }
/* 189 */     return exprResolver.evaluate(placeholdersResolved, this.expressionContext);
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
/*     */   protected abstract Object resolveName(String paramString, MethodParameter paramMethodParameter, NativeWebRequest paramNativeWebRequest) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMissingValue(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/* 215 */     handleMissingValue(name, parameter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {
/* 225 */     throw new ServletRequestBindingException("Missing argument '" + name + "' for method parameter of type " + parameter
/* 226 */         .getNestedParameterType().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object handleNullValue(String name, Object value, Class<?> paramType) {
/* 233 */     if (value == null) {
/* 234 */       if (boolean.class.equals(paramType)) {
/* 235 */         return Boolean.FALSE;
/*     */       }
/* 237 */       if (paramType.isPrimitive()) {
/* 238 */         throw new IllegalStateException("Optional " + paramType.getSimpleName() + " parameter '" + name + "' is present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 243 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleResolvedValue(Object arg, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class NamedValueInfo
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private final boolean required;
/*     */ 
/*     */ 
/*     */     
/*     */     private final String defaultValue;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NamedValueInfo(String name, boolean required, String defaultValue) {
/* 271 */       this.name = name;
/* 272 */       this.required = required;
/* 273 */       this.defaultValue = defaultValue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\AbstractNamedValueMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */