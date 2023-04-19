/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.FinalizableReferenceQueue;
/*      */ import com.google.common.base.FinalizableSoftReference;
/*      */ import com.google.common.base.FinalizableWeakReference;
/*      */ import com.google.common.base.Function;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Field;
/*      */ import java.util.TimerTask;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class MapMaker
/*      */ {
/*   98 */   private Strength keyStrength = Strength.STRONG;
/*   99 */   private Strength valueStrength = Strength.STRONG;
/*  100 */   private long expirationNanos = 0L;
/*      */   private boolean useCustomMap;
/*  102 */   private final CustomConcurrentHashMap.Builder builder = new CustomConcurrentHashMap.Builder();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapMaker initialCapacity(int initialCapacity) {
/*  122 */     this.builder.initialCapacity(initialCapacity);
/*  123 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("java.util.concurrent.ConcurrentHashMap concurrencyLevel")
/*      */   public MapMaker concurrencyLevel(int concurrencyLevel) {
/*  146 */     this.builder.concurrencyLevel(concurrencyLevel);
/*  147 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("java.lang.ref.WeakReference")
/*      */   public MapMaker weakKeys() {
/*  166 */     return setKeyStrength(Strength.WEAK);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("java.lang.ref.SoftReference")
/*      */   public MapMaker softKeys() {
/*  185 */     return setKeyStrength(Strength.SOFT);
/*      */   }
/*      */   
/*      */   private MapMaker setKeyStrength(Strength strength) {
/*  189 */     if (this.keyStrength != Strength.STRONG) {
/*  190 */       throw new IllegalStateException("Key strength was already set to " + this.keyStrength + ".");
/*      */     }
/*      */     
/*  193 */     this.keyStrength = strength;
/*  194 */     this.useCustomMap = true;
/*  195 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("java.lang.ref.WeakReference")
/*      */   public MapMaker weakValues() {
/*  218 */     return setValueStrength(Strength.WEAK);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("java.lang.ref.SoftReference")
/*      */   public MapMaker softValues() {
/*  241 */     return setValueStrength(Strength.SOFT);
/*      */   }
/*      */   
/*      */   private MapMaker setValueStrength(Strength strength) {
/*  245 */     if (this.valueStrength != Strength.STRONG) {
/*  246 */       throw new IllegalStateException("Value strength was already set to " + this.valueStrength + ".");
/*      */     }
/*      */     
/*  249 */     this.valueStrength = strength;
/*  250 */     this.useCustomMap = true;
/*  251 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapMaker expiration(long duration, TimeUnit unit) {
/*  265 */     if (this.expirationNanos != 0L) {
/*  266 */       throw new IllegalStateException("expiration time of " + this.expirationNanos + " ns was already set");
/*      */     }
/*      */     
/*  269 */     if (duration <= 0L) {
/*  270 */       throw new IllegalArgumentException("invalid duration: " + duration);
/*      */     }
/*  272 */     this.expirationNanos = unit.toNanos(duration);
/*  273 */     this.useCustomMap = true;
/*  274 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <K, V> ConcurrentMap<K, V> makeMap() {
/*  287 */     return this.useCustomMap ? (ConcurrentMap)(new StrategyImpl(this)).map : new ConcurrentHashMap<K, V>(this.builder.getInitialCapacity(), 0.75F, this.builder.getConcurrencyLevel());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <K, V> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> computingFunction) {
/*  335 */     return (ConcurrentMap)(new StrategyImpl(this, computingFunction)).map;
/*      */   }
/*      */ 
/*      */   
/*      */   private enum Strength
/*      */   {
/*  341 */     WEAK {
/*      */       boolean equal(Object a, Object b) {
/*  343 */         return (a == b);
/*      */       }
/*      */       int hash(Object o) {
/*  346 */         return System.identityHashCode(o);
/*      */       }
/*      */       
/*      */       <K, V> MapMaker.ValueReference<K, V> referenceValue(MapMaker.ReferenceEntry<K, V> entry, V value) {
/*  350 */         return new MapMaker.WeakValueReference<K, V>(value, entry);
/*      */       }
/*      */ 
/*      */       
/*      */       <K, V> MapMaker.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals, K key, int hash, MapMaker.ReferenceEntry<K, V> next) {
/*  355 */         return (next == null) ? new MapMaker.WeakEntry<K, V>(internals, key, hash) : new MapMaker.LinkedWeakEntry<K, V>(internals, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> MapMaker.ReferenceEntry<K, V> copyEntry(K key, MapMaker.ReferenceEntry<K, V> original, MapMaker.ReferenceEntry<K, V> newNext) {
/*  362 */         MapMaker.WeakEntry<K, V> from = (MapMaker.WeakEntry<K, V>)original;
/*  363 */         return (newNext == null) ? new MapMaker.WeakEntry<K, V>(from.internals, key, from.hash) : new MapMaker.LinkedWeakEntry<K, V>(from.internals, key, from.hash, newNext);
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  370 */     SOFT {
/*      */       boolean equal(Object a, Object b) {
/*  372 */         return (a == b);
/*      */       }
/*      */       int hash(Object o) {
/*  375 */         return System.identityHashCode(o);
/*      */       }
/*      */       
/*      */       <K, V> MapMaker.ValueReference<K, V> referenceValue(MapMaker.ReferenceEntry<K, V> entry, V value) {
/*  379 */         return new MapMaker.SoftValueReference<K, V>(value, entry);
/*      */       }
/*      */ 
/*      */       
/*      */       <K, V> MapMaker.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals, K key, int hash, MapMaker.ReferenceEntry<K, V> next) {
/*  384 */         return (next == null) ? new MapMaker.SoftEntry<K, V>(internals, key, hash) : new MapMaker.LinkedSoftEntry<K, V>(internals, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> MapMaker.ReferenceEntry<K, V> copyEntry(K key, MapMaker.ReferenceEntry<K, V> original, MapMaker.ReferenceEntry<K, V> newNext) {
/*  391 */         MapMaker.SoftEntry<K, V> from = (MapMaker.SoftEntry<K, V>)original;
/*  392 */         return (newNext == null) ? new MapMaker.SoftEntry<K, V>(from.internals, key, from.hash) : new MapMaker.LinkedSoftEntry<K, V>(from.internals, key, from.hash, newNext);
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  399 */     STRONG {
/*      */       boolean equal(Object a, Object b) {
/*  401 */         return a.equals(b);
/*      */       }
/*      */       int hash(Object o) {
/*  404 */         return o.hashCode();
/*      */       }
/*      */       
/*      */       <K, V> MapMaker.ValueReference<K, V> referenceValue(MapMaker.ReferenceEntry<K, V> entry, V value) {
/*  408 */         return new MapMaker.StrongValueReference<K, V>(value);
/*      */       }
/*      */ 
/*      */       
/*      */       <K, V> MapMaker.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals, K key, int hash, MapMaker.ReferenceEntry<K, V> next) {
/*  413 */         return (next == null) ? new MapMaker.StrongEntry<K, V>(internals, key, hash) : new MapMaker.LinkedStrongEntry<K, V>(internals, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> MapMaker.ReferenceEntry<K, V> copyEntry(K key, MapMaker.ReferenceEntry<K, V> original, MapMaker.ReferenceEntry<K, V> newNext) {
/*  421 */         MapMaker.StrongEntry<K, V> from = (MapMaker.StrongEntry<K, V>)original;
/*  422 */         return (newNext == null) ? new MapMaker.StrongEntry<K, V>(from.internals, key, from.hash) : new MapMaker.LinkedStrongEntry<K, V>(from.internals, key, from.hash, newNext);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract boolean equal(Object param1Object1, Object param1Object2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int hash(Object param1Object);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract <K, V> MapMaker.ValueReference<K, V> referenceValue(MapMaker.ReferenceEntry<K, V> param1ReferenceEntry, V param1V);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract <K, V> MapMaker.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> param1Internals, K param1K, int param1Int, MapMaker.ReferenceEntry<K, V> param1ReferenceEntry);
/*      */ 
/*      */ 
/*      */     
/*      */     abstract <K, V> MapMaker.ReferenceEntry<K, V> copyEntry(K param1K, MapMaker.ReferenceEntry<K, V> param1ReferenceEntry1, MapMaker.ReferenceEntry<K, V> param1ReferenceEntry2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StrategyImpl<K, V>
/*      */     implements Serializable, CustomConcurrentHashMap.ComputingStrategy<K, V, ReferenceEntry<K, V>>
/*      */   {
/*      */     final MapMaker.Strength keyStrength;
/*      */ 
/*      */     
/*      */     final MapMaker.Strength valueStrength;
/*      */ 
/*      */     
/*      */     final ConcurrentMap<K, V> map;
/*      */ 
/*      */     
/*      */     final long expirationNanos;
/*      */ 
/*      */     
/*      */     CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */ 
/*      */     
/*      */     StrategyImpl(MapMaker maker) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokespecial <init> : ()V
/*      */       //   4: aload_0
/*      */       //   5: aload_1
/*      */       //   6: invokestatic access$100 : (Lcom/google/common/collect/MapMaker;)Lcom/google/common/collect/MapMaker$Strength;
/*      */       //   9: putfield keyStrength : Lcom/google/common/collect/MapMaker$Strength;
/*      */       //   12: aload_0
/*      */       //   13: aload_1
/*      */       //   14: invokestatic access$200 : (Lcom/google/common/collect/MapMaker;)Lcom/google/common/collect/MapMaker$Strength;
/*      */       //   17: putfield valueStrength : Lcom/google/common/collect/MapMaker$Strength;
/*      */       //   20: aload_0
/*      */       //   21: aload_1
/*      */       //   22: invokestatic access$300 : (Lcom/google/common/collect/MapMaker;)J
/*      */       //   25: putfield expirationNanos : J
/*      */       //   28: aload_0
/*      */       //   29: aload_1
/*      */       //   30: invokestatic access$400 : (Lcom/google/common/collect/MapMaker;)Lcom/google/common/collect/CustomConcurrentHashMap$Builder;
/*      */       //   33: aload_0
/*      */       //   34: invokevirtual buildMap : (Lcom/google/common/collect/CustomConcurrentHashMap$Strategy;)Ljava/util/concurrent/ConcurrentMap;
/*      */       //   37: putfield map : Ljava/util/concurrent/ConcurrentMap;
/*      */       //   40: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #470	-> 0
/*      */       //   #471	-> 4
/*      */       //   #472	-> 12
/*      */       //   #473	-> 20
/*      */       //   #475	-> 28
/*      */       //   #476	-> 40
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	41	0	this	Lcom/google/common/collect/MapMaker$StrategyImpl;
/*      */       //   0	41	1	maker	Lcom/google/common/collect/MapMaker;
/*      */       // Local variable type table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	41	0	this	Lcom/google/common/collect/MapMaker$StrategyImpl<TK;TV;>;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     StrategyImpl(MapMaker maker, Function<? super K, ? extends V> computer) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokespecial <init> : ()V
/*      */       //   4: aload_0
/*      */       //   5: aload_1
/*      */       //   6: invokestatic access$100 : (Lcom/google/common/collect/MapMaker;)Lcom/google/common/collect/MapMaker$Strength;
/*      */       //   9: putfield keyStrength : Lcom/google/common/collect/MapMaker$Strength;
/*      */       //   12: aload_0
/*      */       //   13: aload_1
/*      */       //   14: invokestatic access$200 : (Lcom/google/common/collect/MapMaker;)Lcom/google/common/collect/MapMaker$Strength;
/*      */       //   17: putfield valueStrength : Lcom/google/common/collect/MapMaker$Strength;
/*      */       //   20: aload_0
/*      */       //   21: aload_1
/*      */       //   22: invokestatic access$300 : (Lcom/google/common/collect/MapMaker;)J
/*      */       //   25: putfield expirationNanos : J
/*      */       //   28: aload_0
/*      */       //   29: aload_1
/*      */       //   30: invokestatic access$400 : (Lcom/google/common/collect/MapMaker;)Lcom/google/common/collect/CustomConcurrentHashMap$Builder;
/*      */       //   33: aload_0
/*      */       //   34: aload_2
/*      */       //   35: invokevirtual buildComputingMap : (Lcom/google/common/collect/CustomConcurrentHashMap$ComputingStrategy;Lcom/google/common/base/Function;)Ljava/util/concurrent/ConcurrentMap;
/*      */       //   38: putfield map : Ljava/util/concurrent/ConcurrentMap;
/*      */       //   41: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #479	-> 0
/*      */       //   #480	-> 4
/*      */       //   #481	-> 12
/*      */       //   #482	-> 20
/*      */       //   #484	-> 28
/*      */       //   #485	-> 41
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	42	0	this	Lcom/google/common/collect/MapMaker$StrategyImpl;
/*      */       //   0	42	1	maker	Lcom/google/common/collect/MapMaker;
/*      */       //   0	42	2	computer	Lcom/google/common/base/Function;
/*      */       // Local variable type table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	42	0	this	Lcom/google/common/collect/MapMaker$StrategyImpl<TK;TV;>;
/*      */       //   0	42	2	computer	Lcom/google/common/base/Function<-TK;+TV;>;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValue(MapMaker.ReferenceEntry<K, V> entry, V value) {
/*  488 */       setValueReference(entry, this.valueStrength.referenceValue(entry, value));
/*      */       
/*  490 */       if (this.expirationNanos > 0L) {
/*  491 */         scheduleRemoval(entry.getKey(), value);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void scheduleRemoval(K key, V value) {
/*  505 */       final WeakReference<K> keyReference = new WeakReference<K>(key);
/*  506 */       final WeakReference<V> valueReference = new WeakReference<V>(value);
/*  507 */       ExpirationTimer.instance.schedule(new TimerTask()
/*      */           {
/*      */             public void run() {
/*  510 */               K key = keyReference.get();
/*  511 */               if (key != null)
/*      */               {
/*  513 */                 MapMaker.StrategyImpl.this.map.remove(key, valueReference.get());
/*      */               }
/*      */             }
/*      */           }TimeUnit.NANOSECONDS.toMillis(this.expirationNanos));
/*      */     }
/*      */     
/*      */     public boolean equalKeys(K a, Object b) {
/*  520 */       return this.keyStrength.equal(a, b);
/*      */     }
/*      */     
/*      */     public boolean equalValues(V a, Object b) {
/*  524 */       return this.valueStrength.equal(a, b);
/*      */     }
/*      */     
/*      */     public int hashKey(Object key) {
/*  528 */       return this.keyStrength.hash(key);
/*      */     }
/*      */     
/*      */     public K getKey(MapMaker.ReferenceEntry<K, V> entry) {
/*  532 */       return entry.getKey();
/*      */     }
/*      */     
/*      */     public int getHash(MapMaker.ReferenceEntry<K, V> entry) {
/*  536 */       return entry.getHash();
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.ReferenceEntry<K, V> newEntry(K key, int hash, MapMaker.ReferenceEntry<K, V> next) {
/*  541 */       return this.keyStrength.newEntry(this.internals, key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.ReferenceEntry<K, V> copyEntry(K key, MapMaker.ReferenceEntry<K, V> original, MapMaker.ReferenceEntry<K, V> newNext) {
/*  546 */       MapMaker.ValueReference<K, V> valueReference = original.getValueReference();
/*  547 */       if (valueReference == MapMaker.COMPUTING) {
/*  548 */         MapMaker.ReferenceEntry<K, V> referenceEntry = newEntry(key, original.getHash(), newNext);
/*      */         
/*  550 */         referenceEntry.setValueReference(new FutureValueReference(original, referenceEntry));
/*      */         
/*  552 */         return referenceEntry;
/*      */       } 
/*  554 */       MapMaker.ReferenceEntry<K, V> newEntry = newEntry(key, original.getHash(), newNext);
/*      */       
/*  556 */       newEntry.setValueReference(valueReference.copyFor(newEntry));
/*  557 */       return newEntry;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V waitForValue(MapMaker.ReferenceEntry<K, V> entry) throws InterruptedException {
/*  567 */       MapMaker.ValueReference<K, V> valueReference = entry.getValueReference();
/*  568 */       if (valueReference == MapMaker.COMPUTING) {
/*  569 */         synchronized (entry) {
/*      */           
/*  571 */           while ((valueReference = entry.getValueReference()) == MapMaker.COMPUTING) {
/*  572 */             entry.wait();
/*      */           }
/*      */         } 
/*      */       }
/*  576 */       return valueReference.waitForValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V getValue(MapMaker.ReferenceEntry<K, V> entry) {
/*  584 */       MapMaker.ValueReference<K, V> valueReference = entry.getValueReference();
/*  585 */       return valueReference.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public V compute(K key, MapMaker.ReferenceEntry<K, V> entry, Function<? super K, ? extends V> computer) {
/*      */       V value;
/*      */       try {
/*  592 */         value = (V)computer.apply(key);
/*  593 */       } catch (ComputationException e) {
/*      */ 
/*      */         
/*  596 */         setValueReference(entry, new MapMaker.ComputationExceptionReference<K, V>(e.getCause()));
/*      */         
/*  598 */         throw e;
/*  599 */       } catch (Throwable t) {
/*  600 */         setValueReference(entry, new MapMaker.ComputationExceptionReference<K, V>(t));
/*      */         
/*  602 */         throw new ComputationException(t);
/*      */       } 
/*      */       
/*  605 */       if (value == null) {
/*  606 */         String message = computer + " returned null for key " + key + ".";
/*      */         
/*  608 */         setValueReference(entry, new MapMaker.NullOutputExceptionReference<K, V>(message));
/*      */         
/*  610 */         throw new NullOutputException(message);
/*      */       } 
/*  612 */       setValue(entry, value);
/*      */       
/*  614 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setValueReference(MapMaker.ReferenceEntry<K, V> entry, MapMaker.ValueReference<K, V> valueReference) {
/*  623 */       boolean notifyOthers = (entry.getValueReference() == MapMaker.COMPUTING);
/*  624 */       entry.setValueReference(valueReference);
/*  625 */       if (notifyOthers) {
/*  626 */         synchronized (entry) {
/*  627 */           entry.notifyAll();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private class FutureValueReference
/*      */       implements MapMaker.ValueReference<K, V>
/*      */     {
/*      */       final MapMaker.ReferenceEntry<K, V> original;
/*      */       
/*      */       final MapMaker.ReferenceEntry<K, V> newEntry;
/*      */ 
/*      */       
/*      */       FutureValueReference(MapMaker.ReferenceEntry<K, V> original, MapMaker.ReferenceEntry<K, V> newEntry) {
/*  643 */         this.original = original;
/*  644 */         this.newEntry = newEntry;
/*      */       }
/*      */       
/*      */       public V get() {
/*  648 */         boolean success = false;
/*      */         try {
/*  650 */           V value = (V)this.original.getValueReference().get();
/*  651 */           success = true;
/*  652 */           return value;
/*      */         } finally {
/*  654 */           if (!success) {
/*  655 */             removeEntry();
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/*      */       public MapMaker.ValueReference<K, V> copyFor(MapMaker.ReferenceEntry<K, V> entry) {
/*  661 */         return new FutureValueReference(this.original, entry);
/*      */       }
/*      */       
/*      */       public V waitForValue() throws InterruptedException {
/*  665 */         boolean success = false;
/*      */         
/*      */         try {
/*  668 */           V value = MapMaker.StrategyImpl.this.waitForValue(this.original);
/*  669 */           success = true;
/*  670 */           return value;
/*      */         } finally {
/*  672 */           if (!success) {
/*  673 */             removeEntry();
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       void removeEntry() {
/*  685 */         MapMaker.StrategyImpl.this.internals.removeEntry(this.newEntry);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.ReferenceEntry<K, V> getNext(MapMaker.ReferenceEntry<K, V> entry) {
/*  691 */       return entry.getNext();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setInternals(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals) {
/*  696 */       this.internals = internals;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream out) throws IOException {
/*  706 */       out.writeObject(this.keyStrength);
/*  707 */       out.writeObject(this.valueStrength);
/*  708 */       out.writeLong(this.expirationNanos);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  714 */       out.writeObject(this.internals);
/*  715 */       out.writeObject(this.map);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static class Fields
/*      */     {
/*  724 */       static final Field keyStrength = findField("keyStrength");
/*  725 */       static final Field valueStrength = findField("valueStrength");
/*  726 */       static final Field expirationNanos = findField("expirationNanos");
/*  727 */       static final Field internals = findField("internals");
/*  728 */       static final Field map = findField("map");
/*      */       
/*      */       static Field findField(String name) {
/*      */         try {
/*  732 */           Field f = MapMaker.StrategyImpl.class.getDeclaredField(name);
/*  733 */           f.setAccessible(true);
/*  734 */           return f;
/*  735 */         } catch (NoSuchFieldException e) {
/*  736 */           throw new AssertionError(e);
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*      */       try {
/*  744 */         Fields.keyStrength.set(this, in.readObject());
/*  745 */         Fields.valueStrength.set(this, in.readObject());
/*  746 */         Fields.expirationNanos.set(this, Long.valueOf(in.readLong()));
/*  747 */         Fields.internals.set(this, in.readObject());
/*  748 */         Fields.map.set(this, in.readObject());
/*  749 */       } catch (IllegalAccessException e) {
/*  750 */         throw new AssertionError(e);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  773 */   private static final ValueReference<Object, Object> COMPUTING = new ValueReference<Object, Object>()
/*      */     {
/*      */       public Object get() {
/*  776 */         return null;
/*      */       }
/*      */       
/*      */       public MapMaker.ValueReference<Object, Object> copyFor(MapMaker.ReferenceEntry<Object, Object> entry) {
/*  780 */         throw new AssertionError();
/*      */       }
/*      */       public Object waitForValue() {
/*  783 */         throw new AssertionError();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> ValueReference<K, V> computing() {
/*  793 */     return (ValueReference)COMPUTING;
/*      */   }
/*      */   
/*      */   private static class NullOutputExceptionReference<K, V>
/*      */     implements ValueReference<K, V> {
/*      */     final String message;
/*      */     
/*      */     NullOutputExceptionReference(String message) {
/*  801 */       this.message = message;
/*      */     }
/*      */     public V get() {
/*  804 */       return null;
/*      */     }
/*      */     
/*      */     public MapMaker.ValueReference<K, V> copyFor(MapMaker.ReferenceEntry<K, V> entry) {
/*  808 */       return this;
/*      */     }
/*      */     public V waitForValue() {
/*  811 */       throw new NullOutputException(this.message);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ComputationExceptionReference<K, V>
/*      */     implements ValueReference<K, V> {
/*      */     final Throwable t;
/*      */     
/*      */     ComputationExceptionReference(Throwable t) {
/*  820 */       this.t = t;
/*      */     }
/*      */     public V get() {
/*  823 */       return null;
/*      */     }
/*      */     
/*      */     public MapMaker.ValueReference<K, V> copyFor(MapMaker.ReferenceEntry<K, V> entry) {
/*  827 */       return this;
/*      */     }
/*      */     public V waitForValue() {
/*  830 */       throw new AsynchronousComputationException(this.t);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class QueueHolder
/*      */   {
/*  836 */     static final FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StrongEntry<K, V>
/*      */     implements ReferenceEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int hash;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile MapMaker.ValueReference<K, V> valueReference;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StrongEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals, K key, int hash) {
/*  895 */       this.valueReference = MapMaker.computing(); this.internals = internals;
/*      */       this.key = key;
/*      */       this.hash = hash;
/*  898 */     } public K getKey() { return this.key; } public MapMaker.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */ 
/*      */     
/*      */     public void setValueReference(MapMaker.ValueReference<K, V> valueReference) {
/*  902 */       this.valueReference = valueReference;
/*      */     }
/*      */     public void valueReclaimed() {
/*  905 */       this.internals.removeEntry(this, null);
/*      */     }
/*      */     public MapMaker.ReferenceEntry<K, V> getNext() {
/*  908 */       return null;
/*      */     }
/*      */     public int getHash() {
/*  911 */       return this.hash;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class LinkedStrongEntry<K, V> extends StrongEntry<K, V> {
/*      */     final MapMaker.ReferenceEntry<K, V> next;
/*      */     
/*      */     LinkedStrongEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals, K key, int hash, MapMaker.ReferenceEntry<K, V> next) {
/*  919 */       super(internals, key, hash);
/*  920 */       this.next = next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMaker.ReferenceEntry<K, V> getNext() {
/*  926 */       return this.next;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SoftEntry<K, V>
/*      */     extends FinalizableSoftReference<K> implements ReferenceEntry<K, V>
/*      */   {
/*      */     final CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals;
/*      */     final int hash;
/*      */     volatile MapMaker.ValueReference<K, V> valueReference;
/*      */     
/*  937 */     SoftEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals, K key, int hash) { super(key, MapMaker.QueueHolder.queue);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  954 */       this.valueReference = MapMaker.computing();
/*      */       this.internals = internals;
/*      */       this.hash = hash; }
/*  957 */     public K getKey() { return (K)get(); } public MapMaker.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */      public void finalizeReferent() {
/*      */       this.internals.removeEntry(this);
/*      */     } public void setValueReference(MapMaker.ValueReference<K, V> valueReference) {
/*  961 */       this.valueReference = valueReference;
/*      */     }
/*      */     public void valueReclaimed() {
/*  964 */       this.internals.removeEntry(this, null);
/*      */     }
/*      */     public MapMaker.ReferenceEntry<K, V> getNext() {
/*  967 */       return null;
/*      */     }
/*      */     public int getHash() {
/*  970 */       return this.hash;
/*      */     } }
/*      */   
/*      */   private static class LinkedSoftEntry<K, V> extends SoftEntry<K, V> {
/*      */     final MapMaker.ReferenceEntry<K, V> next;
/*      */     
/*      */     LinkedSoftEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals, K key, int hash, MapMaker.ReferenceEntry<K, V> next) {
/*  977 */       super(internals, key, hash);
/*  978 */       this.next = next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMaker.ReferenceEntry<K, V> getNext() {
/*  984 */       return this.next;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class WeakEntry<K, V>
/*      */     extends FinalizableWeakReference<K> implements ReferenceEntry<K, V>
/*      */   {
/*      */     final CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals;
/*      */     final int hash;
/*      */     volatile MapMaker.ValueReference<K, V> valueReference;
/*      */     
/*  995 */     WeakEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals, K key, int hash) { super(key, MapMaker.QueueHolder.queue);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1012 */       this.valueReference = MapMaker.computing(); this.internals = internals;
/*      */       this.hash = hash; } public K getKey() { return (K)get(); }
/*      */     public void finalizeReferent() { this.internals.removeEntry(this); }
/* 1015 */     public MapMaker.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */ 
/*      */     
/*      */     public void setValueReference(MapMaker.ValueReference<K, V> valueReference) {
/* 1019 */       this.valueReference = valueReference;
/*      */     }
/*      */     public void valueReclaimed() {
/* 1022 */       this.internals.removeEntry(this, null);
/*      */     }
/*      */     public MapMaker.ReferenceEntry<K, V> getNext() {
/* 1025 */       return null;
/*      */     }
/*      */     public int getHash() {
/* 1028 */       return this.hash;
/*      */     } }
/*      */   
/*      */   private static class LinkedWeakEntry<K, V> extends WeakEntry<K, V> {
/*      */     final MapMaker.ReferenceEntry<K, V> next;
/*      */     
/*      */     LinkedWeakEntry(CustomConcurrentHashMap.Internals<K, V, MapMaker.ReferenceEntry<K, V>> internals, K key, int hash, MapMaker.ReferenceEntry<K, V> next) {
/* 1035 */       super(internals, key, hash);
/* 1036 */       this.next = next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMaker.ReferenceEntry<K, V> getNext() {
/* 1042 */       return this.next;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class WeakValueReference<K, V>
/*      */     extends FinalizableWeakReference<V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     final MapMaker.ReferenceEntry<K, V> entry;
/*      */     
/*      */     WeakValueReference(V referent, MapMaker.ReferenceEntry<K, V> entry) {
/* 1053 */       super(referent, MapMaker.QueueHolder.queue);
/* 1054 */       this.entry = entry;
/*      */     }
/*      */     
/*      */     public void finalizeReferent() {
/* 1058 */       this.entry.valueReclaimed();
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.ValueReference<K, V> copyFor(MapMaker.ReferenceEntry<K, V> entry) {
/* 1063 */       return new WeakValueReference(get(), entry);
/*      */     }
/*      */     
/*      */     public V waitForValue() {
/* 1067 */       return get();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SoftValueReference<K, V>
/*      */     extends FinalizableSoftReference<V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     final MapMaker.ReferenceEntry<K, V> entry;
/*      */     
/*      */     SoftValueReference(V referent, MapMaker.ReferenceEntry<K, V> entry) {
/* 1078 */       super(referent, MapMaker.QueueHolder.queue);
/* 1079 */       this.entry = entry;
/*      */     }
/*      */     
/*      */     public void finalizeReferent() {
/* 1083 */       this.entry.valueReclaimed();
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.ValueReference<K, V> copyFor(MapMaker.ReferenceEntry<K, V> entry) {
/* 1088 */       return new SoftValueReference(get(), entry);
/*      */     }
/*      */     
/*      */     public V waitForValue() {
/* 1092 */       return get();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class StrongValueReference<K, V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     final V referent;
/*      */     
/*      */     StrongValueReference(V referent) {
/* 1102 */       this.referent = referent;
/*      */     }
/*      */     
/*      */     public V get() {
/* 1106 */       return this.referent;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.ValueReference<K, V> copyFor(MapMaker.ReferenceEntry<K, V> entry) {
/* 1111 */       return this;
/*      */     }
/*      */     
/*      */     public V waitForValue() {
/* 1115 */       return get();
/*      */     }
/*      */   }
/*      */   
/*      */   private static interface ReferenceEntry<K, V> {
/*      */     MapMaker.ValueReference<K, V> getValueReference();
/*      */     
/*      */     void setValueReference(MapMaker.ValueReference<K, V> param1ValueReference);
/*      */     
/*      */     void valueReclaimed();
/*      */     
/*      */     ReferenceEntry<K, V> getNext();
/*      */     
/*      */     int getHash();
/*      */     
/*      */     K getKey();
/*      */   }
/*      */   
/*      */   private static interface ValueReference<K, V> {
/*      */     V get();
/*      */     
/*      */     ValueReference<K, V> copyFor(MapMaker.ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     V waitForValue() throws InterruptedException;
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\MapMaker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */