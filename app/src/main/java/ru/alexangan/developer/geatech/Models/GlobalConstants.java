package ru.alexangan.developer.geatech.Models;

import android.content.SharedPreferences;

import java.util.List;

import io.realm.RealmList;

// Created by user on 13.02.2017.

public class GlobalConstants
{
    public static SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static int company_id;
    public static String gea_supplier;
    public static TechnicianItem selectedTech;
    public static List<VisitItem> visitItems;
    public static RealmList<VisitItem> inVisitItems;

    public static String tokenStr;
    public static boolean visitsListIsObsolete;
    public static boolean reminderListIsObsolete;
    public static String lastSearchResults;

    public static int LIST_VISITS_MODE_ALL = 0;
    public static int LIST_VISITS_MODE_MY = 1;
    public static int LIST_VISITS_MODE_FREE = 2;

    public static String LOGIN_URL_SUFFIX = "?case=login";
    public static String GET_VISITS_URL_SUFFIX = "?case=get_raport";
    public static String SEARCH_VISITS_URL_SUFFIX = "?case=search";
    public static String GET_MODELS_URL_SUFFIX = "?case=get_modelli";
    public static String SET_DATA_URL_SUFFIX = "?case=set_data_supraluogo";
    public static String SEND_DATA_URL_SUFFIX = "?case=send_data";
    public static String SEND_IMAGE_URL = "http://www.bludelego.com/dev/geatech/send_image.php";
    public static String REST_SRV_URL = "http://www.bludelego.com/dev/geatech/gea.php";
}
