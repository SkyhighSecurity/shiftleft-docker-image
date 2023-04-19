/*      */ package com.fasterxml.jackson.databind.util;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import java.io.IOException;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ 
/*      */ public final class ClassUtil {
/*   18 */   private static final Class<?> CLS_OBJECT = Object.class;
/*      */   
/*   20 */   private static final Annotation[] NO_ANNOTATIONS = new Annotation[0];
/*   21 */   private static final Ctor[] NO_CTORS = new Ctor[0];
/*      */   
/*   23 */   private static final Iterator<?> EMPTY_ITERATOR = Collections.emptyIterator();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterator<T> emptyIterator() {
/*   36 */     return (Iterator)EMPTY_ITERATOR;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<JavaType> findSuperTypes(JavaType type, Class<?> endBefore, boolean addClassItself) {
/*   61 */     if (type == null || type.hasRawClass(endBefore) || type.hasRawClass(Object.class)) {
/*   62 */       return Collections.emptyList();
/*      */     }
/*   64 */     List<JavaType> result = new ArrayList<>(8);
/*   65 */     _addSuperTypes(type, endBefore, result, addClassItself);
/*   66 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Class<?>> findRawSuperTypes(Class<?> cls, Class<?> endBefore, boolean addClassItself) {
/*   73 */     if (cls == null || cls == endBefore || cls == Object.class) {
/*   74 */       return Collections.emptyList();
/*      */     }
/*   76 */     List<Class<?>> result = new ArrayList<>(8);
/*   77 */     _addRawSuperTypes(cls, endBefore, result, addClassItself);
/*   78 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Class<?>> findSuperClasses(Class<?> cls, Class<?> endBefore, boolean addClassItself) {
/*   93 */     List<Class<?>> result = new ArrayList<>(8);
/*   94 */     if (cls != null && cls != endBefore) {
/*   95 */       if (addClassItself) {
/*   96 */         result.add(cls);
/*      */       }
/*   98 */       while ((cls = cls.getSuperclass()) != null && 
/*   99 */         cls != endBefore)
/*      */       {
/*      */         
/*  102 */         result.add(cls);
/*      */       }
/*      */     } 
/*  105 */     return result;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore) {
/*  110 */     return findSuperTypes(cls, endBefore, new ArrayList<>(8));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore, List<Class<?>> result) {
/*  115 */     _addRawSuperTypes(cls, endBefore, result, false);
/*  116 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void _addSuperTypes(JavaType type, Class<?> endBefore, Collection<JavaType> result, boolean addClassItself) {
/*  122 */     if (type == null) {
/*      */       return;
/*      */     }
/*  125 */     Class<?> cls = type.getRawClass();
/*  126 */     if (cls == endBefore || cls == Object.class)
/*  127 */       return;  if (addClassItself) {
/*  128 */       if (result.contains(type)) {
/*      */         return;
/*      */       }
/*  131 */       result.add(type);
/*      */     } 
/*  133 */     for (JavaType intCls : type.getInterfaces()) {
/*  134 */       _addSuperTypes(intCls, endBefore, result, true);
/*      */     }
/*  136 */     _addSuperTypes(type.getSuperClass(), endBefore, result, true);
/*      */   }
/*      */   
/*      */   private static void _addRawSuperTypes(Class<?> cls, Class<?> endBefore, Collection<Class<?>> result, boolean addClassItself) {
/*  140 */     if (cls == endBefore || cls == null || cls == Object.class)
/*  141 */       return;  if (addClassItself) {
/*  142 */       if (result.contains(cls)) {
/*      */         return;
/*      */       }
/*  145 */       result.add(cls);
/*      */     } 
/*  147 */     for (Class<?> intCls : _interfaces(cls)) {
/*  148 */       _addRawSuperTypes(intCls, endBefore, result, true);
/*      */     }
/*  150 */     _addRawSuperTypes(cls.getSuperclass(), endBefore, result, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String canBeABeanType(Class<?> type) {
/*  166 */     if (type.isAnnotation()) {
/*  167 */       return "annotation";
/*      */     }
/*  169 */     if (type.isArray()) {
/*  170 */       return "array";
/*      */     }
/*  172 */     if (type.isEnum()) {
/*  173 */       return "enum";
/*      */     }
/*  175 */     if (type.isPrimitive()) {
/*  176 */       return "primitive";
/*      */     }
/*      */ 
/*      */     
/*  180 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String isLocalType(Class<?> type, boolean allowNonStatic) {
/*      */     
/*  191 */     try { if (hasEnclosingMethod(type)) {
/*  192 */         return "local/anonymous";
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  199 */       if (!allowNonStatic && 
/*  200 */         !Modifier.isStatic(type.getModifiers()) && 
/*  201 */         getEnclosingClass(type) != null) {
/*  202 */         return "non-static member class";
/*      */       
/*      */       }
/*      */        }
/*      */     
/*  207 */     catch (SecurityException securityException) {  }
/*  208 */     catch (NullPointerException nullPointerException) {}
/*  209 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getOuterClass(Class<?> type) {
/*      */     try {
/*  220 */       if (hasEnclosingMethod(type)) {
/*  221 */         return null;
/*      */       }
/*  223 */       if (!Modifier.isStatic(type.getModifiers())) {
/*  224 */         return getEnclosingClass(type);
/*      */       }
/*  226 */     } catch (SecurityException securityException) {}
/*  227 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isProxyType(Class<?> type) {
/*  245 */     String name = type.getName();
/*      */     
/*  247 */     if (name.startsWith("net.sf.cglib.proxy.") || name
/*  248 */       .startsWith("org.hibernate.proxy.")) {
/*  249 */       return true;
/*      */     }
/*      */     
/*  252 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isConcrete(Class<?> type) {
/*  261 */     int mod = type.getModifiers();
/*  262 */     return ((mod & 0x600) == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isConcrete(Member member) {
/*  267 */     int mod = member.getModifiers();
/*  268 */     return ((mod & 0x600) == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCollectionMapOrArray(Class<?> type) {
/*  273 */     if (type.isArray()) return true; 
/*  274 */     if (Collection.class.isAssignableFrom(type)) return true; 
/*  275 */     if (Map.class.isAssignableFrom(type)) return true; 
/*  276 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isBogusClass(Class<?> cls) {
/*  280 */     return (cls == Void.class || cls == void.class || cls == NoClass.class);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isNonStaticInnerClass(Class<?> cls) {
/*  285 */     return (!Modifier.isStatic(cls.getModifiers()) && 
/*  286 */       getEnclosingClass(cls) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isObjectOrPrimitive(Class<?> cls) {
/*  293 */     return (cls == CLS_OBJECT || cls.isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasClass(Object inst, Class<?> raw) {
/*  302 */     return (inst != null && inst.getClass() == raw);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void verifyMustOverride(Class<?> expType, Object instance, String method) {
/*  311 */     if (instance.getClass() != expType) {
/*  312 */       throw new IllegalStateException(String.format("Sub-class %s (of class %s) must override method '%s'", new Object[] { instance
/*      */               
/*  314 */               .getClass().getName(), expType.getName(), method }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean hasGetterSignature(Method m) {
/*  331 */     if (Modifier.isStatic(m.getModifiers())) {
/*  332 */       return false;
/*      */     }
/*      */     
/*  335 */     Class<?>[] pts = m.getParameterTypes();
/*  336 */     if (pts != null && pts.length != 0) {
/*  337 */       return false;
/*      */     }
/*      */     
/*  340 */     if (void.class == m.getReturnType()) {
/*  341 */       return false;
/*      */     }
/*      */     
/*  344 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Throwable throwIfError(Throwable t) {
/*  360 */     if (t instanceof Error) {
/*  361 */       throw (Error)t;
/*      */     }
/*  363 */     return t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Throwable throwIfRTE(Throwable t) {
/*  373 */     if (t instanceof RuntimeException) {
/*  374 */       throw (RuntimeException)t;
/*      */     }
/*  376 */     return t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Throwable throwIfIOE(Throwable t) throws IOException {
/*  386 */     if (t instanceof IOException) {
/*  387 */       throw (IOException)t;
/*      */     }
/*  389 */     return t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Throwable getRootCause(Throwable t) {
/*  404 */     while (t.getCause() != null) {
/*  405 */       t = t.getCause();
/*      */     }
/*  407 */     return t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Throwable throwRootCauseIfIOE(Throwable t) throws IOException {
/*  418 */     return throwIfIOE(getRootCause(t));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void throwAsIAE(Throwable t) {
/*  426 */     throwAsIAE(t, t.getMessage());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void throwAsIAE(Throwable t, String msg) {
/*  436 */     throwIfRTE(t);
/*  437 */     throwIfError(t);
/*  438 */     throw new IllegalArgumentException(msg, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T throwAsMappingException(DeserializationContext ctxt, IOException e0) throws JsonMappingException {
/*  446 */     if (e0 instanceof JsonMappingException) {
/*  447 */       throw (JsonMappingException)e0;
/*      */     }
/*  449 */     JsonMappingException e = JsonMappingException.from(ctxt, e0.getMessage());
/*  450 */     e.initCause(e0);
/*  451 */     throw e;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unwrapAndThrowAsIAE(Throwable t) {
/*  461 */     throwAsIAE(getRootCause(t));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unwrapAndThrowAsIAE(Throwable t, String msg) {
/*  471 */     throwAsIAE(getRootCause(t), msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeOnFailAndThrowAsIOE(JsonGenerator g, Exception fail) throws IOException {
/*  489 */     g.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
/*      */     try {
/*  491 */       g.close();
/*  492 */     } catch (Exception e) {
/*  493 */       fail.addSuppressed(e);
/*      */     } 
/*  495 */     throwIfIOE(fail);
/*  496 */     throwIfRTE(fail);
/*  497 */     throw new RuntimeException(fail);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeOnFailAndThrowAsIOE(JsonGenerator g, Closeable toClose, Exception fail) throws IOException {
/*  513 */     if (g != null) {
/*  514 */       g.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
/*      */       try {
/*  516 */         g.close();
/*  517 */       } catch (Exception e) {
/*  518 */         fail.addSuppressed(e);
/*      */       } 
/*      */     } 
/*  521 */     if (toClose != null) {
/*      */       try {
/*  523 */         toClose.close();
/*  524 */       } catch (Exception e) {
/*  525 */         fail.addSuppressed(e);
/*      */       } 
/*      */     }
/*  528 */     throwIfIOE(fail);
/*  529 */     throwIfRTE(fail);
/*  530 */     throw new RuntimeException(fail);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T createInstance(Class<T> cls, boolean canFixAccess) throws IllegalArgumentException {
/*  555 */     Constructor<T> ctor = findConstructor(cls, canFixAccess);
/*  556 */     if (ctor == null) {
/*  557 */       throw new IllegalArgumentException("Class " + cls.getName() + " has no default (no arg) constructor");
/*      */     }
/*      */     try {
/*  560 */       return ctor.newInstance(new Object[0]);
/*  561 */     } catch (Exception e) {
/*  562 */       unwrapAndThrowAsIAE(e, "Failed to instantiate class " + cls.getName() + ", problem: " + e.getMessage());
/*  563 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Constructor<T> findConstructor(Class<T> cls, boolean forceAccess) throws IllegalArgumentException {
/*      */     try {
/*  571 */       Constructor<T> ctor = cls.getDeclaredConstructor(new Class[0]);
/*  572 */       if (forceAccess) {
/*  573 */         checkAndFixAccess(ctor, forceAccess);
/*      */       
/*      */       }
/*  576 */       else if (!Modifier.isPublic(ctor.getModifiers())) {
/*  577 */         throw new IllegalArgumentException("Default constructor for " + cls.getName() + " is not accessible (non-public?): not allowed to try modify access via Reflection: cannot instantiate type");
/*      */       } 
/*      */       
/*  580 */       return ctor;
/*  581 */     } catch (NoSuchMethodException noSuchMethodException) {
/*      */     
/*  583 */     } catch (Exception e) {
/*  584 */       unwrapAndThrowAsIAE(e, "Failed to find default constructor of class " + cls.getName() + ", problem: " + e.getMessage());
/*      */     } 
/*  586 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> classOf(Object inst) {
/*  599 */     if (inst == null) {
/*  600 */       return null;
/*      */     }
/*  602 */     return inst.getClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> rawClass(JavaType t) {
/*  609 */     if (t == null) {
/*  610 */       return null;
/*      */     }
/*  612 */     return t.getRawClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T nonNull(T valueOrNull, T defaultValue) {
/*  619 */     return (valueOrNull == null) ? defaultValue : valueOrNull;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String nullOrToString(Object value) {
/*  626 */     if (value == null) {
/*  627 */       return null;
/*      */     }
/*  629 */     return value.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String nonNullString(String str) {
/*  636 */     if (str == null) {
/*  637 */       return "";
/*      */     }
/*  639 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String quotedOr(Object str, String forNull) {
/*  649 */     if (str == null) {
/*  650 */       return forNull;
/*      */     }
/*  652 */     return String.format("\"%s\"", new Object[] { str });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getClassDescription(Object classOrInstance) {
/*  668 */     if (classOrInstance == null) {
/*  669 */       return "unknown";
/*      */     }
/*      */     
/*  672 */     Class<?> cls = (classOrInstance instanceof Class) ? (Class)classOrInstance : classOrInstance.getClass();
/*  673 */     return nameOf(cls);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getTypeDescription(JavaType fullType) {
/*  689 */     if (fullType == null) {
/*  690 */       return "[null]";
/*      */     }
/*  692 */     StringBuilder sb = (new StringBuilder(80)).append('`');
/*  693 */     sb.append(fullType.toCanonical());
/*  694 */     return sb.append('`').toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String classNameOf(Object inst) {
/*  705 */     if (inst == null) {
/*  706 */       return "[null]";
/*      */     }
/*  708 */     Class<?> raw = (inst instanceof Class) ? (Class)inst : inst.getClass();
/*  709 */     return nameOf(raw);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String nameOf(Class<?> cls) {
/*  719 */     if (cls == null) {
/*  720 */       return "[null]";
/*      */     }
/*  722 */     int index = 0;
/*  723 */     while (cls.isArray()) {
/*  724 */       index++;
/*  725 */       cls = cls.getComponentType();
/*      */     } 
/*  727 */     String base = cls.isPrimitive() ? cls.getSimpleName() : cls.getName();
/*  728 */     if (index > 0) {
/*  729 */       StringBuilder sb = new StringBuilder(base);
/*      */       while (true) {
/*  731 */         sb.append("[]");
/*  732 */         if (--index <= 0)
/*  733 */         { base = sb.toString(); break; } 
/*      */       } 
/*  735 */     }  return backticked(base);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String nameOf(Named named) {
/*  745 */     if (named == null) {
/*  746 */       return "[null]";
/*      */     }
/*  748 */     return backticked(named.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String backticked(String text) {
/*  763 */     if (text == null) {
/*  764 */       return "[null]";
/*      */     }
/*  766 */     return (new StringBuilder(text.length() + 2)).append('`').append(text).append('`').toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String exceptionMessage(Throwable t) {
/*  779 */     if (t instanceof JsonProcessingException) {
/*  780 */       return ((JsonProcessingException)t).getOriginalMessage();
/*      */     }
/*  782 */     return t.getMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object defaultValue(Class<?> cls) {
/*  797 */     if (cls == int.class) {
/*  798 */       return Integer.valueOf(0);
/*      */     }
/*  800 */     if (cls == long.class) {
/*  801 */       return Long.valueOf(0L);
/*      */     }
/*  803 */     if (cls == boolean.class) {
/*  804 */       return Boolean.FALSE;
/*      */     }
/*  806 */     if (cls == double.class) {
/*  807 */       return Double.valueOf(0.0D);
/*      */     }
/*  809 */     if (cls == float.class) {
/*  810 */       return Float.valueOf(0.0F);
/*      */     }
/*  812 */     if (cls == byte.class) {
/*  813 */       return Byte.valueOf((byte)0);
/*      */     }
/*  815 */     if (cls == short.class) {
/*  816 */       return Short.valueOf((short)0);
/*      */     }
/*  818 */     if (cls == char.class) {
/*  819 */       return Character.valueOf(false);
/*      */     }
/*  821 */     throw new IllegalArgumentException("Class " + cls.getName() + " is not a primitive type");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> wrapperType(Class<?> primitiveType) {
/*  830 */     if (primitiveType == int.class) {
/*  831 */       return Integer.class;
/*      */     }
/*  833 */     if (primitiveType == long.class) {
/*  834 */       return Long.class;
/*      */     }
/*  836 */     if (primitiveType == boolean.class) {
/*  837 */       return Boolean.class;
/*      */     }
/*  839 */     if (primitiveType == double.class) {
/*  840 */       return Double.class;
/*      */     }
/*  842 */     if (primitiveType == float.class) {
/*  843 */       return Float.class;
/*      */     }
/*  845 */     if (primitiveType == byte.class) {
/*  846 */       return Byte.class;
/*      */     }
/*  848 */     if (primitiveType == short.class) {
/*  849 */       return Short.class;
/*      */     }
/*  851 */     if (primitiveType == char.class) {
/*  852 */       return Character.class;
/*      */     }
/*  854 */     throw new IllegalArgumentException("Class " + primitiveType.getName() + " is not a primitive type");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> primitiveType(Class<?> type) {
/*  865 */     if (type.isPrimitive()) {
/*  866 */       return type;
/*      */     }
/*      */     
/*  869 */     if (type == Integer.class) {
/*  870 */       return int.class;
/*      */     }
/*  872 */     if (type == Long.class) {
/*  873 */       return long.class;
/*      */     }
/*  875 */     if (type == Boolean.class) {
/*  876 */       return boolean.class;
/*      */     }
/*  878 */     if (type == Double.class) {
/*  879 */       return double.class;
/*      */     }
/*  881 */     if (type == Float.class) {
/*  882 */       return float.class;
/*      */     }
/*  884 */     if (type == Byte.class) {
/*  885 */       return byte.class;
/*      */     }
/*  887 */     if (type == Short.class) {
/*  888 */       return short.class;
/*      */     }
/*  890 */     if (type == Character.class) {
/*  891 */       return char.class;
/*      */     }
/*  893 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void checkAndFixAccess(Member member) {
/*  912 */     checkAndFixAccess(member, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkAndFixAccess(Member member, boolean force) {
/*  929 */     AccessibleObject ao = (AccessibleObject)member;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  936 */       if (force || 
/*  937 */         !Modifier.isPublic(member.getModifiers()) || 
/*  938 */         !Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
/*  939 */         ao.setAccessible(true);
/*      */       }
/*  941 */     } catch (SecurityException se) {
/*      */ 
/*      */       
/*  944 */       if (!ao.isAccessible()) {
/*  945 */         Class<?> declClass = member.getDeclaringClass();
/*  946 */         throw new IllegalArgumentException("Cannot access " + member + " (from class " + declClass.getName() + "; failed to set access: " + se.getMessage());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<? extends Enum<?>> findEnumType(EnumSet<?> s) {
/*  966 */     if (!s.isEmpty()) {
/*  967 */       return findEnumType(s.iterator().next());
/*      */     }
/*      */     
/*  970 */     return EnumTypeLocator.instance.enumTypeFor(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<? extends Enum<?>> findEnumType(EnumMap<?, ?> m) {
/*  981 */     if (!m.isEmpty()) {
/*  982 */       return findEnumType(m.keySet().iterator().next());
/*      */     }
/*      */     
/*  985 */     return EnumTypeLocator.instance.enumTypeFor(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<? extends Enum<?>> findEnumType(Enum<?> en) {
/*  998 */     Class<?> ec = en.getClass();
/*  999 */     if (ec.getSuperclass() != Enum.class) {
/* 1000 */       ec = ec.getSuperclass();
/*      */     }
/* 1002 */     return (Class)ec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<? extends Enum<?>> findEnumType(Class<?> cls) {
/* 1015 */     if (cls.getSuperclass() != Enum.class) {
/* 1016 */       cls = cls.getSuperclass();
/*      */     }
/* 1018 */     return (Class)cls;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Annotation> Enum<?> findFirstAnnotatedEnumValue(Class<Enum<?>> enumClass, Class<T> annotationClass) {
/* 1034 */     Field[] fields = getDeclaredFields(enumClass);
/* 1035 */     for (Field field : fields) {
/* 1036 */       if (field.isEnumConstant()) {
/* 1037 */         Annotation defaultValueAnnotation = field.getAnnotation(annotationClass);
/* 1038 */         if (defaultValueAnnotation != null) {
/* 1039 */           String name = field.getName();
/* 1040 */           for (Enum<?> enumValue : (Enum[])enumClass.getEnumConstants()) {
/* 1041 */             if (name.equals(enumValue.name())) {
/* 1042 */               return enumValue;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1048 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isJacksonStdImpl(Object impl) {
/* 1068 */     return (impl == null || isJacksonStdImpl(impl.getClass()));
/*      */   }
/*      */   
/*      */   public static boolean isJacksonStdImpl(Class<?> implClass) {
/* 1072 */     return (implClass.getAnnotation(JacksonStdImpl.class) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(Class<?> cls) {
/* 1088 */     Package pkg = cls.getPackage();
/* 1089 */     return (pkg == null) ? null : pkg.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasEnclosingMethod(Class<?> cls) {
/* 1096 */     return (!isObjectOrPrimitive(cls) && cls.getEnclosingMethod() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Field[] getDeclaredFields(Class<?> cls) {
/* 1103 */     return cls.getDeclaredFields();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method[] getDeclaredMethods(Class<?> cls) {
/* 1110 */     return cls.getDeclaredMethods();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Annotation[] findClassAnnotations(Class<?> cls) {
/* 1117 */     if (isObjectOrPrimitive(cls)) {
/* 1118 */       return NO_ANNOTATIONS;
/*      */     }
/* 1120 */     return cls.getDeclaredAnnotations();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method[] getClassMethods(Class<?> cls) {
/*      */     try {
/* 1133 */       return getDeclaredMethods(cls);
/* 1134 */     } catch (NoClassDefFoundError ex) {
/*      */       Class<?> contextClass;
/*      */       
/* 1137 */       ClassLoader loader = Thread.currentThread().getContextClassLoader();
/* 1138 */       if (loader == null)
/*      */       {
/* 1140 */         throw ex;
/*      */       }
/*      */       
/*      */       try {
/* 1144 */         contextClass = loader.loadClass(cls.getName());
/* 1145 */       } catch (ClassNotFoundException e) {
/* 1146 */         ex.addSuppressed(e);
/* 1147 */         throw ex;
/*      */       } 
/* 1149 */       return contextClass.getDeclaredMethods();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Ctor[] getConstructors(Class<?> cls) {
/* 1159 */     if (cls.isInterface() || isObjectOrPrimitive(cls)) {
/* 1160 */       return NO_CTORS;
/*      */     }
/* 1162 */     Constructor[] arrayOfConstructor = (Constructor[])cls.getDeclaredConstructors();
/* 1163 */     int len = arrayOfConstructor.length;
/* 1164 */     Ctor[] result = new Ctor[len];
/* 1165 */     for (int i = 0; i < len; i++) {
/* 1166 */       result[i] = new Ctor(arrayOfConstructor[i]);
/*      */     }
/* 1168 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getDeclaringClass(Class<?> cls) {
/* 1178 */     return isObjectOrPrimitive(cls) ? null : cls.getDeclaringClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Type getGenericSuperclass(Class<?> cls) {
/* 1185 */     return cls.getGenericSuperclass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Type[] getGenericInterfaces(Class<?> cls) {
/* 1192 */     return cls.getGenericInterfaces();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getEnclosingClass(Class<?> cls) {
/* 1200 */     return isObjectOrPrimitive(cls) ? null : cls.getEnclosingClass();
/*      */   }
/*      */   
/*      */   private static Class<?>[] _interfaces(Class<?> cls) {
/* 1204 */     return cls.getInterfaces();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class EnumTypeLocator
/*      */   {
/* 1219 */     static final EnumTypeLocator instance = new EnumTypeLocator();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1227 */     private final Field enumSetTypeField = locateField(EnumSet.class, "elementType", Class.class);
/* 1228 */     private final Field enumMapTypeField = locateField(EnumMap.class, "elementType", Class.class);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Class<? extends Enum<?>> enumTypeFor(EnumSet<?> set) {
/* 1234 */       if (this.enumSetTypeField != null) {
/* 1235 */         return (Class<? extends Enum<?>>)get(set, this.enumSetTypeField);
/*      */       }
/* 1237 */       throw new IllegalStateException("Cannot figure out type for EnumSet (odd JDK platform?)");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Class<? extends Enum<?>> enumTypeFor(EnumMap<?, ?> set) {
/* 1243 */       if (this.enumMapTypeField != null) {
/* 1244 */         return (Class<? extends Enum<?>>)get(set, this.enumMapTypeField);
/*      */       }
/* 1246 */       throw new IllegalStateException("Cannot figure out type for EnumMap (odd JDK platform?)");
/*      */     }
/*      */ 
/*      */     
/*      */     private Object get(Object bean, Field field) {
/*      */       try {
/* 1252 */         return field.get(bean);
/* 1253 */       } catch (Exception e) {
/* 1254 */         throw new IllegalArgumentException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private static Field locateField(Class<?> fromClass, String expectedName, Class<?> type) {
/* 1260 */       Field found = null;
/*      */       
/* 1262 */       Field[] fields = ClassUtil.getDeclaredFields(fromClass);
/* 1263 */       for (Field f : fields) {
/* 1264 */         if (expectedName.equals(f.getName()) && f.getType() == type) {
/* 1265 */           found = f;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 1270 */       if (found == null) {
/* 1271 */         for (Field f : fields) {
/* 1272 */           if (f.getType() == type) {
/*      */             
/* 1274 */             if (found != null) return null; 
/* 1275 */             found = f;
/*      */           } 
/*      */         } 
/*      */       }
/* 1279 */       if (found != null) {
/*      */         try {
/* 1281 */           found.setAccessible(true);
/* 1282 */         } catch (Throwable throwable) {}
/*      */       }
/* 1284 */       return found;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Ctor
/*      */   {
/*      */     public final Constructor<?> _ctor;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Annotation[] _annotations;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Annotation[][] _paramAnnotations;
/*      */ 
/*      */ 
/*      */     
/* 1308 */     private int _paramCount = -1;
/*      */     
/*      */     public Ctor(Constructor<?> ctor) {
/* 1311 */       this._ctor = ctor;
/*      */     }
/*      */     
/*      */     public Constructor<?> getConstructor() {
/* 1315 */       return this._ctor;
/*      */     }
/*      */     
/*      */     public int getParamCount() {
/* 1319 */       int c = this._paramCount;
/* 1320 */       if (c < 0) {
/* 1321 */         c = (this._ctor.getParameterTypes()).length;
/* 1322 */         this._paramCount = c;
/*      */       } 
/* 1324 */       return c;
/*      */     }
/*      */     
/*      */     public Class<?> getDeclaringClass() {
/* 1328 */       return this._ctor.getDeclaringClass();
/*      */     }
/*      */     
/*      */     public Annotation[] getDeclaredAnnotations() {
/* 1332 */       Annotation[] result = this._annotations;
/* 1333 */       if (result == null) {
/* 1334 */         result = this._ctor.getDeclaredAnnotations();
/* 1335 */         this._annotations = result;
/*      */       } 
/* 1337 */       return result;
/*      */     }
/*      */     
/*      */     public Annotation[][] getParameterAnnotations() {
/* 1341 */       Annotation[][] result = this._paramAnnotations;
/* 1342 */       if (result == null) {
/* 1343 */         result = this._ctor.getParameterAnnotations();
/* 1344 */         this._paramAnnotations = result;
/*      */       } 
/* 1346 */       return result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databin\\util\ClassUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */