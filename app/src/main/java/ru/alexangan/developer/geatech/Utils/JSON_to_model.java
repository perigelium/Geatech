package ru.alexangan.developer.geatech.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.SubproductItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;

/**
 * Created by user on 12/2/2016.
 */

public class JSON_to_model
{
    public static RealmList<VisitItem> getVisitTtemsList (String visits_downloaded_data)
    {
        RealmList<VisitItem> visitItems = new RealmList<>();

        try
        {
            JSONObject jsonObject = new JSONObject(visits_downloaded_data);
            JSONArray arr_caseArray = jsonObject.getJSONArray("arr_case");
            //JSONArray visits_array = arr_caseArray.getJSONArray(0);

            for (int i = 0; i < arr_caseArray.length(); i++)
            {
                JSONObject visit_items = arr_caseArray.getJSONObject(i);

                JSONObject visit_data = (JSONObject) visit_items.get("gea_sopralluoghi");
                JSONObject client_data = (JSONObject) visit_items.get("gea_client");
                JSONArray subproducts = (JSONArray) visit_items.get("gea_products");


                //JSONArray gea_supplier = (JSONArray) visit_items.get("gea_supplier");
//                JSONArray gea_rapporto_soppralluogo = (JSONArray) visit_items.get("gea_rapporto_soppralluogo");
//                JSONArray gea_immagini_rapporto_sopralluogo = (JSONArray) visit_items.get("gea_immagini_rapporto_sopralluogo");
//                JSONArray gea_items_rapporto_soppralluogo = (JSONArray) visit_items.get("gea_items_rapporto_soppralluogo");

                Integer id_sopralluogo = visit_data.getInt("id_sopralluogo");
                int id_tecnico = visit_data.getInt("id_tecnico");
                //String data_ora_assegnazione = visit_data.getString("data_ora_assegnazione");
                String data_ora_presa_appuntamento = visit_data.getString("data_ora_presa_appuntamento");
                //String data_sollecito_appuntamento = visit_data.getString("data_sollecito_appuntamento");
                String data_ora_sopralluogo = visit_data.getString("data_ora_sopralluogo");
                //String note_sopralluogo = visit_data.getString("note_sopralluogo");
                //String tipo_gestione_sopralluogo = visit_data.getString("tipo_gestione_sopralluogo");

                GeaSopralluogo geaSopralluogo = new GeaSopralluogo(id_sopralluogo, id_tecnico, data_ora_presa_appuntamento, data_ora_sopralluogo);

                //JSONObject client_dataJSONObject = client_data.getJSONObject();

                String name = client_data.getString("name");
                String address = client_data.getString("address");
                String phone = client_data.getString("phone");
                String mobile = client_data.getString("mobile");
                double coordNord = client_data.getDouble("lat");
                double coordEst = client_data.getDouble("lng");

                ClientData clientData = new ClientData(i, name,  address,  phone,  mobile, coordNord, coordEst);

                String productType = client_data.getString("product_type");
                int idProductType = client_data.getInt("id_product_type");
                String product = client_data.getString("product");


                RealmList<SubproductItem> subproductsList = new RealmList<SubproductItem>();

                for (int j = 0; j < subproducts.length(); j++)
                {

                    JSONObject subproduct_dataJSONObject = subproducts.getJSONObject(j);

                    String subproduct = subproduct_dataJSONObject.getString("subproduct");
                    String subproduct_type = subproduct_dataJSONObject.getString("product_type");
                    Integer pieces_nr = subproduct_dataJSONObject.getInt("pieces_nr");

                    SubproductItem item = new SubproductItem(i, subproduct, subproduct_type, pieces_nr);
                    subproductsList.add(item);
                }

                ProductData productData = new ProductData(i, productType,  idProductType, product, subproductsList);

                VisitItem visitItem = new VisitItem(i, geaSopralluogo, clientData, productData);

                visitItems.add(visitItem);
            }

            //Log.d("DEBUG", String.valueOf(visitItems.size()));

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return visitItems;
    }

    public String getData(String visitDataResponse)
    {
        return visitDataResponse;
    }
}
