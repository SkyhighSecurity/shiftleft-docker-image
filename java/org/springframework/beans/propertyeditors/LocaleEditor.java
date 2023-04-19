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
/*    */ public class LocaleEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text) {
/* 39 */     setValue(StringUtils.parseLocaleString(text));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 44 */     Object value = getValue();
/* 45 */     return (value != null) ? value.toString() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\LocaleEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */