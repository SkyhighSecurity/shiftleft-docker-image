/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.IntroductionInfo;
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
/*     */ public class IntroductionInfoSupport
/*     */   implements IntroductionInfo, Serializable
/*     */ {
/*  46 */   protected final Set<Class<?>> publishedInterfaces = new LinkedHashSet<Class<?>>();
/*     */   
/*  48 */   private transient Map<Method, Boolean> rememberedMethods = new ConcurrentHashMap<Method, Boolean>(32);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void suppressInterface(Class<?> ifc) {
/*  59 */     this.publishedInterfaces.remove(ifc);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getInterfaces() {
/*  64 */     return ClassUtils.toClassArray(this.publishedInterfaces);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean implementsInterface(Class<?> ifc) {
/*  73 */     for (Class<?> pubIfc : this.publishedInterfaces) {
/*  74 */       if (ifc.isInterface() && ifc.isAssignableFrom(pubIfc)) {
/*  75 */         return true;
/*     */       }
/*     */     } 
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void implementInterfacesOnObject(Object delegate) {
/*  86 */     this.publishedInterfaces.addAll(ClassUtils.getAllInterfacesAsSet(delegate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean isMethodOnIntroducedInterface(MethodInvocation mi) {
/*  95 */     Boolean rememberedResult = this.rememberedMethods.get(mi.getMethod());
/*  96 */     if (rememberedResult != null) {
/*  97 */       return rememberedResult.booleanValue();
/*     */     }
/*     */ 
/*     */     
/* 101 */     boolean result = implementsInterface(mi.getMethod().getDeclaringClass());
/* 102 */     this.rememberedMethods.put(mi.getMethod(), Boolean.valueOf(result));
/* 103 */     return result;
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
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 119 */     ois.defaultReadObject();
/*     */     
/* 121 */     this.rememberedMethods = new ConcurrentHashMap<Method, Boolean>(32);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\IntroductionInfoSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */