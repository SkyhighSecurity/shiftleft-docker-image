/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ 
/*    */ public class ClassArrayEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public ClassArrayEditor() {
/* 47 */     this((ClassLoader)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassArrayEditor(ClassLoader classLoader) {
/* 57 */     this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 63 */     if (StringUtils.hasText(text)) {
/* 64 */       String[] classNames = StringUtils.commaDelimitedListToStringArray(text);
/* 65 */       Class<?>[] classes = new Class[classNames.length];
/* 66 */       for (int i = 0; i < classNames.length; i++) {
/* 67 */         String className = classNames[i].trim();
/* 68 */         classes[i] = ClassUtils.resolveClassName(className, this.classLoader);
/*    */       } 
/* 70 */       setValue(classes);
/*    */     } else {
/*    */       
/* 73 */       setValue(null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 79 */     Class<?>[] classes = (Class[])getValue();
/* 80 */     if (ObjectUtils.isEmpty((Object[])classes)) {
/* 81 */       return "";
/*    */     }
/* 83 */     StringBuilder sb = new StringBuilder();
/* 84 */     for (int i = 0; i < classes.length; i++) {
/* 85 */       if (i > 0) {
/* 86 */         sb.append(",");
/*    */       }
/* 88 */       sb.append(ClassUtils.getQualifiedName(classes[i]));
/*    */     } 
/* 90 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\ClassArrayEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */