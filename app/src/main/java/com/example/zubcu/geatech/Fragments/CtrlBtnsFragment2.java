package com.example.zubcu.geatech.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.zubcu.geatech.R;

/**
 * Created by user on 11/10/2016.
 */

public class CtrlBtnsFragment2 extends Fragment{

    private RadioGroup rGroup;

    public static final String checkedBtnIdStr = "";
    int checkedBtnId = 0;
    int previousCheckedId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_ctrl_btns_fragment2, container, false);
        rGroup = ((RadioGroup) rootView.findViewById(R.id.toggleGroup));
        final OnClickedListener listener = (OnClickedListener) getActivity();

        final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int i)
            {
                //rGroup.check(checkedBtnId);

                if(previousCheckedId == i) return;

                previousCheckedId = i;

                for (int j = 0; j < radioGroup.getChildCount(); j++)
                {
                    final RadioButton view = (RadioButton) radioGroup.getChildAt(j);

                    if(view.getId() == i)
                    {
                        listener.onCtrlButtonClicked(view);

                        break;
                    }
                }
            }
        };

        rGroup.setOnCheckedChangeListener(ToggleListener);

        return rootView;
    }

    @Override
    public View getView()
    {
        //rGroup.check(checkedBtnId);
        return super.getView();
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);

/*        if(rGroup!= null && !hidden)
        {
            rGroup.check(checkedBtnId);
        }*/
    }

    @Override
    public void onResume()
    {
        //rGroup.check(checkedBtnId);
        super.onResume();
    }

    public interface OnClickedListener
    {
        void onCtrlButtonClicked(View view);
    }

    public void setCheckedBtnId(int checkedBtnId)
    {
        this.checkedBtnId = checkedBtnId;
    }

    public void clearCheck()
    {
        rGroup.clearCheck();
    }
}
