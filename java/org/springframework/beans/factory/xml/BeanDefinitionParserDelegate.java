/*      */ package org.springframework.beans.factory.xml;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.BeanMetadataAttribute;
/*      */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*      */ import org.springframework.beans.PropertyValue;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.beans.factory.config.RuntimeBeanNameReference;
/*      */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*      */ import org.springframework.beans.factory.config.TypedStringValue;
/*      */ import org.springframework.beans.factory.parsing.BeanEntry;
/*      */ import org.springframework.beans.factory.parsing.ConstructorArgumentEntry;
/*      */ import org.springframework.beans.factory.parsing.ParseState;
/*      */ import org.springframework.beans.factory.parsing.PropertyEntry;
/*      */ import org.springframework.beans.factory.parsing.QualifierEntry;
/*      */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*      */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*      */ import org.springframework.beans.factory.support.LookupOverride;
/*      */ import org.springframework.beans.factory.support.ManagedArray;
/*      */ import org.springframework.beans.factory.support.ManagedList;
/*      */ import org.springframework.beans.factory.support.ManagedMap;
/*      */ import org.springframework.beans.factory.support.ManagedProperties;
/*      */ import org.springframework.beans.factory.support.ManagedSet;
/*      */ import org.springframework.beans.factory.support.MethodOverride;
/*      */ import org.springframework.beans.factory.support.MethodOverrides;
/*      */ import org.springframework.beans.factory.support.ReplaceOverride;
/*      */ import org.springframework.core.env.Environment;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.PatternMatchUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.util.xml.DomUtils;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
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
/*      */ public class BeanDefinitionParserDelegate
/*      */ {
/*      */   public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
/*      */   public static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";
/*      */   public static final String TRUE_VALUE = "true";
/*      */   public static final String FALSE_VALUE = "false";
/*      */   public static final String DEFAULT_VALUE = "default";
/*      */   public static final String DESCRIPTION_ELEMENT = "description";
/*      */   public static final String AUTOWIRE_NO_VALUE = "no";
/*      */   public static final String AUTOWIRE_BY_NAME_VALUE = "byName";
/*      */   public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";
/*      */   public static final String AUTOWIRE_CONSTRUCTOR_VALUE = "constructor";
/*      */   public static final String AUTOWIRE_AUTODETECT_VALUE = "autodetect";
/*      */   public static final String DEPENDENCY_CHECK_ALL_ATTRIBUTE_VALUE = "all";
/*      */   public static final String DEPENDENCY_CHECK_SIMPLE_ATTRIBUTE_VALUE = "simple";
/*      */   public static final String DEPENDENCY_CHECK_OBJECTS_ATTRIBUTE_VALUE = "objects";
/*      */   public static final String NAME_ATTRIBUTE = "name";
/*      */   public static final String BEAN_ELEMENT = "bean";
/*      */   public static final String META_ELEMENT = "meta";
/*      */   public static final String ID_ATTRIBUTE = "id";
/*      */   public static final String PARENT_ATTRIBUTE = "parent";
/*      */   public static final String CLASS_ATTRIBUTE = "class";
/*      */   public static final String ABSTRACT_ATTRIBUTE = "abstract";
/*      */   public static final String SCOPE_ATTRIBUTE = "scope";
/*      */   private static final String SINGLETON_ATTRIBUTE = "singleton";
/*      */   public static final String LAZY_INIT_ATTRIBUTE = "lazy-init";
/*      */   public static final String AUTOWIRE_ATTRIBUTE = "autowire";
/*      */   public static final String AUTOWIRE_CANDIDATE_ATTRIBUTE = "autowire-candidate";
/*      */   public static final String PRIMARY_ATTRIBUTE = "primary";
/*      */   public static final String DEPENDENCY_CHECK_ATTRIBUTE = "dependency-check";
/*      */   public static final String DEPENDS_ON_ATTRIBUTE = "depends-on";
/*      */   public static final String INIT_METHOD_ATTRIBUTE = "init-method";
/*      */   public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
/*      */   public static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";
/*      */   public static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";
/*      */   public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
/*      */   public static final String INDEX_ATTRIBUTE = "index";
/*      */   public static final String TYPE_ATTRIBUTE = "type";
/*      */   public static final String VALUE_TYPE_ATTRIBUTE = "value-type";
/*      */   public static final String KEY_TYPE_ATTRIBUTE = "key-type";
/*      */   public static final String PROPERTY_ELEMENT = "property";
/*      */   public static final String REF_ATTRIBUTE = "ref";
/*      */   public static final String VALUE_ATTRIBUTE = "value";
/*      */   public static final String LOOKUP_METHOD_ELEMENT = "lookup-method";
/*      */   public static final String REPLACED_METHOD_ELEMENT = "replaced-method";
/*      */   public static final String REPLACER_ATTRIBUTE = "replacer";
/*      */   public static final String ARG_TYPE_ELEMENT = "arg-type";
/*      */   public static final String ARG_TYPE_MATCH_ATTRIBUTE = "match";
/*      */   public static final String REF_ELEMENT = "ref";
/*      */   public static final String IDREF_ELEMENT = "idref";
/*      */   public static final String BEAN_REF_ATTRIBUTE = "bean";
/*      */   public static final String LOCAL_REF_ATTRIBUTE = "local";
/*      */   public static final String PARENT_REF_ATTRIBUTE = "parent";
/*      */   public static final String VALUE_ELEMENT = "value";
/*      */   public static final String NULL_ELEMENT = "null";
/*      */   public static final String ARRAY_ELEMENT = "array";
/*      */   public static final String LIST_ELEMENT = "list";
/*      */   public static final String SET_ELEMENT = "set";
/*      */   public static final String MAP_ELEMENT = "map";
/*      */   public static final String ENTRY_ELEMENT = "entry";
/*      */   public static final String KEY_ELEMENT = "key";
/*      */   public static final String KEY_ATTRIBUTE = "key";
/*      */   public static final String KEY_REF_ATTRIBUTE = "key-ref";
/*      */   public static final String VALUE_REF_ATTRIBUTE = "value-ref";
/*      */   public static final String PROPS_ELEMENT = "props";
/*      */   public static final String PROP_ELEMENT = "prop";
/*      */   public static final String MERGE_ATTRIBUTE = "merge";
/*      */   public static final String QUALIFIER_ELEMENT = "qualifier";
/*      */   public static final String QUALIFIER_ATTRIBUTE_ELEMENT = "attribute";
/*      */   public static final String DEFAULT_LAZY_INIT_ATTRIBUTE = "default-lazy-init";
/*      */   public static final String DEFAULT_MERGE_ATTRIBUTE = "default-merge";
/*      */   public static final String DEFAULT_AUTOWIRE_ATTRIBUTE = "default-autowire";
/*      */   public static final String DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE = "default-dependency-check";
/*      */   public static final String DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE = "default-autowire-candidates";
/*      */   public static final String DEFAULT_INIT_METHOD_ATTRIBUTE = "default-init-method";
/*      */   public static final String DEFAULT_DESTROY_METHOD_ATTRIBUTE = "default-destroy-method";
/*  240 */   protected final Log logger = LogFactory.getLog(getClass());
/*      */   
/*      */   private final XmlReaderContext readerContext;
/*      */   
/*  244 */   private final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();
/*      */   
/*  246 */   private final ParseState parseState = new ParseState();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  253 */   private final Set<String> usedNames = new HashSet<String>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionParserDelegate(XmlReaderContext readerContext) {
/*  261 */     Assert.notNull(readerContext, "XmlReaderContext must not be null");
/*  262 */     this.readerContext = readerContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final XmlReaderContext getReaderContext() {
/*  270 */     return this.readerContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final Environment getEnvironment() {
/*  279 */     return this.readerContext.getEnvironment();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object extractSource(Element ele) {
/*  287 */     return this.readerContext.extractSource(ele);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void error(String message, Node source) {
/*  294 */     this.readerContext.error(message, source, this.parseState.snapshot());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void error(String message, Element source) {
/*  301 */     this.readerContext.error(message, source, this.parseState.snapshot());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void error(String message, Element source, Throwable cause) {
/*  308 */     this.readerContext.error(message, source, this.parseState.snapshot(), cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initDefaults(Element root) {
/*  316 */     initDefaults(root, null);
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
/*      */   public void initDefaults(Element root, BeanDefinitionParserDelegate parent) {
/*  328 */     populateDefaults(this.defaults, (parent != null) ? parent.defaults : null, root);
/*  329 */     this.readerContext.fireDefaultsRegistered(this.defaults);
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
/*      */   protected void populateDefaults(DocumentDefaultsDefinition defaults, DocumentDefaultsDefinition parentDefaults, Element root) {
/*  342 */     String lazyInit = root.getAttribute("default-lazy-init");
/*  343 */     if ("default".equals(lazyInit))
/*      */     {
/*  345 */       lazyInit = (parentDefaults != null) ? parentDefaults.getLazyInit() : "false";
/*      */     }
/*  347 */     defaults.setLazyInit(lazyInit);
/*      */     
/*  349 */     String merge = root.getAttribute("default-merge");
/*  350 */     if ("default".equals(merge))
/*      */     {
/*  352 */       merge = (parentDefaults != null) ? parentDefaults.getMerge() : "false";
/*      */     }
/*  354 */     defaults.setMerge(merge);
/*      */     
/*  356 */     String autowire = root.getAttribute("default-autowire");
/*  357 */     if ("default".equals(autowire))
/*      */     {
/*  359 */       autowire = (parentDefaults != null) ? parentDefaults.getAutowire() : "no";
/*      */     }
/*  361 */     defaults.setAutowire(autowire);
/*      */ 
/*      */ 
/*      */     
/*  365 */     defaults.setDependencyCheck(root.getAttribute("default-dependency-check"));
/*      */     
/*  367 */     if (root.hasAttribute("default-autowire-candidates")) {
/*  368 */       defaults.setAutowireCandidates(root.getAttribute("default-autowire-candidates"));
/*      */     }
/*  370 */     else if (parentDefaults != null) {
/*  371 */       defaults.setAutowireCandidates(parentDefaults.getAutowireCandidates());
/*      */     } 
/*      */     
/*  374 */     if (root.hasAttribute("default-init-method")) {
/*  375 */       defaults.setInitMethod(root.getAttribute("default-init-method"));
/*      */     }
/*  377 */     else if (parentDefaults != null) {
/*  378 */       defaults.setInitMethod(parentDefaults.getInitMethod());
/*      */     } 
/*      */     
/*  381 */     if (root.hasAttribute("default-destroy-method")) {
/*  382 */       defaults.setDestroyMethod(root.getAttribute("default-destroy-method"));
/*      */     }
/*  384 */     else if (parentDefaults != null) {
/*  385 */       defaults.setDestroyMethod(parentDefaults.getDestroyMethod());
/*      */     } 
/*      */     
/*  388 */     defaults.setSource(this.readerContext.extractSource(root));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DocumentDefaultsDefinition getDefaults() {
/*  395 */     return this.defaults;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionDefaults getBeanDefinitionDefaults() {
/*  403 */     BeanDefinitionDefaults bdd = new BeanDefinitionDefaults();
/*  404 */     bdd.setLazyInit("TRUE".equalsIgnoreCase(this.defaults.getLazyInit()));
/*  405 */     bdd.setDependencyCheck(getDependencyCheck("default"));
/*  406 */     bdd.setAutowireMode(getAutowireMode("default"));
/*  407 */     bdd.setInitMethodName(this.defaults.getInitMethod());
/*  408 */     bdd.setDestroyMethodName(this.defaults.getDestroyMethod());
/*  409 */     return bdd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getAutowireCandidatePatterns() {
/*  417 */     String candidatePattern = this.defaults.getAutowireCandidates();
/*  418 */     return (candidatePattern != null) ? StringUtils.commaDelimitedListToStringArray(candidatePattern) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionHolder parseBeanDefinitionElement(Element ele) {
/*  428 */     return parseBeanDefinitionElement(ele, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, BeanDefinition containingBean) {
/*  437 */     String id = ele.getAttribute("id");
/*  438 */     String nameAttr = ele.getAttribute("name");
/*      */     
/*  440 */     List<String> aliases = new ArrayList<String>();
/*  441 */     if (StringUtils.hasLength(nameAttr)) {
/*  442 */       String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, ",; ");
/*  443 */       aliases.addAll(Arrays.asList(nameArr));
/*      */     } 
/*      */     
/*  446 */     String beanName = id;
/*  447 */     if (!StringUtils.hasText(beanName) && !aliases.isEmpty()) {
/*  448 */       beanName = aliases.remove(0);
/*  449 */       if (this.logger.isDebugEnabled()) {
/*  450 */         this.logger.debug("No XML 'id' specified - using '" + beanName + "' as bean name and " + aliases + " as aliases");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  455 */     if (containingBean == null) {
/*  456 */       checkNameUniqueness(beanName, aliases, ele);
/*      */     }
/*      */     
/*  459 */     AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
/*  460 */     if (beanDefinition != null) {
/*  461 */       if (!StringUtils.hasText(beanName)) {
/*      */         try {
/*  463 */           if (containingBean != null) {
/*  464 */             beanName = BeanDefinitionReaderUtils.generateBeanName((BeanDefinition)beanDefinition, this.readerContext
/*  465 */                 .getRegistry(), true);
/*      */           } else {
/*      */             
/*  468 */             beanName = this.readerContext.generateBeanName((BeanDefinition)beanDefinition);
/*      */ 
/*      */ 
/*      */             
/*  472 */             String beanClassName = beanDefinition.getBeanClassName();
/*  473 */             if (beanClassName != null && beanName
/*  474 */               .startsWith(beanClassName) && beanName.length() > beanClassName.length() && 
/*  475 */               !this.readerContext.getRegistry().isBeanNameInUse(beanClassName)) {
/*  476 */               aliases.add(beanClassName);
/*      */             }
/*      */           } 
/*  479 */           if (this.logger.isDebugEnabled()) {
/*  480 */             this.logger.debug("Neither XML 'id' nor 'name' specified - using generated bean name [" + beanName + "]");
/*      */           
/*      */           }
/*      */         }
/*  484 */         catch (Exception ex) {
/*  485 */           error(ex.getMessage(), ele);
/*  486 */           return null;
/*      */         } 
/*      */       }
/*  489 */       String[] aliasesArray = StringUtils.toStringArray(aliases);
/*  490 */       return new BeanDefinitionHolder((BeanDefinition)beanDefinition, beanName, aliasesArray);
/*      */     } 
/*      */     
/*  493 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkNameUniqueness(String beanName, List<String> aliases, Element beanElement) {
/*  501 */     String foundName = null;
/*      */     
/*  503 */     if (StringUtils.hasText(beanName) && this.usedNames.contains(beanName)) {
/*  504 */       foundName = beanName;
/*      */     }
/*  506 */     if (foundName == null) {
/*  507 */       foundName = (String)CollectionUtils.findFirstMatch(this.usedNames, aliases);
/*      */     }
/*  509 */     if (foundName != null) {
/*  510 */       error("Bean name '" + foundName + "' is already used in this <beans> element", beanElement);
/*      */     }
/*      */     
/*  513 */     this.usedNames.add(beanName);
/*  514 */     this.usedNames.addAll(aliases);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractBeanDefinition parseBeanDefinitionElement(Element ele, String beanName, BeanDefinition containingBean) {
/*  524 */     this.parseState.push((ParseState.Entry)new BeanEntry(beanName));
/*      */     
/*  526 */     String className = null;
/*  527 */     if (ele.hasAttribute("class")) {
/*  528 */       className = ele.getAttribute("class").trim();
/*      */     }
/*      */     
/*      */     try {
/*  532 */       String parent = null;
/*  533 */       if (ele.hasAttribute("parent")) {
/*  534 */         parent = ele.getAttribute("parent");
/*      */       }
/*  536 */       AbstractBeanDefinition bd = createBeanDefinition(className, parent);
/*      */       
/*  538 */       parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
/*  539 */       bd.setDescription(DomUtils.getChildElementValueByTagName(ele, "description"));
/*      */       
/*  541 */       parseMetaElements(ele, (BeanMetadataAttributeAccessor)bd);
/*  542 */       parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
/*  543 */       parseReplacedMethodSubElements(ele, bd.getMethodOverrides());
/*      */       
/*  545 */       parseConstructorArgElements(ele, (BeanDefinition)bd);
/*  546 */       parsePropertyElements(ele, (BeanDefinition)bd);
/*  547 */       parseQualifierElements(ele, bd);
/*      */       
/*  549 */       bd.setResource(this.readerContext.getResource());
/*  550 */       bd.setSource(extractSource(ele));
/*      */       
/*  552 */       return bd;
/*      */     }
/*  554 */     catch (ClassNotFoundException ex) {
/*  555 */       error("Bean class [" + className + "] not found", ele, ex);
/*      */     }
/*  557 */     catch (NoClassDefFoundError err) {
/*  558 */       error("Class that bean class [" + className + "] depends on not found", ele, err);
/*      */     }
/*  560 */     catch (Throwable ex) {
/*  561 */       error("Unexpected failure during bean definition parsing", ele, ex);
/*      */     } finally {
/*      */       
/*  564 */       this.parseState.pop();
/*      */     } 
/*      */     
/*  567 */     return null;
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
/*      */   public AbstractBeanDefinition parseBeanDefinitionAttributes(Element ele, String beanName, BeanDefinition containingBean, AbstractBeanDefinition bd) {
/*  580 */     if (ele.hasAttribute("singleton")) {
/*  581 */       error("Old 1.x 'singleton' attribute in use - upgrade to 'scope' declaration", ele);
/*      */     }
/*  583 */     else if (ele.hasAttribute("scope")) {
/*  584 */       bd.setScope(ele.getAttribute("scope"));
/*      */     }
/*  586 */     else if (containingBean != null) {
/*      */       
/*  588 */       bd.setScope(containingBean.getScope());
/*      */     } 
/*      */     
/*  591 */     if (ele.hasAttribute("abstract")) {
/*  592 */       bd.setAbstract("true".equals(ele.getAttribute("abstract")));
/*      */     }
/*      */     
/*  595 */     String lazyInit = ele.getAttribute("lazy-init");
/*  596 */     if ("default".equals(lazyInit)) {
/*  597 */       lazyInit = this.defaults.getLazyInit();
/*      */     }
/*  599 */     bd.setLazyInit("true".equals(lazyInit));
/*      */     
/*  601 */     String autowire = ele.getAttribute("autowire");
/*  602 */     bd.setAutowireMode(getAutowireMode(autowire));
/*      */     
/*  604 */     String dependencyCheck = ele.getAttribute("dependency-check");
/*  605 */     bd.setDependencyCheck(getDependencyCheck(dependencyCheck));
/*      */     
/*  607 */     if (ele.hasAttribute("depends-on")) {
/*  608 */       String dependsOn = ele.getAttribute("depends-on");
/*  609 */       bd.setDependsOn(StringUtils.tokenizeToStringArray(dependsOn, ",; "));
/*      */     } 
/*      */     
/*  612 */     String autowireCandidate = ele.getAttribute("autowire-candidate");
/*  613 */     if ("".equals(autowireCandidate) || "default".equals(autowireCandidate)) {
/*  614 */       String candidatePattern = this.defaults.getAutowireCandidates();
/*  615 */       if (candidatePattern != null) {
/*  616 */         String[] patterns = StringUtils.commaDelimitedListToStringArray(candidatePattern);
/*  617 */         bd.setAutowireCandidate(PatternMatchUtils.simpleMatch(patterns, beanName));
/*      */       } 
/*      */     } else {
/*      */       
/*  621 */       bd.setAutowireCandidate("true".equals(autowireCandidate));
/*      */     } 
/*      */     
/*  624 */     if (ele.hasAttribute("primary")) {
/*  625 */       bd.setPrimary("true".equals(ele.getAttribute("primary")));
/*      */     }
/*      */     
/*  628 */     if (ele.hasAttribute("init-method")) {
/*  629 */       String initMethodName = ele.getAttribute("init-method");
/*  630 */       if (!"".equals(initMethodName)) {
/*  631 */         bd.setInitMethodName(initMethodName);
/*      */       
/*      */       }
/*      */     }
/*  635 */     else if (this.defaults.getInitMethod() != null) {
/*  636 */       bd.setInitMethodName(this.defaults.getInitMethod());
/*  637 */       bd.setEnforceInitMethod(false);
/*      */     } 
/*      */ 
/*      */     
/*  641 */     if (ele.hasAttribute("destroy-method")) {
/*  642 */       String destroyMethodName = ele.getAttribute("destroy-method");
/*  643 */       bd.setDestroyMethodName(destroyMethodName);
/*      */     
/*      */     }
/*  646 */     else if (this.defaults.getDestroyMethod() != null) {
/*  647 */       bd.setDestroyMethodName(this.defaults.getDestroyMethod());
/*  648 */       bd.setEnforceDestroyMethod(false);
/*      */     } 
/*      */ 
/*      */     
/*  652 */     if (ele.hasAttribute("factory-method")) {
/*  653 */       bd.setFactoryMethodName(ele.getAttribute("factory-method"));
/*      */     }
/*  655 */     if (ele.hasAttribute("factory-bean")) {
/*  656 */       bd.setFactoryBeanName(ele.getAttribute("factory-bean"));
/*      */     }
/*      */     
/*  659 */     return bd;
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
/*      */   protected AbstractBeanDefinition createBeanDefinition(String className, String parentName) throws ClassNotFoundException {
/*  672 */     return BeanDefinitionReaderUtils.createBeanDefinition(parentName, className, this.readerContext
/*  673 */         .getBeanClassLoader());
/*      */   }
/*      */   
/*      */   public void parseMetaElements(Element ele, BeanMetadataAttributeAccessor attributeAccessor) {
/*  677 */     NodeList nl = ele.getChildNodes();
/*  678 */     for (int i = 0; i < nl.getLength(); i++) {
/*  679 */       Node node = nl.item(i);
/*  680 */       if (isCandidateElement(node) && nodeNameEquals(node, "meta")) {
/*  681 */         Element metaElement = (Element)node;
/*  682 */         String key = metaElement.getAttribute("key");
/*  683 */         String value = metaElement.getAttribute("value");
/*  684 */         BeanMetadataAttribute attribute = new BeanMetadataAttribute(key, value);
/*  685 */         attribute.setSource(extractSource(metaElement));
/*  686 */         attributeAccessor.addMetadataAttribute(attribute);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getAutowireMode(String attValue) {
/*  693 */     String att = attValue;
/*  694 */     if ("default".equals(att)) {
/*  695 */       att = this.defaults.getAutowire();
/*      */     }
/*  697 */     int autowire = 0;
/*  698 */     if ("byName".equals(att)) {
/*  699 */       autowire = 1;
/*      */     }
/*  701 */     else if ("byType".equals(att)) {
/*  702 */       autowire = 2;
/*      */     }
/*  704 */     else if ("constructor".equals(att)) {
/*  705 */       autowire = 3;
/*      */     }
/*  707 */     else if ("autodetect".equals(att)) {
/*  708 */       autowire = 4;
/*      */     } 
/*      */     
/*  711 */     return autowire;
/*      */   }
/*      */   
/*      */   public int getDependencyCheck(String attValue) {
/*  715 */     String att = attValue;
/*  716 */     if ("default".equals(att)) {
/*  717 */       att = this.defaults.getDependencyCheck();
/*      */     }
/*  719 */     if ("all".equals(att)) {
/*  720 */       return 3;
/*      */     }
/*  722 */     if ("objects".equals(att)) {
/*  723 */       return 1;
/*      */     }
/*  725 */     if ("simple".equals(att)) {
/*  726 */       return 2;
/*      */     }
/*      */     
/*  729 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseConstructorArgElements(Element beanEle, BeanDefinition bd) {
/*  737 */     NodeList nl = beanEle.getChildNodes();
/*  738 */     for (int i = 0; i < nl.getLength(); i++) {
/*  739 */       Node node = nl.item(i);
/*  740 */       if (isCandidateElement(node) && nodeNameEquals(node, "constructor-arg")) {
/*  741 */         parseConstructorArgElement((Element)node, bd);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parsePropertyElements(Element beanEle, BeanDefinition bd) {
/*  750 */     NodeList nl = beanEle.getChildNodes();
/*  751 */     for (int i = 0; i < nl.getLength(); i++) {
/*  752 */       Node node = nl.item(i);
/*  753 */       if (isCandidateElement(node) && nodeNameEquals(node, "property")) {
/*  754 */         parsePropertyElement((Element)node, bd);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseQualifierElements(Element beanEle, AbstractBeanDefinition bd) {
/*  763 */     NodeList nl = beanEle.getChildNodes();
/*  764 */     for (int i = 0; i < nl.getLength(); i++) {
/*  765 */       Node node = nl.item(i);
/*  766 */       if (isCandidateElement(node) && nodeNameEquals(node, "qualifier")) {
/*  767 */         parseQualifierElement((Element)node, bd);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseLookupOverrideSubElements(Element beanEle, MethodOverrides overrides) {
/*  776 */     NodeList nl = beanEle.getChildNodes();
/*  777 */     for (int i = 0; i < nl.getLength(); i++) {
/*  778 */       Node node = nl.item(i);
/*  779 */       if (isCandidateElement(node) && nodeNameEquals(node, "lookup-method")) {
/*  780 */         Element ele = (Element)node;
/*  781 */         String methodName = ele.getAttribute("name");
/*  782 */         String beanRef = ele.getAttribute("bean");
/*  783 */         LookupOverride override = new LookupOverride(methodName, beanRef);
/*  784 */         override.setSource(extractSource(ele));
/*  785 */         overrides.addOverride((MethodOverride)override);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseReplacedMethodSubElements(Element beanEle, MethodOverrides overrides) {
/*  794 */     NodeList nl = beanEle.getChildNodes();
/*  795 */     for (int i = 0; i < nl.getLength(); i++) {
/*  796 */       Node node = nl.item(i);
/*  797 */       if (isCandidateElement(node) && nodeNameEquals(node, "replaced-method")) {
/*  798 */         Element replacedMethodEle = (Element)node;
/*  799 */         String name = replacedMethodEle.getAttribute("name");
/*  800 */         String callback = replacedMethodEle.getAttribute("replacer");
/*  801 */         ReplaceOverride replaceOverride = new ReplaceOverride(name, callback);
/*      */         
/*  803 */         List<Element> argTypeEles = DomUtils.getChildElementsByTagName(replacedMethodEle, "arg-type");
/*  804 */         for (Element argTypeEle : argTypeEles) {
/*  805 */           String match = argTypeEle.getAttribute("match");
/*  806 */           match = StringUtils.hasText(match) ? match : DomUtils.getTextValue(argTypeEle);
/*  807 */           if (StringUtils.hasText(match)) {
/*  808 */             replaceOverride.addTypeIdentifier(match);
/*      */           }
/*      */         } 
/*  811 */         replaceOverride.setSource(extractSource(replacedMethodEle));
/*  812 */         overrides.addOverride((MethodOverride)replaceOverride);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseConstructorArgElement(Element ele, BeanDefinition bd) {
/*  821 */     String indexAttr = ele.getAttribute("index");
/*  822 */     String typeAttr = ele.getAttribute("type");
/*  823 */     String nameAttr = ele.getAttribute("name");
/*  824 */     if (StringUtils.hasLength(indexAttr)) {
/*      */       try {
/*  826 */         int index = Integer.parseInt(indexAttr);
/*  827 */         if (index < 0) {
/*  828 */           error("'index' cannot be lower than 0", ele);
/*      */         } else {
/*      */           
/*      */           try {
/*  832 */             this.parseState.push((ParseState.Entry)new ConstructorArgumentEntry(index));
/*  833 */             Object value = parsePropertyValue(ele, bd, null);
/*  834 */             ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(value);
/*  835 */             if (StringUtils.hasLength(typeAttr)) {
/*  836 */               valueHolder.setType(typeAttr);
/*      */             }
/*  838 */             if (StringUtils.hasLength(nameAttr)) {
/*  839 */               valueHolder.setName(nameAttr);
/*      */             }
/*  841 */             valueHolder.setSource(extractSource(ele));
/*  842 */             if (bd.getConstructorArgumentValues().hasIndexedArgumentValue(index)) {
/*  843 */               error("Ambiguous constructor-arg entries for index " + index, ele);
/*      */             } else {
/*      */               
/*  846 */               bd.getConstructorArgumentValues().addIndexedArgumentValue(index, valueHolder);
/*      */             } 
/*      */           } finally {
/*      */             
/*  850 */             this.parseState.pop();
/*      */           }
/*      */         
/*      */         } 
/*  854 */       } catch (NumberFormatException ex) {
/*  855 */         error("Attribute 'index' of tag 'constructor-arg' must be an integer", ele);
/*      */       } 
/*      */     } else {
/*      */       
/*      */       try {
/*  860 */         this.parseState.push((ParseState.Entry)new ConstructorArgumentEntry());
/*  861 */         Object value = parsePropertyValue(ele, bd, null);
/*  862 */         ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(value);
/*  863 */         if (StringUtils.hasLength(typeAttr)) {
/*  864 */           valueHolder.setType(typeAttr);
/*      */         }
/*  866 */         if (StringUtils.hasLength(nameAttr)) {
/*  867 */           valueHolder.setName(nameAttr);
/*      */         }
/*  869 */         valueHolder.setSource(extractSource(ele));
/*  870 */         bd.getConstructorArgumentValues().addGenericArgumentValue(valueHolder);
/*      */       } finally {
/*      */         
/*  873 */         this.parseState.pop();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parsePropertyElement(Element ele, BeanDefinition bd) {
/*  882 */     String propertyName = ele.getAttribute("name");
/*  883 */     if (!StringUtils.hasLength(propertyName)) {
/*  884 */       error("Tag 'property' must have a 'name' attribute", ele);
/*      */       return;
/*      */     } 
/*  887 */     this.parseState.push((ParseState.Entry)new PropertyEntry(propertyName));
/*      */     try {
/*  889 */       if (bd.getPropertyValues().contains(propertyName)) {
/*  890 */         error("Multiple 'property' definitions for property '" + propertyName + "'", ele);
/*      */         return;
/*      */       } 
/*  893 */       Object val = parsePropertyValue(ele, bd, propertyName);
/*  894 */       PropertyValue pv = new PropertyValue(propertyName, val);
/*  895 */       parseMetaElements(ele, (BeanMetadataAttributeAccessor)pv);
/*  896 */       pv.setSource(extractSource(ele));
/*  897 */       bd.getPropertyValues().addPropertyValue(pv);
/*      */     } finally {
/*      */       
/*  900 */       this.parseState.pop();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseQualifierElement(Element ele, AbstractBeanDefinition bd) {
/*  908 */     String typeName = ele.getAttribute("type");
/*  909 */     if (!StringUtils.hasLength(typeName)) {
/*  910 */       error("Tag 'qualifier' must have a 'type' attribute", ele);
/*      */       return;
/*      */     } 
/*  913 */     this.parseState.push((ParseState.Entry)new QualifierEntry(typeName));
/*      */     try {
/*  915 */       AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(typeName);
/*  916 */       qualifier.setSource(extractSource(ele));
/*  917 */       String value = ele.getAttribute("value");
/*  918 */       if (StringUtils.hasLength(value)) {
/*  919 */         qualifier.setAttribute("value", value);
/*      */       }
/*  921 */       NodeList nl = ele.getChildNodes();
/*  922 */       for (int i = 0; i < nl.getLength(); i++) {
/*  923 */         Node node = nl.item(i);
/*  924 */         if (isCandidateElement(node) && nodeNameEquals(node, "attribute")) {
/*  925 */           Element attributeEle = (Element)node;
/*  926 */           String attributeName = attributeEle.getAttribute("key");
/*  927 */           String attributeValue = attributeEle.getAttribute("value");
/*  928 */           if (StringUtils.hasLength(attributeName) && StringUtils.hasLength(attributeValue)) {
/*  929 */             BeanMetadataAttribute attribute = new BeanMetadataAttribute(attributeName, attributeValue);
/*  930 */             attribute.setSource(extractSource(attributeEle));
/*  931 */             qualifier.addMetadataAttribute(attribute);
/*      */           } else {
/*      */             
/*  934 */             error("Qualifier 'attribute' tag must have a 'name' and 'value'", attributeEle);
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       } 
/*  939 */       bd.addQualifier(qualifier);
/*      */     } finally {
/*      */       
/*  942 */       this.parseState.pop();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object parsePropertyValue(Element ele, BeanDefinition bd, String propertyName) {
/*  951 */     String elementName = (propertyName != null) ? ("<property> element for property '" + propertyName + "'") : "<constructor-arg> element";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  956 */     NodeList nl = ele.getChildNodes();
/*  957 */     Element subElement = null;
/*  958 */     for (int i = 0; i < nl.getLength(); i++) {
/*  959 */       Node node = nl.item(i);
/*  960 */       if (node instanceof Element && !nodeNameEquals(node, "description") && 
/*  961 */         !nodeNameEquals(node, "meta"))
/*      */       {
/*  963 */         if (subElement != null) {
/*  964 */           error(elementName + " must not contain more than one sub-element", ele);
/*      */         } else {
/*      */           
/*  967 */           subElement = (Element)node;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  972 */     boolean hasRefAttribute = ele.hasAttribute("ref");
/*  973 */     boolean hasValueAttribute = ele.hasAttribute("value");
/*  974 */     if ((hasRefAttribute && hasValueAttribute) || ((hasRefAttribute || hasValueAttribute) && subElement != null))
/*      */     {
/*  976 */       error(elementName + " is only allowed to contain either 'ref' attribute OR 'value' attribute OR sub-element", ele);
/*      */     }
/*      */ 
/*      */     
/*  980 */     if (hasRefAttribute) {
/*  981 */       String refName = ele.getAttribute("ref");
/*  982 */       if (!StringUtils.hasText(refName)) {
/*  983 */         error(elementName + " contains empty 'ref' attribute", ele);
/*      */       }
/*  985 */       RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/*  986 */       ref.setSource(extractSource(ele));
/*  987 */       return ref;
/*      */     } 
/*  989 */     if (hasValueAttribute) {
/*  990 */       TypedStringValue valueHolder = new TypedStringValue(ele.getAttribute("value"));
/*  991 */       valueHolder.setSource(extractSource(ele));
/*  992 */       return valueHolder;
/*      */     } 
/*  994 */     if (subElement != null) {
/*  995 */       return parsePropertySubElement(subElement, bd);
/*      */     }
/*      */ 
/*      */     
/*  999 */     error(elementName + " must specify a ref or value", ele);
/* 1000 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object parsePropertySubElement(Element ele, BeanDefinition bd) {
/* 1005 */     return parsePropertySubElement(ele, bd, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object parsePropertySubElement(Element ele, BeanDefinition bd, String defaultValueType) {
/* 1016 */     if (!isDefaultNamespace(ele)) {
/* 1017 */       return parseNestedCustomElement(ele, bd);
/*      */     }
/* 1019 */     if (nodeNameEquals(ele, "bean")) {
/* 1020 */       BeanDefinitionHolder nestedBd = parseBeanDefinitionElement(ele, bd);
/* 1021 */       if (nestedBd != null) {
/* 1022 */         nestedBd = decorateBeanDefinitionIfRequired(ele, nestedBd, bd);
/*      */       }
/* 1024 */       return nestedBd;
/*      */     } 
/* 1026 */     if (nodeNameEquals(ele, "ref")) {
/*      */       
/* 1028 */       String refName = ele.getAttribute("bean");
/* 1029 */       boolean toParent = false;
/* 1030 */       if (!StringUtils.hasLength(refName)) {
/*      */         
/* 1032 */         refName = ele.getAttribute("local");
/* 1033 */         if (!StringUtils.hasLength(refName)) {
/*      */           
/* 1035 */           refName = ele.getAttribute("parent");
/* 1036 */           toParent = true;
/* 1037 */           if (!StringUtils.hasLength(refName)) {
/* 1038 */             error("'bean', 'local' or 'parent' is required for <ref> element", ele);
/* 1039 */             return null;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1043 */       if (!StringUtils.hasText(refName)) {
/* 1044 */         error("<ref> element contains empty target attribute", ele);
/* 1045 */         return null;
/*      */       } 
/* 1047 */       RuntimeBeanReference ref = new RuntimeBeanReference(refName, toParent);
/* 1048 */       ref.setSource(extractSource(ele));
/* 1049 */       return ref;
/*      */     } 
/* 1051 */     if (nodeNameEquals(ele, "idref")) {
/* 1052 */       return parseIdRefElement(ele);
/*      */     }
/* 1054 */     if (nodeNameEquals(ele, "value")) {
/* 1055 */       return parseValueElement(ele, defaultValueType);
/*      */     }
/* 1057 */     if (nodeNameEquals(ele, "null")) {
/*      */ 
/*      */       
/* 1060 */       TypedStringValue nullHolder = new TypedStringValue(null);
/* 1061 */       nullHolder.setSource(extractSource(ele));
/* 1062 */       return nullHolder;
/*      */     } 
/* 1064 */     if (nodeNameEquals(ele, "array")) {
/* 1065 */       return parseArrayElement(ele, bd);
/*      */     }
/* 1067 */     if (nodeNameEquals(ele, "list")) {
/* 1068 */       return parseListElement(ele, bd);
/*      */     }
/* 1070 */     if (nodeNameEquals(ele, "set")) {
/* 1071 */       return parseSetElement(ele, bd);
/*      */     }
/* 1073 */     if (nodeNameEquals(ele, "map")) {
/* 1074 */       return parseMapElement(ele, bd);
/*      */     }
/* 1076 */     if (nodeNameEquals(ele, "props")) {
/* 1077 */       return parsePropsElement(ele);
/*      */     }
/*      */     
/* 1080 */     error("Unknown property sub-element: [" + ele.getNodeName() + "]", ele);
/* 1081 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object parseIdRefElement(Element ele) {
/* 1090 */     String refName = ele.getAttribute("bean");
/* 1091 */     if (!StringUtils.hasLength(refName)) {
/*      */       
/* 1093 */       refName = ele.getAttribute("local");
/* 1094 */       if (!StringUtils.hasLength(refName)) {
/* 1095 */         error("Either 'bean' or 'local' is required for <idref> element", ele);
/* 1096 */         return null;
/*      */       } 
/*      */     } 
/* 1099 */     if (!StringUtils.hasText(refName)) {
/* 1100 */       error("<idref> element contains empty target attribute", ele);
/* 1101 */       return null;
/*      */     } 
/* 1103 */     RuntimeBeanNameReference ref = new RuntimeBeanNameReference(refName);
/* 1104 */     ref.setSource(extractSource(ele));
/* 1105 */     return ref;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object parseValueElement(Element ele, String defaultTypeName) {
/* 1113 */     String value = DomUtils.getTextValue(ele);
/* 1114 */     String specifiedTypeName = ele.getAttribute("type");
/* 1115 */     String typeName = specifiedTypeName;
/* 1116 */     if (!StringUtils.hasText(typeName)) {
/* 1117 */       typeName = defaultTypeName;
/*      */     }
/*      */     try {
/* 1120 */       TypedStringValue typedValue = buildTypedStringValue(value, typeName);
/* 1121 */       typedValue.setSource(extractSource(ele));
/* 1122 */       typedValue.setSpecifiedTypeName(specifiedTypeName);
/* 1123 */       return typedValue;
/*      */     }
/* 1125 */     catch (ClassNotFoundException ex) {
/* 1126 */       error("Type class [" + typeName + "] not found for <value> element", ele, ex);
/* 1127 */       return value;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypedStringValue buildTypedStringValue(String value, String targetTypeName) throws ClassNotFoundException {
/*      */     TypedStringValue typedValue;
/* 1138 */     ClassLoader classLoader = this.readerContext.getBeanClassLoader();
/*      */     
/* 1140 */     if (!StringUtils.hasText(targetTypeName)) {
/* 1141 */       typedValue = new TypedStringValue(value);
/*      */     }
/* 1143 */     else if (classLoader != null) {
/* 1144 */       Class<?> targetType = ClassUtils.forName(targetTypeName, classLoader);
/* 1145 */       typedValue = new TypedStringValue(value, targetType);
/*      */     } else {
/*      */       
/* 1148 */       typedValue = new TypedStringValue(value, targetTypeName);
/*      */     } 
/* 1150 */     return typedValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object parseArrayElement(Element arrayEle, BeanDefinition bd) {
/* 1157 */     String elementType = arrayEle.getAttribute("value-type");
/* 1158 */     NodeList nl = arrayEle.getChildNodes();
/* 1159 */     ManagedArray target = new ManagedArray(elementType, nl.getLength());
/* 1160 */     target.setSource(extractSource(arrayEle));
/* 1161 */     target.setElementTypeName(elementType);
/* 1162 */     target.setMergeEnabled(parseMergeAttribute(arrayEle));
/* 1163 */     parseCollectionElements(nl, (Collection<Object>)target, bd, elementType);
/* 1164 */     return target;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Object> parseListElement(Element collectionEle, BeanDefinition bd) {
/* 1171 */     String defaultElementType = collectionEle.getAttribute("value-type");
/* 1172 */     NodeList nl = collectionEle.getChildNodes();
/* 1173 */     ManagedList<Object> target = new ManagedList(nl.getLength());
/* 1174 */     target.setSource(extractSource(collectionEle));
/* 1175 */     target.setElementTypeName(defaultElementType);
/* 1176 */     target.setMergeEnabled(parseMergeAttribute(collectionEle));
/* 1177 */     parseCollectionElements(nl, (Collection<Object>)target, bd, defaultElementType);
/* 1178 */     return (List<Object>)target;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Object> parseSetElement(Element collectionEle, BeanDefinition bd) {
/* 1185 */     String defaultElementType = collectionEle.getAttribute("value-type");
/* 1186 */     NodeList nl = collectionEle.getChildNodes();
/* 1187 */     ManagedSet<Object> target = new ManagedSet(nl.getLength());
/* 1188 */     target.setSource(extractSource(collectionEle));
/* 1189 */     target.setElementTypeName(defaultElementType);
/* 1190 */     target.setMergeEnabled(parseMergeAttribute(collectionEle));
/* 1191 */     parseCollectionElements(nl, (Collection<Object>)target, bd, defaultElementType);
/* 1192 */     return (Set<Object>)target;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseCollectionElements(NodeList elementNodes, Collection<Object> target, BeanDefinition bd, String defaultElementType) {
/* 1198 */     for (int i = 0; i < elementNodes.getLength(); i++) {
/* 1199 */       Node node = elementNodes.item(i);
/* 1200 */       if (node instanceof Element && !nodeNameEquals(node, "description")) {
/* 1201 */         target.add(parsePropertySubElement((Element)node, bd, defaultElementType));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Object, Object> parseMapElement(Element mapEle, BeanDefinition bd) {
/* 1210 */     String defaultKeyType = mapEle.getAttribute("key-type");
/* 1211 */     String defaultValueType = mapEle.getAttribute("value-type");
/*      */     
/* 1213 */     List<Element> entryEles = DomUtils.getChildElementsByTagName(mapEle, "entry");
/* 1214 */     ManagedMap<Object, Object> map = new ManagedMap(entryEles.size());
/* 1215 */     map.setSource(extractSource(mapEle));
/* 1216 */     map.setKeyTypeName(defaultKeyType);
/* 1217 */     map.setValueTypeName(defaultValueType);
/* 1218 */     map.setMergeEnabled(parseMergeAttribute(mapEle));
/*      */     
/* 1220 */     for (Element entryEle : entryEles) {
/*      */ 
/*      */       
/* 1223 */       NodeList entrySubNodes = entryEle.getChildNodes();
/* 1224 */       Element keyEle = null;
/* 1225 */       Element valueEle = null;
/* 1226 */       for (int j = 0; j < entrySubNodes.getLength(); j++) {
/* 1227 */         Node node = entrySubNodes.item(j);
/* 1228 */         if (node instanceof Element) {
/* 1229 */           Element candidateEle = (Element)node;
/* 1230 */           if (nodeNameEquals(candidateEle, "key")) {
/* 1231 */             if (keyEle != null) {
/* 1232 */               error("<entry> element is only allowed to contain one <key> sub-element", entryEle);
/*      */             } else {
/*      */               
/* 1235 */               keyEle = candidateEle;
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 1240 */           else if (!nodeNameEquals(candidateEle, "description")) {
/*      */ 
/*      */             
/* 1243 */             if (valueEle != null) {
/* 1244 */               error("<entry> element must not contain more than one value sub-element", entryEle);
/*      */             } else {
/*      */               
/* 1247 */               valueEle = candidateEle;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1254 */       Object key = null;
/* 1255 */       boolean hasKeyAttribute = entryEle.hasAttribute("key");
/* 1256 */       boolean hasKeyRefAttribute = entryEle.hasAttribute("key-ref");
/* 1257 */       if ((hasKeyAttribute && hasKeyRefAttribute) || ((hasKeyAttribute || hasKeyRefAttribute) && keyEle != null))
/*      */       {
/* 1259 */         error("<entry> element is only allowed to contain either a 'key' attribute OR a 'key-ref' attribute OR a <key> sub-element", entryEle);
/*      */       }
/*      */       
/* 1262 */       if (hasKeyAttribute) {
/* 1263 */         key = buildTypedStringValueForMap(entryEle.getAttribute("key"), defaultKeyType, entryEle);
/*      */       }
/* 1265 */       else if (hasKeyRefAttribute) {
/* 1266 */         String refName = entryEle.getAttribute("key-ref");
/* 1267 */         if (!StringUtils.hasText(refName)) {
/* 1268 */           error("<entry> element contains empty 'key-ref' attribute", entryEle);
/*      */         }
/* 1270 */         RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/* 1271 */         ref.setSource(extractSource(entryEle));
/* 1272 */         key = ref;
/*      */       }
/* 1274 */       else if (keyEle != null) {
/* 1275 */         key = parseKeyElement(keyEle, bd, defaultKeyType);
/*      */       } else {
/*      */         
/* 1278 */         error("<entry> element must specify a key", entryEle);
/*      */       } 
/*      */ 
/*      */       
/* 1282 */       Object value = null;
/* 1283 */       boolean hasValueAttribute = entryEle.hasAttribute("value");
/* 1284 */       boolean hasValueRefAttribute = entryEle.hasAttribute("value-ref");
/* 1285 */       boolean hasValueTypeAttribute = entryEle.hasAttribute("value-type");
/* 1286 */       if ((hasValueAttribute && hasValueRefAttribute) || ((hasValueAttribute || hasValueRefAttribute) && valueEle != null))
/*      */       {
/* 1288 */         error("<entry> element is only allowed to contain either 'value' attribute OR 'value-ref' attribute OR <value> sub-element", entryEle);
/*      */       }
/*      */       
/* 1291 */       if ((hasValueTypeAttribute && hasValueRefAttribute) || (hasValueTypeAttribute && !hasValueAttribute) || (hasValueTypeAttribute && valueEle != null))
/*      */       {
/*      */         
/* 1294 */         error("<entry> element is only allowed to contain a 'value-type' attribute when it has a 'value' attribute", entryEle);
/*      */       }
/*      */       
/* 1297 */       if (hasValueAttribute) {
/* 1298 */         String valueType = entryEle.getAttribute("value-type");
/* 1299 */         if (!StringUtils.hasText(valueType)) {
/* 1300 */           valueType = defaultValueType;
/*      */         }
/* 1302 */         value = buildTypedStringValueForMap(entryEle.getAttribute("value"), valueType, entryEle);
/*      */       }
/* 1304 */       else if (hasValueRefAttribute) {
/* 1305 */         String refName = entryEle.getAttribute("value-ref");
/* 1306 */         if (!StringUtils.hasText(refName)) {
/* 1307 */           error("<entry> element contains empty 'value-ref' attribute", entryEle);
/*      */         }
/* 1309 */         RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/* 1310 */         ref.setSource(extractSource(entryEle));
/* 1311 */         value = ref;
/*      */       }
/* 1313 */       else if (valueEle != null) {
/* 1314 */         value = parsePropertySubElement(valueEle, bd, defaultValueType);
/*      */       } else {
/*      */         
/* 1317 */         error("<entry> element must specify a value", entryEle);
/*      */       } 
/*      */ 
/*      */       
/* 1321 */       map.put(key, value);
/*      */     } 
/*      */     
/* 1324 */     return (Map<Object, Object>)map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object buildTypedStringValueForMap(String value, String defaultTypeName, Element entryEle) {
/*      */     try {
/* 1333 */       TypedStringValue typedValue = buildTypedStringValue(value, defaultTypeName);
/* 1334 */       typedValue.setSource(extractSource(entryEle));
/* 1335 */       return typedValue;
/*      */     }
/* 1337 */     catch (ClassNotFoundException ex) {
/* 1338 */       error("Type class [" + defaultTypeName + "] not found for Map key/value type", entryEle, ex);
/* 1339 */       return value;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object parseKeyElement(Element keyEle, BeanDefinition bd, String defaultKeyTypeName) {
/* 1347 */     NodeList nl = keyEle.getChildNodes();
/* 1348 */     Element subElement = null;
/* 1349 */     for (int i = 0; i < nl.getLength(); i++) {
/* 1350 */       Node node = nl.item(i);
/* 1351 */       if (node instanceof Element)
/*      */       {
/* 1353 */         if (subElement != null) {
/* 1354 */           error("<key> element must not contain more than one value sub-element", keyEle);
/*      */         } else {
/*      */           
/* 1357 */           subElement = (Element)node;
/*      */         } 
/*      */       }
/*      */     } 
/* 1361 */     return parsePropertySubElement(subElement, bd, defaultKeyTypeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Properties parsePropsElement(Element propsEle) {
/* 1368 */     ManagedProperties props = new ManagedProperties();
/* 1369 */     props.setSource(extractSource(propsEle));
/* 1370 */     props.setMergeEnabled(parseMergeAttribute(propsEle));
/*      */     
/* 1372 */     List<Element> propEles = DomUtils.getChildElementsByTagName(propsEle, "prop");
/* 1373 */     for (Element propEle : propEles) {
/* 1374 */       String key = propEle.getAttribute("key");
/*      */ 
/*      */       
/* 1377 */       String value = DomUtils.getTextValue(propEle).trim();
/* 1378 */       TypedStringValue keyHolder = new TypedStringValue(key);
/* 1379 */       keyHolder.setSource(extractSource(propEle));
/* 1380 */       TypedStringValue valueHolder = new TypedStringValue(value);
/* 1381 */       valueHolder.setSource(extractSource(propEle));
/* 1382 */       props.put(keyHolder, valueHolder);
/*      */     } 
/*      */     
/* 1385 */     return (Properties)props;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean parseMergeAttribute(Element collectionElement) {
/* 1392 */     String value = collectionElement.getAttribute("merge");
/* 1393 */     if ("default".equals(value)) {
/* 1394 */       value = this.defaults.getMerge();
/*      */     }
/* 1396 */     return "true".equals(value);
/*      */   }
/*      */   
/*      */   public BeanDefinition parseCustomElement(Element ele) {
/* 1400 */     return parseCustomElement(ele, null);
/*      */   }
/*      */   
/*      */   public BeanDefinition parseCustomElement(Element ele, BeanDefinition containingBd) {
/* 1404 */     String namespaceUri = getNamespaceURI(ele);
/* 1405 */     NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
/* 1406 */     if (handler == null) {
/* 1407 */       error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);
/* 1408 */       return null;
/*      */     } 
/* 1410 */     return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
/*      */   }
/*      */   
/*      */   public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder) {
/* 1414 */     return decorateBeanDefinitionIfRequired(ele, definitionHolder, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder, BeanDefinition containingBd) {
/* 1420 */     BeanDefinitionHolder finalDefinition = definitionHolder;
/*      */ 
/*      */     
/* 1423 */     NamedNodeMap attributes = ele.getAttributes();
/* 1424 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 1425 */       Node node = attributes.item(i);
/* 1426 */       finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
/*      */     } 
/*      */ 
/*      */     
/* 1430 */     NodeList children = ele.getChildNodes();
/* 1431 */     for (int j = 0; j < children.getLength(); j++) {
/* 1432 */       Node node = children.item(j);
/* 1433 */       if (node.getNodeType() == 1) {
/* 1434 */         finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
/*      */       }
/*      */     } 
/* 1437 */     return finalDefinition;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionHolder decorateIfRequired(Node node, BeanDefinitionHolder originalDef, BeanDefinition containingBd) {
/* 1443 */     String namespaceUri = getNamespaceURI(node);
/* 1444 */     if (!isDefaultNamespace(namespaceUri)) {
/* 1445 */       NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
/* 1446 */       if (handler != null) {
/* 1447 */         return handler.decorate(node, originalDef, new ParserContext(this.readerContext, this, containingBd));
/*      */       }
/* 1449 */       if (namespaceUri != null && namespaceUri.startsWith("http://www.springframework.org/")) {
/* 1450 */         error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", node);
/*      */ 
/*      */       
/*      */       }
/* 1454 */       else if (this.logger.isDebugEnabled()) {
/* 1455 */         this.logger.debug("No Spring NamespaceHandler found for XML schema namespace [" + namespaceUri + "]");
/*      */       } 
/*      */     } 
/*      */     
/* 1459 */     return originalDef;
/*      */   }
/*      */   
/*      */   private BeanDefinitionHolder parseNestedCustomElement(Element ele, BeanDefinition containingBd) {
/* 1463 */     BeanDefinition innerDefinition = parseCustomElement(ele, containingBd);
/* 1464 */     if (innerDefinition == null) {
/* 1465 */       error("Incorrect usage of element '" + ele.getNodeName() + "' in a nested manner. This tag cannot be used nested inside <property>.", ele);
/*      */       
/* 1467 */       return null;
/*      */     } 
/*      */     
/* 1470 */     String id = ele.getNodeName() + "#" + ObjectUtils.getIdentityHexString(innerDefinition);
/* 1471 */     if (this.logger.isDebugEnabled()) {
/* 1472 */       this.logger.debug("Using generated bean name [" + id + "] for nested custom element '" + ele
/* 1473 */           .getNodeName() + "'");
/*      */     }
/* 1475 */     return new BeanDefinitionHolder(innerDefinition, id);
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
/*      */   public String getNamespaceURI(Node node) {
/* 1487 */     return node.getNamespaceURI();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLocalName(Node node) {
/* 1498 */     return node.getLocalName();
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
/*      */   public boolean nodeNameEquals(Node node, String desiredName) {
/* 1511 */     return (desiredName.equals(node.getNodeName()) || desiredName.equals(getLocalName(node)));
/*      */   }
/*      */   
/*      */   public boolean isDefaultNamespace(String namespaceUri) {
/* 1515 */     return (!StringUtils.hasLength(namespaceUri) || "http://www.springframework.org/schema/beans".equals(namespaceUri));
/*      */   }
/*      */   
/*      */   public boolean isDefaultNamespace(Node node) {
/* 1519 */     return isDefaultNamespace(getNamespaceURI(node));
/*      */   }
/*      */   
/*      */   private boolean isCandidateElement(Node node) {
/* 1523 */     return (node instanceof Element && (isDefaultNamespace(node) || !isDefaultNamespace(node.getParentNode())));
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\BeanDefinitionParserDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */