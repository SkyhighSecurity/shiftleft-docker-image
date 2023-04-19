/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.core.task.TaskExecutor;
/*     */ import org.springframework.core.task.support.TaskExecutorAdapter;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
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
/*     */ public abstract class AsyncExecutionAspectSupport
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = "taskExecutor";
/*  74 */   private static final boolean completableFuturePresent = ClassUtils.isPresent("java.util.concurrent.CompletableFuture", AsyncExecutionInterceptor.class
/*  75 */       .getClassLoader());
/*     */ 
/*     */   
/*  78 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  80 */   private final Map<Method, AsyncTaskExecutor> executors = new ConcurrentHashMap<Method, AsyncTaskExecutor>(16);
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Executor defaultExecutor;
/*     */ 
/*     */ 
/*     */   
/*     */   private AsyncUncaughtExceptionHandler exceptionHandler;
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncExecutionAspectSupport(Executor defaultExecutor) {
/*  97 */     this(defaultExecutor, new SimpleAsyncUncaughtExceptionHandler());
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
/*     */   public AsyncExecutionAspectSupport(Executor defaultExecutor, AsyncUncaughtExceptionHandler exceptionHandler) {
/* 109 */     this.defaultExecutor = defaultExecutor;
/* 110 */     this.exceptionHandler = exceptionHandler;
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
/*     */   public void setExecutor(Executor defaultExecutor) {
/* 125 */     this.defaultExecutor = defaultExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionHandler(AsyncUncaughtExceptionHandler exceptionHandler) {
/* 133 */     this.exceptionHandler = exceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 144 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AsyncTaskExecutor determineAsyncExecutor(Method method) {
/* 154 */     AsyncTaskExecutor executor = this.executors.get(method);
/* 155 */     if (executor == null) {
/*     */       Executor targetExecutor;
/* 157 */       String qualifier = getExecutorQualifier(method);
/* 158 */       if (StringUtils.hasLength(qualifier)) {
/* 159 */         targetExecutor = findQualifiedExecutor(this.beanFactory, qualifier);
/*     */       } else {
/*     */         
/* 162 */         targetExecutor = this.defaultExecutor;
/* 163 */         if (targetExecutor == null) {
/* 164 */           synchronized (this.executors) {
/* 165 */             if (this.defaultExecutor == null) {
/* 166 */               this.defaultExecutor = getDefaultExecutor(this.beanFactory);
/*     */             }
/* 168 */             targetExecutor = this.defaultExecutor;
/*     */           } 
/*     */         }
/*     */       } 
/* 172 */       if (targetExecutor == null) {
/* 173 */         return null;
/*     */       }
/* 175 */       executor = (targetExecutor instanceof AsyncListenableTaskExecutor) ? (AsyncTaskExecutor)targetExecutor : (AsyncTaskExecutor)new TaskExecutorAdapter(targetExecutor);
/*     */       
/* 177 */       this.executors.put(method, executor);
/*     */     } 
/* 179 */     return executor;
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
/*     */   protected abstract String getExecutorQualifier(Method paramMethod);
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
/*     */   protected Executor findQualifiedExecutor(BeanFactory beanFactory, String qualifier) {
/* 203 */     if (beanFactory == null) {
/* 204 */       throw new IllegalStateException("BeanFactory must be set on " + getClass().getSimpleName() + " to access qualified executor '" + qualifier + "'");
/*     */     }
/*     */     
/* 207 */     return (Executor)BeanFactoryAnnotationUtils.qualifiedBeanOfType(beanFactory, Executor.class, qualifier);
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
/*     */   protected Executor getDefaultExecutor(BeanFactory beanFactory) {
/* 223 */     if (beanFactory != null) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 228 */         return (Executor)beanFactory.getBean(TaskExecutor.class);
/*     */       }
/* 230 */       catch (NoUniqueBeanDefinitionException ex) {
/* 231 */         this.logger.debug("Could not find unique TaskExecutor bean", (Throwable)ex);
/*     */         try {
/* 233 */           return (Executor)beanFactory.getBean("taskExecutor", Executor.class);
/*     */         }
/* 235 */         catch (NoSuchBeanDefinitionException ex2) {
/* 236 */           if (this.logger.isInfoEnabled()) {
/* 237 */             this.logger.info("More than one TaskExecutor bean found within the context, and none is named 'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly as an alias) in order to use it for async processing: " + ex
/*     */                 
/* 239 */                 .getBeanNamesFound());
/*     */           }
/*     */         }
/*     */       
/* 243 */       } catch (NoSuchBeanDefinitionException ex) {
/* 244 */         this.logger.debug("Could not find default TaskExecutor bean", (Throwable)ex);
/*     */         try {
/* 246 */           return (Executor)beanFactory.getBean("taskExecutor", Executor.class);
/*     */         }
/* 248 */         catch (NoSuchBeanDefinitionException ex2) {
/* 249 */           this.logger.info("No task executor bean found for async processing: no bean of type TaskExecutor and no bean named 'taskExecutor' either");
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 255 */     return null;
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
/*     */   protected Object doSubmit(Callable<Object> task, AsyncTaskExecutor executor, Class<?> returnType) {
/* 267 */     if (completableFuturePresent) {
/* 268 */       Future<Object> result = CompletableFutureDelegate.processCompletableFuture(returnType, task, (Executor)executor);
/* 269 */       if (result != null) {
/* 270 */         return result;
/*     */       }
/*     */     } 
/* 273 */     if (ListenableFuture.class.isAssignableFrom(returnType)) {
/* 274 */       return ((AsyncListenableTaskExecutor)executor).submitListenable(task);
/*     */     }
/* 276 */     if (Future.class.isAssignableFrom(returnType)) {
/* 277 */       return executor.submit(task);
/*     */     }
/*     */     
/* 280 */     executor.submit(task);
/* 281 */     return null;
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
/*     */   protected void handleError(Throwable ex, Method method, Object... params) throws Exception {
/* 298 */     if (Future.class.isAssignableFrom(method.getReturnType())) {
/* 299 */       ReflectionUtils.rethrowException(ex);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 304 */         this.exceptionHandler.handleUncaughtException(ex, method, params);
/*     */       }
/* 306 */       catch (Throwable ex2) {
/* 307 */         this.logger.error("Exception handler for async method '" + method.toGenericString() + "' threw unexpected exception itself", ex2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UsesJava8
/*     */   private static class CompletableFutureDelegate
/*     */   {
/*     */     public static <T> Future<T> processCompletableFuture(Class<?> returnType, final Callable<T> task, Executor executor) {
/* 321 */       if (!CompletableFuture.class.isAssignableFrom(returnType)) {
/* 322 */         return null;
/*     */       }
/* 324 */       return CompletableFuture.supplyAsync(new Supplier<T>()
/*     */           {
/*     */             public T get() {
/*     */               try {
/* 328 */                 return task.call();
/*     */               }
/* 330 */               catch (Throwable ex) {
/* 331 */                 throw new CompletionException(ex);
/*     */               } 
/*     */             }
/*     */           },  executor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\AsyncExecutionAspectSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */