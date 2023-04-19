/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.DefaultIndenter;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractJackson2HttpMessageConverter
/*     */   extends AbstractGenericHttpMessageConverter<Object>
/*     */ {
/*  68 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*  70 */   private static final MediaType TEXT_EVENT_STREAM = new MediaType("text", "event-stream");
/*     */ 
/*     */   
/*     */   protected ObjectMapper objectMapper;
/*     */   
/*     */   private Boolean prettyPrint;
/*     */   
/*     */   private PrettyPrinter ssePrettyPrinter;
/*     */ 
/*     */   
/*     */   protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper) {
/*  81 */     init(objectMapper);
/*     */   }
/*     */   
/*     */   protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType supportedMediaType) {
/*  85 */     super(supportedMediaType);
/*  86 */     init(objectMapper);
/*     */   }
/*     */   
/*     */   protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType... supportedMediaTypes) {
/*  90 */     super(supportedMediaTypes);
/*  91 */     init(objectMapper);
/*     */   }
/*     */   
/*     */   protected void init(ObjectMapper objectMapper) {
/*  95 */     this.objectMapper = objectMapper;
/*  96 */     setDefaultCharset(DEFAULT_CHARSET);
/*  97 */     DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
/*  98 */     prettyPrinter.indentObjectsWith((DefaultPrettyPrinter.Indenter)new DefaultIndenter("  ", "\ndata:"));
/*  99 */     this.ssePrettyPrinter = (PrettyPrinter)prettyPrinter;
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
/*     */   public void setObjectMapper(ObjectMapper objectMapper) {
/* 115 */     Assert.notNull(objectMapper, "ObjectMapper must not be null");
/* 116 */     this.objectMapper = objectMapper;
/* 117 */     configurePrettyPrint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMapper getObjectMapper() {
/* 124 */     return this.objectMapper;
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
/*     */   public void setPrettyPrint(boolean prettyPrint) {
/* 137 */     this.prettyPrint = Boolean.valueOf(prettyPrint);
/* 138 */     configurePrettyPrint();
/*     */   }
/*     */   
/*     */   private void configurePrettyPrint() {
/* 142 */     if (this.prettyPrint != null) {
/* 143 */       this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint.booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType) {
/* 150 */     return canRead(clazz, (Class<?>)null, mediaType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
/* 155 */     if (!canRead(mediaType)) {
/* 156 */       return false;
/*     */     }
/* 158 */     JavaType javaType = getJavaType(type, contextClass);
/* 159 */     AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
/* 160 */     if (this.objectMapper.canDeserialize(javaType, causeRef)) {
/* 161 */       return true;
/*     */     }
/* 163 */     logWarningIfNecessary((Type)javaType, causeRef.get());
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType) {
/* 169 */     if (!canWrite(mediaType)) {
/* 170 */       return false;
/*     */     }
/* 172 */     AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
/* 173 */     if (this.objectMapper.canSerialize(clazz, causeRef)) {
/* 174 */       return true;
/*     */     }
/* 176 */     logWarningIfNecessary(clazz, causeRef.get());
/* 177 */     return false;
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
/*     */   protected void logWarningIfNecessary(Type type, Throwable cause) {
/* 189 */     if (cause == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 195 */     boolean debugLevel = (cause instanceof com.fasterxml.jackson.databind.JsonMappingException && (cause.getMessage().startsWith("Can not find") || cause.getMessage().startsWith("Cannot find")));
/*     */     
/* 197 */     if (debugLevel ? this.logger.isDebugEnabled() : this.logger.isWarnEnabled()) {
/* 198 */       String msg = "Failed to evaluate Jackson " + ((type instanceof JavaType) ? "de" : "") + "serialization for type [" + type + "]";
/*     */       
/* 200 */       if (debugLevel) {
/* 201 */         this.logger.debug(msg, cause);
/*     */       }
/* 203 */       else if (this.logger.isDebugEnabled()) {
/* 204 */         this.logger.warn(msg, cause);
/*     */       } else {
/*     */         
/* 207 */         this.logger.warn(msg + ": " + cause);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 216 */     JavaType javaType = getJavaType(clazz, (Class<?>)null);
/* 217 */     return readJavaType(javaType, inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 224 */     JavaType javaType = getJavaType(type, contextClass);
/* 225 */     return readJavaType(javaType, inputMessage);
/*     */   }
/*     */   
/*     */   private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) {
/*     */     try {
/* 230 */       if (inputMessage instanceof MappingJacksonInputMessage) {
/* 231 */         Class<?> deserializationView = ((MappingJacksonInputMessage)inputMessage).getDeserializationView();
/* 232 */         if (deserializationView != null) {
/* 233 */           return this.objectMapper.readerWithView(deserializationView).forType(javaType)
/* 234 */             .readValue(inputMessage.getBody());
/*     */         }
/*     */       } 
/* 237 */       return this.objectMapper.readValue(inputMessage.getBody(), javaType);
/*     */     }
/* 239 */     catch (JsonProcessingException ex) {
/* 240 */       throw new HttpMessageNotReadableException("JSON parse error: " + ex.getOriginalMessage(), ex);
/*     */     }
/* 242 */     catch (IOException ex) {
/* 243 */       throw new HttpMessageNotReadableException("I/O error while reading input message", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 251 */     MediaType contentType = outputMessage.getHeaders().getContentType();
/* 252 */     JsonEncoding encoding = getJsonEncoding(contentType);
/* 253 */     JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
/*     */     try {
/* 255 */       writePrefix(generator, object);
/*     */       
/* 257 */       Object value = object;
/* 258 */       Class<?> serializationView = null;
/* 259 */       FilterProvider filters = null;
/* 260 */       JavaType javaType = null;
/*     */       
/* 262 */       if (object instanceof MappingJacksonValue) {
/* 263 */         MappingJacksonValue container = (MappingJacksonValue)object;
/* 264 */         value = container.getValue();
/* 265 */         serializationView = container.getSerializationView();
/* 266 */         filters = container.getFilters();
/*     */       } 
/* 268 */       if (type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
/* 269 */         javaType = getJavaType(type, (Class<?>)null);
/*     */       }
/*     */ 
/*     */       
/* 273 */       ObjectWriter objectWriter = (serializationView != null) ? this.objectMapper.writerWithView(serializationView) : this.objectMapper.writer();
/* 274 */       if (filters != null) {
/* 275 */         objectWriter = objectWriter.with(filters);
/*     */       }
/* 277 */       if (javaType != null && javaType.isContainerType()) {
/* 278 */         objectWriter = objectWriter.forType(javaType);
/*     */       }
/* 280 */       SerializationConfig config = objectWriter.getConfig();
/* 281 */       if (contentType != null && contentType.isCompatibleWith(TEXT_EVENT_STREAM) && config
/* 282 */         .isEnabled(SerializationFeature.INDENT_OUTPUT)) {
/* 283 */         objectWriter = objectWriter.with(this.ssePrettyPrinter);
/*     */       }
/* 285 */       objectWriter.writeValue(generator, value);
/*     */       
/* 287 */       writeSuffix(generator, object);
/* 288 */       generator.flush();
/*     */     }
/* 290 */     catch (JsonProcessingException ex) {
/* 291 */       throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getOriginalMessage(), ex);
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
/*     */   protected void writePrefix(JsonGenerator generator, Object object) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaType getJavaType(Type type, Class<?> contextClass) {
/* 331 */     TypeFactory typeFactory = this.objectMapper.getTypeFactory();
/* 332 */     if (contextClass != null) {
/* 333 */       ResolvableType resolvedType = ResolvableType.forType(type);
/* 334 */       if (type instanceof TypeVariable) {
/* 335 */         ResolvableType resolvedTypeVariable = resolveVariable((TypeVariable)type, 
/* 336 */             ResolvableType.forClass(contextClass));
/* 337 */         if (resolvedTypeVariable != ResolvableType.NONE) {
/* 338 */           return typeFactory.constructType(resolvedTypeVariable.resolve());
/*     */         }
/*     */       }
/* 341 */       else if (type instanceof ParameterizedType && resolvedType.hasUnresolvableGenerics()) {
/* 342 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 343 */         Class<?>[] generics = new Class[(parameterizedType.getActualTypeArguments()).length];
/* 344 */         Type[] typeArguments = parameterizedType.getActualTypeArguments();
/* 345 */         for (int i = 0; i < typeArguments.length; i++) {
/* 346 */           Type typeArgument = typeArguments[i];
/* 347 */           if (typeArgument instanceof TypeVariable) {
/* 348 */             ResolvableType resolvedTypeArgument = resolveVariable((TypeVariable)typeArgument, 
/* 349 */                 ResolvableType.forClass(contextClass));
/* 350 */             if (resolvedTypeArgument != ResolvableType.NONE) {
/* 351 */               generics[i] = resolvedTypeArgument.resolve();
/*     */             } else {
/*     */               
/* 354 */               generics[i] = ResolvableType.forType(typeArgument).resolve();
/*     */             } 
/*     */           } else {
/*     */             
/* 358 */             generics[i] = ResolvableType.forType(typeArgument).resolve();
/*     */           } 
/*     */         } 
/* 361 */         return typeFactory.constructType(
/* 362 */             ResolvableType.forClassWithGenerics(resolvedType.getRawClass(), generics).getType());
/*     */       } 
/*     */     } 
/* 365 */     return typeFactory.constructType(type);
/*     */   }
/*     */ 
/*     */   
/*     */   private ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {
/* 370 */     if (contextType.hasGenerics()) {
/* 371 */       ResolvableType resolvedType = ResolvableType.forType(typeVariable, contextType);
/* 372 */       if (resolvedType.resolve() != null) {
/* 373 */         return resolvedType;
/*     */       }
/*     */     } 
/*     */     
/* 377 */     ResolvableType superType = contextType.getSuperType();
/* 378 */     if (superType != ResolvableType.NONE) {
/* 379 */       ResolvableType resolvedType = resolveVariable(typeVariable, superType);
/* 380 */       if (resolvedType.resolve() != null) {
/* 381 */         return resolvedType;
/*     */       }
/*     */     } 
/* 384 */     for (ResolvableType ifc : contextType.getInterfaces()) {
/* 385 */       ResolvableType resolvedType = resolveVariable(typeVariable, ifc);
/* 386 */       if (resolvedType.resolve() != null) {
/* 387 */         return resolvedType;
/*     */       }
/*     */     } 
/* 390 */     return ResolvableType.NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonEncoding getJsonEncoding(MediaType contentType) {
/* 399 */     if (contentType != null && contentType.getCharset() != null) {
/* 400 */       Charset charset = contentType.getCharset();
/* 401 */       for (JsonEncoding encoding : JsonEncoding.values()) {
/* 402 */         if (charset.name().equals(encoding.getJavaName())) {
/* 403 */           return encoding;
/*     */         }
/*     */       } 
/*     */     } 
/* 407 */     return JsonEncoding.UTF8;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MediaType getDefaultContentType(Object object) throws IOException {
/* 412 */     if (object instanceof MappingJacksonValue) {
/* 413 */       object = ((MappingJacksonValue)object).getValue();
/*     */     }
/* 415 */     return super.getDefaultContentType(object);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Long getContentLength(Object object, MediaType contentType) throws IOException {
/* 420 */     if (object instanceof MappingJacksonValue) {
/* 421 */       object = ((MappingJacksonValue)object).getValue();
/*     */     }
/* 423 */     return super.getContentLength(object, contentType);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\json\AbstractJackson2HttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */