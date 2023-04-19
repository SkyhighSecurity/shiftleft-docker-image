/*    */ package org.springframework.web.accept;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public class FixedContentNegotiationStrategy
/*    */   implements ContentNegotiationStrategy
/*    */ {
/* 36 */   private static final Log logger = LogFactory.getLog(FixedContentNegotiationStrategy.class);
/*    */ 
/*    */ 
/*    */   
/*    */   private final List<MediaType> contentType;
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedContentNegotiationStrategy(MediaType contentType) {
/* 45 */     this.contentType = Collections.singletonList(contentType);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<MediaType> resolveMediaTypes(NativeWebRequest request) {
/* 51 */     if (logger.isDebugEnabled()) {
/* 52 */       logger.debug("Requested media types: " + this.contentType);
/*    */     }
/* 54 */     return this.contentType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\FixedContentNegotiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */