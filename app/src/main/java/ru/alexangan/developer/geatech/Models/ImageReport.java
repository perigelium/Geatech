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
    File file;

    public ImageReport(int id_rapporto_sopralluogo, int id_immagine_rapporto, File file)
    {
        this.id_rapporto_sopralluogo = id_rapporto_sopralluogo;
        this.id_immagine_rapporto = id_immagine_rapporto;
        this.file = file;
        this.fileName = file.getName();
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

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
