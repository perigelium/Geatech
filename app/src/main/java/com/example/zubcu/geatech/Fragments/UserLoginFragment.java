package com.example.zubcu.geatech.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zubcu.geatech.Interfaces.Communicator;
import com.example.zubcu.geatech.Interfaces.LoginCommunicator;
import com.example.zubcu.geatech.R;

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
    LoginCommunicator loginCommunicator;

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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        loginCommunicator = (LoginCommunicator) getActivity();
    }

    @Override
    public void onClick(View view)
    {
        loginCommunicator.onButtonClicked(view);
    }
}
