/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoPopulatingList<E>
/*     */   implements List<E>, Serializable
/*     */ {
/*     */   private final List<E> backingList;
/*     */   private final ElementFactory<E> elementFactory;
/*     */   
/*     */   public AutoPopulatingList(Class<? extends E> elementClass) {
/*  63 */     this(new ArrayList<E>(), elementClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoPopulatingList(List<E> backingList, Class<? extends E> elementClass) {
/*  72 */     this(backingList, new ReflectiveElementFactory<E>(elementClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoPopulatingList(ElementFactory<E> elementFactory) {
/*  80 */     this(new ArrayList<E>(), elementFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoPopulatingList(List<E> backingList, ElementFactory<E> elementFactory) {
/*  88 */     Assert.notNull(backingList, "Backing List must not be null");
/*  89 */     Assert.notNull(elementFactory, "Element factory must not be null");
/*  90 */     this.backingList = backingList;
/*  91 */     this.elementFactory = elementFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, E element) {
/*  97 */     this.backingList.add(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E o) {
/* 102 */     return this.backingList.add(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> c) {
/* 107 */     return this.backingList.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> c) {
/* 112 */     return this.backingList.addAll(index, c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 117 */     this.backingList.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 122 */     return this.backingList.contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/* 127 */     return this.backingList.containsAll(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/* 136 */     int backingListSize = this.backingList.size();
/* 137 */     E element = null;
/* 138 */     if (index < backingListSize) {
/* 139 */       element = this.backingList.get(index);
/* 140 */       if (element == null) {
/* 141 */         element = this.elementFactory.createElement(index);
/* 142 */         this.backingList.set(index, element);
/*     */       } 
/*     */     } else {
/*     */       
/* 146 */       for (int x = backingListSize; x < index; x++) {
/* 147 */         this.backingList.add(null);
/*     */       }
/* 149 */       element = this.elementFactory.createElement(index);
/* 150 */       this.backingList.add(element);
/*     */     } 
/* 152 */     return element;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/* 157 */     return this.backingList.indexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 162 */     return this.backingList.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 167 */     return this.backingList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 172 */     return this.backingList.lastIndexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 177 */     return this.backingList.listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 182 */     return this.backingList.listIterator(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E remove(int index) {
/* 187 */     return this.backingList.remove(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 192 */     return this.backingList.remove(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 197 */     return this.backingList.removeAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 202 */     return this.backingList.retainAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public E set(int index, E element) {
/* 207 */     return this.backingList.set(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 212 */     return this.backingList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 217 */     return this.backingList.subList(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 222 */     return this.backingList.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 227 */     return this.backingList.toArray(a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 233 */     return this.backingList.equals(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 238 */     return this.backingList.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ElementFactory<E>
/*     */   {
/*     */     E createElement(int param1Int) throws AutoPopulatingList.ElementInstantiationException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ElementInstantiationException
/*     */     extends RuntimeException
/*     */   {
/*     */     public ElementInstantiationException(String msg) {
/* 264 */       super(msg);
/*     */     }
/*     */     
/*     */     public ElementInstantiationException(String message, Throwable cause) {
/* 268 */       super(message, cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ReflectiveElementFactory<E>
/*     */     implements ElementFactory<E>, Serializable
/*     */   {
/*     */     private final Class<? extends E> elementClass;
/*     */ 
/*     */ 
/*     */     
/*     */     public ReflectiveElementFactory(Class<? extends E> elementClass) {
/* 282 */       Assert.notNull(elementClass, "Element class must not be null");
/* 283 */       Assert.isTrue(!elementClass.isInterface(), "Element class must not be an interface type");
/* 284 */       Assert.isTrue(!Modifier.isAbstract(elementClass.getModifiers()), "Element class cannot be an abstract class");
/* 285 */       this.elementClass = elementClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public E createElement(int index) {
/*     */       try {
/* 291 */         return this.elementClass.newInstance();
/*     */       }
/* 293 */       catch (InstantiationException ex) {
/* 294 */         throw new AutoPopulatingList.ElementInstantiationException("Unable to instantiate element class: " + this.elementClass
/* 295 */             .getName(), ex);
/*     */       }
/* 297 */       catch (IllegalAccessException ex) {
/* 298 */         throw new AutoPopulatingList.ElementInstantiationException("Could not access element constructor: " + this.elementClass
/* 299 */             .getName(), ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\AutoPopulatingList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */