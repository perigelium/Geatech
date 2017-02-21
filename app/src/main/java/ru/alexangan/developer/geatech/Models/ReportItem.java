package ru.alexangan.developer.geatech.Models;

import java.util.List;

/**
 * Created by user on 25.01.2017.
 */

public class ReportItem
{
    private int company_id;
    private int tech_id;

    private GeaSopralluogo gea_sopralluoghi;
    private GeaRapporto gea_rapporto_sopralluogo;

    private List<GeaImagineRapporto> gea_immagini_rapporto_sopralluogo;
    private List<GeaItemRapporto> gea_items_rapporto_sopralluogo;

    public GeaSopralluogo getGeaSopralluogo()
    {
        return gea_sopralluoghi;
    }

    public void setGeaSopralluogo(GeaSopralluogo geaSopralluogo)
    {
        this.gea_sopralluoghi = geaSopralluogo;
    }

    public GeaRapporto getGea_rapporto_sopralluogo()
    {
        return gea_rapporto_sopralluogo;
    }

    public void setGea_rapporto_sopralluogo(GeaRapporto gea_rapporto_sopralluogo)
    {
        this.gea_rapporto_sopralluogo = gea_rapporto_sopralluogo;
    }

    public List<GeaImagineRapporto> getGea_immagini_rapporto_sopralluogo()
    {
        return gea_immagini_rapporto_sopralluogo;
    }

    public void setGea_immagini_rapporto_sopralluogo(List<GeaImagineRapporto> gea_immagini_rapporto_sopralluogo)
    {
        this.gea_immagini_rapporto_sopralluogo = gea_immagini_rapporto_sopralluogo;
    }

    public List<GeaItemRapporto> getGea_items_rapporto_sopralluogo()
    {
        return gea_items_rapporto_sopralluogo;
    }

    public void setGea_items_rapporto_sopralluogo(List<GeaItemRapporto> gea_items_rapporto_sopralluogo)
    {
        this.gea_items_rapporto_sopralluogo = gea_items_rapporto_sopralluogo;
    }
}
