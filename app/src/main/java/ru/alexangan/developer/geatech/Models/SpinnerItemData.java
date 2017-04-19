package ru.alexangan.developer.geatech.Models;

/**
 * Created by user on 19.04.2017.
 */

public class SpinnerItemData
{
    String text;
    Integer imageId;

    public SpinnerItemData(String text, Integer imageId)
    {
        this.text = text;
        this.imageId = imageId;
    }

    public String getText()
    {
        return text;
    }

    public Integer getImageId()
    {
        return imageId;
    }
}
