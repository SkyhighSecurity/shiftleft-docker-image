/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiTemplate
/*     */ {
/*  43 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   private Properties environment;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JndiTemplate() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JndiTemplate(Properties environment) {
/*  58 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Properties environment) {
/*  66 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties getEnvironment() {
/*  73 */     return this.environment;
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
/*     */   public <T> T execute(JndiCallback<T> contextCallback) throws NamingException {
/*  85 */     Context ctx = getContext();
/*     */     try {
/*  87 */       return contextCallback.doInContext(ctx);
/*     */     } finally {
/*     */       
/*  90 */       releaseContext(ctx);
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
/*     */   public Context getContext() throws NamingException {
/* 103 */     return createInitialContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseContext(Context ctx) {
/* 112 */     if (ctx != null) {
/*     */       try {
/* 114 */         ctx.close();
/*     */       }
/* 116 */       catch (NamingException ex) {
/* 117 */         this.logger.debug("Could not close JNDI InitialContext", ex);
/*     */       } 
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
/*     */   protected Context createInitialContext() throws NamingException {
/* 130 */     Hashtable<?, ?> icEnv = null;
/* 131 */     Properties env = getEnvironment();
/* 132 */     if (env != null) {
/* 133 */       icEnv = new Hashtable<Object, Object>(env.size());
/* 134 */       CollectionUtils.mergePropertiesIntoMap(env, icEnv);
/*     */     } 
/* 136 */     return new InitialContext(icEnv);
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
/*     */   public Object lookup(final String name) throws NamingException {
/* 149 */     if (this.logger.isDebugEnabled()) {
/* 150 */       this.logger.debug("Looking up JNDI object with name [" + name + "]");
/*     */     }
/* 152 */     return execute(new JndiCallback()
/*     */         {
/*     */           public Object doInContext(Context ctx) throws NamingException {
/* 155 */             Object located = ctx.lookup(name);
/* 156 */             if (located == null) {
/* 157 */               throw new NameNotFoundException("JNDI object with [" + name + "] not found: JNDI implementation returned null");
/*     */             }
/*     */             
/* 160 */             return located;
/*     */           }
/*     */         });
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
/*     */   public <T> T lookup(String name, Class<T> requiredType) throws NamingException {
/* 179 */     Object jndiObject = lookup(name);
/* 180 */     if (requiredType != null && !requiredType.isInstance(jndiObject)) {
/* 181 */       throw new TypeMismatchNamingException(name, requiredType, (jndiObject != null) ? jndiObject
/* 182 */           .getClass() : null);
/*     */     }
/* 184 */     return (T)jndiObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind(final String name, final Object object) throws NamingException {
/* 194 */     if (this.logger.isDebugEnabled()) {
/* 195 */       this.logger.debug("Binding JNDI object with name [" + name + "]");
/*     */     }
/* 197 */     execute(new JndiCallback()
/*     */         {
/*     */           public Object doInContext(Context ctx) throws NamingException {
/* 200 */             ctx.bind(name, object);
/* 201 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rebind(final String name, final Object object) throws NamingException {
/* 214 */     if (this.logger.isDebugEnabled()) {
/* 215 */       this.logger.debug("Rebinding JNDI object with name [" + name + "]");
/*     */     }
/* 217 */     execute(new JndiCallback()
/*     */         {
/*     */           public Object doInContext(Context ctx) throws NamingException {
/* 220 */             ctx.rebind(name, object);
/* 221 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unbind(final String name) throws NamingException {
/* 232 */     if (this.logger.isDebugEnabled()) {
/* 233 */       this.logger.debug("Unbinding JNDI object with name [" + name + "]");
/*     */     }
/* 235 */     execute(new JndiCallback()
/*     */         {
/*     */           public Object doInContext(Context ctx) throws NamingException {
/* 238 */             ctx.unbind(name);
/* 239 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jndi\JndiTemplate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */