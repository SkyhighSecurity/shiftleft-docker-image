/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.Collection;
/*    */ import java.util.EnumMap;
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
/*    */ @GwtCompatible
/*    */ public final class EnumMultiset<E extends Enum<E>>
/*    */   extends AbstractMapBasedMultiset<E>
/*    */ {
/*    */   private transient Class<E> type;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> type) {
/* 39 */     return new EnumMultiset<E>(type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements) {
/* 50 */     Iterator<E> iterator = elements.iterator();
/* 51 */     Preconditions.checkArgument(iterator.hasNext(), "EnumMultiset constructor passed empty Iterable");
/*    */     
/* 53 */     EnumMultiset<E> multiset = new EnumMultiset<E>(((Enum<E>)iterator.next()).getDeclaringClass());
/*    */     
/* 55 */     Iterables.addAll(multiset, elements);
/* 56 */     return multiset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private EnumMultiset(Class<E> type) {
/* 63 */     super(new EnumMap<E, AtomicInteger>(type));
/* 64 */     this.type = type;
/*    */   }
/*    */   
/*    */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 68 */     stream.defaultWriteObject();
/* 69 */     stream.writeObject(this.type);
/* 70 */     Serialization.writeMultiset(this, stream);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 80 */     stream.defaultReadObject();
/*    */     
/* 82 */     Class<E> localType = (Class<E>)stream.readObject();
/* 83 */     this.type = localType;
/* 84 */     setBackingMap(new EnumMap<E, AtomicInteger>(this.type));
/* 85 */     Serialization.populateMultiset(this, stream);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\EnumMultiset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */