/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EnumMap;
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
/*     */ public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private transient Class<K> keyType;
/*     */   private transient Class<V> valueType;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> keyType, Class<V> valueType) {
/*  48 */     return new EnumBiMap<K, V>(keyType, valueType);
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
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> map) {
/*  63 */     EnumBiMap<K, V> bimap = create(inferKeyType(map), inferValueType(map));
/*  64 */     bimap.putAll(map);
/*  65 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumBiMap(Class<K> keyType, Class<V> valueType) {
/*  69 */     super(new EnumMap<K, V>(keyType), (Map)new EnumMap<V, Object>(valueType));
/*  70 */     this.keyType = keyType;
/*  71 */     this.valueType = valueType;
/*     */   }
/*     */   
/*     */   static <K extends Enum<K>> Class<K> inferKeyType(Map<K, ?> map) {
/*  75 */     if (map instanceof EnumBiMap) {
/*  76 */       return ((EnumBiMap)map).keyType();
/*     */     }
/*  78 */     if (map instanceof EnumHashBiMap) {
/*  79 */       return ((EnumHashBiMap)map).keyType();
/*     */     }
/*  81 */     Preconditions.checkArgument(!map.isEmpty());
/*  82 */     return ((Enum<K>)map.keySet().iterator().next()).getDeclaringClass();
/*     */   }
/*     */   
/*     */   private static <V extends Enum<V>> Class<V> inferValueType(Map<?, V> map) {
/*  86 */     if (map instanceof EnumBiMap) {
/*  87 */       return ((EnumBiMap)map).valueType;
/*     */     }
/*  89 */     Preconditions.checkArgument(!map.isEmpty());
/*  90 */     return ((Enum<V>)map.values().iterator().next()).getDeclaringClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<K> keyType() {
/*  95 */     return this.keyType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<V> valueType() {
/* 100 */     return this.valueType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 108 */     stream.defaultWriteObject();
/* 109 */     stream.writeObject(this.keyType);
/* 110 */     stream.writeObject(this.valueType);
/* 111 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 117 */     stream.defaultReadObject();
/* 118 */     this.keyType = (Class<K>)stream.readObject();
/* 119 */     this.valueType = (Class<V>)stream.readObject();
/* 120 */     setDelegates(new EnumMap<K, V>(this.keyType), (Map)new EnumMap<V, Object>(this.valueType));
/* 121 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\EnumBiMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */