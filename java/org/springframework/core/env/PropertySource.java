/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertySource<T>
/*     */ {
/*  60 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   protected final String name;
/*     */ 
/*     */   
/*     */   protected final T source;
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySource(String name, T source) {
/*  71 */     Assert.hasText(name, "Property source name must contain at least one character");
/*  72 */     Assert.notNull(source, "Property source must not be null");
/*  73 */     this.name = name;
/*  74 */     this.source = source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySource(String name) {
/*  85 */     this(name, (T)new Object());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  93 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getSource() {
/* 100 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String name) {
/* 111 */     return (getProperty(name) != null);
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
/*     */   public abstract Object getProperty(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 133 */     return (this == obj || (obj instanceof PropertySource && 
/* 134 */       ObjectUtils.nullSafeEquals(this.name, ((PropertySource)obj).name)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 143 */     return ObjectUtils.nullSafeHashCode(this.name);
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
/*     */   public String toString() {
/* 157 */     if (this.logger.isDebugEnabled()) {
/* 158 */       return getClass().getSimpleName() + "@" + System.identityHashCode(this) + " {name='" + this.name + "', properties=" + this.source + "}";
/*     */     }
/*     */ 
/*     */     
/* 162 */     return getClass().getSimpleName() + " {name='" + this.name + "'}";
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
/*     */   public static PropertySource<?> named(String name) {
/* 185 */     return new ComparisonPropertySource(name);
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
/*     */   public static class StubPropertySource
/*     */     extends PropertySource<Object>
/*     */   {
/*     */     public StubPropertySource(String name) {
/* 204 */       super(name, new Object());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getProperty(String name) {
/* 212 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class ComparisonPropertySource
/*     */     extends StubPropertySource
/*     */   {
/*     */     private static final String USAGE_ERROR = "ComparisonPropertySource instances are for use with collection comparison only";
/*     */ 
/*     */ 
/*     */     
/*     */     public ComparisonPropertySource(String name) {
/* 226 */       super(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 231 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsProperty(String name) {
/* 236 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
/*     */     }
/*     */ 
/*     */     
/*     */     public String getProperty(String name) {
/* 241 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\PropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */