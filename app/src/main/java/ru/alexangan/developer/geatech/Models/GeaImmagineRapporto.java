package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

// Created by Alexandr Angan on 25.01.2017.

// gea_imagine_rapporto_sopralluogo

public class GeaImmagineRapporto extends RealmObject
{

    private int id_immagine_rapporto;
    private int company_id;
    private int tech_id;

    private int id_rapporto_sopralluogo;
    private String nome_file;

    private String filePath;

    public GeaImmagineRapporto
            (int company_id, int tech_id, int id_rapporto_sopralluogo, int id_immagine_rapporto, String filePath, String nome_file)
    {
        this.company_id = company_id;
        this.tech_id = tech_id;
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.id_immagine_rapporto = id_immagine_rapporto;
        this.filePath = filePath;
        this.nome_file = nome_file;
    }

    public GeaImmagineRapporto()
    {
    }

    public int getId_immagine_rapporto()
    {
        return id_immagine_rapporto;
    }

    public int getId_rapporto_sopralluogo()
    {
        return id_rapporto_sopralluogo;
    }

    public String getNome_file()
    {
        return nome_file;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setId_immagine_rapporto(int id_immagine_rapporto)
    {
        this.id_immagine_rapporto = id_immagine_rapporto;
    }

    public void setfilePath(String filePath)
    {
        this.filePath = filePath;
    }
}
