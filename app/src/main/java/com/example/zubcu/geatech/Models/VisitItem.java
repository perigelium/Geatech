
package com.example.zubcu.geatech.Models;




public class VisitItem {

    private VisitData visitData;
    private ClientData clientData;
    private ProductData productData;

    public VisitItem() {
    }

    public VisitItem(VisitData visitData, ClientData clientData, ProductData productData) {
        this.visitData = visitData;
        this.clientData = clientData;
        this.productData = productData;
    }

    public VisitData getVisitData() {
        return visitData;
    }

    public void setVisitData(VisitData visitData) {
        this.visitData = visitData;
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

}
