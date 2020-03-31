/*
 * $Id: HtmlTags.java 3533 2008-07-07 21:27:13Z Howard_s $
 *
 * Copyright 2001, 2002 by Bruno Lowagie.
 *
 * Contributions by:
 * Lubos Strapko
 * 
 */

package com.lowagie.text.html;

/**
 * A class that contains all the possible tagnames and their attributes.
 */

public class HtmlTags {

	/** the root tag. */
	public static final String HTML = "html";

	/** the head tag */
	public static final String HEAD = "head";

	/** This is a possible HTML attribute for the HEAD tag. */
	public static final String CONTENT = "content";

	/** the meta tag */
	public static final String META = "meta";

	/** attribute of the root tag */
	public static final String SUBJECT = "subject";

	/** attribute of the root tag */
	public static final String KEYWORDS = "keywords";

	/** attribute of the root tag */
	public static final String AUTHOR = "author";

	/** the title tag. */
	public static final String TITLE = "title";

	/** the script tag. */
	public static final String SCRIPT = "script";

	/** This is a possible HTML attribute for the SCRIPT tag. */
	public static final String LANGUAGE = "language";

	/** This is a possible value for the LANGUAGE attribute. */
	public static final String JAVASCRIPT = "JavaScript";

	/** the body tag. */
	public static final String BODY = "body";

	/** This is a possible HTML attribute for the BODY tag */
	public static final String JAVASCRIPT_ONLOAD = "onLoad";

	/** This is a possible HTML attribute for the BODY tag */
	public static final String JAVASCRIPT_ONUNLOAD = "onUnLoad";

	/** This is a possible HTML attribute for the BODY tag. */
	public static final String TOPMARGIN = "topmargin";

	/** This is a possible HTML attribute for the BODY tag. */
	public static final String BOTTOMMARGIN = "bottommargin";

	/** This is a possible HTML attribute for the BODY tag. */
	public static final String LEFTMARGIN = "leftmargin";

	/** This is a possible HTML attribute for the BODY tag. */
	public static final String RIGHTMARGIN = "rightmargin";

	// Phrases, Anchors, Lists and Paragraphs

	/** the chunk tag */
	public static final String CHUNK = "font";

	/** the phrase tag */
	public static final String CODE = "code";

	/** the phrase tag */
	public static final String VAR = "var";

	/** the anchor tag */
	public static final String ANCHOR = "a";

	/** the list tag */
	public static final String ORDEREDLIST = "ol";

	/** the list tag */
	public static final String UNORDEREDLIST = "ul";

	/** the listitem tag */
	public static final String LISTITEM = "li";

	/** the paragraph tag */
	public static final String PARAGRAPH = "p";

	/** attribute of anchor tag */
	public static final String NAME = "name";

	/** attribute of anchor tag */
	public static final String REFERENCE = "href";

	/** attribute of anchor tag */
	public static final String[] H = new String[6];
	static {
		H[0] = "h1";
		H[1] = "h2";
		H[2] = "h3";
		H[3] = "h4";
		H[4] = "h5";
		H[5] = "h6";
	}

	// Chunks

	/** attribute of the chunk tag */
	public static final String FONT = "face";

	/** attribute of the chunk tag */
	public static final String SIZE = "point-size";

	/** attribute of the chunk/table/cell tag */
	public static final String COLOR = "color";

	/** some phrase tag */
	public static final String EM = "em";

	/** some phrase tag */
	public static final String I = "i";

	/** some phrase tag */
	public static final String STRONG = "strong";

	/** some phrase tag */
	public static final String B = "b";

	/** some phrase tag */
	public static final String S = "s";

	/** some phrase tag */
	public static final String U = "u";

	/** some phrase tag */
	public static final String SUB = "sub";

	/** some phrase tag */
	public static final String SUP = "sup";

	/** the possible value of a tag */
	public static final String HORIZONTALRULE = "hr";

	// tables/cells

	/** the table tag */
	public static final String TABLE = "table";

	/** the cell tag */
	public static final String ROW = "tr";

	/** the cell tag */
	public static final String CELL = "td";

	/** attribute of the cell tag */
	public static final String HEADERCELL = "th";

	/** attribute of the table tag */
	public static final String COLUMNS = "cols";

	/** attribute of the table tag */
	public static final String CELLPADDING = "cellpadding";

	/** attribute of the table tag */
	public static final String CELLSPACING = "cellspacing";

	/** attribute of the cell tag */
	public static final String COLSPAN = "colspan";

	/** attribute of the cell tag */
	public static final String ROWSPAN = "rowspan";

	/** attribute of the cell tag */
	public static final String NOWRAP = "nowrap";

	/** attribute of the table/cell tag */
	public static final String BORDERWIDTH = "border";

	/** attribute of the table/cell tag */
	public static final String WIDTH = "width";

	/** attribute of the table/cell tag */
	public static final String BACKGROUNDCOLOR = "bgcolor";

	/** attribute of the table/cell tag */
	public static final String BORDERCOLOR = "bordercolor";

	/** attribute of paragraph/image/table tag */
	public static final String ALIGN = "align";

	/** attribute of chapter/section/paragraph/table/cell tag */
	public static final String LEFT = "left";

	/** attribute of chapter/section/paragraph/table/cell tag */
	public static final String RIGHT = "right";

	/** attribute of the cell tag */
	public static final String HORIZONTALALIGN = "align";

	/** attribute of the cell tag */
	public static final String VERTICALALIGN = "valign";

	/** attribute of the table/cell tag */
	public static final String TOP = "top";

	/** attribute of the table/cell tag */
	public static final String BOTTOM = "bottom";

	// Misc

	/** the image tag */
	public static final String IMAGE = "img";

	/** attribute of the image tag 
	 * @see com.lowagie.text.ElementTags#SRC
	 */
	public static final String URL = "src";

	/** attribute of the image tag */
	public static final String ALT = "alt";

	/** attribute of the image tag */
	public static final String PLAINWIDTH = "width";

	/** attribute of the image tag */
	public static final String PLAINHEIGHT = "height";

	/** the newpage tag */
	public static final String NEWLINE = "br";

	// alignment attribute values

	/** the possible value of an alignment attribute */
	public static final String ALIGN_LEFT = "Left";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_CENTER = "Center";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_RIGHT = "Right";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_JUSTIFIED = "Justify";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_TOP = "Top";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_MIDDLE = "Middle";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_BOTTOM = "Bottom";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_BASELINE = "Baseline";

	/** the possible value of an alignment attribute */
	public static final String DEFAULT = "Default";

	/** The DIV tag. */
	public static final String DIV = "div";

	/** The SPAN tag. */
	public static final String SPAN = "span";

	/** The LINK tag. */
	public static final String LINK = "link";

	/** This is a possible HTML attribute for the LINK tag. */
	public static final String TEXT_CSS = "text/css";

	/** This is a possible HTML attribute for the LINK tag. */
	public static final String REL = "rel";

	/** This is used for inline css style information */
	public static final String STYLE = "style";

	/** This is a possible HTML attribute for the LINK tag. */
	public static final String TYPE = "type";

	/** This is a possible HTML attribute. */
	public static final String STYLESHEET = "stylesheet";

	/** This is a possible HTML attribute for auto-formated 
     * @since 2.1.3
     */
	public static final String PRE = "pre";





	// iText specific

	/** the key for any tag */
	public static final String ITEXT_TAG = "tag";

	// HTML tags

	/** the markup for the body part of a file */
	public static final String HTML_TAG_BODY = "body";

	/** The DIV tag. */
	public static final String HTML_TAG_DIV = "div";

	/** This is a possible HTML-tag. */
	public static final String HTML_TAG_LINK = "link";

	/** The SPAN tag. */
	public static final String HTML_TAG_SPAN = "span";

	// HTML attributes

	/** the height attribute. */
	public static final String HTML_ATTR_HEIGHT = "height";

	/** the hyperlink reference attribute. */
	public static final String HTML_ATTR_HREF = "href";

	/** This is a possible HTML attribute for the LINK tag. */
	public static final String HTML_ATTR_REL = "rel";

	/** This is used for inline css style information */
	public static final String HTML_ATTR_STYLE = "style";

	/** This is a possible HTML attribute for the LINK tag. */
	public static final String HTML_ATTR_TYPE = "type";

	/** This is a possible HTML attribute. */
	public static final String HTML_ATTR_STYLESHEET = "stylesheet";

	/** the width attribute. */
	public static final String HTML_ATTR_WIDTH = "width";

	/** attribute for specifying externally defined CSS class */
	public static final String HTML_ATTR_CSS_CLASS = "class";

	/** The ID attribute. */
	public static final String HTML_ATTR_CSS_ID = "id";

	// HTML values

	/** This is a possible value for the language attribute (SCRIPT tag). */
	public static final String HTML_VALUE_JAVASCRIPT = "text/javascript";

	/** This is a possible HTML attribute for the LINK tag. */
	public static final String HTML_VALUE_CSS = "text/css";

	// CSS keys

	/** the CSS tag for background color */
	public static final String CSS_KEY_BGCOLOR = "background-color";

	/** the CSS tag for text color */
	public static final String CSS_KEY_COLOR = "color";

	/** CSS key that indicate the way something has to be displayed */
	public static final String CSS_KEY_DISPLAY = "display";

	/** the CSS tag for the font family */
	public static final String CSS_KEY_FONTFAMILY = "font-family";

	/** the CSS tag for the font size */
	public static final String CSS_KEY_FONTSIZE = "font-size";

	/** the CSS tag for the font style */
	public static final String CSS_KEY_FONTSTYLE = "font-style";

	/** the CSS tag for the font weight */
	public static final String CSS_KEY_FONTWEIGHT = "font-weight";

	/** the CSS tag for text decorations */
	public static final String CSS_KEY_LINEHEIGHT = "line-height";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_MARGIN = "margin";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_MARGINLEFT = "margin-left";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_MARGINRIGHT = "margin-right";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_MARGINTOP = "margin-top";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_MARGINBOTTOM = "margin-bottom";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_PADDING = "padding";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_PADDINGLEFT = "padding-left";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_PADDINGRIGHT = "padding-right";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_PADDINGTOP = "padding-top";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_PADDINGBOTTOM = "padding-bottom";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_BORDERCOLOR = "border-color";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_BORDERWIDTH = "border-width";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_BORDERWIDTHLEFT = "border-left-width";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_BORDERWIDTHRIGHT = "border-right-width";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_BORDERWIDTHTOP = "border-top-width";

	/** the CSS tag for the margin of an object */
	public static final String CSS_KEY_BORDERWIDTHBOTTOM = "border-bottom-width";

	/** the CSS tag for adding a page break when the document is printed */
	public static final String CSS_KEY_PAGE_BREAK_AFTER = "page-break-after";

	/** the CSS tag for adding a page break when the document is printed */
	public static final String CSS_KEY_PAGE_BREAK_BEFORE = "page-break-before";

	/** the CSS tag for the horizontal alignment of an object */
	public static final String CSS_KEY_TEXTALIGN = "text-align";

	/** the CSS tag for text decorations */
	public static final String CSS_KEY_TEXTDECORATION = "text-decoration";

	/** the CSS tag for text decorations */
	public static final String CSS_KEY_VERTICALALIGN = "vertical-align";

	/** the CSS tag for the visibility of objects */
	public static final String CSS_KEY_VISIBILITY = "visibility";

	// CSS values

	/**
	 * value for the CSS tag for adding a page break when the document is
	 * printed
	 */
	public static final String CSS_VALUE_ALWAYS = "always";

	/** A possible value for the DISPLAY key */
	public static final String CSS_VALUE_BLOCK = "block";

	/** a CSS value for text font weight */
	public static final String CSS_VALUE_BOLD = "bold";

	/** the value if you want to hide objects. */
	public static final String CSS_VALUE_HIDDEN = "hidden";

	/** A possible value for the DISPLAY key */
	public static final String CSS_VALUE_INLINE = "inline";

	/** a CSS value for text font style */
	public static final String CSS_VALUE_ITALIC = "italic";

	/** a CSS value for text decoration */
	public static final String CSS_VALUE_LINETHROUGH = "line-through";

	/** A possible value for the DISPLAY key */
	public static final String CSS_VALUE_LISTITEM = "list-item";

	/** a CSS value */
	public static final String CSS_VALUE_NONE = "none";

	/** a CSS value */
	public static final String CSS_VALUE_NORMAL = "normal";

	/** a CSS value for text font style */
	public static final String CSS_VALUE_OBLIQUE = "oblique";

	/** A possible value for the DISPLAY key */
	public static final String CSS_VALUE_TABLE = "table";

	/** A possible value for the DISPLAY key */
	public static final String CSS_VALUE_TABLEROW = "table-row";

	/** A possible value for the DISPLAY key */
	public static final String CSS_VALUE_TABLECELL = "table-cell";

	/** the CSS value for a horizontal alignment of an object */
	public static final String CSS_VALUE_TEXTALIGNLEFT = "left";

	/** the CSS value for a horizontal alignment of an object */
	public static final String CSS_VALUE_TEXTALIGNRIGHT = "right";

	/** the CSS value for a horizontal alignment of an object */
	public static final String CSS_VALUE_TEXTALIGNCENTER = "center";

	/** the CSS value for a horizontal alignment of an object */
	public static final String CSS_VALUE_TEXTALIGNJUSTIFY = "justify";

	/** a CSS value for text decoration */
	public static final String CSS_VALUE_UNDERLINE = "underline";
}