/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
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
/*     */ public abstract class AbstractMappingContentNegotiationStrategy
/*     */   extends MappingMediaTypeFileExtensionResolver
/*     */   implements ContentNegotiationStrategy
/*     */ {
/*     */   public AbstractMappingContentNegotiationStrategy(Map<String, MediaType> mediaTypes) {
/*  53 */     super(mediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException {
/*  61 */     return resolveMediaTypeKey(webRequest, getMediaTypeKey(webRequest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> resolveMediaTypeKey(NativeWebRequest webRequest, String key) throws HttpMediaTypeNotAcceptableException {
/*  72 */     if (StringUtils.hasText(key)) {
/*  73 */       MediaType mediaType = lookupMediaType(key);
/*  74 */       if (mediaType != null) {
/*  75 */         handleMatch(key, mediaType);
/*  76 */         return Collections.singletonList(mediaType);
/*     */       } 
/*  78 */       mediaType = handleNoMatch(webRequest, key);
/*  79 */       if (mediaType != null) {
/*  80 */         addMapping(key, mediaType);
/*  81 */         return Collections.singletonList(mediaType);
/*     */       } 
/*     */     } 
/*  84 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String getMediaTypeKey(NativeWebRequest paramNativeWebRequest);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMatch(String key, MediaType mediaType) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaType handleNoMatch(NativeWebRequest request, String key) throws HttpMediaTypeNotAcceptableException {
/* 110 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\AbstractMappingContentNegotiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */