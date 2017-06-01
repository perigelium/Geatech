package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

/**
 * Created by user on 14.02.2017.
 */

public class GeaItemRapportoSopralluogo extends RealmObject
{
    private int id_item_rapporto;
    private int id_item_modello;
    private int id_rapporto_sopralluogo;
    private String valore;

    public GeaItemRapportoSopralluogo(int id_item_rapporto, int id_rapporto_sopralluogo, int id_item_modello, String valore)
    {
        this.id_item_rapporto = id_item_rapporto;
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.id_item_modello = id_item_modello;
        this.valore = valore;
    }

    public GeaItemRapportoSopralluogo()
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

    public int getId_item_rapporto()
    {
        return id_item_rapporto;
    }
}

