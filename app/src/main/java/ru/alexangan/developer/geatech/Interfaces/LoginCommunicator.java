package ru.alexangan.developer.geatech.Interfaces;

/**
 * Created by user on 11/16/2016.*/


public interface LoginCommunicator
{

    void onLoginSucceeded();

    void onTechSelectedAndApplied();

    void onReturnToLoginScreen();

    void onPasswordSentReturned();

    void onRecoverPasswordClicked();

    void onLoginFailed();
}