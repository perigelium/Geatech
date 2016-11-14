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
import com.example.zubcu.geatech.Fragments.FragmentListVisits2;
import com.example.zubcu.geatech.R;

public class MainActivity extends Activity
        implements CtrlBtnsFragment1.OnClickedListener,
        CtrlBtnsFragment2.OnClickedListener, FragmentListVisits.OnItemSelectedListener
{

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    CtrlBtnsFragment1 ctrlBtnsFragment1;
    CtrlBtnsFragment2 ctrlBtnsFragment2;
    FragmentListVisits listVisits;
    FragmentListVisits2 listVisits2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_window);

        ctrlBtnsFragment1 = new CtrlBtnsFragment1();
        ctrlBtnsFragment2 = new CtrlBtnsFragment2();
        listVisits = new FragmentListVisits();

        mFragmentManager = getFragmentManager();

        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, ctrlBtnsFragment1);
//        mFragmentTransaction.hide(ctrlBtnsFragment2);
//        mFragmentTransaction.show(ctrlBtnsFragment1);
        mFragmentTransaction.add(R.id.CtrlBtnFragContainer, listVisits);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
	}

    @Override
    public void onCtrlButtonClicked(View view)
    {

        if (view == findViewById(R.id.btnReturn))
        {
            mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.replace(R.id.CtrlBtnFragContainer, ctrlBtnsFragment1);
            ctrlBtnsFragment2.setCheckedBtnId(R.id.btnVisits);

            if(!listVisits.isInLayout())
            {
                mFragmentTransaction.add(R.id.CtrlBtnFragContainer, listVisits);
            }

            mFragmentTransaction.commit();
        }

        //Toast.makeText(getApplicationContext(), Integer.toString(buttonIndex) ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnListItemSelected(int itemIndex)
    {
        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.CtrlBtnFragContainer, ctrlBtnsFragment2);
        ctrlBtnsFragment2.setCheckedBtnId(R.id.btnInfo);

        if(listVisits.isInLayout())
        {
            mFragmentTransaction.remove(listVisits);
        }

        mFragmentTransaction.commit();

/*        if(listVisits.isVisible())
        {
        int i = 0;
        }*/
    }
}
