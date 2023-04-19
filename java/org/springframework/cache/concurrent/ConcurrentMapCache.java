/*     */ package org.springframework.cache.concurrent;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.support.AbstractValueAdaptingCache;
/*     */ import org.springframework.core.serializer.support.SerializationDelegate;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConcurrentMapCache
/*     */   extends AbstractValueAdaptingCache
/*     */ {
/*     */   private final String name;
/*     */   private final ConcurrentMap<Object, Object> store;
/*     */   private final SerializationDelegate serialization;
/*     */   
/*     */   public ConcurrentMapCache(String name) {
/*  62 */     this(name, new ConcurrentHashMap<Object, Object>(256), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMapCache(String name, boolean allowNullValues) {
/*  72 */     this(name, new ConcurrentHashMap<Object, Object>(256), allowNullValues);
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
/*     */   public ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
/*  84 */     this(name, store, allowNullValues, (SerializationDelegate)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues, SerializationDelegate serialization) {
/* 103 */     super(allowNullValues);
/* 104 */     Assert.notNull(name, "Name must not be null");
/* 105 */     Assert.notNull(store, "Store must not be null");
/* 106 */     this.name = name;
/* 107 */     this.store = store;
/* 108 */     this.serialization = serialization;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isStoreByValue() {
/* 119 */     return (this.serialization != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 124 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ConcurrentMap<Object, Object> getNativeCache() {
/* 129 */     return this.store;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object lookup(Object key) {
/* 134 */     return this.store.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(Object key, Callable<T> valueLoader) {
/* 141 */     Cache.ValueWrapper storeValue = get(key);
/* 142 */     if (storeValue != null) {
/* 143 */       return (T)storeValue.get();
/*     */     }
/*     */ 
/*     */     
/* 147 */     synchronized (this.store) {
/* 148 */       T value; storeValue = get(key);
/* 149 */       if (storeValue != null) {
/* 150 */         return (T)storeValue.get();
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 155 */         value = valueLoader.call();
/*     */       }
/* 157 */       catch (Throwable ex) {
/* 158 */         throw new Cache.ValueRetrievalException(key, valueLoader, ex);
/*     */       } 
/* 160 */       put(key, value);
/* 161 */       return value;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(Object key, Object value) {
/* 167 */     this.store.put(key, toStoreValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public Cache.ValueWrapper putIfAbsent(Object key, Object value) {
/* 172 */     Object existing = this.store.putIfAbsent(key, toStoreValue(value));
/* 173 */     return toValueWrapper(existing);
/*     */   }
/*     */ 
/*     */   
/*     */   public void evict(Object key) {
/* 178 */     this.store.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 183 */     this.store.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object toStoreValue(Object userValue) {
/* 188 */     Object storeValue = super.toStoreValue(userValue);
/* 189 */     if (this.serialization != null) {
/*     */       try {
/* 191 */         return serializeValue(storeValue);
/*     */       }
/* 193 */       catch (Throwable ex) {
/* 194 */         throw new IllegalArgumentException("Failed to serialize cache value '" + userValue + "'. Does it implement Serializable?", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 199 */     return storeValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object serializeValue(Object storeValue) throws IOException {
/* 204 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */     try {
/* 206 */       this.serialization.serialize(storeValue, out);
/* 207 */       return out.toByteArray();
/*     */     } finally {
/*     */       
/* 210 */       out.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object fromStoreValue(Object storeValue) {
/* 216 */     if (this.serialization != null) {
/*     */       try {
/* 218 */         return super.fromStoreValue(deserializeValue(storeValue));
/*     */       }
/* 220 */       catch (Throwable ex) {
/* 221 */         throw new IllegalArgumentException("Failed to deserialize cache value '" + storeValue + "'", ex);
/*     */       } 
/*     */     }
/*     */     
/* 225 */     return super.fromStoreValue(storeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object deserializeValue(Object storeValue) throws IOException {
/* 231 */     ByteArrayInputStream in = new ByteArrayInputStream((byte[])storeValue);
/*     */     try {
/* 233 */       return this.serialization.deserialize(in);
/*     */     } finally {
/*     */       
/* 236 */       in.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\concurrent\ConcurrentMapCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */