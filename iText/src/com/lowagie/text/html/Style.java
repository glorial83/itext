package com.lowagie.text.html;
import java.util.HashMap;

//******************************************************************************
//**  Style Class
//******************************************************************************
/**
 *   Used to represent a set of styles for an HTML element.
 *
 ******************************************************************************/

public class Style {

    private HashMap<String, String> style;

    public Style(HashMap<String, String> style){
        if (style==null) this.style = new HashMap<String, String>();
        else this.style = style;
    }

    public java.util.Iterator<String> getKeys(){
        return style.keySet().iterator();
    }

    public String get(String key){
        return style.get(key);
    }

    public void put(String key, String value){
        style.put(key, value);
    }

  //**************************************************************************
  //** getBorders
  //**************************************************************************
  /** Returns an array with 4 entries representing border styles defined for a
   *  given element. The entries in the array correspond to the 4 sides of an
   *  element: 0-top, 1-right, 2-bottom, 3-left. A null entry indicates that
   *  no style is defined for the border.
   */
    public Border[] getBorders(){
        Border[] arr = new Border[4];

      //If "border-style:none;" return
        String borderStyle = style.get("border-style");
        if (borderStyle!=null && borderStyle.trim().equals("none")) return arr;


      //Set style for all four sides
        try{
            Border border = new Border(style.get("border"));
            for (int i=0; i<arr.length; i++){
                arr[i] = border;
            }
        }
        catch(Exception e){
        }



      //Set style for individual sides
        java.util.Iterator<String> it = style.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            String val = style.get(key);
            try{
                if (key.startsWith("border-")){
                    key = key.substring(7);
                    if (key.equals("top")) arr[0] = new Border(val);
                    if (key.equals("right")) arr[1] = new Border(val);
                    if (key.equals("bottom")) arr[2] = new Border(val);
                    if (key.equals("left")) arr[3] = new Border(val);
                }
            }
            catch(Exception e){
            }
        }
        return arr;
    }


  //**************************************************************************
  //** getPadding
  //**************************************************************************
  /** Returns an array with 4 entries representing padding styles defined for
   *  a given element. The entries in the array correspond to the 4 sides of
   *  an element: 0-top, 1-right, 2-bottom, 3-left. A null entry indicates
   *  that no style is defined for the border.
   */
    public String[] getPadding(){
        String[] atr = new String[4];


        String padding = style.get("padding");
        if (padding!=null){
            padding = padding.trim();
            while (padding.endsWith(";")) padding = padding.substring(0, padding.length()-1).trim();
            while (padding.contains("  ")) padding = padding.replace("  ", " ");
            String[] arr = padding.split(" ");
            if (arr.length==1){
                String val = arr[0].trim();
                if (val.length()>0){
                    atr[0] = atr[1] = atr[2] = atr[3] = val;
                }
            }
            else{
                int i = 0;
                for (String val : arr){
                    val = val.trim();
                    if (val.length()>0){
                        atr[i] = val;
                        i++;
                    }
                }
            }
        }


      //Set style for individual sides
        java.util.Iterator<String> it = style.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            String val = style.get(key);
            try{
                if (key.startsWith("padding-")){
                    val = val.trim();
                    if (val.length()>0){
                        key = key.substring(8);
                        if (key.equals("top")) atr[0] = val;
                        if (key.equals("right")) atr[1] = val;
                        if (key.equals("bottom")) atr[2] = val;
                        if (key.equals("left")) atr[3] = val;
                    }
                }
            }
            catch(Exception e){
            }
        }
        return atr;
    }


  //**************************************************************************
  //** getPadding
  //**************************************************************************
  /** Returns an array with 4 entries representing padding styles defined for
   *  a given element. The entries in the array correspond to the 4 sides of
   *  an element: 0-top, 1-right, 2-bottom, 3-left. A null entry indicates
   *  that no style is defined for the border.
   */
    public String[] getMargins(){
        String[] atr = new String[4];


        String padding = style.get("margin");
        if (padding!=null){
            padding = padding.trim();
            while (padding.endsWith(";")) padding = padding.substring(0, padding.length()-1).trim();
            while (padding.contains("  ")) padding = padding.replace("  ", " ");
            String[] arr = padding.split(" ");
            if (arr.length==1){
                String val = arr[0].trim();
                if (val.length()>0){
                    atr[0] = atr[1] = atr[2] = atr[3] = val;
                }
            }
            else{
                int i = 0;
                for (String val : arr){
                    val = val.trim();
                    if (val.length()>0){
                        atr[i] = val;
                        i++;
                    }
                }
            }
        }


      //Set style for individual sides
        java.util.Iterator<String> it = style.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            String val = style.get(key);
            try{
                if (key.startsWith("margin-")){
                    val = val.trim();
                    if (val.length()>0){
                        key = key.substring(7);
                        if (key.equals("top")) atr[0] = val;
                        if (key.equals("right")) atr[1] = val;
                        if (key.equals("bottom")) atr[2] = val;
                        if (key.equals("left")) atr[3] = val;
                    }
                }
            }
            catch(Exception e){
            }
        }
        return atr;
    }


  //**************************************************************************
  //** getFont
  //**************************************************************************
  /** Returns the font style defined for a given element.
   */
    public Font getFont(){

        String font = style.get("font");
        if (font!=null){
            font = font.trim();
            //TODO: Need to parse one-line font properties like this:
            //font:15px arial,sans-serif;
            //font: 14px/20px helvetica,arial,hirakakupro-w3,osaka,"ms pgothic",sans-serif;
            //font:italic bold 12px/30px Georgia, serif;
        }


        String color = style.get("color");
        String face = style.get("font-family");
        String size = style.get("font-size");

        Integer s=null;
        String value = style.get("font-weight");
        if (value != null){
            value = value.trim();
            if (value.equalsIgnoreCase("bold")){
                if (s==null) s = 0;
                s |= com.lowagie.text.Font.BOLD;
            }
        }
        value = style.get("font-style");
        if (value != null){
            value = value.trim();
            if (value.equalsIgnoreCase("italic")){
                if (s==null) s = 0;
                s |= com.lowagie.text.Font.ITALIC;
            }
        }
        value = style.get("text-decoration");
        if (value != null){
            value = value.trim();
            if (value.equalsIgnoreCase("underline")){
                if (s==null) s = 0;
                s |= com.lowagie.text.Font.UNDERLINE;
            }
            else if (value.equalsIgnoreCase("line-through")){
                if (s==null) s = 0;
                s |= com.lowagie.text.Font.STRIKETHRU;
            }
        }

        return new Font(face, size, s, color);
    }


  //**************************************************************************
  //** toString
  //**************************************************************************
  /** Returns a string with all the styles represented by this class. The
   *  string can me used to create a "style" attribute for an HTML element.
   */
    public String toString(){

      //Convert style hashmap into a string
        StringBuffer str = new StringBuffer();
        java.util.Iterator<String> it = style.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            String val = style.get(key);
            if (val!=null){
                val = val.trim();
                while (val.endsWith(";")) val = val.substring(0, val.length()-1).trim();
                if (val.length()>0){
                    str.append(key + ": " + val + ";");
                }
            }
        }

        return str.toString();
    }

	public HashMap<String, String> getStyle() {
		return style;
	}

	public void setStyle(HashMap<String, String> style) {
		this.style = style;
	}

}