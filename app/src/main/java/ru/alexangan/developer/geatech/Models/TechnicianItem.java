package ru.alexangan.developer.geatech.Models;

/**
 * Created by user on 09.02.2017.
 */
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TechnicianItem extends RealmObject
{
    private int company_id;
    private int id;
    private String full_name_tehnic;

    public TechnicianItem()
    {
    }

    public TechnicianItem(int company_id, int tech_id, String full_name_tehnic)
    {
        this.company_id = company_id;
        this.id = tech_id;
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

    public int getCompany_id()
    {
        return company_id;
    }

    public void setCompanyId(int company_id)
    {
        this.company_id = company_id;
    }
}
