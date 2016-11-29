package com.example.zubcu.geatech.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.zubcu.geatech.Fragments.ComingListVisitsFragment;
import com.example.zubcu.geatech.Fragments.ComposeReportTemplateFragment;
import com.example.zubcu.geatech.Fragments.CtrlBtnReportDetailed;
import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment1;
import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment2;
import com.example.zubcu.geatech.Fragments.SetDateTimeFragment;
import com.example.zubcu.geatech.Fragments.FragmentCTLinfo;
import com.example.zubcu.geatech.Fragments.FragmentListVisits;
import com.example.zubcu.geatech.Fragments.InWorkListVisitsFragment;
import com.example.zubcu.geatech.Fragments.NotSentListVisitsFragment;
import com.example.zubcu.geatech.Fragments.PhotoGalleryGridFragment;
import com.example.zubcu.geatech.Fragments.ReportDetailedFragment;
import com.example.zubcu.geatech.Fragments.ReportsListFragment;
import com.example.zubcu.geatech.Fragments.SendReportFragment;
import com.example.zubcu.geatech.Interfaces.Communicator;
import com.example.zubcu.geatech.R;
import com.example.zubcu.geatech.Services.SwipeDetector;

public class MainActivity extends Activity implements Communicator
{

    private FragmentManager mFragmentManager;
    //private FragmentTransaction mFragmentTransaction;
    SwipeDetector swipeDetector;

    CtrlBtnsFragment1 ctrlBtnsFragment1;
    CtrlBtnsFragment2 ctrlBtnsFragment2;
    SetDateTimeFragment dateTimeSetFragment;
    FragmentListVisits listVisits;
    InWorkListVisitsFragment inWorkListVisits;
    ComingListVisitsFragment comingListVisits;
    NotSentListVisitsFragment notSentListVisits;
    ReportsListFragment reportsList;
    FragmentCTLinfo ctlInfo;
    CtrlBtnReportDetailed ctrlBtnsReportDetailed;
    ReportDetailedFragment reportDetailedFragment;
    SendReportFragment sendReportFragment;
    PhotoGalleryGridFragment photoGalleryGridFragment;
    ComposeReportTemplateFragment composeReportTemplateFragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_window);

        swipeDetector = new SwipeDetector();

        ctrlBtnsFragment1 = new CtrlBtnsFragment1();
        ctrlBtnsFragment2 = new CtrlBtnsFragment2();
        dateTimeSetFragment = new SetDateTimeFragment();
        listVisits = new FragmentListVisits();
        inWorkListVisits = new InWorkListVisitsFragment();
        comingListVisits = new ComingListVisitsFragment();
        notSentListVisits = new NotSentListVisitsFragment();
        reportsList = new ReportsListFragment();
        ctlInfo = new FragmentCTLinfo();
        ctrlBtnsReportDetailed = new CtrlBtnReportDetailed();
        reportDetailedFragment = new ReportDetailedFragment();
        sendReportFragment = new SendReportFragment();
        photoGalleryGridFragment = new PhotoGalleryGridFragment();
        composeReportTemplateFragment = new ComposeReportTemplateFragment();

        mFragmentManager = getFragmentManager();

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

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

    @Override
    public void onCtrlButtonClicked(View view)
    {
        if(!view.isShown())
        {
            return;
        }

        if (view == findViewById(R.id.btnReturn))
        {
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
            setVisitsListContent(composeReportTemplateFragment);
        }

        if (view == findViewById(R.id.btnAddPhotos))
        {
            removeAllLists();
            setVisitsListContent(photoGalleryGridFragment);
        }

        if (view == findViewById(R.id.btnInfo))
        {
            removeAllLists();
            setVisitsListContent(ctlInfo);
        }

        if (view == findViewById(R.id.btnGreenCloud))
        {
            removeAllLists();
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

        if (composeReportTemplateFragment.isAdded())
        {
            mFragmentTransaction.remove(composeReportTemplateFragment);
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

        if (dateTimeHasSet) // if visit day is empty, show set datetime fragment, otherwise show CTL info.
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.hide(ctrlBtnsFragment1);
            mFragmentTransaction.show(ctrlBtnsFragment2);
            mFragmentTransaction.commit();

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
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.hide(ctrlBtnsFragment2);
        mFragmentTransaction.hide(ctrlBtnsFragment1);
        mFragmentTransaction.show(ctrlBtnsReportDetailed);

        mFragmentTransaction.commit();

        removeAllLists();
        setVisitsListContent(reportDetailedFragment);
        ctrlBtnsFragment1.setCheckedBtnId(R.id.btnReports);
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
