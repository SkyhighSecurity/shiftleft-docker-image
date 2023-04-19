/*    */ package org.springframework.beans;
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
/*    */ public class SimpleTypeConverter
/*    */   extends TypeConverterSupport
/*    */ {
/*    */   public SimpleTypeConverter() {
/* 36 */     this.typeConverterDelegate = new TypeConverterDelegate(this);
/* 37 */     registerDefaultEditors();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\SimpleTypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */