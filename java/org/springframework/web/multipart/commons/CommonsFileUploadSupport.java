/*     */ package org.springframework.web.multipart.commons;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.fileupload.FileItem;
/*     */ import org.apache.commons.fileupload.FileItemFactory;
/*     */ import org.apache.commons.fileupload.FileUpload;
/*     */ import org.apache.commons.fileupload.disk.DiskFileItemFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CommonsFileUploadSupport
/*     */ {
/*  63 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   private final DiskFileItemFactory fileItemFactory;
/*     */ 
/*     */   
/*     */   private final FileUpload fileUpload;
/*     */ 
/*     */   
/*     */   private boolean uploadTempDirSpecified = false;
/*     */ 
/*     */   
/*     */   private boolean preserveFilename = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public CommonsFileUploadSupport() {
/*  81 */     this.fileItemFactory = newFileItemFactory();
/*  82 */     this.fileUpload = newFileUpload((FileItemFactory)getFileItemFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DiskFileItemFactory getFileItemFactory() {
/*  92 */     return this.fileItemFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileUpload getFileUpload() {
/* 101 */     return this.fileUpload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxUploadSize(long maxUploadSize) {
/* 111 */     this.fileUpload.setSizeMax(maxUploadSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxUploadSizePerFile(long maxUploadSizePerFile) {
/* 122 */     this.fileUpload.setFileSizeMax(maxUploadSizePerFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxInMemorySize(int maxInMemorySize) {
/* 133 */     this.fileItemFactory.setSizeThreshold(maxInMemorySize);
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
/*     */   public void setDefaultEncoding(String defaultEncoding) {
/* 151 */     this.fileUpload.setHeaderEncoding(defaultEncoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDefaultEncoding() {
/* 159 */     String encoding = getFileUpload().getHeaderEncoding();
/* 160 */     if (encoding == null) {
/* 161 */       encoding = "ISO-8859-1";
/*     */     }
/* 163 */     return encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUploadTempDir(Resource uploadTempDir) throws IOException {
/* 172 */     if (!uploadTempDir.exists() && !uploadTempDir.getFile().mkdirs()) {
/* 173 */       throw new IllegalArgumentException("Given uploadTempDir [" + uploadTempDir + "] could not be created");
/*     */     }
/* 175 */     this.fileItemFactory.setRepository(uploadTempDir.getFile());
/* 176 */     this.uploadTempDirSpecified = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isUploadTempDirSpecified() {
/* 184 */     return this.uploadTempDirSpecified;
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
/*     */   public void setPreserveFilename(boolean preserveFilename) {
/* 198 */     this.preserveFilename = preserveFilename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DiskFileItemFactory newFileItemFactory() {
/* 209 */     return new DiskFileItemFactory();
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
/*     */   protected abstract FileUpload newFileUpload(FileItemFactory paramFileItemFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileUpload prepareFileUpload(String encoding) {
/* 230 */     FileUpload fileUpload = getFileUpload();
/* 231 */     FileUpload actualFileUpload = fileUpload;
/*     */ 
/*     */ 
/*     */     
/* 235 */     if (encoding != null && !encoding.equals(fileUpload.getHeaderEncoding())) {
/* 236 */       actualFileUpload = newFileUpload((FileItemFactory)getFileItemFactory());
/* 237 */       actualFileUpload.setSizeMax(fileUpload.getSizeMax());
/* 238 */       actualFileUpload.setFileSizeMax(fileUpload.getFileSizeMax());
/* 239 */       actualFileUpload.setHeaderEncoding(encoding);
/*     */     } 
/*     */     
/* 242 */     return actualFileUpload;
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
/*     */   protected MultipartParsingResult parseFileItems(List<FileItem> fileItems, String encoding) {
/* 254 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 255 */     Map<String, String[]> multipartParameters = (Map)new HashMap<String, String>();
/* 256 */     Map<String, String> multipartParameterContentTypes = new HashMap<String, String>();
/*     */ 
/*     */     
/* 259 */     for (FileItem fileItem : fileItems) {
/* 260 */       if (fileItem.isFormField()) {
/*     */         
/* 262 */         String value, partEncoding = determineEncoding(fileItem.getContentType(), encoding);
/* 263 */         if (partEncoding != null) {
/*     */           try {
/* 265 */             value = fileItem.getString(partEncoding);
/*     */           }
/* 267 */           catch (UnsupportedEncodingException ex) {
/* 268 */             if (this.logger.isWarnEnabled()) {
/* 269 */               this.logger.warn("Could not decode multipart item '" + fileItem.getFieldName() + "' with encoding '" + partEncoding + "': using platform default");
/*     */             }
/*     */             
/* 272 */             value = fileItem.getString();
/*     */           } 
/*     */         } else {
/*     */           
/* 276 */           value = fileItem.getString();
/*     */         } 
/* 278 */         String[] curParam = multipartParameters.get(fileItem.getFieldName());
/* 279 */         if (curParam == null) {
/*     */           
/* 281 */           multipartParameters.put(fileItem.getFieldName(), new String[] { value });
/*     */         }
/*     */         else {
/*     */           
/* 285 */           String[] newParam = StringUtils.addStringToArray(curParam, value);
/* 286 */           multipartParameters.put(fileItem.getFieldName(), newParam);
/*     */         } 
/* 288 */         multipartParameterContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
/*     */         
/*     */         continue;
/*     */       } 
/* 292 */       CommonsMultipartFile file = createMultipartFile(fileItem);
/* 293 */       linkedMultiValueMap.add(file.getName(), file);
/* 294 */       if (this.logger.isDebugEnabled()) {
/* 295 */         this.logger.debug("Found multipart file [" + file.getName() + "] of size " + file.getSize() + " bytes with original filename [" + file
/* 296 */             .getOriginalFilename() + "], stored " + file
/* 297 */             .getStorageDescription());
/*     */       }
/*     */     } 
/*     */     
/* 301 */     return new MultipartParsingResult((MultiValueMap<String, MultipartFile>)linkedMultiValueMap, multipartParameters, multipartParameterContentTypes);
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
/*     */   protected CommonsMultipartFile createMultipartFile(FileItem fileItem) {
/* 313 */     CommonsMultipartFile multipartFile = new CommonsMultipartFile(fileItem);
/* 314 */     multipartFile.setPreserveFilename(this.preserveFilename);
/* 315 */     return multipartFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cleanupFileItems(MultiValueMap<String, MultipartFile> multipartFiles) {
/* 326 */     for (List<MultipartFile> files : (Iterable<List<MultipartFile>>)multipartFiles.values()) {
/* 327 */       for (MultipartFile file : files) {
/* 328 */         if (file instanceof CommonsMultipartFile) {
/* 329 */           CommonsMultipartFile cmf = (CommonsMultipartFile)file;
/* 330 */           cmf.getFileItem().delete();
/* 331 */           if (this.logger.isDebugEnabled()) {
/* 332 */             this.logger.debug("Cleaning up multipart file [" + cmf.getName() + "] with original filename [" + cmf
/* 333 */                 .getOriginalFilename() + "], stored " + cmf.getStorageDescription());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String determineEncoding(String contentTypeHeader, String defaultEncoding) {
/* 341 */     if (!StringUtils.hasText(contentTypeHeader)) {
/* 342 */       return defaultEncoding;
/*     */     }
/* 344 */     MediaType contentType = MediaType.parseMediaType(contentTypeHeader);
/* 345 */     Charset charset = contentType.getCharset();
/* 346 */     return (charset != null) ? charset.name() : defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class MultipartParsingResult
/*     */   {
/*     */     private final MultiValueMap<String, MultipartFile> multipartFiles;
/*     */ 
/*     */     
/*     */     private final Map<String, String[]> multipartParameters;
/*     */ 
/*     */     
/*     */     private final Map<String, String> multipartParameterContentTypes;
/*     */ 
/*     */ 
/*     */     
/*     */     public MultipartParsingResult(MultiValueMap<String, MultipartFile> mpFiles, Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {
/* 365 */       this.multipartFiles = mpFiles;
/* 366 */       this.multipartParameters = mpParams;
/* 367 */       this.multipartParameterContentTypes = mpParamContentTypes;
/*     */     }
/*     */     
/*     */     public MultiValueMap<String, MultipartFile> getMultipartFiles() {
/* 371 */       return this.multipartFiles;
/*     */     }
/*     */     
/*     */     public Map<String, String[]> getMultipartParameters() {
/* 375 */       return this.multipartParameters;
/*     */     }
/*     */     
/*     */     public Map<String, String> getMultipartParameterContentTypes() {
/* 379 */       return this.multipartParameterContentTypes;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\commons\CommonsFileUploadSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */