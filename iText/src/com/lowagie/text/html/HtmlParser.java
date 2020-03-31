package com.lowagie.text.html;
import java.util.ArrayList;
import java.util.HashMap;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.xml.simpleparser.SimpleXMLDocHandler;
import com.lowagie.text.xml.simpleparser.SimpleXMLParser;

import static com.lowagie.text.pdf.BaseFont.WINANSI;
import static com.lowagie.text.pdf.BaseFont.CP1252;


//******************************************************************************
//**  HTMLWorker Class
//******************************************************************************
/**
 *   Used to parse HTML and generate a PDF document. This code is based on the
 *   HTMLWorker class in iText version 2.1.7
 *
 ******************************************************************************/

public class HtmlParser implements SimpleXMLDocHandler, DocListener {

    protected ArrayList objectList;
    protected DocListener document;
    private Paragraph currentParagraph;
    private ChainedProperties chain = new ChainedProperties();
    private java.util.Stack stack = new java.util.Stack();
    private boolean pendingTR = false;
    private boolean pendingTD = false;
    private boolean pendingLI = false;
    private StyleSheet styleSheet = new StyleSheet();
    private boolean isPRE = false;
    private java.util.Stack tableState = new java.util.Stack();
    private boolean skipText = false;
    private HashMap interfaceProps;
    public static final String tagsSupportedString =
        "ol ul li a pre font span br p div body table td th tr i b u sub sup em strong s strike"
                + " h1 h2 h3 h4 h5 h6 img hr";
    public static final HashMap tagsSupported = new HashMap();
    static {
        java.util.StringTokenizer tok = new java.util.StringTokenizer(tagsSupportedString);
        while (tok.hasMoreTokens()) tagsSupported.put(tok.nextToken(), null);
    }
    public static HashMap followTags = new HashMap();
    static {
        followTags.put("i", "i");
        followTags.put("b", "b");
        followTags.put("u", "u");
        followTags.put("sub", "sub");
        followTags.put("sup", "sup");
        followTags.put("em", "i");
        followTags.put("strong", "b");
        followTags.put("s", "s");
        followTags.put("strike", "s");
    }


    private boolean createBookmarks = true;
    private Section h1, h2, h3;


    private static Font defaultFont = new Font("helvetica", "12pt", 0, "#000000");
    private String encoding = WINANSI; //CP1252?


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class using a DocListener.
   *  @param createBookmarks Flag used to specify whether to create bookmarks
   *  from HTML headers (e.g. h1, h2, h3).
   */
    public HtmlParser(DocListener document, boolean createBookmarks) {
        this.document = document;
        this.createBookmarks = createBookmarks;
    }


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Overloaded constructor for legacy use.
   */
    public HtmlParser(DocListener document) {
        this(document, false);
    }

    public void setStyleSheet(StyleSheet styleSheet) {
        this.styleSheet = styleSheet;
    }

    public StyleSheet getStyleSheet() {
        return styleSheet;
    }

    public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

    public void parse(java.io.Reader reader) throws java.io.IOException {
        SimpleXMLParser.parse(this, null, reader, true);
    }


//    public void setInterfaceProps(HashMap interfaceProps) {
//        this.interfaceProps = interfaceProps;
//        FontFactoryImp ff = null;
//        if (interfaceProps != null){
//            ff = (FontFactoryImp) interfaceProps.get("font_factory");
//        }
//        if (ff != null) factoryProperties.setFontImp(ff);
//    }
//
//    public HashMap getInterfaceProps() {
//        return interfaceProps;
//    }
//
//    public static ArrayList parseToList(java.io.Reader reader, StyleSheet styleSheet) throws java.io.IOException {
//        return parseToList(reader, styleSheet, null);
//    }
//
//    public static ArrayList parseToList(java.io.Reader reader, StyleSheet styleSheet, HashMap interfaceProps) throws java.io.IOException {
//        HTMLWorker worker = new HTMLWorker(null);
//        if (styleSheet != null) worker.styleSheet = styleSheet;
//        worker.document = worker;
//        worker.setInterfaceProps(interfaceProps);
//        worker.objectList = new ArrayList();
//        worker.parse(reader);
//        return worker.objectList;
//    }



  //**************************************************************************
  //** startDocument
  //**************************************************************************
  /** Used to initialize the document
   */
    public void startDocument() {
        HashMap h = new HashMap();
        styleSheet.applyStyle("body", h);
		chain.add("body", h);
    }


  //**************************************************************************
  //** startElement
  //**************************************************************************
  /** Used to process the start of a new html element.
   *  @param tag HTML tag name (e.g. "div", "table", "span", etc).
   *  @param attr Tag attributes (e.g. "width", "class", "style", etc).
   */
    public void startElement(String tag, HashMap attr) {
        if (!tagsSupported.containsKey(tag)) return;
        try {


          //Any text collected before the body tag is invalid and should not
          //go into the PDF document!
            if (tag.equals(HtmlTags.BODY)) currentParagraph = null;


          //Add "follow" tags to the chain
            String follow = (String) followTags.get(tag);
            if (follow != null) {
                HashMap prop = new HashMap();
                prop.put(follow, null);
                chain.add(follow, prop);
                return;
            }

            styleSheet.applyStyle(tag, attr);

          //Get styles defined for this element
            Style style = styleSheet.getElementStyle(tag, attr);

            if (tag.equals(HtmlTags.ANCHOR)) {
                chain.add(tag, attr);
                if (currentParagraph==null) currentParagraph = new Paragraph();
                stack.push(currentParagraph);
                currentParagraph = new Paragraph();
                return;
            }
            if (tag.equals(HtmlTags.NEWLINE)) {
                if (currentParagraph==null) currentParagraph = new Paragraph();
                currentParagraph.add(createChunk("\n", style));
                return;
            }
            if (tag.equals(HtmlTags.HORIZONTALRULE)) {
                // Attempting to duplicate the behavior seen on Firefox with
                // http://www.w3schools.com/tags/tryit.asp?filename=tryhtml_hr_test
                // where an initial break is only inserted when the preceding element doesn't
                // end with a break, but a trailing break is always inserted.
                boolean addLeadingBreak = true;
                if (currentParagraph == null) {
                    currentParagraph = new Paragraph();
                    addLeadingBreak = false;
                }
                if (addLeadingBreak) { // Not a new paragraph
                    int numChunks = currentParagraph.getChunks().size();
                    if (numChunks == 0 || ((Chunk)(currentParagraph.getChunks().get(numChunks - 1))).getContent().endsWith("\n")){
                        addLeadingBreak = false;
                    }
                }
                String align = (String) attr.get("align");
                int hrAlign = Element.ALIGN_CENTER;
                if (align != null) {
                    if (align.equalsIgnoreCase("left")) hrAlign = Element.ALIGN_LEFT;
                    if (align.equalsIgnoreCase("right")) hrAlign = Element.ALIGN_RIGHT;
                }
                String width = style.get("width");
                float hrWidth = 1;
                if (width != null) {
                    float tmpWidth = Markup.parseLength(width, Markup.DEFAULT_FONT_SIZE);
                    if (tmpWidth > 0) hrWidth = tmpWidth;
                    if (!width.endsWith("%")) hrWidth = 100; // Treat a pixel width as 100% for now.
                }
                String size = (String) attr.get("size");
                float hrSize = 1;
                if (size != null) {
                    float tmpSize = Markup.parseLength(size, Markup.DEFAULT_FONT_SIZE);
                    if (tmpSize > 0) hrSize = tmpSize;
                }
                if (addLeadingBreak) currentParagraph.add(Chunk.NEWLINE);
                currentParagraph.add(new LineSeparator(hrSize, hrWidth, null, hrAlign, currentParagraph.getLeading()/2));
                currentParagraph.add(Chunk.NEWLINE);
                return;
            }
            if (tag.equals(HtmlTags.CHUNK) || tag.equals(HtmlTags.SPAN)) {
                chain.add(tag, attr);
                return;
            }
            if (tag.equals(HtmlTags.IMAGE)) {
                String src = (String) attr.get(ElementTags.SRC);
                if (src == null) return;
                chain.add(tag, attr);
                Image img = null;
                if (interfaceProps != null) {
                    ImageProvider ip = (ImageProvider) interfaceProps.get("img_provider");
                    if (ip != null) img = ip.getImage(src, attr, chain, document);
                    if (img == null) {
                        HashMap images = (HashMap) interfaceProps.get("img_static");
                        if (images != null) {
                            Image tim = (Image) images.get(src);
                            if (tim != null) img = Image.getInstance(tim);
                        }
                        else {
                            if (!src.startsWith("http")) { // relative src references only
                                String baseurl = (String) interfaceProps.get("img_baseurl");
                                if (baseurl != null) {
                                    src = baseurl + src;
                                    img = Image.getInstance(src);
                                }
                            }
                        }
                    }
                }
                if (img == null) {
                    if (src.startsWith("data:image/")){
                        byte[] b = com.lowagie.text.pdf.codec.Base64.decode(
                            src.substring(src.indexOf(",")+1));
                        img = Image.getInstance(b);
                    }
                    else{
                        /*
                        if (!src.startsWith("http")) {
                            String path = chain.getProperty("image_path");
                            if (path == null) path = "";
                            src = new java.io.File(path, src).getPath();
                        }
                        */
                        img = Image.getInstance(src);
                    }
                }

              //Set padding
                String[] padding = style.getPadding();
                float n,s,e,w; n=s=e=w=0f;
                if (padding[0]!=null) n += Markup.getPoints(padding[0]);
                if (padding[1]!=null) e += Markup.getPoints(padding[1]);
                if (padding[2]!=null) s += Markup.getPoints(padding[2]);
                if (padding[3]!=null) w += Markup.getPoints(padding[3]);
                //String before = (String) attr.get("before");
                //String after = (String) attr.get("after");
                img.setSpacingBefore(n);
                img.setSpacingAfter(s);


              //Scale the image
                float width = img.getWidth();
                float height = img.getHeight();
                float dpi = 150f; dpi = 72f;
                float scalingFactor = Markup.DEFAULT_FONT_SIZE/dpi;
                width = width*scalingFactor;
                height = height*scalingFactor;


                String _width = (String) attr.get(ElementTags.WIDTH);
                String _height = (String) attr.get(ElementTags.HEIGHT);
                //String width = style.get("width");
                if (_width!=null) width = Markup.getPoints(_width);
                if (_height!=null) height = Markup.getPoints(_height);



                img.scaleAbsolute(width, height);


//                String width = style.get("width");
//                String height = style.get("height");
//                if (width==null) width = img.getWidth() + "";
//                if (height==null) height = img.getHeight() + "";
//                float actualFontSize = 0; //Markup.parseLength(chain.getProperty(ElementTags.SIZE), Markup.DEFAULT_FONT_SIZE);
//                if (actualFontSize <= 0f) actualFontSize = Markup.DEFAULT_FONT_SIZE;
//                float widthInPoints = Markup.parseLength(width, actualFontSize);
//                float heightInPoints = Markup.parseLength(height, actualFontSize);
//                if (widthInPoints > 0 && heightInPoints > 0) {
//                    System.out.println(" **  widthInPoints: " + widthInPoints + ", heightInPoints: " + heightInPoints);
//                    img.scaleAbsolute(widthInPoints/2f, heightInPoints/2f);
//                }
//                else if (widthInPoints > 0) {
//                        heightInPoints = img.getHeight() * widthInPoints / img.getWidth();
//                        img.scaleAbsolute(widthInPoints, heightInPoints);
//                }
//                else if (heightInPoints > 0) {
//                    widthInPoints = img.getWidth() * heightInPoints / img.getHeight();
//                    img.scaleAbsolute(widthInPoints, heightInPoints);
//                }
//                else{
//                    System.out.println(" **  Shouldn't be here! " + img.getWidth() + ", " + img.getHeight());
//                    img.scaleAbsolute(100, 100);
//                }


                img.setWidthPercentage(0);

                String align = (String) attr.get("align");
                if (align != null) {
                    endElement("p");

                    int ralign = Image.MIDDLE;
                    if (align.equalsIgnoreCase("left")) ralign = Image.LEFT;
                    else if (align.equalsIgnoreCase("right")) ralign = Image.RIGHT;
                    img.setAlignment(ralign);

                    Img i = null;
                    boolean skip = false;
                    if (interfaceProps != null) {
                        i = (Img) interfaceProps.get("img_interface");
                        if (i != null)
                                skip = i.process(img, attr, chain, document);
                    }
                    if (!skip) document.add(img);
                    chain.remove(tag);
                }
                else {
                    chain.remove(tag);
                    if (currentParagraph == null) {
                        currentParagraph = createParagraph(style);
                    }
                    currentParagraph.add(new Chunk(img, 0, 0));
                }
                return;
            }
            endElement("p");


            if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3") ||
                tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {

                if (!attr.containsKey(ElementTags.SIZE)) {
                    int v = 7 - Integer.parseInt(tag.substring(1));
                    attr.put(ElementTags.SIZE, Integer.toString(v));
                }
                chain.add(tag, attr);
                return;
            }
            if (tag.equals(HtmlTags.UNORDEREDLIST)) {
                if (pendingLI) endElement(HtmlTags.LISTITEM);
                chain.add(tag, attr);
                com.lowagie.text.List list = new com.lowagie.text.List(false);

              //The following 2 setting should come from the style!
                list.setIndentationLeft(25);
                list.setListSymbol("\u2022");

                stack.push(list);
                return;
            }
            if (tag.equals(HtmlTags.ORDEREDLIST)) {
                if (pendingLI) endElement(HtmlTags.LISTITEM);
                chain.add(tag, attr);
                com.lowagie.text.List list = new com.lowagie.text.List(true);

              //The following setting should come from the style!
                list.setIndentationLeft(25);

                stack.push(list);
                return;
            }
            if (tag.equals(HtmlTags.LISTITEM)) {
                if (pendingLI) endElement(HtmlTags.LISTITEM);
                skipText = false;
                pendingLI = true;
                chain.add(tag, attr);
                ListItem item = createListItem(style);
                stack.push(item);
                return;
            }
            if (tag.equals(HtmlTags.BODY) || tag.equals("p")) {
                chain.add(tag, attr);
                return;
            }
            if (tag.equals(HtmlTags.PRE)) {
                chain.add(tag, attr);
                isPRE = true;
                return;
            }
            if (tag.equals(HtmlTags.ROW)) {
                if (pendingTR) endElement("tr");
                skipText = true;
                pendingTR = true;
                chain.add("tr", attr);
                return;
            }
            if (tag.equals(HtmlTags.CELL) || tag.equals(HtmlTags.HEADERCELL)) {
                if (pendingTD) endElement(tag);
                skipText = false;
                pendingTD = true;
                stack.push(new IncCell(tag, attr, style, chain, styleSheet));
                chain.add("td", style.getStyle());
                return;
            }
            if (tag.equals(HtmlTags.TABLE)) {
                chain.add(tag, attr);
                stack.push(new IncTable(style));
                tableState.push(new boolean[] { pendingTR, pendingTD });
                pendingTR = pendingTD = false;
                skipText = true;
                return;
            }
            if (tag.equals(HtmlTags.DIV)) {
                stack.push(new IncTable(style));
                stack.push(new IncCell("td", attr, style, chain, styleSheet));
                chain.add(tag, attr);
                return;
            }
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }


  //**************************************************************************
  //** endElement
  //**************************************************************************
  /** Used to process the end of an html element.
   */
    public void endElement(String tag) {


      //Find styles defined in the header and update the document StyleSheet
        if (tag.equals(HtmlTags.STYLE)){
            styleSheet.update(new StyleSheet(cstr(currentParagraph)));
            currentParagraph = null;
        }

        if (!tagsSupported.containsKey(tag)) return;
        try {
            String follow = (String) followTags.get(tag);
            if (follow != null) {
                chain.remove(follow);
                return;
            }

            HashMap attr = chain.getAttributes(tag);

            if (tag.equals("font") || tag.equals("span")) {
                chain.remove(tag);
                return;
            }
            if (tag.equals("a")) {
                if (currentParagraph == null) {
                        currentParagraph = new Paragraph();
                }
                boolean skip = false;
                if (interfaceProps != null) {
                        ALink i = (ALink) interfaceProps.get("alink_interface");
                        if (i != null)
                                skip = i.process(currentParagraph, chain);
                }
                if (!skip) {
                    String href = (String) attr.get("href"); //chain.getProperty("href");
                    if (href != null) {
                        ArrayList chunks = currentParagraph.getChunks();
                        int size = chunks.size();
                        for (int k = 0; k < size; ++k) {
                            Chunk ck = (Chunk) chunks.get(k);
                            ck.setAnchor(href);
                        }
                    }
                }
                Paragraph tmp = (Paragraph) stack.pop();
                Phrase tmp2 = new Phrase();
                tmp2.add(currentParagraph);
                tmp.add(tmp2);
                currentParagraph = tmp;
                chain.remove("a");
                return;
            }
            if (tag.equals("br")) {
                return;
            }
            if (currentParagraph != null) {


                Style style = styleSheet.getElementStyle(tag, attr);

                if (tag.equals("h1") && createBookmarks){
                    h1 = new Section(wrap(currentParagraph, style));
                    h1.setBookmarkTitle(cstr(currentParagraph));
                    document.add(h1);
                }
                else if(tag.equals("h2") && createBookmarks){
                    h2 = new Section(wrap(currentParagraph, style));
                    h2.setBookmarkTitle(cstr(currentParagraph));
                    //if (h1!=null) h1.add(h2); else document.add(h2);
                    document.add(h2);
                }
                else{

                    if (stack.empty()) document.add(wrap(currentParagraph, style));
                    else {

                        Object obj = stack.pop();
                        if (obj instanceof TextElementArray) {
                            TextElementArray current = (TextElementArray) obj;
                            boolean b = current.add(currentParagraph);
                            if (!b){
                                System.out.println("\r\nFailed to insert element! Rendering " + tag);
                              //If we're here, the element was not successfully
                              //added to the TextElementArray so let's render
                              //it instead!

                              //First, update the style
                                if (obj instanceof com.lowagie.text.List){
                                    com.lowagie.text.List list = (com.lowagie.text.List) obj;
                                    style.put("margin-left", list.getIndentationLeft()+"");
                                    style.put("margin-right", list.getIndentationRight()+"");
                                }

                              //Now add the element to the document
                                document.add(wrap(currentParagraph, style));
                            }
                        }
                        else{
                            System.out.println("\r\nNot a TextElementArray? " + tag);
                        }
                        stack.push(obj);
                    }
                }
            }
            currentParagraph = null;

            if (tag.equals(HtmlTags.UNORDEREDLIST) || tag.equals(HtmlTags.ORDEREDLIST)) {
                if (pendingLI) endElement(HtmlTags.LISTITEM);
                skipText = false;
                chain.remove(tag);
                if (stack.empty()) return;
                Object obj = stack.pop();
                if (!(obj instanceof com.lowagie.text.List)) {
                    stack.push(obj);
                    return;
                }
                if (stack.empty()) document.add((Element) obj);
                else ((TextElementArray) stack.peek()).add(obj);
                return;
            }
            if (tag.equals(HtmlTags.LISTITEM)) {
                pendingLI = false;
                skipText = true;
                chain.remove(tag);
                if (stack.empty()) return;

                Object obj = stack.pop();
                if (!(obj instanceof ListItem)) {
                    stack.push(obj);
                    return;
                }
                if (stack.empty()) {
                    document.add((Element) obj);
                    return;
                }
                Object list = stack.pop();
                if (!(list instanceof com.lowagie.text.List)) {
                    stack.push(list);
                    return;
                }

                ListItem item = (ListItem) obj;
                ((com.lowagie.text.List) list).add(item);
                ArrayList cks = item.getChunks();
                if (!cks.isEmpty()){
                    item.getListSymbol().setFont(((Chunk)cks.get(0)).getFont());
                }
                stack.push(list);
                return;
            }
            if (tag.equals(HtmlTags.BODY)) {
                chain.remove(tag);
                return;
            }
            if (tag.equals(HtmlTags.PRE)) {
                chain.remove(tag);
                isPRE = false;
                return;
            }
            if (tag.equals("p")) {
                chain.remove(tag);
                return;
            }
            if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3") ||
                tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {

                chain.remove(tag);
                return;
            }
            if (tag.equals("td") || tag.equals("th")) {
                pendingTD = false;
                chain.remove("td");
                skipText = true;
                return;
            }
            if (tag.equals("tr")) {
                if (pendingTD) endElement("td");
                pendingTR = false;
                chain.remove("tr");
                ArrayList cells = new ArrayList();
                IncTable table = null;
                while (true) {
                    Object obj = stack.pop();
                    if (obj instanceof IncCell) {
                        cells.add(((IncCell) obj).getCell());
                    }
                    if (obj instanceof IncTable) {
                        table = (IncTable) obj;
                        break;
                    }
                }
                table.addCols(cells);
                table.endRow();
                stack.push(table);
                skipText = true;
                return;
            }
            if (tag.equals(HtmlTags.TABLE)) {
                if (pendingTR) endElement("tr");
                chain.remove(tag);
                IncTable table = (IncTable) stack.pop();
                addTable(table.buildTable());

                boolean state[] = (boolean[]) tableState.pop();
                pendingTR = state[0];
                pendingTD = state[1];
                skipText = false;
                return;
            }
            if (tag.equals(HtmlTags.DIV)) {
                chain.remove(tag);
                IncCell cell = (IncCell) stack.pop();
                IncTable table = (IncTable) stack.pop();
                table.addCol(cell.getCell());
                table.endRow();
                addTable(table.buildTable());
                return;
            }
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }


    private void addTable(PdfPTable tb) throws Exception {

        tb.setSplitRows(true);
        if (stack.empty()){
            document.add(tb);
        }
        else {

            Object obj = stack.peek();
            if (obj instanceof IncCell){

              //Tables inside of cells aren't rendered correctly b/c
              //the cell padding of the parent cell are ignored. The
              //following is a workaround.
                String prevTag = chain.getLastTag();
                Style style = styleSheet.getElementStyle(prevTag, chain.getAttributes(prevTag));
                String[] padding = style.getPadding();
                float n,s,e,w; n=s=e=w=0f;
                if (padding[0]!=null) n += Markup.getPoints(padding[0]);
                if (padding[1]!=null) e += Markup.getPoints(padding[1]);
                if (padding[2]!=null) s += Markup.getPoints(padding[2]);
                if (padding[3]!=null) w += Markup.getPoints(padding[3]);
                tb.setSpacingBefore(n);
                tb.setSpacingAfter(s);
            }

            ((TextElementArray) obj).add(tb);
        }
    }


  //**************************************************************************
  //** endDocument
  //**************************************************************************
  /** Used to finalize the document
   */
    public void endDocument() {
        try {
            for (int k = 0; k < stack.size(); ++k){
                document.add((Element) stack.elementAt(k));
            }
            if (currentParagraph != null) document.add(currentParagraph);
            currentParagraph = null;
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }


  //**************************************************************************
  //** text
  //**************************************************************************
  /** Implements the SimpleXMLDocHandler.text() method. Creates a new
   *  paragraph from a block of text.
   */
    public void text(String str) {
        if (skipText) return;

      //Get style for the current tag
        String tag = chain.getLastTag();
        HashMap attr = chain.getAttributes(tag);
        Style style = styleSheet.getElementStyle(tag, attr);


        String content = str;
        if (isPRE) {
            if (currentParagraph == null) {
                currentParagraph = createParagraph(style);
            }
            Chunk chunk = createChunk(content, style);
            currentParagraph.add(chunk);
            return;
        }
        if (content.trim().length() == 0 && content.indexOf(' ') < 0) {
            return;
        }

        StringBuffer buf = new StringBuffer();
        int len = content.length();
        char character;
        boolean newline = false;
        for (int i = 0; i < len; i++) {
            switch (character = content.charAt(i)) {
            case ' ':
                if (!newline) {
                    buf.append(character);
                }
                break;
            case '\n':
                if (i > 0) {
                    newline = true;
                    buf.append(' ');
                }
                break;
            case '\r':
                break;
            case '\t':
                break;
            default:
                newline = false;
                buf.append(character);
            }
        }
        if (currentParagraph == null) {
            currentParagraph = createParagraph(style);
        }

        Chunk chunk = createChunk(buf.toString(), style);
        currentParagraph.add(chunk);
    }



  //**************************************************************************
  //** wrap
  //**************************************************************************
  /** Used to apply padding, margins, and borders, to a given paragraph.
   */
    private Paragraph wrap(Paragraph paragraph, Style style){
        Border[] border = style.getBorders();
        String[] padding = style.getPadding();
        String[] margin = style.getMargins();

        boolean hasPadding = false;
        for (String p : padding){
            if (p!=null){
                hasPadding = true;
                break;
            }
        }

        boolean hasBorders = false;
        for (Border b : border){
            if (b!=null){
                hasBorders = true;
                break;
            }
        }

      //Compute margins
        float n = 0;
        float e = 0;
        float s = 0;
        float w = 0;
        if (margin[0]!=null) n += Markup.getPoints(margin[0]);
        if (margin[1]!=null) e += Markup.getPoints(margin[1]);
        if (margin[2]!=null) s += Markup.getPoints(margin[2]);
        if (margin[3]!=null) w += Markup.getPoints(margin[3]);


      //Apply borders and padding
        if (hasPadding || hasBorders){

            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            IncCell cell = new IncCell("td", null, style, null, null);
            cell.add(paragraph);
            table.addCell(cell.getCell());

            Paragraph p = new Paragraph();
            p.setLeading(0, 0);

            p.setSpacingBefore(n);
            p.setSpacingAfter(s);
            p.setIndentationLeft(w);
            p.setIndentationRight(e);


            p.add(table);
            return p;
        }
        else{
            paragraph.setSpacingBefore(n);
            paragraph.setSpacingAfter(s);
            paragraph.setIndentationLeft(w);
            paragraph.setIndentationRight(e);
            return paragraph;
        }


    }


  //**************************************************************************
  //** cstr
  //**************************************************************************
  /** Returns a Paragraph as a string.
   */
    private String cstr(Paragraph p){
        if (p==null) return null;
        StringBuffer str = new StringBuffer();
        for (Object obj : p.getChunks()){
            str.append(obj.toString());
        }
        return str.toString();
    }


    private Chunk createChunk(String text, Style style) {
        Font font = style.getFont();
        if (!font.isComplete()){
            ArrayList list = chain.getList();
            for (int k = list.size() - 1; k >= 0; --k) {
                Object[] arr = (Object[]) list.get(k);
                String tag = (String) arr[0];
                HashMap attr = (HashMap) arr[1];
                Style s = styleSheet.getElementStyle(tag, attr);
                Font f = s.getFont();
                if (font.getFace()==null) font.setFace(f.getFace());
                if (font.getSize()==null) font.setSize(f.getSize());
                if (font.getStyle()==null) font.setStyle(f.getStyle());
                if (font.getColor()==null) font.setColor(f.getColor());
                if (font.isComplete()) break;
            }
        }


        if (!font.isComplete()){
            if (font.getFace()==null) font.setFace(defaultFont.getFace());
            if (font.getSize()==null) font.setSize(defaultFont.getSize());
            if (font.getStyle()==null) font.setStyle(defaultFont.getStyle());
            if (font.getColor()==null) font.setColor(defaultFont.getColor());
        }

        Chunk ck = new Chunk(text, font.getFont(encoding));


        /*
            float size = font.getSize();
            size /= 2;
            if (props.hasProperty("sub"))
                    ck.setTextRise(-size);
            else if (props.hasProperty("sup"))
                    ck.setTextRise(size);
            ck.setHyphenation(getHyphenation(props));
        */
        return ck;
    }

    private Paragraph createParagraph(Style style) {
        Paragraph p = new Paragraph();
        updateStyle(p, style);
        return p;
    }

    private ListItem createListItem(Style style) {
        ListItem p = new ListItem();
        updateStyle(p, style);
        return p;
    }

    private static void updateStyle(Paragraph p, Style style) {
//		String value = style.get("align");
//                System.out.println("Paragraph Alignment: " + value);
//		if (value != null) {
//			if (value.equalsIgnoreCase("center"))
//				p.setAlignment(Element.ALIGN_CENTER);
//			else if (value.equalsIgnoreCase("right"))
//				p.setAlignment(Element.ALIGN_RIGHT);
//			else if (value.equalsIgnoreCase("justify"))
//				p.setAlignment(Element.ALIGN_JUSTIFIED);
//		}
//		p.setHyphenation(getHyphenation(props));
//		setParagraphLeading(p, props.getProperty("leading"));
//		value = props.getProperty("before");
//		if (value != null) {
//			try {
//				p.setSpacingBefore(Float.parseFloat(value));
//			} catch (Exception e) {
//			}
//		}
//		value = props.getProperty("after");
//		if (value != null) {
//			try {
//				p.setSpacingAfter(Float.parseFloat(value));
//			} catch (Exception e) {
//			}
//		}
//		value = props.getProperty("extraparaspace");
//		if (value != null) {
//			try {
//				p.setExtraParagraphSpace(Float.parseFloat(value));
//			} catch (Exception e) {
//			}
//		}
    }




    public boolean add(Element element) throws DocumentException {
        objectList.add(element);
        return true;
    }

    public void clearTextWrap() throws DocumentException {
    }

    public void close() {
    }

    public boolean newPage() {
        return true;
    }

    public void open() {
    }

    public void resetFooter() {
    }

    public void resetHeader() {
    }

    public void resetPageCount() {
    }

    public void setFooter(com.lowagie.text.HeaderFooter footer) {
    }

    public void setHeader(com.lowagie.text.HeaderFooter header) {
    }

    public boolean setMarginMirroring(boolean marginMirroring) {
        return false;
    }

    public boolean setMarginMirroringTopBottom(boolean marginMirroring) {
        return false;
    }

    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        return true;
    }

    public void setPageCount(int pageN) {
    }

    public boolean setPageSize(Rectangle pageSize) {
        return true;
    }
}