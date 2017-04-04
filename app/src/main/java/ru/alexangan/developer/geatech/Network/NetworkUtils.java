package ru.alexangan.developer.geatech.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import ru.alexangan.developer.geatech.Models.GeaImagineRapporto;
import ru.alexangan.developer.geatech.Utils.ImageUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.LOGIN_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.REST_URL;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.SEND_IMAGE_URL;

/**
 * Created by user on 12/20/2016*/

public class NetworkUtils
{
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();// && isOnline();
    }

/*    public static boolean isOnline()
    {
        try
        {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.4.4");
            ProcessWithTimeout processWithTimeout = new ProcessWithTimeout(process);
            int exitCode = processWithTimeout.waitForProcess(1500);

            if (exitCode == Integer.MIN_VALUE)
            {
                return false;
            } else
            {
                return true;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }*/

    public Call loginRequest(Callback callback, String login, String password, String techName, int techId)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        defaultHttpClient.connectTimeout(4, TimeUnit.SECONDS);
        defaultHttpClient.readTimeout(10, TimeUnit.SECONDS);
        defaultHttpClient.writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = defaultHttpClient.build();

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("login", login);
            jsonObject.put("password", password);

            if(techName!=null)
            {
                jsonObject.put("full_name_tehnic", techName);
            }
            if(techId!=-1)
            {
                jsonObject.put("id", techId);
            }

        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));

        Request request = new Request.Builder()
                .url(REST_URL + LOGIN_URL_SUFFIX)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);

        return call;
    }

    public Call getData(Callback callback, String urlSuffix, String tokenStr)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        defaultHttpClient.connectTimeout(4, TimeUnit.SECONDS);
        defaultHttpClient.readTimeout(10, TimeUnit.SECONDS);
        defaultHttpClient.writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = defaultHttpClient.build();

        JSONObject jsonToken = new JSONObject();

        try
        {
            jsonToken.put("token", tokenStr);

        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonToken));

        Request request = new Request.Builder()
                .url(REST_URL + urlSuffix)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);

        return call;
    }

    public Call sendData(Callback callback, String urlSuffix, String tokenStr, String gsonStr)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        defaultHttpClient.connectTimeout(4, TimeUnit.SECONDS);
        defaultHttpClient.readTimeout(10, TimeUnit.SECONDS);
        defaultHttpClient.writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = defaultHttpClient.build();

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("token", tokenStr);

            if(gsonStr!=null)
            {
                jsonObject.put("report_data", gsonStr);
            }

        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));

        Request request = new Request.Builder()
                .url(REST_URL + urlSuffix)
                .post(body)
                .build();

        Call callData = okHttpClient.newCall(request);
        callData.enqueue(callback);

        return callData;
    }

    public Call setData(Callback callback, String urlSuffix, String jsonStr)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        defaultHttpClient.connectTimeout(4, TimeUnit.SECONDS);
        defaultHttpClient.readTimeout(10, TimeUnit.SECONDS);
        defaultHttpClient.writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = defaultHttpClient.build();

        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonStr);

        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));

        Request request = new Request.Builder()
                .url(REST_URL + urlSuffix)
                .post(body)
                .build();

        Call callData = okHttpClient.newCall(request);
        callData.enqueue(callback);

        return callData;
    }

    public Call sendImage(Callback callback, Context context, GeaImagineRapporto geaImagineRapporto)
    {
        String fileName = geaImagineRapporto.getNome_file();

        File imageFile = new File(geaImagineRapporto.getFilePath());
        String strMediaType = ImageUtils.getMimeTypeOfUri(context, Uri.fromFile(imageFile));

        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        defaultHttpClient.connectTimeout(4, TimeUnit.SECONDS);
        defaultHttpClient.readTimeout(10, TimeUnit.SECONDS);
        defaultHttpClient.writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = defaultHttpClient.build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse(strMediaType), imageFile))
                .addFormDataPart("id_immagine_rapporto", String.valueOf(geaImagineRapporto.getId_immagine_rapporto()))
                .addFormDataPart("id_rapporto_sopralluogo", String.valueOf(geaImagineRapporto.getId_rapporto_sopralluogo()))
                .addFormDataPart("file_nome", fileName)
                .build();

        Request request = new Request.Builder()
                .url(SEND_IMAGE_URL)
                .post(requestBody)
                .build();

        Call callData = okHttpClient.newCall(request);
        callData.enqueue(callback);

        return callData;
    }

    public Call downloadURL(Callback callback, String url)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        defaultHttpClient.connectTimeout(4, TimeUnit.SECONDS);
        defaultHttpClient.readTimeout(10, TimeUnit.SECONDS);
        defaultHttpClient.writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = defaultHttpClient.build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call callDownloadURL = okHttpClient.newCall(request);
        callDownloadURL.enqueue(callback);

        return callDownloadURL;
    }
}
