/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingMediaTypeFileExtensionResolver
/*     */   implements MediaTypeFileExtensionResolver
/*     */ {
/*  44 */   private final ConcurrentMap<String, MediaType> mediaTypes = new ConcurrentHashMap<String, MediaType>(64);
/*     */ 
/*     */   
/*  47 */   private final MultiValueMap<MediaType, String> fileExtensions = (MultiValueMap<MediaType, String>)new LinkedMultiValueMap();
/*     */ 
/*     */   
/*  50 */   private final List<String> allFileExtensions = new LinkedList<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMediaTypeFileExtensionResolver(Map<String, MediaType> mediaTypes) {
/*  57 */     if (mediaTypes != null) {
/*  58 */       for (Map.Entry<String, MediaType> entries : mediaTypes.entrySet()) {
/*  59 */         String extension = ((String)entries.getKey()).toLowerCase(Locale.ENGLISH);
/*  60 */         MediaType mediaType = entries.getValue();
/*  61 */         this.mediaTypes.put(extension, mediaType);
/*  62 */         this.fileExtensions.add(mediaType, extension);
/*  63 */         this.allFileExtensions.add(extension);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, MediaType> getMediaTypes() {
/*  70 */     return this.mediaTypes;
/*     */   }
/*     */   
/*     */   protected List<MediaType> getAllMediaTypes() {
/*  74 */     return new ArrayList<MediaType>(this.mediaTypes.values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addMapping(String extension, MediaType mediaType) {
/*  81 */     MediaType previous = this.mediaTypes.putIfAbsent(extension, mediaType);
/*  82 */     if (previous == null) {
/*  83 */       this.fileExtensions.add(mediaType, extension);
/*  84 */       this.allFileExtensions.add(extension);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> resolveFileExtensions(MediaType mediaType) {
/*  91 */     List<String> fileExtensions = (List<String>)this.fileExtensions.get(mediaType);
/*  92 */     return (fileExtensions != null) ? fileExtensions : Collections.<String>emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getAllFileExtensions() {
/*  97 */     return Collections.unmodifiableList(this.allFileExtensions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaType lookupMediaType(String extension) {
/* 105 */     return this.mediaTypes.get(extension.toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\MappingMediaTypeFileExtensionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */