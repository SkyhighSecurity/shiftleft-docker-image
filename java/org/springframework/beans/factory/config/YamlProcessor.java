/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ import org.yaml.snakeyaml.constructor.BaseConstructor;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.parser.ParserException;
/*     */ import org.yaml.snakeyaml.reader.UnicodeReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class YamlProcessor
/*     */ {
/*  53 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  55 */   private ResolutionMethod resolutionMethod = ResolutionMethod.OVERRIDE;
/*     */   
/*  57 */   private Resource[] resources = new Resource[0];
/*     */   
/*  59 */   private List<DocumentMatcher> documentMatchers = Collections.emptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean matchDefault = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentMatchers(DocumentMatcher... matchers) {
/*  91 */     this.documentMatchers = Arrays.asList(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMatchDefault(boolean matchDefault) {
/* 100 */     this.matchDefault = matchDefault;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResolutionMethod(ResolutionMethod resolutionMethod) {
/* 109 */     Assert.notNull(resolutionMethod, "ResolutionMethod must not be null");
/* 110 */     this.resolutionMethod = resolutionMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResources(Resource... resources) {
/* 118 */     this.resources = resources;
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
/*     */   protected void process(MatchCallback callback) {
/* 133 */     Yaml yaml = createYaml();
/* 134 */     for (Resource resource : this.resources) {
/* 135 */       boolean found = process(callback, yaml, resource);
/* 136 */       if (this.resolutionMethod == ResolutionMethod.FIRST_FOUND && found) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Yaml createYaml() {
/* 146 */     return new Yaml((BaseConstructor)new StrictMapAppenderConstructor());
/*     */   }
/*     */   
/*     */   private boolean process(MatchCallback callback, Yaml yaml, Resource resource) {
/* 150 */     int count = 0;
/*     */     try {
/* 152 */       if (this.logger.isDebugEnabled()) {
/* 153 */         this.logger.debug("Loading from YAML: " + resource);
/*     */       }
/* 155 */       UnicodeReader unicodeReader = new UnicodeReader(resource.getInputStream());
/*     */       try {
/* 157 */         for (Object object : yaml.loadAll((Reader)unicodeReader)) {
/* 158 */           if (object != null && process(asMap(object), callback)) {
/* 159 */             count++;
/* 160 */             if (this.resolutionMethod == ResolutionMethod.FIRST_FOUND) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/* 165 */         if (this.logger.isDebugEnabled()) {
/* 166 */           this.logger.debug("Loaded " + count + " document" + ((count > 1) ? "s" : "") + " from YAML resource: " + resource);
/*     */         }
/*     */       }
/*     */       finally {
/*     */         
/* 171 */         unicodeReader.close();
/*     */       }
/*     */     
/* 174 */     } catch (IOException ex) {
/* 175 */       handleProcessError(resource, ex);
/*     */     } 
/* 177 */     return (count > 0);
/*     */   }
/*     */   
/*     */   private void handleProcessError(Resource resource, IOException ex) {
/* 181 */     if (this.resolutionMethod != ResolutionMethod.FIRST_FOUND && this.resolutionMethod != ResolutionMethod.OVERRIDE_AND_IGNORE)
/*     */     {
/* 183 */       throw new IllegalStateException(ex);
/*     */     }
/* 185 */     if (this.logger.isWarnEnabled()) {
/* 186 */       this.logger.warn("Could not load map from " + resource + ": " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> asMap(Object object) {
/* 193 */     Map<String, Object> result = new LinkedHashMap<String, Object>();
/* 194 */     if (!(object instanceof Map)) {
/*     */       
/* 196 */       result.put("document", object);
/* 197 */       return result;
/*     */     } 
/*     */     
/* 200 */     Map<Object, Object> map = (Map<Object, Object>)object;
/* 201 */     for (Map.Entry<Object, Object> entry : map.entrySet()) {
/* 202 */       Object<String, Object> value = (Object<String, Object>)entry.getValue();
/* 203 */       if (value instanceof Map) {
/* 204 */         value = (Object<String, Object>)asMap(value);
/*     */       }
/* 206 */       Object key = entry.getKey();
/* 207 */       if (key instanceof CharSequence) {
/* 208 */         result.put(key.toString(), value);
/*     */         
/*     */         continue;
/*     */       } 
/* 212 */       result.put("[" + key.toString() + "]", value);
/*     */     } 
/*     */     
/* 215 */     return result;
/*     */   }
/*     */   
/*     */   private boolean process(Map<String, Object> map, MatchCallback callback) {
/* 219 */     Properties properties = CollectionFactory.createStringAdaptingProperties();
/* 220 */     properties.putAll(getFlattenedMap(map));
/*     */     
/* 222 */     if (this.documentMatchers.isEmpty()) {
/* 223 */       if (this.logger.isDebugEnabled()) {
/* 224 */         this.logger.debug("Merging document (no matchers set): " + map);
/*     */       }
/* 226 */       callback.process(properties, map);
/* 227 */       return true;
/*     */     } 
/*     */     
/* 230 */     MatchStatus result = MatchStatus.ABSTAIN;
/* 231 */     for (DocumentMatcher matcher : this.documentMatchers) {
/* 232 */       MatchStatus match = matcher.matches(properties);
/* 233 */       result = MatchStatus.getMostSpecific(match, result);
/* 234 */       if (match == MatchStatus.FOUND) {
/* 235 */         if (this.logger.isDebugEnabled()) {
/* 236 */           this.logger.debug("Matched document with document matcher: " + properties);
/*     */         }
/* 238 */         callback.process(properties, map);
/* 239 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 243 */     if (result == MatchStatus.ABSTAIN && this.matchDefault) {
/* 244 */       if (this.logger.isDebugEnabled()) {
/* 245 */         this.logger.debug("Matched document with default matcher: " + map);
/*     */       }
/* 247 */       callback.process(properties, map);
/* 248 */       return true;
/*     */     } 
/*     */     
/* 251 */     if (this.logger.isDebugEnabled()) {
/* 252 */       this.logger.debug("Unmatched document: " + map);
/*     */     }
/* 254 */     return false;
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
/*     */   protected final Map<String, Object> getFlattenedMap(Map<String, Object> source) {
/* 267 */     Map<String, Object> result = new LinkedHashMap<String, Object>();
/* 268 */     buildFlattenedMap(result, source, null);
/* 269 */     return result;
/*     */   }
/*     */   
/*     */   private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
/* 273 */     for (Map.Entry<String, Object> entry : source.entrySet()) {
/* 274 */       String key = entry.getKey();
/* 275 */       if (StringUtils.hasText(path)) {
/* 276 */         if (key.startsWith("[")) {
/* 277 */           key = path + key;
/*     */         } else {
/*     */           
/* 280 */           key = path + '.' + key;
/*     */         } 
/*     */       }
/* 283 */       Object value = entry.getValue();
/* 284 */       if (value instanceof String) {
/* 285 */         result.put(key, value); continue;
/*     */       } 
/* 287 */       if (value instanceof Map) {
/*     */ 
/*     */         
/* 290 */         Map<String, Object> map = (Map<String, Object>)value;
/* 291 */         buildFlattenedMap(result, map, key); continue;
/*     */       } 
/* 293 */       if (value instanceof Collection) {
/*     */ 
/*     */         
/* 296 */         Collection<Object> collection = (Collection<Object>)value;
/* 297 */         int count = 0;
/* 298 */         for (Object object : collection) {
/* 299 */           buildFlattenedMap(result, 
/* 300 */               Collections.singletonMap("[" + count++ + "]", object), key);
/*     */         }
/*     */         continue;
/*     */       } 
/* 304 */       result.put(key, (value != null) ? value : "");
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
/*     */   public static interface MatchCallback
/*     */   {
/*     */     void process(Properties param1Properties, Map<String, Object> param1Map);
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
/*     */   public static interface DocumentMatcher
/*     */   {
/*     */     YamlProcessor.MatchStatus matches(Properties param1Properties);
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
/*     */   public enum MatchStatus
/*     */   {
/* 348 */     FOUND,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 353 */     NOT_FOUND,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 358 */     ABSTAIN;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static MatchStatus getMostSpecific(MatchStatus a, MatchStatus b) {
/* 364 */       return (a.ordinal() < b.ordinal()) ? a : b;
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
/*     */   public enum ResolutionMethod
/*     */   {
/* 377 */     OVERRIDE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     OVERRIDE_AND_IGNORE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 387 */     FIRST_FOUND;
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
/*     */   protected static class StrictMapAppenderConstructor
/*     */     extends Constructor
/*     */   {
/*     */     protected Map<Object, Object> constructMapping(MappingNode node) {
/*     */       try {
/* 404 */         return super.constructMapping(node);
/*     */       }
/* 406 */       catch (IllegalStateException ex) {
/* 407 */         throw new ParserException("while parsing MappingNode", node
/* 408 */             .getStartMark(), ex.getMessage(), node.getEndMark());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected Map<Object, Object> createDefaultMap() {
/* 414 */       final Map<Object, Object> delegate = super.createDefaultMap();
/* 415 */       return new AbstractMap<Object, Object>()
/*     */         {
/*     */           public Object put(Object key, Object value) {
/* 418 */             if (delegate.containsKey(key)) {
/* 419 */               throw new IllegalStateException("Duplicate key: " + key);
/*     */             }
/* 421 */             return delegate.put(key, value);
/*     */           }
/*     */           
/*     */           public Set<Map.Entry<Object, Object>> entrySet() {
/* 425 */             return delegate.entrySet();
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\YamlProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */