/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreatorCollector
/*     */ {
/*     */   protected static final int C_DEFAULT = 0;
/*     */   protected static final int C_STRING = 1;
/*     */   protected static final int C_INT = 2;
/*     */   protected static final int C_LONG = 3;
/*  30 */   protected static final String[] TYPE_DESCS = new String[] { "default", "from-String", "from-int", "from-long", "from-double", "from-boolean", "delegate", "property-based" };
/*     */ 
/*     */   
/*     */   protected static final int C_DOUBLE = 4;
/*     */   
/*     */   protected static final int C_BOOLEAN = 5;
/*     */   
/*     */   protected static final int C_DELEGATE = 6;
/*     */   
/*     */   protected static final int C_PROPS = 7;
/*     */   
/*     */   protected static final int C_ARRAY_DELEGATE = 8;
/*     */   
/*     */   protected final BeanDescription _beanDesc;
/*     */   
/*     */   protected final boolean _canFixAccess;
/*     */   
/*     */   protected final boolean _forceAccess;
/*     */   
/*  49 */   protected final AnnotatedWithParams[] _creators = new AnnotatedWithParams[9];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   protected int _explicitCreators = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _hasNonDefaultCreator = false;
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty[] _delegateArgs;
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty[] _arrayDelegateArgs;
/*     */ 
/*     */   
/*     */   protected SettableBeanProperty[] _propertyBasedArgs;
/*     */ 
/*     */ 
/*     */   
/*     */   public CreatorCollector(BeanDescription beanDesc, MapperConfig<?> config) {
/*  76 */     this._beanDesc = beanDesc;
/*  77 */     this._canFixAccess = config.canOverrideAccessModifiers();
/*  78 */     this
/*  79 */       ._forceAccess = config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueInstantiator constructValueInstantiator(DeserializationContext ctxt) throws JsonMappingException {
/*  85 */     DeserializationConfig config = ctxt.getConfig();
/*  86 */     JavaType delegateType = _computeDelegateType(ctxt, this._creators[6], this._delegateArgs);
/*     */     
/*  88 */     JavaType arrayDelegateType = _computeDelegateType(ctxt, this._creators[8], this._arrayDelegateArgs);
/*     */     
/*  90 */     JavaType type = this._beanDesc.getType();
/*     */     
/*  92 */     StdValueInstantiator inst = new StdValueInstantiator(config, type);
/*  93 */     inst.configureFromObjectSettings(this._creators[0], this._creators[6], delegateType, this._delegateArgs, this._creators[7], this._propertyBasedArgs);
/*     */ 
/*     */     
/*  96 */     inst.configureFromArraySettings(this._creators[8], arrayDelegateType, this._arrayDelegateArgs);
/*     */     
/*  98 */     inst.configureFromStringCreator(this._creators[1]);
/*  99 */     inst.configureFromIntCreator(this._creators[2]);
/* 100 */     inst.configureFromLongCreator(this._creators[3]);
/* 101 */     inst.configureFromDoubleCreator(this._creators[4]);
/* 102 */     inst.configureFromBooleanCreator(this._creators[5]);
/* 103 */     return (ValueInstantiator)inst;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCreator(AnnotatedWithParams creator) {
/* 123 */     this._creators[0] = _fixAccess(creator);
/*     */   }
/*     */   
/*     */   public void addStringCreator(AnnotatedWithParams creator, boolean explicit) {
/* 127 */     verifyNonDup(creator, 1, explicit);
/*     */   }
/*     */   
/*     */   public void addIntCreator(AnnotatedWithParams creator, boolean explicit) {
/* 131 */     verifyNonDup(creator, 2, explicit);
/*     */   }
/*     */   
/*     */   public void addLongCreator(AnnotatedWithParams creator, boolean explicit) {
/* 135 */     verifyNonDup(creator, 3, explicit);
/*     */   }
/*     */   
/*     */   public void addDoubleCreator(AnnotatedWithParams creator, boolean explicit) {
/* 139 */     verifyNonDup(creator, 4, explicit);
/*     */   }
/*     */   
/*     */   public void addBooleanCreator(AnnotatedWithParams creator, boolean explicit) {
/* 143 */     verifyNonDup(creator, 5, explicit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDelegatingCreator(AnnotatedWithParams creator, boolean explicit, SettableBeanProperty[] injectables, int delegateeIndex) {
/* 150 */     if (creator.getParameterType(delegateeIndex).isCollectionLikeType()) {
/* 151 */       if (verifyNonDup(creator, 8, explicit)) {
/* 152 */         this._arrayDelegateArgs = injectables;
/*     */       }
/*     */     }
/* 155 */     else if (verifyNonDup(creator, 6, explicit)) {
/* 156 */       this._delegateArgs = injectables;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyCreator(AnnotatedWithParams creator, boolean explicit, SettableBeanProperty[] properties) {
/* 164 */     if (verifyNonDup(creator, 7, explicit)) {
/*     */       
/* 166 */       if (properties.length > 1) {
/* 167 */         HashMap<String, Integer> names = new HashMap<>();
/* 168 */         for (int i = 0, len = properties.length; i < len; i++) {
/* 169 */           String name = properties[i].getName();
/*     */ 
/*     */           
/* 172 */           if (!name.isEmpty() || properties[i].getInjectableValueId() == null) {
/*     */ 
/*     */             
/* 175 */             Integer old = names.put(name, Integer.valueOf(i));
/* 176 */             if (old != null)
/* 177 */               throw new IllegalArgumentException(String.format("Duplicate creator property \"%s\" (index %s vs %d) for type %s ", new Object[] { name, old, 
/*     */                       
/* 179 */                       Integer.valueOf(i), ClassUtil.nameOf(this._beanDesc.getBeanClass()) })); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 183 */       this._propertyBasedArgs = properties;
/*     */     } 
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
/*     */   
/*     */   public boolean hasDefaultCreator() {
/* 197 */     return (this._creators[0] != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDelegatingCreator() {
/* 204 */     return (this._creators[6] != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPropertyBasedCreator() {
/* 211 */     return (this._creators[7] != null);
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
/*     */   
/*     */   private JavaType _computeDelegateType(DeserializationContext ctxt, AnnotatedWithParams creator, SettableBeanProperty[] delegateArgs) throws JsonMappingException {
/* 224 */     if (!this._hasNonDefaultCreator || creator == null) {
/* 225 */       return null;
/*     */     }
/*     */     
/* 228 */     int ix = 0;
/* 229 */     if (delegateArgs != null) {
/* 230 */       for (int i = 0, len = delegateArgs.length; i < len; i++) {
/* 231 */         if (delegateArgs[i] == null) {
/* 232 */           ix = i;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 237 */     DeserializationConfig config = ctxt.getConfig();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     JavaType baseType = creator.getParameterType(ix);
/* 243 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 244 */     if (intr != null) {
/* 245 */       AnnotatedParameter delegate = creator.getParameter(ix);
/*     */ 
/*     */       
/* 248 */       Object deserDef = intr.findDeserializer((Annotated)delegate);
/* 249 */       if (deserDef != null) {
/* 250 */         JsonDeserializer<Object> deser = ctxt.deserializerInstance((Annotated)delegate, deserDef);
/* 251 */         baseType = baseType.withValueHandler(deser);
/*     */       } else {
/*     */         
/* 254 */         baseType = intr.refineDeserializationType((MapperConfig)config, (Annotated)delegate, baseType);
/*     */       } 
/*     */     } 
/*     */     
/* 258 */     return baseType;
/*     */   }
/*     */   
/*     */   private <T extends com.fasterxml.jackson.databind.introspect.AnnotatedMember> T _fixAccess(T member) {
/* 262 */     if (member != null && this._canFixAccess) {
/* 263 */       ClassUtil.checkAndFixAccess((Member)member.getAnnotated(), this._forceAccess);
/*     */     }
/*     */     
/* 266 */     return member;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean verifyNonDup(AnnotatedWithParams newOne, int typeIndex, boolean explicit) {
/* 274 */     int mask = 1 << typeIndex;
/* 275 */     this._hasNonDefaultCreator = true;
/* 276 */     AnnotatedWithParams oldOne = this._creators[typeIndex];
/*     */     
/* 278 */     if (oldOne != null) {
/*     */       boolean verify;
/* 280 */       if ((this._explicitCreators & mask) != 0) {
/*     */         
/* 282 */         if (!explicit) {
/* 283 */           return false;
/*     */         }
/*     */         
/* 286 */         verify = true;
/*     */       } else {
/*     */         
/* 289 */         verify = !explicit;
/*     */       } 
/*     */ 
/*     */       
/* 293 */       if (verify && oldOne.getClass() == newOne.getClass()) {
/*     */         
/* 295 */         Class<?> oldType = oldOne.getRawParameterType(0);
/* 296 */         Class<?> newType = newOne.getRawParameterType(0);
/*     */         
/* 298 */         if (oldType == newType) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 304 */           if (_isEnumValueOf(newOne)) {
/* 305 */             return false;
/*     */           }
/* 307 */           if (!_isEnumValueOf(oldOne))
/*     */           {
/*     */             
/* 310 */             throw new IllegalArgumentException(String.format("Conflicting %s creators: already had %s creator %s, encountered another: %s", new Object[] { TYPE_DESCS[typeIndex], explicit ? "explicitly marked" : "implicitly discovered", oldOne, newOne }));
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 319 */         else if (newType.isAssignableFrom(oldType)) {
/*     */           
/* 321 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 326 */     if (explicit) {
/* 327 */       this._explicitCreators |= mask;
/*     */     }
/* 329 */     this._creators[typeIndex] = _fixAccess(newOne);
/* 330 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _isEnumValueOf(AnnotatedWithParams creator) {
/* 339 */     return (creator.getDeclaringClass().isEnum() && "valueOf"
/* 340 */       .equals(creator.getName()));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\CreatorCollector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */