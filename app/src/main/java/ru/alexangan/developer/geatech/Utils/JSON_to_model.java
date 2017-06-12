package ru.alexangan.developer.geatech.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import io.realm.RealmList;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaImmagineRapportoSopralluogo;
import ru.alexangan.developer.geatech.Models.GeaItemRapportoSopralluogo;
import ru.alexangan.developer.geatech.Models.GeaRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.Gea_supplier;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.SubproductItem;
import ru.alexangan.developer.geatech.Models.VisitItem;

/**
 * Created by user on 12/2/2016.*/

public class JSON_to_model
{
    public static RealmList<VisitItem> getVisitTtemsList (String visits_downloaded_data)
    {
        RealmList<VisitItem> visitItems = new RealmList<>();
        Gson gson = new Gson();

        try
        {
            JSONObject jsonObject = new JSONObject(visits_downloaded_data);
            JSONArray arr_caseArray = jsonObject.getJSONArray("arr_case");

            for (int i = 0; i < arr_caseArray.length(); i++)
            {
                JSONObject visit_items = arr_caseArray.getJSONObject(i);

                // gea_sopralluoghi
                JSONObject jsonObj_gea_sopralluoghi = (JSONObject) visit_items.get("gea_sopralluoghi");

                GeaSopralluogo geaSopralluogo = gson.fromJson(String.valueOf(jsonObj_gea_sopralluoghi), GeaSopralluogo.class);

                // gea_client
                JSONObject client_data = (JSONObject) visit_items.get("gea_client");

                ClientData clientData = gson.fromJson(String.valueOf(client_data), ClientData.class);

                String productType = client_data.getString("product_type");
                int idProductType = client_data.getInt("id_product_type");
                String product = client_data.getString("product");

                // gea_products
                JSONArray subproducts = (JSONArray) visit_items.get("gea_products");

                RealmList<SubproductItem> subproductsList = new RealmList<>();

                for (int j = 0; j < subproducts.length(); j++)
                {

                    JSONObject subproduct_dataJSONObject = subproducts.getJSONObject(j);

                    SubproductItem item = gson.fromJson(String.valueOf(subproduct_dataJSONObject), SubproductItem.class);

                    subproductsList.add(item);
                }

                ProductData productData = new ProductData(i, productType,  idProductType, product, subproductsList);

                // gea_supplier
                JSONObject supplier_data = (JSONObject) visit_items.get("gea_supplier");

                String supplier_name = supplier_data.getString("supplier");

                Gea_supplier geaSupplier = new Gea_supplier(i, supplier_name);

                // gea_rapporto_sopralluogo
                JSONObject rapporto_sopralluogo_data = (JSONObject) visit_items.get("gea_rapporto_sopralluogo");

                GeaRapporto gea_rapporto_sopralluogo = gson.fromJson(String.valueOf(rapporto_sopralluogo_data), GeaRapporto.class);

                //
                JSONArray data_items_rapporto_sopralluogo = (JSONArray) visit_items.get("gea_items_rapporto_sopralluogo");

                Type typeGeaItemRapportoSopralluogo = new TypeToken<List<GeaItemRapportoSopralluogo>>()
                {
                }.getType();
                final List<GeaItemRapportoSopralluogo> l_itemsRapportoSopralluogo = gson.fromJson(String.valueOf(data_items_rapporto_sopralluogo), typeGeaItemRapportoSopralluogo);

                RealmList<GeaItemRapportoSopralluogo> rl_ItemsRapportoSopralluogo = new RealmList<>(l_itemsRapportoSopralluogo.toArray(new GeaItemRapportoSopralluogo[l_itemsRapportoSopralluogo.size()]));

                JSONArray data_immagini_rapporto_sopralluogo = (JSONArray) visit_items.get("gea_immagini_rapporto_sopralluogo");

                Type typeimmaginiRapportoSopralluogo = new TypeToken<List<GeaImmagineRapportoSopralluogo>>()
                {
                }.getType();
                final List<GeaImmagineRapportoSopralluogo> l_immaginiRapportoSopralluogo = gson.fromJson(String.valueOf(data_immagini_rapporto_sopralluogo), typeimmaginiRapportoSopralluogo);

                RealmList<GeaImmagineRapportoSopralluogo> rl_ImmaginiRapportoSopralluogo = new RealmList<>(l_immaginiRapportoSopralluogo.toArray(new GeaImmagineRapportoSopralluogo[l_immaginiRapportoSopralluogo.size()]));

                VisitItem visitItem = new VisitItem(i, geaSopralluogo, clientData, productData, geaSupplier, gea_rapporto_sopralluogo, rl_ItemsRapportoSopralluogo, rl_ImmaginiRapportoSopralluogo);

                visitItems.add(visitItem);

            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return visitItems;
    }
}
