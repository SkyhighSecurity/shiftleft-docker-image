/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ public final class LinkedHashMultiset<E>
/*    */   extends AbstractMapBasedMultiset<E>
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public static <E> LinkedHashMultiset<E> create() {
/* 47 */     return new LinkedHashMultiset<E>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> LinkedHashMultiset<E> create(int distinctElements) {
/* 58 */     return new LinkedHashMultiset<E>(distinctElements);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> LinkedHashMultiset<E> create(Iterable<? extends E> elements) {
/* 68 */     LinkedHashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/*    */     
/* 70 */     Iterables.addAll(multiset, elements);
/* 71 */     return multiset;
/*    */   }
/*    */   
/*    */   private LinkedHashMultiset() {
/* 75 */     super(new LinkedHashMap<E, AtomicInteger>());
/*    */   }
/*    */ 
/*    */   
/*    */   private LinkedHashMultiset(int distinctElements) {
/* 80 */     super(new LinkedHashMap<E, AtomicInteger>(Maps.capacity(distinctElements)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 88 */     stream.defaultWriteObject();
/* 89 */     Serialization.writeMultiset(this, stream);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 94 */     stream.defaultReadObject();
/* 95 */     int distinctElements = Serialization.readCount(stream);
/* 96 */     setBackingMap(new LinkedHashMap<E, AtomicInteger>(Maps.capacity(distinctElements)));
/*    */     
/* 98 */     Serialization.populateMultiset(this, stream, distinctElements);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\LinkedHashMultiset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */