/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceEditor;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InputStreamEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final ResourceEditor resourceEditor;
/*    */   
/*    */   public InputStreamEditor() {
/* 53 */     this.resourceEditor = new ResourceEditor();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStreamEditor(ResourceEditor resourceEditor) {
/* 61 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/* 62 */     this.resourceEditor = resourceEditor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 68 */     this.resourceEditor.setAsText(text);
/* 69 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*    */     try {
/* 71 */       setValue((resource != null) ? resource.getInputStream() : null);
/*    */     }
/* 73 */     catch (IOException ex) {
/* 74 */       throw new IllegalArgumentException("Failed to retrieve InputStream for " + resource, ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 84 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\InputStreamEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */