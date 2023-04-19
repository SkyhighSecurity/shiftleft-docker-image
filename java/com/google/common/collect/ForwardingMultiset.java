/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingMultiset<E>
/*    */   extends ForwardingCollection<E>
/*    */   implements Multiset<E>
/*    */ {
/*    */   public int count(Object element) {
/* 41 */     return delegate().count(element);
/*    */   }
/*    */   
/*    */   public int add(E element, int occurrences) {
/* 45 */     return delegate().add(element, occurrences);
/*    */   }
/*    */   
/*    */   public int remove(Object element, int occurrences) {
/* 49 */     return delegate().remove(element, occurrences);
/*    */   }
/*    */   
/*    */   public Set<E> elementSet() {
/* 53 */     return delegate().elementSet();
/*    */   }
/*    */   
/*    */   public Set<Multiset.Entry<E>> entrySet() {
/* 57 */     return delegate().entrySet();
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 61 */     return (object == this || delegate().equals(object));
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 65 */     return delegate().hashCode();
/*    */   }
/*    */   
/*    */   public int setCount(E element, int count) {
/* 69 */     return delegate().setCount(element, count);
/*    */   }
/*    */   
/*    */   public boolean setCount(E element, int oldCount, int newCount) {
/* 73 */     return delegate().setCount(element, oldCount, newCount);
/*    */   }
/*    */   
/*    */   protected abstract Multiset<E> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingMultiset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */