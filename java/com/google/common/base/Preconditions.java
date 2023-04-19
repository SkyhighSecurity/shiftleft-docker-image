/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Preconditions
/*     */ {
/*     */   public static void checkArgument(boolean expression) {
/*  68 */     if (!expression) {
/*  69 */       throw new IllegalArgumentException();
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
/*     */   public static void checkArgument(boolean expression, Object errorMessage) {
/*  83 */     if (!expression) {
/*  84 */       throw new IllegalArgumentException(String.valueOf(errorMessage));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
/* 109 */     if (!expression) {
/* 110 */       throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
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
/*     */   public static void checkState(boolean expression) {
/* 123 */     if (!expression) {
/* 124 */       throw new IllegalStateException();
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
/*     */   public static void checkState(boolean expression, Object errorMessage) {
/* 138 */     if (!expression) {
/* 139 */       throw new IllegalStateException(String.valueOf(errorMessage));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
/* 164 */     if (!expression) {
/* 165 */       throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
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
/*     */   public static <T> T checkNotNull(T reference) {
/* 179 */     if (reference == null) {
/* 180 */       throw new NullPointerException();
/*     */     }
/* 182 */     return reference;
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
/*     */   public static <T> T checkNotNull(T reference, Object errorMessage) {
/* 196 */     if (reference == null) {
/* 197 */       throw new NullPointerException(String.valueOf(errorMessage));
/*     */     }
/* 199 */     return reference;
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
/*     */   public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
/* 221 */     if (reference == null)
/*     */     {
/* 223 */       throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
/*     */     }
/*     */     
/* 226 */     return reference;
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
/*     */   public static int checkElementIndex(int index, int size) {
/* 243 */     return checkElementIndex(index, size, "index");
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
/*     */   public static int checkElementIndex(int index, int size, String desc) {
/* 262 */     if (index < 0 || index >= size) {
/* 263 */       throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
/*     */     }
/* 265 */     return index;
/*     */   }
/*     */   
/*     */   private static String badElementIndex(int index, int size, String desc) {
/* 269 */     if (index < 0)
/* 270 */       return format("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) }); 
/* 271 */     if (size < 0) {
/* 272 */       throw new IllegalArgumentException("negative size: " + size);
/*     */     }
/* 274 */     return format("%s (%s) must be less than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
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
/*     */   public static int checkPositionIndex(int index, int size) {
/* 292 */     return checkPositionIndex(index, size, "index");
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
/*     */   public static int checkPositionIndex(int index, int size, String desc) {
/* 311 */     if (index < 0 || index > size) {
/* 312 */       throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
/*     */     }
/* 314 */     return index;
/*     */   }
/*     */   
/*     */   private static String badPositionIndex(int index, int size, String desc) {
/* 318 */     if (index < 0)
/* 319 */       return format("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) }); 
/* 320 */     if (size < 0) {
/* 321 */       throw new IllegalArgumentException("negative size: " + size);
/*     */     }
/* 323 */     return format("%s (%s) must not be greater than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
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
/*     */   public static void checkPositionIndexes(int start, int end, int size) {
/* 344 */     if (start < 0 || end < start || end > size) {
/* 345 */       throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
/*     */     }
/*     */   }
/*     */   
/*     */   private static String badPositionIndexes(int start, int end, int size) {
/* 350 */     if (start < 0 || start > size) {
/* 351 */       return badPositionIndex(start, size, "start index");
/*     */     }
/* 353 */     if (end < 0 || end > size) {
/* 354 */       return badPositionIndex(end, size, "end index");
/*     */     }
/*     */     
/* 357 */     return format("end index (%s) must not be less than start index (%s)", new Object[] { Integer.valueOf(end), Integer.valueOf(start) });
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
/*     */   @VisibleForTesting
/*     */   static String format(String template, Object... args) {
/* 375 */     StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
/*     */     
/* 377 */     int templateStart = 0;
/* 378 */     int i = 0;
/* 379 */     while (i < args.length) {
/* 380 */       int placeholderStart = template.indexOf("%s", templateStart);
/* 381 */       if (placeholderStart == -1) {
/*     */         break;
/*     */       }
/* 384 */       builder.append(template.substring(templateStart, placeholderStart));
/* 385 */       builder.append(args[i++]);
/* 386 */       templateStart = placeholderStart + 2;
/*     */     } 
/* 388 */     builder.append(template.substring(templateStart));
/*     */ 
/*     */     
/* 391 */     if (i < args.length) {
/* 392 */       builder.append(" [");
/* 393 */       builder.append(args[i++]);
/* 394 */       while (i < args.length) {
/* 395 */         builder.append(", ");
/* 396 */         builder.append(args[i++]);
/*     */       } 
/* 398 */       builder.append("]");
/*     */     } 
/*     */     
/* 401 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\base\Preconditions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */