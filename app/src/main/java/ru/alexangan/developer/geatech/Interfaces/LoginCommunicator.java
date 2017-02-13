package ru.alexangan.developer.geatech.Interfaces;

import android.view.View;

/**
 * Created by user on 11/16/2016.
 */


public interface LoginCommunicator
{

    void onLoginSucceeded();

    void onBtnSelectTechAndEnterAppClicked();

    void onReturnToLoginScreen();

    void onPasswordSentReturned();

    void onRecoverPasswordClicked();

    void onLoginFailed();
}