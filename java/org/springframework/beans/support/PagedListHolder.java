/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class PagedListHolder<E>
/*     */   implements Serializable
/*     */ {
/*     */   public static final int DEFAULT_PAGE_SIZE = 10;
/*     */   public static final int DEFAULT_MAX_LINKED_PAGES = 10;
/*     */   private List<E> source;
/*     */   private Date refreshDate;
/*     */   private SortDefinition sort;
/*     */   private SortDefinition sortUsed;
/*  72 */   private int pageSize = 10;
/*     */   
/*  74 */   private int page = 0;
/*     */   
/*     */   private boolean newPageSet;
/*     */   
/*  78 */   private int maxLinkedPages = 10;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PagedListHolder() {
/*  87 */     this(new ArrayList<E>(0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PagedListHolder(List<E> source) {
/*  97 */     this(source, new MutableSortDefinition(true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PagedListHolder(List<E> source, SortDefinition sort) {
/* 106 */     setSource(source);
/* 107 */     setSort(sort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(List<E> source) {
/* 115 */     Assert.notNull(source, "Source List must not be null");
/* 116 */     this.source = source;
/* 117 */     this.refreshDate = new Date();
/* 118 */     this.sortUsed = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<E> getSource() {
/* 125 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getRefreshDate() {
/* 132 */     return this.refreshDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSort(SortDefinition sort) {
/* 141 */     this.sort = sort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortDefinition getSort() {
/* 148 */     return this.sort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPageSize(int pageSize) {
/* 157 */     if (pageSize != this.pageSize) {
/* 158 */       this.pageSize = pageSize;
/* 159 */       if (!this.newPageSet) {
/* 160 */         this.page = 0;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPageSize() {
/* 169 */     return this.pageSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPage(int page) {
/* 177 */     this.page = page;
/* 178 */     this.newPageSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPage() {
/* 186 */     this.newPageSet = false;
/* 187 */     if (this.page >= getPageCount()) {
/* 188 */       this.page = getPageCount() - 1;
/*     */     }
/* 190 */     return this.page;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLinkedPages(int maxLinkedPages) {
/* 197 */     this.maxLinkedPages = maxLinkedPages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLinkedPages() {
/* 204 */     return this.maxLinkedPages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPageCount() {
/* 212 */     float nrOfPages = getNrOfElements() / getPageSize();
/* 213 */     return (int)((nrOfPages > (int)nrOfPages || nrOfPages == 0.0D) ? (nrOfPages + 1.0F) : nrOfPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFirstPage() {
/* 220 */     return (getPage() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLastPage() {
/* 227 */     return (getPage() == getPageCount() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void previousPage() {
/* 235 */     if (!isFirstPage()) {
/* 236 */       this.page--;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextPage() {
/* 245 */     if (!isLastPage()) {
/* 246 */       this.page++;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNrOfElements() {
/* 254 */     return getSource().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFirstElementOnPage() {
/* 262 */     return getPageSize() * getPage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLastElementOnPage() {
/* 270 */     int endIndex = getPageSize() * (getPage() + 1);
/* 271 */     int size = getNrOfElements();
/* 272 */     return ((endIndex > size) ? size : endIndex) - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<E> getPageList() {
/* 279 */     return getSource().subList(getFirstElementOnPage(), getLastElementOnPage() + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFirstLinkedPage() {
/* 286 */     return Math.max(0, getPage() - getMaxLinkedPages() / 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLastLinkedPage() {
/* 293 */     return Math.min(getFirstLinkedPage() + getMaxLinkedPages() - 1, getPageCount() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resort() {
/* 304 */     SortDefinition sort = getSort();
/* 305 */     if (sort != null && !sort.equals(this.sortUsed)) {
/* 306 */       this.sortUsed = copySortDefinition(sort);
/* 307 */       doSort(getSource(), sort);
/* 308 */       setPage(0);
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
/*     */ 
/*     */   
/*     */   protected SortDefinition copySortDefinition(SortDefinition sort) {
/* 325 */     return new MutableSortDefinition(sort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doSort(List<E> source, SortDefinition sort) {
/* 336 */     PropertyComparator.sort(source, sort);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\support\PagedListHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */