/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
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
/*     */ public class SimpleAliasRegistry
/*     */   implements AliasRegistry
/*     */ {
/*  41 */   private final Map<String, String> aliasMap = new ConcurrentHashMap<String, String>(16);
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerAlias(String name, String alias) {
/*  46 */     Assert.hasText(name, "'name' must not be empty");
/*  47 */     Assert.hasText(alias, "'alias' must not be empty");
/*  48 */     synchronized (this.aliasMap) {
/*  49 */       if (alias.equals(name)) {
/*  50 */         this.aliasMap.remove(alias);
/*     */       } else {
/*     */         
/*  53 */         String registeredName = this.aliasMap.get(alias);
/*  54 */         if (registeredName != null) {
/*  55 */           if (registeredName.equals(name)) {
/*     */             return;
/*     */           }
/*     */           
/*  59 */           if (!allowAliasOverriding()) {
/*  60 */             throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': It is already registered for name '" + registeredName + "'.");
/*     */           }
/*     */         } 
/*     */         
/*  64 */         checkForAliasCircle(name, alias);
/*  65 */         this.aliasMap.put(alias, name);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean allowAliasOverriding() {
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAlias(String name, String alias) {
/*  85 */     for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
/*  86 */       String registeredName = entry.getValue();
/*  87 */       if (registeredName.equals(name)) {
/*  88 */         String registeredAlias = entry.getKey();
/*  89 */         if (registeredAlias.equals(alias) || hasAlias(registeredAlias, alias)) {
/*  90 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAlias(String alias) {
/*  99 */     synchronized (this.aliasMap) {
/* 100 */       String name = this.aliasMap.remove(alias);
/* 101 */       if (name == null) {
/* 102 */         throw new IllegalStateException("No alias '" + alias + "' registered");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlias(String name) {
/* 109 */     return this.aliasMap.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAliases(String name) {
/* 114 */     List<String> result = new ArrayList<String>();
/* 115 */     synchronized (this.aliasMap) {
/* 116 */       retrieveAliases(name, result);
/*     */     } 
/* 118 */     return StringUtils.toStringArray(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void retrieveAliases(String name, List<String> result) {
/* 127 */     for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
/* 128 */       String registeredName = entry.getValue();
/* 129 */       if (registeredName.equals(name)) {
/* 130 */         String alias = entry.getKey();
/* 131 */         result.add(alias);
/* 132 */         retrieveAliases(alias, result);
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
/*     */   public void resolveAliases(StringValueResolver valueResolver) {
/* 145 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/* 146 */     synchronized (this.aliasMap) {
/* 147 */       Map<String, String> aliasCopy = new HashMap<String, String>(this.aliasMap);
/* 148 */       for (String alias : aliasCopy.keySet()) {
/* 149 */         String registeredName = aliasCopy.get(alias);
/* 150 */         String resolvedAlias = valueResolver.resolveStringValue(alias);
/* 151 */         String resolvedName = valueResolver.resolveStringValue(registeredName);
/* 152 */         if (resolvedAlias == null || resolvedName == null || resolvedAlias.equals(resolvedName)) {
/* 153 */           this.aliasMap.remove(alias); continue;
/*     */         } 
/* 155 */         if (!resolvedAlias.equals(alias)) {
/* 156 */           String existingName = this.aliasMap.get(resolvedAlias);
/* 157 */           if (existingName != null) {
/* 158 */             if (existingName.equals(resolvedName)) {
/*     */               
/* 160 */               this.aliasMap.remove(alias);
/*     */               break;
/*     */             } 
/* 163 */             throw new IllegalStateException("Cannot register resolved alias '" + resolvedAlias + "' (original: '" + alias + "') for name '" + resolvedName + "': It is already registered for name '" + registeredName + "'.");
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 168 */           checkForAliasCircle(resolvedName, resolvedAlias);
/* 169 */           this.aliasMap.remove(alias);
/* 170 */           this.aliasMap.put(resolvedAlias, resolvedName); continue;
/*     */         } 
/* 172 */         if (!registeredName.equals(resolvedName)) {
/* 173 */           this.aliasMap.put(alias, resolvedName);
/*     */         }
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
/*     */   protected void checkForAliasCircle(String name, String alias) {
/* 189 */     if (hasAlias(alias, name)) {
/* 190 */       throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': Circular reference - '" + name + "' is a direct or indirect alias for '" + alias + "' already");
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
/*     */   public String canonicalName(String name) {
/* 202 */     String canonicalName = name;
/*     */ 
/*     */     
/*     */     while (true) {
/* 206 */       String resolvedName = this.aliasMap.get(canonicalName);
/* 207 */       if (resolvedName != null) {
/* 208 */         canonicalName = resolvedName;
/*     */       }
/*     */       
/* 211 */       if (resolvedName == null)
/* 212 */         return canonicalName; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\SimpleAliasRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */