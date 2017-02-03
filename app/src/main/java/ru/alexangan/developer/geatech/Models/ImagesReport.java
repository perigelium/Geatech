package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 25.01.2017.
 */
public class ImagesReport extends RealmObject
{
    @PrimaryKey
    private int id_immagine_rapporto;

    private int id_rapporto_sopralluogo;
    private String filePaths;

    public ImagesReport(int id_rapporto_sopralluogo, int id_immagine_rapporto)
    {
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.id_immagine_rapporto = id_immagine_rapporto;
    }

    public ImagesReport()
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

    public String getFilePaths()
    {
        return filePaths;
    }

    public void setFilePaths(String filePaths)
    {
        this.filePaths = filePaths;
    }
}
