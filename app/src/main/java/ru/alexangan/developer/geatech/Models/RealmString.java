package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

public class RealmString extends RealmObject
{
    private int id;
    private String val;

    public RealmString()
    {}

    public RealmString(int id, String val)
    {
        this.id = id;
        this.val = val;
    }

    public String Value()
    {
        return val;
    }

    public int getId()
    {
        return id;
    }
}
