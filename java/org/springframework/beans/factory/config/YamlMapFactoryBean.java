/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class YamlMapFactoryBean
/*     */   extends YamlProcessor
/*     */   implements FactoryBean<Map<String, Object>>, InitializingBean
/*     */ {
/*     */   private boolean singleton = true;
/*     */   private Map<String, Object> map;
/*     */   
/*     */   public void setSingleton(boolean singleton) {
/*  84 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/*  89 */     return this.singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  94 */     if (isSingleton()) {
/*  95 */       this.map = createMap();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getObject() {
/* 101 */     return (this.map != null) ? this.map : createMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 106 */     return Map.class;
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
/*     */   protected Map<String, Object> createMap() {
/* 120 */     final Map<String, Object> result = new LinkedHashMap<String, Object>();
/* 121 */     process(new YamlProcessor.MatchCallback()
/*     */         {
/*     */           public void process(Properties properties, Map<String, Object> map) {
/* 124 */             YamlMapFactoryBean.this.merge(result, map);
/*     */           }
/*     */         });
/* 127 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private void merge(Map<String, Object> output, Map<String, Object> map) {
/* 132 */     for (Map.Entry<String, Object> entry : map.entrySet()) {
/* 133 */       String key = entry.getKey();
/* 134 */       Object value = entry.getValue();
/* 135 */       Object existing = output.get(key);
/* 136 */       if (value instanceof Map && existing instanceof Map) {
/* 137 */         Map<String, Object> result = new LinkedHashMap<String, Object>((Map<? extends String, ?>)existing);
/* 138 */         merge(result, (Map<String, Object>)value);
/* 139 */         output.put(key, result);
/*     */         continue;
/*     */       } 
/* 142 */       output.put(key, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\YamlMapFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */