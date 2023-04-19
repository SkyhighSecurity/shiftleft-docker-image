/*     */ package org.springframework.scripting.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javax.script.Bindings;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import javax.script.ScriptException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptEvaluator;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardScriptEvaluator
/*     */   implements ScriptEvaluator, BeanClassLoaderAware
/*     */ {
/*     */   private volatile ScriptEngineManager scriptEngineManager;
/*     */   private String engineName;
/*     */   
/*     */   public StandardScriptEvaluator() {}
/*     */   
/*     */   public StandardScriptEvaluator(ClassLoader classLoader) {
/*  61 */     this.scriptEngineManager = new ScriptEngineManager(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardScriptEvaluator(ScriptEngineManager scriptEngineManager) {
/*  71 */     this.scriptEngineManager = scriptEngineManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String language) {
/*  83 */     this.engineName = language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEngineName(String engineName) {
/*  93 */     this.engineName = engineName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGlobalBindings(Map<String, Object> globalBindings) {
/* 105 */     if (globalBindings != null) {
/* 106 */       this.scriptEngineManager.setBindings(StandardScriptUtils.getBindings(globalBindings));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 112 */     if (this.scriptEngineManager == null) {
/* 113 */       this.scriptEngineManager = new ScriptEngineManager(classLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object evaluate(ScriptSource script) {
/* 120 */     return evaluate(script, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object evaluate(ScriptSource script, Map<String, Object> argumentBindings) {
/* 125 */     ScriptEngine engine = getScriptEngine(script);
/*     */     try {
/* 127 */       if (CollectionUtils.isEmpty(argumentBindings)) {
/* 128 */         return engine.eval(script.getScriptAsString());
/*     */       }
/*     */       
/* 131 */       Bindings bindings = StandardScriptUtils.getBindings(argumentBindings);
/* 132 */       return engine.eval(script.getScriptAsString(), bindings);
/*     */     
/*     */     }
/* 135 */     catch (IOException ex) {
/* 136 */       throw new ScriptCompilationException(script, "Cannot access script for ScriptEngine", ex);
/*     */     }
/* 138 */     catch (ScriptException ex) {
/* 139 */       throw new ScriptCompilationException(script, new StandardScriptEvalException(ex));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScriptEngine getScriptEngine(ScriptSource script) {
/* 149 */     if (this.scriptEngineManager == null) {
/* 150 */       this.scriptEngineManager = new ScriptEngineManager();
/*     */     }
/*     */     
/* 153 */     if (StringUtils.hasText(this.engineName)) {
/* 154 */       return StandardScriptUtils.retrieveEngineByName(this.scriptEngineManager, this.engineName);
/*     */     }
/* 156 */     if (script instanceof ResourceScriptSource) {
/* 157 */       Resource resource = ((ResourceScriptSource)script).getResource();
/* 158 */       String extension = StringUtils.getFilenameExtension(resource.getFilename());
/* 159 */       if (extension == null) {
/* 160 */         throw new IllegalStateException("No script language defined, and no file extension defined for resource: " + resource);
/*     */       }
/*     */       
/* 163 */       ScriptEngine engine = this.scriptEngineManager.getEngineByExtension(extension);
/* 164 */       if (engine == null) {
/* 165 */         throw new IllegalStateException("No matching engine found for file extension '" + extension + "'");
/*     */       }
/* 167 */       return engine;
/*     */     } 
/*     */     
/* 170 */     throw new IllegalStateException("No script language defined, and no resource associated with script: " + script);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\support\StandardScriptEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */