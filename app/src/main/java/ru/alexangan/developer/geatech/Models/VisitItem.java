
package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class VisitItem extends RealmObject
{
    @PrimaryKey
    private int id;

    private GeaSopralluogo geaSopralluogo;
    private ClientData clientData;
    private ProductData productData;

    public VisitItem() {
    }

    public VisitItem(int id, GeaSopralluogo geaSopralluogo, ClientData clientData, ProductData productData)
    {
        this.id = id;
        this.geaSopralluogo = geaSopralluogo;
        this.clientData = clientData;
        this.productData = productData;
    }

    public GeaSopralluogo getGeaSopralluogo() {
        return geaSopralluogo;
    }

    public void setGeaSopralluogo(GeaSopralluogo geaSopralluogo) {
        this.geaSopralluogo = geaSopralluogo;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public int getId()
    {
        return id;
    }
}
