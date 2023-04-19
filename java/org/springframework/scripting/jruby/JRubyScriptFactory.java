/*     */ package org.springframework.scripting.jruby;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.jruby.RubyException;
/*     */ import org.jruby.exceptions.JumpException;
/*     */ import org.jruby.exceptions.RaiseException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.scripting.ScriptCompilationException;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ @Deprecated
/*     */ public class JRubyScriptFactory
/*     */   implements ScriptFactory, BeanClassLoaderAware
/*     */ {
/*  57 */   private static final Method getMessageMethod = ClassUtils.getMethodIfAvailable(RubyException.class, "getMessage", new Class[0]);
/*     */ 
/*     */   
/*     */   private final String scriptSourceLocator;
/*     */   
/*     */   private final Class<?>[] scriptInterfaces;
/*     */   
/*  64 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JRubyScriptFactory(String scriptSourceLocator, Class<?>... scriptInterfaces) {
/*  75 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  76 */     Assert.notEmpty((Object[])scriptInterfaces, "'scriptInterfaces' must not be empty");
/*  77 */     this.scriptSourceLocator = scriptSourceLocator;
/*  78 */     this.scriptInterfaces = scriptInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  84 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScriptSourceLocator() {
/*  90 */     return this.scriptSourceLocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getScriptInterfaces() {
/*  95 */     return this.scriptInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresConfigInterface() {
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getScriptedObject(ScriptSource scriptSource, Class<?>... actualInterfaces) throws IOException, ScriptCompilationException {
/*     */     try {
/* 114 */       return JRubyScriptUtils.createJRubyObject(scriptSource
/* 115 */           .getScriptAsString(), actualInterfaces, this.beanClassLoader);
/*     */     }
/* 117 */     catch (RaiseException ex) {
/* 118 */       String msg = null;
/* 119 */       RubyException rubyEx = ex.getException();
/* 120 */       if (rubyEx != null) {
/* 121 */         if (getMessageMethod != null) {
/*     */           
/* 123 */           msg = ReflectionUtils.invokeMethod(getMessageMethod, rubyEx).toString();
/*     */ 
/*     */         
/*     */         }
/* 127 */         else if (rubyEx.message != null) {
/* 128 */           msg = rubyEx.message.toString();
/*     */         } 
/*     */       }
/*     */       
/* 132 */       throw new ScriptCompilationException(scriptSource, (msg != null) ? msg : "Unexpected JRuby error", ex);
/*     */     }
/* 134 */     catch (JumpException ex) {
/* 135 */       throw new ScriptCompilationException(scriptSource, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getScriptedObjectType(ScriptSource scriptSource) throws IOException, ScriptCompilationException {
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource) {
/* 148 */     return scriptSource.isModified();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 154 */     return "JRubyScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\jruby\JRubyScriptFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */