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

    public void onCtrlButtonClicked(View view);

    public void OnListItemSwiped(int itemIndex, Boolean dateTimeHasSet);

    public void OnListItemSelected(int itemIndex, Boolean dateTimeHasSet);

    public void OnListItemSelected(int itemIndex);

    public void onNotificationReportReturned(View view);

    void disableCtrlButtons2(boolean enable);

}