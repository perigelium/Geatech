package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

    private LinearLayout llThreeRadiosSectionName, llThreeRadios, llTwoRadiosSectionName, llTwoRadios,
            llFourRadiosSectionName, llFourRadios, llThreeChkboxesSectionName, llThreeChkboxes;
    private TextView tvReportTitle, tvTwoTextTwoEdit1, tvTwoTextTwoEdit2, tvFirstSectionTitle,
            tvTypeOfBuilding, tvUnitOutdoorPositioning, tvWallsType, tvBuildingPlan;
    private RadioGroup rgTypeOfBuilding, rgUnitOutdoorPositioning, rgWallsType;
    private EditText etTypeOfBuilding, etWallsType, etBuildingPlan, etNoteInstallationPlace, etNoteExistingDev;
    CheckBox chkUnderground, chkMezzanine, chkGroundFloor;

    private final String strReportTitle = "Climatizzazione";

    private final String strFirstSectionTitle = "1.Rilievo dello stato dei luoghi";

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

            String strTypeOfBuilding = etTypeOfBuilding.getText().toString();

            if(strTypeOfBuilding.equals("Inserire") || strTypeOfBuilding.equals("Altro"))
            {
                strTypeOfBuilding = "";
                int checkedBtnId = rgTypeOfBuilding.getCheckedRadioButtonId();
                if (checkedBtnId != -1)
                {
                    RadioButton radioButton = (RadioButton) rgTypeOfBuilding.findViewById(checkedBtnId);
                    strTypeOfBuilding = radioButton.getText().toString();

                    Log.d("DEBUG", strTypeOfBuilding);
                }
            }
            climaReportModel.setEtTypeOfBuilding(strTypeOfBuilding);


            String strWallsType = etWallsType.getText().toString();

            if(strTypeOfBuilding.equals("inserire"))
            {
                strWallsType = "";
                int checkedBtnId = rgWallsType.getCheckedRadioButtonId();
                if (checkedBtnId != -1)
                {
                    RadioButton radioButton = (RadioButton) rgWallsType.findViewById(checkedBtnId);
                    strWallsType = radioButton.getText().toString();

                    Log.d("DEBUG", strWallsType);
                }
            }
            climaReportModel.setEtWallsType(strWallsType);

            String strUnitOutdoorPosition = "";
            int checkedBtnId = rgUnitOutdoorPositioning.getCheckedRadioButtonId();
            if (checkedBtnId != -1)
            {
                RadioButton radioButton = (RadioButton) rgUnitOutdoorPositioning.findViewById(checkedBtnId);
                strUnitOutdoorPosition = radioButton.getText().toString();

                Log.d("DEBUG", strUnitOutdoorPosition);
            }
            climaReportModel.setEtUnitOutdoorPositioning(strUnitOutdoorPosition);

            String strUnderground = chkUnderground.getText().toString();
            String strMezzanine = chkMezzanine.getText().toString();
            String strGroundFloor = chkGroundFloor.getText().toString();

            climaReportModel.setEtBuildingPlan(strUnderground + " " + strMezzanine + " " + strGroundFloor);


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
                climaReportModel = new ClimaReportModel(climaReportModels.size());
                climaReportModel.setIdSopralluogo(idSopralluogo);
                realm.copyToRealmOrUpdate(climaReportModel);
            }
        }
        realm.commitTransaction();

        realm.beginTransaction();

        if (climaReportModel != null)
        {

            String strTypeOfBuilding = climaReportModel.getEtTypeOfBuilding();
            String strWallsType = climaReportModel.getEtWallsType();
            String strUnitOutdoorPosition = climaReportModel.getEtUnitOutdoorPositioning();
            String strBuildingPlan = climaReportModel.getEtBuildingPlan();

            int i;
            for (i = 0; i < rgTypeOfBuilding.getChildCount(); i++)
            {
                RadioButton rb = (RadioButton) rgTypeOfBuilding.getChildAt(i);

                if(strTypeOfBuilding.equals(rb.getText().toString()))
                {
                    rb.setChecked(true);
                    break;
                }
            }

            if(i == rgTypeOfBuilding.getChildCount())
            {
                etTypeOfBuilding.setText(strTypeOfBuilding);
            }


/*                atvTipoDiEdificio.setSelection(posTipoDiEdificio, false);
            atvPosizionamentoUnitaEsterna.setSelection(posPosizionamentoUnitaEsterna, false);
            atvTipologiaCostruttivaMurature.setSelection(posTipologiaCostruttivaMurature, false);
            atvLocaliEOPianiDelledificio.setSelection(posLocaliEOPianiDelledificio, false);*//*

            atvTipoDiEdificio.post(new Runnable() {
                public void run() {
                    atvTipoDiEdificio.setSelection(posTipoDiEdificio, true);
                }
            });
            atvPosizionamentoUnitaEsterna.post(new Runnable() {
                public void run() {
                    atvPosizionamentoUnitaEsterna.setSelection(posPosizionamentoUnitaEsterna, true);
                }
            });
            atvTipologiaCostruttivaMurature.post(new Runnable() {
                public void run() {
                    atvTipologiaCostruttivaMurature.setSelection(posTipologiaCostruttivaMurature, true);
                }
            });
            atvLocaliEOPianiDelledificio.post(new Runnable() {
                public void run() {
                    atvLocaliEOPianiDelledificio.setSelection(posLocaliEOPianiDelledificio, true);
                }
            });

                //atvPosizionamentoUnitaEsterna.setSelection(posizionamentiUnitaEsterna.indexOf(climaReportModel.getPosizionamentoUnitaEsterna()));
                //atvTipologiaCostruttivaMurature.setSelection(tipologieCostruttiveMurature.indexOf(climaReportModel.getTipologiaCostruttivaMurature()));
                //atvLocaliEOPianiDelledificio.setSelection(localiEOPianiDelledificio.indexOf(climaReportModel.getLocaliEOPianiDelledificio()));

                etNoteSulLuoghoDiInstallazione.setText(climaReportModel.getNoteSulLuoghoDiInstallazione());
                etNoteSulTipologiaDellImpianto.setText(climaReportModel.getNoteSulTipologiaDellImpianto());
                etNoteRelativeAlCollegamento.setText(climaReportModel.getNoteRelativeAlCollegamento());*/
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

        tvFirstSectionTitle = (TextView) rootView.findViewById(R.id.tvFirstSectionTitle);

        tvFirstSectionTitle.setText(strFirstSectionTitle);


        llThreeRadiosSectionName = (LinearLayout) rootView.findViewById(R.id.llThreeRadiosSectionName);
        llThreeRadiosSectionName.setOnClickListener(this);

        tvTypeOfBuilding = (TextView) rootView.findViewById(R.id.tvThreeRadios);

        tvTypeOfBuilding.setText(saSectionTitles.get(sectionNumber++));

        llThreeRadios = (LinearLayout) rootView.findViewById(R.id.llThreeRadios);
        
        rgTypeOfBuilding = (RadioGroup) rootView.findViewById(R.id.rgThreeRadios);

        int i;
        for (i = 0; i < rgTypeOfBuilding.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rgTypeOfBuilding.getChildAt(i);

            rb.setText(saTypesOfBuilding.get(i));
        }

        etTypeOfBuilding = (EditText) rootView.findViewById(R.id.etThreeChkboxes);
        etTypeOfBuilding.setOnClickListener(this);
        //etTypeOfBuilding.setText(saTypesOfBuilding.get(i));

        llFourRadiosSectionName = (LinearLayout) rootView.findViewById(R.id.llFourRadiosSectionName);
        llFourRadiosSectionName.setOnClickListener(this);

        tvWallsType = (TextView) rootView.findViewById(R.id.tvFourRadios);

        tvWallsType.setText(saSectionTitles.get(sectionNumber++));

        llFourRadios = (LinearLayout) rootView.findViewById(R.id.llFourRadios);

        rgWallsType = (RadioGroup) rootView.findViewById(R.id.rgFourRadios);

        for (i = 0; i < rgWallsType.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rgWallsType.getChildAt(i);

            rb.setText(saWallsType.get(i));
        }

        etWallsType = (EditText) rootView.findViewById(R.id.etFourRadios);
        etWallsType.setOnClickListener(this);
        //etWallsType.setText(saWallsType.get(i));


        llTwoRadiosSectionName = (LinearLayout) rootView.findViewById(R.id.llTwoRadiosSectionName);
        llTwoRadiosSectionName.setOnClickListener(this);

        tvUnitOutdoorPositioning = (TextView) rootView.findViewById(R.id.tvTwoRadios);

        tvUnitOutdoorPositioning.setText(saSectionTitles.get(sectionNumber++));

        llTwoRadios = (LinearLayout) rootView.findViewById(R.id.llTwoRadios);
        
        rgUnitOutdoorPositioning = (RadioGroup) rootView.findViewById(R.id.rgTwoRadios);

        for (i = 0; i < rgUnitOutdoorPositioning.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) rgUnitOutdoorPositioning.getChildAt(i);

            rb.setText(saUnitOutdoorPosition.get(i));
        }

        llThreeChkboxesSectionName = (LinearLayout) rootView.findViewById(R.id.llThreeChkboxesSectionName);
        llThreeChkboxesSectionName.setOnClickListener(this);

        tvBuildingPlan = (TextView) rootView.findViewById(R.id.tvThreeChkboxes);

        tvBuildingPlan.setText(saSectionTitles.get(sectionNumber++));

        llThreeChkboxes = (LinearLayout) rootView.findViewById(R.id.llThreeChkboxes);

            chkUnderground = (CheckBox) rootView.findViewById(R.id.chkThreeChkboxes1);
        chkUnderground.setText(saBuildingPlan.get(0));
            chkMezzanine = (CheckBox) rootView.findViewById(R.id.chkThreeChkboxes2);
        chkMezzanine.setText(saBuildingPlan.get(1));
            chkGroundFloor = (CheckBox) rootView.findViewById(R.id.chkThreeChkboxes3);
        chkGroundFloor.setText(saBuildingPlan.get(2));

        etBuildingPlan = (EditText) rootView.findViewById(R.id.etThreeChkboxes);
        //etBuildingPlan.setText(saBuildingPlan.get(3));

        tvTwoTextTwoEdit1 = (TextView) rootView.findViewById(R.id.tvTwoTextTwoEdit1);

        tvTwoTextTwoEdit1.setText(strImpianto[0]);
        
        tvTwoTextTwoEdit2 = (TextView) rootView.findViewById(R.id.tvTwoTextTwoEdit2);

        tvTwoTextTwoEdit2.setText(strImpianto[1]);

        etNoteInstallationPlace = (EditText) rootView.findViewById(R.id.etTwoTextTwoEdit1);
        etNoteExistingDev = (EditText) rootView.findViewById(R.id.etTwoTextTwoEdit2);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view == etTypeOfBuilding)
        {
          rgTypeOfBuilding.clearCheck();
        }

        if (view == etWallsType)
        {
            rgWallsType.clearCheck();
        }


        if (view == llThreeRadiosSectionName)
        {
            llThreeRadios.setVisibility(llThreeRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }

        if (view == llTwoRadiosSectionName)
        {
            llTwoRadios.setVisibility(llTwoRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }

        if (view == llFourRadiosSectionName)
        {
            llFourRadios.setVisibility(llFourRadios.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }

        if (view == llThreeChkboxesSectionName)
        {
            llThreeChkboxes.setVisibility(llThreeChkboxes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }
}
