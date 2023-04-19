/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ public final class HashMultiset<E>
/*    */   extends AbstractMapBasedMultiset<E>
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public static <E> HashMultiset<E> create() {
/* 41 */     return new HashMultiset<E>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> HashMultiset<E> create(int distinctElements) {
/* 52 */     return new HashMultiset<E>(distinctElements);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> HashMultiset<E> create(Iterable<? extends E> elements) {
/* 61 */     HashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/*    */     
/* 63 */     Iterables.addAll(multiset, elements);
/* 64 */     return multiset;
/*    */   }
/*    */   
/*    */   private HashMultiset() {
/* 68 */     super(new HashMap<E, AtomicInteger>());
/*    */   }
/*    */   
/*    */   private HashMultiset(int distinctElements) {
/* 72 */     super(new HashMap<E, AtomicInteger>(Maps.capacity(distinctElements)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 80 */     stream.defaultWriteObject();
/* 81 */     Serialization.writeMultiset(this, stream);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 86 */     stream.defaultReadObject();
/* 87 */     int distinctElements = Serialization.readCount(stream);
/* 88 */     setBackingMap(Maps.newHashMapWithExpectedSize(distinctElements));
/*    */     
/* 90 */     Serialization.populateMultiset(this, stream, distinctElements);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\HashMultiset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */