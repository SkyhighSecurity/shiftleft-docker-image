/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import org.springframework.http.HttpMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContentCachingRequestWrapper
/*     */   extends HttpServletRequestWrapper
/*     */ {
/*     */   private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
/*     */   private final ByteArrayOutputStream cachedContent;
/*     */   private final Integer contentCacheLimit;
/*     */   private ServletInputStream inputStream;
/*     */   private BufferedReader reader;
/*     */   
/*     */   public ContentCachingRequestWrapper(HttpServletRequest request) {
/*  66 */     super(request);
/*  67 */     int contentLength = request.getContentLength();
/*  68 */     this.cachedContent = new ByteArrayOutputStream((contentLength >= 0) ? contentLength : 1024);
/*  69 */     this.contentCacheLimit = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentCachingRequestWrapper(HttpServletRequest request, int contentCacheLimit) {
/*  80 */     super(request);
/*  81 */     this.cachedContent = new ByteArrayOutputStream(contentCacheLimit);
/*  82 */     this.contentCacheLimit = Integer.valueOf(contentCacheLimit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletInputStream getInputStream() throws IOException {
/*  88 */     if (this.inputStream == null) {
/*  89 */       this.inputStream = new ContentCachingInputStream(getRequest().getInputStream());
/*     */     }
/*  91 */     return this.inputStream;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCharacterEncoding() {
/*  96 */     String enc = super.getCharacterEncoding();
/*  97 */     return (enc != null) ? enc : "ISO-8859-1";
/*     */   }
/*     */ 
/*     */   
/*     */   public BufferedReader getReader() throws IOException {
/* 102 */     if (this.reader == null) {
/* 103 */       this.reader = new BufferedReader(new InputStreamReader((InputStream)getInputStream(), getCharacterEncoding()));
/*     */     }
/* 105 */     return this.reader;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/* 110 */     if (this.cachedContent.size() == 0 && isFormPost()) {
/* 111 */       writeRequestParametersToCachedContent();
/*     */     }
/* 113 */     return super.getParameter(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 118 */     if (this.cachedContent.size() == 0 && isFormPost()) {
/* 119 */       writeRequestParametersToCachedContent();
/*     */     }
/* 121 */     return super.getParameterMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<String> getParameterNames() {
/* 126 */     if (this.cachedContent.size() == 0 && isFormPost()) {
/* 127 */       writeRequestParametersToCachedContent();
/*     */     }
/* 129 */     return super.getParameterNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getParameterValues(String name) {
/* 134 */     if (this.cachedContent.size() == 0 && isFormPost()) {
/* 135 */       writeRequestParametersToCachedContent();
/*     */     }
/* 137 */     return super.getParameterValues(name);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isFormPost() {
/* 142 */     String contentType = getContentType();
/* 143 */     return (contentType != null && contentType.contains("application/x-www-form-urlencoded") && HttpMethod.POST
/* 144 */       .matches(getMethod()));
/*     */   }
/*     */   
/*     */   private void writeRequestParametersToCachedContent() {
/*     */     try {
/* 149 */       if (this.cachedContent.size() == 0) {
/* 150 */         String requestEncoding = getCharacterEncoding();
/* 151 */         Map<String, String[]> form = super.getParameterMap();
/* 152 */         for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
/* 153 */           String name = nameIterator.next();
/* 154 */           List<String> values = Arrays.asList((Object[])form.get(name));
/* 155 */           for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
/* 156 */             String value = valueIterator.next();
/* 157 */             this.cachedContent.write(URLEncoder.encode(name, requestEncoding).getBytes());
/* 158 */             if (value != null) {
/* 159 */               this.cachedContent.write(61);
/* 160 */               this.cachedContent.write(URLEncoder.encode(value, requestEncoding).getBytes());
/* 161 */               if (valueIterator.hasNext()) {
/* 162 */                 this.cachedContent.write(38);
/*     */               }
/*     */             } 
/*     */           } 
/* 166 */           if (nameIterator.hasNext()) {
/* 167 */             this.cachedContent.write(38);
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 172 */     } catch (IOException ex) {
/* 173 */       throw new IllegalStateException("Failed to write request parameters to cached content", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getContentAsByteArray() {
/* 183 */     return this.cachedContent.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleContentOverflow(int contentCacheLimit) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ContentCachingInputStream
/*     */     extends ServletInputStream
/*     */   {
/*     */     private final ServletInputStream is;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean overflow = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ContentCachingInputStream(ServletInputStream is) {
/* 207 */       this.is = is;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() throws IOException {
/* 212 */       int ch = this.is.read();
/* 213 */       if (ch != -1 && !this.overflow) {
/* 214 */         if (ContentCachingRequestWrapper.this.contentCacheLimit != null && ContentCachingRequestWrapper.this.cachedContent.size() == ContentCachingRequestWrapper.this.contentCacheLimit.intValue()) {
/* 215 */           this.overflow = true;
/* 216 */           ContentCachingRequestWrapper.this.handleContentOverflow(ContentCachingRequestWrapper.this.contentCacheLimit.intValue());
/*     */         } else {
/*     */           
/* 219 */           ContentCachingRequestWrapper.this.cachedContent.write(ch);
/*     */         } 
/*     */       }
/* 222 */       return ch;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\ContentCachingRequestWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */