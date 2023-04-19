/*      */ package org.springframework.cglib.proxy;
/*      */ 
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.springframework.asm.ClassVisitor;
/*      */ import org.springframework.asm.Label;
/*      */ import org.springframework.asm.Type;
/*      */ import org.springframework.cglib.core.AbstractClassGenerator;
/*      */ import org.springframework.cglib.core.ClassEmitter;
/*      */ import org.springframework.cglib.core.CodeEmitter;
/*      */ import org.springframework.cglib.core.CodeGenerationException;
/*      */ import org.springframework.cglib.core.CollectionUtils;
/*      */ import org.springframework.cglib.core.Constants;
/*      */ import org.springframework.cglib.core.DuplicatesPredicate;
/*      */ import org.springframework.cglib.core.EmitUtils;
/*      */ import org.springframework.cglib.core.KeyFactory;
/*      */ import org.springframework.cglib.core.KeyFactoryCustomizer;
/*      */ import org.springframework.cglib.core.Local;
/*      */ import org.springframework.cglib.core.MethodInfo;
/*      */ import org.springframework.cglib.core.MethodInfoTransformer;
/*      */ import org.springframework.cglib.core.MethodWrapper;
/*      */ import org.springframework.cglib.core.ObjectSwitchCallback;
/*      */ import org.springframework.cglib.core.Predicate;
/*      */ import org.springframework.cglib.core.ProcessSwitchCallback;
/*      */ import org.springframework.cglib.core.ReflectUtils;
/*      */ import org.springframework.cglib.core.RejectModifierPredicate;
/*      */ import org.springframework.cglib.core.Signature;
/*      */ import org.springframework.cglib.core.Transformer;
/*      */ import org.springframework.cglib.core.TypeUtils;
/*      */ import org.springframework.cglib.core.VisibilityPredicate;
/*      */ import org.springframework.cglib.core.WeakCacheKey;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Enhancer
/*      */   extends AbstractClassGenerator
/*      */ {
/*   65 */   private static final CallbackFilter ALL_ZERO = new CallbackFilter() {
/*      */       public int accept(Method method) {
/*   67 */         return 0;
/*      */       }
/*      */     };
/*      */   
/*   71 */   private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(Enhancer.class.getName());
/*      */   
/*   73 */   private static final EnhancerKey KEY_FACTORY = (EnhancerKey)KeyFactory.create(EnhancerKey.class, (KeyFactoryCustomizer)KeyFactory.HASH_ASM_TYPE, null);
/*      */ 
/*      */   
/*      */   private static final String BOUND_FIELD = "CGLIB$BOUND";
/*      */ 
/*      */   
/*      */   private static final String FACTORY_DATA_FIELD = "CGLIB$FACTORY_DATA";
/*      */ 
/*      */   
/*      */   private static final String THREAD_CALLBACKS_FIELD = "CGLIB$THREAD_CALLBACKS";
/*      */   
/*      */   private static final String STATIC_CALLBACKS_FIELD = "CGLIB$STATIC_CALLBACKS";
/*      */   
/*      */   private static final String SET_THREAD_CALLBACKS_NAME = "CGLIB$SET_THREAD_CALLBACKS";
/*      */   
/*      */   private static final String SET_STATIC_CALLBACKS_NAME = "CGLIB$SET_STATIC_CALLBACKS";
/*      */   
/*      */   private static final String CONSTRUCTED_FIELD = "CGLIB$CONSTRUCTED";
/*      */   
/*      */   private static final String CALLBACK_FILTER_FIELD = "CGLIB$CALLBACK_FILTER";
/*      */   
/*   94 */   private static final Type OBJECT_TYPE = TypeUtils.parseType("Object");
/*      */   
/*   96 */   private static final Type FACTORY = TypeUtils.parseType("org.springframework.cglib.proxy.Factory");
/*      */   
/*   98 */   private static final Type ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
/*      */   
/*  100 */   private static final Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
/*      */   
/*  102 */   private static final Type THREAD_LOCAL = TypeUtils.parseType("ThreadLocal");
/*      */   
/*  104 */   private static final Type CALLBACK = TypeUtils.parseType("org.springframework.cglib.proxy.Callback");
/*      */   
/*  106 */   private static final Type CALLBACK_ARRAY = Type.getType(Callback[].class);
/*      */   
/*  108 */   private static final Signature CSTRUCT_NULL = TypeUtils.parseConstructor("");
/*  109 */   private static final Signature SET_THREAD_CALLBACKS = new Signature("CGLIB$SET_THREAD_CALLBACKS", Type.VOID_TYPE, new Type[] { CALLBACK_ARRAY });
/*      */   
/*  111 */   private static final Signature SET_STATIC_CALLBACKS = new Signature("CGLIB$SET_STATIC_CALLBACKS", Type.VOID_TYPE, new Type[] { CALLBACK_ARRAY });
/*      */   
/*  113 */   private static final Signature NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[] { CALLBACK_ARRAY });
/*      */   
/*  115 */   private static final Signature MULTIARG_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[] { Constants.TYPE_CLASS_ARRAY, Constants.TYPE_OBJECT_ARRAY, CALLBACK_ARRAY });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  121 */   private static final Signature SINGLE_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[] { CALLBACK });
/*      */   
/*  123 */   private static final Signature SET_CALLBACK = new Signature("setCallback", Type.VOID_TYPE, new Type[] { Type.INT_TYPE, CALLBACK });
/*      */   
/*  125 */   private static final Signature GET_CALLBACK = new Signature("getCallback", CALLBACK, new Type[] { Type.INT_TYPE });
/*      */   
/*  127 */   private static final Signature SET_CALLBACKS = new Signature("setCallbacks", Type.VOID_TYPE, new Type[] { CALLBACK_ARRAY });
/*      */   
/*  129 */   private static final Signature GET_CALLBACKS = new Signature("getCallbacks", CALLBACK_ARRAY, new Type[0]);
/*      */ 
/*      */   
/*  132 */   private static final Signature THREAD_LOCAL_GET = TypeUtils.parseSignature("Object get()");
/*      */   
/*  134 */   private static final Signature THREAD_LOCAL_SET = TypeUtils.parseSignature("void set(Object)");
/*      */   
/*  136 */   private static final Signature BIND_CALLBACKS = TypeUtils.parseSignature("void CGLIB$BIND_CALLBACKS(Object)");
/*      */ 
/*      */   
/*      */   private EnhancerFactoryData currentData;
/*      */ 
/*      */   
/*      */   private Object currentKey;
/*      */ 
/*      */   
/*      */   private Class[] interfaces;
/*      */ 
/*      */   
/*      */   private CallbackFilter filter;
/*      */ 
/*      */   
/*      */   private Callback[] callbacks;
/*      */   
/*      */   private Type[] callbackTypes;
/*      */   
/*      */   private boolean validateCallbackTypes;
/*      */   
/*      */   private boolean classOnly;
/*      */   
/*      */   private Class superclass;
/*      */   
/*      */   private Class[] argumentTypes;
/*      */   
/*      */   private Object[] arguments;
/*      */   
/*      */   private boolean useFactory = true;
/*      */   
/*      */   private Long serialVersionUID;
/*      */   
/*      */   private boolean interceptDuringConstruction = true;
/*      */ 
/*      */   
/*      */   public Enhancer() {
/*  173 */     super(SOURCE);
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
/*      */   public void setSuperclass(Class superclass) {
/*  186 */     if (superclass != null && superclass.isInterface()) {
/*  187 */       setInterfaces(new Class[] { superclass });
/*  188 */     } else if (superclass != null && superclass.equals(Object.class)) {
/*      */       
/*  190 */       this.superclass = null;
/*      */     } else {
/*  192 */       this.superclass = superclass;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInterfaces(Class[] interfaces) {
/*  203 */     this.interfaces = interfaces;
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
/*      */   public void setCallbackFilter(CallbackFilter filter) {
/*  215 */     this.filter = filter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallback(Callback callback) {
/*  226 */     setCallbacks(new Callback[] { callback });
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
/*      */   public void setCallbacks(Callback[] callbacks) {
/*  239 */     if (callbacks != null && callbacks.length == 0) {
/*  240 */       throw new IllegalArgumentException("Array cannot be empty");
/*      */     }
/*  242 */     this.callbacks = callbacks;
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
/*      */   public void setUseFactory(boolean useFactory) {
/*  255 */     this.useFactory = useFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInterceptDuringConstruction(boolean interceptDuringConstruction) {
/*  265 */     this.interceptDuringConstruction = interceptDuringConstruction;
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
/*      */   public void setCallbackType(Class callbackType) {
/*  277 */     setCallbackTypes(new Class[] { callbackType });
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
/*      */   public void setCallbackTypes(Class[] callbackTypes) {
/*  290 */     if (callbackTypes != null && callbackTypes.length == 0) {
/*  291 */       throw new IllegalArgumentException("Array cannot be empty");
/*      */     }
/*  293 */     this.callbackTypes = CallbackInfo.determineTypes(callbackTypes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object create() {
/*  303 */     this.classOnly = false;
/*  304 */     this.argumentTypes = null;
/*  305 */     return createHelper();
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
/*      */   public Object create(Class[] argumentTypes, Object[] arguments) {
/*  318 */     this.classOnly = false;
/*  319 */     if (argumentTypes == null || arguments == null || argumentTypes.length != arguments.length) {
/*  320 */       throw new IllegalArgumentException("Arguments must be non-null and of equal length");
/*      */     }
/*  322 */     this.argumentTypes = argumentTypes;
/*  323 */     this.arguments = arguments;
/*  324 */     return createHelper();
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
/*      */   public Class createClass() {
/*  336 */     this.classOnly = true;
/*  337 */     return (Class)createHelper();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSerialVersionUID(Long sUID) {
/*  345 */     this.serialVersionUID = sUID;
/*      */   }
/*      */   
/*      */   private void preValidate() {
/*  349 */     if (this.callbackTypes == null) {
/*  350 */       this.callbackTypes = CallbackInfo.determineTypes(this.callbacks, false);
/*  351 */       this.validateCallbackTypes = true;
/*      */     } 
/*  353 */     if (this.filter == null) {
/*  354 */       if (this.callbackTypes.length > 1) {
/*  355 */         throw new IllegalStateException("Multiple callback types possible but no filter specified");
/*      */       }
/*  357 */       this.filter = ALL_ZERO;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void validate() {
/*  362 */     if ((this.classOnly ^ ((this.callbacks == null) ? 1 : 0)) != 0) {
/*  363 */       if (this.classOnly) {
/*  364 */         throw new IllegalStateException("createClass does not accept callbacks");
/*      */       }
/*  366 */       throw new IllegalStateException("Callbacks are required");
/*      */     } 
/*      */     
/*  369 */     if (this.classOnly && this.callbackTypes == null) {
/*  370 */       throw new IllegalStateException("Callback types are required");
/*      */     }
/*  372 */     if (this.validateCallbackTypes) {
/*  373 */       this.callbackTypes = null;
/*      */     }
/*  375 */     if (this.callbacks != null && this.callbackTypes != null) {
/*  376 */       if (this.callbacks.length != this.callbackTypes.length) {
/*  377 */         throw new IllegalStateException("Lengths of callback and callback types array must be the same");
/*      */       }
/*  379 */       Type[] check = CallbackInfo.determineTypes(this.callbacks);
/*  380 */       for (int i = 0; i < check.length; i++) {
/*  381 */         if (!check[i].equals(this.callbackTypes[i])) {
/*  382 */           throw new IllegalStateException("Callback " + check[i] + " is not assignable to " + this.callbackTypes[i]);
/*      */         }
/*      */       } 
/*  385 */     } else if (this.callbacks != null) {
/*  386 */       this.callbackTypes = CallbackInfo.determineTypes(this.callbacks);
/*      */     } 
/*  388 */     if (this.interfaces != null) {
/*  389 */       for (int i = 0; i < this.interfaces.length; i++) {
/*  390 */         if (this.interfaces[i] == null) {
/*  391 */           throw new IllegalStateException("Interfaces cannot be null");
/*      */         }
/*  393 */         if (!this.interfaces[i].isInterface()) {
/*  394 */           throw new IllegalStateException(this.interfaces[i] + " is not an interface");
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class EnhancerFactoryData
/*      */   {
/*      */     public final Class generatedClass;
/*      */     
/*      */     private final Method setThreadCallbacks;
/*      */     
/*      */     private final Class[] primaryConstructorArgTypes;
/*      */     
/*      */     private final Constructor primaryConstructor;
/*      */     
/*      */     public EnhancerFactoryData(Class generatedClass, Class[] primaryConstructorArgTypes, boolean classOnly) {
/*  412 */       this.generatedClass = generatedClass;
/*      */       try {
/*  414 */         this.setThreadCallbacks = Enhancer.getCallbacksSetter(generatedClass, "CGLIB$SET_THREAD_CALLBACKS");
/*  415 */         if (classOnly) {
/*  416 */           this.primaryConstructorArgTypes = null;
/*  417 */           this.primaryConstructor = null;
/*      */         } else {
/*  419 */           this.primaryConstructorArgTypes = primaryConstructorArgTypes;
/*  420 */           this.primaryConstructor = ReflectUtils.getConstructor(generatedClass, primaryConstructorArgTypes);
/*      */         } 
/*  422 */       } catch (NoSuchMethodException e) {
/*  423 */         throw new CodeGenerationException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object newInstance(Class[] argumentTypes, Object[] arguments, Callback[] callbacks) {
/*  441 */       setThreadCallbacks(callbacks);
/*      */       
/*      */       try {
/*  444 */         if (this.primaryConstructorArgTypes == argumentTypes || 
/*  445 */           Arrays.equals((Object[])this.primaryConstructorArgTypes, (Object[])argumentTypes))
/*      */         {
/*      */           
/*  448 */           return ReflectUtils.newInstance(this.primaryConstructor, arguments);
/*      */         }
/*      */         
/*  451 */         return ReflectUtils.newInstance(this.generatedClass, argumentTypes, arguments);
/*      */       } finally {
/*      */         
/*  454 */         setThreadCallbacks(null);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void setThreadCallbacks(Callback[] callbacks) {
/*      */       try {
/*  461 */         this.setThreadCallbacks.invoke(this.generatedClass, new Object[] { callbacks });
/*  462 */       } catch (IllegalAccessException e) {
/*  463 */         throw new CodeGenerationException(e);
/*  464 */       } catch (InvocationTargetException e) {
/*  465 */         throw new CodeGenerationException(e.getTargetException());
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private Object createHelper() {
/*  471 */     preValidate();
/*  472 */     Object key = KEY_FACTORY.newInstance((this.superclass != null) ? this.superclass.getName() : null, 
/*  473 */         ReflectUtils.getNames(this.interfaces), (this.filter == ALL_ZERO) ? null : new WeakCacheKey(this.filter), this.callbackTypes, this.useFactory, this.interceptDuringConstruction, this.serialVersionUID);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  479 */     this.currentKey = key;
/*  480 */     Object result = create(key);
/*  481 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Class generate(AbstractClassGenerator.ClassLoaderData data) {
/*  486 */     validate();
/*  487 */     if (this.superclass != null) {
/*  488 */       setNamePrefix(this.superclass.getName());
/*  489 */     } else if (this.interfaces != null) {
/*  490 */       setNamePrefix(this.interfaces[ReflectUtils.findPackageProtected(this.interfaces)].getName());
/*      */     } 
/*  492 */     return super.generate(data);
/*      */   }
/*      */   
/*      */   protected ClassLoader getDefaultClassLoader() {
/*  496 */     if (this.superclass != null)
/*  497 */       return this.superclass.getClassLoader(); 
/*  498 */     if (this.interfaces != null) {
/*  499 */       return this.interfaces[0].getClassLoader();
/*      */     }
/*  501 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected ProtectionDomain getProtectionDomain() {
/*  506 */     if (this.superclass != null)
/*  507 */       return ReflectUtils.getProtectionDomain(this.superclass); 
/*  508 */     if (this.interfaces != null) {
/*  509 */       return ReflectUtils.getProtectionDomain(this.interfaces[0]);
/*      */     }
/*  511 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private Signature rename(Signature sig, int index) {
/*  516 */     return new Signature("CGLIB$" + sig.getName() + "$" + index, sig
/*  517 */         .getDescriptor());
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
/*      */   public static void getMethods(Class superclass, Class[] interfaces, List methods) {
/*  535 */     getMethods(superclass, interfaces, methods, (List)null, (Set)null);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void getMethods(Class superclass, Class[] interfaces, List methods, List interfaceMethods, Set forcePublic) {
/*  540 */     ReflectUtils.addAllMethods(superclass, methods);
/*  541 */     List target = (interfaceMethods != null) ? interfaceMethods : methods;
/*  542 */     if (interfaces != null) {
/*  543 */       for (int i = 0; i < interfaces.length; i++) {
/*  544 */         if (interfaces[i] != Factory.class) {
/*  545 */           ReflectUtils.addAllMethods(interfaces[i], target);
/*      */         }
/*      */       } 
/*      */     }
/*  549 */     if (interfaceMethods != null) {
/*  550 */       if (forcePublic != null) {
/*  551 */         forcePublic.addAll(MethodWrapper.createSet(interfaceMethods));
/*      */       }
/*  553 */       methods.addAll(interfaceMethods);
/*      */     } 
/*  555 */     CollectionUtils.filter(methods, (Predicate)new RejectModifierPredicate(8));
/*  556 */     CollectionUtils.filter(methods, (Predicate)new VisibilityPredicate(superclass, true));
/*  557 */     CollectionUtils.filter(methods, (Predicate)new DuplicatesPredicate());
/*  558 */     CollectionUtils.filter(methods, (Predicate)new RejectModifierPredicate(16));
/*      */   }
/*      */   
/*      */   public void generateClass(ClassVisitor v) throws Exception {
/*  562 */     Class sc = (this.superclass == null) ? Object.class : this.superclass;
/*      */     
/*  564 */     if (TypeUtils.isFinal(sc.getModifiers()))
/*  565 */       throw new IllegalArgumentException("Cannot subclass final class " + sc.getName()); 
/*  566 */     List constructors = new ArrayList(Arrays.asList((Object[])sc.getDeclaredConstructors()));
/*  567 */     filterConstructors(sc, constructors);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  572 */     List actualMethods = new ArrayList();
/*  573 */     List interfaceMethods = new ArrayList();
/*  574 */     final Set forcePublic = new HashSet();
/*  575 */     getMethods(sc, this.interfaces, actualMethods, interfaceMethods, forcePublic);
/*      */     
/*  577 */     List methods = CollectionUtils.transform(actualMethods, new Transformer() {
/*      */           public Object transform(Object value) {
/*  579 */             Method method = (Method)value;
/*      */             
/*  581 */             int modifiers = 0x10 | method.getModifiers() & 0xFFFFFBFF & 0xFFFFFEFF & 0xFFFFFFDF;
/*      */ 
/*      */ 
/*      */             
/*  585 */             if (forcePublic.contains(MethodWrapper.create(method))) {
/*  586 */               modifiers = modifiers & 0xFFFFFFFB | 0x1;
/*      */             }
/*  588 */             return ReflectUtils.getMethodInfo(method, modifiers);
/*      */           }
/*      */         });
/*      */     
/*  592 */     ClassEmitter e = new ClassEmitter(v);
/*  593 */     if (this.currentData == null) {
/*  594 */       e.begin_class(46, 1, 
/*      */           
/*  596 */           getClassName(), 
/*  597 */           Type.getType(sc), this.useFactory ? 
/*      */           
/*  599 */           TypeUtils.add(TypeUtils.getTypes(this.interfaces), FACTORY) : 
/*  600 */           TypeUtils.getTypes(this.interfaces), "<generated>");
/*      */     } else {
/*      */       
/*  603 */       e.begin_class(46, 1, 
/*      */           
/*  605 */           getClassName(), null, new Type[] { FACTORY }, "<generated>");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  610 */     List constructorInfo = CollectionUtils.transform(constructors, (Transformer)MethodInfoTransformer.getInstance());
/*      */     
/*  612 */     e.declare_field(2, "CGLIB$BOUND", Type.BOOLEAN_TYPE, null);
/*  613 */     e.declare_field(9, "CGLIB$FACTORY_DATA", OBJECT_TYPE, null);
/*  614 */     if (!this.interceptDuringConstruction) {
/*  615 */       e.declare_field(2, "CGLIB$CONSTRUCTED", Type.BOOLEAN_TYPE, null);
/*      */     }
/*  617 */     e.declare_field(26, "CGLIB$THREAD_CALLBACKS", THREAD_LOCAL, null);
/*  618 */     e.declare_field(26, "CGLIB$STATIC_CALLBACKS", CALLBACK_ARRAY, null);
/*  619 */     if (this.serialVersionUID != null) {
/*  620 */       e.declare_field(26, "serialVersionUID", Type.LONG_TYPE, this.serialVersionUID);
/*      */     }
/*      */     
/*  623 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/*  624 */       e.declare_field(2, getCallbackField(i), this.callbackTypes[i], null);
/*      */     }
/*      */     
/*  627 */     e.declare_field(10, "CGLIB$CALLBACK_FILTER", OBJECT_TYPE, null);
/*      */     
/*  629 */     if (this.currentData == null) {
/*  630 */       emitMethods(e, methods, actualMethods);
/*  631 */       emitConstructors(e, constructorInfo);
/*      */     } else {
/*  633 */       emitDefaultConstructor(e);
/*      */     } 
/*  635 */     emitSetThreadCallbacks(e);
/*  636 */     emitSetStaticCallbacks(e);
/*  637 */     emitBindCallbacks(e);
/*      */     
/*  639 */     if (this.useFactory || this.currentData != null) {
/*  640 */       int[] keys = getCallbackKeys();
/*  641 */       emitNewInstanceCallbacks(e);
/*  642 */       emitNewInstanceCallback(e);
/*  643 */       emitNewInstanceMultiarg(e, constructorInfo);
/*  644 */       emitGetCallback(e, keys);
/*  645 */       emitSetCallback(e, keys);
/*  646 */       emitGetCallbacks(e);
/*  647 */       emitSetCallbacks(e);
/*      */     } 
/*      */     
/*  650 */     e.end_class();
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
/*      */   protected void filterConstructors(Class sc, List constructors) {
/*  664 */     CollectionUtils.filter(constructors, (Predicate)new VisibilityPredicate(sc, true));
/*  665 */     if (constructors.size() == 0) {
/*  666 */       throw new IllegalArgumentException("No visible constructors in " + sc);
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
/*      */   protected Object firstInstance(Class type) throws Exception {
/*  680 */     if (this.classOnly) {
/*  681 */       return type;
/*      */     }
/*  683 */     return createUsingReflection(type);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object nextInstance(Object instance) {
/*  688 */     EnhancerFactoryData data = (EnhancerFactoryData)instance;
/*      */     
/*  690 */     if (this.classOnly) {
/*  691 */       return data.generatedClass;
/*      */     }
/*      */     
/*  694 */     Class[] argumentTypes = this.argumentTypes;
/*  695 */     Object[] arguments = this.arguments;
/*  696 */     if (argumentTypes == null) {
/*  697 */       argumentTypes = Constants.EMPTY_CLASS_ARRAY;
/*  698 */       arguments = null;
/*      */     } 
/*  700 */     return data.newInstance(argumentTypes, arguments, this.callbacks);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object wrapCachedClass(Class klass) {
/*  705 */     Class[] argumentTypes = this.argumentTypes;
/*  706 */     if (argumentTypes == null) {
/*  707 */       argumentTypes = Constants.EMPTY_CLASS_ARRAY;
/*      */     }
/*  709 */     EnhancerFactoryData factoryData = new EnhancerFactoryData(klass, argumentTypes, this.classOnly);
/*  710 */     Field factoryDataField = null;
/*      */ 
/*      */     
/*      */     try {
/*  714 */       factoryDataField = klass.getField("CGLIB$FACTORY_DATA");
/*  715 */       factoryDataField.set((Object)null, factoryData);
/*  716 */       Field callbackFilterField = klass.getDeclaredField("CGLIB$CALLBACK_FILTER");
/*  717 */       callbackFilterField.setAccessible(true);
/*  718 */       callbackFilterField.set((Object)null, this.filter);
/*  719 */     } catch (NoSuchFieldException e) {
/*  720 */       throw new CodeGenerationException(e);
/*  721 */     } catch (IllegalAccessException e) {
/*  722 */       throw new CodeGenerationException(e);
/*      */     } 
/*  724 */     return new WeakReference<EnhancerFactoryData>(factoryData);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object unwrapCachedValue(Object cached) {
/*  729 */     if (this.currentKey instanceof EnhancerKey) {
/*  730 */       EnhancerFactoryData data = ((WeakReference<EnhancerFactoryData>)cached).get();
/*  731 */       return data;
/*      */     } 
/*  733 */     return super.unwrapCachedValue(cached);
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
/*      */   public static void registerCallbacks(Class generatedClass, Callback[] callbacks) {
/*  760 */     setThreadCallbacks(generatedClass, callbacks);
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
/*      */   public static void registerStaticCallbacks(Class generatedClass, Callback[] callbacks) {
/*  773 */     setCallbacksHelper(generatedClass, callbacks, "CGLIB$SET_STATIC_CALLBACKS");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEnhanced(Class type) {
/*      */     try {
/*  783 */       getCallbacksSetter(type, "CGLIB$SET_THREAD_CALLBACKS");
/*  784 */       return true;
/*  785 */     } catch (NoSuchMethodException e) {
/*  786 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void setThreadCallbacks(Class type, Callback[] callbacks) {
/*  791 */     setCallbacksHelper(type, callbacks, "CGLIB$SET_THREAD_CALLBACKS");
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setCallbacksHelper(Class type, Callback[] callbacks, String methodName) {
/*      */     try {
/*  797 */       Method setter = getCallbacksSetter(type, methodName);
/*  798 */       setter.invoke(null, new Object[] { callbacks });
/*  799 */     } catch (NoSuchMethodException e) {
/*  800 */       throw new IllegalArgumentException(type + " is not an enhanced class");
/*  801 */     } catch (IllegalAccessException e) {
/*  802 */       throw new CodeGenerationException(e);
/*  803 */     } catch (InvocationTargetException e) {
/*  804 */       throw new CodeGenerationException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static Method getCallbacksSetter(Class type, String methodName) throws NoSuchMethodException {
/*  809 */     return type.getDeclaredMethod(methodName, new Class[] { Callback[].class });
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
/*      */   private Object createUsingReflection(Class type) {
/*  822 */     setThreadCallbacks(type, this.callbacks);
/*      */     
/*      */     try {
/*  825 */       if (this.argumentTypes != null)
/*      */       {
/*  827 */         return ReflectUtils.newInstance(type, this.argumentTypes, this.arguments);
/*      */       }
/*      */ 
/*      */       
/*  831 */       return ReflectUtils.newInstance(type);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  836 */       setThreadCallbacks(type, (Callback[])null);
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
/*      */   public static Object create(Class type, Callback callback) {
/*  848 */     Enhancer e = new Enhancer();
/*  849 */     e.setSuperclass(type);
/*  850 */     e.setCallback(callback);
/*  851 */     return e.create();
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
/*      */   public static Object create(Class superclass, Class[] interfaces, Callback callback) {
/*  863 */     Enhancer e = new Enhancer();
/*  864 */     e.setSuperclass(superclass);
/*  865 */     e.setInterfaces(interfaces);
/*  866 */     e.setCallback(callback);
/*  867 */     return e.create();
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
/*      */   public static Object create(Class superclass, Class[] interfaces, CallbackFilter filter, Callback[] callbacks) {
/*  880 */     Enhancer e = new Enhancer();
/*  881 */     e.setSuperclass(superclass);
/*  882 */     e.setInterfaces(interfaces);
/*  883 */     e.setCallbackFilter(filter);
/*  884 */     e.setCallbacks(callbacks);
/*  885 */     return e.create();
/*      */   }
/*      */   
/*      */   private void emitDefaultConstructor(ClassEmitter ce) {
/*      */     Constructor<Object> declaredConstructor;
/*      */     try {
/*  891 */       declaredConstructor = Object.class.getDeclaredConstructor(new Class[0]);
/*  892 */     } catch (NoSuchMethodException noSuchMethodException) {
/*  893 */       throw new IllegalStateException("Object should have default constructor ", noSuchMethodException);
/*      */     } 
/*  895 */     MethodInfo constructor = (MethodInfo)MethodInfoTransformer.getInstance().transform(declaredConstructor);
/*  896 */     CodeEmitter e = EmitUtils.begin_method(ce, constructor, 1);
/*  897 */     e.load_this();
/*  898 */     e.dup();
/*  899 */     Signature sig = constructor.getSignature();
/*  900 */     e.super_invoke_constructor(sig);
/*  901 */     e.return_value();
/*  902 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitConstructors(ClassEmitter ce, List constructors) {
/*  906 */     boolean seenNull = false;
/*  907 */     for (Iterator<MethodInfo> it = constructors.iterator(); it.hasNext(); ) {
/*  908 */       MethodInfo constructor = it.next();
/*  909 */       if (this.currentData != null && !"()V".equals(constructor.getSignature().getDescriptor())) {
/*      */         continue;
/*      */       }
/*  912 */       CodeEmitter e = EmitUtils.begin_method(ce, constructor, 1);
/*  913 */       e.load_this();
/*  914 */       e.dup();
/*  915 */       e.load_args();
/*  916 */       Signature sig = constructor.getSignature();
/*  917 */       seenNull = (seenNull || sig.getDescriptor().equals("()V"));
/*  918 */       e.super_invoke_constructor(sig);
/*  919 */       if (this.currentData == null) {
/*  920 */         e.invoke_static_this(BIND_CALLBACKS);
/*  921 */         if (!this.interceptDuringConstruction) {
/*  922 */           e.load_this();
/*  923 */           e.push(1);
/*  924 */           e.putfield("CGLIB$CONSTRUCTED");
/*      */         } 
/*      */       } 
/*  927 */       e.return_value();
/*  928 */       e.end_method();
/*      */     } 
/*  930 */     if (!this.classOnly && !seenNull && this.arguments == null)
/*  931 */       throw new IllegalArgumentException("Superclass has no null constructors but no arguments were given"); 
/*      */   }
/*      */   
/*      */   private int[] getCallbackKeys() {
/*  935 */     int[] keys = new int[this.callbackTypes.length];
/*  936 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/*  937 */       keys[i] = i;
/*      */     }
/*  939 */     return keys;
/*      */   }
/*      */   
/*      */   private void emitGetCallback(ClassEmitter ce, int[] keys) {
/*  943 */     final CodeEmitter e = ce.begin_method(1, GET_CALLBACK, null);
/*  944 */     e.load_this();
/*  945 */     e.invoke_static_this(BIND_CALLBACKS);
/*  946 */     e.load_this();
/*  947 */     e.load_arg(0);
/*  948 */     e.process_switch(keys, new ProcessSwitchCallback() {
/*      */           public void processCase(int key, Label end) {
/*  950 */             e.getfield(Enhancer.getCallbackField(key));
/*  951 */             e.goTo(end);
/*      */           }
/*      */           public void processDefault() {
/*  954 */             e.pop();
/*  955 */             e.aconst_null();
/*      */           }
/*      */         });
/*  958 */     e.return_value();
/*  959 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetCallback(ClassEmitter ce, int[] keys) {
/*  963 */     final CodeEmitter e = ce.begin_method(1, SET_CALLBACK, null);
/*  964 */     e.load_arg(0);
/*  965 */     e.process_switch(keys, new ProcessSwitchCallback() {
/*      */           public void processCase(int key, Label end) {
/*  967 */             e.load_this();
/*  968 */             e.load_arg(1);
/*  969 */             e.checkcast(Enhancer.this.callbackTypes[key]);
/*  970 */             e.putfield(Enhancer.getCallbackField(key));
/*  971 */             e.goTo(end);
/*      */           }
/*      */ 
/*      */           
/*      */           public void processDefault() {}
/*      */         });
/*  977 */     e.return_value();
/*  978 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetCallbacks(ClassEmitter ce) {
/*  982 */     CodeEmitter e = ce.begin_method(1, SET_CALLBACKS, null);
/*  983 */     e.load_this();
/*  984 */     e.load_arg(0);
/*  985 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/*  986 */       e.dup2();
/*  987 */       e.aaload(i);
/*  988 */       e.checkcast(this.callbackTypes[i]);
/*  989 */       e.putfield(getCallbackField(i));
/*      */     } 
/*  991 */     e.return_value();
/*  992 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitGetCallbacks(ClassEmitter ce) {
/*  996 */     CodeEmitter e = ce.begin_method(1, GET_CALLBACKS, null);
/*  997 */     e.load_this();
/*  998 */     e.invoke_static_this(BIND_CALLBACKS);
/*  999 */     e.load_this();
/* 1000 */     e.push(this.callbackTypes.length);
/* 1001 */     e.newarray(CALLBACK);
/* 1002 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1003 */       e.dup();
/* 1004 */       e.push(i);
/* 1005 */       e.load_this();
/* 1006 */       e.getfield(getCallbackField(i));
/* 1007 */       e.aastore();
/*      */     } 
/* 1009 */     e.return_value();
/* 1010 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitNewInstanceCallbacks(ClassEmitter ce) {
/* 1014 */     CodeEmitter e = ce.begin_method(1, NEW_INSTANCE, null);
/* 1015 */     Type thisType = getThisType(e);
/* 1016 */     e.load_arg(0);
/* 1017 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS);
/* 1018 */     emitCommonNewInstance(e);
/*      */   }
/*      */   
/*      */   private Type getThisType(CodeEmitter e) {
/* 1022 */     if (this.currentData == null) {
/* 1023 */       return e.getClassEmitter().getClassType();
/*      */     }
/* 1025 */     return Type.getType(this.currentData.generatedClass);
/*      */   }
/*      */ 
/*      */   
/*      */   private void emitCommonNewInstance(CodeEmitter e) {
/* 1030 */     Type thisType = getThisType(e);
/* 1031 */     e.new_instance(thisType);
/* 1032 */     e.dup();
/* 1033 */     e.invoke_constructor(thisType);
/* 1034 */     e.aconst_null();
/* 1035 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS);
/* 1036 */     e.return_value();
/* 1037 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitNewInstanceCallback(ClassEmitter ce) {
/* 1041 */     CodeEmitter e = ce.begin_method(1, SINGLE_NEW_INSTANCE, null);
/* 1042 */     switch (this.callbackTypes.length) {
/*      */       case 0:
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1:
/* 1048 */         e.push(1);
/* 1049 */         e.newarray(CALLBACK);
/* 1050 */         e.dup();
/* 1051 */         e.push(0);
/* 1052 */         e.load_arg(0);
/* 1053 */         e.aastore();
/* 1054 */         e.invoke_static(getThisType(e), SET_THREAD_CALLBACKS);
/*      */         break;
/*      */       default:
/* 1057 */         e.throw_exception(ILLEGAL_STATE_EXCEPTION, "More than one callback object required"); break;
/*      */     } 
/* 1059 */     emitCommonNewInstance(e);
/*      */   }
/*      */   
/*      */   private void emitNewInstanceMultiarg(ClassEmitter ce, List constructors) {
/* 1063 */     final CodeEmitter e = ce.begin_method(1, MULTIARG_NEW_INSTANCE, null);
/* 1064 */     final Type thisType = getThisType(e);
/* 1065 */     e.load_arg(2);
/* 1066 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS);
/* 1067 */     e.new_instance(thisType);
/* 1068 */     e.dup();
/* 1069 */     e.load_arg(0);
/* 1070 */     EmitUtils.constructor_switch(e, constructors, new ObjectSwitchCallback() {
/*      */           public void processCase(Object key, Label end) {
/* 1072 */             MethodInfo constructor = (MethodInfo)key;
/* 1073 */             Type[] types = constructor.getSignature().getArgumentTypes();
/* 1074 */             for (int i = 0; i < types.length; i++) {
/* 1075 */               e.load_arg(1);
/* 1076 */               e.push(i);
/* 1077 */               e.aaload();
/* 1078 */               e.unbox(types[i]);
/*      */             } 
/* 1080 */             e.invoke_constructor(thisType, constructor.getSignature());
/* 1081 */             e.goTo(end);
/*      */           }
/*      */           public void processDefault() {
/* 1084 */             e.throw_exception(Enhancer.ILLEGAL_ARGUMENT_EXCEPTION, "Constructor not found");
/*      */           }
/*      */         });
/* 1087 */     e.aconst_null();
/* 1088 */     e.invoke_static(thisType, SET_THREAD_CALLBACKS);
/* 1089 */     e.return_value();
/* 1090 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitMethods(ClassEmitter ce, List methods, List<E> actualMethods) {
/* 1094 */     CallbackGenerator[] generators = CallbackInfo.getGenerators(this.callbackTypes);
/*      */     
/* 1096 */     Map<Object, Object> groups = new HashMap<Object, Object>();
/* 1097 */     final Map<Object, Object> indexes = new HashMap<Object, Object>();
/* 1098 */     final Map<Object, Object> originalModifiers = new HashMap<Object, Object>();
/* 1099 */     final Map positions = CollectionUtils.getIndexMap(methods);
/* 1100 */     Map<Object, Object> declToBridge = new HashMap<Object, Object>();
/*      */     
/* 1102 */     Iterator<MethodInfo> it1 = methods.iterator();
/* 1103 */     Iterator<E> it2 = (actualMethods != null) ? actualMethods.iterator() : null;
/*      */     
/* 1105 */     while (it1.hasNext()) {
/* 1106 */       MethodInfo method = it1.next();
/* 1107 */       Method actualMethod = (it2 != null) ? (Method)it2.next() : null;
/* 1108 */       int index = this.filter.accept(actualMethod);
/* 1109 */       if (index >= this.callbackTypes.length) {
/* 1110 */         throw new IllegalArgumentException("Callback filter returned an index that is too large: " + index);
/*      */       }
/* 1112 */       originalModifiers.put(method, new Integer((actualMethod != null) ? actualMethod.getModifiers() : method.getModifiers()));
/* 1113 */       indexes.put(method, new Integer(index));
/* 1114 */       List<MethodInfo> group = (List)groups.get(generators[index]);
/* 1115 */       if (group == null) {
/* 1116 */         groups.put(generators[index], group = new ArrayList(methods.size()));
/*      */       }
/* 1118 */       group.add(method);
/*      */ 
/*      */ 
/*      */       
/* 1122 */       if (TypeUtils.isBridge(actualMethod.getModifiers())) {
/* 1123 */         Set<Signature> bridges = (Set)declToBridge.get(actualMethod.getDeclaringClass());
/* 1124 */         if (bridges == null) {
/* 1125 */           bridges = new HashSet();
/* 1126 */           declToBridge.put(actualMethod.getDeclaringClass(), bridges);
/*      */         } 
/* 1128 */         bridges.add(method.getSignature());
/*      */       } 
/*      */     } 
/*      */     
/* 1132 */     final Map bridgeToTarget = (new BridgeMethodResolver(declToBridge, getClassLoader())).resolveAll();
/*      */     
/* 1134 */     Set<CallbackGenerator> seenGen = new HashSet();
/* 1135 */     CodeEmitter se = ce.getStaticHook();
/* 1136 */     se.new_instance(THREAD_LOCAL);
/* 1137 */     se.dup();
/* 1138 */     se.invoke_constructor(THREAD_LOCAL, CSTRUCT_NULL);
/* 1139 */     se.putfield("CGLIB$THREAD_CALLBACKS");
/*      */     
/* 1141 */     Object[] state = new Object[1];
/* 1142 */     CallbackGenerator.Context context = new CallbackGenerator.Context() {
/*      */         public ClassLoader getClassLoader() {
/* 1144 */           return Enhancer.this.getClassLoader();
/*      */         }
/*      */         public int getOriginalModifiers(MethodInfo method) {
/* 1147 */           return ((Integer)originalModifiers.get(method)).intValue();
/*      */         }
/*      */         public int getIndex(MethodInfo method) {
/* 1150 */           return ((Integer)indexes.get(method)).intValue();
/*      */         }
/*      */         public void emitCallback(CodeEmitter e, int index) {
/* 1153 */           Enhancer.this.emitCurrentCallback(e, index);
/*      */         }
/*      */         public Signature getImplSignature(MethodInfo method) {
/* 1156 */           return Enhancer.this.rename(method.getSignature(), ((Integer)positions.get(method)).intValue());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public void emitLoadArgsAndInvoke(CodeEmitter e, MethodInfo method) {
/* 1163 */           Signature bridgeTarget = (Signature)bridgeToTarget.get(method.getSignature());
/* 1164 */           if (bridgeTarget != null) {
/*      */             
/* 1166 */             for (int i = 0; i < (bridgeTarget.getArgumentTypes()).length; i++) {
/* 1167 */               e.load_arg(i);
/* 1168 */               Type target = bridgeTarget.getArgumentTypes()[i];
/* 1169 */               if (!target.equals(method.getSignature().getArgumentTypes()[i])) {
/* 1170 */                 e.checkcast(target);
/*      */               }
/*      */             } 
/*      */             
/* 1174 */             e.invoke_virtual_this(bridgeTarget);
/*      */             
/* 1176 */             Type retType = method.getSignature().getReturnType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1187 */             if (!retType.equals(bridgeTarget.getReturnType())) {
/* 1188 */               e.checkcast(retType);
/*      */             }
/*      */           } else {
/* 1191 */             e.load_args();
/* 1192 */             e.super_invoke(method.getSignature());
/*      */           } 
/*      */         }
/*      */         public CodeEmitter beginMethod(ClassEmitter ce, MethodInfo method) {
/* 1196 */           CodeEmitter e = EmitUtils.begin_method(ce, method);
/* 1197 */           if (!Enhancer.this.interceptDuringConstruction && 
/* 1198 */             !TypeUtils.isAbstract(method.getModifiers())) {
/* 1199 */             Label constructed = e.make_label();
/* 1200 */             e.load_this();
/* 1201 */             e.getfield("CGLIB$CONSTRUCTED");
/* 1202 */             e.if_jump(154, constructed);
/* 1203 */             e.load_this();
/* 1204 */             e.load_args();
/* 1205 */             e.super_invoke();
/* 1206 */             e.return_value();
/* 1207 */             e.mark(constructed);
/*      */           } 
/* 1209 */           return e;
/*      */         }
/*      */       };
/* 1212 */     for (int i = 0; i < this.callbackTypes.length; i++) {
/* 1213 */       CallbackGenerator gen = generators[i];
/* 1214 */       if (!seenGen.contains(gen)) {
/* 1215 */         seenGen.add(gen);
/* 1216 */         List fmethods = (List)groups.get(gen);
/* 1217 */         if (fmethods != null) {
/*      */           try {
/* 1219 */             gen.generate(ce, context, fmethods);
/* 1220 */             gen.generateStatic(se, context, fmethods);
/* 1221 */           } catch (RuntimeException x) {
/* 1222 */             throw x;
/* 1223 */           } catch (Exception x) {
/* 1224 */             throw new CodeGenerationException(x);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1229 */     se.return_value();
/* 1230 */     se.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetThreadCallbacks(ClassEmitter ce) {
/* 1234 */     CodeEmitter e = ce.begin_method(9, SET_THREAD_CALLBACKS, null);
/*      */ 
/*      */     
/* 1237 */     e.getfield("CGLIB$THREAD_CALLBACKS");
/* 1238 */     e.load_arg(0);
/* 1239 */     e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_SET);
/* 1240 */     e.return_value();
/* 1241 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitSetStaticCallbacks(ClassEmitter ce) {
/* 1245 */     CodeEmitter e = ce.begin_method(9, SET_STATIC_CALLBACKS, null);
/*      */ 
/*      */     
/* 1248 */     e.load_arg(0);
/* 1249 */     e.putfield("CGLIB$STATIC_CALLBACKS");
/* 1250 */     e.return_value();
/* 1251 */     e.end_method();
/*      */   }
/*      */   
/*      */   private void emitCurrentCallback(CodeEmitter e, int index) {
/* 1255 */     e.load_this();
/* 1256 */     e.getfield(getCallbackField(index));
/* 1257 */     e.dup();
/* 1258 */     Label end = e.make_label();
/* 1259 */     e.ifnonnull(end);
/* 1260 */     e.pop();
/* 1261 */     e.load_this();
/* 1262 */     e.invoke_static_this(BIND_CALLBACKS);
/* 1263 */     e.load_this();
/* 1264 */     e.getfield(getCallbackField(index));
/* 1265 */     e.mark(end);
/*      */   }
/*      */   
/*      */   private void emitBindCallbacks(ClassEmitter ce) {
/* 1269 */     CodeEmitter e = ce.begin_method(26, BIND_CALLBACKS, null);
/*      */ 
/*      */     
/* 1272 */     Local me = e.make_local();
/* 1273 */     e.load_arg(0);
/* 1274 */     e.checkcast_this();
/* 1275 */     e.store_local(me);
/*      */     
/* 1277 */     Label end = e.make_label();
/* 1278 */     e.load_local(me);
/* 1279 */     e.getfield("CGLIB$BOUND");
/* 1280 */     e.if_jump(154, end);
/* 1281 */     e.load_local(me);
/* 1282 */     e.push(1);
/* 1283 */     e.putfield("CGLIB$BOUND");
/*      */     
/* 1285 */     e.getfield("CGLIB$THREAD_CALLBACKS");
/* 1286 */     e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_GET);
/* 1287 */     e.dup();
/* 1288 */     Label found_callback = e.make_label();
/* 1289 */     e.ifnonnull(found_callback);
/* 1290 */     e.pop();
/*      */     
/* 1292 */     e.getfield("CGLIB$STATIC_CALLBACKS");
/* 1293 */     e.dup();
/* 1294 */     e.ifnonnull(found_callback);
/* 1295 */     e.pop();
/* 1296 */     e.goTo(end);
/*      */     
/* 1298 */     e.mark(found_callback);
/* 1299 */     e.checkcast(CALLBACK_ARRAY);
/* 1300 */     e.load_local(me);
/* 1301 */     e.swap();
/* 1302 */     for (int i = this.callbackTypes.length - 1; i >= 0; i--) {
/* 1303 */       if (i != 0) {
/* 1304 */         e.dup2();
/*      */       }
/* 1306 */       e.aaload(i);
/* 1307 */       e.checkcast(this.callbackTypes[i]);
/* 1308 */       e.putfield(getCallbackField(i));
/*      */     } 
/*      */     
/* 1311 */     e.mark(end);
/* 1312 */     e.return_value();
/* 1313 */     e.end_method();
/*      */   }
/*      */   
/*      */   private static String getCallbackField(int index) {
/* 1317 */     return "CGLIB$CALLBACK_" + index;
/*      */   }
/*      */   
/*      */   public static interface EnhancerKey {
/*      */     Object newInstance(String param1String, String[] param1ArrayOfString, WeakCacheKey<CallbackFilter> param1WeakCacheKey, Type[] param1ArrayOfType, boolean param1Boolean1, boolean param1Boolean2, Long param1Long);
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\proxy\Enhancer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */