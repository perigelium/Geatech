package ru.alexangan.developer.geatech.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Fragments.CTLinfoFragment;
import ru.alexangan.developer.geatech.Fragments.CaldaiaReportFragment;
import ru.alexangan.developer.geatech.Fragments.ClimatizzazioneReportFragment;
import ru.alexangan.developer.geatech.Fragments.ComingListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnReportDetailed;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnsBottom;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnsFragment2;
import ru.alexangan.developer.geatech.Fragments.DomoticaReportFragment;
import ru.alexangan.developer.geatech.Fragments.EmptyReportFragment;
import ru.alexangan.developer.geatech.Fragments.FotovoltaicoReportFragment;
import ru.alexangan.developer.geatech.Fragments.FragListVisitsFree;
import ru.alexangan.developer.geatech.Fragments.FragListVisitsOther;
import ru.alexangan.developer.geatech.Fragments.FragListVisitsToday;
import ru.alexangan.developer.geatech.Fragments.InWorkListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.NotSentListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.NotificationBarFragment;
import ru.alexangan.developer.geatech.Fragments.PhotoGalleryGridFragment;
import ru.alexangan.developer.geatech.Fragments.PompaDiCaloreReportFragment;
import ru.alexangan.developer.geatech.Fragments.ReportSentDetailedFragment;
import ru.alexangan.developer.geatech.Fragments.ReportsListFragment;
import ru.alexangan.developer.geatech.Fragments.STermodinamicoReportFragment;
import ru.alexangan.developer.geatech.Fragments.SendReportFragment;
import ru.alexangan.developer.geatech.Fragments.SetDateTimeFragment;
import ru.alexangan.developer.geatech.Fragments.StorageReportFragment;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.FileUtils;
import ru.alexangan.developer.geatech.Utils.JSON_to_model;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static android.os.Environment.DIRECTORY_PICTURES;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.GET_MODELS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.GET_VISITS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.inVisitItems;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.listVisitsIsObsolete;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class MainActivity extends Activity implements Communicator, Callback
{
    private FragmentManager mFragmentManager;
    SwipeDetector swipeDetector;

    CtrlBtnsBottom ctrlBtnsBottom;
    CtrlBtnsFragment2 ctrlBtnsFragment2;
    SetDateTimeFragment dateTimeSetFragment;
    FragListVisitsFree listVisitsFree;
    FragListVisitsToday listVisitsToday;
    FragListVisitsOther listVisitsOther;
    InWorkListVisitsFragment inWorkListVisits;
    ComingListVisitsFragment comingListVisits;
    NotSentListVisitsFragment notSentListVisits;
    ReportsListFragment reportsList;
    private ProgressDialog requestServerDialog;
    private boolean timeNotSetItemsOnly;
    Handler handler;
    Runnable runnable;
    AlertDialog alert;

    CTLinfoFragment ctlInfo;
    CtrlBtnReportDetailed ctrlBtnsReportDetailed;
    ReportSentDetailedFragment reportDetailedFragment;
    SendReportFragment sendReportFragment;
    PhotoGalleryGridFragment photoGalleryGridFragment;

    STermodinamicoReportFragment termodinamicoReportFragment;
    CaldaiaReportFragment caldaieReportFragment;
    ClimatizzazioneReportFragment climatizzazioneReportFragment;
    FotovoltaicoReportFragment fotovoltaicoReportFragment;
    DomoticaReportFragment domoticaReportFragment;
    PompaDiCaloreReportFragment pompaDiCaloreReportFragment;
    StorageReportFragment storageReportFragment;
    EmptyReportFragment emptyReportFragment;
    Fragment frag;

    NotificationBarFragment notificationBarFragment;
    int currentSelIndex;
    boolean ctrlBtnChkChanged;
    private Call callVisits, callModels;
    NetworkUtils networkUtils;

    @Override
    protected void onPause()
    {
        super.onPause();

        if(handler!=null && runnable!= null)
        {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        String imagesDirPath = getExternalFilesDir(DIRECTORY_PICTURES).getAbsolutePath();

            File imagesDir = new File(imagesDirPath);

            for (File child : imagesDir.listFiles())
            {
                child.delete();
            }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_window);

        currentSelIndex = -1;
        ctrlBtnChkChanged = true;

        realm.beginTransaction();
        visitItems = realm.where(VisitItem.class).findAll();
        realm.commitTransaction();

        if (visitItems.size() == 0)
        {
            Toast.makeText(this, R.string.ListVisitsIsEmpty, Toast.LENGTH_LONG).show();

            this.finish();
        }

        swipeDetector = new SwipeDetector();

        ctrlBtnsBottom = new CtrlBtnsBottom();
        //ctrlBtnsBottom = new ctrlBtnsBottom();
        ctrlBtnsFragment2 = new CtrlBtnsFragment2();
        dateTimeSetFragment = new SetDateTimeFragment();
        listVisitsFree = new FragListVisitsFree();
        listVisitsToday = new FragListVisitsToday();
        listVisitsOther = new FragListVisitsOther();
        inWorkListVisits = new InWorkListVisitsFragment();
        comingListVisits = new ComingListVisitsFragment();
        notSentListVisits = new NotSentListVisitsFragment();
        reportsList = new ReportsListFragment();
        ctlInfo = new CTLinfoFragment();
        ctrlBtnsReportDetailed = new CtrlBtnReportDetailed();
        reportDetailedFragment = new ReportSentDetailedFragment();
        sendReportFragment = new SendReportFragment();
        photoGalleryGridFragment = new PhotoGalleryGridFragment();

        termodinamicoReportFragment = new STermodinamicoReportFragment();
        caldaieReportFragment = new CaldaiaReportFragment();
        climatizzazioneReportFragment = new ClimatizzazioneReportFragment();
        fotovoltaicoReportFragment = new FotovoltaicoReportFragment();
        domoticaReportFragment = new DomoticaReportFragment();
        pompaDiCaloreReportFragment = new PompaDiCaloreReportFragment();
        storageReportFragment = new StorageReportFragment();
        emptyReportFragment = new EmptyReportFragment();
        frag = new Fragment();

        notificationBarFragment = new NotificationBarFragment();

        mFragmentManager = getFragmentManager();

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.NotificationBarFragContainer, notificationBarFragment);

        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsBottom);
        //mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsBottom);
        //mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment2);
        //mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsReportDetailed);

        //mFragmentTransaction.addToBackStack("listVisitsFree");

/*        Bundle args = new Bundle();
        args.putBoolean("withNoSopralluogoTime", false);
        listVisits.setArguments(args);*/
        mFragmentTransaction.add(R.id.InnerFragContainer, listVisitsFree);
        mFragmentTransaction.add(R.id.InnerFragContainer, listVisitsToday);
        mFragmentTransaction.add(R.id.InnerFragContainer, listVisitsOther);

        //mFragmentTransaction.hide(ctrlBtnsReportDetailed);
        //mFragmentTransaction.hide(ctrlBtnsFragment2);
        //mFragmentTransaction.hide(ctrlBtnsBottom);
        mFragmentTransaction.show(ctrlBtnsBottom);

        //mFragmentManager.executePendingTransactions();

        mFragmentTransaction.commit();

        networkUtils = new NetworkUtils();

        requestServerDialog = new ProgressDialog(this);
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.DownloadingDataPleaseWait));
        requestServerDialog.setIndeterminate(true);

        timeNotSetItemsOnly = false;
        listVisitsIsObsolete = false;

        handler = new Handler();

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                showToastMessage(getString(R.string.ServerAnswerNotReceived));
                requestServerDialog.dismiss();
            }
        };
    }

    @Override
    public void onBackPressed()
    {
        if (!listVisitsFree.isAdded())
        {
            removeAllLists();

            if (ctrlBtnsReportDetailed.isVisible())
            {
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.hide(ctrlBtnsReportDetailed);
                mFragmentTransaction.commit();

                //ctrlBtnsBottom.setCheckedBtnId(R.id.btnSentReports);
            } else
            {
                ctrlBtnsBottom.setCheckedBtnId(R.id.btnVisits);
            }
            //removeAllLists();

            //currentSelIndex = -1;
        } else
        {
            //super.onBackPressed();
            this.finish();
        }
    }

    @Override
    public void onCtrlButtons1Clicked(View view)
    {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.hide(ctrlBtnsFragment2);
        fragmentTransaction.show(ctrlBtnsBottom);
        fragmentTransaction.commit();
        currentSelIndex = -1;

        if (view == findViewById(R.id.btnVisits))
        {
            removeAllLists();

            listVisitsFree = new FragListVisitsFree();

            if (!listVisitsFree.isAdded())
            {
                Bundle args = new Bundle();
                args.putBoolean("timeNotSetItemsOnly", timeNotSetItemsOnly);
                listVisitsFree.setArguments(args);

                if (listVisitsIsObsolete && NetworkUtils.isNetworkAvailable(this))
                {
                    refreshVisitsList();
                } else
                {
                    setVisitsListContent(listVisitsFree);
                }

                timeNotSetItemsOnly = false;
            }
        }

        if (view == findViewById(R.id.btnReportsReturn))
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(ctrlBtnsReportDetailed);
            mFragmentTransaction.commit();

/*            removeAllLists();
            setVisitsListContent(reportsList);*/

            //ctrlBtnsBottom.setCheckedBtnId(R.id.btnSentReports);
        }

        if (view == findViewById(R.id.btnNotifUrgentReports))
        {
            removeAllLists();
            setVisitsListContent(comingListVisits);
        }

        if (view == findViewById(R.id.btnInWorkVisits))
        {
            removeAllLists();
            setVisitsListContent(inWorkListVisits);
        }

        if (view == findViewById(R.id.btnNotSentReports))
        {
            removeAllLists();
            setVisitsListContent(notSentListVisits);
        }

/*        if (view == findViewById(R.id.btnSentReports))
        {
            removeAllLists();
            setVisitsListContent(reportsList);
        }*/
    }

    @Override
    public void onCtrlButtons2Clicked(View view)
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.hide(ctrlBtnsBottom);
        mFragmentTransaction.show(ctrlBtnsFragment2);
        mFragmentTransaction.commit();

        if (currentSelIndex == -1)
        {
            return;
        }

        if (view == findViewById(R.id.btnReturn))
        {
            removeAllLists();

            ctrlBtnsBottom.setCheckedBtnId(R.id.btnVisits);
        }

        if (view == findViewById(R.id.btnFillReport))
        {
            VisitItem visitItem = visitItems.get(currentSelIndex);
            ProductData productData = visitItem.getProductData();
            String productType = productData.getProductType();

            removeAllLists();

            frag = null;
            frag = assignFragmentModel(productType);

            if (!frag.isAdded())
            {
                Bundle args = frag.getArguments() != null ? frag.getArguments() : new Bundle();
                args.putInt("selectedIndex", currentSelIndex);

                frag.setArguments(args);

                setVisitsListContent(frag);
            }
        }

        if (view == findViewById(R.id.btnAddPhotos))
        {
            removeAllLists();

            if (!photoGalleryGridFragment.isAdded())
            {
                Bundle args = frag.getArguments() != null ? frag.getArguments() : new Bundle();
                args.putInt("selectedIndex", currentSelIndex);
                photoGalleryGridFragment.setArguments(args);

                setVisitsListContent(photoGalleryGridFragment);
            }
        }

        if (view == findViewById(R.id.btnInfo))
        {
            removeAllLists();

            if (!ctlInfo.isAdded())
            {
                Bundle args = new Bundle();
                args.putInt("selectedIndex", currentSelIndex);
                ctlInfo.setArguments(args);

                setVisitsListContent(ctlInfo);
            }
        }

        if (view == findViewById(R.id.btnSendReport))
        {
            removeAllLists();

            if (!sendReportFragment.isAdded())
            {
                Bundle args = new Bundle();
                args.putInt("selectedIndex", currentSelIndex);
                sendReportFragment.setArguments(args);

                setVisitsListContent(sendReportFragment);
            }
        }
    }

    private void removeAllLists()
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        if (currentSelIndex != -1)
        {
            VisitItem visitItem = visitItems.get(currentSelIndex);
            ProductData productData = visitItem.getProductData();
            String productType = productData.getProductType();

            Fragment frag = assignFragmentModel(productType);

            if (frag.isAdded())
            {
                mFragmentTransaction.remove(frag);
            }
        }

        if (emptyReportFragment.isAdded())
        {
            mFragmentTransaction.remove(emptyReportFragment);
        }

        if (photoGalleryGridFragment.isAdded())
        {
            mFragmentTransaction.remove(photoGalleryGridFragment);
        }

        if (sendReportFragment.isAdded())
        {
            mFragmentTransaction.remove(sendReportFragment);
        }

        if (reportDetailedFragment.isAdded())
        {
            mFragmentTransaction.remove(reportDetailedFragment);
        }

        if (dateTimeSetFragment.isAdded())
        {
            mFragmentTransaction.remove(dateTimeSetFragment);
        }

        if (ctlInfo.isAdded())
        {
            mFragmentTransaction.remove(ctlInfo);
        }

        if (listVisitsFree.isAdded())
        {
            mFragmentTransaction.remove(listVisitsFree);
        }

        if (comingListVisits.isAdded())
        {
            mFragmentTransaction.remove(comingListVisits);
        }

        if (inWorkListVisits.isAdded())
        {
            mFragmentTransaction.remove(inWorkListVisits);
        }

        if (notSentListVisits.isAdded())
        {
            mFragmentTransaction.remove(notSentListVisits);
        }

        if (reportsList.isAdded())
        {
            mFragmentTransaction.remove(reportsList);
        }

        mFragmentManager.executePendingTransactions();

        mFragmentTransaction.commit();
    }

    private void setVisitsListContent(Fragment fragment)
    {
        FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();

        if (!fragment.isAdded())
        {
            vFragmentTransaction.add(R.id.CtrlBtnFragContainer, fragment);
        }

        vFragmentTransaction.commit();
        //timeNotSetItemsOnly = false;
    }

    @Override
    public void OnListItemSelected(int itemIndex, boolean dateTimeHasSet)
    {
        //removeAllLists();

        currentSelIndex = itemIndex;

        if (dateTimeHasSet) // if visit day is empty, show set datetime fragment, otherwise show CTL info.
        {
            ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);
        } else
        {
            removeAllLists();

            if (!dateTimeSetFragment.isAdded())
            {
                Bundle args = new Bundle();
                args.putInt("selectedIndex", itemIndex);
                dateTimeSetFragment.setArguments(args);

                setVisitsListContent(dateTimeSetFragment);
            }
        }
    }

    @Override
    public void onDateTimeSetReturned(int itemIndex)
    {
        currentSelIndex = itemIndex;
        listVisitsIsObsolete = true;
        ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);
    }

    @Override
    public void onDetailedReportReturned()
    {
        onCtrlButtons1Clicked(findViewById(R.id.btnReportsReturn));
    }

    @Override
    public void onSendReportReturned(int id_rapporto_sopralluogo)
    {
        OnReportListItemSelected(id_rapporto_sopralluogo);
    }

    @Override
    public void OnReportListItemSelected(int itemIndex)
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.hide(ctrlBtnsFragment2);
        mFragmentTransaction.hide(ctrlBtnsBottom);
        mFragmentTransaction.show(ctrlBtnsReportDetailed);
        mFragmentTransaction.commit();

        removeAllLists();

        if (!reportDetailedFragment.isAdded())
        {
            Bundle args = reportDetailedFragment.getArguments() != null ? reportDetailedFragment.getArguments() : new Bundle();

            args.putInt("selectedIndex", itemIndex);
            reportDetailedFragment.setArguments(args);

            setVisitsListContent(reportDetailedFragment);
        }
    }

    @Override
    public void OnInWorkListItemSelected(int itemIndex)
    {
        currentSelIndex = itemIndex;

        ctrlBtnsFragment2.setCheckedBtnId(R.id.btnFillReport);
    }

    @Override
    public void onNotificationReportReturned(View view)
    {
        if (view.getId() == R.id.btnNotifTimeNotSetVisits)
        {
            removeAllLists();

            listVisitsFree = new FragListVisitsFree();

            if (!listVisitsFree.isAdded())
            {
                timeNotSetItemsOnly = true;
                listVisitsIsObsolete = true;

                ctrlBtnsBottom.setCheckedBtnId(R.id.btnVisits);
            }

            //ctrlBtnsBottom.setCheckedBtnId(R.id.btnVisits);
        }

        if (view.getId() == R.id.btnNotifUrgentReports)
        {
            Toast.makeText(this, R.string.NoUrgentReportsFound, Toast.LENGTH_LONG).show();
        }

        if (view.getId() == R.id.btnAppSettings)
        {
            String[] listItemsArray = {"Aggiorna applicazione", "Logout", "Esci"};

            //ContextThemeWrapper themedContext = new ContextThemeWrapper
            // (this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.alert_dialog_custom, null);

            ListView listView = (ListView) layout.findViewById(R.id.alertList);
            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, R.layout.alert_dialog_item_custom, listItemsArray);
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int which, long id)
                {
/*                    if (which == 1)
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Password recover", true);
                        startActivity(intent);
                        finish();
                    }*/

                    if (which == 2)
                    {
                        exitApp();
                    }

                    if (which == 1)
                    {
                        logout();
                    }
                    if (which == 0)
                    {
                        //showToastMessage("Not implemented exception");
                        refreshGeaModels();
                    }

                }
            });

            //ListView listView = (ListView) v.findViewById(R.id.notifBarlist);

            //final ArrayAdapter <String> listAdapter = new ArrayAdapter<>(this, R.id.DialogTextView, listItemsArray);
            //listView.setAdapter(listAdapter);
            //listView.setBackgroundColor(Color.GRAY);

/*            builder.setItems(listItemsArray, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface Dialog, int which)
                {
                    if(which == 1)  // exit app
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Password recover", true);
                        startActivity(intent);
                        finish();
                    }

                    if(which == 0) // password recover
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Exit app", true);
                        startActivity(intent);
                        finish();
                    }
                }
            });*/

            builder.setView(layout);
            alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void OnComingListItemSelected(int itemIndex)
    {
        currentSelIndex = itemIndex;
        ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);
    }

    @Override
    public void onLogoutCommand()
    {
        logout();
    }

    @Override
    public void onCoordsSetReturned(int itemIndex)
    {
        //currentSelIndex = itemIndex;
        ctrlBtnsFragment2.setCheckedBtnId(R.id.btnFillReport);
    }

    @Override
    public void OnListItemSwiped(int itemIndex, boolean dateTimeHasSet)
    {
        if (!dateTimeHasSet)
        {
            OnListItemSelected(itemIndex, false);
        } else
        {
            OnListItemSelected(itemIndex, true);
        }
    }

    public Fragment assignFragmentModel(String productType)
    {
        switch (productType)
        {
            case "SOLARE TERMODINAMICO":
                return termodinamicoReportFragment;
            case "CALDAIE":
                return caldaieReportFragment;
            case "CLIMATIZZAZIONE":
                return climatizzazioneReportFragment;
            case "FOTOVOLTAICO":
                return fotovoltaicoReportFragment;
            case "DOMOTICA":
                return domoticaReportFragment;
            case "STORAGE":
                return storageReportFragment;
            case "POMPA DI CALORE":
                return pompaDiCaloreReportFragment;

            default:
                return emptyReportFragment;
        }
    }

    public void showToastMessage(final String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callVisits)
        {
            showToastMessage(getString(R.string.ListVisitsReceiveFailed));

            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    requestServerDialog.dismiss();
                }
            });

            handler.removeCallbacks(runnable);
        }

        if (call == callModels)
        {
            showToastMessage(getString(R.string.ApplicationUpdateFailed));

            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    requestServerDialog.dismiss();
                }
            });

            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callVisits)
        {
            handler.removeCallbacks(runnable);

            final String visitsJSONData = response.body().string();

            response.body().close();

            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    realm.beginTransaction();

                    inVisitItems = JSON_to_model.getVisitTtemsList(visitsJSONData);

                    visitItems = realm.where(VisitItem.class).findAll();
                    visitItems.deleteAllFromRealm();

                    if (inVisitItems != null && inVisitItems.size() > 0)
                    {
                        for (VisitItem visitItem : inVisitItems)
                        {
                            realm.copyToRealmOrUpdate(visitItem);
                        }
                    }
                    realm.commitTransaction();

                    realm.beginTransaction();
                    visitItems = realm.where(VisitItem.class).findAll();
                    realm.commitTransaction();

                    //removeAllLists();

                    setVisitsListContent(listVisitsFree);

                    requestServerDialog.dismiss();
                    //ctrlBtnsBottom.setCheckedBtnId(R.id.btnVisits);
                }
            });
        }

        if (call == callModels)
        {
            String modelsJSONData = response.body().string();

            //Log.d("DEBUG", modelsJSONData);

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(modelsJSONData);

                if (jsonObject.has("type_report_data"))
                {
                    try
                    {
                        JSONObject type_report_data = jsonObject.getJSONObject("type_report_data");

                        String str_gea_modelli = type_report_data.getString("gea_modelli_rapporto_sopralluogo");
                        String str_gea_sezioni_modelli = type_report_data.getString("gea_sezioni_modelli_rapporto_sopralluogo");
                        String str_gea_items_modelli = type_report_data.getString("gea_items_modelli_rapporto_sopralluogo");


                        if (Build.VERSION.SDK_INT >= 24)
                        {
                            //str_gea_modelli = String.valueOf(Html.fromHtml(str_gea_modelli, Html.FROM_HTML_MODE_LEGACY));
                            str_gea_sezioni_modelli = String.valueOf(Html.fromHtml(str_gea_sezioni_modelli, Html.FROM_HTML_MODE_LEGACY));
                            str_gea_items_modelli = String.valueOf(Html.fromHtml(str_gea_items_modelli, Html.FROM_HTML_MODE_LEGACY));
                        } else
                        {
                            //str_gea_modelli = String.valueOf(Html.fromHtml(str_gea_modelli));
                            str_gea_sezioni_modelli = String.valueOf(Html.fromHtml(str_gea_sezioni_modelli));
                            str_gea_items_modelli = String.valueOf(Html.fromHtml(str_gea_items_modelli));
                        }

                        Gson gson = new Gson();

                        Type typeGeaModelli = new TypeToken<List<GeaModelloRapporto>>()
                        {
                        }.getType();
                        final List<GeaModelloRapporto> l_geaModelli = gson.fromJson(str_gea_modelli, typeGeaModelli);

                        Type typeGeaSezioniModelli = new TypeToken<List<GeaSezioneModelliRapporto>>()
                        {
                        }.getType();
                        final List<GeaSezioneModelliRapporto> l_geaSezioniModelli = gson.fromJson(str_gea_sezioni_modelli, typeGeaSezioniModelli);

                        Type typeGeaItemsModelli = new TypeToken<List<GeaItemModelliRapporto>>()
                        {
                        }.getType();
                        final List<GeaItemModelliRapporto> l_geaItemsModelli = gson.fromJson(str_gea_items_modelli, typeGeaItemsModelli);

                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                realm.beginTransaction();
                                RealmResults<GeaModelloRapporto> geaModelli = realm.where(GeaModelloRapporto.class).findAll();
                                geaModelli.deleteAllFromRealm();
                                realm.commitTransaction();

                                for (GeaModelloRapporto gm : l_geaModelli)
                                {
                                    realm.beginTransaction();
                                    realm.copyToRealm(gm);
                                    realm.commitTransaction();
                                }

                                realm.beginTransaction();
                                RealmResults<GeaSezioneModelliRapporto> geaSezioniModelli = realm.where(GeaSezioneModelliRapporto.class).findAll();
                                geaSezioniModelli.deleteAllFromRealm();
                                realm.commitTransaction();

                                for (GeaSezioneModelliRapporto gs : l_geaSezioniModelli)
                                {
                                    realm.beginTransaction();
                                    realm.copyToRealm(gs);
                                    realm.commitTransaction();
                                }

                                realm.beginTransaction();
                                RealmResults<GeaItemModelliRapporto> geaItemModelli = realm.where(GeaItemModelliRapporto.class).findAll();
                                geaItemModelli.deleteAllFromRealm();
                                realm.commitTransaction();

                                for (GeaItemModelliRapporto gi : l_geaItemsModelli)
                                {
                                    realm.beginTransaction();
                                    realm.copyToRealm(gi);
                                    realm.commitTransaction();
                                }

                                requestServerDialog.dismiss();
                                alert.dismiss();
                                showToastMessage(getString(R.string.ApplicationUpdateSucceeded));
                                logout();
                            }
                        });

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            requestServerDialog.dismiss();
        }
    }

    private void logout()
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void exitApp()
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit app", true);
        startActivity(intent);
        finish();
    }

    private void refreshVisitsList()
    {
        requestServerDialog.show();

        handler.postDelayed(runnable, 30000);

        callVisits = networkUtils.getData(this, GET_VISITS_URL_SUFFIX, tokenStr);

        listVisitsIsObsolete = false;
    }

    private void refreshGeaModels()
    {
        requestServerDialog.show();

        handler.postDelayed(runnable, 30000);

        callModels = networkUtils.getData(this, GET_MODELS_URL_SUFFIX, tokenStr);

        listVisitsIsObsolete = false;
    }
}
