/*    */ package lombok.launch;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.util.Arrays;
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
/*    */ class Main
/*    */ {
/*    */   private static ShadowClassLoader classLoader;
/*    */   
/*    */   static synchronized ClassLoader getShadowClassLoader() {
/* 31 */     if (classLoader == null) {
/* 32 */       classLoader = new ShadowClassLoader(Main.class.getClassLoader(), "lombok", null, Arrays.asList(new String[0]), Arrays.asList(new String[] { "lombok.patcher.Symbols" }));
/*    */     }
/* 34 */     return classLoader;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Throwable {
/* 38 */     ClassLoader cl = getShadowClassLoader();
/* 39 */     Class<?> mc = cl.loadClass("lombok.core.Main");
/*    */     try {
/* 41 */       mc.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });
/* 42 */     } catch (InvocationTargetException e) {
/* 43 */       throw e.getCause();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\launch\Main.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */