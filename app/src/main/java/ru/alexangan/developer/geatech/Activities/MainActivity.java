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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
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
import ru.alexangan.developer.geatech.Fragments.CtrlBtnsSopralluogo;
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
import ru.alexangan.developer.geatech.Fragments.SettingsFragment;
import ru.alexangan.developer.geatech.Fragments.StorageReportFragment;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.JSON_to_model;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static android.os.Environment.DIRECTORY_PICTURES;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.GET_MODELS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.GET_VISITS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.LIST_VISITS_MODE_ALL;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.LIST_VISITS_MODE_FREE;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.LIST_VISITS_MODE_TODAY;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.inVisitItems;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.listVisitsIsObsolete;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class MainActivity extends Activity implements Communicator, Callback
{
    private FragmentManager mFragmentManager;
    SwipeDetector swipeDetector;

    CtrlBtnsBottom ctrlBtnsBottom;
    CtrlBtnsSopralluogo ctrlBtnsSopralluogo;
    SetDateTimeFragment setDateTimeFragment;
    SettingsFragment settingsFragment;
    FragListVisitsFree fragListVisitsFree;
    FragListVisitsToday fragListVisitsToday;
    FragListVisitsOther fragListVisitsOther;
    InWorkListVisitsFragment inWorkListVisits;
    ComingListVisitsFragment comingListVisits;
    NotSentListVisitsFragment notSentListVisits;
    ReportsListFragment reportsList;
    private ProgressDialog requestServerDialog;
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
    protected void onStart()
    {
        super.onStart();

        ctrlBtnsBottom.setCheckedBtnId(R.id.btnVisits);
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
        ctrlBtnsSopralluogo = new CtrlBtnsSopralluogo();
        setDateTimeFragment = new SetDateTimeFragment();
        settingsFragment = new SettingsFragment();
        fragListVisitsFree = new FragListVisitsFree();
        fragListVisitsToday = new FragListVisitsToday();
        fragListVisitsOther = new FragListVisitsOther();
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
        mFragmentTransaction.add(R.id.footerFragContainer, ctrlBtnsBottom);
        mFragmentTransaction.commit();

        FragmentTransaction nFragmentTransaction = mFragmentManager.beginTransaction();
        nFragmentTransaction.add(R.id.headerFragContainer, notificationBarFragment);
        //nFragmentTransaction.addToBackStack(notificationBarFragment.getTag());
        nFragmentTransaction.commit();

/*        mFragmentTransaction.add(R.id.headerFragContainer, ctrlBtnsSopralluogo);
        mFragmentTransaction.addToBackStack(ctrlBtnsSopralluogo.getTag());*/

        //mFragmentTransaction.show(ctrlBtnsBottom);
        //mFragmentTransaction.hide(ctrlBtnsSopralluogo);
        //mFragmentManager.executePendingTransactions();

        networkUtils = new NetworkUtils();

        requestServerDialog = new ProgressDialog(this);
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.DownloadingDataPleaseWait));
        requestServerDialog.setIndeterminate(true);

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
        if (!fragListVisitsOther.isAdded())
        {
            Button btn = (Button) findViewById(R.id.btnVisits);
            btn.setSelected(false);
            ctrlBtnsBottom.setCheckedBtnId(R.id.btnVisits);

/*        if (!fragListVisitsOther.isAdded())
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            if(!fragListVisitsFree.isAdded())
            {
                mFragmentTransaction.add(R.id.innerFragContainer, fragListVisitsFree);
                mFragmentTransaction.addToBackStack(fragListVisitsFree.getTag());
            }
            if(!fragListVisitsToday.isAdded())
            {
                mFragmentTransaction.add(R.id.innerFragContainer, fragListVisitsToday);
                mFragmentTransaction.addToBackStack(fragListVisitsToday.getTag());
            }
            if(!fragListVisitsOther.isAdded())
            {
                mFragmentTransaction.add(R.id.innerFragContainer, fragListVisitsOther);
                mFragmentTransaction.addToBackStack(fragListVisitsOther.getTag());
            }
            mFragmentTransaction.commit();

            mFragmentManager.executePendingTransactions();

            if (ctrlBtnsSopralluogo.isAdded())
            {
                FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();
                vFragmentTransaction.replace(R.id.headerFragContainer, notificationBarFragment);

                mFragmentManager.executePendingTransactions();
                vFragmentTransaction.commit();
            }*/
        }else
        {
            //super.onBackPressed();
            this.finish();
        }
    }

    @Override
    public void onCtrlBtnsBottomClicked(int btnId)
    {
        currentSelIndex = -1;
        mFragmentManager.popBackStack();

        if (!notificationBarFragment.isAdded())
        {
            FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();
            vFragmentTransaction.replace(R.id.headerFragContainer, notificationBarFragment);
            vFragmentTransaction.commit();
        }

        if (btnId == R.id.btnVisits)
        {
            //removeAllLists(fragListVisitsFree.getTag());

            mFragmentManager.popBackStack();

            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            if(!fragListVisitsFree.isAdded())
            {
                mFragmentTransaction.add(R.id.innerFragContainer, fragListVisitsFree);
                mFragmentTransaction.addToBackStack(fragListVisitsFree.getTag());
            }
            if(!fragListVisitsToday.isAdded())
            {
                mFragmentTransaction.add(R.id.innerFragContainer, fragListVisitsToday);
                mFragmentTransaction.addToBackStack(fragListVisitsToday.getTag());
            }
            if(!fragListVisitsOther.isAdded())
            {
                mFragmentTransaction.add(R.id.innerFragContainer, fragListVisitsOther);
                mFragmentTransaction.addToBackStack(fragListVisitsOther.getTag());
            }
            mFragmentTransaction.commit();

            mFragmentManager.executePendingTransactions();

            int mode = mSettings.getInt("listVisitsFilterMode", 0);
            showSelectedVisitsList(mode);
        }

/*        if (btnId == R.id.btnReportsReturn)
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(ctrlBtnsReportDetailed);
            mFragmentTransaction.commit();
        }*/

        if (btnId == R.id.btnInWorkVisits)
        {
            setVisitsListContent(inWorkListVisits);
        }

        if (btnId == R.id.btnNotifUrgentReports)
        {
            setVisitsListContent(comingListVisits);
        }

        if (btnId == R.id.btnNotSentReports)
        {
            setVisitsListContent(notSentListVisits);
        }

        if (btnId == R.id.btnAppSettings)
        {
            setVisitsListContent(settingsFragment);

/*            String[] listItemsArray = {"Aggiorna applicazione", "Logout", "Esci"};

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
*//*                    if (which == 1)
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Password recover", true);
                        startActivity(intent);
                        finish();
                    }*//*

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
                        if(NetworkUtils.isNetworkAvailable(MainActivity.this))
                        {
                            refreshGeaModels();
                        }
                        else
                        {
                            showToastMessage(getString(R.string.CheckInternetConnection));
                        }
                    }

                }
            });

            builder.setView(layout);
            alert = builder.create();
            alert.show();*/
        }

/*        if (view == findViewById(R.id.btnSentReports))
        {
            mFragmentManager.popBackStack();
            setVisitsListContent(reportsList);
        }*/
    }

    @Override
    public void onCtrlBtnsSopralluogoClicked(int btnId)
    {
        if (currentSelIndex == -1)
        {
            return;
        }

        if (btnId == R.id.btnSopralluogoReturn)
        {
            onBackPressed();
        }

        if (btnId == R.id.btnSopralluogoInfo)
        {
            mFragmentManager.popBackStack();

            if (!setDateTimeFragment.isAdded())
            {
                Bundle args = new Bundle();
                args.putInt("selectedIndex", currentSelIndex);
                ctlInfo.setArguments(args);

                setVisitsListContent(setDateTimeFragment);
            }
        }

        if (btnId == R.id.btnFillReport)
        {
            VisitItem visitItem = visitItems.get(currentSelIndex);
            ProductData productData = visitItem.getProductData();
            String productType = productData.getProductType();

            mFragmentManager.popBackStack();

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

        if (btnId == R.id.btnAddPhotos)
        {
            mFragmentManager.popBackStack();

            if (!photoGalleryGridFragment.isAdded())
            {
                realm.beginTransaction();

                VisitItem visitItem = visitItems.get(currentSelIndex);
                GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
                int idSopralluogo = geaSopralluogo.getId_sopralluogo();
                ReportStates reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                        .equalTo("id_sopralluogo", idSopralluogo).findFirst();

                realm.commitTransaction();

                if(reportStates!=null)
                {
                    int id_sopralluogo = reportStates.getId_sopralluogo();

                    Bundle args = frag.getArguments() != null ? frag.getArguments() : new Bundle();
                    args.putInt("id_sopralluogo", id_sopralluogo);
                    photoGalleryGridFragment.setArguments(args);

                    //setVisitsListContent(photoGalleryGridFragment);

                    if (!photoGalleryGridFragment.isAdded())
                    {
                        FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();
                        vFragmentTransaction.add(R.id.photosFragContainer, photoGalleryGridFragment);
                        vFragmentTransaction.addToBackStack(photoGalleryGridFragment.getTag());
                        vFragmentTransaction.commit();

/*                        FrameLayout flphotosFragContainer = (FrameLayout) findViewById(R.id.photosFragContainer);
                        flphotosFragContainer.setVisibility(View.VISIBLE);*/

                    }
                }
            }
        }

        if (btnId == R.id.btnSendReport)
        {
            mFragmentManager.popBackStack();

            if (!sendReportFragment.isAdded())
            {
                Bundle args = new Bundle();
                args.putInt("selectedIndex", currentSelIndex);
                sendReportFragment.setArguments(args);

                setVisitsListContent(sendReportFragment);
            }
        }
    }

    private void setVisitsListContent(Fragment fragment)
    {
        if (!fragment.isAdded())
        {
            FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();
            vFragmentTransaction.add(R.id.innerFragContainer, fragment);
            vFragmentTransaction.addToBackStack(fragment.getTag());
            vFragmentTransaction.commit();
        }
        //timeNotSetItemsOnly = false;
    }

    @Override
    public void OnListItemSelected(int itemIndex, boolean dateTimeHasSet)
    {
        currentSelIndex = itemIndex;

        mFragmentManager.popBackStack();

        if (!ctrlBtnsSopralluogo.isAdded())
        {
            FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();
            vFragmentTransaction.replace(R.id.headerFragContainer, ctrlBtnsSopralluogo);
            vFragmentTransaction.commit();
        }
        mFragmentManager.executePendingTransactions();
        
            if (!setDateTimeFragment.isAdded())
            {
                Bundle args = new Bundle();
                args.putInt("selectedIndex", itemIndex);
                setDateTimeFragment.setArguments(args);

                //setVisitsListContent(setDateTimeFragment);
                ctrlBtnsSopralluogo.setCheckedBtnId(R.id.btnSopralluogoInfo);
            }
        //}
    }

    @Override
    public void onDateTimeSetReturned(int itemIndex)
    {
        currentSelIndex = itemIndex;
        listVisitsIsObsolete = true;
        ctrlBtnsSopralluogo.setCheckedBtnId(R.id.btnSopralluogoInfo);
    }

    @Override
    public void onDetailedReportReturned()
    {
        onCtrlBtnsBottomClicked(R.id.btnReportsReturn);
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
        mFragmentTransaction.hide(ctrlBtnsBottom);
        mFragmentTransaction.show(ctrlBtnsReportDetailed);
        mFragmentTransaction.commit();

        mFragmentManager.popBackStackImmediate();

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

        ctrlBtnsSopralluogo.setCheckedBtnId(R.id.btnFillReport);
    }

    @Override
    public void onNotificationReportReturned(int mode)
    {
        showSelectedVisitsList(mode);

/*        if (view.getId() == R.id.btnNotifTimeNotSetVisits)
        {
            mFragmentManager.popBackStack();

            fragListVisitsFree = new FragListVisitsFree();

            if (!fragListVisitsFree.isAdded())
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
*//*                    if (which == 1)
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Password recover", true);
                        startActivity(intent);
                        finish();
                    }*//*

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

            builder.setView(layout);
            alert = builder.create();
            alert.show();
        }*/
    }

    private void showSelectedVisitsList(int mode)
    {
        //mFragmentManager.popBackStack();
        mSettings.edit().putInt("listVisitsFilterMode", mode).apply();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        if(mode == LIST_VISITS_MODE_ALL)
        {

            mFragmentTransaction.show(fragListVisitsFree);
            mFragmentTransaction.show(fragListVisitsToday);
            mFragmentTransaction.show(fragListVisitsOther);
        }

        if(mode == LIST_VISITS_MODE_TODAY)
        {
            mFragmentTransaction.hide(fragListVisitsFree);
            mFragmentTransaction.show(fragListVisitsToday);
            mFragmentTransaction.hide(fragListVisitsOther);
        }

        if(mode == LIST_VISITS_MODE_FREE)
        {
            mFragmentTransaction.show(fragListVisitsFree);
            mFragmentTransaction.hide(fragListVisitsToday);
            mFragmentTransaction.hide(fragListVisitsOther);
        }
        mFragmentTransaction.commit();
    }

    @Override
    public void OnComingListItemSelected(int itemIndex)
    {
        currentSelIndex = itemIndex;
        ctrlBtnsSopralluogo.setCheckedBtnId(R.id.btnSopralluogoInfo);
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
        ctrlBtnsSopralluogo.setCheckedBtnId(R.id.btnFillReport);
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

                    //mFragmentManager.popBackStack();

                    onCtrlBtnsBottomClicked(R.id.btnVisits);

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
