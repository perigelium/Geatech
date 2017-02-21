
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
    private RealmList<SubproductItem> subItem = new RealmList<>();

    public ProductData()
    {
    }

    public ProductData(int id, String productType, int idProductType, String product, RealmList<SubproductItem> subItem)
    {
        this.id = id;
        this.productType = productType;
        this.idProductType = idProductType;
        this.product = product;
        this.subItem = subItem;
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


    public RealmList<SubproductItem> getSubItem()
    {
        return subItem;
    }


    public int getId()
    {
        return id;
    }
}
