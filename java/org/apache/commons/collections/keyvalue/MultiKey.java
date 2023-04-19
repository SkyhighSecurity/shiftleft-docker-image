/*     */ package org.apache.commons.collections.keyvalue;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiKey
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4465448607415788805L;
/*     */   private final Object[] keys;
/*     */   private transient int hashCode;
/*     */   
/*     */   public MultiKey(Object key1, Object key2) {
/*  69 */     this(new Object[] { key1, key2 }, false);
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
/*     */   public MultiKey(Object key1, Object key2, Object key3) {
/*  83 */     this(new Object[] { key1, key2, key3 }, false);
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
/*     */   public MultiKey(Object key1, Object key2, Object key3, Object key4) {
/*  98 */     this(new Object[] { key1, key2, key3, key4 }, false);
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
/*     */   public MultiKey(Object key1, Object key2, Object key3, Object key4, Object key5) {
/* 114 */     this(new Object[] { key1, key2, key3, key4, key5 }, false);
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
/*     */   public MultiKey(Object[] keys) {
/* 129 */     this(keys, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiKey(Object[] keys, boolean makeClone) {
/* 158 */     if (keys == null) {
/* 159 */       throw new IllegalArgumentException("The array of keys must not be null");
/*     */     }
/* 161 */     if (makeClone) {
/* 162 */       this.keys = (Object[])keys.clone();
/*     */     } else {
/* 164 */       this.keys = keys;
/*     */     } 
/*     */     
/* 167 */     calculateHashCode(keys);
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
/*     */   public Object[] getKeys() {
/* 180 */     return (Object[])this.keys.clone();
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
/*     */   public Object getKey(int index) {
/* 195 */     return this.keys[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 205 */     return this.keys.length;
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
/*     */   public boolean equals(Object other) {
/* 219 */     if (other == this) {
/* 220 */       return true;
/*     */     }
/* 222 */     if (other instanceof MultiKey) {
/* 223 */       MultiKey otherMulti = (MultiKey)other;
/* 224 */       return Arrays.equals(this.keys, otherMulti.keys);
/*     */     } 
/* 226 */     return false;
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
/*     */   public int hashCode() {
/* 240 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 249 */     return "MultiKey" + Arrays.<Object>asList(this.keys).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void calculateHashCode(Object[] keys) {
/* 258 */     int total = 0;
/* 259 */     for (int i = 0; i < keys.length; i++) {
/* 260 */       if (keys[i] != null) {
/* 261 */         total ^= keys[i].hashCode();
/*     */       }
/*     */     } 
/* 264 */     this.hashCode = total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() {
/* 275 */     calculateHashCode(this.keys);
/* 276 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\keyvalue\MultiKey.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */