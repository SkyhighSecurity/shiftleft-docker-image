/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ContentNegotiationManager
/*     */   implements ContentNegotiationStrategy, MediaTypeFileExtensionResolver
/*     */ {
/*  47 */   private static final List<MediaType> MEDIA_TYPE_ALL = Collections.singletonList(MediaType.ALL);
/*     */ 
/*     */   
/*  50 */   private final List<ContentNegotiationStrategy> strategies = new ArrayList<ContentNegotiationStrategy>();
/*     */   
/*  52 */   private final Set<MediaTypeFileExtensionResolver> resolvers = new LinkedHashSet<MediaTypeFileExtensionResolver>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager(ContentNegotiationStrategy... strategies) {
/*  62 */     this(Arrays.asList(strategies));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager(Collection<ContentNegotiationStrategy> strategies) {
/*  72 */     Assert.notEmpty(strategies, "At least one ContentNegotiationStrategy is expected");
/*  73 */     this.strategies.addAll(strategies);
/*  74 */     for (ContentNegotiationStrategy strategy : this.strategies) {
/*  75 */       if (strategy instanceof MediaTypeFileExtensionResolver) {
/*  76 */         this.resolvers.add((MediaTypeFileExtensionResolver)strategy);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager() {
/*  85 */     this(new ContentNegotiationStrategy[] { new HeaderContentNegotiationStrategy() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ContentNegotiationStrategy> getStrategies() {
/*  94 */     return this.strategies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends ContentNegotiationStrategy> T getStrategy(Class<T> strategyType) {
/* 105 */     for (ContentNegotiationStrategy strategy : getStrategies()) {
/* 106 */       if (strategyType.isInstance(strategy)) {
/* 107 */         return (T)strategy;
/*     */       }
/*     */     } 
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileExtensionResolvers(MediaTypeFileExtensionResolver... resolvers) {
/* 119 */     this.resolvers.addAll(Arrays.asList(resolvers));
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> resolveMediaTypes(NativeWebRequest request) throws HttpMediaTypeNotAcceptableException {
/* 124 */     for (ContentNegotiationStrategy strategy : this.strategies) {
/* 125 */       List<MediaType> mediaTypes = strategy.resolveMediaTypes(request);
/* 126 */       if (mediaTypes.isEmpty() || mediaTypes.equals(MEDIA_TYPE_ALL)) {
/*     */         continue;
/*     */       }
/* 129 */       return mediaTypes;
/*     */     } 
/* 131 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> resolveFileExtensions(MediaType mediaType) {
/* 136 */     Set<String> result = new LinkedHashSet<String>();
/* 137 */     for (MediaTypeFileExtensionResolver resolver : this.resolvers) {
/* 138 */       result.addAll(resolver.resolveFileExtensions(mediaType));
/*     */     }
/* 140 */     return new ArrayList<String>(result);
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
/*     */   public List<String> getAllFileExtensions() {
/* 155 */     Set<String> result = new LinkedHashSet<String>();
/* 156 */     for (MediaTypeFileExtensionResolver resolver : this.resolvers) {
/* 157 */       result.addAll(resolver.getAllFileExtensions());
/*     */     }
/* 159 */     return new ArrayList<String>(result);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\ContentNegotiationManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */