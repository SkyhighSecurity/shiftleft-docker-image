/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BeanFactoryUtils
/*     */ {
/*     */   public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";
/*     */   
/*     */   public static boolean isFactoryDereference(String name) {
/*  61 */     return (name != null && name.startsWith("&"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String transformedBeanName(String name) {
/*  72 */     Assert.notNull(name, "'name' must not be null");
/*  73 */     String beanName = name;
/*  74 */     while (beanName.startsWith("&")) {
/*  75 */       beanName = beanName.substring("&".length());
/*     */     }
/*  77 */     return beanName;
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
/*     */   public static boolean isGeneratedBeanName(String name) {
/*  90 */     return (name != null && name.contains("#"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String originalBeanName(String name) {
/* 101 */     Assert.notNull(name, "'name' must not be null");
/* 102 */     int separatorIndex = name.indexOf("#");
/* 103 */     return (separatorIndex != -1) ? name.substring(0, separatorIndex) : name;
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
/*     */   public static int countBeansIncludingAncestors(ListableBeanFactory lbf) {
/* 116 */     return (beanNamesIncludingAncestors(lbf)).length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] beanNamesIncludingAncestors(ListableBeanFactory lbf) {
/* 126 */     return beanNamesForTypeIncludingAncestors(lbf, Object.class);
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
/*     */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, ResolvableType type) {
/* 143 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 144 */     String[] result = lbf.getBeanNamesForType(type);
/* 145 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 146 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 147 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 148 */         String[] parentResult = beanNamesForTypeIncludingAncestors((ListableBeanFactory)hbf
/* 149 */             .getParentBeanFactory(), type);
/* 150 */         result = mergeNamesWithParent(result, parentResult, hbf);
/*     */       } 
/*     */     } 
/* 153 */     return result;
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
/*     */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type) {
/* 169 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 170 */     String[] result = lbf.getBeanNamesForType(type);
/* 171 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 172 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 173 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 174 */         String[] parentResult = beanNamesForTypeIncludingAncestors((ListableBeanFactory)hbf
/* 175 */             .getParentBeanFactory(), type);
/* 176 */         result = mergeNamesWithParent(result, parentResult, hbf);
/*     */       } 
/*     */     } 
/* 179 */     return result;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
/* 205 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 206 */     String[] result = lbf.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/* 207 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 208 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 209 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 210 */         String[] parentResult = beanNamesForTypeIncludingAncestors((ListableBeanFactory)hbf
/* 211 */             .getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
/* 212 */         result = mergeNamesWithParent(result, parentResult, hbf);
/*     */       } 
/*     */     } 
/* 215 */     return result;
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
/*     */   public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type) throws BeansException {
/* 238 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 239 */     Map<String, T> result = new LinkedHashMap<String, T>(4);
/* 240 */     result.putAll(lbf.getBeansOfType(type));
/* 241 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 242 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 243 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 244 */         Map<String, T> parentResult = beansOfTypeIncludingAncestors((ListableBeanFactory)hbf
/* 245 */             .getParentBeanFactory(), type);
/* 246 */         for (Map.Entry<String, T> entry : parentResult.entrySet()) {
/* 247 */           String beanName = entry.getKey();
/* 248 */           if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
/* 249 */             result.put(beanName, entry.getValue());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 254 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 287 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 288 */     Map<String, T> result = new LinkedHashMap<String, T>(4);
/* 289 */     result.putAll(lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit));
/* 290 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 291 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 292 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 293 */         Map<String, T> parentResult = beansOfTypeIncludingAncestors((ListableBeanFactory)hbf
/* 294 */             .getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
/* 295 */         for (Map.Entry<String, T> entry : parentResult.entrySet()) {
/* 296 */           String beanName = entry.getKey();
/* 297 */           if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
/* 298 */             result.put(beanName, entry.getValue());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 303 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type) throws BeansException {
/* 332 */     Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type);
/* 333 */     return uniqueBean(type, beansOfType);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 369 */     Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type, includeNonSingletons, allowEagerInit);
/* 370 */     return uniqueBean(type, beansOfType);
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
/*     */   public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type) throws BeansException {
/* 390 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 391 */     Map<String, T> beansOfType = lbf.getBeansOfType(type);
/* 392 */     return uniqueBean(type, beansOfType);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 423 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 424 */     Map<String, T> beansOfType = lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit);
/* 425 */     return uniqueBean(type, beansOfType);
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
/*     */   private static String[] mergeNamesWithParent(String[] result, String[] parentResult, HierarchicalBeanFactory hbf) {
/* 438 */     if (parentResult.length == 0) {
/* 439 */       return result;
/*     */     }
/* 441 */     List<String> merged = new ArrayList<String>(result.length + parentResult.length);
/* 442 */     merged.addAll(Arrays.asList(result));
/* 443 */     for (String beanName : parentResult) {
/* 444 */       if (!merged.contains(beanName) && !hbf.containsLocalBean(beanName)) {
/* 445 */         merged.add(beanName);
/*     */       }
/*     */     } 
/* 448 */     return StringUtils.toStringArray(merged);
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
/*     */   private static <T> T uniqueBean(Class<T> type, Map<String, T> matchingBeans) {
/* 460 */     int count = matchingBeans.size();
/* 461 */     if (count == 1) {
/* 462 */       return matchingBeans.values().iterator().next();
/*     */     }
/* 464 */     if (count > 1) {
/* 465 */       throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
/*     */     }
/*     */     
/* 468 */     throw new NoSuchBeanDefinitionException(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\BeanFactoryUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */