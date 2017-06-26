package ru.alexangan.developer.geatech.Interfaces;

/**
 * Created by user on 11/16/2016.*/


public interface Communicator
{
    void onDateTimeSetReturned(int itemIndex);

    //void onCompilationHorisontalSwipeReturned(int itemIndex, boolean swipeDirection);

    void showDetailedReport(int id_rapporto_sopralluogo);

    void onCtrlBtnsBottomClicked(int btnId);

    void onCtrlBtnsSopralluogoClicked(int btnId);

    void OnVisitListItemSelected(int itemIndex);

    void OnReportListItemSelected(int itemIndex);

    void onListVisitsDisplayModeSelected(int mode);

    void hideHeaderAndFooter();

    void showHeaderAndFooter();

    void onLogoutCommand();

    void showReportsSearchResults(String reportsSearchResultsJSON);

    void OnNotSentListItemSelected(int idVisit);
}