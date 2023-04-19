/*     */ package org.springframework.http.converter.protobuf;
/*     */ 
/*     */ import com.google.protobuf.ExtensionRegistry;
/*     */ import com.google.protobuf.ExtensionRegistryLite;
/*     */ import com.google.protobuf.Message;
/*     */ import com.google.protobuf.MessageOrBuilder;
/*     */ import com.google.protobuf.TextFormat;
/*     */ import com.googlecode.protobuf.format.HtmlFormat;
/*     */ import com.googlecode.protobuf.format.JsonFormat;
/*     */ import com.googlecode.protobuf.format.ProtobufFormatter;
/*     */ import com.googlecode.protobuf.format.XmlFormat;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProtobufHttpMessageConverter
/*     */   extends AbstractHttpMessageConverter<Message>
/*     */ {
/*  60 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*  62 */   public static final MediaType PROTOBUF = new MediaType("application", "x-protobuf", DEFAULT_CHARSET);
/*     */ 
/*     */   
/*     */   public static final String X_PROTOBUF_SCHEMA_HEADER = "X-Protobuf-Schema";
/*     */   
/*     */   public static final String X_PROTOBUF_MESSAGE_HEADER = "X-Protobuf-Message";
/*     */   
/*  69 */   private static final ProtobufFormatter JSON_FORMAT = (ProtobufFormatter)new JsonFormat();
/*     */   
/*  71 */   private static final ProtobufFormatter XML_FORMAT = (ProtobufFormatter)new XmlFormat();
/*     */   
/*  73 */   private static final ProtobufFormatter HTML_FORMAT = (ProtobufFormatter)new HtmlFormat();
/*     */ 
/*     */   
/*  76 */   private static final ConcurrentHashMap<Class<?>, Method> methodCache = new ConcurrentHashMap<Class<?>, Method>();
/*     */   
/*  78 */   private final ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtobufHttpMessageConverter() {
/*  85 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtobufHttpMessageConverter(ExtensionRegistryInitializer registryInitializer) {
/*  93 */     super(new MediaType[] { PROTOBUF, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML });
/*  94 */     if (registryInitializer != null) {
/*  95 */       registryInitializer.initializeExtensionRegistry(this.extensionRegistry);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/* 102 */     return Message.class.isAssignableFrom(clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MediaType getDefaultContentType(Message message) {
/* 107 */     return PROTOBUF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Message readInternal(Class<? extends Message> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 114 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/* 115 */     if (contentType == null) {
/* 116 */       contentType = PROTOBUF;
/*     */     }
/* 118 */     Charset charset = contentType.getCharset();
/* 119 */     if (charset == null) {
/* 120 */       charset = DEFAULT_CHARSET;
/*     */     }
/*     */     
/*     */     try {
/* 124 */       Message.Builder builder = getMessageBuilder(clazz);
/* 125 */       if (MediaType.TEXT_PLAIN.isCompatibleWith(contentType)) {
/* 126 */         InputStreamReader reader = new InputStreamReader(inputMessage.getBody(), charset);
/* 127 */         TextFormat.merge(reader, this.extensionRegistry, builder);
/*     */       }
/* 129 */       else if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
/* 130 */         JSON_FORMAT.merge(inputMessage.getBody(), charset, this.extensionRegistry, builder);
/*     */       }
/* 132 */       else if (MediaType.APPLICATION_XML.isCompatibleWith(contentType)) {
/* 133 */         XML_FORMAT.merge(inputMessage.getBody(), charset, this.extensionRegistry, builder);
/*     */       } else {
/*     */         
/* 136 */         builder.mergeFrom(inputMessage.getBody(), (ExtensionRegistryLite)this.extensionRegistry);
/*     */       } 
/* 138 */       return builder.build();
/*     */     }
/* 140 */     catch (Exception ex) {
/* 141 */       throw new HttpMessageNotReadableException("Could not read Protobuf message: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canWrite(MediaType mediaType) {
/* 151 */     return (super.canWrite(mediaType) || MediaType.TEXT_HTML.isCompatibleWith(mediaType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(Message message, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 158 */     MediaType contentType = outputMessage.getHeaders().getContentType();
/* 159 */     if (contentType == null) {
/* 160 */       contentType = getDefaultContentType(message);
/*     */     }
/* 162 */     Charset charset = contentType.getCharset();
/* 163 */     if (charset == null) {
/* 164 */       charset = DEFAULT_CHARSET;
/*     */     }
/*     */     
/* 167 */     if (MediaType.TEXT_PLAIN.isCompatibleWith(contentType)) {
/* 168 */       OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputMessage.getBody(), charset);
/* 169 */       TextFormat.print((MessageOrBuilder)message, outputStreamWriter);
/* 170 */       outputStreamWriter.flush();
/*     */     }
/* 172 */     else if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
/* 173 */       JSON_FORMAT.print(message, outputMessage.getBody(), charset);
/*     */     }
/* 175 */     else if (MediaType.APPLICATION_XML.isCompatibleWith(contentType)) {
/* 176 */       XML_FORMAT.print(message, outputMessage.getBody(), charset);
/*     */     }
/* 178 */     else if (MediaType.TEXT_HTML.isCompatibleWith(contentType)) {
/* 179 */       HTML_FORMAT.print(message, outputMessage.getBody(), charset);
/*     */     }
/* 181 */     else if (PROTOBUF.isCompatibleWith(contentType)) {
/* 182 */       setProtoHeader(outputMessage, message);
/* 183 */       FileCopyUtils.copy(message.toByteArray(), outputMessage.getBody());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setProtoHeader(HttpOutputMessage response, Message message) {
/* 194 */     response.getHeaders().set("X-Protobuf-Schema", message.getDescriptorForType().getFile().getName());
/* 195 */     response.getHeaders().set("X-Protobuf-Message", message.getDescriptorForType().getFullName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Message.Builder getMessageBuilder(Class<? extends Message> clazz) throws Exception {
/* 204 */     Method method = methodCache.get(clazz);
/* 205 */     if (method == null) {
/* 206 */       method = clazz.getMethod("newBuilder", new Class[0]);
/* 207 */       methodCache.put(clazz, method);
/*     */     } 
/* 209 */     return (Message.Builder)method.invoke(clazz, new Object[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\protobuf\ProtobufHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */