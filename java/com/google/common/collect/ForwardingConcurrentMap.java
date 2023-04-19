/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentMap;
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
/*    */ public abstract class ForwardingConcurrentMap<K, V>
/*    */   extends ForwardingMap<K, V>
/*    */   implements ConcurrentMap<K, V>
/*    */ {
/*    */   public V putIfAbsent(K key, V value) {
/* 39 */     return delegate().putIfAbsent(key, value);
/*    */   }
/*    */   
/*    */   public boolean remove(Object key, Object value) {
/* 43 */     return delegate().remove(key, value);
/*    */   }
/*    */   
/*    */   public V replace(K key, V value) {
/* 47 */     return delegate().replace(key, value);
/*    */   }
/*    */   
/*    */   public boolean replace(K key, V oldValue, V newValue) {
/* 51 */     return delegate().replace(key, oldValue, newValue);
/*    */   }
/*    */   
/*    */   protected abstract ConcurrentMap<K, V> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingConcurrentMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */