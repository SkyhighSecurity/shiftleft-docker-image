/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.ListIterator;
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
/*    */ public abstract class ForwardingList<E>
/*    */   extends ForwardingCollection<E>
/*    */   implements List<E>
/*    */ {
/*    */   public void add(int index, E element) {
/* 47 */     delegate().add(index, element);
/*    */   }
/*    */   
/*    */   public boolean addAll(int index, Collection<? extends E> elements) {
/* 51 */     return delegate().addAll(index, elements);
/*    */   }
/*    */   
/*    */   public E get(int index) {
/* 55 */     return delegate().get(index);
/*    */   }
/*    */   
/*    */   public int indexOf(Object element) {
/* 59 */     return delegate().indexOf(element);
/*    */   }
/*    */   
/*    */   public int lastIndexOf(Object element) {
/* 63 */     return delegate().lastIndexOf(element);
/*    */   }
/*    */   
/*    */   public ListIterator<E> listIterator() {
/* 67 */     return delegate().listIterator();
/*    */   }
/*    */   
/*    */   public ListIterator<E> listIterator(int index) {
/* 71 */     return delegate().listIterator(index);
/*    */   }
/*    */   
/*    */   public E remove(int index) {
/* 75 */     return delegate().remove(index);
/*    */   }
/*    */   
/*    */   public E set(int index, E element) {
/* 79 */     return delegate().set(index, element);
/*    */   }
/*    */   
/*    */   @GwtIncompatible("List.subList")
/*    */   public List<E> subList(int fromIndex, int toIndex) {
/* 84 */     return Platform.subList(delegate(), fromIndex, toIndex);
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 88 */     return (object == this || delegate().equals(object));
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 92 */     return delegate().hashCode();
/*    */   }
/*    */   
/*    */   protected abstract List<E> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */