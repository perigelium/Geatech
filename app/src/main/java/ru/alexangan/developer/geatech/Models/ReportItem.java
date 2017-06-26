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
    private ClientData clientData;
    private GeaRapporto gea_rapporto_sopralluogo;
    private ReportStates reportStates;
    private RealmList<GeaItemRapporto> gea_items_rapporto_sopralluogo;
    private RealmList<GeaImmagineRapporto> gea_immagini_rapporto_sopralluogo;

    public ReportItem()
    {}

    public ReportItem(int company_id, int tech_id, int id_sopralluogo, int id_rapporto_sopralluogo,
                      ReportStates reportStates, GeaSopralluogo gea_sopralluoghi,
                      ClientData clientData,
                      GeaRapporto gea_rapporto_sopralluogo,
                      RealmList<GeaItemRapporto> gea_items_rapporto_sopralluogo,
                      RealmList<GeaImmagineRapporto> gea_immagini_rapporto_sopralluogo)
    {
        this.company_id = company_id;
        this.tech_id = tech_id;
        this.id_sopralluogo = id_sopralluogo;
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        Calendar calendarNow = Calendar.getInstance(Locale.ITALY);
        lastChangeUnixTime = calendarNow.getTimeInMillis();
        this.gea_sopralluoghi = gea_sopralluoghi;
        this.clientData = clientData;
        this.reportStates = reportStates;
        this.gea_rapporto_sopralluogo = gea_rapporto_sopralluogo;
        this.gea_items_rapporto_sopralluogo = gea_items_rapporto_sopralluogo;
        this.gea_immagini_rapporto_sopralluogo = gea_immagini_rapporto_sopralluogo;
    }

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

    public RealmList<GeaImmagineRapporto> getGea_immagini_rapporto_sopralluogo()
    {
        return gea_immagini_rapporto_sopralluogo;
    }

    public void setGea_immagini_rapporto_sopralluogo(RealmList<GeaImmagineRapporto> gea_immagini_rapporto_sopralluogo)
    {
        this.gea_immagini_rapporto_sopralluogo = gea_immagini_rapporto_sopralluogo;
    }

    public RealmList<GeaItemRapporto> getGea_items_rapporto_sopralluogo()
    {
        return gea_items_rapporto_sopralluogo;
    }

    public void setGea_items_rapporto_sopralluogo(RealmList<GeaItemRapporto> gea_items_rapporto_sopralluogo)
    {
        this.gea_items_rapporto_sopralluogo = gea_items_rapporto_sopralluogo;
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

    public void setReportStates(ReportStates reportStates)
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

    public ClientData getClientData()
    {
        return clientData;
    }

    public GeaSopralluogo getGea_sopralluoghi()
    {
        return gea_sopralluoghi;
    }

    public void setClientData(ClientData clientData)
    {
        this.clientData = clientData;
    }
}
