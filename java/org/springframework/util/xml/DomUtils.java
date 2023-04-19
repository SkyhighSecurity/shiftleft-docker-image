/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ public abstract class DomUtils
/*     */ {
/*     */   public static List<Element> getChildElementsByTagName(Element ele, String... childEleNames) {
/*  60 */     Assert.notNull(ele, "Element must not be null");
/*  61 */     Assert.notNull(childEleNames, "Element names collection must not be null");
/*  62 */     List<String> childEleNameList = Arrays.asList(childEleNames);
/*  63 */     NodeList nl = ele.getChildNodes();
/*  64 */     List<Element> childEles = new ArrayList<Element>();
/*  65 */     for (int i = 0; i < nl.getLength(); i++) {
/*  66 */       Node node = nl.item(i);
/*  67 */       if (node instanceof Element && nodeNameMatch(node, childEleNameList)) {
/*  68 */         childEles.add((Element)node);
/*     */       }
/*     */     } 
/*  71 */     return childEles;
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
/*     */   public static List<Element> getChildElementsByTagName(Element ele, String childEleName) {
/*  85 */     return getChildElementsByTagName(ele, new String[] { childEleName });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Element getChildElementByTagName(Element ele, String childEleName) {
/*  95 */     Assert.notNull(ele, "Element must not be null");
/*  96 */     Assert.notNull(childEleName, "Element name must not be null");
/*  97 */     NodeList nl = ele.getChildNodes();
/*  98 */     for (int i = 0; i < nl.getLength(); i++) {
/*  99 */       Node node = nl.item(i);
/* 100 */       if (node instanceof Element && nodeNameMatch(node, childEleName)) {
/* 101 */         return (Element)node;
/*     */       }
/*     */     } 
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getChildElementValueByTagName(Element ele, String childEleName) {
/* 114 */     Element child = getChildElementByTagName(ele, childEleName);
/* 115 */     return (child != null) ? getTextValue(child) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Element> getChildElements(Element ele) {
/* 124 */     Assert.notNull(ele, "Element must not be null");
/* 125 */     NodeList nl = ele.getChildNodes();
/* 126 */     List<Element> childEles = new ArrayList<Element>();
/* 127 */     for (int i = 0; i < nl.getLength(); i++) {
/* 128 */       Node node = nl.item(i);
/* 129 */       if (node instanceof Element) {
/* 130 */         childEles.add((Element)node);
/*     */       }
/*     */     } 
/* 133 */     return childEles;
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
/*     */   public static String getTextValue(Element valueEle) {
/* 146 */     Assert.notNull(valueEle, "Element must not be null");
/* 147 */     StringBuilder sb = new StringBuilder();
/* 148 */     NodeList nl = valueEle.getChildNodes();
/* 149 */     for (int i = 0; i < nl.getLength(); i++) {
/* 150 */       Node item = nl.item(i);
/* 151 */       if ((item instanceof org.w3c.dom.CharacterData && !(item instanceof org.w3c.dom.Comment)) || item instanceof org.w3c.dom.EntityReference) {
/* 152 */         sb.append(item.getNodeValue());
/*     */       }
/*     */     } 
/* 155 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean nodeNameEquals(Node node, String desiredName) {
/* 164 */     Assert.notNull(node, "Node must not be null");
/* 165 */     Assert.notNull(desiredName, "Desired name must not be null");
/* 166 */     return nodeNameMatch(node, desiredName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler createContentHandler(Node node) {
/* 175 */     return new DomContentHandler(node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean nodeNameMatch(Node node, String desiredName) {
/* 182 */     return (desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean nodeNameMatch(Node node, Collection<?> desiredNames) {
/* 189 */     return (desiredNames.contains(node.getNodeName()) || desiredNames.contains(node.getLocalName()));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\DomUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */