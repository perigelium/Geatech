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
import ru.alexangan.developer.geatech.Models.GlobalConstants;
import ru.alexangan.developer.geatech.Models.LoginCredentials;
import ru.alexangan.developer.geatech.Models.TecnicianModel;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.JSON_to_model;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.inVisitItems;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;

public class TechnicianSelectFragment extends Fragment implements View.OnClickListener, Callback
{
    Activity activity;

    private LoginCommunicator loginCommunicator;
    Call callTechnicianList, callLoginToken, callVisits;
    String visitsJSONData;
    NetworkUtils networkUtils;

    ImageButton ibAddTechnician;
    Button btnSessionDisconnect, btnApplyAndEnterApp, btnNewTechnician;
    LinearLayout llTechnNameAndSurname;
    FrameLayout flTechnicianAdded;
    EditText etTechCognome, etTechNome;
    boolean bNewTechAdded;
    TecnicianModel selectedTech;
    private CheckBox chkboxRememberTech;

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
                    selectedTech = realm.where(TecnicianModel.class).equalTo("full_name_tehnic", selectedItem).findFirst();
                    realm.commitTransaction();

                    spinnerCurItem = position;

                    //Toast.makeText(activity, selectedTech.getFullNameTehnic(), Toast.LENGTH_SHORT).show();
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

        String technician_list = mSettings.getString("technician_list", null);

        if (technician_list != null)
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<TecnicianModel>>()
            {
            }.getType();

            final List<TecnicianModel> techModelList = gson.fromJson(technician_list, type);

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

                        if (tecnicianModelR == null)
                        {
                            realm.copyToRealm(technicianItem);
                        }
                        realm.commitTransaction();
                    }

                    ArrayAdapter<String> technicianListAdapter = new ArrayAdapter<>(activity, R.layout.spinner_tech_selection_row, R.id.tvSpinnerTechSelItem, saTecnicianList);
                    spTecnicianList.setAdapter(technicianListAdapter);

                    if (bNewTechAdded)
                    {
                        llTechnNameAndSurname.setVisibility(View.GONE);
                        flTechnicianAdded.setVisibility(View.VISIBLE);
                        bNewTechAdded = false;
                    }
                }
            });
        } else
        {
            RealmResults<TecnicianModel> techModelList = realm.where(TecnicianModel.class).findAll();

            if (techModelList.size() > 0)
            {
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

        return rootView;
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
                callTechnicianList = networkUtils.loginRequest(this, strNomeCognome, -1);
            }
        }

        if (view.getId() == R.id.btnApplyAndEnterApp)
        {
            //getactivity().getFragmentManager().beginTransaction().remove(this).commit();

            GlobalConstants.selectedTech = selectedTech;

            if (selectedTech == null)
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Seleziona il tecnico per favore", Toast.LENGTH_LONG).show();
                    }
                });
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
                callLoginToken = networkUtils.loginRequest(TechnicianSelectFragment.this,
                        selectedTech.getFullNameTehnic(), selectedTech.getId());
            } else
            {
                loginCommunicator.onBtnSelectTechAndEnterAppClicked();
            }
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
            loginCommunicator.onLoginFailed();
        }

        if (call == callVisits)
        {
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, "Receive visits data failed", Toast.LENGTH_LONG).show();
                }
            });
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
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Receive technician list failed", Toast.LENGTH_LONG).show();
                    }
                });

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
                boolean credentialsesFound = mSettings.getBoolean("credentialsesFound", false);

                if (!credentialsesFound)
                {
                    LoginCredentials loginCredentials = new LoginCredentials();

                    String login = mSettings.getString("login", null);
                    String password = mSettings.getString("password", null);

                    if (login != null && password != null)
                    {
                        loginCredentials.setLogin(login);
                        loginCredentials.setPassword(password);
                    }

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(loginCredentials);
                    realm.commitTransaction();
                }

                try
                {
                    String technicianStr = jsonObject.getString("data_tehnic");

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<TecnicianModel>>()
                    {
                    }.getType();

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

                                if (tecnicianModelR == null)
                                {
                                    realm.copyToRealm(technicianItem);
                                }
                                realm.commitTransaction();
                            }

                            ArrayAdapter<String> technicianListAdapter = new ArrayAdapter<>(activity, R.layout.spinner_tech_selection_row, R.id.tvSpinnerTechSelItem, saTecnicianList);
                            spTecnicianList.setAdapter(technicianListAdapter);

                            if (bNewTechAdded)
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

                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(activity, "Receive data failed", Toast.LENGTH_LONG).show();
                        }
                    });

                    loginCommunicator.onLoginFailed();
                }
            } else
            {
                loginCommunicator.onLoginFailed();
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

                //Log.d("DEBUG", "Receive token failed");

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
                        Toast.makeText(activity, "Receive visits data failed", Toast.LENGTH_LONG).show();
                    }
                });

                return;
            }

            inVisitItems = JSON_to_model.getVisitTtemsList(visitsJSONData);

            Log.d("DEBUG", String.valueOf(inVisitItems.size()));

            //Log.d("DEBUG", visitsJSONData);

            loginCommunicator.onBtnSelectTechAndEnterAppClicked();
        }
    }
}
