package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

/**
 * Created by user on 14.02.2017.
 */

public class GeaItemRapporto extends RealmObject
{
    private int id_rapporto_sopralluogo;
    private int id_item_modello;
    private String valore;

    public GeaItemRapporto(int id_rapporto_sopralluogo, int id_item_modello, String valore)
    {
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.id_item_modello = id_item_modello;
        this.valore = valore;
    }

    public GeaItemRapporto()
    {
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
}

