package ru.alexangan.developer.geatech.Models;

/**
 * Created by user on 09.02.2017.
 */
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TecnicianModel extends RealmObject
{
    @PrimaryKey
    private String id;
    private String full_name_tehnic;

    public TecnicianModel()
    {
    }

    public TecnicianModel(String id, String full_name_tehnic)
    {
        this.id = id;
        this.full_name_tehnic = full_name_tehnic;
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
        return full_name_tehnic;
    }

    public void setFullNameTehnic(String full_name_tehnic)
    {
        this.full_name_tehnic = full_name_tehnic;
    }
}
