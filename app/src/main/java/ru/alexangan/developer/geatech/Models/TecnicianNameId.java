package ru.alexangan.developer.geatech.Models;

/**
 * Created by user on 09.02.2017.
 */
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TecnicianNameId extends RealmObject
{
    @PrimaryKey
    private int id;
    private String full_name_tehnic;

    public TecnicianNameId()
    {
    }

    public TecnicianNameId(int id, String full_name_tehnic)
    {
        this.id = id;
        this.full_name_tehnic = full_name_tehnic;
    }

    public int getId()
    {
        return id;
    }

    public String getFullNameTehnic()
    {
        return full_name_tehnic;
    }
}
