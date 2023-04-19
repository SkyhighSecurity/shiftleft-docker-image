/*     */ package org.apache.commons.collections.map;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections.Factory;
/*     */ import org.apache.commons.collections.FunctorException;
/*     */ import org.apache.commons.collections.MultiMap;
/*     */ import org.apache.commons.collections.iterators.EmptyIterator;
/*     */ import org.apache.commons.collections.iterators.IteratorChain;
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
/*     */ public class MultiValueMap
/*     */   extends AbstractMapDecorator
/*     */   implements MultiMap
/*     */ {
/*     */   private final Factory collectionFactory;
/*     */   private transient Collection values;
/*     */   
/*     */   public static MultiValueMap decorate(Map map) {
/*  78 */     return new MultiValueMap(map, new ReflectionFactory(ArrayList.class));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultiValueMap decorate(Map map, Class collectionClass) {
/*  89 */     return new MultiValueMap(map, new ReflectionFactory(collectionClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultiValueMap decorate(Map map, Factory collectionFactory) {
/* 100 */     return new MultiValueMap(map, collectionFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiValueMap() {
/* 109 */     this(new HashMap(), new ReflectionFactory(ArrayList.class));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultiValueMap(Map map, Factory collectionFactory) {
/* 120 */     super(map);
/* 121 */     if (collectionFactory == null) {
/* 122 */       throw new IllegalArgumentException("The factory must not be null");
/*     */     }
/* 124 */     this.collectionFactory = collectionFactory;
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
/*     */   public void clear() {
/* 140 */     getMap().clear();
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
/*     */   public Object remove(Object key, Object value) {
/* 157 */     Collection valuesForKey = getCollection(key);
/* 158 */     if (valuesForKey == null) {
/* 159 */       return null;
/*     */     }
/* 161 */     boolean removed = valuesForKey.remove(value);
/* 162 */     if (!removed) {
/* 163 */       return null;
/*     */     }
/* 165 */     if (valuesForKey.isEmpty()) {
/* 166 */       remove(key);
/*     */     }
/* 168 */     return value;
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
/*     */   public boolean containsValue(Object value) {
/* 180 */     Set pairs = getMap().entrySet();
/* 181 */     if (pairs == null) {
/* 182 */       return false;
/*     */     }
/* 184 */     Iterator pairsIterator = pairs.iterator();
/* 185 */     while (pairsIterator.hasNext()) {
/* 186 */       Map.Entry keyValuePair = pairsIterator.next();
/* 187 */       Collection coll = (Collection)keyValuePair.getValue();
/* 188 */       if (coll.contains(value)) {
/* 189 */         return true;
/*     */       }
/*     */     } 
/* 192 */     return false;
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
/*     */   public Object put(Object key, Object value) {
/* 206 */     boolean result = false;
/* 207 */     Collection coll = getCollection(key);
/* 208 */     if (coll == null) {
/* 209 */       coll = createCollection(1);
/* 210 */       result = coll.add(value);
/* 211 */       if (coll.size() > 0) {
/*     */         
/* 213 */         getMap().put(key, coll);
/* 214 */         result = true;
/*     */       } 
/*     */     } else {
/* 217 */       result = coll.add(value);
/*     */     } 
/* 219 */     return result ? value : null;
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
/*     */   public void putAll(Map map) {
/* 234 */     if (map instanceof MultiMap) {
/* 235 */       for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
/* 236 */         Map.Entry entry = it.next();
/* 237 */         Collection coll = (Collection)entry.getValue();
/* 238 */         putAll(entry.getKey(), coll);
/*     */       } 
/*     */     } else {
/* 241 */       for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
/* 242 */         Map.Entry entry = it.next();
/* 243 */         put(entry.getKey(), entry.getValue());
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
/*     */   
/*     */   public Collection values() {
/* 256 */     Collection vs = this.values;
/* 257 */     return (vs != null) ? vs : (this.values = new Values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object key, Object value) {
/* 267 */     Collection coll = getCollection(key);
/* 268 */     if (coll == null) {
/* 269 */       return false;
/*     */     }
/* 271 */     return coll.contains(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection getCollection(Object key) {
/* 282 */     return (Collection)getMap().get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size(Object key) {
/* 292 */     Collection coll = getCollection(key);
/* 293 */     if (coll == null) {
/* 294 */       return 0;
/*     */     }
/* 296 */     return coll.size();
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
/*     */   public boolean putAll(Object key, Collection values) {
/* 308 */     if (values == null || values.size() == 0) {
/* 309 */       return false;
/*     */     }
/* 311 */     boolean result = false;
/* 312 */     Collection coll = getCollection(key);
/* 313 */     if (coll == null) {
/* 314 */       coll = createCollection(values.size());
/* 315 */       coll.addAll(values);
/* 316 */       if (coll.size() > 0) {
/*     */         
/* 318 */         getMap().put(key, coll);
/* 319 */         result = true;
/*     */       } 
/*     */     } else {
/* 322 */       result = coll.addAll(values);
/*     */     } 
/* 324 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterator(Object key) {
/* 334 */     if (!containsKey(key)) {
/* 335 */       return EmptyIterator.INSTANCE;
/*     */     }
/* 337 */     return new ValuesIterator(this, key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int totalSize() {
/* 347 */     int total = 0;
/* 348 */     Collection values = getMap().values();
/* 349 */     for (Iterator it = values.iterator(); it.hasNext(); ) {
/* 350 */       Collection coll = it.next();
/* 351 */       total += coll.size();
/*     */     } 
/* 353 */     return total;
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
/*     */   protected Collection createCollection(int size) {
/* 367 */     return (Collection)this.collectionFactory.create();
/*     */   }
/*     */   
/*     */   private class Values extends AbstractCollection {
/*     */     private final MultiValueMap this$0;
/*     */     
/*     */     private Values(MultiValueMap this$0) {
/* 374 */       MultiValueMap.this = MultiValueMap.this;
/*     */     } public Iterator iterator() {
/* 376 */       IteratorChain chain = new IteratorChain();
/* 377 */       for (Iterator it = MultiValueMap.this.keySet().iterator(); it.hasNext();) {
/* 378 */         chain.addIterator(new MultiValueMap.ValuesIterator(MultiValueMap.this, it.next()));
/*     */       }
/* 380 */       return (Iterator)chain;
/*     */     }
/*     */     
/*     */     public int size() {
/* 384 */       return MultiValueMap.this.totalSize();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 388 */       MultiValueMap.this.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ValuesIterator
/*     */     implements Iterator {
/*     */     private final Object key;
/*     */     private final Collection values;
/*     */     private final Iterator iterator;
/*     */     private final MultiValueMap this$0;
/*     */     
/*     */     public ValuesIterator(MultiValueMap this$0, Object key) {
/* 400 */       this.this$0 = this$0;
/* 401 */       this.key = key;
/* 402 */       this.values = this$0.getCollection(key);
/* 403 */       this.iterator = this.values.iterator();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 407 */       this.iterator.remove();
/* 408 */       if (this.values.isEmpty()) {
/* 409 */         this.this$0.remove(this.key);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 414 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     public Object next() {
/* 418 */       return this.iterator.next();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ReflectionFactory
/*     */     implements Factory
/*     */   {
/*     */     private final Class clazz;
/*     */     
/*     */     public ReflectionFactory(Class clazz) {
/* 429 */       this.clazz = clazz;
/*     */     }
/*     */     
/*     */     public Object create() {
/*     */       try {
/* 434 */         return this.clazz.newInstance();
/* 435 */       } catch (Exception ex) {
/* 436 */         throw new FunctorException("Cannot instantiate class: " + this.clazz, ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\map\MultiValueMap.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */