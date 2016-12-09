package com.example.zubcu.geatech.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zubcu.geatech.Fragments.UserFirstAccessFragment;
import com.example.zubcu.geatech.Fragments.UserLoginFragment;
import com.example.zubcu.geatech.Fragments.UserPasswordRecoverFragment;
import com.example.zubcu.geatech.Interfaces.LoginCommunicator;
import com.example.zubcu.geatech.Interfaces.RESTdataReceiverEventListener;
import com.example.zubcu.geatech.Network.RESTdataReceiver;
import com.example.zubcu.geatech.R;

import static com.example.zubcu.geatech.R.*;


public class LoginActivity extends Activity implements RESTdataReceiverEventListener, LoginCommunicator
{
    RESTdataReceiver restDataReceiver;
    private FragmentManager mFragmentManager;
    UserFirstAccessFragment userFirstAccessFragment;
    UserPasswordRecoverFragment userPasswordRecoverFragment;
    UserLoginFragment userLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_window_container);


        userLoginFragment = new UserLoginFragment();
        userFirstAccessFragment = new UserFirstAccessFragment();
        userPasswordRecoverFragment = new UserPasswordRecoverFragment();

        mFragmentManager = getFragmentManager();

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.loginFragContainer, userLoginFragment);

        mFragmentTransaction.commit();

        restDataReceiver = new RESTdataReceiver( this, this );
    }

    @Override
    public void onTokenReceiveCompleted()
    {
        restDataReceiver.getJSONfromServer();
    }

    @Override
    public void onJSONdataReceiveCompleted()
    {
        Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public void onEventFailed()
    {
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onButtonClicked(View view)
    {
        if(view.getId() == R.id.btnPasswordRecover )
        {
            Toast.makeText(this, "btnPasswordRecover clicked", Toast.LENGTH_LONG).show();
        }

        if(view.getId() == R.id.btnFirstAccess )
        {
            Toast.makeText(this, "btnFirstAccess clicked", Toast.LENGTH_LONG).show();
        }

        if(view.getId() == R.id.btnLogin )
        {
            restDataReceiver.getLoginToken();
        }
    }
}








/*
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

*/
/*                RequestBody formBody = new FormBody.Builder()
                        .add("login", "alexgheorghianu@yahoo.com")
                        .add("password", "BF8hWaAr")
                        .add("sender", "Alexandr")
                        .build();*//*


        Request request = new Request.Builder()
                .url("http://www.bludelego.com/dev/geatech/api.php?case=login")
                .post(body)
                .build();

        callToken = okHttpClient.newCall(request);
        callToken.enqueue(this);

        //okHttpClient.newCall(request).enqueue(this);

*/
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
*//*
*/
/*                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++)
                        {
                            Log.d("DEBUG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }*//*
*/
/*

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

*//*
*/
/*                    myTokenObservable = Observable.defer(new Func0<Observable<Boolean>>() {
                        @Override public Observable<Boolean> call() {

                            return Observable.just(true);
                        }
                    });*//*
*/
/**//*
*/
/*


 *//*
*/
/**//*
*/
/*                   myTokenObservable = Observable.create(
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
*/
/*

                    tokenHasGot = true;
                }
            }
        });*//*


        return tokenHasGot;
    }

    void getJSONfromServer(JSONObject jsonObject)
    {
*/
/*        JSONObject jsonObject1 = null;

        try
        {
            jsonObject1 = new JSONObject(loginResponse);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        if(jsonObject1.has("token"))
        {*//*

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


*/
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


*//*
*/
/*                                myObservable = Observable.create(
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
*/
/*

                        response.body().close();
                    }
                });*//*


            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        //}
    }
}

*/
/*class ResponseInterceptor implements Interceptor
{
    private Object url;

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException
    {
        Request request0 = chain.request();

*//*
*/
/*        long t1 = System.nanoTime();
        Log.d("DEBUG", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));*//*
*/
/*

        Response response0 = chain.proceed(request0);

        loginResponse = response0.body().string();

        Log.d("DEBUG", loginResponse);

*//*
*/
/*        long t2 = System.nanoTime();
        Log.d("DEBUG", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));*//*
*/
/*

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

*//*
*/
/*                                myJSONObservable = Observable.defer(new Func0<Observable<String>>() {
                                    @Override public Observable<String> call() {

                                        return Observable.just(visits_downloaded_data);
                                    }
                                });*//*
*/
/*


*//*
*/
/*                                myObservable = Observable.create(
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
*/
/*

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

