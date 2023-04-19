/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourceRegion;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public abstract class HttpRange
/*     */ {
/*     */   private static final int MAX_RANGES = 100;
/*     */   private static final String BYTE_RANGE_PREFIX = "bytes=";
/*     */   
/*     */   public ResourceRegion toResourceRegion(Resource resource) {
/*  62 */     Assert.isTrue((resource.getClass() != InputStreamResource.class), "Cannot convert an InputStreamResource to a ResourceRegion");
/*     */     
/*  64 */     long contentLength = getLengthFor(resource);
/*  65 */     Assert.isTrue((contentLength > 0L), "Resource content length should be > 0");
/*  66 */     long start = getRangeStart(contentLength);
/*  67 */     long end = getRangeEnd(contentLength);
/*  68 */     return new ResourceRegion(resource, start, end - start + 1L);
/*     */   }
/*     */   
/*     */   private static long getLengthFor(Resource resource) {
/*     */     long contentLength;
/*     */     try {
/*  74 */       contentLength = resource.contentLength();
/*  75 */       Assert.isTrue((contentLength > 0L), "Resource content length should be > 0");
/*     */     }
/*  77 */     catch (IOException ex) {
/*  78 */       throw new IllegalArgumentException("Failed to obtain Resource content length", ex);
/*     */     } 
/*  80 */     return contentLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract long getRangeStart(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract long getRangeEnd(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpRange createByteRange(long firstBytePos) {
/* 105 */     return new ByteRange(firstBytePos, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpRange createByteRange(long firstBytePos, long lastBytePos) {
/* 116 */     return new ByteRange(firstBytePos, Long.valueOf(lastBytePos));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpRange createSuffixRange(long suffixLength) {
/* 126 */     return new SuffixByteRange(suffixLength);
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
/*     */   public static List<HttpRange> parseRanges(String ranges) {
/* 138 */     if (!StringUtils.hasLength(ranges)) {
/* 139 */       return Collections.emptyList();
/*     */     }
/* 141 */     if (!ranges.startsWith("bytes=")) {
/* 142 */       throw new IllegalArgumentException("Range '" + ranges + "' does not start with 'bytes='");
/*     */     }
/* 144 */     ranges = ranges.substring("bytes=".length());
/*     */     
/* 146 */     String[] tokens = StringUtils.tokenizeToStringArray(ranges, ",");
/* 147 */     Assert.isTrue((tokens.length <= 100), "Too many ranges " + tokens.length);
/* 148 */     List<HttpRange> result = new ArrayList<HttpRange>(tokens.length);
/* 149 */     for (String token : tokens) {
/* 150 */       result.add(parseRange(token));
/*     */     }
/* 152 */     return result;
/*     */   }
/*     */   
/*     */   private static HttpRange parseRange(String range) {
/* 156 */     Assert.hasLength(range, "Range String must not be empty");
/* 157 */     int dashIdx = range.indexOf('-');
/* 158 */     if (dashIdx > 0) {
/* 159 */       long firstPos = Long.parseLong(range.substring(0, dashIdx));
/* 160 */       if (dashIdx < range.length() - 1) {
/* 161 */         Long lastPos = Long.valueOf(Long.parseLong(range.substring(dashIdx + 1, range.length())));
/* 162 */         return new ByteRange(firstPos, lastPos);
/*     */       } 
/*     */       
/* 165 */       return new ByteRange(firstPos, null);
/*     */     } 
/*     */     
/* 168 */     if (dashIdx == 0) {
/* 169 */       long suffixLength = Long.parseLong(range.substring(1));
/* 170 */       return new SuffixByteRange(suffixLength);
/*     */     } 
/*     */     
/* 173 */     throw new IllegalArgumentException("Range '" + range + "' does not contain \"-\"");
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
/*     */   public static List<ResourceRegion> toResourceRegions(List<HttpRange> ranges, Resource resource) {
/* 188 */     if (CollectionUtils.isEmpty(ranges)) {
/* 189 */       return Collections.emptyList();
/*     */     }
/* 191 */     List<ResourceRegion> regions = new ArrayList<ResourceRegion>(ranges.size());
/* 192 */     for (HttpRange range : ranges) {
/* 193 */       regions.add(range.toResourceRegion(resource));
/*     */     }
/* 195 */     if (ranges.size() > 1) {
/* 196 */       long length = getLengthFor(resource);
/* 197 */       long total = 0L;
/* 198 */       for (ResourceRegion region : regions) {
/* 199 */         total += region.getCount();
/*     */       }
/* 201 */       Assert.isTrue((total < length), "The sum of all ranges (" + total + ") should be less than the resource length (" + length + ")");
/*     */     } 
/*     */     
/* 204 */     return regions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Collection<HttpRange> ranges) {
/* 214 */     Assert.notEmpty(ranges, "Ranges Collection must not be empty");
/* 215 */     StringBuilder builder = new StringBuilder("bytes=");
/* 216 */     for (Iterator<HttpRange> iterator = ranges.iterator(); iterator.hasNext(); ) {
/* 217 */       HttpRange range = iterator.next();
/* 218 */       builder.append(range);
/* 219 */       if (iterator.hasNext()) {
/* 220 */         builder.append(", ");
/*     */       }
/*     */     } 
/* 223 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ByteRange
/*     */     extends HttpRange
/*     */   {
/*     */     private final long firstPos;
/*     */ 
/*     */     
/*     */     private final Long lastPos;
/*     */ 
/*     */ 
/*     */     
/*     */     public ByteRange(long firstPos, Long lastPos) {
/* 240 */       assertPositions(firstPos, lastPos);
/* 241 */       this.firstPos = firstPos;
/* 242 */       this.lastPos = lastPos;
/*     */     }
/*     */     
/*     */     private void assertPositions(long firstBytePos, Long lastBytePos) {
/* 246 */       if (firstBytePos < 0L) {
/* 247 */         throw new IllegalArgumentException("Invalid first byte position: " + firstBytePos);
/*     */       }
/* 249 */       if (lastBytePos != null && lastBytePos.longValue() < firstBytePos) {
/* 250 */         throw new IllegalArgumentException("firstBytePosition=" + firstBytePos + " should be less then or equal to lastBytePosition=" + lastBytePos);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getRangeStart(long length) {
/* 257 */       return this.firstPos;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getRangeEnd(long length) {
/* 262 */       if (this.lastPos != null && this.lastPos.longValue() < length) {
/* 263 */         return this.lastPos.longValue();
/*     */       }
/*     */       
/* 266 */       return length - 1L;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 272 */       if (this == other) {
/* 273 */         return true;
/*     */       }
/* 275 */       if (!(other instanceof ByteRange)) {
/* 276 */         return false;
/*     */       }
/* 278 */       ByteRange otherRange = (ByteRange)other;
/* 279 */       return (this.firstPos == otherRange.firstPos && 
/* 280 */         ObjectUtils.nullSafeEquals(this.lastPos, otherRange.lastPos));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 285 */       return ObjectUtils.nullSafeHashCode(Long.valueOf(this.firstPos)) * 31 + 
/* 286 */         ObjectUtils.nullSafeHashCode(this.lastPos);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 291 */       StringBuilder builder = new StringBuilder();
/* 292 */       builder.append(this.firstPos);
/* 293 */       builder.append('-');
/* 294 */       if (this.lastPos != null) {
/* 295 */         builder.append(this.lastPos);
/*     */       }
/* 297 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SuffixByteRange
/*     */     extends HttpRange
/*     */   {
/*     */     private final long suffixLength;
/*     */ 
/*     */ 
/*     */     
/*     */     public SuffixByteRange(long suffixLength) {
/* 312 */       if (suffixLength < 0L) {
/* 313 */         throw new IllegalArgumentException("Invalid suffix length: " + suffixLength);
/*     */       }
/* 315 */       this.suffixLength = suffixLength;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getRangeStart(long length) {
/* 320 */       if (this.suffixLength < length) {
/* 321 */         return length - this.suffixLength;
/*     */       }
/*     */       
/* 324 */       return 0L;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getRangeEnd(long length) {
/* 330 */       return length - 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 335 */       if (this == other) {
/* 336 */         return true;
/*     */       }
/* 338 */       if (!(other instanceof SuffixByteRange)) {
/* 339 */         return false;
/*     */       }
/* 341 */       SuffixByteRange otherRange = (SuffixByteRange)other;
/* 342 */       return (this.suffixLength == otherRange.suffixLength);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 347 */       return ObjectUtils.hashCode(this.suffixLength);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 352 */       return "-" + this.suffixLength;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\HttpRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */