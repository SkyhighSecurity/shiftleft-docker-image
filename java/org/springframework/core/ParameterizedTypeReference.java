/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
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
/*     */ public abstract class ParameterizedTypeReference<T>
/*     */ {
/*     */   private final Type type;
/*     */   
/*     */   protected ParameterizedTypeReference() {
/*  48 */     Class<?> parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
/*  49 */     Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
/*  50 */     Assert.isInstanceOf(ParameterizedType.class, type, "Type must be a parameterized type");
/*  51 */     ParameterizedType parameterizedType = (ParameterizedType)type;
/*  52 */     Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
/*  53 */     Assert.isTrue((actualTypeArguments.length == 1), "Number of type arguments must be 1");
/*  54 */     this.type = actualTypeArguments[0];
/*     */   }
/*     */   
/*     */   private ParameterizedTypeReference(Type type) {
/*  58 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getType() {
/*  63 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  68 */     return (this == obj || (obj instanceof ParameterizedTypeReference && this.type
/*  69 */       .equals(((ParameterizedTypeReference)obj).type)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  74 */     return this.type.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  79 */     return "ParameterizedTypeReference<" + this.type + ">";
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
/*     */   public static <T> ParameterizedTypeReference<T> forType(Type type) {
/*  92 */     return new ParameterizedTypeReference<T>(type) {  }
/*     */       ;
/*     */   }
/*     */   
/*     */   private static Class<?> findParameterizedTypeReferenceSubclass(Class<?> child) {
/*  97 */     Class<?> parent = child.getSuperclass();
/*  98 */     if (Object.class == parent) {
/*  99 */       throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
/*     */     }
/* 101 */     if (ParameterizedTypeReference.class == parent) {
/* 102 */       return child;
/*     */     }
/*     */     
/* 105 */     return findParameterizedTypeReferenceSubclass(parent);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\ParameterizedTypeReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */