/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class ReadOnlySystemAttributesMap
/*     */   implements Map<String, String>
/*     */ {
/*     */   public boolean containsKey(Object key) {
/*  42 */     return (get(key) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(Object key) {
/*  51 */     if (!(key instanceof String)) {
/*  52 */       throw new IllegalArgumentException("Type of key [" + key
/*  53 */           .getClass().getName() + "] must be java.lang.String");
/*     */     }
/*  55 */     return getSystemAttribute((String)key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  60 */     return false;
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
/*     */   public int size() {
/*  74 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String put(String key, String value) {
/*  79 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  84 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String remove(Object key) {
/*  89 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  94 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/*  99 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends String> map) {
/* 104 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> values() {
/* 109 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, String>> entrySet() {
/* 114 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   protected abstract String getSystemAttribute(String paramString);
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\ReadOnlySystemAttributesMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */