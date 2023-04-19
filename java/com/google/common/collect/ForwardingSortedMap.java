/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import java.util.SortedMap;
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
/*    */ public abstract class ForwardingSortedMap<K, V>
/*    */   extends ForwardingMap<K, V>
/*    */   implements SortedMap<K, V>
/*    */ {
/*    */   public Comparator<? super K> comparator() {
/* 40 */     return delegate().comparator();
/*    */   }
/*    */   
/*    */   public K firstKey() {
/* 44 */     return delegate().firstKey();
/*    */   }
/*    */   
/*    */   public SortedMap<K, V> headMap(K toKey) {
/* 48 */     return delegate().headMap(toKey);
/*    */   }
/*    */   
/*    */   public K lastKey() {
/* 52 */     return delegate().lastKey();
/*    */   }
/*    */   
/*    */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 56 */     return delegate().subMap(fromKey, toKey);
/*    */   }
/*    */   
/*    */   public SortedMap<K, V> tailMap(K fromKey) {
/* 60 */     return delegate().tailMap(fromKey);
/*    */   }
/*    */   
/*    */   protected abstract SortedMap<K, V> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingSortedMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */