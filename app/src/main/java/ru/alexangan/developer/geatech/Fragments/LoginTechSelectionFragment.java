package ru.alexangan.developer.geatech.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
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
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;

public class LoginTechSelectionFragment extends Fragment implements View.OnClickListener, Callback
{
    Activity activity;

    private LoginCommunicator loginCommunicator;
    Call callTechnicianList, callLoginToken, callVisits, callModels;
    String visitsJSONData, login, password;
    NetworkUtils networkUtils;

    ImageButton ibtnAddTechnician;
    Button btnSessionDisconnect, btnApplyAndEnterApp, btnNewTechnician;
    LinearLayout llTechnNameAndSurname;
    FrameLayout flTechnicianAdded;
    EditText etTechCognome, etTechNome;
    boolean bNewTechAdded;
    TechnicianItem lastSelectedTech;
    private CheckBox chkboxRememberTech;

    Spinner spTecnicianList;
    int spinnerCurItem;

    ArrayList<String> saTecnicianList;
    int geaItemModelliSize;
    private ProgressDialog downloadingDialog;

    public LoginTechSelectionFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onResume()
    {
        super.onResume();

        spinnerCurItem = -1;
        bNewTechAdded = false;

        login = mSettings.getString("login", null);
        password = mSettings.getString("password", null);

        enableInput();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        networkUtils = new NetworkUtils();
        saTecnicianList = new ArrayList<>();
        activity = getActivity();

        downloadingDialog = new ProgressDialog(getActivity());
        downloadingDialog.setTitle("");
        downloadingDialog.setMessage(getString(R.string.DownloadingDataPleaseWait));
        downloadingDialog.setIndeterminate(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        loginCommunicator = (LoginCommunicator) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.login_tech_selection_fragment, container, false);

        TextView tvSupplierName = (TextView) rootView.findViewById(R.id.tvSupplierName);
        tvSupplierName.setText(GlobalConstants.gea_supplier);

        btnNewTechnician = (Button) rootView.findViewById(R.id.btnNewTechnician);
        btnNewTechnician.setOnClickListener(this);

        llTechnNameAndSurname = (LinearLayout) rootView.findViewById(R.id.llTechnNameAndSurname);

        flTechnicianAdded = (FrameLayout) rootView.findViewById(R.id.flTechnicianAdded);
        flTechnicianAdded.setVisibility(View.GONE);

        etTechNome = (EditText) rootView.findViewById(R.id.etTechNome);
        etTechCognome = (EditText) rootView.findViewById(R.id.etTechCognome);

        ibtnAddTechnician = (ImageButton) rootView.findViewById(R.id.ibtnAddTechnician);
        ibtnAddTechnician.setOnClickListener(this);

        chkboxRememberTech = (CheckBox) rootView.findViewById(R.id.chkboxRememberTech);

        btnSessionDisconnect = (Button) rootView.findViewById(R.id.btnSessionDisconnect);
        btnSessionDisconnect.setOnClickListener(this);

        btnApplyAndEnterApp = (Button) rootView.findViewById(R.id.btnApplyAndEnterApp);
        btnApplyAndEnterApp.setOnClickListener(this);


        spTecnicianList = (Spinner) rootView.findViewById(R.id.spTecnicianList);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class).findAll();
        geaItemModelliSize = geaItemModelli.size();
        realm.commitTransaction();

        String technician_list = mSettings.getString("technician_list_json", null);

        FillTechnicianList(technician_list);

        return rootView;
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
                    Realm realm = Realm.getDefaultInstance();
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    realm.beginTransaction();
                    lastSelectedTech = realm.where(TechnicianItem.class).equalTo("full_name_tehnic", selectedItem).findFirst();
                    realm.commitTransaction();

                    spinnerCurItem = position;
                }

                if (lastSelectedTech != null)
                {
                    chkboxRememberTech.setTextColor(Color.parseColor("#ffffff"));
                    chkboxRememberTech.setEnabled(true);
                } else
                {
                    chkboxRememberTech.setChecked(false);
                    chkboxRememberTech.setEnabled(false);
                    chkboxRememberTech.setTextColor(Color.parseColor("#93D8A8"));
                }

                enableInput();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
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
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    RealmResults<TechnicianItem> techModelListOld = realm.where(TechnicianItem.class).equalTo("company_id", company_id).findAll();
                    techModelListOld.deleteAllFromRealm();
                    realm.commitTransaction();

                    for (TechnicianItem technicianItem : techModelList)
                    {
                        realm.beginTransaction();

                        technicianItem.setCompanyId(company_id);
                        realm.copyToRealm(technicianItem);

                        realm.commitTransaction();
                    }

                    if (bNewTechAdded)
                    {
                        llTechnNameAndSurname.setVisibility(View.GONE);
                        flTechnicianAdded.setVisibility(View.VISIBLE);
                        bNewTechAdded = false;
                    }

                    realm.beginTransaction();
                    RealmResults<TechnicianItem> techModelListLast = realm.where(TechnicianItem.class).equalTo("company_id", company_id).findAll();
                    realm.commitTransaction();

                    if (techModelListLast.size() != 0)
                    {
                        saTecnicianList.clear();
                        saTecnicianList.add("");
                        int idPredefinedTech = mSettings.getInt("idPredefinedTech", -1);
                        int selectedTechPos = -1;
                        String fullNameTechnic = "";
                        int i;

                        for (i = 0; i < techModelListLast.size(); i++)
                        {
                            String fullTechName = techModelListLast.get(i).getFullNameTehnic();
                            saTecnicianList.add(fullTechName);

                            if (idPredefinedTech != -1 && techModelListLast.get(i).getId() == idPredefinedTech)
                            {
                                selectedTechPos = i + 1;
                                chkboxRememberTech.setChecked(true);
                            }
                        }

                        ArrayAdapter<String> technicianListAdapter = new ArrayAdapter<>(activity, R.layout.spinner_tech_selection_row, R.id.tvSpinnerTechSelItem, saTecnicianList);
                        spTecnicianList.setAdapter(technicianListAdapter);

                        spTecnicianList.setSelection(selectedTechPos);

                        if (idPredefinedTech != -1)
                        {

                            chkboxRememberTech.setChecked(true);
                        } else
                        {
                            chkboxRememberTech.setChecked(false);
                        }

                        if (i != techModelList.size())
                        {
                            realm.beginTransaction();
                            lastSelectedTech = realm.where(TechnicianItem.class).equalTo("full_name_tehnic", fullNameTechnic).findFirst();
                            realm.commitTransaction();
                        } else
                        {
                            lastSelectedTech = null;
                        }
                    }
                    realm.close();
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

        if (view.getId() == R.id.ibtnAddTechnician)
        {
            if (!NetworkUtils.isNetworkAvailable(activity))
            {
                showToastMessage(getString(R.string.CheckInternetConnection));
                return;
            }

            String strTechNome = etTechNome.getText().toString();
            String strTechCognome = etTechCognome.getText().toString();
            String strNomeCognome = strTechNome + " " + strTechCognome;

            if (strTechNome.length() > 3 && strTechCognome.length() > 3 && strNomeCognome.length() > 6)
            {
                disableInputAndShowProgressDialog();

                bNewTechAdded = true;
                callTechnicianList = networkUtils.loginRequest(this, login, password, strNomeCognome, -1);
            } else
            {
                showToastMessage(getString(R.string.InsertNameAndSurnameFirst));
            }
        }

        if (view.getId() == R.id.btnApplyAndEnterApp)
        {
            GlobalConstants.selectedTech = lastSelectedTech;

            if (lastSelectedTech == null)
            {
                showToastMessage(getString(R.string.SelectTechPlease));
                return;
            }

            if (NetworkUtils.isNetworkAvailable(activity))
            {
                disableInputAndShowProgressDialog();

                callLoginToken = networkUtils.loginRequest(LoginTechSelectionFragment.this, login, password,
                        lastSelectedTech.getFullNameTehnic(), lastSelectedTech.getId());
            } else
            {
                btnApplyAndEnterApp.setAlpha(.4f);
                btnApplyAndEnterApp.setEnabled(false);

                loginCommunicator.onTechSelectedAndApplied();
            }
        }
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                enableInput();
            }
        });

        if (call == callLoginToken)
        {
            showToastMessage(getString(R.string.ServerError));
            loginCommunicator.onLoginFailed();
        }

        if (call == callVisits)
        {
            showToastMessage(getString(R.string.ListVisitsReceiveFailed));
            loginCommunicator.onLoginFailed();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (chkboxRememberTech.isChecked())
        {
            if (lastSelectedTech != null)
            {
                mSettings.edit().putInt("idPredefinedTech", lastSelectedTech.getId()).apply();
            }
        } else
        {
            mSettings.edit().putInt("idPredefinedTech", -1).apply();
        }
        flTechnicianAdded.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callTechnicianList)
        {
            String technListResponse = response.body().string();

            response.body().close();

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(technListResponse);

                if (jsonObject.has("data_tehnic"))
                {
                    try
                    {
                        String technician_list = jsonObject.getString("data_tehnic");

                        FillTechnicianList(technician_list);

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        showToastMessage(getString(R.string.DatabaseError));
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
                        }
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
                showToastMessage("JSON parse error");
            }

            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    enableInput();
                }
            });
        }

        if (call == callLoginToken)
        {
            String loginResponse = response.body().string();

            response.body().close();

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(loginResponse);

                if (jsonObject.has("token"))
                {
                    try
                    {
                        tokenStr = jsonObject.getString("token");

                        if (tokenStr.length() != 0)
                        {
                            try
                            {
                                boolean geaModelsProcessUpdate = geaItemModelliSize != 0;

                                callModels = networkUtils.getData(this, GET_MODELS_URL_SUFFIX, tokenStr, null, null, geaModelsProcessUpdate);

                            } catch (Exception e)
                            {
                                e.printStackTrace();

                                showToastMessage(getString(R.string.ReceivedDataError));

                                activity.runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        enableInput();
                                    }
                                });
                            }
                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();

                        activity.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                enableInput();
                            }
                        });
                    }
                } else
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            enableInput();
                        }
                    });

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
                        }
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                showToastMessage(getString(R.string.ReceivedDataError));

                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        enableInput();
                    }
                });
            }
        }

        if (call == callModels)
        {
            String modelsJSONData = response.body().string();

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(modelsJSONData);

                if (jsonObject.has("models_is_uptodate"))
                {
                    try
                    {
                        String models_is_uptodate = jsonObject.getString("models_is_uptodate");

                        if (models_is_uptodate.equals("1") && geaItemModelliSize != 0)
                        {
                            callVisits = networkUtils.getData(this, GET_VISITS_URL_SUFFIX, tokenStr, null, null, false);

                            return;
                        }

                        if (jsonObject.has("type_report_data"))
                        {
                            JSONObject type_report_data = jsonObject.getJSONObject("type_report_data");

                            String str_gea_modelli = type_report_data.getString("gea_modelli_rapporto_sopralluogo");
                            String str_gea_sezioni_modelli = type_report_data.getString("gea_sezioni_modelli_rapporto_sopralluogo");
                            String str_gea_items_modelli = type_report_data.getString("gea_items_modelli_rapporto_sopralluogo");

                            if (Build.VERSION.SDK_INT >= 24)
                            {
                                str_gea_sezioni_modelli = String.valueOf(Html.fromHtml(str_gea_sezioni_modelli, Html.FROM_HTML_MODE_LEGACY));
                                str_gea_items_modelli = String.valueOf(Html.fromHtml(str_gea_items_modelli, Html.FROM_HTML_MODE_LEGACY));
                            } else
                            {
                                str_gea_sezioni_modelli = String.valueOf(Html.fromHtml(str_gea_sezioni_modelli));
                                str_gea_items_modelli = String.valueOf(Html.fromHtml(str_gea_items_modelli));
                            }

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

                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();

                            RealmResults<GeaModelloRapporto> geaModelli = realm.where(GeaModelloRapporto.class).findAll();
                            geaModelli.deleteAllFromRealm();
                            realm.commitTransaction();

                            realm.beginTransaction();
                            for (GeaModelloRapporto gm : l_geaModelli)
                            {
                                realm.copyToRealm(gm);
                            }
                            realm.commitTransaction();

                            realm.beginTransaction();
                            RealmResults<GeaSezioneModelliRapporto> geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class).findAll();
                            geaSezioniModelli.deleteAllFromRealm();
                            realm.commitTransaction();

                            realm.beginTransaction();
                            for (GeaSezioneModelliRapporto gs : l_geaSezioniModelli)
                            {
                                realm.copyToRealm(gs);
                            }
                            realm.commitTransaction();

                            realm.beginTransaction();
                            RealmResults<GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class).findAll();
                            geaItemModelli.deleteAllFromRealm();
                            realm.commitTransaction();

                            realm.beginTransaction();
                            for (GeaItemModelliRapporto gi : l_geaItemsModelli)
                            {
                                realm.copyToRealm(gi);
                            }
                            realm.commitTransaction();

                            geaItemModelliSize = l_geaItemsModelli.size();

                            if (geaItemModelliSize != 0)
                            {
                                callVisits = networkUtils.getData(this, GET_VISITS_URL_SUFFIX, tokenStr, null, null, false);
                            } else
                            {
                                activity.runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        enableInput();
                                    }
                                });
                            }
                            realm.close();
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e)
            {
                e.printStackTrace();

                showToastMessage(getString(R.string.ReceivedDataError));
            }
        }

        if (call == callVisits)
        {
            visitsJSONData = response.body().string();

            try
            {

                inVisitItems = JSON_to_model.getVisitTtemsList(visitsJSONData);

                if (inVisitItems != null && inVisitItems.size() > 0)
                {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    RealmResults<VisitItem> dVisitItems = realm.where(VisitItem.class).findAll();
                    dVisitItems.deleteAllFromRealm();
                    realm.commitTransaction();

                    realm.beginTransaction();
                    for (VisitItem visitItem : inVisitItems)
                    {
                        realm.copyToRealmOrUpdate(visitItem);
                    }
                    realm.commitTransaction();
                    realm.close();
                }

                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        downloadingDialog.dismiss();



                            loginCommunicator.onTechSelectedAndApplied();

                    }
                });
            } catch (Exception e)
            {
                e.printStackTrace();

                showToastMessage(getString(R.string.ReceivedDataError));

                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        enableInput();
                    }
                });
            }
        }
    }

    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableInputAndShowProgressDialog()
    {
        btnNewTechnician.setAlpha(.4f);
        btnNewTechnician.setEnabled(false);
        btnApplyAndEnterApp.setAlpha(.4f);
        btnApplyAndEnterApp.setEnabled(false);

        downloadingDialog.show();
    }

    private void enableInput()
    {
        btnNewTechnician.setAlpha(1.0f);
        btnNewTechnician.setEnabled(true);
        btnApplyAndEnterApp.setAlpha(1.0f);
        btnApplyAndEnterApp.setEnabled(true);

        downloadingDialog.dismiss();
    }
}