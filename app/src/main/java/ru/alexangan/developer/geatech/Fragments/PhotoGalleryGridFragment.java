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
import ru.alexangan.developer.geatech.Utils.ImageUtils;

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
    String photosFolderName;
    int imageHolderWidth = 100;
    int imageHolderHeight = 75;
    ArrayList<Bitmap> imageThumbnails;
    ArrayList<File> pathItems;
    Bitmap bmpCameraAddButton, bmpGalleryAddButton, fullSizeBitmap;
    private int selectedIndex;
    ImageView imageViewFullSize;
    GridView gvPhotoGallery;
    Activity activity;
    private int PERMISSION_REQUEST_CODE = 12;
    private ProgressDialog loadingImagesDialog;

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
            photosFolderName = "photos" + selectedIndex;
        }

        loadingImagesDialog = new ProgressDialog(getActivity());
        loadingImagesDialog.setTitle("");
        loadingImagesDialog.setIndeterminate(true);

/*        handler = new Handler();

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                showToastMessage("Timeout reached");
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        loadingImagesDialog.dismiss();
                    }
                });
            }
        };*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.photo_gallery_grid, container, false);

        gvPhotoGallery = (GridView) rootView.findViewById(R.id.gvPhotoGallery);

        imageViewFullSize = (ImageView) rootView.findViewById(R.id.imageViewFullSize);

        imageViewFullSize.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                imageViewFullSize.setVisibility(View.GONE);
                gvPhotoGallery.setVisibility(View.VISIBLE);
            }
        });

        Resources resources = getResources();
        bmpCameraAddButton = BitmapFactory.decodeResource(resources, R.drawable.photo_add);
        bmpGalleryAddButton = BitmapFactory.decodeResource(resources, R.drawable.galerea_photo_add);

        imageThumbnails = new ArrayList<>();
        pathItems = new ArrayList<>();

        loadingImagesDialog.setMessage(getString(R.string.LoadingImagesInProgress));
        loadingImagesDialog.show();

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

                loadingImagesDialog.dismiss();

                imageThumbnails.add(bmpGalleryAddButton);
                imageThumbnails.add(bmpCameraAddButton);

                pathItems.add(new File("bmpGalleryAddButton"));
                pathItems.add(new File("bmpCameraAddButton"));

                gridAdapter = new GridViewAdapter(activity, R.layout.grid_item_layout, imageThumbnails);

                gvPhotoGallery.setAdapter(gridAdapter);

                //handler.removeCallbacks(runnable);
            }
        };

        asyncTask.execute();

/*        downloadUrl("http://google.com", new MyCallbackInterface() {

            @Override
            public void onDownloadFinished(String result) {
                // Do something when download finished
            }
        });*/
        
/*        getImagesArray(); // Long time operation

        loadingImagesDialog.dismiss();

        imageThumbnails.add(bmpCameraAddButton);
        imageBitmaps.add(bmpCameraAddButton);
        pathItems.add(new File("bmpCameraAddButton"));

        gridAdapter = new GridViewAdapter(activity, R.layout.grid_item_layout, imageThumbnails);

        gvPhotoGallery.setAdapter(gridAdapter);*/


        gvPhotoGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {

                currentPicPos = position;

                if (currentPicPos == imageThumbnails.size() - 1 || currentPicPos == imageThumbnails.size() - 2)
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

                    if (pathItems.get(currentPicPos).delete() == true)
                    {
                        imageThumbnails.remove(currentPicPos);
                        pathItems.remove(currentPicPos);
                    }

                    imageThumbnails.removeAll(Collections.singleton(null)); // remove all null items
                    pathItems.removeAll(Collections.singleton(null)); // remove all null items

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

                if (currentPicPos == imageThumbnails.size() - 1)
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
                } else
                if (currentPicPos == imageThumbnails.size() - 2)
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

                }else
                {
                    fullSizeBitmap = null;

                    AsyncTask asyncTask = new AsyncTask()
                    {
                        @Override
                        protected Object doInBackground(Object[] objects)
                        {
                            fullSizeBitmap = ImageUtils.decodeSampledBitmapFromUri(pathItems.get(currentPicPos).getAbsolutePath(), 2048, 2048);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o)
                        {
                            super.onPostExecute(o);

                            loadingImagesDialog.dismiss();

                            if(fullSizeBitmap!=null)
                            {
                                imageViewFullSize.setImageBitmap(fullSizeBitmap);
                            }
                            imageViewFullSize.setVisibility(View.VISIBLE);
                            gvPhotoGallery.setVisibility(View.GONE);
                        }
                    };

                    loadingImagesDialog.setMessage(getString(R.string.PreparingImageForDisplaying));
                    loadingImagesDialog.show();

                    asyncTask.execute();
                }
            }
        });

        return rootView;
    }

    private void getImagesArray()
    {
        File appDirectory = new File(activity.getFilesDir(), photosFolderName);

        if (!appDirectory.exists())
        {
            appDirectory.mkdir();
        }

        File[] filePaths = appDirectory.listFiles();

        for (File path : filePaths)
        {
            Bitmap bm = ImageUtils.decodeSampledBitmapFromUri(path.getAbsolutePath(), imageHolderWidth, imageHolderHeight);

            imageThumbnails.add(bm);
            pathItems.add(path);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        realm.beginTransaction();

        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();
        ReportStates reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

        if (reportStates == null)
        {
            return;
        }

        int id_rapporto_sopralluogo = reportStates.getId_rapporto_sopralluogo();

        RealmResults<GeaImagineRapporto> reportImages = realm.where(GeaImagineRapporto.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId()).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();
        reportImages.deleteAllFromRealm();

        reportStates.setPhotoAddedNumber(imageThumbnails.size() - 2);

        realm.commitTransaction();

        int reportImagesSize = reportImages.size();

        for (File imageFile : pathItems)
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
                    String filePath = getLastShotImagePath();

                    Uri uri = Uri.parse(new File(filePath).toString());

                    saveReturnedImage(uri);

                    //Toast.makeText(getActivity(), "" + imagePath + "",Toast.LENGTH_LONG).show();
                }
                break;

            case PICK_GALLERY_IMAGE:
                if (resultCode == RESULT_OK)
                {
                    Uri selectedImage = imageReturnedIntent.getData();

                    String filePath = getRealPathFromURI(getActivity(), selectedImage);

                    saveReturnedImage(Uri.parse(filePath));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    private void saveReturnedImage(Uri selectedImage)
    {
        InputStream imageStream = null;
        File imageFile = new File(selectedImage.toString());

        try
        {
            imageStream = new FileInputStream(imageFile);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return;
        }

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

        File file = new File(activity.getFilesDir(), photosFolderName + "/" + fileName);

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
/*            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);

            imageBitmaps.add(0, bitmap);*/

            Bitmap bm = ImageUtils.decodeSampledBitmapFromUri(file.getAbsolutePath(), imageHolderWidth, imageHolderHeight);

            if(bm!=null)
            {
                imageThumbnails.add(0, bm);
                pathItems.add(0, file);
                // redraw the gallery thumbnails to reflect the new addition
                gvPhotoGallery.setAdapter(gridAdapter);
            }
            else
            {
                showToastMessage(getString(R.string.UnableToAddImage));
            }
        }
    }

    public String getLastShotImagePath()
    {
        String filePath = "";
        Cursor cursor = null;

        try
        {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToLast();

            filePath = cursor.getString(column_index_data);

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

    public String getRealPathFromURI(Context context, Uri contentUri)
    {
        Cursor cursor = null;
        String realPath = "";

        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            realPath = cursor.getString(column_index);

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
