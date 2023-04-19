/*     */ package org.apache.commons.collections.map;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections.KeyValue;
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
/*     */ public final class StaticBucketMap
/*     */   implements Map
/*     */ {
/*     */   private static final int DEFAULT_BUCKETS = 255;
/*     */   private Node[] buckets;
/*     */   private Lock[] locks;
/*     */   
/*     */   public StaticBucketMap() {
/* 117 */     this(255);
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
/*     */   public StaticBucketMap(int numBuckets) {
/* 131 */     int size = Math.max(17, numBuckets);
/*     */ 
/*     */     
/* 134 */     if (size % 2 == 0) {
/* 135 */       size--;
/*     */     }
/*     */     
/* 138 */     this.buckets = new Node[size];
/* 139 */     this.locks = new Lock[size];
/*     */     
/* 141 */     for (int i = 0; i < size; i++) {
/* 142 */       this.locks[i] = new Lock();
/*     */     }
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
/*     */   private final int getHash(Object key) {
/* 161 */     if (key == null) {
/* 162 */       return 0;
/*     */     }
/* 164 */     int hash = key.hashCode();
/* 165 */     hash += hash << 15 ^ 0xFFFFFFFF;
/* 166 */     hash ^= hash >>> 10;
/* 167 */     hash += hash << 3;
/* 168 */     hash ^= hash >>> 6;
/* 169 */     hash += hash << 11 ^ 0xFFFFFFFF;
/* 170 */     hash ^= hash >>> 16;
/* 171 */     hash %= this.buckets.length;
/* 172 */     return (hash < 0) ? (hash * -1) : hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 182 */     int cnt = 0;
/*     */     
/* 184 */     for (int i = 0; i < this.buckets.length; i++) {
/* 185 */       synchronized (this.locks[i]) {
/* 186 */         cnt += (this.locks[i]).size;
/*     */       } 
/*     */     } 
/* 189 */     return cnt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 198 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(Object key) {
/* 208 */     int hash = getHash(key);
/*     */     
/* 210 */     synchronized (this.locks[hash]) {
/* 211 */       Node n = this.buckets[hash];
/*     */       
/* 213 */       while (n != null) {
/* 214 */         if (n.key == key || (n.key != null && n.key.equals(key))) {
/* 215 */           return n.value;
/*     */         }
/*     */         
/* 218 */         n = n.next;
/*     */       } 
/*     */     } 
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 231 */     int hash = getHash(key);
/*     */     
/* 233 */     synchronized (this.locks[hash]) {
/* 234 */       Node n = this.buckets[hash];
/*     */       
/* 236 */       while (n != null) {
/* 237 */         if (n.key == key || (n.key != null && n.key.equals(key))) {
/* 238 */           return true;
/*     */         }
/*     */         
/* 241 */         n = n.next;
/*     */       } 
/*     */     } 
/* 244 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 254 */     for (int i = 0; i < this.buckets.length; i++) {
/* 255 */       synchronized (this.locks[i]) {
/* 256 */         Node n = this.buckets[i];
/*     */         
/* 258 */         while (n != null) {
/* 259 */           if (n.value == value || (n.value != null && n.value.equals(value))) {
/* 260 */             return true;
/*     */           }
/*     */           
/* 263 */           n = n.next;
/*     */         } 
/*     */       } 
/*     */     } 
/* 267 */     return false;
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
/*     */   public Object put(Object key, Object value) {
/* 279 */     int hash = getHash(key);
/*     */     
/* 281 */     synchronized (this.locks[hash]) {
/* 282 */       Node n = this.buckets[hash];
/*     */       
/* 284 */       if (n == null) {
/* 285 */         n = new Node();
/* 286 */         n.key = key;
/* 287 */         n.value = value;
/* 288 */         this.buckets[hash] = n;
/* 289 */         (this.locks[hash]).size++;
/* 290 */         return null;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 296 */       for (Node next = n; next != null; next = next.next) {
/* 297 */         n = next;
/*     */         
/* 299 */         if (n.key == key || (n.key != null && n.key.equals(key))) {
/* 300 */           Object returnVal = n.value;
/* 301 */           n.value = value;
/* 302 */           return returnVal;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 308 */       Node newNode = new Node();
/* 309 */       newNode.key = key;
/* 310 */       newNode.value = value;
/* 311 */       n.next = newNode;
/* 312 */       (this.locks[hash]).size++;
/*     */     } 
/* 314 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object remove(Object key) {
/* 324 */     int hash = getHash(key);
/*     */     
/* 326 */     synchronized (this.locks[hash]) {
/* 327 */       Node n = this.buckets[hash];
/* 328 */       Node prev = null;
/*     */       
/* 330 */       while (n != null) {
/* 331 */         if (n.key == key || (n.key != null && n.key.equals(key))) {
/*     */           
/* 333 */           if (null == prev) {
/*     */             
/* 335 */             this.buckets[hash] = n.next;
/*     */           } else {
/*     */             
/* 338 */             prev.next = n.next;
/*     */           } 
/* 340 */           (this.locks[hash]).size--;
/* 341 */           return n.value;
/*     */         } 
/*     */         
/* 344 */         prev = n;
/* 345 */         n = n.next;
/*     */       } 
/*     */     } 
/* 348 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set keySet() {
/* 358 */     return new KeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection values() {
/* 367 */     return new Values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set entrySet() {
/* 376 */     return new EntrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map map) {
/* 387 */     Iterator i = map.keySet().iterator();
/*     */     
/* 389 */     while (i.hasNext()) {
/* 390 */       Object key = i.next();
/* 391 */       put(key, map.get(key));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 399 */     for (int i = 0; i < this.buckets.length; i++) {
/* 400 */       Lock lock = this.locks[i];
/* 401 */       synchronized (lock) {
/* 402 */         this.buckets[i] = null;
/* 403 */         lock.size = 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 415 */     if (obj == this) {
/* 416 */       return true;
/*     */     }
/* 418 */     if (!(obj instanceof Map)) {
/* 419 */       return false;
/*     */     }
/* 421 */     Map other = (Map)obj;
/* 422 */     return entrySet().equals(other.entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 431 */     int hashCode = 0;
/*     */     
/* 433 */     for (int i = 0; i < this.buckets.length; i++) {
/* 434 */       synchronized (this.locks[i]) {
/* 435 */         Node n = this.buckets[i];
/*     */         
/* 437 */         while (n != null) {
/* 438 */           hashCode += n.hashCode();
/* 439 */           n = n.next;
/*     */         } 
/*     */       } 
/*     */     } 
/* 443 */     return hashCode;
/*     */   }
/*     */   
/*     */   private static final class Node
/*     */     implements Map.Entry, KeyValue
/*     */   {
/*     */     protected Object key;
/*     */     protected Object value;
/*     */     protected Node next;
/*     */     
/*     */     private Node() {}
/*     */     
/*     */     public Object getKey() {
/* 456 */       return this.key;
/*     */     }
/*     */     
/*     */     public Object getValue() {
/* 460 */       return this.value;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 464 */       return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 469 */       if (obj == this) {
/* 470 */         return true;
/*     */       }
/* 472 */       if (!(obj instanceof Map.Entry)) {
/* 473 */         return false;
/*     */       }
/*     */       
/* 476 */       Map.Entry e2 = (Map.Entry)obj;
/* 477 */       return (((this.key == null) ? (e2.getKey() == null) : this.key.equals(e2.getKey())) && ((this.value == null) ? (e2.getValue() == null) : this.value.equals(e2.getValue())));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object setValue(Object obj) {
/* 483 */       Object retVal = this.value;
/* 484 */       this.value = obj;
/* 485 */       return retVal;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Lock {
/*     */     public int size;
/*     */     
/*     */     private Lock() {}
/*     */   }
/*     */   
/*     */   private class EntryIterator implements Iterator { private ArrayList current;
/*     */     private int bucket;
/*     */     
/*     */     private EntryIterator(StaticBucketMap this$0) {
/* 499 */       StaticBucketMap.this = StaticBucketMap.this;
/*     */       
/* 501 */       this.current = new ArrayList();
/*     */     }
/*     */     private Map.Entry last;
/*     */     private final StaticBucketMap this$0;
/*     */     
/*     */     public boolean hasNext() {
/* 507 */       if (this.current.size() > 0) return true; 
/* 508 */       while (this.bucket < StaticBucketMap.this.buckets.length) {
/* 509 */         synchronized (StaticBucketMap.this.locks[this.bucket]) {
/* 510 */           StaticBucketMap.Node n = StaticBucketMap.this.buckets[this.bucket];
/* 511 */           while (n != null) {
/* 512 */             this.current.add(n);
/* 513 */             n = n.next;
/*     */           } 
/* 515 */           this.bucket++;
/* 516 */           if (this.current.size() > 0) return true; 
/*     */         } 
/*     */       } 
/* 519 */       return false;
/*     */     }
/*     */     
/*     */     protected Map.Entry nextEntry() {
/* 523 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 524 */       this.last = this.current.remove(this.current.size() - 1);
/* 525 */       return this.last;
/*     */     }
/*     */     
/*     */     public Object next() {
/* 529 */       return nextEntry();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 533 */       if (this.last == null) throw new IllegalStateException(); 
/* 534 */       StaticBucketMap.this.remove(this.last.getKey());
/* 535 */       this.last = null;
/*     */     } }
/*     */   
/*     */   private class ValueIterator extends EntryIterator {
/*     */     private ValueIterator(StaticBucketMap this$0) {
/* 540 */       StaticBucketMap.this = StaticBucketMap.this;
/*     */     } private final StaticBucketMap this$0;
/*     */     public Object next() {
/* 543 */       return nextEntry().getValue();
/*     */     } }
/*     */   private class KeyIterator extends EntryIterator { private final StaticBucketMap this$0;
/*     */     
/*     */     private KeyIterator(StaticBucketMap this$0) {
/* 548 */       StaticBucketMap.this = StaticBucketMap.this;
/*     */     }
/*     */     public Object next() {
/* 551 */       return nextEntry().getKey();
/*     */     } }
/*     */   private class EntrySet extends AbstractSet { private final StaticBucketMap this$0;
/*     */     
/*     */     private EntrySet(StaticBucketMap this$0) {
/* 556 */       StaticBucketMap.this = StaticBucketMap.this;
/*     */     }
/*     */     public int size() {
/* 559 */       return StaticBucketMap.this.size();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 563 */       StaticBucketMap.this.clear();
/*     */     }
/*     */     
/*     */     public Iterator iterator() {
/* 567 */       return new StaticBucketMap.EntryIterator();
/*     */     }
/*     */     
/*     */     public boolean contains(Object obj) {
/* 571 */       Map.Entry entry = (Map.Entry)obj;
/* 572 */       int hash = StaticBucketMap.this.getHash(entry.getKey());
/* 573 */       synchronized (StaticBucketMap.this.locks[hash]) {
/* 574 */         for (StaticBucketMap.Node n = StaticBucketMap.this.buckets[hash]; n != null; n = n.next) {
/* 575 */           if (n.equals(entry)) return true; 
/*     */         } 
/*     */       } 
/* 578 */       return false;
/*     */     }
/*     */     
/*     */     public boolean remove(Object obj) {
/* 582 */       if (!(obj instanceof Map.Entry)) {
/* 583 */         return false;
/*     */       }
/* 585 */       Map.Entry entry = (Map.Entry)obj;
/* 586 */       int hash = StaticBucketMap.this.getHash(entry.getKey());
/* 587 */       synchronized (StaticBucketMap.this.locks[hash]) {
/* 588 */         for (StaticBucketMap.Node n = StaticBucketMap.this.buckets[hash]; n != null; n = n.next) {
/* 589 */           if (n.equals(entry)) {
/* 590 */             StaticBucketMap.this.remove(n.getKey());
/* 591 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 595 */       return false;
/*     */     } }
/*     */   
/*     */   private class KeySet extends AbstractSet { private final StaticBucketMap this$0;
/*     */     
/*     */     private KeySet(StaticBucketMap this$0) {
/* 601 */       StaticBucketMap.this = StaticBucketMap.this;
/*     */     }
/*     */     public int size() {
/* 604 */       return StaticBucketMap.this.size();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 608 */       StaticBucketMap.this.clear();
/*     */     }
/*     */     
/*     */     public Iterator iterator() {
/* 612 */       return new StaticBucketMap.KeyIterator();
/*     */     }
/*     */     
/*     */     public boolean contains(Object obj) {
/* 616 */       return StaticBucketMap.this.containsKey(obj);
/*     */     }
/*     */     
/*     */     public boolean remove(Object obj) {
/* 620 */       int hash = StaticBucketMap.this.getHash(obj);
/* 621 */       synchronized (StaticBucketMap.this.locks[hash]) {
/* 622 */         for (StaticBucketMap.Node n = StaticBucketMap.this.buckets[hash]; n != null; n = n.next) {
/* 623 */           Object k = n.getKey();
/* 624 */           if (k == obj || (k != null && k.equals(obj))) {
/* 625 */             StaticBucketMap.this.remove(k);
/* 626 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 630 */       return false;
/*     */     } }
/*     */   
/*     */   private class Values extends AbstractCollection {
/*     */     private final StaticBucketMap this$0;
/*     */     
/*     */     private Values(StaticBucketMap this$0) {
/* 637 */       StaticBucketMap.this = StaticBucketMap.this;
/*     */     }
/*     */     public int size() {
/* 640 */       return StaticBucketMap.this.size();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 644 */       StaticBucketMap.this.clear();
/*     */     }
/*     */     
/*     */     public Iterator iterator() {
/* 648 */       return new StaticBucketMap.ValueIterator();
/*     */     }
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
/*     */   public void atomic(Runnable r) {
/* 689 */     if (r == null) throw new NullPointerException(); 
/* 690 */     atomic(r, 0);
/*     */   }
/*     */   
/*     */   private void atomic(Runnable r, int bucket) {
/* 694 */     if (bucket >= this.buckets.length) {
/* 695 */       r.run();
/*     */       return;
/*     */     } 
/* 698 */     synchronized (this.locks[bucket]) {
/* 699 */       atomic(r, bucket + 1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\map\StaticBucketMap.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */