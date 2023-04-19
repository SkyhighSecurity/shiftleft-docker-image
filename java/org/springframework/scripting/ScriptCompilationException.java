/*    */ package org.springframework.scripting;
/*    */ 
/*    */ import org.springframework.core.NestedRuntimeException;
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
/*    */ public class ScriptCompilationException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   private ScriptSource scriptSource;
/*    */   
/*    */   public ScriptCompilationException(String msg) {
/* 38 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptCompilationException(String msg, Throwable cause) {
/* 47 */     super(msg, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptCompilationException(ScriptSource scriptSource, String msg) {
/* 57 */     super("Could not compile " + scriptSource + ": " + msg);
/* 58 */     this.scriptSource = scriptSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptCompilationException(ScriptSource scriptSource, Throwable cause) {
/* 67 */     super("Could not compile " + scriptSource, cause);
/* 68 */     this.scriptSource = scriptSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptCompilationException(ScriptSource scriptSource, String msg, Throwable cause) {
/* 78 */     super("Could not compile " + scriptSource + ": " + msg, cause);
/* 79 */     this.scriptSource = scriptSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScriptSource getScriptSource() {
/* 88 */     return this.scriptSource;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\ScriptCompilationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */