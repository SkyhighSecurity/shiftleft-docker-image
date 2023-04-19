/*    */ package org.apache.commons.collections.buffer;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ public class CircularFifoBuffer
/*    */   extends BoundedFifoBuffer
/*    */ {
/*    */   private static final long serialVersionUID = -8423413834657610406L;
/*    */   
/*    */   public CircularFifoBuffer() {
/* 58 */     super(32);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CircularFifoBuffer(int size) {
/* 68 */     super(size);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CircularFifoBuffer(Collection coll) {
/* 79 */     super(coll);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean add(Object element) {
/* 90 */     if (isFull()) {
/* 91 */       remove();
/*    */     }
/* 93 */     return super.add(element);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\buffer\CircularFifoBuffer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */