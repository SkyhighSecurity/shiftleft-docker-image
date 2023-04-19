/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import org.springframework.expression.TypedValue;
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
/*    */ public class BooleanTypedValue
/*    */   extends TypedValue
/*    */ {
/* 27 */   public static final BooleanTypedValue TRUE = new BooleanTypedValue(true);
/*    */   
/* 29 */   public static final BooleanTypedValue FALSE = new BooleanTypedValue(false);
/*    */ 
/*    */   
/*    */   private BooleanTypedValue(boolean b) {
/* 33 */     super(Boolean.valueOf(b));
/*    */   }
/*    */ 
/*    */   
/*    */   public static BooleanTypedValue forValue(boolean b) {
/* 38 */     return b ? TRUE : FALSE;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\BooleanTypedValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */