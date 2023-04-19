/*    */ package org.springframework.core.io.support;
/*    */ 
/*    */ import org.springframework.core.io.ResourceLoader;
/*    */ import org.springframework.util.ResourceUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ResourcePatternUtils
/*    */ {
/*    */   public static boolean isUrl(String resourceLocation) {
/* 45 */     return (resourceLocation != null && (resourceLocation
/* 46 */       .startsWith("classpath*:") || 
/* 47 */       ResourceUtils.isUrl(resourceLocation)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ResourcePatternResolver getResourcePatternResolver(ResourceLoader resourceLoader) {
/* 61 */     if (resourceLoader instanceof ResourcePatternResolver) {
/* 62 */       return (ResourcePatternResolver)resourceLoader;
/*    */     }
/* 64 */     if (resourceLoader != null) {
/* 65 */       return new PathMatchingResourcePatternResolver(resourceLoader);
/*    */     }
/*    */     
/* 68 */     return new PathMatchingResourcePatternResolver();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\ResourcePatternUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */