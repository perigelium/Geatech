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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.GridViewAdapter;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.ImageUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

/**
 * Created by Alex Angan on 11/10/2016 .
 */

public class ReportSentDetailedFragment extends Fragment
{
    private int id_rapporto_sopralluogo;
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
            id_rapporto_sopralluogo = getArguments().getInt("selectedIndex");
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

        realm.beginTransaction();
        ReportStates reportStates = realm.where(ReportStates.class).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findFirst();
        realm.commitTransaction();

        if (reportStates != null)
        {
            photosFolderName = "photos" + reportStates.getId_sopralluogo();

            String product_type = reportStates.getProductType();
            int id_rapporto_sopralluogo = reportStates.getId_rapporto_sopralluogo();

            List<GeaItemRapporto> geaItemsRapporto = realm.where(GeaItemRapporto.class).equalTo("company_id", company_id)
                    .equalTo("tech_id", selectedTech.getId()).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();

            if (geaItemsRapporto.size() != 0)
            {
                tvReportName.setText(product_type);

/*            realm.beginTransaction();
            RealmResults <GeaSezioneModelliRapporto> geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class)
                    .equalTo("id_modello", geaModello.getId_modello()).findAll();
            realm.commitTransaction();*/

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

                tvdataOraSopralluogo.setText(reportStates.getData_ora_sopralluogo());
                tvdataOraRaportoCompletato.setText(reportStates.getDataOraRaportoCompletato());
                tvdataOraRaportoInviato.setText(reportStates.getData_ora_invio_rapporto());
            }


            TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
            clientNameTextView.setText(reportStates.getClientName());

            TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
            clientPhoneTextView.setText(reportStates.getClientMobile());

            TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
            clientAddressTextView.setText(reportStates.getClientAddress());


            TextView tvCoordNord = (TextView) rootView.findViewById(R.id.etCoordNord);
            TextView tvCoordEst = (TextView) rootView.findViewById(R.id.etCoordEst);
            TextView tvAltitude = (TextView) rootView.findViewById(R.id.etAltitude);

            double latitude = reportStates.getLatitudine();
            double longitude = reportStates.getLongitudine();
            double altitude = reportStates.getAltitudine();

            tvCoordNord.setText(String.valueOf(latitude));
            tvCoordEst.setText(String.valueOf(longitude));

            if (altitude != ReportStates.ALTITUDE_UNKNOWN)
            {
                tvAltitude.setText(String.valueOf((int) altitude));
            } else
            {
                tvAltitude.setText(R.string.Unknown);
            }
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listItemsSentReport);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, reportItems);

        listView.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listView);

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

    private void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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

            setDynamicHeight(gvPhotoGallery);

            //handler.removeCallbacks(runnable);
        }
    }

    private void setDynamicHeight(GridView gridView)
    {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null)
        {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > 3)
        {
            x = items / 3;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }
}
