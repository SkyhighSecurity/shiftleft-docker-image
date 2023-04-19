/*     */ package org.springframework.aop.aspectj.autoproxy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aspectj.util.PartialOrder;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.aspectj.AbstractAspectJAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJProxyUtils;
/*     */ import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AspectJAwareAdvisorAutoProxyCreator
/*     */   extends AbstractAdvisorAutoProxyCreator
/*     */ {
/*  49 */   private static final Comparator<Advisor> DEFAULT_PRECEDENCE_COMPARATOR = new AspectJPrecedenceComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Advisor> sortAdvisors(List<Advisor> advisors) {
/*  71 */     List<PartiallyComparableAdvisorHolder> partiallyComparableAdvisors = new ArrayList<PartiallyComparableAdvisorHolder>(advisors.size());
/*  72 */     for (Advisor element : advisors) {
/*  73 */       partiallyComparableAdvisors.add(new PartiallyComparableAdvisorHolder(element, DEFAULT_PRECEDENCE_COMPARATOR));
/*     */     }
/*     */ 
/*     */     
/*  77 */     List<PartiallyComparableAdvisorHolder> sorted = PartialOrder.sort(partiallyComparableAdvisors);
/*  78 */     if (sorted != null) {
/*  79 */       List<Advisor> result = new ArrayList<Advisor>(advisors.size());
/*  80 */       for (PartiallyComparableAdvisorHolder pcAdvisor : sorted) {
/*  81 */         result.add(pcAdvisor.getAdvisor());
/*     */       }
/*  83 */       return result;
/*     */     } 
/*     */     
/*  86 */     return super.sortAdvisors(advisors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendAdvisors(List<Advisor> candidateAdvisors) {
/*  97 */     AspectJProxyUtils.makeAdvisorChainAspectJCapableIfNecessary(candidateAdvisors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldSkip(Class<?> beanClass, String beanName) {
/* 103 */     List<Advisor> candidateAdvisors = findCandidateAdvisors();
/* 104 */     for (Advisor advisor : candidateAdvisors) {
/* 105 */       if (advisor instanceof org.springframework.aop.aspectj.AspectJPointcutAdvisor && (
/* 106 */         (AbstractAspectJAdvice)advisor.getAdvice()).getAspectName().equals(beanName)) {
/* 107 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 111 */     return super.shouldSkip(beanClass, beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PartiallyComparableAdvisorHolder
/*     */     implements PartialOrder.PartialComparable
/*     */   {
/*     */     private final Advisor advisor;
/*     */     
/*     */     private final Comparator<Advisor> comparator;
/*     */ 
/*     */     
/*     */     public PartiallyComparableAdvisorHolder(Advisor advisor, Comparator<Advisor> comparator) {
/* 125 */       this.advisor = advisor;
/* 126 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Object obj) {
/* 131 */       Advisor otherAdvisor = ((PartiallyComparableAdvisorHolder)obj).advisor;
/* 132 */       return this.comparator.compare(this.advisor, otherAdvisor);
/*     */     }
/*     */ 
/*     */     
/*     */     public int fallbackCompareTo(Object obj) {
/* 137 */       return 0;
/*     */     }
/*     */     
/*     */     public Advisor getAdvisor() {
/* 141 */       return this.advisor;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 146 */       StringBuilder sb = new StringBuilder();
/* 147 */       Advice advice = this.advisor.getAdvice();
/* 148 */       sb.append(ClassUtils.getShortName(advice.getClass()));
/* 149 */       sb.append(": ");
/* 150 */       if (this.advisor instanceof Ordered) {
/* 151 */         sb.append("order ").append(((Ordered)this.advisor).getOrder()).append(", ");
/*     */       }
/* 153 */       if (advice instanceof AbstractAspectJAdvice) {
/* 154 */         AbstractAspectJAdvice ajAdvice = (AbstractAspectJAdvice)advice;
/* 155 */         sb.append(ajAdvice.getAspectName());
/* 156 */         sb.append(", declaration order ");
/* 157 */         sb.append(ajAdvice.getDeclarationOrder());
/*     */       } 
/* 159 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\aspectj\autoproxy\AspectJAwareAdvisorAutoProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */