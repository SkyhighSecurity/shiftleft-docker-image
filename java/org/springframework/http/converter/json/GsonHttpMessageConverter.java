/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonIOException;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
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
/*     */ public class GsonHttpMessageConverter
/*     */   extends AbstractGenericHttpMessageConverter<Object>
/*     */ {
/*  60 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */ 
/*     */   
/*  63 */   private Gson gson = new Gson();
/*     */ 
/*     */ 
/*     */   
/*     */   private String jsonPrefix;
/*     */ 
/*     */ 
/*     */   
/*     */   public GsonHttpMessageConverter() {
/*  72 */     super(new MediaType[] { MediaType.APPLICATION_JSON, new MediaType("application", "*+json") });
/*  73 */     setDefaultCharset(DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGson(Gson gson) {
/*  84 */     Assert.notNull(gson, "A Gson instance is required");
/*  85 */     this.gson = gson;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Gson getGson() {
/*  92 */     return this.gson;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJsonPrefix(String jsonPrefix) {
/* 100 */     this.jsonPrefix = jsonPrefix;
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
/*     */   public void setPrefixJson(boolean prefixJson) {
/* 113 */     this.jsonPrefix = prefixJson ? ")]}', " : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 122 */     TypeToken<?> token = getTypeToken(type);
/* 123 */     return readTypeToken(token, inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 131 */     TypeToken<?> token = getTypeToken(clazz);
/* 132 */     return readTypeToken(token, inputMessage);
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
/*     */   
/*     */   @Deprecated
/*     */   protected TypeToken<?> getTypeToken(Type type) {
/* 156 */     return TypeToken.get(type);
/*     */   }
/*     */   
/*     */   private Object readTypeToken(TypeToken<?> token, HttpInputMessage inputMessage) throws IOException {
/* 160 */     Reader json = new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders()));
/*     */     try {
/* 162 */       return this.gson.fromJson(json, token.getType());
/*     */     }
/* 164 */     catch (JsonParseException ex) {
/* 165 */       throw new HttpMessageNotReadableException("JSON parse error: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Charset getCharset(HttpHeaders headers) {
/* 170 */     if (headers == null || headers.getContentType() == null || headers.getContentType().getCharset() == null) {
/* 171 */       return DEFAULT_CHARSET;
/*     */     }
/* 173 */     return headers.getContentType().getCharset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 180 */     Charset charset = getCharset(outputMessage.getHeaders());
/* 181 */     OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset);
/*     */     try {
/* 183 */       if (this.jsonPrefix != null) {
/* 184 */         writer.append(this.jsonPrefix);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 192 */       if (type instanceof java.lang.reflect.ParameterizedType) {
/* 193 */         this.gson.toJson(o, type, writer);
/*     */       } else {
/*     */         
/* 196 */         this.gson.toJson(o, writer);
/*     */       } 
/*     */       
/* 199 */       writer.flush();
/*     */     }
/* 201 */     catch (JsonIOException ex) {
/* 202 */       throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\json\GsonHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */