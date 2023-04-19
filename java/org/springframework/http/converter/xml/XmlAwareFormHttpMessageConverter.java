/*    */ package org.springframework.http.converter.xml;
/*    */ 
/*    */ import javax.xml.transform.Source;
/*    */ import org.springframework.http.converter.FormHttpMessageConverter;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
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
/*    */ @Deprecated
/*    */ public class XmlAwareFormHttpMessageConverter
/*    */   extends FormHttpMessageConverter
/*    */ {
/*    */   public XmlAwareFormHttpMessageConverter() {
/* 37 */     addPartConverter((HttpMessageConverter)new SourceHttpMessageConverter<Source>());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\xml\XmlAwareFormHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */