/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public abstract class VfsUtils
/*     */ {
/*     */   private static final String VFS3_PKG = "org.jboss.vfs.";
/*     */   private static final String VFS_NAME = "VFS";
/*     */   private static final Method VFS_METHOD_GET_ROOT_URL;
/*     */   private static final Method VFS_METHOD_GET_ROOT_URI;
/*     */   private static final Method VIRTUAL_FILE_METHOD_EXISTS;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_INPUT_STREAM;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_SIZE;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;
/*     */   private static final Method VIRTUAL_FILE_METHOD_TO_URL;
/*     */   private static final Method VIRTUAL_FILE_METHOD_TO_URI;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_NAME;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_PATH_NAME;
/*     */   private static final Method VIRTUAL_FILE_METHOD_GET_CHILD;
/*     */   protected static final Class<?> VIRTUAL_FILE_VISITOR_INTERFACE;
/*     */   protected static final Method VIRTUAL_FILE_METHOD_VISIT;
/*     */   private static final Field VISITOR_ATTRIBUTES_FIELD_RECURSE;
/*     */   private static final Method GET_PHYSICAL_FILE;
/*     */   
/*     */   static {
/*  69 */     ClassLoader loader = VfsUtils.class.getClassLoader();
/*     */     try {
/*  71 */       Class<?> vfsClass = loader.loadClass("org.jboss.vfs.VFS");
/*  72 */       VFS_METHOD_GET_ROOT_URL = ReflectionUtils.findMethod(vfsClass, "getChild", new Class[] { URL.class });
/*  73 */       VFS_METHOD_GET_ROOT_URI = ReflectionUtils.findMethod(vfsClass, "getChild", new Class[] { URI.class });
/*     */       
/*  75 */       Class<?> virtualFile = loader.loadClass("org.jboss.vfs.VirtualFile");
/*  76 */       VIRTUAL_FILE_METHOD_EXISTS = ReflectionUtils.findMethod(virtualFile, "exists");
/*  77 */       VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = ReflectionUtils.findMethod(virtualFile, "openStream");
/*  78 */       VIRTUAL_FILE_METHOD_GET_SIZE = ReflectionUtils.findMethod(virtualFile, "getSize");
/*  79 */       VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = ReflectionUtils.findMethod(virtualFile, "getLastModified");
/*  80 */       VIRTUAL_FILE_METHOD_TO_URI = ReflectionUtils.findMethod(virtualFile, "toURI");
/*  81 */       VIRTUAL_FILE_METHOD_TO_URL = ReflectionUtils.findMethod(virtualFile, "toURL");
/*  82 */       VIRTUAL_FILE_METHOD_GET_NAME = ReflectionUtils.findMethod(virtualFile, "getName");
/*  83 */       VIRTUAL_FILE_METHOD_GET_PATH_NAME = ReflectionUtils.findMethod(virtualFile, "getPathName");
/*  84 */       GET_PHYSICAL_FILE = ReflectionUtils.findMethod(virtualFile, "getPhysicalFile");
/*  85 */       VIRTUAL_FILE_METHOD_GET_CHILD = ReflectionUtils.findMethod(virtualFile, "getChild", new Class[] { String.class });
/*     */       
/*  87 */       VIRTUAL_FILE_VISITOR_INTERFACE = loader.loadClass("org.jboss.vfs.VirtualFileVisitor");
/*  88 */       VIRTUAL_FILE_METHOD_VISIT = ReflectionUtils.findMethod(virtualFile, "visit", new Class[] { VIRTUAL_FILE_VISITOR_INTERFACE });
/*     */       
/*  90 */       Class<?> visitorAttributesClass = loader.loadClass("org.jboss.vfs.VisitorAttributes");
/*  91 */       VISITOR_ATTRIBUTES_FIELD_RECURSE = ReflectionUtils.findField(visitorAttributesClass, "RECURSE");
/*     */     }
/*  93 */     catch (Throwable ex) {
/*  94 */       throw new IllegalStateException("Could not detect JBoss VFS infrastructure", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static Object invokeVfsMethod(Method method, Object target, Object... args) throws IOException {
/*     */     try {
/* 100 */       return method.invoke(target, args);
/*     */     }
/* 102 */     catch (InvocationTargetException ex) {
/* 103 */       Throwable targetEx = ex.getTargetException();
/* 104 */       if (targetEx instanceof IOException) {
/* 105 */         throw (IOException)targetEx;
/*     */       }
/* 107 */       ReflectionUtils.handleInvocationTargetException(ex);
/*     */     }
/* 109 */     catch (Exception ex) {
/* 110 */       ReflectionUtils.handleReflectionException(ex);
/*     */     } 
/*     */     
/* 113 */     throw new IllegalStateException("Invalid code path reached");
/*     */   }
/*     */   
/*     */   static boolean exists(Object vfsResource) {
/*     */     try {
/* 118 */       return ((Boolean)invokeVfsMethod(VIRTUAL_FILE_METHOD_EXISTS, vfsResource, new Object[0])).booleanValue();
/*     */     }
/* 120 */     catch (IOException ex) {
/* 121 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static boolean isReadable(Object vfsResource) {
/*     */     try {
/* 127 */       return (((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource, new Object[0])).longValue() > 0L);
/*     */     }
/* 129 */     catch (IOException ex) {
/* 130 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static long getSize(Object vfsResource) throws IOException {
/* 135 */     return ((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource, new Object[0])).longValue();
/*     */   }
/*     */   
/*     */   static long getLastModified(Object vfsResource) throws IOException {
/* 139 */     return ((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED, vfsResource, new Object[0])).longValue();
/*     */   }
/*     */   
/*     */   static InputStream getInputStream(Object vfsResource) throws IOException {
/* 143 */     return (InputStream)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_INPUT_STREAM, vfsResource, new Object[0]);
/*     */   }
/*     */   
/*     */   static URL getURL(Object vfsResource) throws IOException {
/* 147 */     return (URL)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URL, vfsResource, new Object[0]);
/*     */   }
/*     */   
/*     */   static URI getURI(Object vfsResource) throws IOException {
/* 151 */     return (URI)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URI, vfsResource, new Object[0]);
/*     */   }
/*     */   
/*     */   static String getName(Object vfsResource) {
/*     */     try {
/* 156 */       return (String)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_NAME, vfsResource, new Object[0]);
/*     */     }
/* 158 */     catch (IOException ex) {
/* 159 */       throw new IllegalStateException("Cannot get resource name", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   static Object getRelative(URL url) throws IOException {
/* 164 */     return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, new Object[] { url });
/*     */   }
/*     */   
/*     */   static Object getChild(Object vfsResource, String path) throws IOException {
/* 168 */     return invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_CHILD, vfsResource, new Object[] { path });
/*     */   }
/*     */   
/*     */   static File getFile(Object vfsResource) throws IOException {
/* 172 */     return (File)invokeVfsMethod(GET_PHYSICAL_FILE, vfsResource, new Object[0]);
/*     */   }
/*     */   
/*     */   static Object getRoot(URI url) throws IOException {
/* 176 */     return invokeVfsMethod(VFS_METHOD_GET_ROOT_URI, null, new Object[] { url });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Object getRoot(URL url) throws IOException {
/* 182 */     return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, new Object[] { url });
/*     */   }
/*     */   
/*     */   protected static Object doGetVisitorAttribute() {
/* 186 */     return ReflectionUtils.getField(VISITOR_ATTRIBUTES_FIELD_RECURSE, null);
/*     */   }
/*     */   
/*     */   protected static String doGetPath(Object resource) {
/* 190 */     return (String)ReflectionUtils.invokeMethod(VIRTUAL_FILE_METHOD_GET_PATH_NAME, resource);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\VfsUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */