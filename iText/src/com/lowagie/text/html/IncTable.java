package com.lowagie.text.html;
import java.util.ArrayList;
import java.util.Collections;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

//******************************************************************************
//**  IncTable Class
//******************************************************************************
/**
 *   Used to create a new table.
 *
 ******************************************************************************/

public class IncTable {

    private Style style;
    private ArrayList rows = new ArrayList();
    private ArrayList cols;
    
    public IncTable(Style style) {
        this.style = style;
    }

//    public Style getStyle(){
//        return style;
//    }
    
    public void addCol(PdfPCell cell) {
        if (cols == null)
            cols = new ArrayList();
        cols.add(cell);
    }
    
    public void addCols(ArrayList ncols) {
        if (cols == null)
            cols = new ArrayList(ncols);
        else
            cols.addAll(ncols);
    }
    
    public void endRow() {
        if (cols != null) {
            Collections.reverse(cols);
            rows.add(cols);
            cols = null;
        }
    }
    
    public ArrayList getRows() {
        return rows;
    }



    public PdfPTable buildTable() {

        if (rows.isEmpty()) return new PdfPTable(1);


      //Get number of columns (inc colspan)
        int ncol = 0;
        ArrayList c0 = (ArrayList) rows.get(0);
        for (int k = 0; k < c0.size(); ++k) {
            ncol += ((PdfPCell)c0.get(k)).getColspan();
        }

      //Create table
        PdfPTable table = new PdfPTable(ncol);


      //Set alignment
        int hAlign = PdfPTable.ALIGN_LEFT;
        String align = style.get("align");
        if (align!=null){
            align=align.trim().toLowerCase();
            if (align.equals("center")) hAlign = PdfPTable.ALIGN_CENTER;
            else if(align.equals("right")) hAlign = PdfPTable.ALIGN_RIGHT;
        }
        table.setHorizontalAlignment(hAlign);
        

        
        

      //Set width
        String width = style.get("width");
        Float widthInPoints = null;
        if (width==null){
            table.setWidthPercentage(100); //<--Not the default for most browsers!
        }
        else {
            width = width.trim();
            try{
                if (width.endsWith("%")){
                    width = width.substring(0, width.length()-1).trim();
                    table.setWidthPercentage(Float.parseFloat(width));
                }
                else {
                    widthInPoints = Markup.getPoints(width);
                }
            }
            catch(Exception e){
            }
        }


      //Add cells to the table
        for (int row = 0; row < rows.size(); ++row) {
            ArrayList col = (ArrayList)rows.get(row);
            for (int k = 0; k < col.size(); ++k) {
                table.addCell((PdfPCell)col.get(k));
            }
        }

        
      //Update table width as needed
        if (widthInPoints!=null){
            try{
                table.setTotalWidth(widthInPoints);
                table.setLockedWidth(true);      
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        

        return table;
    }
}