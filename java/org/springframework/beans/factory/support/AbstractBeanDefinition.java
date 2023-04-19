/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.Arrays;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*      */ import org.springframework.beans.MutablePropertyValues;
/*      */ import org.springframework.beans.PropertyValues;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.core.AttributeAccessor;
/*      */ import org.springframework.core.io.DescriptiveResource;
/*      */ import org.springframework.core.io.Resource;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractBeanDefinition
/*      */   extends BeanMetadataAttributeAccessor
/*      */   implements BeanDefinition, Cloneable
/*      */ {
/*      */   public static final String SCOPE_DEFAULT = "";
/*      */   public static final int AUTOWIRE_NO = 0;
/*      */   public static final int AUTOWIRE_BY_NAME = 1;
/*      */   public static final int AUTOWIRE_BY_TYPE = 2;
/*      */   public static final int AUTOWIRE_CONSTRUCTOR = 3;
/*      */   @Deprecated
/*      */   public static final int AUTOWIRE_AUTODETECT = 4;
/*      */   public static final int DEPENDENCY_CHECK_NONE = 0;
/*      */   public static final int DEPENDENCY_CHECK_OBJECTS = 1;
/*      */   public static final int DEPENDENCY_CHECK_SIMPLE = 2;
/*      */   public static final int DEPENDENCY_CHECK_ALL = 3;
/*      */   public static final String INFER_METHOD = "(inferred)";
/*      */   private volatile Object beanClass;
/*  140 */   private String scope = "";
/*      */   
/*      */   private boolean abstractFlag = false;
/*      */   
/*      */   private boolean lazyInit = false;
/*      */   
/*  146 */   private int autowireMode = 0;
/*      */   
/*  148 */   private int dependencyCheck = 0;
/*      */   
/*      */   private String[] dependsOn;
/*      */   
/*      */   private boolean autowireCandidate = true;
/*      */   
/*      */   private boolean primary = false;
/*      */   
/*  156 */   private final Map<String, AutowireCandidateQualifier> qualifiers = new LinkedHashMap<String, AutowireCandidateQualifier>(0);
/*      */ 
/*      */   
/*      */   private boolean nonPublicAccessAllowed = true;
/*      */   
/*      */   private boolean lenientConstructorResolution = true;
/*      */   
/*      */   private String factoryBeanName;
/*      */   
/*      */   private String factoryMethodName;
/*      */   
/*      */   private ConstructorArgumentValues constructorArgumentValues;
/*      */   
/*      */   private MutablePropertyValues propertyValues;
/*      */   
/*  171 */   private MethodOverrides methodOverrides = new MethodOverrides();
/*      */   
/*      */   private String initMethodName;
/*      */   
/*      */   private String destroyMethodName;
/*      */   
/*      */   private boolean enforceInitMethod = true;
/*      */   
/*      */   private boolean enforceDestroyMethod = true;
/*      */   
/*      */   private boolean synthetic = false;
/*      */   
/*  183 */   private int role = 0;
/*      */ 
/*      */   
/*      */   private String description;
/*      */ 
/*      */   
/*      */   private Resource resource;
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractBeanDefinition() {
/*  194 */     this((ConstructorArgumentValues)null, (MutablePropertyValues)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractBeanDefinition(ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/*  202 */     setConstructorArgumentValues(cargs);
/*  203 */     setPropertyValues(pvs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractBeanDefinition(BeanDefinition original) {
/*  212 */     setParentName(original.getParentName());
/*  213 */     setBeanClassName(original.getBeanClassName());
/*  214 */     setScope(original.getScope());
/*  215 */     setAbstract(original.isAbstract());
/*  216 */     setLazyInit(original.isLazyInit());
/*  217 */     setFactoryBeanName(original.getFactoryBeanName());
/*  218 */     setFactoryMethodName(original.getFactoryMethodName());
/*  219 */     setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
/*  220 */     setPropertyValues(new MutablePropertyValues((PropertyValues)original.getPropertyValues()));
/*  221 */     setRole(original.getRole());
/*  222 */     setSource(original.getSource());
/*  223 */     copyAttributesFrom((AttributeAccessor)original);
/*      */     
/*  225 */     if (original instanceof AbstractBeanDefinition) {
/*  226 */       AbstractBeanDefinition originalAbd = (AbstractBeanDefinition)original;
/*  227 */       if (originalAbd.hasBeanClass()) {
/*  228 */         setBeanClass(originalAbd.getBeanClass());
/*      */       }
/*  230 */       setAutowireMode(originalAbd.getAutowireMode());
/*  231 */       setDependencyCheck(originalAbd.getDependencyCheck());
/*  232 */       setDependsOn(originalAbd.getDependsOn());
/*  233 */       setAutowireCandidate(originalAbd.isAutowireCandidate());
/*  234 */       setPrimary(originalAbd.isPrimary());
/*  235 */       copyQualifiersFrom(originalAbd);
/*  236 */       setNonPublicAccessAllowed(originalAbd.isNonPublicAccessAllowed());
/*  237 */       setLenientConstructorResolution(originalAbd.isLenientConstructorResolution());
/*  238 */       setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
/*  239 */       setInitMethodName(originalAbd.getInitMethodName());
/*  240 */       setEnforceInitMethod(originalAbd.isEnforceInitMethod());
/*  241 */       setDestroyMethodName(originalAbd.getDestroyMethodName());
/*  242 */       setEnforceDestroyMethod(originalAbd.isEnforceDestroyMethod());
/*  243 */       setSynthetic(originalAbd.isSynthetic());
/*  244 */       setResource(originalAbd.getResource());
/*      */     } else {
/*      */       
/*  247 */       setResourceDescription(original.getResourceDescription());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void overrideFrom(BeanDefinition other) {
/*  269 */     if (StringUtils.hasLength(other.getBeanClassName())) {
/*  270 */       setBeanClassName(other.getBeanClassName());
/*      */     }
/*  272 */     if (StringUtils.hasLength(other.getScope())) {
/*  273 */       setScope(other.getScope());
/*      */     }
/*  275 */     setAbstract(other.isAbstract());
/*  276 */     setLazyInit(other.isLazyInit());
/*  277 */     if (StringUtils.hasLength(other.getFactoryBeanName())) {
/*  278 */       setFactoryBeanName(other.getFactoryBeanName());
/*      */     }
/*  280 */     if (StringUtils.hasLength(other.getFactoryMethodName())) {
/*  281 */       setFactoryMethodName(other.getFactoryMethodName());
/*      */     }
/*  283 */     getConstructorArgumentValues().addArgumentValues(other.getConstructorArgumentValues());
/*  284 */     getPropertyValues().addPropertyValues((PropertyValues)other.getPropertyValues());
/*  285 */     setRole(other.getRole());
/*  286 */     setSource(other.getSource());
/*  287 */     copyAttributesFrom((AttributeAccessor)other);
/*      */     
/*  289 */     if (other instanceof AbstractBeanDefinition) {
/*  290 */       AbstractBeanDefinition otherAbd = (AbstractBeanDefinition)other;
/*  291 */       if (otherAbd.hasBeanClass()) {
/*  292 */         setBeanClass(otherAbd.getBeanClass());
/*      */       }
/*  294 */       setAutowireMode(otherAbd.getAutowireMode());
/*  295 */       setDependencyCheck(otherAbd.getDependencyCheck());
/*  296 */       setDependsOn(otherAbd.getDependsOn());
/*  297 */       setAutowireCandidate(otherAbd.isAutowireCandidate());
/*  298 */       setPrimary(otherAbd.isPrimary());
/*  299 */       copyQualifiersFrom(otherAbd);
/*  300 */       setNonPublicAccessAllowed(otherAbd.isNonPublicAccessAllowed());
/*  301 */       setLenientConstructorResolution(otherAbd.isLenientConstructorResolution());
/*  302 */       getMethodOverrides().addOverrides(otherAbd.getMethodOverrides());
/*  303 */       if (StringUtils.hasLength(otherAbd.getInitMethodName())) {
/*  304 */         setInitMethodName(otherAbd.getInitMethodName());
/*  305 */         setEnforceInitMethod(otherAbd.isEnforceInitMethod());
/*      */       } 
/*  307 */       if (otherAbd.getDestroyMethodName() != null) {
/*  308 */         setDestroyMethodName(otherAbd.getDestroyMethodName());
/*  309 */         setEnforceDestroyMethod(otherAbd.isEnforceDestroyMethod());
/*      */       } 
/*  311 */       setSynthetic(otherAbd.isSynthetic());
/*  312 */       setResource(otherAbd.getResource());
/*      */     } else {
/*      */       
/*  315 */       setResourceDescription(other.getResourceDescription());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void applyDefaults(BeanDefinitionDefaults defaults) {
/*  324 */     setLazyInit(defaults.isLazyInit());
/*  325 */     setAutowireMode(defaults.getAutowireMode());
/*  326 */     setDependencyCheck(defaults.getDependencyCheck());
/*  327 */     setInitMethodName(defaults.getInitMethodName());
/*  328 */     setEnforceInitMethod(false);
/*  329 */     setDestroyMethodName(defaults.getDestroyMethodName());
/*  330 */     setEnforceDestroyMethod(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBeanClassName(String beanClassName) {
/*  339 */     this.beanClass = beanClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getBeanClassName() {
/*  347 */     Object beanClassObject = this.beanClass;
/*  348 */     if (beanClassObject instanceof Class) {
/*  349 */       return ((Class)beanClassObject).getName();
/*      */     }
/*      */     
/*  352 */     return (String)beanClassObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBeanClass(Class<?> beanClass) {
/*  360 */     this.beanClass = beanClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> getBeanClass() throws IllegalStateException {
/*  370 */     Object beanClassObject = this.beanClass;
/*  371 */     if (beanClassObject == null) {
/*  372 */       throw new IllegalStateException("No bean class specified on bean definition");
/*      */     }
/*  374 */     if (!(beanClassObject instanceof Class)) {
/*  375 */       throw new IllegalStateException("Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
/*      */     }
/*      */     
/*  378 */     return (Class)beanClassObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasBeanClass() {
/*  385 */     return this.beanClass instanceof Class;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
/*  397 */     String className = getBeanClassName();
/*  398 */     if (className == null) {
/*  399 */       return null;
/*      */     }
/*  401 */     Class<?> resolvedClass = ClassUtils.forName(className, classLoader);
/*  402 */     this.beanClass = resolvedClass;
/*  403 */     return resolvedClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScope(String scope) {
/*  418 */     this.scope = scope;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getScope() {
/*  426 */     return this.scope;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSingleton() {
/*  436 */     return ("singleton".equals(this.scope) || "".equals(this.scope));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrototype() {
/*  446 */     return "prototype".equals(this.scope);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAbstract(boolean abstractFlag) {
/*  456 */     this.abstractFlag = abstractFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAbstract() {
/*  465 */     return this.abstractFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLazyInit(boolean lazyInit) {
/*  475 */     this.lazyInit = lazyInit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLazyInit() {
/*  484 */     return this.lazyInit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutowireMode(int autowireMode) {
/*  500 */     this.autowireMode = autowireMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAutowireMode() {
/*  507 */     return this.autowireMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getResolvedAutowireMode() {
/*  518 */     if (this.autowireMode == 4) {
/*      */ 
/*      */ 
/*      */       
/*  522 */       Constructor[] arrayOfConstructor = (Constructor[])getBeanClass().getConstructors();
/*  523 */       for (Constructor<?> constructor : arrayOfConstructor) {
/*  524 */         if ((constructor.getParameterTypes()).length == 0) {
/*  525 */           return 2;
/*      */         }
/*      */       } 
/*  528 */       return 3;
/*      */     } 
/*      */     
/*  531 */     return this.autowireMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDependencyCheck(int dependencyCheck) {
/*  545 */     this.dependencyCheck = dependencyCheck;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDependencyCheck() {
/*  552 */     return this.dependencyCheck;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDependsOn(String... dependsOn) {
/*  564 */     this.dependsOn = dependsOn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getDependsOn() {
/*  572 */     return this.dependsOn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutowireCandidate(boolean autowireCandidate) {
/*  586 */     this.autowireCandidate = autowireCandidate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutowireCandidate() {
/*  594 */     return this.autowireCandidate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPrimary(boolean primary) {
/*  604 */     this.primary = primary;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrimary() {
/*  612 */     return this.primary;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addQualifier(AutowireCandidateQualifier qualifier) {
/*  621 */     this.qualifiers.put(qualifier.getTypeName(), qualifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQualifier(String typeName) {
/*  628 */     return this.qualifiers.keySet().contains(typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AutowireCandidateQualifier getQualifier(String typeName) {
/*  635 */     return this.qualifiers.get(typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<AutowireCandidateQualifier> getQualifiers() {
/*  643 */     return new LinkedHashSet<AutowireCandidateQualifier>(this.qualifiers.values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyQualifiersFrom(AbstractBeanDefinition source) {
/*  651 */     Assert.notNull(source, "Source must not be null");
/*  652 */     this.qualifiers.putAll(source.qualifiers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed) {
/*  667 */     this.nonPublicAccessAllowed = nonPublicAccessAllowed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNonPublicAccessAllowed() {
/*  674 */     return this.nonPublicAccessAllowed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLenientConstructorResolution(boolean lenientConstructorResolution) {
/*  684 */     this.lenientConstructorResolution = lenientConstructorResolution;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLenientConstructorResolution() {
/*  691 */     return this.lenientConstructorResolution;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFactoryBeanName(String factoryBeanName) {
/*  701 */     this.factoryBeanName = factoryBeanName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFactoryBeanName() {
/*  709 */     return this.factoryBeanName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFactoryMethodName(String factoryMethodName) {
/*  722 */     this.factoryMethodName = factoryMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFactoryMethodName() {
/*  730 */     return this.factoryMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
/*  737 */     this.constructorArgumentValues = (constructorArgumentValues != null) ? constructorArgumentValues : new ConstructorArgumentValues();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConstructorArgumentValues getConstructorArgumentValues() {
/*  746 */     return this.constructorArgumentValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasConstructorArgumentValues() {
/*  753 */     return !this.constructorArgumentValues.isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyValues(MutablePropertyValues propertyValues) {
/*  760 */     this.propertyValues = (propertyValues != null) ? propertyValues : new MutablePropertyValues();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MutablePropertyValues getPropertyValues() {
/*  768 */     return this.propertyValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMethodOverrides(MethodOverrides methodOverrides) {
/*  775 */     this.methodOverrides = (methodOverrides != null) ? methodOverrides : new MethodOverrides();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MethodOverrides getMethodOverrides() {
/*  784 */     return this.methodOverrides;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitMethodName(String initMethodName) {
/*  792 */     this.initMethodName = initMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getInitMethodName() {
/*  799 */     return this.initMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnforceInitMethod(boolean enforceInitMethod) {
/*  808 */     this.enforceInitMethod = enforceInitMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnforceInitMethod() {
/*  816 */     return this.enforceInitMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestroyMethodName(String destroyMethodName) {
/*  824 */     this.destroyMethodName = destroyMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDestroyMethodName() {
/*  831 */     return this.destroyMethodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnforceDestroyMethod(boolean enforceDestroyMethod) {
/*  840 */     this.enforceDestroyMethod = enforceDestroyMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnforceDestroyMethod() {
/*  848 */     return this.enforceDestroyMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSynthetic(boolean synthetic) {
/*  857 */     this.synthetic = synthetic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSynthetic() {
/*  865 */     return this.synthetic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRole(int role) {
/*  872 */     this.role = role;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRole() {
/*  880 */     return this.role;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDescription(String description) {
/*  887 */     this.description = description;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDescription() {
/*  895 */     return this.description;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResource(Resource resource) {
/*  903 */     this.resource = resource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Resource getResource() {
/*  910 */     return this.resource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResourceDescription(String resourceDescription) {
/*  918 */     this.resource = (Resource)new DescriptiveResource(resourceDescription);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getResourceDescription() {
/*  927 */     return (this.resource != null) ? this.resource.getDescription() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOriginatingBeanDefinition(BeanDefinition originatingBd) {
/*  934 */     this.resource = (Resource)new BeanDefinitionResource(originatingBd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinition getOriginatingBeanDefinition() {
/*  945 */     return (this.resource instanceof BeanDefinitionResource) ? ((BeanDefinitionResource)this.resource)
/*  946 */       .getBeanDefinition() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void validate() throws BeanDefinitionValidationException {
/*  954 */     if (!getMethodOverrides().isEmpty() && getFactoryMethodName() != null) {
/*  955 */       throw new BeanDefinitionValidationException("Cannot combine static factory method with method overrides: the static factory method must create the instance");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  960 */     if (hasBeanClass()) {
/*  961 */       prepareMethodOverrides();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void prepareMethodOverrides() throws BeanDefinitionValidationException {
/*  972 */     MethodOverrides methodOverrides = getMethodOverrides();
/*  973 */     if (!methodOverrides.isEmpty()) {
/*  974 */       Set<MethodOverride> overrides = methodOverrides.getOverrides();
/*  975 */       synchronized (overrides) {
/*  976 */         for (MethodOverride mo : overrides) {
/*  977 */           prepareMethodOverride(mo);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void prepareMethodOverride(MethodOverride mo) throws BeanDefinitionValidationException {
/*  991 */     int count = ClassUtils.getMethodCountForName(getBeanClass(), mo.getMethodName());
/*  992 */     if (count == 0) {
/*  993 */       throw new BeanDefinitionValidationException("Invalid method override: no method with name '" + mo
/*  994 */           .getMethodName() + "' on class [" + 
/*  995 */           getBeanClassName() + "]");
/*      */     }
/*  997 */     if (count == 1)
/*      */     {
/*  999 */       mo.setOverloaded(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object clone() {
/* 1011 */     return cloneBeanDefinition();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract AbstractBeanDefinition cloneBeanDefinition();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object other) {
/* 1023 */     if (this == other) {
/* 1024 */       return true;
/*      */     }
/* 1026 */     if (!(other instanceof AbstractBeanDefinition)) {
/* 1027 */       return false;
/*      */     }
/*      */     
/* 1030 */     AbstractBeanDefinition that = (AbstractBeanDefinition)other;
/*      */     
/* 1032 */     if (!ObjectUtils.nullSafeEquals(getBeanClassName(), that.getBeanClassName())) return false; 
/* 1033 */     if (!ObjectUtils.nullSafeEquals(this.scope, that.scope)) return false; 
/* 1034 */     if (this.abstractFlag != that.abstractFlag) return false; 
/* 1035 */     if (this.lazyInit != that.lazyInit) return false;
/*      */     
/* 1037 */     if (this.autowireMode != that.autowireMode) return false; 
/* 1038 */     if (this.dependencyCheck != that.dependencyCheck) return false; 
/* 1039 */     if (!Arrays.equals((Object[])this.dependsOn, (Object[])that.dependsOn)) return false; 
/* 1040 */     if (this.autowireCandidate != that.autowireCandidate) return false; 
/* 1041 */     if (!ObjectUtils.nullSafeEquals(this.qualifiers, that.qualifiers)) return false; 
/* 1042 */     if (this.primary != that.primary) return false;
/*      */     
/* 1044 */     if (this.nonPublicAccessAllowed != that.nonPublicAccessAllowed) return false; 
/* 1045 */     if (this.lenientConstructorResolution != that.lenientConstructorResolution) return false; 
/* 1046 */     if (!ObjectUtils.nullSafeEquals(this.constructorArgumentValues, that.constructorArgumentValues)) return false; 
/* 1047 */     if (!ObjectUtils.nullSafeEquals(this.propertyValues, that.propertyValues)) return false; 
/* 1048 */     if (!ObjectUtils.nullSafeEquals(this.methodOverrides, that.methodOverrides)) return false;
/*      */     
/* 1050 */     if (!ObjectUtils.nullSafeEquals(this.factoryBeanName, that.factoryBeanName)) return false; 
/* 1051 */     if (!ObjectUtils.nullSafeEquals(this.factoryMethodName, that.factoryMethodName)) return false; 
/* 1052 */     if (!ObjectUtils.nullSafeEquals(this.initMethodName, that.initMethodName)) return false; 
/* 1053 */     if (this.enforceInitMethod != that.enforceInitMethod) return false; 
/* 1054 */     if (!ObjectUtils.nullSafeEquals(this.destroyMethodName, that.destroyMethodName)) return false; 
/* 1055 */     if (this.enforceDestroyMethod != that.enforceDestroyMethod) return false;
/*      */     
/* 1057 */     if (this.synthetic != that.synthetic) return false; 
/* 1058 */     if (this.role != that.role) return false;
/*      */     
/* 1060 */     return super.equals(other);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1065 */     int hashCode = ObjectUtils.nullSafeHashCode(getBeanClassName());
/* 1066 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.scope);
/* 1067 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.constructorArgumentValues);
/* 1068 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.propertyValues);
/* 1069 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryBeanName);
/* 1070 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryMethodName);
/* 1071 */     hashCode = 29 * hashCode + super.hashCode();
/* 1072 */     return hashCode;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1077 */     StringBuilder sb = new StringBuilder("class [");
/* 1078 */     sb.append(getBeanClassName()).append("]");
/* 1079 */     sb.append("; scope=").append(this.scope);
/* 1080 */     sb.append("; abstract=").append(this.abstractFlag);
/* 1081 */     sb.append("; lazyInit=").append(this.lazyInit);
/* 1082 */     sb.append("; autowireMode=").append(this.autowireMode);
/* 1083 */     sb.append("; dependencyCheck=").append(this.dependencyCheck);
/* 1084 */     sb.append("; autowireCandidate=").append(this.autowireCandidate);
/* 1085 */     sb.append("; primary=").append(this.primary);
/* 1086 */     sb.append("; factoryBeanName=").append(this.factoryBeanName);
/* 1087 */     sb.append("; factoryMethodName=").append(this.factoryMethodName);
/* 1088 */     sb.append("; initMethodName=").append(this.initMethodName);
/* 1089 */     sb.append("; destroyMethodName=").append(this.destroyMethodName);
/* 1090 */     if (this.resource != null) {
/* 1091 */       sb.append("; defined in ").append(this.resource.getDescription());
/*      */     }
/* 1093 */     return sb.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\AbstractBeanDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */