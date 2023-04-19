/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
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
/*     */ public abstract class NumberUtils
/*     */ {
/*  39 */   private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
/*     */   
/*  41 */   private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Set<Class<?>> STANDARD_NUMBER_TYPES;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  50 */     Set<Class<?>> numberTypes = new HashSet<Class<?>>(8);
/*  51 */     numberTypes.add(Byte.class);
/*  52 */     numberTypes.add(Short.class);
/*  53 */     numberTypes.add(Integer.class);
/*  54 */     numberTypes.add(Long.class);
/*  55 */     numberTypes.add(BigInteger.class);
/*  56 */     numberTypes.add(Float.class);
/*  57 */     numberTypes.add(Double.class);
/*  58 */     numberTypes.add(BigDecimal.class);
/*  59 */     STANDARD_NUMBER_TYPES = Collections.unmodifiableSet(numberTypes);
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
/*     */   public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) throws IllegalArgumentException {
/*  83 */     Assert.notNull(number, "Number must not be null");
/*  84 */     Assert.notNull(targetClass, "Target class must not be null");
/*     */     
/*  86 */     if (targetClass.isInstance(number)) {
/*  87 */       return (T)number;
/*     */     }
/*  89 */     if (Byte.class == targetClass) {
/*  90 */       long value = checkedLongValue(number, targetClass);
/*  91 */       if (value < -128L || value > 127L) {
/*  92 */         raiseOverflowException(number, targetClass);
/*     */       }
/*  94 */       return (T)Byte.valueOf(number.byteValue());
/*     */     } 
/*  96 */     if (Short.class == targetClass) {
/*  97 */       long value = checkedLongValue(number, targetClass);
/*  98 */       if (value < -32768L || value > 32767L) {
/*  99 */         raiseOverflowException(number, targetClass);
/*     */       }
/* 101 */       return (T)Short.valueOf(number.shortValue());
/*     */     } 
/* 103 */     if (Integer.class == targetClass) {
/* 104 */       long value = checkedLongValue(number, targetClass);
/* 105 */       if (value < -2147483648L || value > 2147483647L) {
/* 106 */         raiseOverflowException(number, targetClass);
/*     */       }
/* 108 */       return (T)Integer.valueOf(number.intValue());
/*     */     } 
/* 110 */     if (Long.class == targetClass) {
/* 111 */       long value = checkedLongValue(number, targetClass);
/* 112 */       return (T)Long.valueOf(value);
/*     */     } 
/* 114 */     if (BigInteger.class == targetClass) {
/* 115 */       if (number instanceof BigDecimal)
/*     */       {
/* 117 */         return (T)((BigDecimal)number).toBigInteger();
/*     */       }
/*     */ 
/*     */       
/* 121 */       return (T)BigInteger.valueOf(number.longValue());
/*     */     } 
/*     */     
/* 124 */     if (Float.class == targetClass) {
/* 125 */       return (T)Float.valueOf(number.floatValue());
/*     */     }
/* 127 */     if (Double.class == targetClass) {
/* 128 */       return (T)Double.valueOf(number.doubleValue());
/*     */     }
/* 130 */     if (BigDecimal.class == targetClass)
/*     */     {
/*     */       
/* 133 */       return (T)new BigDecimal(number.toString());
/*     */     }
/*     */     
/* 136 */     throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number
/* 137 */         .getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
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
/*     */   private static long checkedLongValue(Number number, Class<? extends Number> targetClass) {
/* 151 */     BigInteger bigInt = null;
/* 152 */     if (number instanceof BigInteger) {
/* 153 */       bigInt = (BigInteger)number;
/*     */     }
/* 155 */     else if (number instanceof BigDecimal) {
/* 156 */       bigInt = ((BigDecimal)number).toBigInteger();
/*     */     } 
/*     */     
/* 159 */     if (bigInt != null && (bigInt.compareTo(LONG_MIN) < 0 || bigInt.compareTo(LONG_MAX) > 0)) {
/* 160 */       raiseOverflowException(number, targetClass);
/*     */     }
/* 162 */     return number.longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void raiseOverflowException(Number number, Class<?> targetClass) {
/* 172 */     throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number
/* 173 */         .getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
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
/*     */   public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
/* 197 */     Assert.notNull(text, "Text must not be null");
/* 198 */     Assert.notNull(targetClass, "Target class must not be null");
/* 199 */     String trimmed = StringUtils.trimAllWhitespace(text);
/*     */     
/* 201 */     if (Byte.class == targetClass) {
/* 202 */       return isHexNumber(trimmed) ? (T)Byte.decode(trimmed) : (T)Byte.valueOf(trimmed);
/*     */     }
/* 204 */     if (Short.class == targetClass) {
/* 205 */       return isHexNumber(trimmed) ? (T)Short.decode(trimmed) : (T)Short.valueOf(trimmed);
/*     */     }
/* 207 */     if (Integer.class == targetClass) {
/* 208 */       return isHexNumber(trimmed) ? (T)Integer.decode(trimmed) : (T)Integer.valueOf(trimmed);
/*     */     }
/* 210 */     if (Long.class == targetClass) {
/* 211 */       return isHexNumber(trimmed) ? (T)Long.decode(trimmed) : (T)Long.valueOf(trimmed);
/*     */     }
/* 213 */     if (BigInteger.class == targetClass) {
/* 214 */       return isHexNumber(trimmed) ? (T)decodeBigInteger(trimmed) : (T)new BigInteger(trimmed);
/*     */     }
/* 216 */     if (Float.class == targetClass) {
/* 217 */       return (T)Float.valueOf(trimmed);
/*     */     }
/* 219 */     if (Double.class == targetClass) {
/* 220 */       return (T)Double.valueOf(trimmed);
/*     */     }
/* 222 */     if (BigDecimal.class == targetClass || Number.class == targetClass) {
/* 223 */       return (T)new BigDecimal(trimmed);
/*     */     }
/*     */     
/* 226 */     throw new IllegalArgumentException("Cannot convert String [" + text + "] to target class [" + targetClass
/* 227 */         .getName() + "]");
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
/*     */   public static <T extends Number> T parseNumber(String text, Class<T> targetClass, NumberFormat numberFormat) {
/* 247 */     if (numberFormat != null) {
/* 248 */       Assert.notNull(text, "Text must not be null");
/* 249 */       Assert.notNull(targetClass, "Target class must not be null");
/* 250 */       DecimalFormat decimalFormat = null;
/* 251 */       boolean resetBigDecimal = false;
/* 252 */       if (numberFormat instanceof DecimalFormat) {
/* 253 */         decimalFormat = (DecimalFormat)numberFormat;
/* 254 */         if (BigDecimal.class == targetClass && !decimalFormat.isParseBigDecimal()) {
/* 255 */           decimalFormat.setParseBigDecimal(true);
/* 256 */           resetBigDecimal = true;
/*     */         } 
/*     */       } 
/*     */       try {
/* 260 */         Number number = numberFormat.parse(StringUtils.trimAllWhitespace(text));
/* 261 */         return (T)convertNumberToTargetClass(number, (Class)targetClass);
/*     */       }
/* 263 */       catch (ParseException ex) {
/* 264 */         throw new IllegalArgumentException("Could not parse number: " + ex.getMessage());
/*     */       } finally {
/*     */         
/* 267 */         if (resetBigDecimal) {
/* 268 */           decimalFormat.setParseBigDecimal(false);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     return parseNumber(text, targetClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isHexNumber(String value) {
/* 283 */     int index = value.startsWith("-") ? 1 : 0;
/* 284 */     return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static BigInteger decodeBigInteger(String value) {
/* 293 */     int radix = 10;
/* 294 */     int index = 0;
/* 295 */     boolean negative = false;
/*     */ 
/*     */     
/* 298 */     if (value.startsWith("-")) {
/* 299 */       negative = true;
/* 300 */       index++;
/*     */     } 
/*     */ 
/*     */     
/* 304 */     if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
/* 305 */       index += 2;
/* 306 */       radix = 16;
/*     */     }
/* 308 */     else if (value.startsWith("#", index)) {
/* 309 */       index++;
/* 310 */       radix = 16;
/*     */     }
/* 312 */     else if (value.startsWith("0", index) && value.length() > 1 + index) {
/* 313 */       index++;
/* 314 */       radix = 8;
/*     */     } 
/*     */     
/* 317 */     BigInteger result = new BigInteger(value.substring(index), radix);
/* 318 */     return negative ? result.negate() : result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\NumberUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */