/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.FormHttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class HttpPutFormContentFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*  64 */   private FormHttpMessageConverter formConverter = (FormHttpMessageConverter)new AllEncompassingFormHttpMessageConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormConverter(FormHttpMessageConverter converter) {
/*  72 */     Assert.notNull(converter, "FormHttpMessageConverter is required.");
/*  73 */     this.formConverter = converter;
/*     */   }
/*     */   
/*     */   public FormHttpMessageConverter getFormConverter() {
/*  77 */     return this.formConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(Charset charset) {
/*  86 */     this.formConverter.setCharset(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*  94 */     if (("PUT".equals(request.getMethod()) || "PATCH".equals(request.getMethod())) && isFormContentType(request)) {
/*  95 */       ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request)
/*     */         {
/*     */           public InputStream getBody() throws IOException {
/*  98 */             return (InputStream)request.getInputStream();
/*     */           }
/*     */         };
/* 101 */       MultiValueMap<String, String> formParameters = this.formConverter.read(null, (HttpInputMessage)servletServerHttpRequest);
/* 102 */       if (!formParameters.isEmpty()) {
/* 103 */         HttpPutFormContentRequestWrapper httpPutFormContentRequestWrapper = new HttpPutFormContentRequestWrapper(request, formParameters);
/* 104 */         filterChain.doFilter((ServletRequest)httpPutFormContentRequestWrapper, (ServletResponse)response);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 109 */     filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*     */   }
/*     */   
/*     */   private boolean isFormContentType(HttpServletRequest request) {
/* 113 */     String contentType = request.getContentType();
/* 114 */     if (contentType != null) {
/*     */       try {
/* 116 */         MediaType mediaType = MediaType.parseMediaType(contentType);
/* 117 */         return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType);
/*     */       }
/* 119 */       catch (IllegalArgumentException ex) {
/* 120 */         return false;
/*     */       } 
/*     */     }
/*     */     
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class HttpPutFormContentRequestWrapper
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     private MultiValueMap<String, String> formParameters;
/*     */     
/*     */     public HttpPutFormContentRequestWrapper(HttpServletRequest request, MultiValueMap<String, String> parameters) {
/* 134 */       super(request);
/* 135 */       this.formParameters = (parameters != null) ? parameters : (MultiValueMap<String, String>)new LinkedMultiValueMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getParameter(String name) {
/* 140 */       String queryStringValue = super.getParameter(name);
/* 141 */       String formValue = (String)this.formParameters.getFirst(name);
/* 142 */       return (queryStringValue != null) ? queryStringValue : formValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, String[]> getParameterMap() {
/* 147 */       Map<String, String[]> result = (Map)new LinkedHashMap<String, String>();
/* 148 */       Enumeration<String> names = getParameterNames();
/* 149 */       while (names.hasMoreElements()) {
/* 150 */         String name = names.nextElement();
/* 151 */         result.put(name, getParameterValues(name));
/*     */       } 
/* 153 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Enumeration<String> getParameterNames() {
/* 158 */       Set<String> names = new LinkedHashSet<String>();
/* 159 */       names.addAll(Collections.list(super.getParameterNames()));
/* 160 */       names.addAll(this.formParameters.keySet());
/* 161 */       return Collections.enumeration(names);
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getParameterValues(String name) {
/* 166 */       String[] parameterValues = super.getParameterValues(name);
/* 167 */       List<String> formParam = (List<String>)this.formParameters.get(name);
/* 168 */       if (formParam == null) {
/* 169 */         return parameterValues;
/*     */       }
/* 171 */       if (parameterValues == null || getQueryString() == null) {
/* 172 */         return StringUtils.toStringArray(formParam);
/*     */       }
/*     */       
/* 175 */       List<String> result = new ArrayList<String>(parameterValues.length + formParam.size());
/* 176 */       result.addAll(Arrays.asList(parameterValues));
/* 177 */       result.addAll(formParam);
/* 178 */       return StringUtils.toStringArray(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\HttpPutFormContentFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */