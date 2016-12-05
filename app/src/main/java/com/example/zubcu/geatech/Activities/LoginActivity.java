package com.example.zubcu.geatech.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import static com.example.zubcu.geatech.R.*;


public class LoginActivity extends Activity implements Callback, View.OnClickListener
{
    Button btnLogin;
    Button btAcces;
    Button btPassword;
    OkHttpClient okHttpClient;
    public static Context context;
    String loginResponse;
    public String visitsJSONData;
    Boolean tokenHasGot;
    Call callToken, callJSON;

    //public static Observable<Boolean> myTokenObservable;
    //public static Observable<String> myJSONObservable;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Subscriber<Boolean> myTokenSubscriber = new Subscriber<Boolean>()
    {
        @Override
        public void onNext(Boolean s)
        {
            tokenHasGot = s;
        }

        @Override
        public void onCompleted()
        {
        }

        @Override
        public void onError(Throwable e)
        {
        }
    };

    Subscriber<String> myJSONSubscriber = new Subscriber<String>()
    {
        @Override
        public void onNext(String s)
        {
            visitsJSONData = s;
        }

        @Override
        public void onCompleted()
        {
        }

        @Override
        public void onError(Throwable e)
        {
        }
    };

    public Observable<Boolean> myTokenObservable = Observable.defer(new Func0<Observable<Boolean>>()
    {
        @Override
        public Observable<Boolean> call()
        {
            getLoginToken();

            return Observable.just(tokenHasGot);
        }
});

/*    public Observable<String> myJSONObservable = Observable.defer(new Func0<Observable<String>>()
    {
        @Override
        public Observable<String> call()
        {
            getJSONfromServer();
            return Observable.just(visitsJSONData);
        }
    });*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.login_activity);

        context = getApplicationContext();
        tokenHasGot = false;


        btnLogin = (Button) findViewById(id.btLogin);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onFailure(Call call, IOException e)
    {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {

            if (!response.isSuccessful())
            {
                throw new IOException("Unexpected code " + response);
            }

        if(call == callToken)
        {
            loginResponse = response.body().string();

            response.body().close();

            if (loginResponse == null)
            {
                Toast.makeText(getApplicationContext(), "Receive token failed" ,Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = null;

            try
            {
                jsonObject = new JSONObject(loginResponse);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            if (jsonObject.has("token"))
            {
                getJSONfromServer(jsonObject);
            }
        }

        if(call == callJSON)
        {
            visitsJSONData = response.body().string();

            Log.d("DEBUG", visitsJSONData);

            Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
            registerIntent.putExtra("JSON", visitsJSONData);
            startActivity(registerIntent);
        }
    }

    @Override
    public void onClick(View view)
    {
/*        myTokenObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(myTokenSubscriber);*/

        getLoginToken();

        if(loginResponse == null)
        {

            return;
        }

/*
        myJSONObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(myJSONSubscriber);*/

        if(visitsJSONData == null)
        {
            //Toast.makeText(getApplicationContext(), "Unable to download data" ,Toast.LENGTH_SHORT).show();
            return;
        }

/*        Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
        registerIntent.putExtra("JSON", visitsJSONData);
        startActivity(registerIntent);*/
    }

    private Boolean getLoginToken()
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        //defaultHttpClient.interceptors().add(new ResponseInterceptor());
        okHttpClient = defaultHttpClient.build();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("login", "alexgheorghianu@yahoo.com");
            jsonObject.put("password", "BF8hWoAr");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//                String json = "{\"login\": \"alexgheorghianu@yahoo.com\", \"password\": \"BF8hWaAr\"}";

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));

/*                RequestBody formBody = new FormBody.Builder()
                        .add("login", "alexgheorghianu@yahoo.com")
                        .add("password", "BF8hWaAr")
                        .add("sender", "Alexandr")
                        .build();*/

        Request request = new Request.Builder()
                .url("http://www.bludelego.com/dev/geatech/api.php?case=login")
                .post(body)
                .build();

        callToken = okHttpClient.newCall(request);
        callToken.enqueue(this);

        //okHttpClient.newCall(request).enqueue(this);

