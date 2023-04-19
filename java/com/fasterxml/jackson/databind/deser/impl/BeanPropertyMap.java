/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanPropertyMap
/*     */   implements Iterable<SettableBeanProperty>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*     */   protected final boolean _caseInsensitive;
/*     */   private int _hashMask;
/*     */   private int _size;
/*     */   private int _spillCount;
/*     */   private Object[] _hashArea;
/*     */   private final SettableBeanProperty[] _propsInOrder;
/*     */   private final Map<String, List<PropertyName>> _aliasDefs;
/*     */   private final Map<String, String> _aliasMapping;
/*     */   
/*     */   public BeanPropertyMap(boolean caseInsensitive, Collection<SettableBeanProperty> props, Map<String, List<PropertyName>> aliasDefs) {
/*  85 */     this._caseInsensitive = caseInsensitive;
/*  86 */     this._propsInOrder = props.<SettableBeanProperty>toArray(new SettableBeanProperty[props.size()]);
/*  87 */     this._aliasDefs = aliasDefs;
/*  88 */     this._aliasMapping = _buildAliasMapping(aliasDefs);
/*  89 */     init(props);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanPropertyMap(BeanPropertyMap src, SettableBeanProperty newProp, int hashIndex, int orderedIndex) {
/* 100 */     this._caseInsensitive = src._caseInsensitive;
/* 101 */     this._hashMask = src._hashMask;
/* 102 */     this._size = src._size;
/* 103 */     this._spillCount = src._spillCount;
/* 104 */     this._aliasDefs = src._aliasDefs;
/* 105 */     this._aliasMapping = src._aliasMapping;
/*     */ 
/*     */     
/* 108 */     this._hashArea = Arrays.copyOf(src._hashArea, src._hashArea.length);
/* 109 */     this._propsInOrder = Arrays.<SettableBeanProperty>copyOf(src._propsInOrder, src._propsInOrder.length);
/* 110 */     this._hashArea[hashIndex] = newProp;
/* 111 */     this._propsInOrder[orderedIndex] = newProp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanPropertyMap(BeanPropertyMap src, SettableBeanProperty newProp, String key, int slot) {
/* 122 */     this._caseInsensitive = src._caseInsensitive;
/* 123 */     this._hashMask = src._hashMask;
/* 124 */     this._size = src._size;
/* 125 */     this._spillCount = src._spillCount;
/* 126 */     this._aliasDefs = src._aliasDefs;
/* 127 */     this._aliasMapping = src._aliasMapping;
/*     */ 
/*     */     
/* 130 */     this._hashArea = Arrays.copyOf(src._hashArea, src._hashArea.length);
/* 131 */     int last = src._propsInOrder.length;
/*     */     
/* 133 */     this._propsInOrder = Arrays.<SettableBeanProperty>copyOf(src._propsInOrder, last + 1);
/* 134 */     this._propsInOrder[last] = newProp;
/*     */     
/* 136 */     int hashSize = this._hashMask + 1;
/* 137 */     int ix = slot << 1;
/*     */ 
/*     */     
/* 140 */     if (this._hashArea[ix] != null) {
/*     */       
/* 142 */       ix = hashSize + (slot >> 1) << 1;
/* 143 */       if (this._hashArea[ix] != null) {
/*     */         
/* 145 */         ix = (hashSize + (hashSize >> 1) << 1) + this._spillCount;
/* 146 */         this._spillCount += 2;
/* 147 */         if (ix >= this._hashArea.length) {
/* 148 */           this._hashArea = Arrays.copyOf(this._hashArea, this._hashArea.length + 4);
/*     */         }
/*     */       } 
/*     */     } 
/* 152 */     this._hashArea[ix] = key;
/* 153 */     this._hashArea[ix + 1] = newProp;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BeanPropertyMap(boolean caseInsensitive, Collection<SettableBeanProperty> props) {
/* 159 */     this(caseInsensitive, props, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanPropertyMap(BeanPropertyMap base, boolean caseInsensitive) {
/* 167 */     this._caseInsensitive = caseInsensitive;
/* 168 */     this._aliasDefs = base._aliasDefs;
/* 169 */     this._aliasMapping = base._aliasMapping;
/*     */ 
/*     */     
/* 172 */     this._propsInOrder = Arrays.<SettableBeanProperty>copyOf(base._propsInOrder, base._propsInOrder.length);
/* 173 */     init(Arrays.asList(this._propsInOrder));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanPropertyMap withCaseInsensitivity(boolean state) {
/* 184 */     if (this._caseInsensitive == state) {
/* 185 */       return this;
/*     */     }
/* 187 */     return new BeanPropertyMap(this, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init(Collection<SettableBeanProperty> props) {
/* 192 */     this._size = props.size();
/*     */ 
/*     */     
/* 195 */     int hashSize = findSize(this._size);
/* 196 */     this._hashMask = hashSize - 1;
/*     */ 
/*     */     
/* 199 */     int alloc = (hashSize + (hashSize >> 1)) * 2;
/* 200 */     Object[] hashed = new Object[alloc];
/* 201 */     int spillCount = 0;
/*     */     
/* 203 */     for (SettableBeanProperty prop : props) {
/*     */       
/* 205 */       if (prop == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 209 */       String key = getPropertyName(prop);
/* 210 */       int slot = _hashCode(key);
/* 211 */       int ix = slot << 1;
/*     */ 
/*     */       
/* 214 */       if (hashed[ix] != null) {
/*     */         
/* 216 */         ix = hashSize + (slot >> 1) << 1;
/* 217 */         if (hashed[ix] != null) {
/*     */           
/* 219 */           ix = (hashSize + (hashSize >> 1) << 1) + spillCount;
/* 220 */           spillCount += 2;
/* 221 */           if (ix >= hashed.length) {
/* 222 */             hashed = Arrays.copyOf(hashed, hashed.length + 4);
/*     */           }
/*     */         } 
/*     */       } 
/* 226 */       hashed[ix] = key;
/* 227 */       hashed[ix + 1] = prop;
/*     */     } 
/*     */ 
/*     */     
/* 231 */     this._hashArea = hashed;
/* 232 */     this._spillCount = spillCount;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int findSize(int size) {
/* 237 */     if (size <= 5) {
/* 238 */       return 8;
/*     */     }
/* 240 */     if (size <= 12) {
/* 241 */       return 16;
/*     */     }
/* 243 */     int needed = size + (size >> 2);
/* 244 */     int result = 32;
/* 245 */     while (result < needed) {
/* 246 */       result += result;
/*     */     }
/* 248 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanPropertyMap construct(Collection<SettableBeanProperty> props, boolean caseInsensitive, Map<String, List<PropertyName>> aliasMapping) {
/* 256 */     return new BeanPropertyMap(caseInsensitive, props, aliasMapping);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static BeanPropertyMap construct(Collection<SettableBeanProperty> props, boolean caseInsensitive) {
/* 261 */     return construct(props, caseInsensitive, 
/* 262 */         Collections.emptyMap());
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
/*     */   public BeanPropertyMap withProperty(SettableBeanProperty newProp) {
/* 275 */     String key = getPropertyName(newProp);
/*     */     
/* 277 */     for (int i = 1, end = this._hashArea.length; i < end; i += 2) {
/* 278 */       SettableBeanProperty prop = (SettableBeanProperty)this._hashArea[i];
/* 279 */       if (prop != null && prop.getName().equals(key)) {
/* 280 */         return new BeanPropertyMap(this, newProp, i, _findFromOrdered(prop));
/*     */       }
/*     */     } 
/*     */     
/* 284 */     int slot = _hashCode(key);
/*     */     
/* 286 */     return new BeanPropertyMap(this, newProp, key, slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanPropertyMap assignIndexes() {
/* 292 */     int index = 0;
/* 293 */     for (int i = 1, end = this._hashArea.length; i < end; i += 2) {
/* 294 */       SettableBeanProperty prop = (SettableBeanProperty)this._hashArea[i];
/* 295 */       if (prop != null) {
/* 296 */         prop.assignIndex(index++);
/*     */       }
/*     */     } 
/* 299 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanPropertyMap renameAll(NameTransformer transformer) {
/* 308 */     if (transformer == null || transformer == NameTransformer.NOP) {
/* 309 */       return this;
/*     */     }
/*     */     
/* 312 */     int len = this._propsInOrder.length;
/* 313 */     ArrayList<SettableBeanProperty> newProps = new ArrayList<>(len);
/*     */     
/* 315 */     for (int i = 0; i < len; i++) {
/* 316 */       SettableBeanProperty prop = this._propsInOrder[i];
/*     */ 
/*     */       
/* 319 */       if (prop == null) {
/* 320 */         newProps.add(prop);
/*     */       } else {
/*     */         
/* 323 */         newProps.add(_rename(prop, transformer));
/*     */       } 
/*     */     } 
/*     */     
/* 327 */     return new BeanPropertyMap(this._caseInsensitive, newProps, this._aliasDefs);
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
/*     */   public BeanPropertyMap withoutProperties(Collection<String> toExclude) {
/* 345 */     if (toExclude.isEmpty()) {
/* 346 */       return this;
/*     */     }
/* 348 */     int len = this._propsInOrder.length;
/* 349 */     ArrayList<SettableBeanProperty> newProps = new ArrayList<>(len);
/*     */     
/* 351 */     for (int i = 0; i < len; i++) {
/* 352 */       SettableBeanProperty prop = this._propsInOrder[i];
/*     */ 
/*     */ 
/*     */       
/* 356 */       if (prop != null && 
/* 357 */         !toExclude.contains(prop.getName())) {
/* 358 */         newProps.add(prop);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 363 */     return new BeanPropertyMap(this._caseInsensitive, newProps, this._aliasDefs);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void replace(SettableBeanProperty newProp) {
/* 369 */     String key = getPropertyName(newProp);
/* 370 */     int ix = _findIndexInHash(key);
/* 371 */     if (ix < 0) {
/* 372 */       throw new NoSuchElementException("No entry '" + key + "' found, can't replace");
/*     */     }
/* 374 */     SettableBeanProperty prop = (SettableBeanProperty)this._hashArea[ix];
/* 375 */     this._hashArea[ix] = newProp;
/*     */     
/* 377 */     this._propsInOrder[_findFromOrdered(prop)] = newProp;
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
/*     */   public void replace(SettableBeanProperty origProp, SettableBeanProperty newProp) {
/* 389 */     int i = 1;
/* 390 */     int end = this._hashArea.length;
/*     */     
/* 392 */     for (;; i += 2) {
/* 393 */       if (i > end) {
/* 394 */         throw new NoSuchElementException("No entry '" + origProp.getName() + "' found, can't replace");
/*     */       }
/* 396 */       if (this._hashArea[i] == origProp) {
/* 397 */         this._hashArea[i] = newProp;
/*     */         break;
/*     */       } 
/*     */     } 
/* 401 */     this._propsInOrder[_findFromOrdered(origProp)] = newProp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(SettableBeanProperty propToRm) {
/* 410 */     ArrayList<SettableBeanProperty> props = new ArrayList<>(this._size);
/* 411 */     String key = getPropertyName(propToRm);
/* 412 */     boolean found = false;
/*     */     
/* 414 */     for (int i = 1, end = this._hashArea.length; i < end; i += 2) {
/* 415 */       SettableBeanProperty prop = (SettableBeanProperty)this._hashArea[i];
/* 416 */       if (prop == null) {
/*     */         continue;
/*     */       }
/* 419 */       if (!found) {
/*     */ 
/*     */         
/* 422 */         found = key.equals(this._hashArea[i - 1]);
/* 423 */         if (found) {
/*     */           
/* 425 */           this._propsInOrder[_findFromOrdered(prop)] = null;
/*     */           continue;
/*     */         } 
/*     */       } 
/* 429 */       props.add(prop); continue;
/*     */     } 
/* 431 */     if (!found) {
/* 432 */       throw new NoSuchElementException("No entry '" + propToRm.getName() + "' found, can't remove");
/*     */     }
/* 434 */     init(props);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 443 */     return this._size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCaseInsensitive() {
/* 449 */     return this._caseInsensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAliases() {
/* 456 */     return !this._aliasDefs.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<SettableBeanProperty> iterator() {
/* 464 */     return _properties().iterator();
/*     */   }
/*     */   
/*     */   private List<SettableBeanProperty> _properties() {
/* 468 */     ArrayList<SettableBeanProperty> p = new ArrayList<>(this._size);
/* 469 */     for (int i = 1, end = this._hashArea.length; i < end; i += 2) {
/* 470 */       SettableBeanProperty prop = (SettableBeanProperty)this._hashArea[i];
/* 471 */       if (prop != null) {
/* 472 */         p.add(prop);
/*     */       }
/*     */     } 
/* 475 */     return p;
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
/*     */   public SettableBeanProperty[] getPropertiesInInsertionOrder() {
/* 487 */     return this._propsInOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String getPropertyName(SettableBeanProperty prop) {
/* 493 */     return this._caseInsensitive ? prop.getName().toLowerCase() : prop.getName();
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
/*     */   public SettableBeanProperty find(int index) {
/* 509 */     for (int i = 1, end = this._hashArea.length; i < end; i += 2) {
/* 510 */       SettableBeanProperty prop = (SettableBeanProperty)this._hashArea[i];
/* 511 */       if (prop != null && index == prop.getPropertyIndex()) {
/* 512 */         return prop;
/*     */       }
/*     */     } 
/* 515 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty find(String key) {
/* 520 */     if (key == null) {
/* 521 */       throw new IllegalArgumentException("Cannot pass null property name");
/*     */     }
/* 523 */     if (this._caseInsensitive) {
/* 524 */       key = key.toLowerCase();
/*     */     }
/*     */ 
/*     */     
/* 528 */     int slot = key.hashCode() & this._hashMask;
/*     */ 
/*     */ 
/*     */     
/* 532 */     int ix = slot << 1;
/* 533 */     Object match = this._hashArea[ix];
/* 534 */     if (match == key || key.equals(match)) {
/* 535 */       return (SettableBeanProperty)this._hashArea[ix + 1];
/*     */     }
/* 537 */     return _find2(key, slot, match);
/*     */   }
/*     */ 
/*     */   
/*     */   private final SettableBeanProperty _find2(String key, int slot, Object match) {
/* 542 */     if (match == null)
/*     */     {
/* 544 */       return _findWithAlias(this._aliasMapping.get(key));
/*     */     }
/*     */     
/* 547 */     int hashSize = this._hashMask + 1;
/* 548 */     int ix = hashSize + (slot >> 1) << 1;
/* 549 */     match = this._hashArea[ix];
/* 550 */     if (key.equals(match)) {
/* 551 */       return (SettableBeanProperty)this._hashArea[ix + 1];
/*     */     }
/* 553 */     if (match != null) {
/* 554 */       int i = hashSize + (hashSize >> 1) << 1;
/* 555 */       for (int end = i + this._spillCount; i < end; i += 2) {
/* 556 */         match = this._hashArea[i];
/* 557 */         if (match == key || key.equals(match)) {
/* 558 */           return (SettableBeanProperty)this._hashArea[i + 1];
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 563 */     return _findWithAlias(this._aliasMapping.get(key));
/*     */   }
/*     */ 
/*     */   
/*     */   private SettableBeanProperty _findWithAlias(String keyFromAlias) {
/* 568 */     if (keyFromAlias == null) {
/* 569 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 573 */     int slot = _hashCode(keyFromAlias);
/* 574 */     int ix = slot << 1;
/* 575 */     Object match = this._hashArea[ix];
/* 576 */     if (keyFromAlias.equals(match)) {
/* 577 */       return (SettableBeanProperty)this._hashArea[ix + 1];
/*     */     }
/* 579 */     if (match == null) {
/* 580 */       return null;
/*     */     }
/* 582 */     return _find2ViaAlias(keyFromAlias, slot, match);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private SettableBeanProperty _find2ViaAlias(String key, int slot, Object match) {
/* 588 */     int hashSize = this._hashMask + 1;
/* 589 */     int ix = hashSize + (slot >> 1) << 1;
/* 590 */     match = this._hashArea[ix];
/* 591 */     if (key.equals(match)) {
/* 592 */       return (SettableBeanProperty)this._hashArea[ix + 1];
/*     */     }
/* 594 */     if (match != null) {
/* 595 */       int i = hashSize + (hashSize >> 1) << 1;
/* 596 */       for (int end = i + this._spillCount; i < end; i += 2) {
/* 597 */         match = this._hashArea[i];
/* 598 */         if (match == key || key.equals(match)) {
/* 599 */           return (SettableBeanProperty)this._hashArea[i + 1];
/*     */         }
/*     */       } 
/*     */     } 
/* 603 */     return null;
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
/*     */   public boolean findDeserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean, String key) throws IOException {
/* 624 */     SettableBeanProperty prop = find(key);
/* 625 */     if (prop == null) {
/* 626 */       return false;
/*     */     }
/*     */     try {
/* 629 */       prop.deserializeAndSet(p, ctxt, bean);
/* 630 */     } catch (Exception e) {
/* 631 */       wrapAndThrow(e, bean, key, ctxt);
/*     */     } 
/* 633 */     return true;
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
/*     */   public String toString() {
/* 645 */     StringBuilder sb = new StringBuilder();
/* 646 */     sb.append("Properties=[");
/* 647 */     int count = 0;
/*     */     
/* 649 */     Iterator<SettableBeanProperty> it = iterator();
/* 650 */     while (it.hasNext()) {
/* 651 */       SettableBeanProperty prop = it.next();
/* 652 */       if (count++ > 0) {
/* 653 */         sb.append(", ");
/*     */       }
/* 655 */       sb.append(prop.getName());
/* 656 */       sb.append('(');
/* 657 */       sb.append(prop.getType());
/* 658 */       sb.append(')');
/*     */     } 
/* 660 */     sb.append(']');
/* 661 */     if (!this._aliasDefs.isEmpty()) {
/* 662 */       sb.append("(aliases: ");
/* 663 */       sb.append(this._aliasDefs);
/* 664 */       sb.append(")");
/*     */     } 
/* 666 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty _rename(SettableBeanProperty prop, NameTransformer xf) {
/* 677 */     if (prop == null) {
/* 678 */       return prop;
/*     */     }
/* 680 */     String newName = xf.transform(prop.getName());
/* 681 */     prop = prop.withSimpleName(newName);
/* 682 */     JsonDeserializer<?> deser = prop.getValueDeserializer();
/* 683 */     if (deser != null) {
/*     */ 
/*     */       
/* 686 */       JsonDeserializer<Object> newDeser = deser.unwrappingDeserializer(xf);
/* 687 */       if (newDeser != deser) {
/* 688 */         prop = prop.withValueDeserializer(newDeser);
/*     */       }
/*     */     } 
/* 691 */     return prop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt) throws IOException {
/* 698 */     while (t instanceof java.lang.reflect.InvocationTargetException && t.getCause() != null) {
/* 699 */       t = t.getCause();
/*     */     }
/*     */     
/* 702 */     ClassUtil.throwIfError(t);
/*     */     
/* 704 */     boolean wrap = (ctxt == null || ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/*     */     
/* 706 */     if (t instanceof IOException) {
/* 707 */       if (!wrap || !(t instanceof com.fasterxml.jackson.core.JsonProcessingException)) {
/* 708 */         throw (IOException)t;
/*     */       }
/* 710 */     } else if (!wrap) {
/* 711 */       ClassUtil.throwIfRTE(t);
/*     */     } 
/* 713 */     throw JsonMappingException.wrapWithPath(t, bean, fieldName);
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
/*     */   private final int _findIndexInHash(String key) {
/* 726 */     int slot = _hashCode(key);
/* 727 */     int ix = slot << 1;
/*     */ 
/*     */     
/* 730 */     if (key.equals(this._hashArea[ix])) {
/* 731 */       return ix + 1;
/*     */     }
/*     */     
/* 734 */     int hashSize = this._hashMask + 1;
/* 735 */     ix = hashSize + (slot >> 1) << 1;
/* 736 */     if (key.equals(this._hashArea[ix])) {
/* 737 */       return ix + 1;
/*     */     }
/*     */     
/* 740 */     int i = hashSize + (hashSize >> 1) << 1;
/* 741 */     for (int end = i + this._spillCount; i < end; i += 2) {
/* 742 */       if (key.equals(this._hashArea[i])) {
/* 743 */         return i + 1;
/*     */       }
/*     */     } 
/* 746 */     return -1;
/*     */   }
/*     */   
/*     */   private final int _findFromOrdered(SettableBeanProperty prop) {
/* 750 */     for (int i = 0, end = this._propsInOrder.length; i < end; i++) {
/* 751 */       if (this._propsInOrder[i] == prop) {
/* 752 */         return i;
/*     */       }
/*     */     } 
/* 755 */     throw new IllegalStateException("Illegal state: property '" + prop.getName() + "' missing from _propsInOrder");
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
/*     */   private final int _hashCode(String key) {
/* 769 */     return key.hashCode() & this._hashMask;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, String> _buildAliasMapping(Map<String, List<PropertyName>> defs) {
/* 775 */     if (defs == null || defs.isEmpty()) {
/* 776 */       return Collections.emptyMap();
/*     */     }
/* 778 */     Map<String, String> aliases = new HashMap<>();
/* 779 */     for (Map.Entry<String, List<PropertyName>> entry : defs.entrySet()) {
/* 780 */       String key = entry.getKey();
/* 781 */       if (this._caseInsensitive) {
/* 782 */         key = key.toLowerCase();
/*     */       }
/* 784 */       for (PropertyName pn : entry.getValue()) {
/* 785 */         String mapped = pn.getSimpleName();
/* 786 */         if (this._caseInsensitive) {
/* 787 */           mapped = mapped.toLowerCase();
/*     */         }
/* 789 */         aliases.put(mapped, key);
/*     */       } 
/*     */     } 
/* 792 */     return aliases;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\BeanPropertyMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */