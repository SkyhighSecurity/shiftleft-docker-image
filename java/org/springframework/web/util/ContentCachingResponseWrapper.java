/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.springframework.util.FastByteArrayOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContentCachingResponseWrapper
/*     */   extends HttpServletResponseWrapper
/*     */ {
/*  44 */   private final FastByteArrayOutputStream content = new FastByteArrayOutputStream(1024);
/*     */   
/*  46 */   private final ServletOutputStream outputStream = new ResponseServletOutputStream();
/*     */   
/*     */   private PrintWriter writer;
/*     */   
/*  50 */   private int statusCode = 200;
/*     */ 
/*     */ 
/*     */   
/*     */   private Integer contentLength;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentCachingResponseWrapper(HttpServletResponse response) {
/*  60 */     super(response);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(int sc) {
/*  66 */     super.setStatus(sc);
/*  67 */     this.statusCode = sc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(int sc, String sm) {
/*  73 */     super.setStatus(sc, sm);
/*  74 */     this.statusCode = sc;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendError(int sc) throws IOException {
/*  79 */     copyBodyToResponse(false);
/*     */     try {
/*  81 */       super.sendError(sc);
/*     */     }
/*  83 */     catch (IllegalStateException ex) {
/*     */       
/*  85 */       super.setStatus(sc);
/*     */     } 
/*  87 */     this.statusCode = sc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendError(int sc, String msg) throws IOException {
/*  93 */     copyBodyToResponse(false);
/*     */     try {
/*  95 */       super.sendError(sc, msg);
/*     */     }
/*  97 */     catch (IllegalStateException ex) {
/*     */       
/*  99 */       super.setStatus(sc, msg);
/*     */     } 
/* 101 */     this.statusCode = sc;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRedirect(String location) throws IOException {
/* 106 */     copyBodyToResponse(false);
/* 107 */     super.sendRedirect(location);
/*     */   }
/*     */ 
/*     */   
/*     */   public ServletOutputStream getOutputStream() throws IOException {
/* 112 */     return this.outputStream;
/*     */   }
/*     */ 
/*     */   
/*     */   public PrintWriter getWriter() throws IOException {
/* 117 */     if (this.writer == null) {
/* 118 */       String characterEncoding = getCharacterEncoding();
/* 119 */       this.writer = (characterEncoding != null) ? new ResponsePrintWriter(characterEncoding) : new ResponsePrintWriter("ISO-8859-1");
/*     */     } 
/*     */     
/* 122 */     return this.writer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flushBuffer() throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentLength(int len) {
/* 132 */     if (len > this.content.size()) {
/* 133 */       this.content.resize(len);
/*     */     }
/* 135 */     this.contentLength = Integer.valueOf(len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentLengthLong(long len) {
/* 140 */     if (len > 2147483647L) {
/* 141 */       throw new IllegalArgumentException("Content-Length exceeds ContentCachingResponseWrapper's maximum (2147483647): " + len);
/*     */     }
/*     */     
/* 144 */     int lenInt = (int)len;
/* 145 */     if (lenInt > this.content.size()) {
/* 146 */       this.content.resize(lenInt);
/*     */     }
/* 148 */     this.contentLength = Integer.valueOf(lenInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBufferSize(int size) {
/* 153 */     if (size > this.content.size()) {
/* 154 */       this.content.resize(size);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetBuffer() {
/* 160 */     this.content.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 165 */     super.reset();
/* 166 */     this.content.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusCode() {
/* 173 */     return this.statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getContentAsByteArray() {
/* 180 */     return this.content.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getContentInputStream() {
/* 188 */     return this.content.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getContentSize() {
/* 196 */     return this.content.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyBodyToResponse() throws IOException {
/* 204 */     copyBodyToResponse(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyBodyToResponse(boolean complete) throws IOException {
/* 214 */     if (this.content.size() > 0) {
/* 215 */       HttpServletResponse rawResponse = (HttpServletResponse)getResponse();
/* 216 */       if ((complete || this.contentLength != null) && !rawResponse.isCommitted()) {
/* 217 */         rawResponse.setContentLength(complete ? this.content.size() : this.contentLength.intValue());
/* 218 */         this.contentLength = null;
/*     */       } 
/* 220 */       this.content.writeTo((OutputStream)rawResponse.getOutputStream());
/* 221 */       this.content.reset();
/* 222 */       if (complete)
/* 223 */         super.flushBuffer(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private class ResponseServletOutputStream
/*     */     extends ServletOutputStream
/*     */   {
/*     */     private ResponseServletOutputStream() {}
/*     */     
/*     */     public void write(int b) throws IOException {
/* 233 */       ContentCachingResponseWrapper.this.content.write(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 238 */       ContentCachingResponseWrapper.this.content.write(b, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ResponsePrintWriter
/*     */     extends PrintWriter
/*     */   {
/*     */     public ResponsePrintWriter(String characterEncoding) throws UnsupportedEncodingException {
/* 246 */       super(new OutputStreamWriter((OutputStream)ContentCachingResponseWrapper.this.content, characterEncoding));
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(char[] buf, int off, int len) {
/* 251 */       super.write(buf, off, len);
/* 252 */       flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String s, int off, int len) {
/* 257 */       super.write(s, off, len);
/* 258 */       flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int c) {
/* 263 */       super.write(c);
/* 264 */       flush();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\ContentCachingResponseWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */