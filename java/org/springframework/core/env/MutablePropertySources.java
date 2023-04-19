/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class MutablePropertySources
/*     */   implements PropertySources
/*     */ {
/*     */   private final Log logger;
/*  44 */   private final List<PropertySource<?>> propertySourceList = new CopyOnWriteArrayList<PropertySource<?>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertySources() {
/*  51 */     this.logger = LogFactory.getLog(getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertySources(PropertySources propertySources) {
/*  59 */     this();
/*  60 */     for (PropertySource<?> propertySource : (Iterable<PropertySource<?>>)propertySources) {
/*  61 */       addLast(propertySource);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MutablePropertySources(Log logger) {
/*  70 */     this.logger = logger;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(String name) {
/*  76 */     return this.propertySourceList.contains(PropertySource.named(name));
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertySource<?> get(String name) {
/*  81 */     int index = this.propertySourceList.indexOf(PropertySource.named(name));
/*  82 */     return (index != -1) ? this.propertySourceList.get(index) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<PropertySource<?>> iterator() {
/*  87 */     return this.propertySourceList.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirst(PropertySource<?> propertySource) {
/*  94 */     if (this.logger.isDebugEnabled()) {
/*  95 */       this.logger.debug("Adding PropertySource '" + propertySource.getName() + "' with highest search precedence");
/*     */     }
/*  97 */     removeIfPresent(propertySource);
/*  98 */     this.propertySourceList.add(0, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLast(PropertySource<?> propertySource) {
/* 105 */     if (this.logger.isDebugEnabled()) {
/* 106 */       this.logger.debug("Adding PropertySource '" + propertySource.getName() + "' with lowest search precedence");
/*     */     }
/* 108 */     removeIfPresent(propertySource);
/* 109 */     this.propertySourceList.add(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
/* 117 */     if (this.logger.isDebugEnabled()) {
/* 118 */       this.logger.debug("Adding PropertySource '" + propertySource.getName() + "' with search precedence immediately higher than '" + relativePropertySourceName + "'");
/*     */     }
/*     */     
/* 121 */     assertLegalRelativeAddition(relativePropertySourceName, propertySource);
/* 122 */     removeIfPresent(propertySource);
/* 123 */     int index = assertPresentAndGetIndex(relativePropertySourceName);
/* 124 */     addAtIndex(index, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
/* 132 */     if (this.logger.isDebugEnabled()) {
/* 133 */       this.logger.debug("Adding PropertySource '" + propertySource.getName() + "' with search precedence immediately lower than '" + relativePropertySourceName + "'");
/*     */     }
/*     */     
/* 136 */     assertLegalRelativeAddition(relativePropertySourceName, propertySource);
/* 137 */     removeIfPresent(propertySource);
/* 138 */     int index = assertPresentAndGetIndex(relativePropertySourceName);
/* 139 */     addAtIndex(index + 1, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int precedenceOf(PropertySource<?> propertySource) {
/* 146 */     return this.propertySourceList.indexOf(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySource<?> remove(String name) {
/* 154 */     if (this.logger.isDebugEnabled()) {
/* 155 */       this.logger.debug("Removing PropertySource '" + name + "'");
/*     */     }
/* 157 */     int index = this.propertySourceList.indexOf(PropertySource.named(name));
/* 158 */     return (index != -1) ? this.propertySourceList.remove(index) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replace(String name, PropertySource<?> propertySource) {
/* 169 */     if (this.logger.isDebugEnabled()) {
/* 170 */       this.logger.debug("Replacing PropertySource '" + name + "' with '" + propertySource.getName() + "'");
/*     */     }
/* 172 */     int index = assertPresentAndGetIndex(name);
/* 173 */     this.propertySourceList.set(index, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 180 */     return this.propertySourceList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return this.propertySourceList.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource<?> propertySource) {
/* 192 */     String newPropertySourceName = propertySource.getName();
/* 193 */     if (relativePropertySourceName.equals(newPropertySourceName)) {
/* 194 */       throw new IllegalArgumentException("PropertySource named '" + newPropertySourceName + "' cannot be added relative to itself");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeIfPresent(PropertySource<?> propertySource) {
/* 203 */     this.propertySourceList.remove(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAtIndex(int index, PropertySource<?> propertySource) {
/* 210 */     removeIfPresent(propertySource);
/* 211 */     this.propertySourceList.add(index, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int assertPresentAndGetIndex(String name) {
/* 220 */     int index = this.propertySourceList.indexOf(PropertySource.named(name));
/* 221 */     if (index == -1) {
/* 222 */       throw new IllegalArgumentException("PropertySource named '" + name + "' does not exist");
/*     */     }
/* 224 */     return index;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\MutablePropertySources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */