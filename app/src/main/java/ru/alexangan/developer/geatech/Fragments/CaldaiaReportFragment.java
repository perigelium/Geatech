package ru.alexangan.developer.geatech.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class CaldaiaReportFragment extends Fragment
{
    View rootView;
    private int selectedIndex;
    int idSopralluogo;
    ReportStates reportStates;
    Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CaldaiaReportFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CaldaiaReportFragment newInstance(String param1, String param2)
    {
        CaldaiaReportFragment fragment = new CaldaiaReportFragment();
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
        rootView =  inflater.inflate(R.layout.caldaia_report, container, false);

        realm.beginTransaction();
        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();

        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

/*        reportModelCaldaia = realm.where(ReportModelCaldaia.class).equalTo("id_sopralluogo", idSopralluogo).findFirst();
        RealmResults<ReportModelCaldaia> caldaieModels = realm.where(ReportModelCaldaia.class).findAll();

        if (reportStates != null)
        {
            if (reportModelCaldaia == null)
            {
                reportModelCaldaia = new ReportModelCaldaia(caldaieModels.size());
                reportModelCaldaia.setIdSopralluogo(idSopralluogo);
                realm.copyToRealmOrUpdate(reportModelCaldaia);
            }
        }*/
        realm.commitTransaction();

        return rootView;
    }
}
