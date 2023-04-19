/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Functions
/*     */ {
/*     */   public static Function<Object, String> toStringFunction() {
/*  48 */     return ToStringFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum ToStringFunction
/*     */     implements Function<Object, String> {
/*  53 */     INSTANCE;
/*     */     
/*     */     public String apply(Object o) {
/*  56 */       return o.toString();
/*     */     }
/*     */     
/*     */     public String toString() {
/*  60 */       return "toString";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Function<E, E> identity() {
/*  69 */     return IdentityFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum IdentityFunction
/*     */     implements Function<Object, Object> {
/*  74 */     INSTANCE;
/*     */     
/*     */     public Object apply(Object o) {
/*  77 */       return o;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  81 */       return "identity";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Function<K, V> forMap(Map<K, V> map) {
/*  91 */     return new FunctionForMapNoDefault<K, V>(map);
/*     */   }
/*     */   
/*     */   private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
/*     */     final Map<K, V> map;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     FunctionForMapNoDefault(Map<K, V> map) {
/*  99 */       this.map = Preconditions.<Map<K, V>>checkNotNull(map);
/*     */     }
/*     */     public V apply(K key) {
/* 102 */       V result = this.map.get(key);
/* 103 */       Preconditions.checkArgument((result != null || this.map.containsKey(key)), "Key '%s' not present in map", new Object[] { key });
/*     */       
/* 105 */       return result;
/*     */     }
/*     */     public boolean equals(Object o) {
/* 108 */       if (o instanceof FunctionForMapNoDefault) {
/* 109 */         FunctionForMapNoDefault<?, ?> that = (FunctionForMapNoDefault<?, ?>)o;
/* 110 */         return this.map.equals(that.map);
/*     */       } 
/* 112 */       return false;
/*     */     }
/*     */     public int hashCode() {
/* 115 */       return this.map.hashCode();
/*     */     }
/*     */     public String toString() {
/* 118 */       return "forMap(" + this.map + ")";
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @Nullable V defaultValue) {
/* 135 */     return new ForMapWithDefault<K, V>(map, defaultValue);
/*     */   }
/*     */   
/*     */   private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable {
/*     */     final Map<K, ? extends V> map;
/*     */     final V defaultValue;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ForMapWithDefault(Map<K, ? extends V> map, V defaultValue) {
/* 144 */       this.map = Preconditions.<Map<K, ? extends V>>checkNotNull(map);
/* 145 */       this.defaultValue = defaultValue;
/*     */     }
/*     */     public V apply(K key) {
/* 148 */       return this.map.containsKey(key) ? this.map.get(key) : this.defaultValue;
/*     */     }
/*     */     public boolean equals(Object o) {
/* 151 */       if (o instanceof ForMapWithDefault) {
/* 152 */         ForMapWithDefault<?, ?> that = (ForMapWithDefault<?, ?>)o;
/* 153 */         return (this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue));
/*     */       } 
/*     */       
/* 156 */       return false;
/*     */     }
/*     */     public int hashCode() {
/* 159 */       return Objects.hashCode(new Object[] { this.map, this.defaultValue });
/*     */     }
/*     */     public String toString() {
/* 162 */       return "forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
/* 181 */     return new FunctionComposition<A, B, C>(g, f);
/*     */   }
/*     */   
/*     */   private static class FunctionComposition<A, B, C>
/*     */     implements Function<A, C>, Serializable {
/*     */     private final Function<B, C> g;
/*     */     private final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
/* 191 */       this.g = Preconditions.<Function<B, C>>checkNotNull(g);
/* 192 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */     public C apply(A a) {
/* 195 */       return this.g.apply(this.f.apply(a));
/*     */     }
/*     */     public boolean equals(Object obj) {
/* 198 */       if (obj instanceof FunctionComposition) {
/* 199 */         FunctionComposition<?, ?, ?> that = (FunctionComposition<?, ?, ?>)obj;
/* 200 */         return (this.f.equals(that.f) && this.g.equals(that.g));
/*     */       } 
/* 202 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 206 */       return this.f.hashCode() ^ this.g.hashCode();
/*     */     }
/*     */     public String toString() {
/* 209 */       return this.g.toString() + "(" + this.f.toString() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
/* 219 */     return new PredicateFunction<T>(predicate);
/*     */   }
/*     */   
/*     */   private static class PredicateFunction<T>
/*     */     implements Function<T, Boolean>, Serializable {
/*     */     private final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private PredicateFunction(Predicate<T> predicate) {
/* 228 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */     
/*     */     public Boolean apply(T t) {
/* 232 */       return Boolean.valueOf(this.predicate.apply(t));
/*     */     }
/*     */     public boolean equals(Object obj) {
/* 235 */       if (obj instanceof PredicateFunction) {
/* 236 */         PredicateFunction<?> that = (PredicateFunction)obj;
/* 237 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 239 */       return false;
/*     */     }
/*     */     public int hashCode() {
/* 242 */       return this.predicate.hashCode();
/*     */     }
/*     */     public String toString() {
/* 245 */       return "forPredicate(" + this.predicate + ")";
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
/*     */   public static <E> Function<Object, E> constant(@Nullable E value) {
/* 257 */     return new ConstantFunction<E>(value);
/*     */   }
/*     */   
/*     */   private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
/*     */     private final E value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public ConstantFunction(@Nullable E value) {
/* 265 */       this.value = value;
/*     */     }
/*     */     public E apply(Object from) {
/* 268 */       return this.value;
/*     */     }
/*     */     public boolean equals(Object obj) {
/* 271 */       if (obj instanceof ConstantFunction) {
/* 272 */         ConstantFunction<?> that = (ConstantFunction)obj;
/* 273 */         return Objects.equal(this.value, that.value);
/*     */       } 
/* 275 */       return false;
/*     */     }
/*     */     public int hashCode() {
/* 278 */       return (this.value == null) ? 0 : this.value.hashCode();
/*     */     }
/*     */     public String toString() {
/* 281 */       return "constant(" + this.value + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\base\Functions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */