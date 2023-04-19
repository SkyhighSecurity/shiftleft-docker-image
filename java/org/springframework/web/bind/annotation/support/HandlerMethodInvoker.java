/*     */ package org.springframework.web.bind.annotation.support;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.annotation.SynthesizingMethodParameter;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.ui.ExtendedModelMap;
/*     */ import org.springframework.ui.Model;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.CookieValue;
/*     */ import org.springframework.web.bind.annotation.InitBinder;
/*     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.support.DefaultSessionAttributeStore;
/*     */ import org.springframework.web.bind.support.SessionAttributeStore;
/*     */ import org.springframework.web.bind.support.SessionStatus;
/*     */ import org.springframework.web.bind.support.SimpleSessionStatus;
/*     */ import org.springframework.web.bind.support.WebArgumentResolver;
/*     */ import org.springframework.web.bind.support.WebBindingInitializer;
/*     */ import org.springframework.web.bind.support.WebRequestDataBinder;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartRequest;
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
/*     */ public class HandlerMethodInvoker
/*     */ {
/* 103 */   private static final String MODEL_KEY_PREFIX_STALE = SessionAttributeStore.class.getName() + ".STALE.";
/*     */ 
/*     */   
/* 106 */   private static final Log logger = LogFactory.getLog(HandlerMethodInvoker.class);
/*     */   
/*     */   private final HandlerMethodResolver methodResolver;
/*     */   
/*     */   private final WebBindingInitializer bindingInitializer;
/*     */   
/*     */   private final SessionAttributeStore sessionAttributeStore;
/*     */   
/*     */   private final ParameterNameDiscoverer parameterNameDiscoverer;
/*     */   
/*     */   private final WebArgumentResolver[] customArgumentResolvers;
/*     */   
/*     */   private final HttpMessageConverter<?>[] messageConverters;
/*     */   
/* 120 */   private final SimpleSessionStatus sessionStatus = new SimpleSessionStatus();
/*     */ 
/*     */   
/*     */   public HandlerMethodInvoker(HandlerMethodResolver methodResolver) {
/* 124 */     this(methodResolver, null);
/*     */   }
/*     */   
/*     */   public HandlerMethodInvoker(HandlerMethodResolver methodResolver, WebBindingInitializer bindingInitializer) {
/* 128 */     this(methodResolver, bindingInitializer, (SessionAttributeStore)new DefaultSessionAttributeStore(), null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodInvoker(HandlerMethodResolver methodResolver, WebBindingInitializer bindingInitializer, SessionAttributeStore sessionAttributeStore, ParameterNameDiscoverer parameterNameDiscoverer, WebArgumentResolver[] customArgumentResolvers, HttpMessageConverter<?>[] messageConverters) {
/* 135 */     this.methodResolver = methodResolver;
/* 136 */     this.bindingInitializer = bindingInitializer;
/* 137 */     this.sessionAttributeStore = sessionAttributeStore;
/* 138 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/* 139 */     this.customArgumentResolvers = customArgumentResolvers;
/* 140 */     this.messageConverters = messageConverters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object invokeHandlerMethod(Method handlerMethod, Object handler, NativeWebRequest webRequest, ExtendedModelMap implicitModel) throws Exception {
/* 147 */     Method handlerMethodToInvoke = BridgeMethodResolver.findBridgedMethod(handlerMethod);
/*     */     try {
/* 149 */       boolean debug = logger.isDebugEnabled();
/* 150 */       for (String attrName : this.methodResolver.getActualSessionAttributeNames()) {
/* 151 */         Object attrValue = this.sessionAttributeStore.retrieveAttribute((WebRequest)webRequest, attrName);
/* 152 */         if (attrValue != null) {
/* 153 */           implicitModel.addAttribute(attrName, attrValue);
/*     */         }
/*     */       } 
/* 156 */       for (Method attributeMethod : this.methodResolver.getModelAttributeMethods()) {
/* 157 */         Method attributeMethodToInvoke = BridgeMethodResolver.findBridgedMethod(attributeMethod);
/* 158 */         Object[] arrayOfObject = resolveHandlerArguments(attributeMethodToInvoke, handler, webRequest, implicitModel);
/* 159 */         if (debug) {
/* 160 */           logger.debug("Invoking model attribute method: " + attributeMethodToInvoke);
/*     */         }
/* 162 */         String attrName = ((ModelAttribute)AnnotationUtils.findAnnotation(attributeMethod, ModelAttribute.class)).value();
/* 163 */         if (!"".equals(attrName) && implicitModel.containsAttribute(attrName)) {
/*     */           continue;
/*     */         }
/* 166 */         ReflectionUtils.makeAccessible(attributeMethodToInvoke);
/* 167 */         Object attrValue = attributeMethodToInvoke.invoke(handler, arrayOfObject);
/* 168 */         if ("".equals(attrName)) {
/* 169 */           Class<?> resolvedType = GenericTypeResolver.resolveReturnType(attributeMethodToInvoke, handler.getClass());
/* 170 */           attrName = Conventions.getVariableNameForReturnType(attributeMethodToInvoke, resolvedType, attrValue);
/*     */         } 
/* 172 */         if (!implicitModel.containsAttribute(attrName)) {
/* 173 */           implicitModel.addAttribute(attrName, attrValue);
/*     */         }
/*     */       } 
/* 176 */       Object[] args = resolveHandlerArguments(handlerMethodToInvoke, handler, webRequest, implicitModel);
/* 177 */       if (debug) {
/* 178 */         logger.debug("Invoking request handler method: " + handlerMethodToInvoke);
/*     */       }
/* 180 */       ReflectionUtils.makeAccessible(handlerMethodToInvoke);
/* 181 */       return handlerMethodToInvoke.invoke(handler, args);
/*     */     }
/* 183 */     catch (IllegalStateException ex) {
/*     */ 
/*     */       
/* 186 */       throw new HandlerMethodInvocationException(handlerMethodToInvoke, ex);
/*     */     }
/* 188 */     catch (InvocationTargetException ex) {
/*     */       
/* 190 */       ReflectionUtils.rethrowException(ex.getTargetException());
/* 191 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void updateModelAttributes(Object handler, Map<String, Object> mavModel, ExtendedModelMap implicitModel, NativeWebRequest webRequest) throws Exception {
/* 198 */     if (this.methodResolver.hasSessionAttributes() && this.sessionStatus.isComplete()) {
/* 199 */       for (String attrName : this.methodResolver.getActualSessionAttributeNames()) {
/* 200 */         this.sessionAttributeStore.cleanupAttribute((WebRequest)webRequest, attrName);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 206 */     Map<String, Object> model = (mavModel != null) ? mavModel : (Map<String, Object>)implicitModel;
/* 207 */     if (model != null) {
/*     */       try {
/* 209 */         String[] originalAttrNames = StringUtils.toStringArray(model.keySet());
/* 210 */         for (String attrName : originalAttrNames) {
/* 211 */           Object attrValue = model.get(attrName);
/* 212 */           boolean isSessionAttr = this.methodResolver.isSessionAttribute(attrName, (attrValue != null) ? attrValue
/* 213 */               .getClass() : null);
/* 214 */           if (isSessionAttr) {
/* 215 */             if (this.sessionStatus.isComplete()) {
/* 216 */               implicitModel.put(MODEL_KEY_PREFIX_STALE + attrName, Boolean.TRUE);
/*     */             }
/* 218 */             else if (!implicitModel.containsKey(MODEL_KEY_PREFIX_STALE + attrName)) {
/* 219 */               this.sessionAttributeStore.storeAttribute((WebRequest)webRequest, attrName, attrValue);
/*     */             } 
/*     */           }
/* 222 */           if (!attrName.startsWith(BindingResult.MODEL_KEY_PREFIX) && (isSessionAttr || 
/* 223 */             isBindingCandidate(attrValue))) {
/* 224 */             String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + attrName;
/* 225 */             if (mavModel != null && !model.containsKey(bindingResultKey)) {
/* 226 */               WebDataBinder binder = createBinder(webRequest, attrValue, attrName);
/* 227 */               initBinder(handler, attrName, binder, webRequest);
/* 228 */               mavModel.put(bindingResultKey, binder.getBindingResult());
/*     */             }
/*     */           
/*     */           } 
/*     */         } 
/* 233 */       } catch (InvocationTargetException ex) {
/*     */         
/* 235 */         ReflectionUtils.rethrowException(ex.getTargetException());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] resolveHandlerArguments(Method handlerMethod, Object handler, NativeWebRequest webRequest, ExtendedModelMap implicitModel) throws Exception {
/* 244 */     Class<?>[] paramTypes = handlerMethod.getParameterTypes();
/* 245 */     Object[] args = new Object[paramTypes.length];
/*     */     
/* 247 */     for (int i = 0; i < args.length; i++) {
/* 248 */       SynthesizingMethodParameter synthesizingMethodParameter = new SynthesizingMethodParameter(handlerMethod, i);
/* 249 */       synthesizingMethodParameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
/* 250 */       GenericTypeResolver.resolveParameterType((MethodParameter)synthesizingMethodParameter, handler.getClass());
/* 251 */       String paramName = null;
/* 252 */       String headerName = null;
/* 253 */       boolean requestBodyFound = false;
/* 254 */       String cookieName = null;
/* 255 */       String pathVarName = null;
/* 256 */       String attrName = null;
/* 257 */       boolean required = false;
/* 258 */       String defaultValue = null;
/* 259 */       boolean validate = false;
/* 260 */       Object[] validationHints = null;
/* 261 */       int annotationsFound = 0;
/* 262 */       Annotation[] paramAnns = synthesizingMethodParameter.getParameterAnnotations();
/*     */       
/* 264 */       for (Annotation paramAnn : paramAnns) {
/* 265 */         if (RequestParam.class.isInstance(paramAnn)) {
/* 266 */           RequestParam requestParam = (RequestParam)paramAnn;
/* 267 */           paramName = requestParam.name();
/* 268 */           required = requestParam.required();
/* 269 */           defaultValue = parseDefaultValueAttribute(requestParam.defaultValue());
/* 270 */           annotationsFound++;
/*     */         }
/* 272 */         else if (RequestHeader.class.isInstance(paramAnn)) {
/* 273 */           RequestHeader requestHeader = (RequestHeader)paramAnn;
/* 274 */           headerName = requestHeader.name();
/* 275 */           required = requestHeader.required();
/* 276 */           defaultValue = parseDefaultValueAttribute(requestHeader.defaultValue());
/* 277 */           annotationsFound++;
/*     */         }
/* 279 */         else if (RequestBody.class.isInstance(paramAnn)) {
/* 280 */           requestBodyFound = true;
/* 281 */           annotationsFound++;
/*     */         }
/* 283 */         else if (CookieValue.class.isInstance(paramAnn)) {
/* 284 */           CookieValue cookieValue = (CookieValue)paramAnn;
/* 285 */           cookieName = cookieValue.name();
/* 286 */           required = cookieValue.required();
/* 287 */           defaultValue = parseDefaultValueAttribute(cookieValue.defaultValue());
/* 288 */           annotationsFound++;
/*     */         }
/* 290 */         else if (PathVariable.class.isInstance(paramAnn)) {
/* 291 */           PathVariable pathVar = (PathVariable)paramAnn;
/* 292 */           pathVarName = pathVar.value();
/* 293 */           annotationsFound++;
/*     */         }
/* 295 */         else if (ModelAttribute.class.isInstance(paramAnn)) {
/* 296 */           ModelAttribute attr = (ModelAttribute)paramAnn;
/* 297 */           attrName = attr.value();
/* 298 */           annotationsFound++;
/*     */         }
/* 300 */         else if (Value.class.isInstance(paramAnn)) {
/* 301 */           defaultValue = ((Value)paramAnn).value();
/*     */         } else {
/*     */           
/* 304 */           Validated validatedAnn = (Validated)AnnotationUtils.getAnnotation(paramAnn, Validated.class);
/* 305 */           if (validatedAnn != null || paramAnn.annotationType().getSimpleName().startsWith("Valid")) {
/* 306 */             validate = true;
/* 307 */             Object hints = (validatedAnn != null) ? validatedAnn.value() : AnnotationUtils.getValue(paramAnn);
/* 308 */             (new Object[1])[0] = hints; validationHints = (hints instanceof Object[]) ? (Object[])hints : new Object[1];
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 313 */       if (annotationsFound > 1) {
/* 314 */         throw new IllegalStateException("Handler parameter annotations are exclusive choices - do not specify more than one such annotation on the same parameter: " + handlerMethod);
/*     */       }
/*     */ 
/*     */       
/* 318 */       if (annotationsFound == 0) {
/* 319 */         Object argValue = resolveCommonArgument((MethodParameter)synthesizingMethodParameter, webRequest);
/* 320 */         if (argValue != WebArgumentResolver.UNRESOLVED) {
/* 321 */           args[i] = argValue;
/*     */         }
/* 323 */         else if (defaultValue != null) {
/* 324 */           args[i] = resolveDefaultValue(defaultValue);
/*     */         } else {
/*     */           
/* 327 */           Class<?> paramType = synthesizingMethodParameter.getParameterType();
/* 328 */           if (Model.class.isAssignableFrom(paramType) || Map.class.isAssignableFrom(paramType)) {
/* 329 */             if (!paramType.isAssignableFrom(implicitModel.getClass())) {
/* 330 */               throw new IllegalStateException("Argument [" + paramType.getSimpleName() + "] is of type Model or Map but is not assignable from the actual model. You may need to switch newer MVC infrastructure classes to use this argument.");
/*     */             }
/*     */ 
/*     */             
/* 334 */             args[i] = implicitModel;
/*     */           }
/* 336 */           else if (SessionStatus.class.isAssignableFrom(paramType)) {
/* 337 */             args[i] = this.sessionStatus;
/*     */           }
/* 339 */           else if (HttpEntity.class.isAssignableFrom(paramType)) {
/* 340 */             args[i] = resolveHttpEntityRequest((MethodParameter)synthesizingMethodParameter, webRequest);
/*     */           } else {
/* 342 */             if (Errors.class.isAssignableFrom(paramType)) {
/* 343 */               throw new IllegalStateException("Errors/BindingResult argument declared without preceding model attribute. Check your handler method signature!");
/*     */             }
/*     */             
/* 346 */             if (BeanUtils.isSimpleProperty(paramType)) {
/* 347 */               paramName = "";
/*     */             } else {
/*     */               
/* 350 */               attrName = "";
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 355 */       if (paramName != null) {
/* 356 */         args[i] = resolveRequestParam(paramName, required, defaultValue, (MethodParameter)synthesizingMethodParameter, webRequest, handler);
/*     */       }
/* 358 */       else if (headerName != null) {
/* 359 */         args[i] = resolveRequestHeader(headerName, required, defaultValue, (MethodParameter)synthesizingMethodParameter, webRequest, handler);
/*     */       }
/* 361 */       else if (requestBodyFound) {
/* 362 */         args[i] = resolveRequestBody((MethodParameter)synthesizingMethodParameter, webRequest, handler);
/*     */       }
/* 364 */       else if (cookieName != null) {
/* 365 */         args[i] = resolveCookieValue(cookieName, required, defaultValue, (MethodParameter)synthesizingMethodParameter, webRequest, handler);
/*     */       }
/* 367 */       else if (pathVarName != null) {
/* 368 */         args[i] = resolvePathVariable(pathVarName, (MethodParameter)synthesizingMethodParameter, webRequest, handler);
/*     */       }
/* 370 */       else if (attrName != null) {
/*     */         
/* 372 */         WebDataBinder binder = resolveModelAttribute(attrName, (MethodParameter)synthesizingMethodParameter, implicitModel, webRequest, handler);
/* 373 */         boolean assignBindingResult = (args.length > i + 1 && Errors.class.isAssignableFrom(paramTypes[i + 1]));
/* 374 */         if (binder.getTarget() != null) {
/* 375 */           doBind(binder, webRequest, validate, validationHints, !assignBindingResult);
/*     */         }
/* 377 */         args[i] = binder.getTarget();
/* 378 */         if (assignBindingResult) {
/* 379 */           args[i + 1] = binder.getBindingResult();
/* 380 */           i++;
/*     */         } 
/* 382 */         implicitModel.putAll(binder.getBindingResult().getModel());
/*     */       } 
/*     */     } 
/*     */     
/* 386 */     return args;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initBinder(Object handler, String attrName, WebDataBinder binder, NativeWebRequest webRequest) throws Exception {
/* 392 */     if (this.bindingInitializer != null) {
/* 393 */       this.bindingInitializer.initBinder(binder, (WebRequest)webRequest);
/*     */     }
/* 395 */     if (handler != null) {
/* 396 */       Set<Method> initBinderMethods = this.methodResolver.getInitBinderMethods();
/* 397 */       if (!initBinderMethods.isEmpty()) {
/* 398 */         boolean debug = logger.isDebugEnabled();
/* 399 */         for (Method initBinderMethod : initBinderMethods) {
/* 400 */           Method methodToInvoke = BridgeMethodResolver.findBridgedMethod(initBinderMethod);
/* 401 */           String[] targetNames = ((InitBinder)AnnotationUtils.findAnnotation(initBinderMethod, InitBinder.class)).value();
/* 402 */           if (targetNames.length == 0 || Arrays.<String>asList(targetNames).contains(attrName)) {
/*     */             
/* 404 */             Object[] initBinderArgs = resolveInitBinderArguments(handler, methodToInvoke, binder, webRequest);
/* 405 */             if (debug) {
/* 406 */               logger.debug("Invoking init-binder method: " + methodToInvoke);
/*     */             }
/* 408 */             ReflectionUtils.makeAccessible(methodToInvoke);
/* 409 */             Object returnValue = methodToInvoke.invoke(handler, initBinderArgs);
/* 410 */             if (returnValue != null) {
/* 411 */               throw new IllegalStateException("InitBinder methods must not have a return value: " + methodToInvoke);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] resolveInitBinderArguments(Object handler, Method initBinderMethod, WebDataBinder binder, NativeWebRequest webRequest) throws Exception {
/* 423 */     Class<?>[] initBinderParams = initBinderMethod.getParameterTypes();
/* 424 */     Object[] initBinderArgs = new Object[initBinderParams.length];
/*     */     
/* 426 */     for (int i = 0; i < initBinderArgs.length; i++) {
/* 427 */       SynthesizingMethodParameter synthesizingMethodParameter = new SynthesizingMethodParameter(initBinderMethod, i);
/* 428 */       synthesizingMethodParameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
/* 429 */       GenericTypeResolver.resolveParameterType((MethodParameter)synthesizingMethodParameter, handler.getClass());
/* 430 */       String paramName = null;
/* 431 */       boolean paramRequired = false;
/* 432 */       String paramDefaultValue = null;
/* 433 */       String pathVarName = null;
/* 434 */       Annotation[] paramAnns = synthesizingMethodParameter.getParameterAnnotations();
/*     */       
/* 436 */       for (Annotation paramAnn : paramAnns) {
/* 437 */         if (RequestParam.class.isInstance(paramAnn)) {
/* 438 */           RequestParam requestParam = (RequestParam)paramAnn;
/* 439 */           paramName = requestParam.name();
/* 440 */           paramRequired = requestParam.required();
/* 441 */           paramDefaultValue = parseDefaultValueAttribute(requestParam.defaultValue());
/*     */           break;
/*     */         } 
/* 444 */         if (ModelAttribute.class.isInstance(paramAnn)) {
/* 445 */           throw new IllegalStateException("@ModelAttribute is not supported on @InitBinder methods: " + initBinderMethod);
/*     */         }
/*     */         
/* 448 */         if (PathVariable.class.isInstance(paramAnn)) {
/* 449 */           PathVariable pathVar = (PathVariable)paramAnn;
/* 450 */           pathVarName = pathVar.value();
/*     */         } 
/*     */       } 
/*     */       
/* 454 */       if (paramName == null && pathVarName == null) {
/* 455 */         Object argValue = resolveCommonArgument((MethodParameter)synthesizingMethodParameter, webRequest);
/* 456 */         if (argValue != WebArgumentResolver.UNRESOLVED) {
/* 457 */           initBinderArgs[i] = argValue;
/*     */         } else {
/*     */           
/* 460 */           Class<?> paramType = initBinderParams[i];
/* 461 */           if (paramType.isInstance(binder)) {
/* 462 */             initBinderArgs[i] = binder;
/*     */           }
/* 464 */           else if (BeanUtils.isSimpleProperty(paramType)) {
/* 465 */             paramName = "";
/*     */           } else {
/*     */             
/* 468 */             throw new IllegalStateException("Unsupported argument [" + paramType.getName() + "] for @InitBinder method: " + initBinderMethod);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 474 */       if (paramName != null) {
/* 475 */         initBinderArgs[i] = 
/* 476 */           resolveRequestParam(paramName, paramRequired, paramDefaultValue, (MethodParameter)synthesizingMethodParameter, webRequest, null);
/*     */       }
/* 478 */       else if (pathVarName != null) {
/* 479 */         initBinderArgs[i] = resolvePathVariable(pathVarName, (MethodParameter)synthesizingMethodParameter, webRequest, null);
/*     */       } 
/*     */     } 
/*     */     
/* 483 */     return initBinderArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolveRequestParam(String paramName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall) throws Exception {
/* 491 */     Class<?> paramType = methodParam.getParameterType();
/* 492 */     if (Map.class.isAssignableFrom(paramType) && paramName.length() == 0) {
/* 493 */       return resolveRequestParamMap((Class)paramType, webRequest);
/*     */     }
/* 495 */     if (paramName.length() == 0) {
/* 496 */       paramName = getRequiredParameterName(methodParam);
/*     */     }
/* 498 */     Object paramValue = null;
/* 499 */     MultipartRequest multipartRequest = (MultipartRequest)webRequest.getNativeRequest(MultipartRequest.class);
/* 500 */     if (multipartRequest != null) {
/* 501 */       List<MultipartFile> files = multipartRequest.getFiles(paramName);
/* 502 */       if (!files.isEmpty()) {
/* 503 */         paramValue = (files.size() == 1) ? files.get(0) : files;
/*     */       }
/*     */     } 
/* 506 */     if (paramValue == null) {
/* 507 */       String[] paramValues = webRequest.getParameterValues(paramName);
/* 508 */       if (paramValues != null) {
/* 509 */         paramValue = (paramValues.length == 1) ? paramValues[0] : paramValues;
/*     */       }
/*     */     } 
/* 512 */     if (paramValue == null) {
/* 513 */       if (defaultValue != null) {
/* 514 */         paramValue = resolveDefaultValue(defaultValue);
/*     */       }
/* 516 */       else if (required) {
/* 517 */         raiseMissingParameterException(paramName, paramType);
/*     */       } 
/* 519 */       paramValue = checkValue(paramName, paramValue, paramType);
/*     */     } 
/* 521 */     WebDataBinder binder = createBinder(webRequest, null, paramName);
/* 522 */     initBinder(handlerForInitBinderCall, paramName, binder, webRequest);
/* 523 */     return binder.convertIfNecessary(paramValue, paramType, methodParam);
/*     */   }
/*     */   
/*     */   private Map<String, ?> resolveRequestParamMap(Class<? extends Map<?, ?>> mapType, NativeWebRequest webRequest) {
/* 527 */     Map<String, String[]> parameterMap = webRequest.getParameterMap();
/* 528 */     if (MultiValueMap.class.isAssignableFrom(mapType)) {
/* 529 */       LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(parameterMap.size());
/* 530 */       for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
/* 531 */         for (String value : (String[])entry.getValue()) {
/* 532 */           linkedMultiValueMap.add(entry.getKey(), value);
/*     */         }
/*     */       } 
/* 535 */       return (Map<String, ?>)linkedMultiValueMap;
/*     */     } 
/*     */     
/* 538 */     Map<String, String> result = new LinkedHashMap<String, String>(parameterMap.size());
/* 539 */     for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
/* 540 */       if (((String[])entry.getValue()).length > 0) {
/* 541 */         result.put(entry.getKey(), ((String[])entry.getValue())[0]);
/*     */       }
/*     */     } 
/* 544 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolveRequestHeader(String headerName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall) throws Exception {
/* 553 */     Class<?> paramType = methodParam.getParameterType();
/* 554 */     if (Map.class.isAssignableFrom(paramType)) {
/* 555 */       return resolveRequestHeaderMap((Class)paramType, webRequest);
/*     */     }
/* 557 */     if (headerName.length() == 0) {
/* 558 */       headerName = getRequiredParameterName(methodParam);
/*     */     }
/* 560 */     Object headerValue = null;
/* 561 */     String[] headerValues = webRequest.getHeaderValues(headerName);
/* 562 */     if (headerValues != null) {
/* 563 */       headerValue = (headerValues.length == 1) ? headerValues[0] : headerValues;
/*     */     }
/* 565 */     if (headerValue == null) {
/* 566 */       if (defaultValue != null) {
/* 567 */         headerValue = resolveDefaultValue(defaultValue);
/*     */       }
/* 569 */       else if (required) {
/* 570 */         raiseMissingHeaderException(headerName, paramType);
/*     */       } 
/* 572 */       headerValue = checkValue(headerName, headerValue, paramType);
/*     */     } 
/* 574 */     WebDataBinder binder = createBinder(webRequest, null, headerName);
/* 575 */     initBinder(handlerForInitBinderCall, headerName, binder, webRequest);
/* 576 */     return binder.convertIfNecessary(headerValue, paramType, methodParam);
/*     */   }
/*     */   
/*     */   private Map<String, ?> resolveRequestHeaderMap(Class<? extends Map<?, ?>> mapType, NativeWebRequest webRequest) {
/* 580 */     if (MultiValueMap.class.isAssignableFrom(mapType)) {
/*     */       LinkedMultiValueMap linkedMultiValueMap;
/* 582 */       if (HttpHeaders.class.isAssignableFrom(mapType)) {
/* 583 */         HttpHeaders httpHeaders = new HttpHeaders();
/*     */       } else {
/*     */         
/* 586 */         linkedMultiValueMap = new LinkedMultiValueMap();
/*     */       } 
/* 588 */       for (Iterator<String> iterator1 = webRequest.getHeaderNames(); iterator1.hasNext(); ) {
/* 589 */         String headerName = iterator1.next();
/* 590 */         for (String headerValue : webRequest.getHeaderValues(headerName)) {
/* 591 */           linkedMultiValueMap.add(headerName, headerValue);
/*     */         }
/*     */       } 
/* 594 */       return (Map<String, ?>)linkedMultiValueMap;
/*     */     } 
/*     */     
/* 597 */     Map<String, String> result = new LinkedHashMap<String, String>();
/* 598 */     for (Iterator<String> iterator = webRequest.getHeaderNames(); iterator.hasNext(); ) {
/* 599 */       String headerName = iterator.next();
/* 600 */       String headerValue = webRequest.getHeader(headerName);
/* 601 */       result.put(headerName, headerValue);
/*     */     } 
/* 603 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object resolveRequestBody(MethodParameter methodParam, NativeWebRequest webRequest, Object handler) throws Exception {
/* 613 */     return readWithMessageConverters(methodParam, createHttpInputMessage(webRequest), methodParam.getParameterType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpEntity<?> resolveHttpEntityRequest(MethodParameter methodParam, NativeWebRequest webRequest) throws Exception {
/* 619 */     HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
/* 620 */     Class<?> paramType = getHttpEntityType(methodParam);
/* 621 */     Object body = readWithMessageConverters(methodParam, inputMessage, paramType);
/* 622 */     return new HttpEntity(body, (MultiValueMap)inputMessage.getHeaders());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readWithMessageConverters(MethodParameter methodParam, HttpInputMessage inputMessage, Class<?> paramType) throws Exception {
/* 629 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/* 630 */     if (contentType == null) {
/* 631 */       StringBuilder builder = new StringBuilder(ClassUtils.getShortName(methodParam.getParameterType()));
/* 632 */       String paramName = methodParam.getParameterName();
/* 633 */       if (paramName != null) {
/* 634 */         builder.append(' ');
/* 635 */         builder.append(paramName);
/*     */       } 
/* 637 */       throw new HttpMediaTypeNotSupportedException("Cannot extract parameter (" + builder
/* 638 */           .toString() + "): no Content-Type found");
/*     */     } 
/*     */     
/* 641 */     List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
/* 642 */     if (this.messageConverters != null) {
/* 643 */       for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
/* 644 */         allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
/* 645 */         if (messageConverter.canRead(paramType, contentType)) {
/* 646 */           if (logger.isDebugEnabled()) {
/* 647 */             logger.debug("Reading [" + paramType.getName() + "] as \"" + contentType + "\" using [" + messageConverter + "]");
/*     */           }
/*     */           
/* 650 */           return messageConverter.read(paramType, inputMessage);
/*     */         } 
/*     */       } 
/*     */     }
/* 654 */     throw new HttpMediaTypeNotSupportedException(contentType, allSupportedMediaTypes);
/*     */   }
/*     */   
/*     */   private Class<?> getHttpEntityType(MethodParameter methodParam) {
/* 658 */     Assert.isAssignable(HttpEntity.class, methodParam.getParameterType());
/* 659 */     ParameterizedType type = (ParameterizedType)methodParam.getGenericParameterType();
/* 660 */     if ((type.getActualTypeArguments()).length == 1) {
/* 661 */       Type typeArgument = type.getActualTypeArguments()[0];
/* 662 */       if (typeArgument instanceof Class) {
/* 663 */         return (Class)typeArgument;
/*     */       }
/* 665 */       if (typeArgument instanceof GenericArrayType) {
/* 666 */         Type componentType = ((GenericArrayType)typeArgument).getGenericComponentType();
/* 667 */         if (componentType instanceof Class) {
/*     */           
/* 669 */           Object array = Array.newInstance((Class)componentType, 0);
/* 670 */           return array.getClass();
/*     */         } 
/*     */       } 
/*     */     } 
/* 674 */     throw new IllegalArgumentException("HttpEntity parameter (" + methodParam
/* 675 */         .getParameterName() + ") is not parameterized");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolveCookieValue(String cookieName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall) throws Exception {
/* 683 */     Class<?> paramType = methodParam.getParameterType();
/* 684 */     if (cookieName.length() == 0) {
/* 685 */       cookieName = getRequiredParameterName(methodParam);
/*     */     }
/* 687 */     Object cookieValue = resolveCookieValue(cookieName, paramType, webRequest);
/* 688 */     if (cookieValue == null) {
/* 689 */       if (defaultValue != null) {
/* 690 */         cookieValue = resolveDefaultValue(defaultValue);
/*     */       }
/* 692 */       else if (required) {
/* 693 */         raiseMissingCookieException(cookieName, paramType);
/*     */       } 
/* 695 */       cookieValue = checkValue(cookieName, cookieValue, paramType);
/*     */     } 
/* 697 */     WebDataBinder binder = createBinder(webRequest, null, cookieName);
/* 698 */     initBinder(handlerForInitBinderCall, cookieName, binder, webRequest);
/* 699 */     return binder.convertIfNecessary(cookieValue, paramType, methodParam);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object resolveCookieValue(String cookieName, Class<?> paramType, NativeWebRequest webRequest) throws Exception {
/* 709 */     throw new UnsupportedOperationException("@CookieValue not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolvePathVariable(String pathVarName, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall) throws Exception {
/* 715 */     Class<?> paramType = methodParam.getParameterType();
/* 716 */     if (pathVarName.length() == 0) {
/* 717 */       pathVarName = getRequiredParameterName(methodParam);
/*     */     }
/* 719 */     String pathVarValue = resolvePathVariable(pathVarName, paramType, webRequest);
/* 720 */     WebDataBinder binder = createBinder(webRequest, null, pathVarName);
/* 721 */     initBinder(handlerForInitBinderCall, pathVarName, binder, webRequest);
/* 722 */     return binder.convertIfNecessary(pathVarValue, paramType, methodParam);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolvePathVariable(String pathVarName, Class<?> paramType, NativeWebRequest webRequest) throws Exception {
/* 732 */     throw new UnsupportedOperationException("@PathVariable not supported");
/*     */   }
/*     */   
/*     */   private String getRequiredParameterName(MethodParameter methodParam) {
/* 736 */     String name = methodParam.getParameterName();
/* 737 */     if (name == null) {
/* 738 */       throw new IllegalStateException("No parameter name specified for argument of type [" + methodParam
/* 739 */           .getParameterType().getName() + "], and no parameter name information found in class file either.");
/*     */     }
/*     */     
/* 742 */     return name;
/*     */   }
/*     */   
/*     */   private Object checkValue(String name, Object value, Class<?> paramType) {
/* 746 */     if (value == null) {
/* 747 */       if (boolean.class == paramType) {
/* 748 */         return Boolean.FALSE;
/*     */       }
/* 750 */       if (paramType.isPrimitive()) {
/* 751 */         throw new IllegalStateException("Optional " + paramType + " parameter '" + name + "' is not present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 756 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private WebDataBinder resolveModelAttribute(String attrName, MethodParameter methodParam, ExtendedModelMap implicitModel, NativeWebRequest webRequest, Object handler) throws Exception {
/*     */     Object bindObject;
/* 763 */     String name = attrName;
/* 764 */     if ("".equals(name)) {
/* 765 */       name = Conventions.getVariableNameForParameter(methodParam);
/*     */     }
/* 767 */     Class<?> paramType = methodParam.getParameterType();
/*     */     
/* 769 */     if (implicitModel.containsKey(name)) {
/* 770 */       bindObject = implicitModel.get(name);
/*     */     }
/* 772 */     else if (this.methodResolver.isSessionAttribute(name, paramType)) {
/* 773 */       bindObject = this.sessionAttributeStore.retrieveAttribute((WebRequest)webRequest, name);
/* 774 */       if (bindObject == null) {
/* 775 */         raiseSessionRequiredException("Session attribute '" + name + "' required - not found in session");
/*     */       }
/*     */     } else {
/*     */       
/* 779 */       bindObject = BeanUtils.instantiateClass(paramType);
/*     */     } 
/* 781 */     WebDataBinder binder = createBinder(webRequest, bindObject, name);
/* 782 */     initBinder(handler, name, binder, webRequest);
/* 783 */     return binder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isBindingCandidate(Object value) {
/* 792 */     return (value != null && !value.getClass().isArray() && !(value instanceof java.util.Collection) && !(value instanceof Map) && 
/* 793 */       !BeanUtils.isSimpleValueType(value.getClass()));
/*     */   }
/*     */   
/*     */   protected void raiseMissingParameterException(String paramName, Class<?> paramType) throws Exception {
/* 797 */     throw new IllegalStateException("Missing parameter '" + paramName + "' of type [" + paramType.getName() + "]");
/*     */   }
/*     */   
/*     */   protected void raiseMissingHeaderException(String headerName, Class<?> paramType) throws Exception {
/* 801 */     throw new IllegalStateException("Missing header '" + headerName + "' of type [" + paramType.getName() + "]");
/*     */   }
/*     */   
/*     */   protected void raiseMissingCookieException(String cookieName, Class<?> paramType) throws Exception {
/* 805 */     throw new IllegalStateException("Missing cookie value '" + cookieName + "' of type [" + paramType
/* 806 */         .getName() + "]");
/*     */   }
/*     */   
/*     */   protected void raiseSessionRequiredException(String message) throws Exception {
/* 810 */     throw new IllegalStateException(message);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName) throws Exception {
/* 816 */     return (WebDataBinder)new WebRequestDataBinder(target, objectName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void doBind(WebDataBinder binder, NativeWebRequest webRequest, boolean validate, Object[] validationHints, boolean failOnErrors) throws Exception {
/* 822 */     doBind(binder, webRequest);
/* 823 */     if (validate) {
/* 824 */       binder.validate(validationHints);
/*     */     }
/* 826 */     if (failOnErrors && binder.getBindingResult().hasErrors()) {
/* 827 */       throw new BindException(binder.getBindingResult());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doBind(WebDataBinder binder, NativeWebRequest webRequest) throws Exception {
/* 832 */     ((WebRequestDataBinder)binder).bind((WebRequest)webRequest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest) throws Exception {
/* 840 */     throw new UnsupportedOperationException("@RequestBody not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest) throws Exception {
/* 848 */     throw new UnsupportedOperationException("@Body not supported");
/*     */   }
/*     */   
/*     */   protected String parseDefaultValueAttribute(String value) {
/* 852 */     return "\n\t\t\n\t\t\n\n\t\t\t\t\n".equals(value) ? null : value;
/*     */   }
/*     */   
/*     */   protected Object resolveDefaultValue(String value) {
/* 856 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object resolveCommonArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
/* 863 */     if (this.customArgumentResolvers != null) {
/* 864 */       for (WebArgumentResolver argumentResolver : this.customArgumentResolvers) {
/* 865 */         Object object = argumentResolver.resolveArgument(methodParameter, webRequest);
/* 866 */         if (object != WebArgumentResolver.UNRESOLVED) {
/* 867 */           return object;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 873 */     Class<?> paramType = methodParameter.getParameterType();
/* 874 */     Object value = resolveStandardArgument(paramType, webRequest);
/* 875 */     if (value != WebArgumentResolver.UNRESOLVED && !ClassUtils.isAssignableValue(paramType, value)) {
/* 876 */       throw new IllegalStateException("Standard argument type [" + paramType.getName() + "] resolved to incompatible value of type [" + ((value != null) ? value
/* 877 */           .getClass() : null) + "]. Consider declaring the argument type in a less specific fashion.");
/*     */     }
/*     */     
/* 880 */     return value;
/*     */   }
/*     */   
/*     */   protected Object resolveStandardArgument(Class<?> parameterType, NativeWebRequest webRequest) throws Exception {
/* 884 */     if (WebRequest.class.isAssignableFrom(parameterType)) {
/* 885 */       return webRequest;
/*     */     }
/* 887 */     return WebArgumentResolver.UNRESOLVED;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void addReturnValueAsModelAttribute(Method handlerMethod, Class<?> handlerType, Object returnValue, ExtendedModelMap implicitModel) {
/* 893 */     ModelAttribute attr = (ModelAttribute)AnnotationUtils.findAnnotation(handlerMethod, ModelAttribute.class);
/* 894 */     String attrName = (attr != null) ? attr.value() : "";
/* 895 */     if ("".equals(attrName)) {
/* 896 */       Class<?> resolvedType = GenericTypeResolver.resolveReturnType(handlerMethod, handlerType);
/* 897 */       attrName = Conventions.getVariableNameForReturnType(handlerMethod, resolvedType, returnValue);
/*     */     } 
/* 899 */     implicitModel.addAttribute(attrName, returnValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\annotation\support\HandlerMethodInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */