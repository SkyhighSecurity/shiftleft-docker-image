/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QualifiedIdentifier
/*    */   extends SpelNodeImpl
/*    */ {
/*    */   private TypedValue value;
/*    */   
/*    */   public QualifiedIdentifier(int pos, SpelNodeImpl... operands) {
/* 39 */     super(pos, operands);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 46 */     if (this.value == null) {
/* 47 */       StringBuilder sb = new StringBuilder();
/* 48 */       for (int i = 0; i < getChildCount(); i++) {
/* 49 */         Object value = this.children[i].getValueInternal(state).getValue();
/* 50 */         if (i > 0 && !value.toString().startsWith("$")) {
/* 51 */           sb.append(".");
/*    */         }
/* 53 */         sb.append(value);
/*    */       } 
/* 55 */       this.value = new TypedValue(sb.toString());
/*    */     } 
/* 57 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toStringAST() {
/* 62 */     StringBuilder sb = new StringBuilder();
/* 63 */     if (this.value != null) {
/* 64 */       sb.append(this.value.getValue());
/*    */     } else {
/*    */       
/* 67 */       for (int i = 0; i < getChildCount(); i++) {
/* 68 */         if (i > 0) {
/* 69 */           sb.append(".");
/*    */         }
/* 71 */         sb.append(getChild(i).toStringAST());
/*    */       } 
/*    */     } 
/* 74 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\QualifiedIdentifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */