
package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class SubproductItem  extends RealmObject
{
    private int id;
    private String subproduct;
    private String productType;
    private Integer piecesNr;

    public SubproductItem() {}

    public SubproductItem(int id, String subproduct, String productType, Integer piecesNr)
    {
        this.id = id;
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

    public int getId()
    {
        return id;
    }
}
