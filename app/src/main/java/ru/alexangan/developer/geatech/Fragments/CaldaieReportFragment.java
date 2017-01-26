package ru.alexangan.developer.geatech.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Models.CaldaieReportModel;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.visitItems;

public class CaldaieReportFragment extends Fragment
{
    View rootView;
    CaldaieReportModel caldaieReportModel;
    private int selectedIndex;
    int idSopralluogo;
    ReportStates reportStates;
    Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CaldaieReportFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CaldaieReportFragment newInstance(String param1, String param2)
    {
        CaldaieReportFragment fragment = new CaldaieReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getActivity();

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView =  inflater.inflate(R.layout.caldaie_report, container, false);

        realm.beginTransaction();
        VisitItem visitItem = visitItems.get(selectedIndex);
        VisitStates visitStates = visitItem.getVisitStates();
        idSopralluogo = visitStates.getIdSopralluogo();

        reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        caldaieReportModel = realm.where(CaldaieReportModel.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        RealmResults<CaldaieReportModel> caldaieModels = realm.where(CaldaieReportModel.class).findAll();

        if (reportStates != null)
        {
            if (caldaieReportModel == null)
            {
                caldaieReportModel = new CaldaieReportModel(caldaieModels.size());
                caldaieReportModel.setIdSopralluogo(idSopralluogo);
                realm.copyToRealmOrUpdate(caldaieReportModel);
            }
        }
        realm.commitTransaction();

        return rootView;
    }
}
