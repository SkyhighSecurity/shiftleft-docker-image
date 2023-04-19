/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.Properties;
/*    */ import org.springframework.beans.propertyeditors.PropertiesEditor;
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
/*    */ public class PropertyValuesEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/* 39 */   private final PropertiesEditor propertiesEditor = new PropertiesEditor();
/*    */ 
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 43 */     this.propertiesEditor.setAsText(text);
/* 44 */     Properties props = (Properties)this.propertiesEditor.getValue();
/* 45 */     setValue(new MutablePropertyValues(props));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyValuesEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */