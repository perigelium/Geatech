
package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class ClientData extends RealmObject
{
    private int id;
    private String name;
    private String address;
    private String phone;
    private String mobile;
    public double coordNord;
    public double coordEst;
    public int altitude;

    public ClientData() {}

    public ClientData(int id, String name, String address, String phone, String mobile, double coordNord, double coordEst)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.mobile = mobile;
        this.coordNord = coordNord;
        this.coordEst = coordEst;
    }


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

    public void setCoordNord(double coordNord)
    {
        this.coordNord = coordNord;
    }

    public double getCoordEst()
    {
        return coordEst;
    }

    public void setCoordEst(double coordEst)
    {
        this.coordEst = coordEst;
    }

    public double getAltitude()
    {
        return altitude;
    }

    public void setAltitude(int altitude)
    {
        this.altitude = altitude;
    }

    public int getId()
    {
        return id;
    }
}
