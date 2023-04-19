/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RequestPart;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.UriComponentsContributor;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*     */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*     */ import org.springframework.web.multipart.support.MultipartResolutionDelegate;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ import org.springframework.web.util.WebUtils;
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
/*     */ public class RequestParamMethodArgumentResolver
/*     */   extends AbstractNamedValueMethodArgumentResolver
/*     */   implements UriComponentsContributor
/*     */ {
/*  77 */   private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean useDefaultResolution;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestParamMethodArgumentResolver(boolean useDefaultResolution) {
/*  89 */     this.useDefaultResolution = useDefaultResolution;
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
/*     */   public RequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
/* 102 */     super(beanFactory);
/* 103 */     this.useDefaultResolution = useDefaultResolution;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/* 124 */     if (parameter.hasParameterAnnotation(RequestParam.class)) {
/* 125 */       if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
/* 126 */         String paramName = ((RequestParam)parameter.getParameterAnnotation(RequestParam.class)).name();
/* 127 */         return StringUtils.hasText(paramName);
/*     */       } 
/*     */       
/* 130 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if (parameter.hasParameterAnnotation(RequestPart.class)) {
/* 135 */       return false;
/*     */     }
/* 137 */     parameter = parameter.nestedIfOptional();
/* 138 */     if (MultipartResolutionDelegate.isMultipartArgument(parameter)) {
/* 139 */       return true;
/*     */     }
/* 141 */     if (this.useDefaultResolution) {
/* 142 */       return BeanUtils.isSimpleProperty(parameter.getNestedParameterType());
/*     */     }
/*     */     
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
/* 152 */     RequestParam ann = (RequestParam)parameter.getParameterAnnotation(RequestParam.class);
/* 153 */     return (ann != null) ? new RequestParamNamedValueInfo(ann) : new RequestParamNamedValueInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/* 158 */     HttpServletRequest servletRequest = (HttpServletRequest)request.getNativeRequest(HttpServletRequest.class);
/*     */     
/* 160 */     MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)WebUtils.getNativeRequest((ServletRequest)servletRequest, MultipartHttpServletRequest.class);
/*     */     
/* 162 */     Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest);
/* 163 */     if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
/* 164 */       return mpArg;
/*     */     }
/*     */     
/* 167 */     Object arg = null;
/* 168 */     if (multipartRequest != null) {
/* 169 */       List<MultipartFile> files = multipartRequest.getFiles(name);
/* 170 */       if (!files.isEmpty()) {
/* 171 */         arg = (files.size() == 1) ? files.get(0) : files;
/*     */       }
/*     */     } 
/* 174 */     if (arg == null) {
/* 175 */       String[] paramValues = request.getParameterValues(name);
/* 176 */       if (paramValues != null) {
/* 177 */         arg = (paramValues.length == 1) ? paramValues[0] : paramValues;
/*     */       }
/*     */     } 
/* 180 */     return arg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMissingValue(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/* 187 */     HttpServletRequest servletRequest = (HttpServletRequest)request.getNativeRequest(HttpServletRequest.class);
/* 188 */     if (MultipartResolutionDelegate.isMultipartArgument(parameter)) {
/* 189 */       if (!MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
/* 190 */         throw new MultipartException("Current request is not a multipart request");
/*     */       }
/*     */       
/* 193 */       throw new MissingServletRequestPartException(name);
/*     */     } 
/*     */ 
/*     */     
/* 197 */     throw new MissingServletRequestParameterException(name, parameter
/* 198 */         .getNestedParameterType().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService) {
/* 206 */     Class<?> paramType = parameter.getNestedParameterType();
/* 207 */     if (Map.class.isAssignableFrom(paramType) || MultipartFile.class == paramType || "javax.servlet.http.Part"
/* 208 */       .equals(paramType.getName())) {
/*     */       return;
/*     */     }
/*     */     
/* 212 */     RequestParam requestParam = (RequestParam)parameter.getParameterAnnotation(RequestParam.class);
/*     */     
/* 214 */     String name = (requestParam == null || StringUtils.isEmpty(requestParam.name())) ? parameter.getParameterName() : requestParam.name();
/*     */     
/* 216 */     if (value == null) {
/* 217 */       if (requestParam != null && (
/* 218 */         !requestParam.required() || !requestParam.defaultValue().equals("\n\t\t\n\t\t\n\n\t\t\t\t\n"))) {
/*     */         return;
/*     */       }
/*     */       
/* 222 */       builder.queryParam(name, new Object[0]);
/*     */     }
/* 224 */     else if (value instanceof java.util.Collection) {
/* 225 */       for (Object element : value) {
/* 226 */         element = formatUriValue(conversionService, TypeDescriptor.nested(parameter, 1), element);
/* 227 */         builder.queryParam(name, new Object[] { element });
/*     */       } 
/*     */     } else {
/*     */       
/* 231 */       builder.queryParam(name, new Object[] { formatUriValue(conversionService, new TypeDescriptor(parameter), value) });
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String formatUriValue(ConversionService cs, TypeDescriptor sourceType, Object value) {
/* 236 */     if (value == null) {
/* 237 */       return null;
/*     */     }
/* 239 */     if (value instanceof String) {
/* 240 */       return (String)value;
/*     */     }
/* 242 */     if (cs != null) {
/* 243 */       return (String)cs.convert(value, sourceType, STRING_TYPE_DESCRIPTOR);
/*     */     }
/*     */     
/* 246 */     return value.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RequestParamNamedValueInfo
/*     */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*     */   {
/*     */     public RequestParamNamedValueInfo() {
/* 254 */       super("", false, "\n\t\t\n\t\t\n\n\t\t\t\t\n");
/*     */     }
/*     */     
/*     */     public RequestParamNamedValueInfo(RequestParam annotation) {
/* 258 */       super(annotation.name(), annotation.required(), annotation.defaultValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\RequestParamMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */