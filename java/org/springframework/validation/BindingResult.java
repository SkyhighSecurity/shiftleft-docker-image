/*    */ package org.springframework.validation;
/*    */ 
/*    */ import java.beans.PropertyEditor;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.PropertyEditorRegistry;
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
/*    */ 
/*    */ 
/*    */ public interface BindingResult
/*    */   extends Errors
/*    */ {
/* 50 */   public static final String MODEL_KEY_PREFIX = BindingResult.class.getName() + ".";
/*    */   
/*    */   Object getTarget();
/*    */   
/*    */   Map<String, Object> getModel();
/*    */   
/*    */   Object getRawFieldValue(String paramString);
/*    */   
/*    */   PropertyEditor findEditor(String paramString, Class<?> paramClass);
/*    */   
/*    */   PropertyEditorRegistry getPropertyEditorRegistry();
/*    */   
/*    */   void addError(ObjectError paramObjectError);
/*    */   
/*    */   String[] resolveMessageCodes(String paramString);
/*    */   
/*    */   String[] resolveMessageCodes(String paramString1, String paramString2);
/*    */   
/*    */   void recordSuppressedField(String paramString);
/*    */   
/*    */   String[] getSuppressedFields();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\BindingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */