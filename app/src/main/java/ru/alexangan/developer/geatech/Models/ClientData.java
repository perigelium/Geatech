
package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class ClientData extends RealmObject
{
    private String name;
    private String address;
    private String phone;
    private String mobile;
    private double coordNord;
    private double coordEst;
    private int altitude;

    private String product_type;
    private String id_product_type;
    private String product;

    public ClientData() {}


    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobile() {
        return mobile;
    }

    public double getCoordNord()
    {
        return coordNord;
    }

    public double getCoordEst()
    {
        return coordEst;
    }

    public double getAltitude()
    {
        return altitude;
    }

    public String getProduct()
    {
        return product;
    }

    public String getProduct_type()
    {
        return product_type;
    }

    public String getId_product_type()
    {
        return id_product_type;
    }
}
