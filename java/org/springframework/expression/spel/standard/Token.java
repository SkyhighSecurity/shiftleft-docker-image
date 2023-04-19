/*    */ package org.springframework.expression.spel.standard;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class Token
/*    */ {
/*    */   TokenKind kind;
/*    */   String data;
/*    */   int startPos;
/*    */   int endPos;
/*    */   
/*    */   Token(TokenKind tokenKind, int startPos, int endPos) {
/* 44 */     this.kind = tokenKind;
/* 45 */     this.startPos = startPos;
/* 46 */     this.endPos = endPos;
/*    */   }
/*    */   
/*    */   Token(TokenKind tokenKind, char[] tokenData, int startPos, int endPos) {
/* 50 */     this(tokenKind, startPos, endPos);
/* 51 */     this.data = new String(tokenData);
/*    */   }
/*    */ 
/*    */   
/*    */   public TokenKind getKind() {
/* 56 */     return this.kind;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     StringBuilder s = new StringBuilder();
/* 62 */     s.append("[").append(this.kind.toString());
/* 63 */     if (this.kind.hasPayload()) {
/* 64 */       s.append(":").append(this.data);
/*    */     }
/* 66 */     s.append("]");
/* 67 */     s.append("(").append(this.startPos).append(",").append(this.endPos).append(")");
/* 68 */     return s.toString();
/*    */   }
/*    */   
/*    */   public boolean isIdentifier() {
/* 72 */     return (this.kind == TokenKind.IDENTIFIER);
/*    */   }
/*    */   
/*    */   public boolean isNumericRelationalOperator() {
/* 76 */     return (this.kind == TokenKind.GT || this.kind == TokenKind.GE || this.kind == TokenKind.LT || this.kind == TokenKind.LE || this.kind == TokenKind.EQ || this.kind == TokenKind.NE);
/*    */   }
/*    */ 
/*    */   
/*    */   public String stringValue() {
/* 81 */     return this.data;
/*    */   }
/*    */   
/*    */   public Token asInstanceOfToken() {
/* 85 */     return new Token(TokenKind.INSTANCEOF, this.startPos, this.endPos);
/*    */   }
/*    */   
/*    */   public Token asMatchesToken() {
/* 89 */     return new Token(TokenKind.MATCHES, this.startPos, this.endPos);
/*    */   }
/*    */   
/*    */   public Token asBetweenToken() {
/* 93 */     return new Token(TokenKind.BETWEEN, this.startPos, this.endPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\standard\Token.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */