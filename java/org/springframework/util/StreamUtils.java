/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StreamUtils
/*     */ {
/*     */   public static final int BUFFER_SIZE = 4096;
/*  48 */   private static final byte[] EMPTY_CONTENT = new byte[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] copyToByteArray(InputStream in) throws IOException {
/*  59 */     if (in == null) {
/*  60 */       return new byte[0];
/*     */     }
/*     */     
/*  63 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/*  64 */     copy(in, out);
/*  65 */     return out.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String copyToString(InputStream in, Charset charset) throws IOException {
/*  76 */     if (in == null) {
/*  77 */       return "";
/*     */     }
/*     */     
/*  80 */     StringBuilder out = new StringBuilder();
/*  81 */     InputStreamReader reader = new InputStreamReader(in, charset);
/*  82 */     char[] buffer = new char[4096];
/*  83 */     int bytesRead = -1;
/*  84 */     while ((bytesRead = reader.read(buffer)) != -1) {
/*  85 */       out.append(buffer, 0, bytesRead);
/*     */     }
/*  87 */     return out.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(byte[] in, OutputStream out) throws IOException {
/*  98 */     Assert.notNull(in, "No input byte array specified");
/*  99 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 101 */     out.write(in);
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
/*     */   public static void copy(String in, Charset charset, OutputStream out) throws IOException {
/* 113 */     Assert.notNull(in, "No input String specified");
/* 114 */     Assert.notNull(charset, "No charset specified");
/* 115 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 117 */     Writer writer = new OutputStreamWriter(out, charset);
/* 118 */     writer.write(in);
/* 119 */     writer.flush();
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
/*     */   public static int copy(InputStream in, OutputStream out) throws IOException {
/* 131 */     Assert.notNull(in, "No InputStream specified");
/* 132 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 134 */     int byteCount = 0;
/* 135 */     byte[] buffer = new byte[4096];
/* 136 */     int bytesRead = -1;
/* 137 */     while ((bytesRead = in.read(buffer)) != -1) {
/* 138 */       out.write(buffer, 0, bytesRead);
/* 139 */       byteCount += bytesRead;
/*     */     } 
/* 141 */     out.flush();
/* 142 */     return byteCount;
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
/*     */   public static long copyRange(InputStream in, OutputStream out, long start, long end) throws IOException {
/* 159 */     Assert.notNull(in, "No InputStream specified");
/* 160 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 162 */     long skipped = in.skip(start);
/* 163 */     if (skipped < start) {
/* 164 */       throw new IOException("Skipped only " + skipped + " bytes out of " + start + " required");
/*     */     }
/*     */     
/* 167 */     long bytesToCopy = end - start + 1L;
/* 168 */     byte[] buffer = new byte[4096];
/* 169 */     while (bytesToCopy > 0L) {
/* 170 */       int bytesRead = in.read(buffer);
/* 171 */       if (bytesRead == -1) {
/*     */         break;
/*     */       }
/* 174 */       if (bytesRead <= bytesToCopy) {
/* 175 */         out.write(buffer, 0, bytesRead);
/* 176 */         bytesToCopy -= bytesRead;
/*     */         continue;
/*     */       } 
/* 179 */       out.write(buffer, 0, (int)bytesToCopy);
/* 180 */       bytesToCopy = 0L;
/*     */     } 
/*     */     
/* 183 */     return end - start + 1L - bytesToCopy;
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
/*     */   public static int drain(InputStream in) throws IOException {
/* 195 */     Assert.notNull(in, "No InputStream specified");
/* 196 */     byte[] buffer = new byte[4096];
/* 197 */     int bytesRead = -1;
/* 198 */     int byteCount = 0;
/* 199 */     while ((bytesRead = in.read(buffer)) != -1) {
/* 200 */       byteCount += bytesRead;
/*     */     }
/* 202 */     return byteCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream emptyInput() {
/* 211 */     return new ByteArrayInputStream(EMPTY_CONTENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream nonClosing(InputStream in) {
/* 221 */     Assert.notNull(in, "No InputStream specified");
/* 222 */     return new NonClosingInputStream(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OutputStream nonClosing(OutputStream out) {
/* 232 */     Assert.notNull(out, "No OutputStream specified");
/* 233 */     return new NonClosingOutputStream(out);
/*     */   }
/*     */   
/*     */   private static class NonClosingInputStream
/*     */     extends FilterInputStream
/*     */   {
/*     */     public NonClosingInputStream(InputStream in) {
/* 240 */       super(in);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class NonClosingOutputStream
/*     */     extends FilterOutputStream
/*     */   {
/*     */     public NonClosingOutputStream(OutputStream out) {
/* 252 */       super(out);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int let) throws IOException {
/* 258 */       this.out.write(b, off, let);
/*     */     }
/*     */     
/*     */     public void close() throws IOException {}
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\StreamUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */