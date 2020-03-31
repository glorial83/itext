package com.lowagie.text.html;
import com.lowagie.text.Paragraph;

public interface ALink {
    boolean process(Paragraph current, ChainedProperties cprops);
}