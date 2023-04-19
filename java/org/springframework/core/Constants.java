/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Constants
/*     */ {
/*     */   private final String className;
/*  52 */   private final Map<String, Object> fieldCache = new HashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constants(Class<?> clazz) {
/*  62 */     Assert.notNull(clazz, "Class must not be null");
/*  63 */     this.className = clazz.getName();
/*  64 */     Field[] fields = clazz.getFields();
/*  65 */     for (Field field : fields) {
/*  66 */       if (ReflectionUtils.isPublicStaticFinal(field)) {
/*  67 */         String name = field.getName();
/*     */         try {
/*  69 */           Object value = field.get(null);
/*  70 */           this.fieldCache.put(name, value);
/*     */         }
/*  72 */         catch (IllegalAccessException illegalAccessException) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getClassName() {
/*  84 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getSize() {
/*  91 */     return this.fieldCache.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Map<String, Object> getFieldCache() {
/*  99 */     return this.fieldCache;
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
/*     */   public Number asNumber(String code) throws ConstantException {
/* 112 */     Object obj = asObject(code);
/* 113 */     if (!(obj instanceof Number)) {
/* 114 */       throw new ConstantException(this.className, code, "not a Number");
/*     */     }
/* 116 */     return (Number)obj;
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
/*     */   public String asString(String code) throws ConstantException {
/* 128 */     return asObject(code).toString();
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
/*     */   public Object asObject(String code) throws ConstantException {
/* 140 */     Assert.notNull(code, "Code must not be null");
/* 141 */     String codeToUse = code.toUpperCase(Locale.ENGLISH);
/* 142 */     Object val = this.fieldCache.get(codeToUse);
/* 143 */     if (val == null) {
/* 144 */       throw new ConstantException(this.className, codeToUse, "not found");
/*     */     }
/* 146 */     return val;
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
/*     */   public Set<String> getNames(String namePrefix) {
/* 161 */     String prefixToUse = (namePrefix != null) ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 162 */     Set<String> names = new HashSet<String>();
/* 163 */     for (String code : this.fieldCache.keySet()) {
/* 164 */       if (code.startsWith(prefixToUse)) {
/* 165 */         names.add(code);
/*     */       }
/*     */     } 
/* 168 */     return names;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getNamesForProperty(String propertyName) {
/* 179 */     return getNames(propertyToConstantNamePrefix(propertyName));
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
/*     */   public Set<String> getNamesForSuffix(String nameSuffix) {
/* 193 */     String suffixToUse = (nameSuffix != null) ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 194 */     Set<String> names = new HashSet<String>();
/* 195 */     for (String code : this.fieldCache.keySet()) {
/* 196 */       if (code.endsWith(suffixToUse)) {
/* 197 */         names.add(code);
/*     */       }
/*     */     } 
/* 200 */     return names;
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
/*     */   public Set<Object> getValues(String namePrefix) {
/* 215 */     String prefixToUse = (namePrefix != null) ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 216 */     Set<Object> values = new HashSet();
/* 217 */     for (String code : this.fieldCache.keySet()) {
/* 218 */       if (code.startsWith(prefixToUse)) {
/* 219 */         values.add(this.fieldCache.get(code));
/*     */       }
/*     */     } 
/* 222 */     return values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Object> getValuesForProperty(String propertyName) {
/* 233 */     return getValues(propertyToConstantNamePrefix(propertyName));
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
/*     */   public Set<Object> getValuesForSuffix(String nameSuffix) {
/* 247 */     String suffixToUse = (nameSuffix != null) ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 248 */     Set<Object> values = new HashSet();
/* 249 */     for (String code : this.fieldCache.keySet()) {
/* 250 */       if (code.endsWith(suffixToUse)) {
/* 251 */         values.add(this.fieldCache.get(code));
/*     */       }
/*     */     } 
/* 254 */     return values;
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
/*     */   public String toCode(Object value, String namePrefix) throws ConstantException {
/* 267 */     String prefixToUse = (namePrefix != null) ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 268 */     for (Map.Entry<String, Object> entry : this.fieldCache.entrySet()) {
/* 269 */       if (((String)entry.getKey()).startsWith(prefixToUse) && entry.getValue().equals(value)) {
/* 270 */         return entry.getKey();
/*     */       }
/*     */     } 
/* 273 */     throw new ConstantException(this.className, prefixToUse, value);
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
/*     */   public String toCodeForProperty(Object value, String propertyName) throws ConstantException {
/* 286 */     return toCode(value, propertyToConstantNamePrefix(propertyName));
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
/*     */   public String toCodeForSuffix(Object value, String nameSuffix) throws ConstantException {
/* 298 */     String suffixToUse = (nameSuffix != null) ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 299 */     for (Map.Entry<String, Object> entry : this.fieldCache.entrySet()) {
/* 300 */       if (((String)entry.getKey()).endsWith(suffixToUse) && entry.getValue().equals(value)) {
/* 301 */         return entry.getKey();
/*     */       }
/*     */     } 
/* 304 */     throw new ConstantException(this.className, suffixToUse, value);
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
/*     */   public String propertyToConstantNamePrefix(String propertyName) {
/* 322 */     StringBuilder parsedPrefix = new StringBuilder();
/* 323 */     for (int i = 0; i < propertyName.length(); i++) {
/* 324 */       char c = propertyName.charAt(i);
/* 325 */       if (Character.isUpperCase(c)) {
/* 326 */         parsedPrefix.append("_");
/* 327 */         parsedPrefix.append(c);
/*     */       } else {
/*     */         
/* 330 */         parsedPrefix.append(Character.toUpperCase(c));
/*     */       } 
/*     */     } 
/* 333 */     return parsedPrefix.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\Constants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */