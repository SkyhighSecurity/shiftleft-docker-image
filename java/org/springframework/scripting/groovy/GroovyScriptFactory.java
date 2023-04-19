/*     */ package org.springframework.scripting.groovy;
/*     */ 
/*     */ import groovy.lang.GroovyClassLoader;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.MetaClass;
/*     */ import groovy.lang.Script;
/*     */ import java.io.IOException;
/*     */ import org.codehaus.groovy.control.CompilerConfiguration;
/*     */ import org.codehaus.groovy.control.customizers.CompilationCustomizer;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GroovyScriptFactory
/*     */   implements ScriptFactory, BeanFactoryAware, BeanClassLoaderAware
/*     */ {
/*     */   private final String scriptSourceLocator;
/*     */   private GroovyObjectCustomizer groovyObjectCustomizer;
/*     */   private CompilerConfiguration compilerConfiguration;
/*     */   private GroovyClassLoader groovyClassLoader;
/*     */   private Class<?> scriptClass;
/*     */   private Class<?> scriptResultClass;
/*     */   private CachedResultHolder cachedResult;
/*  75 */   private final Object scriptClassMonitor = new Object();
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
/*     */   public GroovyScriptFactory(String scriptSourceLocator) {
/*  88 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  89 */     this.scriptSourceLocator = scriptSourceLocator;
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
/*     */   public GroovyScriptFactory(String scriptSourceLocator, GroovyObjectCustomizer groovyObjectCustomizer) {
/* 104 */     this(scriptSourceLocator);
/* 105 */     this.groovyObjectCustomizer = groovyObjectCustomizer;
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
/*     */   public GroovyScriptFactory(String scriptSourceLocator, CompilerConfiguration compilerConfiguration) {
/* 120 */     this(scriptSourceLocator);
/* 121 */     this.compilerConfiguration = compilerConfiguration;
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
/*     */   
/*     */   public GroovyScriptFactory(String scriptSourceLocator, CompilationCustomizer... compilationCustomizers) {
/* 137 */     this(scriptSourceLocator);
/* 138 */     if (!ObjectUtils.isEmpty((Object[])compilationCustomizers)) {
/* 139 */       this.compilerConfiguration = new CompilerConfiguration();
/* 140 */       this.compilerConfiguration.addCompilationCustomizers(compilationCustomizers);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 147 */     if (beanFactory instanceof ConfigurableListableBeanFactory) {
/* 148 */       ((ConfigurableListableBeanFactory)beanFactory).ignoreDependencyType(MetaClass.class);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 154 */     this.groovyClassLoader = buildGroovyClassLoader(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovyClassLoader getGroovyClassLoader() {
/* 161 */     synchronized (this.scriptClassMonitor) {
/* 162 */       if (this.groovyClassLoader == null) {
/* 163 */         this.groovyClassLoader = buildGroovyClassLoader(ClassUtils.getDefaultClassLoader());
/*     */       }
/* 165 */       return this.groovyClassLoader;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected GroovyClassLoader buildGroovyClassLoader(ClassLoader classLoader) {
/* 175 */     return (this.compilerConfiguration != null) ? new GroovyClassLoader(classLoader, this.compilerConfiguration) : new GroovyClassLoader(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScriptSourceLocator() {
/* 182 */     return this.scriptSourceLocator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?>[] getScriptInterfaces() {
/* 192 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresConfigInterface() {
/* 201 */     return false;
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
/* 213 */     synchronized (this.scriptClassMonitor) {
/*     */ 
/*     */       
/* 216 */       this.wasModifiedForTypeCheck = false;
/*     */       
/* 218 */       if (this.cachedResult != null) {
/* 219 */         Object result = this.cachedResult.object;
/* 220 */         this.cachedResult = null;
/* 221 */         return result;
/*     */       } 
/*     */       
/* 224 */       if (this.scriptClass == null || scriptSource.isModified()) {
/*     */         
/* 226 */         this.scriptClass = getGroovyClassLoader().parseClass(scriptSource
/* 227 */             .getScriptAsString(), scriptSource.suggestedClassName());
/*     */         
/* 229 */         if (Script.class.isAssignableFrom(this.scriptClass)) {
/*     */           
/* 231 */           Object result = executeScript(scriptSource, this.scriptClass);
/* 232 */           this.scriptResultClass = (result != null) ? result.getClass() : null;
/* 233 */           return result;
/*     */         } 
/*     */         
/* 236 */         this.scriptResultClass = this.scriptClass;
/*     */       } 
/*     */       
/* 239 */       Class<?> scriptClassToExecute = this.scriptClass;
/*     */ 
/*     */       
/* 242 */       return executeScript(scriptSource, scriptClassToExecute);
/*     */     } 
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
/*     */   public Class<?> getScriptedObjectType(ScriptSource scriptSource) throws IOException, ScriptCompilationException {
/* 256 */     synchronized (this.scriptClassMonitor) {
/*     */       
/* 258 */       if (this.scriptClass == null || scriptSource.isModified()) {
/*     */         
/* 260 */         this.wasModifiedForTypeCheck = true;
/* 261 */         this.scriptClass = getGroovyClassLoader().parseClass(scriptSource
/* 262 */             .getScriptAsString(), scriptSource.suggestedClassName());
/*     */         
/* 264 */         if (Script.class.isAssignableFrom(this.scriptClass)) {
/*     */           
/* 266 */           Object result = executeScript(scriptSource, this.scriptClass);
/* 267 */           this.scriptResultClass = (result != null) ? result.getClass() : null;
/* 268 */           this.cachedResult = new CachedResultHolder(result);
/*     */         } else {
/*     */           
/* 271 */           this.scriptResultClass = this.scriptClass;
/*     */         } 
/*     */       } 
/* 274 */       return this.scriptResultClass;
/*     */     } 
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
/*     */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource) {
/* 287 */     synchronized (this.scriptClassMonitor) {
/* 288 */       return (scriptSource.isModified() || this.wasModifiedForTypeCheck);
/*     */     } 
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
/*     */   protected Object executeScript(ScriptSource scriptSource, Class<?> scriptClass) throws ScriptCompilationException {
/*     */     try {
/* 303 */       GroovyObject goo = (GroovyObject)scriptClass.newInstance();
/*     */       
/* 305 */       if (this.groovyObjectCustomizer != null)
/*     */       {
/* 307 */         this.groovyObjectCustomizer.customize(goo);
/*     */       }
/*     */       
/* 310 */       if (goo instanceof Script)
/*     */       {
/* 312 */         return ((Script)goo).run();
/*     */       }
/*     */ 
/*     */       
/* 316 */       return goo;
/*     */     
/*     */     }
/* 319 */     catch (InstantiationException ex) {
/* 320 */       throw new ScriptCompilationException(scriptSource, "Unable to instantiate Groovy script class: " + scriptClass
/* 321 */           .getName(), ex);
/*     */     }
/* 323 */     catch (IllegalAccessException ex) {
/* 324 */       throw new ScriptCompilationException(scriptSource, "Could not access Groovy script constructor: " + scriptClass
/* 325 */           .getName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 332 */     return "GroovyScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CachedResultHolder
/*     */   {
/*     */     public final Object object;
/*     */ 
/*     */ 
/*     */     
/*     */     public CachedResultHolder(Object object) {
/* 344 */       this.object = object;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\groovy\GroovyScriptFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */