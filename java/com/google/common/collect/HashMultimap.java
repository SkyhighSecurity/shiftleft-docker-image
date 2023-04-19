/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public final class HashMultimap<K, V>
/*     */   extends AbstractSetMultimap<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 8;
/*     */   @VisibleForTesting
/*  51 */   transient int expectedValuesPerKey = 8;
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashMultimap<K, V> create() {
/*  59 */     return new HashMultimap<K, V>();
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
/*     */   public static <K, V> HashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/*  73 */     return new HashMultimap<K, V>(expectedKeys, expectedValuesPerKey);
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
/*     */   public static <K, V> HashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/*  85 */     return new HashMultimap<K, V>(multimap);
/*     */   }
/*     */   
/*     */   private HashMultimap() {
/*  89 */     super(new HashMap<K, Collection<V>>());
/*     */   }
/*     */   
/*     */   private HashMultimap(int expectedKeys, int expectedValuesPerKey) {
/*  93 */     super(Maps.newHashMapWithExpectedSize(expectedKeys));
/*  94 */     Preconditions.checkArgument((expectedValuesPerKey >= 0));
/*  95 */     this.expectedValuesPerKey = expectedValuesPerKey;
/*     */   }
/*     */   
/*     */   private HashMultimap(Multimap<? extends K, ? extends V> multimap) {
/*  99 */     super(Maps.newHashMapWithExpectedSize(multimap.keySet().size()));
/*     */     
/* 101 */     putAll(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<V> createCollection() {
/* 112 */     return Sets.newHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 121 */     stream.defaultWriteObject();
/* 122 */     stream.writeInt(this.expectedValuesPerKey);
/* 123 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 128 */     stream.defaultReadObject();
/* 129 */     this.expectedValuesPerKey = stream.readInt();
/* 130 */     int distinctKeys = Serialization.readCount(stream);
/* 131 */     Map<K, Collection<V>> map = Maps.newHashMapWithExpectedSize(distinctKeys);
/* 132 */     setMap(map);
/* 133 */     Serialization.populateMultimap(this, stream, distinctKeys);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\HashMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */