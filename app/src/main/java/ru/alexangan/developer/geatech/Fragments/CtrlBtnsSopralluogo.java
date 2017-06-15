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


public class CtrlBtnsSopralluogo extends Fragment implements View.OnClickListener
{

    private Communicator mCommunicator;
    List<Button> btnArray;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.ctrl_btns_sopralluogo, container, false);

        mCommunicator = (Communicator) getActivity();

        final Button btnSopralluogoReturn = (Button) rootView.findViewById(R.id.btnSopralluogoReturn);
        btnSopralluogoReturn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mCommunicator.onCtrlBtnsSopralluogoClicked(btnSopralluogoReturn.getId());
            }
        });

        Button btnSopralluogoInfo = (Button) rootView.findViewById(R.id.btnSopralluogoInfo);
        Button btnFillReport = (Button) rootView.findViewById(R.id.btnFillReport);
        Button btnAddPhotos = (Button) rootView.findViewById(R.id.btnAddPhotos);
        Button btnSendReport = (Button) rootView.findViewById(R.id.btnSendReport);

        btnArray = new ArrayList<>();
        btnArray.add(btnSopralluogoInfo);
        btnArray.add(btnFillReport);
        btnArray.add(btnAddPhotos);
        btnArray.add(btnSendReport);

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
        mCommunicator.onCtrlBtnsSopralluogoClicked(btnSelected.getId());
    }

    public void selectNextButton(int btnSelectedId, boolean direction)
    {
        Button btnSelected = (Button) getActivity().findViewById(btnSelectedId);

        if(btnSelected == null)
        {
            return;
        }

        if( ! direction)
        {
            if(btnSelected == btnArray.get(0))
            {
                return;
            }
        }

        if(direction)
        {
            if(btnSelected == btnArray.get(btnArray.size() - 1))
            {
                return;
            }
        }

        boolean reachedBtnPrevSelected = false;
        int start;
        int step;
        int indexBound;

        if(direction)
        {
            start = 0;
            step = 1;
            indexBound = btnArray.size();
        }
        else
        {
            start = btnArray.size() - 1;
            step = -1;
            indexBound = -1;
        }

        for (int i = start; i != indexBound; i+=step)
        {
            Button btn = btnArray.get(i);

            if(btn.equals(btnSelected))
            {
                reachedBtnPrevSelected = true;
                btn.setSelected(false);
                continue;
            }

            if(reachedBtnPrevSelected)
            {
                btn.setSelected(true);
                btnSelected = btn;
                reachedBtnPrevSelected = false;
            }
            else
            {
                btn.setSelected(false);
            }
        }

        mCommunicator.onCtrlBtnsSopralluogoClicked(btnSelected.getId());
    }
}
