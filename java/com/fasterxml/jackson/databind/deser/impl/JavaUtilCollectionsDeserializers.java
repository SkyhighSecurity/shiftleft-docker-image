/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public abstract class JavaUtilCollectionsDeserializers
/*     */ {
/*     */   private static final int TYPE_SINGLETON_SET = 1;
/*     */   private static final int TYPE_SINGLETON_LIST = 2;
/*     */   private static final int TYPE_SINGLETON_MAP = 3;
/*     */   private static final int TYPE_UNMODIFIABLE_SET = 4;
/*     */   private static final int TYPE_UNMODIFIABLE_LIST = 5;
/*     */   private static final int TYPE_UNMODIFIABLE_MAP = 6;
/*     */   public static final int TYPE_AS_LIST = 7;
/*  35 */   private static final Class<?> CLASS_AS_ARRAYS_LIST = Arrays.<Object>asList(new Object[] { null, null }).getClass();
/*     */   
/*     */   private static final Class<?> CLASS_SINGLETON_SET;
/*     */   
/*     */   private static final Class<?> CLASS_SINGLETON_LIST;
/*     */   
/*     */   private static final Class<?> CLASS_SINGLETON_MAP;
/*     */   
/*     */   private static final Class<?> CLASS_UNMODIFIABLE_SET;
/*     */   
/*     */   private static final Class<?> CLASS_UNMODIFIABLE_LIST;
/*     */   
/*     */   private static final Class<?> CLASS_UNMODIFIABLE_LIST_ALIAS;
/*     */   private static final Class<?> CLASS_UNMODIFIABLE_MAP;
/*     */   
/*     */   static {
/*  51 */     Set<?> set = Collections.singleton(Boolean.TRUE);
/*  52 */     CLASS_SINGLETON_SET = set.getClass();
/*  53 */     CLASS_UNMODIFIABLE_SET = Collections.unmodifiableSet(set).getClass();
/*     */     
/*  55 */     List<?> list = Collections.singletonList(Boolean.TRUE);
/*  56 */     CLASS_SINGLETON_LIST = list.getClass();
/*  57 */     CLASS_UNMODIFIABLE_LIST = Collections.unmodifiableList(list).getClass();
/*     */     
/*  59 */     CLASS_UNMODIFIABLE_LIST_ALIAS = Collections.unmodifiableList(new LinkedList()).getClass();
/*     */     
/*  61 */     Map<?, ?> map = Collections.singletonMap("a", "b");
/*  62 */     CLASS_SINGLETON_MAP = map.getClass();
/*  63 */     CLASS_UNMODIFIABLE_MAP = Collections.unmodifiableMap(map).getClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonDeserializer<?> findForCollection(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
/*     */     JavaUtilCollectionsConverter conv;
/*  73 */     if (type.hasRawClass(CLASS_AS_ARRAYS_LIST)) {
/*  74 */       conv = converter(7, type, List.class);
/*  75 */     } else if (type.hasRawClass(CLASS_SINGLETON_LIST)) {
/*  76 */       conv = converter(2, type, List.class);
/*  77 */     } else if (type.hasRawClass(CLASS_SINGLETON_SET)) {
/*  78 */       conv = converter(1, type, Set.class);
/*     */     }
/*  80 */     else if (type.hasRawClass(CLASS_UNMODIFIABLE_LIST) || type.hasRawClass(CLASS_UNMODIFIABLE_LIST_ALIAS)) {
/*  81 */       conv = converter(5, type, List.class);
/*  82 */     } else if (type.hasRawClass(CLASS_UNMODIFIABLE_SET)) {
/*  83 */       conv = converter(4, type, Set.class);
/*     */     } else {
/*  85 */       return null;
/*     */     } 
/*  87 */     return (JsonDeserializer<?>)new StdDelegatingDeserializer(conv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonDeserializer<?> findForMap(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
/*     */     JavaUtilCollectionsConverter conv;
/*  97 */     if (type.hasRawClass(CLASS_SINGLETON_MAP)) {
/*  98 */       conv = converter(3, type, Map.class);
/*  99 */     } else if (type.hasRawClass(CLASS_UNMODIFIABLE_MAP)) {
/* 100 */       conv = converter(6, type, Map.class);
/*     */     } else {
/* 102 */       return null;
/*     */     } 
/* 104 */     return (JsonDeserializer<?>)new StdDelegatingDeserializer(conv);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static JavaUtilCollectionsConverter converter(int kind, JavaType concreteType, Class<?> rawSuper) {
/* 110 */     return new JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JavaUtilCollectionsConverter
/*     */     implements Converter<Object, Object>
/*     */   {
/*     */     private final JavaType _inputType;
/*     */ 
/*     */     
/*     */     private final int _kind;
/*     */ 
/*     */     
/*     */     private JavaUtilCollectionsConverter(int kind, JavaType inputType) {
/* 125 */       this._inputType = inputType;
/* 126 */       this._kind = kind; } public Object convert(Object value) {
/*     */       Set<?> set;
/*     */       List<?> list;
/*     */       Map<?, ?> map;
/*     */       Map.Entry<?, ?> entry;
/* 131 */       if (value == null) {
/* 132 */         return null;
/*     */       }
/*     */       
/* 135 */       switch (this._kind) {
/*     */         
/*     */         case 1:
/* 138 */           set = (Set)value;
/* 139 */           _checkSingleton(set.size());
/* 140 */           return Collections.singleton(set.iterator().next());
/*     */ 
/*     */         
/*     */         case 2:
/* 144 */           list = (List)value;
/* 145 */           _checkSingleton(list.size());
/* 146 */           return Collections.singletonList(list.get(0));
/*     */ 
/*     */         
/*     */         case 3:
/* 150 */           map = (Map<?, ?>)value;
/* 151 */           _checkSingleton(map.size());
/* 152 */           entry = map.entrySet().iterator().next();
/* 153 */           return Collections.singletonMap(entry.getKey(), entry.getValue());
/*     */ 
/*     */         
/*     */         case 4:
/* 157 */           return Collections.unmodifiableSet((Set)value);
/*     */         case 5:
/* 159 */           return Collections.unmodifiableList((List)value);
/*     */         case 6:
/* 161 */           return Collections.unmodifiableMap((Map<?, ?>)value);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 166 */       return value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JavaType getInputType(TypeFactory typeFactory) {
/* 172 */       return this._inputType;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JavaType getOutputType(TypeFactory typeFactory) {
/* 178 */       return this._inputType;
/*     */     }
/*     */     
/*     */     private void _checkSingleton(int size) {
/* 182 */       if (size != 1)
/*     */       {
/* 184 */         throw new IllegalArgumentException("Can not deserialize Singleton container from " + size + " entries");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\JavaUtilCollectionsDeserializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */