package com.example.zubcu.geatech.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment1;
import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment2;
import com.example.zubcu.geatech.Fragments.DateTimeSetFragment;
import com.example.zubcu.geatech.Fragments.FragmentListVisits;
import com.example.zubcu.geatech.R;

public class MainActivity extends Activity
        implements CtrlBtnsFragment1.OnClickedListener,
        CtrlBtnsFragment2.OnClickedListener, FragmentListVisits.OnItemSelectedListener
{

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    CtrlBtnsFragment1 ctrlBtnsFragment1;
    CtrlBtnsFragment2 ctrlBtnsFragment2;
    DateTimeSetFragment dateTimeSetFragment;
    FragmentListVisits listVisits;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_window);

        ctrlBtnsFragment1 = new CtrlBtnsFragment1();
        ctrlBtnsFragment2 = new CtrlBtnsFragment2();
        dateTimeSetFragment = new DateTimeSetFragment();
        listVisits = new FragmentListVisits();

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
/*            Bundle args = new Bundle();
            args.putInt(ctrlBtnsFragment1.checkedBtnIdStr, R.id.btnVisits);
            ctrlBtnsFragment1.setArguments(args);*/

            mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.hide(ctrlBtnsFragment2);
            ctrlBtnsFragment1.setCheckedBtnId(R.id.btnVisits);
            mFragmentTransaction.show(ctrlBtnsFragment1);

            //mFragmentTransaction.replace(R.id.CtrlBtnFragContainer, ctrlBtnsFragment1);


            if(!listVisits.isAdded())
            {
                mFragmentTransaction.add(R.id.CtrlBtnFragContainer, listVisits);
            }

            if(dateTimeSetFragment.isAdded())
            {
                mFragmentTransaction.remove(dateTimeSetFragment);
            }

            mFragmentTransaction.commit();
        }

        //Toast.makeText(getApplicationContext(), Integer.toString(buttonIndex) ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnListItemSelected(int itemIndex)
    {
/*        Bundle args = new Bundle();
        args.putInt(ctrlBtnsFragment2.checkedBtnIdStr, R.id.btnInfo);
        ctrlBtnsFragment2.setArguments(args);*/

        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.hide(ctrlBtnsFragment1);
        ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);
        mFragmentTransaction.show(ctrlBtnsFragment2);
        //mFragmentTransaction.replace(R.id.CtrlBtnFragContainer, ctrlBtnsFragment2);


        if(listVisits.isAdded())
        {
            mFragmentTransaction.remove(listVisits);
        }

        if(!dateTimeSetFragment.isAdded())
        {
            mFragmentTransaction.add(R.id.CtrlBtnFragContainer, dateTimeSetFragment);
        }

        mFragmentTransaction.commit();

/*        if(listVisits.isVisible())
        {
        int i = 0;
        }*/
    }
}
