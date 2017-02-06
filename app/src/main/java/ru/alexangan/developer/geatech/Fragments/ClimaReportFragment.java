package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Models.ClimaReportModel;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.visitItems;

public class ClimaReportFragment extends Fragment implements View.OnClickListener
{
    private int selectedIndex;
    int idSopralluogo;
    ReportStates reportStates;
    View rootView;
    Context context;
    ClimaReportModel climaReportModel;

    private LinearLayout llHeaderThreeRadiosAndEdit1, llSectionThreeRadiosAndEdit1, llHeaderTwoRadios1, llSectionTwoRadios1,
            llHeaderFourRadiosAndEdit1, llSectionFourRadiosAndEdit1, llHeaderThreeChkboxesAndEdit1, llSectionThreeChkboxesAndEdit1;
    private TextView tvReportTitle, tv1TwoTextTwoEdit1, tv2TwoTextTwoEdit1, tvFirstSectionTitle, tvSectionHeader2,
            tvHeaderThreeRadiosAndEdit1, tvHeaderTwoRadios1, tvHeaderFourRadiosAndEdit1, tvHeaderThreeChkboxesAndEdit1;
    private RadioGroup rg1ThreeRadiosAndEdit1, rg1TwoRadios1, rg1FourRadiosAndEdit1;
    private EditText et1ThreeRadiosAndEdit1, et1FourRadiosAndEdit1, et1ThreeChkboxesAndEdit1, et1TwoTextTwoEdit1, et2TwoTextTwoEdit1;
    CheckBox chk1ThreeChkboxesAndEdit1, chk2ThreeChkboxesAndEdit1, chk3ThreeChkboxesAndEdit1;
    ImageView ivArrowTwoRadios1;
    private ImageView ivArrowThreeRadiosAndEdit1;
    private ImageView ivArrowFourRadiosAndEdit1;
    private ImageView ivArrowThreeChkboxesAndEdit1;

    private final String strReportTitle = "Climatizzazione";

    private final String [] strSectionTitles = {"1. Rilievo dello stato dei luoghi", "2. Impianto"};

    private final String[] strSectionsTitles = new String[] {
            " 1.1 TIPO DI EDIFICIO:", " 1.2 TIPOLOGIA COSTRUTTIVA MURATURE:",
            " 1.3 POSIZIONAMENTO UNITÀ ESTERNA:", " 1.4 LOCALI E/O PIANI DELL'EDIFICIO:"};

    private final String[] strTypesOfBuilding = new String[] {
             "Appartamento", "Villa(Singola/Multi)", "Negozio"};

    private final String[] strUnitOutdoorPosition = new String[] {
            "A Parete", "A Pavimento"};

    private final String[] strWallsType = new String[] {
            "Cemento Armato", "Mattoni Pieni", "Mattoni Forati", "Pietra"};

    private final String[] strBuildingPlan = new String[] {
            "Interrato", "Piano rialzato", "Piano Terra"};

    private final String[] strImpianto = new String[] { " 2.1 NOTE SUL LUOGO DI INSTALLAZIONE E INDIVIDUAZIONE DI EVENTUALI CRITICITÀ SUI COLLEGAMENTI IDRAULICI E/O SULLA GESTIONE DELL'UTENZA:",
            " 2.2 NOTE SULLA TIPOLOGIA DELL'IMPIANTO ESISTENTE (CALDAIA, ELEMENTI RADIANTI, TUBAZIONI, ETC.)\n" +
                    "DESCRIVERE COMA VERRÀ INTERFACCIATO IL SISTEMA ALL'UTENZA PRINCIPALEE I COMPONENTI CHE VERRANNO UTILIZZATI:"};

    ArrayList<String> saSectionTitles, saTypesOfBuilding, saUnitOutdoorPosition, saWallsType, saBuildingPlan;

    public ClimaReportFragment()
    {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportStates != null && climaReportModel !=null)
        {
            realm.beginTransaction();


                String strTypeOfBuilding = "";
                int checkedBtnId = rg1ThreeRadiosAndEdit1.getCheckedRadioButtonId();
                if (checkedBtnId != -1)
                {
                    RadioButton radioButton = (RadioButton) rg1ThreeRadiosAndEdit1.findViewById(checkedBtnId);
                    strTypeOfBuilding = radioButton.getText().toString();

                    Log.d("DEBUG", strTypeOfBuilding);
                }
                else
                {
                    strTypeOfBuilding = et1ThreeRadiosAndEdit1.getText().toString();
                }

            climaReportModel.setEtTypeOfBuilding(strTypeOfBuilding);


                String strWallsType = "";
                checkedBtnId = rg1FourRadiosAndEdit1.getCheckedRadioButtonId();
                if (checkedBtnId != -1)
                {
                    RadioButton radioButton = (RadioButton) rg1FourRadiosAndEdit1.findViewById(checkedBtnId);
                    strWallsType = radioButton.getText().toString();

                    Log.d("DEBUG", strWallsType);
                }
                else
                {
                    strWallsType = et1FourRadiosAndEdit1.getText().toString();
                }
            climaReportModel.setEtWallsType(strWallsType);

            String strUnitOutdoorPosition = "";
            checkedBtnId = rg1TwoRadios1.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rg1TwoRadios1.findViewById(checkedBtnId);
                strUnitOutdoorPosition = radioButton.getText().toString();

                Log.d("DEBUG", strUnitOutdoorPosition);

                climaReportModel.setEtUnitOutdoorPositioning(strUnitOutdoorPosition);
            }

            String strAltroBuildingPlan = et1ThreeChkboxesAndEdit1.getText().toString();

            if(strAltroBuildingPlan == null || strAltroBuildingPlan.length() < 4)
            {
                String strUnderground = chk1ThreeChkboxesAndEdit1.isChecked() ? chk1ThreeChkboxesAndEdit1.getText().toString() : null;
                String strMezzanine = chk2ThreeChkboxesAndEdit1.isChecked() ? chk2ThreeChkboxesAndEdit1.getText().toString() : null;
                String strGroundFloor = chk3ThreeChkboxesAndEdit1.isChecked() ? chk3ThreeChkboxesAndEdit1.getText().toString() : null;
                climaReportModel.setEtBuildingPlan(strUnderground + "," + strMezzanine + "," + strGroundFloor);
            }
            else
            {
                climaReportModel.setEtAltroBuildingPlan(strAltroBuildingPlan);
            }

            String strNoteInstallationPlace = et1TwoTextTwoEdit1.getText().toString();

            climaReportModel.setEtNoteInstallationPlace(strNoteInstallationPlace);

            String strNoteExistingDev = et2TwoTextTwoEdit1.getText().toString();

            climaReportModel.setEtNoteExistingDev(strNoteExistingDev);


