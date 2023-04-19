/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
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
/*    */ public class CharArrayPropertyEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text) {
/* 33 */     setValue((text != null) ? text.toCharArray() : null);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 38 */     char[] value = (char[])getValue();
/* 39 */     return (value != null) ? new String(value) : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\CharArrayPropertyEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */