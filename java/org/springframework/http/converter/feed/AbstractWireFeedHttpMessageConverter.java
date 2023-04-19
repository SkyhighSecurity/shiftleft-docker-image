/*    */ package org.springframework.http.converter.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.WireFeed;
/*    */ import com.rometools.rome.io.FeedException;
/*    */ import com.rometools.rome.io.WireFeedInput;
/*    */ import com.rometools.rome.io.WireFeedOutput;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ import java.nio.charset.Charset;
/*    */ import org.springframework.http.HttpInputMessage;
/*    */ import org.springframework.http.HttpOutputMessage;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*    */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*    */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractWireFeedHttpMessageConverter<T extends WireFeed>
/*    */   extends AbstractHttpMessageConverter<T>
/*    */ {
/* 53 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*    */ 
/*    */   
/*    */   protected AbstractWireFeedHttpMessageConverter(MediaType supportedMediaType) {
/* 57 */     super(supportedMediaType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 66 */     WireFeedInput feedInput = new WireFeedInput();
/* 67 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/*    */     
/* 69 */     Charset charset = (contentType != null && contentType.getCharset() != null) ? contentType.getCharset() : DEFAULT_CHARSET;
/*    */     try {
/* 71 */       Reader reader = new InputStreamReader(inputMessage.getBody(), charset);
/* 72 */       return (T)feedInput.build(reader);
/*    */     }
/* 74 */     catch (FeedException ex) {
/* 75 */       throw new HttpMessageNotReadableException("Could not read WireFeed: " + ex.getMessage(), ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeInternal(T wireFeed, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 84 */     Charset charset = StringUtils.hasLength(wireFeed.getEncoding()) ? Charset.forName(wireFeed.getEncoding()) : DEFAULT_CHARSET;
/* 85 */     MediaType contentType = outputMessage.getHeaders().getContentType();
/* 86 */     if (contentType != null) {
/* 87 */       contentType = new MediaType(contentType.getType(), contentType.getSubtype(), charset);
/* 88 */       outputMessage.getHeaders().setContentType(contentType);
/*    */     } 
/*    */     
/* 91 */     WireFeedOutput feedOutput = new WireFeedOutput();
/*    */     try {
/* 93 */       Writer writer = new OutputStreamWriter(outputMessage.getBody(), charset);
/* 94 */       feedOutput.output((WireFeed)wireFeed, writer);
/*    */     }
/* 96 */     catch (FeedException ex) {
/* 97 */       throw new HttpMessageNotWritableException("Could not write WireFeed: " + ex.getMessage(), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\feed\AbstractWireFeedHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */