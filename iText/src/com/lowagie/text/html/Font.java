package com.lowagie.text.html;
import com.lowagie.text.FontFactory;
import com.lowagie.text.FontFactoryImp;
import static com.lowagie.text.pdf.BaseFont.WINANSI;
import static com.lowagie.text.pdf.BaseFont.CP1252;

//******************************************************************************
//**  Font Class
//******************************************************************************
/**
 *   Used to represent a font style.
 *
 ******************************************************************************/

public class Font {
    private String face;
    private Float size;//<-- font size is in points!
    private Integer style;
    private java.awt.Color color;
    private static FontFactoryImp fontImp = FontFactory.getFontImp();
//    static{
//          fontImp.register("c:/windows/fonts/arial.ttf", "arial");
//    }

    public Font(String face, Float size, Integer style, java.awt.Color color){
        setFace(face);
        setSize(size);
        setStyle(style);
        setColor(color);
    }

    public Font(String face, String size, Integer style, String color){
        setFace(face);
        setSize(size);
        setStyle(style);
        setColor(color);
    }


  //**************************************************************************
  //** setFace
  //**************************************************************************
  /** Used to set the font face/family.
   *  @param face Comma delimited list of fonts (e.g. "arial,helvetica,sans-serif").
   */
    public void setFace(String face){

        if (face != null) {
            for (String f : face.split(",")){
                face = f.trim();
                if (fontImp.isRegistered(face)){
                    this.face = face;
                    return;
                }
            }
        }
        this.face = null;
    }

    public String getFace(){
        return face;
    }


  //**************************************************************************
  //** setSize
  //**************************************************************************
  /** Used to set the font size, in points.
   */
    public void setSize(Float size){
        this.size = size;
    }


  //**************************************************************************
  //** setSize
  //**************************************************************************
  /** Used to set the font size.
   *  @param value String representing a font size (e.g. "12pt" or "72px").
   */
    public void setSize(String value){
        if (value != null){
            value = value.trim();
            try{
                if (value.endsWith("px")){
                  //iText fonts are in points, not pixels. Need to convert
                  //pixels for points (e.g. pixel * .75 if doc is 96ppi)
                    value = value.substring(0, value.length()-2).trim();
                    size = Float.parseFloat(value) * 0.75f;
                    return;
                }
                else{
                    if (value.endsWith("pt")){
                        value = value.substring(0, value.length()-2).trim();
                    }
                    size = Float.parseFloat(value);
                    return;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        size = null;
    }

    public Float getSize(){
        return size;
    }

    public void setStyle(Integer style){
        this.style = style;
    }

    public Integer getStyle(){
        return style;
    }

    public void setColor(String color){
        this.color = Markup.decodeColor(color);
    }

    public void setColor(java.awt.Color color){
        this.color = color;
    }

    public java.awt.Color getColor(){
        return color;
    }


  //**************************************************************************
  //** isComplete
  //**************************************************************************
  /** Used to indicate whether all the requisite attributes have been set.
   */
    public boolean isComplete(){
        return (face!=null && size!=null && style!=null && color!=null);
    }


  //**************************************************************************
  //** getFont
  //**************************************************************************
  /** Generates a PDF Font for with this style. Returns null if all the
   *  requisite attributes have not been set.
   *
   *  @param encoding Character encoding ("WINANSI", "CP1252", etc).
   */
    public com.lowagie.text.Font getFont(String encoding){
        if (!isComplete()) return null;
        if (encoding==null) encoding = WINANSI; //CP1252?
//        System.out.println("face: " + face);
//        System.out.println("size: " + size);
//        System.out.println("style: " + style);
//        System.out.println("color: " + color);
//        System.out.println("encoding: " + encoding);
        return fontImp.getFont(face, encoding, true, size, style, color);
    }
}