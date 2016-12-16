package ru.alexangan.developer.geatech.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 11/28/2016.
 */

public class ItalianMonths
{

    private static List<String> itMonths = new ArrayList<>(Arrays.asList("gen", "feb", "mar", "apr", "mag", "giu", "lug",
            "ago", "set", "ott", "nov", "dic"));

    public static String numToString(int value)
    {
        if(value < 1 && value > 12)
        {
            return "non nota";
        }

        return itMonths.get(value - 1);
    }
}
