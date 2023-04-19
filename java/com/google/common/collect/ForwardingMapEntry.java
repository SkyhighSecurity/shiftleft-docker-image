/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Map;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingMapEntry<K, V>
/*    */   extends ForwardingObject
/*    */   implements Map.Entry<K, V>
/*    */ {
/*    */   public K getKey() {
/* 41 */     return delegate().getKey();
/*    */   }
/*    */   
/*    */   public V getValue() {
/* 45 */     return delegate().getValue();
/*    */   }
/*    */   
/*    */   public V setValue(V value) {
/* 49 */     return delegate().setValue(value);
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 53 */     return delegate().equals(object);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 57 */     return delegate().hashCode();
/*    */   }
/*    */   
/*    */   protected abstract Map.Entry<K, V> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingMapEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */