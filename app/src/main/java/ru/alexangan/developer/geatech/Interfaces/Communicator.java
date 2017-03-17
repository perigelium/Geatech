package ru.alexangan.developer.geatech.Interfaces;

import android.view.View;

/**
 * Created by user on 11/16/2016.
 */


public interface Communicator
{
    public void onDateTimeSetReturned(Boolean DateTimeAlreadySet);

    public void onDetailedReportReturned();

    public void onSendReportReturned();

    public void onCtrlButtons1Clicked(View view);
    public void onCtrlButtons2Clicked(View view);

    public void OnListItemSwiped(int itemIndex, Boolean dateTimeHasSet);

    public void OnListItemSelected(int itemIndex, Boolean dateTimeHasSet);

    public void OnReportListItemSelected(int itemIndex);

    public void OnInWorkListItemSelected(int itemIndex);

    public void onNotificationReportReturned(View view);

    void OnComingListItemSelected(int itemIndex);

    void onLogoutCommand();
}