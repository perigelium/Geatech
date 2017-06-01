
package ru.alexangan.developer.geatech.Models;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class VisitItem extends RealmObject
{
    @PrimaryKey
    private int id;

    private GeaSopralluogo geaSopralluogo;
    private ClientData clientData;
    private ProductData productData;
    private Gea_supplier geaSupplier;
    private GeaRapportoSopralluogo geaRapportoSopralluogo;
    private RealmList<GeaImmagineRapportoSopralluogo> gea_immagini_rapporto_sopralluogo;
    private RealmList<GeaItemRapportoSopralluogo> gea_items_rapporto_sopralluogo;

    public VisitItem() {
    }

    public VisitItem(int id, GeaSopralluogo geaSopralluogo, ClientData clientData, ProductData productData, Gea_supplier geaSupplier,
    GeaRapportoSopralluogo geaRapportoSopralluogo, RealmList<GeaItemRapportoSopralluogo> gea_items_rapporto_sopralluogo, RealmList<GeaImmagineRapportoSopralluogo> gea_immagini_rapporto_sopralluogo)
    {
        this.id = id;
        this.geaSopralluogo = geaSopralluogo;
        this.clientData = clientData;
        this.productData = productData;
        this.geaSupplier = geaSupplier;
        this.geaRapportoSopralluogo = geaRapportoSopralluogo;
        this.gea_items_rapporto_sopralluogo = gea_items_rapporto_sopralluogo;
        this.gea_immagini_rapporto_sopralluogo = gea_immagini_rapporto_sopralluogo;
    }

    public GeaSopralluogo getGeaSopralluogo() {
        return geaSopralluogo;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public ProductData getProductData() {
        return productData;
    }

    public int getId()
    {
        return id;
    }

    public Gea_supplier getGeaSupplier()
    {
        return geaSupplier;
    }

    public GeaRapportoSopralluogo getGeaRapportoSopralluogo()
    {
        return geaRapportoSopralluogo;
    }

    public RealmList<GeaImmagineRapportoSopralluogo> getGea_immagini_rapporto_sopralluogo()
    {
        return gea_immagini_rapporto_sopralluogo;
    }

    public RealmList<GeaItemRapportoSopralluogo> getGea_items_rapporto_sopralluogo()
    {
        return gea_items_rapporto_sopralluogo;
    }
}
