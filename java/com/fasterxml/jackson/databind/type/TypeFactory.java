/*      */ package com.fasterxml.jackson.databind.type;
/*      */ 
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonNode;
/*      */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.LRUMap;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TypeFactory
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   40 */   private static final JavaType[] NO_TYPES = new JavaType[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   47 */   protected static final TypeFactory instance = new TypeFactory();
/*      */   
/*   49 */   protected static final TypeBindings EMPTY_BINDINGS = TypeBindings.emptyBindings();
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
/*   61 */   private static final Class<?> CLS_STRING = String.class;
/*   62 */   private static final Class<?> CLS_OBJECT = Object.class;
/*      */   
/*   64 */   private static final Class<?> CLS_COMPARABLE = Comparable.class;
/*   65 */   private static final Class<?> CLS_CLASS = Class.class;
/*   66 */   private static final Class<?> CLS_ENUM = Enum.class;
/*   67 */   private static final Class<?> CLS_JSON_NODE = JsonNode.class;
/*      */   
/*   69 */   private static final Class<?> CLS_BOOL = boolean.class;
/*   70 */   private static final Class<?> CLS_INT = int.class;
/*   71 */   private static final Class<?> CLS_LONG = long.class;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   80 */   protected static final SimpleType CORE_TYPE_BOOL = new SimpleType(CLS_BOOL);
/*   81 */   protected static final SimpleType CORE_TYPE_INT = new SimpleType(CLS_INT);
/*   82 */   protected static final SimpleType CORE_TYPE_LONG = new SimpleType(CLS_LONG);
/*      */ 
/*      */   
/*   85 */   protected static final SimpleType CORE_TYPE_STRING = new SimpleType(CLS_STRING);
/*      */ 
/*      */   
/*   88 */   protected static final SimpleType CORE_TYPE_OBJECT = new SimpleType(CLS_OBJECT);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   96 */   protected static final SimpleType CORE_TYPE_COMPARABLE = new SimpleType(CLS_COMPARABLE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  104 */   protected static final SimpleType CORE_TYPE_ENUM = new SimpleType(CLS_ENUM);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  112 */   protected static final SimpleType CORE_TYPE_CLASS = new SimpleType(CLS_CLASS);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  120 */   protected static final SimpleType CORE_TYPE_JSON_NODE = new SimpleType(CLS_JSON_NODE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final LRUMap<Object, JavaType> _typeCache;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final TypeModifier[] _modifiers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final TypeParser _parser;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final ClassLoader _classLoader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeFactory() {
/*  155 */     this(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeFactory(LRUMap<Object, JavaType> typeCache) {
/*  162 */     if (typeCache == null) {
/*  163 */       typeCache = new LRUMap(16, 200);
/*      */     }
/*  165 */     this._typeCache = typeCache;
/*  166 */     this._parser = new TypeParser(this);
/*  167 */     this._modifiers = null;
/*  168 */     this._classLoader = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeFactory(LRUMap<Object, JavaType> typeCache, TypeParser p, TypeModifier[] mods, ClassLoader classLoader) {
/*  174 */     if (typeCache == null) {
/*  175 */       typeCache = new LRUMap(16, 200);
/*      */     }
/*  177 */     this._typeCache = typeCache;
/*      */     
/*  179 */     this._parser = p.withFactory(this);
/*  180 */     this._modifiers = mods;
/*  181 */     this._classLoader = classLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TypeFactory withModifier(TypeModifier mod) {
/*      */     TypeModifier[] mods;
/*  191 */     LRUMap<Object, JavaType> typeCache = this._typeCache;
/*      */     
/*  193 */     if (mod == null) {
/*  194 */       mods = null;
/*      */ 
/*      */       
/*  197 */       typeCache = null;
/*  198 */     } else if (this._modifiers == null) {
/*  199 */       mods = new TypeModifier[] { mod };
/*      */ 
/*      */       
/*  202 */       typeCache = null;
/*      */     } else {
/*      */       
/*  205 */       mods = (TypeModifier[])ArrayBuilders.insertInListNoDup((Object[])this._modifiers, mod);
/*      */     } 
/*  207 */     return new TypeFactory(typeCache, this._parser, mods, this._classLoader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TypeFactory withClassLoader(ClassLoader classLoader) {
/*  215 */     return new TypeFactory(this._typeCache, this._parser, this._modifiers, classLoader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TypeFactory withCache(LRUMap<Object, JavaType> cache) {
/*  226 */     return new TypeFactory(cache, this._parser, this._modifiers, this._classLoader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static TypeFactory defaultInstance() {
/*  234 */     return instance;
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
/*      */   public void clearCache() {
/*  247 */     this._typeCache.clear();
/*      */   }
/*      */   
/*      */   public ClassLoader getClassLoader() {
/*  251 */     return this._classLoader;
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
/*      */   public static JavaType unknownType() {
/*  266 */     return defaultInstance()._unknownType();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> rawClass(Type t) {
/*  276 */     if (t instanceof Class) {
/*  277 */       return (Class)t;
/*      */     }
/*      */     
/*  280 */     return defaultInstance().constructType(t).getRawClass();
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
/*      */   public Class<?> findClass(String className) throws ClassNotFoundException {
/*  297 */     if (className.indexOf('.') < 0) {
/*  298 */       Class<?> prim = _findPrimitive(className);
/*  299 */       if (prim != null) {
/*  300 */         return prim;
/*      */       }
/*      */     } 
/*      */     
/*  304 */     Throwable prob = null;
/*  305 */     ClassLoader loader = getClassLoader();
/*  306 */     if (loader == null) {
/*  307 */       loader = Thread.currentThread().getContextClassLoader();
/*      */     }
/*  309 */     if (loader != null) {
/*      */       try {
/*  311 */         return classForName(className, true, loader);
/*  312 */       } catch (Exception e) {
/*  313 */         prob = ClassUtil.getRootCause(e);
/*      */       } 
/*      */     }
/*      */     try {
/*  317 */       return classForName(className);
/*  318 */     } catch (Exception e) {
/*  319 */       if (prob == null) {
/*  320 */         prob = ClassUtil.getRootCause(e);
/*      */       }
/*      */       
/*  323 */       ClassUtil.throwIfRTE(prob);
/*  324 */       throw new ClassNotFoundException(prob.getMessage(), prob);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Class<?> classForName(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
/*  329 */     return Class.forName(name, true, loader);
/*      */   }
/*      */   
/*      */   protected Class<?> classForName(String name) throws ClassNotFoundException {
/*  333 */     return Class.forName(name);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Class<?> _findPrimitive(String className) {
/*  338 */     if ("int".equals(className)) return int.class; 
/*  339 */     if ("long".equals(className)) return long.class; 
/*  340 */     if ("float".equals(className)) return float.class; 
/*  341 */     if ("double".equals(className)) return double.class; 
/*  342 */     if ("boolean".equals(className)) return boolean.class; 
/*  343 */     if ("byte".equals(className)) return byte.class; 
/*  344 */     if ("char".equals(className)) return char.class; 
/*  345 */     if ("short".equals(className)) return short.class; 
/*  346 */     if ("void".equals(className)) return void.class; 
/*  347 */     return null;
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
/*      */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
/*  366 */     Class<?> rawBase = baseType.getRawClass();
/*  367 */     if (rawBase == subclass) {
/*  368 */       return baseType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  374 */     if (rawBase == Object.class)
/*  375 */     { newType = _fromClass(null, subclass, EMPTY_BINDINGS); }
/*      */     else
/*      */     
/*  378 */     { if (!rawBase.isAssignableFrom(subclass)) {
/*  379 */         throw new IllegalArgumentException(String.format("Class %s not subtype of %s", new Object[] { subclass
/*  380 */                 .getName(), baseType }));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  385 */       if (baseType.getBindings().isEmpty())
/*  386 */       { newType = _fromClass(null, subclass, EMPTY_BINDINGS); }
/*      */       
/*      */       else
/*      */       
/*  390 */       { if (baseType.isContainerType())
/*  391 */           if (baseType.isMapLikeType())
/*  392 */           { if (subclass == HashMap.class || subclass == LinkedHashMap.class || subclass == EnumMap.class || subclass == TreeMap.class)
/*      */             
/*      */             { 
/*      */               
/*  396 */               newType = _fromClass(null, subclass, 
/*  397 */                   TypeBindings.create(subclass, baseType.getKeyType(), baseType.getContentType()));
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
/*  430 */               newType = newType.withHandlersFrom(baseType);
/*  431 */               return newType; }  } else if (baseType.isCollectionLikeType()) { if (subclass == ArrayList.class || subclass == LinkedList.class || subclass == HashSet.class || subclass == TreeSet.class) { newType = _fromClass(null, subclass, TypeBindings.create(subclass, baseType.getContentType())); } else { if (rawBase == EnumSet.class) return baseType;  int typeParamCount = (subclass.getTypeParameters()).length; }  newType = newType.withHandlersFrom(baseType); return newType; }   int i = (subclass.getTypeParameters()).length; }  }  JavaType newType = newType.withHandlersFrom(baseType); return newType;
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeBindings _bindingsForSubtype(JavaType baseType, int typeParamCount, Class<?> subclass) {
/*  436 */     PlaceholderForType[] placeholders = new PlaceholderForType[typeParamCount];
/*  437 */     for (int i = 0; i < typeParamCount; i++) {
/*  438 */       placeholders[i] = new PlaceholderForType(i);
/*      */     }
/*  440 */     TypeBindings b = TypeBindings.create(subclass, (JavaType[])placeholders);
/*      */     
/*  442 */     JavaType tmpSub = _fromClass(null, subclass, b);
/*      */     
/*  444 */     JavaType baseWithPlaceholders = tmpSub.findSuperType(baseType.getRawClass());
/*  445 */     if (baseWithPlaceholders == null) {
/*  446 */       throw new IllegalArgumentException(String.format("Internal error: unable to locate supertype (%s) from resolved subtype %s", new Object[] { baseType
/*  447 */               .getRawClass().getName(), subclass
/*  448 */               .getName() }));
/*      */     }
/*      */     
/*  451 */     String error = _resolveTypePlaceholders(baseType, baseWithPlaceholders);
/*  452 */     if (error != null) {
/*  453 */       throw new IllegalArgumentException("Failed to specialize base type " + baseType.toCanonical() + " as " + subclass
/*  454 */           .getName() + ", problem: " + error);
/*      */     }
/*      */     
/*  457 */     JavaType[] typeParams = new JavaType[typeParamCount];
/*  458 */     for (int j = 0; j < typeParamCount; j++) {
/*  459 */       JavaType t = placeholders[j].actualType();
/*      */ 
/*      */ 
/*      */       
/*  463 */       if (t == null) {
/*  464 */         t = unknownType();
/*      */       }
/*  466 */       typeParams[j] = t;
/*      */     } 
/*  468 */     return TypeBindings.create(subclass, typeParams);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String _resolveTypePlaceholders(JavaType sourceType, JavaType actualType) throws IllegalArgumentException {
/*  474 */     List<JavaType> expectedTypes = sourceType.getBindings().getTypeParameters();
/*  475 */     List<JavaType> actualTypes = actualType.getBindings().getTypeParameters();
/*  476 */     for (int i = 0, len = expectedTypes.size(); i < len; i++) {
/*  477 */       JavaType exp = expectedTypes.get(i);
/*  478 */       JavaType act = actualTypes.get(i);
/*      */       
/*  480 */       if (!_verifyAndResolvePlaceholders(exp, act))
/*      */       {
/*      */ 
/*      */         
/*  484 */         if (!exp.hasRawClass(Object.class))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  493 */           if (i != 0 || 
/*  494 */             !sourceType.isMapLikeType() || 
/*  495 */             !act.hasRawClass(Object.class))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  501 */             if (!exp.isInterface() || 
/*  502 */               !exp.isTypeOrSuperTypeOf(act.getRawClass()))
/*      */             {
/*      */ 
/*      */               
/*  506 */               return String.format("Type parameter #%d/%d differs; can not specialize %s with %s", new Object[] {
/*  507 */                     Integer.valueOf(i + 1), Integer.valueOf(len), exp.toCanonical(), act.toCanonical()
/*      */                   }); }  }  }  } 
/*      */     } 
/*  510 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean _verifyAndResolvePlaceholders(JavaType exp, JavaType act) {
/*  516 */     if (act instanceof PlaceholderForType) {
/*  517 */       ((PlaceholderForType)act).actualType(exp);
/*  518 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  522 */     if (exp.getRawClass() != act.getRawClass()) {
/*  523 */       return false;
/*      */     }
/*      */     
/*  526 */     List<JavaType> expectedTypes = exp.getBindings().getTypeParameters();
/*  527 */     List<JavaType> actualTypes = act.getBindings().getTypeParameters();
/*  528 */     for (int i = 0, len = expectedTypes.size(); i < len; i++) {
/*  529 */       JavaType exp2 = expectedTypes.get(i);
/*  530 */       JavaType act2 = actualTypes.get(i);
/*  531 */       if (!_verifyAndResolvePlaceholders(exp2, act2)) {
/*  532 */         return false;
/*      */       }
/*      */     } 
/*  535 */     return true;
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
/*      */   public JavaType constructGeneralizedType(JavaType baseType, Class<?> superClass) {
/*  551 */     Class<?> rawBase = baseType.getRawClass();
/*  552 */     if (rawBase == superClass) {
/*  553 */       return baseType;
/*      */     }
/*  555 */     JavaType superType = baseType.findSuperType(superClass);
/*  556 */     if (superType == null) {
/*      */       
/*  558 */       if (!superClass.isAssignableFrom(rawBase)) {
/*  559 */         throw new IllegalArgumentException(String.format("Class %s not a super-type of %s", new Object[] { superClass
/*  560 */                 .getName(), baseType }));
/*      */       }
/*      */       
/*  563 */       throw new IllegalArgumentException(String.format("Internal error: class %s not included as super-type for %s", new Object[] { superClass
/*      */               
/*  565 */               .getName(), baseType }));
/*      */     } 
/*  567 */     return superType;
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
/*      */   public JavaType constructFromCanonical(String canonical) throws IllegalArgumentException {
/*  582 */     return this._parser.parse(canonical);
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
/*      */   public JavaType[] findTypeParameters(JavaType type, Class<?> expType) {
/*  596 */     JavaType match = type.findSuperType(expType);
/*  597 */     if (match == null) {
/*  598 */       return NO_TYPES;
/*      */     }
/*  600 */     return match.getBindings().typeParameterArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JavaType[] findTypeParameters(Class<?> clz, Class<?> expType, TypeBindings bindings) {
/*  608 */     return findTypeParameters(constructType(clz, bindings), expType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JavaType[] findTypeParameters(Class<?> clz, Class<?> expType) {
/*  616 */     return findTypeParameters(constructType(clz), expType);
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
/*      */   public JavaType moreSpecificType(JavaType type1, JavaType type2) {
/*  631 */     if (type1 == null) {
/*  632 */       return type2;
/*      */     }
/*  634 */     if (type2 == null) {
/*  635 */       return type1;
/*      */     }
/*  637 */     Class<?> raw1 = type1.getRawClass();
/*  638 */     Class<?> raw2 = type2.getRawClass();
/*  639 */     if (raw1 == raw2) {
/*  640 */       return type1;
/*      */     }
/*      */     
/*  643 */     if (raw1.isAssignableFrom(raw2)) {
/*  644 */       return type2;
/*      */     }
/*  646 */     return type1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType constructType(Type type) {
/*  656 */     return _fromAny(null, type, EMPTY_BINDINGS);
/*      */   }
/*      */   
/*      */   public JavaType constructType(Type type, TypeBindings bindings) {
/*  660 */     return _fromAny(null, type, bindings);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType constructType(TypeReference<?> typeRef) {
/*  666 */     return _fromAny(null, typeRef.getType(), EMPTY_BINDINGS);
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
/*      */   @Deprecated
/*      */   public JavaType constructType(Type type, Class<?> contextClass) {
/*  692 */     JavaType contextType = (contextClass == null) ? null : constructType(contextClass);
/*  693 */     return constructType(type, contextType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JavaType constructType(Type type, JavaType contextType) {
/*      */     TypeBindings bindings;
/*  702 */     if (contextType == null) {
/*  703 */       bindings = EMPTY_BINDINGS;
/*      */     } else {
/*  705 */       bindings = contextType.getBindings();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  710 */       if (type.getClass() != Class.class)
/*      */       {
/*      */         
/*  713 */         while (bindings.isEmpty()) {
/*  714 */           contextType = contextType.getSuperClass();
/*  715 */           if (contextType == null) {
/*      */             break;
/*      */           }
/*  718 */           bindings = contextType.getBindings();
/*      */         } 
/*      */       }
/*      */     } 
/*  722 */     return _fromAny(null, type, bindings);
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
/*      */   public ArrayType constructArrayType(Class<?> elementType) {
/*  738 */     return ArrayType.construct(_fromAny(null, elementType, null), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ArrayType constructArrayType(JavaType elementType) {
/*  748 */     return ArrayType.construct(elementType, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
/*  759 */     return constructCollectionType(collectionClass, 
/*  760 */         _fromClass(null, elementClass, EMPTY_BINDINGS));
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
/*      */   public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, JavaType elementType) {
/*  772 */     TypeBindings bindings = TypeBindings.createIfNeeded(collectionClass, elementType);
/*  773 */     CollectionType result = (CollectionType)_fromClass(null, collectionClass, bindings);
/*      */ 
/*      */     
/*  776 */     if (bindings.isEmpty() && elementType != null) {
/*  777 */       JavaType t = result.findSuperType(Collection.class);
/*  778 */       JavaType realET = t.getContentType();
/*  779 */       if (!realET.equals(elementType))
/*  780 */         throw new IllegalArgumentException(String.format("Non-generic Collection class %s did not resolve to something with element type %s but %s ", new Object[] {
/*      */                 
/*  782 */                 ClassUtil.nameOf(collectionClass), elementType, realET
/*      */               })); 
/*      */     } 
/*  785 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, Class<?> elementClass) {
/*  795 */     return constructCollectionLikeType(collectionClass, 
/*  796 */         _fromClass(null, elementClass, EMPTY_BINDINGS));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, JavaType elementType) {
/*  806 */     JavaType type = _fromClass(null, collectionClass, 
/*  807 */         TypeBindings.createIfNeeded(collectionClass, elementType));
/*  808 */     if (type instanceof CollectionLikeType) {
/*  809 */       return (CollectionLikeType)type;
/*      */     }
/*  811 */     return CollectionLikeType.upgradeFrom(type, elementType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapType constructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
/*      */     JavaType kt;
/*      */     JavaType vt;
/*  823 */     if (mapClass == Properties.class) {
/*  824 */       kt = vt = CORE_TYPE_STRING;
/*      */     } else {
/*  826 */       kt = _fromClass(null, keyClass, EMPTY_BINDINGS);
/*  827 */       vt = _fromClass(null, valueClass, EMPTY_BINDINGS);
/*      */     } 
/*  829 */     return constructMapType(mapClass, kt, vt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapType constructMapType(Class<? extends Map> mapClass, JavaType keyType, JavaType valueType) {
/*  839 */     TypeBindings bindings = TypeBindings.createIfNeeded(mapClass, new JavaType[] { keyType, valueType });
/*  840 */     MapType result = (MapType)_fromClass(null, mapClass, bindings);
/*      */ 
/*      */     
/*  843 */     if (bindings.isEmpty()) {
/*  844 */       JavaType t = result.findSuperType(Map.class);
/*  845 */       JavaType realKT = t.getKeyType();
/*  846 */       if (!realKT.equals(keyType))
/*  847 */         throw new IllegalArgumentException(String.format("Non-generic Map class %s did not resolve to something with key type %s but %s ", new Object[] {
/*      */                 
/*  849 */                 ClassUtil.nameOf(mapClass), keyType, realKT
/*      */               })); 
/*  851 */       JavaType realVT = t.getContentType();
/*  852 */       if (!realVT.equals(valueType))
/*  853 */         throw new IllegalArgumentException(String.format("Non-generic Map class %s did not resolve to something with value type %s but %s ", new Object[] {
/*      */                 
/*  855 */                 ClassUtil.nameOf(mapClass), valueType, realVT
/*      */               })); 
/*      */     } 
/*  858 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapLikeType constructMapLikeType(Class<?> mapClass, Class<?> keyClass, Class<?> valueClass) {
/*  868 */     return constructMapLikeType(mapClass, 
/*  869 */         _fromClass(null, keyClass, EMPTY_BINDINGS), 
/*  870 */         _fromClass(null, valueClass, EMPTY_BINDINGS));
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
/*      */   public MapLikeType constructMapLikeType(Class<?> mapClass, JavaType keyType, JavaType valueType) {
/*  882 */     JavaType type = _fromClass(null, mapClass, 
/*  883 */         TypeBindings.createIfNeeded(mapClass, new JavaType[] { keyType, valueType }));
/*  884 */     if (type instanceof MapLikeType) {
/*  885 */       return (MapLikeType)type;
/*      */     }
/*  887 */     return MapLikeType.upgradeFrom(type, keyType, valueType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType constructSimpleType(Class<?> rawType, JavaType[] parameterTypes) {
/*  896 */     return _fromClass(null, rawType, TypeBindings.create(rawType, parameterTypes));
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
/*      */   @Deprecated
/*      */   public JavaType constructSimpleType(Class<?> rawType, Class<?> parameterTarget, JavaType[] parameterTypes) {
/*  910 */     return constructSimpleType(rawType, parameterTypes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType constructReferenceType(Class<?> rawType, JavaType referredType) {
/*  918 */     return ReferenceType.construct(rawType, null, null, null, referredType);
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
/*      */   public JavaType uncheckedSimpleType(Class<?> cls) {
/*  937 */     return _constructSimple(cls, EMPTY_BINDINGS, null, null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
/*  968 */     int len = parameterClasses.length;
/*  969 */     JavaType[] pt = new JavaType[len];
/*  970 */     for (int i = 0; i < len; i++) {
/*  971 */       pt[i] = _fromClass(null, parameterClasses[i], EMPTY_BINDINGS);
/*      */     }
/*  973 */     return constructParametricType(parametrized, pt);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType constructParametricType(Class<?> rawType, JavaType... parameterTypes) {
/* 1004 */     return _fromClass(null, rawType, TypeBindings.create(rawType, parameterTypes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JavaType constructParametrizedType(Class<?> parametrized, Class<?> parametersFor, JavaType... parameterTypes) {
/* 1016 */     return constructParametricType(parametrized, parameterTypes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JavaType constructParametrizedType(Class<?> parametrized, Class<?> parametersFor, Class<?>... parameterClasses) {
/* 1028 */     return constructParametricType(parametrized, parameterClasses);
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
/*      */   public CollectionType constructRawCollectionType(Class<? extends Collection> collectionClass) {
/* 1050 */     return constructCollectionType(collectionClass, unknownType());
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
/*      */   public CollectionLikeType constructRawCollectionLikeType(Class<?> collectionClass) {
/* 1065 */     return constructCollectionLikeType(collectionClass, unknownType());
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
/*      */   public MapType constructRawMapType(Class<? extends Map> mapClass) {
/* 1080 */     return constructMapType(mapClass, unknownType(), unknownType());
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
/*      */   public MapLikeType constructRawMapLikeType(Class<?> mapClass) {
/* 1095 */     return constructMapLikeType(mapClass, unknownType(), unknownType());
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
/*      */   private JavaType _mapType(Class<?> rawClass, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
/*      */     JavaType kt;
/*      */     JavaType vt;
/* 1110 */     if (rawClass == Properties.class)
/* 1111 */     { kt = vt = CORE_TYPE_STRING; }
/*      */     else
/* 1113 */     { List<JavaType> typeParams = bindings.getTypeParameters();
/*      */       
/* 1115 */       switch (typeParams.size())
/*      */       { case 0:
/* 1117 */           kt = vt = _unknownType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1127 */           return MapType.construct(rawClass, bindings, superClass, superInterfaces, kt, vt);case 2: kt = typeParams.get(0); vt = typeParams.get(1); return MapType.construct(rawClass, bindings, superClass, superInterfaces, kt, vt); }  throw new IllegalArgumentException("Strange Map type " + rawClass.getName() + ": cannot determine type parameters"); }  return MapType.construct(rawClass, bindings, superClass, superInterfaces, kt, vt);
/*      */   }
/*      */ 
/*      */   
/*      */   private JavaType _collectionType(Class<?> rawClass, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
/*      */     JavaType ct;
/* 1133 */     List<JavaType> typeParams = bindings.getTypeParameters();
/*      */ 
/*      */     
/* 1136 */     if (typeParams.isEmpty()) {
/* 1137 */       ct = _unknownType();
/* 1138 */     } else if (typeParams.size() == 1) {
/* 1139 */       ct = typeParams.get(0);
/*      */     } else {
/* 1141 */       throw new IllegalArgumentException("Strange Collection type " + rawClass.getName() + ": cannot determine type parameters");
/*      */     } 
/* 1143 */     return CollectionType.construct(rawClass, bindings, superClass, superInterfaces, ct);
/*      */   }
/*      */ 
/*      */   
/*      */   private JavaType _referenceType(Class<?> rawClass, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
/*      */     JavaType ct;
/* 1149 */     List<JavaType> typeParams = bindings.getTypeParameters();
/*      */ 
/*      */     
/* 1152 */     if (typeParams.isEmpty()) {
/* 1153 */       ct = _unknownType();
/* 1154 */     } else if (typeParams.size() == 1) {
/* 1155 */       ct = typeParams.get(0);
/*      */     } else {
/* 1157 */       throw new IllegalArgumentException("Strange Reference type " + rawClass.getName() + ": cannot determine type parameters");
/*      */     } 
/* 1159 */     return ReferenceType.construct(rawClass, bindings, superClass, superInterfaces, ct);
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
/*      */   protected JavaType _constructSimple(Class<?> raw, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
/* 1173 */     if (bindings.isEmpty()) {
/* 1174 */       JavaType result = _findWellKnownSimple(raw);
/* 1175 */       if (result != null) {
/* 1176 */         return result;
/*      */       }
/*      */     } 
/* 1179 */     return _newSimpleType(raw, bindings, superClass, superInterfaces);
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
/*      */   protected JavaType _newSimpleType(Class<?> raw, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
/* 1192 */     return new SimpleType(raw, bindings, superClass, superInterfaces);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JavaType _unknownType() {
/* 1201 */     return CORE_TYPE_OBJECT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JavaType _findWellKnownSimple(Class<?> clz) {
/* 1212 */     if (clz.isPrimitive()) {
/* 1213 */       if (clz == CLS_BOOL) return CORE_TYPE_BOOL; 
/* 1214 */       if (clz == CLS_INT) return CORE_TYPE_INT; 
/* 1215 */       if (clz == CLS_LONG) return CORE_TYPE_LONG; 
/*      */     } else {
/* 1217 */       if (clz == CLS_STRING) return CORE_TYPE_STRING; 
/* 1218 */       if (clz == CLS_OBJECT) return CORE_TYPE_OBJECT; 
/* 1219 */       if (clz == CLS_JSON_NODE) return CORE_TYPE_JSON_NODE; 
/*      */     } 
/* 1221 */     return null;
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
/*      */   protected JavaType _fromAny(ClassStack context, Type type, TypeBindings bindings) {
/*      */     JavaType resultType;
/* 1240 */     if (type instanceof Class) {
/*      */       
/* 1242 */       resultType = _fromClass(context, (Class)type, EMPTY_BINDINGS);
/*      */     
/*      */     }
/* 1245 */     else if (type instanceof ParameterizedType) {
/* 1246 */       resultType = _fromParamType(context, (ParameterizedType)type, bindings);
/*      */     } else {
/* 1248 */       if (type instanceof JavaType)
/*      */       {
/* 1250 */         return (JavaType)type;
/*      */       }
/* 1252 */       if (type instanceof GenericArrayType) {
/* 1253 */         resultType = _fromArrayType(context, (GenericArrayType)type, bindings);
/*      */       }
/* 1255 */       else if (type instanceof TypeVariable) {
/* 1256 */         resultType = _fromVariable(context, (TypeVariable)type, bindings);
/*      */       }
/* 1258 */       else if (type instanceof WildcardType) {
/* 1259 */         resultType = _fromWildcard(context, (WildcardType)type, bindings);
/*      */       } else {
/*      */         
/* 1262 */         throw new IllegalArgumentException("Unrecognized Type: " + ((type == null) ? "[null]" : type.toString()));
/*      */       } 
/*      */     } 
/*      */     
/* 1266 */     if (this._modifiers != null) {
/* 1267 */       TypeBindings b = resultType.getBindings();
/* 1268 */       if (b == null) {
/* 1269 */         b = EMPTY_BINDINGS;
/*      */       }
/* 1271 */       for (TypeModifier mod : this._modifiers) {
/* 1272 */         JavaType t = mod.modifyType(resultType, type, b, this);
/* 1273 */         if (t == null) {
/* 1274 */           throw new IllegalStateException(String.format("TypeModifier %s (of type %s) return null for type %s", new Object[] { mod, mod
/*      */                   
/* 1276 */                   .getClass().getName(), resultType }));
/*      */         }
/* 1278 */         resultType = t;
/*      */       } 
/*      */     } 
/* 1281 */     return resultType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JavaType _fromClass(ClassStack context, Class<?> rawType, TypeBindings bindings) {
/*      */     Object key;
/* 1291 */     JavaType result = _findWellKnownSimple(rawType);
/* 1292 */     if (result != null) {
/* 1293 */       return result;
/*      */     }
/*      */ 
/*      */     
/* 1297 */     if (bindings == null || bindings.isEmpty()) {
/* 1298 */       key = rawType;
/*      */     } else {
/* 1300 */       key = bindings.asKey(rawType);
/*      */     } 
/* 1302 */     result = (JavaType)this._typeCache.get(key);
/* 1303 */     if (result != null) {
/* 1304 */       return result;
/*      */     }
/*      */ 
/*      */     
/* 1308 */     if (context == null) {
/* 1309 */       context = new ClassStack(rawType);
/*      */     } else {
/* 1311 */       ClassStack prev = context.find(rawType);
/* 1312 */       if (prev != null) {
/*      */         
/* 1314 */         ResolvedRecursiveType selfRef = new ResolvedRecursiveType(rawType, EMPTY_BINDINGS);
/* 1315 */         prev.addSelfReference(selfRef);
/* 1316 */         return selfRef;
/*      */       } 
/*      */       
/* 1319 */       context = context.child(rawType);
/*      */     } 
/*      */ 
/*      */     
/* 1323 */     if (rawType.isArray()) {
/* 1324 */       result = ArrayType.construct(_fromAny(context, rawType.getComponentType(), bindings), bindings);
/*      */     } else {
/*      */       JavaType superClass;
/*      */ 
/*      */       
/*      */       JavaType[] superInterfaces;
/*      */ 
/*      */       
/* 1332 */       if (rawType.isInterface()) {
/* 1333 */         superClass = null;
/* 1334 */         superInterfaces = _resolveSuperInterfaces(context, rawType, bindings);
/*      */       } else {
/*      */         
/* 1337 */         superClass = _resolveSuperClass(context, rawType, bindings);
/* 1338 */         superInterfaces = _resolveSuperInterfaces(context, rawType, bindings);
/*      */       } 
/*      */ 
/*      */       
/* 1342 */       if (rawType == Properties.class) {
/* 1343 */         result = MapType.construct(rawType, bindings, superClass, superInterfaces, CORE_TYPE_STRING, CORE_TYPE_STRING);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1348 */       else if (superClass != null) {
/* 1349 */         result = superClass.refine(rawType, bindings, superClass, superInterfaces);
/*      */       } 
/*      */       
/* 1352 */       if (result == null) {
/* 1353 */         result = _fromWellKnownClass(context, rawType, bindings, superClass, superInterfaces);
/* 1354 */         if (result == null) {
/* 1355 */           result = _fromWellKnownInterface(context, rawType, bindings, superClass, superInterfaces);
/* 1356 */           if (result == null)
/*      */           {
/* 1358 */             result = _newSimpleType(rawType, bindings, superClass, superInterfaces);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1363 */     context.resolveSelfReferences(result);
/*      */ 
/*      */     
/* 1366 */     if (!result.hasHandlers()) {
/* 1367 */       this._typeCache.putIfAbsent(key, result);
/*      */     }
/* 1369 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   protected JavaType _resolveSuperClass(ClassStack context, Class<?> rawType, TypeBindings parentBindings) {
/* 1374 */     Type parent = ClassUtil.getGenericSuperclass(rawType);
/* 1375 */     if (parent == null) {
/* 1376 */       return null;
/*      */     }
/* 1378 */     return _fromAny(context, parent, parentBindings);
/*      */   }
/*      */ 
/*      */   
/*      */   protected JavaType[] _resolveSuperInterfaces(ClassStack context, Class<?> rawType, TypeBindings parentBindings) {
/* 1383 */     Type[] types = ClassUtil.getGenericInterfaces(rawType);
/* 1384 */     if (types == null || types.length == 0) {
/* 1385 */       return NO_TYPES;
/*      */     }
/* 1387 */     int len = types.length;
/* 1388 */     JavaType[] resolved = new JavaType[len];
/* 1389 */     for (int i = 0; i < len; i++) {
/* 1390 */       Type type = types[i];
/* 1391 */       resolved[i] = _fromAny(context, type, parentBindings);
/*      */     } 
/* 1393 */     return resolved;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JavaType _fromWellKnownClass(ClassStack context, Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
/* 1404 */     if (bindings == null) {
/* 1405 */       bindings = EMPTY_BINDINGS;
/*      */     }
/*      */ 
/*      */     
/* 1409 */     if (rawType == Map.class) {
/* 1410 */       return _mapType(rawType, bindings, superClass, superInterfaces);
/*      */     }
/* 1412 */     if (rawType == Collection.class) {
/* 1413 */       return _collectionType(rawType, bindings, superClass, superInterfaces);
/*      */     }
/*      */     
/* 1416 */     if (rawType == AtomicReference.class) {
/* 1417 */       return _referenceType(rawType, bindings, superClass, superInterfaces);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1423 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JavaType _fromWellKnownInterface(ClassStack context, Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
/* 1431 */     int intCount = superInterfaces.length;
/*      */     
/* 1433 */     for (int i = 0; i < intCount; i++) {
/* 1434 */       JavaType result = superInterfaces[i].refine(rawType, bindings, superClass, superInterfaces);
/* 1435 */       if (result != null) {
/* 1436 */         return result;
/*      */       }
/*      */     } 
/* 1439 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JavaType _fromParamType(ClassStack context, ParameterizedType ptype, TypeBindings parentBindings) {
/*      */     TypeBindings newBindings;
/* 1450 */     Class<?> rawType = (Class)ptype.getRawType();
/*      */ 
/*      */ 
/*      */     
/* 1454 */     if (rawType == CLS_ENUM) {
/* 1455 */       return CORE_TYPE_ENUM;
/*      */     }
/* 1457 */     if (rawType == CLS_COMPARABLE) {
/* 1458 */       return CORE_TYPE_COMPARABLE;
/*      */     }
/* 1460 */     if (rawType == CLS_CLASS) {
/* 1461 */       return CORE_TYPE_CLASS;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1467 */     Type[] args = ptype.getActualTypeArguments();
/* 1468 */     int paramCount = (args == null) ? 0 : args.length;
/*      */ 
/*      */     
/* 1471 */     if (paramCount == 0) {
/* 1472 */       newBindings = EMPTY_BINDINGS;
/*      */     } else {
/* 1474 */       JavaType[] pt = new JavaType[paramCount];
/* 1475 */       for (int i = 0; i < paramCount; i++) {
/* 1476 */         pt[i] = _fromAny(context, args[i], parentBindings);
/*      */       }
/* 1478 */       newBindings = TypeBindings.create(rawType, pt);
/*      */     } 
/* 1480 */     return _fromClass(context, rawType, newBindings);
/*      */   }
/*      */ 
/*      */   
/*      */   protected JavaType _fromArrayType(ClassStack context, GenericArrayType type, TypeBindings bindings) {
/* 1485 */     JavaType elementType = _fromAny(context, type.getGenericComponentType(), bindings);
/* 1486 */     return ArrayType.construct(elementType, bindings);
/*      */   }
/*      */ 
/*      */   
/*      */   protected JavaType _fromVariable(ClassStack context, TypeVariable<?> var, TypeBindings bindings) {
/*      */     Type[] bounds;
/* 1492 */     String name = var.getName();
/* 1493 */     if (bindings == null) {
/* 1494 */       throw new IllegalArgumentException("Null `bindings` passed (type variable \"" + name + "\")");
/*      */     }
/* 1496 */     JavaType type = bindings.findBoundType(name);
/* 1497 */     if (type != null) {
/* 1498 */       return type;
/*      */     }
/*      */ 
/*      */     
/* 1502 */     if (bindings.hasUnbound(name)) {
/* 1503 */       return CORE_TYPE_OBJECT;
/*      */     }
/* 1505 */     bindings = bindings.withUnboundVariable(name);
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
/* 1516 */     synchronized (var) {
/* 1517 */       bounds = var.getBounds();
/*      */     } 
/* 1519 */     return _fromAny(context, bounds[0], bindings);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JavaType _fromWildcard(ClassStack context, WildcardType type, TypeBindings bindings) {
/* 1529 */     return _fromAny(context, type.getUpperBounds()[0], bindings);
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\type\TypeFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */