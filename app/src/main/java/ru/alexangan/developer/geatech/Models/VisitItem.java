
package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class VisitItem extends RealmObject
{
    @PrimaryKey
    private int id;
    private VisitStates visitStates;
    private ClientData clientData;
    private ProductData productData;
    private ReportStates reportStates;

    public VisitItem() {
    }

    public VisitItem(int id, VisitStates visitStates, ClientData clientData, ProductData productData, ReportStates reportStates)
    {
        this.id = id;
        this.visitStates = visitStates;
        this.clientData = clientData;
        this.productData = productData;
        this.reportStates = reportStates;
    }

    public VisitStates getVisitStates() {
        return visitStates;
    }

    public void setVisitStates(VisitStates visitStates) {
        this.visitStates = visitStates;
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


    public ReportStates getReportStates()
    {
        return reportStates;
    }

    public void setReportStates(ReportStates reportStates)
    {
        this.reportStates = reportStates;
    }

    public int getId()
    {
        return id;
    }
}
