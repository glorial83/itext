/*
* $Id: Markup.java 3654 2009-01-21 16:11:00Z blowagie $
*
* Copyright 2001, 2002 by Bruno Lowagie.
*
* Contributions by:
* Lubos Strapko
*
*/
package com.lowagie.text.html;

import java.awt.Color;
import java.util.Properties;
import java.util.StringTokenizer;


public class Markup extends HtmlTags {



  /** a default value for font-size
   *  @since 2.1.3
   */
    public static final float DEFAULT_FONT_SIZE = 12f;


  //**************************************************************************
  //** getPoints
  //**************************************************************************
  /** HTML tags commonly use pixels as a unit of measure. However, iText units
   *  are in points, not pixels. This method is used to convert pixels to
   *  points (e.g. pixel * .75 if doc is 96ppi).
   */
    public static float getPoints(String pixels){
        float x = 0f;
        try{
            pixels = pixels.trim().toLowerCase();
            if (pixels.endsWith("px")){
                pixels = pixels.substring(0, pixels.length()-2).trim();
            }
            x = Float.parseFloat(pixels) * 0.75f;
        }
        catch(Exception e){
        }
        return x;
    }



  /** Parses a length.
   *  @param string a length in the form of an optional + or -, followed by a
   *  number and a unit.
   *  @return a float
   */
    public static float parseLength(String string) {
        // TODO: Evaluate the effect of this.
        // It may change the default behavour of the methd if this is changed.
        // return parseLength(string, Markup.DEFAULT_FONT_SIZE);
        int pos = 0;
        int length = string.length();
        boolean ok = true;
        while (ok && pos < length) {
            switch (string.charAt(pos)) {
                case '+':
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':
                        pos++;
                        break;
                default:
                        ok = false;
            }
        }
        if (pos == 0) return 0f;
        if (pos == length) return Float.parseFloat(string + "f");
        float f = Float.parseFloat(string.substring(0, pos) + "f");

        string = string.substring(pos);
        // inches
        if (string.startsWith("in")) {
            return f * 72f;
        }
        // centimeters
        if (string.startsWith("cm")) {
            return (f / 2.54f) * 72f;
        }
        // millimeters
        if (string.startsWith("mm")) {
            return (f / 25.4f) * 72f;
        }
        // picas
        if (string.startsWith("pc")) {
            return f * 12f;
        }
        // default: we assume the length was measured in points
        return f;
    }

    /**
    * New method contributed by: Lubos Strapko
    *
    * @since 2.1.3
    */
    public static float parseLength(String string, float actualFontSize) {
    if (string == null)
    return 0f;
    int pos = 0;
    int length = string.length();
    boolean ok = true;
    while (ok && pos < length) {
    switch (string.charAt(pos)) {
    case '+':
    case '-':
    case '0':
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case '9':
    case '.':
            pos++;
            break;
    default:
            ok = false;
    }
    }
    if (pos == 0)
    return 0f;
    if (pos == length)
    return Float.parseFloat(string + "f");
    float f = Float.parseFloat(string.substring(0, pos) + "f");
    string = string.substring(pos);
    // inches
    if (string.startsWith("in")) {
    return f * 72f;
    }
    // centimeters
    if (string.startsWith("cm")) {
    return (f / 2.54f) * 72f;
    }
    // millimeters
    if (string.startsWith("mm")) {
    return (f / 25.4f) * 72f;
    }
    // picas
    if (string.startsWith("pc")) {
    return f * 12f;
    }
    // 1em is equal to the current font size
    if (string.startsWith("em")) {
    return f * actualFontSize;
    }
    // one ex is the x-height of a font (x-height is usually about half the
    // font-size)
    if (string.startsWith("ex")) {
    return f * actualFontSize / 2;
    }
    // default: we assume the length was measured in points
    return f;
    }

    /**
    * Converts a <CODE>Color</CODE> into a HTML representation of this <CODE>
    * Color</CODE>.
    *
    * @param s
    *            the <CODE>Color</CODE> that has to be converted.
    * @return the HTML representation of this <COLOR>Color </COLOR>
    */
    public static Color decodeColor(String s) {
        if (s == null) return null;
        s = s.toLowerCase().trim();
        try {
            return WebColors.getRGBColor(s);
        }
        catch(IllegalArgumentException iae) {
            return null;
        }
    }



    /**
    * This method parses a String with attributes and returns a Properties
    * object.
    *
    * @param string
    *            a String of this form: 'key1="value1"; key2="value2";...
    *            keyN="valueN" '
    * @return a Properties object
    */
    public static Properties parseAttributes(String string) {
    Properties result = new Properties();
    if (string == null)
    return result;
    StringTokenizer keyValuePairs = new StringTokenizer(string, ";");
    StringTokenizer keyValuePair;
    String key;
    String value;
    while (keyValuePairs.hasMoreTokens()) {
    keyValuePair = new StringTokenizer(keyValuePairs.nextToken(), ":");
    if (keyValuePair.hasMoreTokens())
            key = keyValuePair.nextToken().trim();
    else
            continue;
    if (keyValuePair.hasMoreTokens())
            value = keyValuePair.nextToken().trim();
    else
            continue;
    if (value.startsWith("\""))
            value = value.substring(1);
    if (value.endsWith("\""))
            value = value.substring(0, value.length() - 1);
    result.setProperty(key.toLowerCase(), value);
    }
    return result;
    }

  /** Removes the comments sections of a String.
   *  @param string the original String
   *  @param startComment the String that marks the start of a Comment section
   *  @param endComment the String that marks the end of a Comment section.
   *  @return the String stripped of its comment section
   */
    public static String removeComment(String string, String startComment, String endComment) {
        StringBuffer result = new StringBuffer();
        int pos = 0;
        int end = endComment.length();
        int start = string.indexOf(startComment, pos);
        while (start > -1) {
            result.append(string.substring(pos, start));
            pos = string.indexOf(endComment, start) + end;
            start = string.indexOf(startComment, pos);
        }
        result.append(string.substring(pos));
        return result.toString();
    }
}