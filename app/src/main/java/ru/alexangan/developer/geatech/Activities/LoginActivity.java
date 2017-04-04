package ru.alexangan.developer.geatech.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import ru.alexangan.developer.geatech.Fragments.LoginCompanyFragment;
import ru.alexangan.developer.geatech.Fragments.LoginPasswordRecoverFragment;
import ru.alexangan.developer.geatech.Fragments.LoginTechSelectionFragment;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.APP_PREFERENCES;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;


public class LoginActivity extends Activity implements LoginCommunicator
{
    private FragmentManager mFragmentManager;

    LoginPasswordRecoverFragment loginPasswordRecoverFragment;
    LoginCompanyFragment loginCompanyFragment;
    LoginTechSelectionFragment loginTechSelectionFragment;

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

        if (getIntent().getBooleanExtra("Exit app", false))
        {
            finish();
            return;
        }

/*        if (getIntent().getBooleanExtra("Password recover", false))
        {
            FragmentTransaction cFragmentTransaction = mFragmentManager.beginTransaction();
            cFragmentTransaction.replace(R.id.loginFragContainer, loginPasswordRecoverFragment);
            cFragmentTransaction.commit();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_window_container);

        //context = getApplicationContext();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        loginCompanyFragment = new LoginCompanyFragment();
        loginPasswordRecoverFragment = new LoginPasswordRecoverFragment();
        loginTechSelectionFragment = new LoginTechSelectionFragment();

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
    }

    @Override
    public void onTechSelectedAndApplied()
    {
        Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public void onLoginSucceeded()
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.loginFragContainer, loginTechSelectionFragment);
        mFragmentTransaction.commit();
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
    public void onRecoverPasswordClicked()
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.loginFragContainer, loginPasswordRecoverFragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onLoginFailed()
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.loginFragContainer, loginCompanyFragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onBackPressed()
    {
        if (loginTechSelectionFragment.isAdded())
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.loginFragContainer, loginCompanyFragment);
            mFragmentTransaction.commit();
        } else
        {
            finish();
        }
    }
}


