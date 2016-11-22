package com.example.zubcu.geatech.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.example.zubcu.geatech.Fragments.ComingListVisitsFragment;
import com.example.zubcu.geatech.Fragments.CtrlBtnReportDetailed;
import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment1;
import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment2;
import com.example.zubcu.geatech.Fragments.DateTimeSetFragment;
import com.example.zubcu.geatech.Fragments.FragmentCTLinfo;
import com.example.zubcu.geatech.Fragments.FragmentListVisits;
import com.example.zubcu.geatech.Fragments.InWorkListVisitsFragment;
import com.example.zubcu.geatech.Fragments.NotSentListVisitsFragment;
import com.example.zubcu.geatech.Fragments.ReportDetailedFragment;
import com.example.zubcu.geatech.Fragments.ReportsListFragment;
import com.example.zubcu.geatech.Interfaces.Communicator;
import com.example.zubcu.geatech.R;

public class MainActivity extends Activity
        implements CtrlBtnsFragment1.OnClickedListener,
        CtrlBtnsFragment2.OnClickedListener, FragmentListVisits.OnItemSelectedListener, Communicator, ReportsListFragment.OnItemSelectedListener
{

    private FragmentManager mFragmentManager;
    //private FragmentTransaction mFragmentTransaction;

    CtrlBtnsFragment1 ctrlBtnsFragment1;
    CtrlBtnsFragment2 ctrlBtnsFragment2;
    DateTimeSetFragment dateTimeSetFragment;
    FragmentListVisits listVisits;
    InWorkListVisitsFragment inWorkListVisits;
    ComingListVisitsFragment comingListVisits;
    NotSentListVisitsFragment notSentListVisits;
    ReportsListFragment reportsList;
    FragmentCTLinfo ctlInfo;
    CtrlBtnReportDetailed ctrlBtnsReportDetailed;
    ReportDetailedFragment reportDetailedFragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_window);

        ctrlBtnsFragment1 = new CtrlBtnsFragment1();
        ctrlBtnsFragment2 = new CtrlBtnsFragment2();
        dateTimeSetFragment = new DateTimeSetFragment();
        listVisits = new FragmentListVisits();
        inWorkListVisits = new InWorkListVisitsFragment();
        comingListVisits = new ComingListVisitsFragment();
        notSentListVisits = new NotSentListVisitsFragment();
        reportsList = new ReportsListFragment();
        ctlInfo = new FragmentCTLinfo();
        ctrlBtnsReportDetailed = new CtrlBtnReportDetailed();
        reportDetailedFragment = new ReportDetailedFragment();

        mFragmentManager = getFragmentManager();

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment1);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment2);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsReportDetailed);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, listVisits);

        mFragmentTransaction.hide(ctrlBtnsReportDetailed);
        mFragmentTransaction.hide(ctrlBtnsFragment2);
        mFragmentTransaction.show(ctrlBtnsFragment1);

        mFragmentTransaction.commit();
    }

    @Override
    public void onCtrlButtonClicked(View view)
    {
        if (view == findViewById(R.id.btnReturn))
        {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(ctrlBtnsFragment2);
            mFragmentTransaction.show(ctrlBtnsFragment1);
            mFragmentTransaction.commit();

            //ctrlBtnsFragment2.clearCheck();
            removeAllLists();
            setVisitsListContent(listVisits);
            ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);
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
            ctrlBtnsFragment1.setCheckedBtnId(R.id.btnReports);
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
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.hide(ctrlBtnsFragment1);
        mFragmentTransaction.show(ctrlBtnsFragment2);
        mFragmentTransaction.commit();

        removeAllLists();

        if (dateTimeHasSet) // if visit day is empty, show set datetime fragment, otherwise show CTL info.
        {

            setVisitsListContent(ctlInfo);
            ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);
            //ctrlBtnsFragment1.clearCheck();
        }
        else
        {
            setVisitsListContent(dateTimeSetFragment);
        }
    }

    @Override
    public void onDateTimeSetReturned(Boolean mDatetimeAlreadySet)
    {
        onCtrlButtonClicked(findViewById(R.id.btnReturn));
    }

    @Override
    public void onDetailedReportReturned()
    {
        onCtrlButtonClicked(findViewById(R.id.btnReportsReturn));
    }

    @Override
    public void OnListItemSelected(int itemIndex)
    {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.hide(ctrlBtnsFragment2);
        mFragmentTransaction.hide(ctrlBtnsFragment1);
        mFragmentTransaction.show(ctrlBtnsReportDetailed);

        mFragmentTransaction.commit();

        //ctrlBtnsFragment1.clearCheck();

        removeAllLists();
        setVisitsListContent(reportDetailedFragment);


    }
}
