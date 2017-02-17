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

    private int id_rapporto_sopralluogo;
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

    public int getId_rapporto_sopralluogo()
    {
        return id_rapporto_sopralluogo;
    }

    public void setId_rapporto_sopralluogo(int id_rapporto_sopralluogo)
    {
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
    }

    public int getId_item_modello()
    {
        return id_item_modello;
    }

    public void setId_item_modello(int id_item_modello)
    {
        this.id_item_modello = id_item_modello;
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

