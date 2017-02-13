package ru.alexangan.developer.geatech.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import ru.alexangan.developer.geatech.Fragments.TechnicianSelectFragment;
import ru.alexangan.developer.geatech.Fragments.LoginCompanyFragment;
import ru.alexangan.developer.geatech.Fragments.LoginPasswordRecoverFragment;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.APP_PREFERENCES;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.inVisitItems;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;


public class LoginActivity extends Activity implements LoginCommunicator
{
    private FragmentManager mFragmentManager;

    LoginPasswordRecoverFragment loginPasswordRecoverFragment;
    LoginCompanyFragment loginCompanyFragment;
    TechnicianSelectFragment technicianSelectFragment;
    private Context context;

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
        loginPasswordRecoverFragment = new LoginPasswordRecoverFragment();
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
            cFragmentTransaction.replace(R.id.loginFragContainer, loginPasswordRecoverFragment);
            cFragmentTransaction.commit();
        }
    }

    @Override
    public void onBtnSelectTechAndEnterAppClicked()
    {
        if (inVisitItems != null)
        {
            realm.beginTransaction();
            for (VisitItem visitItem : inVisitItems)
            {
                realm.copyToRealmOrUpdate(visitItem);
            }
            realm.commitTransaction();

            //int visitItemsSize = visitItems.size();
        }

        realm.beginTransaction();
        visitItems = realm.where(VisitItem.class).findAll();
        realm.commitTransaction();

        if(visitItems.size() != 0)
        {
            Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(registerIntent);
        }
        else
        {
            Toast.makeText(this, "Database inizializzazione fallito, controlla la connessione a Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoginSucceeded()
    {
/*        if (view.getId() == R.id.btnPasswordRecover)
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.replace(R.id.loginFragContainer, loginPasswordRecoverFragment);

            mFragmentTransaction.commit();
        }*/

        //if (view.getId() == R.id.btnLogin)
        {
/*            if (!credentialsesFound)
            {
                Toast.makeText(this, "login fallito", Toast.LENGTH_LONG).show();
            }*/
/*            else if(!NetworkUtils.isNetworkAvailable(context))
            {
                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(registerIntent);
            }*/
            //else
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
        if (technicianSelectFragment.isAdded())
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.loginFragContainer, loginCompanyFragment);
            mFragmentTransaction.commit();
        }
        else
        {
            finish();
        }
    }
}


