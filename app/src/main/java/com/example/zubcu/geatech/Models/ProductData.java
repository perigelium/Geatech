
package com.example.zubcu.geatech.Models;

import java.util.ArrayList;
import java.util.List;


public class ProductData
{

    private String productType;
    private String idProductType;
    private String product;
    private List<SubItem> subItem = new ArrayList<SubItem>();

    public ProductData()
    {
    }

    public ProductData(String productType, String product, List<SubItem> subItem)
    {
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


    public List<SubItem> getSubItem()
    {
        return subItem;
    }


    public void setSubItem(List<SubItem> subItem)
    {
        this.subItem = subItem;
    }

}
