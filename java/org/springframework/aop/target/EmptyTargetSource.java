/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.aop.TargetSource;
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
/*     */ public class EmptyTargetSource
/*     */   implements TargetSource, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3680494563553489691L;
/*  45 */   public static final EmptyTargetSource INSTANCE = new EmptyTargetSource(null, true);
/*     */ 
/*     */   
/*     */   private final Class<?> targetClass;
/*     */   
/*     */   private final boolean isStatic;
/*     */ 
/*     */   
/*     */   public static EmptyTargetSource forClass(Class<?> targetClass) {
/*  54 */     return forClass(targetClass, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EmptyTargetSource forClass(Class<?> targetClass, boolean isStatic) {
/*  64 */     return (targetClass == null && isStatic) ? INSTANCE : new EmptyTargetSource(targetClass, isStatic);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EmptyTargetSource(Class<?> targetClass, boolean isStatic) {
/*  85 */     this.targetClass = targetClass;
/*  86 */     this.isStatic = isStatic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetClass() {
/*  94 */     return this.targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 102 */     return this.isStatic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTarget() {
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseTarget(Object target) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() {
/* 126 */     return (this.targetClass == null && this.isStatic) ? INSTANCE : this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 131 */     if (this == other) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (!(other instanceof EmptyTargetSource)) {
/* 135 */       return false;
/*     */     }
/* 137 */     EmptyTargetSource otherTs = (EmptyTargetSource)other;
/* 138 */     return (ObjectUtils.nullSafeEquals(this.targetClass, otherTs.targetClass) && this.isStatic == otherTs.isStatic);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 143 */     return EmptyTargetSource.class.hashCode() * 13 + ObjectUtils.nullSafeHashCode(this.targetClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 148 */     return "EmptyTargetSource: " + ((this.targetClass != null) ? ("target class [" + this.targetClass
/* 149 */       .getName() + "]") : "no target class") + ", " + (this.isStatic ? "static" : "dynamic");
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\target\EmptyTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */