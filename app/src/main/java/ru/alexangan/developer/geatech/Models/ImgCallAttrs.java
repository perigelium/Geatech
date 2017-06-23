package ru.alexangan.developer.geatech.Models;

import okhttp3.Call;

/**
 * Created by user on 23.06.2017.
 */

public class ImgCallAttrs
{
    Call call;
    int id_sopralluogo;
    String fileName;

    public ImgCallAttrs(Call call, int id_sopralluogo, String fileName)
    {
        this.call = call;
        this.id_sopralluogo = id_sopralluogo;
        this.fileName = fileName;
    }

    public Call getCall()
    {
        return call;
    }

    public int getId_sopralluogo()
    {
        return id_sopralluogo;
    }

    public String getFileName()
    {
        return fileName;
    }
}
