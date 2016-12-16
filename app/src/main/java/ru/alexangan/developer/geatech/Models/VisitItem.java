
package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class VisitItem extends RealmObject
{

    private VisitData visitData;
    private ClientData clientData;
    private ProductData productData;
    private ReportStatesModel reportStatesModel;

    public VisitItem() {
    }

    public VisitItem(VisitData visitData, ClientData clientData, ProductData productData, ReportStatesModel reportStatesModel) {
        this.visitData = visitData;
        this.clientData = clientData;
        this.productData = productData;
        this.reportStatesModel = reportStatesModel;
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


    public ReportStatesModel getReportStatesModel()
    {
        return reportStatesModel;
    }

    public void setReportStatesModel(ReportStatesModel reportStatesModel)
    {
        this.reportStatesModel = reportStatesModel;
    }
}
