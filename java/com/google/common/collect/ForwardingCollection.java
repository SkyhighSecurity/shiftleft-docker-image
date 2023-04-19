/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingCollection<E>
/*    */   extends ForwardingObject
/*    */   implements Collection<E>
/*    */ {
/*    */   public Iterator<E> iterator() {
/* 40 */     return delegate().iterator();
/*    */   }
/*    */   
/*    */   public int size() {
/* 44 */     return delegate().size();
/*    */   }
/*    */   
/*    */   public boolean removeAll(Collection<?> collection) {
/* 48 */     return delegate().removeAll(collection);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 52 */     return delegate().isEmpty();
/*    */   }
/*    */   
/*    */   public boolean contains(Object object) {
/* 56 */     return delegate().contains(object);
/*    */   }
/*    */   
/*    */   public Object[] toArray() {
/* 60 */     return delegate().toArray();
/*    */   }
/*    */   
/*    */   public <T> T[] toArray(T[] array) {
/* 64 */     return delegate().toArray(array);
/*    */   }
/*    */   
/*    */   public boolean add(E element) {
/* 68 */     return delegate().add(element);
/*    */   }
/*    */   
/*    */   public boolean remove(Object object) {
/* 72 */     return delegate().remove(object);
/*    */   }
/*    */   
/*    */   public boolean containsAll(Collection<?> collection) {
/* 76 */     return delegate().containsAll(collection);
/*    */   }
/*    */   
/*    */   public boolean addAll(Collection<? extends E> collection) {
/* 80 */     return delegate().addAll(collection);
/*    */   }
/*    */   
/*    */   public boolean retainAll(Collection<?> collection) {
/* 84 */     return delegate().retainAll(collection);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 88 */     delegate().clear();
/*    */   }
/*    */   
/*    */   protected abstract Collection<E> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */