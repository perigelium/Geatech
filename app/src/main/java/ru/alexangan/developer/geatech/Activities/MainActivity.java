package ru.alexangan.developer.geatech.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Fragments.CaldaiaReportFragment;
import ru.alexangan.developer.geatech.Fragments.ClimatizzazioneReportFragment;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnsBottom;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnsSopralluogo;
import ru.alexangan.developer.geatech.Fragments.DomoticaReportFragment;
import ru.alexangan.developer.geatech.Fragments.EmptyReportFragment;
import ru.alexangan.developer.geatech.Fragments.FotovoltaicoReportFragment;
import ru.alexangan.developer.geatech.Fragments.FragListInWorkVisits;
import ru.alexangan.developer.geatech.Fragments.FragListReportsNotSent;
import ru.alexangan.developer.geatech.Fragments.FragListReportsReminded;
import ru.alexangan.developer.geatech.Fragments.FragListReportsSent;
import ru.alexangan.developer.geatech.Fragments.FragListVisitsFree;
import ru.alexangan.developer.geatech.Fragments.FragListVisitsOther;
import ru.alexangan.developer.geatech.Fragments.FragListVisitsOverdue;
import ru.alexangan.developer.geatech.Fragments.FragListVisitsReminded;
import ru.alexangan.developer.geatech.Fragments.FragListVisitsToday;
import ru.alexangan.developer.geatech.Fragments.NotificationBarFragment;
import ru.alexangan.developer.geatech.Fragments.PhotoGalleryGridFragment;
import ru.alexangan.developer.geatech.Fragments.PompaDiCaloreReportFragment;
import ru.alexangan.developer.geatech.Fragments.ReportSentDetailedFragment;
import ru.alexangan.developer.geatech.Fragments.STermodinamicoReportFragment;
import ru.alexangan.developer.geatech.Fragments.SendReportFragment;
import ru.alexangan.developer.geatech.Fragments.SetDateTimeFragment;
import ru.alexangan.developer.geatech.Fragments.SettingsFragment;
import ru.alexangan.developer.geatech.Fragments.StorageReportFragment;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ScrollViewEx;
import ru.alexangan.developer.geatech.Interfaces.ScrollViewListener;
import ru.alexangan.developer.geatech.Models.GeaItemModelliRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSezioneModelliRapporto;
import ru.alexangan.developer.geatech.Models.GlobalConstants;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
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
import static ru.alexangan.developer.geatech.Models.GlobalConstants.LIST_VISITS_MODE_MY;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.inVisitItems;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitsListIsObsolete;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;
import static ru.alexangan.developer.geatech.R.id.llInnerFragContainer;

public class MainActivity extends Activity implements Communicator, Callback, ScrollViewListener
{
    Realm realm;
    private FragmentManager mFragmentManager;
    SwipeDetector swipeDetector;

    CtrlBtnsBottom ctrlBtnsBottom;
    CtrlBtnsSopralluogo ctrlBtnsSopralluogo;
    SetDateTimeFragment setDateTimeFragment;
    SettingsFragment settingsFragment;
    FragListVisitsFree fragListVisitsFree;
    FragListVisitsToday fragListVisitsToday;
    FragListVisitsOther fragListVisitsOther;
    FragListVisitsOverdue fragListVisitsOverdue;
    FragListInWorkVisits fragListInWorkVisits;
    FragListVisitsReminded fragListVisitsReminded;
    FragListReportsReminded fragListReportsReminded;
    FragListReportsNotSent fragListReportsNotSent;
    FragListReportsSent fragListReportsSent;

    private ProgressDialog requestServerDialog;
    Handler handler;
    Runnable runnable;

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
    int currentVisitId;
    boolean ctrlBtnChkChanged;
    private Call callVisits, callModels;
    NetworkUtils networkUtils;
    private boolean firstStart;
    private int curSelBottomBtnId;

    ScrollViewEx scrvInnerFragContainer;

    @Override
    protected void onPause()
    {
        super.onPause();

        if (handler != null && runnable != null)
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

        if (firstStart)
        {
            ctrlBtnsBottom.setCheckedBtnId(R.id.btnVisits);
            firstStart = false;
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
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.work_window);

        scrvInnerFragContainer = (ScrollViewEx) findViewById(R.id.svInnerFragContainer);
        scrvInnerFragContainer.setScrollViewListener(this);

        setEdgeBounceEffect(scrvInnerFragContainer, Color.parseColor("#ffcccccc"));

        scrvInnerFragContainer.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                return false;
            }
        });

        currentVisitId = -1;
        curSelBottomBtnId = 0;
        ctrlBtnChkChanged = true;
        firstStart = true;

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
        fragListVisitsOverdue = new FragListVisitsOverdue();
        fragListInWorkVisits = new FragListInWorkVisits();
        fragListVisitsReminded = new FragListVisitsReminded();
        fragListReportsReminded = new FragListReportsReminded();
        fragListReportsNotSent = new FragListReportsNotSent();
        fragListReportsSent = new FragListReportsSent();

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
        mFragmentTransaction.add(R.id.headerFragContainer, notificationBarFragment);
        mFragmentTransaction.commit();

        //mFragmentManager.executePendingTransactions();

        networkUtils = new NetworkUtils();

        requestServerDialog = new ProgressDialog(this);
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.DownloadingDataPleaseWait));
        requestServerDialog.setIndeterminate(true);

        GlobalConstants.visitsListIsObsolete = false;
        GlobalConstants.reportsListIsObsolete = false;
        GlobalConstants.reminderListIsObsolete = false;
        mSettings.edit().putInt("listVisitsFilterMode", LIST_VISITS_MODE_ALL).apply();

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
        if (curSelBottomBtnId != 0)
        {
            ctrlBtnsBottom.setCheckedBtnId(curSelBottomBtnId);
            curSelBottomBtnId = 0;
        } else if (!fragListVisitsToday.isAdded() && !fragListVisitsOther.isAdded()
                && !fragListVisitsFree.isAdded() && !fragListVisitsOverdue.isAdded())
        {
            Button btn = (Button) findViewById(R.id.btnVisits);
            btn.setSelected(false);
            ctrlBtnsBottom.setCheckedBtnId(R.id.btnVisits);
        } else
        {
            //super.onBackPressed();
            this.finish();
        }
    }

    @Override
    public void onCtrlBtnsBottomClicked(int btnId)
    {
        currentVisitId = -1;
        mFragmentManager.popBackStackImmediate();

        if (!notificationBarFragment.isAdded())
        {
            FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();
            vFragmentTransaction.replace(R.id.headerFragContainer, notificationBarFragment);
            vFragmentTransaction.commit();

            mFragmentManager.executePendingTransactions();
        }

        if (btnId == R.id.btnVisits)
        {
            if (visitsListIsObsolete)
            {
                refreshVisitsList();
            } else
            {
                int mode = mSettings.getInt("listVisitsFilterMode", 0);
                showSelectedVisitsList(mode);
                scrvInnerFragContainer.getParent().requestChildFocus(scrvInnerFragContainer, scrvInnerFragContainer);
            }
        } else
        {
            notificationBarFragment.setView(R.string.NullString, View.GONE, View.GONE);
        }

        if (btnId == R.id.btnInWorkVisits)
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            if (!fragListInWorkVisits.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListInWorkVisits);
                mFragmentTransaction.addToBackStack(fragListInWorkVisits.getTag());
            }
            mFragmentTransaction.commit();

            notificationBarFragment.setView(R.string.InWorkCompilations, View.GONE, View.GONE);
        }

        if (btnId == R.id.btnNotifications)
        {
            showNotifVisitsLists();
            scrvInnerFragContainer.getParent().requestChildFocus(scrvInnerFragContainer, scrvInnerFragContainer);
        }

        if (btnId == R.id.btnCompletedReports)
        {
            showCompletedReportsLists();
            scrvInnerFragContainer.getParent().requestChildFocus(scrvInnerFragContainer, scrvInnerFragContainer);
        }

        if (btnId == R.id.btnAppSettings)
        {
            setVisitsListContent(settingsFragment);

            notificationBarFragment.setView(R.string.Settings, View.GONE, View.GONE);
        }
    }

    private void showCompletedReportsLists()
    {
        if (GlobalConstants.reportsListIsObsolete && NetworkUtils.isNetworkAvailable(this))
        {
            refreshVisitsList();
        } else
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            if (!fragListReportsNotSent.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListReportsNotSent);
            }

            if (!fragListReportsSent.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListReportsSent);
            }
            mFragmentTransaction.addToBackStack(fragListReportsSent.getTag());
            mFragmentTransaction.commit();

            notificationBarFragment.setView(R.string.CompletedReports, View.GONE, View.GONE);
        }
    }

    private void showNotifVisitsLists()
    {
        if (GlobalConstants.reminderListIsObsolete && NetworkUtils.isNetworkAvailable(this))
        {
            refreshVisitsList();
        } else
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            if (!fragListVisitsReminded.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListVisitsReminded);
            }

            if (!fragListReportsReminded.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListReportsReminded);
            }
            mFragmentTransaction.addToBackStack(fragListReportsReminded.getTag());
            mFragmentTransaction.commit();

            notificationBarFragment.setView(R.string.Notifications, View.GONE, View.GONE);
        }
    }

    @Override
    public void onCtrlBtnsSopralluogoClicked(int btnId)
    {
        if (currentVisitId == -1)
        {
            return;
        }

        if (btnId == R.id.btnSopralluogoReturn)
        {
            onBackPressed();
            return;
        }

        curSelBottomBtnId = ctrlBtnsBottom.getSelectedButtonId();
        ctrlBtnsBottom.unselectAllButtons();

        if (btnId == R.id.btnSopralluogoInfo)
        {
            mFragmentManager.popBackStackImmediate();

            if (!setDateTimeFragment.isAdded())
            {
                Bundle args = new Bundle();
                args.putInt("selectedVisitId", currentVisitId);
                setDateTimeFragment.setArguments(args);

                setVisitsListContent(setDateTimeFragment);
            }
        }

        VisitItem visitItem = visitItems.get(currentVisitId);
        int tech_id = visitItem.getGeaSopralluogo().getId_tecnico();
        boolean ownReportMode = tech_id == GlobalConstants.selectedTech.getId();
        ProductData productData = visitItem.getProductData();
        int product_id = productData.getId();

        if (ownReportMode && btnId == R.id.btnFillReport)
        {
            mFragmentManager.popBackStack();

            frag = null;
            String productType = productData.getProductType();
            frag = assignFragmentModel(productType);

            if (!frag.isAdded())
            {
                Bundle args = frag.getArguments() != null ? frag.getArguments() : new Bundle();
                args.putInt("selectedVisitId", currentVisitId);

                frag.setArguments(args);

                setVisitsListContent(frag);
            }
        }

        if (ownReportMode && btnId == R.id.btnAddPhotos)
        {
            mFragmentManager.popBackStack();

            if (!photoGalleryGridFragment.isAdded())
            {
                realm.beginTransaction();

                int idSopralluogo = visitItem.getGeaSopralluogo().getId_sopralluogo();
                int id_rapporto_sopralluogo = visitItem.getGeaRapporto().getId_rapporto_sopralluogo();

                ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id)
                        .equalTo("tech_id", selectedTech.getId())
                        .equalTo("id_sopralluogo", idSopralluogo)
                        .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findFirst();

                realm.commitTransaction();

                if (reportItem != null)
                {
                    Bundle args = frag.getArguments() != null ? frag.getArguments() : new Bundle();
                    args.putInt("id_sopralluogo", idSopralluogo);
                    photoGalleryGridFragment.setArguments(args);

                    if (!photoGalleryGridFragment.isAdded())
                    {
                        FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();
                        vFragmentTransaction.add(R.id.photosFragContainer, photoGalleryGridFragment);
                        vFragmentTransaction.addToBackStack(photoGalleryGridFragment.getTag());
                        vFragmentTransaction.commit();
                    }
                }
                else
                {
                    showToastMessage("This report have not been initialized.");
                }
            }
        }

        if (ownReportMode && btnId == R.id.btnSendReport)
        {
            mFragmentManager.popBackStack();

            if (!sendReportFragment.isAdded())
            {
                Bundle args = new Bundle();
                args.putInt("selectedVisitId", currentVisitId);
                sendReportFragment.setArguments(args);

                setVisitsListContent(sendReportFragment);
            }
        }

        if(btnId != R.id.btnSopralluogoInfo &&  ! ownReportMode)
        {
            ctrlBtnsSopralluogo.setCheckedBtnId(R.id.btnSopralluogoInfo);

            showToastMessage(getString(R.string.YouCanEditYourOwnReportsOnly));
        }
    }

    private void setVisitsListContent(Fragment fragment)
    {
        if (!fragment.isAdded())
        {
            mFragmentManager.popBackStack();
            FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();
            vFragmentTransaction.add(llInnerFragContainer, fragment);
            vFragmentTransaction.addToBackStack(fragment.getTag());
            vFragmentTransaction.commit();
        }
    }

    @Override
    public void OnVisitListItemSelected(int itemId, boolean dateTimeHasSet)
    {
        currentVisitId = itemId;

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
            args.putInt("selectedVisitId", itemId);
            setDateTimeFragment.setArguments(args);

            ctrlBtnsSopralluogo.setCheckedBtnId(R.id.btnSopralluogoInfo);
        }
    }

    @Override
    public void onDateTimeSetReturned(int itemIndex)
    {
        currentVisitId = itemIndex;
        refreshVisitsList();
        ctrlBtnsSopralluogo.setCheckedBtnId(R.id.btnSopralluogoInfo);
    }

    @Override
    public void onCompilationHorisontalSwipeReturned(int btnId, boolean swipeDirection)
    {
        ctrlBtnsSopralluogo.selectNextButton(btnId, swipeDirection);
    }

    @Override
    public void onSendReportReturned(int id_rapporto_sopralluogo)
    {
        OnReportListItemSelected(id_rapporto_sopralluogo);
    }

    @Override
    public void OnReportListItemSelected(int id_rapporto_sopralluogo)
    {
        mFragmentManager.popBackStackImmediate();

        if (!notificationBarFragment.isAdded())
        {
            FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();
            vFragmentTransaction.replace(R.id.headerFragContainer, notificationBarFragment);
            vFragmentTransaction.commit();

            mFragmentManager.executePendingTransactions();
        }

        if (!reportDetailedFragment.isAdded())
        {
            notificationBarFragment.setView(R.string.ReportDetailed, View.GONE, View.GONE);

            curSelBottomBtnId = ctrlBtnsBottom.getSelectedButtonId();
            ctrlBtnsBottom.unselectAllButtons();

            Bundle args =
                    reportDetailedFragment.getArguments() != null ? reportDetailedFragment.getArguments() : new Bundle();

            args.putInt("id_rapporto_sopralluogo", id_rapporto_sopralluogo);
            reportDetailedFragment.setArguments(args);

            setVisitsListContent(reportDetailedFragment);
        }
    }

    @Override
    public void onNotificationReportReturned(int mode)
    {
        if (!firstStart)
        {
            mFragmentManager.popBackStackImmediate();
            showSelectedVisitsList(mode);
        }
    }

    private void showSelectedVisitsList(int mode)
    {
        mFragmentManager.popBackStackImmediate();

        notificationBarFragment.setView(R.string.ComingVisitsList, View.VISIBLE, View.VISIBLE);

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        if (mode == LIST_VISITS_MODE_ALL)
        {
            mSettings.edit().putBoolean("ownVisitsOnly", false).apply();

            if (!fragListVisitsOverdue.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListVisitsOverdue);
            }

            if (!fragListVisitsFree.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListVisitsFree);
            }

            if (!fragListVisitsToday.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListVisitsToday);
            }

            if (!fragListVisitsOther.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListVisitsOther);
            }
        }

        if (mode == LIST_VISITS_MODE_MY)
        {
            mSettings.edit().putBoolean("ownVisitsOnly", true).apply();

            if (!fragListVisitsOverdue.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListVisitsOverdue);
            }

            if (!fragListVisitsToday.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListVisitsToday);
            }

            if (!fragListVisitsOther.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListVisitsOther);
            }

            notificationBarFragment.setView(R.string.MyVisitsList, View.VISIBLE, View.VISIBLE);
        }

        if (mode == LIST_VISITS_MODE_FREE)
        {
            if (!fragListVisitsFree.isAdded())
            {
                mFragmentTransaction.add(llInnerFragContainer, fragListVisitsFree);
            }

            notificationBarFragment.setView(R.string.FreeVisits, View.VISIBLE, View.VISIBLE);
        }
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    @Override
    public void onLogoutCommand()
    {
        logout();
    }

    @Override
    public void OnVisitListItemSwiped(int itemIndex, boolean dateTimeHasSet)
    {
        if (!dateTimeHasSet)
        {
            OnVisitListItemSelected(itemIndex, false);
        } else
        {
            OnVisitListItemSelected(itemIndex, true);
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callVisits)
        {
            GlobalConstants.visitsListIsObsolete = false;
            GlobalConstants.reportsListIsObsolete = false;
            GlobalConstants.reminderListIsObsolete = false;

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

                    if(GlobalConstants.visitsListIsObsolete)
                    {
                        GlobalConstants.visitsListIsObsolete = false;
                        onCtrlBtnsBottomClicked(R.id.btnVisits);
                    }

                    if(GlobalConstants.reportsListIsObsolete)
                    {
                        GlobalConstants.reportsListIsObsolete = false;
                        onCtrlBtnsBottomClicked(R.id.btnCompletedReports);
                    }

                    if(GlobalConstants.reminderListIsObsolete)
                    {
                        GlobalConstants.reminderListIsObsolete = false;
                        onCtrlBtnsBottomClicked(R.id.btnNotifications);
                    }

                    requestServerDialog.dismiss();
                }
            });
        }

        if (call == callModels)
        {
            String modelsJSONData = response.body().string();

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
                            str_gea_sezioni_modelli = String.valueOf(Html.fromHtml(str_gea_sezioni_modelli, Html.FROM_HTML_MODE_LEGACY));
                            str_gea_items_modelli = String.valueOf(Html.fromHtml(str_gea_items_modelli, Html.FROM_HTML_MODE_LEGACY));
                        } else
                        {
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
        if(NetworkUtils.isNetworkAvailable(this))
        {
            requestServerDialog.show();

            handler.postDelayed(runnable, 30000);

            callVisits = networkUtils.getData(this, GET_VISITS_URL_SUFFIX, tokenStr);
        }
        else
        {
            showToastMessage(getString(R.string.CheckInternetConnection));
        }
    }

    @Override
    public void refreshGeaModels()
    {
        requestServerDialog.show();

        handler.postDelayed(runnable, 30000);

        callModels = networkUtils.getData(this, GET_MODELS_URL_SUFFIX, tokenStr);

        visitsListIsObsolete = false;
    }

    @Override
    public void hideHeaderAndFooter()
    {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.hide(ctrlBtnsBottom);
        fragmentTransaction.hide(ctrlBtnsSopralluogo);
        fragmentTransaction.commit();
    }

    @Override
    public void showHeaderAndFooter()
    {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.show(ctrlBtnsBottom);
        fragmentTransaction.show(ctrlBtnsSopralluogo);
        fragmentTransaction.commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setEdgeBounceEffect(View scrollableView, int color) {
        final String[] edgeGlows = {"mEdgeGlowTop", "mEdgeGlowBottom", "mEdgeGlowLeft", "mEdgeGlowRight"};
        for (String edgeGlow : edgeGlows) {
            Class<?> clazz = scrollableView.getClass();
            while (clazz != null) {
                try {
                    final Field edgeGlowField = clazz.getDeclaredField(edgeGlow);
                    edgeGlowField.setAccessible(true);
                    final EdgeEffect edgeEffect = (EdgeEffect) edgeGlowField.get(scrollableView);
                    edgeEffect.setColor(color);
                    break;
                } catch (Exception e) {
                    clazz = clazz.getSuperclass();
                }
            }
        }
    }

    @Override
    public void onScrollChanged(ScrollViewEx scrollView, int x, int y, int oldx, int oldy)
    {
        View view = scrollView.getChildAt(0);
        int diff = view.getTop() - scrollView.getScrollY();

        // if diff is zero, then the top has been reached
        if (diff == 0 && NetworkUtils.isNetworkAvailable(this))
        {
            if(fragListVisitsToday.isAdded() || fragListVisitsOther.isAdded()
                    || fragListVisitsFree.isAdded() || fragListVisitsOverdue.isAdded())
            {
                GlobalConstants.visitsListIsObsolete = true;
                refreshVisitsList();
            }

            if(fragListReportsNotSent.isAdded() || fragListReportsSent.isAdded())
            {
                GlobalConstants.reportsListIsObsolete = true;
                refreshVisitsList();
            }

            if(fragListVisitsReminded.isAdded() || fragListReportsReminded.isAdded())
            {
                GlobalConstants.reminderListIsObsolete = true;
                refreshVisitsList();
            }
        }
    }
}
