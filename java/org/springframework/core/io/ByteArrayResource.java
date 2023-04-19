/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayResource
/*     */   extends AbstractResource
/*     */ {
/*     */   private final byte[] byteArray;
/*     */   private final String description;
/*     */   
/*     */   public ByteArrayResource(byte[] byteArray) {
/*  54 */     this(byteArray, "resource loaded from byte array");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayResource(byte[] byteArray, String description) {
/*  63 */     Assert.notNull(byteArray, "Byte array must not be null");
/*  64 */     this.byteArray = byteArray;
/*  65 */     this.description = (description != null) ? description : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final byte[] getByteArray() {
/*  73 */     return this.byteArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*  81 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long contentLength() {
/*  89 */     return this.byteArray.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*  99 */     return new ByteArrayInputStream(this.byteArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 108 */     return "Byte array resource [" + this.description + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 118 */     return (obj == this || (obj instanceof ByteArrayResource && 
/* 119 */       Arrays.equals(((ByteArrayResource)obj).byteArray, this.byteArray)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     return byte[].class.hashCode() * 29 * this.byteArray.length;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\ByteArrayResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */