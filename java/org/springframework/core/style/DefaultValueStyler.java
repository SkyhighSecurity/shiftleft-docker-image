/*     */ package org.springframework.core.style;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultValueStyler
/*     */   implements ValueStyler
/*     */ {
/*     */   private static final String EMPTY = "[empty]";
/*     */   private static final String NULL = "[null]";
/*     */   private static final String COLLECTION = "collection";
/*     */   private static final String SET = "set";
/*     */   private static final String LIST = "list";
/*     */   private static final String MAP = "map";
/*     */   private static final String ARRAY = "array";
/*     */   
/*     */   public String style(Object value) {
/*  53 */     if (value == null) {
/*  54 */       return "[null]";
/*     */     }
/*  56 */     if (value instanceof String) {
/*  57 */       return "'" + value + "'";
/*     */     }
/*  59 */     if (value instanceof Class) {
/*  60 */       return ClassUtils.getShortName((Class)value);
/*     */     }
/*  62 */     if (value instanceof Method) {
/*  63 */       Method method = (Method)value;
/*  64 */       return method.getName() + "@" + ClassUtils.getShortName(method.getDeclaringClass());
/*     */     } 
/*  66 */     if (value instanceof Map) {
/*  67 */       return style((Map<?, ?>)value);
/*     */     }
/*  69 */     if (value instanceof Map.Entry) {
/*  70 */       return style((Map.Entry<?, ?>)value);
/*     */     }
/*  72 */     if (value instanceof Collection) {
/*  73 */       return style((Collection)value);
/*     */     }
/*  75 */     if (value.getClass().isArray()) {
/*  76 */       return styleArray(ObjectUtils.toObjectArray(value));
/*     */     }
/*     */     
/*  79 */     return String.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   private <K, V> String style(Map<K, V> value) {
/*  84 */     StringBuilder result = new StringBuilder(value.size() * 8 + 16);
/*  85 */     result.append("map[");
/*  86 */     for (Iterator<Map.Entry<K, V>> it = value.entrySet().iterator(); it.hasNext(); ) {
/*  87 */       Map.Entry<K, V> entry = it.next();
/*  88 */       result.append(style(entry));
/*  89 */       if (it.hasNext()) {
/*  90 */         result.append(',').append(' ');
/*     */       }
/*     */     } 
/*  93 */     if (value.isEmpty()) {
/*  94 */       result.append("[empty]");
/*     */     }
/*  96 */     result.append("]");
/*  97 */     return result.toString();
/*     */   }
/*     */   
/*     */   private String style(Map.Entry<?, ?> value) {
/* 101 */     return style(value.getKey()) + " -> " + style(value.getValue());
/*     */   }
/*     */   
/*     */   private String style(Collection<?> value) {
/* 105 */     StringBuilder result = new StringBuilder(value.size() * 8 + 16);
/* 106 */     result.append(getCollectionTypeString(value)).append('[');
/* 107 */     for (Iterator<?> i = value.iterator(); i.hasNext(); ) {
/* 108 */       result.append(style(i.next()));
/* 109 */       if (i.hasNext()) {
/* 110 */         result.append(',').append(' ');
/*     */       }
/*     */     } 
/* 113 */     if (value.isEmpty()) {
/* 114 */       result.append("[empty]");
/*     */     }
/* 116 */     result.append("]");
/* 117 */     return result.toString();
/*     */   }
/*     */   
/*     */   private String getCollectionTypeString(Collection<?> value) {
/* 121 */     if (value instanceof java.util.List) {
/* 122 */       return "list";
/*     */     }
/* 124 */     if (value instanceof java.util.Set) {
/* 125 */       return "set";
/*     */     }
/*     */     
/* 128 */     return "collection";
/*     */   }
/*     */ 
/*     */   
/*     */   private String styleArray(Object[] array) {
/* 133 */     StringBuilder result = new StringBuilder(array.length * 8 + 16);
/* 134 */     result.append("array<").append(ClassUtils.getShortName(array.getClass().getComponentType())).append(">[");
/* 135 */     for (int i = 0; i < array.length - 1; i++) {
/* 136 */       result.append(style(array[i]));
/* 137 */       result.append(',').append(' ');
/*     */     } 
/* 139 */     if (array.length > 0) {
/* 140 */       result.append(style(array[array.length - 1]));
/*     */     } else {
/*     */       
/* 143 */       result.append("[empty]");
/*     */     } 
/* 145 */     result.append("]");
/* 146 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\style\DefaultValueStyler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */