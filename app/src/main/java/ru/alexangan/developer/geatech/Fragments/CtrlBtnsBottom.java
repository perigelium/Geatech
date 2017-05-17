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

//Created by Alex Angan on 11/10/2016


public class CtrlBtnsBottom extends Fragment implements View.OnClickListener
{

    private Communicator mCommunicator;
    List<Button> btnArray;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.ctrl_btns_bottom, container, false);

        mCommunicator = (Communicator) getActivity();

        Button btnVisits = (Button) rootView.findViewById(R.id.btnVisits);
        Button btnInWorkVisits = (Button) rootView.findViewById(R.id.btnInWorkVisits);
        Button btnComingVisits = (Button) rootView.findViewById(R.id.btnNotifUrgentReports);
        Button btnNotSentReports = (Button) rootView.findViewById(R.id.btnNotSentReports);
        Button btnAppSettings = (Button) rootView.findViewById(R.id.btnAppSettings);

        btnArray = new ArrayList<>();
        btnArray.add(btnVisits);
        btnArray.add(btnComingVisits);
        btnArray.add(btnInWorkVisits);
        btnArray.add(btnNotSentReports);
        btnArray.add(btnAppSettings);

        for (Button btn : btnArray)
        {
            btn.setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void setCheckedBtnId(int checkedBtnId)
    {
        Button rBtn = (Button) rootView.findViewById(checkedBtnId);
        selectButton(rBtn);
    }

    @Override
    public void onClick(View view)
    {
        Button rBtn = (Button) view;
        selectButton(rBtn);
    }

    private void selectButton(Button btnSelected)
    {
        if (btnSelected.isSelected())
        {
            return;
        }

        for (Button btn : btnArray)
        {
            btn.setSelected(false);
        }

        btnSelected.setSelected(true);
        mCommunicator.onCtrlBtnsBottomClicked(btnSelected.getId());
    }
}
