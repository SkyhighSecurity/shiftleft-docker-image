/*     */ package com.fasterxml.jackson.databind.jsontype;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class BasicPolymorphicTypeValidator
/*     */   extends PolymorphicTypeValidator.Base
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Set<Class<?>> _invalidBaseTypes;
/*     */   protected final TypeMatcher[] _baseTypeMatchers;
/*     */   protected final NameMatcher[] _subTypeNameMatchers;
/*     */   protected final TypeMatcher[] _subClassMatchers;
/*     */   
/*     */   protected static abstract class TypeMatcher
/*     */   {
/*     */     public abstract boolean match(Class<?> param1Class);
/*     */   }
/*     */   
/*     */   protected static abstract class NameMatcher
/*     */   {
/*     */     public abstract boolean match(String param1String);
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     protected Set<Class<?>> _invalidBaseTypes;
/*     */     protected List<BasicPolymorphicTypeValidator.TypeMatcher> _baseTypeMatchers;
/*     */     protected List<BasicPolymorphicTypeValidator.NameMatcher> _subTypeNameMatchers;
/*     */     protected List<BasicPolymorphicTypeValidator.TypeMatcher> _subTypeClassMatchers;
/*     */     
/*     */     public Builder allowIfBaseType(final Class<?> baseOfBase) {
/* 101 */       return _appendBaseMatcher(new BasicPolymorphicTypeValidator.TypeMatcher()
/*     */           {
/*     */             public boolean match(Class<?> clazz) {
/* 104 */               return baseOfBase.isAssignableFrom(clazz);
/*     */             }
/*     */           });
/*     */     }
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
/*     */     public Builder allowIfBaseType(final Pattern patternForBase) {
/* 127 */       return _appendBaseMatcher(new BasicPolymorphicTypeValidator.TypeMatcher()
/*     */           {
/*     */             public boolean match(Class<?> clazz) {
/* 130 */               return patternForBase.matcher(clazz.getName()).matches();
/*     */             }
/*     */           });
/*     */     }
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
/*     */     public Builder allowIfBaseType(final String prefixForBase) {
/* 147 */       return _appendBaseMatcher(new BasicPolymorphicTypeValidator.TypeMatcher()
/*     */           {
/*     */             public boolean match(Class<?> clazz) {
/* 150 */               return clazz.getName().startsWith(prefixForBase);
/*     */             }
/*     */           });
/*     */     }
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
/*     */     public Builder denyForExactBaseType(Class<?> baseTypeToDeny) {
/* 168 */       if (this._invalidBaseTypes == null) {
/* 169 */         this._invalidBaseTypes = new HashSet<>();
/*     */       }
/* 171 */       this._invalidBaseTypes.add(baseTypeToDeny);
/* 172 */       return this;
/*     */     }
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
/*     */     public Builder allowIfSubType(final Class<?> subTypeBase) {
/* 189 */       return _appendSubClassMatcher(new BasicPolymorphicTypeValidator.TypeMatcher()
/*     */           {
/*     */             public boolean match(Class<?> clazz) {
/* 192 */               return subTypeBase.isAssignableFrom(clazz);
/*     */             }
/*     */           });
/*     */     }
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
/*     */     public Builder allowIfSubType(final Pattern patternForSubType) {
/* 214 */       return _appendSubNameMatcher(new BasicPolymorphicTypeValidator.NameMatcher()
/*     */           {
/*     */             public boolean match(String clazzName) {
/* 217 */               return patternForSubType.matcher(clazzName).matches();
/*     */             }
/*     */           });
/*     */     }
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
/*     */     public Builder allowIfSubType(final String prefixForSubType) {
/* 234 */       return _appendSubNameMatcher(new BasicPolymorphicTypeValidator.NameMatcher()
/*     */           {
/*     */             public boolean match(String clazzName) {
/* 237 */               return clazzName.startsWith(prefixForSubType);
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public BasicPolymorphicTypeValidator build() {
/* 243 */       return new BasicPolymorphicTypeValidator(this._invalidBaseTypes, (this._baseTypeMatchers == null) ? null : this._baseTypeMatchers
/* 244 */           .<BasicPolymorphicTypeValidator.TypeMatcher>toArray(new BasicPolymorphicTypeValidator.TypeMatcher[0]), (this._subTypeNameMatchers == null) ? null : this._subTypeNameMatchers
/* 245 */           .<BasicPolymorphicTypeValidator.NameMatcher>toArray(new BasicPolymorphicTypeValidator.NameMatcher[0]), (this._subTypeClassMatchers == null) ? null : this._subTypeClassMatchers
/* 246 */           .<BasicPolymorphicTypeValidator.TypeMatcher>toArray(new BasicPolymorphicTypeValidator.TypeMatcher[0]));
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder _appendBaseMatcher(BasicPolymorphicTypeValidator.TypeMatcher matcher) {
/* 251 */       if (this._baseTypeMatchers == null) {
/* 252 */         this._baseTypeMatchers = new ArrayList<>();
/*     */       }
/* 254 */       this._baseTypeMatchers.add(matcher);
/* 255 */       return this;
/*     */     }
/*     */     
/*     */     protected Builder _appendSubNameMatcher(BasicPolymorphicTypeValidator.NameMatcher matcher) {
/* 259 */       if (this._subTypeNameMatchers == null) {
/* 260 */         this._subTypeNameMatchers = new ArrayList<>();
/*     */       }
/* 262 */       this._subTypeNameMatchers.add(matcher);
/* 263 */       return this;
/*     */     }
/*     */     
/*     */     protected Builder _appendSubClassMatcher(BasicPolymorphicTypeValidator.TypeMatcher matcher) {
/* 267 */       if (this._subTypeClassMatchers == null) {
/* 268 */         this._subTypeClassMatchers = new ArrayList<>();
/*     */       }
/* 270 */       this._subTypeClassMatchers.add(matcher);
/* 271 */       return this;
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
/*     */   protected BasicPolymorphicTypeValidator(Set<Class<?>> invalidBaseTypes, TypeMatcher[] baseTypeMatchers, NameMatcher[] subTypeNameMatchers, TypeMatcher[] subClassMatchers) {
/* 309 */     this._invalidBaseTypes = invalidBaseTypes;
/* 310 */     this._baseTypeMatchers = baseTypeMatchers;
/* 311 */     this._subTypeNameMatchers = subTypeNameMatchers;
/* 312 */     this._subClassMatchers = subClassMatchers;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/* 316 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public PolymorphicTypeValidator.Validity validateBaseType(MapperConfig<?> ctxt, JavaType baseType) {
/* 321 */     Class<?> rawBase = baseType.getRawClass();
/* 322 */     if (this._invalidBaseTypes != null && 
/* 323 */       this._invalidBaseTypes.contains(rawBase)) {
/* 324 */       return PolymorphicTypeValidator.Validity.DENIED;
/*     */     }
/*     */     
/* 327 */     if (this._baseTypeMatchers != null) {
/* 328 */       for (TypeMatcher m : this._baseTypeMatchers) {
/* 329 */         if (m.match(rawBase)) {
/* 330 */           return PolymorphicTypeValidator.Validity.ALLOWED;
/*     */         }
/*     */       } 
/*     */     }
/* 334 */     return PolymorphicTypeValidator.Validity.INDETERMINATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolymorphicTypeValidator.Validity validateSubClassName(MapperConfig<?> ctxt, JavaType baseType, String subClassName) throws JsonMappingException {
/* 342 */     if (this._subTypeNameMatchers != null) {
/* 343 */       for (NameMatcher m : this._subTypeNameMatchers) {
/* 344 */         if (m.match(subClassName)) {
/* 345 */           return PolymorphicTypeValidator.Validity.ALLOWED;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 350 */     return PolymorphicTypeValidator.Validity.INDETERMINATE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PolymorphicTypeValidator.Validity validateSubType(MapperConfig<?> ctxt, JavaType baseType, JavaType subType) throws JsonMappingException {
/* 356 */     if (this._subClassMatchers != null) {
/* 357 */       Class<?> subClass = subType.getRawClass();
/* 358 */       for (TypeMatcher m : this._subClassMatchers) {
/* 359 */         if (m.match(subClass)) {
/* 360 */           return PolymorphicTypeValidator.Validity.ALLOWED;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 365 */     return PolymorphicTypeValidator.Validity.INDETERMINATE;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\BasicPolymorphicTypeValidator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */