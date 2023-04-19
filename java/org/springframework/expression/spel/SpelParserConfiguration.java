/*     */ package org.springframework.expression.spel;
/*     */ 
/*     */ import org.springframework.core.SpringProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpelParserConfiguration
/*     */ {
/*     */   private static final SpelCompilerMode defaultCompilerMode;
/*     */   private final SpelCompilerMode compilerMode;
/*     */   private final ClassLoader compilerClassLoader;
/*     */   private final boolean autoGrowNullReferences;
/*     */   private final boolean autoGrowCollections;
/*     */   private final int maximumAutoGrowSize;
/*     */   
/*     */   static {
/*  35 */     String compilerMode = SpringProperties.getProperty("spring.expression.compiler.mode");
/*     */     
/*  37 */     defaultCompilerMode = (compilerMode != null) ? SpelCompilerMode.valueOf(compilerMode.toUpperCase()) : SpelCompilerMode.OFF;
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
/*     */   public SpelParserConfiguration() {
/*  56 */     this(null, null, false, false, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelParserConfiguration(SpelCompilerMode compilerMode, ClassLoader compilerClassLoader) {
/*  65 */     this(compilerMode, compilerClassLoader, false, false, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelParserConfiguration(boolean autoGrowNullReferences, boolean autoGrowCollections) {
/*  75 */     this(null, null, autoGrowNullReferences, autoGrowCollections, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelParserConfiguration(boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {
/*  85 */     this(null, null, autoGrowNullReferences, autoGrowCollections, maximumAutoGrowSize);
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
/*     */   public SpelParserConfiguration(SpelCompilerMode compilerMode, ClassLoader compilerClassLoader, boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {
/*  99 */     this.compilerMode = (compilerMode != null) ? compilerMode : defaultCompilerMode;
/* 100 */     this.compilerClassLoader = compilerClassLoader;
/* 101 */     this.autoGrowNullReferences = autoGrowNullReferences;
/* 102 */     this.autoGrowCollections = autoGrowCollections;
/* 103 */     this.maximumAutoGrowSize = maximumAutoGrowSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelCompilerMode getCompilerMode() {
/* 111 */     return this.compilerMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getCompilerClassLoader() {
/* 118 */     return this.compilerClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowNullReferences() {
/* 125 */     return this.autoGrowNullReferences;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowCollections() {
/* 132 */     return this.autoGrowCollections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximumAutoGrowSize() {
/* 139 */     return this.maximumAutoGrowSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\SpelParserConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */