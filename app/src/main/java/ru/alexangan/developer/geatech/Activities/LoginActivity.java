package ru.alexangan.developer.geatech.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import ru.alexangan.developer.geatech.Fragments.CredentialsSentFragment;
import ru.alexangan.developer.geatech.Fragments.TechnicianSelectFragment;
import ru.alexangan.developer.geatech.Fragments.LoginCompanyFragment;
import ru.alexangan.developer.geatech.Fragments.UserPasswordRecoverFragment;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.Interfaces.NetworkEventsListener;
import ru.alexangan.developer.geatech.Models.TecnicianModel;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;


public class LoginActivity extends Activity implements NetworkEventsListener, LoginCommunicator
{
    private FragmentManager mFragmentManager;

    //UserFirstLoginFragment userFirstLoginFragment;
    UserPasswordRecoverFragment userPasswordRecoverFragment;
    LoginCompanyFragment loginCompanyFragment;
    CredentialsSentFragment credentialsSentFragment;
    TechnicianSelectFragment technicianSelectFragment;
    private Context context;
    public static Realm realm;
    public static SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static TecnicianModel selectedTech;


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        realm.close();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_window_container);

        if (getIntent().getBooleanExtra("Exit app", false))
        {
            finish();
            return;
        }

        context = getApplicationContext();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        loginCompanyFragment = new LoginCompanyFragment();
        //userFirstLoginFragment = new UserFirstLoginFragment();
        userPasswordRecoverFragment = new UserPasswordRecoverFragment();
        credentialsSentFragment = new CredentialsSentFragment();
        technicianSelectFragment = new TechnicianSelectFragment();

        mFragmentManager = getFragmentManager();

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.loginFragContainer, loginCompanyFragment);

        mFragmentTransaction.commit();

        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        try
        {
            realm = Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e)
        {
            try
            {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                realm = Realm.getInstance(realmConfiguration);
            } catch (Exception ex)
            {
                throw ex;
                //No Realm file to remove.
            }
        }

        if (getIntent().getBooleanExtra("Password recover", false))
        {
            FragmentTransaction cFragmentTransaction = mFragmentManager.beginTransaction();
            cFragmentTransaction.replace(R.id.loginFragContainer, userPasswordRecoverFragment);
            cFragmentTransaction.commit();
        }
    }

/*    @Override
    public void onTokenReceiveCompleted()
    {
        NetworkUtils networkUtils = new NetworkUtils();
        callVisits = networkUtils.getJSONfromServer(this);
    }*/

    @Override
    public void onJSONdataReceiveCompleted(TecnicianModel tecnicianModel)
    {
        selectedTech = tecnicianModel;
        Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public void onEventFailed()
    {
        Toast.makeText(this, "login fallito", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginSucceeded(View view, Boolean credentialsesFound)
    {
        if (view.getId() == R.id.tvBtnPasswordRecover)
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.replace(R.id.loginFragContainer, userPasswordRecoverFragment);

            mFragmentTransaction.commit();
        }

/*        if(view.getId() == R.id.btnFirstAccess )
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.replace(R.id.loginFragContainer, userFirstLoginFragment);

            mFragmentTransaction.commit();
        }*/

        if (view.getId() == R.id.btnLogin)
        {
            if (!credentialsesFound)
            {
                Toast.makeText(this, "login fallito", Toast.LENGTH_LONG).show();
            }
/*            else if(!NetworkUtils.isNetworkAvailable(context))
            {
                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(registerIntent);
            }*/
            else
            {
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.loginFragContainer, technicianSelectFragment);
                mFragmentTransaction.commit();

            }
        }
    }

    @Override
    public void onReturnToLoginScreen()
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.loginFragContainer, loginCompanyFragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onPasswordSentReturned()
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.loginFragContainer, loginCompanyFragment);

        mFragmentTransaction.commit();
    }

    @Override
    public void onRecoverPasswordReturned()
    {
        // Implement sending request for a new password

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.loginFragContainer, credentialsSentFragment);

        mFragmentTransaction.commit();
    }

/*    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callLoginToken)
        {
            Toast.makeText(this, "Receive token failed", Toast.LENGTH_LONG).show();
        }

        if (call == callVisits)
        {
            Toast.makeText(this, "Receive JSON data failed", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "Receive token failed", Toast.LENGTH_LONG).show();

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
                    onTokenReceiveCompleted();
                }
            }
        }

        if (call == callVisits)
        {
            visitsJSONData = response.body().string();

            if (visitsJSONData == null)
            {
                Toast.makeText(this, "Receive JSON data failed", Toast.LENGTH_LONG).show();

                return;
            }

            inVisitItems = JSON_to_model.getVisitTtemsList(visitsJSONData);

            Log.d("DEBUG", String.valueOf(inVisitItems.size()));

            //Log.d("DEBUG", visitsJSONData);

            onJSONdataReceiveCompleted();

        }
    }*/
}


