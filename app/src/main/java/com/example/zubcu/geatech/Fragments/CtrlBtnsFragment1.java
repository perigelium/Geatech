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

public class CtrlBtnsFragment1 extends Fragment{

    private RadioGroup rGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_ctrl_btns_fragment1, container, false);

        final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int i)
            {
                for (int j = 0; j < radioGroup.getChildCount(); j++)
                {
                    final RadioButton view = (RadioButton) radioGroup.getChildAt(j);

                    if(view.getId() == i)
                    {
                        OnClickListener listener = (OnClickListener) getActivity();
                        listener.onCtrlButtonClicked(view);
                        break;
                    }
                }
            }
        };

        rGroup = ((RadioGroup) rootView.findViewById(R.id.toggleGroup));
        rGroup.setOnCheckedChangeListener(ToggleListener);
        rGroup.check(R.id.visits);

        return rootView;
    }

    public interface OnClickListener
    {
        void onCtrlButtonClicked(View view);
    }
}
