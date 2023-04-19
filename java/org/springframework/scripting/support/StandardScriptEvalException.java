/*    */ package org.springframework.scripting.support;
/*    */ 
/*    */ import javax.script.ScriptException;
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
/*    */ public class StandardScriptEvalException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final ScriptException scriptException;
/*    */   
/*    */   public StandardScriptEvalException(ScriptException ex) {
/* 46 */     super(ex.getMessage());
/* 47 */     this.scriptException = ex;
/*    */   }
/*    */ 
/*    */   
/*    */   public final ScriptException getScriptException() {
/* 52 */     return this.scriptException;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized Throwable fillInStackTrace() {
/* 57 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\support\StandardScriptEvalException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */