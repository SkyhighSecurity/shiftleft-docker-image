/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
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
/*    */ @JacksonStdImpl
/*    */ public class ToStringSerializer
/*    */   extends ToStringSerializerBase
/*    */ {
/* 20 */   public static final ToStringSerializer instance = new ToStringSerializer();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ToStringSerializer() {
/* 30 */     super(Object.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ToStringSerializer(Class<?> handledType) {
/* 38 */     super(handledType);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String valueToString(Object value) {
/* 43 */     return value.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\ToStringSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */