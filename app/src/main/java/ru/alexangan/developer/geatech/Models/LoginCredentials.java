package ru.alexangan.developer.geatech.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 12/19/2016.
 */

public class LoginCredentials extends RealmObject
{
    @PrimaryKey
    private int company_id;

    private String login;
    private String password;

    public LoginCredentials(){};

    public LoginCredentials(int company_id, String login, String password)
    {
        this.company_id = company_id;
        this.login = login;
        this.password = password;
    }

    public String getLogin()
    {
        return login;
    }

    public String getPassword()
    {
        return password;
    }

    public int getCompany_id()
    {
        return company_id;
    }
}
