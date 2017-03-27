package ru.alexangan.developer.geatech.Fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.GridViewAdapter;
import ru.alexangan.developer.geatech.Models.GeaImagineRapporto;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.FileUtils;
import ru.alexangan.developer.geatech.Utils.ImageUtils;
import ru.alexangan.developer.geatech.Utils.MediaUtils;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class PhotoGalleryGridFragment extends Fragment
{
    private GridViewAdapter gridAdapter;

    private final int PICK_GALLERY_IMAGE = 1;
    private final int PICK_CAMERA_IMAGE = 2;
    int currentPicPos = 0;
    File photosDir;
    int imgHolderWidth = 100;
    int imgHolderHeight = 75;
    ArrayList<Bitmap> alImgThumbs;
    ArrayList<File> alPathItems;
    Bitmap bmpCameraAddButton, bmpGalleryAddButton, bmpFullSize;
    private int selectedIndex;
    ImageView ivFullSize;
    GridView gvPhotoGallery;
    Activity activity;
    private int PERMISSION_REQUEST_CODE = 12;
    private ProgressDialog progressLoadingImages;
    ReportStates reportStates;
    Bitmap bm;

    private Handler handler;
    private Runnable runnable;

/*    interface MyCallbackInterface
    {

        void onDownloadFinished(String result);
    }*/

    public PhotoGalleryGridFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        } else
        {
            return;
        }

        progressLoadingImages = new ProgressDialog(getActivity());
        progressLoadingImages.setTitle("");
        progressLoadingImages.setIndeterminate(true);

        realm.beginTransaction();

        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();
        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

        if (reportStates == null)
        {
            realm.commitTransaction();
            return;
        }

        String photosFolderName = "photos" + idSopralluogo;

        photosDir = new File(activity.getFilesDir(), photosFolderName);

        if (!photosDir.exists())
        {
            photosDir.mkdir();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.photo_gallery_grid, container, false);

        gvPhotoGallery = (GridView) rootView.findViewById(R.id.gvPhotoGallery);
        ivFullSize = (ImageView) rootView.findViewById(R.id.imageViewFullSize);

        ivFullSize.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ivFullSize.setVisibility(View.GONE);
                gvPhotoGallery.setVisibility(View.VISIBLE);
            }
        });

        Resources resources = getResources();
        bmpCameraAddButton = BitmapFactory.decodeResource(resources, R.drawable.photo_add);
        bmpGalleryAddButton = BitmapFactory.decodeResource(resources, R.drawable.galerea_photo_add);

        alImgThumbs = new ArrayList<>();
        alPathItems = new ArrayList<>();

        progressLoadingImages.setMessage(getString(R.string.LoadingImagesInProgress));
        progressLoadingImages.show();

/*        handler.postDelayed(runnable, 10000);*/

        AsyncTask asyncTask = new AsyncTask()
        {
            @Override
            protected Object doInBackground(Object[] objects)
            {
                getImagesArray(); // Long time operation
                return null;
            }

            @Override
            protected void onPostExecute(Object o)
            {
                super.onPostExecute(o);

                progressLoadingImages.dismiss();

                alImgThumbs.add(bmpGalleryAddButton);
                alImgThumbs.add(bmpCameraAddButton);

                alPathItems.add(new File("bmpGalleryAddButton"));
                alPathItems.add(new File("bmpCameraAddButton"));

                gridAdapter = new GridViewAdapter(activity, R.layout.grid_item_layout, alImgThumbs);

                gvPhotoGallery.setAdapter(gridAdapter);

                //handler.removeCallbacks(runnable);
            }
        };

        asyncTask.execute();


        gvPhotoGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {

                currentPicPos = position;

                if (currentPicPos == alImgThumbs.size() - 1 || currentPicPos == alImgThumbs.size() - 2)
                {
/*                    if (checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {

                            String[] permissions = new String[]
                                    {
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                    };

                            requestMultiplePermissions(permissions);
                        }
                    } else
                    {
                        Intent pickIntent = new Intent();
                        pickIntent.setType("image*//*");
                        pickIntent.setAction(Intent.ACTION_PICK);

                        startActivityForResult(pickIntent, PICK_GALLERY_IMAGE);
                    }*/
                } else // remove item
                {
                    currentPicPos = position;

                    if (alPathItems.get(currentPicPos).delete() == true)
                    {
                        alImgThumbs.remove(currentPicPos);
                        alPathItems.remove(currentPicPos);
                    }

                    alImgThumbs.removeAll(Collections.singleton(null)); // remove all null items
                    alPathItems.removeAll(Collections.singleton(null)); // remove all null items

                    gvPhotoGallery.setAdapter(gridAdapter);
                }
                return true;
            }
        });

        gvPhotoGallery.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                currentPicPos = position;

                if (currentPicPos == alImgThumbs.size() - 1)
                {
                    if (checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {

                            String[] permissions = new String[]
                                    {
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                    };

                            requestMultiplePermissions(permissions);
                        }
                    } else
                    {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, PICK_CAMERA_IMAGE);
                    }
                } else if (currentPicPos == alImgThumbs.size() - 2)
                {

                    if (checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {

                            String[] permissions = new String[]
                                    {
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                    };

                            requestMultiplePermissions(permissions);
                        }
                    } else
                    {
                        Intent pickIntent = new Intent();
                        pickIntent.setType("image/*");
                        pickIntent.setAction(Intent.ACTION_PICK);

                        startActivityForResult(pickIntent, PICK_GALLERY_IMAGE);
                    }

                } else
                {
                    bmpFullSize = null;

                    AsyncTask asyncTask = new AsyncTask()
                    {
                        @Override
                        protected Object doInBackground(Object[] objects)
                        {
                            bmpFullSize = ImageUtils.createProportionalBitmap(alPathItems.get(currentPicPos).getAbsolutePath());
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o)
                        {
                            super.onPostExecute(o);

                            progressLoadingImages.dismiss();

                            if (bmpFullSize != null)
                            {
                                ivFullSize.setImageBitmap(bmpFullSize);
                            }
                            ivFullSize.setVisibility(View.VISIBLE);
                            gvPhotoGallery.setVisibility(View.GONE);
                        }
                    };

                    progressLoadingImages.setMessage(getString(R.string.PreparingImageForDisplaying));
                    progressLoadingImages.show();

                    asyncTask.execute();
                }
            }
        });

        return rootView;
    }

    private void getImagesArray()
    {
        File[] filePaths = photosDir.listFiles();

        if (filePaths == null)
        {
            return;
        }

        for (File path : filePaths)
        {
            Bitmap bm = ImageUtils.decodeSampledBitmapFromUri(path.getAbsolutePath(), imgHolderWidth, imgHolderHeight);

            if (bm != null)
            {
                alImgThumbs.add(bm);
                alPathItems.add(path);
            }
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        int id_rapporto_sopralluogo = reportStates.getId_rapporto_sopralluogo();

        RealmResults<GeaImagineRapporto> reportImages = realm.where(GeaImagineRapporto.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId()).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();
        reportImages.deleteAllFromRealm();

        reportStates.setPhotoAddedNumber(alPathItems.size() - 2);

        realm.commitTransaction();

        int reportImagesSize = reportImages.size();

        for (File imageFile : alPathItems)
        {
            if (!imageFile.getPath().equals("bmpCameraAddButton") && !imageFile.getPath().equals("bmpGalleryAddButton"))
            {

                String fileName = imageFile.getName();

                realm.beginTransaction();

                GeaImagineRapporto gea_immagine = new GeaImagineRapporto(
                        company_id, selectedTech.getId(), id_rapporto_sopralluogo, reportImagesSize++, imageFile.getAbsolutePath(), fileName);
                realm.copyToRealm(gea_immagine);

                realm.commitTransaction();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode)
        {
            case PICK_CAMERA_IMAGE:
                if (resultCode == RESULT_OK)
                {
                    String filePath = MediaUtils.getLastShotImagePath(activity);

                    Uri uri = Uri.parse(new File(filePath).toString());

                    saveReturnedImage(uri);

                    //Toast.makeText(getActivity(), "" + imagePath + "",Toast.LENGTH_LONG).show();
                }
                break;

            case PICK_GALLERY_IMAGE:
                if (resultCode == RESULT_OK)
                {
                    Uri selectedImage = imageReturnedIntent.getData();

                    String filePath = MediaUtils.getRealPathFromURI(getActivity(), selectedImage);

                    saveReturnedImage(Uri.parse(filePath));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    private void saveReturnedImage(Uri selectedImage)
    {
        final File fileSrcImage = new File(selectedImage.toString());

        String fileName = selectedImage.getLastPathSegment();

        String fileExtension = "";
        int extensionPtr = fileName.lastIndexOf(".");

        if (extensionPtr != -1)
        {
            fileExtension = fileName.substring(extensionPtr);
        }

        if (fileExtension.length() < 3)
        {
            String strMediaType = ImageUtils.getMimeTypeOfUri(activity, selectedImage);
            fileExtension = strMediaType.substring(strMediaType.lastIndexOf("/") + 1);
            fileExtension = "." + fileExtension;
            fileName += fileExtension;
        }

        final File fileFullSizeImage = new File(photosDir, fileName);
        bm = null;

        AsyncTask asyncTask = new AsyncTask()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                progressLoadingImages.setMessage(getString(R.string.LoadingImagesInProgress));
                progressLoadingImages.show();
            }

            @Override
            protected Object doInBackground(Object[] objects)
            {

/*        // Resize image to 2048x2048 dimensions maximum
        bm = ImageUtils.createProportionalBitmap(fileSrcImage.getAbsolutePath());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 0 *//**//*ignored for PNG*//*
                ,bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = null;
                try
                {
                    fos = new FileOutputStream(fileFullSizeImage);
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

                try
                {
                    if (fos != null)
                    {
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    }

                } catch (IOException e)
                {
                    e.printStackTrace();
                }*/

                // or simply copy the original file
                if (!FileUtils.copyFile(fileSrcImage, fileFullSizeImage))
                {
                    showToastMessage(getString(R.string.UnableToAddImage));
                    return null;
                }

                if (fileFullSizeImage.length() != 0)
                {
                    bm = ImageUtils.decodeSampledBitmapFromUri(fileFullSizeImage.getAbsolutePath(), imgHolderWidth, imgHolderHeight);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o)
            {
                super.onPostExecute(o);

                if (bm != null)
                {
                    alImgThumbs.add(0, bm);
                    alPathItems.add(0, fileFullSizeImage);
                    // redraw the gallery thumbnails to reflect the new addition
                    gvPhotoGallery.setAdapter(gridAdapter);
                } else
                {
                    showToastMessage(getString(R.string.UnableToAddImage));
                }

                progressLoadingImages.dismiss();
            }
        };

        asyncTask.execute();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestMultiplePermissions(String[] permissions)
    {
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }

    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

/*    private class DownloadWebpageTask extends AsyncTask<String, Void, String>
    {

        final MyCallbackInterface callback;

        DownloadWebpageTask(MyCallbackInterface callback)
        {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... strings)
        {
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            callback.onDownloadFinished(result);
        }

        //except for this leave your code for this class untouched...
    }

    public void downloadUrl(String stringUrl, final MyCallbackInterface callback)
    {
        new DownloadWebpageTask(callback)
        {
            @Override
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);
                callback.onDownloadFinished(result);
            }
        }.execute(stringUrl);
    }*/
}
