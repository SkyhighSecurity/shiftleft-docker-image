/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class EmptyImmutableSet
/*    */   extends ImmutableSet<Object>
/*    */ {
/* 33 */   static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
/*    */ 
/*    */ 
/*    */   
/*    */   public int size() {
/* 38 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public boolean contains(Object target) {
/* 46 */     return false;
/*    */   }
/*    */   
/*    */   public UnmodifiableIterator<Object> iterator() {
/* 50 */     return Iterators.emptyIterator();
/*    */   }
/*    */   
/* 53 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*    */   
/*    */   public Object[] toArray() {
/* 56 */     return EMPTY_ARRAY;
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   public <T> T[] toArray(T[] a) {
/* 60 */     if (a.length > 0) {
/* 61 */       a[0] = null;
/*    */     }
/* 63 */     return a;
/*    */   }
/*    */   
/*    */   public boolean containsAll(Collection<?> targets) {
/* 67 */     return targets.isEmpty();
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 71 */     if (object instanceof Set) {
/* 72 */       Set<?> that = (Set)object;
/* 73 */       return that.isEmpty();
/*    */     } 
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/* 79 */     return 0;
/*    */   }
/*    */   
/*    */   boolean isHashCodeFast() {
/* 83 */     return true;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 87 */     return "[]";
/*    */   }
/*    */   
/*    */   Object readResolve() {
/* 91 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\EmptyImmutableSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */