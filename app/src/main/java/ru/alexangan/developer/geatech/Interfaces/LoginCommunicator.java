package ru.alexangan.developer.geatech.Interfaces;

import android.view.View;

/**
 * Created by user on 11/16/2016.
 */


public interface LoginCommunicator
{

    void onLoginSucceeded(View view, Boolean credentialsesFound);

    void onReturnToLoginScreen();

    void onPasswordSentReturned();

    void onRecoverPasswordReturned();
}