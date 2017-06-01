package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;

/**
 * Created by user on 01.06.2017.
 */
public class Gea_supplier extends RealmObject
{
    private int id;
    private String supplier_name;

    public Gea_supplier() {}

    public Gea_supplier(int id, String supplier_name)
    {
        this.id = id;
        this.supplier_name = supplier_name;
    }

    public String getSupplier_name()
    {
        return supplier_name;
    }
}
