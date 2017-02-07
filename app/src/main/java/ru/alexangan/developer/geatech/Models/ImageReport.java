package ru.alexangan.developer.geatech.Models;

import java.io.File;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 25.01.2017.
 */
public class ImageReport extends RealmObject
{
    @PrimaryKey
    private int id_immagine_rapporto;

    private int id_rapporto_sopralluogo;
    private String fileName;
    private String filePath;

    public ImageReport(int id_rapporto_sopralluogo, int id_immagine_rapporto, String filePath, String fileName)
    {
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.id_immagine_rapporto = id_immagine_rapporto;
        this.filePath = filePath;
        this.fileName = fileName;
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

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
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
