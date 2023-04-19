/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public final class PropertyBasedCreator
/*     */ {
/*     */   protected final int _propertyCount;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final HashMap<String, SettableBeanProperty> _propertyLookup;
/*     */   protected final SettableBeanProperty[] _allProperties;
/*     */   
/*     */   protected PropertyBasedCreator(DeserializationContext ctxt, ValueInstantiator valueInstantiator, SettableBeanProperty[] creatorProps, boolean caseInsensitive, boolean addAliases) {
/*  59 */     this._valueInstantiator = valueInstantiator;
/*  60 */     if (caseInsensitive) {
/*  61 */       this._propertyLookup = new CaseInsensitiveMap();
/*     */     } else {
/*  63 */       this._propertyLookup = new HashMap<>();
/*     */     } 
/*  65 */     int len = creatorProps.length;
/*  66 */     this._propertyCount = len;
/*  67 */     this._allProperties = new SettableBeanProperty[len];
/*     */ 
/*     */ 
/*     */     
/*  71 */     if (addAliases) {
/*  72 */       DeserializationConfig config = ctxt.getConfig();
/*  73 */       for (SettableBeanProperty prop : creatorProps) {
/*     */         
/*  75 */         if (!prop.isIgnorable()) {
/*  76 */           List<PropertyName> aliases = prop.findAliases((MapperConfig)config);
/*  77 */           if (!aliases.isEmpty()) {
/*  78 */             for (PropertyName pn : aliases) {
/*  79 */               this._propertyLookup.put(pn.getSimpleName(), prop);
/*     */             }
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*  85 */     for (int i = 0; i < len; i++) {
/*  86 */       SettableBeanProperty prop = creatorProps[i];
/*  87 */       this._allProperties[i] = prop;
/*     */       
/*  89 */       if (!prop.isIgnorable()) {
/*  90 */         this._propertyLookup.put(prop.getName(), prop);
/*     */       }
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
/*     */   public static PropertyBasedCreator construct(DeserializationContext ctxt, ValueInstantiator valueInstantiator, SettableBeanProperty[] srcCreatorProps, BeanPropertyMap allProperties) throws JsonMappingException {
/* 106 */     int len = srcCreatorProps.length;
/* 107 */     SettableBeanProperty[] creatorProps = new SettableBeanProperty[len];
/* 108 */     for (int i = 0; i < len; i++) {
/* 109 */       SettableBeanProperty prop = srcCreatorProps[i];
/* 110 */       if (!prop.hasValueDeserializer()) {
/* 111 */         prop = prop.withValueDeserializer(ctxt.findContextualValueDeserializer(prop.getType(), (BeanProperty)prop));
/*     */       }
/* 113 */       creatorProps[i] = prop;
/*     */     } 
/* 115 */     return new PropertyBasedCreator(ctxt, valueInstantiator, creatorProps, allProperties
/* 116 */         .isCaseInsensitive(), true);
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
/*     */   public static PropertyBasedCreator construct(DeserializationContext ctxt, ValueInstantiator valueInstantiator, SettableBeanProperty[] srcCreatorProps, boolean caseInsensitive) throws JsonMappingException {
/* 135 */     int len = srcCreatorProps.length;
/* 136 */     SettableBeanProperty[] creatorProps = new SettableBeanProperty[len];
/* 137 */     for (int i = 0; i < len; i++) {
/* 138 */       SettableBeanProperty prop = srcCreatorProps[i];
/* 139 */       if (!prop.hasValueDeserializer()) {
/* 140 */         prop = prop.withValueDeserializer(ctxt.findContextualValueDeserializer(prop.getType(), (BeanProperty)prop));
/*     */       }
/* 142 */       creatorProps[i] = prop;
/*     */     } 
/* 144 */     return new PropertyBasedCreator(ctxt, valueInstantiator, creatorProps, caseInsensitive, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static PropertyBasedCreator construct(DeserializationContext ctxt, ValueInstantiator valueInstantiator, SettableBeanProperty[] srcCreatorProps) throws JsonMappingException {
/* 153 */     return construct(ctxt, valueInstantiator, srcCreatorProps, ctxt
/* 154 */         .isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<SettableBeanProperty> properties() {
/* 164 */     return this._propertyLookup.values();
/*     */   }
/*     */   
/*     */   public SettableBeanProperty findCreatorProperty(String name) {
/* 168 */     return this._propertyLookup.get(name);
/*     */   }
/*     */   
/*     */   public SettableBeanProperty findCreatorProperty(int propertyIndex) {
/* 172 */     for (SettableBeanProperty prop : this._propertyLookup.values()) {
/* 173 */       if (prop.getPropertyIndex() == propertyIndex) {
/* 174 */         return prop;
/*     */       }
/*     */     } 
/* 177 */     return null;
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
/*     */   public PropertyValueBuffer startBuilding(JsonParser p, DeserializationContext ctxt, ObjectIdReader oir) {
/* 193 */     return new PropertyValueBuffer(p, ctxt, this._propertyCount, oir);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
/* 198 */     Object bean = this._valueInstantiator.createFromObjectWith(ctxt, this._allProperties, buffer);
/*     */ 
/*     */     
/* 201 */     if (bean != null) {
/*     */       
/* 203 */       bean = buffer.handleIdValue(ctxt, bean);
/*     */ 
/*     */       
/* 206 */       for (PropertyValue pv = buffer.buffered(); pv != null; pv = pv.next) {
/* 207 */         pv.assign(bean);
/*     */       }
/*     */     } 
/* 210 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class CaseInsensitiveMap
/*     */     extends HashMap<String, SettableBeanProperty>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SettableBeanProperty get(Object key0) {
/* 231 */       return super.get(((String)key0).toLowerCase());
/*     */     }
/*     */ 
/*     */     
/*     */     public SettableBeanProperty put(String key, SettableBeanProperty value) {
/* 236 */       key = key.toLowerCase();
/* 237 */       return super.put(key, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\impl\PropertyBasedCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */