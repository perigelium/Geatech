package ru.alexangan.developer.geatech.Models;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by user on 25.01.2017.
 */

public class ReportItem extends RealmObject
{
    private int company_id;
    private int tech_id;
    private int id_sopralluogo;
    private int id_rapporto_sopralluogo;

    private long lastChangeUnixTime;
    private GeaSopralluogo gea_sopralluoghi;
    private GeaRapporto gea_rapporto;
    private ReportStates reportStates;
    private RealmList<GeaItemRapporto> gea_items_rapporto;
    private RealmList<GeaImmagineRapporto> gea_immagini_rapporto;

    public ReportItem()
    {}

    public ReportItem(int company_id, int tech_id, int id_sopralluogo, int id_rapporto_sopralluogo,
                      ReportStates reportStates, GeaSopralluogo gea_sopralluoghi,
                      GeaRapporto gea_rapporto,
                      RealmList<GeaItemRapporto> gea_items_rapporto,
                      RealmList<GeaImmagineRapporto> gea_immagini_rapporto)
    {
        this.company_id = company_id;
        this.tech_id = tech_id;
        this.id_sopralluogo = id_sopralluogo;
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        Calendar calendarNow = Calendar.getInstance(Locale.ITALY);
        lastChangeUnixTime = calendarNow.getTimeInMillis();
        this.gea_sopralluoghi = gea_sopralluoghi;
        this.reportStates = reportStates;
        this.gea_rapporto = gea_rapporto;
        this.gea_items_rapporto = gea_items_rapporto;
        this.gea_immagini_rapporto = gea_immagini_rapporto;
    }

    public GeaSopralluogo getGeaSopralluogo()
    {
        return gea_sopralluoghi;
    }

    public void setGeaSopralluogo(GeaSopralluogo geaSopralluogo)
    {
        this.gea_sopralluoghi = geaSopralluogo;
    }

    public GeaRapporto getGea_rapporto()
    {
        return gea_rapporto;
    }

    public void setGea_rapporto(GeaRapporto gea_rapporto)
    {
        this.gea_rapporto = gea_rapporto;
    }

    public List<GeaImmagineRapporto> getGea_immagini_rapporto()
    {
        return gea_immagini_rapporto;
    }

    public void setGea_immagini_rapporto(RealmList<GeaImmagineRapporto> gea_immagini_rapporto)
    {
        this.gea_immagini_rapporto = gea_immagini_rapporto;
    }

    public List<GeaItemRapporto> getGea_items_rapporto()
    {
        return gea_items_rapporto;
    }

    public void setGea_items_rapporto(RealmList<GeaItemRapporto> gea_items_rapporto)
    {
        this.gea_items_rapporto = gea_items_rapporto;
    }

    public long getLastChangeUnixTime()
    {
        return lastChangeUnixTime;
    }

    public void setLastChangeUnixTime(long lastChangeUnixTime)
    {
        this.lastChangeUnixTime = lastChangeUnixTime;
    }

    public ReportStates getReportStates()
    {
        return reportStates;
    }

    public void setReportStates(ReportItem reportItem)
    {
        this.reportStates = reportStates;
    }

    public int getId_sopralluogo()
    {
        return id_sopralluogo;
    }

    public int getTech_id()
    {
        return tech_id;
    }

    public int getId_rapporto_sopralluogo()
    {
        return id_rapporto_sopralluogo;
    }
}
