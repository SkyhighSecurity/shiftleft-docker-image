/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CompactStringObjectMap
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  25 */   private static final CompactStringObjectMap EMPTY = new CompactStringObjectMap(1, 0, new Object[4]);
/*     */   
/*     */   private final int _hashMask;
/*     */   
/*     */   private final int _spillCount;
/*     */   
/*     */   private final Object[] _hashArea;
/*     */   
/*     */   private CompactStringObjectMap(int hashMask, int spillCount, Object[] hashArea) {
/*  34 */     this._hashMask = hashMask;
/*  35 */     this._spillCount = spillCount;
/*  36 */     this._hashArea = hashArea;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> CompactStringObjectMap construct(Map<String, T> all) {
/*  41 */     if (all.isEmpty()) {
/*  42 */       return EMPTY;
/*     */     }
/*     */ 
/*     */     
/*  46 */     int size = findSize(all.size());
/*  47 */     int mask = size - 1;
/*     */     
/*  49 */     int alloc = (size + (size >> 1)) * 2;
/*  50 */     Object[] hashArea = new Object[alloc];
/*  51 */     int spillCount = 0;
/*     */     
/*  53 */     for (Map.Entry<String, T> entry : all.entrySet()) {
/*  54 */       String key = entry.getKey();
/*     */ 
/*     */       
/*  57 */       if (key == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  61 */       int slot = key.hashCode() & mask;
/*  62 */       int ix = slot + slot;
/*     */ 
/*     */       
/*  65 */       if (hashArea[ix] != null) {
/*     */         
/*  67 */         ix = size + (slot >> 1) << 1;
/*  68 */         if (hashArea[ix] != null) {
/*     */           
/*  70 */           ix = (size + (size >> 1) << 1) + spillCount;
/*  71 */           spillCount += 2;
/*  72 */           if (ix >= hashArea.length) {
/*  73 */             hashArea = Arrays.copyOf(hashArea, hashArea.length + 4);
/*     */           }
/*     */         } 
/*     */       } 
/*  77 */       hashArea[ix] = key;
/*  78 */       hashArea[ix + 1] = entry.getValue();
/*     */     } 
/*  80 */     return new CompactStringObjectMap(mask, spillCount, hashArea);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int findSize(int size) {
/*  85 */     if (size <= 5) {
/*  86 */       return 8;
/*     */     }
/*  88 */     if (size <= 12) {
/*  89 */       return 16;
/*     */     }
/*  91 */     int needed = size + (size >> 2);
/*  92 */     int result = 32;
/*  93 */     while (result < needed) {
/*  94 */       result += result;
/*     */     }
/*  96 */     return result;
/*     */   }
/*     */   
/*     */   public Object find(String key) {
/* 100 */     int slot = key.hashCode() & this._hashMask;
/* 101 */     int ix = slot << 1;
/* 102 */     Object match = this._hashArea[ix];
/* 103 */     if (match == key || key.equals(match)) {
/* 104 */       return this._hashArea[ix + 1];
/*     */     }
/* 106 */     return _find2(key, slot, match);
/*     */   }
/*     */ 
/*     */   
/*     */   private final Object _find2(String key, int slot, Object match) {
/* 111 */     if (match == null) {
/* 112 */       return null;
/*     */     }
/* 114 */     int hashSize = this._hashMask + 1;
/* 115 */     int ix = hashSize + (slot >> 1) << 1;
/* 116 */     match = this._hashArea[ix];
/* 117 */     if (key.equals(match)) {
/* 118 */       return this._hashArea[ix + 1];
/*     */     }
/* 120 */     if (match != null) {
/* 121 */       int i = hashSize + (hashSize >> 1) << 1;
/* 122 */       for (int end = i + this._spillCount; i < end; i += 2) {
/* 123 */         match = this._hashArea[i];
/* 124 */         if (match == key || key.equals(match)) {
/* 125 */           return this._hashArea[i + 1];
/*     */         }
/*     */       } 
/*     */     } 
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object findCaseInsensitive(String key) {
/* 136 */     for (int i = 0, end = this._hashArea.length; i < end; i += 2) {
/* 137 */       Object k2 = this._hashArea[i];
/* 138 */       if (k2 != null) {
/* 139 */         String s = (String)k2;
/* 140 */         if (s.equalsIgnoreCase(key)) {
/* 141 */           return this._hashArea[i + 1];
/*     */         }
/*     */       } 
/*     */     } 
/* 145 */     return null;
/*     */   }
/*     */   
/*     */   public List<String> keys() {
/* 149 */     int end = this._hashArea.length;
/* 150 */     List<String> keys = new ArrayList<>(end >> 2);
/* 151 */     for (int i = 0; i < end; i += 2) {
/* 152 */       Object key = this._hashArea[i];
/* 153 */       if (key != null) {
/* 154 */         keys.add((String)key);
/*     */       }
/*     */     } 
/* 157 */     return keys;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databin\\util\CompactStringObjectMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */