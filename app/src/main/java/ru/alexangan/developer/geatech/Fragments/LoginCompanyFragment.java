package ru.alexangan.developer.geatech.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.Models.LoginCredentials;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;

public class LoginCompanyFragment extends Fragment implements  View.OnClickListener, Callback
{
    Activity activity;
    Button btnLogin, btnPasswordRecover;
    EditText etLogin, etPassword;
    LoginCommunicator loginCommunicator;
    public static final String CHKBOX_REMEMBER_ME_STATE = "rememberMeState";
    private boolean chkboxRememberMeState, credentialsesFound;
    CheckBox chkboxRememberMe;
    private Call callTechnicianList;
    NetworkUtils networkUtils;

    public LoginCompanyFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        networkUtils = new NetworkUtils();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(mSettings.contains(CHKBOX_REMEMBER_ME_STATE))
        {
            chkboxRememberMeState = mSettings.getBoolean(CHKBOX_REMEMBER_ME_STATE, false);
            chkboxRememberMe.setChecked(chkboxRememberMeState);

            if(chkboxRememberMeState == true)
            {
                realm.beginTransaction();
                RealmResults<LoginCredentials> loginCredentialses = realm.where(LoginCredentials.class).findAll();
                realm.commitTransaction();

                if(loginCredentialses.size() != 0)
                {
                    etLogin.setText(loginCredentialses.get(loginCredentialses.size() - 1).getLogin());
                    etPassword.setText(loginCredentialses.get(loginCredentialses.size() - 1).getPassword());
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.login_company_fragment, container, false);

        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnPasswordRecover = (Button) rootView.findViewById(R.id.btnPasswordRecover);
        btnPasswordRecover.setOnClickListener(this);

        etLogin = (EditText) rootView.findViewById(R.id.etLogin);
        etLogin.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                chkboxRememberMe.setChecked(false);
                return false;
            }
        });

        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        etPassword.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                chkboxRememberMe.setChecked(false);
                return false;
            }
        });

        chkboxRememberMe = (CheckBox) rootView.findViewById(R.id.chkboxRememberMe);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        loginCommunicator = (LoginCommunicator) getActivity();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        chkboxRememberMeState = chkboxRememberMe.isChecked();

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(CHKBOX_REMEMBER_ME_STATE, chkboxRememberMeState);
        editor.apply();
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btnLogin)
        {
            realm.beginTransaction();
            RealmResults<LoginCredentials> loginCredentialses = realm.where(LoginCredentials.class).findAll();
            //loginCredentialses.deleteAllFromRealm();
            realm.commitTransaction();

            credentialsesFound = false;
            String strLogin = etLogin.getText().toString();
            String strPassword = etPassword.getText().toString();

            for (LoginCredentials loginCredentials : loginCredentialses)
            {
                if (strLogin.compareTo(loginCredentials.getLogin()) == 0
                        && strPassword.compareTo(loginCredentials.getPassword()) == 0)
                {
                    credentialsesFound = true;
                    break;
                }
            }

/*            mSettings.edit().putBoolean("credentialsesFound", credentialsesFound).apply();

            if(!credentialsesFound)
            {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString("login", strLogin);
                editor.putString("password", strPassword);
                editor.apply();
            }*/

            if(credentialsesFound)
            {
                loginCommunicator.onLoginSucceeded();
            }
            else
            {
                callTechnicianList = networkUtils.loginRequest(this, null, -1);
            }
        }

        if(view.getId() == R.id.btnPasswordRecover)
        {
            loginCommunicator.onRecoverPasswordClicked();
        }
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callTechnicianList)
        {
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, "Receive technician list failed", Toast.LENGTH_LONG).show();
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
                //boolean credentialsesFound = mSettings.getBoolean("credentialsesFound", false);

                if(!credentialsesFound)
                {
                    LoginCredentials loginCredentials = new LoginCredentials();

                    String login = mSettings.getString("login", null);
                    String password = mSettings.getString("password", null);

                    if(login != null && password != null)
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

                    mSettings.edit().putString("data_tehnic", technicianStr).apply();

                    loginCommunicator.onLoginSucceeded();

                } catch (JSONException e)
                {
                    e.printStackTrace();

                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(activity, "Receive technic list failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Receive technic list failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
