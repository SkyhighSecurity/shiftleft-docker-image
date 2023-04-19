/*     */ package org.springframework.scripting.groovy;
/*     */ 
/*     */ import groovy.lang.Binding;
/*     */ import groovy.lang.GroovyRuntimeException;
/*     */ import groovy.lang.GroovyShell;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import org.codehaus.groovy.control.CompilerConfiguration;
/*     */ import org.codehaus.groovy.control.customizers.CompilationCustomizer;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptEvaluator;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.scripting.support.ResourceScriptSource;
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
/*     */ public class GroovyScriptEvaluator
/*     */   implements ScriptEvaluator, BeanClassLoaderAware
/*     */ {
/*     */   private ClassLoader classLoader;
/*  45 */   private CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovyScriptEvaluator() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovyScriptEvaluator(ClassLoader classLoader) {
/*  59 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompilerConfiguration(CompilerConfiguration compilerConfiguration) {
/*  69 */     this.compilerConfiguration = (compilerConfiguration != null) ? compilerConfiguration : new CompilerConfiguration();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompilerConfiguration getCompilerConfiguration() {
/*  79 */     return this.compilerConfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompilationCustomizers(CompilationCustomizer... compilationCustomizers) {
/*  89 */     this.compilerConfiguration.addCompilationCustomizers(compilationCustomizers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  94 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object evaluate(ScriptSource script) {
/* 100 */     return evaluate(script, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object evaluate(ScriptSource script, Map<String, Object> arguments) {
/* 105 */     GroovyShell groovyShell = new GroovyShell(this.classLoader, new Binding(arguments), this.compilerConfiguration);
/*     */ 
/*     */     
/*     */     try {
/* 109 */       String filename = (script instanceof ResourceScriptSource) ? ((ResourceScriptSource)script).getResource().getFilename() : null;
/* 110 */       if (filename != null) {
/* 111 */         return groovyShell.evaluate(script.getScriptAsString(), filename);
/*     */       }
/*     */       
/* 114 */       return groovyShell.evaluate(script.getScriptAsString());
/*     */     
/*     */     }
/* 117 */     catch (IOException ex) {
/* 118 */       throw new ScriptCompilationException(script, "Cannot access Groovy script", ex);
/*     */     }
/* 120 */     catch (GroovyRuntimeException ex) {
/* 121 */       throw new ScriptCompilationException(script, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\groovy\GroovyScriptEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */