/*    */ package org.springframework.core.io;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileSystemResourceLoader
/*    */   extends DefaultResourceLoader
/*    */ {
/*    */   protected Resource getResourceByPath(String path) {
/* 51 */     if (path != null && path.startsWith("/")) {
/* 52 */       path = path.substring(1);
/*    */     }
/* 54 */     return new FileSystemContextResource(path);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static class FileSystemContextResource
/*    */     extends FileSystemResource
/*    */     implements ContextResource
/*    */   {
/*    */     public FileSystemContextResource(String path) {
/* 65 */       super(path);
/*    */     }
/*    */ 
/*    */     
/*    */     public String getPathWithinContext() {
/* 70 */       return getPath();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\FileSystemResourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */