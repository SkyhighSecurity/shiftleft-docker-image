/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StdDateFormat
/*     */   extends DateFormat
/*     */ {
/*     */   protected static final String PATTERN_PLAIN_STR = "\\d\\d\\d\\d[-]\\d\\d[-]\\d\\d";
/*  38 */   protected static final Pattern PATTERN_PLAIN = Pattern.compile("\\d\\d\\d\\d[-]\\d\\d[-]\\d\\d"); protected static final Pattern PATTERN_ISO8601;
/*     */   public static final String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
/*     */   
/*     */   static {
/*  42 */     Pattern p = null;
/*     */     try {
/*  44 */       p = Pattern.compile("\\d\\d\\d\\d[-]\\d\\d[-]\\d\\d[T]\\d\\d[:]\\d\\d(?:[:]\\d\\d)?(\\.\\d+)?(Z|[+-]\\d\\d(?:[:]?\\d\\d)?)?");
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  49 */     catch (Throwable t) {
/*  50 */       throw new RuntimeException(t);
/*     */     } 
/*  52 */     PATTERN_ISO8601 = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String DATE_FORMAT_STR_PLAIN = "yyyy-MM-dd";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String DATE_FORMAT_STR_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   protected static final String[] ALL_FORMATS = new String[] { "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   protected static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");
/*     */ 
/*     */   
/*  93 */   protected static final Locale DEFAULT_LOCALE = Locale.US;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected static final DateFormat DATE_FORMAT_RFC1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", DEFAULT_LOCALE); static {
/* 107 */     DATE_FORMAT_RFC1123.setTimeZone(DEFAULT_TIMEZONE);
/* 108 */   } protected static final DateFormat DATE_FORMAT_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", DEFAULT_LOCALE); static {
/* 109 */     DATE_FORMAT_ISO8601.setTimeZone(DEFAULT_TIMEZONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static final StdDateFormat instance = new StdDateFormat();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   protected static final Calendar CALENDAR = new GregorianCalendar(DEFAULT_TIMEZONE, DEFAULT_LOCALE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected transient TimeZone _timezone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Locale _locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Boolean _lenient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient Calendar _calendar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient DateFormat _formatRFC1123;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _tzSerializedWithColon = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StdDateFormat() {
/* 169 */     this._locale = DEFAULT_LOCALE;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public StdDateFormat(TimeZone tz, Locale loc) {
/* 174 */     this._timezone = tz;
/* 175 */     this._locale = loc;
/*     */   }
/*     */   
/*     */   protected StdDateFormat(TimeZone tz, Locale loc, Boolean lenient) {
/* 179 */     this(tz, loc, lenient, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StdDateFormat(TimeZone tz, Locale loc, Boolean lenient, boolean formatTzOffsetWithColon) {
/* 187 */     this._timezone = tz;
/* 188 */     this._locale = loc;
/* 189 */     this._lenient = lenient;
/* 190 */     this._tzSerializedWithColon = formatTzOffsetWithColon;
/*     */   }
/*     */   
/*     */   public static TimeZone getDefaultTimeZone() {
/* 194 */     return DEFAULT_TIMEZONE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StdDateFormat withTimeZone(TimeZone tz) {
/* 202 */     if (tz == null) {
/* 203 */       tz = DEFAULT_TIMEZONE;
/*     */     }
/* 205 */     if (tz == this._timezone || tz.equals(this._timezone)) {
/* 206 */       return this;
/*     */     }
/* 208 */     return new StdDateFormat(tz, this._locale, this._lenient, this._tzSerializedWithColon);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StdDateFormat withLocale(Locale loc) {
/* 218 */     if (loc.equals(this._locale)) {
/* 219 */       return this;
/*     */     }
/* 221 */     return new StdDateFormat(this._timezone, loc, this._lenient, this._tzSerializedWithColon);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StdDateFormat withLenient(Boolean b) {
/* 232 */     if (_equals(b, this._lenient)) {
/* 233 */       return this;
/*     */     }
/* 235 */     return new StdDateFormat(this._timezone, this._locale, b, this._tzSerializedWithColon);
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
/*     */   public StdDateFormat withColonInTimeZone(boolean b) {
/* 252 */     if (this._tzSerializedWithColon == b) {
/* 253 */       return this;
/*     */     }
/* 255 */     return new StdDateFormat(this._timezone, this._locale, this._lenient, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StdDateFormat clone() {
/* 262 */     return new StdDateFormat(this._timezone, this._locale, this._lenient, this._tzSerializedWithColon);
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
/*     */   @Deprecated
/*     */   public static DateFormat getISO8601Format(TimeZone tz, Locale loc) {
/* 276 */     return _cloneFormat(DATE_FORMAT_ISO8601, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", tz, loc, null);
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
/*     */   @Deprecated
/*     */   public static DateFormat getRFC1123Format(TimeZone tz, Locale loc) {
/* 290 */     return _cloneFormat(DATE_FORMAT_RFC1123, "EEE, dd MMM yyyy HH:mm:ss zzz", tz, loc, null);
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
/*     */   public TimeZone getTimeZone() {
/* 302 */     return this._timezone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone tz) {
/* 311 */     if (!tz.equals(this._timezone)) {
/* 312 */       _clearFormats();
/* 313 */       this._timezone = tz;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLenient(boolean enabled) {
/* 324 */     Boolean newValue = Boolean.valueOf(enabled);
/* 325 */     if (!_equals(newValue, this._lenient)) {
/* 326 */       this._lenient = newValue;
/*     */       
/* 328 */       _clearFormats();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLenient() {
/* 335 */     return (this._lenient == null || this._lenient.booleanValue());
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
/*     */   public boolean isColonIncludedInTimeZone() {
/* 353 */     return this._tzSerializedWithColon;
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
/*     */   public Date parse(String dateStr) throws ParseException {
/* 365 */     dateStr = dateStr.trim();
/* 366 */     ParsePosition pos = new ParsePosition(0);
/* 367 */     Date dt = _parseDate(dateStr, pos);
/* 368 */     if (dt != null) {
/* 369 */       return dt;
/*     */     }
/* 371 */     StringBuilder sb = new StringBuilder();
/* 372 */     for (String f : ALL_FORMATS) {
/* 373 */       if (sb.length() > 0) {
/* 374 */         sb.append("\", \"");
/*     */       } else {
/* 376 */         sb.append('"');
/*     */       } 
/* 378 */       sb.append(f);
/*     */     } 
/* 380 */     sb.append('"');
/* 381 */     throw new ParseException(
/* 382 */         String.format("Cannot parse date \"%s\": not compatible with any of standard forms (%s)", new Object[] {
/* 383 */             dateStr, sb.toString() }), pos.getErrorIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String dateStr, ParsePosition pos) {
/*     */     try {
/* 391 */       return _parseDate(dateStr, pos);
/* 392 */     } catch (ParseException parseException) {
/*     */ 
/*     */       
/* 395 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Date _parseDate(String dateStr, ParsePosition pos) throws ParseException {
/* 400 */     if (looksLikeISO8601(dateStr)) {
/* 401 */       return parseAsISO8601(dateStr, pos);
/*     */     }
/*     */     
/* 404 */     int i = dateStr.length();
/* 405 */     while (--i >= 0) {
/* 406 */       char ch = dateStr.charAt(i);
/* 407 */       if (ch < '0' || ch > '9')
/*     */       {
/* 409 */         if (i > 0 || ch != '-') {
/*     */           break;
/*     */         }
/*     */       }
/*     */     } 
/* 414 */     if (i < 0 && (dateStr
/*     */       
/* 416 */       .charAt(0) == '-' || NumberInput.inLongRange(dateStr, false))) {
/* 417 */       return _parseDateFromLong(dateStr, pos);
/*     */     }
/*     */     
/* 420 */     return parseAsRFC1123(dateStr, pos);
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
/*     */   public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
/* 433 */     TimeZone tz = this._timezone;
/* 434 */     if (tz == null) {
/* 435 */       tz = DEFAULT_TIMEZONE;
/*     */     }
/* 437 */     _format(tz, this._locale, date, toAppendTo);
/* 438 */     return toAppendTo;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _format(TimeZone tz, Locale loc, Date date, StringBuffer buffer) {
/* 444 */     Calendar cal = _getCalendar(tz);
/* 445 */     cal.setTime(date);
/*     */     
/* 447 */     int year = cal.get(1);
/*     */ 
/*     */     
/* 450 */     if (cal.get(0) == 0) {
/* 451 */       _formatBCEYear(buffer, year);
/*     */     } else {
/* 453 */       if (year > 9999)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 459 */         buffer.append('+');
/*     */       }
/* 461 */       pad4(buffer, year);
/*     */     } 
/* 463 */     buffer.append('-');
/* 464 */     pad2(buffer, cal.get(2) + 1);
/* 465 */     buffer.append('-');
/* 466 */     pad2(buffer, cal.get(5));
/* 467 */     buffer.append('T');
/* 468 */     pad2(buffer, cal.get(11));
/* 469 */     buffer.append(':');
/* 470 */     pad2(buffer, cal.get(12));
/* 471 */     buffer.append(':');
/* 472 */     pad2(buffer, cal.get(13));
/* 473 */     buffer.append('.');
/* 474 */     pad3(buffer, cal.get(14));
/*     */     
/* 476 */     int offset = tz.getOffset(cal.getTimeInMillis());
/* 477 */     if (offset != 0) {
/* 478 */       int hours = Math.abs(offset / 60000 / 60);
/* 479 */       int minutes = Math.abs(offset / 60000 % 60);
/* 480 */       buffer.append((offset < 0) ? 45 : 43);
/* 481 */       pad2(buffer, hours);
/* 482 */       if (this._tzSerializedWithColon) {
/* 483 */         buffer.append(':');
/*     */       }
/* 485 */       pad2(buffer, minutes);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 490 */     else if (this._tzSerializedWithColon) {
/* 491 */       buffer.append("+00:00");
/*     */     } else {
/*     */       
/* 494 */       buffer.append("+0000");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _formatBCEYear(StringBuffer buffer, int bceYearNoSign) {
/* 502 */     if (bceYearNoSign == 1) {
/* 503 */       buffer.append("+0000");
/*     */       return;
/*     */     } 
/* 506 */     int isoYear = bceYearNoSign - 1;
/* 507 */     buffer.append('-');
/*     */ 
/*     */ 
/*     */     
/* 511 */     pad4(buffer, isoYear);
/*     */   }
/*     */   
/*     */   private static void pad2(StringBuffer buffer, int value) {
/* 515 */     int tens = value / 10;
/* 516 */     if (tens == 0) {
/* 517 */       buffer.append('0');
/*     */     } else {
/* 519 */       buffer.append((char)(48 + tens));
/* 520 */       value -= 10 * tens;
/*     */     } 
/* 522 */     buffer.append((char)(48 + value));
/*     */   }
/*     */   
/*     */   private static void pad3(StringBuffer buffer, int value) {
/* 526 */     int h = value / 100;
/* 527 */     if (h == 0) {
/* 528 */       buffer.append('0');
/*     */     } else {
/* 530 */       buffer.append((char)(48 + h));
/* 531 */       value -= h * 100;
/*     */     } 
/* 533 */     pad2(buffer, value);
/*     */   }
/*     */   
/*     */   private static void pad4(StringBuffer buffer, int value) {
/* 537 */     int h = value / 100;
/* 538 */     if (h == 0) {
/* 539 */       buffer.append('0').append('0');
/*     */     } else {
/* 541 */       if (h > 99) {
/* 542 */         buffer.append(h);
/*     */       } else {
/* 544 */         pad2(buffer, h);
/*     */       } 
/* 546 */       value -= 100 * h;
/*     */     } 
/* 548 */     pad2(buffer, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 559 */     return String.format("DateFormat %s: (timezone: %s, locale: %s, lenient: %s)", new Object[] {
/* 560 */           getClass().getName(), this._timezone, this._locale, this._lenient });
/*     */   }
/*     */   
/*     */   public String toPattern() {
/* 564 */     StringBuilder sb = new StringBuilder(100);
/* 565 */     sb.append("[one of: '")
/* 566 */       .append("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
/* 567 */       .append("', '")
/* 568 */       .append("EEE, dd MMM yyyy HH:mm:ss zzz")
/* 569 */       .append("' (");
/*     */     
/* 571 */     sb.append(Boolean.FALSE.equals(this._lenient) ? "strict" : "lenient")
/*     */       
/* 573 */       .append(")]");
/* 574 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 579 */     return (o == this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 584 */     return System.identityHashCode(this);
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
/*     */   protected boolean looksLikeISO8601(String dateStr) {
/* 599 */     if (dateStr.length() >= 7 && 
/* 600 */       Character.isDigit(dateStr.charAt(0)) && 
/* 601 */       Character.isDigit(dateStr.charAt(3)) && dateStr
/* 602 */       .charAt(4) == '-' && 
/* 603 */       Character.isDigit(dateStr.charAt(5)))
/*     */     {
/* 605 */       return true;
/*     */     }
/* 607 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private Date _parseDateFromLong(String longStr, ParsePosition pos) throws ParseException {
/*     */     long ts;
/*     */     try {
/* 614 */       ts = NumberInput.parseLong(longStr);
/* 615 */     } catch (NumberFormatException e) {
/* 616 */       throw new ParseException(String.format("Timestamp value %s out of 64-bit value range", new Object[] { longStr }), pos
/*     */           
/* 618 */           .getErrorIndex());
/*     */     } 
/* 620 */     return new Date(ts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Date parseAsISO8601(String dateStr, ParsePosition pos) throws ParseException {
/*     */     try {
/* 627 */       return _parseAsISO8601(dateStr, pos);
/* 628 */     } catch (IllegalArgumentException e) {
/* 629 */       throw new ParseException(String.format("Cannot parse date \"%s\", problem: %s", new Object[] { dateStr, e
/* 630 */               .getMessage()
/* 631 */             }), pos.getErrorIndex());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Date _parseAsISO8601(String dateStr, ParsePosition bogus) throws IllegalArgumentException, ParseException {
/*     */     String formatStr;
/* 638 */     int totalLen = dateStr.length();
/*     */     
/* 640 */     TimeZone tz = DEFAULT_TIMEZONE;
/* 641 */     if (this._timezone != null && 'Z' != dateStr.charAt(totalLen - 1)) {
/* 642 */       tz = this._timezone;
/*     */     }
/* 644 */     Calendar cal = _getCalendar(tz);
/* 645 */     cal.clear();
/*     */     
/* 647 */     if (totalLen <= 10) {
/* 648 */       Matcher m = PATTERN_PLAIN.matcher(dateStr);
/* 649 */       if (m.matches()) {
/* 650 */         int year = _parse4D(dateStr, 0);
/* 651 */         int month = _parse2D(dateStr, 5) - 1;
/* 652 */         int day = _parse2D(dateStr, 8);
/*     */         
/* 654 */         cal.set(year, month, day, 0, 0, 0);
/* 655 */         cal.set(14, 0);
/* 656 */         return cal.getTime();
/*     */       } 
/* 658 */       formatStr = "yyyy-MM-dd";
/*     */     } else {
/* 660 */       Matcher m = PATTERN_ISO8601.matcher(dateStr);
/* 661 */       if (m.matches()) {
/*     */ 
/*     */         
/* 664 */         int seconds, start = m.start(2);
/* 665 */         int end = m.end(2);
/* 666 */         int len = end - start;
/* 667 */         if (len > 1) {
/*     */           
/* 669 */           int offsetSecs = _parse2D(dateStr, start + 1) * 3600;
/* 670 */           if (len >= 5) {
/* 671 */             offsetSecs += _parse2D(dateStr, end - 2) * 60;
/*     */           }
/* 673 */           if (dateStr.charAt(start) == '-') {
/* 674 */             offsetSecs *= -1000;
/*     */           } else {
/* 676 */             offsetSecs *= 1000;
/*     */           } 
/* 678 */           cal.set(15, offsetSecs);
/*     */           
/* 680 */           cal.set(16, 0);
/*     */         } 
/*     */         
/* 683 */         int year = _parse4D(dateStr, 0);
/* 684 */         int month = _parse2D(dateStr, 5) - 1;
/* 685 */         int day = _parse2D(dateStr, 8);
/*     */ 
/*     */         
/* 688 */         int hour = _parse2D(dateStr, 11);
/* 689 */         int minute = _parse2D(dateStr, 14);
/*     */ 
/*     */ 
/*     */         
/* 693 */         if (totalLen > 16 && dateStr.charAt(16) == ':') {
/* 694 */           seconds = _parse2D(dateStr, 17);
/*     */         } else {
/* 696 */           seconds = 0;
/*     */         } 
/* 698 */         cal.set(year, month, day, hour, minute, seconds);
/*     */ 
/*     */         
/* 701 */         start = m.start(1) + 1;
/* 702 */         end = m.end(1);
/* 703 */         int msecs = 0;
/* 704 */         if (start >= end) {
/* 705 */           cal.set(14, 0);
/*     */         } else {
/*     */           
/* 708 */           msecs = 0;
/* 709 */           int fractLen = end - start;
/* 710 */           switch (fractLen) {
/*     */             
/*     */             default:
/* 713 */               if (fractLen > 9) {
/* 714 */                 throw new ParseException(String.format("Cannot parse date \"%s\": invalid fractional seconds '%s'; can use at most 9 digits", new Object[] { dateStr, m
/*     */                         
/* 716 */                         .group(1).substring(1) }), start);
/*     */               }
/*     */ 
/*     */             
/*     */             case 3:
/* 721 */               msecs += dateStr.charAt(start + 2) - 48;
/*     */             case 2:
/* 723 */               msecs += 10 * (dateStr.charAt(start + 1) - 48);
/*     */             case 1:
/* 725 */               msecs += 100 * (dateStr.charAt(start) - 48);
/*     */               break;
/*     */             case 0:
/*     */               break;
/*     */           } 
/* 730 */           cal.set(14, msecs);
/*     */         } 
/* 732 */         return cal.getTime();
/*     */       } 
/* 734 */       formatStr = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
/*     */     } 
/*     */     
/* 737 */     throw new ParseException(
/* 738 */         String.format("Cannot parse date \"%s\": while it seems to fit format '%s', parsing fails (leniency? %s)", new Object[] { dateStr, formatStr, this._lenient }), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int _parse4D(String str, int index) {
/* 746 */     return 1000 * (str.charAt(index) - 48) + 100 * (str
/* 747 */       .charAt(index + 1) - 48) + 10 * (str
/* 748 */       .charAt(index + 2) - 48) + str
/* 749 */       .charAt(index + 3) - 48;
/*     */   }
/*     */   
/*     */   private static int _parse2D(String str, int index) {
/* 753 */     return 10 * (str.charAt(index) - 48) + str
/* 754 */       .charAt(index + 1) - 48;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Date parseAsRFC1123(String dateStr, ParsePosition pos) {
/* 759 */     if (this._formatRFC1123 == null) {
/* 760 */       this._formatRFC1123 = _cloneFormat(DATE_FORMAT_RFC1123, "EEE, dd MMM yyyy HH:mm:ss zzz", this._timezone, this._locale, this._lenient);
/*     */     }
/*     */     
/* 763 */     return this._formatRFC1123.parse(dateStr, pos);
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
/*     */   private static final DateFormat _cloneFormat(DateFormat df, String format, TimeZone tz, Locale loc, Boolean lenient) {
/* 775 */     if (!loc.equals(DEFAULT_LOCALE)) {
/* 776 */       df = new SimpleDateFormat(format, loc);
/* 777 */       df.setTimeZone((tz == null) ? DEFAULT_TIMEZONE : tz);
/*     */     } else {
/* 779 */       df = (DateFormat)df.clone();
/* 780 */       if (tz != null) {
/* 781 */         df.setTimeZone(tz);
/*     */       }
/*     */     } 
/* 784 */     if (lenient != null) {
/* 785 */       df.setLenient(lenient.booleanValue());
/*     */     }
/* 787 */     return df;
/*     */   }
/*     */   
/*     */   protected void _clearFormats() {
/* 791 */     this._formatRFC1123 = null;
/*     */   }
/*     */   
/*     */   protected Calendar _getCalendar(TimeZone tz) {
/* 795 */     Calendar cal = this._calendar;
/* 796 */     if (cal == null) {
/* 797 */       this._calendar = cal = (Calendar)CALENDAR.clone();
/*     */     }
/* 799 */     if (!cal.getTimeZone().equals(tz)) {
/* 800 */       cal.setTimeZone(tz);
/*     */     }
/* 802 */     cal.setLenient(isLenient());
/* 803 */     return cal;
/*     */   }
/*     */   
/*     */   protected static <T> boolean _equals(T value1, T value2) {
/* 807 */     if (value1 == value2) {
/* 808 */       return true;
/*     */     }
/* 810 */     return (value1 != null && value1.equals(value2));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databin\\util\StdDateFormat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */