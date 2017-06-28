package ru.alexangan.developer.geatech.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

//Created by user on 17.02.2017.

public class DatabaseUtils
{
    public static ArrayList<Integer> getNotSetItems(int id_sopralluogo, int id_rapporto_sopralluogo)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", id_sopralluogo)
                .findFirst();
        realm.commitTransaction();

        List<GeaItemRapporto> l_geaItemRapporto = reportItem.getGea_items_rapporto_sopralluogo();

        ArrayList<Integer> notSetItems = new ArrayList<>();

        if (l_geaItemRapporto.size() != 0)
        {
            for (GeaItemRapporto geaItemRapporto : l_geaItemRapporto)
            {
                if (geaItemRapporto.getValore() == null || geaItemRapporto.getValore().trim().length() == 0)
                {
                    notSetItems.add(geaItemRapporto.getId_item_modello());
                }
            }
        }

        return notSetItems;
    }

    public static int getReportInitializationState(int id_sopralluogo, int id_rapporto_sopralluogo)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", id_sopralluogo)
                .findFirst();
        realm.commitTransaction();

        if(reportItem == null)
        {
            return ReportStates.REPORT_NON_INITIATED;
        }

        List<GeaItemRapporto> l_geaItemRapporto = reportItem.getGea_items_rapporto_sopralluogo();

        if (l_geaItemRapporto.size() == 0)
        {
            return ReportStates.REPORT_NON_INITIATED;
        }

        boolean reportComplete = true;
        int partiallyComplete = 0;

        for (GeaItemRapporto geaItemRapporto : l_geaItemRapporto)
        {
            if (geaItemRapporto.getValore() == null || geaItemRapporto.getValore().trim().length() == 0)
            {
                reportComplete = false;
            } else
            {
                partiallyComplete++;
            }
        }

        int completionPercent = partiallyComplete * 100 / l_geaItemRapporto.size();

        realm.beginTransaction();
        reportItem.getGea_rapporto_sopralluogo().setCompletion_percent(completionPercent);
        realm.commitTransaction();

        if (reportComplete)
        {
            return ReportStates.REPORT_COMPLETED;
        }

        if (completionPercent > 80)
        {
            return ReportStates.REPORT_ALMOST_COMPLETED;
        }

        if (completionPercent > 50)
        {
            return ReportStates.REPORT_HALF_COMPLETED;
        }

        if (completionPercent > 0)
        {
            return ReportStates.REPORT_INITIATED;
        }

        return ReportStates.REPORT_NON_INITIATED;
    }

    public static void insertStringInReportItem(
            int id_rapporto_sopralluogo, List<GeaItemRapporto> l_geaItemRapporto, int idItem, String strData)
    {
        Realm realm = Realm.getDefaultInstance();

        int i;

        for (i = 0; i < l_geaItemRapporto.size(); i++)
        {
            if (l_geaItemRapporto.get(i).getId_item_modello() == idItem)
            {
                break;
            }
        }

        realm.beginTransaction();

        if (i == l_geaItemRapporto.size())
        {
            GeaItemRapporto geaItem = new GeaItemRapporto(id_rapporto_sopralluogo, idItem, strData);

            l_geaItemRapporto.add(geaItem);
        } else
        {
            l_geaItemRapporto.get(i).setValore(strData);
        }
        realm.commitTransaction();
    }

    public static String getValueFromReportItem(List<GeaItemRapporto> l_geaItemRapporto, int idItem)
    {
        int i;

        for (i = 0; i < l_geaItemRapporto.size(); i++)
        {
            if (l_geaItemRapporto.get(i).getId_item_modello() == idItem)
            {
                break;
            }
        }

        if (i < l_geaItemRapporto.size() && l_geaItemRapporto.get(i) != null)
        {
            return l_geaItemRapporto.get(i).getValore();
        } else
        {
            return "";
        }
    }

/*    public void cleanUpReportData(ReportItem reportItem)
    {
        if(reportItem != null)
        {
            realm.beginTransaction();

            RealmResults geaItemsRapporto = realm.where(GeaItemRapporto.class)
                    .equalTo("company_id", company_id)
                    .equalTo("tech_id", selectedTech.getId())
                    .equalTo("id_rapporto_sopralluogo", reportItem.getGea_rapporto_sopralluogo().getId_rapporto_sopralluogo())
                    .findAll();

            RealmResults<GeaImmagineRapporto> listReportImages = realm.where(GeaImmagineRapporto.class)
                    .equalTo("company_id", company_id)
                    .equalTo("tech_id", selectedTech.getId())
                    .equalTo("id_rapporto_sopralluogo", reportItem.getGea_rapporto_sopralluogo().getId_rapporto_sopralluogo())
                    .findAll();

            geaItemsRapporto.deleteAllFromRealm();
            listReportImages.deleteAllFromRealm();
            reportItem.deleteFromRealm();

            realm.commitTransaction();
        }
    }*/
}
