/*     */ package org.springframework.beans.factory.groovy;
/*     */ 
/*     */ import groovy.lang.Binding;
/*     */ import groovy.lang.Closure;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.GroovyObjectSupport;
/*     */ import groovy.lang.GroovyShell;
/*     */ import groovy.lang.GroovySystem;
/*     */ import groovy.lang.MetaClass;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.codehaus.groovy.runtime.DefaultGroovyMethods;
/*     */ import org.codehaus.groovy.runtime.InvokerHelper;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
/*     */ import org.springframework.beans.factory.parsing.Location;
/*     */ import org.springframework.beans.factory.parsing.Problem;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.ManagedMap;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*     */ import org.springframework.beans.factory.xml.NamespaceHandler;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.core.io.DescriptiveResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GroovyBeanDefinitionReader
/*     */   extends AbstractBeanDefinitionReader
/*     */   implements GroovyObject
/*     */ {
/*     */   private final XmlBeanDefinitionReader standardXmlBeanDefinitionReader;
/*     */   private final XmlBeanDefinitionReader groovyDslXmlBeanDefinitionReader;
/* 146 */   private final Map<String, String> namespaces = new HashMap<String, String>();
/*     */   
/* 148 */   private final Map<String, DeferredProperty> deferredProperties = new HashMap<String, DeferredProperty>();
/*     */   
/* 150 */   private MetaClass metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   private Binding binding;
/*     */ 
/*     */ 
/*     */   
/*     */   private GroovyBeanDefinitionWrapper currentBeanDefinition;
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovyBeanDefinitionReader(BeanDefinitionRegistry registry) {
/* 163 */     super(registry);
/* 164 */     this.standardXmlBeanDefinitionReader = new XmlBeanDefinitionReader(registry);
/* 165 */     this.groovyDslXmlBeanDefinitionReader = new XmlBeanDefinitionReader(registry);
/* 166 */     this.groovyDslXmlBeanDefinitionReader.setValidating(false);
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
/*     */   public GroovyBeanDefinitionReader(XmlBeanDefinitionReader xmlBeanDefinitionReader) {
/* 179 */     super(xmlBeanDefinitionReader.getRegistry());
/* 180 */     this.standardXmlBeanDefinitionReader = new XmlBeanDefinitionReader(xmlBeanDefinitionReader.getRegistry());
/* 181 */     this.groovyDslXmlBeanDefinitionReader = xmlBeanDefinitionReader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMetaClass(MetaClass metaClass) {
/* 186 */     this.metaClass = metaClass;
/*     */   }
/*     */   
/*     */   public MetaClass getMetaClass() {
/* 190 */     return this.metaClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinding(Binding binding) {
/* 198 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Binding getBinding() {
/* 205 */     return this.binding;
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
/*     */   public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
/* 220 */     return loadBeanDefinitions(new EncodedResource(resource));
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
/*     */   public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
/* 234 */     String filename = encodedResource.getResource().getFilename();
/* 235 */     if (StringUtils.endsWithIgnoreCase(filename, ".xml")) {
/* 236 */       return this.standardXmlBeanDefinitionReader.loadBeanDefinitions(encodedResource);
/*     */     }
/*     */     
/* 239 */     Closure beans = new Closure(this)
/*     */       {
/*     */         public Object call(Object[] args) {
/* 242 */           GroovyBeanDefinitionReader.this.invokeBeanDefiningClosure((Closure)args[0]);
/* 243 */           return null;
/*     */         }
/*     */       };
/* 246 */     Binding binding = new Binding()
/*     */       {
/*     */         public void setVariable(String name, Object value) {
/* 249 */           if (GroovyBeanDefinitionReader.this.currentBeanDefinition != null) {
/* 250 */             GroovyBeanDefinitionReader.this.applyPropertyToBeanDefinition(name, value);
/*     */           } else {
/*     */             
/* 253 */             super.setVariable(name, value);
/*     */           } 
/*     */         }
/*     */       };
/* 257 */     binding.setVariable("beans", beans);
/*     */     
/* 259 */     int countBefore = getRegistry().getBeanDefinitionCount();
/*     */     try {
/* 261 */       GroovyShell shell = new GroovyShell(getResourceLoader().getClassLoader(), binding);
/* 262 */       shell.evaluate(encodedResource.getReader(), "beans");
/*     */     }
/* 264 */     catch (Throwable ex) {
/* 265 */       throw new BeanDefinitionParsingException(new Problem("Error evaluating Groovy script: " + ex.getMessage(), new Location(encodedResource
/* 266 */               .getResource()), null, ex));
/*     */     } 
/* 268 */     return getRegistry().getBeanDefinitionCount() - countBefore;
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
/*     */   public GroovyBeanDefinitionReader beans(Closure closure) {
/* 280 */     return invokeBeanDefiningClosure(closure);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericBeanDefinition bean(Class<?> type) {
/* 289 */     GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
/* 290 */     beanDefinition.setBeanClass(type);
/* 291 */     return beanDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBeanDefinition bean(Class<?> type, Object... args) {
/* 301 */     GroovyBeanDefinitionWrapper current = this.currentBeanDefinition;
/*     */     try {
/* 303 */       Closure callable = null;
/* 304 */       Collection<Object> constructorArgs = null;
/* 305 */       if (!ObjectUtils.isEmpty(args)) {
/* 306 */         int index = args.length;
/* 307 */         Object lastArg = args[index - 1];
/* 308 */         if (lastArg instanceof Closure) {
/* 309 */           callable = (Closure)lastArg;
/* 310 */           index--;
/*     */         } 
/* 312 */         if (index > -1) {
/* 313 */           constructorArgs = resolveConstructorArguments(args, 0, index);
/*     */         }
/*     */       } 
/* 316 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(null, type, constructorArgs);
/* 317 */       if (callable != null) {
/* 318 */         callable.call(this.currentBeanDefinition);
/*     */       }
/* 320 */       return this.currentBeanDefinition.getBeanDefinition();
/*     */     }
/*     */     finally {
/*     */       
/* 324 */       this.currentBeanDefinition = current;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void xmlns(Map<String, String> definition) {
/* 333 */     if (!definition.isEmpty()) {
/* 334 */       for (Map.Entry<String, String> entry : definition.entrySet()) {
/* 335 */         String namespace = entry.getKey();
/* 336 */         String uri = entry.getValue();
/* 337 */         if (uri == null) {
/* 338 */           throw new IllegalArgumentException("Namespace definition must supply a non-null URI");
/*     */         }
/*     */         
/* 341 */         NamespaceHandler namespaceHandler = this.groovyDslXmlBeanDefinitionReader.getNamespaceHandlerResolver().resolve(uri);
/* 342 */         if (namespaceHandler == null) {
/* 343 */           throw new BeanDefinitionParsingException(new Problem("No namespace handler found for URI: " + uri, new Location(new DescriptiveResource("Groovy"))));
/*     */         }
/*     */         
/* 346 */         this.namespaces.put(namespace, uri);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void importBeans(String resourcePattern) throws IOException {
/* 357 */     loadBeanDefinitions(resourcePattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invokeMethod(String name, Object arg) {
/* 368 */     Object[] args = (Object[])arg;
/* 369 */     if ("beans".equals(name) && args.length == 1 && args[0] instanceof Closure) {
/* 370 */       return beans((Closure)args[0]);
/*     */     }
/* 372 */     if ("ref".equals(name)) {
/*     */       String refName;
/* 374 */       if (args[0] == null) {
/* 375 */         throw new IllegalArgumentException("Argument to ref() is not a valid bean or was not found");
/*     */       }
/* 377 */       if (args[0] instanceof RuntimeBeanReference) {
/* 378 */         refName = ((RuntimeBeanReference)args[0]).getBeanName();
/*     */       } else {
/*     */         
/* 381 */         refName = args[0].toString();
/*     */       } 
/* 383 */       boolean parentRef = false;
/* 384 */       if (args.length > 1 && args[1] instanceof Boolean) {
/* 385 */         parentRef = ((Boolean)args[1]).booleanValue();
/*     */       }
/* 387 */       return new RuntimeBeanReference(refName, parentRef);
/*     */     } 
/* 389 */     if (this.namespaces.containsKey(name) && args.length > 0 && args[0] instanceof Closure) {
/* 390 */       GroovyDynamicElementReader reader = createDynamicElementReader(name);
/* 391 */       reader.invokeMethod("doCall", args);
/*     */     } else {
/* 393 */       if (args.length > 0 && args[0] instanceof Closure)
/*     */       {
/* 395 */         return invokeBeanDefiningMethod(name, args);
/*     */       }
/* 397 */       if (args.length > 0 && (args[0] instanceof Class || args[0] instanceof RuntimeBeanReference || args[0] instanceof Map))
/*     */       {
/* 399 */         return invokeBeanDefiningMethod(name, args);
/*     */       }
/* 401 */       if (args.length > 1 && args[args.length - 1] instanceof Closure)
/* 402 */         return invokeBeanDefiningMethod(name, args); 
/*     */     } 
/* 404 */     MetaClass mc = DefaultGroovyMethods.getMetaClass(getRegistry());
/* 405 */     if (!mc.respondsTo(getRegistry(), name, args).isEmpty()) {
/* 406 */       return mc.invokeMethod(getRegistry(), name, args);
/*     */     }
/* 408 */     return this;
/*     */   }
/*     */   
/*     */   private boolean addDeferredProperty(String property, Object newValue) {
/* 412 */     if (newValue instanceof List || newValue instanceof Map) {
/* 413 */       this.deferredProperties.put(this.currentBeanDefinition.getBeanName() + '.' + property, new DeferredProperty(this.currentBeanDefinition, property, newValue));
/*     */       
/* 415 */       return true;
/*     */     } 
/* 417 */     return false;
/*     */   }
/*     */   
/*     */   private void finalizeDeferredProperties() {
/* 421 */     for (DeferredProperty dp : this.deferredProperties.values()) {
/* 422 */       if (dp.value instanceof List) {
/* 423 */         dp.value = manageListIfNecessary((List)dp.value);
/*     */       }
/* 425 */       else if (dp.value instanceof Map) {
/* 426 */         dp.value = manageMapIfNecessary((Map<?, ?>)dp.value);
/*     */       } 
/* 428 */       dp.apply();
/*     */     } 
/* 430 */     this.deferredProperties.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected GroovyBeanDefinitionReader invokeBeanDefiningClosure(Closure callable) {
/* 439 */     callable.setDelegate(this);
/* 440 */     callable.call();
/* 441 */     finalizeDeferredProperties();
/* 442 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private GroovyBeanDefinitionWrapper invokeBeanDefiningMethod(String beanName, Object[] args) {
/* 453 */     boolean hasClosureArgument = args[args.length - 1] instanceof Closure;
/* 454 */     if (args[0] instanceof Class) {
/* 455 */       Class<?> beanClass = (Class)args[0];
/* 456 */       if (args.length >= 1) {
/* 457 */         if (hasClosureArgument) {
/* 458 */           if (args.length - 1 != 1) {
/* 459 */             this
/* 460 */               .currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, beanClass, resolveConstructorArguments(args, 1, args.length - 1));
/*     */           } else {
/*     */             
/* 463 */             this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, beanClass);
/*     */           } 
/*     */         } else {
/*     */           
/* 467 */           this
/* 468 */             .currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, beanClass, resolveConstructorArguments(args, 1, args.length));
/*     */         }
/*     */       
/*     */       }
/*     */     }
/* 473 */     else if (args[0] instanceof RuntimeBeanReference) {
/* 474 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName);
/* 475 */       this.currentBeanDefinition.getBeanDefinition().setFactoryBeanName(((RuntimeBeanReference)args[0]).getBeanName());
/*     */     }
/* 477 */     else if (args[0] instanceof Map) {
/*     */       
/* 479 */       if (args.length > 1 && args[1] instanceof Class) {
/* 480 */         List<Object> constructorArgs = resolveConstructorArguments(args, 2, hasClosureArgument ? (args.length - 1) : args.length);
/* 481 */         this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, (Class)args[1], constructorArgs);
/* 482 */         Map namedArgs = (Map)args[0];
/* 483 */         for (Object o : namedArgs.keySet()) {
/* 484 */           String propName = (String)o;
/* 485 */           setProperty(propName, namedArgs.get(propName));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 490 */         this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName);
/*     */         
/* 492 */         Map.Entry factoryBeanEntry = ((Map<?, ?>)args[0]).entrySet().iterator().next();
/*     */ 
/*     */         
/* 495 */         int constructorArgsTest = hasClosureArgument ? 2 : 1;
/*     */         
/* 497 */         if (args.length > constructorArgsTest) {
/*     */           
/* 499 */           int endOfConstructArgs = hasClosureArgument ? (args.length - 1) : args.length;
/* 500 */           this
/* 501 */             .currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, null, resolveConstructorArguments(args, 1, endOfConstructArgs));
/*     */         } else {
/*     */           
/* 504 */           this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName);
/*     */         } 
/* 506 */         this.currentBeanDefinition.getBeanDefinition().setFactoryBeanName(factoryBeanEntry.getKey().toString());
/* 507 */         this.currentBeanDefinition.getBeanDefinition().setFactoryMethodName(factoryBeanEntry.getValue().toString());
/*     */       }
/*     */     
/*     */     }
/* 511 */     else if (args[0] instanceof Closure) {
/* 512 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName);
/* 513 */       this.currentBeanDefinition.getBeanDefinition().setAbstract(true);
/*     */     } else {
/*     */       
/* 516 */       List<Object> constructorArgs = resolveConstructorArguments(args, 0, hasClosureArgument ? (args.length - 1) : args.length);
/* 517 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, null, constructorArgs);
/*     */     } 
/*     */     
/* 520 */     if (hasClosureArgument) {
/* 521 */       Closure callable = (Closure)args[args.length - 1];
/* 522 */       callable.setDelegate(this);
/* 523 */       callable.setResolveStrategy(1);
/* 524 */       callable.call(this.currentBeanDefinition);
/*     */     } 
/*     */     
/* 527 */     GroovyBeanDefinitionWrapper beanDefinition = this.currentBeanDefinition;
/* 528 */     this.currentBeanDefinition = null;
/* 529 */     beanDefinition.getBeanDefinition().setAttribute(GroovyBeanDefinitionWrapper.class.getName(), beanDefinition);
/* 530 */     getRegistry().registerBeanDefinition(beanName, (BeanDefinition)beanDefinition.getBeanDefinition());
/* 531 */     return beanDefinition;
/*     */   }
/*     */   
/*     */   protected List<Object> resolveConstructorArguments(Object[] args, int start, int end) {
/* 535 */     Object[] constructorArgs = Arrays.copyOfRange(args, start, end);
/* 536 */     for (int i = 0; i < constructorArgs.length; i++) {
/* 537 */       if (constructorArgs[i] instanceof groovy.lang.GString) {
/* 538 */         constructorArgs[i] = constructorArgs[i].toString();
/*     */       }
/* 540 */       else if (constructorArgs[i] instanceof List) {
/* 541 */         constructorArgs[i] = manageListIfNecessary((List)constructorArgs[i]);
/*     */       }
/* 543 */       else if (constructorArgs[i] instanceof Map) {
/* 544 */         constructorArgs[i] = manageMapIfNecessary((Map<?, ?>)constructorArgs[i]);
/*     */       } 
/*     */     } 
/* 547 */     return Arrays.asList(constructorArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object manageMapIfNecessary(Map<?, ?> map) {
/* 557 */     boolean containsRuntimeRefs = false;
/* 558 */     for (Object element : map.values()) {
/* 559 */       if (element instanceof RuntimeBeanReference) {
/* 560 */         containsRuntimeRefs = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 564 */     if (containsRuntimeRefs) {
/* 565 */       ManagedMap<?, ?> managedMap = new ManagedMap();
/* 566 */       managedMap.putAll(map);
/* 567 */       return managedMap;
/*     */     } 
/* 569 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object manageListIfNecessary(List<?> list) {
/* 579 */     boolean containsRuntimeRefs = false;
/* 580 */     for (Object element : list) {
/* 581 */       if (element instanceof RuntimeBeanReference) {
/* 582 */         containsRuntimeRefs = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 586 */     if (containsRuntimeRefs) {
/* 587 */       ManagedList<?> managedList = new ManagedList();
/* 588 */       managedList.addAll(list);
/* 589 */       return managedList;
/*     */     } 
/* 591 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) {
/* 599 */     if (this.currentBeanDefinition != null) {
/* 600 */       applyPropertyToBeanDefinition(name, value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void applyPropertyToBeanDefinition(String name, Object value) {
/* 605 */     if (value instanceof groovy.lang.GString) {
/* 606 */       value = value.toString();
/*     */     }
/* 608 */     if (addDeferredProperty(name, value)) {
/*     */       return;
/*     */     }
/* 611 */     if (value instanceof Closure) {
/* 612 */       GroovyBeanDefinitionWrapper current = this.currentBeanDefinition;
/*     */       try {
/* 614 */         Closure callable = (Closure)value;
/* 615 */         Class<?> parameterType = callable.getParameterTypes()[0];
/* 616 */         if (Object.class == parameterType) {
/* 617 */           this.currentBeanDefinition = new GroovyBeanDefinitionWrapper("");
/* 618 */           callable.call(this.currentBeanDefinition);
/*     */         } else {
/*     */           
/* 621 */           this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(null, parameterType);
/* 622 */           callable.call(null);
/*     */         } 
/*     */         
/* 625 */         value = this.currentBeanDefinition.getBeanDefinition();
/*     */       } finally {
/*     */         
/* 628 */         this.currentBeanDefinition = current;
/*     */       } 
/*     */     } 
/* 631 */     this.currentBeanDefinition.addProperty(name, value);
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
/*     */   public Object getProperty(String name) {
/* 645 */     Binding binding = getBinding();
/* 646 */     if (binding != null && binding.hasVariable(name)) {
/* 647 */       return binding.getVariable(name);
/*     */     }
/*     */     
/* 650 */     if (this.namespaces.containsKey(name)) {
/* 651 */       return createDynamicElementReader(name);
/*     */     }
/* 653 */     if (getRegistry().containsBeanDefinition(name)) {
/*     */       
/* 655 */       GroovyBeanDefinitionWrapper beanDefinition = (GroovyBeanDefinitionWrapper)getRegistry().getBeanDefinition(name).getAttribute(GroovyBeanDefinitionWrapper.class.getName());
/* 656 */       if (beanDefinition != null) {
/* 657 */         return new GroovyRuntimeBeanReference(name, beanDefinition, false);
/*     */       }
/*     */       
/* 660 */       return new RuntimeBeanReference(name, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 665 */     if (this.currentBeanDefinition != null) {
/* 666 */       MutablePropertyValues pvs = this.currentBeanDefinition.getBeanDefinition().getPropertyValues();
/* 667 */       if (pvs.contains(name)) {
/* 668 */         return pvs.get(name);
/*     */       }
/*     */       
/* 671 */       DeferredProperty dp = this.deferredProperties.get(this.currentBeanDefinition.getBeanName() + name);
/* 672 */       if (dp != null) {
/* 673 */         return dp.value;
/*     */       }
/*     */       
/* 676 */       return getMetaClass().getProperty(this, name);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 681 */     return getMetaClass().getProperty(this, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private GroovyDynamicElementReader createDynamicElementReader(String namespace) {
/* 687 */     XmlReaderContext readerContext = this.groovyDslXmlBeanDefinitionReader.createReaderContext((Resource)new DescriptiveResource("Groovy"));
/*     */     
/* 689 */     BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
/* 690 */     boolean decorating = (this.currentBeanDefinition != null);
/* 691 */     if (!decorating) {
/* 692 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(namespace);
/*     */     }
/* 694 */     return new GroovyDynamicElementReader(namespace, this.namespaces, delegate, this.currentBeanDefinition, decorating)
/*     */       {
/*     */         protected void afterInvocation() {
/* 697 */           if (!this.decorating) {
/* 698 */             GroovyBeanDefinitionReader.this.currentBeanDefinition = null;
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DeferredProperty
/*     */   {
/*     */     private final GroovyBeanDefinitionWrapper beanDefinition;
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     public Object value;
/*     */ 
/*     */ 
/*     */     
/*     */     public DeferredProperty(GroovyBeanDefinitionWrapper beanDefinition, String name, Object value) {
/* 720 */       this.beanDefinition = beanDefinition;
/* 721 */       this.name = name;
/* 722 */       this.value = value;
/*     */     }
/*     */     
/*     */     public void apply() {
/* 726 */       this.beanDefinition.addProperty(this.name, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class GroovyRuntimeBeanReference
/*     */     extends RuntimeBeanReference
/*     */     implements GroovyObject
/*     */   {
/*     */     private final GroovyBeanDefinitionWrapper beanDefinition;
/*     */     
/*     */     private MetaClass metaClass;
/*     */ 
/*     */     
/*     */     public GroovyRuntimeBeanReference(String beanName, GroovyBeanDefinitionWrapper beanDefinition, boolean toParent) {
/* 741 */       super(beanName, toParent);
/* 742 */       this.beanDefinition = beanDefinition;
/* 743 */       this.metaClass = InvokerHelper.getMetaClass(this);
/*     */     }
/*     */     
/*     */     public MetaClass getMetaClass() {
/* 747 */       return this.metaClass;
/*     */     }
/*     */     
/*     */     public Object getProperty(String property) {
/* 751 */       if (property.equals("beanName")) {
/* 752 */         return getBeanName();
/*     */       }
/* 754 */       if (property.equals("source")) {
/* 755 */         return getSource();
/*     */       }
/* 757 */       if (this.beanDefinition != null) {
/* 758 */         return new GroovyPropertyValue(property, this.beanDefinition
/* 759 */             .getBeanDefinition().getPropertyValues().get(property));
/*     */       }
/*     */       
/* 762 */       return this.metaClass.getProperty(this, property);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invokeMethod(String name, Object args) {
/* 767 */       return this.metaClass.invokeMethod(this, name, args);
/*     */     }
/*     */     
/*     */     public void setMetaClass(MetaClass metaClass) {
/* 771 */       this.metaClass = metaClass;
/*     */     }
/*     */     
/*     */     public void setProperty(String property, Object newValue) {
/* 775 */       if (!GroovyBeanDefinitionReader.this.addDeferredProperty(property, newValue)) {
/* 776 */         this.beanDefinition.getBeanDefinition().getPropertyValues().add(property, newValue);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private class GroovyPropertyValue
/*     */       extends GroovyObjectSupport
/*     */     {
/*     */       private final String propertyName;
/*     */ 
/*     */       
/*     */       private final Object propertyValue;
/*     */ 
/*     */       
/*     */       public GroovyPropertyValue(String propertyName, Object propertyValue) {
/* 792 */         this.propertyName = propertyName;
/* 793 */         this.propertyValue = propertyValue;
/*     */       }
/*     */       
/*     */       public void leftShift(Object value) {
/* 797 */         InvokerHelper.invokeMethod(this.propertyValue, "leftShift", value);
/* 798 */         updateDeferredProperties(value);
/*     */       }
/*     */       
/*     */       public boolean add(Object value) {
/* 802 */         boolean retVal = ((Boolean)InvokerHelper.invokeMethod(this.propertyValue, "add", value)).booleanValue();
/* 803 */         updateDeferredProperties(value);
/* 804 */         return retVal;
/*     */       }
/*     */       
/*     */       public boolean addAll(Collection values) {
/* 808 */         boolean retVal = ((Boolean)InvokerHelper.invokeMethod(this.propertyValue, "addAll", values)).booleanValue();
/* 809 */         for (Object value : values) {
/* 810 */           updateDeferredProperties(value);
/*     */         }
/* 812 */         return retVal;
/*     */       }
/*     */ 
/*     */       
/*     */       public Object invokeMethod(String name, Object args) {
/* 817 */         return InvokerHelper.invokeMethod(this.propertyValue, name, args);
/*     */       }
/*     */ 
/*     */       
/*     */       public Object getProperty(String name) {
/* 822 */         return InvokerHelper.getProperty(this.propertyValue, name);
/*     */       }
/*     */ 
/*     */       
/*     */       public void setProperty(String name, Object value) {
/* 827 */         InvokerHelper.setProperty(this.propertyValue, name, value);
/*     */       }
/*     */       
/*     */       private void updateDeferredProperties(Object value) {
/* 831 */         if (value instanceof RuntimeBeanReference)
/* 832 */           GroovyBeanDefinitionReader.this.deferredProperties.put(GroovyBeanDefinitionReader.GroovyRuntimeBeanReference.this.beanDefinition.getBeanName(), new GroovyBeanDefinitionReader.DeferredProperty(GroovyBeanDefinitionReader.GroovyRuntimeBeanReference.this
/* 833 */                 .beanDefinition, this.propertyName, this.propertyValue)); 
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\groovy\GroovyBeanDefinitionReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */