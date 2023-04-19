/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.TimeZone;
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
/*    */ public class TimeZoneEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 39 */     setValue(StringUtils.parseTimeZoneString(text));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 44 */     TimeZone value = (TimeZone)getValue();
/* 45 */     return (value != null) ? value.getID() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\TimeZoneEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */