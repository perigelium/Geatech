package com.example.zubcu.geatech.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.example.zubcu.geatech.Fragments.ComingListVisitsFragment;
import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment1;
import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment2;
import com.example.zubcu.geatech.Fragments.DateTimeSetFragment;
import com.example.zubcu.geatech.Fragments.FragmentCTLinfo;
import com.example.zubcu.geatech.Fragments.FragmentListVisits;
import com.example.zubcu.geatech.Fragments.InWorkListVisitsFragment;
import com.example.zubcu.geatech.Fragments.NotSentListVisitsFragment;
import com.example.zubcu.geatech.Interfaces.Communicator;
import com.example.zubcu.geatech.R;

public class MainActivity extends Activity
        implements CtrlBtnsFragment1.OnClickedListener,
        CtrlBtnsFragment2.OnClickedListener, FragmentListVisits.OnItemSelectedListener, Communicator
{

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    CtrlBtnsFragment1 ctrlBtnsFragment1;
    CtrlBtnsFragment2 ctrlBtnsFragment2;
    DateTimeSetFragment dateTimeSetFragment;
    FragmentListVisits listVisits;
    InWorkListVisitsFragment inWorkListVisits;
    ComingListVisitsFragment comingListVisits;
    NotSentListVisitsFragment notSentListVisits;
    FragmentCTLinfo ctlInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_window);

        ctrlBtnsFragment1 = new CtrlBtnsFragment1();
        ctrlBtnsFragment2 = new CtrlBtnsFragment2();
        dateTimeSetFragment = new DateTimeSetFragment();
        listVisits = new FragmentListVisits();
        inWorkListVisits = new InWorkListVisitsFragment();
        comingListVisits = new ComingListVisitsFragment();
        notSentListVisits = new NotSentListVisitsFragment();
        ctlInfo = new FragmentCTLinfo();

        mFragmentManager = getFragmentManager();

        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment1);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment2);


        mFragmentTransaction.hide(ctrlBtnsFragment2);
        mFragmentTransaction.show(ctrlBtnsFragment1);
        ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, listVisits);
        //mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
	}

    @Override
    public void onCtrlButtonClicked(View view)
    {

        if (view == findViewById(R.id.btnReturn))
        {
            mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.hide(ctrlBtnsFragment2);

            ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);
            mFragmentTransaction.show(ctrlBtnsFragment1);


            if(!listVisits.isAdded())
            {
                mFragmentTransaction.add(R.id.CtrlBtnFragContainer, listVisits);
            }

            if(dateTimeSetFragment.isAdded())
            {
                mFragmentTransaction.remove(dateTimeSetFragment);
            }

            if(ctlInfo.isAdded())
            {
                mFragmentTransaction.remove(ctlInfo);
            }

            mFragmentTransaction.commit();
        }

        if (view == findViewById(R.id.btnComingVisits))
        {
            mFragmentTransaction = mFragmentManager.beginTransaction();

            if(listVisits.isAdded())
            {
                mFragmentTransaction.remove(listVisits);
            }

            if(inWorkListVisits.isAdded())
            {
                mFragmentTransaction.remove(inWorkListVisits);
            }

            if(notSentListVisits.isAdded())
            {
                mFragmentTransaction.remove(notSentListVisits);
            }

            if(!comingListVisits.isAdded())
            {
                mFragmentTransaction.add(R.id.CtrlBtnFragContainer, comingListVisits);
            }

            mFragmentTransaction.commit();
        }

        if (view == findViewById(R.id.btnVisits))
        {
            mFragmentTransaction = mFragmentManager.beginTransaction();

            if(comingListVisits.isAdded())
            {
                mFragmentTransaction.remove(comingListVisits);
            }

            if(inWorkListVisits.isAdded())
            {
                mFragmentTransaction.remove(inWorkListVisits);
            }

            if(notSentListVisits.isAdded())
            {
                mFragmentTransaction.remove(notSentListVisits);
            }

            if(!listVisits.isAdded())
            {
                mFragmentTransaction.add(R.id.CtrlBtnFragContainer, listVisits);
            }

            mFragmentTransaction.commit();
        }

        if (view == findViewById(R.id.btnInWorkVisits))
        {
            mFragmentTransaction = mFragmentManager.beginTransaction();

            if(listVisits.isAdded())
            {
                mFragmentTransaction.remove(listVisits);
            }

            if(comingListVisits.isAdded())
            {
                mFragmentTransaction.remove(comingListVisits);
            }

            if(notSentListVisits.isAdded())
            {
                mFragmentTransaction.remove(notSentListVisits);
            }

            if(!inWorkListVisits.isAdded())
            {
                mFragmentTransaction.add(R.id.CtrlBtnFragContainer, inWorkListVisits);
            }

            mFragmentTransaction.commit();
        }

        if (view == findViewById(R.id.btnNotSentReports))
        {
            mFragmentTransaction = mFragmentManager.beginTransaction();

            if(listVisits.isAdded())
            {
                mFragmentTransaction.remove(listVisits);
            }

            if(comingListVisits.isAdded())
            {
                mFragmentTransaction.remove(comingListVisits);
            }

            if(dateTimeSetFragment.isAdded())
            {
                mFragmentTransaction.remove(dateTimeSetFragment);
            }

            if(ctlInfo.isAdded())
            {
                mFragmentTransaction.remove(ctlInfo);
            }

            if(inWorkListVisits.isAdded())
            {
                mFragmentTransaction.remove(inWorkListVisits);
            }

            if(!notSentListVisits.isAdded())
            {
                mFragmentTransaction.add(R.id.CtrlBtnFragContainer, notSentListVisits);
            }

            mFragmentTransaction.commit();
        }

        //Toast.makeText(getApplicationContext(), Integer.toString(buttonIndex) ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnListItemSelected(int itemIndex, Boolean dateTimeHasSet)
    {
/*        Bundle args = new Bundle();
        args.putInt(ctrlBtnsFragment2.checkedBtnIdStr, R.id.btnInfo);
        ctrlBtnsFragment2.setArguments(args);*/

        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.hide(ctrlBtnsFragment1);
        ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);
        mFragmentTransaction.show(ctrlBtnsFragment2);

        if(listVisits.isAdded())
        {
            mFragmentTransaction.remove(listVisits);
        }

        if (dateTimeHasSet) // if visit day is empty, show set datetime fragment, otherwise show CTL info.
        {
            if (!ctlInfo.isAdded())
            {
                mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctlInfo);
            }
        }
        else
        {
            if (!dateTimeSetFragment.isAdded())
            {
                mFragmentTransaction.add(R.id.CtrlBtnFragContainer, dateTimeSetFragment);
            }
        }

        mFragmentTransaction.commit();
    }

    @Override
    public void onDateTimeSetReturned(Boolean mDatetimeAlreadySet)
    {
        onCtrlButtonClicked(findViewById(R.id.btnReturn));
    }
}
