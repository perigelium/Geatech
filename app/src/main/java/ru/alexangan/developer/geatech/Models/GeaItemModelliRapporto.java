package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

public class GeaItemModelliRapporto extends RealmObject
{
    private int id_item_modello;
    private int id_sezione;
    private String descrizione_item;
    private int ordine;
    private String unita_misura;

    private String valore;

/*    private String gruppo;
    private String tipo;*/

    public int getId_item_modello()
    {
        return id_item_modello;
    }

    public String getDescrizione_item()
    {
        return descrizione_item;
    }

    public String getUnita_misura()
    {
        return unita_misura;
    }

    public String getValore()
    {
        return valore;
    }
}
