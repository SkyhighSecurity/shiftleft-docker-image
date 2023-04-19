/*    */ package org.springframework.web.multipart.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
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
/*    */ public class ByteArrayMultipartFileEditor
/*    */   extends ByteArrayPropertyEditor
/*    */ {
/*    */   public void setValue(Object value) {
/* 35 */     if (value instanceof MultipartFile) {
/* 36 */       MultipartFile multipartFile = (MultipartFile)value;
/*    */       try {
/* 38 */         super.setValue(multipartFile.getBytes());
/*    */       }
/* 40 */       catch (IOException ex) {
/* 41 */         throw new IllegalArgumentException("Cannot read contents of multipart file", ex);
/*    */       }
/*    */     
/* 44 */     } else if (value instanceof byte[]) {
/* 45 */       super.setValue(value);
/*    */     } else {
/*    */       
/* 48 */       super.setValue((value != null) ? value.toString().getBytes() : null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 54 */     byte[] value = (byte[])getValue();
/* 55 */     return (value != null) ? new String(value) : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\ByteArrayMultipartFileEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */