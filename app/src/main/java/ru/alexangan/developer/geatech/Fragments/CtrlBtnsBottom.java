package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.R;

/**
 * Created by user on 11/10/2016*/

public class CtrlBtnsBottom extends Fragment implements View.OnClickListener
{

    private Communicator mCommunicator;
    List<Button> btnArray;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.ctrl_btns_bottom, container, false);

        //final OnClickedListener listener = (OnClickedListener) getActivity();

        mCommunicator = (Communicator) getActivity();

        //rGroup = ((RadioGroup) rootView.findViewById(R.id.toggleGroup));
        //rGroup.check(checkedBtnId);

        Button btnVisits = (Button)rootView.findViewById(R.id.btnVisits);
        Button btnInWorkVisits = (Button)rootView.findViewById(R.id.btnInWorkVisits);
        Button btnComingVisits = (Button)rootView.findViewById(R.id.btnNotifUrgentReports);
        Button btnNotSentReports = (Button)rootView.findViewById(R.id.btnNotSentReports);
        Button btnAppSettings = (Button)rootView.findViewById(R.id.btnAppSettings);

        btnArray = new ArrayList<>();
        btnArray.add(btnVisits);
        btnArray.add(btnComingVisits);
        btnArray.add(btnInWorkVisits);
        btnArray.add(btnNotSentReports);
        btnArray.add(btnAppSettings);

        for(Button btn : btnArray)
        {
            btn.setOnClickListener(this);
        }

/*        final RadioGroup.OnCheckedChangeListener ToggleListener =
                new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int i)
            {

                if(previousCheckedId == i) return;

                previousCheckedId = i;

                for (int j = 0; j < radioGroup.getChildCount(); j++)
                {
                    final Button view = (Button) radioGroup.getChildAt(j);

                    if(view.getId() == i)
                    {
                        mCommunicator.onCtrlButtonClicked(view);
                        break;
                    }
                }
            }
        };

        rGroup.setOnCheckedChangeListener(ToggleListener);*/

        return rootView;
    }

/*    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);

        if (rGroup != null && hidden)
        {
            rGroup.check(checkedBtnId);
        }
    }*/

    @Override
    public void onResume()
    {
        super.onResume();
    }

/*    public interface OnClickedListener
    {
        void onCtrlButtonClicked(View view);
    }*/

    public void setCheckedBtnId(int checkedBtnId)
    {
        //this.checkedBtnId = checkedBtnId;

        Button rBtn = (Button) rootView.findViewById(checkedBtnId);
        selectButton(rBtn);
    }

    @Override
    public void onClick(View view)
    {
        Button rBtn = (Button)view;
        selectButton(rBtn);
    }

    private void selectButton(Button btnSelected)
    {
        for(Button btn : btnArray)
        {
            btn.setSelected(false);
        }

        btnSelected.setSelected(true);
        mCommunicator.onCtrlButtons1Clicked(btnSelected);
    }
}
