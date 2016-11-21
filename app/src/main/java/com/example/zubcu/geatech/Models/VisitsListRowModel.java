package com.example.zubcu.geatech.Models;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.R.attr.key;

/**
 * Created by user on 11/15/2016.
 */

public class VisitsListRowModel
{
    public String CLIENT_NAME = "Nome di cliente";
    public String SERVICE_NAME = "Type of service";
    public String CLIENT_ADDRESS = "Indirizzio di Cliente";
    public Pair<String, String> visitsDate;

    public VisitsListRowModel(String name, String service, String address, Pair<String, String> visitsDate)
    {
        this.CLIENT_NAME = name;
        this.SERVICE_NAME = service;
        this.CLIENT_ADDRESS = address;

        this.visitsDate = visitsDate;
    }

    public String getCLIENT_NAME()
    {
        return CLIENT_NAME;
    }

    public String getSERVICE_NAME()
    {
        return SERVICE_NAME;
    }

    public String getCLIENT_ADDRESS()
    {
        return CLIENT_ADDRESS;
    }

    public String getVISIT_DAY()
    {
        return this.visitsDate.first;
    }

    public String getVISIT_MONTH()
    {
        return this.visitsDate.second;
    }
}
