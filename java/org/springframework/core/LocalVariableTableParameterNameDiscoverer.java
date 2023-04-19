/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class LocalVariableTableParameterNameDiscoverer
/*     */   implements ParameterNameDiscoverer
/*     */ {
/*  57 */   private static final Log logger = LogFactory.getLog(LocalVariableTableParameterNameDiscoverer.class);
/*     */ 
/*     */   
/*  60 */   private static final Map<Member, String[]> NO_DEBUG_INFO_MAP = (Map)Collections.emptyMap();
/*     */ 
/*     */   
/*  63 */   private final Map<Class<?>, Map<Member, String[]>> parameterNamesCache = new ConcurrentHashMap<Class<?>, Map<Member, String[]>>(32);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getParameterNames(Method method) {
/*  69 */     Method originalMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  70 */     Class<?> declaringClass = originalMethod.getDeclaringClass();
/*  71 */     Map<Member, String[]> map = this.parameterNamesCache.get(declaringClass);
/*  72 */     if (map == null) {
/*  73 */       map = inspectClass(declaringClass);
/*  74 */       this.parameterNamesCache.put(declaringClass, map);
/*     */     } 
/*  76 */     if (map != NO_DEBUG_INFO_MAP) {
/*  77 */       return map.get(originalMethod);
/*     */     }
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getParameterNames(Constructor<?> ctor) {
/*  84 */     Class<?> declaringClass = ctor.getDeclaringClass();
/*  85 */     Map<Member, String[]> map = this.parameterNamesCache.get(declaringClass);
/*  86 */     if (map == null) {
/*  87 */       map = inspectClass(declaringClass);
/*  88 */       this.parameterNamesCache.put(declaringClass, map);
/*     */     } 
/*  90 */     if (map != NO_DEBUG_INFO_MAP) {
/*  91 */       return map.get(ctor);
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<Member, String[]> inspectClass(Class<?> clazz) {
/* 101 */     InputStream is = clazz.getResourceAsStream(ClassUtils.getClassFileName(clazz));
/* 102 */     if (is == null) {
/*     */ 
/*     */       
/* 105 */       if (logger.isDebugEnabled()) {
/* 106 */         logger.debug("Cannot find '.class' file for class [" + clazz + "] - unable to determine constructor/method parameter names");
/*     */       }
/*     */       
/* 109 */       return NO_DEBUG_INFO_MAP;
/*     */     } 
/*     */     try {
/* 112 */       ClassReader classReader = new ClassReader(is);
/* 113 */       Map<Member, String[]> map = (Map)new ConcurrentHashMap<Member, String>(32);
/* 114 */       classReader.accept(new ParameterNameDiscoveringVisitor(clazz, map), 0);
/* 115 */       return map;
/*     */     }
/* 117 */     catch (IOException ex) {
/* 118 */       if (logger.isDebugEnabled()) {
/* 119 */         logger.debug("Exception thrown while reading '.class' file for class [" + clazz + "] - unable to determine constructor/method parameter names", ex);
/*     */       
/*     */       }
/*     */     }
/* 123 */     catch (IllegalArgumentException ex) {
/* 124 */       if (logger.isDebugEnabled()) {
/* 125 */         logger.debug("ASM ClassReader failed to parse class file [" + clazz + "], probably due to a new Java class file version that isn't supported yet - unable to determine constructor/method parameter names", ex);
/*     */       }
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 132 */         is.close();
/*     */       }
/* 134 */       catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */     
/* 138 */     return NO_DEBUG_INFO_MAP;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ParameterNameDiscoveringVisitor
/*     */     extends ClassVisitor
/*     */   {
/*     */     private static final String STATIC_CLASS_INIT = "<clinit>";
/*     */ 
/*     */     
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private final Map<Member, String[]> memberMap;
/*     */ 
/*     */     
/*     */     public ParameterNameDiscoveringVisitor(Class<?> clazz, Map<Member, String[]> memberMap) {
/* 155 */       super(393216);
/* 156 */       this.clazz = clazz;
/* 157 */       this.memberMap = memberMap;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 163 */       if (!isSyntheticOrBridged(access) && !"<clinit>".equals(name)) {
/* 164 */         return new LocalVariableTableParameterNameDiscoverer.LocalVariableTableVisitor(this.clazz, this.memberMap, name, desc, isStatic(access));
/*     */       }
/* 166 */       return null;
/*     */     }
/*     */     
/*     */     private static boolean isSyntheticOrBridged(int access) {
/* 170 */       return ((access & 0x1000 | access & 0x40) > 0);
/*     */     }
/*     */     
/*     */     private static boolean isStatic(int access) {
/* 174 */       return ((access & 0x8) > 0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LocalVariableTableVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     private static final String CONSTRUCTOR = "<init>";
/*     */ 
/*     */     
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private final Map<Member, String[]> memberMap;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final Type[] args;
/*     */     
/*     */     private final String[] parameterNames;
/*     */     
/*     */     private final boolean isStatic;
/*     */     
/*     */     private boolean hasLvtInfo = false;
/*     */     
/*     */     private final int[] lvtSlotIndex;
/*     */ 
/*     */     
/*     */     public LocalVariableTableVisitor(Class<?> clazz, Map<Member, String[]> map, String name, String desc, boolean isStatic) {
/* 204 */       super(393216);
/* 205 */       this.clazz = clazz;
/* 206 */       this.memberMap = map;
/* 207 */       this.name = name;
/* 208 */       this.args = Type.getArgumentTypes(desc);
/* 209 */       this.parameterNames = new String[this.args.length];
/* 210 */       this.isStatic = isStatic;
/* 211 */       this.lvtSlotIndex = computeLvtSlotIndices(isStatic, this.args);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitLocalVariable(String name, String description, String signature, Label start, Label end, int index) {
/* 216 */       this.hasLvtInfo = true;
/* 217 */       for (int i = 0; i < this.lvtSlotIndex.length; i++) {
/* 218 */         if (this.lvtSlotIndex[i] == index) {
/* 219 */           this.parameterNames[i] = name;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitEnd() {
/* 226 */       if (this.hasLvtInfo || (this.isStatic && this.parameterNames.length == 0))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 231 */         this.memberMap.put(resolveMember(), this.parameterNames);
/*     */       }
/*     */     }
/*     */     
/*     */     private Member resolveMember() {
/* 236 */       ClassLoader loader = this.clazz.getClassLoader();
/* 237 */       Class<?>[] argTypes = new Class[this.args.length];
/* 238 */       for (int i = 0; i < this.args.length; i++) {
/* 239 */         argTypes[i] = ClassUtils.resolveClassName(this.args[i].getClassName(), loader);
/*     */       }
/*     */       try {
/* 242 */         if ("<init>".equals(this.name)) {
/* 243 */           return this.clazz.getDeclaredConstructor(argTypes);
/*     */         }
/* 245 */         return this.clazz.getDeclaredMethod(this.name, argTypes);
/*     */       }
/* 247 */       catch (NoSuchMethodException ex) {
/* 248 */         throw new IllegalStateException("Method [" + this.name + "] was discovered in the .class file but cannot be resolved in the class object", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static int[] computeLvtSlotIndices(boolean isStatic, Type[] paramTypes) {
/* 254 */       int[] lvtIndex = new int[paramTypes.length];
/* 255 */       int nextIndex = isStatic ? 0 : 1;
/* 256 */       for (int i = 0; i < paramTypes.length; i++) {
/* 257 */         lvtIndex[i] = nextIndex;
/* 258 */         if (isWideType(paramTypes[i])) {
/* 259 */           nextIndex += 2;
/*     */         } else {
/*     */           
/* 262 */           nextIndex++;
/*     */         } 
/*     */       } 
/* 265 */       return lvtIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean isWideType(Type aType) {
/* 270 */       return (aType == Type.LONG_TYPE || aType == Type.DOUBLE_TYPE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\LocalVariableTableParameterNameDiscoverer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */