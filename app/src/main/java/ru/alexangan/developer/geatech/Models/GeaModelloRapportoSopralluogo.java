package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class GeaModelloRapportoSopralluogo extends RealmObject
{

    private int id_modello;
    private String nome_modello;

    public int getId_modello()
    {
        return id_modello;
    }

    public String getNome_modello()
    {
        return nome_modello;
    }
}
