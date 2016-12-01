package com.example.zubcu.geatech.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.zubcu.geatech.Activities.LoginActivity.JSON;
import static com.example.zubcu.geatech.Activities.LoginActivity.loginResponse;
import static com.example.zubcu.geatech.R.*;


public class LoginActivity extends Activity implements View.OnClickListener
{
    Button btnLogin;
    Button btAcces;
    Button btPassword;
    OkHttpClient okHttpClient;
    public static Context context;
    static String loginResponse;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.login_activity);

        context = getApplicationContext();


        btnLogin = (Button) findViewById(id.btLogin);
        btnLogin.setOnClickListener(this);


/*        btAcces = (Button)findViewById(id.btAces);
        btAcces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstAccess = new Intent(LoginActivity.this, UserFirstAccess.class);
                startActivity(firstAccess);
            }
        });

        btPassword = (Button)findViewById(id.btPassword);
        btPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PasswordRecover = new Intent(LoginActivity.this, UserPasswordRecover.class);
                startActivity(PasswordRecover);
            }
        });*/
    }

    @Override
    public void onClick(View view)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        defaultHttpClient.interceptors().add(new ResponseInterceptor());
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

        okHttpClient.newCall(request).enqueue(new Callback()
        {

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
/*                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++)
                        {
                            Log.d("DEBUG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }*/

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
                    Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(registerIntent);
                }
            }
        });
    }
}

class ResponseInterceptor implements Interceptor
{
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException
    {
        Request request0 = chain.request();

/*        long t1 = System.nanoTime();
        Log.d("DEBUG", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));*/

        Response response0 = chain.proceed(request0);

        loginResponse = response0.body().string();

        Log.d("DEBUG", loginResponse);

/*        long t2 = System.nanoTime();
        Log.d("DEBUG", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));*/

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

                                String visits_downloaded_data = response.body().string();

                                Log.d("DEBUG", visits_downloaded_data);

                                try
                                {
                                    JSONObject jsonObject = new JSONObject(visits_downloaded_data);
                                    JSONArray arr_caseArray = jsonObject.getJSONArray("arr_case");
                                    JSONArray visits_array = arr_caseArray.getJSONArray(0);

                                    for (int i = 0; i < visits_array.length(); i++)
                                    {
                                        JSONArray visit_array = visits_array.getJSONArray(i);

                                        JSONObject visit_data = visit_array.getJSONObject(0);
                                        JSONObject client_data = visit_array.getJSONObject(1);
                                        JSONObject subproducts = visit_array.getJSONObject(2);

                                        String visit_dataStringArray = visit_data.getString("visit_data");
                                        JSONObject visit_dataJSONObject = new JSONObject(visit_dataStringArray);



                                        Integer id_sopralluogo = visit_dataJSONObject.getInt("id_sopralluogo");
                                        String data_ora_assegnazione = visit_dataJSONObject.getString("data_ora_assegnazione");
                                        String data_ora_presa_appuntamento = visit_dataJSONObject.getString("data_ora_presa_appuntamento");
                                        String data_ora_sopralluogo = visit_dataJSONObject.getString("data_ora_sopralluogo");
                                        String note_sopralluogo = visit_dataJSONObject.getString("note_sopralluogo");





                                        String tipo_gestione_sopralluogo = visit_dataJSONObject.getString("tipo_gestione_sopralluogo");

                                        JSONArray client_dataArray = client_data.getJSONArray("client_data");
                                        JSONObject client_dataJSONObject = client_dataArray.getJSONObject(0);
                                        String name = client_dataJSONObject.getString("name");

                                        JSONArray subproductsArray = subproducts.getJSONArray("sub_item");

                                        for (int j = 0; j < subproductsArray.length(); j++)
                                        {

                                            JSONObject msg = subproductsArray.getJSONObject(j);
                                            String subproduct = msg.getString("subproduct");
                                            Log.d("DEBUG", subproduct);
                                            String product_type = msg.getString("product_type");
                                        }
                                    }

                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }

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
}
