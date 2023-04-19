/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedHashSet;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class StandardClassMetadata
/*     */   implements ClassMetadata
/*     */ {
/*     */   private final Class<?> introspectedClass;
/*     */   
/*     */   public StandardClassMetadata(Class<?> introspectedClass) {
/*  42 */     Assert.notNull(introspectedClass, "Class must not be null");
/*  43 */     this.introspectedClass = introspectedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?> getIntrospectedClass() {
/*  50 */     return this.introspectedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  56 */     return this.introspectedClass.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/*  61 */     return this.introspectedClass.isInterface();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotation() {
/*  66 */     return this.introspectedClass.isAnnotation();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  71 */     return Modifier.isAbstract(this.introspectedClass.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConcrete() {
/*  76 */     return (!isInterface() && !isAbstract());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/*  81 */     return Modifier.isFinal(this.introspectedClass.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIndependent() {
/*  86 */     return (!hasEnclosingClass() || (this.introspectedClass
/*  87 */       .getDeclaringClass() != null && 
/*  88 */       Modifier.isStatic(this.introspectedClass.getModifiers())));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEnclosingClass() {
/*  93 */     return (this.introspectedClass.getEnclosingClass() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName() {
/*  98 */     Class<?> enclosingClass = this.introspectedClass.getEnclosingClass();
/*  99 */     return (enclosingClass != null) ? enclosingClass.getName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSuperClass() {
/* 104 */     return (this.introspectedClass.getSuperclass() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSuperClassName() {
/* 109 */     Class<?> superClass = this.introspectedClass.getSuperclass();
/* 110 */     return (superClass != null) ? superClass.getName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getInterfaceNames() {
/* 115 */     Class<?>[] ifcs = this.introspectedClass.getInterfaces();
/* 116 */     String[] ifcNames = new String[ifcs.length];
/* 117 */     for (int i = 0; i < ifcs.length; i++) {
/* 118 */       ifcNames[i] = ifcs[i].getName();
/*     */     }
/* 120 */     return ifcNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMemberClassNames() {
/* 125 */     LinkedHashSet<String> memberClassNames = new LinkedHashSet<String>(4);
/* 126 */     for (Class<?> nestedClass : this.introspectedClass.getDeclaredClasses()) {
/* 127 */       memberClassNames.add(nestedClass.getName());
/*     */     }
/* 129 */     return StringUtils.toStringArray(memberClassNames);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\StandardClassMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */