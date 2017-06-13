
package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SubproductItem extends RealmObject
{
    @PrimaryKey
    private int id;
    private String subproduct;
    private String product_type;
    private int pieces_nr;

    public SubproductItem()
    {
    }

/*    public SubproductItem(int id, String subproduct, String productType, int piecesNr)
    {
        this.id = id;
        this.subproduct = subproduct;
        this.productType = productType;
        this.piecesNr = piecesNr;
    }*/

    public String getSubproduct()
    {
        return subproduct;
    }

    public String getProductType()
    {
        return product_type;
    }

    public int getPiecesNr()
    {
        return pieces_nr;
    }

    public int getId()
    {
        return id;
    }
}
