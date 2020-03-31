package com.lowagie.text.html;
import java.util.HashMap;
import java.util.ArrayList;
import com.lowagie.text.Element;
import com.lowagie.text.ElementListener;
import com.lowagie.text.Phrase;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.PdfPCell;

//******************************************************************************
//**  IncCell Class
//******************************************************************************
/**
 *   Used to represent a cell in a table.
 *
 ******************************************************************************/

public class IncCell implements TextElementArray {

    private ArrayList chunks = new ArrayList();
    private PdfPCell cell;

    /** Creates a new instance of IncCell */
    public IncCell(String tag, HashMap attr, Style style, ChainedProperties cprops, StyleSheet styleSheet) {

      //Process inputs
        if (attr==null) attr = new HashMap();
        if (style==null) style = new Style(new HashMap<String, String>());
        if (cprops==null) cprops = new ChainedProperties();
        if (styleSheet==null) styleSheet = new StyleSheet();


      //Find parent table style
        HashMap tableAttributes = cprops.getAttributes(HtmlTags.TABLE);

        Style tableStyle = styleSheet.getElementStyle("table", tableAttributes);

        if (tableAttributes!=null){
            String tableID = (String) tableAttributes.get("id");
            if (tableID!=null){
                Style _style = styleSheet.getElementStyle("#" + tableID + " " + tag, null);
                if (_style!=null){
                    java.util.Iterator<String> it = _style.getKeys();
                    while(it.hasNext()){
                        String key = it.next();
                        String val = _style.get(key);
                        style.put(key, val);
                    }
                }
            }

            String className = (String) tableAttributes.get(HtmlTags.HTML_ATTR_CSS_CLASS);
            if (className!=null){
            	if (styleSheet.tagMap != null) {
            		//class의 style과 attr로 준 style 합치기
                    if(styleSheet.tagMap.containsKey(className)) {
                    	HashMap<String, String> styleMap = style.getStyle();

                    	HashMap<String, String> temp = new HashMap<String, String>(styleSheet.tagMap.get(className));
                    	temp.putAll(styleMap);
                    	styleMap.putAll(temp);

                    	style.setStyle(styleMap);
                    }
                }
            }
        }


      //Create cell
        cell = new PdfPCell((Phrase)null);


      //Set colspan
        String value = (String) attr.get("colspan");
        if (value != null) cell.setColspan(Integer.parseInt(value));


      //Set horizontal alignment
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        value = (String) attr.get("align");

        if (value==null) value = style.get("align");
        if (value==null) value = style.get("text-align");

        if (value!=null) {
            if ("center".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            else if ("right".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            else if ("left".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            else if ("justify".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        }
        else{
            if (tag.equals("th")){
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            }
        }

      //Set vertical alignment
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        value = (String) attr.get("valign");
        if (value != null) {
            if ("top".equalsIgnoreCase(value))
                cell.setVerticalAlignment(Element.ALIGN_TOP);
            else if ("bottom".equalsIgnoreCase(value))
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        }


      //Set column wrapping
        boolean nowrap = false;
        value = (String) attr.get("nowrap");
        if (value!=null) nowrap = (value.equalsIgnoreCase("nowrap"));
        value = (String) style.get("white-space");
        if (value!=null) nowrap = (value.equalsIgnoreCase("nowrap"));
        cell.setNoWrap(nowrap);


      //Set width
        value = (String) attr.get("width");
        if (value==null) value = (String) style.get("width");
        if (value!=null) cell.setColumnWidth(value);


      //Set height
        value = (String) attr.get("height");
        if (value==null) value = (String) style.get("height");
        if (value!=null){

            if (value.endsWith("%")){
                //Rectangle pageSize
                //widthPercentage = totalWidth / (pageSize.getRight() - pageSize.getLeft()) * 100f;
            }
            else{
                try{
                    float heightInPoints = Markup.getPoints(value);
                    cell.setFixedHeight(heightInPoints);
                }
                catch(Exception e){
                }
            }
        }


      //Border
        cell.setBorderWidth(0);
        updateBorders(tableStyle.getBorders());
        updateBorders(style.getBorders());


      //Padding
        cell.setPadding(0);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        String[] padding = style.getPadding();

        if (padding[0]!=null) cell.setPaddingTop(Markup.getPoints(padding[0]));
        if (padding[1]!=null) cell.setPaddingRight(Markup.getPoints(padding[1]));
        if (padding[2]!=null) cell.setPaddingBottom(Markup.getPoints(padding[2]));
        if (padding[3]!=null) cell.setPaddingLeft(Markup.getPoints(padding[3]));


      //Background color
        value = style.get("background-color");
        if (value==null) value = (String) attr.get("bgcolor");
        cell.setBackgroundColor(Markup.decodeColor(value));
    }


  //**************************************************************************
  //** getBorders
  //**************************************************************************
  /** Used to stylize the cell borders. */
    private void updateBorders(Border[] border){
        if (border[0]!=null){
            cell.setBorderWidthTop(Markup.getPoints(border[0].getWidth()));
            cell.setBorderColorTop(Markup.decodeColor(border[0].getColor()));
        }
        if (border[1]!=null){
            cell.setBorderWidthRight(Markup.getPoints(border[1].getWidth()));
            cell.setBorderColorRight(Markup.decodeColor(border[1].getColor()));
        }
        if (border[2]!=null){
            cell.setBorderWidthBottom(Markup.getPoints(border[2].getWidth()));
            cell.setBorderColorBottom(Markup.decodeColor(border[2].getColor()));
        }
        if (border[3]!=null){
            cell.setBorderWidthLeft(Markup.getPoints(border[3].getWidth()));
            cell.setBorderColorLeft(Markup.decodeColor(border[3].getColor()));
        }
    }




    public boolean add(Object o) {
        if (!(o instanceof Element)) return false;
        cell.addElement((Element)o);
        return true;
    }

    public ArrayList getChunks() {
        return chunks;
    }

    public boolean process(ElementListener listener) {
        return true;
    }

    public int type() {
        return Element.RECTANGLE;
    }

    public PdfPCell getCell() {
        return cell;
    }

    /**
     * @see com.lowagie.text.Element#isContent()
     * @since	iText 2.0.8
     */
    public boolean isContent() {
        return true;
    }

    /**
     * @see com.lowagie.text.Element#isNestable()
     * @since	iText 2.0.8
     */
    public boolean isNestable() {
        return true;
    }
}