/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SystemEnvironmentPropertySource
/*     */   extends MapPropertySource
/*     */ {
/*     */   public SystemEnvironmentPropertySource(String name, Map<String, Object> source) {
/*  72 */     super(name, source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String name) {
/*  82 */     return (getProperty(name) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) {
/*  91 */     String actualName = resolvePropertyName(name);
/*  92 */     if (this.logger.isDebugEnabled() && !name.equals(actualName)) {
/*  93 */       this.logger.debug("PropertySource '" + getName() + "' does not contain property '" + name + "', but found equivalent '" + actualName + "'");
/*     */     }
/*     */     
/*  96 */     return super.getProperty(actualName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String resolvePropertyName(String name) {
/* 105 */     Assert.notNull(name, "Property name must not be null");
/* 106 */     String resolvedName = checkPropertyName(name);
/* 107 */     if (resolvedName != null) {
/* 108 */       return resolvedName;
/*     */     }
/* 110 */     String uppercasedName = name.toUpperCase();
/* 111 */     if (!name.equals(uppercasedName)) {
/* 112 */       resolvedName = checkPropertyName(uppercasedName);
/* 113 */       if (resolvedName != null) {
/* 114 */         return resolvedName;
/*     */       }
/*     */     } 
/* 117 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   private String checkPropertyName(String name) {
/* 122 */     if (containsKey(name)) {
/* 123 */       return name;
/*     */     }
/*     */     
/* 126 */     String noDotName = name.replace('.', '_');
/* 127 */     if (!name.equals(noDotName) && containsKey(noDotName)) {
/* 128 */       return noDotName;
/*     */     }
/*     */     
/* 131 */     String noHyphenName = name.replace('-', '_');
/* 132 */     if (!name.equals(noHyphenName) && containsKey(noHyphenName)) {
/* 133 */       return noHyphenName;
/*     */     }
/*     */     
/* 136 */     String noDotNoHyphenName = noDotName.replace('-', '_');
/* 137 */     if (!noDotName.equals(noDotNoHyphenName) && containsKey(noDotNoHyphenName)) {
/* 138 */       return noDotNoHyphenName;
/*     */     }
/*     */     
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   private boolean containsKey(String name) {
/* 145 */     return isSecurityManagerPresent() ? this.source.keySet().contains(name) : this.source.containsKey(name);
/*     */   }
/*     */   
/*     */   protected boolean isSecurityManagerPresent() {
/* 149 */     return (System.getSecurityManager() != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\SystemEnvironmentPropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */