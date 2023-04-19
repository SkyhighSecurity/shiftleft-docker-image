/*    */ package org.springframework.http.converter.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.atom.Feed;
/*    */ import org.springframework.http.MediaType;
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
/*    */ public class AtomFeedHttpMessageConverter
/*    */   extends AbstractWireFeedHttpMessageConverter<Feed>
/*    */ {
/*    */   public AtomFeedHttpMessageConverter() {
/* 41 */     super(new MediaType("application", "atom+xml"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean supports(Class<?> clazz) {
/* 46 */     return Feed.class.isAssignableFrom(clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\feed\AtomFeedHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */