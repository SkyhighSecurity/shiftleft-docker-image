/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringTrimmerEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final String charsToDelete;
/*    */   private final boolean emptyAsNull;
/*    */   
/*    */   public StringTrimmerEditor(boolean emptyAsNull) {
/* 45 */     this.charsToDelete = null;
/* 46 */     this.emptyAsNull = emptyAsNull;
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
/*    */   public StringTrimmerEditor(String charsToDelete, boolean emptyAsNull) {
/* 58 */     this.charsToDelete = charsToDelete;
/* 59 */     this.emptyAsNull = emptyAsNull;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) {
/* 65 */     if (text == null) {
/* 66 */       setValue(null);
/*    */     } else {
/*    */       
/* 69 */       String value = text.trim();
/* 70 */       if (this.charsToDelete != null) {
/* 71 */         value = StringUtils.deleteAny(value, this.charsToDelete);
/*    */       }
/* 73 */       if (this.emptyAsNull && "".equals(value)) {
/* 74 */         setValue(null);
/*    */       } else {
/*    */         
/* 77 */         setValue(value);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 84 */     Object value = getValue();
/* 85 */     return (value != null) ? value.toString() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\StringTrimmerEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */