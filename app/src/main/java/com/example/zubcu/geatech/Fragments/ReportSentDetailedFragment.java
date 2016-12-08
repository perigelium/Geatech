package com.example.zubcu.geatech.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zubcu.geatech.Models.ClientData;
import com.example.zubcu.geatech.Models.ItalianMonths;
import com.example.zubcu.geatech.Models.ProductData;
import com.example.zubcu.geatech.Models.ReportStatesModel;
import com.example.zubcu.geatech.Models.VisitData;
import com.example.zubcu.geatech.Models.VisitItem;
import com.example.zubcu.geatech.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.zubcu.geatech.Network.RESTdataReceiver.visitItems;

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
        VisitData visitData = visitItem.getVisitData();
        ReportStatesModel reportStatesModel = visitItem.getReportStatesModel();

        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(clientData.getMobile());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        EditText etCoordNord = (EditText)rootView.findViewById(R.id.etCoordNord);
        EditText etCoordEst = (EditText)rootView.findViewById(R.id.etCoordEst);
        EditText etAltitude = (EditText)rootView.findViewById(R.id.etAltitude);

        etCoordNord.setText(String.valueOf(reportStatesModel.getCoordNord()));
        etCoordEst.setText(String.valueOf(reportStatesModel.getCoordEst()));
        etAltitude.setText(String.valueOf((int)reportStatesModel.getAltitude()));

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