            if(strTypeOfBuilding.length() != 0)
            {
                reportStates.setReportCompletionState(1);

                if (strUnitOutdoorPosition.length() != 0 || strWallsType.length() != 0)
                {
                    reportStates.setReportCompletionState(2);

                    if (strTypeOfBuilding.length() != 0 && strUnitOutdoorPosition.length() != 0 && strWallsType.length() != 0)
                    {
                        reportStates.setReportCompletionState(3);

                        Calendar calendarNow = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                        String strDateTime = sdf.format(calendarNow.getTime());
                        reportStates.setDataOraRaportoCompletato(strDateTime);
                    }
                }
            }
            else
            {
                reportStates.setReportCompletionState(0);
                reportStates.setDataOraRaportoCompletato(null);
            }
        }
        realm.commitTransaction();

        if (reportStates == null)
        {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        realm.beginTransaction();
        VisitItem visitItem = visitItems.get(selectedIndex);
        VisitStates visitStates = visitItem.getVisitStates();
        idSopralluogo = visitStates.getIdSopralluogo();

        reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        climaReportModel = realm.where(ClimaReportModel.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        RealmResults <ClimaReportModel> climaReportModels = realm.where(ClimaReportModel.class).findAll();

        if (reportStates != null)
        {
            if (climaReportModel == null)
            {
                climaReportModel = new ClimaReportModel(climaReportModels.size(), idSopralluogo);
                //climaReportModel.setIdSopralluogo(idSopralluogo);
                realm.copyToRealmOrUpdate(climaReportModel);
            }
        }
        realm.commitTransaction();

        realm.beginTransaction();

        if (climaReportModel != null)
        {
            int i;
            String strTypeOfBuilding = climaReportModel.getEtTypeOfBuilding();

            if(strTypeOfBuilding != null)
            {
                for (i = 0; i < rg1ThreeRadiosAndEdit1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1ThreeRadiosAndEdit1.getChildAt(i);

                    if (strTypeOfBuilding.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }

                if (i == rg1ThreeRadiosAndEdit1.getChildCount())
                {
                    et1ThreeRadiosAndEdit1.setText(strTypeOfBuilding);
                }
            }

            String strWallsType = climaReportModel.getEtWallsType();
            if(strWallsType != null)
            {
                for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

                    if (strWallsType.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }

                if (i == rg1FourRadiosAndEdit1.getChildCount())
                {
                    et1FourRadiosAndEdit1.setText(strWallsType);
                }
            }

            String strUnitOutdoorPosition = climaReportModel.getEtUnitOutdoorPositioning();

            if(strUnitOutdoorPosition!=null)
            {
                for (i = 0; i < rg1TwoRadios1.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

                    if (strUnitOutdoorPosition.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }
            }

            if(climaReportModel.getEtBuildingPlan()!=null)
            {
                if (climaReportModel.getEtBuildingPlan().contains(chk1ThreeChkboxesAndEdit1.getText().toString()))
                {
                    chk1ThreeChkboxesAndEdit1.setChecked(true);
                }

                if (climaReportModel.getEtBuildingPlan().contains(chk2ThreeChkboxesAndEdit1.getText().toString()))
                {
                    chk2ThreeChkboxesAndEdit1.setChecked(true);
                }

                if (climaReportModel.getEtBuildingPlan().contains(chk3ThreeChkboxesAndEdit1.getText().toString()))
                {
                    chk3ThreeChkboxesAndEdit1.setChecked(true);
                }
            }
            if(climaReportModel.getEtAltroBuildingPlan() != null && climaReportModel.getEtAltroBuildingPlan().length() > 3)
            {
                et1ThreeChkboxesAndEdit1.setText(climaReportModel.getEtAltroBuildingPlan());
            }

            et1TwoTextTwoEdit1.setText(climaReportModel.getEtNoteInstallationPlace());
            et2TwoTextTwoEdit1.setText(climaReportModel.getEtNoteExistingDev());
        }
        realm.commitTransaction();

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

        saSectionTitles = new ArrayList<>();
        saTypesOfBuilding = new ArrayList<>();
        saUnitOutdoorPosition = new ArrayList<>();
        saWallsType = new ArrayList<>();
        saBuildingPlan = new ArrayList<>();

        saSectionTitles.addAll(Arrays.asList(strSectionsTitles));
        saTypesOfBuilding.addAll(Arrays.asList(strTypesOfBuilding));
        saUnitOutdoorPosition.addAll(Arrays.asList(strUnitOutdoorPosition));
        saWallsType.addAll(Arrays.asList(strWallsType));
        saBuildingPlan.addAll(Arrays.asList(strBuildingPlan));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        int sectionNumber = 0;
        rootView =  inflater.inflate(R.layout.climatizzazione_report, container, false);

        tvReportTitle = (TextView) rootView.findViewById(R.id.tvReportTitle);

        tvReportTitle.setText(strReportTitle);

        tvFirstSectionTitle = (TextView) rootView.findViewById(R.id.tvSectionHeader1);
        tvFirstSectionTitle.setText(strSectionsTitles[0]);


        llHeaderThreeRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llHeaderThreeRadiosAndEdit1);
        llHeaderThreeRadiosAndEdit1.setOnClickListener(this);

        ivArrowThreeRadiosAndEdit1 = (ImageView) rootView.findViewById(R.id.ivArrowThreeRadiosAndEdit1);

        tvHeaderThreeRadiosAndEdit1 = (TextView) rootView.findViewById(R.id.tvHeaderThreeRadiosAndEdit1);

        tvHeaderThreeRadiosAndEdit1.setText(saSectionTitles.get(sectionNumber++));

        llSectionThreeRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionThreeRadiosAndEdit1);
        
        rg1ThreeRadiosAndEdit1 = (RadioGroup) rootView.findViewById(R.id.rg1ThreeRadiosAndEdit1);

        int i;
        for (i = 0; i < rg1ThreeRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1ThreeRadiosAndEdit1.getChildAt(i);

            rb.setText(saTypesOfBuilding.get(i));
        }

        et1ThreeRadiosAndEdit1 = (EditText) rootView.findViewById(R.id.et1ThreeRadiosAndEdit1);
        //et1ThreeRadiosAndEdit1.setOnClickListener(this);
        et1ThreeRadiosAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1ThreeRadiosAndEdit1.clearCheck();
                return false;
            }
        });
        //et1ThreeRadiosAndEdit1.setText(saTypesOfBuilding.get(i));


        llHeaderFourRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llHeaderFourRadiosAndEdit1);
        llHeaderFourRadiosAndEdit1.setOnClickListener(this);

        ivArrowFourRadiosAndEdit1 = (ImageView) rootView.findViewById(R.id.ivArrowFourRadiosAndEdit1);

        tvHeaderFourRadiosAndEdit1 = (TextView) rootView.findViewById(R.id.tvHeaderFourRadiosAndEdit1);

        tvHeaderFourRadiosAndEdit1.setText(saSectionTitles.get(sectionNumber++));

        llSectionFourRadiosAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionFourRadiosAndEdit1);

        rg1FourRadiosAndEdit1 = (RadioGroup) rootView.findViewById(R.id.rg1FourRadiosAndEdit1);

        for (i = 0; i < rg1FourRadiosAndEdit1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1FourRadiosAndEdit1.getChildAt(i);

            rb.setText(saWallsType.get(i));
        }

        et1FourRadiosAndEdit1 = (EditText) rootView.findViewById(R.id.et1FourRadiosAndEdit1);
        //et1FourRadiosAndEdit1.setOnClickListener(this);
        et1FourRadiosAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rg1FourRadiosAndEdit1.clearCheck();
                return false;
            }
        });
        //et1FourRadiosAndEdit1.setText(saWallsType.get(i));


        llHeaderTwoRadios1 = (LinearLayout) rootView.findViewById(R.id.llHeaderTwoRadios1);
        llHeaderTwoRadios1.setOnClickListener(this);

        tvHeaderTwoRadios1 = (TextView) rootView.findViewById(R.id.tvHeaderTwoRadios1);

        tvHeaderTwoRadios1.setText(saSectionTitles.get(sectionNumber++));

        ivArrowTwoRadios1 = (ImageView) rootView.findViewById(R.id.ivArrowTwoRadios1);

        llSectionTwoRadios1 = (LinearLayout) rootView.findViewById(R.id.llSectionTwoRadios1);
        
        rg1TwoRadios1 = (RadioGroup) rootView.findViewById(R.id.rg1TwoRadios1);

        for (i = 0; i < rg1TwoRadios1.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rg1TwoRadios1.getChildAt(i);

            rb.setText(saUnitOutdoorPosition.get(i));
        }

        llHeaderThreeChkboxesAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llHeaderThreeChkboxesAndEdit1);
        llHeaderThreeChkboxesAndEdit1.setOnClickListener(this);

        ivArrowThreeChkboxesAndEdit1 = (ImageView) rootView.findViewById(R.id.ivArrowThreeChkboxesAndEdit1);

        tvHeaderThreeChkboxesAndEdit1 = (TextView) rootView.findViewById(R.id.tvHeaderThreeChkboxesAndEdit1);

        tvHeaderThreeChkboxesAndEdit1.setText(saSectionTitles.get(sectionNumber++));

        llSectionThreeChkboxesAndEdit1 = (LinearLayout) rootView.findViewById(R.id.llSectionThreeChkboxesAndEdit1);

            chk1ThreeChkboxesAndEdit1 = (CheckBox) rootView.findViewById(R.id.chk1ThreeChkboxesAndEdit1);
        chk1ThreeChkboxesAndEdit1.setText(saBuildingPlan.get(0));
            chk2ThreeChkboxesAndEdit1 = (CheckBox) rootView.findViewById(R.id.chk2ThreeChkboxesAndEdit1);
        chk2ThreeChkboxesAndEdit1.setText(saBuildingPlan.get(1));
            chk3ThreeChkboxesAndEdit1 = (CheckBox) rootView.findViewById(R.id.chk3ThreeChkboxesAndEdit1);
        chk3ThreeChkboxesAndEdit1.setText(saBuildingPlan.get(2));

        et1ThreeChkboxesAndEdit1 = (EditText) rootView.findViewById(R.id.et1ThreeChkboxesAndEdit1);
        et1ThreeChkboxesAndEdit1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                chk1ThreeChkboxesAndEdit1.setChecked(false);
                chk2ThreeChkboxesAndEdit1.setChecked(false);
                chk3ThreeChkboxesAndEdit1.setChecked(false);
                return false;
            }
        });
        //et1ThreeChkboxesAndEdit1.setText(saBuildingPlan.get(3));

        tvSectionHeader2 = (TextView) rootView.findViewById(R.id.tvSectionHeader2);
        tvSectionHeader2.setText(strSectionsTitles[1]);

        tv1TwoTextTwoEdit1 = (TextView) rootView.findViewById(R.id.tv1TwoTextTwoEdit1);

        tv1TwoTextTwoEdit1.setText(strImpianto[0]);
        
        tv2TwoTextTwoEdit1 = (TextView) rootView.findViewById(R.id.tv2TwoTextTwoEdit1);

        tv2TwoTextTwoEdit1.setText(strImpianto[1]);

        et1TwoTextTwoEdit1 = (EditText) rootView.findViewById(R.id.et1TwoTextTwoEdit1);
        et2TwoTextTwoEdit1 = (EditText) rootView.findViewById(R.id.et2TwoTextTwoEdit1);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view == llHeaderThreeRadiosAndEdit1)
        {
            llSectionThreeRadiosAndEdit1.setVisibility(llSectionThreeRadiosAndEdit1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowThreeRadiosAndEdit1.setImageResource(llSectionThreeRadiosAndEdit1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderTwoRadios1)
        {
            llSectionTwoRadios1.setVisibility(llSectionTwoRadios1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowTwoRadios1.setImageResource(llSectionTwoRadios1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderFourRadiosAndEdit1)
        {
            llSectionFourRadiosAndEdit1.setVisibility(llSectionFourRadiosAndEdit1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowFourRadiosAndEdit1.setImageResource(llSectionFourRadiosAndEdit1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llHeaderThreeChkboxesAndEdit1)
        {
            llSectionThreeChkboxesAndEdit1.setVisibility(llSectionThreeChkboxesAndEdit1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivArrowThreeChkboxesAndEdit1.setImageResource(llSectionThreeChkboxesAndEdit1.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }
    }
}
