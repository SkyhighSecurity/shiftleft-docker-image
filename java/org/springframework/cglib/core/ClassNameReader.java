/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.asm.ClassReader;
/*    */ import org.springframework.asm.ClassVisitor;
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
/*    */ public class ClassNameReader
/*    */ {
/* 29 */   private static final EarlyExitException EARLY_EXIT = new EarlyExitException();
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getClassName(ClassReader r) {
/* 34 */     return getClassInfo(r)[0];
/*    */   }
/*    */   private static class EarlyExitException extends RuntimeException {
/*    */     private EarlyExitException() {} }
/*    */   public static String[] getClassInfo(ClassReader r) {
/* 39 */     final List array = new ArrayList();
/*    */     try {
/* 41 */       r.accept(new ClassVisitor(393216, null)
/*    */           {
/*    */ 
/*    */ 
/*    */             
/*    */             public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
/*    */             {
/* 48 */               array.add(name.replace('/', '.'));
/* 49 */               if (superName != null) {
/* 50 */                 array.add(superName.replace('/', '.'));
/*    */               }
/* 52 */               for (int i = 0; i < interfaces.length; i++) {
/* 53 */                 array.add(interfaces[i].replace('/', '.'));
/*    */               }
/*    */               
/* 56 */               throw ClassNameReader.EARLY_EXIT;
/*    */             }
/*    */           }6);
/* 59 */     } catch (EarlyExitException earlyExitException) {}
/*    */     
/* 61 */     return (String[])array.toArray((Object[])new String[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\core\ClassNameReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */