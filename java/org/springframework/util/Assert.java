/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Assert
/*     */ {
/*     */   public static void state(boolean expression, String message) {
/*  69 */     if (!expression) {
/*  70 */       throw new IllegalStateException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void state(boolean expression) {
/*  79 */     state(expression, "[Assertion failed] - this state invariant must be true");
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
/*     */   public static void isTrue(boolean expression, String message) {
/*  91 */     if (!expression) {
/*  92 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void isTrue(boolean expression) {
/* 101 */     isTrue(expression, "[Assertion failed] - this expression must be true");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void isNull(Object object, String message) {
/* 112 */     if (object != null) {
/* 113 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void isNull(Object object) {
/* 122 */     isNull(object, "[Assertion failed] - the object argument must be null");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void notNull(Object object, String message) {
/* 133 */     if (object == null) {
/* 134 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notNull(Object object) {
/* 143 */     notNull(object, "[Assertion failed] - this argument is required; it must not be null");
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
/*     */   public static void hasLength(String text, String message) {
/* 156 */     if (!StringUtils.hasLength(text)) {
/* 157 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void hasLength(String text) {
/* 166 */     hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
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
/*     */   public static void hasText(String text, String message) {
/* 180 */     if (!StringUtils.hasText(text)) {
/* 181 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void hasText(String text) {
/* 190 */     hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
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
/*     */   public static void doesNotContain(String textToSearch, String substring, String message) {
/* 203 */     if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch
/* 204 */       .contains(substring)) {
/* 205 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void doesNotContain(String textToSearch, String substring) {
/* 214 */     doesNotContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
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
/*     */   public static void notEmpty(Object[] array, String message) {
/* 227 */     if (ObjectUtils.isEmpty(array)) {
/* 228 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notEmpty(Object[] array) {
/* 237 */     notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
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
/*     */   public static void noNullElements(Object[] array, String message) {
/* 249 */     if (array != null) {
/* 250 */       for (Object element : array) {
/* 251 */         if (element == null) {
/* 252 */           throw new IllegalArgumentException(message);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void noNullElements(Object[] array) {
/* 263 */     noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
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
/*     */   public static void notEmpty(Collection<?> collection, String message) {
/* 276 */     if (CollectionUtils.isEmpty(collection)) {
/* 277 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notEmpty(Collection<?> collection) {
/* 286 */     notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
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
/*     */   public static void notEmpty(Map<?, ?> map, String message) {
/* 299 */     if (CollectionUtils.isEmpty(map)) {
/* 300 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notEmpty(Map<?, ?> map) {
/* 309 */     notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
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
/*     */   public static void isInstanceOf(Class<?> type, Object obj, String message) {
/* 325 */     notNull(type, "Type to check against must not be null");
/* 326 */     if (!type.isInstance(obj)) {
/* 327 */       instanceCheckFailed(type, obj, message);
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
/*     */   public static void isInstanceOf(Class<?> type, Object obj) {
/* 339 */     isInstanceOf(type, obj, "");
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
/*     */   public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
/* 355 */     notNull(superType, "Super type to check against must not be null");
/* 356 */     if (subType == null || !superType.isAssignableFrom(subType)) {
/* 357 */       assignableCheckFailed(superType, subType, message);
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
/*     */   public static void isAssignable(Class<?> superType, Class<?> subType) {
/* 369 */     isAssignable(superType, subType, "");
/*     */   }
/*     */ 
/*     */   
/*     */   private static void instanceCheckFailed(Class<?> type, Object obj, String msg) {
/* 374 */     String className = (obj != null) ? obj.getClass().getName() : "null";
/* 375 */     String result = "";
/* 376 */     boolean defaultMessage = true;
/* 377 */     if (StringUtils.hasLength(msg)) {
/* 378 */       if (endsWithSeparator(msg)) {
/* 379 */         result = msg + " ";
/*     */       } else {
/*     */         
/* 382 */         result = messageWithTypeName(msg, className);
/* 383 */         defaultMessage = false;
/*     */       } 
/*     */     }
/* 386 */     if (defaultMessage) {
/* 387 */       result = result + "Object of class [" + className + "] must be an instance of " + type;
/*     */     }
/* 389 */     throw new IllegalArgumentException(result);
/*     */   }
/*     */   
/*     */   private static void assignableCheckFailed(Class<?> superType, Class<?> subType, String msg) {
/* 393 */     String result = "";
/* 394 */     boolean defaultMessage = true;
/* 395 */     if (StringUtils.hasLength(msg)) {
/* 396 */       if (endsWithSeparator(msg)) {
/* 397 */         result = msg + " ";
/*     */       } else {
/*     */         
/* 400 */         result = messageWithTypeName(msg, subType);
/* 401 */         defaultMessage = false;
/*     */       } 
/*     */     }
/* 404 */     if (defaultMessage) {
/* 405 */       result = result + subType + " is not assignable to " + superType;
/*     */     }
/* 407 */     throw new IllegalArgumentException(result);
/*     */   }
/*     */   
/*     */   private static boolean endsWithSeparator(String msg) {
/* 411 */     return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
/*     */   }
/*     */   
/*     */   private static String messageWithTypeName(String msg, Object typeName) {
/* 415 */     return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\Assert.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */