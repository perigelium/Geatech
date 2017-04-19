package ru.alexangan.developer.geatech.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 11/28/2016.
 */

public class ItalianMonths
{

    private static List<String> itMonths = new ArrayList<>(Arrays.asList("GEN", "FEB", "MAR", "APR", "MAG", "GIU", "LUG",
            "AGO", "SET", "OTT", "NOV", "DIC"));

    private static List<String> itMonthsLong = new ArrayList<>(Arrays.asList("genaio", "febraio", "marzo", "aprile", "maggio", "giugno", "luglio",
            "agosto", "settembre", "ottobre", "novembre", "dicembre"));

    public static String numToString(int value)
    {
        if(value < 1 && value > 12)
        {
            return "non nota";
        }

        return itMonths.get(value - 1);
    }

    public static String numToLongString(int value)
    {
        if(value < 1 && value > 12)
        {
            return "non nota";
        }

        return itMonthsLong.get(value - 1);
    }
}
