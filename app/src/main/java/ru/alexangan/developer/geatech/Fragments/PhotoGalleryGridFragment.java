package ru.alexangan.developer.geatech.Fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.GridViewAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GeaImmagineRapporto;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.ImageUtils;
import ru.alexangan.developer.geatech.Utils.MediaUtils;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.DIRECTORY_PICTURES;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

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
    Bitmap bmpFullSize;
    ImageView ivFullSize;
    GridView gvPhotoGallery;
    Activity activity;
    private int PERMISSION_REQUEST_CODE = 12;
    private ProgressDialog progressLoadingImages;
    ReportItem reportItem;
    Bitmap bm;
    String fullSizeImgPath;
    private FloatingActionButton fabAddPhoto;
    AlertDialog alert;
    private ImageView ivTrashCan;
    private Communicator mCommunicator;
    private Realm realm;

/*    private Handler handler;
    private Runnable runnable;*/

/*    interface MyCallbackInterface
    {

        void onDownloadFinished(String result);
    }*/

/*    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ivFullSize.setImageBitmap(bmpCameraAddButton);
        ivFullSize.setVisibility(View.VISIBLE);
        gvPhotoGallery.setVisibility(View.GONE);
    }*/

    public PhotoGalleryGridFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        realm = Realm.getDefaultInstance();
        int id_sopralluogo;
        mCommunicator = (Communicator) getActivity();

        if (getArguments() != null)
        {
            id_sopralluogo = getArguments().getInt("id_sopralluogo");

            if (id_sopralluogo == 0)
            {
                showToastMessage("id_sopralluogo equal 0 !");
            }

            realm.beginTransaction();
            reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                    .equalTo("id_sopralluogo", id_sopralluogo).findFirst();
            realm.commitTransaction();

            String photosFolderName = "photos" + id_sopralluogo;

            photosDir = new File(activity.getFilesDir(), photosFolderName);

            if (!photosDir.exists())
            {
                photosDir.mkdir();
            }

            progressLoadingImages = new ProgressDialog(getActivity());
            progressLoadingImages.setTitle("");
            progressLoadingImages.setIndeterminate(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.photo_gallery_grid, container, false);

        gvPhotoGallery = (GridView) rootView.findViewById(R.id.gvPhotoGallery);
        ivFullSize = (ImageView) rootView.findViewById(R.id.ivFullSize);
        ivFullSize.setVisibility(View.GONE);
        ivTrashCan = (ImageView) rootView.findViewById(R.id.ivTrashCan);
        ivTrashCan.setVisibility(View.GONE);

        ivTrashCan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (alPathItems.get(currentPicPos).delete())
                {
                    alImgThumbs.remove(currentPicPos);
                    alPathItems.remove(currentPicPos);
                } else
                {
                    alImgThumbs.clear();
                    alPathItems.clear();

                    RedrawTheGalleryTask redrawTheGalleryTask = new RedrawTheGalleryTask();
                    redrawTheGalleryTask.execute();
                }

                alImgThumbs.removeAll(Collections.singleton(null)); // remove all null items
                alPathItems.removeAll(Collections.singleton(null)); // remove all null items

                gvPhotoGallery.setAdapter(gridAdapter);

                ivFullSize.setVisibility(View.GONE);
                ivTrashCan.setVisibility(View.GONE);
                gvPhotoGallery.setVisibility(View.VISIBLE);
                fabAddPhoto.setVisibility(View.VISIBLE);

                mCommunicator.showHeaderAndFooter();
            }
        });

        fabAddPhoto = (FloatingActionButton) rootView.findViewById(R.id.fabAddPhoto);

        String[] listItemsArray = {"Scatta foto", "Scegli esistente", "Cancel"};

        View layout = inflater.inflate(R.layout.alert_dialog_custom, null);

        ListView listView = (ListView) layout.findViewById(R.id.alertList);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(activity, R.layout.alert_dialog_item_custom, listItemsArray);
        listView.setAdapter(listAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(layout);
        alert = builder.create();
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int which, long id)
            {
                if (which == 2)
                {
                    alert.dismiss();
                }

                if (which == 1)
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
                    alert.dismiss();
                }
                if (which == 0)
                {
                    if (checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            String[] permissions = new String[]
                                    {
                                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    };

                            requestMultiplePermissions(permissions);
                        }
                    } else
                    {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
                        String fileName = dateFormat.format(new Date()) + ".jpg";
                        File file = new File(activity.getExternalFilesDir(DIRECTORY_PICTURES).getAbsolutePath(), fileName);

                        fullSizeImgPath = file.getAbsolutePath();
                        Uri uriFullSizeCameraImage = Uri.fromFile(file);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFullSizeCameraImage);
                        startActivityForResult(intent, PICK_CAMERA_IMAGE);
                    }
                    alert.dismiss();
                }

            }
        });

        fabAddPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alert.show();
            }
        });


        ivFullSize.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ivFullSize.setVisibility(View.GONE);
                ivTrashCan.setVisibility(View.GONE);
                gvPhotoGallery.setVisibility(View.VISIBLE);
                fabAddPhoto.setVisibility(View.VISIBLE);

                mCommunicator.showHeaderAndFooter();
            }
        });

        //Resources resources = getResources();
        //bmpCameraAddButton = BitmapFactory.decodeResource(resources, R.drawable.photo_add);
        //bmpGalleryAddButton = BitmapFactory.decodeResource(resources, R.drawable.galerea_photo_add);

        alImgThumbs = new ArrayList<>();
        alPathItems = new ArrayList<>();

        RedrawTheGalleryTask redrawTheGalleryTask = new RedrawTheGalleryTask();
        redrawTheGalleryTask.execute();

        gvPhotoGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {

                currentPicPos = position;

                //if (currentPicPos != alImgThumbs.size() - 1 && currentPicPos != alImgThumbs.size() - 2)
                {  // remove item
                    currentPicPos = position;

                    if (alPathItems.get(currentPicPos).delete())
                    {
                        alImgThumbs.remove(currentPicPos);
                        alPathItems.remove(currentPicPos);
                    } else
                    {
                        alImgThumbs.clear();
                        alPathItems.clear();

                        RedrawTheGalleryTask redrawTheGalleryTask = new RedrawTheGalleryTask();
                        redrawTheGalleryTask.execute();
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

/*                if (currentPicPos == alImgThumbs.size() - 1)
                { // open Camera
                    if (checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            String[] permissions = new String[]
                                    {
                                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    };

                            requestMultiplePermissions(permissions);
                        }
                    } else
                    {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
                        String fileName = dateFormat.format(new Date()) + ".jpg";
                        File file = new File(activity.getExternalFilesDir(DIRECTORY_PICTURES).getAbsolutePath(), fileName);

                        fullSizeImgPath = file.getAbsolutePath();
                        Uri uriFullSizeCameraImage = Uri.fromFile(file);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFullSizeCameraImage);
                        startActivityForResult(intent, PICK_CAMERA_IMAGE);
                    }
                } else if (currentPicPos == alImgThumbs.size() - 2)
                { // open Gallery

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
                        pickIntent.setType("image*//*");
                        pickIntent.setAction(Intent.ACTION_PICK);

                        startActivityForResult(pickIntent, PICK_GALLERY_IMAGE);
                    }

                } else*/
                { // show full-size image
                    bmpFullSize = null;

                    ShowFullSizedImageTask showFullSizedImageTask = new ShowFullSizedImageTask();
                    showFullSizedImageTask.execute();
                }
            }
        });

        //File[] deletedFilePaths = activity.getExternalFilesDir(DIRECTORY_PICTURES).listFiles();

        return rootView;
    }

    private void getImagesArray()
    {
        if (photosDir == null)
        {
            return;
        }

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
    public void onDestroy()
    {
        super.onDestroy();

        if (reportItem == null)
        {
            return;
        }

        realm.beginTransaction();

        int id_rapporto_sopralluogo = reportItem.getGea_rapporto().getId_rapporto_sopralluogo();

        reportItem.getGea_immagini_rapporto().clear();

        alPathItems.clear();
        alPathItems.addAll(Arrays.asList(photosDir.listFiles()));

        reportItem.getReportStates().setPhotosAddedNumber(alPathItems.size());

        realm.commitTransaction();

        int reportImagesSize = 0;

        try
        {
            for (File imageFile : alPathItems)
            {
                String fileName = imageFile.getName();

                realm.beginTransaction();

                GeaImmagineRapporto gea_immagine = new GeaImmagineRapporto(
                        company_id, selectedTech.getId(), id_rapporto_sopralluogo, reportImagesSize++, imageFile.getAbsolutePath(), fileName);
                reportItem.getGea_immagini_rapporto().add(gea_immagine);

                realm.commitTransaction();
            }
        }
        catch (ConcurrentModificationException e)
        {
            showToastMessage("Salvare immagini non riuscito, provare anche una volta");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode)
        {
            case PICK_CAMERA_IMAGE:
            {
                File imgFile = new File(fullSizeImgPath);

                if (imgFile.exists())
                {
                    saveReturnedImage(fullSizeImgPath);
                }/* else
                {
                    String filePath = MediaUtils.getLastShotImagePath(activity);
                    //Uri uri = Uri.parse(new File(filePath).toString());
                    saveReturnedImage(filePath);
                }*/
            }
            break;

            case PICK_GALLERY_IMAGE:
                if (resultCode == RESULT_OK)
                {
                    Uri selectedImage = imageReturnedIntent.getData();

                    String filePath = MediaUtils.getRealPathFromURI(getActivity(), selectedImage);

                    saveReturnedImage(filePath);
                }
                break;
        }
        //super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    private void saveReturnedImage(String srcImagePath)
    {
        File srcImageFile = new File(srcImagePath);
        Uri uriSrcImage = Uri.parse(srcImagePath);
        String srcImageFileName = uriSrcImage.getLastPathSegment();

        String fileExtension = "";
        int extensionPtr = srcImageFileName.lastIndexOf(".");

        if (extensionPtr != -1)
        {
            fileExtension = srcImageFileName.substring(extensionPtr);
        }

        if (fileExtension.length() < 3)
        {
            String strMediaType = ImageUtils.getMimeTypeOfUri(activity, uriSrcImage);
            fileExtension = strMediaType.substring(strMediaType.lastIndexOf("/") + 1);
            fileExtension = "." + fileExtension;
            srcImageFileName += fileExtension;
        }

        final File fileResizedImage = new File(photosDir, srcImageFileName);

        if (fileResizedImage.exists())
        {
            showToastMessage(getString(R.string.UnableToAddImage));
            return;
        }

        bm = null;

        File fileWithExt = new File(photosDir, srcImageFileName);

        ShowOriginalImageAsThumbTask showOriginalImageAsThumbTask = new ShowOriginalImageAsThumbTask(srcImageFile);
        showOriginalImageAsThumbTask.execute();

        ResizeImageTask resizeImageTask = new ResizeImageTask(srcImageFile, fileWithExt);
        resizeImageTask.execute();
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

    class ShowOriginalImageAsThumbTask extends AsyncTask<File, Void, Void>
    {
        File fileSrc;

        ShowOriginalImageAsThumbTask(File fileSrcImage)
        {
            fileSrc = fileSrcImage;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressLoadingImages.setMessage(getString(R.string.LoadingImagesInProgress));
            progressLoadingImages.show();
        }

        @Override
        protected Void doInBackground(File... files)
        {

            bm = ImageUtils.decodeSampledBitmapFromUri(fileSrc.getAbsolutePath(), imgHolderWidth, imgHolderHeight);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (bm != null)
            {
                alImgThumbs.add(bm);
                alPathItems.add(fileSrc);
                // redraw the gallery thumbnails to reflect the new addition
                gvPhotoGallery.setAdapter(gridAdapter);
            } else
            {
                showToastMessage(getString(R.string.UnableToAddImage));
            }

            progressLoadingImages.dismiss();
        }
    }

    class ResizeImageTask extends AsyncTask<File, Void, Void>
    {
        File fileSrc, fileDst;
        boolean success = true;

        ResizeImageTask(File fileSrcImage, File fileResizedImage)
        {
            fileSrc = fileSrcImage;
            fileDst = fileResizedImage;
        }

        @Override
        protected Void doInBackground(File... files)
        {
            // Resize image to 2048x2048 dimensions maximum
            bm = ImageUtils.createProportionalBitmap(fileSrc.getAbsolutePath());

            if (bm == null)
            {
                return null;
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 85, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = null;
            try
            {
                fos = new FileOutputStream(fileDst);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
                success = false;
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
                success = false;
            }

            //double size = fileDst.length();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (success)
            {
                alPathItems.set(alPathItems.size() - 1, fileDst);
                //alPathItems.add(fileDst);

/*                if(!fileSrc.delete())
                {
                    Log.d("DEBUG", "File not deleted !");
                }*/
            }
        }
    }

    class ShowFullSizedImageTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressLoadingImages.setMessage(getString(R.string.PreparingImageForDisplaying));
            progressLoadingImages.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            bmpFullSize = ImageUtils.createProportionalBitmap(alPathItems.get(currentPicPos).getAbsolutePath());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            progressLoadingImages.dismiss();

            if (bmpFullSize != null)
            {
                ivFullSize.setImageBitmap(bmpFullSize);
            }
            ivFullSize.setVisibility(View.VISIBLE);
            ivTrashCan.setVisibility(View.VISIBLE);
            gvPhotoGallery.setVisibility(View.GONE);
            fabAddPhoto.setVisibility(View.GONE);

            mCommunicator.hideHeaderAndFooter();
        }
    }

    class RedrawTheGalleryTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressLoadingImages.setMessage(getString(R.string.LoadingImagesInProgress));
            progressLoadingImages.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getImagesArray(); // Long time operation
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            progressLoadingImages.dismiss();

            //alImgThumbs.add(bmpGalleryAddButton);
            //alImgThumbs.add(bmpCameraAddButton);

            //alPathItems.add(new File("bmpGalleryAddButton"));
            //alPathItems.add(new File("bmpCameraAddButton"));

            gridAdapter = new GridViewAdapter(activity, R.layout.grid_item_layout, alImgThumbs);

            gvPhotoGallery.setAdapter(gridAdapter);
        }
    }
}
