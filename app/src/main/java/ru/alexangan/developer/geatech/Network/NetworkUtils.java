package ru.alexangan.developer.geatech.Network;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import ru.alexangan.developer.geatech.Models.ImageReport;
import ru.alexangan.developer.geatech.Utils.ImageUtils;

import static ru.alexangan.developer.geatech.Network.RESTdataReceiver.tokenStr;

/**
 * Created by user on 12/20/2016.
 */

public class NetworkUtils
{
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String REST_URL = "http://www.bludelego.com/dev/geatech/api.php";
    private final String DATA_URL_SUFFIX = "?case=send_data";
    private final String SEND_IMAGE_URL = "http://www.bludelego.com/dev/geatech/send_image.php";
    private final String TOKEN_URL_SUFFIX = "?case=login";
    private final String REST_LOGIN = "alexgheorghianu@yahoo.com";
    private final String REST_PASSWORD = "BF8hWoAr";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public Call getLoginToken(Callback callback)
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
            return null;
        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));

        Request request = new Request.Builder()
                .url(REST_URL + TOKEN_URL_SUFFIX)
                .post(body)
                .build();

        Call callToken = okHttpClient.newCall(request);
        callToken.enqueue(callback);

        return callToken;
    }

    public Call getJSONfromServer(Callback callback)
    {
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
                .url(REST_URL + DATA_URL_SUFFIX)
                .post(body)
                .build();

        OkHttpClient client1 = new OkHttpClient();

        Call callJSON = client1.newCall(request);
        callJSON.enqueue(callback);

        return callJSON;
    }

    public Call sendReport(Callback callback, String gsonStr)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = defaultHttpClient.build();

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("token", tokenStr);
            jsonObject.put("report_data", gsonStr);

        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));

        Request request = new Request.Builder()
                .url(REST_URL + DATA_URL_SUFFIX)
                .post(body)
                .build();

        Call callData = okHttpClient.newCall(request);
        callData.enqueue(callback);

        return callData;
    }


    public Call sendImage(Callback callback, Context context, ImageReport imageReport)
    {
        String fileName = imageReport.getFileName();
        String fileExtension = "";
        int extensionPtr = fileName.lastIndexOf(".");

        if(extensionPtr!=-1)
        {
            fileExtension = fileName.substring(extensionPtr);
        }

        String strMediaType = ImageUtils.getMimeTypeOfUri(context, Uri.fromFile(imageReport.getFile()));

        if(fileExtension.length() < 3)
        {
            fileExtension = strMediaType.substring(strMediaType.lastIndexOf("/") + 1);
            fileExtension = "." + fileExtension;
            fileName+=fileExtension;
        }

        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = defaultHttpClient.build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse(strMediaType), imageReport.getFile()))
                .addFormDataPart("id_immagine_rapporto", String.valueOf(imageReport.getId_immagine_rapporto()))
                .addFormDataPart("id_rapporto_sopralluogo", String.valueOf(imageReport.getId_rapporto_sopralluogo()))
                .build();

        Request request = new Request.Builder()
                .url(SEND_IMAGE_URL)
                .post(requestBody)
                .build();

        Call callData = okHttpClient.newCall(request);
        callData.enqueue(callback);

        return callData;
    }
}
