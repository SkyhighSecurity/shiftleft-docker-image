/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ public abstract class CollectionUtils
/*     */ {
/*     */   public static boolean isEmpty(Collection<?> collection) {
/*  51 */     return (collection == null || collection.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(Map<?, ?> map) {
/*  61 */     return (map == null || map.isEmpty());
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
/*     */   public static List arrayToList(Object source) {
/*  78 */     return Arrays.asList(ObjectUtils.toObjectArray(source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection) {
/*  88 */     if (collection == null) {
/*  89 */       throw new IllegalArgumentException("Collection must not be null");
/*     */     }
/*  91 */     Object[] arr = ObjectUtils.toObjectArray(array);
/*  92 */     for (Object elem : arr) {
/*  93 */       collection.add((E)elem);
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
/*     */   public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
/* 107 */     if (map == null) {
/* 108 */       throw new IllegalArgumentException("Map must not be null");
/*     */     }
/* 110 */     if (props != null) {
/* 111 */       for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
/* 112 */         String key = (String)en.nextElement();
/* 113 */         Object value = props.get(key);
/* 114 */         if (value == null)
/*     */         {
/* 116 */           value = props.getProperty(key);
/*     */         }
/* 118 */         map.put((K)key, (V)value);
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
/*     */   public static boolean contains(Iterator<?> iterator, Object element) {
/* 131 */     if (iterator != null) {
/* 132 */       while (iterator.hasNext()) {
/* 133 */         Object candidate = iterator.next();
/* 134 */         if (ObjectUtils.nullSafeEquals(candidate, element)) {
/* 135 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(Enumeration<?> enumeration, Object element) {
/* 149 */     if (enumeration != null) {
/* 150 */       while (enumeration.hasMoreElements()) {
/* 151 */         Object candidate = enumeration.nextElement();
/* 152 */         if (ObjectUtils.nullSafeEquals(candidate, element)) {
/* 153 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 157 */     return false;
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
/*     */   public static boolean containsInstance(Collection<?> collection, Object element) {
/* 169 */     if (collection != null) {
/* 170 */       for (Object candidate : collection) {
/* 171 */         if (candidate == element) {
/* 172 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
/* 187 */     if (isEmpty(source) || isEmpty(candidates)) {
/* 188 */       return false;
/*     */     }
/* 190 */     for (Object candidate : candidates) {
/* 191 */       if (source.contains(candidate)) {
/* 192 */         return true;
/*     */       }
/*     */     } 
/* 195 */     return false;
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
/*     */   public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
/* 209 */     if (isEmpty(source) || isEmpty(candidates)) {
/* 210 */       return null;
/*     */     }
/* 212 */     for (E candidate : candidates) {
/* 213 */       if (source.contains(candidate)) {
/* 214 */         return candidate;
/*     */       }
/*     */     } 
/* 217 */     return null;
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
/*     */   public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
/* 229 */     if (isEmpty(collection)) {
/* 230 */       return null;
/*     */     }
/* 232 */     T value = null;
/* 233 */     for (Object element : collection) {
/* 234 */       if (type == null || type.isInstance(element)) {
/* 235 */         if (value != null)
/*     */         {
/* 237 */           return null;
/*     */         }
/* 239 */         value = (T)element;
/*     */       } 
/*     */     } 
/* 242 */     return value;
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
/*     */   public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
/* 255 */     if (isEmpty(collection) || ObjectUtils.isEmpty((Object[])types)) {
/* 256 */       return null;
/*     */     }
/* 258 */     for (Class<?> type : types) {
/* 259 */       Object value = findValueOfType(collection, type);
/* 260 */       if (value != null) {
/* 261 */         return value;
/*     */       }
/*     */     } 
/* 264 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasUniqueObject(Collection<?> collection) {
/* 274 */     if (isEmpty(collection)) {
/* 275 */       return false;
/*     */     }
/* 277 */     boolean hasCandidate = false;
/* 278 */     Object candidate = null;
/* 279 */     for (Object elem : collection) {
/* 280 */       if (!hasCandidate) {
/* 281 */         hasCandidate = true;
/* 282 */         candidate = elem; continue;
/*     */       } 
/* 284 */       if (candidate != elem) {
/* 285 */         return false;
/*     */       }
/*     */     } 
/* 288 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> findCommonElementType(Collection<?> collection) {
/* 298 */     if (isEmpty(collection)) {
/* 299 */       return null;
/*     */     }
/* 301 */     Class<?> candidate = null;
/* 302 */     for (Object val : collection) {
/* 303 */       if (val != null) {
/* 304 */         if (candidate == null) {
/* 305 */           candidate = val.getClass(); continue;
/*     */         } 
/* 307 */         if (candidate != val.getClass()) {
/* 308 */           return null;
/*     */         }
/*     */       } 
/*     */     } 
/* 312 */     return candidate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
/* 321 */     ArrayList<A> elements = new ArrayList<A>();
/* 322 */     while (enumeration.hasMoreElements()) {
/* 323 */       elements.add((A)enumeration.nextElement());
/*     */     }
/* 325 */     return elements.toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
/* 334 */     return new EnumerationIterator<E>(enumeration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> map) {
/* 344 */     return new MultiValueMapAdapter<K, V>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap(MultiValueMap<? extends K, ? extends V> map) {
/* 355 */     Assert.notNull(map, "'map' must not be null");
/* 356 */     Map<K, List<V>> result = new LinkedHashMap<K, List<V>>(map.size());
/* 357 */     for (Map.Entry<? extends K, ? extends List<? extends V>> entry : map.entrySet()) {
/* 358 */       List<? extends V> values = Collections.unmodifiableList(entry.getValue());
/* 359 */       result.put(entry.getKey(), values);
/*     */     } 
/* 361 */     Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap(result);
/* 362 */     return toMultiValueMap(unmodifiableMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EnumerationIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private final Enumeration<E> enumeration;
/*     */ 
/*     */     
/*     */     public EnumerationIterator(Enumeration<E> enumeration) {
/* 374 */       this.enumeration = enumeration;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 379 */       return this.enumeration.hasMoreElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 384 */       return this.enumeration.nextElement();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() throws UnsupportedOperationException {
/* 389 */       throw new UnsupportedOperationException("Not supported");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MultiValueMapAdapter<K, V>
/*     */     implements MultiValueMap<K, V>, Serializable
/*     */   {
/*     */     private final Map<K, List<V>> map;
/*     */ 
/*     */ 
/*     */     
/*     */     public MultiValueMapAdapter(Map<K, List<V>> map) {
/* 403 */       Assert.notNull(map, "'map' must not be null");
/* 404 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(K key, V value) {
/* 409 */       List<V> values = this.map.get(key);
/* 410 */       if (values == null) {
/* 411 */         values = new LinkedList<V>();
/* 412 */         this.map.put(key, values);
/*     */       } 
/* 414 */       values.add(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V getFirst(K key) {
/* 419 */       List<V> values = this.map.get(key);
/* 420 */       return (values != null) ? values.get(0) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(K key, V value) {
/* 425 */       List<V> values = new LinkedList<V>();
/* 426 */       values.add(value);
/* 427 */       this.map.put(key, values);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setAll(Map<K, V> values) {
/* 432 */       for (Map.Entry<K, V> entry : values.entrySet()) {
/* 433 */         set(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<K, V> toSingleValueMap() {
/* 439 */       LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K, V>(this.map.size());
/* 440 */       for (Map.Entry<K, List<V>> entry : this.map.entrySet()) {
/* 441 */         singleValueMap.put(entry.getKey(), ((List<V>)entry.getValue()).get(0));
/*     */       }
/* 443 */       return singleValueMap;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 448 */       return this.map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 453 */       return this.map.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 458 */       return this.map.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsValue(Object value) {
/* 463 */       return this.map.containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> get(Object key) {
/* 468 */       return this.map.get(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> put(K key, List<V> value) {
/* 473 */       return this.map.put(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> remove(Object key) {
/* 478 */       return this.map.remove(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Map<? extends K, ? extends List<V>> map) {
/* 483 */       this.map.putAll(map);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 488 */       this.map.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> keySet() {
/* 493 */       return this.map.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<List<V>> values() {
/* 498 */       return this.map.values();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<K, List<V>>> entrySet() {
/* 503 */       return this.map.entrySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 508 */       if (this == other) {
/* 509 */         return true;
/*     */       }
/* 511 */       return this.map.equals(other);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 516 */       return this.map.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 521 */       return this.map.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\CollectionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */