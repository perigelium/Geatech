package ru.alexangan.developer.geatech.Network;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.RealmList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Activities.LoginActivity;
import ru.alexangan.developer.geatech.Interfaces.RESTdataReceiverEventListener;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Utils.JSON_to_model;

public class RESTdataReceiver implements Callback
{
    String loginResponse;
    String visitsJSONData;
    Call callToken, callJSON;

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String REST_URL = "http://www.bludelego.com/dev/geatech/api.php";
    private final String TOKEN_URL_SUFFIX = "?case=login";
    private final String DATA_URL_SUFFIX = "?case=get_raport";
    private final String REST_LOGIN = "alexgheorghianu@yahoo.com";
    private final String REST_PASSWORD = "BF8hWoAr";

    private RESTdataReceiverEventListener callback;
    public static String tokenStr;
    private Activity activity;


    public static RealmList<VisitItem> inVisitItems;

    public RESTdataReceiver(RESTdataReceiverEventListener cb, Activity activity)
    {
        callback = cb;
        this.activity = activity;
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callToken)
        {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Receive token failed", Toast.LENGTH_LONG).show();
                }
            });


        }

        if (call == callJSON)
        {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Receive JSON data failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callToken)
        {
            loginResponse = response.body().string();

            response.body().close();

            if (loginResponse == null)
            {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Receive token failed", Toast.LENGTH_LONG).show();
                    }
                });

                Log.d("DEBUG", "Receive token failed");

                return;
            }

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(loginResponse);
            } catch (JSONException e)
            {
                e.printStackTrace();
                return;
            }

            if (jsonObject.has("token"))
            {
                try
                {
                    tokenStr = jsonObject.getString("token");

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    return;
                }

                if (callback != null && tokenStr.length() != 0)
                {
                    callback.onTokenReceiveCompleted();
                }
            }
        }

        if (call == callJSON)
        {
            visitsJSONData = response.body().string();

            if(visitsJSONData == null)
            {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Receive JSON data failed", Toast.LENGTH_LONG).show();
                    }
                });

                return;
            }

            inVisitItems = JSON_to_model.getVisitTtemsList(visitsJSONData);

            Log.d("DEBUG", String.valueOf(inVisitItems.size()));

            //Log.d("DEBUG", visitsJSONData);

            if (callback != null)
            {
                callback.onJSONdataReceiveCompleted();
            }
        }
    }

    public void getLoginToken()
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = defaultHttpClient.build();

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("login", REST_LOGIN);
            jsonObject.put("password", REST_PASSWORD);

        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));

        Request request = new Request.Builder()
                .url(REST_URL + TOKEN_URL_SUFFIX)
                .post(body)
                .build();

        callToken = okHttpClient.newCall(request);
        callToken.enqueue(this);
    }

    public void getJSONfromServer()
    {
            JSONObject jsonToken = new JSONObject();
            try
            {
                jsonToken.put("token", tokenStr);

            } catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }

            RequestBody body = RequestBody.create(JSON, String.valueOf(jsonToken));

            Request request = new Request.Builder()
                    .url(REST_URL + DATA_URL_SUFFIX)
                    .post(body)
                    .build();

            OkHttpClient client1 = new OkHttpClient();

            callJSON = client1.newCall(request);
            callJSON.enqueue(this);
    }
}
