/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class AnnotatedClass
/*     */   extends Annotated
/*     */   implements TypeResolutionContext
/*     */ {
/*  20 */   private static final Creators NO_CREATORS = new Creators(null, 
/*  21 */       Collections.emptyList(), 
/*  22 */       Collections.emptyList());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Class<?> _class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TypeBindings _bindings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final List<JavaType> _superTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TypeFactory _typeFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ClassIntrospector.MixInResolver _mixInResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Class<?> _primaryMixIn;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Annotations _classAnnotations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Creators _creators;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnnotatedMethodMap _memberMethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<AnnotatedField> _fields;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected transient Boolean _nonStaticInnerClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotatedClass(JavaType type, Class<?> rawType, List<JavaType> superTypes, Class<?> primaryMixIn, Annotations classAnnotations, TypeBindings bindings, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir, TypeFactory tf) {
/* 134 */     this._type = type;
/* 135 */     this._class = rawType;
/* 136 */     this._superTypes = superTypes;
/* 137 */     this._primaryMixIn = primaryMixIn;
/* 138 */     this._classAnnotations = classAnnotations;
/* 139 */     this._bindings = bindings;
/* 140 */     this._annotationIntrospector = aintr;
/* 141 */     this._mixInResolver = mir;
/* 142 */     this._typeFactory = tf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotatedClass(Class<?> rawType) {
/* 152 */     this._type = null;
/* 153 */     this._class = rawType;
/* 154 */     this._superTypes = Collections.emptyList();
/* 155 */     this._primaryMixIn = null;
/* 156 */     this._classAnnotations = AnnotationCollector.emptyAnnotations();
/* 157 */     this._bindings = TypeBindings.emptyBindings();
/* 158 */     this._annotationIntrospector = null;
/* 159 */     this._mixInResolver = null;
/* 160 */     this._typeFactory = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static AnnotatedClass construct(JavaType type, MapperConfig<?> config) {
/* 168 */     return construct(type, config, (ClassIntrospector.MixInResolver)config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static AnnotatedClass construct(JavaType type, MapperConfig<?> config, ClassIntrospector.MixInResolver mir) {
/* 178 */     return AnnotatedClassResolver.resolve(config, type, mir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static AnnotatedClass constructWithoutSuperTypes(Class<?> raw, MapperConfig<?> config) {
/* 191 */     return constructWithoutSuperTypes(raw, config, (ClassIntrospector.MixInResolver)config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static AnnotatedClass constructWithoutSuperTypes(Class<?> raw, MapperConfig<?> config, ClassIntrospector.MixInResolver mir) {
/* 201 */     return AnnotatedClassResolver.resolveWithoutSuperTypes(config, raw, mir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType resolveType(Type type) {
/* 212 */     return this._typeFactory.constructType(type, this._bindings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getAnnotated() {
/* 222 */     return this._class;
/*     */   }
/*     */   public int getModifiers() {
/* 225 */     return this._class.getModifiers();
/*     */   }
/*     */   public String getName() {
/* 228 */     return this._class.getName();
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls) {
/* 232 */     return (A)this._classAnnotations.get(acls);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(Class<?> acls) {
/* 237 */     return this._classAnnotations.has(acls);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasOneOf(Class<? extends Annotation>[] annoClasses) {
/* 242 */     return this._classAnnotations.hasOneOf((Class[])annoClasses);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getRawType() {
/* 247 */     return this._class;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Iterable<Annotation> annotations() {
/* 253 */     if (this._classAnnotations instanceof AnnotationMap)
/* 254 */       return ((AnnotationMap)this._classAnnotations).annotations(); 
/* 255 */     if (this._classAnnotations instanceof AnnotationCollector.OneAnnotation || this._classAnnotations instanceof AnnotationCollector.TwoAnnotations)
/*     */     {
/* 257 */       throw new UnsupportedOperationException("please use getAnnotations/ hasAnnotation to check for Annotations");
/*     */     }
/* 259 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType getType() {
/* 264 */     return this._type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotations getAnnotations() {
/* 274 */     return this._classAnnotations;
/*     */   }
/*     */   
/*     */   public boolean hasAnnotations() {
/* 278 */     return (this._classAnnotations.size() > 0);
/*     */   }
/*     */   
/*     */   public AnnotatedConstructor getDefaultConstructor() {
/* 282 */     return (_creators()).defaultConstructor;
/*     */   }
/*     */   
/*     */   public List<AnnotatedConstructor> getConstructors() {
/* 286 */     return (_creators()).constructors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AnnotatedMethod> getFactoryMethods() {
/* 293 */     return (_creators()).creatorMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public List<AnnotatedMethod> getStaticMethods() {
/* 301 */     return getFactoryMethods();
/*     */   }
/*     */   
/*     */   public Iterable<AnnotatedMethod> memberMethods() {
/* 305 */     return _methods();
/*     */   }
/*     */   
/*     */   public int getMemberMethodCount() {
/* 309 */     return _methods().size();
/*     */   }
/*     */   
/*     */   public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
/* 313 */     return _methods().find(name, paramTypes);
/*     */   }
/*     */   
/*     */   public int getFieldCount() {
/* 317 */     return _fields().size();
/*     */   }
/*     */   
/*     */   public Iterable<AnnotatedField> fields() {
/* 321 */     return _fields();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNonStaticInnerClass() {
/* 329 */     Boolean B = this._nonStaticInnerClass;
/* 330 */     if (B == null) {
/* 331 */       this._nonStaticInnerClass = B = Boolean.valueOf(ClassUtil.isNonStaticInnerClass(this._class));
/*     */     }
/* 333 */     return B.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<AnnotatedField> _fields() {
/* 343 */     List<AnnotatedField> f = this._fields;
/* 344 */     if (f == null) {
/*     */       
/* 346 */       if (this._type == null) {
/* 347 */         f = Collections.emptyList();
/*     */       } else {
/* 349 */         f = AnnotatedFieldCollector.collectFields(this._annotationIntrospector, this, this._mixInResolver, this._typeFactory, this._type);
/*     */       } 
/*     */       
/* 352 */       this._fields = f;
/*     */     } 
/* 354 */     return f;
/*     */   }
/*     */   
/*     */   private final AnnotatedMethodMap _methods() {
/* 358 */     AnnotatedMethodMap m = this._memberMethods;
/* 359 */     if (m == null) {
/*     */ 
/*     */       
/* 362 */       if (this._type == null) {
/* 363 */         m = new AnnotatedMethodMap();
/*     */       } else {
/* 365 */         m = AnnotatedMethodCollector.collectMethods(this._annotationIntrospector, this, this._mixInResolver, this._typeFactory, this._type, this._superTypes, this._primaryMixIn);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 370 */       this._memberMethods = m;
/*     */     } 
/* 372 */     return m;
/*     */   }
/*     */   
/*     */   private final Creators _creators() {
/* 376 */     Creators c = this._creators;
/* 377 */     if (c == null) {
/* 378 */       if (this._type == null) {
/* 379 */         c = NO_CREATORS;
/*     */       } else {
/* 381 */         c = AnnotatedCreatorCollector.collectCreators(this._annotationIntrospector, this, this._type, this._primaryMixIn);
/*     */       } 
/*     */       
/* 384 */       this._creators = c;
/*     */     } 
/* 386 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 397 */     return "[AnnotedClass " + this._class.getName() + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 402 */     return this._class.getName().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 407 */     if (o == this) return true; 
/* 408 */     if (!ClassUtil.hasClass(o, getClass())) {
/* 409 */       return false;
/*     */     }
/* 411 */     return (((AnnotatedClass)o)._class == this._class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Creators
/*     */   {
/*     */     public final AnnotatedConstructor defaultConstructor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final List<AnnotatedConstructor> constructors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final List<AnnotatedMethod> creatorMethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Creators(AnnotatedConstructor defCtor, List<AnnotatedConstructor> ctors, List<AnnotatedMethod> ctorMethods) {
/* 442 */       this.defaultConstructor = defCtor;
/* 443 */       this.constructors = ctors;
/* 444 */       this.creatorMethods = ctorMethods;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedClass.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */