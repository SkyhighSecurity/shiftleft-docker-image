/*    */ package org.springframework.util.comparator;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class InstanceComparator<T>
/*    */   implements Comparator<T>
/*    */ {
/*    */   private final Class<?>[] instanceOrder;
/*    */   
/*    */   public InstanceComparator(Class<?>... instanceOrder) {
/* 49 */     Assert.notNull(instanceOrder, "'instanceOrder' must not be null");
/* 50 */     this.instanceOrder = instanceOrder;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(T o1, T o2) {
/* 56 */     int i1 = getOrder(o1);
/* 57 */     int i2 = getOrder(o2);
/* 58 */     return (i1 < i2) ? -1 : ((i1 == i2) ? 0 : 1);
/*    */   }
/*    */   
/*    */   private int getOrder(T object) {
/* 62 */     if (object != null) {
/* 63 */       for (int i = 0; i < this.instanceOrder.length; i++) {
/* 64 */         if (this.instanceOrder[i].isInstance(object)) {
/* 65 */           return i;
/*    */         }
/*    */       } 
/*    */     }
/* 69 */     return this.instanceOrder.length;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\comparator\InstanceComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */