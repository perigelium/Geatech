
package com.example.zubcu.geatech.Models;




public class SubItem {

    private String subproduct;
    private String productType;
    private Integer piecesNr;

    public SubItem() {
    }

    public SubItem(String subproduct, String productType, Integer piecesNr) {
        this.subproduct = subproduct;
        this.productType = productType;
        this.piecesNr = piecesNr;
    }

    public String getSubproduct() {
        return subproduct;
    }


    public String getProductType() {
        return productType;
    }


    public Integer getPiecesNr() {
        return piecesNr;
    }


}
