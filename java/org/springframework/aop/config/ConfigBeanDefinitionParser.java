/*     */ package org.springframework.aop.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.aop.aspectj.AspectJAfterAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAroundAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJExpressionPointcut;
/*     */ import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
/*     */ import org.springframework.aop.aspectj.DeclareParentsAdvisor;
/*     */ import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanReference;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanNameReference;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ParseState;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.DomUtils;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ConfigBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String ASPECT = "aspect";
/*     */   private static final String EXPRESSION = "expression";
/*     */   private static final String ID = "id";
/*     */   private static final String POINTCUT = "pointcut";
/*     */   private static final String ADVICE_BEAN_NAME = "adviceBeanName";
/*     */   private static final String ADVISOR = "advisor";
/*     */   private static final String ADVICE_REF = "advice-ref";
/*     */   private static final String POINTCUT_REF = "pointcut-ref";
/*     */   private static final String REF = "ref";
/*     */   private static final String BEFORE = "before";
/*     */   private static final String DECLARE_PARENTS = "declare-parents";
/*     */   private static final String TYPE_PATTERN = "types-matching";
/*     */   private static final String DEFAULT_IMPL = "default-impl";
/*     */   private static final String DELEGATE_REF = "delegate-ref";
/*     */   private static final String IMPLEMENT_INTERFACE = "implement-interface";
/*     */   private static final String AFTER = "after";
/*     */   private static final String AFTER_RETURNING_ELEMENT = "after-returning";
/*     */   private static final String AFTER_THROWING_ELEMENT = "after-throwing";
/*     */   private static final String AROUND = "around";
/*     */   private static final String RETURNING = "returning";
/*     */   private static final String RETURNING_PROPERTY = "returningName";
/*     */   private static final String THROWING = "throwing";
/*     */   private static final String THROWING_PROPERTY = "throwingName";
/*     */   private static final String ARG_NAMES = "arg-names";
/*     */   private static final String ARG_NAMES_PROPERTY = "argumentNames";
/*     */   private static final String ASPECT_NAME_PROPERTY = "aspectName";
/*     */   private static final String DECLARATION_ORDER_PROPERTY = "declarationOrder";
/*     */   private static final String ORDER_PROPERTY = "order";
/*     */   private static final int METHOD_INDEX = 0;
/*     */   private static final int POINTCUT_INDEX = 1;
/*     */   private static final int ASPECT_INSTANCE_FACTORY_INDEX = 2;
/*  95 */   private ParseState parseState = new ParseState();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 101 */     CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), parserContext.extractSource(element));
/* 102 */     parserContext.pushContainingComponent(compositeDef);
/*     */     
/* 104 */     configureAutoProxyCreator(parserContext, element);
/*     */     
/* 106 */     List<Element> childElts = DomUtils.getChildElements(element);
/* 107 */     for (Element elt : childElts) {
/* 108 */       String localName = parserContext.getDelegate().getLocalName(elt);
/* 109 */       if ("pointcut".equals(localName)) {
/* 110 */         parsePointcut(elt, parserContext); continue;
/*     */       } 
/* 112 */       if ("advisor".equals(localName)) {
/* 113 */         parseAdvisor(elt, parserContext); continue;
/*     */       } 
/* 115 */       if ("aspect".equals(localName)) {
/* 116 */         parseAspect(elt, parserContext);
/*     */       }
/*     */     } 
/*     */     
/* 120 */     parserContext.popAndRegisterContainingComponent();
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void configureAutoProxyCreator(ParserContext parserContext, Element element) {
/* 131 */     AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(parserContext, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseAdvisor(Element advisorElement, ParserContext parserContext) {
/* 140 */     AbstractBeanDefinition advisorDef = createAdvisorBeanDefinition(advisorElement, parserContext);
/* 141 */     String id = advisorElement.getAttribute("id");
/*     */     
/*     */     try {
/* 144 */       this.parseState.push(new AdvisorEntry(id));
/* 145 */       String advisorBeanName = id;
/* 146 */       if (StringUtils.hasText(advisorBeanName)) {
/* 147 */         parserContext.getRegistry().registerBeanDefinition(advisorBeanName, (BeanDefinition)advisorDef);
/*     */       } else {
/*     */         
/* 150 */         advisorBeanName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)advisorDef);
/*     */       } 
/*     */       
/* 153 */       Object pointcut = parsePointcutProperty(advisorElement, parserContext);
/* 154 */       if (pointcut instanceof BeanDefinition) {
/* 155 */         advisorDef.getPropertyValues().add("pointcut", pointcut);
/* 156 */         parserContext.registerComponent((ComponentDefinition)new AdvisorComponentDefinition(advisorBeanName, (BeanDefinition)advisorDef, (BeanDefinition)pointcut));
/*     */       
/*     */       }
/* 159 */       else if (pointcut instanceof String) {
/* 160 */         advisorDef.getPropertyValues().add("pointcut", new RuntimeBeanReference((String)pointcut));
/* 161 */         parserContext.registerComponent((ComponentDefinition)new AdvisorComponentDefinition(advisorBeanName, (BeanDefinition)advisorDef));
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 166 */       this.parseState.pop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractBeanDefinition createAdvisorBeanDefinition(Element advisorElement, ParserContext parserContext) {
/* 175 */     RootBeanDefinition advisorDefinition = new RootBeanDefinition(DefaultBeanFactoryPointcutAdvisor.class);
/* 176 */     advisorDefinition.setSource(parserContext.extractSource(advisorElement));
/*     */     
/* 178 */     String adviceRef = advisorElement.getAttribute("advice-ref");
/* 179 */     if (!StringUtils.hasText(adviceRef)) {
/* 180 */       parserContext.getReaderContext().error("'advice-ref' attribute contains empty value.", advisorElement, this.parseState
/* 181 */           .snapshot());
/*     */     } else {
/*     */       
/* 184 */       advisorDefinition.getPropertyValues().add("adviceBeanName", new RuntimeBeanNameReference(adviceRef));
/*     */     } 
/*     */ 
/*     */     
/* 188 */     if (advisorElement.hasAttribute("order")) {
/* 189 */       advisorDefinition.getPropertyValues().add("order", advisorElement
/* 190 */           .getAttribute("order"));
/*     */     }
/*     */     
/* 193 */     return (AbstractBeanDefinition)advisorDefinition;
/*     */   }
/*     */   
/*     */   private void parseAspect(Element aspectElement, ParserContext parserContext) {
/* 197 */     String aspectId = aspectElement.getAttribute("id");
/* 198 */     String aspectName = aspectElement.getAttribute("ref");
/*     */     
/*     */     try {
/* 201 */       this.parseState.push(new AspectEntry(aspectId, aspectName));
/* 202 */       List<BeanDefinition> beanDefinitions = new ArrayList<BeanDefinition>();
/* 203 */       List<BeanReference> beanReferences = new ArrayList<BeanReference>();
/*     */       
/* 205 */       List<Element> declareParents = DomUtils.getChildElementsByTagName(aspectElement, "declare-parents");
/* 206 */       for (int i = 0; i < declareParents.size(); i++) {
/* 207 */         Element declareParentsElement = declareParents.get(i);
/* 208 */         beanDefinitions.add(parseDeclareParents(declareParentsElement, parserContext));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 213 */       NodeList nodeList = aspectElement.getChildNodes();
/* 214 */       boolean adviceFoundAlready = false;
/* 215 */       for (int j = 0; j < nodeList.getLength(); j++) {
/* 216 */         Node node = nodeList.item(j);
/* 217 */         if (isAdviceNode(node, parserContext)) {
/* 218 */           if (!adviceFoundAlready) {
/* 219 */             adviceFoundAlready = true;
/* 220 */             if (!StringUtils.hasText(aspectName)) {
/* 221 */               parserContext.getReaderContext().error("<aspect> tag needs aspect bean reference via 'ref' attribute when declaring advices.", aspectElement, this.parseState
/*     */                   
/* 223 */                   .snapshot());
/*     */               return;
/*     */             } 
/* 226 */             beanReferences.add(new RuntimeBeanReference(aspectName));
/*     */           } 
/* 228 */           AbstractBeanDefinition advisorDefinition = parseAdvice(aspectName, j, aspectElement, (Element)node, parserContext, beanDefinitions, beanReferences);
/*     */           
/* 230 */           beanDefinitions.add(advisorDefinition);
/*     */         } 
/*     */       } 
/*     */       
/* 234 */       AspectComponentDefinition aspectComponentDefinition = createAspectComponentDefinition(aspectElement, aspectId, beanDefinitions, beanReferences, parserContext);
/*     */       
/* 236 */       parserContext.pushContainingComponent(aspectComponentDefinition);
/*     */       
/* 238 */       List<Element> pointcuts = DomUtils.getChildElementsByTagName(aspectElement, "pointcut");
/* 239 */       for (Element pointcutElement : pointcuts) {
/* 240 */         parsePointcut(pointcutElement, parserContext);
/*     */       }
/*     */       
/* 243 */       parserContext.popAndRegisterContainingComponent();
/*     */     } finally {
/*     */       
/* 246 */       this.parseState.pop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AspectComponentDefinition createAspectComponentDefinition(Element aspectElement, String aspectId, List<BeanDefinition> beanDefs, List<BeanReference> beanRefs, ParserContext parserContext) {
/* 254 */     BeanDefinition[] beanDefArray = beanDefs.<BeanDefinition>toArray(new BeanDefinition[beanDefs.size()]);
/* 255 */     BeanReference[] beanRefArray = beanRefs.<BeanReference>toArray(new BeanReference[beanRefs.size()]);
/* 256 */     Object source = parserContext.extractSource(aspectElement);
/* 257 */     return new AspectComponentDefinition(aspectId, beanDefArray, beanRefArray, source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isAdviceNode(Node aNode, ParserContext parserContext) {
/* 266 */     if (!(aNode instanceof Element)) {
/* 267 */       return false;
/*     */     }
/*     */     
/* 270 */     String name = parserContext.getDelegate().getLocalName(aNode);
/* 271 */     return ("before".equals(name) || "after".equals(name) || "after-returning".equals(name) || "after-throwing"
/* 272 */       .equals(name) || "around".equals(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractBeanDefinition parseDeclareParents(Element declareParentsElement, ParserContext parserContext) {
/* 282 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DeclareParentsAdvisor.class);
/* 283 */     builder.addConstructorArgValue(declareParentsElement.getAttribute("implement-interface"));
/* 284 */     builder.addConstructorArgValue(declareParentsElement.getAttribute("types-matching"));
/*     */     
/* 286 */     String defaultImpl = declareParentsElement.getAttribute("default-impl");
/* 287 */     String delegateRef = declareParentsElement.getAttribute("delegate-ref");
/*     */     
/* 289 */     if (StringUtils.hasText(defaultImpl) && !StringUtils.hasText(delegateRef)) {
/* 290 */       builder.addConstructorArgValue(defaultImpl);
/*     */     }
/* 292 */     else if (StringUtils.hasText(delegateRef) && !StringUtils.hasText(defaultImpl)) {
/* 293 */       builder.addConstructorArgReference(delegateRef);
/*     */     } else {
/*     */       
/* 296 */       parserContext.getReaderContext().error("Exactly one of the default-impl or delegate-ref attributes must be specified", declareParentsElement, this.parseState
/*     */           
/* 298 */           .snapshot());
/*     */     } 
/*     */     
/* 301 */     AbstractBeanDefinition definition = builder.getBeanDefinition();
/* 302 */     definition.setSource(parserContext.extractSource(declareParentsElement));
/* 303 */     parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)definition);
/* 304 */     return definition;
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
/*     */   private AbstractBeanDefinition parseAdvice(String aspectName, int order, Element aspectElement, Element adviceElement, ParserContext parserContext, List<BeanDefinition> beanDefinitions, List<BeanReference> beanReferences) {
/*     */     try {
/* 318 */       this.parseState.push(new AdviceEntry(parserContext.getDelegate().getLocalName(adviceElement)));
/*     */ 
/*     */       
/* 321 */       RootBeanDefinition methodDefinition = new RootBeanDefinition(MethodLocatingFactoryBean.class);
/* 322 */       methodDefinition.getPropertyValues().add("targetBeanName", aspectName);
/* 323 */       methodDefinition.getPropertyValues().add("methodName", adviceElement.getAttribute("method"));
/* 324 */       methodDefinition.setSynthetic(true);
/*     */ 
/*     */       
/* 327 */       RootBeanDefinition aspectFactoryDef = new RootBeanDefinition(SimpleBeanFactoryAwareAspectInstanceFactory.class);
/*     */       
/* 329 */       aspectFactoryDef.getPropertyValues().add("aspectBeanName", aspectName);
/* 330 */       aspectFactoryDef.setSynthetic(true);
/*     */ 
/*     */       
/* 333 */       AbstractBeanDefinition adviceDef = createAdviceDefinition(adviceElement, parserContext, aspectName, order, methodDefinition, aspectFactoryDef, beanDefinitions, beanReferences);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 338 */       RootBeanDefinition advisorDefinition = new RootBeanDefinition(AspectJPointcutAdvisor.class);
/* 339 */       advisorDefinition.setSource(parserContext.extractSource(adviceElement));
/* 340 */       advisorDefinition.getConstructorArgumentValues().addGenericArgumentValue(adviceDef);
/* 341 */       if (aspectElement.hasAttribute("order")) {
/* 342 */         advisorDefinition.getPropertyValues().add("order", aspectElement
/* 343 */             .getAttribute("order"));
/*     */       }
/*     */ 
/*     */       
/* 347 */       parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)advisorDefinition);
/*     */       
/* 349 */       return (AbstractBeanDefinition)advisorDefinition;
/*     */     } finally {
/*     */       
/* 352 */       this.parseState.pop();
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
/*     */ 
/*     */   
/*     */   private AbstractBeanDefinition createAdviceDefinition(Element adviceElement, ParserContext parserContext, String aspectName, int order, RootBeanDefinition methodDef, RootBeanDefinition aspectFactoryDef, List<BeanDefinition> beanDefinitions, List<BeanReference> beanReferences) {
/* 367 */     RootBeanDefinition adviceDefinition = new RootBeanDefinition(getAdviceClass(adviceElement, parserContext));
/* 368 */     adviceDefinition.setSource(parserContext.extractSource(adviceElement));
/*     */     
/* 370 */     adviceDefinition.getPropertyValues().add("aspectName", aspectName);
/* 371 */     adviceDefinition.getPropertyValues().add("declarationOrder", Integer.valueOf(order));
/*     */     
/* 373 */     if (adviceElement.hasAttribute("returning")) {
/* 374 */       adviceDefinition.getPropertyValues().add("returningName", adviceElement
/* 375 */           .getAttribute("returning"));
/*     */     }
/* 377 */     if (adviceElement.hasAttribute("throwing")) {
/* 378 */       adviceDefinition.getPropertyValues().add("throwingName", adviceElement
/* 379 */           .getAttribute("throwing"));
/*     */     }
/* 381 */     if (adviceElement.hasAttribute("arg-names")) {
/* 382 */       adviceDefinition.getPropertyValues().add("argumentNames", adviceElement
/* 383 */           .getAttribute("arg-names"));
/*     */     }
/*     */     
/* 386 */     ConstructorArgumentValues cav = adviceDefinition.getConstructorArgumentValues();
/* 387 */     cav.addIndexedArgumentValue(0, methodDef);
/*     */     
/* 389 */     Object pointcut = parsePointcutProperty(adviceElement, parserContext);
/* 390 */     if (pointcut instanceof BeanDefinition) {
/* 391 */       cav.addIndexedArgumentValue(1, pointcut);
/* 392 */       beanDefinitions.add((BeanDefinition)pointcut);
/*     */     }
/* 394 */     else if (pointcut instanceof String) {
/* 395 */       RuntimeBeanReference pointcutRef = new RuntimeBeanReference((String)pointcut);
/* 396 */       cav.addIndexedArgumentValue(1, pointcutRef);
/* 397 */       beanReferences.add(pointcutRef);
/*     */     } 
/*     */     
/* 400 */     cav.addIndexedArgumentValue(2, aspectFactoryDef);
/*     */     
/* 402 */     return (AbstractBeanDefinition)adviceDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<?> getAdviceClass(Element adviceElement, ParserContext parserContext) {
/* 409 */     String elementName = parserContext.getDelegate().getLocalName(adviceElement);
/* 410 */     if ("before".equals(elementName)) {
/* 411 */       return AspectJMethodBeforeAdvice.class;
/*     */     }
/* 413 */     if ("after".equals(elementName)) {
/* 414 */       return AspectJAfterAdvice.class;
/*     */     }
/* 416 */     if ("after-returning".equals(elementName)) {
/* 417 */       return AspectJAfterReturningAdvice.class;
/*     */     }
/* 419 */     if ("after-throwing".equals(elementName)) {
/* 420 */       return AspectJAfterThrowingAdvice.class;
/*     */     }
/* 422 */     if ("around".equals(elementName)) {
/* 423 */       return AspectJAroundAdvice.class;
/*     */     }
/*     */     
/* 426 */     throw new IllegalArgumentException("Unknown advice kind [" + elementName + "].");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractBeanDefinition parsePointcut(Element pointcutElement, ParserContext parserContext) {
/* 435 */     String id = pointcutElement.getAttribute("id");
/* 436 */     String expression = pointcutElement.getAttribute("expression");
/*     */     
/* 438 */     AbstractBeanDefinition pointcutDefinition = null;
/*     */     
/*     */     try {
/* 441 */       this.parseState.push(new PointcutEntry(id));
/* 442 */       pointcutDefinition = createPointcutDefinition(expression);
/* 443 */       pointcutDefinition.setSource(parserContext.extractSource(pointcutElement));
/*     */       
/* 445 */       String pointcutBeanName = id;
/* 446 */       if (StringUtils.hasText(pointcutBeanName)) {
/* 447 */         parserContext.getRegistry().registerBeanDefinition(pointcutBeanName, (BeanDefinition)pointcutDefinition);
/*     */       } else {
/*     */         
/* 450 */         pointcutBeanName = parserContext.getReaderContext().registerWithGeneratedName((BeanDefinition)pointcutDefinition);
/*     */       } 
/*     */       
/* 453 */       parserContext.registerComponent((ComponentDefinition)new PointcutComponentDefinition(pointcutBeanName, (BeanDefinition)pointcutDefinition, expression));
/*     */     }
/*     */     finally {
/*     */       
/* 457 */       this.parseState.pop();
/*     */     } 
/*     */     
/* 460 */     return pointcutDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object parsePointcutProperty(Element element, ParserContext parserContext) {
/* 470 */     if (element.hasAttribute("pointcut") && element.hasAttribute("pointcut-ref")) {
/* 471 */       parserContext.getReaderContext().error("Cannot define both 'pointcut' and 'pointcut-ref' on <advisor> tag.", element, this.parseState
/*     */           
/* 473 */           .snapshot());
/* 474 */       return null;
/*     */     } 
/* 476 */     if (element.hasAttribute("pointcut")) {
/*     */       
/* 478 */       String expression = element.getAttribute("pointcut");
/* 479 */       AbstractBeanDefinition pointcutDefinition = createPointcutDefinition(expression);
/* 480 */       pointcutDefinition.setSource(parserContext.extractSource(element));
/* 481 */       return pointcutDefinition;
/*     */     } 
/* 483 */     if (element.hasAttribute("pointcut-ref")) {
/* 484 */       String pointcutRef = element.getAttribute("pointcut-ref");
/* 485 */       if (!StringUtils.hasText(pointcutRef)) {
/* 486 */         parserContext.getReaderContext().error("'pointcut-ref' attribute contains empty value.", element, this.parseState
/* 487 */             .snapshot());
/* 488 */         return null;
/*     */       } 
/* 490 */       return pointcutRef;
/*     */     } 
/*     */     
/* 493 */     parserContext.getReaderContext().error("Must define one of 'pointcut' or 'pointcut-ref' on <advisor> tag.", element, this.parseState
/*     */         
/* 495 */         .snapshot());
/* 496 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractBeanDefinition createPointcutDefinition(String expression) {
/* 505 */     RootBeanDefinition beanDefinition = new RootBeanDefinition(AspectJExpressionPointcut.class);
/* 506 */     beanDefinition.setScope("prototype");
/* 507 */     beanDefinition.setSynthetic(true);
/* 508 */     beanDefinition.getPropertyValues().add("expression", expression);
/* 509 */     return (AbstractBeanDefinition)beanDefinition;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\config\ConfigBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */