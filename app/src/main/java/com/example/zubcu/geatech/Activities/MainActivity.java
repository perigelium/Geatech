package com.example.zubcu.geatech.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment1;
import com.example.zubcu.geatech.Fragments.CtrlBtnsFragment2;
import com.example.zubcu.geatech.Fragments.FragmentListVisits;
import com.example.zubcu.geatech.R;

public class MainActivity extends Activity
        implements CtrlBtnsFragment1.OnClickListener,
        CtrlBtnsFragment2.OnClickListener, FragmentListVisits.OnClickListener
{

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    CtrlBtnsFragment1 ctrlBtnsFragment1;
    CtrlBtnsFragment2 ctrlBtnsFragment2;
    FragmentListVisits listVisits;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_window);

        ctrlBtnsFragment1 = new CtrlBtnsFragment1();
        ctrlBtnsFragment2 = new CtrlBtnsFragment2();
        listVisits = new FragmentListVisits();

        mFragmentManager = getFragmentManager();

        mFragmentTransaction = mFragmentManager.beginTransaction();


        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment2);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment1);
        mFragmentTransaction.add(R.id.ListContainer, listVisits);
        mFragmentTransaction.hide(ctrlBtnsFragment2);
        //mFragmentTransaction.addToBackStack("frag");

        mFragmentTransaction.commit();
	}

    @Override
    public void onCtrlButtonClicked(View view)
    {
        mFragmentManager = getFragmentManager();

        mFragmentTransaction = mFragmentManager.beginTransaction();


        if (view == findViewById(R.id.btnReturn))
        {
            mFragmentTransaction.hide(ctrlBtnsFragment2);
            mFragmentTransaction.show(ctrlBtnsFragment1);
        }

        mFragmentTransaction.commit();

        //Toast.makeText(getApplicationContext(), Integer.toString(buttonIndex) ,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnListItemSelected(int itemIndex)
    {
        mFragmentManager = getFragmentManager();
        //CtrlBtnsFragment1 ctrlF1 = (CtrlBtnsFragment1) mFragmentManager.findFragmentById(R.id.ctrlBtnsFragment1);

        mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.hide(ctrlBtnsFragment1);
            mFragmentTransaction.show(ctrlBtnsFragment2);

        mFragmentTransaction.commit();
    }
}
