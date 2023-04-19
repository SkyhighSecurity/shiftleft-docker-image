/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import javax.servlet.jsp.tagext.Tag;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TagUtils
/*     */ {
/*     */   public static final String SCOPE_PAGE = "page";
/*     */   public static final String SCOPE_REQUEST = "request";
/*     */   public static final String SCOPE_SESSION = "session";
/*     */   public static final String SCOPE_APPLICATION = "application";
/*     */   
/*     */   public static int getScope(String scope) {
/*  69 */     Assert.notNull(scope, "Scope to search for cannot be null");
/*  70 */     if (scope.equals("request")) {
/*  71 */       return 2;
/*     */     }
/*  73 */     if (scope.equals("session")) {
/*  74 */       return 3;
/*     */     }
/*  76 */     if (scope.equals("application")) {
/*  77 */       return 4;
/*     */     }
/*     */     
/*  80 */     return 1;
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
/*     */   public static boolean hasAncestorOfType(Tag tag, Class<?> ancestorTagClass) {
/*  96 */     Assert.notNull(tag, "Tag cannot be null");
/*  97 */     Assert.notNull(ancestorTagClass, "Ancestor tag class cannot be null");
/*  98 */     if (!Tag.class.isAssignableFrom(ancestorTagClass)) {
/*  99 */       throw new IllegalArgumentException("Class '" + ancestorTagClass
/* 100 */           .getName() + "' is not a valid Tag type");
/*     */     }
/* 102 */     Tag ancestor = tag.getParent();
/* 103 */     while (ancestor != null) {
/* 104 */       if (ancestorTagClass.isAssignableFrom(ancestor.getClass())) {
/* 105 */         return true;
/*     */       }
/* 107 */       ancestor = ancestor.getParent();
/*     */     } 
/* 109 */     return false;
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
/*     */   public static void assertHasAncestorOfType(Tag tag, Class<?> ancestorTagClass, String tagName, String ancestorTagName) {
/* 129 */     Assert.hasText(tagName, "'tagName' must not be empty");
/* 130 */     Assert.hasText(ancestorTagName, "'ancestorTagName' must not be empty");
/* 131 */     if (!hasAncestorOfType(tag, ancestorTagClass))
/* 132 */       throw new IllegalStateException("The '" + tagName + "' tag can only be used inside a valid '" + ancestorTagName + "' tag."); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\TagUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */