package ru.alexangan.developer.geatech.Interfaces;

import android.view.View;

/**
 * Created by user on 11/16/2016.
 */


public interface Communicator
{
    void onDateTimeSetReturned(boolean mDatetimeSetBefore);

    void onDetailedReportReturned();

    void onSendReportReturned();

    void onCtrlButtons1Clicked(View view);
    void onCtrlButtons2Clicked(View view);

    void OnListItemSwiped(int itemIndex, boolean dateTimeHasSet);

    void OnListItemSelected(int itemIndex, boolean dateTimeHasSet);

    void OnReportListItemSelected(int itemIndex);

    void OnInWorkListItemSelected(int itemIndex);

    void onNotificationReportReturned(View view);

    void OnComingListItemSelected(int itemIndex);

    void onLogoutCommand();
}