/*    */ package org.springframework.core.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Proxy;
/*    */ import java.net.URL;
/*    */ import org.springframework.core.io.VfsUtils;
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
/*    */ abstract class VfsPatternUtils
/*    */   extends VfsUtils
/*    */ {
/*    */   static Object getVisitorAttribute() {
/* 36 */     return doGetVisitorAttribute();
/*    */   }
/*    */   
/*    */   static String getPath(Object resource) {
/* 40 */     return doGetPath(resource);
/*    */   }
/*    */   
/*    */   static Object findRoot(URL url) throws IOException {
/* 44 */     return getRoot(url);
/*    */   }
/*    */   
/*    */   static void visit(Object resource, InvocationHandler visitor) throws IOException {
/* 48 */     Object visitorProxy = Proxy.newProxyInstance(VIRTUAL_FILE_VISITOR_INTERFACE
/* 49 */         .getClassLoader(), new Class[] { VIRTUAL_FILE_VISITOR_INTERFACE }, visitor);
/*    */     
/* 51 */     invokeVfsMethod(VIRTUAL_FILE_METHOD_VISIT, resource, new Object[] { visitorProxy });
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\VfsPatternUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */