/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*    */ import com.fasterxml.jackson.databind.BeanDescription;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*    */ import com.fasterxml.jackson.databind.type.ClassKey;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RootNameLookup
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 22 */   protected transient LRUMap<ClassKey, PropertyName> _rootNames = new LRUMap<>(20, 200);
/*    */ 
/*    */   
/*    */   public PropertyName findRootName(JavaType rootType, MapperConfig<?> config) {
/* 26 */     return findRootName(rootType.getRawClass(), config);
/*    */   }
/*    */ 
/*    */   
/*    */   public PropertyName findRootName(Class<?> rootType, MapperConfig<?> config) {
/* 31 */     ClassKey key = new ClassKey(rootType);
/* 32 */     PropertyName name = this._rootNames.get(key);
/* 33 */     if (name != null) {
/* 34 */       return name;
/*    */     }
/* 36 */     BeanDescription beanDesc = config.introspectClassAnnotations(rootType);
/* 37 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 38 */     AnnotatedClass ac = beanDesc.getClassInfo();
/* 39 */     name = intr.findRootName(ac);
/*    */     
/* 41 */     if (name == null || !name.hasSimpleName())
/*    */     {
/* 43 */       name = PropertyName.construct(rootType.getSimpleName());
/*    */     }
/* 45 */     this._rootNames.put(key, name);
/* 46 */     return name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object readResolve() {
/* 60 */     return new RootNameLookup();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databin\\util\RootNameLookup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */