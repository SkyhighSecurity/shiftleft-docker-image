/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
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
/*     */ public abstract class ObjectUtils
/*     */ {
/*     */   private static final int INITIAL_HASH = 7;
/*     */   private static final int MULTIPLIER = 31;
/*     */   private static final String EMPTY_STRING = "";
/*     */   private static final String NULL_STRING = "null";
/*     */   private static final String ARRAY_START = "{";
/*     */   private static final String ARRAY_END = "}";
/*     */   private static final String EMPTY_ARRAY = "{}";
/*     */   private static final String ARRAY_ELEMENT_SEPARATOR = ", ";
/*     */   
/*     */   public static boolean isCheckedException(Throwable ex) {
/*  65 */     return (!(ex instanceof RuntimeException) && !(ex instanceof Error));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCompatibleWithThrowsClause(Throwable ex, Class<?>... declaredExceptions) {
/*  76 */     if (!isCheckedException(ex)) {
/*  77 */       return true;
/*     */     }
/*  79 */     if (declaredExceptions != null) {
/*  80 */       for (Class<?> declaredException : declaredExceptions) {
/*  81 */         if (declaredException.isInstance(ex)) {
/*  82 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isArray(Object obj) {
/*  95 */     return (obj != null && obj.getClass().isArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(Object[] array) {
/* 105 */     return (array == null || array.length == 0);
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
/*     */   public static boolean isEmpty(Object obj) {
/* 130 */     if (obj == null) {
/* 131 */       return true;
/*     */     }
/*     */     
/* 134 */     if (obj instanceof CharSequence) {
/* 135 */       return (((CharSequence)obj).length() == 0);
/*     */     }
/* 137 */     if (obj.getClass().isArray()) {
/* 138 */       return (Array.getLength(obj) == 0);
/*     */     }
/* 140 */     if (obj instanceof Collection) {
/* 141 */       return ((Collection)obj).isEmpty();
/*     */     }
/* 143 */     if (obj instanceof Map) {
/* 144 */       return ((Map)obj).isEmpty();
/*     */     }
/*     */ 
/*     */     
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsElement(Object[] array, Object element) {
/* 159 */     if (array == null) {
/* 160 */       return false;
/*     */     }
/* 162 */     for (Object arrayEle : array) {
/* 163 */       if (nullSafeEquals(arrayEle, element)) {
/* 164 */         return true;
/*     */       }
/*     */     } 
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsConstant(Enum<?>[] enumValues, String constant) {
/* 178 */     return containsConstant(enumValues, constant, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
/* 189 */     for (Enum<?> candidate : enumValues) {
/* 190 */       if (caseSensitive ? candidate
/* 191 */         .toString().equals(constant) : candidate
/* 192 */         .toString().equalsIgnoreCase(constant)) {
/* 193 */         return true;
/*     */       }
/*     */     } 
/* 196 */     return false;
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
/*     */   public static <E extends Enum<?>> E caseInsensitiveValueOf(E[] enumValues, String constant) {
/* 208 */     for (E candidate : enumValues) {
/* 209 */       if (candidate.toString().equalsIgnoreCase(constant)) {
/* 210 */         return candidate;
/*     */       }
/*     */     } 
/* 213 */     throw new IllegalArgumentException(
/* 214 */         String.format("constant [%s] does not exist in enum type %s", new Object[] {
/* 215 */             constant, enumValues.getClass().getComponentType().getName()
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
/* 226 */     Class<?> compType = Object.class;
/* 227 */     if (array != null) {
/* 228 */       compType = array.getClass().getComponentType();
/*     */     }
/* 230 */     else if (obj != null) {
/* 231 */       compType = obj.getClass();
/*     */     } 
/* 233 */     int newArrLength = (array != null) ? (array.length + 1) : 1;
/*     */     
/* 235 */     A[] newArr = (A[])Array.newInstance(compType, newArrLength);
/* 236 */     if (array != null) {
/* 237 */       System.arraycopy(array, 0, newArr, 0, array.length);
/*     */     }
/* 239 */     newArr[newArr.length - 1] = (A)obj;
/* 240 */     return newArr;
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
/*     */   public static Object[] toObjectArray(Object source) {
/* 253 */     if (source instanceof Object[]) {
/* 254 */       return (Object[])source;
/*     */     }
/* 256 */     if (source == null) {
/* 257 */       return new Object[0];
/*     */     }
/* 259 */     if (!source.getClass().isArray()) {
/* 260 */       throw new IllegalArgumentException("Source is not an array: " + source);
/*     */     }
/* 262 */     int length = Array.getLength(source);
/* 263 */     if (length == 0) {
/* 264 */       return new Object[0];
/*     */     }
/* 266 */     Class<?> wrapperType = Array.get(source, 0).getClass();
/* 267 */     Object[] newArray = (Object[])Array.newInstance(wrapperType, length);
/* 268 */     for (int i = 0; i < length; i++) {
/* 269 */       newArray[i] = Array.get(source, i);
/*     */     }
/* 271 */     return newArray;
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
/*     */   public static boolean nullSafeEquals(Object o1, Object o2) {
/* 291 */     if (o1 == o2) {
/* 292 */       return true;
/*     */     }
/* 294 */     if (o1 == null || o2 == null) {
/* 295 */       return false;
/*     */     }
/* 297 */     if (o1.equals(o2)) {
/* 298 */       return true;
/*     */     }
/* 300 */     if (o1.getClass().isArray() && o2.getClass().isArray()) {
/* 301 */       return arrayEquals(o1, o2);
/*     */     }
/* 303 */     return false;
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
/*     */   private static boolean arrayEquals(Object o1, Object o2) {
/* 316 */     if (o1 instanceof Object[] && o2 instanceof Object[]) {
/* 317 */       return Arrays.equals((Object[])o1, (Object[])o2);
/*     */     }
/* 319 */     if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
/* 320 */       return Arrays.equals((boolean[])o1, (boolean[])o2);
/*     */     }
/* 322 */     if (o1 instanceof byte[] && o2 instanceof byte[]) {
/* 323 */       return Arrays.equals((byte[])o1, (byte[])o2);
/*     */     }
/* 325 */     if (o1 instanceof char[] && o2 instanceof char[]) {
/* 326 */       return Arrays.equals((char[])o1, (char[])o2);
/*     */     }
/* 328 */     if (o1 instanceof double[] && o2 instanceof double[]) {
/* 329 */       return Arrays.equals((double[])o1, (double[])o2);
/*     */     }
/* 331 */     if (o1 instanceof float[] && o2 instanceof float[]) {
/* 332 */       return Arrays.equals((float[])o1, (float[])o2);
/*     */     }
/* 334 */     if (o1 instanceof int[] && o2 instanceof int[]) {
/* 335 */       return Arrays.equals((int[])o1, (int[])o2);
/*     */     }
/* 337 */     if (o1 instanceof long[] && o2 instanceof long[]) {
/* 338 */       return Arrays.equals((long[])o1, (long[])o2);
/*     */     }
/* 340 */     if (o1 instanceof short[] && o2 instanceof short[]) {
/* 341 */       return Arrays.equals((short[])o1, (short[])o2);
/*     */     }
/* 343 */     return false;
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
/*     */   public static int nullSafeHashCode(Object obj) {
/* 364 */     if (obj == null) {
/* 365 */       return 0;
/*     */     }
/* 367 */     if (obj.getClass().isArray()) {
/* 368 */       if (obj instanceof Object[]) {
/* 369 */         return nullSafeHashCode((Object[])obj);
/*     */       }
/* 371 */       if (obj instanceof boolean[]) {
/* 372 */         return nullSafeHashCode((boolean[])obj);
/*     */       }
/* 374 */       if (obj instanceof byte[]) {
/* 375 */         return nullSafeHashCode((byte[])obj);
/*     */       }
/* 377 */       if (obj instanceof char[]) {
/* 378 */         return nullSafeHashCode((char[])obj);
/*     */       }
/* 380 */       if (obj instanceof double[]) {
/* 381 */         return nullSafeHashCode((double[])obj);
/*     */       }
/* 383 */       if (obj instanceof float[]) {
/* 384 */         return nullSafeHashCode((float[])obj);
/*     */       }
/* 386 */       if (obj instanceof int[]) {
/* 387 */         return nullSafeHashCode((int[])obj);
/*     */       }
/* 389 */       if (obj instanceof long[]) {
/* 390 */         return nullSafeHashCode((long[])obj);
/*     */       }
/* 392 */       if (obj instanceof short[]) {
/* 393 */         return nullSafeHashCode((short[])obj);
/*     */       }
/*     */     } 
/* 396 */     return obj.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(Object[] array) {
/* 404 */     if (array == null) {
/* 405 */       return 0;
/*     */     }
/* 407 */     int hash = 7;
/* 408 */     for (Object element : array) {
/* 409 */       hash = 31 * hash + nullSafeHashCode(element);
/*     */     }
/* 411 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(boolean[] array) {
/* 419 */     if (array == null) {
/* 420 */       return 0;
/*     */     }
/* 422 */     int hash = 7;
/* 423 */     for (boolean element : array) {
/* 424 */       hash = 31 * hash + hashCode(element);
/*     */     }
/* 426 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(byte[] array) {
/* 434 */     if (array == null) {
/* 435 */       return 0;
/*     */     }
/* 437 */     int hash = 7;
/* 438 */     for (byte element : array) {
/* 439 */       hash = 31 * hash + element;
/*     */     }
/* 441 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(char[] array) {
/* 449 */     if (array == null) {
/* 450 */       return 0;
/*     */     }
/* 452 */     int hash = 7;
/* 453 */     for (char element : array) {
/* 454 */       hash = 31 * hash + element;
/*     */     }
/* 456 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(double[] array) {
/* 464 */     if (array == null) {
/* 465 */       return 0;
/*     */     }
/* 467 */     int hash = 7;
/* 468 */     for (double element : array) {
/* 469 */       hash = 31 * hash + hashCode(element);
/*     */     }
/* 471 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(float[] array) {
/* 479 */     if (array == null) {
/* 480 */       return 0;
/*     */     }
/* 482 */     int hash = 7;
/* 483 */     for (float element : array) {
/* 484 */       hash = 31 * hash + hashCode(element);
/*     */     }
/* 486 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(int[] array) {
/* 494 */     if (array == null) {
/* 495 */       return 0;
/*     */     }
/* 497 */     int hash = 7;
/* 498 */     for (int element : array) {
/* 499 */       hash = 31 * hash + element;
/*     */     }
/* 501 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(long[] array) {
/* 509 */     if (array == null) {
/* 510 */       return 0;
/*     */     }
/* 512 */     int hash = 7;
/* 513 */     for (long element : array) {
/* 514 */       hash = 31 * hash + hashCode(element);
/*     */     }
/* 516 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(short[] array) {
/* 524 */     if (array == null) {
/* 525 */       return 0;
/*     */     }
/* 527 */     int hash = 7;
/* 528 */     for (short element : array) {
/* 529 */       hash = 31 * hash + element;
/*     */     }
/* 531 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hashCode(boolean bool) {
/* 539 */     return bool ? 1231 : 1237;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hashCode(double dbl) {
/* 547 */     return hashCode(Double.doubleToLongBits(dbl));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hashCode(float flt) {
/* 555 */     return Float.floatToIntBits(flt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hashCode(long lng) {
/* 563 */     return (int)(lng ^ lng >>> 32L);
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
/*     */   public static String identityToString(Object obj) {
/* 578 */     if (obj == null) {
/* 579 */       return "";
/*     */     }
/* 581 */     return obj.getClass().getName() + "@" + getIdentityHexString(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getIdentityHexString(Object obj) {
/* 590 */     return Integer.toHexString(System.identityHashCode(obj));
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
/*     */   public static String getDisplayString(Object obj) {
/* 603 */     if (obj == null) {
/* 604 */       return "";
/*     */     }
/* 606 */     return nullSafeToString(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String nullSafeClassName(Object obj) {
/* 616 */     return (obj != null) ? obj.getClass().getName() : "null";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String nullSafeToString(Object obj) {
/* 627 */     if (obj == null) {
/* 628 */       return "null";
/*     */     }
/* 630 */     if (obj instanceof String) {
/* 631 */       return (String)obj;
/*     */     }
/* 633 */     if (obj instanceof Object[]) {
/* 634 */       return nullSafeToString((Object[])obj);
/*     */     }
/* 636 */     if (obj instanceof boolean[]) {
/* 637 */       return nullSafeToString((boolean[])obj);
/*     */     }
/* 639 */     if (obj instanceof byte[]) {
/* 640 */       return nullSafeToString((byte[])obj);
/*     */     }
/* 642 */     if (obj instanceof char[]) {
/* 643 */       return nullSafeToString((char[])obj);
/*     */     }
/* 645 */     if (obj instanceof double[]) {
/* 646 */       return nullSafeToString((double[])obj);
/*     */     }
/* 648 */     if (obj instanceof float[]) {
/* 649 */       return nullSafeToString((float[])obj);
/*     */     }
/* 651 */     if (obj instanceof int[]) {
/* 652 */       return nullSafeToString((int[])obj);
/*     */     }
/* 654 */     if (obj instanceof long[]) {
/* 655 */       return nullSafeToString((long[])obj);
/*     */     }
/* 657 */     if (obj instanceof short[]) {
/* 658 */       return nullSafeToString((short[])obj);
/*     */     }
/* 660 */     String str = obj.toString();
/* 661 */     return (str != null) ? str : "";
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
/*     */   public static String nullSafeToString(Object[] array) {
/* 674 */     if (array == null) {
/* 675 */       return "null";
/*     */     }
/* 677 */     int length = array.length;
/* 678 */     if (length == 0) {
/* 679 */       return "{}";
/*     */     }
/* 681 */     StringBuilder sb = new StringBuilder();
/* 682 */     for (int i = 0; i < length; i++) {
/* 683 */       if (i == 0) {
/* 684 */         sb.append("{");
/*     */       } else {
/*     */         
/* 687 */         sb.append(", ");
/*     */       } 
/* 689 */       sb.append(String.valueOf(array[i]));
/*     */     } 
/* 691 */     sb.append("}");
/* 692 */     return sb.toString();
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
/*     */   public static String nullSafeToString(boolean[] array) {
/* 705 */     if (array == null) {
/* 706 */       return "null";
/*     */     }
/* 708 */     int length = array.length;
/* 709 */     if (length == 0) {
/* 710 */       return "{}";
/*     */     }
/* 712 */     StringBuilder sb = new StringBuilder();
/* 713 */     for (int i = 0; i < length; i++) {
/* 714 */       if (i == 0) {
/* 715 */         sb.append("{");
/*     */       } else {
/*     */         
/* 718 */         sb.append(", ");
/*     */       } 
/*     */       
/* 721 */       sb.append(array[i]);
/*     */     } 
/* 723 */     sb.append("}");
/* 724 */     return sb.toString();
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
/*     */   public static String nullSafeToString(byte[] array) {
/* 737 */     if (array == null) {
/* 738 */       return "null";
/*     */     }
/* 740 */     int length = array.length;
/* 741 */     if (length == 0) {
/* 742 */       return "{}";
/*     */     }
/* 744 */     StringBuilder sb = new StringBuilder();
/* 745 */     for (int i = 0; i < length; i++) {
/* 746 */       if (i == 0) {
/* 747 */         sb.append("{");
/*     */       } else {
/*     */         
/* 750 */         sb.append(", ");
/*     */       } 
/* 752 */       sb.append(array[i]);
/*     */     } 
/* 754 */     sb.append("}");
/* 755 */     return sb.toString();
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
/*     */   public static String nullSafeToString(char[] array) {
/* 768 */     if (array == null) {
/* 769 */       return "null";
/*     */     }
/* 771 */     int length = array.length;
/* 772 */     if (length == 0) {
/* 773 */       return "{}";
/*     */     }
/* 775 */     StringBuilder sb = new StringBuilder();
/* 776 */     for (int i = 0; i < length; i++) {
/* 777 */       if (i == 0) {
/* 778 */         sb.append("{");
/*     */       } else {
/*     */         
/* 781 */         sb.append(", ");
/*     */       } 
/* 783 */       sb.append("'").append(array[i]).append("'");
/*     */     } 
/* 785 */     sb.append("}");
/* 786 */     return sb.toString();
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
/*     */   public static String nullSafeToString(double[] array) {
/* 799 */     if (array == null) {
/* 800 */       return "null";
/*     */     }
/* 802 */     int length = array.length;
/* 803 */     if (length == 0) {
/* 804 */       return "{}";
/*     */     }
/* 806 */     StringBuilder sb = new StringBuilder();
/* 807 */     for (int i = 0; i < length; i++) {
/* 808 */       if (i == 0) {
/* 809 */         sb.append("{");
/*     */       } else {
/*     */         
/* 812 */         sb.append(", ");
/*     */       } 
/*     */       
/* 815 */       sb.append(array[i]);
/*     */     } 
/* 817 */     sb.append("}");
/* 818 */     return sb.toString();
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
/*     */   public static String nullSafeToString(float[] array) {
/* 831 */     if (array == null) {
/* 832 */       return "null";
/*     */     }
/* 834 */     int length = array.length;
/* 835 */     if (length == 0) {
/* 836 */       return "{}";
/*     */     }
/* 838 */     StringBuilder sb = new StringBuilder();
/* 839 */     for (int i = 0; i < length; i++) {
/* 840 */       if (i == 0) {
/* 841 */         sb.append("{");
/*     */       } else {
/*     */         
/* 844 */         sb.append(", ");
/*     */       } 
/*     */       
/* 847 */       sb.append(array[i]);
/*     */     } 
/* 849 */     sb.append("}");
/* 850 */     return sb.toString();
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
/*     */   public static String nullSafeToString(int[] array) {
/* 863 */     if (array == null) {
/* 864 */       return "null";
/*     */     }
/* 866 */     int length = array.length;
/* 867 */     if (length == 0) {
/* 868 */       return "{}";
/*     */     }
/* 870 */     StringBuilder sb = new StringBuilder();
/* 871 */     for (int i = 0; i < length; i++) {
/* 872 */       if (i == 0) {
/* 873 */         sb.append("{");
/*     */       } else {
/*     */         
/* 876 */         sb.append(", ");
/*     */       } 
/* 878 */       sb.append(array[i]);
/*     */     } 
/* 880 */     sb.append("}");
/* 881 */     return sb.toString();
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
/*     */   public static String nullSafeToString(long[] array) {
/* 894 */     if (array == null) {
/* 895 */       return "null";
/*     */     }
/* 897 */     int length = array.length;
/* 898 */     if (length == 0) {
/* 899 */       return "{}";
/*     */     }
/* 901 */     StringBuilder sb = new StringBuilder();
/* 902 */     for (int i = 0; i < length; i++) {
/* 903 */       if (i == 0) {
/* 904 */         sb.append("{");
/*     */       } else {
/*     */         
/* 907 */         sb.append(", ");
/*     */       } 
/* 909 */       sb.append(array[i]);
/*     */     } 
/* 911 */     sb.append("}");
/* 912 */     return sb.toString();
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
/*     */   public static String nullSafeToString(short[] array) {
/* 925 */     if (array == null) {
/* 926 */       return "null";
/*     */     }
/* 928 */     int length = array.length;
/* 929 */     if (length == 0) {
/* 930 */       return "{}";
/*     */     }
/* 932 */     StringBuilder sb = new StringBuilder();
/* 933 */     for (int i = 0; i < length; i++) {
/* 934 */       if (i == 0) {
/* 935 */         sb.append("{");
/*     */       } else {
/*     */         
/* 938 */         sb.append(", ");
/*     */       } 
/* 940 */       sb.append(array[i]);
/*     */     } 
/* 942 */     sb.append("}");
/* 943 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\ObjectUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */