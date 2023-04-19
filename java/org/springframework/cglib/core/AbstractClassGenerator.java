/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.cglib.core.internal.Function;
/*     */ import org.springframework.cglib.core.internal.LoadingCache;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractClassGenerator<T>
/*     */   implements ClassGenerator
/*     */ {
/*  38 */   private static final ThreadLocal CURRENT = new ThreadLocal();
/*     */   
/*  40 */   private static volatile Map<ClassLoader, ClassLoaderData> CACHE = new WeakHashMap<ClassLoader, ClassLoaderData>();
/*     */   
/*  42 */   private GeneratorStrategy strategy = DefaultGeneratorStrategy.INSTANCE;
/*  43 */   private NamingPolicy namingPolicy = DefaultNamingPolicy.INSTANCE;
/*     */   private Source source;
/*     */   private ClassLoader classLoader;
/*     */   private String namePrefix;
/*     */   private Object key;
/*     */   private boolean useCache = true;
/*     */   private String className;
/*     */   private boolean attemptLoad;
/*     */   
/*     */   protected static class ClassLoaderData {
/*  53 */     private final Set<String> reservedClassNames = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final LoadingCache<AbstractClassGenerator, Object, Object> generatedClasses;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final WeakReference<ClassLoader> classLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     private final Predicate uniqueNamePredicate = new Predicate() {
/*     */         public boolean evaluate(Object name) {
/*  75 */           return AbstractClassGenerator.ClassLoaderData.this.reservedClassNames.contains(name);
/*     */         }
/*     */       };
/*     */     
/*  79 */     private static final Function<AbstractClassGenerator, Object> GET_KEY = new Function<AbstractClassGenerator, Object>() {
/*     */         public Object apply(AbstractClassGenerator gen) {
/*  81 */           return gen.key;
/*     */         }
/*     */       };
/*     */     
/*     */     public ClassLoaderData(ClassLoader classLoader) {
/*  86 */       if (classLoader == null) {
/*  87 */         throw new IllegalArgumentException("classLoader == null is not yet supported");
/*     */       }
/*  89 */       this.classLoader = new WeakReference<ClassLoader>(classLoader);
/*  90 */       Function<AbstractClassGenerator, Object> load = new Function<AbstractClassGenerator, Object>()
/*     */         {
/*     */           public Object apply(AbstractClassGenerator gen) {
/*  93 */             Class klass = gen.generate(AbstractClassGenerator.ClassLoaderData.this);
/*  94 */             return gen.wrapCachedClass(klass);
/*     */           }
/*     */         };
/*  97 */       this.generatedClasses = new LoadingCache(GET_KEY, load);
/*     */     }
/*     */     
/*     */     public ClassLoader getClassLoader() {
/* 101 */       return this.classLoader.get();
/*     */     }
/*     */     
/*     */     public void reserveName(String name) {
/* 105 */       this.reservedClassNames.add(name);
/*     */     }
/*     */     
/*     */     public Predicate getUniqueNamePredicate() {
/* 109 */       return this.uniqueNamePredicate;
/*     */     }
/*     */     
/*     */     public Object get(AbstractClassGenerator<Object> gen, boolean useCache) {
/* 113 */       if (!useCache) {
/* 114 */         return gen.generate(this);
/*     */       }
/* 116 */       Object cachedValue = this.generatedClasses.get(gen);
/* 117 */       return gen.unwrapCachedValue(cachedValue);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected T wrapCachedClass(Class<?> klass) {
/* 123 */     return (T)new WeakReference<Class<?>>(klass);
/*     */   }
/*     */   
/*     */   protected Object unwrapCachedValue(T cached) {
/* 127 */     return ((WeakReference)cached).get();
/*     */   }
/*     */   
/*     */   protected static class Source
/*     */   {
/*     */     public Source(String name) {
/* 133 */       this.name = name;
/*     */     }
/*     */     String name; }
/*     */   
/*     */   protected AbstractClassGenerator(Source source) {
/* 138 */     this.source = source;
/*     */   }
/*     */   
/*     */   protected void setNamePrefix(String namePrefix) {
/* 142 */     this.namePrefix = namePrefix;
/*     */   }
/*     */   
/*     */   protected final String getClassName() {
/* 146 */     return this.className;
/*     */   }
/*     */   
/*     */   private void setClassName(String className) {
/* 150 */     this.className = className;
/*     */   }
/*     */   
/*     */   private String generateClassName(Predicate nameTestPredicate) {
/* 154 */     return this.namingPolicy.getClassName(this.namePrefix, this.source.name, this.key, nameTestPredicate);
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
/*     */   public void setClassLoader(ClassLoader classLoader) {
/* 167 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamingPolicy(NamingPolicy namingPolicy) {
/* 176 */     if (namingPolicy == null)
/* 177 */       namingPolicy = DefaultNamingPolicy.INSTANCE; 
/* 178 */     this.namingPolicy = namingPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NamingPolicy getNamingPolicy() {
/* 185 */     return this.namingPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseCache(boolean useCache) {
/* 193 */     this.useCache = useCache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseCache() {
/* 200 */     return this.useCache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttemptLoad(boolean attemptLoad) {
/* 209 */     this.attemptLoad = attemptLoad;
/*     */   }
/*     */   
/*     */   public boolean getAttemptLoad() {
/* 213 */     return this.attemptLoad;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStrategy(GeneratorStrategy strategy) {
/* 221 */     if (strategy == null)
/* 222 */       strategy = DefaultGeneratorStrategy.INSTANCE; 
/* 223 */     this.strategy = strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneratorStrategy getStrategy() {
/* 230 */     return this.strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AbstractClassGenerator getCurrent() {
/* 238 */     return CURRENT.get();
/*     */   }
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 242 */     ClassLoader t = this.classLoader;
/* 243 */     if (t == null) {
/* 244 */       t = getDefaultClassLoader();
/*     */     }
/* 246 */     if (t == null) {
/* 247 */       t = getClass().getClassLoader();
/*     */     }
/* 249 */     if (t == null) {
/* 250 */       t = Thread.currentThread().getContextClassLoader();
/*     */     }
/* 252 */     if (t == null) {
/* 253 */       throw new IllegalStateException("Cannot determine classloader");
/*     */     }
/* 255 */     return t;
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
/*     */   protected ProtectionDomain getProtectionDomain() {
/* 270 */     return null;
/*     */   }
/*     */   
/*     */   protected Object create(Object key) {
/*     */     try {
/* 275 */       ClassLoader loader = getClassLoader();
/* 276 */       Map<ClassLoader, ClassLoaderData> cache = CACHE;
/* 277 */       ClassLoaderData data = cache.get(loader);
/* 278 */       if (data == null) {
/* 279 */         synchronized (AbstractClassGenerator.class) {
/* 280 */           cache = CACHE;
/* 281 */           data = cache.get(loader);
/* 282 */           if (data == null) {
/* 283 */             Map<ClassLoader, ClassLoaderData> newCache = new WeakHashMap<ClassLoader, ClassLoaderData>(cache);
/* 284 */             data = new ClassLoaderData(loader);
/* 285 */             newCache.put(loader, data);
/* 286 */             CACHE = newCache;
/*     */           } 
/*     */         } 
/*     */       }
/* 290 */       this.key = key;
/* 291 */       Object obj = data.get(this, getUseCache());
/* 292 */       if (obj instanceof Class) {
/* 293 */         return firstInstance((Class)obj);
/*     */       }
/* 295 */       return nextInstance(obj);
/* 296 */     } catch (RuntimeException e) {
/* 297 */       throw e;
/* 298 */     } catch (Error e) {
/* 299 */       throw e;
/* 300 */     } catch (Exception e) {
/* 301 */       throw new CodeGenerationException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class generate(ClassLoaderData data) {
/* 307 */     Object save = CURRENT.get();
/* 308 */     CURRENT.set(this); try {
/*     */       Class gen;
/* 310 */       ClassLoader classLoader = data.getClassLoader();
/* 311 */       if (classLoader == null) {
/* 312 */         throw new IllegalStateException("ClassLoader is null while trying to define class " + 
/* 313 */             getClassName() + ". It seems that the loader has been expired from a weak reference somehow. Please file an issue at cglib's issue tracker.");
/*     */       }
/*     */       
/* 316 */       synchronized (classLoader) {
/* 317 */         String name = generateClassName(data.getUniqueNamePredicate());
/* 318 */         data.reserveName(name);
/* 319 */         setClassName(name);
/*     */       } 
/* 321 */       if (this.attemptLoad) {
/*     */         try {
/* 323 */           gen = classLoader.loadClass(getClassName());
/* 324 */           return gen;
/* 325 */         } catch (ClassNotFoundException classNotFoundException) {}
/*     */       }
/*     */ 
/*     */       
/* 329 */       byte[] b = this.strategy.generate(this);
/* 330 */       String className = ClassNameReader.getClassName(new ClassReader(b));
/* 331 */       ProtectionDomain protectionDomain = getProtectionDomain();
/* 332 */       synchronized (classLoader) {
/* 333 */         if (protectionDomain == null) {
/* 334 */           gen = ReflectUtils.defineClass(className, b, classLoader);
/*     */         } else {
/* 336 */           gen = ReflectUtils.defineClass(className, b, classLoader, protectionDomain);
/*     */         } 
/*     */       } 
/* 339 */       return gen;
/* 340 */     } catch (RuntimeException e) {
/* 341 */       throw e;
/* 342 */     } catch (Error e) {
/* 343 */       throw e;
/* 344 */     } catch (Exception e) {
/* 345 */       throw new CodeGenerationException(e);
/*     */     } finally {
/* 347 */       CURRENT.set(save);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract ClassLoader getDefaultClassLoader();
/*     */   
/*     */   protected abstract Object firstInstance(Class paramClass) throws Exception;
/*     */   
/*     */   protected abstract Object nextInstance(Object paramObject) throws Exception;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\core\AbstractClassGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */