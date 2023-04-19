/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MimeType
/*     */   implements Comparable<MimeType>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4085923477777865903L;
/*     */   protected static final String WILDCARD_TYPE = "*";
/*     */   private static final String PARAM_CHARSET = "charset";
/*     */   
/*     */   static {
/*  63 */     BitSet ctl = new BitSet(128);
/*  64 */     for (int i = 0; i <= 31; i++) {
/*  65 */       ctl.set(i);
/*     */     }
/*  67 */     ctl.set(127);
/*     */     
/*  69 */     BitSet separators = new BitSet(128);
/*  70 */     separators.set(40);
/*  71 */     separators.set(41);
/*  72 */     separators.set(60);
/*  73 */     separators.set(62);
/*  74 */     separators.set(64);
/*  75 */     separators.set(44);
/*  76 */     separators.set(59);
/*  77 */     separators.set(58);
/*  78 */     separators.set(92);
/*  79 */     separators.set(34);
/*  80 */     separators.set(47);
/*  81 */     separators.set(91);
/*  82 */     separators.set(93);
/*  83 */     separators.set(63);
/*  84 */     separators.set(61);
/*  85 */     separators.set(123);
/*  86 */     separators.set(125);
/*  87 */     separators.set(32);
/*  88 */     separators.set(9);
/*     */   }
/*  90 */   private static final BitSet TOKEN = new BitSet(128); static {
/*  91 */     TOKEN.set(0, 128);
/*  92 */     TOKEN.andNot(ctl);
/*  93 */     TOKEN.andNot(separators);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String type;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String subtype;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, String> parameters;
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type) {
/* 112 */     this(type, "*");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type, String subtype) {
/* 123 */     this(type, subtype, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type, String subtype, Charset charset) {
/* 134 */     this(type, subtype, Collections.singletonMap("charset", charset.name()));
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
/*     */   public MimeType(MimeType other, Charset charset) {
/* 146 */     this(other.getType(), other.getSubtype(), addCharsetParameter(charset, other.getParameters()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(MimeType other, Map<String, String> parameters) {
/* 157 */     this(other.getType(), other.getSubtype(), parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type, String subtype, Map<String, String> parameters) {
/* 168 */     Assert.hasLength(type, "'type' must not be empty");
/* 169 */     Assert.hasLength(subtype, "'subtype' must not be empty");
/* 170 */     checkToken(type);
/* 171 */     checkToken(subtype);
/* 172 */     this.type = type.toLowerCase(Locale.ENGLISH);
/* 173 */     this.subtype = subtype.toLowerCase(Locale.ENGLISH);
/* 174 */     if (!CollectionUtils.isEmpty(parameters)) {
/* 175 */       Map<String, String> map = new LinkedCaseInsensitiveMap<String>(parameters.size(), Locale.ENGLISH);
/* 176 */       for (Map.Entry<String, String> entry : parameters.entrySet()) {
/* 177 */         String attribute = entry.getKey();
/* 178 */         String value = entry.getValue();
/* 179 */         checkParameters(attribute, value);
/* 180 */         map.put(attribute, value);
/*     */       } 
/* 182 */       this.parameters = Collections.unmodifiableMap(map);
/*     */     } else {
/*     */       
/* 185 */       this.parameters = Collections.emptyMap();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkToken(String token) {
/* 196 */     for (int i = 0; i < token.length(); i++) {
/* 197 */       char ch = token.charAt(i);
/* 198 */       if (!TOKEN.get(ch)) {
/* 199 */         throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + token + "\"");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void checkParameters(String attribute, String value) {
/* 205 */     Assert.hasLength(attribute, "'attribute' must not be empty");
/* 206 */     Assert.hasLength(value, "'value' must not be empty");
/* 207 */     checkToken(attribute);
/* 208 */     if ("charset".equals(attribute)) {
/* 209 */       value = unquote(value);
/* 210 */       Charset.forName(value);
/*     */     }
/* 212 */     else if (!isQuotedString(value)) {
/* 213 */       checkToken(value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isQuotedString(String s) {
/* 218 */     if (s.length() < 2) {
/* 219 */       return false;
/*     */     }
/*     */     
/* 222 */     return ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String unquote(String s) {
/* 227 */     if (s == null) {
/* 228 */       return null;
/*     */     }
/* 230 */     return isQuotedString(s) ? s.substring(1, s.length() - 1) : s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWildcardType() {
/* 238 */     return "*".equals(getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWildcardSubtype() {
/* 248 */     return ("*".equals(getSubtype()) || getSubtype().startsWith("*+"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConcrete() {
/* 257 */     return (!isWildcardType() && !isWildcardSubtype());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 264 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubtype() {
/* 271 */     return this.subtype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 280 */     String charset = getParameter("charset");
/* 281 */     return (charset != null) ? Charset.forName(unquote(charset)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Charset getCharSet() {
/* 292 */     return getCharset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/* 301 */     return this.parameters.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getParameters() {
/* 309 */     return this.parameters;
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
/*     */   public boolean includes(MimeType other) {
/* 322 */     if (other == null) {
/* 323 */       return false;
/*     */     }
/* 325 */     if (isWildcardType())
/*     */     {
/* 327 */       return true;
/*     */     }
/* 329 */     if (getType().equals(other.getType())) {
/* 330 */       if (getSubtype().equals(other.getSubtype())) {
/* 331 */         return true;
/*     */       }
/* 333 */       if (isWildcardSubtype()) {
/*     */         
/* 335 */         int thisPlusIdx = getSubtype().lastIndexOf('+');
/* 336 */         if (thisPlusIdx == -1) {
/* 337 */           return true;
/*     */         }
/*     */ 
/*     */         
/* 341 */         int otherPlusIdx = other.getSubtype().indexOf('+');
/* 342 */         if (otherPlusIdx != -1) {
/* 343 */           String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
/* 344 */           String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
/* 345 */           String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
/* 346 */           if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && "*".equals(thisSubtypeNoSuffix)) {
/* 347 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 353 */     return false;
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
/*     */   public boolean isCompatibleWith(MimeType other) {
/* 366 */     if (other == null) {
/* 367 */       return false;
/*     */     }
/* 369 */     if (isWildcardType() || other.isWildcardType()) {
/* 370 */       return true;
/*     */     }
/* 372 */     if (getType().equals(other.getType())) {
/* 373 */       if (getSubtype().equals(other.getSubtype())) {
/* 374 */         return true;
/*     */       }
/*     */       
/* 377 */       if (isWildcardSubtype() || other.isWildcardSubtype()) {
/* 378 */         int thisPlusIdx = getSubtype().indexOf('+');
/* 379 */         int otherPlusIdx = other.getSubtype().indexOf('+');
/* 380 */         if (thisPlusIdx == -1 && otherPlusIdx == -1) {
/* 381 */           return true;
/*     */         }
/* 383 */         if (thisPlusIdx != -1 && otherPlusIdx != -1) {
/* 384 */           String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
/* 385 */           String otherSubtypeNoSuffix = other.getSubtype().substring(0, otherPlusIdx);
/* 386 */           String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
/* 387 */           String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
/* 388 */           if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && ("*"
/* 389 */             .equals(thisSubtypeNoSuffix) || "*".equals(otherSubtypeNoSuffix))) {
/* 390 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 395 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 401 */     if (this == other) {
/* 402 */       return true;
/*     */     }
/* 404 */     if (!(other instanceof MimeType)) {
/* 405 */       return false;
/*     */     }
/* 407 */     MimeType otherType = (MimeType)other;
/* 408 */     return (this.type.equalsIgnoreCase(otherType.type) && this.subtype
/* 409 */       .equalsIgnoreCase(otherType.subtype) && 
/* 410 */       parametersAreEqual(otherType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean parametersAreEqual(MimeType other) {
/* 420 */     if (this.parameters.size() != other.parameters.size()) {
/* 421 */       return false;
/*     */     }
/*     */     
/* 424 */     for (String key : this.parameters.keySet()) {
/* 425 */       if (!other.parameters.containsKey(key)) {
/* 426 */         return false;
/*     */       }
/* 428 */       if ("charset".equals(key)) {
/* 429 */         if (!ObjectUtils.nullSafeEquals(getCharset(), other.getCharset()))
/* 430 */           return false; 
/*     */         continue;
/*     */       } 
/* 433 */       if (!ObjectUtils.nullSafeEquals(this.parameters.get(key), other.parameters.get(key))) {
/* 434 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 438 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 443 */     int result = this.type.hashCode();
/* 444 */     result = 31 * result + this.subtype.hashCode();
/* 445 */     result = 31 * result + this.parameters.hashCode();
/* 446 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 451 */     StringBuilder builder = new StringBuilder();
/* 452 */     appendTo(builder);
/* 453 */     return builder.toString();
/*     */   }
/*     */   
/*     */   protected void appendTo(StringBuilder builder) {
/* 457 */     builder.append(this.type);
/* 458 */     builder.append('/');
/* 459 */     builder.append(this.subtype);
/* 460 */     appendTo(this.parameters, builder);
/*     */   }
/*     */   
/*     */   private void appendTo(Map<String, String> map, StringBuilder builder) {
/* 464 */     for (Map.Entry<String, String> entry : map.entrySet()) {
/* 465 */       builder.append(';');
/* 466 */       builder.append(entry.getKey());
/* 467 */       builder.append('=');
/* 468 */       builder.append(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(MimeType other) {
/* 479 */     int comp = getType().compareToIgnoreCase(other.getType());
/* 480 */     if (comp != 0) {
/* 481 */       return comp;
/*     */     }
/* 483 */     comp = getSubtype().compareToIgnoreCase(other.getSubtype());
/* 484 */     if (comp != 0) {
/* 485 */       return comp;
/*     */     }
/* 487 */     comp = getParameters().size() - other.getParameters().size();
/* 488 */     if (comp != 0) {
/* 489 */       return comp;
/*     */     }
/*     */     
/* 492 */     TreeSet<String> thisAttributes = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
/* 493 */     thisAttributes.addAll(getParameters().keySet());
/* 494 */     TreeSet<String> otherAttributes = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
/* 495 */     otherAttributes.addAll(other.getParameters().keySet());
/* 496 */     Iterator<String> thisAttributesIterator = thisAttributes.iterator();
/* 497 */     Iterator<String> otherAttributesIterator = otherAttributes.iterator();
/*     */     
/* 499 */     while (thisAttributesIterator.hasNext()) {
/* 500 */       String thisAttribute = thisAttributesIterator.next();
/* 501 */       String otherAttribute = otherAttributesIterator.next();
/* 502 */       comp = thisAttribute.compareToIgnoreCase(otherAttribute);
/* 503 */       if (comp != 0) {
/* 504 */         return comp;
/*     */       }
/* 506 */       if ("charset".equals(thisAttribute)) {
/* 507 */         Charset thisCharset = getCharset();
/* 508 */         Charset otherCharset = other.getCharset();
/* 509 */         if (thisCharset != otherCharset) {
/* 510 */           if (thisCharset == null) {
/* 511 */             return -1;
/*     */           }
/* 513 */           if (otherCharset == null) {
/* 514 */             return 1;
/*     */           }
/* 516 */           comp = thisCharset.compareTo(otherCharset);
/* 517 */           if (comp != 0) {
/* 518 */             return comp;
/*     */           }
/*     */         } 
/*     */         continue;
/*     */       } 
/* 523 */       String thisValue = getParameters().get(thisAttribute);
/* 524 */       String otherValue = other.getParameters().get(otherAttribute);
/* 525 */       if (otherValue == null) {
/* 526 */         otherValue = "";
/*     */       }
/* 528 */       comp = thisValue.compareTo(otherValue);
/* 529 */       if (comp != 0) {
/* 530 */         return comp;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 535 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MimeType valueOf(String value) {
/* 546 */     return MimeTypeUtils.parseMimeType(value);
/*     */   }
/*     */   
/*     */   private static Map<String, String> addCharsetParameter(Charset charset, Map<String, String> parameters) {
/* 550 */     Map<String, String> map = new LinkedHashMap<String, String>(parameters);
/* 551 */     map.put("charset", charset.name());
/* 552 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SpecificityComparator<T extends MimeType>
/*     */     implements Comparator<T>
/*     */   {
/*     */     public int compare(T mimeType1, T mimeType2) {
/* 560 */       if (mimeType1.isWildcardType() && !mimeType2.isWildcardType()) {
/* 561 */         return 1;
/*     */       }
/* 563 */       if (mimeType2.isWildcardType() && !mimeType1.isWildcardType()) {
/* 564 */         return -1;
/*     */       }
/* 566 */       if (!mimeType1.getType().equals(mimeType2.getType())) {
/* 567 */         return 0;
/*     */       }
/*     */       
/* 570 */       if (mimeType1.isWildcardSubtype() && !mimeType2.isWildcardSubtype()) {
/* 571 */         return 1;
/*     */       }
/* 573 */       if (mimeType2.isWildcardSubtype() && !mimeType1.isWildcardSubtype()) {
/* 574 */         return -1;
/*     */       }
/* 576 */       if (!mimeType1.getSubtype().equals(mimeType2.getSubtype())) {
/* 577 */         return 0;
/*     */       }
/*     */       
/* 580 */       return compareParameters(mimeType1, mimeType2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected int compareParameters(T mimeType1, T mimeType2) {
/* 586 */       int paramsSize1 = mimeType1.getParameters().size();
/* 587 */       int paramsSize2 = mimeType2.getParameters().size();
/* 588 */       return (paramsSize2 < paramsSize1) ? -1 : ((paramsSize2 == paramsSize1) ? 0 : 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\MimeType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */