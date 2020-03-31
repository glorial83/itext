package com.lowagie.text.html;

//******************************************************************************
//**  Border Class
//******************************************************************************
/**
 *   Used to represent a border style associated with an HTML element.
 *
 ******************************************************************************/

public class Border {

    private String width;
    private String color;
    private String style;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of this class.
   *  @param val String representing a border style (e.g. "1px solid #333333;")
   */
    public Border(String val) throws Exception{
        val = val.trim();
        while (val.endsWith(";")) val = val.substring(0, val.length()-1).trim();
        while (val.contains("  ")) val = val.replace("  ", " ");
        String[] arr = val.split(" ");
        width=arr[0];
        if (arr.length>1) style = arr[1];
        if (arr.length>2) color = arr[2];
    }

    public String getWidth(){
        return width;
    }

    public String getColor(){
        return color;
    }

    public String getStyle(){
        return style;
    }
}