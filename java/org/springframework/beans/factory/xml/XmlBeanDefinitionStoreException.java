/*    */ package org.springframework.beans.factory.xml;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlBeanDefinitionStoreException
/*    */   extends BeanDefinitionStoreException
/*    */ {
/*    */   public XmlBeanDefinitionStoreException(String resourceDescription, String msg, SAXException cause) {
/* 45 */     super(resourceDescription, msg, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLineNumber() {
/* 54 */     Throwable cause = getCause();
/* 55 */     if (cause instanceof SAXParseException) {
/* 56 */       return ((SAXParseException)cause).getLineNumber();
/*    */     }
/* 58 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\XmlBeanDefinitionStoreException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */