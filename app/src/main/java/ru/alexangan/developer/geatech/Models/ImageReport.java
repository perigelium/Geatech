package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 25.01.2017.
 */

// gea_imagine_rapporto_sopralluogo

public class ImageReport extends RealmObject
{
    @PrimaryKey
    private int id_immagine_rapporto;
    private int id_rapporto_sopralluogo;
    private String nome_file;

    private String filePath;

    public ImageReport(int id_rapporto_sopralluogo, int id_immagine_rapporto, String filePath, String nome_file)
    {
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.id_immagine_rapporto = id_immagine_rapporto;
        this.filePath = filePath;
        this.nome_file = nome_file;
    }

    public ImageReport()
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

    public void setNome_file(String nome_file)
    {
        this.nome_file = nome_file;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }
}
