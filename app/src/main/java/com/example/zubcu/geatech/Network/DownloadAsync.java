package com.example.zubcu.geatech.Network;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 11/30/2016.
 */

public class DownloadAsync extends AsyncTask<String, Void, String>
{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mInfoTextView.setText("Загружаем...");
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return downloadOneUrl(urls[0]);

            } catch (IOException e)
            {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            //int mAltitude = getElevationFromGoogleMaps(result);
            //etAltitude.setText(String.valueOf(mAltitude), TextView.BufferType.EDITABLE);
        }


    private String downloadOneUrl(String address) throws IOException
    {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            { // 200 OK
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                int read = 0;
                while ((read = inputStream.read()) != -1)
                {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();

                data = new String(result);

            } else
            {
                data = connection.getResponseMessage() + " . Error Code : " + responseCode;
            }
            connection.disconnect();
            //return data;
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }  finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        return data;
    }
}
