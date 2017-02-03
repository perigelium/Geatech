package ru.alexangan.developer.geatech.Models;

/**
 * Created by user on 25.01.2017.
 */

public class ReportItem
{
    private VisitStates visitStates;
    private Gea_rapporto_sopralluogo gea_rapporto_sopralluogo;
    private ImagesReport gea_immagini_rapporto_sopralluogo;

    public VisitStates getVisitStates()
    {
        return visitStates;
    }

    public void setVisitStates(VisitStates visitStates)
    {
        this.visitStates = visitStates;
    }

    public Gea_rapporto_sopralluogo getGea_rapporto_sopralluogo()
    {
        return gea_rapporto_sopralluogo;
    }

    public void setGea_rapporto_sopralluogo(Gea_rapporto_sopralluogo gea_rapporto_sopralluogo)
    {
        this.gea_rapporto_sopralluogo = gea_rapporto_sopralluogo;
    }

    public ImagesReport getGea_immagini_rapporto_sopralluogo()
    {
        return gea_immagini_rapporto_sopralluogo;
    }

    public void setGea_immagini_rapporto_sopralluogo(ImagesReport gea_immagini_rapporto_sopralluogo)
    {
        this.gea_immagini_rapporto_sopralluogo = gea_immagini_rapporto_sopralluogo;
    }
}
