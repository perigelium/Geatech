package com.example.zubcu.geatech.Interfaces;

/**
 * Created by user on 11/16/2016.
 */


public interface Communicator
{
    public void onDateTimeSetReturned(Boolean DateTimeAlreadySet);

    public void onDetailedReportReturned();

    public void onSendReportReturned();
}