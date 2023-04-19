/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.InvalidMimeTypeException;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.comparator.CompoundComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaType
/*     */   extends MimeType
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2069937152339670231L;
/*     */   public static final String ALL_VALUE = "*/*";
/* 258 */   public static final MediaType ALL = valueOf("*/*"); public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml";
/* 259 */   public static final MediaType APPLICATION_ATOM_XML = valueOf("application/atom+xml");
/* 260 */   public static final MediaType APPLICATION_FORM_URLENCODED = valueOf("application/x-www-form-urlencoded"); public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
/* 261 */   public static final MediaType APPLICATION_JSON = valueOf("application/json"); public static final String APPLICATION_JSON_VALUE = "application/json";
/* 262 */   public static final MediaType APPLICATION_JSON_UTF8 = valueOf("application/json;charset=UTF-8"); public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
/* 263 */   public static final MediaType APPLICATION_OCTET_STREAM = valueOf("application/octet-stream"); public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
/* 264 */   public static final MediaType APPLICATION_PDF = valueOf("application/pdf"); public static final String APPLICATION_PDF_VALUE = "application/pdf";
/* 265 */   public static final MediaType APPLICATION_RSS_XML = valueOf("application/rss+xml"); public static final String APPLICATION_RSS_XML_VALUE = "application/rss+xml";
/* 266 */   public static final MediaType APPLICATION_XHTML_XML = valueOf("application/xhtml+xml"); public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml";
/* 267 */   public static final MediaType APPLICATION_XML = valueOf("application/xml"); public static final String APPLICATION_XML_VALUE = "application/xml";
/* 268 */   public static final MediaType IMAGE_GIF = valueOf("image/gif"); public static final String IMAGE_GIF_VALUE = "image/gif";
/* 269 */   public static final MediaType IMAGE_JPEG = valueOf("image/jpeg"); public static final String IMAGE_JPEG_VALUE = "image/jpeg";
/* 270 */   public static final MediaType IMAGE_PNG = valueOf("image/png"); public static final String IMAGE_PNG_VALUE = "image/png";
/* 271 */   public static final MediaType MULTIPART_FORM_DATA = valueOf("multipart/form-data"); public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";
/* 272 */   public static final MediaType TEXT_EVENT_STREAM = valueOf("text/event-stream"); public static final String TEXT_EVENT_STREAM_VALUE = "text/event-stream";
/* 273 */   public static final MediaType TEXT_HTML = valueOf("text/html"); public static final String TEXT_HTML_VALUE = "text/html";
/* 274 */   public static final MediaType TEXT_MARKDOWN = valueOf("text/markdown"); public static final String TEXT_MARKDOWN_VALUE = "text/markdown";
/* 275 */   public static final MediaType TEXT_PLAIN = valueOf("text/plain"); public static final String TEXT_PLAIN_VALUE = "text/plain";
/* 276 */   public static final MediaType TEXT_XML = valueOf("text/xml");
/*     */ 
/*     */   
/*     */   public static final String TEXT_XML_VALUE = "text/xml";
/*     */ 
/*     */   
/*     */   private static final String PARAM_QUALITY_FACTOR = "q";
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(String type) {
/* 287 */     super(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(String type, String subtype) {
/* 298 */     super(type, subtype, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(String type, String subtype, Charset charset) {
/* 309 */     super(type, subtype, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(String type, String subtype, double qualityValue) {
/* 320 */     this(type, subtype, Collections.singletonMap("q", Double.toString(qualityValue)));
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
/*     */   public MediaType(MediaType other, Charset charset) {
/* 332 */     super(other, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(MediaType other, Map<String, String> parameters) {
/* 343 */     super(other.getType(), other.getSubtype(), parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(String type, String subtype, Map<String, String> parameters) {
/* 354 */     super(type, subtype, parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkParameters(String attribute, String value) {
/* 360 */     super.checkParameters(attribute, value);
/* 361 */     if ("q".equals(attribute)) {
/* 362 */       value = unquote(value);
/* 363 */       double d = Double.parseDouble(value);
/* 364 */       Assert.isTrue((d >= 0.0D && d <= 1.0D), "Invalid quality value \"" + value + "\": should be between 0.0 and 1.0");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getQualityValue() {
/* 375 */     String qualityFactor = getParameter("q");
/* 376 */     return (qualityFactor != null) ? Double.parseDouble(unquote(qualityFactor)) : 1.0D;
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
/*     */   public boolean includes(MediaType other) {
/* 391 */     return includes(other);
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
/*     */   public boolean isCompatibleWith(MediaType other) {
/* 406 */     return isCompatibleWith(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType copyQualityValue(MediaType mediaType) {
/* 415 */     if (!mediaType.getParameters().containsKey("q")) {
/* 416 */       return this;
/*     */     }
/* 418 */     Map<String, String> params = new LinkedHashMap<String, String>(getParameters());
/* 419 */     params.put("q", (String)mediaType.getParameters().get("q"));
/* 420 */     return new MediaType(this, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType removeQualityValue() {
/* 429 */     if (!getParameters().containsKey("q")) {
/* 430 */       return this;
/*     */     }
/* 432 */     Map<String, String> params = new LinkedHashMap<String, String>(getParameters());
/* 433 */     params.remove("q");
/* 434 */     return new MediaType(this, params);
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
/*     */   public static MediaType valueOf(String value) {
/* 447 */     return parseMediaType(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MediaType parseMediaType(String mediaType) {
/*     */     MimeType type;
/*     */     try {
/* 459 */       type = MimeTypeUtils.parseMimeType(mediaType);
/*     */     }
/* 461 */     catch (InvalidMimeTypeException ex) {
/* 462 */       throw new InvalidMediaTypeException(ex);
/*     */     } 
/*     */     try {
/* 465 */       return new MediaType(type.getType(), type.getSubtype(), type.getParameters());
/*     */     }
/* 467 */     catch (IllegalArgumentException ex) {
/* 468 */       throw new InvalidMediaTypeException(mediaType, ex.getMessage());
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
/*     */   public static List<MediaType> parseMediaTypes(String mediaTypes) {
/* 480 */     if (!StringUtils.hasLength(mediaTypes)) {
/* 481 */       return Collections.emptyList();
/*     */     }
/* 483 */     String[] tokens = StringUtils.tokenizeToStringArray(mediaTypes, ",");
/* 484 */     List<MediaType> result = new ArrayList<MediaType>(tokens.length);
/* 485 */     for (String token : tokens) {
/* 486 */       result.add(parseMediaType(token));
/*     */     }
/* 488 */     return result;
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
/*     */   public static List<MediaType> parseMediaTypes(List<String> mediaTypes) {
/* 501 */     if (CollectionUtils.isEmpty(mediaTypes)) {
/* 502 */       return Collections.emptyList();
/*     */     }
/* 504 */     if (mediaTypes.size() == 1) {
/* 505 */       return parseMediaTypes(mediaTypes.get(0));
/*     */     }
/*     */     
/* 508 */     List<MediaType> result = new ArrayList<MediaType>(8);
/* 509 */     for (String mediaType : mediaTypes) {
/* 510 */       result.addAll(parseMediaTypes(mediaType));
/*     */     }
/* 512 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Collection<MediaType> mediaTypes) {
/* 523 */     return MimeTypeUtils.toString(mediaTypes);
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
/*     */ 
/*     */   
/*     */   public static void sortBySpecificity(List<MediaType> mediaTypes) {
/* 554 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 555 */     if (mediaTypes.size() > 1) {
/* 556 */       Collections.sort(mediaTypes, SPECIFICITY_COMPARATOR);
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
/*     */   public static void sortByQualityValue(List<MediaType> mediaTypes) {
/* 581 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 582 */     if (mediaTypes.size() > 1) {
/* 583 */       Collections.sort(mediaTypes, QUALITY_VALUE_COMPARATOR);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortBySpecificityAndQuality(List<MediaType> mediaTypes) {
/* 594 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 595 */     if (mediaTypes.size() > 1) {
/* 596 */       Collections.sort(mediaTypes, (Comparator<? super MediaType>)new CompoundComparator(new Comparator[] { SPECIFICITY_COMPARATOR, QUALITY_VALUE_COMPARATOR }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 605 */   public static final Comparator<MediaType> QUALITY_VALUE_COMPARATOR = new Comparator<MediaType>()
/*     */     {
/*     */       public int compare(MediaType mediaType1, MediaType mediaType2)
/*     */       {
/* 609 */         double quality1 = mediaType1.getQualityValue();
/* 610 */         double quality2 = mediaType2.getQualityValue();
/* 611 */         int qualityComparison = Double.compare(quality2, quality1);
/* 612 */         if (qualityComparison != 0) {
/* 613 */           return qualityComparison;
/*     */         }
/* 615 */         if (mediaType1.isWildcardType() && !mediaType2.isWildcardType()) {
/* 616 */           return 1;
/*     */         }
/* 618 */         if (mediaType2.isWildcardType() && !mediaType1.isWildcardType()) {
/* 619 */           return -1;
/*     */         }
/* 621 */         if (!mediaType1.getType().equals(mediaType2.getType())) {
/* 622 */           return 0;
/*     */         }
/*     */         
/* 625 */         if (mediaType1.isWildcardSubtype() && !mediaType2.isWildcardSubtype()) {
/* 626 */           return 1;
/*     */         }
/* 628 */         if (mediaType2.isWildcardSubtype() && !mediaType1.isWildcardSubtype()) {
/* 629 */           return -1;
/*     */         }
/* 631 */         if (!mediaType1.getSubtype().equals(mediaType2.getSubtype())) {
/* 632 */           return 0;
/*     */         }
/*     */         
/* 635 */         int paramsSize1 = mediaType1.getParameters().size();
/* 636 */         int paramsSize2 = mediaType2.getParameters().size();
/*     */         
/* 638 */         return (paramsSize2 < paramsSize1) ? -1 : ((paramsSize2 == paramsSize1) ? 0 : 1);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 648 */   public static final Comparator<MediaType> SPECIFICITY_COMPARATOR = (Comparator<MediaType>)new MimeType.SpecificityComparator<MediaType>()
/*     */     {
/*     */       protected int compareParameters(MediaType mediaType1, MediaType mediaType2)
/*     */       {
/* 652 */         double quality1 = mediaType1.getQualityValue();
/* 653 */         double quality2 = mediaType2.getQualityValue();
/* 654 */         int qualityComparison = Double.compare(quality2, quality1);
/* 655 */         if (qualityComparison != 0) {
/* 656 */           return qualityComparison;
/*     */         }
/* 658 */         return super.compareParameters(mediaType1, mediaType2);
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\MediaType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */