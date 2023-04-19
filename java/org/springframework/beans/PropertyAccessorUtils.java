/*     */ package org.springframework.beans;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertyAccessorUtils
/*     */ {
/*     */   public static String getPropertyName(String propertyPath) {
/*  36 */     int separatorIndex = propertyPath.endsWith("]") ? propertyPath.indexOf('[') : -1;
/*  37 */     return (separatorIndex != -1) ? propertyPath.substring(0, separatorIndex) : propertyPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNestedOrIndexedProperty(String propertyPath) {
/*  46 */     if (propertyPath == null) {
/*  47 */       return false;
/*     */     }
/*  49 */     for (int i = 0; i < propertyPath.length(); i++) {
/*  50 */       char ch = propertyPath.charAt(i);
/*  51 */       if (ch == '.' || ch == '[')
/*     */       {
/*  53 */         return true;
/*     */       }
/*     */     } 
/*  56 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getFirstNestedPropertySeparatorIndex(String propertyPath) {
/*  66 */     return getNestedPropertySeparatorIndex(propertyPath, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getLastNestedPropertySeparatorIndex(String propertyPath) {
/*  76 */     return getNestedPropertySeparatorIndex(propertyPath, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getNestedPropertySeparatorIndex(String propertyPath, boolean last) {
/*  87 */     boolean inKey = false;
/*  88 */     int length = propertyPath.length();
/*  89 */     int i = last ? (length - 1) : 0;
/*  90 */     while (last ? (i >= 0) : (i < length)) {
/*  91 */       switch (propertyPath.charAt(i)) {
/*     */         case '[':
/*     */         case ']':
/*  94 */           inKey = !inKey;
/*     */           break;
/*     */         case '.':
/*  97 */           if (!inKey)
/*  98 */             return i; 
/*     */           break;
/*     */       } 
/* 101 */       if (last) {
/* 102 */         i--;
/*     */         continue;
/*     */       } 
/* 105 */       i++;
/*     */     } 
/*     */     
/* 108 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matchesProperty(String registeredPath, String propertyPath) {
/* 119 */     if (!registeredPath.startsWith(propertyPath)) {
/* 120 */       return false;
/*     */     }
/* 122 */     if (registeredPath.length() == propertyPath.length()) {
/* 123 */       return true;
/*     */     }
/* 125 */     if (registeredPath.charAt(propertyPath.length()) != '[') {
/* 126 */       return false;
/*     */     }
/* 128 */     return 
/* 129 */       (registeredPath.indexOf(']', propertyPath.length() + 1) == registeredPath.length() - 1);
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
/*     */   public static String canonicalPropertyName(String propertyName) {
/* 141 */     if (propertyName == null) {
/* 142 */       return "";
/*     */     }
/*     */     
/* 145 */     StringBuilder sb = new StringBuilder(propertyName);
/* 146 */     int searchIndex = 0;
/* 147 */     while (searchIndex != -1) {
/* 148 */       int keyStart = sb.indexOf("[", searchIndex);
/* 149 */       searchIndex = -1;
/* 150 */       if (keyStart != -1) {
/* 151 */         int keyEnd = sb.indexOf("]", keyStart + "["
/* 152 */             .length());
/* 153 */         if (keyEnd != -1) {
/* 154 */           String key = sb.substring(keyStart + "[".length(), keyEnd);
/* 155 */           if ((key.startsWith("'") && key.endsWith("'")) || (key.startsWith("\"") && key.endsWith("\""))) {
/* 156 */             sb.delete(keyStart + 1, keyStart + 2);
/* 157 */             sb.delete(keyEnd - 2, keyEnd - 1);
/* 158 */             keyEnd -= 2;
/*     */           } 
/* 160 */           searchIndex = keyEnd + "]".length();
/*     */         } 
/*     */       } 
/*     */     } 
/* 164 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] canonicalPropertyNames(String[] propertyNames) {
/* 175 */     if (propertyNames == null) {
/* 176 */       return null;
/*     */     }
/* 178 */     String[] result = new String[propertyNames.length];
/* 179 */     for (int i = 0; i < propertyNames.length; i++) {
/* 180 */       result[i] = canonicalPropertyName(propertyNames[i]);
/*     */     }
/* 182 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyAccessorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */