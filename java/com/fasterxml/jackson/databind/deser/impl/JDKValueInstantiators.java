/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.std.JsonLocationInstantiator;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
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
/*     */ public abstract class JDKValueInstantiators
/*     */ {
/*     */   public static ValueInstantiator findStdValueInstantiator(DeserializationConfig config, Class<?> raw) {
/*  29 */     if (raw == JsonLocation.class) {
/*  30 */       return (ValueInstantiator)new JsonLocationInstantiator();
/*     */     }
/*     */ 
/*     */     
/*  34 */     if (Collection.class.isAssignableFrom(raw)) {
/*  35 */       if (raw == ArrayList.class) {
/*  36 */         return (ValueInstantiator)ArrayListInstantiator.INSTANCE;
/*     */       }
/*  38 */       if (Collections.EMPTY_SET.getClass() == raw) {
/*  39 */         return (ValueInstantiator)new ConstantValueInstantiator(Collections.EMPTY_SET);
/*     */       }
/*  41 */       if (Collections.EMPTY_LIST.getClass() == raw) {
/*  42 */         return (ValueInstantiator)new ConstantValueInstantiator(Collections.EMPTY_LIST);
/*     */       }
/*  44 */     } else if (Map.class.isAssignableFrom(raw)) {
/*  45 */       if (raw == LinkedHashMap.class) {
/*  46 */         return (ValueInstantiator)LinkedHashMapInstantiator.INSTANCE;
/*     */       }
/*  48 */       if (raw == HashMap.class) {
/*  49 */         return (ValueInstantiator)HashMapInstantiator.INSTANCE;
/*     */       }
/*  51 */       if (Collections.EMPTY_MAP.getClass() == raw) {
/*  52 */         return (ValueInstantiator)new ConstantValueInstantiator(Collections.EMPTY_MAP);
/*     */       }
/*     */     } 
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ArrayListInstantiator
/*     */     extends ValueInstantiator.Base
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 2L;
/*  64 */     public static final ArrayListInstantiator INSTANCE = new ArrayListInstantiator();
/*     */     public ArrayListInstantiator() {
/*  66 */       super(ArrayList.class);
/*     */     }
/*     */     
/*     */     public boolean canInstantiate() {
/*  70 */       return true;
/*     */     }
/*     */     public boolean canCreateUsingDefault() {
/*  73 */       return true;
/*     */     }
/*     */     
/*     */     public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
/*  77 */       return new ArrayList();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class HashMapInstantiator
/*     */     extends ValueInstantiator.Base
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 2L;
/*  87 */     public static final HashMapInstantiator INSTANCE = new HashMapInstantiator();
/*     */     
/*     */     public HashMapInstantiator() {
/*  90 */       super(HashMap.class);
/*     */     }
/*     */     
/*     */     public boolean canInstantiate() {
/*  94 */       return true;
/*     */     }
/*     */     public boolean canCreateUsingDefault() {
/*  97 */       return true;
/*     */     }
/*     */     
/*     */     public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
/* 101 */       return new HashMap<>();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class LinkedHashMapInstantiator
/*     */     extends ValueInstantiator.Base
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 2L;
/* 111 */     public static final LinkedHashMapInstantiator INSTANCE = new LinkedHashMapInstantiator();
/*     */     
/*     */     public LinkedHashMapInstantiator() {
/* 114 */       super(LinkedHashMap.class);
/*     */     }
/*     */     
/*     */     public boolean canInstantiate() {
/* 118 */       return true;
/*     */     }
/*     */     public boolean canCreateUsingDefault() {
/* 121 */       return true;
/*     */     }
/*     */     
/*     */     public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
/* 125 */       return new LinkedHashMap<>();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConstantValueInstantiator
/*     */     extends ValueInstantiator.Base
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 2L;
/*     */     protected final Object _value;
/*     */     
/*     */     public ConstantValueInstantiator(Object value) {
/* 138 */       super(value.getClass());
/* 139 */       this._value = value;
/*     */     }
/*     */     
/*     */     public boolean canInstantiate() {
/* 143 */       return true;
/*     */     }
/*     */     public boolean canCreateUsingDefault() {
/* 146 */       return true;
/*     */     }
/*     */     
/*     */     public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
/* 150 */       return this._value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\JDKValueInstantiators.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */