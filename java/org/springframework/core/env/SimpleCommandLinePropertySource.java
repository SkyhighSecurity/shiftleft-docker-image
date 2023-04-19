/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.List;
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
/*     */ public class SimpleCommandLinePropertySource
/*     */   extends CommandLinePropertySource<CommandLineArgs>
/*     */ {
/*     */   public SimpleCommandLinePropertySource(String... args) {
/*  89 */     super((new SimpleCommandLineArgsParser()).parse(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCommandLinePropertySource(String name, String[] args) {
/*  97 */     super(name, (new SimpleCommandLineArgsParser()).parse(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getPropertyNames() {
/* 105 */     return StringUtils.toStringArray(this.source.getOptionNames());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean containsOption(String name) {
/* 110 */     return this.source.containsOption(name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<String> getOptionValues(String name) {
/* 115 */     return this.source.getOptionValues(name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<String> getNonOptionArgs() {
/* 120 */     return this.source.getNonOptionArgs();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\SimpleCommandLinePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */