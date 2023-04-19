/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.ClassVisitor;
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
/*    */ public abstract class ClassTransformer
/*    */   extends ClassVisitor
/*    */ {
/*    */   public ClassTransformer() {
/* 23 */     super(393216);
/*    */   }
/*    */   public ClassTransformer(int opcode) {
/* 26 */     super(opcode);
/*    */   }
/*    */   
/*    */   public abstract void setTarget(ClassVisitor paramClassVisitor);
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\transform\ClassTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */