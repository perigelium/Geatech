package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

// Created by Alexandr Angan on 25.01.2017.

// gea_imagine_rapporto_sopralluogo

public class GeaImmagineRapportoSopralluogo extends RealmObject
{

    private int id_rapporto_sopralluogo;
    private int id_immagine_rapporto;
    private String nome_file;
    private String file_original;
    private String file_extension;
    private String file_size;
    private String file_mime;
    private String file_format;
    private String date_insert;
    private String wait_file;
    private String id_user_upload;

    private String filePath;

    public GeaImmagineRapportoSopralluogo
            (int id_rapporto_sopralluogo, int id_immagine_rapporto, String nome_file)
    {
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.id_immagine_rapporto = id_immagine_rapporto;
        this.nome_file = nome_file;
    }

    public GeaImmagineRapportoSopralluogo()
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

}
