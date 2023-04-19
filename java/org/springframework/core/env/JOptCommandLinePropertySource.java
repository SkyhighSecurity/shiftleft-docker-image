/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
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
/*     */ public class JOptCommandLinePropertySource
/*     */   extends CommandLinePropertySource<OptionSet>
/*     */ {
/*     */   public JOptCommandLinePropertySource(OptionSet options) {
/*  68 */     super(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JOptCommandLinePropertySource(String name, OptionSet options) {
/*  76 */     super(name, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean containsOption(String name) {
/*  82 */     return this.source.has(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getPropertyNames() {
/*  87 */     List<String> names = new ArrayList<String>();
/*  88 */     for (OptionSpec<?> spec : (Iterable<OptionSpec<?>>)this.source.specs()) {
/*  89 */       List<String> aliases = spec.options();
/*  90 */       if (!aliases.isEmpty())
/*     */       {
/*  92 */         names.add(aliases.get(aliases.size() - 1));
/*     */       }
/*     */     } 
/*  95 */     return StringUtils.toStringArray(names);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getOptionValues(String name) {
/* 100 */     List<?> argValues = this.source.valuesOf(name);
/* 101 */     List<String> stringArgValues = new ArrayList<String>();
/* 102 */     for (Object argValue : argValues) {
/* 103 */       stringArgValues.add(argValue.toString());
/*     */     }
/* 105 */     if (stringArgValues.isEmpty()) {
/* 106 */       return this.source.has(name) ? Collections.<String>emptyList() : null;
/*     */     }
/* 108 */     return Collections.unmodifiableList(stringArgValues);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<String> getNonOptionArgs() {
/* 113 */     List<?> argValues = this.source.nonOptionArguments();
/* 114 */     List<String> stringArgValues = new ArrayList<String>();
/* 115 */     for (Object argValue : argValues) {
/* 116 */       stringArgValues.add(argValue.toString());
/*     */     }
/* 118 */     return stringArgValues.isEmpty() ? Collections.<String>emptyList() : 
/* 119 */       Collections.<String>unmodifiableList(stringArgValues);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\JOptCommandLinePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */