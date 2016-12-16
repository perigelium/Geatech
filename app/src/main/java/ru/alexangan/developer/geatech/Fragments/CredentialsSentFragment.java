package ru.alexangan.developer.geatech.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.R;

public class CredentialsSentFragment extends Fragment implements View.OnClickListener
{
    private LoginCommunicator mCommunicator;
    Button credentialsSent;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CredentialsSentFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CredentialsSentFragment newInstance(String param1, String param2)
    {
        CredentialsSentFragment fragment = new CredentialsSentFragment();
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
        View rootView = inflater.inflate(R.layout.loginscr_data_submitted, container, false);

        credentialsSent = (Button) rootView.findViewById(R.id.btnCredentialsSent);
        credentialsSent.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btnCredentialsSent)
        {
            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            mCommunicator.onPasswordSentReturned();
        }
    }

}
