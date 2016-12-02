
package com.example.zubcu.geatech.Models;


public class ClientData {

    private String name;
    private String address;
    private String phone;
    private String mobile;

    public ClientData() {
    }

    public ClientData(String name, String address, String phone, String mobile) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.mobile = mobile;
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



}
