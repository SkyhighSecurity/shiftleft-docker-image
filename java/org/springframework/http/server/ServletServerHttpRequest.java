/*     */ package org.springframework.http.server;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.Principal;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedCaseInsensitiveMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletServerHttpRequest
/*     */   implements ServerHttpRequest
/*     */ {
/*     */   protected static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
/*     */   protected static final String FORM_CHARSET = "UTF-8";
/*     */   private final HttpServletRequest servletRequest;
/*     */   private URI uri;
/*     */   private HttpHeaders headers;
/*     */   private ServerHttpAsyncRequestControl asyncRequestControl;
/*     */   
/*     */   public ServletServerHttpRequest(HttpServletRequest servletRequest) {
/*  74 */     Assert.notNull(servletRequest, "HttpServletRequest must not be null");
/*  75 */     this.servletRequest = servletRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpServletRequest getServletRequest() {
/*  83 */     return this.servletRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  88 */     return HttpMethod.resolve(this.servletRequest.getMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  93 */     if (this.uri == null) {
/*  94 */       String urlString = null;
/*  95 */       boolean hasQuery = false;
/*     */       try {
/*  97 */         StringBuffer url = this.servletRequest.getRequestURL();
/*  98 */         String query = this.servletRequest.getQueryString();
/*  99 */         hasQuery = StringUtils.hasText(query);
/* 100 */         if (hasQuery) {
/* 101 */           url.append('?').append(query);
/*     */         }
/* 103 */         urlString = url.toString();
/* 104 */         this.uri = new URI(urlString);
/*     */       }
/* 106 */       catch (URISyntaxException ex) {
/* 107 */         if (!hasQuery) {
/* 108 */           throw new IllegalStateException("Could not resolve HttpServletRequest as URI: " + urlString, ex);
/*     */         }
/*     */ 
/*     */         
/*     */         try {
/* 113 */           urlString = this.servletRequest.getRequestURL().toString();
/* 114 */           this.uri = new URI(urlString);
/*     */         }
/* 116 */         catch (URISyntaxException ex2) {
/* 117 */           throw new IllegalStateException("Could not resolve HttpServletRequest as URI: " + urlString, ex2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/* 127 */     if (this.headers == null) {
/* 128 */       this.headers = new HttpHeaders();
/*     */       
/* 130 */       for (Enumeration<?> names = this.servletRequest.getHeaderNames(); names.hasMoreElements(); ) {
/* 131 */         String headerName = (String)names.nextElement();
/* 132 */         Enumeration<?> headerValues = this.servletRequest.getHeaders(headerName);
/* 133 */         while (headerValues.hasMoreElements()) {
/* 134 */           String headerValue = (String)headerValues.nextElement();
/* 135 */           this.headers.add(headerName, headerValue);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 142 */         MediaType contentType = this.headers.getContentType();
/* 143 */         if (contentType == null) {
/* 144 */           String requestContentType = this.servletRequest.getContentType();
/* 145 */           if (StringUtils.hasLength(requestContentType)) {
/* 146 */             contentType = MediaType.parseMediaType(requestContentType);
/* 147 */             this.headers.setContentType(contentType);
/*     */           } 
/*     */         } 
/* 150 */         if (contentType != null && contentType.getCharset() == null) {
/* 151 */           String requestEncoding = this.servletRequest.getCharacterEncoding();
/* 152 */           if (StringUtils.hasLength(requestEncoding)) {
/* 153 */             Charset charSet = Charset.forName(requestEncoding);
/* 154 */             LinkedCaseInsensitiveMap<String, String> linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap();
/* 155 */             linkedCaseInsensitiveMap.putAll(contentType.getParameters());
/* 156 */             linkedCaseInsensitiveMap.put("charset", charSet.toString());
/* 157 */             MediaType mediaType = new MediaType(contentType.getType(), contentType.getSubtype(), (Map)linkedCaseInsensitiveMap);
/* 158 */             this.headers.setContentType(mediaType);
/*     */           }
/*     */         
/*     */         } 
/* 162 */       } catch (InvalidMediaTypeException invalidMediaTypeException) {}
/*     */ 
/*     */ 
/*     */       
/* 166 */       if (this.headers.getContentLength() < 0L) {
/* 167 */         int requestContentLength = this.servletRequest.getContentLength();
/* 168 */         if (requestContentLength != -1) {
/* 169 */           this.headers.setContentLength(requestContentLength);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 174 */     return this.headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getPrincipal() {
/* 179 */     return this.servletRequest.getUserPrincipal();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getLocalAddress() {
/* 184 */     return new InetSocketAddress(this.servletRequest.getLocalName(), this.servletRequest.getLocalPort());
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getRemoteAddress() {
/* 189 */     return new InetSocketAddress(this.servletRequest.getRemoteHost(), this.servletRequest.getRemotePort());
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getBody() throws IOException {
/* 194 */     if (isFormPost(this.servletRequest)) {
/* 195 */       return getBodyFromServletRequestParameters(this.servletRequest);
/*     */     }
/*     */     
/* 198 */     return (InputStream)this.servletRequest.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerHttpAsyncRequestControl getAsyncRequestControl(ServerHttpResponse response) {
/* 204 */     if (this.asyncRequestControl == null) {
/* 205 */       if (!ServletServerHttpResponse.class.isInstance(response)) {
/* 206 */         throw new IllegalArgumentException("Response must be a ServletServerHttpResponse: " + response
/* 207 */             .getClass());
/*     */       }
/* 209 */       ServletServerHttpResponse servletServerResponse = (ServletServerHttpResponse)response;
/* 210 */       this.asyncRequestControl = new ServletServerHttpAsyncRequestControl(this, servletServerResponse);
/*     */     } 
/* 212 */     return this.asyncRequestControl;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isFormPost(HttpServletRequest request) {
/* 217 */     String contentType = request.getContentType();
/* 218 */     return (contentType != null && contentType.contains("application/x-www-form-urlencoded") && HttpMethod.POST
/* 219 */       .matches(request.getMethod()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static InputStream getBodyFromServletRequestParameters(HttpServletRequest request) throws IOException {
/* 229 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
/* 230 */     Writer writer = new OutputStreamWriter(bos, "UTF-8");
/*     */     
/* 232 */     Map<String, String[]> form = request.getParameterMap();
/* 233 */     for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
/* 234 */       String name = nameIterator.next();
/* 235 */       List<String> values = Arrays.asList((Object[])form.get(name));
/* 236 */       for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
/* 237 */         String value = valueIterator.next();
/* 238 */         writer.write(URLEncoder.encode(name, "UTF-8"));
/* 239 */         if (value != null) {
/* 240 */           writer.write(61);
/* 241 */           writer.write(URLEncoder.encode(value, "UTF-8"));
/* 242 */           if (valueIterator.hasNext()) {
/* 243 */             writer.write(38);
/*     */           }
/*     */         } 
/*     */       } 
/* 247 */       if (nameIterator.hasNext()) {
/* 248 */         writer.append('&');
/*     */       }
/*     */     } 
/* 251 */     writer.flush();
/*     */     
/* 253 */     return new ByteArrayInputStream(bos.toByteArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\server\ServletServerHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */