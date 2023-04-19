/*    */ package org.springframework.web.bind;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.validation.BindingResult;
/*    */ import org.springframework.validation.ObjectError;
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
/*    */ public class MethodArgumentNotValidException
/*    */   extends Exception
/*    */ {
/*    */   private final MethodParameter parameter;
/*    */   private final BindingResult bindingResult;
/*    */   
/*    */   public MethodArgumentNotValidException(MethodParameter parameter, BindingResult bindingResult) {
/* 43 */     this.parameter = parameter;
/* 44 */     this.bindingResult = bindingResult;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodParameter getParameter() {
/* 51 */     return this.parameter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BindingResult getBindingResult() {
/* 58 */     return this.bindingResult;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 67 */     StringBuilder sb = (new StringBuilder("Validation failed for argument at index ")).append(this.parameter.getParameterIndex()).append(" in method: ").append(this.parameter.getMethod().toGenericString()).append(", with ").append(this.bindingResult.getErrorCount()).append(" error(s): ");
/* 68 */     for (ObjectError error : this.bindingResult.getAllErrors()) {
/* 69 */       sb.append("[").append(error).append("] ");
/*    */     }
/* 71 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\MethodArgumentNotValidException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */