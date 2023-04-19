/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible
/*     */ public final class Predicates
/*     */ {
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysTrue() {
/*  53 */     return AlwaysTruePredicate.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysFalse() {
/*  62 */     return AlwaysFalsePredicate.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> isNull() {
/*  71 */     return IsNullPredicate.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> notNull() {
/*  80 */     return NotNullPredicate.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> not(Predicate<T> predicate) {
/*  88 */     return new NotPredicate<T>(predicate);
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
/*     */   public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) {
/* 102 */     return new AndPredicate<T>(defensiveCopy(components));
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
/*     */   public static <T> Predicate<T> and(Predicate<? super T>... components) {
/* 115 */     return new AndPredicate<T>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) {
/* 126 */     return new AndPredicate<T>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
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
/*     */   public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) {
/* 141 */     return new OrPredicate<T>(defensiveCopy(components));
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
/*     */   public static <T> Predicate<T> or(Predicate<? super T>... components) {
/* 154 */     return new OrPredicate<T>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) {
/* 165 */     return new OrPredicate<T>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> equalTo(@Nullable T target) {
/* 175 */     return (target == null) ? isNull() : new IsEqualToPredicate<T>(target);
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
/*     */   @GwtIncompatible("Class.isInstance")
/*     */   public static Predicate<Object> instanceOf(Class<?> clazz) {
/* 191 */     return new InstanceOfPredicate(clazz);
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
/*     */   public static <T> Predicate<T> in(Collection<? extends T> target) {
/* 207 */     return new InPredicate<T>(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
/* 218 */     return new CompositionPredicate<A, Object>(predicate, function);
/*     */   }
/*     */   
/*     */   enum AlwaysTruePredicate
/*     */     implements Predicate<Object>
/*     */   {
/* 224 */     INSTANCE;
/*     */     
/*     */     public boolean apply(Object o) {
/* 227 */       return true;
/*     */     }
/*     */     public String toString() {
/* 230 */       return "AlwaysTrue";
/*     */     }
/*     */   }
/*     */   
/*     */   enum AlwaysFalsePredicate
/*     */     implements Predicate<Object>
/*     */   {
/* 237 */     INSTANCE;
/*     */     
/*     */     public boolean apply(Object o) {
/* 240 */       return false;
/*     */     }
/*     */     public String toString() {
/* 243 */       return "AlwaysFalse";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class NotPredicate<T>
/*     */     implements Predicate<T>, Serializable {
/*     */     private final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private NotPredicate(Predicate<T> predicate) {
/* 253 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */     public boolean apply(T t) {
/* 256 */       return !this.predicate.apply(t);
/*     */     }
/*     */     public int hashCode() {
/* 259 */       return this.predicate.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */     public boolean equals(Object obj) {
/* 262 */       if (obj instanceof NotPredicate) {
/* 263 */         NotPredicate<?> that = (NotPredicate)obj;
/* 264 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 266 */       return false;
/*     */     }
/*     */     public String toString() {
/* 269 */       return "Not(" + this.predicate.toString() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 274 */   private static final Joiner commaJoiner = Joiner.on(",");
/*     */   
/*     */   private static class AndPredicate<T>
/*     */     implements Predicate<T>, Serializable {
/*     */     private final Iterable<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private AndPredicate(Iterable<? extends Predicate<? super T>> components) {
/* 282 */       this.components = components;
/*     */     }
/*     */     public boolean apply(T t) {
/* 285 */       for (Predicate<? super T> predicate : this.components) {
/* 286 */         if (!predicate.apply(t)) {
/* 287 */           return false;
/*     */         }
/*     */       } 
/* 290 */       return true;
/*     */     }
/*     */     public int hashCode() {
/* 293 */       int result = -1;
/* 294 */       for (Predicate<? super T> predicate : this.components) {
/* 295 */         result &= predicate.hashCode();
/*     */       }
/* 297 */       return result;
/*     */     }
/*     */     public boolean equals(Object obj) {
/* 300 */       if (obj instanceof AndPredicate) {
/* 301 */         AndPredicate<?> that = (AndPredicate)obj;
/* 302 */         return Predicates.iterableElementsEqual(this.components, that.components);
/*     */       } 
/* 304 */       return false;
/*     */     }
/*     */     public String toString() {
/* 307 */       return "And(" + Predicates.commaJoiner.join(this.components) + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OrPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final Iterable<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private OrPredicate(Iterable<? extends Predicate<? super T>> components) {
/* 318 */       this.components = components;
/*     */     }
/*     */     public boolean apply(T t) {
/* 321 */       for (Predicate<? super T> predicate : this.components) {
/* 322 */         if (predicate.apply(t)) {
/* 323 */           return true;
/*     */         }
/*     */       } 
/* 326 */       return false;
/*     */     }
/*     */     public int hashCode() {
/* 329 */       int result = 0;
/* 330 */       for (Predicate<? super T> predicate : this.components) {
/* 331 */         result |= predicate.hashCode();
/*     */       }
/* 333 */       return result;
/*     */     }
/*     */     public boolean equals(Object obj) {
/* 336 */       if (obj instanceof OrPredicate) {
/* 337 */         OrPredicate<?> that = (OrPredicate)obj;
/* 338 */         return Predicates.iterableElementsEqual(this.components, that.components);
/*     */       } 
/* 340 */       return false;
/*     */     }
/*     */     public String toString() {
/* 343 */       return "Or(" + Predicates.commaJoiner.join(this.components) + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class IsEqualToPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private IsEqualToPredicate(T target) {
/* 354 */       this.target = target;
/*     */     }
/*     */     public boolean apply(T t) {
/* 357 */       return this.target.equals(t);
/*     */     }
/*     */     public int hashCode() {
/* 360 */       return this.target.hashCode();
/*     */     }
/*     */     public boolean equals(Object obj) {
/* 363 */       if (obj instanceof IsEqualToPredicate) {
/* 364 */         IsEqualToPredicate<?> that = (IsEqualToPredicate)obj;
/* 365 */         return this.target.equals(that.target);
/*     */       } 
/* 367 */       return false;
/*     */     }
/*     */     public String toString() {
/* 370 */       return "IsEqualTo(" + this.target + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InstanceOfPredicate
/*     */     implements Predicate<Object>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InstanceOfPredicate(Class<?> clazz) {
/* 381 */       this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
/*     */     }
/*     */     public boolean apply(Object o) {
/* 384 */       return Platform.isInstance(this.clazz, o);
/*     */     }
/*     */     public int hashCode() {
/* 387 */       return this.clazz.hashCode();
/*     */     }
/*     */     public boolean equals(Object obj) {
/* 390 */       if (obj instanceof InstanceOfPredicate) {
/* 391 */         InstanceOfPredicate that = (InstanceOfPredicate)obj;
/* 392 */         return (this.clazz == that.clazz);
/*     */       } 
/* 394 */       return false;
/*     */     }
/*     */     public String toString() {
/* 397 */       return "IsInstanceOf(" + this.clazz.getName() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private enum IsNullPredicate
/*     */     implements Predicate<Object>
/*     */   {
/* 405 */     INSTANCE;
/*     */     
/*     */     public boolean apply(Object o) {
/* 408 */       return (o == null);
/*     */     }
/*     */     public String toString() {
/* 411 */       return "IsNull";
/*     */     }
/*     */   }
/*     */   
/*     */   private enum NotNullPredicate
/*     */     implements Predicate<Object>
/*     */   {
/* 418 */     INSTANCE;
/*     */     
/*     */     public boolean apply(Object o) {
/* 421 */       return (o != null);
/*     */     }
/*     */     public String toString() {
/* 424 */       return "NotNull";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InPredicate<T>
/*     */     implements Predicate<T>, Serializable {
/*     */     private final Collection<?> target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InPredicate(Collection<?> target) {
/* 434 */       this.target = Preconditions.<Collection>checkNotNull(target);
/*     */     }
/*     */     
/*     */     public boolean apply(T t) {
/*     */       try {
/* 439 */         return this.target.contains(t);
/* 440 */       } catch (NullPointerException e) {
/* 441 */         return false;
/* 442 */       } catch (ClassCastException e) {
/* 443 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 448 */       if (obj instanceof InPredicate) {
/* 449 */         InPredicate<?> that = (InPredicate)obj;
/* 450 */         return this.target.equals(that.target);
/*     */       } 
/* 452 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 456 */       return this.target.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 460 */       return "In(" + this.target + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompositionPredicate<A, B>
/*     */     implements Predicate<A>, Serializable
/*     */   {
/*     */     final Predicate<B> p;
/*     */     final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
/* 472 */       this.p = Preconditions.<Predicate<B>>checkNotNull(p);
/* 473 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */     
/*     */     public boolean apply(A a) {
/* 477 */       return this.p.apply(this.f.apply(a));
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 481 */       if (obj instanceof CompositionPredicate) {
/* 482 */         CompositionPredicate<?, ?> that = (CompositionPredicate<?, ?>)obj;
/* 483 */         return (this.f.equals(that.f) && this.p.equals(that.p));
/*     */       } 
/* 485 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 502 */       return this.f.hashCode() ^ this.p.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 506 */       return this.p.toString() + "(" + this.f.toString() + ")";
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
/*     */   private static boolean iterableElementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
/* 524 */     Iterator<?> iterator1 = iterable1.iterator();
/* 525 */     Iterator<?> iterator2 = iterable2.iterator();
/* 526 */     while (iterator1.hasNext()) {
/* 527 */       if (!iterator2.hasNext()) {
/* 528 */         return false;
/*     */       }
/* 530 */       if (!iterator1.next().equals(iterator2.next())) {
/* 531 */         return false;
/*     */       }
/*     */     } 
/* 534 */     return !iterator2.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) {
/* 540 */     return Arrays.asList((Predicate<? super T>[])new Predicate[] { first, second });
/*     */   }
/*     */   
/*     */   private static <T> List<T> defensiveCopy(T... array) {
/* 544 */     return defensiveCopy(Arrays.asList(array));
/*     */   }
/*     */   
/*     */   static <T> List<T> defensiveCopy(Iterable<T> iterable) {
/* 548 */     ArrayList<T> list = new ArrayList<T>();
/* 549 */     for (T element : iterable) {
/* 550 */       list.add(Preconditions.checkNotNull(element));
/*     */     }
/* 552 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\base\Predicates.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */