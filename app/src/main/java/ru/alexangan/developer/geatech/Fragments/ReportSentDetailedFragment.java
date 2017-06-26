package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import ru.alexangan.developer.geatech.Adapters.GridViewAdapter;
import ru.alexangan.developer.geatech.Models.GeaImmagineRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.ImgCallAttrs;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.ImageUtils;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

// Created by Alex Angan on 11/10/2016 .

public class ReportSentDetailedFragment extends Fragment implements Callback
{
    private int id_rapporto_sopralluogo;
    GridView gvPhotoGallery;
    private ProgressDialog loadingImagesDialog;
    Activity activity;
    int imageHolderWidth = 100;
    int imageHolderHeight = 75;
    private String photosFolderName;
    ArrayList<Bitmap> imageThumbnails;
    ArrayList<File> pathItems;
    private TextView tvTechName, tvdataOraSopralluogo, tvdataOraRaportoInviato;
    private TextView tvdataOraRaportoCompletato;
    private TextView tvReportName, tvClientName;
    private TextView tvClientPhones, tvClientAddress;
    private TextView tvCoordNord, tvCoordEst, tvAltitude;
    private ListView listView;
    List<Call> callDownloadImagesList;
    List<ImgCallAttrs> imgCallAttrs;
    ReportItem reportItemUnmanaged;
    private int id_sopralluogo;
    private int callsSent;
    RealmList<GeaImmagineRapporto> rl_ImaginesRapportoSopralluogo;
    List<String> imageNames;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            id_rapporto_sopralluogo = getArguments().getInt("id_rapporto_sopralluogo");
        }

        activity = getActivity();
        callsSent = -1;

        loadingImagesDialog = new ProgressDialog(getActivity());
        loadingImagesDialog.setTitle("");
        loadingImagesDialog.setIndeterminate(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.report_sent_detailed_fragment, container, false);

        gvPhotoGallery = (GridView) rootView.findViewById(R.id.gvPhotoGallery);

        tvdataOraSopralluogo = (TextView) rootView.findViewById(R.id.tvdataOraSopralluogo);
        tvdataOraRaportoCompletato = (TextView) rootView.findViewById(R.id.tvdataOraRaportoCompletato);
        tvdataOraRaportoInviato = (TextView) rootView.findViewById(R.id.tvdataOraRaportoInviato);
        tvReportName = (TextView) rootView.findViewById(R.id.tvReportName);

        tvClientName = (TextView) rootView.findViewById(R.id.tvClientName);
        tvClientPhones = (TextView) rootView.findViewById(R.id.tvClientPhones);
        tvClientAddress = (TextView) rootView.findViewById(R.id.tvClientAddress);

        tvCoordNord = (TextView) rootView.findViewById(R.id.etCoordNord);
        tvCoordEst = (TextView) rootView.findViewById(R.id.etCoordEst);
        tvAltitude = (TextView) rootView.findViewById(R.id.etAltitude);

        tvTechName = (TextView) rootView.findViewById(R.id.tvTechName);

        listView = (ListView) rootView.findViewById(R.id.listItemsSentReport);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Realm realm = Realm.getDefaultInstance();

        List<String> reportListStrValues = new ArrayList<>();
        tvTechName.setText(selectedTech.getFullNameTehnic());

        realm.beginTransaction();
        ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId())
                .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo)
                .findFirst();
        realm.commitTransaction();

        reportItemUnmanaged = realm.copyFromRealm(reportItem);

        if (reportItemUnmanaged != null)
        {
            id_sopralluogo = reportItemUnmanaged.getId_sopralluogo();
            photosFolderName = "photos" + id_sopralluogo;

            String product_type = reportItemUnmanaged.getClientData().getProduct_type();

            List<GeaItemRapporto> geaItemsRapporto = reportItemUnmanaged.getGea_items_rapporto_sopralluogo();
            rl_ImaginesRapportoSopralluogo = reportItemUnmanaged.getGea_immagini_rapporto_sopralluogo();

            if (geaItemsRapporto.size() != 0)
            {
                tvReportName.setText(product_type);

                int idItem = geaItemsRapporto.get(0).getId_item_modello();
                int idItemStart = idItem;
                int idItemEnd = idItem;

                for (GeaItemRapporto geaItemRapporto : geaItemsRapporto)
                {
                    idItem = geaItemRapporto.getId_item_modello();

                    if (idItem > idItemEnd)
                    {
                        idItemEnd = idItem;
                    }

                    if (idItem < idItemStart)
                    {
                        idItemStart = idItem;
                    }
                }

                realm.beginTransaction();
                RealmResults<GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class)
                        .between("id_item_modello", idItemStart, idItemEnd + 1).findAll();
                realm.commitTransaction();

                for (int i = 0; i < geaItemModelli.size(); i++)
                {
                    for (int j = 0; j < geaItemsRapporto.size(); j++)
                    {
                        if (geaItemModelli.get(i).getId_item_modello() == geaItemsRapporto.get(j).getId_item_modello())
                        {
                            String valore = geaItemsRapporto.get(j).getValore();
                            valore = valore.replace("||", "  ");
                            reportListStrValues.add(String.valueOf(geaItemModelli.get(i).getDescrizione_item()) + " : " + valore);
                        }
                    }
                }

                tvdataOraSopralluogo.setText(reportItem.getGeaSopralluogo().getData_ora_sopralluogo());
                tvdataOraRaportoCompletato.setText(reportItem.getGea_rapporto_sopralluogo().getDataOraRaportoCompletato());
                tvdataOraRaportoInviato.setText(reportItem.getGea_rapporto_sopralluogo().getData_ora_invio_rapporto());
            }

            tvClientName.setText(reportItemUnmanaged.getClientData().getName());
            String clientPhones = reportItemUnmanaged.getClientData().getMobile() + ", " + reportItem.getClientData().getPhone();
            tvClientPhones.setText(clientPhones);
            tvClientAddress.setText(reportItemUnmanaged.getClientData().getAddress());

            String latitude = reportItemUnmanaged.getGea_rapporto_sopralluogo().getLatitudine();
            String longitude = reportItemUnmanaged.getGea_rapporto_sopralluogo().getLongitudine();
            String altitude = reportItemUnmanaged.getGea_rapporto_sopralluogo().getAltitudine();

            tvCoordNord.setText(String.valueOf(latitude));
            tvCoordEst.setText(String.valueOf(longitude));
            tvAltitude.setText(altitude);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.report_sent_detailed_item_row, R.id.tvReportDataItem, reportListStrValues);

        listView.setAdapter(adapter);

        realm.close();

        imageThumbnails = new ArrayList<>();
        pathItems = new ArrayList<>();

        RedrawImagesTask redrawImagesTask = new RedrawImagesTask();
        redrawImagesTask.execute();
    }

    private void getImagesArray()
    {
        callDownloadImagesList = new ArrayList<>();
        imgCallAttrs = new ArrayList<>();
        NetworkUtils networkUtils = new NetworkUtils();

        String image_base_url = mSettings.getString("image_base_url", null);
        callDownloadImagesList.clear();

        if (image_base_url != null && id_rapporto_sopralluogo != 0)
        {
            for (int i = 0; i < rl_ImaginesRapportoSopralluogo.size(); i++)
            {
                String imageName = rl_ImaginesRapportoSopralluogo.get(i).getNome_file();

                if (!imageNames.contains(imageName))
                {
                    String pathSuffix = Integer.toString(id_rapporto_sopralluogo) + "/" + imageName;

                    callDownloadImagesList.add(networkUtils.downloadURL(this, image_base_url + pathSuffix));

                    imgCallAttrs.add(new ImgCallAttrs(callDownloadImagesList.get(callDownloadImagesList.size() - 1), id_sopralluogo, imageName));
                }

            }
            callsSent = callDownloadImagesList.size();
        }
    }

    @Override
    public void onFailure(Call call, IOException e)
    {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        for (int i = 0; i < callDownloadImagesList.size(); i++)
        {
            if (call == imgCallAttrs.get(i).getCall())
            {
                int id_sopralluogo = imgCallAttrs.get(i).getId_sopralluogo();
                String fileName = imgCallAttrs.get(i).getFileName();

                String photosFolderName = "photos" + id_sopralluogo;

                File photosDir = new File(activity.getFilesDir(), photosFolderName);

                if (!photosDir.exists())
                {
                    photosDir.mkdir();
                }

                File file = new File(photosDir, fileName);
                BufferedSink sink = Okio.buffer(Okio.sink(file));
                sink.writeAll(response.body().source());
                sink.close();
                response.body().close();

                pathItems.add(file);
                callsSent--;
                break;
            }
        }

        if (callsSent == 0)
        {
            RedrawImagesTask redrawImagesTask = new RedrawImagesTask();
            redrawImagesTask.execute();
        }
    }

    private void fillImagesArray()
    {
        File appDirectory = new File(activity.getFilesDir(), photosFolderName);

        if (!appDirectory.exists() && !appDirectory.mkdir())
        {
            return;
        }

        imageNames = new ArrayList<>();

        File[] filePaths = appDirectory.listFiles();

        for (File file : filePaths)
        {
            Bitmap bm = ImageUtils.decodeSampledBitmapFromUri(file.getAbsolutePath(), imageHolderWidth, imageHolderHeight);

            imageThumbnails.add(bm);
            pathItems.add(file);
            imageNames.add(file.getName());
        }
    }

    class LoadImagesTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                getImagesArray(); // Long time operation
            } catch (Exception e)
            {
                loadingImagesDialog.dismiss();
            }

            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loadingImagesDialog.setMessage(getString(R.string.LoadingImagesInProgress));
            loadingImagesDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            loadingImagesDialog.dismiss();

            GridViewAdapter gridAdapter = new GridViewAdapter(activity, R.layout.grid_item_layout, imageThumbnails);

            gvPhotoGallery.setAdapter(gridAdapter);

            ViewUtils.setGridViewHeight(gvPhotoGallery);

            //handler.removeCallbacks(runnable);
        }
    }

    class RedrawImagesTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                fillImagesArray(); // Long time operation
            } catch (Exception e)
            {
                loadingImagesDialog.dismiss();
            }

            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loadingImagesDialog.setMessage(getString(R.string.LoadingImagesInProgress));
            loadingImagesDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            loadingImagesDialog.dismiss();

            GridViewAdapter gridAdapter = new GridViewAdapter(activity, R.layout.grid_item_layout, imageThumbnails);

            gvPhotoGallery.setAdapter(gridAdapter);

            ViewUtils.setGridViewHeight(gvPhotoGallery);

            LoadImagesTask loadImagesTask = new LoadImagesTask();
            loadImagesTask.execute();

            //handler.removeCallbacks(runnable);
        }
    }
}
