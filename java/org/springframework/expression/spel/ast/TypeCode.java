/*    */ package org.springframework.expression.spel.ast;
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
/*    */ public enum TypeCode
/*    */ {
/* 27 */   OBJECT(Object.class),
/*    */   
/* 29 */   BOOLEAN(boolean.class),
/*    */   
/* 31 */   BYTE(byte.class),
/*    */   
/* 33 */   CHAR(char.class),
/*    */   
/* 35 */   DOUBLE(double.class),
/*    */   
/* 37 */   FLOAT(float.class),
/*    */   
/* 39 */   INT(int.class),
/*    */   
/* 41 */   LONG(long.class),
/*    */   
/* 43 */   SHORT(short.class);
/*    */ 
/*    */   
/*    */   private Class<?> type;
/*    */ 
/*    */   
/*    */   TypeCode(Class<?> type) {
/* 50 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getType() {
/* 55 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public static TypeCode forName(String name) {
/* 60 */     String searchingFor = name.toUpperCase();
/* 61 */     TypeCode[] tcs = values();
/* 62 */     for (int i = 1; i < tcs.length; i++) {
/* 63 */       if (tcs[i].name().equals(searchingFor)) {
/* 64 */         return tcs[i];
/*    */       }
/*    */     } 
/* 67 */     return OBJECT;
/*    */   }
/*    */   
/*    */   public static TypeCode forClass(Class<?> clazz) {
/* 71 */     TypeCode[] allValues = values();
/* 72 */     for (TypeCode typeCode : allValues) {
/* 73 */       if (clazz == typeCode.getType()) {
/* 74 */         return typeCode;
/*    */       }
/*    */     } 
/* 77 */     return OBJECT;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\TypeCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */