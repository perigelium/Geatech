package com.example.zubcu.geatech.Services;

import android.util.Pair;

import com.example.zubcu.geatech.Models.GeneralInfoModel;

import java.util.ArrayList;

/**
 * Created by user on 11/28/2016.
 */

public class GeneralInfoReceiver
{
    private static GeneralInfoReceiver ourInstance = new GeneralInfoReceiver();

    public static GeneralInfoReceiver getInstance()
    {
        return ourInstance;
    }

    static ArrayList<GeneralInfoModel> listVisitsArrayList;

    final String[] technicianNames = new String[]{"Nome di tecnico 1","Nome di tecnico 2","Nome di tecnico 3",
            "Nome di tecnico 4","Nome di tecnico 5","Nome di tecnico 6","Nome di tecnico 7","Nome di tecnico 8",
            "Nome di tecnico 9","Nome di tecnico 10"};

    final String[] clientsPhones = new String[]{"093827053943", "04956034", "394530495", "04597603947603",
    "039430946", "30496839486", "3094680394680", "30498309486", "430496830948603", "129385629350"};

    final String[] latitudes = new String[]{"093827053943", "04956034", "394530495", "04597603947603",
            "039430946", "30496839486", "3094680394680", "30498309486", "430496830948603", "129385629350"};
    final String[] longitudes = new String[]{"093827053943", "04956034", "394530495", "04597603947603",
            "039430946", "30496839486", "3094680394680", "30498309486", "430496830948603", "129385629350"};
    final String[] altitudes = new String[]{"150", "300", "800", "1000", "100", "50", "125", "5", "1250", "20"};

    final String[] clientsNames = new String[]{"Рыжик", "Барсик", "Мурзик",
            "Мурка", "Васька", "Пушок", "Дымка",
            "Кузя", "Масяня", "Симба"};

    final String[] serviceNames = new String[]{"Termodinamico", "Fotovoltaico", "Service_4",
            "Service_5", "Service_6", "Service_7", "Service_8", "Service_9", "Service_10", "Service_11"};

    final String[] clientsAddresses = new String[]{"Indirizzo_1", "Indirizzo_2", "Indirizzo_3",
            "Indirizzo_4", "Indirizzo_5", "Indirizzo_6", "Indirizzo_7", "Indirizzo_8", "Indirizzo_9",
            "Indirizzo_10"};


    static final ArrayList<Pair<String, String>> visitsDate;
    static
    {
        visitsDate = new ArrayList<Pair<String, String>>();
        visitsDate.add(Pair.create("2", "feb"));
        visitsDate.add(Pair.create("3", "mar"));
        visitsDate.add(Pair.create("", ""));
        visitsDate.add(Pair.create("5", "mag"));
        visitsDate.add(Pair.create("6", "gun"));
        visitsDate.add(Pair.create("7", "lug"));
        visitsDate.add(Pair.create("", ""));
        visitsDate.add(Pair.create("10", "oto"));
        visitsDate.add(Pair.create("11", "nov"));
        visitsDate.add(Pair.create("12", "dic"));
    }

    private GeneralInfoReceiver()
    {

        listVisitsArrayList = new ArrayList<>();

        for (int i = 0; i < clientsNames.length; i++)
        {
            listVisitsArrayList.add(
                    new GeneralInfoModel(clientsNames[i], clientsPhones[i], clientsAddresses[i],
                            latitudes[i], longitudes[i], altitudes[i],
                            technicianNames[i], serviceNames[i]));
        }

        //this.listVisitsArrayList = listVisitsArrayList;
    }

    public static ArrayList<GeneralInfoModel> getListVisitsArrayList()
    {
        return listVisitsArrayList;
    }
}
