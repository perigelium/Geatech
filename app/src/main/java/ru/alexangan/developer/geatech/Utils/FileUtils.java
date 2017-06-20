package ru.alexangan.developer.geatech.Utils;

import java.io.File;

//Created by user on 11.04.2017.

public class FileUtils
{
    public static void deleteRecursive(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
            {
                deleteRecursive(child);
            }

        fileOrDirectory.delete();
    }
}
