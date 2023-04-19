/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class TreeMultimap<K, V>
/*     */   extends AbstractSortedSetMultimap<K, V>
/*     */ {
/*     */   private transient Comparator<? super K> keyComparator;
/*     */   private transient Comparator<? super V> valueComparator;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
/*  82 */     return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural());
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
/*     */   public static <K, V> TreeMultimap<K, V> create(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
/*  96 */     return new TreeMultimap<K, V>((Comparator<? super K>)Preconditions.checkNotNull(keyComparator), (Comparator<? super V>)Preconditions.checkNotNull(valueComparator));
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
/*     */   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 109 */     return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural(), multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   TreeMultimap() {
/* 115 */     this(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TreeMultimap(@Nullable Comparator<? super K> keyComparator, @Nullable Comparator<? super V> valueComparator) {
/* 123 */     super((keyComparator == null) ? new TreeMap<K, Collection<V>>() : new TreeMap<K, Collection<V>>(keyComparator));
/*     */ 
/*     */     
/* 126 */     this.keyComparator = keyComparator;
/* 127 */     this.valueComparator = valueComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator, Multimap<? extends K, ? extends V> multimap) {
/* 133 */     this(keyComparator, valueComparator);
/* 134 */     putAll(multimap);
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
/*     */   SortedSet<V> createCollection() {
/* 146 */     return (this.valueComparator == null) ? new TreeSet<V>() : new TreeSet<V>(this.valueComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super K> keyComparator() {
/* 154 */     return this.keyComparator;
/*     */   }
/*     */   
/*     */   public Comparator<? super V> valueComparator() {
/* 158 */     return this.valueComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<K> keySet() {
/* 169 */     return (SortedSet<K>)super.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMap<K, Collection<V>> asMap() {
/* 180 */     return (SortedMap<K, Collection<V>>)super.asMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 189 */     stream.defaultWriteObject();
/* 190 */     stream.writeObject(keyComparator());
/* 191 */     stream.writeObject(valueComparator());
/* 192 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 198 */     stream.defaultReadObject();
/* 199 */     this.keyComparator = (Comparator<? super K>)stream.readObject();
/* 200 */     this.valueComparator = (Comparator<? super V>)stream.readObject();
/* 201 */     setMap(new TreeMap<K, Collection<V>>(this.keyComparator));
/* 202 */     Serialization.populateMultimap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\TreeMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */