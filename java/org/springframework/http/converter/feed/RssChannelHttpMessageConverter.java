/*    */ package org.springframework.http.converter.feed;
/*    */ 
/*    */ import com.rometools.rome.feed.rss.Channel;
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
/*    */ public class RssChannelHttpMessageConverter
/*    */   extends AbstractWireFeedHttpMessageConverter<Channel>
/*    */ {
/*    */   public RssChannelHttpMessageConverter() {
/* 41 */     super(MediaType.APPLICATION_RSS_XML);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean supports(Class<?> clazz) {
/* 46 */     return Channel.class.isAssignableFrom(clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\feed\RssChannelHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */