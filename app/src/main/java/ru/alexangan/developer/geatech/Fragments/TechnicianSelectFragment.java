package ru.alexangan.developer.geatech.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.Interfaces.NetworkEventsListener;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.TecnicianModel;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.JSON_to_model;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.inVisitItems;

public class TechnicianSelectFragment extends Fragment implements View.OnClickListener, Callback
{
    Activity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LoginCommunicator loginCommunicator;
    Call callTechnicianList, callLoginToken, callVisits;
    String visitsJSONData;
    NetworkEventsListener networkEventsListener;
    NetworkUtils networkUtils;

    TextView tvNewTechnician;
    ImageButton ibAddTechnician;
    Button btnSessionDisconnect, btnApplyAndEnterApp;
    LinearLayout llTechnNameAndSurname;
    FrameLayout flTechnicianAdded;
    EditText etTechCognome, etTechNome;
    boolean bNewTechAdded;
    TecnicianModel selectedTech;

    Spinner spTecnicianList;
    int spinnerCurItem;

    ArrayList<String> saTecnicianList;




    public TechnicianSelectFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        networkUtils = new NetworkUtils();
        saTecnicianList = new ArrayList<>();
        activity = getActivity();
        spinnerCurItem = 0;
        bNewTechAdded = false;

        networkEventsListener = (NetworkEventsListener) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        loginCommunicator = (LoginCommunicator) getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        callTechnicianList = networkUtils.loginRequest(this, null, null);

        spTecnicianList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                if(spinnerCurItem == position)
                {
                    return;
                }
                else
                {

                    String selectedItem = parent.getItemAtPosition(position).toString();
                    realm.beginTransaction();
                    selectedTech = realm.where(TecnicianModel.class).equalTo("full_name_tehnic", selectedItem).findFirst();
                    realm.commitTransaction();

                    //Toast.makeText(activity, selectedTech.getFullNameTehnic(), Toast.LENGTH_SHORT).show();

                    spinnerCurItem = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.tecnician_selection_fragment, container, false);

        tvNewTechnician = (TextView) rootView.findViewById(R.id.tvNewTechnician);
        tvNewTechnician.setOnClickListener(this);

        llTechnNameAndSurname = (LinearLayout) rootView.findViewById(R.id.llTechnNameAndSurname);

        flTechnicianAdded = (FrameLayout) rootView.findViewById(R.id.flTechnicianAdded);

        etTechNome = (EditText) rootView.findViewById(R.id.etTechNome);
        etTechCognome = (EditText) rootView.findViewById(R.id.etTechCognome);

        ibAddTechnician = (ImageButton) rootView.findViewById(R.id.ibAddTechnician);
        ibAddTechnician.setOnClickListener(this);

        btnSessionDisconnect = (Button) rootView.findViewById(R.id.btnSessionDisconnect);
        btnSessionDisconnect.setOnClickListener(this);

        btnApplyAndEnterApp = (Button) rootView.findViewById(R.id.btnApplyAndEnterApp);
        btnApplyAndEnterApp.setOnClickListener(this);

        RealmResults<TecnicianModel> techModelList = realm.where(TecnicianModel.class).findAll();
        saTecnicianList.add("");
        for (TecnicianModel technicianItem : techModelList)
        {
            saTecnicianList.add(technicianItem.getFullNameTehnic());
        }

        //saTecnicianList.addAll(Arrays.asList(TipiDiEdificieStrA));
        ArrayAdapter<String> technicianListAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, saTecnicianList);
        spTecnicianList = (Spinner) rootView.findViewById(R.id.spTecnicianList);

        //atvTipoDiEdificio.setListSelection(tipiDiEdificie.indexOf(clima1Model.getTipoDiEdificio()));

        spTecnicianList.setAdapter(technicianListAdapter);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnSessionDisconnect)
        {
            loginCommunicator.onReturnToLoginScreen();
        }

        if (view.getId() == R.id.tvNewTechnician)
        {
            llTechnNameAndSurname.setVisibility(View.VISIBLE);
        }

        if (view.getId() == R.id.ibAddTechnician)
        {
            String strTechNome = etTechNome.getText().toString();
            String strTechCognome = etTechCognome.getText().toString();
            String strNomeCognome = strTechNome + " " + strTechCognome;

            if(strNomeCognome.length() > 7)
            {
                bNewTechAdded = true;
                callTechnicianList = networkUtils.loginRequest(this, strNomeCognome, null);
            }
        }

        if (view.getId() == R.id.btnApplyAndEnterApp && selectedTech!=null)
        {
            //getactivity().getFragmentManager().beginTransaction().remove(this).commit();

            callLoginToken = networkUtils.loginRequest(TechnicianSelectFragment.this, selectedTech.getFullNameTehnic(), selectedTech.getId());
        }
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callLoginToken)
        {
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, "Receive token failed", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (call == callVisits)
        {
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, "Receive JSON data failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callTechnicianList)
        {
            String technListResponse = response.body().string();

            response.body().close();

            if (technListResponse == null)
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Receive technician list failed", Toast.LENGTH_LONG).show();
                    }
                });

                Log.d("DEBUG", "Receive technician list failed");

                return;
            }

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(technListResponse);
            } catch (JSONException e)
            {
                e.printStackTrace();
                return;
            }

            if (jsonObject.has("data_tehnic"))
            {
                try
                {
                    String technicianStr = jsonObject.getString("data_tehnic");

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<TecnicianModel>>() {}.getType();

                    final List<TecnicianModel> techModelList = gson.fromJson(technicianStr, type);

                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            saTecnicianList.clear();
                            saTecnicianList.add("");
                            for (TecnicianModel technicianItem : techModelList)
                            {
                                realm.beginTransaction();
                                saTecnicianList.add(technicianItem.getFullNameTehnic());
                                TecnicianModel tecnicianModelR = realm.where(TecnicianModel.class).equalTo("id", technicianItem.getId()).findFirst();

                                if(tecnicianModelR==null)
                                {
                                    realm.copyToRealm(technicianItem);
                                }
                                realm.commitTransaction();
                            }

                            ArrayAdapter<String> technicianListAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, saTecnicianList);
                            spTecnicianList.setAdapter(technicianListAdapter);

                            if(bNewTechAdded)
                            {
                                llTechnNameAndSurname.setVisibility(View.GONE);
                                flTechnicianAdded.setVisibility(View.VISIBLE);
                                bNewTechAdded = false;
                            }
                        }
                    });

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        }

        if (call == callLoginToken)
        {
            String loginResponse = response.body().string();
            String tokenStr;

            response.body().close();

            if (loginResponse == null)
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
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
                    callVisits = networkUtils.getJSONfromServer(this, tokenStr);
                }
            }
        }

        if (call == callVisits)
        {
            visitsJSONData = response.body().string();

            if (visitsJSONData == null)
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Receive JSON data failed", Toast.LENGTH_LONG).show();
                    }
                });

                return;
            }

            inVisitItems = JSON_to_model.getVisitTtemsList(visitsJSONData);

            Log.d("DEBUG", String.valueOf(inVisitItems.size()));

            //Log.d("DEBUG", visitsJSONData);

            if(inVisitItems!=null)
            {
                networkEventsListener.onJSONdataReceiveCompleted(selectedTech);
            }
        }
    }
}
