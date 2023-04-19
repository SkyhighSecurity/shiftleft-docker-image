/*    */ package org.springframework.scripting.bsh;
/*    */ 
/*    */ import bsh.EvalError;
/*    */ import bsh.Interpreter;
/*    */ import java.io.IOException;
/*    */ import java.io.StringReader;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.scripting.ScriptCompilationException;
/*    */ import org.springframework.scripting.ScriptEvaluator;
/*    */ import org.springframework.scripting.ScriptSource;
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
/*    */ public class BshScriptEvaluator
/*    */   implements ScriptEvaluator, BeanClassLoaderAware
/*    */ {
/*    */   private ClassLoader classLoader;
/*    */   
/*    */   public BshScriptEvaluator() {}
/*    */   
/*    */   public BshScriptEvaluator(ClassLoader classLoader) {
/* 54 */     this.classLoader = classLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 60 */     this.classLoader = classLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object evaluate(ScriptSource script) {
/* 66 */     return evaluate(script, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object evaluate(ScriptSource script, Map<String, Object> arguments) {
/*    */     try {
/* 72 */       Interpreter interpreter = new Interpreter();
/* 73 */       interpreter.setClassLoader(this.classLoader);
/* 74 */       if (arguments != null) {
/* 75 */         for (Map.Entry<String, Object> entry : arguments.entrySet()) {
/* 76 */           interpreter.set(entry.getKey(), entry.getValue());
/*    */         }
/*    */       }
/* 79 */       return interpreter.eval(new StringReader(script.getScriptAsString()));
/*    */     }
/* 81 */     catch (IOException ex) {
/* 82 */       throw new ScriptCompilationException(script, "Cannot access BeanShell script", ex);
/*    */     }
/* 84 */     catch (EvalError ex) {
/* 85 */       throw new ScriptCompilationException(script, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\bsh\BshScriptEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */