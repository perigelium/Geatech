package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;
import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Models.ClimaReportModel;
import ru.alexangan.developer.geatech.Models.RealmString;
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

    private LinearLayout llThreeRadiosSectionName, llThreeRadios, llTwoRadiosSectionName, llTwoRadios,
            llFourRadiosSectionName, llFourRadios, llThreeChkboxesSectionName, llThreeChkboxes;
    private TextView tvReportTitle, tvTwoTextTwoEdit1, tvTwoTextTwoEdit2, tvFirstSectionTitle, tvSecondSectionTitle,
            tvTypeOfBuilding, tvUnitOutdoorPositioning, tvWallsType, tvBuildingPlan;
    private RadioGroup rgTypeOfBuilding, rgUnitOutdoorPositioning, rgWallsType;
    private EditText etTypeOfBuilding, etWallsType, etBuildingPlan, etNoteInstallationPlace, etNoteExistingDev;
    CheckBox chkUnderground, chkMezzanine, chkGroundFloor;
    ImageView ivTwoRadiosDropdownArrow;
    private ImageView ivThreeRadiosDropdownArrow;
    private ImageView ivFourRadiosDropdownArrow;
    private ImageView ivThreeChkboxesDropdownArrow;

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
                int checkedBtnId = rgTypeOfBuilding.getCheckedRadioButtonId();
                if (checkedBtnId != -1)
                {
                    RadioButton radioButton = (RadioButton) rgTypeOfBuilding.findViewById(checkedBtnId);
                    strTypeOfBuilding = radioButton.getText().toString();

                    Log.d("DEBUG", strTypeOfBuilding);
                }
                else
                {
                    strTypeOfBuilding = etTypeOfBuilding.getText().toString();
                }

            climaReportModel.setEtTypeOfBuilding(strTypeOfBuilding);


                String strWallsType = "";
                checkedBtnId = rgWallsType.getCheckedRadioButtonId();
                if (checkedBtnId != -1)
                {
                    RadioButton radioButton = (RadioButton) rgWallsType.findViewById(checkedBtnId);
                    strWallsType = radioButton.getText().toString();

                    Log.d("DEBUG", strWallsType);
                }
                else
                {
                    strWallsType = etWallsType.getText().toString();
                }
            climaReportModel.setEtWallsType(strWallsType);

            String strUnitOutdoorPosition = "";
            checkedBtnId = rgUnitOutdoorPositioning.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rgUnitOutdoorPositioning.findViewById(checkedBtnId);
                strUnitOutdoorPosition = radioButton.getText().toString();

                Log.d("DEBUG", strUnitOutdoorPosition);

                climaReportModel.setEtUnitOutdoorPositioning(strUnitOutdoorPosition);
            }

            String strAltroBuildingPlan = etBuildingPlan.getText().toString();

            if(strAltroBuildingPlan == null || strAltroBuildingPlan.length() < 4)
            {
                String strUnderground = chkUnderground.isChecked() ? chkUnderground.getText().toString() : null;
                String strMezzanine = chkMezzanine.isChecked() ? chkMezzanine.getText().toString() : null;
                String strGroundFloor = chkGroundFloor.isChecked() ? chkGroundFloor.getText().toString() : null;
                climaReportModel.setEtBuildingPlan(strUnderground + "," + strMezzanine + "," + strGroundFloor);
            }
            else
            {
                climaReportModel.setEtAltroBuildingPlan(strAltroBuildingPlan);
            }

            String strNoteInstallationPlace = etNoteInstallationPlace.getText().toString();

            climaReportModel.setEtNoteInstallationPlace(strNoteInstallationPlace);

            String strNoteExistingDev = etNoteExistingDev.getText().toString();

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
                for (i = 0; i < rgTypeOfBuilding.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rgTypeOfBuilding.getChildAt(i);

                    if (strTypeOfBuilding.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }

                if (i == rgTypeOfBuilding.getChildCount())
                {
                    etTypeOfBuilding.setText(strTypeOfBuilding);
                }
            }

            String strWallsType = climaReportModel.getEtWallsType();
            if(strWallsType != null)
            {
                for (i = 0; i < rgWallsType.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rgWallsType.getChildAt(i);

                    if (strWallsType.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }

                if (i == rgWallsType.getChildCount())
                {
                    etWallsType.setText(strWallsType);
                }
            }

            String strUnitOutdoorPosition = climaReportModel.getEtUnitOutdoorPositioning();

            if(strUnitOutdoorPosition!=null)
            {
                for (i = 0; i < rgUnitOutdoorPositioning.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) rgUnitOutdoorPositioning.getChildAt(i);

                    if (strUnitOutdoorPosition.equals(rb.getText().toString()))
                    {
                        rb.setChecked(true);
                        break;
                    }
                }
            }

            if(climaReportModel.getEtBuildingPlan()!=null)
            {
                if (climaReportModel.getEtBuildingPlan().contains(chkUnderground.getText().toString()))
                {
                    chkUnderground.setChecked(true);
                }

                if (climaReportModel.getEtBuildingPlan().contains(chkMezzanine.getText().toString()))
                {
                    chkMezzanine.setChecked(true);
                }

                if (climaReportModel.getEtBuildingPlan().contains(chkGroundFloor.getText().toString()))
                {
                    chkGroundFloor.setChecked(true);
                }
            }
            if(climaReportModel.getEtAltroBuildingPlan() != null && climaReportModel.getEtAltroBuildingPlan().length() > 3)
            {
                etBuildingPlan.setText(climaReportModel.getEtAltroBuildingPlan());
            }

            etNoteInstallationPlace.setText(climaReportModel.getEtNoteInstallationPlace());
            etNoteExistingDev.setText(climaReportModel.getEtNoteExistingDev());
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


        llThreeRadiosSectionName = (LinearLayout) rootView.findViewById(R.id.llSectionThreeRadiosAndEdit1);
        llThreeRadiosSectionName.setOnClickListener(this);

        ivThreeRadiosDropdownArrow = (ImageView) rootView.findViewById(R.id.ivArrowThreeRadiosAndEdit1);

        tvTypeOfBuilding = (TextView) rootView.findViewById(R.id.tvSectionThreeRadiosAndEdit1);

        tvTypeOfBuilding.setText(saSectionTitles.get(sectionNumber++));

        llThreeRadios = (LinearLayout) rootView.findViewById(R.id.llThreeRadiosAndEdit1);
        
        rgTypeOfBuilding = (RadioGroup) rootView.findViewById(R.id.rgThreeRadiosAndEdit1);

        int i;
        for (i = 0; i < rgTypeOfBuilding.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rgTypeOfBuilding.getChildAt(i);

            rb.setText(saTypesOfBuilding.get(i));
        }

        etTypeOfBuilding = (EditText) rootView.findViewById(R.id.et1ThreeRadiosAndEdit1);
        //etTypeOfBuilding.setOnClickListener(this);
        etTypeOfBuilding.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rgTypeOfBuilding.clearCheck();
                return false;
            }
        });
        //etTypeOfBuilding.setText(saTypesOfBuilding.get(i));


        llFourRadiosSectionName = (LinearLayout) rootView.findViewById(R.id.llSectionFourRadiosAndEdit1);
        llFourRadiosSectionName.setOnClickListener(this);

        ivFourRadiosDropdownArrow = (ImageView) rootView.findViewById(R.id.ivArrowFourRadiosAndEdit1);

        tvWallsType = (TextView) rootView.findViewById(R.id.tvSectionFourRadiosAndEdit1);

        tvWallsType.setText(saSectionTitles.get(sectionNumber++));

        llFourRadios = (LinearLayout) rootView.findViewById(R.id.llFourRadiosAndEdit1);

        rgWallsType = (RadioGroup) rootView.findViewById(R.id.rgFourRadiosAndEdit1);

        for (i = 0; i < rgWallsType.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rgWallsType.getChildAt(i);

            rb.setText(saWallsType.get(i));
        }

        etWallsType = (EditText) rootView.findViewById(R.id.etFourRadiosAndEdit1);
        //etWallsType.setOnClickListener(this);
        etWallsType.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                rgWallsType.clearCheck();
                return false;
            }
        });
        //etWallsType.setText(saWallsType.get(i));


        llTwoRadiosSectionName = (LinearLayout) rootView.findViewById(R.id.llSectionTwoRadios1);
        llTwoRadiosSectionName.setOnClickListener(this);

        tvUnitOutdoorPositioning = (TextView) rootView.findViewById(R.id.tvSectionTwoRadios1);

        tvUnitOutdoorPositioning.setText(saSectionTitles.get(sectionNumber++));

        ivTwoRadiosDropdownArrow = (ImageView) rootView.findViewById(R.id.ivArrowTwoRadios1);

        llTwoRadios = (LinearLayout) rootView.findViewById(R.id.llTwoRadios1);
        
        rgUnitOutdoorPositioning = (RadioGroup) rootView.findViewById(R.id.rgTwoRadios1);

        for (i = 0; i < rgUnitOutdoorPositioning.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rgUnitOutdoorPositioning.getChildAt(i);

            rb.setText(saUnitOutdoorPosition.get(i));
        }

        llThreeChkboxesSectionName = (LinearLayout) rootView.findViewById(R.id.llSectionThreeChkboxesAndEdit1);
        llThreeChkboxesSectionName.setOnClickListener(this);

        ivThreeChkboxesDropdownArrow = (ImageView) rootView.findViewById(R.id.ivArrowThreeChkboxesAndEdit1);

        tvBuildingPlan = (TextView) rootView.findViewById(R.id.tvSectionThreeChkboxesAndEdit1);

        tvBuildingPlan.setText(saSectionTitles.get(sectionNumber++));

        llThreeChkboxes = (LinearLayout) rootView.findViewById(R.id.llThreeChkboxesAndEdit1);

            chkUnderground = (CheckBox) rootView.findViewById(R.id.chk1ThreeChkboxesAndEdit1);
        chkUnderground.setText(saBuildingPlan.get(0));
            chkMezzanine = (CheckBox) rootView.findViewById(R.id.chk2ThreeChkboxesAndEdit1);
        chkMezzanine.setText(saBuildingPlan.get(1));
            chkGroundFloor = (CheckBox) rootView.findViewById(R.id.chk3ThreeChkboxesAndEdit1);
        chkGroundFloor.setText(saBuildingPlan.get(2));

        etBuildingPlan = (EditText) rootView.findViewById(R.id.et1ThreeChkboxesAndEdit1);
        etBuildingPlan.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                chkUnderground.setChecked(false);
                chkMezzanine.setChecked(false);
                chkGroundFloor.setChecked(false);
                return false;
            }
        });
        //etBuildingPlan.setText(saBuildingPlan.get(3));

        tvSecondSectionTitle = (TextView) rootView.findViewById(R.id.tvSectionHeader2);
        tvSecondSectionTitle.setText(strSectionsTitles[1]);

        tvTwoTextTwoEdit1 = (TextView) rootView.findViewById(R.id.tv1TwoTextTwoEdit1);

        tvTwoTextTwoEdit1.setText(strImpianto[0]);
        
        tvTwoTextTwoEdit2 = (TextView) rootView.findViewById(R.id.tv2TwoTextTwoEdit1);

        tvTwoTextTwoEdit2.setText(strImpianto[1]);

        etNoteInstallationPlace = (EditText) rootView.findViewById(R.id.et1TwoTextTwoEdit1);
        etNoteExistingDev = (EditText) rootView.findViewById(R.id.et2TwoTextTwoEdit1);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view == llThreeRadiosSectionName)
        {
            llThreeRadios.setVisibility(llThreeRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivThreeRadiosDropdownArrow.setImageResource(llThreeRadios.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llTwoRadiosSectionName)
        {
            llTwoRadios.setVisibility(llTwoRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivTwoRadiosDropdownArrow.setImageResource(llTwoRadios.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llFourRadiosSectionName)
        {
            llFourRadios.setVisibility(llFourRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivFourRadiosDropdownArrow.setImageResource(llFourRadios.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        if (view == llThreeChkboxesSectionName)
        {
            llThreeChkboxes.setVisibility(llThreeChkboxes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            ivThreeChkboxesDropdownArrow.setImageResource(llThreeChkboxes.getVisibility() == View.VISIBLE
                    ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }
    }
}
