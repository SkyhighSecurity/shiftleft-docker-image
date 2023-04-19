/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
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
/*     */ public abstract class MultipartResolutionDelegate
/*     */ {
/*  42 */   public static final Object UNRESOLVABLE = new Object();
/*     */ 
/*     */   
/*  45 */   private static Class<?> servletPartClass = null;
/*     */   
/*     */   static {
/*     */     try {
/*  49 */       servletPartClass = ClassUtils.forName("javax.servlet.http.Part", MultipartResolutionDelegate.class
/*  50 */           .getClassLoader());
/*     */     }
/*  52 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMultipartRequest(HttpServletRequest request) {
/*  60 */     return (WebUtils.getNativeRequest((ServletRequest)request, MultipartHttpServletRequest.class) != null || 
/*  61 */       isMultipartContent(request));
/*     */   }
/*     */   
/*     */   private static boolean isMultipartContent(HttpServletRequest request) {
/*  65 */     String contentType = request.getContentType();
/*  66 */     return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
/*     */   }
/*     */   
/*     */   static MultipartHttpServletRequest asMultipartHttpServletRequest(HttpServletRequest request) {
/*  70 */     MultipartHttpServletRequest unwrapped = (MultipartHttpServletRequest)WebUtils.getNativeRequest((ServletRequest)request, MultipartHttpServletRequest.class);
/*  71 */     if (unwrapped != null) {
/*  72 */       return unwrapped;
/*     */     }
/*  74 */     return adaptToMultipartHttpServletRequest(request);
/*     */   }
/*     */   
/*     */   private static MultipartHttpServletRequest adaptToMultipartHttpServletRequest(HttpServletRequest request) {
/*  78 */     if (servletPartClass != null)
/*     */     {
/*  80 */       return new StandardMultipartHttpServletRequest(request);
/*     */     }
/*  82 */     throw new MultipartException("Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isMultipartArgument(MethodParameter parameter) {
/*  87 */     Class<?> paramType = parameter.getNestedParameterType();
/*  88 */     return (MultipartFile.class == paramType || 
/*  89 */       isMultipartFileCollection(parameter) || isMultipartFileArray(parameter) || (servletPartClass != null && (servletPartClass == paramType || 
/*     */       
/*  91 */       isPartCollection(parameter) || isPartArray(parameter))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object resolveMultipartArgument(String name, MethodParameter parameter, HttpServletRequest request) throws Exception {
/*  98 */     MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)WebUtils.getNativeRequest((ServletRequest)request, MultipartHttpServletRequest.class);
/*  99 */     boolean isMultipart = (multipartRequest != null || isMultipartContent(request));
/*     */     
/* 101 */     if (MultipartFile.class == parameter.getNestedParameterType()) {
/* 102 */       if (multipartRequest == null && isMultipart) {
/* 103 */         multipartRequest = adaptToMultipartHttpServletRequest(request);
/*     */       }
/* 105 */       return (multipartRequest != null) ? multipartRequest.getFile(name) : null;
/*     */     } 
/* 107 */     if (isMultipartFileCollection(parameter)) {
/* 108 */       if (multipartRequest == null && isMultipart) {
/* 109 */         multipartRequest = adaptToMultipartHttpServletRequest(request);
/*     */       }
/* 111 */       return (multipartRequest != null) ? multipartRequest.getFiles(name) : null;
/*     */     } 
/* 113 */     if (isMultipartFileArray(parameter)) {
/* 114 */       if (multipartRequest == null && isMultipart) {
/* 115 */         multipartRequest = adaptToMultipartHttpServletRequest(request);
/*     */       }
/* 117 */       if (multipartRequest != null) {
/* 118 */         List<MultipartFile> multipartFiles = multipartRequest.getFiles(name);
/* 119 */         return multipartFiles.toArray(new MultipartFile[multipartFiles.size()]);
/*     */       } 
/*     */       
/* 122 */       return null;
/*     */     } 
/*     */     
/* 125 */     if (servletPartClass != null) {
/* 126 */       if (servletPartClass == parameter.getNestedParameterType()) {
/* 127 */         return isMultipart ? RequestPartResolver.resolvePart(request, name) : null;
/*     */       }
/* 129 */       if (isPartCollection(parameter)) {
/* 130 */         return isMultipart ? RequestPartResolver.resolvePartList(request, name) : null;
/*     */       }
/* 132 */       if (isPartArray(parameter)) {
/* 133 */         return isMultipart ? RequestPartResolver.resolvePartArray(request, name) : null;
/*     */       }
/*     */     } 
/* 136 */     return UNRESOLVABLE;
/*     */   }
/*     */   
/*     */   private static boolean isMultipartFileCollection(MethodParameter methodParam) {
/* 140 */     return (MultipartFile.class == getCollectionParameterType(methodParam));
/*     */   }
/*     */   
/*     */   private static boolean isMultipartFileArray(MethodParameter methodParam) {
/* 144 */     return (MultipartFile.class == methodParam.getNestedParameterType().getComponentType());
/*     */   }
/*     */   
/*     */   private static boolean isPartCollection(MethodParameter methodParam) {
/* 148 */     return (servletPartClass == getCollectionParameterType(methodParam));
/*     */   }
/*     */   
/*     */   private static boolean isPartArray(MethodParameter methodParam) {
/* 152 */     return (servletPartClass == methodParam.getNestedParameterType().getComponentType());
/*     */   }
/*     */   
/*     */   private static Class<?> getCollectionParameterType(MethodParameter methodParam) {
/* 156 */     Class<?> paramType = methodParam.getNestedParameterType();
/* 157 */     if (Collection.class == paramType || List.class.isAssignableFrom(paramType)) {
/* 158 */       Class<?> valueType = ResolvableType.forMethodParameter(methodParam).asCollection().resolveGeneric(new int[0]);
/* 159 */       if (valueType != null) {
/* 160 */         return valueType;
/*     */       }
/*     */     } 
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RequestPartResolver
/*     */   {
/*     */     public static Object resolvePart(HttpServletRequest servletRequest, String name) throws Exception {
/* 173 */       return servletRequest.getPart(name);
/*     */     }
/*     */     
/*     */     public static Object resolvePartList(HttpServletRequest servletRequest, String name) throws Exception {
/* 177 */       Collection<Part> parts = servletRequest.getParts();
/* 178 */       List<Part> result = new ArrayList<Part>(parts.size());
/* 179 */       for (Part part : parts) {
/* 180 */         if (part.getName().equals(name)) {
/* 181 */           result.add(part);
/*     */         }
/*     */       } 
/* 184 */       return result;
/*     */     }
/*     */     
/*     */     public static Object resolvePartArray(HttpServletRequest servletRequest, String name) throws Exception {
/* 188 */       Collection<Part> parts = servletRequest.getParts();
/* 189 */       List<Part> result = new ArrayList<Part>(parts.size());
/* 190 */       for (Part part : parts) {
/* 191 */         if (part.getName().equals(name)) {
/* 192 */           result.add(part);
/*     */         }
/*     */       } 
/* 195 */       return result.toArray(new Part[result.size()]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\MultipartResolutionDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */