package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.Clima1Model;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.visitItems;

public class Clima1ReportFragment extends Fragment //implements View.OnTouchListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int selectedIndex;
    int idSopralluogo;
    ReportStates reportStates;
    View rootView;

    private OnFragmentInteractionListener mListener;
    Context context;
    Spinner atvTipoDiEdificio, atvPosizionamentoUnitaEsterna,
            atvTipologiaCostruttivaMurature, atvLocaliEOPianiDelledificio;

    EditText etNoteSulLuoghoDiInstallazione, etNoteSulTipologiaDellImpianto, etNoteRelativeAlCollegamento;

    Clima1Model clima1Model;

    private final String[] TipiDiEdificieStrA = new String[] {
             "Appartamento", "Villa(Singola/Multi)", "Negozio", "Altro"};

    private final String[] PosizionamentiUnitaEsternaStrA = new String[] {
            "A Parete", "A Pavimento"};

    private final String[] TipologieCostruttiveMuratureStrA = new String[] {
            "Cemento Armato", "Mattoni Pieni", "Mattoni Forati", "Pietra", "Altro"};

    private final String[] LocaliEOPianiDelledificioStrA = new String[] {
            "Interrato", "Piano rialzato", "Piano Terra", "Altro"};

    ArrayList<String> tipiDiEdificie, posizionamentiUnitaEsterna, tipologieCostruttiveMurature, localiEOPianiDelledificio;


    public Clima1ReportFragment()
    {
    }

    // TODO: Rename and change types and number of parameters
    public static SendReportFragment newInstance(String param1, String param2)
    {
        SendReportFragment fragment = new SendReportFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (reportStates != null && clima1Model!=null)
        {
            realm.beginTransaction();

            String tipoDiEdificio = atvTipoDiEdificio.getSelectedItem().toString();
            String posizionamentoUnitaEsterna = atvPosizionamentoUnitaEsterna.getSelectedItem().toString();
            String tipologiaCostruttivaMurature = atvTipologiaCostruttivaMurature.getSelectedItem().toString();
            String localiEOPianiDelledificio = atvLocaliEOPianiDelledificio.getSelectedItem().toString();

            clima1Model.setTipoDiEdificio(atvTipoDiEdificio.getSelectedItem().toString());
            clima1Model.setPosizionamentoUnitaEsterna(atvPosizionamentoUnitaEsterna.getSelectedItem().toString());
            clima1Model.setTipologiaCostruttivaMurature(atvTipologiaCostruttivaMurature.getSelectedItem().toString());
            clima1Model.setLocaliEOPianiDelledificio(atvLocaliEOPianiDelledificio.getSelectedItem().toString());

            clima1Model.setNoteSulLuoghoDiInstallazione(etNoteSulLuoghoDiInstallazione.getText().toString());
            clima1Model.setNoteSulTipologiaDellImpianto(etNoteSulTipologiaDellImpianto.getText().toString());
            clima1Model.setNoteRelativeAlCollegamento(etNoteRelativeAlCollegamento.getText().toString());

            if(clima1Model.getTipoDiEdificio().length() != 0 || clima1Model.getNoteSulLuoghoDiInstallazione().length() != 0
                    || clima1Model.getNoteRelativeAlCollegamento().length() != 0 || clima1Model.getNoteSulTipologiaDellImpianto().length() != 0
                    )
            {
                reportStates.setReportCompletionState(1);

                if (clima1Model.getNoteSulLuoghoDiInstallazione().length() != 0 || clima1Model.getNoteRelativeAlCollegamento().length() != 0 || clima1Model.getNoteSulTipologiaDellImpianto().length() != 0)
                {
                    reportStates.setReportCompletionState(2);

                    if (clima1Model.getTipoDiEdificio().length() != 0 && clima1Model.getNoteSulLuoghoDiInstallazione().length() != 0 &&
                            clima1Model.getNoteRelativeAlCollegamento().length() != 0 && clima1Model.getNoteSulTipologiaDellImpianto().length() != 0)
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

        if (clima1Model != null)
        {

            String tipoDiEdificioStr = clima1Model.getTipoDiEdificio();
            String posizionamentoUnitaEsternaStr = clima1Model.getPosizionamentoUnitaEsterna();
            String tipologiaCostruttivaMuratureStr = clima1Model.getTipologiaCostruttivaMurature();
            String localiEOPianiDelledificioStr = clima1Model.getLocaliEOPianiDelledificio();

            final int posTipoDiEdificio = tipiDiEdificie.indexOf(tipoDiEdificioStr);
            final int posPosizionamentoUnitaEsterna = posizionamentiUnitaEsterna.indexOf(posizionamentoUnitaEsternaStr);
            final int posTipologiaCostruttivaMurature = tipologieCostruttiveMurature.indexOf(tipologiaCostruttivaMuratureStr);
            final int posLocaliEOPianiDelledificio = localiEOPianiDelledificio.indexOf(localiEOPianiDelledificioStr);

/*                atvTipoDiEdificio.setSelection(posTipoDiEdificio, false);
            atvPosizionamentoUnitaEsterna.setSelection(posPosizionamentoUnitaEsterna, false);
            atvTipologiaCostruttivaMurature.setSelection(posTipologiaCostruttivaMurature, false);
            atvLocaliEOPianiDelledificio.setSelection(posLocaliEOPianiDelledificio, false);*/

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

                //atvPosizionamentoUnitaEsterna.setSelection(posizionamentiUnitaEsterna.indexOf(clima1Model.getPosizionamentoUnitaEsterna()));
                //atvTipologiaCostruttivaMurature.setSelection(tipologieCostruttiveMurature.indexOf(clima1Model.getTipologiaCostruttivaMurature()));
                //atvLocaliEOPianiDelledificio.setSelection(localiEOPianiDelledificio.indexOf(clima1Model.getLocaliEOPianiDelledificio()));

                etNoteSulLuoghoDiInstallazione.setText(clima1Model.getNoteSulLuoghoDiInstallazione());
                etNoteSulTipologiaDellImpianto.setText(clima1Model.getNoteSulTipologiaDellImpianto());
                etNoteRelativeAlCollegamento.setText(clima1Model.getNoteRelativeAlCollegamento());
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

        tipiDiEdificie = new ArrayList<>();
        posizionamentiUnitaEsterna = new ArrayList<>();
        tipologieCostruttiveMurature = new ArrayList<>();
        localiEOPianiDelledificio = new ArrayList<>();

        tipiDiEdificie.addAll(Arrays.asList(TipiDiEdificieStrA));
        posizionamentiUnitaEsterna.addAll(Arrays.asList(PosizionamentiUnitaEsternaStrA));
        tipologieCostruttiveMurature.addAll(Arrays.asList(TipologieCostruttiveMuratureStrA));
        localiEOPianiDelledificio.addAll(Arrays.asList(LocaliEOPianiDelledificioStrA));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView =  inflater.inflate(R.layout.climatizzazione0_report, container, false);

        ArrayAdapter<String> TipiDiEdificieAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, tipiDiEdificie);

        ArrayAdapter<String> PosizionamentiUnitaEsternaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, posizionamentiUnitaEsterna);

        ArrayAdapter<String> TipologieCostruttiveMuratureAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, tipologieCostruttiveMurature);

        ArrayAdapter<String> LocaliEOPianiDelledificioAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, localiEOPianiDelledificio);

        realm.beginTransaction();
        VisitItem visitItem = visitItems.get(selectedIndex);
        VisitStates visitStates = visitItem.getVisitStates();
        idSopralluogo = visitStates.getIdSopralluogo();

        reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        clima1Model = realm.where(Clima1Model.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        RealmResults <Clima1Model> clima1Models = realm.where(Clima1Model.class).findAll();

        if (reportStates != null)
        {
            if (clima1Model == null)
            {
                clima1Model = new Clima1Model(clima1Models.size());
                clima1Model.setIdSopralluogo(idSopralluogo);
                realm.copyToRealmOrUpdate(clima1Model);
            }
        }
        realm.commitTransaction();

        atvTipoDiEdificio = null;
        atvTipoDiEdificio = (Spinner) rootView.findViewById(R.id.atvTipoDiEdificio);

        //atvTipoDiEdificio.setListSelection(tipiDiEdificie.indexOf(clima1Model.getTipoDiEdificio()));

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
                clima1Model.setTipoDiEdificio(selectedItem);
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
        //atvPosizionamentoUnitaEsterna.setText(clima1Model.getPosizionamentoUnitaEsterna());

        //atvPosizionamentoUnitaEsterna.setListSelection(posizionamentiUnitaEsterna.indexOf(clima1Model.getTipoDiEdificio()));

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
                clima1Model.setPosizionamentoUnitaEsterna(selectedItem);
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

        //atvTipologiaCostruttivaMurature.setListSelection(tipologieCostruttiveMurature.indexOf(clima1Model.getTipoDiEdificio()));

        //atvTipologiaCostruttivaMurature.setText(clima1Model.getTipologiaCostruttivaMurature());
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
                clima1Model.setTipologiaCostruttivaMurature(selectedItem);
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
        //atvLocaliEOPianiDelledificio.setText(clima1Model.getLocaliEOPianiDelledificio());

        //atvLocaliEOPianiDelledificio.setListSelection(localiEOPianiDelledificio.indexOf(clima1Model.getLocaliEOPianiDelledificio()));

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
                clima1Model.setLocaliEOPianiDelledificio(selectedItem);
                realm.commitTransaction();
                //Toast.makeText(context, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        etNoteSulLuoghoDiInstallazione = (EditText) rootView.findViewById(R.id.etNoteSulLuoghoDiInstallazione);

        etNoteSulLuoghoDiInstallazione.setText(clima1Model.getNoteSulLuoghoDiInstallazione());

        etNoteSulLuoghoDiInstallazione.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    realm.beginTransaction();
                    clima1Model.setNoteSulLuoghoDiInstallazione(etNoteSulLuoghoDiInstallazione.getText().toString());
                    realm.commitTransaction();
                    //Toast.makeText(context, "onFocusChange etNoteSulLuoghoDiInstallazione", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etNoteSulTipologiaDellImpianto = (EditText) rootView.findViewById(R.id.etNoteSulTipologiaDellImpianto);

        etNoteSulTipologiaDellImpianto.setText(clima1Model.getNoteSulTipologiaDellImpianto());

        etNoteSulTipologiaDellImpianto.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (v == etNoteSulTipologiaDellImpianto && !hasFocus)
                {
                    realm.beginTransaction();
                    clima1Model.setNoteSulTipologiaDellImpianto(etNoteSulTipologiaDellImpianto.getText().toString());
                    realm.commitTransaction();
                    //Toast.makeText(context, "onFocusChange etNoteSulTipologiaDellImpianto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etNoteRelativeAlCollegamento = (EditText) rootView.findViewById(R.id.etNoteRelativeAlCollegamento);
        etNoteRelativeAlCollegamento.setText(clima1Model.getNoteRelativeAlCollegamento());

        etNoteRelativeAlCollegamento.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (v == etNoteRelativeAlCollegamento && !hasFocus)
                {
                    realm.beginTransaction();
                    clima1Model.setNoteRelativeAlCollegamento(etNoteRelativeAlCollegamento.getText().toString());
                    realm.commitTransaction();
                    //Toast.makeText(context, "onFocusChange etNoteRelativeAlCollegamento", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

/*    @Override
    public boolean onTouch(View v, MotionEvent motionEvent)
    {

        if (v.getId() == R.id.etNoteSulLuoghoDiInstallazione)
        {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }

        return false;
    }*/

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
