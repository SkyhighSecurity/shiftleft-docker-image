/*    */ package org.springframework.asm;
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
/*    */ class CurrentFrame
/*    */   extends Frame
/*    */ {
/*    */   void execute(int opcode, int arg, ClassWriter cw, Item item) {
/* 50 */     super.execute(opcode, arg, cw, item);
/* 51 */     Frame successor = new Frame();
/* 52 */     merge(cw, successor, 0);
/* 53 */     set(successor);
/* 54 */     this.owner.inputStackTop = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\asm\CurrentFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */