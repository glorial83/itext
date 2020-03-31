package com.lowagie.text.html;
import java.util.HashMap;
import java.util.Map;

import com.steadystate.css.parser.CSSOMParser;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleDeclaration;


//******************************************************************************
//**  StyleSheet Class
//******************************************************************************
/**
 *   Used to represent a collection of styles associated with an HTML document.
 *
 ******************************************************************************/

public class StyleSheet {

    private HashMap<String, HashMap<String, String>> classMap =
            new HashMap<String, HashMap<String, String>>();
    public HashMap<String, HashMap<String, String>> tagMap =
            new HashMap<String, HashMap<String, String>>();	//public으로 변경함


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class.
   */
    public StyleSheet() {
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class using a css fragment.
   */
    public StyleSheet(String css) {

        try{
            InputSource source = new InputSource(new java.io.StringReader(css));
            CSSOMParser parser = new CSSOMParser();
            CSSStyleSheet stylesheet = parser.parseStyleSheet(source);
            CSSRuleList ruleList = stylesheet.getCssRules();

            for (int i = 0; i < ruleList.getLength(); i++) {
                CSSRule rule = ruleList.item(i);
                if (rule instanceof CSSStyleRule){

                    CSSStyleRule styleRule = (CSSStyleRule) rule;
                    for (String tag : styleRule.getSelectorText().split(",")){
                        tag = tag.trim();

                        CSSStyleDeclaration styleDeclaration = styleRule.getStyle();
                        for (int j = 0; j < styleDeclaration.getLength(); j++) {
                            String property = styleDeclaration.item(j);
                            String value = styleDeclaration.getPropertyCSSValue(property).getCssText();
                            //styleDeclaration.getPropertyPriority(property)

                            if (tag.startsWith("*.")){
                            	tag = tag.replace("*.", "");
                            }else if (tag.startsWith("*#")){
                            }else{
                            }

                            addStyle(tag, property, value);
                        }

                    }
                }
            }
        }
        catch(java.io.IOException e){
            e.printStackTrace();
        }
    }


  //**************************************************************************
  //** getElementStyle
  //**************************************************************************
  /** Returns a list of all the styles defined for a given tag. The list
   *  includes styles that may be defined in a style sheet and in a "style"
   *  attribute.
   *
   *  @param tag HTML tag name (e.g. "div", "table", "span", etc).
   *  @param attr Tag attributes (e.g. "width", "class", "style", etc).
   */
    public Style getElementStyle(String tag, HashMap<String, String> attr){

        if (attr==null) attr = new HashMap<String, String>();

        HashMap<String, String> style = new HashMap<String, String>();
        HashMap<String, String> map = tagMap.get(tag.toLowerCase());

        if (attr.containsKey("width")) style.put("width", attr.get("width"));
        if (attr.containsKey("height")) style.put("height", attr.get("height"));
        if (attr.containsKey("border")) style.put("border", attr.get("border"));
        if (attr.containsKey("align")) style.put("align", attr.get("align"));


        if (attr.containsKey("face")) style.put("font-family", attr.get("face"));
        if (attr.containsKey("size")) style.put("font-size", attr.get("size"));


      //Parse the style attribute
        if (attr.containsKey("style")){

            style.putAll(parseStyle(attr.get("style")));

            if (map != null) {
                java.util.Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()){
                    String key = it.next();
                    String val = map.get(key);
                    if (val==null) val = "";
                    if (!style.containsKey(key)) style.put(key, val);
                }
            }
        }

        //class의 style과 attr로 준 style 합치기
        if (attr.containsKey("class")){
        	String className = attr.get("class");
            if (tagMap != null) {
                if(tagMap.containsKey(className)) {
                	Map<String, String> temp = new HashMap<String, String>(tagMap.get(className));
                	temp.putAll(style);
                	style.putAll(temp);
                }
            }
        }

        if (map != null) {
            java.util.Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()){
                String key = it.next();
                String val = map.get(key);
                if (val!=null) {
                	if (!style.containsKey(key)) {
                		style.put(key, val);
                	}
                }
            }
        }

        return new Style(style);
    }


  //**************************************************************************
  //** addStyle
  //**************************************************************************
  /** Used to add a style definition for a given tag.
   *  @param tag HTML tag name (e.g. "h1", "div", etc)
   *  @param key Style keyword (e.g. "font-size")
   *  @param value Value associated with the style keyword (e.g. "18pt")
   */
    public void addStyle(String tag, String key, String value) {

        if (tag.startsWith("*.")){
            return;
        }
        else if (tag.startsWith("*#")){
            int idx = tag.indexOf(" ");
            tag = tag.substring(1, idx) + tag.substring(idx).toLowerCase();
        }
        else{
            tag = tag.toLowerCase();
        }


        HashMap<String, String> map = tagMap.get(tag);
        if (map == null) {
            map = new HashMap<String, String>();
            tagMap.put(tag, map);
        }

        key = key.trim();
        value = value.trim();
        while (value.endsWith(";")){
            value = value.substring(0, value.length()-1).trim();
        }

        map.put(key.toLowerCase(), value);
    }


  //**************************************************************************
  //** update
  //**************************************************************************
  /** Used to add/update style definitions using another StyleSheet.
   */
    public void update(StyleSheet styleSheet){
        if (styleSheet==null) return;
        java.util.Iterator<String> it = styleSheet.tagMap.keySet().iterator();
        while (it.hasNext()){
            String tag = it.next();
            HashMap<String, String> map = styleSheet.tagMap.get(tag);
            if (map!=null){
                java.util.Iterator<String> i2 = map.keySet().iterator();
                while (i2.hasNext()){
                    String key = i2.next();
                    String val = map.get(key);
                    addStyle(tag, key, val);
                }
            }
        }
    }




    private java.util.HashMap<String, String> parseStyle(String css){
        css = "p{" + css + "}"; //<--Hack for CSSOMParser!
        java.util.HashMap<String, String> map = new java.util.HashMap<String, String>();
        try{
            InputSource source = new InputSource(new java.io.StringReader(css));
            CSSOMParser parser = new CSSOMParser();
            CSSStyleSheet stylesheet = parser.parseStyleSheet(source);
            CSSRuleList ruleList = stylesheet.getCssRules();


            for (int i = 0; i < ruleList.getLength(); i++) {
                CSSRule rule = ruleList.item(i);
                if (rule instanceof CSSStyleRule){

                    CSSStyleRule styleRule = (CSSStyleRule) rule;
                    CSSStyleDeclaration styleDeclaration = styleRule.getStyle();
                    for (int j = 0; j < styleDeclaration.getLength(); j++) {
                        String property = styleDeclaration.item(j);
                        String value = styleDeclaration.getPropertyCSSValue(property).getCssText();
                        //System.out.println("property: " + property);
                        //System.out.println("value: " + value);
                        //System.out.println("priority: " + styleDeclaration.getPropertyPriority(property));
                        map.put(property, value);
                    }

                }
            }
        }
        catch(Exception e){
        }
        return map;
    }

    //addStyle한 값을 map으로 반환
    public void applyStyle(String tag, HashMap props) {
		HashMap map = (HashMap) tagMap.get(tag.toLowerCase());
		if (map != null) {
			HashMap temp = new HashMap(map);
			temp.putAll(props);
			props.putAll(temp);
		}
		String cm = (String) props.get(Markup.HTML_ATTR_CSS_CLASS);
		if (cm == null)
			return;
		map = (HashMap) classMap.get(cm.toLowerCase());
		if (map == null)
			return;
		props.remove(Markup.HTML_ATTR_CSS_CLASS);
		HashMap temp = new HashMap(map);
		temp.putAll(props);
		props.putAll(temp);
	}
}