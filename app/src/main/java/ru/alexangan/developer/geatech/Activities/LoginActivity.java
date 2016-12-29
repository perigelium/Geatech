package ru.alexangan.developer.geatech.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import ru.alexangan.developer.geatech.Fragments.UserFirstLoginFragment;
import ru.alexangan.developer.geatech.Fragments.UserLoginFragment;
import ru.alexangan.developer.geatech.Fragments.UserPasswordRecoverFragment;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.Interfaces.RESTdataReceiverEventListener;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.Network.RESTdataReceiver;
import ru.alexangan.developer.geatech.R;


public class LoginActivity extends Activity implements RESTdataReceiverEventListener, LoginCommunicator
{
    RESTdataReceiver restDataReceiver;
    private FragmentManager mFragmentManager;

    UserFirstLoginFragment userFirstLoginFragment;
    UserPasswordRecoverFragment userPasswordRecoverFragment;
    UserLoginFragment userLoginFragment;
    CredentialsSentFragment credentialsSentFragment;
    private Context context;
    public static Realm realm;
    public static SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";


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

        if( getIntent().getBooleanExtra("Exit app", false))
        {
            finish();
            return;
        }

        context = getApplicationContext();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        userLoginFragment = new UserLoginFragment();
        userFirstLoginFragment = new UserFirstLoginFragment();
        userPasswordRecoverFragment = new UserPasswordRecoverFragment();
        credentialsSentFragment = new CredentialsSentFragment();

        mFragmentManager = getFragmentManager();

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.loginFragContainer, userLoginFragment);

        mFragmentTransaction.commit();

        restDataReceiver = new RESTdataReceiver( this, this );

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

        if( getIntent().getBooleanExtra("Password recover", false))
        {
            FragmentTransaction cFragmentTransaction = mFragmentManager.beginTransaction();
            cFragmentTransaction.replace(R.id.loginFragContainer, userPasswordRecoverFragment);
            cFragmentTransaction.commit();
        }
    }

    @Override
    public void onTokenReceiveCompleted()
    {
        restDataReceiver.getJSONfromServer();
    }

    @Override
    public void onJSONdataReceiveCompleted()
    {
        Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public void onEventFailed()
    {
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onButtonClicked(View view, Boolean credentialsesFound)
    {
        if(view.getId() == R.id.btnPasswordRecover )
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.replace(R.id.loginFragContainer, userPasswordRecoverFragment);

            mFragmentTransaction.commit();
        }

        if(view.getId() == R.id.btnFirstAccess )
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.replace(R.id.loginFragContainer, userFirstLoginFragment);

            mFragmentTransaction.commit();
        }

        if(view.getId() == R.id.btnLogin )
        {
            if (!credentialsesFound)
            {
                Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
            }
            else if(!NetworkUtils.isNetworkAvailable(context))
            {
                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(registerIntent);
            }
            else
            {
                restDataReceiver.getLoginToken();
            }
        }
    }

    @Override
    public void onPasswordSentReturned()
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.loginFragContainer, userLoginFragment);

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
}


