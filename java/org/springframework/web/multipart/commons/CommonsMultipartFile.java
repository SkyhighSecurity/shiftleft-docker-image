/*     */ package org.springframework.web.multipart.commons;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.fileupload.FileItem;
/*     */ import org.apache.commons.fileupload.FileUploadException;
/*     */ import org.apache.commons.fileupload.disk.DiskFileItem;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ public class CommonsMultipartFile
/*     */   implements MultipartFile, Serializable
/*     */ {
/*  44 */   protected static final Log logger = LogFactory.getLog(CommonsMultipartFile.class);
/*     */ 
/*     */   
/*     */   private final FileItem fileItem;
/*     */ 
/*     */   
/*     */   private final long size;
/*     */ 
/*     */   
/*     */   private boolean preserveFilename = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public CommonsMultipartFile(FileItem fileItem) {
/*  58 */     this.fileItem = fileItem;
/*  59 */     this.size = this.fileItem.getSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FileItem getFileItem() {
/*  68 */     return this.fileItem;
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
/*  82 */     this.preserveFilename = preserveFilename;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  88 */     return this.fileItem.getFieldName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOriginalFilename() {
/*  93 */     String filename = this.fileItem.getName();
/*  94 */     if (filename == null)
/*     */     {
/*  96 */       return "";
/*     */     }
/*  98 */     if (this.preserveFilename)
/*     */     {
/* 100 */       return filename;
/*     */     }
/*     */ 
/*     */     
/* 104 */     int unixSep = filename.lastIndexOf('/');
/*     */     
/* 106 */     int winSep = filename.lastIndexOf('\\');
/*     */     
/* 108 */     int pos = (winSep > unixSep) ? winSep : unixSep;
/* 109 */     if (pos != -1)
/*     */     {
/* 111 */       return filename.substring(pos + 1);
/*     */     }
/*     */ 
/*     */     
/* 115 */     return filename;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 121 */     return this.fileItem.getContentType();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 126 */     return (this.size == 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 131 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/* 136 */     if (!isAvailable()) {
/* 137 */       throw new IllegalStateException("File has been moved - cannot be read again");
/*     */     }
/* 139 */     byte[] bytes = this.fileItem.get();
/* 140 */     return (bytes != null) ? bytes : new byte[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 145 */     if (!isAvailable()) {
/* 146 */       throw new IllegalStateException("File has been moved - cannot be read again");
/*     */     }
/* 148 */     InputStream inputStream = this.fileItem.getInputStream();
/* 149 */     return (inputStream != null) ? inputStream : StreamUtils.emptyInput();
/*     */   }
/*     */ 
/*     */   
/*     */   public void transferTo(File dest) throws IOException, IllegalStateException {
/* 154 */     if (!isAvailable()) {
/* 155 */       throw new IllegalStateException("File has already been moved - cannot be transferred again");
/*     */     }
/*     */     
/* 158 */     if (dest.exists() && !dest.delete()) {
/* 159 */       throw new IOException("Destination file [" + dest
/* 160 */           .getAbsolutePath() + "] already exists and could not be deleted");
/*     */     }
/*     */     
/*     */     try {
/* 164 */       this.fileItem.write(dest);
/* 165 */       if (logger.isDebugEnabled()) {
/* 166 */         String action = "transferred";
/* 167 */         if (!this.fileItem.isInMemory()) {
/* 168 */           action = isAvailable() ? "copied" : "moved";
/*     */         }
/* 170 */         logger.debug("Multipart file '" + getName() + "' with original filename [" + 
/* 171 */             getOriginalFilename() + "], stored " + getStorageDescription() + ": " + action + " to [" + dest
/* 172 */             .getAbsolutePath() + "]");
/*     */       }
/*     */     
/* 175 */     } catch (FileUploadException ex) {
/* 176 */       throw new IllegalStateException(ex.getMessage(), ex);
/*     */     }
/* 178 */     catch (IllegalStateException ex) {
/*     */       
/* 180 */       throw ex;
/*     */     }
/* 182 */     catch (IOException ex) {
/*     */       
/* 184 */       throw ex;
/*     */     }
/* 186 */     catch (Exception ex) {
/* 187 */       throw new IOException("File transfer failed", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAvailable() {
/* 197 */     if (this.fileItem.isInMemory()) {
/* 198 */       return true;
/*     */     }
/*     */     
/* 201 */     if (this.fileItem instanceof DiskFileItem) {
/* 202 */       return ((DiskFileItem)this.fileItem).getStoreLocation().exists();
/*     */     }
/*     */     
/* 205 */     return (this.fileItem.getSize() == this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStorageDescription() {
/* 214 */     if (this.fileItem.isInMemory()) {
/* 215 */       return "in memory";
/*     */     }
/* 217 */     if (this.fileItem instanceof DiskFileItem) {
/* 218 */       return "at [" + ((DiskFileItem)this.fileItem).getStoreLocation().getAbsolutePath() + "]";
/*     */     }
/*     */     
/* 221 */     return "on disk";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\commons\CommonsMultipartFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */