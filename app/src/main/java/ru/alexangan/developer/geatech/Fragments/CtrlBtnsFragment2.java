package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.R;

/**
 * Created by user on 11/10/2016.
 */

public class CtrlBtnsFragment2 extends Fragment implements View.OnClickListener
{

    private Communicator mCommunicator;
    RadioGroup rGroup;
    int checkedBtnId = 0;
    int previousCheckedId = 0;
    List<Button> btnArray;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.ctrl_btns_fragment2, container, false);

        //final OnClickedListener listener = (OnClickedListener) getActivity();

        mCommunicator = (Communicator) getActivity();

        //rGroup = ((RadioGroup) rootView.findViewById(R.id.toggleGroup));
        //rGroup.check(checkedBtnId);

        Button btnVisits = (Button)rootView.findViewById(R.id.btnReturn);
        Button btnComingVisits = (Button)rootView.findViewById(R.id.btnInfo);
        Button btnInWorkVisits = (Button)rootView.findViewById(R.id.btnFillReport);
        Button btnNotSentReports = (Button)rootView.findViewById(R.id.btnAddPhotos);
        Button btnSentReports = (Button)rootView.findViewById(R.id.btnSendReport);

        btnArray = new ArrayList<>();
        btnArray.add(btnVisits);
        btnArray.add(btnComingVisits);
        btnArray.add(btnInWorkVisits);
        btnArray.add(btnNotSentReports);
        btnArray.add(btnSentReports);

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
        mCommunicator.onCtrlButtons2Clicked(btnSelected);
    }
}
