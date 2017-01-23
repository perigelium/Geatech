package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.alexangan.developer.geatech.Activities.LoginActivity;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Interfaces.RESTdataReceiverEventListener;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.Network.RESTdataReceiver;
import ru.alexangan.developer.geatech.R;

import static android.content.Context.RESTRICTIONS_SERVICE;
import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.visitItems;

public class SendReportFragment extends Fragment implements View.OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    private Communicator mCommunicator;

    private OnFragmentInteractionListener mListener;
    private Button sendReport;
    private int selectedIndex;
    ReportStates reportStates;
    VisitItem visitItem;

    public SendReportFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendReportFragment newInstance(String param1, String param2)
    {
        SendReportFragment fragment = new SendReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView =  inflater.inflate(R.layout.send_report_fragment, container, false);

        sendReport = (Button) rootView.findViewById(R.id.btnSendReport);
        sendReport.setOnClickListener(this);

        visitItem = visitItems.get(selectedIndex);
        VisitStates visitStates = visitItem.getVisitStates();
        int idSopralluogo = visitStates.getIdSopralluogo();

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btnSendReport)
        {
            if(reportStates != null)
            {
                Calendar calendarNow = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                String strDateTime = sdf.format(calendarNow.getTime());

                realm.beginTransaction();
                reportStates.setDataOraRaportoInviato(strDateTime);
                realm.commitTransaction();
            }

            Gson gson = new Gson();
            String json = gson.toJson(reportStates);

            //RESTdataReceiver resTdataReceiver = new RESTdataReceiver(cb, LoginActivity.this);
            //resTdataReceiver.sendReport(json);

            Toast.makeText(getActivity(),"Rapporto inviato", Toast.LENGTH_LONG).show();
            
            mCommunicator.onSendReportReturned();
        }
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
