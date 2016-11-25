package com.example.zubcu.geatech.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.zubcu.geatech.Adapters.GridViewAdapter;
import com.example.zubcu.geatech.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;

public class PhotoGalleryGridFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    // variable for selection intent
    private final int PICKER = 1;
    // variable to store the currently selected image
    public static int currentPicPos = 0;
    final static String photosFolderName = "imagesFolder";
    final static int imageHolderWidth = 100;
    final static int imageHolderHeight = 75;
    ArrayList<Bitmap> imageItems;
    Bitmap photoAddButton;
    Context context;


    public PhotoGalleryGridFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PhotoGalleryGridFragment newInstance(String param1, String param2)
    {
        PhotoGalleryGridFragment fragment = new PhotoGalleryGridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getActivity();

        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ArrayList<Bitmap> getImagesArray()
    {
        File appDirectory = new File(context.getFilesDir(), photosFolderName);

        if (!appDirectory.exists())
        {
            appDirectory.mkdir();
        }

        File[] filePaths = appDirectory.listFiles();

        for (File path : filePaths)
        {
            Bitmap bitmap = decodeSampledBitmapFromUri(path.getAbsolutePath(), imageHolderWidth, imageHolderHeight);
            imageItems.add(bitmap);
        }

        return imageItems;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.photo_gallery_grid, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gvPhotoGallery);

        Resources resources = getResources();
        photoAddButton = BitmapFactory.decodeResource(resources, R.drawable.photo_add);
        imageItems = new ArrayList<>();

        imageItems = getImagesArray();

        if(imageItems.size() == 0 || imageItems.get(imageItems.size() - 1) != photoAddButton)
        {
            imageItems.add(photoAddButton);
        }

        gridAdapter = new GridViewAdapter(context, R.layout.grid_item_layout, imageItems);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                currentPicPos = position;

                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_PICK);

                //startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICKER);
                startActivityForResult(pickIntent, PICKER);
            }
        });

        // set long click listener for each gallery thumbnail item
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {

                currentPicPos = position;
                imageItems.remove(currentPicPos);
                imageItems.removeAll(Collections.singleton(null));

                if(imageItems.get(imageItems.size() - 1) != photoAddButton)
                {
                    imageItems.add(photoAddButton);
                }

                gridView.setAdapter(gridAdapter);

                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK)
        {
            // check if we are returning from picture selection
            if (requestCode == PICKER)
            {
                Uri selectedImage = imageReturnedIntent.getData();

                InputStream imageStream = null;
                try
                {
                    imageStream = context.getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

                //Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                final String fileName = selectedImage.getLastPathSegment();
                File file = new File(context.getFilesDir(), photosFolderName + "/" + fileName);

                FileOutputStream out = null;
                int read = 0;
                byte[] bytes = new byte[1024];

                try
                {
                    out = new FileOutputStream(file);

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

                if (file.length() != 0)
                {

                    Bitmap bm = decodeSampledBitmapFromUri(file.getAbsolutePath(), imageHolderWidth, imageHolderHeight);

                    gridAdapter.addPic(currentPicPos, bm);

                    if(imageItems.get(imageItems.size() - 1) != photoAddButton)
                    {
                        imageItems.add(photoAddButton);
                    }

                    // redraw the gallery thumbnails to reflect the new addition
                    gridView.setAdapter(gridAdapter);
                }
            }
        }
        // superclass method
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                             int reqHeight) {
        Bitmap bm = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }
}
