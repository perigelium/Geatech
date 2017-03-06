package ru.alexangan.developer.geatech.Utils;

import java.util.Iterator;
import java.util.Map;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

/**
 * Created by user on 17.02.2017.
 */

public class DatabaseUtils
{
    public static int getReportInitializationState(int id_rapporto_sopralluogo)
    {
        realm.beginTransaction();
        RealmResults<GeaItemRapporto> geaItemRapportoResults =
                realm.where(GeaItemRapporto.class).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();
        realm.commitTransaction();

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

        if (reportComplete)
        {
            return 3;
        }

        if(partiallyComplete >= (geaItemRapportoResults.size())/2)
        {
            return 2;
        }

        if (partiallyComplete > 0)
        {
            return 1;
        }

        return 0;
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
