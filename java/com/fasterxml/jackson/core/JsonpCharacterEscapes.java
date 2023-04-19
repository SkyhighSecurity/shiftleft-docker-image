/*    */ package com.fasterxml.jackson.core;
/*    */ 
/*    */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*    */ import com.fasterxml.jackson.core.io.SerializedString;
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
/*    */ public class JsonpCharacterEscapes
/*    */   extends CharacterEscapes
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 19 */   private static final int[] asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
/* 20 */   private static final SerializedString escapeFor2028 = new SerializedString("\\u2028");
/* 21 */   private static final SerializedString escapeFor2029 = new SerializedString("\\u2029");
/*    */   
/* 23 */   private static final JsonpCharacterEscapes sInstance = new JsonpCharacterEscapes();
/*    */   
/*    */   public static JsonpCharacterEscapes instance() {
/* 26 */     return sInstance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SerializableString getEscapeSequence(int ch) {
/* 32 */     switch (ch) {
/*    */       case 8232:
/* 34 */         return (SerializableString)escapeFor2028;
/*    */       case 8233:
/* 36 */         return (SerializableString)escapeFor2029;
/*    */     } 
/* 38 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getEscapeCodesForAscii() {
/* 44 */     return asciiEscapes;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\core\JsonpCharacterEscapes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */