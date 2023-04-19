/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
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
/*    */ 
/*    */ public class PropertiesEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 48 */     Properties props = new Properties();
/* 49 */     if (text != null) {
/*    */       
/*    */       try {
/* 52 */         props.load(new ByteArrayInputStream(text.getBytes("ISO-8859-1")));
/*    */       }
/* 54 */       catch (IOException ex) {
/*    */         
/* 56 */         throw new IllegalArgumentException("Failed to parse [" + text + "] into Properties", ex);
/*    */       } 
/*    */     }
/*    */     
/* 60 */     setValue(props);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(Object value) {
/* 68 */     if (!(value instanceof Properties) && value instanceof Map) {
/* 69 */       Properties props = new Properties();
/* 70 */       props.putAll((Map<?, ?>)value);
/* 71 */       super.setValue(props);
/*    */     } else {
/*    */       
/* 74 */       super.setValue(value);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\PropertiesEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */