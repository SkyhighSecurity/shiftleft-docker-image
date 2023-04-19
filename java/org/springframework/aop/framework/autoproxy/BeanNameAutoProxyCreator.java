/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PatternMatchUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanNameAutoProxyCreator
/*     */   extends AbstractAutoProxyCreator
/*     */ {
/*     */   private List<String> beanNames;
/*     */   
/*     */   public void setBeanNames(String... beanNames) {
/*  64 */     Assert.notEmpty((Object[])beanNames, "'beanNames' must not be empty");
/*  65 */     this.beanNames = new ArrayList<String>(beanNames.length);
/*  66 */     for (String mappedName : beanNames) {
/*  67 */       this.beanNames.add(StringUtils.trimWhitespace(mappedName));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource) {
/*  77 */     if (this.beanNames != null) {
/*  78 */       for (String mappedName : this.beanNames) {
/*  79 */         if (FactoryBean.class.isAssignableFrom(beanClass)) {
/*  80 */           if (!mappedName.startsWith("&")) {
/*     */             continue;
/*     */           }
/*  83 */           mappedName = mappedName.substring("&".length());
/*     */         } 
/*  85 */         if (isMatch(beanName, mappedName)) {
/*  86 */           return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
/*     */         }
/*  88 */         BeanFactory beanFactory = getBeanFactory();
/*  89 */         if (beanFactory != null) {
/*  90 */           String[] aliases = beanFactory.getAliases(beanName);
/*  91 */           for (String alias : aliases) {
/*  92 */             if (isMatch(alias, mappedName)) {
/*  93 */               return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*  99 */     return DO_NOT_PROXY;
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
/*     */   protected boolean isMatch(String beanName, String mappedName) {
/* 112 */     return PatternMatchUtils.simpleMatch(mappedName, beanName);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\autoproxy\BeanNameAutoProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */