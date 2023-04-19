/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SubTypeValidator
/*     */ {
/*     */   protected static final String PREFIX_SPRING = "org.springframework.";
/*     */   protected static final String PREFIX_C3P0 = "com.mchange.v2.c3p0.";
/*     */   protected static final Set<String> DEFAULT_NO_DESER_CLASS_NAMES;
/*     */   
/*     */   static {
/*  32 */     Set<String> s = new HashSet<>();
/*     */ 
/*     */     
/*  35 */     s.add("org.apache.commons.collections.functors.InvokerTransformer");
/*  36 */     s.add("org.apache.commons.collections.functors.InstantiateTransformer");
/*  37 */     s.add("org.apache.commons.collections4.functors.InvokerTransformer");
/*  38 */     s.add("org.apache.commons.collections4.functors.InstantiateTransformer");
/*  39 */     s.add("org.codehaus.groovy.runtime.ConvertedClosure");
/*  40 */     s.add("org.codehaus.groovy.runtime.MethodClosure");
/*  41 */     s.add("org.springframework.beans.factory.ObjectFactory");
/*  42 */     s.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
/*  43 */     s.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
/*     */     
/*  45 */     s.add("com.sun.rowset.JdbcRowSetImpl");
/*     */     
/*  47 */     s.add("java.util.logging.FileHandler");
/*  48 */     s.add("java.rmi.server.UnicastRemoteObject");
/*     */ 
/*     */     
/*  51 */     s.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     s.add("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
/*  57 */     s.add("com.sun.org.apache.bcel.internal.util.ClassLoader");
/*     */     
/*  59 */     s.add("org.hibernate.jmx.StatisticsService");
/*  60 */     s.add("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory");
/*     */     
/*  62 */     s.add("org.apache.ibatis.parsing.XPathParser");
/*     */ 
/*     */     
/*  65 */     s.add("jodd.db.connection.DataSourceConnectionProvider");
/*     */ 
/*     */     
/*  68 */     s.add("oracle.jdbc.connector.OracleManagedConnectionFactory");
/*  69 */     s.add("oracle.jdbc.rowset.OracleJDBCRowSet");
/*     */ 
/*     */     
/*  72 */     s.add("org.slf4j.ext.EventData");
/*  73 */     s.add("flex.messaging.util.concurrent.AsynchBeansWorkManagerExecutor");
/*  74 */     s.add("com.sun.deploy.security.ruleset.DRSHelper");
/*  75 */     s.add("org.apache.axis2.jaxws.spi.handler.HandlerResolverImpl");
/*     */ 
/*     */     
/*  78 */     s.add("org.jboss.util.propertyeditor.DocumentEditor");
/*  79 */     s.add("org.apache.openjpa.ee.RegistryManagedRuntime");
/*  80 */     s.add("org.apache.openjpa.ee.JNDIManagedRuntime");
/*  81 */     s.add("org.apache.axis2.transport.jms.JMSOutTransportInfo");
/*     */ 
/*     */     
/*  84 */     s.add("com.mysql.cj.jdbc.admin.MiniAdmin");
/*     */ 
/*     */     
/*  87 */     s.add("ch.qos.logback.core.db.DriverManagerConnectionSource");
/*     */ 
/*     */     
/*  90 */     s.add("org.jdom.transform.XSLTransformer");
/*  91 */     s.add("org.jdom2.transform.XSLTransformer");
/*     */ 
/*     */     
/*  94 */     s.add("net.sf.ehcache.transaction.manager.DefaultTransactionManagerLookup");
/*  95 */     s.add("net.sf.ehcache.hibernate.EhcacheJtaTransactionManagerLookup");
/*     */ 
/*     */     
/*  98 */     s.add("ch.qos.logback.core.db.JNDIConnectionSource");
/*     */ 
/*     */     
/* 101 */     s.add("com.zaxxer.hikari.HikariConfig");
/*     */     
/* 103 */     s.add("com.zaxxer.hikari.HikariDataSource");
/*     */ 
/*     */     
/* 106 */     s.add("org.apache.cxf.jaxrs.provider.XSLTJaxbProvider");
/*     */ 
/*     */     
/* 109 */     s.add("org.apache.commons.configuration.JNDIConfiguration");
/* 110 */     s.add("org.apache.commons.configuration2.JNDIConfiguration");
/*     */ 
/*     */     
/* 113 */     s.add("org.apache.xalan.lib.sql.JNDIConnectionPool");
/*     */     
/* 115 */     DEFAULT_NO_DESER_CLASS_NAMES = Collections.unmodifiableSet(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   protected Set<String> _cfgIllegalClassNames = DEFAULT_NO_DESER_CLASS_NAMES;
/*     */   
/* 123 */   private static final SubTypeValidator instance = new SubTypeValidator();
/*     */ 
/*     */   
/*     */   public static SubTypeValidator instance() {
/* 127 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateSubType(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/* 134 */     Class<?> raw = type.getRawClass();
/* 135 */     String full = raw.getName();
/*     */ 
/*     */ 
/*     */     
/* 139 */     if (!this._cfgIllegalClassNames.contains(full))
/*     */     
/*     */     { 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       if (!raw.isInterface())
/*     */       {
/* 148 */         if (full.startsWith("org.springframework."))
/* 149 */         { for (Class<?> cls = raw; cls != null && cls != Object.class; ) {
/* 150 */             String name = cls.getSimpleName();
/*     */             
/* 152 */             if (!"AbstractPointcutAdvisor".equals(name)) { if ("AbstractApplicationContext"
/*     */                 
/* 154 */                 .equals(name))
/*     */                 // Byte code: goto -> 134  cls = cls.getSuperclass(); }
/*     */              // Byte code: goto -> 134
/*     */           }  }
/* 158 */         else if (full.startsWith("com.mchange.v2.c3p0."))
/*     */         
/*     */         { 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 165 */           if (full.endsWith("DataSource"))
/*     */           
/*     */           { 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 172 */             ctxt.reportBadTypeDefinition(beanDesc, "Illegal type (%s) to deserialize: prevented for security reasons", new Object[] { full }); return; }  }  }  return; }  ctxt.reportBadTypeDefinition(beanDesc, "Illegal type (%s) to deserialize: prevented for security reasons", new Object[] { full });
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\SubTypeValidator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */