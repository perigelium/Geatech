package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

/**
 * Created by user on 11/10/2016.
 */

public class ReportSentDetailedFragment extends Fragment
{
    private int selectedIndex;
    List<String> reportItems;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.report_sent_detailed_fragment, container, false);

        VisitItem visitItem = visitItems.get(selectedIndex);
        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();
        String productType = productData.getProductType();

        TextView tvdataOraSopralluogo = (TextView) rootView.findViewById(R.id.tvdataOraSopralluogo);
        TextView tvdataOraRaportoCompletato = (TextView) rootView.findViewById(R.id.tvdataOraRaportoCompletato);
        TextView tvdataOraRaportoInviato = (TextView) rootView.findViewById(R.id.tvdataOraRaportoInviato);

        TextView tvTechName = (TextView) rootView.findViewById(R.id.tvTechName);
        tvTechName.setText(selectedTech.getFullNameTehnic());


        realm.beginTransaction();
        ReportStates reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if(reportStates != null)
        {
            int id_rapporto_sopralluogo = reportStates.getId_rapporto_sopralluogo();

            List <GeaItemRapporto> geaItemsRapporto = realm.where(GeaItemRapporto.class).equalTo("company_id", company_id)
                    .equalTo("tech_id", selectedTech.getId()).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();

/*            realm.beginTransaction();
            GeaModelloRapporto geaModello = realm.where(GeaModelloRapporto.class).equalTo("nome_modello", productType).findFirst();
            realm.commitTransaction();

            realm.beginTransaction();
            RealmResults <GeaSezioneModelliRapporto> geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class)
                    .equalTo("id_modello", geaModello.getId_modello()).findAll();
            realm.commitTransaction();*/

            realm.beginTransaction();
            RealmResults <GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class)
             .between("id_item_modello", geaItemsRapporto.get(0).getId_item_modello(), geaItemsRapporto.get(geaItemsRapporto.size()-1).getId_item_modello()).findAll();
            realm.commitTransaction();

            for(int i = 0; i < geaItemsRapporto.size(); i++)
            {
                reportItems.add(String.valueOf(geaItemModelli.get(i).getDescrizione_item()) + " : " + geaItemsRapporto.get(i).getValore());
            }

            tvdataOraSopralluogo.setText(reportStates.getData_ora_sopralluogo());
            tvdataOraRaportoCompletato.setText(reportStates.getDataOraRaportoCompletato());
            tvdataOraRaportoInviato.setText(reportStates.getData_ora_invio_rapporto());
        }

        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(clientData.getMobile());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        TextView etCoordNord = (TextView)rootView.findViewById(R.id.etCoordNord);
        TextView etCoordEst = (TextView)rootView.findViewById(R.id.etCoordEst);
        TextView etAltitude = (TextView)rootView.findViewById(R.id.etAltitude);

        if(reportStates != null)
        {
            double latitude = reportStates.getLatitudine();
            double longitude = reportStates.getLongitudine();
            double altitude = reportStates.getAltitudine();

            etCoordNord.setText(String.valueOf(latitude));
            etCoordEst.setText(String.valueOf(longitude));

            if(altitude!=-999)
            {
                etAltitude.setText(String.valueOf((int) altitude));
            }
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listItemsSentReport);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,  reportItems);

        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public View getView()
    {
        return super.getView();
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }

        reportItems = new ArrayList<>();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}
