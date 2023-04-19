/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntLiteral
/*    */   extends Literal
/*    */ {
/*    */   private final TypedValue value;
/*    */   
/*    */   public IntLiteral(String payload, int pos, int value) {
/* 35 */     super(payload, pos);
/* 36 */     this.value = new TypedValue(Integer.valueOf(value));
/* 37 */     this.exitTypeDescriptor = "I";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getLiteralValue() {
/* 43 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 48 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 53 */     int intValue = ((Integer)this.value.getValue()).intValue();
/* 54 */     if (intValue == -1) {
/*    */       
/* 56 */       mv.visitInsn(2);
/*    */     }
/* 58 */     else if (intValue >= 0 && intValue < 6) {
/* 59 */       mv.visitInsn(3 + intValue);
/*    */     } else {
/*    */       
/* 62 */       mv.visitLdcInsn(Integer.valueOf(intValue));
/*    */     } 
/* 64 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\IntLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */