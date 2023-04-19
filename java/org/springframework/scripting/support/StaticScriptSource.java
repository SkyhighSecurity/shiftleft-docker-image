/*    */ package org.springframework.scripting.support;
/*    */ 
/*    */ import org.springframework.scripting.ScriptSource;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class StaticScriptSource
/*    */   implements ScriptSource
/*    */ {
/*    */   private String script;
/*    */   private boolean modified;
/*    */   private String className;
/*    */   
/*    */   public StaticScriptSource(String script) {
/* 46 */     setScript(script);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StaticScriptSource(String script, String className) {
/* 56 */     setScript(script);
/* 57 */     this.className = className;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void setScript(String script) {
/* 65 */     Assert.hasText(script, "Script must not be empty");
/* 66 */     this.modified = !script.equals(this.script);
/* 67 */     this.script = script;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized String getScriptAsString() {
/* 73 */     this.modified = false;
/* 74 */     return this.script;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized boolean isModified() {
/* 79 */     return this.modified;
/*    */   }
/*    */ 
/*    */   
/*    */   public String suggestedClassName() {
/* 84 */     return this.className;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     return "static script" + ((this.className != null) ? (" [" + this.className + "]") : "");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\support\StaticScriptSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */