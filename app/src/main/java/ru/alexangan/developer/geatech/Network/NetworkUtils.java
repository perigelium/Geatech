package ru.alexangan.developer.geatech.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 12/20/2016.
 */

public class NetworkUtils
{
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String REST_URL = "http://www.bludelego.com/dev/geatech/api.php";
    private final String DATA_URL_SUFFIX = "?case=set_data_supraluogo";

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public Call sendReport(Callback callback, String gsonStr)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = defaultHttpClient.build();

        RequestBody body = RequestBody.create(JSON, gsonStr);

        Request request = new Request.Builder()
                .url(REST_URL + DATA_URL_SUFFIX)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);

        return call;
    }
}
