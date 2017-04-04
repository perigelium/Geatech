package ru.alexangan.developer.geatech.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by user on 27.03.2017.*/

public class MediaUtils
{
    public static String getLastShotImagePath(Activity activity)
    {
        String filePath = "";
        Cursor cursor = null;

        try
        {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
            int column_index_data;
            if (cursor != null)
            {
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                filePath = cursor.getString(column_index_data);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return filePath;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri)
    {
        Cursor cursor = null;
        String realPath = "";

        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index;
            if (cursor != null)
            {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                realPath = cursor.getString(column_index);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return realPath;
    }
}
