package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class GeaSezioneModelliRapportoSopralluogo extends RealmObject
{
    private int id_sezione;
    private int id_modello;
    private String descrizione_sezione;
    private int ordine;

    public int getId_sezione()
    {
        return id_sezione;
    }

    public int getId_modello()
    {
        return id_modello;
    }

    public String getDescrizione_sezione()
    {
        return descrizione_sezione;
    }

    public int getOrdine()
    {
        return ordine;
    }
}
