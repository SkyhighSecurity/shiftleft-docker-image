/*    */ package org.springframework.validation;
/*    */ 
/*    */ import org.springframework.beans.PropertyAccessException;
/*    */ import org.springframework.context.support.DefaultMessageSourceResolvable;
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
/*    */ public class DefaultBindingErrorProcessor
/*    */   implements BindingErrorProcessor
/*    */ {
/*    */   public static final String MISSING_FIELD_ERROR_CODE = "required";
/*    */   
/*    */   public void processMissingFieldError(String missingField, BindingResult bindingResult) {
/* 58 */     String fixedField = bindingResult.getNestedPath() + missingField;
/* 59 */     String[] codes = bindingResult.resolveMessageCodes("required", missingField);
/* 60 */     Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), fixedField);
/* 61 */     FieldError error = new FieldError(bindingResult.getObjectName(), fixedField, "", true, codes, arguments, "Field '" + fixedField + "' is required");
/*    */     
/* 63 */     bindingResult.addError(error);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void processPropertyAccessException(PropertyAccessException ex, BindingResult bindingResult) {
/* 69 */     String field = ex.getPropertyName();
/* 70 */     String[] codes = bindingResult.resolveMessageCodes(ex.getErrorCode(), field);
/* 71 */     Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), field);
/* 72 */     Object rejectedValue = ex.getValue();
/* 73 */     if (ObjectUtils.isArray(rejectedValue)) {
/* 74 */       rejectedValue = StringUtils.arrayToCommaDelimitedString(ObjectUtils.toObjectArray(rejectedValue));
/*    */     }
/*    */     
/* 77 */     FieldError error = new FieldError(bindingResult.getObjectName(), field, rejectedValue, true, codes, arguments, ex.getLocalizedMessage());
/* 78 */     bindingResult.addError(error);
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
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object[] getArgumentsForBindError(String objectName, String field) {
/* 93 */     String[] codes = { objectName + "." + field, field };
/* 94 */     return new Object[] { new DefaultMessageSourceResolvable(codes, field) };
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\DefaultBindingErrorProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */