/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ class CommandLineArgs
/*    */ {
/* 36 */   private final Map<String, List<String>> optionArgs = new HashMap<String, List<String>>();
/* 37 */   private final List<String> nonOptionArgs = new ArrayList<String>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addOptionArg(String optionName, String optionValue) {
/* 46 */     if (!this.optionArgs.containsKey(optionName)) {
/* 47 */       this.optionArgs.put(optionName, new ArrayList<String>());
/*    */     }
/* 49 */     if (optionValue != null) {
/* 50 */       ((List<String>)this.optionArgs.get(optionName)).add(optionValue);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<String> getOptionNames() {
/* 58 */     return Collections.unmodifiableSet(this.optionArgs.keySet());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean containsOption(String optionName) {
/* 65 */     return this.optionArgs.containsKey(optionName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getOptionValues(String optionName) {
/* 74 */     return this.optionArgs.get(optionName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addNonOptionArg(String value) {
/* 81 */     this.nonOptionArgs.add(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getNonOptionArgs() {
/* 88 */     return Collections.unmodifiableList(this.nonOptionArgs);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\CommandLineArgs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */