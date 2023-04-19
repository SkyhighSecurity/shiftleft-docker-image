/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
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
/*     */ public class CustomCollectionEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final Class<? extends Collection> collectionType;
/*     */   private final boolean nullAsEmptyCollection;
/*     */   
/*     */   public CustomCollectionEditor(Class<? extends Collection> collectionType) {
/*  63 */     this(collectionType, false);
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
/*     */   public CustomCollectionEditor(Class<? extends Collection> collectionType, boolean nullAsEmptyCollection) {
/*  86 */     if (collectionType == null) {
/*  87 */       throw new IllegalArgumentException("Collection type is required");
/*     */     }
/*  89 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/*  90 */       throw new IllegalArgumentException("Collection type [" + collectionType
/*  91 */           .getName() + "] does not implement [java.util.Collection]");
/*     */     }
/*  93 */     this.collectionType = collectionType;
/*  94 */     this.nullAsEmptyCollection = nullAsEmptyCollection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/* 103 */     setValue(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) {
/* 111 */     if (value == null && this.nullAsEmptyCollection) {
/* 112 */       super.setValue(createCollection(this.collectionType, 0));
/*     */     }
/* 114 */     else if (value == null || (this.collectionType.isInstance(value) && !alwaysCreateNewCollection())) {
/*     */       
/* 116 */       super.setValue(value);
/*     */     }
/* 118 */     else if (value instanceof Collection) {
/*     */       
/* 120 */       Collection<?> source = (Collection)value;
/* 121 */       Collection<Object> target = createCollection(this.collectionType, source.size());
/* 122 */       for (Object elem : source) {
/* 123 */         target.add(convertElement(elem));
/*     */       }
/* 125 */       super.setValue(target);
/*     */     }
/* 127 */     else if (value.getClass().isArray()) {
/*     */       
/* 129 */       int length = Array.getLength(value);
/* 130 */       Collection<Object> target = createCollection(this.collectionType, length);
/* 131 */       for (int i = 0; i < length; i++) {
/* 132 */         target.add(convertElement(Array.get(value, i)));
/*     */       }
/* 134 */       super.setValue(target);
/*     */     }
/*     */     else {
/*     */       
/* 138 */       Collection<Object> target = createCollection(this.collectionType, 1);
/* 139 */       target.add(convertElement(value));
/* 140 */       super.setValue(target);
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
/*     */   protected Collection<Object> createCollection(Class<? extends Collection> collectionType, int initialCapacity) {
/* 153 */     if (!collectionType.isInterface()) {
/*     */       try {
/* 155 */         return collectionType.newInstance();
/*     */       }
/* 157 */       catch (Throwable ex) {
/* 158 */         throw new IllegalArgumentException("Could not instantiate collection class: " + collectionType
/* 159 */             .getName(), ex);
/*     */       } 
/*     */     }
/* 162 */     if (List.class == collectionType) {
/* 163 */       return new ArrayList(initialCapacity);
/*     */     }
/* 165 */     if (SortedSet.class == collectionType) {
/* 166 */       return new TreeSet();
/*     */     }
/*     */     
/* 169 */     return new LinkedHashSet(initialCapacity);
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
/*     */   protected boolean alwaysCreateNewCollection() {
/* 181 */     return false;
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
/*     */   protected Object convertElement(Object element) {
/* 199 */     return element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 209 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\CustomCollectionEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */