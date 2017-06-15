package ru.alexangan.developer.geatech.Interfaces;

/**
 * Created by user on 11/16/2016.*/


public interface Communicator
{
    void onDateTimeSetReturned(int itemIndex);

    void onCompilationHorisontalSwipeReturned(int itemIndex, boolean swipeDirection);

    void onSendReportReturned(int id_rapporto_sopralluogo);

    void onCtrlBtnsBottomClicked(int btnId);

    void onCtrlBtnsSopralluogoClicked(int btnId);

    void OnVisitListItemSwiped(int itemIndex, boolean dateTimeHasSet);

    void OnVisitListItemSelected(int itemIndex, boolean dateTimeHasSet);

    void OnReportListItemSelected(int itemIndex);

    void onNotificationReportReturned(int mode);

    void hideHeaderAndFooter();

    void showHeaderAndFooter();

    void onLogoutCommand();

    void refreshGeaModels();
}