/*    */ package org.springframework.web.accept;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
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
/*    */ 
/*    */ public class ParameterContentNegotiationStrategy
/*    */   extends AbstractMappingContentNegotiationStrategy
/*    */ {
/* 38 */   private static final Log logger = LogFactory.getLog(ParameterContentNegotiationStrategy.class);
/*    */   
/* 40 */   private String parameterName = "format";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParameterContentNegotiationStrategy(Map<String, MediaType> mediaTypes) {
/* 47 */     super(mediaTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setParameterName(String parameterName) {
/* 56 */     Assert.notNull(parameterName, "'parameterName' is required");
/* 57 */     this.parameterName = parameterName;
/*    */   }
/*    */   
/*    */   public String getParameterName() {
/* 61 */     return this.parameterName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getMediaTypeKey(NativeWebRequest request) {
/* 67 */     return request.getParameter(getParameterName());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleMatch(String mediaTypeKey, MediaType mediaType) {
/* 72 */     if (logger.isDebugEnabled()) {
/* 73 */       logger.debug("Requested media type: '" + mediaType + "' based on '" + 
/* 74 */           getParameterName() + "'='" + mediaTypeKey + "'");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected MediaType handleNoMatch(NativeWebRequest request, String key) throws HttpMediaTypeNotAcceptableException {
/* 82 */     throw new HttpMediaTypeNotAcceptableException(getAllMediaTypes());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\accept\ParameterContentNegotiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */