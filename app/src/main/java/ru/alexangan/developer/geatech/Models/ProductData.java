
package ru.alexangan.developer.geatech.Models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class ProductData extends RealmObject
{
    @PrimaryKey
    private int id;

    private String productType;
    private int idProductType;
    private String product;
    private RealmList<SubproductItem> subproductItems = new RealmList<>();

    public ProductData()
    {
    }

    public ProductData(int id, String productType, int idProductType, String product, RealmList<SubproductItem> subproductItems)
    {
        this.id = id;
        this.productType = productType;
        this.idProductType = idProductType;
        this.product = product;
        this.subproductItems = subproductItems;
    }

    public String getProductType()
    {
        return productType;
    }


    public int getIdProductType()
    {
        return idProductType;
    }


    public String getProduct()
    {
        return product;
    }


    public RealmList<SubproductItem> getSubproductItems()
    {
        return subproductItems;
    }


    public int getId()
    {
        return id;
    }
}
