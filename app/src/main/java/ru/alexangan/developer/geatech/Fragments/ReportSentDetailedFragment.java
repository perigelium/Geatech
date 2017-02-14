package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

/**
 * Created by user on 11/10/2016.
 */

public class ReportSentDetailedFragment extends Fragment
{
    private int selectedIndex;

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
        VisitStates visitStates = visitItem.getVisitStates();
        int idSopralluogo = visitStates.getId_sopralluogo();

        TextView tvdataOraSopralluogo = (TextView) rootView.findViewById(R.id.tvdataOraSopralluogo);
        TextView tvdataOraRaportoCompletato = (TextView) rootView.findViewById(R.id.tvdataOraRaportoCompletato);
        TextView tvdataOraRaportoInviato = (TextView) rootView.findViewById(R.id.tvdataOraRaportoInviato);

        realm.beginTransaction();
        ReportStates reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if(reportStates != null)
        {
            tvdataOraSopralluogo.setText(reportStates.getDataOraSopralluogo());

            tvdataOraRaportoCompletato.setText(reportStates.getDataOraRaportoCompletato());

            tvdataOraRaportoInviato.setText(reportStates.getDataOraRaportoInviato());
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

        if(reportStates != null && reportStates.getLatitude()!=0.0 && reportStates.getLongitude()!=0.0 && reportStates.getAltitude()!=0.0)
        {
            double latitude = reportStates.getLatitude();
            double longitude = reportStates.getLongitude();
            double altitude = reportStates.getAltitude();

            etCoordNord.setText(String.valueOf(latitude));
            etCoordEst.setText(String.valueOf(longitude));
            etAltitude.setText(String.valueOf((int) altitude));
        }

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
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}
