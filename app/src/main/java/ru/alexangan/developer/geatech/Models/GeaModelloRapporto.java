package ru.alexangan.developer.geatech.Models;


import io.realm.RealmObject;

public class GeaModelloRapporto extends RealmObject
{

    private int id_modello;
    private int id_product_type;
    private String nome_modello;

    public int getId_product_type()
    {
        return id_product_type;
    }

    public int getId_modello()
    {
        return id_modello;
    }

    public String getNome_modello()
    {
        return nome_modello;
    }
}
