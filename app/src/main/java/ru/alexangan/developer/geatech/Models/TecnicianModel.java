package ru.alexangan.developer.geatech.Models;

/**
 * Created by user on 09.02.2017.
 */
import java.util.List;

import io.realm.RealmObject;

public class TecnicianModel extends RealmObject
{

    private String id;
    private String fullNameTehnic;

    public TecnicianModel()
    {
    }

    public TecnicianModel(String id, String fullNameTehnic)
    {
        this.id = id;
        this.fullNameTehnic = fullNameTehnic;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getFullNameTehnic()
    {
        return fullNameTehnic;
    }

    public void setFullNameTehnic(String fullNameTehnic)
    {
        this.fullNameTehnic = fullNameTehnic;
    }
}
