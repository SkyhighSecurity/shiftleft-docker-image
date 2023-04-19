/*    */ package org.springframework.util.comparator;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
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
/*    */ public final class BooleanComparator
/*    */   implements Comparator<Boolean>, Serializable
/*    */ {
/* 35 */   public static final BooleanComparator TRUE_LOW = new BooleanComparator(true);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static final BooleanComparator TRUE_HIGH = new BooleanComparator(false);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean trueLow;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BooleanComparator(boolean trueLow) {
/* 58 */     this.trueLow = trueLow;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(Boolean v1, Boolean v2) {
/* 64 */     return ((v1.booleanValue() ^ v2.booleanValue()) != 0) ? (((v1.booleanValue() ^ this.trueLow) != 0) ? 1 : -1) : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 69 */     if (this == obj) {
/* 70 */       return true;
/*    */     }
/* 72 */     if (!(obj instanceof BooleanComparator)) {
/* 73 */       return false;
/*    */     }
/* 75 */     return (this.trueLow == ((BooleanComparator)obj).trueLow);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 80 */     return (this.trueLow ? -1 : 1) * getClass().hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     return "BooleanComparator: " + (this.trueLow ? "true low" : "true high");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\comparator\BooleanComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */