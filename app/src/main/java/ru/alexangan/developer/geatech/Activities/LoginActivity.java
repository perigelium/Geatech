package ru.alexangan.developer.geatech.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.alexangan.developer.geatech.Fragments.LoginCompanyFragment;
import ru.alexangan.developer.geatech.Fragments.LoginTechSelectionFragment;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.APP_PREFERENCES;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;


public class LoginActivity extends Activity implements LoginCommunicator
{
    private FragmentManager mFragmentManager;

    LoginCompanyFragment loginCompanyFragment;
    LoginTechSelectionFragment loginTechSelectionFragment;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (getIntent().getBooleanExtra("Exit app", false))
        {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_window_container);

        //context = getApplicationContext();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        loginCompanyFragment = new LoginCompanyFragment();
        loginTechSelectionFragment = new LoginTechSelectionFragment();

        mFragmentManager = getFragmentManager();

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.loginFragContainer, loginCompanyFragment);

        mFragmentTransaction.commit();
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


