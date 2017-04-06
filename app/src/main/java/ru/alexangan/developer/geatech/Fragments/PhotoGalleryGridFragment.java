package ru.alexangan.developer.geatech.Fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.GridViewAdapter;
import ru.alexangan.developer.geatech.Models.GeaImagineRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.ImageUtils;
import ru.alexangan.developer.geatech.Utils.MediaUtils;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStorageDirectory;
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
    ImageView ivFullSize;
    GridView gvPhotoGallery;
    Activity activity;
    private int PERMISSION_REQUEST_CODE = 12;
    private ProgressDialog progressLoadingImages;
    ReportStates reportStates;
    Bitmap bm;
    String fullSizeImgPath;

/*    private Handler handler;
    private Runnable runnable;*/

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
        int selectedIndex;

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

        realm.commitTransaction();

        if (reportStates == null)
        {
            return;
        }

        String photosFolderName = "photos" + reportStates.getId_sopralluogo();

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

        RedrawTheGalleryTask redrawTheGalleryTask = new RedrawTheGalleryTask();
        redrawTheGalleryTask.execute();

        gvPhotoGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {

                currentPicPos = position;

                if (currentPicPos != alImgThumbs.size() - 1 && currentPicPos != alImgThumbs.size() - 2)
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

                if (currentPicPos == alImgThumbs.size() - 1)
                { // open Camera
                    if (checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            String[] permissions = new String[]
                                    {
                                            Manifest.permission.CAMERA
                                    };

                            requestMultiplePermissions(permissions);
                        }
                    } else
                    {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
                        String fileName = dateFormat.format(new Date()) + ".jpg";
                        File file = new File(getExternalStorageDirectory().getAbsolutePath(), fileName);
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
                        pickIntent.setType("image/*");
                        pickIntent.setAction(Intent.ACTION_PICK);

                        startActivityForResult(pickIntent, PICK_GALLERY_IMAGE);
                    }

                } else
                { // show full-size image
                    bmpFullSize = null;

                    ShowFullSizedImageTask showFullSizedImageTask = new ShowFullSizedImageTask();
                    showFullSizedImageTask.execute();
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

        realm.beginTransaction();

        int id_rapporto_sopralluogo = reportStates.getId_rapporto_sopralluogo();

        RealmResults<GeaImagineRapporto> reportImages = realm.where(GeaImagineRapporto.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId()).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();
        reportImages.deleteAllFromRealm();

        alPathItems.clear();
        for (File path : photosDir.listFiles())
        {
            alPathItems.add(path);
        }

        reportStates.setPhotoAddedNumber(alPathItems.size());

        realm.commitTransaction();

        int reportImagesSize = reportImages.size();

        for (File imageFile : alPathItems)
        {
/*            if (!imageFile.getPath().equals("bmpCameraAddButton") && !imageFile.getPath().equals("bmpGalleryAddButton"))
            {*/

                String fileName = imageFile.getName();

                realm.beginTransaction();

                GeaImagineRapporto gea_immagine = new GeaImagineRapporto(
                        company_id, selectedTech.getId(), id_rapporto_sopralluogo, reportImagesSize++, imageFile.getAbsolutePath(), fileName);
                realm.copyToRealm(gea_immagine);

                realm.commitTransaction();
           // }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        InputStream stream = null;

        switch (requestCode)
        {
            case PICK_CAMERA_IMAGE:
                //if (resultCode == RESULT_OK)
            {
/*                    if(imageReturnedIntent != null)
                    {*/

                //stream = activity.getContentResolver().openInputStream(imageReturnedIntent.getData());
                //Uri uri = imageReturnedIntent.getData();

                File imgFile = new File(fullSizeImgPath);
                Bitmap bmFulSize = null;

                if (imgFile.exists())
                {
                    //bmFulSize = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                    //Bitmap bm = (Bitmap) imageReturnedIntent.getExtras().get("data");

                    saveReturnedImage(fullSizeImgPath);
                } else
                {
                    String filePath = MediaUtils.getLastShotImagePath(activity);
                    //Uri uri = Uri.parse(new File(filePath).toString());
                    saveReturnedImage(filePath);
                }
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
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    private void saveReturnedImage(Bitmap receivedBitmap)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String fileName = dateFormat.format(new Date()) + ".png";
        File file = new File(photosDir, fileName);

        if (file.exists())
        {
            showToastMessage(getString(R.string.UnableToAddImage));
            return;
        }


        Bitmap bmThumb = null;
        bmThumb = Bitmap.createScaledBitmap(receivedBitmap, imgHolderWidth, imgHolderHeight, false);



/*        String fileExtension = "";
        int extensionPtr = fileName.lastIndexOf(".");

        if (extensionPtr != -1)
        {
            fileExtension = fileName.substring(extensionPtr);
        }*/

        //if (fileExtension.length() < 3)

        OutputStream os = null;
        try
        {
            os = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        try
        {
            receivedBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

            if (os != null)
            {
                os.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //final File fileSrcImage = new File(selectedImage.toString());

/*        Uri uri = Uri.fromFile(file);

        {
            String strMediaType = ImageUtils.getMimeTypeOfUri(activity, uri);
            String fileExtension = strMediaType.substring(strMediaType.lastIndexOf("/") + 1);
            fileExtension = "." + fileExtension;
            fileName += fileExtension;
        }*/

        //final File fileResizedImage = new File(photosDir, fileName);


        alImgThumbs.add(alImgThumbs.size() - 2, bmThumb);
        alPathItems.add(alPathItems.size() - 2, file);

        gridAdapter = new GridViewAdapter(activity, R.layout.grid_item_layout, alImgThumbs);

        gvPhotoGallery.setAdapter(gridAdapter);

        //bm = null;

        //ShowOriginalImageTask showOriginalImageTask = new ShowOriginalImageTask(file);
        //showOriginalImageTask.execute();

        //ResizeImageTask resizeImageTask = new ResizeImageTask(file, fileResizedImage);
        //resizeImageTask.execute();
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

        ShowOriginalImageTask showOriginalImageTask = new ShowOriginalImageTask(srcImageFile);
        showOriginalImageTask.execute();

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

    class ShowOriginalImageTask extends AsyncTask<File, Void, Void>
    {
        File fileSrc;

        ShowOriginalImageTask(File fileSrcImage)
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
                alImgThumbs.add(alImgThumbs.size() - 2, bm);
                alPathItems.add(alPathItems.size() - 2, fileSrc);
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
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
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

/*        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (success)
            {
                //fileFullSizeImage.delete();
                //RedrawTheGalleryTask redrawTheGalleryTask = new RedrawTheGalleryTask();
                //redrawTheGalleryTask.execute();
            }
        }*/
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
            gvPhotoGallery.setVisibility(View.GONE);
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

            alImgThumbs.add(bmpGalleryAddButton);
            alImgThumbs.add(bmpCameraAddButton);

            alPathItems.add(new File("bmpGalleryAddButton"));
            alPathItems.add(new File("bmpCameraAddButton"));

            gridAdapter = new GridViewAdapter(activity, R.layout.grid_item_layout, alImgThumbs);

            gvPhotoGallery.setAdapter(gridAdapter);
        }
    }
}
