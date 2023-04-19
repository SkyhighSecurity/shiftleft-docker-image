/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.UUID;
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
/*    */ public class UUIDEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 36 */     if (StringUtils.hasText(text)) {
/* 37 */       setValue(UUID.fromString(text));
/*    */     } else {
/*    */       
/* 40 */       setValue(null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 46 */     UUID value = (UUID)getValue();
/* 47 */     return (value != null) ? value.toString() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\UUIDEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */