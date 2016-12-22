package ru.alexangan.developer.geatech.Fragments;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.Models.LoginCredentials;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.mSettings;
import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;

public class UserLoginFragment extends Fragment implements  View.OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Button btnLogin, btnPasswordRecover, btnFirstAccess;
    EditText etLogin, etPassword;
    LoginCommunicator loginCommunicator;
    public static final String CHKBOX_REMEMBER_ME_STATE = "rememberMeState";
    private boolean chkboxRememberMeState;
    CheckBox chkboxRememberMe;


    public UserLoginFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserPasswordRecoverFragment newInstance(String param1, String param2)
    {
        UserPasswordRecoverFragment fragment = new UserPasswordRecoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        LoginCredentials loginCredentials = new LoginCredentials();

        loginCredentials.setLogin("l");
        loginCredentials.setPassword("p");

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(loginCredentials);
        realm.commitTransaction();
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
        View rootView = inflater.inflate(R.layout.user_login_fragment, container, false);

        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnPasswordRecover = (Button) rootView.findViewById(R.id.btnPasswordRecover);
        btnPasswordRecover.setOnClickListener(this);

        btnFirstAccess = (Button) rootView.findViewById(R.id.btnFirstAccess);
        btnFirstAccess.setOnClickListener(this);

        etLogin = (EditText) rootView.findViewById(R.id.etLogin);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
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
        realm.beginTransaction();
        RealmResults <LoginCredentials> loginCredentialses = realm.where(LoginCredentials.class).findAll();
        realm.commitTransaction();

        Boolean credentialsesFound = false;

        for(LoginCredentials loginCredentials : loginCredentialses)
        {
            if(etLogin.getText().toString().compareTo(loginCredentials.getLogin()) == 0
                    && etPassword.getText().toString().compareTo(loginCredentials.getPassword()) == 0)
            {
                credentialsesFound = true;
                break;
            }
        }

        loginCommunicator.onButtonClicked(view, credentialsesFound);
    }
}
