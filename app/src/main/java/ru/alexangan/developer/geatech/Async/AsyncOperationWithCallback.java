package ru.alexangan.developer.geatech.Async;

import android.os.AsyncTask;

import ru.alexangan.developer.geatech.Interfaces.OnTaskCompleted;

/**
 * Created by user on 23.03.2017.
 */

public class AsyncOperationWithCallback extends AsyncTask<Object, Object, Object>
{
    private OnTaskCompleted listener;

    public AsyncOperationWithCallback(OnTaskCompleted listener)
    {
        this.listener = listener;
    }

    // required methods

    @Override
    protected Object doInBackground(Object... objects)
    {
        return null;
    }

    protected void onPostExecute(Object o)
    {
        // your stuff
        listener.onTaskCompleted();
    }
}
