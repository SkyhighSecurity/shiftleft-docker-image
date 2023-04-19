/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import org.springframework.http.MediaType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingJackson2HttpMessageConverter
/*     */   extends AbstractJackson2HttpMessageConverter
/*     */ {
/*     */   private String jsonPrefix;
/*     */   
/*     */   public MappingJackson2HttpMessageConverter() {
/*  57 */     this(Jackson2ObjectMapperBuilder.json().build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
/*  66 */     super(objectMapper, new MediaType[] { MediaType.APPLICATION_JSON, new MediaType("application", "*+json") });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJsonPrefix(String jsonPrefix) {
/*  75 */     this.jsonPrefix = jsonPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefixJson(boolean prefixJson) {
/*  86 */     this.jsonPrefix = prefixJson ? ")]}', " : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
/*  93 */     if (this.jsonPrefix != null) {
/*  94 */       generator.writeRaw(this.jsonPrefix);
/*     */     }
/*     */     
/*  97 */     String jsonpFunction = (object instanceof MappingJacksonValue) ? ((MappingJacksonValue)object).getJsonpFunction() : null;
/*  98 */     if (jsonpFunction != null) {
/*  99 */       generator.writeRaw("/**/");
/* 100 */       generator.writeRaw(jsonpFunction + "(");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
/* 108 */     String jsonpFunction = (object instanceof MappingJacksonValue) ? ((MappingJacksonValue)object).getJsonpFunction() : null;
/* 109 */     if (jsonpFunction != null)
/* 110 */       generator.writeRaw(");"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\json\MappingJackson2HttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */