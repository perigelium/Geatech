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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.EditText;

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

public class ClimaReportFragment extends Fragment //implements View.OnTouchListener
{
    private int selectedIndex;
    int idSopralluogo;
    ReportStates reportStates;
    View rootView;

    Context context;
    ClimaReportModel climaReportModel;

    RadioGroup rgTypeOfBuilding, rgUnitOutdoorPositioning, rgWallsType, rgBuildingPlan;
    EditText etTypeOfBuilding, etUnitOutdoorPositioning, etWallsType, etBuildingPlan, etNoteInstallationPlace, etNoteExistingDev;

/*    Spinner atvTipoDiEdificio, atvPosizionamentoUnitaEsterna,
            atvTipologiaCostruttivaMurature, atvLocaliEOPianiDelledificio;

    EditText etNoteSulLuoghoDiInstallazione, etNoteSulTipologiaDellImpianto, etNoteRelativeAlCollegamento;*/

/*    private final String[] TipiDiEdificieStrA = new String[] {
             "Appartamento", "Villa(Singola/Multi)", "Negozio", "Altro"};

    private final String[] PosizionamentiUnitaEsternaStrA = new String[] {
            "A Parete", "A Pavimento"};

    private final String[] TipologieCostruttiveMuratureStrA = new String[] {
            "Cemento Armato", "Mattoni Pieni", "Mattoni Forati", "Pietra", "Altro"};

    private final String[] LocaliEOPianiDelledificioStrA = new String[] {
            "Interrato", "Piano rialzato", "Piano Terra", "Altro"};

    ArrayList<String> tipiDiEdificie, posizionamentiUnitaEsterna, tipologieCostruttiveMurature, localiEOPianiDelledificio;*/


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

        int checkedBtnId = rgTypeOfBuilding.getCheckedRadioButtonId();

        if(checkedBtnId != -1)
        {
            RadioButton radioButton = (RadioButton) rgTypeOfBuilding.findViewById(checkedBtnId);
            String text = radioButton.getText().toString();

            Log.d("DEBUG", text);
        }

