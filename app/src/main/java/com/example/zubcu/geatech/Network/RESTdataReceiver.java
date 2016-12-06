package com.example.zubcu.geatech.Network;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.zubcu.geatech.Activities.LoginActivity;
import com.example.zubcu.geatech.Interfaces.RESTdataReceiverEventListener;
import com.example.zubcu.geatech.Models.VisitItem;
import com.example.zubcu.geatech.Utils.JSON_to_model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private String tokenStr;
    private Activity loginActivity;


    public static ArrayList<VisitItem> visitItems;

    public RESTdataReceiver(RESTdataReceiverEventListener cb, LoginActivity activity)
    {
        callback = cb;
        loginActivity = activity;
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callToken)
        {
            loginActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(loginActivity, "Receive token failed", Toast.LENGTH_LONG).show();
                }
            });


        }

        if (call == callJSON)
        {
            loginActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(loginActivity, "Receive JSON data failed", Toast.LENGTH_LONG).show();
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
                loginActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(loginActivity, "Receive token failed", Toast.LENGTH_LONG).show();
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
                loginActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(loginActivity, "Receive JSON data failed", Toast.LENGTH_LONG).show();
                    }
                });

                return;
            }

            visitItems = JSON_to_model.getVisitTtemsList(visitsJSONData);

            Log.d("DEBUG", String.valueOf(visitItems.size()));

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
