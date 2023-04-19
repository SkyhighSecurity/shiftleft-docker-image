/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntPathMatcher
/*     */   implements PathMatcher
/*     */ {
/*     */   public static final String DEFAULT_PATH_SEPARATOR = "/";
/*     */   private static final int CACHE_TURNOFF_THRESHOLD = 65536;
/*  78 */   private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?\\}");
/*     */   
/*  80 */   private static final char[] WILDCARD_CHARS = new char[] { '*', '?', '{' };
/*     */ 
/*     */   
/*     */   private String pathSeparator;
/*     */   
/*     */   private PathSeparatorPatternCache pathSeparatorPatternCache;
/*     */   
/*     */   private boolean caseSensitive = true;
/*     */   
/*     */   private boolean trimTokens = false;
/*     */   
/*     */   private volatile Boolean cachePatterns;
/*     */   
/*  93 */   private final Map<String, String[]> tokenizedPatternCache = (Map)new ConcurrentHashMap<String, String>(256);
/*     */   
/*  95 */   final Map<String, AntPathStringMatcher> stringMatcherCache = new ConcurrentHashMap<String, AntPathStringMatcher>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AntPathMatcher() {
/* 102 */     this.pathSeparator = "/";
/* 103 */     this.pathSeparatorPatternCache = new PathSeparatorPatternCache("/");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AntPathMatcher(String pathSeparator) {
/* 112 */     Assert.notNull(pathSeparator, "'pathSeparator' is required");
/* 113 */     this.pathSeparator = pathSeparator;
/* 114 */     this.pathSeparatorPatternCache = new PathSeparatorPatternCache(pathSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathSeparator(String pathSeparator) {
/* 123 */     this.pathSeparator = (pathSeparator != null) ? pathSeparator : "/";
/* 124 */     this.pathSeparatorPatternCache = new PathSeparatorPatternCache(this.pathSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean caseSensitive) {
/* 133 */     this.caseSensitive = caseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrimTokens(boolean trimTokens) {
/* 141 */     this.trimTokens = trimTokens;
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
/*     */   public void setCachePatterns(boolean cachePatterns) {
/* 157 */     this.cachePatterns = Boolean.valueOf(cachePatterns);
/*     */   }
/*     */   
/*     */   private void deactivatePatternCache() {
/* 161 */     this.cachePatterns = Boolean.valueOf(false);
/* 162 */     this.tokenizedPatternCache.clear();
/* 163 */     this.stringMatcherCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPattern(String path) {
/* 169 */     return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean match(String pattern, String path) {
/* 174 */     return doMatch(pattern, path, true, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matchStart(String pattern, String path) {
/* 179 */     return doMatch(pattern, path, false, null);
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
/*     */   protected boolean doMatch(String pattern, String path, boolean fullMatch, Map<String, String> uriTemplateVariables) {
/* 191 */     if (path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
/* 192 */       return false;
/*     */     }
/*     */     
/* 195 */     String[] pattDirs = tokenizePattern(pattern);
/* 196 */     if (fullMatch && this.caseSensitive && !isPotentialMatch(path, pattDirs)) {
/* 197 */       return false;
/*     */     }
/*     */     
/* 200 */     String[] pathDirs = tokenizePath(path);
/*     */     
/* 202 */     int pattIdxStart = 0;
/* 203 */     int pattIdxEnd = pattDirs.length - 1;
/* 204 */     int pathIdxStart = 0;
/* 205 */     int pathIdxEnd = pathDirs.length - 1;
/*     */ 
/*     */     
/* 208 */     while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
/* 209 */       String pattDir = pattDirs[pattIdxStart];
/* 210 */       if ("**".equals(pattDir)) {
/*     */         break;
/*     */       }
/* 213 */       if (!matchStrings(pattDir, pathDirs[pathIdxStart], uriTemplateVariables)) {
/* 214 */         return false;
/*     */       }
/* 216 */       pattIdxStart++;
/* 217 */       pathIdxStart++;
/*     */     } 
/*     */     
/* 220 */     if (pathIdxStart > pathIdxEnd) {
/*     */       
/* 222 */       if (pattIdxStart > pattIdxEnd) {
/* 223 */         return (pattern.endsWith(this.pathSeparator) == path.endsWith(this.pathSeparator));
/*     */       }
/* 225 */       if (!fullMatch) {
/* 226 */         return true;
/*     */       }
/* 228 */       if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") && path.endsWith(this.pathSeparator)) {
/* 229 */         return true;
/*     */       }
/* 231 */       for (int j = pattIdxStart; j <= pattIdxEnd; j++) {
/* 232 */         if (!pattDirs[j].equals("**")) {
/* 233 */           return false;
/*     */         }
/*     */       } 
/* 236 */       return true;
/*     */     } 
/* 238 */     if (pattIdxStart > pattIdxEnd)
/*     */     {
/* 240 */       return false;
/*     */     }
/* 242 */     if (!fullMatch && "**".equals(pattDirs[pattIdxStart]))
/*     */     {
/* 244 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 248 */     while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
/* 249 */       String pattDir = pattDirs[pattIdxEnd];
/* 250 */       if (pattDir.equals("**")) {
/*     */         break;
/*     */       }
/* 253 */       if (!matchStrings(pattDir, pathDirs[pathIdxEnd], uriTemplateVariables)) {
/* 254 */         return false;
/*     */       }
/* 256 */       pattIdxEnd--;
/* 257 */       pathIdxEnd--;
/*     */     } 
/* 259 */     if (pathIdxStart > pathIdxEnd) {
/*     */       
/* 261 */       for (int j = pattIdxStart; j <= pattIdxEnd; j++) {
/* 262 */         if (!pattDirs[j].equals("**")) {
/* 263 */           return false;
/*     */         }
/*     */       } 
/* 266 */       return true;
/*     */     } 
/*     */     
/* 269 */     while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
/* 270 */       int patIdxTmp = -1;
/* 271 */       for (int j = pattIdxStart + 1; j <= pattIdxEnd; j++) {
/* 272 */         if (pattDirs[j].equals("**")) {
/* 273 */           patIdxTmp = j;
/*     */           break;
/*     */         } 
/*     */       } 
/* 277 */       if (patIdxTmp == pattIdxStart + 1) {
/*     */         
/* 279 */         pattIdxStart++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 284 */       int patLength = patIdxTmp - pattIdxStart - 1;
/* 285 */       int strLength = pathIdxEnd - pathIdxStart + 1;
/* 286 */       int foundIdx = -1;
/*     */ 
/*     */       
/* 289 */       for (int k = 0; k <= strLength - patLength; ) {
/* 290 */         for (int m = 0; m < patLength; m++) {
/* 291 */           String subPat = pattDirs[pattIdxStart + m + 1];
/* 292 */           String subStr = pathDirs[pathIdxStart + k + m];
/* 293 */           if (!matchStrings(subPat, subStr, uriTemplateVariables)) {
/*     */             k++; continue;
/*     */           } 
/*     */         } 
/* 297 */         foundIdx = pathIdxStart + k;
/*     */       } 
/*     */ 
/*     */       
/* 301 */       if (foundIdx == -1) {
/* 302 */         return false;
/*     */       }
/*     */       
/* 305 */       pattIdxStart = patIdxTmp;
/* 306 */       pathIdxStart = foundIdx + patLength;
/*     */     } 
/*     */     
/* 309 */     for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
/* 310 */       if (!pattDirs[i].equals("**")) {
/* 311 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 315 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isPotentialMatch(String path, String[] pattDirs) {
/* 319 */     if (!this.trimTokens) {
/* 320 */       int pos = 0;
/* 321 */       for (String pattDir : pattDirs) {
/* 322 */         int skipped = skipSeparator(path, pos, this.pathSeparator);
/* 323 */         pos += skipped;
/* 324 */         skipped = skipSegment(path, pos, pattDir);
/* 325 */         if (skipped < pattDir.length()) {
/* 326 */           return (skipped > 0 || (pattDir.length() > 0 && isWildcardChar(pattDir.charAt(0))));
/*     */         }
/* 328 */         pos += skipped;
/*     */       } 
/*     */     } 
/* 331 */     return true;
/*     */   }
/*     */   
/*     */   private int skipSegment(String path, int pos, String prefix) {
/* 335 */     int skipped = 0;
/* 336 */     for (int i = 0; i < prefix.length(); i++) {
/* 337 */       char c = prefix.charAt(i);
/* 338 */       if (isWildcardChar(c)) {
/* 339 */         return skipped;
/*     */       }
/* 341 */       int currPos = pos + skipped;
/* 342 */       if (currPos >= path.length()) {
/* 343 */         return 0;
/*     */       }
/* 345 */       if (c == path.charAt(currPos)) {
/* 346 */         skipped++;
/*     */       }
/*     */     } 
/* 349 */     return skipped;
/*     */   }
/*     */   
/*     */   private int skipSeparator(String path, int pos, String separator) {
/* 353 */     int skipped = 0;
/* 354 */     while (path.startsWith(separator, pos + skipped)) {
/* 355 */       skipped += separator.length();
/*     */     }
/* 357 */     return skipped;
/*     */   }
/*     */   
/*     */   private boolean isWildcardChar(char c) {
/* 361 */     for (char candidate : WILDCARD_CHARS) {
/* 362 */       if (c == candidate) {
/* 363 */         return true;
/*     */       }
/*     */     } 
/* 366 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] tokenizePattern(String pattern) {
/* 377 */     String[] tokenized = null;
/* 378 */     Boolean cachePatterns = this.cachePatterns;
/* 379 */     if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 380 */       tokenized = this.tokenizedPatternCache.get(pattern);
/*     */     }
/* 382 */     if (tokenized == null) {
/* 383 */       tokenized = tokenizePath(pattern);
/* 384 */       if (cachePatterns == null && this.tokenizedPatternCache.size() >= 65536) {
/*     */ 
/*     */ 
/*     */         
/* 388 */         deactivatePatternCache();
/* 389 */         return tokenized;
/*     */       } 
/* 391 */       if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 392 */         this.tokenizedPatternCache.put(pattern, tokenized);
/*     */       }
/*     */     } 
/* 395 */     return tokenized;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] tokenizePath(String path) {
/* 404 */     return StringUtils.tokenizeToStringArray(path, this.pathSeparator, this.trimTokens, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean matchStrings(String pattern, String str, Map<String, String> uriTemplateVariables) {
/* 414 */     return getStringMatcher(pattern).matchStrings(str, uriTemplateVariables);
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
/*     */   protected AntPathStringMatcher getStringMatcher(String pattern) {
/* 431 */     AntPathStringMatcher matcher = null;
/* 432 */     Boolean cachePatterns = this.cachePatterns;
/* 433 */     if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 434 */       matcher = this.stringMatcherCache.get(pattern);
/*     */     }
/* 436 */     if (matcher == null) {
/* 437 */       matcher = new AntPathStringMatcher(pattern, this.caseSensitive);
/* 438 */       if (cachePatterns == null && this.stringMatcherCache.size() >= 65536) {
/*     */ 
/*     */ 
/*     */         
/* 442 */         deactivatePatternCache();
/* 443 */         return matcher;
/*     */       } 
/* 445 */       if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 446 */         this.stringMatcherCache.put(pattern, matcher);
/*     */       }
/*     */     } 
/* 449 */     return matcher;
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
/*     */   public String extractPathWithinPattern(String pattern, String path) {
/* 467 */     String[] patternParts = StringUtils.tokenizeToStringArray(pattern, this.pathSeparator, this.trimTokens, true);
/* 468 */     String[] pathParts = StringUtils.tokenizeToStringArray(path, this.pathSeparator, this.trimTokens, true);
/* 469 */     StringBuilder builder = new StringBuilder();
/* 470 */     boolean pathStarted = false;
/*     */     
/* 472 */     for (int segment = 0; segment < patternParts.length; segment++) {
/* 473 */       String patternPart = patternParts[segment];
/* 474 */       if (patternPart.indexOf('*') > -1 || patternPart.indexOf('?') > -1) {
/* 475 */         for (; segment < pathParts.length; segment++) {
/* 476 */           if (pathStarted || (segment == 0 && !pattern.startsWith(this.pathSeparator))) {
/* 477 */             builder.append(this.pathSeparator);
/*     */           }
/* 479 */           builder.append(pathParts[segment]);
/* 480 */           pathStarted = true;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 485 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
/* 490 */     Map<String, String> variables = new LinkedHashMap<String, String>();
/* 491 */     boolean result = doMatch(pattern, path, true, variables);
/* 492 */     if (!result) {
/* 493 */       throw new IllegalStateException("Pattern \"" + pattern + "\" is not a match for \"" + path + "\"");
/*     */     }
/* 495 */     return variables;
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
/*     */ 
/*     */   
/*     */   public String combine(String pattern1, String pattern2) {
/* 528 */     if (!StringUtils.hasText(pattern1) && !StringUtils.hasText(pattern2)) {
/* 529 */       return "";
/*     */     }
/* 531 */     if (!StringUtils.hasText(pattern1)) {
/* 532 */       return pattern2;
/*     */     }
/* 534 */     if (!StringUtils.hasText(pattern2)) {
/* 535 */       return pattern1;
/*     */     }
/*     */     
/* 538 */     boolean pattern1ContainsUriVar = (pattern1.indexOf('{') != -1);
/* 539 */     if (!pattern1.equals(pattern2) && !pattern1ContainsUriVar && match(pattern1, pattern2))
/*     */     {
/*     */       
/* 542 */       return pattern2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 547 */     if (pattern1.endsWith(this.pathSeparatorPatternCache.getEndsOnWildCard())) {
/* 548 */       return concat(pattern1.substring(0, pattern1.length() - 2), pattern2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 553 */     if (pattern1.endsWith(this.pathSeparatorPatternCache.getEndsOnDoubleWildCard())) {
/* 554 */       return concat(pattern1, pattern2);
/*     */     }
/*     */     
/* 557 */     int starDotPos1 = pattern1.indexOf("*.");
/* 558 */     if (pattern1ContainsUriVar || starDotPos1 == -1 || this.pathSeparator.equals("."))
/*     */     {
/* 560 */       return concat(pattern1, pattern2);
/*     */     }
/*     */     
/* 563 */     String ext1 = pattern1.substring(starDotPos1 + 1);
/* 564 */     int dotPos2 = pattern2.indexOf('.');
/* 565 */     String file2 = (dotPos2 == -1) ? pattern2 : pattern2.substring(0, dotPos2);
/* 566 */     String ext2 = (dotPos2 == -1) ? "" : pattern2.substring(dotPos2);
/* 567 */     boolean ext1All = (ext1.equals(".*") || ext1.equals(""));
/* 568 */     boolean ext2All = (ext2.equals(".*") || ext2.equals(""));
/* 569 */     if (!ext1All && !ext2All) {
/* 570 */       throw new IllegalArgumentException("Cannot combine patterns: " + pattern1 + " vs " + pattern2);
/*     */     }
/* 572 */     String ext = ext1All ? ext2 : ext1;
/* 573 */     return file2 + ext;
/*     */   }
/*     */   
/*     */   private String concat(String path1, String path2) {
/* 577 */     boolean path1EndsWithSeparator = path1.endsWith(this.pathSeparator);
/* 578 */     boolean path2StartsWithSeparator = path2.startsWith(this.pathSeparator);
/*     */     
/* 580 */     if (path1EndsWithSeparator && path2StartsWithSeparator) {
/* 581 */       return path1 + path2.substring(1);
/*     */     }
/* 583 */     if (path1EndsWithSeparator || path2StartsWithSeparator) {
/* 584 */       return path1 + path2;
/*     */     }
/*     */     
/* 587 */     return path1 + this.pathSeparator + path2;
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
/*     */   public Comparator<String> getPatternComparator(String path) {
/* 609 */     return new AntPatternComparator(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class AntPathStringMatcher
/*     */   {
/* 620 */     private static final Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");
/*     */     
/*     */     private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";
/*     */     
/*     */     private final Pattern pattern;
/*     */     
/* 626 */     private final List<String> variableNames = new LinkedList<String>();
/*     */     
/*     */     public AntPathStringMatcher(String pattern) {
/* 629 */       this(pattern, true);
/*     */     }
/*     */     
/*     */     public AntPathStringMatcher(String pattern, boolean caseSensitive) {
/* 633 */       StringBuilder patternBuilder = new StringBuilder();
/* 634 */       Matcher matcher = GLOB_PATTERN.matcher(pattern);
/* 635 */       int end = 0;
/* 636 */       while (matcher.find()) {
/* 637 */         patternBuilder.append(quote(pattern, end, matcher.start()));
/* 638 */         String match = matcher.group();
/* 639 */         if ("?".equals(match)) {
/* 640 */           patternBuilder.append('.');
/*     */         }
/* 642 */         else if ("*".equals(match)) {
/* 643 */           patternBuilder.append(".*");
/*     */         }
/* 645 */         else if (match.startsWith("{") && match.endsWith("}")) {
/* 646 */           int colonIdx = match.indexOf(':');
/* 647 */           if (colonIdx == -1) {
/* 648 */             patternBuilder.append("(.*)");
/* 649 */             this.variableNames.add(matcher.group(1));
/*     */           } else {
/*     */             
/* 652 */             String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
/* 653 */             patternBuilder.append('(');
/* 654 */             patternBuilder.append(variablePattern);
/* 655 */             patternBuilder.append(')');
/* 656 */             String variableName = match.substring(1, colonIdx);
/* 657 */             this.variableNames.add(variableName);
/*     */           } 
/*     */         } 
/* 660 */         end = matcher.end();
/*     */       } 
/* 662 */       patternBuilder.append(quote(pattern, end, pattern.length()));
/* 663 */       this
/* 664 */         .pattern = caseSensitive ? Pattern.compile(patternBuilder.toString()) : Pattern.compile(patternBuilder.toString(), 2);
/*     */     }
/*     */     
/*     */     private String quote(String s, int start, int end) {
/* 668 */       if (start == end) {
/* 669 */         return "";
/*     */       }
/* 671 */       return Pattern.quote(s.substring(start, end));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matchStrings(String str, Map<String, String> uriTemplateVariables) {
/* 679 */       Matcher matcher = this.pattern.matcher(str);
/* 680 */       if (matcher.matches()) {
/* 681 */         if (uriTemplateVariables != null) {
/*     */           
/* 683 */           if (this.variableNames.size() != matcher.groupCount()) {
/* 684 */             throw new IllegalArgumentException("The number of capturing groups in the pattern segment " + this.pattern + " does not match the number of URI template variables it defines, which can occur if capturing groups are used in a URI template regex. Use non-capturing groups instead.");
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 689 */           for (int i = 1; i <= matcher.groupCount(); i++) {
/* 690 */             String name = this.variableNames.get(i - 1);
/* 691 */             String value = matcher.group(i);
/* 692 */             uriTemplateVariables.put(name, value);
/*     */           } 
/*     */         } 
/* 695 */         return true;
/*     */       } 
/*     */       
/* 698 */       return false;
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
/*     */   protected static class AntPatternComparator
/*     */     implements Comparator<String>
/*     */   {
/*     */     private final String path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AntPatternComparator(String path) {
/* 722 */       this.path = path;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(String pattern1, String pattern2) {
/* 733 */       PatternInfo info1 = new PatternInfo(pattern1);
/* 734 */       PatternInfo info2 = new PatternInfo(pattern2);
/*     */       
/* 736 */       if (info1.isLeastSpecific() && info2.isLeastSpecific()) {
/* 737 */         return 0;
/*     */       }
/* 739 */       if (info1.isLeastSpecific()) {
/* 740 */         return 1;
/*     */       }
/* 742 */       if (info2.isLeastSpecific()) {
/* 743 */         return -1;
/*     */       }
/*     */       
/* 746 */       boolean pattern1EqualsPath = pattern1.equals(this.path);
/* 747 */       boolean pattern2EqualsPath = pattern2.equals(this.path);
/* 748 */       if (pattern1EqualsPath && pattern2EqualsPath) {
/* 749 */         return 0;
/*     */       }
/* 751 */       if (pattern1EqualsPath) {
/* 752 */         return -1;
/*     */       }
/* 754 */       if (pattern2EqualsPath) {
/* 755 */         return 1;
/*     */       }
/*     */       
/* 758 */       if (info1.isPrefixPattern() && info2.getDoubleWildcards() == 0) {
/* 759 */         return 1;
/*     */       }
/* 761 */       if (info2.isPrefixPattern() && info1.getDoubleWildcards() == 0) {
/* 762 */         return -1;
/*     */       }
/*     */       
/* 765 */       if (info1.getTotalCount() != info2.getTotalCount()) {
/* 766 */         return info1.getTotalCount() - info2.getTotalCount();
/*     */       }
/*     */       
/* 769 */       if (info1.getLength() != info2.getLength()) {
/* 770 */         return info2.getLength() - info1.getLength();
/*     */       }
/*     */       
/* 773 */       if (info1.getSingleWildcards() < info2.getSingleWildcards()) {
/* 774 */         return -1;
/*     */       }
/* 776 */       if (info2.getSingleWildcards() < info1.getSingleWildcards()) {
/* 777 */         return 1;
/*     */       }
/*     */       
/* 780 */       if (info1.getUriVars() < info2.getUriVars()) {
/* 781 */         return -1;
/*     */       }
/* 783 */       if (info2.getUriVars() < info1.getUriVars()) {
/* 784 */         return 1;
/*     */       }
/*     */       
/* 787 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static class PatternInfo
/*     */     {
/*     */       private final String pattern;
/*     */ 
/*     */       
/*     */       private int uriVars;
/*     */ 
/*     */       
/*     */       private int singleWildcards;
/*     */       
/*     */       private int doubleWildcards;
/*     */       
/*     */       private boolean catchAllPattern;
/*     */       
/*     */       private boolean prefixPattern;
/*     */       
/*     */       private Integer length;
/*     */ 
/*     */       
/*     */       public PatternInfo(String pattern) {
/* 812 */         this.pattern = pattern;
/* 813 */         if (this.pattern != null) {
/* 814 */           initCounters();
/* 815 */           this.catchAllPattern = this.pattern.equals("/**");
/* 816 */           this.prefixPattern = (!this.catchAllPattern && this.pattern.endsWith("/**"));
/*     */         } 
/* 818 */         if (this.uriVars == 0) {
/* 819 */           this.length = Integer.valueOf((this.pattern != null) ? this.pattern.length() : 0);
/*     */         }
/*     */       }
/*     */       
/*     */       protected void initCounters() {
/* 824 */         int pos = 0;
/* 825 */         while (pos < this.pattern.length()) {
/* 826 */           if (this.pattern.charAt(pos) == '{') {
/* 827 */             this.uriVars++;
/* 828 */             pos++; continue;
/*     */           } 
/* 830 */           if (this.pattern.charAt(pos) == '*') {
/* 831 */             if (pos + 1 < this.pattern.length() && this.pattern.charAt(pos + 1) == '*') {
/* 832 */               this.doubleWildcards++;
/* 833 */               pos += 2; continue;
/*     */             } 
/* 835 */             if (pos > 0 && !this.pattern.substring(pos - 1).equals(".*")) {
/* 836 */               this.singleWildcards++;
/* 837 */               pos++;
/*     */               continue;
/*     */             } 
/* 840 */             pos++;
/*     */             
/*     */             continue;
/*     */           } 
/* 844 */           pos++;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public int getUriVars() {
/* 850 */         return this.uriVars;
/*     */       }
/*     */       
/*     */       public int getSingleWildcards() {
/* 854 */         return this.singleWildcards;
/*     */       }
/*     */       
/*     */       public int getDoubleWildcards() {
/* 858 */         return this.doubleWildcards;
/*     */       }
/*     */       
/*     */       public boolean isLeastSpecific() {
/* 862 */         return (this.pattern == null || this.catchAllPattern);
/*     */       }
/*     */       
/*     */       public boolean isPrefixPattern() {
/* 866 */         return this.prefixPattern;
/*     */       }
/*     */       
/*     */       public int getTotalCount() {
/* 870 */         return this.uriVars + this.singleWildcards + 2 * this.doubleWildcards;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public int getLength() {
/* 877 */         if (this.length == null) {
/* 878 */           this.length = Integer.valueOf(AntPathMatcher.VARIABLE_PATTERN.matcher(this.pattern).replaceAll("#").length());
/*     */         }
/* 880 */         return this.length.intValue();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PathSeparatorPatternCache
/*     */   {
/*     */     private final String endsOnWildCard;
/*     */ 
/*     */     
/*     */     private final String endsOnDoubleWildCard;
/*     */ 
/*     */     
/*     */     public PathSeparatorPatternCache(String pathSeparator) {
/* 896 */       this.endsOnWildCard = pathSeparator + "*";
/* 897 */       this.endsOnDoubleWildCard = pathSeparator + "**";
/*     */     }
/*     */     
/*     */     public String getEndsOnWildCard() {
/* 901 */       return this.endsOnWildCard;
/*     */     }
/*     */     
/*     */     public String getEndsOnDoubleWildCard() {
/* 905 */       return this.endsOnDoubleWildCard;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\AntPathMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */