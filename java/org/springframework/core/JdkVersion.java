/*     */ package org.springframework.core;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class JdkVersion
/*     */ {
/*     */   public static final int JAVA_13 = 0;
/*     */   public static final int JAVA_14 = 1;
/*     */   public static final int JAVA_15 = 2;
/*     */   public static final int JAVA_16 = 3;
/*     */   public static final int JAVA_17 = 4;
/*     */   public static final int JAVA_18 = 5;
/*     */   public static final int JAVA_19 = 6;
/*  77 */   private static final String javaVersion = System.getProperty("java.version");
/*     */   static {
/*  79 */     if (javaVersion.contains("1.9.")) {
/*  80 */       majorJavaVersion = 6;
/*     */     }
/*  82 */     else if (javaVersion.contains("1.8.")) {
/*  83 */       majorJavaVersion = 5;
/*     */     }
/*  85 */     else if (javaVersion.contains("1.7.")) {
/*  86 */       majorJavaVersion = 4;
/*     */     }
/*     */     else {
/*     */       
/*  90 */       majorJavaVersion = 3;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int majorJavaVersion;
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getJavaVersion() {
/* 102 */     return javaVersion;
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
/*     */   public static int getMajorJavaVersion() {
/* 115 */     return majorJavaVersion;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\JdkVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */