package com.example.zubcu.geatech.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.zubcu.geatech.Interfaces.LocationRetrievedEvents;
import com.example.zubcu.geatech.Interfaces.LoginCommunicator;
import com.example.zubcu.geatech.Network.LocationRetriever;
import com.example.zubcu.geatech.R;

public class UserFirstLoginFragment extends Fragment implements View.OnClickListener
{
    Context context;
    Button InviaPassword;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LoginCommunicator mCommunicator;


    public UserFirstLoginFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserFirstLoginFragment newInstance(String param1, String param2)
    {
        UserFirstLoginFragment fragment = new UserFirstLoginFragment();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCommunicator = (LoginCommunicator) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.loginscr_first_access, container, false);

        InviaPassword = (Button) rootView.findViewById(R.id.btnInviaPassword);
        InviaPassword.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btnInviaPassword)
        {
            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            mCommunicator.onRecoverPasswordReturned();
        }
    }
}
