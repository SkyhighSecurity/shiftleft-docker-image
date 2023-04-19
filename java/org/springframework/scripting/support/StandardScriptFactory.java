/*     */ package org.springframework.scripting.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.script.Invocable;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class StandardScriptFactory
/*     */   implements ScriptFactory, BeanClassLoaderAware
/*     */ {
/*     */   private final String scriptEngineName;
/*     */   private final String scriptSourceLocator;
/*     */   private final Class<?>[] scriptInterfaces;
/*  54 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile ScriptEngine scriptEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardScriptFactory(String scriptSourceLocator) {
/*  65 */     this(null, scriptSourceLocator, (Class[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardScriptFactory(String scriptSourceLocator, Class<?>... scriptInterfaces) {
/*  76 */     this(null, scriptSourceLocator, scriptInterfaces);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardScriptFactory(String scriptEngineName, String scriptSourceLocator) {
/*  87 */     this(scriptEngineName, scriptSourceLocator, (Class[])null);
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
/*     */   public StandardScriptFactory(String scriptEngineName, String scriptSourceLocator, Class<?>... scriptInterfaces) {
/* 100 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/* 101 */     this.scriptEngineName = scriptEngineName;
/* 102 */     this.scriptSourceLocator = scriptSourceLocator;
/* 103 */     this.scriptInterfaces = scriptInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 109 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScriptSourceLocator() {
/* 114 */     return this.scriptSourceLocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getScriptInterfaces() {
/* 119 */     return this.scriptInterfaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresConfigInterface() {
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getScriptedObject(ScriptSource scriptSource, Class<?>... actualInterfaces) throws IOException, ScriptCompilationException {
/* 135 */     Object script = evaluateScript(scriptSource);
/*     */     
/* 137 */     if (!ObjectUtils.isEmpty((Object[])actualInterfaces)) {
/* 138 */       boolean adaptationRequired = false;
/* 139 */       for (Class<?> requestedIfc : actualInterfaces) {
/* 140 */         if ((script instanceof Class) ? !requestedIfc.isAssignableFrom((Class)script) : 
/* 141 */           !requestedIfc.isInstance(script)) {
/* 142 */           adaptationRequired = true;
/*     */         }
/*     */       } 
/* 145 */       if (adaptationRequired) {
/* 146 */         script = adaptToInterfaces(script, scriptSource, actualInterfaces);
/*     */       }
/*     */     } 
/*     */     
/* 150 */     if (script instanceof Class) {
/* 151 */       Class<?> scriptClass = (Class)script;
/*     */       try {
/* 153 */         return scriptClass.newInstance();
/*     */       }
/* 155 */       catch (InstantiationException ex) {
/* 156 */         throw new ScriptCompilationException(scriptSource, "Unable to instantiate script class: " + scriptClass
/* 157 */             .getName(), ex);
/*     */       }
/* 159 */       catch (IllegalAccessException ex) {
/* 160 */         throw new ScriptCompilationException(scriptSource, "Could not access script constructor: " + scriptClass
/* 161 */             .getName(), ex);
/*     */       } 
/*     */     } 
/*     */     
/* 165 */     return script;
/*     */   }
/*     */   
/*     */   protected Object evaluateScript(ScriptSource scriptSource) {
/*     */     try {
/* 170 */       if (this.scriptEngine == null) {
/* 171 */         this.scriptEngine = retrieveScriptEngine(scriptSource);
/* 172 */         if (this.scriptEngine == null) {
/* 173 */           throw new IllegalStateException("Could not determine script engine for " + scriptSource);
/*     */         }
/*     */       } 
/* 176 */       return this.scriptEngine.eval(scriptSource.getScriptAsString());
/*     */     }
/* 178 */     catch (Exception ex) {
/* 179 */       throw new ScriptCompilationException(scriptSource, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ScriptEngine retrieveScriptEngine(ScriptSource scriptSource) {
/* 184 */     ScriptEngineManager scriptEngineManager = new ScriptEngineManager(this.beanClassLoader);
/*     */     
/* 186 */     if (this.scriptEngineName != null) {
/* 187 */       return StandardScriptUtils.retrieveEngineByName(scriptEngineManager, this.scriptEngineName);
/*     */     }
/*     */     
/* 190 */     if (scriptSource instanceof ResourceScriptSource) {
/* 191 */       String filename = ((ResourceScriptSource)scriptSource).getResource().getFilename();
/* 192 */       if (filename != null) {
/* 193 */         String extension = StringUtils.getFilenameExtension(filename);
/* 194 */         if (extension != null) {
/* 195 */           ScriptEngine engine = scriptEngineManager.getEngineByExtension(extension);
/* 196 */           if (engine != null) {
/* 197 */             return engine;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 203 */     return null;
/*     */   }
/*     */   
/*     */   protected Object adaptToInterfaces(Object script, ScriptSource scriptSource, Class<?>... actualInterfaces) {
/*     */     Class<?> adaptedIfc;
/* 208 */     if (actualInterfaces.length == 1) {
/* 209 */       adaptedIfc = actualInterfaces[0];
/*     */     } else {
/*     */       
/* 212 */       adaptedIfc = ClassUtils.createCompositeInterface(actualInterfaces, this.beanClassLoader);
/*     */     } 
/*     */     
/* 215 */     if (adaptedIfc != null) {
/* 216 */       if (!(this.scriptEngine instanceof Invocable)) {
/* 217 */         throw new ScriptCompilationException(scriptSource, "ScriptEngine must implement Invocable in order to adapt it to an interface: " + this.scriptEngine);
/*     */       }
/*     */ 
/*     */       
/* 221 */       Invocable invocable = (Invocable)this.scriptEngine;
/* 222 */       if (script != null) {
/* 223 */         script = invocable.getInterface(script, adaptedIfc);
/*     */       }
/* 225 */       if (script == null) {
/* 226 */         script = invocable.getInterface(adaptedIfc);
/* 227 */         if (script == null) {
/* 228 */           throw new ScriptCompilationException(scriptSource, "Could not adapt script to interface [" + adaptedIfc
/* 229 */               .getName() + "]");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 234 */     return script;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getScriptedObjectType(ScriptSource scriptSource) throws IOException, ScriptCompilationException {
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource) {
/* 246 */     return scriptSource.isModified();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 252 */     return "StandardScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\support\StandardScriptFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */