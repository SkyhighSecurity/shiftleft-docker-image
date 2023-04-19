/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible
/*     */ public final class HashBiMap<K, V>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create() {
/*  44 */     return new HashBiMap<K, V>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(int expectedSize) {
/*  55 */     return new HashBiMap<K, V>(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
/*  65 */     HashBiMap<K, V> bimap = create(map.size());
/*  66 */     bimap.putAll(map);
/*  67 */     return bimap;
/*     */   }
/*     */   
/*     */   private HashBiMap() {
/*  71 */     super(new HashMap<K, V>(), new HashMap<V, K>());
/*     */   }
/*     */   
/*     */   private HashBiMap(int expectedSize) {
/*  75 */     super(new HashMap<K, V>(Maps.capacity(expectedSize)), new HashMap<V, K>(Maps.capacity(expectedSize)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(@Nullable K key, @Nullable V value) {
/*  82 */     return super.put(key, value);
/*     */   }
/*     */   
/*     */   public V forcePut(@Nullable K key, @Nullable V value) {
/*  86 */     return super.forcePut(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/*  94 */     stream.defaultWriteObject();
/*  95 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 100 */     stream.defaultReadObject();
/* 101 */     int size = Serialization.readCount(stream);
/* 102 */     setDelegates(Maps.newHashMapWithExpectedSize(size), Maps.newHashMapWithExpectedSize(size));
/*     */     
/* 104 */     Serialization.populateMap(this, stream, size);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\HashBiMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */