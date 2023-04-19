/*    */ package org.springframework.web.bind;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
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
/*    */ public class MissingPathVariableException
/*    */   extends ServletRequestBindingException
/*    */ {
/*    */   private final String variableName;
/*    */   private final MethodParameter parameter;
/*    */   
/*    */   public MissingPathVariableException(String variableName, MethodParameter parameter) {
/* 45 */     super("");
/* 46 */     this.variableName = variableName;
/* 47 */     this.parameter = parameter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 53 */     return "Missing URI template variable '" + this.variableName + "' for method parameter of type " + this.parameter
/* 54 */       .getNestedParameterType().getSimpleName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getVariableName() {
/* 61 */     return this.variableName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final MethodParameter getParameter() {
/* 68 */     return this.parameter;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\MissingPathVariableException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */