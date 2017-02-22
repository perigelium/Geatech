package ru.alexangan.developer.geatech.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.GlobalConstants;
import ru.alexangan.developer.geatech.Models.TechnicianItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.JSON_to_model;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.GET_MODELS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.GET_VISITS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.inVisitItems;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class TechnicianSelectFragment extends Fragment implements View.OnClickListener, Callback
{
    Activity activity;

    private LoginCommunicator loginCommunicator;
    Call callTechnicianList, callLoginToken, callVisits, callModels;
    String visitsJSONData, login, password;
    NetworkUtils networkUtils;

    ImageButton ibAddTechnician;
    Button btnSessionDisconnect, btnApplyAndEnterApp, btnNewTechnician;
    LinearLayout llTechnNameAndSurname;
    FrameLayout flTechnicianAdded;
    EditText etTechCognome, etTechNome;
    boolean bNewTechAdded;
    TechnicianItem selectedTech;
    private CheckBox chkboxRememberTech;

    Spinner spTecnicianList;
    int spinnerCurItem;

    ArrayList<String> saTecnicianList;
    private String modelsJSONData;
    int geaItemModelliSize;

    public TechnicianSelectFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onResume()
    {
        super.onResume();

        spinnerCurItem = 0;
        bNewTechAdded = false;

        login = mSettings.getString("login", null);
        password = mSettings.getString("password", null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        networkUtils = new NetworkUtils();
        saTecnicianList = new ArrayList<>();
        activity = getActivity();
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

        spTecnicianList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (spinnerCurItem == position)
                {
                    return;
                } else
                {

                    String selectedItem = parent.getItemAtPosition(position).toString();
                    realm.beginTransaction();
                    selectedTech = realm.where(TechnicianItem.class).equalTo("full_name_tehnic", selectedItem).findFirst();
                    realm.commitTransaction();

                    spinnerCurItem = position;
                }

                if (selectedTech != null)
                {
                    chkboxRememberTech.setTextColor(Color.parseColor("#ffffff"));
                    chkboxRememberTech.setEnabled(true);
                } else
                {
                    chkboxRememberTech.setChecked(false);
                    chkboxRememberTech.setEnabled(false);
                    chkboxRememberTech.setTextColor(Color.parseColor("#93D8A8"));
                }

/*                spTecnicianList.setBackgroundColor(Color.parseColor("#29B352"));
                spTecnicianList.invalidate();*/
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
        View rootView = inflater.inflate(R.layout.login_tech_selection_fragment, container, false);

        btnNewTechnician = (Button) rootView.findViewById(R.id.btnNewTechnician);
        btnNewTechnician.setOnClickListener(this);

        llTechnNameAndSurname = (LinearLayout) rootView.findViewById(R.id.llTechnNameAndSurname);

        flTechnicianAdded = (FrameLayout) rootView.findViewById(R.id.flTechnicianAdded);

        etTechNome = (EditText) rootView.findViewById(R.id.etTechNome);
        etTechCognome = (EditText) rootView.findViewById(R.id.etTechCognome);

        ibAddTechnician = (ImageButton) rootView.findViewById(R.id.ibAddTechnician);
        ibAddTechnician.setOnClickListener(this);

        chkboxRememberTech = (CheckBox) rootView.findViewById(R.id.chkboxRememberTech);

        btnSessionDisconnect = (Button) rootView.findViewById(R.id.btnSessionDisconnect);
        btnSessionDisconnect.setOnClickListener(this);

        btnApplyAndEnterApp = (Button) rootView.findViewById(R.id.btnApplyAndEnterApp);
        btnApplyAndEnterApp.setOnClickListener(this);

        spTecnicianList = (Spinner) rootView.findViewById(R.id.spTecnicianList);

        realm.beginTransaction();
        RealmResults<GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class).findAll();
        geaItemModelliSize = geaItemModelli.size();
        realm.commitTransaction();

        String technician_list = mSettings.getString("technician_list_json", null);

        FillTechnicianList(technician_list);

        return rootView;
    }

    private void FillTechnicianList(String technician_list)
    {
        if (technician_list != null)
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<TechnicianItem>>()
            {
            }.getType();

            final List<TechnicianItem> techModelList = gson.fromJson(technician_list, type);

            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {

                    realm.beginTransaction();
                    RealmResults<TechnicianItem> techModelListOld = realm.where(TechnicianItem.class).equalTo("company_id", company_id).findAll();
                    techModelListOld.deleteAllFromRealm();
                    realm.commitTransaction();

                    //saTecnicianList.add("");
                    for (TechnicianItem technicianItem : techModelList)
                    {
                        realm.beginTransaction();
                        //saTecnicianList.add(technicianItem.getFullNameTehnic());
                        //TecnicianNameId tecnicianModelR = realm.where(TecnicianNameId.class).equalTo("id", technicianItem.getId()).findFirst();

                        //if (tecnicianModelR == null)
                        {
                            technicianItem.setCompanyId(company_id);
                            realm.copyToRealm(technicianItem);
                        }
                        realm.commitTransaction();
                    }

                    if (bNewTechAdded)
                    {
                        llTechnNameAndSurname.setVisibility(View.GONE);
                        flTechnicianAdded.setVisibility(View.VISIBLE);
                        bNewTechAdded = false;
                    }

                    realm.beginTransaction();
                    RealmResults<TechnicianItem> techModelList = realm.where(TechnicianItem.class).equalTo("company_id", company_id).findAll();
                    realm.commitTransaction();

                    if (techModelList.size() != 0)
                    {
                        saTecnicianList.clear();
                        saTecnicianList.add("");
                        int idPredefinedTech = mSettings.getInt("idPredefinedTech", -1);
                        int selectedTechPos = -1;

                        for (int i = 0; i < techModelList.size(); i++)
                        {
                            saTecnicianList.add(techModelList.get(i).getFullNameTehnic());

                            if (idPredefinedTech != -1 && techModelList.get(i).getId() == idPredefinedTech)
                            {
                                selectedTechPos = i;
                            }
                        }

                        ArrayAdapter<String> technicianListAdapter = new ArrayAdapter<>(activity, R.layout.spinner_tech_selection_row, R.id.tvSpinnerTechSelItem, saTecnicianList);
                        spTecnicianList.setAdapter(technicianListAdapter);

                        if (idPredefinedTech != -1)
                        {
                            spTecnicianList.setSelection(selectedTechPos + 1);
                            chkboxRememberTech.setChecked(true);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnSessionDisconnect)
        {
            loginCommunicator.onReturnToLoginScreen();
        }

        if (view.getId() == R.id.btnNewTechnician)
        {
            llTechnNameAndSurname.setVisibility(View.VISIBLE);
        }

        if (view.getId() == R.id.ibAddTechnician)
        {
            String strTechNome = etTechNome.getText().toString();
            String strTechCognome = etTechCognome.getText().toString();
            String strNomeCognome = strTechNome + " " + strTechCognome;

            if (strNomeCognome.length() > 7)
            {
                bNewTechAdded = true;
                callTechnicianList = networkUtils.loginRequest(this, login, password, strNomeCognome, -1);
            }
        }

        if (view.getId() == R.id.btnApplyAndEnterApp)
        {
            GlobalConstants.selectedTech = selectedTech;

            if (selectedTech == null)
            {
                showToastMessage("Seleziona il tecnico per favore");
                return;
            }

            if (chkboxRememberTech.isChecked())
            {
                if (selectedTech != null)
                {
                    mSettings.edit().putInt("idPredefinedTech", selectedTech.getId()).apply();
                }
            } else
            {
                mSettings.edit().putInt("idPredefinedTech", -1).apply();
            }

            if (NetworkUtils.isNetworkAvailable(activity))
            {
                callLoginToken = networkUtils.loginRequest(TechnicianSelectFragment.this, login, password,
                        selectedTech.getFullNameTehnic(), selectedTech.getId());
            } else
            {
                loginCommunicator.onTechSelectedAndApplied();
            }
        }
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callLoginToken)
        {
            showToastMessage("Token non ricevuto");
            loginCommunicator.onLoginFailed();
        }

        if (call == callVisits)
        {
            showToastMessage("Visite data non ricevuto");
            loginCommunicator.onLoginFailed();
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
                showToastMessage("Ricevere lista tecnici non è riuscito");
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
                    String technician_list = jsonObject.getString("data_tehnic");

                    FillTechnicianList(technician_list);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    showToastMessage("Parse lista tecnici non è riuscito");
                }
            } else
            {
                if (jsonObject.has("error"))
                {
                    final String errorStr;

                    try
                    {
                        errorStr = jsonObject.getString("error");
                        if (errorStr.length() != 0)
                        {
                            showToastMessage(errorStr);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        if (call == callLoginToken)
        {
            String loginResponse = response.body().string();

            response.body().close();

            if (loginResponse == null)
            {
                showToastMessage("Token non ricevuto");

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

                    if (tokenStr.length() != 0)
                    {
                        callVisits = networkUtils.getData(this, GET_VISITS_URL_SUFFIX, tokenStr);

                        if(geaItemModelliSize == 0)
                        {
                            callModels = networkUtils.getData(this, GET_MODELS_URL_SUFFIX, tokenStr);
                        }

                        //loginCommunicator.onTechSelectedAndApplied();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    return;
                }
            } else
            {
                if (jsonObject.has("error"))
                {
                    final String errorStr;

                    try
                    {
                        errorStr = jsonObject.getString("error");
                        if (errorStr.length() != 0)
                        {
                            showToastMessage(errorStr);
                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        if (call == callVisits)
        {
            visitsJSONData = response.body().string();

            if (visitsJSONData == null)
            {
                showToastMessage("Data di visite non è stato ricevuto");
                return;
            }

            Log.d("DEBUG", visitsJSONData);

            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    realm.beginTransaction();

                    inVisitItems = JSON_to_model.getVisitTtemsList(visitsJSONData);

                    visitItems = realm.where(VisitItem.class).findAll();
                    visitItems.deleteAllFromRealm();

                    if (inVisitItems != null && inVisitItems.size() > 0)
                    {
                        for (VisitItem visitItem : inVisitItems)
                        {
                            realm.copyToRealmOrUpdate(visitItem);
                        }
                    }
                    realm.commitTransaction();

                    if(geaItemModelliSize != 0)
                    {
                        loginCommunicator.onTechSelectedAndApplied();
                    }
                }
            });
        }

        if (call == callModels)
        {
            modelsJSONData = response.body().string();

            if (modelsJSONData == null)
            {
                showToastMessage("Data di modelli non è stato ricevuto");
                return;
            }

            Log.d("DEBUG", modelsJSONData);

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(modelsJSONData);
            } catch (JSONException e)
            {
                e.printStackTrace();
                return;
            }

            if (jsonObject.has("type_report_data"))
            {
                try
                {
                    JSONObject type_report_data = jsonObject.getJSONObject("type_report_data");

                    String str_gea_modelli = type_report_data.getString("gea_modelli_rapporto_sopralluogo");
                    String str_gea_sezioni_modelli = type_report_data.getString("gea_sezioni_modelli_rapporto_sopralluogo");
                    String str_gea_items_modelli = type_report_data.getString("gea_items_modelli_rapporto_sopralluogo");

                    Gson gson = new Gson();

                    Type typeGeaModelli = new TypeToken<List<GeaModelloRapporto>>()
                    {
                    }.getType();
                    final List<GeaModelloRapporto> l_geaModelli = gson.fromJson(str_gea_modelli, typeGeaModelli);

                    Type typeGeaSezioniModelli = new TypeToken<List<GeaSezioneModelliRapporto>>()
                    {
                    }.getType();
                    final List<GeaSezioneModelliRapporto> l_geaSezioniModelli = gson.fromJson(str_gea_sezioni_modelli, typeGeaSezioniModelli);

                    Type typeGeaItemsModelli = new TypeToken<List<GeaItemModelliRapporto>>()
                    {
                    }.getType();
                    final List<GeaItemModelliRapporto> l_geaItemsModelli = gson.fromJson(str_gea_items_modelli, typeGeaItemsModelli);


                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            realm.beginTransaction();
                            RealmResults<GeaModelloRapporto> geaModelli = realm.where(GeaModelloRapporto.class).findAll();
                            geaModelli.deleteAllFromRealm();
                            realm.commitTransaction();

                            for (GeaModelloRapporto gm : l_geaModelli)
                            {
                                realm.beginTransaction();
                                realm.copyToRealm(gm);
                                realm.commitTransaction();
                            }

                            realm.beginTransaction();
                            RealmResults<GeaSezioneModelliRapporto> geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class).findAll();
                            geaSezioniModelli.deleteAllFromRealm();
                            realm.commitTransaction();

                            for (GeaSezioneModelliRapporto gs : l_geaSezioniModelli)
                            {
                                realm.beginTransaction();
                                realm.copyToRealm(gs);
                                realm.commitTransaction();
                            }

                            realm.beginTransaction();
                            RealmResults<GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class).findAll();
                            geaItemModelli.deleteAllFromRealm();
                            realm.commitTransaction();

                            for (GeaItemModelliRapporto gi : l_geaItemsModelli)
                            {
                                realm.beginTransaction();
                                realm.copyToRealm(gi);
                                realm.commitTransaction();
                            }

                            loginCommunicator.onTechSelectedAndApplied();
                        }
                    });

                } catch (Exception e)
                {
                    e.printStackTrace();
                    return;
                }
            }

        }
    }

    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}