/*        {

            @Override
            public void onFailure(Call call, IOException e)
            {
                Log.d( "DEBUG", "get token failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                if (!response.isSuccessful())
                {
                    throw new IOException("Unexpected code " + response);
                }
*//*                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++)
                        {
                            Log.d("DEBUG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }*//*

                loginResponse = response.body().string();

                response.body().close();

                if (loginResponse == null)
                {
                    return;
                }

                JSONObject jsonObject1 = null;

                try
                {
                    jsonObject1 = new JSONObject(loginResponse);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if(jsonObject1.has("token"))
                {

*//*                    myTokenObservable = Observable.defer(new Func0<Observable<Boolean>>() {
                        @Override public Observable<Boolean> call() {

                            return Observable.just(true);
                        }
                    });*//**//*


 *//**//*                   myTokenObservable = Observable.create(
                            new Observable.OnSubscribe<Boolean>()
                            {
                                @Override
                                public void call(Subscriber<? super Boolean> sub)
                                {
                                    sub.onNext(true);
                                    //sub.onCompleted();
                                }
                            }
                    );*//*

                    tokenHasGot = true;
                }
            }
        });*/

        return tokenHasGot;
    }

    void getJSONfromServer(JSONObject jsonObject)
    {
/*        JSONObject jsonObject1 = null;

        try
        {
            jsonObject1 = new JSONObject(loginResponse);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        if(jsonObject1.has("token"))
        {*/
            try
            {
                String tokenStr = jsonObject.getString("token");

                JSONObject jsonToken = new JSONObject();
                try {
                    jsonToken.put("token", tokenStr);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, String.valueOf(jsonToken));

                Request request = new Request.Builder()
                        .url("http://www.bludelego.com/dev/geatech/api.php?case=get_raport")
                        .post(body)
                        .build();

                OkHttpClient client1 = new OkHttpClient();

                callJSON = client1.newCall(request);
                callJSON.enqueue(this);


/*                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {
                        Log.d( "DEBUG", "get visits data failed");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        if (!response.isSuccessful())
                        {
                            throw new IOException("Unexpected code " + response);
                        }

                        visitsJSONData = response.body().string();

                        Log.d("DEBUG", visitsJSONData);


*//*                                myObservable = Observable.create(
                                        new Observable.OnSubscribe<String>()
                                        {
                                            @Override
                                            public void call(Subscriber<? super String> sub)
                                            {
                                                sub.onNext(visits_downloaded_data);
                                                sub.onCompleted();
                                            }
                                        }
                                );*//*

                        response.body().close();
                    }
                });*/

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        //}
    }
}

/*class ResponseInterceptor implements Interceptor
{
    private Object url;

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException
    {
        Request request0 = chain.request();

*//*        long t1 = System.nanoTime();
        Log.d("DEBUG", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));*//*

        Response response0 = chain.proceed(request0);

        loginResponse = response0.body().string();

        Log.d("DEBUG", loginResponse);

*//*        long t2 = System.nanoTime();
        Log.d("DEBUG", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));*//*

                JSONObject jsonObject1 = null;

                try
                {
                    jsonObject1 = new JSONObject(loginResponse);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if(jsonObject1.has("token"))
                {
                    try
                    {
                        String tokenStr = jsonObject1.getString("token");

                        JSONObject jsonToken = new JSONObject();
                        try {
                            jsonToken.put("token", tokenStr);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonToken));

                        Request request = new Request.Builder()
                                .url("http://www.bludelego.com/dev/geatech/api.php?case=get_raport")
                                .post(body)
                                .build();

                        OkHttpClient client1 = new OkHttpClient();

                        client1.newCall(request).enqueue(new Callback()
                        {
                            @Override
                            public void onFailure(Call call, IOException e)
                            {
                                Log.d( "DEBUG", "get visits data failed");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException
                            {
                                if (!response.isSuccessful())
                                {
                                    throw new IOException("Unexpected code " + response);
                                }

                                visitDataResponse = response.body().string();

                                Log.d("DEBUG", visits_downloaded_data);

*//*                                myJSONObservable = Observable.defer(new Func0<Observable<String>>() {
                                    @Override public Observable<String> call() {

                                        return Observable.just(visits_downloaded_data);
                                    }
                                });*//*


*//*                                myObservable = Observable.create(
                                        new Observable.OnSubscribe<String>()
                                        {
                                            @Override
                                            public void call(Subscriber<? super String> sub)
                                            {
                                                sub.onNext(visits_downloaded_data);
                                                sub.onCompleted();
                                            }
                                        }
                                );*//*

                                response.body().close();
                            }
                        });

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

        return response0;
    }
}*/
