/*    */ package org.springframework.web.bind.support;
/*    */ 
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.context.request.WebRequest;
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
/*    */ public class DefaultSessionAttributeStore
/*    */   implements SessionAttributeStore
/*    */ {
/* 36 */   private String attributeNamePrefix = "";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAttributeNamePrefix(String attributeNamePrefix) {
/* 45 */     this.attributeNamePrefix = (attributeNamePrefix != null) ? attributeNamePrefix : "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void storeAttribute(WebRequest request, String attributeName, Object attributeValue) {
/* 51 */     Assert.notNull(request, "WebRequest must not be null");
/* 52 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 53 */     Assert.notNull(attributeValue, "Attribute value must not be null");
/* 54 */     String storeAttributeName = getAttributeNameInSession(request, attributeName);
/* 55 */     request.setAttribute(storeAttributeName, attributeValue, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object retrieveAttribute(WebRequest request, String attributeName) {
/* 60 */     Assert.notNull(request, "WebRequest must not be null");
/* 61 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 62 */     String storeAttributeName = getAttributeNameInSession(request, attributeName);
/* 63 */     return request.getAttribute(storeAttributeName, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cleanupAttribute(WebRequest request, String attributeName) {
/* 68 */     Assert.notNull(request, "WebRequest must not be null");
/* 69 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 70 */     String storeAttributeName = getAttributeNameInSession(request, attributeName);
/* 71 */     request.removeAttribute(storeAttributeName, 1);
/*    */   }
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
/*    */   protected String getAttributeNameInSession(WebRequest request, String attributeName) {
/* 84 */     return this.attributeNamePrefix + attributeName;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\support\DefaultSessionAttributeStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */