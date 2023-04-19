/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Locale;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleHttpInvokerRequestExecutor
/*     */   extends AbstractHttpInvokerRequestExecutor
/*     */ {
/*  48 */   private int connectTimeout = -1;
/*     */   
/*  50 */   private int readTimeout = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int connectTimeout) {
/*  60 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/*  70 */     this.readTimeout = readTimeout;
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
/*     */   
/*     */   protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
/*  89 */     HttpURLConnection con = openConnection(config);
/*  90 */     prepareConnection(con, baos.size());
/*  91 */     writeRequestBody(config, con, baos);
/*  92 */     validateResponse(config, con);
/*  93 */     InputStream responseBody = readResponseBody(config, con);
/*     */     
/*  95 */     return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
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
/*     */   protected HttpURLConnection openConnection(HttpInvokerClientConfiguration config) throws IOException {
/* 107 */     URLConnection con = (new URL(config.getServiceUrl())).openConnection();
/* 108 */     if (!(con instanceof HttpURLConnection)) {
/* 109 */       throw new IOException("Service URL [" + config
/* 110 */           .getServiceUrl() + "] does not resolve to an HTTP connection");
/*     */     }
/* 112 */     return (HttpURLConnection)con;
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
/*     */   protected void prepareConnection(HttpURLConnection connection, int contentLength) throws IOException {
/* 127 */     if (this.connectTimeout >= 0) {
/* 128 */       connection.setConnectTimeout(this.connectTimeout);
/*     */     }
/* 130 */     if (this.readTimeout >= 0) {
/* 131 */       connection.setReadTimeout(this.readTimeout);
/*     */     }
/*     */     
/* 134 */     connection.setDoOutput(true);
/* 135 */     connection.setRequestMethod("POST");
/* 136 */     connection.setRequestProperty("Content-Type", getContentType());
/* 137 */     connection.setRequestProperty("Content-Length", Integer.toString(contentLength));
/*     */     
/* 139 */     LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
/* 140 */     if (localeContext != null) {
/* 141 */       Locale locale = localeContext.getLocale();
/* 142 */       if (locale != null) {
/* 143 */         connection.setRequestProperty("Accept-Language", StringUtils.toLanguageTag(locale));
/*     */       }
/*     */     } 
/*     */     
/* 147 */     if (isAcceptGzipEncoding()) {
/* 148 */       connection.setRequestProperty("Accept-Encoding", "gzip");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRequestBody(HttpInvokerClientConfiguration config, HttpURLConnection con, ByteArrayOutputStream baos) throws IOException {
/* 169 */     baos.writeTo(con.getOutputStream());
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
/*     */   protected void validateResponse(HttpInvokerClientConfiguration config, HttpURLConnection con) throws IOException {
/* 185 */     if (con.getResponseCode() >= 300) {
/* 186 */       throw new IOException("Did not receive successful HTTP response: status code = " + con
/* 187 */           .getResponseCode() + ", status message = [" + con
/* 188 */           .getResponseMessage() + "]");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream readResponseBody(HttpInvokerClientConfiguration config, HttpURLConnection con) throws IOException {
/* 211 */     if (isGzipResponse(con))
/*     */     {
/* 213 */       return new GZIPInputStream(con.getInputStream());
/*     */     }
/*     */ 
/*     */     
/* 217 */     return con.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isGzipResponse(HttpURLConnection con) {
/* 228 */     String encodingHeader = con.getHeaderField("Content-Encoding");
/* 229 */     return (encodingHeader != null && encodingHeader.toLowerCase().contains("gzip"));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\httpinvoker\SimpleHttpInvokerRequestExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */