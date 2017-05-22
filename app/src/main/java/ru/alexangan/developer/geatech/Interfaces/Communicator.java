package ru.alexangan.developer.geatech.Interfaces;

/**
 * Created by user on 11/16/2016.*/


public interface Communicator
{
    void onDateTimeSetReturned(int itemIndex);

    void onDetailedReportReturned();

    void onSendReportReturned(int id_rapporto_sopralluogo);

    void onCtrlBtnsBottomClicked(int btnId);

    void onCtrlBtnsSopralluogoClicked(int btnId);

    void OnListItemSwiped(int itemIndex, boolean dateTimeHasSet);

    void OnListItemSelected(int itemIndex, boolean dateTimeHasSet);

    void OnReportListItemSelected(int itemIndex);

    void OnInWorkListItemSelected(int itemIndex);

    void onNotificationReportReturned(int mode);

    void OnComingListItemSelected(int itemIndex);

    void onLogoutCommand();

    void onCoordsSetReturned(int itemIndex);
}