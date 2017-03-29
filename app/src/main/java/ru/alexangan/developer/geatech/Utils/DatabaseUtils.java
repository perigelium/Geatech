package ru.alexangan.developer.geatech.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.ReportStates;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

/**
 * Created by user on 17.02.2017.
 */

public class DatabaseUtils
{
    public static ArrayList <Integer> getNotSetItems(int id_rapporto_sopralluogo)
    {
        realm.beginTransaction();
        RealmResults<GeaItemRapporto> geaItemRapportoResults =
                realm.where(GeaItemRapporto.class).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();
        realm.commitTransaction();

        if(geaItemRapportoResults.size() == 0)
        {
            return null;
        }

        ArrayList <Integer> notSetItems = new ArrayList<>();

        for (GeaItemRapporto geaItemRapporto : geaItemRapportoResults)
        {
            if (geaItemRapporto.getValore() == null || geaItemRapporto.getValore().trim().length() == 0)
            {
                notSetItems.add(geaItemRapporto.getId_item_modello());
            }
        }

        return  notSetItems;
    }

    public static int getReportInitializationState(int id_rapporto_sopralluogo)
    {
        realm.beginTransaction();
        RealmResults<GeaItemRapporto> geaItemRapportoResults =
                realm.where(GeaItemRapporto.class).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();
        realm.commitTransaction();

        if(geaItemRapportoResults.size() == 0)
        {
            return ReportStates.REPORT_STATE_INDETERMINATE;
        }

        boolean reportComplete = true;
        int partiallyComplete = 0;

        for (GeaItemRapporto geaItemRapporto : geaItemRapportoResults)
        {
            if (geaItemRapporto.getValore() == null || geaItemRapporto.getValore().trim().length() == 0)
            {
                reportComplete = false;
            } else
            {
                partiallyComplete++;
            }
        }

        int completionPercent = partiallyComplete*100/geaItemRapportoResults.size();

        realm.beginTransaction();

        ReportStates reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findFirst();


        reportStates.setCompletion_percent(completionPercent);

        realm.commitTransaction();

        if (reportComplete)
        {
            return ReportStates.REPORT_COMPLETED;
        }

        if(completionPercent > 80)
        {
            return ReportStates.REPORT_ALMOST_COMPLETED;
        }

        if(completionPercent > 50)
        {
            return ReportStates.REPORT_PARTIALLY_COMPLETED;
        }

        if (completionPercent > 0)
        {
            return ReportStates.REPORT_INITIATED;
        }

        return ReportStates.REPORT_NON_INITIATED;
    }

    public static void insertStringInReportItem(int id_rapporto_sopralluogo, int idItem, String strData)
    {
        realm.beginTransaction();
        GeaItemRapporto geaItemRapporto =
                realm.where(GeaItemRapporto.class).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).equalTo("id_item_modello", idItem).findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        if(geaItemRapporto == null)
        {
            GeaItemRapporto geaItem = new GeaItemRapporto(company_id, selectedTech.getId(), id_rapporto_sopralluogo, idItem, strData);
            realm.copyToRealm(geaItem);
        }
        else
        {
            geaItemRapporto.setValore(strData);
        }
        realm.commitTransaction();
    }

    public static String getValueFromReportItem(int id_rapporto_sopralluogo, int idItem)
    {
        realm.beginTransaction();
        GeaItemRapporto geaItemRapporto =
                realm.where(GeaItemRapporto.class).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).equalTo("id_item_modello", idItem).findFirst();
        realm.commitTransaction();

        if(geaItemRapporto !=null)
        {
            return geaItemRapporto.getValore();
        }
        else
        {
            return "";
        }
    }
}
