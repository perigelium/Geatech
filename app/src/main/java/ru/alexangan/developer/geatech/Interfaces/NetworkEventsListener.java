package ru.alexangan.developer.geatech.Interfaces;

import ru.alexangan.developer.geatech.Models.TecnicianModel;

/**
 * Created by user on 12/6/2016.
 */

public interface NetworkEventsListener
{
    public void onJSONdataReceiveCompleted(TecnicianModel selectedTech);

    public void onEventFailed();
}
