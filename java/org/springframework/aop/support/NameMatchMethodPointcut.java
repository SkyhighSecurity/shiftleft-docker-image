/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PatternMatchUtils;
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
/*     */ public class NameMatchMethodPointcut
/*     */   extends StaticMethodMatcherPointcut
/*     */   implements Serializable
/*     */ {
/*  41 */   private List<String> mappedNames = new LinkedList<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappedName(String mappedName) {
/*  50 */     setMappedNames(new String[] { mappedName });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappedNames(String... mappedNames) {
/*  59 */     this.mappedNames = new LinkedList<String>();
/*  60 */     if (mappedNames != null) {
/*  61 */       this.mappedNames.addAll(Arrays.asList(mappedNames));
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
/*     */   public NameMatchMethodPointcut addMethodName(String name) {
/*  75 */     this.mappedNames.add(name);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass) {
/*  82 */     for (String mappedName : this.mappedNames) {
/*  83 */       if (mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName)) {
/*  84 */         return true;
/*     */       }
/*     */     } 
/*  87 */     return false;
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
/*     */   protected boolean isMatch(String methodName, String mappedName) {
/* 100 */     return PatternMatchUtils.simpleMatch(mappedName, methodName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 106 */     return (this == other || (other instanceof NameMatchMethodPointcut && 
/* 107 */       ObjectUtils.nullSafeEquals(this.mappedNames, ((NameMatchMethodPointcut)other).mappedNames)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     return (this.mappedNames != null) ? this.mappedNames.hashCode() : 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\NameMatchMethodPointcut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */