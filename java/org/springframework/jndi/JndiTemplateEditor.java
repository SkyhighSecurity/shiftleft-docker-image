/*    */ package org.springframework.jndi;
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
/*    */ public class JndiTemplateEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/* 33 */   private final PropertiesEditor propertiesEditor = new PropertiesEditor();
/*    */ 
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 37 */     if (text == null) {
/* 38 */       throw new IllegalArgumentException("JndiTemplate cannot be created from null string");
/*    */     }
/* 40 */     if ("".equals(text)) {
/*    */       
/* 42 */       setValue(new JndiTemplate());
/*    */     }
/*    */     else {
/*    */       
/* 46 */       this.propertiesEditor.setAsText(text);
/* 47 */       Properties props = (Properties)this.propertiesEditor.getValue();
/* 48 */       setValue(new JndiTemplate(props));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jndi\JndiTemplateEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */