/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public class ImmutableMultiset<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   private final transient ImmutableMap<E, Integer> map;
/*     */   private final transient int size;
/*     */   private transient ImmutableSet<Multiset.Entry<E>> entrySet;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of() {
/*  53 */     return EmptyImmutableMultiset.INSTANCE;
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
/*     */   public static <E> ImmutableMultiset<E> of(E... elements) {
/*  66 */     return copyOf(Arrays.asList(elements));
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
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
/*  91 */     if (elements instanceof ImmutableMultiset) {
/*     */       
/*  93 */       ImmutableMultiset<E> result = (ImmutableMultiset)elements;
/*  94 */       return result;
/*     */     } 
/*     */ 
/*     */     
/*  98 */     Multiset<? extends E> multiset = (elements instanceof Multiset) ? (Multiset<? extends E>)elements : LinkedHashMultiset.<E>create(elements);
/*     */ 
/*     */ 
/*     */     
/* 102 */     return copyOfInternal(multiset);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> multiset) {
/* 107 */     long size = 0L;
/* 108 */     ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
/*     */     
/* 110 */     for (Multiset.Entry<? extends E> entry : multiset.entrySet()) {
/* 111 */       int count = entry.getCount();
/* 112 */       if (count > 0) {
/*     */ 
/*     */         
/* 115 */         builder.put(entry.getElement(), Integer.valueOf(count));
/* 116 */         size += count;
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     if (size == 0L) {
/* 121 */       return of();
/*     */     }
/* 123 */     return new ImmutableMultiset<E>(builder.build(), (int)Math.min(size, 2147483647L));
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
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
/* 139 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 140 */     Iterators.addAll(multiset, elements);
/* 141 */     return copyOfInternal(multiset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FieldSettersHolder
/*     */   {
/* 153 */     static final Serialization.FieldSetter<ImmutableMultiset> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultiset.class, "map");
/*     */     
/* 155 */     static final Serialization.FieldSetter<ImmutableMultiset> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultiset.class, "size");
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableMultiset(ImmutableMap<E, Integer> map, int size) {
/* 160 */     this.map = map;
/* 161 */     this.size = size;
/*     */   }
/*     */   
/*     */   public int count(@Nullable Object element) {
/* 165 */     Integer value = this.map.get(element);
/* 166 */     return (value == null) ? 0 : value.intValue();
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 170 */     final Iterator<Map.Entry<E, Integer>> mapIterator = this.map.entrySet().iterator();
/*     */ 
/*     */     
/* 173 */     return new UnmodifiableIterator<E>() {
/*     */         int remaining;
/*     */         E element;
/*     */         
/*     */         public boolean hasNext() {
/* 178 */           return (this.remaining > 0 || mapIterator.hasNext());
/*     */         }
/*     */         
/*     */         public E next() {
/* 182 */           if (this.remaining <= 0) {
/* 183 */             Map.Entry<E, Integer> entry = mapIterator.next();
/* 184 */             this.element = entry.getKey();
/* 185 */             this.remaining = ((Integer)entry.getValue()).intValue();
/*     */           } 
/* 187 */           this.remaining--;
/* 188 */           return this.element;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public int size() {
/* 194 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean contains(@Nullable Object element) {
/* 198 */     return this.map.containsKey(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int add(E element, int occurrences) {
/* 207 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int remove(Object element, int occurrences) {
/* 216 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int setCount(E element, int count) {
/* 225 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setCount(E element, int oldCount, int newCount) {
/* 234 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 238 */     if (object == this) {
/* 239 */       return true;
/*     */     }
/* 241 */     if (object instanceof Multiset) {
/* 242 */       Multiset<?> that = (Multiset)object;
/* 243 */       if (size() != that.size()) {
/* 244 */         return false;
/*     */       }
/* 246 */       for (Multiset.Entry<?> entry : that.entrySet()) {
/* 247 */         if (count(entry.getElement()) != entry.getCount()) {
/* 248 */           return false;
/*     */         }
/*     */       } 
/* 251 */       return true;
/*     */     } 
/* 253 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 258 */     return this.map.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 262 */     return entrySet().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> elementSet() {
/* 270 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 276 */     ImmutableSet<Multiset.Entry<E>> es = this.entrySet;
/* 277 */     return (es == null) ? (this.entrySet = new EntrySet<E>(this)) : es;
/*     */   }
/*     */   
/*     */   private static class EntrySet<E> extends ImmutableSet<Multiset.Entry<E>> {
/*     */     final ImmutableMultiset<E> multiset;
/*     */     
/*     */     public EntrySet(ImmutableMultiset<E> multiset) {
/* 284 */       this.multiset = multiset;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     public UnmodifiableIterator<Multiset.Entry<E>> iterator() {
/* 288 */       final Iterator<Map.Entry<E, Integer>> mapIterator = this.multiset.map.entrySet().iterator();
/*     */       
/* 290 */       return (UnmodifiableIterator)new UnmodifiableIterator<Multiset.Entry<Multiset.Entry<E>>>() {
/*     */           public boolean hasNext() {
/* 292 */             return mapIterator.hasNext();
/*     */           }
/*     */           public Multiset.Entry<E> next() {
/* 295 */             Map.Entry<E, Integer> mapEntry = mapIterator.next();
/* 296 */             return Multisets.immutableEntry(mapEntry.getKey(), ((Integer)mapEntry.getValue()).intValue());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 303 */       return this.multiset.map.size();
/*     */     }
/*     */     
/*     */     public boolean contains(Object o) {
/* 307 */       if (o instanceof Multiset.Entry) {
/* 308 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 309 */         if (entry.getCount() <= 0) {
/* 310 */           return false;
/*     */         }
/* 312 */         int count = this.multiset.count(entry.getElement());
/* 313 */         return (count == entry.getCount());
/*     */       } 
/* 315 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 319 */       return this.multiset.map.hashCode();
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 323 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 334 */     stream.defaultWriteObject();
/* 335 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 340 */     stream.defaultReadObject();
/* 341 */     int entryCount = stream.readInt();
/* 342 */     ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
/* 343 */     long tmpSize = 0L;
/* 344 */     for (int i = 0; i < entryCount; i++) {
/*     */       
/* 346 */       E element = (E)stream.readObject();
/* 347 */       int count = stream.readInt();
/* 348 */       if (count <= 0) {
/* 349 */         throw new InvalidObjectException("Invalid count " + count);
/*     */       }
/* 351 */       builder.put(element, Integer.valueOf(count));
/* 352 */       tmpSize += count;
/*     */     } 
/*     */     
/* 355 */     FieldSettersHolder.MAP_FIELD_SETTER.set(this, builder.build());
/* 356 */     FieldSettersHolder.SIZE_FIELD_SETTER.set(this, (int)Math.min(tmpSize, 2147483647L));
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 361 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 371 */     return new Builder<E>();
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
/*     */   public static final class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/* 393 */     private final Multiset<E> contents = LinkedHashMultiset.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E element) {
/* 409 */       this.contents.add((E)Preconditions.checkNotNull(element));
/* 410 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addCopies(E element, int occurrences) {
/* 427 */       this.contents.add((E)Preconditions.checkNotNull(element), occurrences);
/* 428 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> setCount(E element, int count) {
/* 442 */       this.contents.setCount((E)Preconditions.checkNotNull(element), count);
/* 443 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E... elements) {
/* 455 */       super.add(elements);
/* 456 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 469 */       if (elements instanceof Multiset) {
/*     */         
/* 471 */         Multiset<? extends E> multiset = (Multiset<? extends E>)elements;
/* 472 */         for (Multiset.Entry<? extends E> entry : multiset.entrySet()) {
/* 473 */           addCopies(entry.getElement(), entry.getCount());
/*     */         }
/*     */       } else {
/* 476 */         super.addAll(elements);
/*     */       } 
/* 478 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 490 */       super.addAll(elements);
/* 491 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMultiset<E> build() {
/* 499 */       return ImmutableMultiset.copyOf(this.contents);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableMultiset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */