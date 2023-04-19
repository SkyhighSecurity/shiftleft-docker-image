/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.IntroductionAwareMethodMatcher;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MethodMatchers
/*     */ {
/*     */   public static MethodMatcher union(MethodMatcher mm1, MethodMatcher mm2) {
/*  51 */     return (MethodMatcher)new UnionMethodMatcher(mm1, mm2);
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
/*     */   static MethodMatcher union(MethodMatcher mm1, ClassFilter cf1, MethodMatcher mm2, ClassFilter cf2) {
/*  64 */     return (MethodMatcher)new ClassFilterAwareUnionMethodMatcher(mm1, cf1, mm2, cf2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodMatcher intersection(MethodMatcher mm1, MethodMatcher mm2) {
/*  75 */     return (MethodMatcher)new IntersectionMethodMatcher(mm1, mm2);
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
/*     */   public static boolean matches(MethodMatcher mm, Method method, Class<?> targetClass, boolean hasIntroductions) {
/*  91 */     Assert.notNull(mm, "MethodMatcher must not be null");
/*  92 */     return ((mm instanceof IntroductionAwareMethodMatcher && ((IntroductionAwareMethodMatcher)mm)
/*  93 */       .matches(method, targetClass, hasIntroductions)) || mm
/*  94 */       .matches(method, targetClass));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class UnionMethodMatcher
/*     */     implements IntroductionAwareMethodMatcher, Serializable
/*     */   {
/*     */     private final MethodMatcher mm1;
/*     */ 
/*     */     
/*     */     private final MethodMatcher mm2;
/*     */ 
/*     */     
/*     */     public UnionMethodMatcher(MethodMatcher mm1, MethodMatcher mm2) {
/* 109 */       Assert.notNull(mm1, "First MethodMatcher must not be null");
/* 110 */       Assert.notNull(mm2, "Second MethodMatcher must not be null");
/* 111 */       this.mm1 = mm1;
/* 112 */       this.mm2 = mm2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, boolean hasIntroductions) {
/* 117 */       return ((matchesClass1(targetClass) && MethodMatchers.matches(this.mm1, method, targetClass, hasIntroductions)) || (
/* 118 */         matchesClass2(targetClass) && MethodMatchers.matches(this.mm2, method, targetClass, hasIntroductions)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 123 */       return ((matchesClass1(targetClass) && this.mm1.matches(method, targetClass)) || (
/* 124 */         matchesClass2(targetClass) && this.mm2.matches(method, targetClass)));
/*     */     }
/*     */     
/*     */     protected boolean matchesClass1(Class<?> targetClass) {
/* 128 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean matchesClass2(Class<?> targetClass) {
/* 132 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRuntime() {
/* 137 */       return (this.mm1.isRuntime() || this.mm2.isRuntime());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 142 */       return (this.mm1.matches(method, targetClass, args) || this.mm2.matches(method, targetClass, args));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 147 */       if (this == obj) {
/* 148 */         return true;
/*     */       }
/* 150 */       if (!(obj instanceof UnionMethodMatcher)) {
/* 151 */         return false;
/*     */       }
/* 153 */       UnionMethodMatcher that = (UnionMethodMatcher)obj;
/* 154 */       return (this.mm1.equals(that.mm1) && this.mm2.equals(that.mm2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 159 */       int hashCode = 17;
/* 160 */       hashCode = 37 * hashCode + this.mm1.hashCode();
/* 161 */       hashCode = 37 * hashCode + this.mm2.hashCode();
/* 162 */       return hashCode;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ClassFilterAwareUnionMethodMatcher
/*     */     extends UnionMethodMatcher
/*     */   {
/*     */     private final ClassFilter cf1;
/*     */ 
/*     */     
/*     */     private final ClassFilter cf2;
/*     */ 
/*     */ 
/*     */     
/*     */     public ClassFilterAwareUnionMethodMatcher(MethodMatcher mm1, ClassFilter cf1, MethodMatcher mm2, ClassFilter cf2) {
/* 179 */       super(mm1, mm2);
/* 180 */       this.cf1 = cf1;
/* 181 */       this.cf2 = cf2;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean matchesClass1(Class<?> targetClass) {
/* 186 */       return this.cf1.matches(targetClass);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean matchesClass2(Class<?> targetClass) {
/* 191 */       return this.cf2.matches(targetClass);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 196 */       if (this == other) {
/* 197 */         return true;
/*     */       }
/* 199 */       if (!super.equals(other)) {
/* 200 */         return false;
/*     */       }
/* 202 */       ClassFilter otherCf1 = ClassFilter.TRUE;
/* 203 */       ClassFilter otherCf2 = ClassFilter.TRUE;
/* 204 */       if (other instanceof ClassFilterAwareUnionMethodMatcher) {
/* 205 */         ClassFilterAwareUnionMethodMatcher cfa = (ClassFilterAwareUnionMethodMatcher)other;
/* 206 */         otherCf1 = cfa.cf1;
/* 207 */         otherCf2 = cfa.cf2;
/*     */       } 
/* 209 */       return (this.cf1.equals(otherCf1) && this.cf2.equals(otherCf2));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class IntersectionMethodMatcher
/*     */     implements IntroductionAwareMethodMatcher, Serializable
/*     */   {
/*     */     private final MethodMatcher mm1;
/*     */ 
/*     */     
/*     */     private final MethodMatcher mm2;
/*     */ 
/*     */     
/*     */     public IntersectionMethodMatcher(MethodMatcher mm1, MethodMatcher mm2) {
/* 225 */       Assert.notNull(mm1, "First MethodMatcher must not be null");
/* 226 */       Assert.notNull(mm2, "Second MethodMatcher must not be null");
/* 227 */       this.mm1 = mm1;
/* 228 */       this.mm2 = mm2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, boolean hasIntroductions) {
/* 233 */       return (MethodMatchers.matches(this.mm1, method, targetClass, hasIntroductions) && 
/* 234 */         MethodMatchers.matches(this.mm2, method, targetClass, hasIntroductions));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 239 */       return (this.mm1.matches(method, targetClass) && this.mm2.matches(method, targetClass));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRuntime() {
/* 244 */       return (this.mm1.isRuntime() || this.mm2.isRuntime());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 253 */       boolean aMatches = this.mm1.isRuntime() ? this.mm1.matches(method, targetClass, args) : this.mm1.matches(method, targetClass);
/*     */       
/* 255 */       boolean bMatches = this.mm2.isRuntime() ? this.mm2.matches(method, targetClass, args) : this.mm2.matches(method, targetClass);
/* 256 */       return (aMatches && bMatches);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 261 */       if (this == other) {
/* 262 */         return true;
/*     */       }
/* 264 */       if (!(other instanceof IntersectionMethodMatcher)) {
/* 265 */         return false;
/*     */       }
/* 267 */       IntersectionMethodMatcher that = (IntersectionMethodMatcher)other;
/* 268 */       return (this.mm1.equals(that.mm1) && this.mm2.equals(that.mm2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 273 */       int hashCode = 17;
/* 274 */       hashCode = 37 * hashCode + this.mm1.hashCode();
/* 275 */       hashCode = 37 * hashCode + this.mm2.hashCode();
/* 276 */       return hashCode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\support\MethodMatchers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */