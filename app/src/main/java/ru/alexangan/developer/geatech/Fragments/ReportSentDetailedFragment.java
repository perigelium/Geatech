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

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.GridViewAdapter;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.ImageUtils;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

// Created by Alex Angan on 11/10/2016 .

public class ReportSentDetailedFragment extends Fragment
{
    private int sel_index;
    List<String> reportItems;
    GridView gvPhotoGallery;
    private ProgressDialog loadingImagesDialog;
    Activity activity;
    int imageHolderWidth = 100;
    int imageHolderHeight = 75;
    private String photosFolderName;
    ArrayList<Bitmap> imageThumbnails;
    ArrayList<File> pathItems;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            sel_index = getArguments().getInt("selectedIndex");
        }

        activity = getActivity();
        reportItems = new ArrayList<>();

        loadingImagesDialog = new ProgressDialog(getActivity());
        loadingImagesDialog.setTitle("");
        loadingImagesDialog.setIndeterminate(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.report_sent_detailed_fragment, container, false);

        gvPhotoGallery = (GridView) rootView.findViewById(R.id.gvPhotoGallery);

        TextView tvdataOraSopralluogo = (TextView) rootView.findViewById(R.id.tvdataOraSopralluogo);
        TextView tvdataOraRaportoCompletato = (TextView) rootView.findViewById(R.id.tvdataOraRaportoCompletato);
        TextView tvdataOraRaportoInviato = (TextView) rootView.findViewById(R.id.tvdataOraRaportoInviato);
        TextView tvReportName = (TextView) rootView.findViewById(R.id.tvReportName);

        TextView tvTechName = (TextView) rootView.findViewById(R.id.tvTechName);
        tvTechName.setText(selectedTech.getFullNameTehnic());

        VisitItem visitItem = visitItems.get(sel_index);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();

        realm.beginTransaction();
        ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if (reportItem != null)
        {
            photosFolderName = "photos" + idSopralluogo;

            String product_type = visitItem.getProductData().getProductType();
            int id_rapporto_sopralluogo = reportItem.getGea_rapporto().getId_rapporto_sopralluogo();

            List<GeaItemRapporto> geaItemsRapporto = realm.where(GeaItemRapporto.class).equalTo("company_id", company_id)
                    .equalTo("tech_id", selectedTech.getId()).equalTo("sel_index", id_rapporto_sopralluogo).findAll();

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
                            reportItems.add(String.valueOf(geaItemModelli.get(i).getDescrizione_item()) + " : " + geaItemsRapporto.get(j).getValore());
                        }
                    }
                }

                tvdataOraSopralluogo.setText(reportItem.getGeaSopralluogo().getData_ora_sopralluogo());
                tvdataOraRaportoCompletato.setText(reportItem.getGea_rapporto().getDataOraRaportoCompletato());
                tvdataOraRaportoInviato.setText(reportItem.getGea_rapporto().getData_ora_invio_rapporto());
            }


            TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
            clientNameTextView.setText(visitItem.getClientData().getName());

            TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
            clientPhoneTextView.setText(visitItem.getClientData().getMobile());

            TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
            clientAddressTextView.setText(visitItem.getClientData().getAddress());


            TextView tvCoordNord = (TextView) rootView.findViewById(R.id.etCoordNord);
            TextView tvCoordEst = (TextView) rootView.findViewById(R.id.etCoordEst);
            TextView tvAltitude = (TextView) rootView.findViewById(R.id.etAltitude);

            String latitude = reportItem.getGea_rapporto().getLatitudine();
            String longitude = reportItem.getGea_rapporto().getLongitudine();
            String altitude = reportItem.getGea_rapporto().getAltitudine();

            tvCoordNord.setText(String.valueOf(latitude));
            tvCoordEst.setText(String.valueOf(longitude));

            if (altitude != null)
            {
                tvAltitude.setText(altitude);
            } else
            {
                tvAltitude.setText(R.string.Unknown);
            }
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listItemsSentReport);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.report_sent_detailed_item_row, R.id.tvReportDataItem, reportItems);

        listView.setAdapter(adapter);

        ViewUtils.setListViewHeightBasedOnChildren(listView);

        imageThumbnails = new ArrayList<>();
        pathItems = new ArrayList<>();

        LoadImagesTask loadImagesTask = new LoadImagesTask();
        loadImagesTask.execute();

        return rootView;
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
            getImagesArray(); // Long time operation
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
