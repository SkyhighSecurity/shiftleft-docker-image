/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class StackTraceElementDeserializer
/*    */   extends StdScalarDeserializer<StackTraceElement>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public StackTraceElementDeserializer() {
/* 16 */     super(StackTraceElement.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public StackTraceElement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 21 */     JsonToken t = p.getCurrentToken();
/*    */     
/* 23 */     if (t == JsonToken.START_OBJECT) {
/* 24 */       String className = "", methodName = "", fileName = "";
/*    */       
/* 26 */       String moduleName = null, moduleVersion = null;
/* 27 */       String classLoaderName = null;
/* 28 */       int lineNumber = -1;
/*    */       
/* 30 */       while ((t = p.nextValue()) != JsonToken.END_OBJECT) {
/* 31 */         String propName = p.getCurrentName();
/*    */         
/* 33 */         if ("className".equals(propName)) {
/* 34 */           className = p.getText();
/* 35 */         } else if ("classLoaderName".equals(propName)) {
/* 36 */           classLoaderName = p.getText();
/* 37 */         } else if ("fileName".equals(propName)) {
/* 38 */           fileName = p.getText();
/* 39 */         } else if ("lineNumber".equals(propName)) {
/* 40 */           if (t.isNumeric()) {
/* 41 */             lineNumber = p.getIntValue();
/*    */           } else {
/* 43 */             lineNumber = _parseIntPrimitive(p, ctxt);
/*    */           } 
/* 45 */         } else if ("methodName".equals(propName)) {
/* 46 */           methodName = p.getText();
/* 47 */         } else if (!"nativeMethod".equals(propName)) {
/*    */           
/* 49 */           if ("moduleName".equals(propName)) {
/* 50 */             moduleName = p.getText();
/* 51 */           } else if ("moduleVersion".equals(propName)) {
/* 52 */             moduleVersion = p.getText();
/* 53 */           } else if (!"declaringClass".equals(propName) && 
/* 54 */             !"format".equals(propName)) {
/*    */ 
/*    */ 
/*    */             
/* 58 */             handleUnknownProperty(p, ctxt, this._valueClass, propName);
/*    */           } 
/* 60 */         }  p.skipChildren();
/*    */       } 
/* 62 */       return constructValue(ctxt, className, methodName, fileName, lineNumber, moduleName, moduleVersion, classLoaderName);
/*    */     } 
/* 64 */     if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/* 65 */       p.nextToken();
/* 66 */       StackTraceElement value = deserialize(p, ctxt);
/* 67 */       if (p.nextToken() != JsonToken.END_ARRAY) {
/* 68 */         handleMissingEndArrayForSingle(p, ctxt);
/*    */       }
/* 70 */       return value;
/*    */     } 
/* 72 */     return (StackTraceElement)ctxt.handleUnexpectedToken(this._valueClass, p);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   protected StackTraceElement constructValue(DeserializationContext ctxt, String className, String methodName, String fileName, int lineNumber, String moduleName, String moduleVersion) {
/* 79 */     return constructValue(ctxt, className, methodName, fileName, lineNumber, moduleName, moduleVersion, (String)null);
/*    */   }
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
/*    */   protected StackTraceElement constructValue(DeserializationContext ctxt, String className, String methodName, String fileName, int lineNumber, String moduleName, String moduleVersion, String classLoaderName) {
/* 94 */     return new StackTraceElement(className, methodName, fileName, lineNumber);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\StackTraceElementDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */