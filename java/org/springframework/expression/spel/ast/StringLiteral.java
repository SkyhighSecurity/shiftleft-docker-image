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
/*    */ 
/*    */ public class StringLiteral
/*    */   extends Literal
/*    */ {
/*    */   private final TypedValue value;
/*    */   
/*    */   public StringLiteral(String payload, int pos, String value) {
/* 36 */     super(payload, pos);
/* 37 */     value = value.substring(1, value.length() - 1);
/* 38 */     this.value = new TypedValue(value.replaceAll("''", "'").replaceAll("\"\"", "\""));
/* 39 */     this.exitTypeDescriptor = "Ljava/lang/String";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getLiteralValue() {
/* 45 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return "'" + getLiteralValue().getValue() + "'";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCompilable() {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 60 */     mv.visitLdcInsn(this.value.getValue());
/* 61 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\StringLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */