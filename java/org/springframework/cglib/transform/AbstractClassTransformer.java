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
/*    */ public abstract class AbstractClassTransformer
/*    */   extends ClassTransformer
/*    */ {
/*    */   protected AbstractClassTransformer() {
/* 23 */     super(393216);
/*    */   }
/*    */   
/*    */   public void setTarget(ClassVisitor target) {
/* 27 */     this.cv = target;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\transform\AbstractClassTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */