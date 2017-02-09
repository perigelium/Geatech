package ru.alexangan.developer.geatech.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.Interfaces.NetworkEventsListener;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.JSON_to_model;

import static ru.alexangan.developer.geatech.Activities.MainActivity.inVisitItems;

public class TechnicianSelectFragment extends Fragment implements View.OnClickListener, Callback
{
    Activity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LoginCommunicator mCommunicator;
    Call callLoginToken, callVisits;
    String loginResponse, tokenStr;
    String visitsJSONData;
    NetworkEventsListener networkEventsListener;

    Spinner atvTipoDiEdificio;
    private final String[] TipiDiEdificieStrA = new String[] {
            "", "Appartamento", "Villa(Singola/Multi)", "Negozio", "Altro"};

    ArrayList<String> tipiDiEdificie;


    public TechnicianSelectFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        tipiDiEdificie = new ArrayList<>();
        activity = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCommunicator = (LoginCommunicator) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.tecnician_selection_fragment, container, false);

        //ImageView ivSeleziona = (ImageView) rootView.findViewById(R.id.ivSeleziona);
        //ivSeleziona.bringToFront();


        ImageButton ibAddTechnician = (ImageButton) rootView.findViewById(R.id.ibAddTechnician);
        ibAddTechnician.setOnClickListener(this);
        Button btnSessionDisconnect = (Button) rootView.findViewById(R.id.btnSessionDisconnect);
        btnSessionDisconnect.setOnClickListener(this);
        Button btnApplyAndEnterApp = (Button) rootView.findViewById(R.id.btnApplyAndEnterApp);
        btnApplyAndEnterApp.setOnClickListener(this);


        tipiDiEdificie.addAll(Arrays.asList(TipiDiEdificieStrA));
        ArrayAdapter<String> TipiDiEdificieAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, tipiDiEdificie);

        atvTipoDiEdificio = (Spinner) rootView.findViewById(R.id.spTecnicianList);

        //atvTipoDiEdificio.setListSelection(tipiDiEdificie.indexOf(clima1Model.getTipoDiEdificio()));

        atvTipoDiEdificio.setAdapter(TipiDiEdificieAdapter);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btnApplyAndEnterApp)
        {
            //getactivity().getFragmentManager().beginTransaction().remove(this).commit();
            //mCommunicator.onRecoverPasswordReturned();

            NetworkUtils networkUtils = new NetworkUtils();
            callTechnicianList = networkUtils.getTecnicianList(this);
        }
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callLoginToken)
        {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Receive token failed", Toast.LENGTH_LONG).show();
                }
            });


        }

        if (call == callVisits)
        {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Receive JSON data failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callLoginToken)
        {
            loginResponse = response.body().string();

            response.body().close();

            if (loginResponse == null)
            {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Receive token failed", Toast.LENGTH_LONG).show();
                    }
                });

                Log.d("DEBUG", "Receive token failed");

                return;
            }

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(loginResponse);
            } catch (JSONException e)
            {
                e.printStackTrace();
                return;
            }

            if (jsonObject.has("token"))
            {
                try
                {
                    tokenStr = jsonObject.getString("token");

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    return;
                }

                if (tokenStr.length() != 0)
                {
                    NetworkUtils networkUtils = new NetworkUtils();
                    callVisits = networkUtils.getJSONfromServer(this);
                }
            }
        }

        if (call == callVisits)
        {
            visitsJSONData = response.body().string();

            if(visitsJSONData == null)
            {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Receive JSON data failed", Toast.LENGTH_LONG).show();
                    }
                });

                return;
            }

            inVisitItems = JSON_to_model.getVisitTtemsList(visitsJSONData);

            Log.d("DEBUG", String.valueOf(inVisitItems.size()));

            //Log.d("DEBUG", visitsJSONData);

            networkEventsListener.onJSONdataReceiveCompleted();

        }
    }
}
