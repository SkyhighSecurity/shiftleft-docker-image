/*    */ package org.springframework.expression;
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
/*    */ public interface ParserContext
/*    */ {
/* 61 */   public static final ParserContext TEMPLATE_EXPRESSION = new ParserContext()
/*    */     {
/*    */       public String getExpressionPrefix()
/*    */       {
/* 65 */         return "#{";
/*    */       }
/*    */ 
/*    */       
/*    */       public String getExpressionSuffix() {
/* 70 */         return "}";
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean isTemplate() {
/* 75 */         return true;
/*    */       }
/*    */     };
/*    */   
/*    */   boolean isTemplate();
/*    */   
/*    */   String getExpressionPrefix();
/*    */   
/*    */   String getExpressionSuffix();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\ParserContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */