/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ final class ImmutableEnumSet<E>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   private final transient Set<E> delegate;
/*     */   private transient int hashCode;
/*     */   
/*     */   ImmutableEnumSet(Set<E> delegate) {
/*  54 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  58 */     return Iterators.unmodifiableIterator(this.delegate.iterator());
/*     */   }
/*     */   
/*     */   public int size() {
/*  62 */     return this.delegate.size();
/*     */   }
/*     */   
/*     */   public boolean contains(Object object) {
/*  66 */     return this.delegate.contains(object);
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> collection) {
/*  70 */     return this.delegate.containsAll(collection);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  74 */     return this.delegate.isEmpty();
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/*  78 */     return this.delegate.toArray();
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/*  82 */     return this.delegate.toArray(array);
/*     */   }
/*     */   
/*     */   public boolean equals(Object object) {
/*  86 */     return (object == this || this.delegate.equals(object));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  92 */     int result = this.hashCode;
/*  93 */     return (result == 0) ? (this.hashCode = this.delegate.hashCode()) : result;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  97 */     return this.delegate.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 103 */     return new EnumSerializedForm<Enum>((EnumSet)this.delegate);
/*     */   }
/*     */   
/*     */   private static class EnumSerializedForm<E extends Enum<E>>
/*     */     implements Serializable
/*     */   {
/*     */     final EnumSet<E> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EnumSerializedForm(EnumSet<E> delegate) {
/* 113 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 117 */       return new ImmutableEnumSet(this.delegate.clone());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableEnumSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */