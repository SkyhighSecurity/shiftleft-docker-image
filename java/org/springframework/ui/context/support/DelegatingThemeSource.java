/*    */ package org.springframework.ui.context.support;
/*    */ 
/*    */ import org.springframework.ui.context.HierarchicalThemeSource;
/*    */ import org.springframework.ui.context.Theme;
/*    */ import org.springframework.ui.context.ThemeSource;
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
/*    */ public class DelegatingThemeSource
/*    */   implements HierarchicalThemeSource
/*    */ {
/*    */   private ThemeSource parentThemeSource;
/*    */   
/*    */   public void setParentThemeSource(ThemeSource parentThemeSource) {
/* 41 */     this.parentThemeSource = parentThemeSource;
/*    */   }
/*    */ 
/*    */   
/*    */   public ThemeSource getParentThemeSource() {
/* 46 */     return this.parentThemeSource;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Theme getTheme(String themeName) {
/* 52 */     if (this.parentThemeSource != null) {
/* 53 */       return this.parentThemeSource.getTheme(themeName);
/*    */     }
/*    */     
/* 56 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\ui\context\support\DelegatingThemeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */