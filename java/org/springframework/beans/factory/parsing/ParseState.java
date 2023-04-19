/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ParseState
/*     */ {
/*     */   private static final char TAB = '\t';
/*     */   private final Stack<Entry> state;
/*     */   
/*     */   public ParseState() {
/*  50 */     this.state = new Stack<Entry>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParseState(ParseState other) {
/*  59 */     this.state = (Stack<Entry>)other.state.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void push(Entry entry) {
/*  67 */     this.state.push(entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pop() {
/*  74 */     this.state.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entry peek() {
/*  82 */     return this.state.empty() ? null : this.state.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseState snapshot() {
/*  90 */     return new ParseState(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     StringBuilder sb = new StringBuilder();
/* 100 */     for (int x = 0; x < this.state.size(); x++) {
/* 101 */       if (x > 0) {
/* 102 */         sb.append('\n');
/* 103 */         for (int y = 0; y < x; y++) {
/* 104 */           sb.append('\t');
/*     */         }
/* 106 */         sb.append("-> ");
/*     */       } 
/* 108 */       sb.append(this.state.get(x));
/*     */     } 
/* 110 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static interface Entry {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\ParseState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */