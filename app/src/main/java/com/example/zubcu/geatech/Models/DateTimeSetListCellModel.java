package com.example.zubcu.geatech.Models;

import java.util.HashMap;

/**
 * Created by user on 11/15/2016.
 */

public class DateTimeSetListCellModel
{
    public String PRODOTTO_NOME = "name";
    public String PRODOTTO_TIPO = "tipo";
    public String PRODOTTO_N_PEZZI = "n_pezzi";

    //
    public DateTimeSetListCellModel(String name, String tipo, String n_pezzi)
    {
        this.PRODOTTO_NOME = name;
        this.PRODOTTO_TIPO =tipo;
        this.PRODOTTO_N_PEZZI = n_pezzi;
    }
}
