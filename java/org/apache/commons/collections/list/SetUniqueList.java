/*     */ package org.apache.commons.collections.list;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections.iterators.AbstractIteratorDecorator;
/*     */ import org.apache.commons.collections.iterators.AbstractListIteratorDecorator;
/*     */ import org.apache.commons.collections.set.UnmodifiableSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SetUniqueList
/*     */   extends AbstractSerializableListDecorator
/*     */ {
/*     */   private static final long serialVersionUID = 7196982186153478694L;
/*     */   protected final Set set;
/*     */   
/*     */   public static SetUniqueList decorate(List list) {
/*  74 */     if (list == null) {
/*  75 */       throw new IllegalArgumentException("List must not be null");
/*     */     }
/*  77 */     if (list.isEmpty()) {
/*  78 */       return new SetUniqueList(list, new HashSet());
/*     */     }
/*  80 */     List temp = new ArrayList(list);
/*  81 */     list.clear();
/*  82 */     SetUniqueList sl = new SetUniqueList(list, new HashSet());
/*  83 */     sl.addAll(temp);
/*  84 */     return sl;
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
/*     */   protected SetUniqueList(List list, Set set) {
/*  99 */     super(list);
/* 100 */     if (set == null) {
/* 101 */       throw new IllegalArgumentException("Set must not be null");
/*     */     }
/* 103 */     this.set = set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set asSet() {
/* 113 */     return UnmodifiableSet.decorate(this.set);
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
/*     */   public boolean add(Object object) {
/* 130 */     int sizeBefore = size();
/*     */ 
/*     */     
/* 133 */     add(size(), object);
/*     */ 
/*     */     
/* 136 */     return (sizeBefore != size());
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
/*     */   public void add(int index, Object object) {
/* 151 */     if (!this.set.contains(object)) {
/* 152 */       super.add(index, object);
/* 153 */       this.set.add(object);
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
/*     */   public boolean addAll(Collection coll) {
/* 167 */     return addAll(size(), coll);
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
/*     */   public boolean addAll(int index, Collection coll) {
/* 187 */     int sizeBefore = size();
/*     */ 
/*     */     
/* 190 */     for (Iterator it = coll.iterator(); it.hasNext(); ) {
/* 191 */       int sizeBeforeAddNext = size();
/* 192 */       add(index, it.next());
/*     */       
/* 194 */       if (sizeBeforeAddNext != size()) {
/* 195 */         index++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 200 */     return (sizeBefore != size());
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
/*     */   public Object set(int index, Object object) {
/* 217 */     int pos = indexOf(object);
/* 218 */     Object removed = super.set(index, object);
/*     */     
/* 220 */     if (pos != -1 && pos != index)
/*     */     {
/*     */       
/* 223 */       super.remove(pos);
/*     */     }
/*     */     
/* 226 */     this.set.remove(removed);
/* 227 */     this.set.add(object);
/*     */     
/* 229 */     return removed;
/*     */   }
/*     */   
/*     */   public boolean remove(Object object) {
/* 233 */     boolean result = super.remove(object);
/* 234 */     this.set.remove(object);
/* 235 */     return result;
/*     */   }
/*     */   
/*     */   public Object remove(int index) {
/* 239 */     Object result = super.remove(index);
/* 240 */     this.set.remove(result);
/* 241 */     return result;
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection coll) {
/* 245 */     boolean result = super.removeAll(coll);
/* 246 */     this.set.removeAll(coll);
/* 247 */     return result;
/*     */   }
/*     */   
/*     */   public boolean retainAll(Collection coll) {
/* 251 */     boolean result = super.retainAll(coll);
/* 252 */     this.set.retainAll(coll);
/* 253 */     return result;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 257 */     super.clear();
/* 258 */     this.set.clear();
/*     */   }
/*     */   
/*     */   public boolean contains(Object object) {
/* 262 */     return this.set.contains(object);
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection coll) {
/* 266 */     return this.set.containsAll(coll);
/*     */   }
/*     */   
/*     */   public Iterator iterator() {
/* 270 */     return (Iterator)new SetListIterator(super.iterator(), this.set);
/*     */   }
/*     */   
/*     */   public ListIterator listIterator() {
/* 274 */     return (ListIterator)new SetListListIterator(super.listIterator(), this.set);
/*     */   }
/*     */   
/*     */   public ListIterator listIterator(int index) {
/* 278 */     return (ListIterator)new SetListListIterator(super.listIterator(index), this.set);
/*     */   }
/*     */   
/*     */   public List subList(int fromIndex, int toIndex) {
/* 282 */     List superSubList = super.subList(fromIndex, toIndex);
/* 283 */     Set subSet = createSetBasedOnList(this.set, superSubList);
/* 284 */     return new SetUniqueList(superSubList, subSet);
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
/*     */   protected Set createSetBasedOnList(Set set, List list) {
/* 297 */     Set subSet = null;
/* 298 */     if (set.getClass().equals(HashSet.class)) {
/* 299 */       subSet = new HashSet();
/*     */     } else {
/*     */       try {
/* 302 */         subSet = (Set)set.getClass().newInstance();
/* 303 */       } catch (InstantiationException ie) {
/* 304 */         subSet = new HashSet();
/* 305 */       } catch (IllegalAccessException iae) {
/* 306 */         subSet = new HashSet();
/*     */       } 
/*     */     } 
/* 309 */     subSet.addAll(list);
/* 310 */     return subSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class SetListIterator
/*     */     extends AbstractIteratorDecorator
/*     */   {
/*     */     protected final Set set;
/*     */     
/* 320 */     protected Object last = null;
/*     */     
/*     */     protected SetListIterator(Iterator it, Set set) {
/* 323 */       super(it);
/* 324 */       this.set = set;
/*     */     }
/*     */     
/*     */     public Object next() {
/* 328 */       this.last = super.next();
/* 329 */       return this.last;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 333 */       super.remove();
/* 334 */       this.set.remove(this.last);
/* 335 */       this.last = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class SetListListIterator
/*     */     extends AbstractListIteratorDecorator
/*     */   {
/*     */     protected final Set set;
/*     */     
/* 345 */     protected Object last = null;
/*     */     
/*     */     protected SetListListIterator(ListIterator it, Set set) {
/* 348 */       super(it);
/* 349 */       this.set = set;
/*     */     }
/*     */     
/*     */     public Object next() {
/* 353 */       this.last = super.next();
/* 354 */       return this.last;
/*     */     }
/*     */     
/*     */     public Object previous() {
/* 358 */       this.last = super.previous();
/* 359 */       return this.last;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 363 */       super.remove();
/* 364 */       this.set.remove(this.last);
/* 365 */       this.last = null;
/*     */     }
/*     */     
/*     */     public void add(Object object) {
/* 369 */       if (!this.set.contains(object)) {
/* 370 */         super.add(object);
/* 371 */         this.set.add(object);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void set(Object object) {
/* 376 */       throw new UnsupportedOperationException("ListIterator does not support set");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\list\SetUniqueList.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */