/*     */ package com.google.common.collect;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EnumMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EnumHashBiMap<K extends Enum<K>, V>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private transient Class<K> keyType;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Class<K> keyType) {
/*  47 */     return new EnumHashBiMap<K, V>(keyType);
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
/*     */   
/*     */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Map<K, ? extends V> map) {
/*  62 */     EnumHashBiMap<K, V> bimap = create(EnumBiMap.inferKeyType(map));
/*  63 */     bimap.putAll(map);
/*  64 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumHashBiMap(Class<K> keyType) {
/*  68 */     super(new EnumMap<K, V>(keyType), Maps.newHashMapWithExpectedSize(((Enum[])keyType.getEnumConstants()).length));
/*     */     
/*  70 */     this.keyType = keyType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, @Nullable V value) {
/*  76 */     return super.put(key, value);
/*     */   }
/*     */   
/*     */   public V forcePut(K key, @Nullable V value) {
/*  80 */     return super.forcePut(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<K> keyType() {
/*  85 */     return this.keyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/*  93 */     stream.defaultWriteObject();
/*  94 */     stream.writeObject(this.keyType);
/*  95 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 101 */     stream.defaultReadObject();
/* 102 */     this.keyType = (Class<K>)stream.readObject();
/* 103 */     setDelegates(new EnumMap<K, V>(this.keyType), new HashMap<V, K>(((Enum[])this.keyType.getEnumConstants()).length * 3 / 2));
/*     */     
/* 105 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\EnumHashBiMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */