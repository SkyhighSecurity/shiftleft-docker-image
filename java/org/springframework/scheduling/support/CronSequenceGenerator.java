/*     */ package org.springframework.scheduling.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CronSequenceGenerator
/*     */ {
/*     */   private final String expression;
/*     */   private final TimeZone timeZone;
/*  62 */   private final BitSet months = new BitSet(12);
/*     */   
/*  64 */   private final BitSet daysOfMonth = new BitSet(31);
/*     */   
/*  66 */   private final BitSet daysOfWeek = new BitSet(7);
/*     */   
/*  68 */   private final BitSet hours = new BitSet(24);
/*     */   
/*  70 */   private final BitSet minutes = new BitSet(60);
/*     */   
/*  72 */   private final BitSet seconds = new BitSet(60);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CronSequenceGenerator(String expression) {
/*  83 */     this(expression, TimeZone.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CronSequenceGenerator(String expression, TimeZone timeZone) {
/*  94 */     this.expression = expression;
/*  95 */     this.timeZone = timeZone;
/*  96 */     parse(expression);
/*     */   }
/*     */   
/*     */   private CronSequenceGenerator(String expression, String[] fields) {
/* 100 */     this.expression = expression;
/* 101 */     this.timeZone = null;
/* 102 */     doParse(fields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getExpression() {
/* 110 */     return this.expression;
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
/*     */   public Date next(Date date) {
/* 139 */     Calendar calendar = new GregorianCalendar();
/* 140 */     calendar.setTimeZone(this.timeZone);
/* 141 */     calendar.setTime(date);
/*     */ 
/*     */     
/* 144 */     calendar.set(14, 0);
/* 145 */     long originalTimestamp = calendar.getTimeInMillis();
/* 146 */     doNext(calendar, calendar.get(1));
/*     */     
/* 148 */     if (calendar.getTimeInMillis() == originalTimestamp) {
/*     */       
/* 150 */       calendar.add(13, 1);
/* 151 */       doNext(calendar, calendar.get(1));
/*     */     } 
/*     */     
/* 154 */     return calendar.getTime();
/*     */   }
/*     */   
/*     */   private void doNext(Calendar calendar, int dot) {
/* 158 */     List<Integer> resets = new ArrayList<Integer>();
/*     */     
/* 160 */     int second = calendar.get(13);
/* 161 */     List<Integer> emptyList = Collections.emptyList();
/* 162 */     int updateSecond = findNext(this.seconds, second, calendar, 13, 12, emptyList);
/* 163 */     if (second == updateSecond) {
/* 164 */       resets.add(Integer.valueOf(13));
/*     */     }
/*     */     
/* 167 */     int minute = calendar.get(12);
/* 168 */     int updateMinute = findNext(this.minutes, minute, calendar, 12, 11, resets);
/* 169 */     if (minute == updateMinute) {
/* 170 */       resets.add(Integer.valueOf(12));
/*     */     } else {
/*     */       
/* 173 */       doNext(calendar, dot);
/*     */     } 
/*     */     
/* 176 */     int hour = calendar.get(11);
/* 177 */     int updateHour = findNext(this.hours, hour, calendar, 11, 7, resets);
/* 178 */     if (hour == updateHour) {
/* 179 */       resets.add(Integer.valueOf(11));
/*     */     } else {
/*     */       
/* 182 */       doNext(calendar, dot);
/*     */     } 
/*     */     
/* 185 */     int dayOfWeek = calendar.get(7);
/* 186 */     int dayOfMonth = calendar.get(5);
/* 187 */     int updateDayOfMonth = findNextDay(calendar, this.daysOfMonth, dayOfMonth, this.daysOfWeek, dayOfWeek, resets);
/* 188 */     if (dayOfMonth == updateDayOfMonth) {
/* 189 */       resets.add(Integer.valueOf(5));
/*     */     } else {
/*     */       
/* 192 */       doNext(calendar, dot);
/*     */     } 
/*     */     
/* 195 */     int month = calendar.get(2);
/* 196 */     int updateMonth = findNext(this.months, month, calendar, 2, 1, resets);
/* 197 */     if (month != updateMonth) {
/* 198 */       if (calendar.get(1) - dot > 4) {
/* 199 */         throw new IllegalArgumentException("Invalid cron expression \"" + this.expression + "\" led to runaway search for next trigger");
/*     */       }
/*     */       
/* 202 */       doNext(calendar, dot);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int findNextDay(Calendar calendar, BitSet daysOfMonth, int dayOfMonth, BitSet daysOfWeek, int dayOfWeek, List<Integer> resets) {
/* 210 */     int count = 0;
/* 211 */     int max = 366;
/*     */ 
/*     */     
/* 214 */     while ((!daysOfMonth.get(dayOfMonth) || !daysOfWeek.get(dayOfWeek - 1)) && count++ < max) {
/* 215 */       calendar.add(5, 1);
/* 216 */       dayOfMonth = calendar.get(5);
/* 217 */       dayOfWeek = calendar.get(7);
/* 218 */       reset(calendar, resets);
/*     */     } 
/* 220 */     if (count >= max) {
/* 221 */       throw new IllegalArgumentException("Overflow in day for expression \"" + this.expression + "\"");
/*     */     }
/* 223 */     return dayOfMonth;
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
/*     */   private int findNext(BitSet bits, int value, Calendar calendar, int field, int nextField, List<Integer> lowerOrders) {
/* 239 */     int nextValue = bits.nextSetBit(value);
/*     */     
/* 241 */     if (nextValue == -1) {
/* 242 */       calendar.add(nextField, 1);
/* 243 */       reset(calendar, Collections.singletonList(Integer.valueOf(field)));
/* 244 */       nextValue = bits.nextSetBit(0);
/*     */     } 
/* 246 */     if (nextValue != value) {
/* 247 */       calendar.set(field, nextValue);
/* 248 */       reset(calendar, lowerOrders);
/*     */     } 
/* 250 */     return nextValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reset(Calendar calendar, List<Integer> fields) {
/* 257 */     for (Iterator<Integer> iterator = fields.iterator(); iterator.hasNext(); ) { int field = ((Integer)iterator.next()).intValue();
/* 258 */       calendar.set(field, (field == 5) ? 1 : 0); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parse(String expression) throws IllegalArgumentException {
/* 269 */     String[] fields = StringUtils.tokenizeToStringArray(expression, " ");
/* 270 */     if (!areValidCronFields(fields))
/* 271 */       throw new IllegalArgumentException(String.format("Cron expression must consist of 6 fields (found %d in \"%s\")", new Object[] {
/* 272 */               Integer.valueOf(fields.length), expression
/*     */             })); 
/* 274 */     doParse(fields);
/*     */   }
/*     */   
/*     */   private void doParse(String[] fields) {
/* 278 */     setNumberHits(this.seconds, fields[0], 0, 60);
/* 279 */     setNumberHits(this.minutes, fields[1], 0, 60);
/* 280 */     setNumberHits(this.hours, fields[2], 0, 24);
/* 281 */     setDaysOfMonth(this.daysOfMonth, fields[3]);
/* 282 */     setMonths(this.months, fields[4]);
/* 283 */     setDays(this.daysOfWeek, replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);
/*     */     
/* 285 */     if (this.daysOfWeek.get(7)) {
/*     */       
/* 287 */       this.daysOfWeek.set(0);
/* 288 */       this.daysOfWeek.clear(7);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String replaceOrdinals(String value, String commaSeparatedList) {
/* 298 */     String[] list = StringUtils.commaDelimitedListToStringArray(commaSeparatedList);
/* 299 */     for (int i = 0; i < list.length; i++) {
/* 300 */       String item = list[i].toUpperCase();
/* 301 */       value = StringUtils.replace(value.toUpperCase(), item, "" + i);
/*     */     } 
/* 303 */     return value;
/*     */   }
/*     */   
/*     */   private void setDaysOfMonth(BitSet bits, String field) {
/* 307 */     int max = 31;
/*     */     
/* 309 */     setDays(bits, field, max + 1);
/*     */     
/* 311 */     bits.clear(0);
/*     */   }
/*     */   
/*     */   private void setDays(BitSet bits, String field, int max) {
/* 315 */     if (field.contains("?")) {
/* 316 */       field = "*";
/*     */     }
/* 318 */     setNumberHits(bits, field, 0, max);
/*     */   }
/*     */   
/*     */   private void setMonths(BitSet bits, String value) {
/* 322 */     int max = 12;
/* 323 */     value = replaceOrdinals(value, "FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC");
/* 324 */     BitSet months = new BitSet(13);
/*     */     
/* 326 */     setNumberHits(months, value, 1, max + 1);
/*     */     
/* 328 */     for (int i = 1; i <= max; i++) {
/* 329 */       if (months.get(i)) {
/* 330 */         bits.set(i - 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setNumberHits(BitSet bits, String value, int min, int max) {
/* 336 */     String[] fields = StringUtils.delimitedListToStringArray(value, ",");
/* 337 */     for (String field : fields) {
/* 338 */       if (!field.contains("/")) {
/*     */         
/* 340 */         int[] range = getRange(field, min, max);
/* 341 */         bits.set(range[0], range[1] + 1);
/*     */       } else {
/*     */         
/* 344 */         String[] split = StringUtils.delimitedListToStringArray(field, "/");
/* 345 */         if (split.length > 2) {
/* 346 */           throw new IllegalArgumentException("Incrementer has more than two fields: '" + field + "' in expression \"" + this.expression + "\"");
/*     */         }
/*     */         
/* 349 */         int[] range = getRange(split[0], min, max);
/* 350 */         if (!split[0].contains("-")) {
/* 351 */           range[1] = max - 1;
/*     */         }
/* 353 */         int delta = Integer.parseInt(split[1]);
/* 354 */         if (delta <= 0) {
/* 355 */           throw new IllegalArgumentException("Incrementer delta must be 1 or higher: '" + field + "' in expression \"" + this.expression + "\"");
/*     */         }
/*     */         int i;
/* 358 */         for (i = range[0]; i <= range[1]; i += delta) {
/* 359 */           bits.set(i);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int[] getRange(String field, int min, int max) {
/* 366 */     int[] result = new int[2];
/* 367 */     if (field.contains("*")) {
/* 368 */       result[0] = min;
/* 369 */       result[1] = max - 1;
/* 370 */       return result;
/*     */     } 
/* 372 */     if (!field.contains("-")) {
/* 373 */       result[1] = Integer.valueOf(field).intValue(); result[0] = Integer.valueOf(field).intValue();
/*     */     } else {
/*     */       
/* 376 */       String[] split = StringUtils.delimitedListToStringArray(field, "-");
/* 377 */       if (split.length > 2) {
/* 378 */         throw new IllegalArgumentException("Range has more than two fields: '" + field + "' in expression \"" + this.expression + "\"");
/*     */       }
/*     */       
/* 381 */       result[0] = Integer.valueOf(split[0]).intValue();
/* 382 */       result[1] = Integer.valueOf(split[1]).intValue();
/*     */     } 
/* 384 */     if (result[0] >= max || result[1] >= max) {
/* 385 */       throw new IllegalArgumentException("Range exceeds maximum (" + max + "): '" + field + "' in expression \"" + this.expression + "\"");
/*     */     }
/*     */     
/* 388 */     if (result[0] < min || result[1] < min) {
/* 389 */       throw new IllegalArgumentException("Range less than minimum (" + min + "): '" + field + "' in expression \"" + this.expression + "\"");
/*     */     }
/*     */     
/* 392 */     if (result[0] > result[1]) {
/* 393 */       throw new IllegalArgumentException("Invalid inverted range: '" + field + "' in expression \"" + this.expression + "\"");
/*     */     }
/*     */     
/* 396 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidExpression(String expression) {
/* 407 */     if (expression == null) {
/* 408 */       return false;
/*     */     }
/* 410 */     String[] fields = StringUtils.tokenizeToStringArray(expression, " ");
/* 411 */     if (!areValidCronFields(fields)) {
/* 412 */       return false;
/*     */     }
/*     */     try {
/* 415 */       new CronSequenceGenerator(expression, fields);
/* 416 */       return true;
/*     */     }
/* 418 */     catch (IllegalArgumentException ex) {
/* 419 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean areValidCronFields(String[] fields) {
/* 424 */     return (fields != null && fields.length == 6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 430 */     if (this == other) {
/* 431 */       return true;
/*     */     }
/* 433 */     if (!(other instanceof CronSequenceGenerator)) {
/* 434 */       return false;
/*     */     }
/* 436 */     CronSequenceGenerator otherCron = (CronSequenceGenerator)other;
/* 437 */     return (this.months.equals(otherCron.months) && this.daysOfMonth.equals(otherCron.daysOfMonth) && this.daysOfWeek
/* 438 */       .equals(otherCron.daysOfWeek) && this.hours.equals(otherCron.hours) && this.minutes
/* 439 */       .equals(otherCron.minutes) && this.seconds.equals(otherCron.seconds));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 444 */     return 17 * this.months.hashCode() + 29 * this.daysOfMonth.hashCode() + 37 * this.daysOfWeek.hashCode() + 41 * this.hours
/* 445 */       .hashCode() + 53 * this.minutes.hashCode() + 61 * this.seconds.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 450 */     return getClass().getSimpleName() + ": " + this.expression;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\support\CronSequenceGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */