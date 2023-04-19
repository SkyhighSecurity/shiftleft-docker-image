/*    */ package org.springframework.core.type.filter;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.aspectj.bridge.IMessageHandler;
/*    */ import org.aspectj.weaver.ResolvedType;
/*    */ import org.aspectj.weaver.World;
/*    */ import org.aspectj.weaver.bcel.BcelWorld;
/*    */ import org.aspectj.weaver.patterns.Bindings;
/*    */ import org.aspectj.weaver.patterns.IScope;
/*    */ import org.aspectj.weaver.patterns.PatternParser;
/*    */ import org.aspectj.weaver.patterns.SimpleScope;
/*    */ import org.aspectj.weaver.patterns.TypePattern;
/*    */ import org.springframework.core.type.classreading.MetadataReader;
/*    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AspectJTypeFilter
/*    */   implements TypeFilter
/*    */ {
/*    */   private final World world;
/*    */   private final TypePattern typePattern;
/*    */   
/*    */   public AspectJTypeFilter(String typePatternExpression, ClassLoader classLoader) {
/* 53 */     this.world = (World)new BcelWorld(classLoader, IMessageHandler.THROW, null);
/* 54 */     this.world.setBehaveInJava5Way(true);
/* 55 */     PatternParser patternParser = new PatternParser(typePatternExpression);
/* 56 */     TypePattern typePattern = patternParser.parseTypePattern();
/* 57 */     typePattern.resolve(this.world);
/* 58 */     SimpleScope simpleScope = new SimpleScope(this.world, new org.aspectj.weaver.patterns.FormalBinding[0]);
/* 59 */     this.typePattern = typePattern.resolveBindings((IScope)simpleScope, Bindings.NONE, false, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
/* 67 */     String className = metadataReader.getClassMetadata().getClassName();
/* 68 */     ResolvedType resolvedType = this.world.resolve(className);
/* 69 */     return this.typePattern.matchesStatically(resolvedType);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\filter\AspectJTypeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */