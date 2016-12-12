package com.example.zubcu.geatech.Interfaces;

import android.view.View;

/**
 * Created by user on 11/16/2016.
 */


public interface LoginCommunicator
{

    public void onButtonClicked(View view);

    void onPasswordSentReturned();

    void onRecoverPasswordReturned();
}