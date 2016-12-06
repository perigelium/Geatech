package com.example.zubcu.geatech.Interfaces;

/**
 * Created by user on 12/6/2016.
 */

public interface RESTdataReceiverEventListener
{
    public void onTokenReceiveCompleted();

    public void onJSONdataReceiveCompleted();

    public void onEventFailed();
}
