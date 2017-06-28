
package ru.alexangan.developer.geatech.Models;


public class ReportsSearchResultItem
{
    private int id_sopralluogo;
    private String name;
    private String address;
    private String phone;
    private String mobile;

    private String product_type;
    private String data_ora_sopralluogo;

    public ReportsSearchResultItem() {}

    public ReportsSearchResultItem(int id_sopralluogo, String name, String mobile, String phone,
                                   String address, String product_type, String data_ora_sopralluogo)
    {
        this.id_sopralluogo = id_sopralluogo;
        this.name = name;
        this.mobile = mobile;
        this.phone = phone;
        this.address = address;
        this.product_type = product_type;
        this.data_ora_sopralluogo = data_ora_sopralluogo;
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

    public String getProduct_type()
    {
        return product_type;
    }

    public String getData_ora_sopralluogo()
    {
        return data_ora_sopralluogo;
    }

    public int getId_sopralluogo()
    {
        return id_sopralluogo;
    }
}
