package ru.alexangan.developer.geatech.Utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 06.02.2017.
 */

public class ImageUtils
{
    public static String getMimeTypeOfUri(Context context, Uri uri)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
    /* The doc says that if inJustDecodeBounds set to true, the decoder
     * will return null (no bitmap), but the out... fields will still be
     * set, allowing the caller to query the bitmap without having to
     * allocate the memory for its pixels. */
        opt.inJustDecodeBounds = true;

        InputStream istream = null;
        try
        {
            istream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        BitmapFactory.decodeStream(istream, null, opt);

        try
        {
            istream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return opt.outMimeType;
    }
}