        if (reportStates != null && climaReportModel !=null)
        {
            realm.beginTransaction();

/*            String tipoDiEdificio = atvTipoDiEdificio.getSelectedItem().toString();
            String posizionamentoUnitaEsterna = atvPosizionamentoUnitaEsterna.getSelectedItem().toString();
            String tipologiaCostruttivaMurature = atvTipologiaCostruttivaMurature.getSelectedItem().toString();
            String localiEOPianiDelledificio = atvLocaliEOPianiDelledificio.getSelectedItem().toString();

            climaReportModel.setTipoDiEdificio(atvTipoDiEdificio.getSelectedItem().toString());
            climaReportModel.setPosizionamentoUnitaEsterna(atvPosizionamentoUnitaEsterna.getSelectedItem().toString());
            climaReportModel.setTipologiaCostruttivaMurature(atvTipologiaCostruttivaMurature.getSelectedItem().toString());
            climaReportModel.setLocaliEOPianiDelledificio(atvLocaliEOPianiDelledificio.getSelectedItem().toString());

            climaReportModel.setNoteSulLuoghoDiInstallazione(etNoteSulLuoghoDiInstallazione.getText().toString());
            climaReportModel.setNoteSulTipologiaDellImpianto(etNoteSulTipologiaDellImpianto.getText().toString());
            climaReportModel.setNoteRelativeAlCollegamento(etNoteRelativeAlCollegamento.getText().toString());

            if(climaReportModel.getTipoDiEdificio().length() != 0 || climaReportModel.getNoteSulLuoghoDiInstallazione().length() != 0
                    || climaReportModel.getNoteRelativeAlCollegamento().length() != 0 || climaReportModel.getNoteSulTipologiaDellImpianto().length() != 0
                    )
            {
                reportStates.setReportCompletionState(1);

                if (climaReportModel.getNoteSulLuoghoDiInstallazione().length() != 0 || climaReportModel.getNoteRelativeAlCollegamento().length() != 0 || climaReportModel.getNoteSulTipologiaDellImpianto().length() != 0)
                {
                    reportStates.setReportCompletionState(2);

                    if (climaReportModel.getTipoDiEdificio().length() != 0 && climaReportModel.getNoteSulLuoghoDiInstallazione().length() != 0 &&
                            climaReportModel.getNoteRelativeAlCollegamento().length() != 0 && climaReportModel.getNoteSulTipologiaDellImpianto().length() != 0)
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
            }*/
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

        if (climaReportModel != null)
        {

/*            String tipoDiEdificioStr = climaReportModel.getTipoDiEdificio();
            String posizionamentoUnitaEsternaStr = climaReportModel.getPosizionamentoUnitaEsterna();
            String tipologiaCostruttivaMuratureStr = climaReportModel.getTipologiaCostruttivaMurature();
            String localiEOPianiDelledificioStr = climaReportModel.getLocaliEOPianiDelledificio();

            final int posTipoDiEdificio = tipiDiEdificie.indexOf(tipoDiEdificioStr);
            final int posPosizionamentoUnitaEsterna = posizionamentiUnitaEsterna.indexOf(posizionamentoUnitaEsternaStr);
            final int posTipologiaCostruttivaMurature = tipologieCostruttiveMurature.indexOf(tipologiaCostruttivaMuratureStr);
            final int posLocaliEOPianiDelledificio = localiEOPianiDelledificio.indexOf(localiEOPianiDelledificioStr);

*//*                atvTipoDiEdificio.setSelection(posTipoDiEdificio, false);
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

/*        tipiDiEdificie = new ArrayList<>();
        posizionamentiUnitaEsterna = new ArrayList<>();
        tipologieCostruttiveMurature = new ArrayList<>();
        localiEOPianiDelledificio = new ArrayList<>();

        tipiDiEdificie.addAll(Arrays.asList(TipiDiEdificieStrA));
        posizionamentiUnitaEsterna.addAll(Arrays.asList(PosizionamentiUnitaEsternaStrA));
        tipologieCostruttiveMurature.addAll(Arrays.asList(TipologieCostruttiveMuratureStrA));
        localiEOPianiDelledificio.addAll(Arrays.asList(LocaliEOPianiDelledificioStrA));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView =  inflater.inflate(R.layout.climatizzazione_report, container, false);

/*        ArrayAdapter<String> TipiDiEdificieAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, tipiDiEdificie);

        ArrayAdapter<String> PosizionamentiUnitaEsternaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, posizionamentiUnitaEsterna);

        ArrayAdapter<String> TipologieCostruttiveMuratureAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, tipologieCostruttiveMurature);

        ArrayAdapter<String> LocaliEOPianiDelledificioAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, localiEOPianiDelledificio);*/

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

        rgTypeOfBuilding = (RadioGroup) rootView.findViewById(R.id.rgTypeOfBuilding);


/*        atvTipoDiEdificio = null;
        atvTipoDiEdificio = (Spinner) rootView.findViewById(R.id.atvTipoDiEdificio);

        //atvTipoDiEdificio.setListSelection(tipiDiEdificie.indexOf(climaReportModel.getTipoDiEdificio()));

        atvTipoDiEdificio.setAdapter(TipiDiEdificieAdapter);

        //atvTipoDiEdificio.setOnTouchListener(this);
        //atvTipoDiEdificio.setInputType(InputType.TYPE_NULL);

        atvTipoDiEdificio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                realm.beginTransaction();
                climaReportModel.setTipoDiEdificio(selectedItem);
                realm.commitTransaction();

                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        atvPosizionamentoUnitaEsterna = null;
        atvPosizionamentoUnitaEsterna = (Spinner) rootView.findViewById(R.id.atvPosizionamentoUnitaEsterna);
        //atvPosizionamentoUnitaEsterna.setText(climaReportModel.getPosizionamentoUnitaEsterna());

        //atvPosizionamentoUnitaEsterna.setListSelection(posizionamentiUnitaEsterna.indexOf(climaReportModel.getTipoDiEdificio()));

        atvPosizionamentoUnitaEsterna.setAdapter(PosizionamentiUnitaEsternaAdapter);
        //atvPosizionamentoUnitaEsterna.setOnTouchListener(this);
        //atvPosizionamentoUnitaEsterna.setInputType(InputType.TYPE_NULL);

        atvPosizionamentoUnitaEsterna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                realm.beginTransaction();
                climaReportModel.setPosizionamentoUnitaEsterna(selectedItem);
                realm.commitTransaction();
                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        atvTipologiaCostruttivaMurature = null;
        atvTipologiaCostruttivaMurature = (Spinner) rootView.findViewById(R.id.atvTipologiaCostruttivaMurature);

        //atvTipologiaCostruttivaMurature.setListSelection(tipologieCostruttiveMurature.indexOf(climaReportModel.getTipoDiEdificio()));

        //atvTipologiaCostruttivaMurature.setText(climaReportModel.getTipologiaCostruttivaMurature());
        atvTipologiaCostruttivaMurature.setAdapter(TipologieCostruttiveMuratureAdapter);
        //atvTipologiaCostruttivaMurature.setOnTouchListener(this);
        //atvTipologiaCostruttivaMurature.setInputType(InputType.TYPE_NULL);

        atvTipologiaCostruttivaMurature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                realm.beginTransaction();
                climaReportModel.setTipologiaCostruttivaMurature(selectedItem);
                realm.commitTransaction();
                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        atvLocaliEOPianiDelledificio = null;
        atvLocaliEOPianiDelledificio = (Spinner) rootView.findViewById(R.id.atvLocaliEOPianiDelledificio);
        //atvLocaliEOPianiDelledificio.setText(climaReportModel.getLocaliEOPianiDelledificio());

        //atvLocaliEOPianiDelledificio.setListSelection(localiEOPianiDelledificio.indexOf(climaReportModel.getLocaliEOPianiDelledificio()));

        atvLocaliEOPianiDelledificio.setAdapter(LocaliEOPianiDelledificioAdapter);
        //atvLocaliEOPianiDelledificio.setOnTouchListener(this);
        //atvLocaliEOPianiDelledificio.setInputType(InputType.TYPE_NULL);

        atvLocaliEOPianiDelledificio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                realm.beginTransaction();
                climaReportModel.setLocaliEOPianiDelledificio(selectedItem);
                realm.commitTransaction();
                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        etNoteSulLuoghoDiInstallazione = (EditText) rootView.findViewById(R.id.etNoteSulLuoghoDiInstallazione);

        etNoteSulLuoghoDiInstallazione.setText(climaReportModel.getNoteSulLuoghoDiInstallazione());

        etNoteSulLuoghoDiInstallazione.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    realm.beginTransaction();
                    climaReportModel.setNoteSulLuoghoDiInstallazione(etNoteSulLuoghoDiInstallazione.getText().toString());
                    realm.commitTransaction();
                    //Toast.makeText(context, "onFocusChange etNoteSulLuoghoDiInstallazione", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etNoteSulTipologiaDellImpianto = (EditText) rootView.findViewById(R.id.etNoteSulTipologiaDellImpianto);

        etNoteSulTipologiaDellImpianto.setText(climaReportModel.getNoteSulTipologiaDellImpianto());

        etNoteSulTipologiaDellImpianto.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (v == etNoteSulTipologiaDellImpianto && !hasFocus)
                {
                    realm.beginTransaction();
                    climaReportModel.setNoteSulTipologiaDellImpianto(etNoteSulTipologiaDellImpianto.getText().toString());
                    realm.commitTransaction();
                    //Toast.makeText(context, "onFocusChange etNoteSulTipologiaDellImpianto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etNoteRelativeAlCollegamento = (EditText) rootView.findViewById(R.id.etNoteRelativeAlCollegamento);
        etNoteRelativeAlCollegamento.setText(climaReportModel.getNoteRelativeAlCollegamento());

        etNoteRelativeAlCollegamento.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (v == etNoteRelativeAlCollegamento && !hasFocus)
                {
                    realm.beginTransaction();
                    climaReportModel.setNoteRelativeAlCollegamento(etNoteRelativeAlCollegamento.getText().toString());
                    realm.commitTransaction();
                    //Toast.makeText(context, "onFocusChange etNoteRelativeAlCollegamento", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        return rootView;
    }
}
