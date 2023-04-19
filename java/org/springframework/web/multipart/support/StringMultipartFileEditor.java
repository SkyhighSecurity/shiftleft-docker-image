/*    */ package org.springframework.web.multipart.support;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.io.IOException;
/*    */ import org.springframework.web.multipart.MultipartFile;
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
/*    */ public class StringMultipartFileEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final String charsetName;
/*    */   
/*    */   public StringMultipartFileEditor() {
/* 42 */     this.charsetName = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StringMultipartFileEditor(String charsetName) {
/* 51 */     this.charsetName = charsetName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) {
/* 57 */     setValue(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(Object value) {
/* 62 */     if (value instanceof MultipartFile) {
/* 63 */       MultipartFile multipartFile = (MultipartFile)value;
/*    */       try {
/* 65 */         super.setValue((this.charsetName != null) ? new String(multipartFile
/* 66 */               .getBytes(), this.charsetName) : new String(multipartFile
/* 67 */               .getBytes()));
/*    */       }
/* 69 */       catch (IOException ex) {
/* 70 */         throw new IllegalArgumentException("Cannot read contents of multipart file", ex);
/*    */       } 
/*    */     } else {
/*    */       
/* 74 */       super.setValue(value);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\StringMultipartFileEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */