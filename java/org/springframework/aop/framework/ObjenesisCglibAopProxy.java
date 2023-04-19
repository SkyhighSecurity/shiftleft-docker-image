/*    */ package org.springframework.aop.framework;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.cglib.proxy.Callback;
/*    */ import org.springframework.cglib.proxy.Enhancer;
/*    */ import org.springframework.cglib.proxy.Factory;
/*    */ import org.springframework.objenesis.SpringObjenesis;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ObjenesisCglibAopProxy
/*    */   extends CglibAopProxy
/*    */ {
/* 38 */   private static final Log logger = LogFactory.getLog(ObjenesisCglibAopProxy.class);
/*    */   
/* 40 */   private static final SpringObjenesis objenesis = new SpringObjenesis();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjenesisCglibAopProxy(AdvisedSupport config) {
/* 48 */     super(config);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
/* 55 */     Class<?> proxyClass = enhancer.createClass();
/* 56 */     Object proxyInstance = null;
/*    */     
/* 58 */     if (objenesis.isWorthTrying()) {
/*    */       try {
/* 60 */         proxyInstance = objenesis.newInstance(proxyClass, enhancer.getUseCache());
/*    */       }
/* 62 */       catch (Throwable ex) {
/* 63 */         logger.debug("Unable to instantiate proxy using Objenesis, falling back to regular proxy construction", ex);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 68 */     if (proxyInstance == null) {
/*    */       
/*    */       try {
/*    */ 
/*    */         
/* 73 */         proxyInstance = (this.constructorArgs != null) ? proxyClass.getConstructor(this.constructorArgTypes).newInstance(this.constructorArgs) : proxyClass.newInstance();
/*    */       }
/* 75 */       catch (Throwable ex) {
/* 76 */         throw new AopConfigException("Unable to instantiate proxy using Objenesis, and regular proxy instantiation via default constructor fails as well", ex);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 81 */     ((Factory)proxyInstance).setCallbacks(callbacks);
/* 82 */     return proxyInstance;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\framework\ObjenesisCglibAopProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */