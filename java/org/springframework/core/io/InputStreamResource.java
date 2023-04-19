/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InputStreamResource
/*     */   extends AbstractResource
/*     */ {
/*     */   private final InputStream inputStream;
/*     */   private final String description;
/*     */   private boolean read = false;
/*     */   
/*     */   public InputStreamResource(InputStream inputStream) {
/*  56 */     this(inputStream, "resource loaded through InputStream");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStreamResource(InputStream inputStream, String description) {
/*  65 */     if (inputStream == null) {
/*  66 */       throw new IllegalArgumentException("InputStream must not be null");
/*     */     }
/*  68 */     this.inputStream = inputStream;
/*  69 */     this.description = (description != null) ? description : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException, IllegalStateException {
/*  95 */     if (this.read) {
/*  96 */       throw new IllegalStateException("InputStream has already been read - do not use InputStreamResource if a stream needs to be read multiple times");
/*     */     }
/*     */     
/*  99 */     this.read = true;
/* 100 */     return this.inputStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 109 */     return "InputStream resource [" + this.description + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 118 */     return (obj == this || (obj instanceof InputStreamResource && ((InputStreamResource)obj).inputStream
/* 119 */       .equals(this.inputStream)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 127 */     return this.inputStream.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\InputStreamResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */