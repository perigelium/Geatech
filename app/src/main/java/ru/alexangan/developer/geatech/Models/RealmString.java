package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

public class RealmString extends RealmObject
{
    private String val;

    public RealmString()
    {}

    public RealmString(String val)
    {
        this.val = val;
    }

    public String Value()
    {
        return val;
    }
}
