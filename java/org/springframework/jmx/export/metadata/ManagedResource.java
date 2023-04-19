/*     */ package org.springframework.jmx.export.metadata;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ManagedResource
/*     */   extends AbstractJmxAttribute
/*     */ {
/*     */   private String objectName;
/*     */   private boolean log = false;
/*     */   private String logFile;
/*     */   private String persistPolicy;
/*  40 */   private int persistPeriod = -1;
/*     */ 
/*     */   
/*     */   private String persistName;
/*     */ 
/*     */   
/*     */   private String persistLocation;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectName(String objectName) {
/*  51 */     this.objectName = objectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  58 */     return this.objectName;
/*     */   }
/*     */   
/*     */   public void setLog(boolean log) {
/*  62 */     this.log = log;
/*     */   }
/*     */   
/*     */   public boolean isLog() {
/*  66 */     return this.log;
/*     */   }
/*     */   
/*     */   public void setLogFile(String logFile) {
/*  70 */     this.logFile = logFile;
/*     */   }
/*     */   
/*     */   public String getLogFile() {
/*  74 */     return this.logFile;
/*     */   }
/*     */   
/*     */   public void setPersistPolicy(String persistPolicy) {
/*  78 */     this.persistPolicy = persistPolicy;
/*     */   }
/*     */   
/*     */   public String getPersistPolicy() {
/*  82 */     return this.persistPolicy;
/*     */   }
/*     */   
/*     */   public void setPersistPeriod(int persistPeriod) {
/*  86 */     this.persistPeriod = persistPeriod;
/*     */   }
/*     */   
/*     */   public int getPersistPeriod() {
/*  90 */     return this.persistPeriod;
/*     */   }
/*     */   
/*     */   public void setPersistName(String persistName) {
/*  94 */     this.persistName = persistName;
/*     */   }
/*     */   
/*     */   public String getPersistName() {
/*  98 */     return this.persistName;
/*     */   }
/*     */   
/*     */   public void setPersistLocation(String persistLocation) {
/* 102 */     this.persistLocation = persistLocation;
/*     */   }
/*     */   
/*     */   public String getPersistLocation() {
/* 106 */     return this.persistLocation;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\metadata\ManagedResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */