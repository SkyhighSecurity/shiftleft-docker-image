/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
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
/*    */ public abstract class FileSystemUtils
/*    */ {
/*    */   public static boolean deleteRecursively(File root) {
/* 39 */     if (root != null && root.exists()) {
/* 40 */       if (root.isDirectory()) {
/* 41 */         File[] children = root.listFiles();
/* 42 */         if (children != null) {
/* 43 */           for (File child : children) {
/* 44 */             deleteRecursively(child);
/*    */           }
/*    */         }
/*    */       } 
/* 48 */       return root.delete();
/*    */     } 
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void copyRecursively(File src, File dest) throws IOException {
/* 61 */     Assert.isTrue((src != null && (src.isDirectory() || src.isFile())), "Source File must denote a directory or file");
/*    */     
/* 63 */     Assert.notNull(dest, "Destination File must not be null");
/* 64 */     doCopyRecursively(src, dest);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void doCopyRecursively(File src, File dest) throws IOException {
/* 75 */     if (src.isDirectory()) {
/* 76 */       dest.mkdir();
/* 77 */       File[] entries = src.listFiles();
/* 78 */       if (entries == null) {
/* 79 */         throw new IOException("Could not list files in directory: " + src);
/*    */       }
/* 81 */       for (File entry : entries) {
/* 82 */         doCopyRecursively(entry, new File(dest, entry.getName()));
/*    */       }
/*    */     }
/* 85 */     else if (src.isFile()) {
/*    */       try {
/* 87 */         dest.createNewFile();
/*    */       }
/* 89 */       catch (IOException ex) {
/* 90 */         throw new IOException("Failed to create file: " + dest, ex);
/*    */       } 
/* 92 */       FileCopyUtils.copy(src, dest);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\FileSystemUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */