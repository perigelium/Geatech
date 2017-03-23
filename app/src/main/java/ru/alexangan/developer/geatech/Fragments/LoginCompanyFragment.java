package ru.alexangan.developer.geatech.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import ru.alexangan.developer.geatech.Activities.MainActivity;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.Models.LoginCredentials;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;

public class LoginCompanyFragment extends Fragment implements View.OnClickListener, Callback
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
    String strLogin, strPassword;
    private ProgressDialog downloadingDialog;

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

        downloadingDialog = new ProgressDialog(getActivity());
        downloadingDialog.setTitle("");
        downloadingDialog.setMessage(getString(R.string.DownloadingDataPleaseWait));
        downloadingDialog.setIndeterminate(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (mSettings.contains(CHKBOX_REMEMBER_ME_STATE))
        {
            chkboxRememberMeState = mSettings.getBoolean(CHKBOX_REMEMBER_ME_STATE, false);
            chkboxRememberMe.setChecked(chkboxRememberMeState);

            if (chkboxRememberMeState == true)
            {
                realm.beginTransaction();
                LoginCredentials loginCredentials = realm.where(LoginCredentials.class).findFirst();
                realm.commitTransaction();

                if (loginCredentials!=null)
                {
                    String strLogin = loginCredentials.getLogin();
                    etLogin.setText(strLogin);
                    String strPassword = loginCredentials.getPassword();
                    etPassword.setText(strPassword);
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

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                btnLogin.setAlpha(1.0f);
                btnLogin.setEnabled(true);
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnLogin)
        {
/*            btnLogin.setAlpha(.4f);
            btnLogin.setEnabled(false);*/

            realm.beginTransaction();
            RealmResults<LoginCredentials> loginCredentialses = realm.where(LoginCredentials.class).findAll();

            //loginCredentialses.deleteAllFromRealm();             ////   Remove

            realm.commitTransaction();

            credentialsesFound = false;
            strLogin = etLogin.getText().toString();

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strLogin).matches())
            {
                showToastMessage(getString(R.string.InvalidLogin));
                return;
            }

            strPassword = etPassword.getText().toString();

            if (strPassword.length() < 5)
            {
                showToastMessage(getString(R.string.InvalidPassword));
                return;
            }

            for (LoginCredentials loginCredentials : loginCredentialses)
            {
                if (strLogin.equals(loginCredentials.getLogin())
                        && strPassword.equals(loginCredentials.getPassword()))
                {
                    credentialsesFound = true;
                    company_id = loginCredentials.getCompany_id();
                    break;
                }
            }

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("login", strLogin);
            editor.putString("password", strPassword);
            editor.apply();

            if (!NetworkUtils.isNetworkAvailable(activity))
            {
                if(credentialsesFound)
                {
                    loginCommunicator.onLoginSucceeded();
                }
                else
                {
                    showToastMessage(getString(R.string.LoginFailedCheckInternetConnection));
                }
            } else
            {
                disableInputAndShowProgressDialog();

                callTechnicianList = networkUtils.loginRequest(this, strLogin, strPassword, null, -1);
            }
        }

        if (view.getId() == R.id.btnPasswordRecover)
        {
            loginCommunicator.onRecoverPasswordClicked();
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

        if (call == callTechnicianList)
        {
            showToastMessage(getString(R.string.LoginFailedCheckInternetConnection));
        }
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
            } catch (JSONException e)
            {
                e.printStackTrace();
                enableInput();
                return;
            }

            if (jsonObject.has("data_tehnic"))
            {
                if (!credentialsesFound && strLogin != null && strPassword != null)
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            realm.beginTransaction();
                            RealmResults<LoginCredentials> loginCredentialses = realm.where(LoginCredentials.class).findAll();
                            loginCredentialses.deleteAllFromRealm();
                            company_id = loginCredentialses.size();
                            LoginCredentials loginCredentials = new LoginCredentials(company_id, strLogin, strPassword);
                            realm.copyToRealm(loginCredentials);
                            realm.commitTransaction();
                        }
                    });
                }

                try
                {
                    String technicianStr = jsonObject.getString("data_tehnic");

                    mSettings.edit().putString("technician_list_json", technicianStr).apply();

                    downloadingDialog.dismiss();

                    loginCommunicator.onLoginSucceeded();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    showToastMessage(getString(R.string.ParsingTechListFailed));
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
                else
                {
                    showToastMessage(getString(R.string.LoginOrPasswordNotValid));
                }
            }
        }

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                enableInput();
            }
        });
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

    private void disableInputAndShowProgressDialog()
    {
        btnLogin.setAlpha(0.4f);
        btnLogin.setEnabled(false);

        downloadingDialog.show();
    }

    private void enableInput()
    {
        btnLogin.setAlpha(1.0f);
        btnLogin.setEnabled(true);

        downloadingDialog.dismiss();
    }
}
