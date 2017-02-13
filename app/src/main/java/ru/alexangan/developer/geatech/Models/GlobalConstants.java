package ru.alexangan.developer.geatech.Models;

import android.content.SharedPreferences;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by user on 13.02.2017.
 */

public class GlobalConstants
{
    public static Realm realm;
    public static SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static TecnicianModel selectedTech;
    public static RealmResults<VisitItem> visitItems;
    public static RealmList<VisitItem> inVisitItems;
    public static String tokenStr;
}
