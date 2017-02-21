package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 14.02.2017.
 */

public class GeaItemRapporto extends RealmObject
{
    @PrimaryKey
    private int id_item_modello;

    private int company_id;
    private int tech_id;

    private int id_rapporto_sopralluogo;
    private String valore;

    public GeaItemRapporto(int company_id, int tech_id, int id_rapporto_sopralluogo, int id_item_modello, String valore)
    {
        this.company_id = company_id;
        this.tech_id = tech_id;
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.id_item_modello = id_item_modello;
        this.valore = valore;
    }

    public GeaItemRapporto()
    {
    }

    public int getId_rapporto_sopralluogo()
    {
        return id_rapporto_sopralluogo;
    }

    public int getId_item_modello()
    {
        return id_item_modello;
    }

    public String getValore()
    {
        return valore;
    }

    public void setValore(String valore)
    {
        this.valore = valore;
    }

    public int getCompany_id()
    {
        return company_id;
    }

    public int getTech_id()
    {
        return tech_id;
    }
}

