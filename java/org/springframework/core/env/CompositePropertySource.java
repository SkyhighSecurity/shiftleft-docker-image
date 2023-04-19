/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class CompositePropertySource
/*     */   extends EnumerablePropertySource<Object>
/*     */ {
/*  44 */   private final Set<PropertySource<?>> propertySources = new LinkedHashSet<PropertySource<?>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositePropertySource(String name) {
/*  52 */     super(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) {
/*  58 */     for (PropertySource<?> propertySource : this.propertySources) {
/*  59 */       Object candidate = propertySource.getProperty(name);
/*  60 */       if (candidate != null) {
/*  61 */         return candidate;
/*     */       }
/*     */     } 
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String name) {
/*  69 */     for (PropertySource<?> propertySource : this.propertySources) {
/*  70 */       if (propertySource.containsProperty(name)) {
/*  71 */         return true;
/*     */       }
/*     */     } 
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getPropertyNames() {
/*  79 */     Set<String> names = new LinkedHashSet<String>();
/*  80 */     for (PropertySource<?> propertySource : this.propertySources) {
/*  81 */       if (!(propertySource instanceof EnumerablePropertySource)) {
/*  82 */         throw new IllegalStateException("Failed to enumerate property names due to non-enumerable property source: " + propertySource);
/*     */       }
/*     */       
/*  85 */       names.addAll(Arrays.asList(((EnumerablePropertySource)propertySource).getPropertyNames()));
/*     */     } 
/*  87 */     return StringUtils.toStringArray(names);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertySource(PropertySource<?> propertySource) {
/*  96 */     this.propertySources.add(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirstPropertySource(PropertySource<?> propertySource) {
/* 105 */     List<PropertySource<?>> existing = new ArrayList<PropertySource<?>>(this.propertySources);
/* 106 */     this.propertySources.clear();
/* 107 */     this.propertySources.add(propertySource);
/* 108 */     this.propertySources.addAll(existing);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<PropertySource<?>> getPropertySources() {
/* 116 */     return this.propertySources;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return String.format("%s [name='%s', propertySources=%s]", new Object[] {
/* 123 */           getClass().getSimpleName(), this.name, this.propertySources
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\CompositePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */