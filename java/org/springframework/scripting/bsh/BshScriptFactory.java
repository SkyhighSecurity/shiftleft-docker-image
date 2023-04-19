/*     */ package org.springframework.scripting.bsh;
/*     */ 
/*     */ import bsh.EvalError;
/*     */ import java.io.IOException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class BshScriptFactory
/*     */   implements ScriptFactory, BeanClassLoaderAware
/*     */ {
/*     */   private final String scriptSourceLocator;
/*     */   private final Class<?>[] scriptInterfaces;
/*  50 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*     */   private Class<?> scriptClass;
/*     */   
/*  54 */   private final Object scriptClassMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean wasModifiedForTypeCheck = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BshScriptFactory(String scriptSourceLocator) {
/*  67 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  68 */     this.scriptSourceLocator = scriptSourceLocator;
/*  69 */     this.scriptInterfaces = null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public BshScriptFactory(String scriptSourceLocator, Class<?>... scriptInterfaces) {
/*  84 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  85 */     this.scriptSourceLocator = scriptSourceLocator;
/*  86 */     this.scriptInterfaces = scriptInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  92 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScriptSourceLocator() {
/*  98 */     return this.scriptSourceLocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getScriptInterfaces() {
/* 103 */     return this.scriptInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresConfigInterface() {
/* 111 */     return true;
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
/*     */   public Object getScriptedObject(ScriptSource scriptSource, Class<?>... actualInterfaces) throws IOException, ScriptCompilationException {
/*     */     Class<?> clazz;
/*     */     try {
/* 125 */       synchronized (this.scriptClassMonitor) {
/* 126 */         boolean requiresScriptEvaluation = (this.wasModifiedForTypeCheck && this.scriptClass == null);
/* 127 */         this.wasModifiedForTypeCheck = false;
/*     */         
/* 129 */         if (scriptSource.isModified() || requiresScriptEvaluation) {
/*     */           
/* 131 */           Object result = BshScriptUtils.evaluateBshScript(scriptSource
/* 132 */               .getScriptAsString(), actualInterfaces, this.beanClassLoader);
/* 133 */           if (result instanceof Class) {
/*     */ 
/*     */             
/* 136 */             this.scriptClass = (Class)result;
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 143 */             return result;
/*     */           } 
/*     */         } 
/* 146 */         clazz = this.scriptClass;
/*     */       }
/*     */     
/* 149 */     } catch (EvalError ex) {
/* 150 */       this.scriptClass = null;
/* 151 */       throw new ScriptCompilationException(scriptSource, ex);
/*     */     } 
/*     */     
/* 154 */     if (clazz != null) {
/*     */       
/*     */       try {
/* 157 */         return clazz.newInstance();
/*     */       }
/* 159 */       catch (Throwable ex) {
/* 160 */         throw new ScriptCompilationException(scriptSource, "Could not instantiate script class: " + clazz
/* 161 */             .getName(), ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 167 */       return BshScriptUtils.createBshObject(scriptSource
/* 168 */           .getScriptAsString(), actualInterfaces, this.beanClassLoader);
/*     */     }
/* 170 */     catch (EvalError ex) {
/* 171 */       throw new ScriptCompilationException(scriptSource, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getScriptedObjectType(ScriptSource scriptSource) throws IOException, ScriptCompilationException {
/* 180 */     synchronized (this.scriptClassMonitor) {
/*     */       
/* 182 */       if (scriptSource.isModified()) {
/*     */         
/* 184 */         this.wasModifiedForTypeCheck = true;
/* 185 */         this.scriptClass = BshScriptUtils.determineBshObjectType(scriptSource
/* 186 */             .getScriptAsString(), this.beanClassLoader);
/*     */       } 
/* 188 */       return this.scriptClass;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource) {
/* 199 */     synchronized (this.scriptClassMonitor) {
/* 200 */       return (scriptSource.isModified() || this.wasModifiedForTypeCheck);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 207 */     return "BshScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\bsh\BshScriptFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */