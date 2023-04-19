package org.springframework.ui.context;

public interface HierarchicalThemeSource extends ThemeSource {
  void setParentThemeSource(ThemeSource paramThemeSource);
  
  ThemeSource getParentThemeSource();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\ui\context\HierarchicalThemeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */