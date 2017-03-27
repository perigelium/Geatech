package ru.alexangan.developer.geatech.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 27.03.2017.
 */

public class FileUtils
{
    public static boolean copyFile(File fileSrcImage, File fileFullSizeImage)
    {
        InputStream imageStream;
        try
        {
            imageStream = new FileInputStream(fileSrcImage);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }

        FileOutputStream out = null;
        int read;
        byte[] bytes = new byte[1024];

        try
        {
            out = new FileOutputStream(fileFullSizeImage);

            while ((read = imageStream.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
}
