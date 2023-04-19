/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.deser.CreatorProperty;
/*    */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*    */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonLocationInstantiator
/*    */   extends ValueInstantiator.Base
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public JsonLocationInstantiator() {
/* 24 */     super(JsonLocation.class);
/*    */   }
/*    */   
/*    */   public boolean canCreateFromObjectWith() {
/* 28 */     return true;
/*    */   }
/*    */   
/*    */   public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
/* 32 */     JavaType intType = config.constructType(int.class);
/* 33 */     JavaType longType = config.constructType(long.class);
/* 34 */     return new SettableBeanProperty[] {
/* 35 */         (SettableBeanProperty)creatorProp("sourceRef", config.constructType(Object.class), 0), 
/* 36 */         (SettableBeanProperty)creatorProp("byteOffset", longType, 1), 
/* 37 */         (SettableBeanProperty)creatorProp("charOffset", longType, 2), 
/* 38 */         (SettableBeanProperty)creatorProp("lineNr", intType, 3), 
/* 39 */         (SettableBeanProperty)creatorProp("columnNr", intType, 4)
/*    */       };
/*    */   }
/*    */   
/*    */   private static CreatorProperty creatorProp(String name, JavaType type, int index) {
/* 44 */     return new CreatorProperty(PropertyName.construct(name), type, null, null, null, null, index, null, PropertyMetadata.STD_REQUIRED);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) {
/* 50 */     return new JsonLocation(args[0], _long(args[1]), _long(args[2]), 
/* 51 */         _int(args[3]), _int(args[4]));
/*    */   }
/*    */   
/*    */   private static final long _long(Object o) {
/* 55 */     return (o == null) ? 0L : ((Number)o).longValue();
/*    */   }
/*    */   
/*    */   private static final int _int(Object o) {
/* 59 */     return (o == null) ? 0 : ((Number)o).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\JsonLocationInstantiator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */