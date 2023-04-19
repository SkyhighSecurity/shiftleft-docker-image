/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FileCopyUtils
/*     */ {
/*     */   public static final int BUFFER_SIZE = 4096;
/*     */   
/*     */   public static int copy(File in, File out) throws IOException {
/*  61 */     Assert.notNull(in, "No input File specified");
/*  62 */     Assert.notNull(out, "No output File specified");
/*     */     
/*  64 */     return copy(new BufferedInputStream(new FileInputStream(in)), new BufferedOutputStream(new FileOutputStream(out)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(byte[] in, File out) throws IOException {
/*  75 */     Assert.notNull(in, "No input byte array specified");
/*  76 */     Assert.notNull(out, "No output File specified");
/*     */     
/*  78 */     ByteArrayInputStream inStream = new ByteArrayInputStream(in);
/*  79 */     OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
/*  80 */     copy(inStream, outStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] copyToByteArray(File in) throws IOException {
/*  90 */     Assert.notNull(in, "No input File specified");
/*     */     
/*  92 */     return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
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
/*     */   public static int copy(InputStream in, OutputStream out) throws IOException {
/* 109 */     Assert.notNull(in, "No InputStream specified");
/* 110 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/*     */     try {
/* 113 */       return StreamUtils.copy(in, out);
/*     */     } finally {
/*     */       
/*     */       try {
/* 117 */         in.close();
/*     */       }
/* 119 */       catch (IOException iOException) {}
/*     */       
/*     */       try {
/* 122 */         out.close();
/*     */       }
/* 124 */       catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static void copy(byte[] in, OutputStream out) throws IOException {
/* 137 */     Assert.notNull(in, "No input byte array specified");
/* 138 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/*     */     try {
/* 141 */       out.write(in);
/*     */     } finally {
/*     */       
/*     */       try {
/* 145 */         out.close();
/*     */       }
/* 147 */       catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static byte[] copyToByteArray(InputStream in) throws IOException {
/* 160 */     if (in == null) {
/* 161 */       return new byte[0];
/*     */     }
/*     */     
/* 164 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/* 165 */     copy(in, out);
/* 166 */     return out.toByteArray();
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
/*     */   public static int copy(Reader in, Writer out) throws IOException {
/* 183 */     Assert.notNull(in, "No Reader specified");
/* 184 */     Assert.notNull(out, "No Writer specified");
/*     */     
/*     */     try {
/* 187 */       int byteCount = 0;
/* 188 */       char[] buffer = new char[4096];
/* 189 */       int bytesRead = -1;
/* 190 */       while ((bytesRead = in.read(buffer)) != -1) {
/* 191 */         out.write(buffer, 0, bytesRead);
/* 192 */         byteCount += bytesRead;
/*     */       } 
/* 194 */       out.flush();
/* 195 */       return byteCount;
/*     */     } finally {
/*     */       
/*     */       try {
/* 199 */         in.close();
/*     */       }
/* 201 */       catch (IOException iOException) {}
/*     */       
/*     */       try {
/* 204 */         out.close();
/*     */       }
/* 206 */       catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static void copy(String in, Writer out) throws IOException {
/* 219 */     Assert.notNull(in, "No input String specified");
/* 220 */     Assert.notNull(out, "No Writer specified");
/*     */     
/*     */     try {
/* 223 */       out.write(in);
/*     */     } finally {
/*     */       
/*     */       try {
/* 227 */         out.close();
/*     */       }
/* 229 */       catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static String copyToString(Reader in) throws IOException {
/* 242 */     if (in == null) {
/* 243 */       return "";
/*     */     }
/*     */     
/* 246 */     StringWriter out = new StringWriter();
/* 247 */     copy(in, out);
/* 248 */     return out.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\FileCopyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */