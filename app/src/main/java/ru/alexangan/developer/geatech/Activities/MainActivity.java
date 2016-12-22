package ru.alexangan.developer.geatech.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Fragments.CTLinfoFragment;
import ru.alexangan.developer.geatech.Fragments.ComingListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.Clima1ReportFragment;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnReportDetailed;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnsFragment1;
import ru.alexangan.developer.geatech.Fragments.CtrlBtnsFragment2;
import ru.alexangan.developer.geatech.Fragments.InWorkListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.ListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.NotSentListVisitsFragment;
import ru.alexangan.developer.geatech.Fragments.NotificationBarFragment;
import ru.alexangan.developer.geatech.Fragments.PhotoGalleryGridFragment;
import ru.alexangan.developer.geatech.Fragments.ReportSentDetailedFragment;
import ru.alexangan.developer.geatech.Fragments.ReportsListFragment;
import ru.alexangan.developer.geatech.Fragments.SendReportFragment;
import ru.alexangan.developer.geatech.Fragments.SetDateTimeFragment;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Network.RESTdataReceiver.inVisitItems;

public class MainActivity extends Activity implements Communicator
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
    CTLinfoFragment ctlInfo;
    CtrlBtnReportDetailed ctrlBtnsReportDetailed;
    ReportSentDetailedFragment reportDetailedFragment;
    SendReportFragment sendReportFragment;
    PhotoGalleryGridFragment photoGalleryGridFragment;
    Clima1ReportFragment clima1ReportFragment;
    NotificationBarFragment notificationBarFragment;
    public static RealmResults<VisitItem> visitItems;
    int currentSelIndex;

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

        if(inVisitItems!=null)
        {
            realm.beginTransaction();
            for (VisitItem visitItem : inVisitItems)
            {
                realm.copyToRealmOrUpdate(visitItem);
            }
            realm.commitTransaction();

            //int visitItemsSize = visitItems.size();
        }

        realm.beginTransaction();
        visitItems = realm.where(VisitItem.class).findAll();
        realm.commitTransaction();

        if(visitItems.size() == 0)
        {
            Toast.makeText(this, "Database inizializzazione falito, controlla la connessione a Internet", Toast.LENGTH_LONG).show();

            this.finish();
        }

/*        realm.beginTransaction();
        int reportStatesCount = realm.where(ReportStates.class).findAll().size();
        realm.commitTransaction();*/

        for (VisitItem realmVisitItem : visitItems)
        {
            int idSopralluogo = realmVisitItem.getVisitStates().getIdSopralluogo();

            Boolean addressAndProductPresent = false;
            realm.beginTransaction();
            RealmResults<ReportStates> reportStatesList = realm.where(ReportStates.class).findAll();
            realm.commitTransaction();
            for (ReportStates reportStates : reportStatesList)
            {
                realm.beginTransaction();
                int idSopralluogoRep = reportStates.getIdSopralluogo();

                if(idSopralluogo == idSopralluogoRep)
                {
                    addressAndProductPresent = true;
                    reportStates.setVisitId(realmVisitItem.getId());
                }
                realm.commitTransaction();
            }

            if(addressAndProductPresent == false)
            {
                realm.beginTransaction();
                ReportStates newReportStates = new ReportStates(reportStatesList.size(), realmVisitItem.getId());
                newReportStates.setIdSopralluogo(idSopralluogo);
                realm.copyToRealm(newReportStates);
                realm.commitTransaction();
            }
        }


        //String visitsJSONData = getIntent().getStringExtra("JSON");

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
        clima1ReportFragment = new Clima1ReportFragment();
        notificationBarFragment = new NotificationBarFragment();

        mFragmentManager = getFragmentManager();

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.NotificationBarFragContainer, notificationBarFragment);

        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment1);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment2);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsReportDetailed);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, listVisits);
        ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);
        ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);

        mFragmentTransaction.hide(ctrlBtnsReportDetailed);
        mFragmentTransaction.hide(ctrlBtnsFragment2);
        mFragmentTransaction.show(ctrlBtnsFragment1);

        mFragmentTransaction.commit();
    }

    public void sendReportNow(int position)
    {

    }

    @Override
    public void onCtrlButtonClicked(View view)
    {
        if(!view.isShown())
        {
            return;
        }

        if (view == findViewById(R.id.btnReturn))
        {
            currentSelIndex = -1;
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(ctrlBtnsFragment2);
            mFragmentTransaction.show(ctrlBtnsFragment1);
            mFragmentTransaction.commit();

            removeAllLists();
            setVisitsListContent(listVisits);
        }

        if (view == findViewById(R.id.btnReportsReturn))
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(ctrlBtnsReportDetailed);
            mFragmentTransaction.hide(ctrlBtnsFragment2);
            mFragmentTransaction.show(ctrlBtnsFragment1);
            mFragmentTransaction.commit();

            removeAllLists();
            setVisitsListContent(reportsList);
        }

        if (view == findViewById(R.id.btnFillReport))
        {
            removeAllLists();

            Bundle args = new Bundle();
            args.putInt("selectedIndex", currentSelIndex);
            clima1ReportFragment.setArguments(args);

            setVisitsListContent(clima1ReportFragment);
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

        if (view == findViewById(R.id.btnGreenCloud))
        {
            removeAllLists();

            Bundle args = new Bundle();
            args.putInt("selectedIndex", currentSelIndex);
            sendReportFragment.setArguments(args);

            setVisitsListContent(sendReportFragment);
        }

        if (view == findViewById(R.id.btnComingVisits))
        {
            removeAllLists();
            setVisitsListContent(comingListVisits);
        }

        if (view == findViewById(R.id.btnVisits))
        {
            removeAllLists();
            setVisitsListContent(listVisits);
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

        if (view == findViewById(R.id.btnReports))
        {
            removeAllLists();
            setVisitsListContent(reportsList);
        }

        //Toast.makeText(getApplicationContext(), Integer.toString(buttonIndex) ,Toast.LENGTH_SHORT).show();
    }

    private void removeAllLists()
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        if (clima1ReportFragment.isAdded())
        {
            mFragmentTransaction.remove(clima1ReportFragment);
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

        if(listVisits.isAdded())
        {
            mFragmentTransaction.remove(listVisits);
        }

        if(comingListVisits.isAdded())
        {
            mFragmentTransaction.remove(comingListVisits);
        }

        if(inWorkListVisits.isAdded())
        {
            mFragmentTransaction.remove(inWorkListVisits);
        }

        if (notSentListVisits.isAdded())
        {
            mFragmentTransaction.remove(notSentListVisits);
        }

        if(reportsList.isAdded())
        {
            mFragmentTransaction.remove(reportsList);
        }

        mFragmentTransaction.commit();
    }

    private void setVisitsListContent(Fragment fragment)
    {
        FragmentTransaction vFragmentTransaction = mFragmentManager.beginTransaction();

        if(!fragment.isAdded())
        {
            vFragmentTransaction.add(R.id.CtrlBtnFragContainer, fragment);
        }

        vFragmentTransaction.commit();
    }

    @Override
    public void OnListItemSelected(int itemIndex, Boolean dateTimeHasSet)
    {
        removeAllLists();
        currentSelIndex = itemIndex;

        if (dateTimeHasSet) // if visit day is empty, show set datetime fragment, otherwise show CTL info.
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.hide(ctrlBtnsFragment1);
            mFragmentTransaction.show(ctrlBtnsFragment2);
            mFragmentTransaction.commit();

            Bundle args = new Bundle();
            args.putInt("selectedIndex", itemIndex);
            ctlInfo.setArguments(args);

            setVisitsListContent(ctlInfo);
            ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);
        }
        else
        {
            Bundle args = new Bundle();
            args.putInt("selectedIndex", itemIndex);
            dateTimeSetFragment.setArguments(args);

            setVisitsListContent(dateTimeSetFragment);
        }

        ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);
    }

    @Override
    public void onDateTimeSetReturned(Boolean mDatetimeAlreadySet)
    {
        removeAllLists();
        setVisitsListContent(listVisits);
    }

    @Override
    public void onDetailedReportReturned()
    {
        onCtrlButtonClicked(findViewById(R.id.btnReportsReturn));
    }

    @Override
    public void onSendReportReturned()
    {
        onCtrlButtonClicked(findViewById(R.id.btnSendReport));
    }

    @Override
    public void OnListItemSelected(int itemIndex)
    {
        currentSelIndex = itemIndex;
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.hide(ctrlBtnsFragment2);
        mFragmentTransaction.hide(ctrlBtnsFragment1);
        mFragmentTransaction.show(ctrlBtnsReportDetailed);

        mFragmentTransaction.commit();

        removeAllLists();
        Bundle args = new Bundle();
        args.putInt("selectedIndex", itemIndex);
        reportDetailedFragment.setArguments(args);

        setVisitsListContent(reportDetailedFragment);

        ctrlBtnsFragment1.setCheckedBtnId(R.id.btnReports);
    }

    @Override
    public void onNotificationReportReturned(View view)
    {

    }

    @Override
    public void OnListItemSwiped(int itemIndex, Boolean dateTimeHasSet)
    {
        if(!dateTimeHasSet)
        {
            OnListItemSelected(itemIndex, false);
        }
        else
        {
            OnListItemSelected(itemIndex, true);
        }
    }
}
