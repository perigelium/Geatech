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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.GridViewAdapter;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.ImageUtils;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

// Created by Alex Angan on 11/10/2016 .

public class ReportSentDetailedFragment extends Fragment
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
    private TextView tvReportName, clientNameTextView;
    private TextView clientPhoneTextView, clientAddressTextView;
    private TextView tvCoordNord, tvCoordEst, tvAltitude;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            id_rapporto_sopralluogo = getArguments().getInt("id_rapporto_sopralluogo");
        }

        activity = getActivity();

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

        clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);

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

        List<String> reportListStrValues = new ArrayList<>();
        tvTechName.setText(selectedTech.getFullNameTehnic());

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId())
                .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo)
                .findFirst();
        realm.commitTransaction();

        if (reportItem != null)
        {
            photosFolderName = "photos" + reportItem.getId_sopralluogo();

            String product_type = reportItem.getClientData().getProduct_type();

            List<GeaItemRapporto> geaItemsRapporto = reportItem.getGea_items_rapporto_sopralluogo();

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
                            reportListStrValues.add(String.valueOf(geaItemModelli.get(i).getDescrizione_item()) + " : " + geaItemsRapporto.get(j).getValore());
                        }
                    }
                }

                tvdataOraSopralluogo.setText(reportItem.getGeaSopralluogo().getData_ora_sopralluogo());
                tvdataOraRaportoCompletato.setText(reportItem.getGea_rapporto_sopralluogo().getDataOraRaportoCompletato());
                tvdataOraRaportoInviato.setText(reportItem.getGea_rapporto_sopralluogo().getData_ora_invio_rapporto());
            }

            clientNameTextView.setText(reportItem.getClientData().getName());
            clientPhoneTextView.setText(reportItem.getClientData().getMobile());
            clientAddressTextView.setText(reportItem.getClientData().getAddress());

            String latitude = reportItem.getGea_rapporto_sopralluogo().getLatitudine();
            String longitude = reportItem.getGea_rapporto_sopralluogo().getLongitudine();
            String altitude = reportItem.getGea_rapporto_sopralluogo().getAltitudine();

            tvCoordNord.setText(String.valueOf(latitude));
            tvCoordEst.setText(String.valueOf(longitude));
            tvAltitude.setText(altitude);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.report_sent_detailed_item_row, R.id.tvReportDataItem, reportListStrValues);

        listView.setAdapter(adapter);

        realm.close();

        imageThumbnails = new ArrayList<>();
        pathItems = new ArrayList<>();

        LoadImagesTask loadImagesTask = new LoadImagesTask();
        loadImagesTask.execute();
    }

    private void getImagesArray()
    {
        File appDirectory = new File(activity.getFilesDir(), photosFolderName);

        if (!appDirectory.exists() && !appDirectory.mkdir())
        {
            return;
        }

        File[] filePaths = appDirectory.listFiles();

        for (File file : filePaths)
        {
            Bitmap bm = ImageUtils.decodeSampledBitmapFromUri(file.getAbsolutePath(), imageHolderWidth, imageHolderHeight);

            imageThumbnails.add(bm);
            pathItems.add(file);
        }
    }

    @Override
    public View getView()
    {
        return super.getView();
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume()
    {
        super.onResume();
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
}
