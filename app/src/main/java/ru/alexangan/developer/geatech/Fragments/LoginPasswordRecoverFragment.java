package ru.alexangan.developer.geatech.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.alexangan.developer.geatech.Interfaces.LoginCommunicator;
import ru.alexangan.developer.geatech.R;

public class LoginPasswordRecoverFragment extends Fragment implements View.OnClickListener
{
    Activity activity;
    Button InviaPassword;
    private LoginCommunicator mCommunicator;
    private EditText etMailPasswordRecover;


    public LoginPasswordRecoverFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();
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
        View rootView = inflater.inflate(R.layout.login_password_recover, container, false);

        etMailPasswordRecover = (EditText) rootView.findViewById(R.id.etMailPasswordRecover);

        InviaPassword = (Button) rootView.findViewById(R.id.btnInviaPassword);
        InviaPassword.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btnInviaPassword)
        {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(etMailPasswordRecover.getText().toString()).matches())
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, R.string.PasswordSentCheckYourMail, Toast.LENGTH_LONG).show();
                    }
                });

                mCommunicator.onPasswordSentReturned();
            }
            else
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, R.string.InsertValidEmail, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
