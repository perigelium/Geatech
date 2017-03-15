package ru.alexangan.developer.geatech.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Fragments.CTLinfoFragment;
import ru.alexangan.developer.geatech.Fragments.CaldaiaReportFragment;
import ru.alexangan.developer.geatech.Fragments.STermodinamicoReportFragment;
import ru.alexangan.developer.geatech.Fragments.ClimatizzazioneReportFragment;
import ru.alexangan.developer.geatech.Fragments.ComingListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnReportDetailed;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnsFragment1;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnsFragment2;
import ru.alexangan.developer.geatech.Fragments.DomoticaReportFragment;
import ru.alexangan.developer.geatech.Fragments.EmptyReportFragment;
import ru.alexangan.developer.geatech.Fragments.FotovoltaicoReportFragment;
import ru.alexangan.developer.geatech.Fragments.InWorkListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.ListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.NotSentListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.NotificationBarFragment;
import ru.alexangan.developer.geatech.Fragments.PhotoGalleryGridFragment;
import ru.alexangan.developer.geatech.Fragments.PompaDiCaloreReportFragment;
import ru.alexangan.developer.geatech.Fragments.ReportSentDetailedFragment;
import ru.alexangan.developer.geatech.Fragments.ReportsListFragment;
import ru.alexangan.developer.geatech.Fragments.SendReportFragment;
import ru.alexangan.developer.geatech.Fragments.SetDateTimeFragment;
import ru.alexangan.developer.geatech.Fragments.StorageReportFragment;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.JSON_to_model;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.GET_VISITS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.inVisitItems;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class MainActivity extends Activity implements Communicator, Callback
{
    private FragmentManager mFragmentManager;
    SwipeDetector swipeDetector;

    CtrlBtnsFragment1 ctrlBtnsFragment1;
    CtrlBtnsFragment2 ctrlBtnsFragment2;
    SetDateTimeFragment dateTimeSetFragment;
    ListVisitsFragment listVisits;
    InWorkListVisitsFragment inWorkListVisits;
    ComingListVisitsFragment comingListVisits;
    NotSentListVisitsFragment notSentListVisits;
    ReportsListFragment reportsList;
    private ProgressDialog requestServerDialog;

    @Override
    protected void onStart()
    {
        super.onStart();
    }

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
    private Call callVisits;
    NetworkUtils networkUtils;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
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
            Toast.makeText(this, "Elenco sopralluoghi e vuoto", Toast.LENGTH_LONG).show();

            this.finish();
        }

/*        for (VisitItem realmVisitItem : visitItems)
        {
            int idSopralluogo = realmVisitItem.getGeaSopralluogo().getId_sopralluogo();

            Boolean addressAndProductPresent = false;
            realm.beginTransaction();
            RealmResults<ReportStates> reportStatesList = realm.where(ReportStates.class).findAll();
            realm.commitTransaction();

            for (ReportStates reportStates : reportStatesList)
            {
                realm.beginTransaction();
                int idSopralluogoRep = reportStates.getId_sopralluogo();

                if (idSopralluogo == idSopralluogoRep)
                {
                    addressAndProductPresent = true;
                    reportStates.setVisitId(realmVisitItem.getId());
                }
                realm.commitTransaction();
            }

            // possible reasonable to initialize each ReportStates object on first call.
            if (addressAndProductPresent == false)
            {
                realm.beginTransaction();
                ReportStates newReportStates = new ReportStates(company_id, selectedTech.getId(), reportStatesList.size(), realmVisitItem.getId());
                newReportStates.setId_sopralluogo(idSopralluogo);
                realm.copyToRealm(newReportStates);
                realm.commitTransaction();
            }
        }*/

        swipeDetector = new SwipeDetector();

        ctrlBtnsFragment1 = new CtrlBtnsFragment1();
        ctrlBtnsFragment2 = new CtrlBtnsFragment2();
        dateTimeSetFragment = new SetDateTimeFragment();
        listVisits = new ListVisitsFragment();
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

        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment1);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment2);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsReportDetailed);

        mFragmentTransaction.addToBackStack("listVisits");

        Bundle args = new Bundle();
        args.putBoolean("withNoSopralluogoTime", false);
        listVisits.setArguments(args);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, listVisits);

        mFragmentTransaction.hide(ctrlBtnsReportDetailed);
        mFragmentTransaction.hide(ctrlBtnsFragment2);
        mFragmentTransaction.show(ctrlBtnsFragment1);

        //mFragmentManager.executePendingTransactions();

        mFragmentTransaction.commit();

        networkUtils = new NetworkUtils();

        requestServerDialog = new ProgressDialog(this);
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage("Download dei dati, si prega di attendere un po'...");
        requestServerDialog.setIndeterminate(true);
    }

    @Override
    public void onBackPressed()
    {
        if (!listVisits.isAdded())
        {
/*            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(ctrlBtnsReportDetailed);
            mFragmentTransaction.hide(ctrlBtnsFragment2);
            mFragmentTransaction.show(ctrlBtnsFragment1);
            mFragmentTransaction.commit();*/

            removeAllLists();

            ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);

/*            FragmentTransaction mFragmentTransaction1 = mFragmentManager.beginTransaction();
            mFragmentTransaction1.show(ctrlBtnsFragment1);
            mFragmentTransaction1.commit();*/

            //removeAllLists();

            //currentSelIndex = -1;

/*            listVisits = new ListVisitsFragment();

            Bundle args = new Bundle();
            args.putBoolean("timeNotSetItemsOnly", false);
            listVisits.setArguments(args);*/

            //setVisitsListContent(listVisits);

        } else
        {
            //super.onBackPressed();
            this.finish();
        }
    }

    @Override
    public void onCtrlButtons1Clicked(View view)
    {
/*        if (!view.isShown() || !ctrlBtnChkChanged)
        {
            return;
        }*/

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.hide(ctrlBtnsFragment2);
        fragmentTransaction.show(ctrlBtnsFragment1);
        fragmentTransaction.commit();
        currentSelIndex = -1;

        if (view == findViewById(R.id.btnVisits))
        {
            removeAllLists();

            listVisits = new ListVisitsFragment();

            Bundle args = new Bundle();
            args.putBoolean("visitTimeNotSetOnly", false);
            listVisits.setArguments(args);

            setVisitsListContent(listVisits);
        }

        if (view == findViewById(R.id.btnReportsReturn))
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(ctrlBtnsReportDetailed);
            mFragmentTransaction.commit();

            removeAllLists();
            setVisitsListContent(reportsList);
        }

        if (view == findViewById(R.id.btnComingVisits))
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

        if (view == findViewById(R.id.btnSentReports))
        {
            removeAllLists();
            setVisitsListContent(reportsList);
        }
    }

    @Override
    public void onCtrlButtons2Clicked(View view)
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.hide(ctrlBtnsFragment1);
        mFragmentTransaction.show(ctrlBtnsFragment2);
        mFragmentTransaction.commit();

        if (currentSelIndex == -1)
        {
            return;
        }

        if (view == findViewById(R.id.btnReturn))
        {
/*            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.hide(ctrlBtnsFragment2);
            fragmentTransaction.show(ctrlBtnsFragment1);
            fragmentTransaction.commit();*/

            removeAllLists();

            ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);

/*            removeAllLists();
            listVisits = new ListVisitsFragment();

            Bundle args = new Bundle();
            args.putBoolean("visitTimeNotSetOnly", false);
            listVisits.setArguments(args);

            setVisitsListContent(listVisits);*/
        }

        if (view == findViewById(R.id.btnFillReport))
        {
            VisitItem visitItem = visitItems.get(currentSelIndex);
            ProductData productData = visitItem.getProductData();
            String productType = productData.getProductType();

            removeAllLists();

            frag = null;
            frag = assignFragmentModel(productType);

            if (frag != null)
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

            Bundle args = new Bundle();
            args.putInt("selectedIndex", currentSelIndex);
            photoGalleryGridFragment.setArguments(args);

            setVisitsListContent(photoGalleryGridFragment);
        }

        if (view == findViewById(R.id.btnInfo))
        {
            removeAllLists();

            Bundle args = new Bundle();
            args.putInt("selectedIndex", currentSelIndex);
            ctlInfo.setArguments(args);

            setVisitsListContent(ctlInfo);
        }

        if (view == findViewById(R.id.btnSendReport))
        {
            removeAllLists();

            Bundle args = new Bundle();
            args.putInt("selectedIndex", currentSelIndex);
            sendReportFragment.setArguments(args);

            setVisitsListContent(sendReportFragment);
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

        if (listVisits.isAdded())
        {
            mFragmentTransaction.remove(listVisits);
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
    }

    @Override
    public void OnListItemSelected(int itemIndex, Boolean dateTimeHasSet)
    {
        //removeAllLists();

        currentSelIndex = itemIndex;

        if (dateTimeHasSet) // if visit day is empty, show set datetime fragment, otherwise show CTL info.
        {
/*            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(ctrlBtnsFragment1);
            mFragmentTransaction.show(ctrlBtnsFragment2);
            mFragmentTransaction.commit();*/

            ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);
        } else
        {
            removeAllLists();

            if (!dateTimeSetFragment.isAdded())
            {
                Bundle args = new Bundle();
                args.putInt("selectedIndex", itemIndex);
                dateTimeSetFragment.setArguments(args);
            }

            setVisitsListContent(dateTimeSetFragment);
        }
    }

    @Override
    public void onDateTimeSetReturned(Boolean mDatetimeAlreadySet)
    {
        if (!mDatetimeAlreadySet && networkUtils.isNetworkAvailable(this))
        {
            requestServerDialog.show();

            callVisits = networkUtils.getData(this, GET_VISITS_URL_SUFFIX, tokenStr);
        } else
        {
            //removeAllLists();

/*            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(ctrlBtnsFragment2);
            mFragmentTransaction.show(ctrlBtnsFragment1);
            mFragmentTransaction.commit();*/

            ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);

/*            listVisits = new ListVisitsFragment();

            Bundle args = new Bundle();
            args.putBoolean("visitTimeNotSetOnly", false);
            listVisits.setArguments(args);

            setVisitsListContent(listVisits);*/
        }
    }

    @Override
    public void onDetailedReportReturned()
    {
        onCtrlButtons1Clicked(findViewById(R.id.btnReportsReturn));
    }

    @Override
    public void onSendReportReturned()
    {
        onCtrlButtons2Clicked(findViewById(R.id.btnSendReport));
    }

    @Override
    public void OnReportListItemSelected(int itemIndex)
    {
        currentSelIndex = itemIndex;

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.hide(ctrlBtnsFragment2);
        mFragmentTransaction.hide(ctrlBtnsFragment1);
        mFragmentTransaction.show(ctrlBtnsReportDetailed);
        mFragmentTransaction.commit();

        removeAllLists();

        Bundle args = reportDetailedFragment.getArguments() != null ? reportDetailedFragment.getArguments() : new Bundle();

        args.putInt("selectedIndex", itemIndex);
        reportDetailedFragment.setArguments(args);

        setVisitsListContent(reportDetailedFragment);

        //ctrlBtnsFragment1.setCheckedBtnId(R.id.btnSentReports);
    }

    @Override
    public void OnInWorkListItemSelected(int itemIndex)
    {
        currentSelIndex = itemIndex;

/*        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.hide(ctrlBtnsFragment1);
        mFragmentTransaction.show(ctrlBtnsFragment2);
        mFragmentTransaction.commit();*/

/*        ctrlBtnChkChanged = false;
        ctrlBtnsFragment1.setCheckedBtnId(R.id.btnFillReport);
        ctrlBtnsFragment1.onHiddenChanged(true);
        ctrlBtnChkChanged = true;*/

/*        VisitItem visitItem = visitItems.get(itemIndex);
        ProductData productData = visitItem.getProductData();
        String productType = productData.getProductType();

        removeAllLists();

        Fragment frag = assignFragmentModel(productType);

        Bundle args = frag.getArguments() != null ? frag.getArguments() : new Bundle();

        args.putInt("selectedIndex", itemIndex);
        frag.setArguments(args);

        setVisitsListContent(frag);*/

        ctrlBtnsFragment2.setCheckedBtnId(R.id.btnFillReport);
    }

    @Override
    public void onNotificationReportReturned(View view)
    {
        if (view.getId() == R.id.btnNotifTimeNotSetVisits)
        {
            //removeAllLists();

            ctrlBtnsFragment1.setCheckedBtnId(R.id.btnComingVisits);

/*            listVisits = new ListVisitsFragment();

            Bundle args = new Bundle();
            args.putBoolean("timeNotSetItemsOnly", true);
            listVisits.setArguments(args);

            setVisitsListContent(listVisits);*/
        }

        if (view.getId() == R.id.btnNotifUrgentReports)
        {
            Toast.makeText(this, "Nessun trovato delle urgente rapporti", Toast.LENGTH_LONG).show();
        }

        if (view.getId() == R.id.btnAppSettings)
        {
            String[] listItemsArray = {"Esci", "Cambia\npassword"};

            //ContextThemeWrapper themedContext = new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
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
                    if (which == 1)  // exit app
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Password recover", true);
                        startActivity(intent);
                        finish();
                    }

                    if (which == 0) // password recover
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Exit app", true);
                        startActivity(intent);
                        finish();
                    }

                }
            });

            //ListView listView = (ListView) v.findViewById(R.id.notifBarlist);

            //final ArrayAdapter <String> listAdapter = new ArrayAdapter<>(this, R.id.dialogTextView, listItemsArray);
            //listView.setAdapter(listAdapter);
            //listView.setBackgroundColor(Color.GRAY);

/*            builder.setItems(listItemsArray, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
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
            AlertDialog alert = builder.create();
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
    public void OnListItemSwiped(int itemIndex, Boolean dateTimeHasSet)
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
        Fragment frag;

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
            showToastMessage("Visite data non ricevuto");

            requestServerDialog.dismiss();
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callVisits)
        {
            final String visitsJSONData = response.body().string();

            Log.d("DEBUG", visitsJSONData);

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

                    ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);

/*                    ctrlBtnChkChanged = false;
                    ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);
                    ctrlBtnsFragment1.onHiddenChanged(true);
                    ctrlBtnChkChanged = true;*/

/*                    listVisits = new ListVisitsFragment();

                    Bundle args = new Bundle();
                    args.putBoolean("visitTimeNotSetOnly", false);
                    listVisits.setArguments(args);

                    setVisitsListContent(listVisits);*/
                }
            });
        }

        requestServerDialog.dismiss();
    }
}
