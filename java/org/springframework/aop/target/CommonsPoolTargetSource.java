/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import org.apache.commons.pool.ObjectPool;
/*     */ import org.apache.commons.pool.PoolableObjectFactory;
/*     */ import org.apache.commons.pool.impl.GenericObjectPool;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.core.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class CommonsPoolTargetSource
/*     */   extends AbstractPoolingTargetSource
/*     */   implements PoolableObjectFactory
/*     */ {
/*  67 */   private static final Constants constants = new Constants(GenericObjectPool.class);
/*     */ 
/*     */   
/*  70 */   private int maxIdle = 8;
/*     */   
/*  72 */   private int minIdle = 0;
/*     */   
/*  74 */   private long maxWait = -1L;
/*     */   
/*  76 */   private long timeBetweenEvictionRunsMillis = -1L;
/*     */   
/*  78 */   private long minEvictableIdleTimeMillis = 1800000L;
/*     */   
/*  80 */   private byte whenExhaustedAction = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObjectPool pool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommonsPoolTargetSource() {
/*  95 */     setMaxSize(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxIdle(int maxIdle) {
/* 104 */     this.maxIdle = maxIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxIdle() {
/* 111 */     return this.maxIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinIdle(int minIdle) {
/* 120 */     this.minIdle = minIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinIdle() {
/* 127 */     return this.minIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxWait(long maxWait) {
/* 136 */     this.maxWait = maxWait;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxWait() {
/* 143 */     return this.maxWait;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
/* 153 */     this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTimeBetweenEvictionRunsMillis() {
/* 160 */     return this.timeBetweenEvictionRunsMillis;
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
/*     */   public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
/* 172 */     this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMinEvictableIdleTimeMillis() {
/* 179 */     return this.minEvictableIdleTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWhenExhaustedActionName(String whenExhaustedActionName) {
/* 189 */     setWhenExhaustedAction(constants.asNumber(whenExhaustedActionName).byteValue());
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
/*     */   public void setWhenExhaustedAction(byte whenExhaustedAction) {
/* 201 */     this.whenExhaustedAction = whenExhaustedAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getWhenExhaustedAction() {
/* 208 */     return this.whenExhaustedAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void createPool() {
/* 218 */     this.logger.debug("Creating Commons object pool");
/* 219 */     this.pool = createObjectPool();
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
/*     */   protected ObjectPool createObjectPool() {
/* 231 */     GenericObjectPool gop = new GenericObjectPool(this);
/* 232 */     gop.setMaxActive(getMaxSize());
/* 233 */     gop.setMaxIdle(getMaxIdle());
/* 234 */     gop.setMinIdle(getMinIdle());
/* 235 */     gop.setMaxWait(getMaxWait());
/* 236 */     gop.setTimeBetweenEvictionRunsMillis(getTimeBetweenEvictionRunsMillis());
/* 237 */     gop.setMinEvictableIdleTimeMillis(getMinEvictableIdleTimeMillis());
/* 238 */     gop.setWhenExhaustedAction(getWhenExhaustedAction());
/* 239 */     return (ObjectPool)gop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTarget() throws Exception {
/* 248 */     return this.pool.borrowObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseTarget(Object target) throws Exception {
/* 256 */     this.pool.returnObject(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getActiveCount() throws UnsupportedOperationException {
/* 261 */     return this.pool.getNumActive();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIdleCount() throws UnsupportedOperationException {
/* 266 */     return this.pool.getNumIdle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/* 275 */     this.logger.debug("Closing Commons ObjectPool");
/* 276 */     this.pool.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object makeObject() throws BeansException {
/* 286 */     return newPrototypeInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroyObject(Object obj) throws Exception {
/* 291 */     destroyPrototypeInstance(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean validateObject(Object obj) {
/* 296 */     return true;
/*     */   }
/*     */   
/*     */   public void activateObject(Object obj) {}
/*     */   
/*     */   public void passivateObject(Object obj) {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\target\CommonsPoolTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */