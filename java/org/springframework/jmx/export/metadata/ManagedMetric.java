/*     */ package org.springframework.jmx.export.metadata;
/*     */ 
/*     */ import org.springframework.jmx.support.MetricType;
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
/*     */ public class ManagedMetric
/*     */   extends AbstractJmxAttribute
/*     */ {
/*  33 */   private String category = "";
/*     */   
/*  35 */   private String displayName = "";
/*     */   
/*  37 */   private MetricType metricType = MetricType.GAUGE;
/*     */   
/*  39 */   private int persistPeriod = -1;
/*     */   
/*  41 */   private String persistPolicy = "";
/*     */   
/*  43 */   private String unit = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCategory(String category) {
/*  50 */     this.category = category;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCategory() {
/*  57 */     return this.category;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisplayName(String displayName) {
/*  64 */     this.displayName = displayName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDisplayName() {
/*  71 */     return this.displayName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricType(MetricType metricType) {
/*  78 */     Assert.notNull(metricType, "MetricType must not be null");
/*  79 */     this.metricType = metricType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetricType getMetricType() {
/*  86 */     return this.metricType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPersistPeriod(int persistPeriod) {
/*  93 */     this.persistPeriod = persistPeriod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPersistPeriod() {
/* 100 */     return this.persistPeriod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPersistPolicy(String persistPolicy) {
/* 107 */     this.persistPolicy = persistPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPersistPolicy() {
/* 114 */     return this.persistPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnit(String unit) {
/* 121 */     this.unit = unit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUnit() {
/* 128 */     return this.unit;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\metadata\ManagedMetric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */