/*      */ package org.springframework.core;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Map;
/*      */ import org.springframework.lang.UsesJava8;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ConcurrentReferenceHashMap;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ResolvableType
/*      */   implements Serializable
/*      */ {
/*   89 */   public static final ResolvableType NONE = new ResolvableType(null, null, null, Integer.valueOf(0));
/*      */   
/*   91 */   private static final ResolvableType[] EMPTY_TYPES_ARRAY = new ResolvableType[0];
/*      */   
/*   93 */   private static final ConcurrentReferenceHashMap<ResolvableType, ResolvableType> cache = new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Type type;
/*      */ 
/*      */ 
/*      */   
/*      */   private final SerializableTypeWrapper.TypeProvider typeProvider;
/*      */ 
/*      */ 
/*      */   
/*      */   private final VariableResolver variableResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   private final ResolvableType componentType;
/*      */ 
/*      */ 
/*      */   
/*      */   private final Class<?> resolved;
/*      */ 
/*      */ 
/*      */   
/*      */   private final Integer hash;
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType superType;
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType[] interfaces;
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType[] generics;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver) {
/*  136 */     this.type = type;
/*  137 */     this.typeProvider = typeProvider;
/*  138 */     this.variableResolver = variableResolver;
/*  139 */     this.componentType = null;
/*  140 */     this.resolved = null;
/*  141 */     this.hash = Integer.valueOf(calculateHashCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver, Integer hash) {
/*  150 */     this.type = type;
/*  151 */     this.typeProvider = typeProvider;
/*  152 */     this.variableResolver = variableResolver;
/*  153 */     this.componentType = null;
/*  154 */     this.resolved = resolveClass();
/*  155 */     this.hash = hash;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver, ResolvableType componentType) {
/*  165 */     this.type = type;
/*  166 */     this.typeProvider = typeProvider;
/*  167 */     this.variableResolver = variableResolver;
/*  168 */     this.componentType = componentType;
/*  169 */     this.resolved = resolveClass();
/*  170 */     this.hash = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Class<?> clazz) {
/*  179 */     this.resolved = (clazz != null) ? clazz : Object.class;
/*  180 */     this.type = this.resolved;
/*  181 */     this.typeProvider = null;
/*  182 */     this.variableResolver = null;
/*  183 */     this.componentType = null;
/*  184 */     this.hash = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Type getType() {
/*  193 */     return SerializableTypeWrapper.unwrap(this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> getRawClass() {
/*  201 */     if (this.type == this.resolved) {
/*  202 */       return this.resolved;
/*      */     }
/*  204 */     Type rawType = this.type;
/*  205 */     if (rawType instanceof ParameterizedType) {
/*  206 */       rawType = ((ParameterizedType)rawType).getRawType();
/*      */     }
/*  208 */     return (rawType instanceof Class) ? (Class)rawType : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getSource() {
/*  219 */     Object source = (this.typeProvider != null) ? this.typeProvider.getSource() : null;
/*  220 */     return (source != null) ? source : this.type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInstance(Object obj) {
/*  230 */     return (obj != null && isAssignableFrom(obj.getClass()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAssignableFrom(Class<?> other) {
/*  241 */     return isAssignableFrom(forClass(other), null);
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
/*      */   public boolean isAssignableFrom(ResolvableType other) {
/*  256 */     return isAssignableFrom(other, null);
/*      */   }
/*      */   
/*      */   private boolean isAssignableFrom(ResolvableType other, Map<Type, Type> matchedBefore) {
/*  260 */     Assert.notNull(other, "ResolvableType must not be null");
/*      */ 
/*      */     
/*  263 */     if (this == NONE || other == NONE) {
/*  264 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  268 */     if (isArray()) {
/*  269 */       return (other.isArray() && getComponentType().isAssignableFrom(other.getComponentType()));
/*      */     }
/*      */     
/*  272 */     if (matchedBefore != null && matchedBefore.get(this.type) == other.type) {
/*  273 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  277 */     WildcardBounds ourBounds = WildcardBounds.get(this);
/*  278 */     WildcardBounds typeBounds = WildcardBounds.get(other);
/*      */ 
/*      */     
/*  281 */     if (typeBounds != null) {
/*  282 */       return (ourBounds != null && ourBounds.isSameKind(typeBounds) && ourBounds
/*  283 */         .isAssignableFrom(typeBounds.getBounds()));
/*      */     }
/*      */ 
/*      */     
/*  287 */     if (ourBounds != null) {
/*  288 */       return ourBounds.isAssignableFrom(new ResolvableType[] { other });
/*      */     }
/*      */ 
/*      */     
/*  292 */     boolean exactMatch = (matchedBefore != null);
/*  293 */     boolean checkGenerics = true;
/*  294 */     Class<?> ourResolved = null;
/*  295 */     if (this.type instanceof TypeVariable) {
/*  296 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*      */       
/*  298 */       if (this.variableResolver != null) {
/*  299 */         ResolvableType resolved = this.variableResolver.resolveVariable(variable);
/*  300 */         if (resolved != null) {
/*  301 */           ourResolved = resolved.resolve();
/*      */         }
/*      */       } 
/*  304 */       if (ourResolved == null)
/*      */       {
/*  306 */         if (other.variableResolver != null) {
/*  307 */           ResolvableType resolved = other.variableResolver.resolveVariable(variable);
/*  308 */           if (resolved != null) {
/*  309 */             ourResolved = resolved.resolve();
/*  310 */             checkGenerics = false;
/*      */           } 
/*      */         } 
/*      */       }
/*  314 */       if (ourResolved == null)
/*      */       {
/*  316 */         exactMatch = false;
/*      */       }
/*      */     } 
/*  319 */     if (ourResolved == null) {
/*  320 */       ourResolved = resolve(Object.class);
/*      */     }
/*  322 */     Class<?> otherResolved = other.resolve(Object.class);
/*      */ 
/*      */ 
/*      */     
/*  326 */     if (exactMatch ? !ourResolved.equals(otherResolved) : !ClassUtils.isAssignable(ourResolved, otherResolved)) {
/*  327 */       return false;
/*      */     }
/*      */     
/*  330 */     if (checkGenerics) {
/*      */       
/*  332 */       ResolvableType[] ourGenerics = getGenerics();
/*  333 */       ResolvableType[] typeGenerics = other.as(ourResolved).getGenerics();
/*  334 */       if (ourGenerics.length != typeGenerics.length) {
/*  335 */         return false;
/*      */       }
/*  337 */       if (matchedBefore == null) {
/*  338 */         matchedBefore = new IdentityHashMap<Type, Type>(1);
/*      */       }
/*  340 */       matchedBefore.put(this.type, other.type);
/*  341 */       for (int i = 0; i < ourGenerics.length; i++) {
/*  342 */         if (!ourGenerics[i].isAssignableFrom(typeGenerics[i], matchedBefore)) {
/*  343 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  348 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isArray() {
/*  356 */     if (this == NONE) {
/*  357 */       return false;
/*      */     }
/*  359 */     return ((this.type instanceof Class && ((Class)this.type).isArray()) || this.type instanceof GenericArrayType || 
/*  360 */       resolveType().isArray());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getComponentType() {
/*  369 */     if (this == NONE) {
/*  370 */       return NONE;
/*      */     }
/*  372 */     if (this.componentType != null) {
/*  373 */       return this.componentType;
/*      */     }
/*  375 */     if (this.type instanceof Class) {
/*  376 */       Class<?> componentType = ((Class)this.type).getComponentType();
/*  377 */       return forType(componentType, this.variableResolver);
/*      */     } 
/*  379 */     if (this.type instanceof GenericArrayType) {
/*  380 */       return forType(((GenericArrayType)this.type).getGenericComponentType(), this.variableResolver);
/*      */     }
/*  382 */     return resolveType().getComponentType();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType asCollection() {
/*  393 */     return as(Collection.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType asMap() {
/*  404 */     return as(Map.class);
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
/*      */   public ResolvableType as(Class<?> type) {
/*  421 */     if (this == NONE) {
/*  422 */       return NONE;
/*      */     }
/*  424 */     if (ObjectUtils.nullSafeEquals(resolve(), type)) {
/*  425 */       return this;
/*      */     }
/*  427 */     for (ResolvableType interfaceType : getInterfaces()) {
/*  428 */       ResolvableType interfaceAsType = interfaceType.as(type);
/*  429 */       if (interfaceAsType != NONE) {
/*  430 */         return interfaceAsType;
/*      */       }
/*      */     } 
/*  433 */     return getSuperType().as(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getSuperType() {
/*  442 */     Class<?> resolved = resolve();
/*  443 */     if (resolved == null || resolved.getGenericSuperclass() == null) {
/*  444 */       return NONE;
/*      */     }
/*  446 */     if (this.superType == null) {
/*  447 */       this.superType = forType(SerializableTypeWrapper.forGenericSuperclass(resolved), asVariableResolver());
/*      */     }
/*  449 */     return this.superType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType[] getInterfaces() {
/*  459 */     Class<?> resolved = resolve();
/*  460 */     if (resolved == null || ObjectUtils.isEmpty((Object[])resolved.getGenericInterfaces())) {
/*  461 */       return EMPTY_TYPES_ARRAY;
/*      */     }
/*  463 */     if (this.interfaces == null) {
/*  464 */       this.interfaces = forTypes(SerializableTypeWrapper.forGenericInterfaces(resolved), asVariableResolver());
/*      */     }
/*  466 */     return this.interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasGenerics() {
/*  475 */     return ((getGenerics()).length > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isEntirelyUnresolvable() {
/*  483 */     if (this == NONE) {
/*  484 */       return false;
/*      */     }
/*  486 */     ResolvableType[] generics = getGenerics();
/*  487 */     for (ResolvableType generic : generics) {
/*  488 */       if (!generic.isUnresolvableTypeVariable() && !generic.isWildcardWithoutBounds()) {
/*  489 */         return false;
/*      */       }
/*      */     } 
/*  492 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasUnresolvableGenerics() {
/*  503 */     if (this == NONE) {
/*  504 */       return false;
/*      */     }
/*  506 */     ResolvableType[] generics = getGenerics();
/*  507 */     for (ResolvableType generic : generics) {
/*  508 */       if (generic.isUnresolvableTypeVariable() || generic.isWildcardWithoutBounds()) {
/*  509 */         return true;
/*      */       }
/*      */     } 
/*  512 */     Class<?> resolved = resolve();
/*  513 */     if (resolved != null) {
/*  514 */       for (Type genericInterface : resolved.getGenericInterfaces()) {
/*  515 */         if (genericInterface instanceof Class && 
/*  516 */           forClass((Class)genericInterface).hasGenerics()) {
/*  517 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  521 */       return getSuperType().hasUnresolvableGenerics();
/*      */     } 
/*  523 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isUnresolvableTypeVariable() {
/*  531 */     if (this.type instanceof TypeVariable) {
/*  532 */       if (this.variableResolver == null) {
/*  533 */         return true;
/*      */       }
/*  535 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*  536 */       ResolvableType resolved = this.variableResolver.resolveVariable(variable);
/*  537 */       if (resolved == null || resolved.isUnresolvableTypeVariable()) {
/*  538 */         return true;
/*      */       }
/*      */     } 
/*  541 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isWildcardWithoutBounds() {
/*  549 */     if (this.type instanceof WildcardType) {
/*  550 */       WildcardType wt = (WildcardType)this.type;
/*  551 */       if ((wt.getLowerBounds()).length == 0) {
/*  552 */         Type[] upperBounds = wt.getUpperBounds();
/*  553 */         if (upperBounds.length == 0 || (upperBounds.length == 1 && Object.class == upperBounds[0])) {
/*  554 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  558 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getNested(int nestingLevel) {
/*  568 */     return getNested(nestingLevel, null);
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
/*      */   public ResolvableType getNested(int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
/*  592 */     ResolvableType result = this;
/*  593 */     for (int i = 2; i <= nestingLevel; i++) {
/*  594 */       if (result.isArray()) {
/*  595 */         result = result.getComponentType();
/*      */       }
/*      */       else {
/*      */         
/*  599 */         while (result != NONE && !result.hasGenerics()) {
/*  600 */           result = result.getSuperType();
/*      */         }
/*  602 */         Integer index = (typeIndexesPerLevel != null) ? typeIndexesPerLevel.get(Integer.valueOf(i)) : null;
/*  603 */         index = Integer.valueOf((index == null) ? ((result.getGenerics()).length - 1) : index.intValue());
/*  604 */         result = result.getGeneric(new int[] { index.intValue() });
/*      */       } 
/*      */     } 
/*  607 */     return result;
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
/*      */   public ResolvableType getGeneric(int... indexes) {
/*  628 */     ResolvableType[] generics = getGenerics();
/*  629 */     if (indexes == null || indexes.length == 0) {
/*  630 */       return (generics.length == 0) ? NONE : generics[0];
/*      */     }
/*  632 */     ResolvableType generic = this;
/*  633 */     for (int index : indexes) {
/*  634 */       generics = generic.getGenerics();
/*  635 */       if (index < 0 || index >= generics.length) {
/*  636 */         return NONE;
/*      */       }
/*  638 */       generic = generics[index];
/*      */     } 
/*  640 */     return generic;
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
/*      */   public ResolvableType[] getGenerics() {
/*  657 */     if (this == NONE) {
/*  658 */       return EMPTY_TYPES_ARRAY;
/*      */     }
/*  660 */     if (this.generics == null) {
/*  661 */       if (this.type instanceof Class) {
/*  662 */         Class<?> typeClass = (Class)this.type;
/*  663 */         this.generics = forTypes(SerializableTypeWrapper.forTypeParameters(typeClass), this.variableResolver);
/*      */       }
/*  665 */       else if (this.type instanceof ParameterizedType) {
/*  666 */         Type[] actualTypeArguments = ((ParameterizedType)this.type).getActualTypeArguments();
/*  667 */         ResolvableType[] generics = new ResolvableType[actualTypeArguments.length];
/*  668 */         for (int i = 0; i < actualTypeArguments.length; i++) {
/*  669 */           generics[i] = forType(actualTypeArguments[i], this.variableResolver);
/*      */         }
/*  671 */         this.generics = generics;
/*      */       } else {
/*      */         
/*  674 */         this.generics = resolveType().getGenerics();
/*      */       } 
/*      */     }
/*  677 */     return this.generics;
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
/*      */   public Class<?>[] resolveGenerics() {
/*  689 */     return resolveGenerics(null);
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
/*      */   public Class<?>[] resolveGenerics(Class<?> fallback) {
/*  703 */     ResolvableType[] generics = getGenerics();
/*  704 */     Class<?>[] resolvedGenerics = new Class[generics.length];
/*  705 */     for (int i = 0; i < generics.length; i++) {
/*  706 */       resolvedGenerics[i] = generics[i].resolve(fallback);
/*      */     }
/*  708 */     return resolvedGenerics;
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
/*      */   public Class<?> resolveGeneric(int... indexes) {
/*  721 */     return getGeneric(indexes).resolve();
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
/*      */   public Class<?> resolve() {
/*  735 */     return resolve(null);
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
/*      */   public Class<?> resolve(Class<?> fallback) {
/*  750 */     return (this.resolved != null) ? this.resolved : fallback;
/*      */   }
/*      */   
/*      */   private Class<?> resolveClass() {
/*  754 */     if (this.type instanceof Class || this.type == null) {
/*  755 */       return (Class)this.type;
/*      */     }
/*  757 */     if (this.type instanceof GenericArrayType) {
/*  758 */       Class<?> resolvedComponent = getComponentType().resolve();
/*  759 */       return (resolvedComponent != null) ? Array.newInstance(resolvedComponent, 0).getClass() : null;
/*      */     } 
/*  761 */     return resolveType().resolve();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ResolvableType resolveType() {
/*  770 */     if (this.type instanceof ParameterizedType) {
/*  771 */       return forType(((ParameterizedType)this.type).getRawType(), this.variableResolver);
/*      */     }
/*  773 */     if (this.type instanceof WildcardType) {
/*  774 */       Type resolved = resolveBounds(((WildcardType)this.type).getUpperBounds());
/*  775 */       if (resolved == null) {
/*  776 */         resolved = resolveBounds(((WildcardType)this.type).getLowerBounds());
/*      */       }
/*  778 */       return forType(resolved, this.variableResolver);
/*      */     } 
/*  780 */     if (this.type instanceof TypeVariable) {
/*  781 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*      */       
/*  783 */       if (this.variableResolver != null) {
/*  784 */         ResolvableType resolved = this.variableResolver.resolveVariable(variable);
/*  785 */         if (resolved != null) {
/*  786 */           return resolved;
/*      */         }
/*      */       } 
/*      */       
/*  790 */       return forType(resolveBounds(variable.getBounds()), this.variableResolver);
/*      */     } 
/*  792 */     return NONE;
/*      */   }
/*      */   
/*      */   private Type resolveBounds(Type[] bounds) {
/*  796 */     if (ObjectUtils.isEmpty((Object[])bounds) || Object.class == bounds[0]) {
/*  797 */       return null;
/*      */     }
/*  799 */     return bounds[0];
/*      */   }
/*      */   
/*      */   private ResolvableType resolveVariable(TypeVariable<?> variable) {
/*  803 */     if (this.type instanceof TypeVariable) {
/*  804 */       return resolveType().resolveVariable(variable);
/*      */     }
/*  806 */     if (this.type instanceof ParameterizedType) {
/*  807 */       ParameterizedType parameterizedType = (ParameterizedType)this.type;
/*  808 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])resolve().getTypeParameters();
/*  809 */       for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*  810 */         if (ObjectUtils.nullSafeEquals(arrayOfTypeVariable[i].getName(), variable.getName())) {
/*  811 */           Type actualType = parameterizedType.getActualTypeArguments()[i];
/*  812 */           return forType(actualType, this.variableResolver);
/*      */         } 
/*      */       } 
/*  815 */       if (parameterizedType.getOwnerType() != null) {
/*  816 */         return forType(parameterizedType.getOwnerType(), this.variableResolver).resolveVariable(variable);
/*      */       }
/*      */     } 
/*  819 */     if (this.variableResolver != null) {
/*  820 */       return this.variableResolver.resolveVariable(variable);
/*      */     }
/*  822 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object other) {
/*  828 */     if (this == other) {
/*  829 */       return true;
/*      */     }
/*  831 */     if (!(other instanceof ResolvableType)) {
/*  832 */       return false;
/*      */     }
/*      */     
/*  835 */     ResolvableType otherType = (ResolvableType)other;
/*  836 */     if (!ObjectUtils.nullSafeEquals(this.type, otherType.type)) {
/*  837 */       return false;
/*      */     }
/*  839 */     if (this.typeProvider != otherType.typeProvider && (this.typeProvider == null || otherType.typeProvider == null || 
/*      */       
/*  841 */       !ObjectUtils.nullSafeEquals(this.typeProvider.getType(), otherType.typeProvider.getType()))) {
/*  842 */       return false;
/*      */     }
/*  844 */     if (this.variableResolver != otherType.variableResolver && (this.variableResolver == null || otherType.variableResolver == null || 
/*      */       
/*  846 */       !ObjectUtils.nullSafeEquals(this.variableResolver.getSource(), otherType.variableResolver.getSource()))) {
/*  847 */       return false;
/*      */     }
/*  849 */     if (!ObjectUtils.nullSafeEquals(this.componentType, otherType.componentType)) {
/*  850 */       return false;
/*      */     }
/*  852 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  857 */     return (this.hash != null) ? this.hash.intValue() : calculateHashCode();
/*      */   }
/*      */   
/*      */   private int calculateHashCode() {
/*  861 */     int hashCode = ObjectUtils.nullSafeHashCode(this.type);
/*  862 */     if (this.typeProvider != null) {
/*  863 */       hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.typeProvider.getType());
/*      */     }
/*  865 */     if (this.variableResolver != null) {
/*  866 */       hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.variableResolver.getSource());
/*      */     }
/*  868 */     if (this.componentType != null) {
/*  869 */       hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.componentType);
/*      */     }
/*  871 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   VariableResolver asVariableResolver() {
/*  878 */     if (this == NONE) {
/*  879 */       return null;
/*      */     }
/*  881 */     return new DefaultVariableResolver();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object readResolve() {
/*  888 */     return (this.type == null) ? NONE : this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  897 */     if (isArray()) {
/*  898 */       return getComponentType() + "[]";
/*      */     }
/*  900 */     if (this.resolved == null) {
/*  901 */       return "?";
/*      */     }
/*  903 */     if (this.type instanceof TypeVariable) {
/*  904 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*  905 */       if (this.variableResolver == null || this.variableResolver.resolveVariable(variable) == null)
/*      */       {
/*      */         
/*  908 */         return "?";
/*      */       }
/*      */     } 
/*  911 */     StringBuilder result = new StringBuilder(this.resolved.getName());
/*  912 */     if (hasGenerics()) {
/*  913 */       result.append('<');
/*  914 */       result.append(StringUtils.arrayToDelimitedString((Object[])getGenerics(), ", "));
/*  915 */       result.append('>');
/*      */     } 
/*  917 */     return result.toString();
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
/*      */   public static ResolvableType forClass(Class<?> clazz) {
/*  934 */     return new ResolvableType(clazz);
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
/*      */   public static ResolvableType forRawClass(Class<?> clazz) {
/*  950 */     return new ResolvableType(clazz)
/*      */       {
/*      */         public ResolvableType[] getGenerics() {
/*  953 */           return ResolvableType.EMPTY_TYPES_ARRAY;
/*      */         }
/*      */         
/*      */         public boolean isAssignableFrom(Class<?> other) {
/*  957 */           return ClassUtils.isAssignable(getRawClass(), other);
/*      */         }
/*      */         
/*      */         public boolean isAssignableFrom(ResolvableType other) {
/*  961 */           Class<?> otherClass = other.getRawClass();
/*  962 */           return (otherClass != null && ClassUtils.isAssignable(getRawClass(), otherClass));
/*      */         }
/*      */       };
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
/*      */   public static ResolvableType forClass(Class<?> baseType, Class<?> implementationClass) {
/*  979 */     Assert.notNull(baseType, "Base type must not be null");
/*  980 */     ResolvableType asType = forType(implementationClass).as(baseType);
/*  981 */     return (asType == NONE) ? forType(baseType) : asType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forClassWithGenerics(Class<?> clazz, Class<?>... generics) {
/*  992 */     Assert.notNull(clazz, "Class must not be null");
/*  993 */     Assert.notNull(generics, "Generics array must not be null");
/*  994 */     ResolvableType[] resolvableGenerics = new ResolvableType[generics.length];
/*  995 */     for (int i = 0; i < generics.length; i++) {
/*  996 */       resolvableGenerics[i] = forClass(generics[i]);
/*      */     }
/*  998 */     return forClassWithGenerics(clazz, resolvableGenerics);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forClassWithGenerics(Class<?> clazz, ResolvableType... generics) {
/* 1009 */     Assert.notNull(clazz, "Class must not be null");
/* 1010 */     Assert.notNull(generics, "Generics array must not be null");
/* 1011 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])clazz.getTypeParameters();
/* 1012 */     Assert.isTrue((arrayOfTypeVariable.length == generics.length), "Mismatched number of generics specified");
/*      */     
/* 1014 */     Type[] arguments = new Type[generics.length];
/* 1015 */     for (int i = 0; i < generics.length; i++) {
/* 1016 */       ResolvableType generic = generics[i];
/* 1017 */       Type argument = (generic != null) ? generic.getType() : null;
/* 1018 */       arguments[i] = (argument != null) ? argument : arrayOfTypeVariable[i];
/*      */     } 
/*      */     
/* 1021 */     ParameterizedType syntheticType = new SyntheticParameterizedType(clazz, arguments);
/* 1022 */     return forType(syntheticType, new TypeVariablesVariableResolver((TypeVariable<?>[])arrayOfTypeVariable, generics));
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
/*      */   public static ResolvableType forInstance(Object instance) {
/* 1036 */     Assert.notNull(instance, "Instance must not be null");
/* 1037 */     if (instance instanceof ResolvableTypeProvider) {
/* 1038 */       ResolvableType type = ((ResolvableTypeProvider)instance).getResolvableType();
/* 1039 */       if (type != null) {
/* 1040 */         return type;
/*      */       }
/*      */     } 
/* 1043 */     return forClass(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forField(Field field) {
/* 1053 */     Assert.notNull(field, "Field must not be null");
/* 1054 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null);
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
/*      */   public static ResolvableType forField(Field field, Class<?> implementationClass) {
/* 1068 */     Assert.notNull(field, "Field must not be null");
/* 1069 */     ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
/* 1070 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver());
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
/*      */   public static ResolvableType forField(Field field, ResolvableType implementationType) {
/* 1084 */     Assert.notNull(field, "Field must not be null");
/* 1085 */     ResolvableType owner = (implementationType != null) ? implementationType : NONE;
/* 1086 */     owner = owner.as(field.getDeclaringClass());
/* 1087 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver());
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
/*      */   public static ResolvableType forField(Field field, int nestingLevel) {
/* 1099 */     Assert.notNull(field, "Field must not be null");
/* 1100 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null).getNested(nestingLevel);
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
/*      */   public static ResolvableType forField(Field field, int nestingLevel, Class<?> implementationClass) {
/* 1116 */     Assert.notNull(field, "Field must not be null");
/* 1117 */     ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
/* 1118 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver()).getNested(nestingLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forConstructorParameter(Constructor<?> constructor, int parameterIndex) {
/* 1129 */     Assert.notNull(constructor, "Constructor must not be null");
/* 1130 */     return forMethodParameter(new MethodParameter(constructor, parameterIndex));
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
/*      */   public static ResolvableType forConstructorParameter(Constructor<?> constructor, int parameterIndex, Class<?> implementationClass) {
/* 1147 */     Assert.notNull(constructor, "Constructor must not be null");
/* 1148 */     MethodParameter methodParameter = new MethodParameter(constructor, parameterIndex);
/* 1149 */     methodParameter.setContainingClass(implementationClass);
/* 1150 */     return forMethodParameter(methodParameter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodReturnType(Method method) {
/* 1160 */     Assert.notNull(method, "Method must not be null");
/* 1161 */     return forMethodParameter(new MethodParameter(method, -1));
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
/*      */   public static ResolvableType forMethodReturnType(Method method, Class<?> implementationClass) {
/* 1174 */     Assert.notNull(method, "Method must not be null");
/* 1175 */     MethodParameter methodParameter = new MethodParameter(method, -1);
/* 1176 */     methodParameter.setContainingClass(implementationClass);
/* 1177 */     return forMethodParameter(methodParameter);
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
/*      */   public static ResolvableType forMethodParameter(Method method, int parameterIndex) {
/* 1189 */     Assert.notNull(method, "Method must not be null");
/* 1190 */     return forMethodParameter(new MethodParameter(method, parameterIndex));
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
/*      */   public static ResolvableType forMethodParameter(Method method, int parameterIndex, Class<?> implementationClass) {
/* 1205 */     Assert.notNull(method, "Method must not be null");
/* 1206 */     MethodParameter methodParameter = new MethodParameter(method, parameterIndex);
/* 1207 */     methodParameter.setContainingClass(implementationClass);
/* 1208 */     return forMethodParameter(methodParameter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodParameter(MethodParameter methodParameter) {
/* 1218 */     return forMethodParameter(methodParameter, (Type)null);
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
/*      */   public static ResolvableType forMethodParameter(MethodParameter methodParameter, ResolvableType implementationType) {
/* 1231 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*      */     
/* 1233 */     implementationType = (implementationType != null) ? implementationType : forType(methodParameter.getContainingClass());
/* 1234 */     ResolvableType owner = implementationType.as(methodParameter.getDeclaringClass());
/* 1235 */     return forType(null, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver())
/* 1236 */       .getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);
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
/*      */   public static ResolvableType forMethodParameter(MethodParameter methodParameter, Type targetType) {
/* 1248 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/* 1249 */     ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
/* 1250 */     return forType(targetType, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver())
/* 1251 */       .getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void resolveMethodParameter(MethodParameter methodParameter) {
/* 1261 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/* 1262 */     ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
/* 1263 */     methodParameter.setParameterType(
/* 1264 */         forType(null, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).resolve());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forArrayComponent(ResolvableType componentType) {
/* 1273 */     Assert.notNull(componentType, "Component type must not be null");
/* 1274 */     Class<?> arrayClass = Array.newInstance(componentType.resolve(), 0).getClass();
/* 1275 */     return new ResolvableType(arrayClass, null, null, componentType);
/*      */   }
/*      */   
/*      */   private static ResolvableType[] forTypes(Type[] types, VariableResolver owner) {
/* 1279 */     ResolvableType[] result = new ResolvableType[types.length];
/* 1280 */     for (int i = 0; i < types.length; i++) {
/* 1281 */       result[i] = forType(types[i], owner);
/*      */     }
/* 1283 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forType(Type type) {
/* 1294 */     return forType(type, null, null);
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
/*      */   public static ResolvableType forType(Type type, ResolvableType owner) {
/* 1306 */     VariableResolver variableResolver = null;
/* 1307 */     if (owner != null) {
/* 1308 */       variableResolver = owner.asVariableResolver();
/*      */     }
/* 1310 */     return forType(type, variableResolver);
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
/*      */   public static ResolvableType forType(ParameterizedTypeReference<?> typeReference) {
/* 1323 */     return forType(typeReference.getType(), null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static ResolvableType forType(Type type, VariableResolver variableResolver) {
/* 1334 */     return forType(type, null, variableResolver);
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
/*      */   static ResolvableType forType(Type type, SerializableTypeWrapper.TypeProvider typeProvider, VariableResolver variableResolver) {
/* 1346 */     if (type == null && typeProvider != null) {
/* 1347 */       type = SerializableTypeWrapper.forTypeProvider(typeProvider);
/*      */     }
/* 1349 */     if (type == null) {
/* 1350 */       return NONE;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1355 */     if (type instanceof Class) {
/* 1356 */       return new ResolvableType(type, typeProvider, variableResolver, (ResolvableType)null);
/*      */     }
/*      */ 
/*      */     
/* 1360 */     cache.purgeUnreferencedEntries();
/*      */ 
/*      */     
/* 1363 */     ResolvableType key = new ResolvableType(type, typeProvider, variableResolver);
/* 1364 */     ResolvableType resolvableType = (ResolvableType)cache.get(key);
/* 1365 */     if (resolvableType == null) {
/* 1366 */       resolvableType = new ResolvableType(type, typeProvider, variableResolver, key.hash);
/* 1367 */       cache.put(resolvableType, resolvableType);
/*      */     } 
/* 1369 */     return resolvableType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void clearCache() {
/* 1377 */     cache.clear();
/* 1378 */     SerializableTypeWrapper.cache.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static interface VariableResolver
/*      */     extends Serializable
/*      */   {
/*      */     Object getSource();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ResolvableType resolveVariable(TypeVariable<?> param1TypeVariable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class DefaultVariableResolver
/*      */     implements VariableResolver
/*      */   {
/*      */     private DefaultVariableResolver() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public ResolvableType resolveVariable(TypeVariable<?> variable) {
/* 1406 */       return ResolvableType.this.resolveVariable(variable);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getSource() {
/* 1411 */       return ResolvableType.this;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TypeVariablesVariableResolver
/*      */     implements VariableResolver
/*      */   {
/*      */     private final TypeVariable<?>[] variables;
/*      */     
/*      */     private final ResolvableType[] generics;
/*      */     
/*      */     public TypeVariablesVariableResolver(TypeVariable<?>[] variables, ResolvableType[] generics) {
/* 1424 */       this.variables = variables;
/* 1425 */       this.generics = generics;
/*      */     }
/*      */ 
/*      */     
/*      */     public ResolvableType resolveVariable(TypeVariable<?> variable) {
/* 1430 */       for (int i = 0; i < this.variables.length; i++) {
/* 1431 */         TypeVariable<?> v1 = SerializableTypeWrapper.<TypeVariable>unwrap(this.variables[i]);
/* 1432 */         TypeVariable<?> v2 = SerializableTypeWrapper.<TypeVariable>unwrap(variable);
/* 1433 */         if (ObjectUtils.nullSafeEquals(v1, v2)) {
/* 1434 */           return this.generics[i];
/*      */         }
/*      */       } 
/* 1437 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getSource() {
/* 1442 */       return this.generics;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class SyntheticParameterizedType
/*      */     implements ParameterizedType, Serializable
/*      */   {
/*      */     private final Type rawType;
/*      */     private final Type[] typeArguments;
/*      */     
/*      */     public SyntheticParameterizedType(Type rawType, Type[] typeArguments) {
/* 1454 */       this.rawType = rawType;
/* 1455 */       this.typeArguments = typeArguments;
/*      */     }
/*      */ 
/*      */     
/*      */     @UsesJava8
/*      */     public String getTypeName() {
/* 1461 */       StringBuilder result = new StringBuilder(this.rawType.getTypeName());
/* 1462 */       if (this.typeArguments.length > 0) {
/* 1463 */         result.append('<');
/* 1464 */         for (int i = 0; i < this.typeArguments.length; i++) {
/* 1465 */           if (i > 0) {
/* 1466 */             result.append(", ");
/*      */           }
/* 1468 */           result.append(this.typeArguments[i].getTypeName());
/*      */         } 
/* 1470 */         result.append('>');
/*      */       } 
/* 1472 */       return result.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public Type getOwnerType() {
/* 1477 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Type getRawType() {
/* 1482 */       return this.rawType;
/*      */     }
/*      */ 
/*      */     
/*      */     public Type[] getActualTypeArguments() {
/* 1487 */       return this.typeArguments;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/* 1492 */       if (this == other) {
/* 1493 */         return true;
/*      */       }
/* 1495 */       if (!(other instanceof ParameterizedType)) {
/* 1496 */         return false;
/*      */       }
/* 1498 */       ParameterizedType otherType = (ParameterizedType)other;
/* 1499 */       return (otherType.getOwnerType() == null && this.rawType.equals(otherType.getRawType()) && 
/* 1500 */         Arrays.equals((Object[])this.typeArguments, (Object[])otherType.getActualTypeArguments()));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1505 */       return this.rawType.hashCode() * 31 + Arrays.hashCode((Object[])this.typeArguments);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class WildcardBounds
/*      */   {
/*      */     private final Kind kind;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ResolvableType[] bounds;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public WildcardBounds(Kind kind, ResolvableType[] bounds) {
/* 1526 */       this.kind = kind;
/* 1527 */       this.bounds = bounds;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isSameKind(WildcardBounds bounds) {
/* 1534 */       return (this.kind == bounds.kind);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isAssignableFrom(ResolvableType... types) {
/* 1543 */       for (ResolvableType bound : this.bounds) {
/* 1544 */         for (ResolvableType type : types) {
/* 1545 */           if (!isAssignable(bound, type)) {
/* 1546 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/* 1550 */       return true;
/*      */     }
/*      */     
/*      */     private boolean isAssignable(ResolvableType source, ResolvableType from) {
/* 1554 */       return (this.kind == Kind.UPPER) ? source.isAssignableFrom(from) : from.isAssignableFrom(source);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ResolvableType[] getBounds() {
/* 1561 */       return this.bounds;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static WildcardBounds get(ResolvableType type) {
/* 1571 */       ResolvableType resolveToWildcard = type;
/* 1572 */       while (!(resolveToWildcard.getType() instanceof WildcardType)) {
/* 1573 */         if (resolveToWildcard == ResolvableType.NONE) {
/* 1574 */           return null;
/*      */         }
/* 1576 */         resolveToWildcard = resolveToWildcard.resolveType();
/*      */       } 
/* 1578 */       WildcardType wildcardType = (WildcardType)resolveToWildcard.type;
/* 1579 */       Kind boundsType = ((wildcardType.getLowerBounds()).length > 0) ? Kind.LOWER : Kind.UPPER;
/* 1580 */       Type[] bounds = (boundsType == Kind.UPPER) ? wildcardType.getUpperBounds() : wildcardType.getLowerBounds();
/* 1581 */       ResolvableType[] resolvableBounds = new ResolvableType[bounds.length];
/* 1582 */       for (int i = 0; i < bounds.length; i++) {
/* 1583 */         resolvableBounds[i] = ResolvableType.forType(bounds[i], type.variableResolver);
/*      */       }
/* 1585 */       return new WildcardBounds(boundsType, resolvableBounds);
/*      */     }
/*      */ 
/*      */     
/*      */     enum Kind
/*      */     {
/* 1591 */       UPPER, LOWER;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\ResolvableType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */