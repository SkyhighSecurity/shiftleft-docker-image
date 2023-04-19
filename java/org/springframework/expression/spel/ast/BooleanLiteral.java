/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
/*    */ import org.springframework.expression.spel.support.BooleanTypedValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BooleanLiteral
/*    */   extends Literal
/*    */ {
/*    */   private final BooleanTypedValue value;
/*    */   
/*    */   public BooleanLiteral(String payload, int pos, boolean value) {
/* 35 */     super(payload, pos);
/* 36 */     this.value = BooleanTypedValue.forValue(value);
/* 37 */     this.exitTypeDescriptor = "Z";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BooleanTypedValue getLiteralValue() {
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
/* 53 */     if (this.value == BooleanTypedValue.TRUE) {
/* 54 */       mv.visitLdcInsn(Integer.valueOf(1));
/*    */     } else {
/*    */       
/* 57 */       mv.visitLdcInsn(Integer.valueOf(0));
/*    */     } 
/* 59 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\BooleanLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */