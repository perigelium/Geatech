
package ru.alexangan.developer.geatech.Models;

import io.realm.RealmList;
import io.realm.RealmObject;


public class ProductData extends RealmObject
{

    private int id;
    private String productType;
    private String idProductType;
    private String product;
    private RealmList<SubproductItem> subItem = new RealmList<>();

    public ProductData()
    {
    }

    public ProductData(int id, String productType, String product, RealmList<SubproductItem> subItem)
    {
        this.id = id;
        this.productType = productType;
        //this.idProductType = idProductType;
        this.product = product;
        this.subItem = subItem;
    }

    public String getProductType()
    {
        return productType;
    }


    public String getIdProductType()
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


    public void setSubItem(RealmList<SubproductItem> subItem)
    {
        this.subItem = subItem;
    }

    public int getId()
    {
        return id;
    }
}
