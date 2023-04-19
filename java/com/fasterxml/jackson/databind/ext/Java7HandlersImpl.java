/*    */ package com.fasterxml.jackson.databind.ext;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Java7HandlersImpl
/*    */   extends Java7Handlers
/*    */ {
/* 18 */   private final Class<?> _pathClass = Path.class;
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getClassJavaNioFilePath() {
/* 23 */     return this._pathClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonDeserializer<?> getDeserializerForJavaNioFilePath(Class<?> rawType) {
/* 28 */     if (rawType == this._pathClass) {
/* 29 */       return (JsonDeserializer<?>)new NioPathDeserializer();
/*    */     }
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonSerializer<?> getSerializerForJavaNioFilePath(Class<?> rawType) {
/* 36 */     if (this._pathClass.isAssignableFrom(rawType)) {
/* 37 */       return (JsonSerializer<?>)new NioPathSerializer();
/*    */     }
/* 39 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ext\Java7HandlersImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */