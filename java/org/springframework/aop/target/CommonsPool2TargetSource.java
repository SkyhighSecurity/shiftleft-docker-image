/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import org.apache.commons.pool2.ObjectPool;
/*     */ import org.apache.commons.pool2.PooledObject;
/*     */ import org.apache.commons.pool2.PooledObjectFactory;
/*     */ import org.apache.commons.pool2.impl.DefaultPooledObject;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPool;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonsPool2TargetSource
/*     */   extends AbstractPoolingTargetSource
/*     */   implements PooledObjectFactory<Object>
/*     */ {
/*  66 */   private int maxIdle = 8;
/*     */   
/*  68 */   private int minIdle = 0;
/*     */   
/*  70 */   private long maxWait = -1L;
/*     */   
/*  72 */   private long timeBetweenEvictionRunsMillis = -1L;
/*     */   
/*  74 */   private long minEvictableIdleTimeMillis = 1800000L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean blockWhenExhausted = true;
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
/*     */   public CommonsPool2TargetSource() {
/*  91 */     setMaxSize(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxIdle(int maxIdle) {
/* 101 */     this.maxIdle = maxIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxIdle() {
/* 108 */     return this.maxIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinIdle(int minIdle) {
/* 117 */     this.minIdle = minIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinIdle() {
/* 124 */     return this.minIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxWait(long maxWait) {
/* 133 */     this.maxWait = maxWait;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxWait() {
/* 140 */     return this.maxWait;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
/* 150 */     this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTimeBetweenEvictionRunsMillis() {
/* 157 */     return this.timeBetweenEvictionRunsMillis;
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
/* 169 */     this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMinEvictableIdleTimeMillis() {
/* 176 */     return this.minEvictableIdleTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockWhenExhausted(boolean blockWhenExhausted) {
/* 183 */     this.blockWhenExhausted = blockWhenExhausted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlockWhenExhausted() {
/* 190 */     return this.blockWhenExhausted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void createPool() {
/* 200 */     this.logger.debug("Creating Commons object pool");
/* 201 */     this.pool = createObjectPool();
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
/* 213 */     GenericObjectPoolConfig config = new GenericObjectPoolConfig();
/* 214 */     config.setMaxTotal(getMaxSize());
/* 215 */     config.setMaxIdle(getMaxIdle());
/* 216 */     config.setMinIdle(getMinIdle());
/* 217 */     config.setMaxWaitMillis(getMaxWait());
/* 218 */     config.setTimeBetweenEvictionRunsMillis(getTimeBetweenEvictionRunsMillis());
/* 219 */     config.setMinEvictableIdleTimeMillis(getMinEvictableIdleTimeMillis());
/* 220 */     config.setBlockWhenExhausted(isBlockWhenExhausted());
/* 221 */     return (ObjectPool)new GenericObjectPool(this, config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTarget() throws Exception {
/* 230 */     return this.pool.borrowObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseTarget(Object target) throws Exception {
/* 238 */     this.pool.returnObject(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getActiveCount() throws UnsupportedOperationException {
/* 243 */     return this.pool.getNumActive();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIdleCount() throws UnsupportedOperationException {
/* 248 */     return this.pool.getNumIdle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/* 257 */     this.logger.debug("Closing Commons ObjectPool");
/* 258 */     this.pool.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PooledObject<Object> makeObject() throws Exception {
/* 268 */     return (PooledObject<Object>)new DefaultPooledObject(newPrototypeInstance());
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroyObject(PooledObject<Object> p) throws Exception {
/* 273 */     destroyPrototypeInstance(p.getObject());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean validateObject(PooledObject<Object> p) {
/* 278 */     return true;
/*     */   }
/*     */   
/*     */   public void activateObject(PooledObject<Object> p) throws Exception {}
/*     */   
/*     */   public void passivateObject(PooledObject<Object> p) throws Exception {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\target\CommonsPool2TargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */